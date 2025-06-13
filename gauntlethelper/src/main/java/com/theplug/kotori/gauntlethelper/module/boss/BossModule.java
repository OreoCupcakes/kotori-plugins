/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2023, rdutta <https://github.com/rdutta>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.theplug.kotori.gauntlethelper.module.boss;

import com.theplug.kotori.gauntlethelper.GauntletHelperConfig;
import com.theplug.kotori.gauntlethelper.module.Module;
import com.theplug.kotori.gauntlethelper.module.overlay.TimerOverlay;

import java.util.*;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import com.theplug.kotori.kotoriutils.methods.*;
import com.theplug.kotori.kotoriutils.rlapi.GraphicIDPlus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.events.*;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.game.npcoverlay.NpcOverlayService;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@Singleton
public final class BossModule implements Module
{
	public static final int ONEHAND_SLASH_AXE_ANIMATION = 395;
	public static final int ONEHAND_CRUSH_PICKAXE_ANIMATION = 400;
	public static final int ONEHAND_CRUSH_AXE_ANIMATION = 401;
	public static final int UNARMED_PUNCH_ANIMATION = 422;
	public static final int UNARMED_KICK_ANIMATION = 423;
	public static final int BOW_ATTACK_ANIMATION = 426;
	public static final int ONEHAND_STAB_HALBERD_ANIMATION = 428;
	public static final int ONEHAND_SLASH_HALBERD_ANIMATION = 440;
	public static final int ONEHAND_SLASH_SWORD_ANIMATION = 390;
	public static final int ONEHAND_STAB_SWORD_ANIMATION = 386;
	public static final int ONEHAND_STAB_DAGGER_ANIMATION = 376;
	public static final int ONEHAND_SLASH_DAGGER_ANIMATION = 377;
	public static final int CRYSTAL_DAGGER_SPECIAL_ANIMATION = 1062;
	public static final int HIGH_LEVEL_MAGIC_ATTACK = 1167;
	public static final int HUNLEFF_TORNADO = 8418;

	private static final Set<Integer> MELEE_ANIM_IDS = Set.of(
			ONEHAND_STAB_SWORD_ANIMATION, ONEHAND_SLASH_SWORD_ANIMATION,
			ONEHAND_SLASH_AXE_ANIMATION, ONEHAND_CRUSH_PICKAXE_ANIMATION,
			ONEHAND_CRUSH_AXE_ANIMATION, UNARMED_PUNCH_ANIMATION,
			UNARMED_KICK_ANIMATION, ONEHAND_STAB_HALBERD_ANIMATION,
			ONEHAND_SLASH_HALBERD_ANIMATION, ONEHAND_SLASH_DAGGER_ANIMATION,
			ONEHAND_STAB_DAGGER_ANIMATION, CRYSTAL_DAGGER_SPECIAL_ANIMATION
	);

	private static final Set<Integer> ATTACK_ANIM_IDS = new HashSet<>();

	static
	{
		ATTACK_ANIM_IDS.addAll(MELEE_ANIM_IDS);
		ATTACK_ANIM_IDS.add(BOW_ATTACK_ANIMATION);
		ATTACK_ANIM_IDS.add(HIGH_LEVEL_MAGIC_ATTACK);
	}

	private static final List<Integer> PROJECTILE_MAGIC_IDS = List.of(
			GraphicIDPlus.HUNLLEF_MAGE_ATTACK, GraphicIDPlus.HUNLLEF_CORRUPTED_MAGE_ATTACK, GraphicIDPlus.HUNLLEF_ECHO_MAGE_ATTACK
	);

	private static final List<Integer> PROJECTILE_RANGE_IDS = List.of(
			GraphicIDPlus.HUNLLEF_RANGE_ATTACK, GraphicIDPlus.HUNLLEF_CORRUPTED_RANGE_ATTACK, GraphicIDPlus.HUNLLEF_ECHO_RANGE_ATTACK
	);

	private static final List<Integer> PROJECTILE_PRAYER_IDS = List.of(
			GraphicIDPlus.HUNLLEF_PRAYER_ATTACK, GraphicIDPlus.HUNLLEF_CORRUPTED_PRAYER_ATTACK
	);

	private static final Set<Integer> PROJECTILE_IDS = new HashSet<>();

	static
	{
		PROJECTILE_IDS.addAll(PROJECTILE_MAGIC_IDS);
		PROJECTILE_IDS.addAll(PROJECTILE_RANGE_IDS);
		PROJECTILE_IDS.addAll(PROJECTILE_PRAYER_IDS);
		PROJECTILE_IDS.add(GraphicIDPlus.HUNLLEF_ECHO_INVERSION_ATTACK);
	}

	private static final List<Integer> TORNADO_IDS = List.of(NpcID.CRYSTAL_HUNLLEF_CRYSTALS, NpcID.CRYSTAL_HUNLLEF_CRYSTALS_HM, NpcID.CRYSTAL_HUNLLEF_CRYSTALS_ECHO_CRYSTALS);
	private static final List<Integer> HUNLLEF_IDS = List.of(
		NpcID.CRYSTAL_HUNLLEF_MELEE,
		NpcID.CRYSTAL_HUNLLEF_RANGED,
		NpcID.CRYSTAL_HUNLLEF_MAGIC,
		NpcID.CRYSTAL_HUNLLEF_DEATH,
		NpcID.CRYSTAL_HUNLLEF_MELEE_HM,
		NpcID.CRYSTAL_HUNLLEF_RANGED_HM,
		NpcID.CRYSTAL_HUNLLEF_MAGIC_HM,
		NpcID.CRYSTAL_HUNLLEF_DEATH_HM
	);

	private final Function<NPC, HighlightedNpc> npcHighlighter = this::highlightNpc;

	@Getter(AccessLevel.PACKAGE)
	private final Set<Tornado> tornadoes = new HashSet<>();

	@Inject
	private EventBus eventBus;
	@Inject
	private Client client;
	@Inject
	private GauntletHelperConfig config;
	@Inject
	private NpcOverlayService npcOverlayService;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private TimerOverlay timerOverlay;
	@Inject
	private BossOverlay bossOverlay;
	@Inject
	private PrayerWidgetOverlay prayerWidgetOverlay;
	@Inject
	private PrayerInfoBoxOverlay prayerInfoBoxOverlay;

	@Setter
	@Getter
	private Hunllef hunllef;

	@Getter
	private Projectile missile;

	private HeadIcon hunllefOverhead;

	@Getter
	@Setter
	private boolean wrongAttackStyle;

	@Getter
	@Setter
	private boolean switchWeapon;

	private boolean checkedWeapons;
	private boolean doFiveOneMethod;
	private boolean justEnteredArena;

	private final HashMap<Integer, CrystalWeapons> weaponsToUse = new HashMap<>();

	private int lastUniquePlayerAttackCount = -1;

	//Leagues Echo Variables
	private boolean inversePrayerAttack = false;

	@Override
	public void start()
	{
		eventBus.register(this);
		npcOverlayService.registerHighlighter(npcHighlighter);
		overlayManager.add(timerOverlay);
		overlayManager.add(bossOverlay);
		overlayManager.add(prayerWidgetOverlay);
		overlayManager.add(prayerInfoBoxOverlay);
		timerOverlay.setHunllefStart();

		createHunllefObject();

		justEnteredArena = true;
	}

	@Override
	public void stop()
	{
		eventBus.unregister(this);
		npcOverlayService.unregisterHighlighter(npcHighlighter);
		overlayManager.remove(timerOverlay);
		overlayManager.remove(bossOverlay);
		overlayManager.remove(prayerWidgetOverlay);
		overlayManager.remove(prayerInfoBoxOverlay);
		timerOverlay.reset();

		hunllef = null;
		missile = null;
		hunllefOverhead = null;
		wrongAttackStyle = false;
		switchWeapon = false;
		tornadoes.clear();

		justEnteredArena = false;
		checkedWeapons = false;
		doFiveOneMethod = false;
		weaponsToUse.clear();
		lastUniquePlayerAttackCount = -1;

		inversePrayerAttack = false;
	}

	@Subscribe
	void onGameTick(final GameTick event)
	{
		if (hunllef == null)
		{
			return;
		}

		hunllef.decrementTicksUntilNextAttack();

		if (!tornadoes.isEmpty())
		{
			tornadoes.forEach(Tornado::updateTimeLeft);
		}

		if (missile != null && missile.getRemainingCycles() <= 0)
		{
			if (missile.getId() == GraphicIDPlus.HUNLLEF_ECHO_INVERSION_ATTACK)
			{
				inversePrayerAttack = false;
			}

			missile = null;
		}

		handlePrayerInteractions();
		handleWeaponSwitching();
	}

	@Subscribe
	void onGameStateChanged(final GameStateChanged event)
	{
		switch (event.getGameState())
		{
			case LOGIN_SCREEN:
			case HOPPING:
				stop();
				break;
		}
	}

	@Subscribe
	void onActorDeath(final ActorDeath event)
	{
		if (event.getActor() == client.getLocalPlayer())
		{
			timerOverlay.onPlayerDeath();
		}
	}

	@Subscribe
	void onNpcSpawned(final NpcSpawned event)
	{
		final NPC npc = event.getNpc();
		final int id = npc.getId();

		/*
		Moved to startup() because this hunllef has already spawned before this module turns on
		if (HUNLLEF_IDS.contains(id))
		{
			hunllef = new Hunllef(npc);
		}
		 */
		if (TORNADO_IDS.contains(id))
		{
			tornadoes.add(new Tornado(npc));
		}
	}

	@Subscribe
	void onNpcDespawned(final NpcDespawned event)
	{
		final NPC npc = event.getNpc();
		final int id = npc.getId();

		if (HUNLLEF_IDS.contains(id))
		{
			hunllef = null;
		}
		else if (TORNADO_IDS.contains(id))
		{
			tornadoes.removeIf(t -> t.getNpc().equals(npc));
		}
	}

	@Subscribe
	void onProjectileMoved(final ProjectileMoved event)
	{
		if (hunllef == null)
		{
			return;
		}

		final Projectile projectile = event.getProjectile();
		final int id = projectile.getId();

		if (!PROJECTILE_IDS.contains(id))
		{
			return;
		}

		if (missile == null)
		{
			/*
				This projectile check is for Echo Hunllef. Echo Hunllef shoots out two projectiles for its normal magic and ranged attack. We only want to capture one of them as they do damage on the same tick.
				1 game cycle = 20ms, 1 game tick (0.6s) = 30 game cycles
			 */
			if (projectile.getRemainingCycles() >= 15)
			{
				missile = projectile;
			}
			else
			{
				return;
			}

			if (GraphicIDPlus.HUNLLEF_ECHO_INVERSION_ATTACK == id)
			{
				inversePrayerAttack = true;
			}
		}
		else
		{
			return;
		}

		hunllef.updateAttackCount();

		if (PROJECTILE_PRAYER_IDS.contains(id) && config.hunllefPrayerAudio())
		{
			client.playSoundEffect(SoundEffectID.MAGIC_SPLASH_BOING);
		}
	}

	@Subscribe
	void onAnimationChanged(final AnimationChanged event)
	{
		final Actor actor = event.getActor();
		final int id = actor.getAnimation();

		if (actor instanceof Player)
		{
			if (!ATTACK_ANIM_IDS.contains(id))
			{
				return;
			}

			final boolean validAttack = isAttackAnimationValid(id);

			if (validAttack)
			{
				wrongAttackStyle = false;

				if (hunllefOverhead == null || hunllefOverhead == HeadIcon.RANGE_MAGE_MELEE)
				{
					hunllef.resetPlayerAttackCount();
					switchWeapon = true;
				}
				else
				{
					hunllef.updatePlayerAttackCount();

					if (hunllef.getPlayerAttackCount() == 1)
					{
						switchWeapon = true;
					}
				}
			}
			else
			{
				wrongAttackStyle = true;
			}
		}
		else if (actor instanceof NPC)
		{
			if (id == HUNLEFF_TORNADO)
			{
				hunllef.updateAttackCount();
			}
		}
	}

	private HighlightedNpc highlightNpc(final NPC npc)
	{
		final int id = npc.getId();

		switch (id)
		{
			case NpcID.CRYSTAL_HUNLLEF_MELEE:
			case NpcID.CRYSTAL_HUNLLEF_RANGED:
			case NpcID.CRYSTAL_HUNLLEF_MAGIC:
			case NpcID.CRYSTAL_HUNLLEF_DEATH:
			case NpcID.CRYSTAL_HUNLLEF_MELEE_HM:
			case NpcID.CRYSTAL_HUNLLEF_RANGED_HM:
			case NpcID.CRYSTAL_HUNLLEF_MAGIC_HM:
			case NpcID.CRYSTAL_HUNLLEF_DEATH_HM:
				return HighlightedNpc.builder()
					.npc(npc)
					.tile(true)
					.borderWidth(config.hunllefTileOutlineWidth())
					.fillColor(config.hunllefFillColor())
					.highlightColor(config.hunllefOutlineColor())
					.render(n -> config.hunllefTileOutline() && !npc.isDead())
					.build();
			default:
				return null;
		}
	}

	private boolean isAttackAnimationValid(final int animationId)
	{
		hunllefOverhead = ReflectionLibrary.getNpcOverheadIcon(hunllef.getNpc());
		if (hunllefOverhead == null)
		{
			return true;
		}

		switch (hunllefOverhead)
		{
			case MELEE:
				if (MELEE_ANIM_IDS.contains(animationId))
				{
					return false;
				}
				break;
			case RANGED:
				if (animationId == BOW_ATTACK_ANIMATION)
				{
					return false;
				}
				break;
			case MAGIC:
				if (animationId == HIGH_LEVEL_MAGIC_ATTACK)
				{
					return false;
				}
				break;
			case RANGE_MAGE_MELEE:
				if (MELEE_ANIM_IDS.contains(animationId) || animationId == BOW_ATTACK_ANIMATION || animationId == HIGH_LEVEL_MAGIC_ATTACK)
				{
					return false;
				}
				break;
		}

		return true;
	}

	private void handlePrayerInteractions()
	{
		if (!config.autoProtectionPrayers() && !config.autoOffensivePrayers())
		{
			return;
		}

		Prayer protection = null;
		Prayer offensive = null;

		if (config.autoProtectionPrayers())
		{
			if (hunllef != null)
			{
				if (!inversePrayerAttack)
				{
					protection = hunllef.getAttackPhase().getPrayer();
				}
			}
		}

		if (config.autoOffensivePrayers())
		{
			switch (VarUtilities.getPlayerAttackStyle())
			{
				case 0:
					offensive = VarUtilities.bestStrengthBoostPrayer();
					break;
				case 1:
					offensive = VarUtilities.bestRangedPrayer();
					break;
				case 2:
					offensive = VarUtilities.bestMagicPrayer();
					break;
			}
		}

		if (!config.autoFlickPrayers())
		{
			if (protection == null)
			{
				PrayerInteractions.deactivatePrayers(false);
			}
			else
			{
				PrayerInteractions.activatePrayer(protection);
				PrayerInteractions.activatePrayer(offensive);
			}
		}
		else
		{
			if (protection == null)
			{
				PrayerInteractions.deactivatePrayers(false);
			}
			else
			{
				PrayerInteractions.oneTickFlickPrayers(protection, offensive);
			}
		}
	}

	private void handleWeaponSwitching()
	{
		if (!config.autoWeaponSwitch() || hunllef == null)
		{
			return;
		}

		checkWeaponsToUse();

		hunllefOverhead = ReflectionLibrary.getNpcOverheadIcon(hunllef.getNpc());

		if (config.killingEchoHunllef() && (hunllefOverhead == HeadIcon.RANGE_MAGE_MELEE || hunllefOverhead == null))
		{
			hunllef.resetPlayerAttackCount();
		}

		int truePlayerAttackCount = hunllef.getPlayerAttackCount();

		if (lastUniquePlayerAttackCount != truePlayerAttackCount)
		{
			lastUniquePlayerAttackCount = truePlayerAttackCount;

			switch (lastUniquePlayerAttackCount)
			{
				case 1:
					if (doFiveOneMethod)
					{
						equipWeapon();
					}
					break;
				case 6:
					if (!justEnteredArena)
					{
						equipWeapon();
					}
					else
					{
						if (determineSwitchNecessary())
						{
							equipWeapon();
						}
						justEnteredArena = false;
					}
					break;
			}
		}
	}

	private void checkWeaponsToUse()
	{
		if (checkedWeapons)
		{
			return;
		}

		int numOfPerfected = 0;

		for (CrystalWeapons weapon : CrystalWeapons.values())
		{
			int id = weapon.getItemId();

			if (!InventoryInteractions.inventoryContains(id) &&
					!InventoryInteractions.yourEquipmentContains(id, EquipmentInventorySlot.WEAPON))
			{
				continue;
			}

			if (weapon.isPerfected())
			{
				numOfPerfected++;
			}

			int weaponType = weapon.getWeaponType();

            CrystalWeapons storedWeapon = weaponsToUse.get(weaponType);

			if (storedWeapon == null)
			{
				weaponsToUse.put(weaponType, weapon);
			}
			else if (storedWeapon.getPriority() < weapon.getPriority())
			{
				weaponsToUse.put(weaponType, weapon);
			}
		}

		doFiveOneMethod = numOfPerfected < 2;
		checkedWeapons = true;
	}

	private boolean determineSwitchNecessary()
	{
		if (hunllef == null)
		{
			return false;
		}

		int overhead = determineStyleToAvoid();
		int currentStyle = CrystalWeapons.getWeaponTypeById(InventoryInteractions.getEquippedItemId(EquipmentInventorySlot.WEAPON));

		switch (overhead)
		{
			case 0:
			case 1:
			case 2:
				return overhead == currentStyle;
			case 3:
				return overhead != currentStyle;
			case 4:
				return true;
			default:
				return false;
		}
	}

	private int determineStyleToAvoid()
	{
		if (hunllefOverhead == null)
		{
			return 4;
		}

		switch (hunllefOverhead)
		{
			case MELEE:
				return 0;
			case RANGED:
				return 1;
			case MAGIC:
				return 2;
			case RANGE_MAGE_MELEE:
				return 3;
			default:
				return -1;
		}
	}

	private void equipWeapon()
	{
		int styleToAvoid = determineStyleToAvoid();
		if (styleToAvoid == -1)
		{
			return;
		}

		CrystalWeapons pickedWeapon = null;

		for (CrystalWeapons weapon : weaponsToUse.values())
		{
			if (InventoryInteractions.yourEquipmentContains(weapon.getItemId(), EquipmentInventorySlot.WEAPON))
			{
				continue;
			}

			if (styleToAvoid == 3)
			{
				if (weapon.getWeaponType() == 3)
				{
					pickedWeapon = weapon;
					break;
				}
			}
			else if (styleToAvoid == 4)
			{
				if (pickedWeapon == null)
				{
					pickedWeapon = weapon;
				}
				else if (weapon.getPriority() < pickedWeapon.getPriority())
				{
					pickedWeapon = weapon;
				}
			}
			else
			{
				if (styleToAvoid != weapon.getWeaponType())
				{
					if (pickedWeapon == null)
					{
						pickedWeapon = weapon;
					}
					else if (weapon.getPriority() > pickedWeapon.getPriority())
					{
						pickedWeapon = weapon;
					}
				}
			}
		}

		if (pickedWeapon != null)
		{
			if (styleToAvoid == 4 && pickedWeapon.getWeaponType() == CrystalWeapons.getWeaponTypeById(InventoryInteractions.getEquippedItemId(EquipmentInventorySlot.WEAPON)))
			{
				return;
			}

			InventoryInteractions.equipItems(pickedWeapon.getItemId());
		}
	}

	private void createHunllefObject()
	{
		for (NPC npc : NPCInteractions.getNpcs())
		{
			if (HUNLLEF_IDS.contains(npc.getId()))
			{
				hunllef = new Hunllef(npc, config.killingEchoHunllef());
				hunllefOverhead = ReflectionLibrary.getNpcOverheadIcon(npc);
				break;
			}
		}
	}
}
