/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2020, dutta64 <https://github.com/dutta64>
 * Copyright (c) 2018, Damen <https://github.com/basicDamen>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *	list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *	this list of conditions and the following disclaimer in the documentation
 *	and/or other materials provided with the distribution.
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

package com.theplug.kotori.grotesqueguardians;

import com.google.inject.Provides;

import javax.inject.Inject;

import com.theplug.kotori.grotesqueguardians.entity.Guardian;
import com.theplug.kotori.kotoriutils.KotoriUtils;
import com.theplug.kotori.kotoriutils.methods.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.game.npcoverlay.NpcOverlayService;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import com.theplug.kotori.grotesqueguardians.overlay.PrayerOverlay;
import com.theplug.kotori.grotesqueguardians.overlay.SceneOverlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

import java.util.*;
import java.util.function.Function;

@Slf4j
@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "<html><font color=#6b8af6>[P]</font> Grotesque Guardian</html>",
	enabledByDefault = false,
	description = "A plugin for Grotesque Guardians boss.",
	tags = {"grotesque", "guardians", "gargoyles", "kotori"}
)
public class GrotesqueGuardiansPlugin extends Plugin
{
	public static final String DUSK = "Dusk";
	public static final String DAWN = "Dawn";
	public static final String DUSK_ECHO = "Dusk (Echo)";
	public static final String DEF_NOT_DUSK = "Definitely Not Dusk";

	private static final Set<Integer> FALLING_ROCKS = Set.of(1449, 1889, 1890, 1938);
	private static final Set<Integer> LIGHTNING = Set.of(1416, 1424);
	private static final Set<Integer> FLAME_WALLS = Set.of(1434, 3108);
	private static final Set<Integer> STONE_ORBS = Set.of(1446, 160);
	private static final Set<Integer> ENERGY_SPHERES = Set.of(31686, 31687, 31688);

	@Getter
	private final Set<GraphicsObject> fallingRockObjects = new HashSet<>();
	@Getter
	private final Set<GraphicsObject> lightningObjects = new HashSet<>();
	@Getter
	private final Set<GraphicsObject> flameWallObjects = new HashSet<>();
	@Getter
	private final Set<GraphicsObject> stoneOrbOjects = new HashSet<>();
	@Getter
	private final Set<GameObject> energySpheres = new HashSet<>();

	private static final int REGION_ID = 6727;

	private final Function<NPC, HighlightedNpc> npcHighlighter = this::highlightNpc;

	@Inject
	private Client client;

	@Inject
	private GrotesqueGuardiansConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private KeyManager keyManager;

	@Inject
	private SceneOverlay sceneOverlay;

	@Inject
	private PrayerOverlay prayerOverlay;

	@Inject
	private NpcOverlayService npcOverlayService;

	@Getter
	private final Map<Actor, Guardian> guardians = new HashMap<>();

	@Getter
	private boolean onRoof;

	@Getter
	private long lastTickTime;

	@Getter
	@Setter
	private boolean flashOnExplosion;

	private boolean echoPhaseTwo;

	private Prayer lastProtectionPrayer = null;
	private final Set<Prayer> lastOffensivePrayers = new HashSet<>();
	private boolean prayersDeactivated;
	private int lastPlayerAttackStyle = -1;

	private int[] itemsToEquip = null;
	private boolean finishedEquippingItems;
	private Guardian.Variant lastVariantEquipped = null;

	private final HotkeyListener duskGearHotkey = new HotkeyListener(() -> config.duskGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			itemsToEquip = InventoryInteractions.parseStringToItemIds(config.duskGearString());
		}
	};

	private final HotkeyListener dawnGearHotkey = new HotkeyListener(() -> config.dawnGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			itemsToEquip = InventoryInteractions.parseStringToItemIds(config.dawnGearString());
		}
	};


	@Provides
	GrotesqueGuardiansConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(GrotesqueGuardiansConfig.class);
	}

	@Override
	protected void startUp()
	{
		if (client.getGameState() != GameState.LOGGED_IN || !isInRegion())
		{
			return;
		}

		init();
	}

	private void init()
	{
		onRoof = true;

		for (final NPC npc : NPCInteractions.getNpcs())
		{
			addNpc(npc);
		}

		overlayManager.add(sceneOverlay);
		overlayManager.add(prayerOverlay);
		npcOverlayService.registerHighlighter(npcHighlighter);

		keyManager.registerKeyListener(duskGearHotkey);
		keyManager.registerKeyListener(dawnGearHotkey);
	}

	@Override
	protected void shutDown()
	{
		onRoof = false;
		flashOnExplosion = false;
		echoPhaseTwo = false;

		guardians.clear();

		lastProtectionPrayer = null;
		lastOffensivePrayers.clear();
		prayersDeactivated = false;
		lastPlayerAttackStyle = -1;

		itemsToEquip = null;
		finishedEquippingItems = false;
		lastVariantEquipped = null;

		overlayManager.remove(sceneOverlay);
		overlayManager.remove(prayerOverlay);
		npcOverlayService.unregisterHighlighter(npcHighlighter);

		keyManager.unregisterKeyListener(duskGearHotkey);
		keyManager.unregisterKeyListener(dawnGearHotkey);
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged configChanged)
	{
		if (!onRoof || !configChanged.getGroup().equals(GrotesqueGuardiansConfig.GROUP))
		{
			return;
		}

		npcOverlayService.rebuild();
	}

	@Subscribe
	private void onGameStateChanged(final GameStateChanged event)
	{
		final GameState gameState = event.getGameState();

		switch (gameState)
		{
			case LOGGED_IN:
				if (isInRegion())
				{
					if (!onRoof)
					{
						init();
					}
				}
				else
				{
					if (onRoof)
					{
						shutDown();
					}
				}
				break;
			case HOPPING:
			case LOGIN_SCREEN:
				if (onRoof)
				{
					shutDown();
				}
				break;
			default:
				break;
		}
	}

	@Subscribe
	private void onGameTick(final GameTick event)
	{
		if (!onRoof)
		{
			return;
		}

		lastTickTime = System.currentTimeMillis();

		clearExpiredGraphicObjectSets();

		if (guardians.isEmpty())
		{
			handleDeactivatePrayers();
			return;
		}

		for (Guardian garg : guardians.values())
		{
			garg.setEchoVariantPhased(echoPhaseTwo);
			garg.removeExpiredProjectile();
			garg.updateTicksUntilNextAttack();
		}

		handleProtectionPrayers();
		handleOffensivePrayers();
		prayPreservePrayer();

		handleGearEquips();
	}

	@Subscribe
	private void onNpcSpawned(final NpcSpawned event)
	{
		if (!onRoof)
		{
			return;
		}

		addNpc(event.getNpc());
	}

	@Subscribe
	private void onNpcDespawned(final NpcDespawned event)
	{
		if (!onRoof)
		{
			return;
		}

		removeNpc(event.getNpc());
	}

	@Subscribe
	private void onAnimationChanged(final AnimationChanged event)
	{
		if (!onRoof || guardians.isEmpty())
		{
			return;
		}

		final Actor actor = event.getActor();
		final int animation = actor.getAnimation();

		Guardian garg = guardians.get(actor);

		if (garg == null || !garg.getNpcName().contains(DUSK))
		{
			return;
		}

		switch (animation)
		{
			case Guardian.DUSK_PHASE_2_ECLIPSE_EXPLOSION:
				if (!config.killingEchoVariant())
				{
					flashOnExplosion = true;
				}
				break;
			case Guardian.ECHO_DUSK_PHASE_2_TRANSITION:
				if (config.killingEchoVariant())
				{
					echoPhaseTwo = true;
				}
				break;
		}
	}

	@Subscribe
	private void onProjectileMoved(final ProjectileMoved event)
	{
		if (!onRoof || guardians.isEmpty())
		{
			return;
		}

		final Projectile projectile = event.getProjectile();

		for (Guardian garg : guardians.values())
		{
			if (garg.getLastAttackProjectile() == null)
			{
				if (projectile.getRemainingCycles() >= 15)
				{
					garg.updateLastAttackProjectile(projectile);
				}
			}
		}
	}

	@Subscribe
	private void onGraphicsObjectCreated(final GraphicsObjectCreated event)
	{
		if (!onRoof)
		{
			return;
		}

		final GraphicsObject graphicsObject = event.getGraphicsObject();
		final int graphicId = graphicsObject.getId();

		if (FALLING_ROCKS.contains(graphicId))
		{
			fallingRockObjects.add(graphicsObject);
		}
		else if (LIGHTNING.contains(graphicId))
		{
			lightningObjects.add(graphicsObject);
		}
		else if (FLAME_WALLS.contains(graphicId))
		{
			flameWallObjects.add(graphicsObject);
		}
		else if (STONE_ORBS.contains(graphicId))
		{
			stoneOrbOjects.add(graphicsObject);
		}
	}

	@Subscribe
	private void onGameObjectSpawned(final GameObjectSpawned event)
	{
		if (!onRoof)
		{
			return;
		}

		final GameObject gameObject = event.getGameObject();

		if (ENERGY_SPHERES.contains(gameObject.getId()))
		{
			energySpheres.add(gameObject);
		}
	}

	@Subscribe
	private void onGameObjectDespawned(final GameObjectDespawned event)
	{
		if (!onRoof)
		{
			return;
		}

		final GameObject gameObject = event.getGameObject();

		if (ENERGY_SPHERES.contains(gameObject.getId()))
		{
			energySpheres.remove(gameObject);
		}
	}

	private void addNpc(final NPC npc)
	{
		final String name = npc.getName();

		if (name == null)
		{
			return;
		}

		switch (name)
		{
			case DUSK:
			case DUSK_ECHO:
			case DAWN:
			case DEF_NOT_DUSK:
				guardians.put(npc, new Guardian(npc, config.killingEchoVariant()));
				break;
		}
	}

	private void removeNpc(final NPC npc)
	{
		final String name = npc.getName();

		if (name == null)
		{
			return;
		}

		switch (name)
		{
			case DUSK:
			case DUSK_ECHO:
				echoPhaseTwo = false;
				guardians.remove(npc);
				break;
			case DAWN:
			case DEF_NOT_DUSK:
				guardians.remove(npc);
				break;
		}
	}

	private void clearExpiredGraphicObjectSets()
	{
		if (!fallingRockObjects.isEmpty())
		{
			fallingRockObjects.removeIf(GraphicsObject::finished);
		}
		if (!lightningObjects.isEmpty())
		{
			lightningObjects.removeIf(GraphicsObject::finished);
		}
		if (!flameWallObjects.isEmpty())
		{
			flameWallObjects.removeIf(GraphicsObject::finished);
		}
		if (!stoneOrbOjects.isEmpty())
		{
			stoneOrbOjects.removeIf(GraphicsObject::finished);
		}
	}

	private boolean isInRegion()
	{
		return REGION_ID == MiscUtilities.getPlayerRegionID();
	}

	private int getNumberImmuneGuardians()
	{
		int immuneGargs = 0;

		for (Guardian garg : guardians.values())
		{
			if (garg.getVariant() == null)
			{
				immuneGargs++;
			}
		}

		return immuneGargs;
	}

	private void prayPreservePrayer()
	{
		if (!config.keepPreservePrayerOn())
		{
			return;
		}

		PrayerInteractions.activatePrayer(Prayer.PRESERVE);
	}

	private void handleProtectionPrayers()
	{
		if (!config.autoProtectionPrayers() || guardians.isEmpty())
		{
			return;
		}

		Prayer prayerToUse = null;
		int priority = 0;
		Set<Guardian.AttackStyle> attacksAtZeroTicks = new HashSet<>();


		for (Guardian garg : guardians.values())
		{
			int ticksUntilAttack = garg.getTicksUntilNextAttack();
			Guardian.AttackStyle attackStyle = garg.getAttackStyle();

			if (ticksUntilAttack == 1)
			{
				int attackPriority = attackStyle.getPriority();
				if (attackPriority > priority)
				{
					prayerToUse = attackStyle.getPrayer();
					priority = attackPriority;
				}
			}
			else if (ticksUntilAttack <= 0)
			{
				attacksAtZeroTicks.add(attackStyle);
			}
		}

		if (prayerToUse == null && !attacksAtZeroTicks.isEmpty() && getNumberImmuneGuardians() != guardians.size())
		{
			for (Guardian.AttackStyle attack : attacksAtZeroTicks)
			{
				int attackPriority = attack.getPriority();
				if (attackPriority > priority)
				{
					prayerToUse = attack.getPrayer();
					priority = attackPriority;
				}
			}
		}

		if (prayerToUse == null)
		{
			if (config.lazyFlickPrayers())
			{
				PrayerInteractions.deactivatePrayer(lastProtectionPrayer);
			}
			return;
		}

		PrayerInteractions.activatePrayer(prayerToUse);
		lastProtectionPrayer = prayerToUse;
		prayersDeactivated = false;
	}

	private void handleOffensivePrayers()
	{
		if (!config.autoOffensivePrayers() || guardians.isEmpty())
		{
			return;
		}

		if (getNumberImmuneGuardians() == guardians.size())
		{
			if (lastOffensivePrayers.isEmpty() || !config.lazyFlickPrayers())
			{
				return;
			}

			for (Prayer prayer : lastOffensivePrayers)
			{
				PrayerInteractions.deactivatePrayer(prayer);
			}

			lastPlayerAttackStyle = -1;
			lastOffensivePrayers.clear();
			return;
		}

		int curPlayerAttackStyle = VarUtilities.getPlayerAttackStyle();

		if (lastPlayerAttackStyle == curPlayerAttackStyle)
		{
			return;
		}

		lastOffensivePrayers.addAll(VarUtilities.bestOffensivePrayers(true, false));

		if (!lastOffensivePrayers.isEmpty())
		{
			for (Prayer prayer : lastOffensivePrayers)
			{
				PrayerInteractions.activatePrayer(prayer);
			}
			lastPlayerAttackStyle = curPlayerAttackStyle;
			prayersDeactivated = false;
		}
	}

	private void handleDeactivatePrayers()
	{
		if (prayersDeactivated || (!config.autoProtectionPrayers() && !config.autoOffensivePrayers()))
		{
			lastProtectionPrayer = null;
			lastOffensivePrayers.clear();
			lastPlayerAttackStyle = -1;
			return;
		}

		prayersDeactivated = PrayerInteractions.deactivatePrayers(config.keepPreservePrayerOn(), config.actionsPerTick());
	}

	private void handleGearEquips()
	{
		//Logic for auto gear equip, based on Guardian variant phases
		if (config.autoEquipGear())
		{
			checkVariant:
			for (Guardian garg : guardians.values())
			{
				Guardian.Variant variant = garg.getVariant();

				if (lastVariantEquipped == variant)
				{
					break;
				}

				switch (variant)
				{
					case DAWN_PHASE_1:
					case DAWN_PHASE_3:
					case DUSK_DESPAWN:
						itemsToEquip = InventoryInteractions.parseStringToItemIds(config.dawnGearString());
						lastVariantEquipped = variant;
						break checkVariant;
					case DUSK_PHASE_2:
					case DUSK_PHASE_4:
					case DAWN_DESPAWN:
						itemsToEquip = InventoryInteractions.parseStringToItemIds(config.duskGearString());
						lastVariantEquipped = variant;
						break checkVariant;
				}
			}
		}

		if (itemsToEquip == null)
		{
			return;
		}

		finishedEquippingItems = InventoryInteractions.equipItems(itemsToEquip, config.actionsPerTick());

		if (finishedEquippingItems)
		{
			finishedEquippingItems = false;
			itemsToEquip = null;
		}
	}




	private HighlightedNpc highlightNpc(final NPC npc)
	{
		final int id = npc.getId();

		switch (id)
		{
			case NpcID.DUSK:
			case NpcID.DUSK_7851:
			case NpcID.DUSK_7854:
			case NpcID.DUSK_7882:
			case NpcID.DUSK_7883:
			case NpcID.DUSK_7888:
			case NpcID.DUSK_7855:
			case NpcID.DUSK_7886:
			case NpcID.DUSK_7889:
			case NpcID.DUSK_7887:
			case NpcID.DAWN:
			case NpcID.DAWN_7852:
			case NpcID.DAWN_7884:
			case NpcID.DAWN_7853:
			case NpcID.DAWN_7885:
				return HighlightedNpc.builder()
						.npc(npc)
						.trueTile(true)
						.swTrueTile(config.highlightNpcSouthWestTrueTile())
						.outline(false)
						.highlightColor(config.npcBorderColor())
						.fillColor(config.npcFillColor())
						.borderWidth(config.npcBorderWidth())
						.render(n -> config.highlightNpcTrueTile() && !npc.isDead())
						.build();
		}

		return null;
	}
}
