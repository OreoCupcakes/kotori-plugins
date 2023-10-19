/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2020, dutta64 <https://github.com/dutta64>
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
package com.theplug.kotori.dagannothhelper;

import com.google.inject.Provides;
import com.theplug.kotori.dagannothhelper.entity.DagannothKing;
import com.theplug.kotori.dagannothhelper.overlay.InfoboxOverlay;
import com.theplug.kotori.dagannothhelper.overlay.PrayerOverlay;
import com.theplug.kotori.dagannothhelper.overlay.SceneOverlay;
import com.theplug.kotori.kotoriutils.KotoriUtils;
import com.theplug.kotori.kotoriutils.methods.InventoryInteractions;
import com.theplug.kotori.kotoriutils.methods.PrayerInteractions;
import com.theplug.kotori.kotoriutils.methods.SpellInteractions;
import com.theplug.kotori.kotoriutils.methods.VarUtilities;
import com.theplug.kotori.kotoriutils.rlapi.Spells;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Singleton
@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "<html><font color=#6b8af6>[K]</font> Dagannoth Helper</html>",
	enabledByDefault = false,
	description = "A plugin for the Dagannoth Kings including overlays and helpers.",
	tags = {"dagannoth", "kings", "dks", "daggs", "ported","kotori"}
)
public class DagannothHelperPlugin extends Plugin
{
	public static final int DAG_REX_ATTACK = 2853;
	public static final int DAG_PRIME_ATTACK = 2854;
	public static final int DAG_SUPREME_ATTACK = 2855;
	public static final int DAG_KING_DEATH = 2856;

	private static final Set<Integer> WATERBITH_REGIONS = Set.of(11588, 11589);

	@Inject
	private Client client;

	@Inject
	private DagannothHelperConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private KeyManager keyManager;

	@Inject
	private PrayerOverlay prayerOverlay;

	@Inject
	private SceneOverlay sceneOverlay;

	@Inject
	private InfoboxOverlay infoboxOverlay;

	@Getter
	private final Set<DagannothKing> dagannothKings = new HashSet<>();

	private boolean atDks;

	@Getter
	private long lastTickTime;

	private boolean prayersDeactivated;
	private int lastAttackStyle = -1;
	private Prayer lastOffensivePrayer = null;
	private Prayer lastSecondaryOffensivePrayer = null;
	private Prayer lastProtectionPrayer = null;
	private boolean finishedEquippingItems;
	private boolean rexDeath;
	private boolean supremeDeath;
	private boolean primeDeath;
	private int[] itemsToEquip = null;
	private Spells spellToLeftClick = null;

	private final HotkeyListener meleeGearHotkey = new HotkeyListener(() -> config.meleeGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			itemsToEquip = InventoryInteractions.parseStringToItemIds(config.meleeGearString());
		}
	};
	private final HotkeyListener rangedGearHotkey = new HotkeyListener(() -> config.rangedGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			itemsToEquip = InventoryInteractions.parseStringToItemIds(config.rangedGearString());
		}
	};
	private final HotkeyListener magicGearHotkey = new HotkeyListener(() -> config.magicGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			itemsToEquip = InventoryInteractions.parseStringToItemIds(config.magicGearString());
		}
	};
	private final HotkeyListener spellHotkey1 = new HotkeyListener(() -> config.spellHotkey1())
	{
		@Override
		public void hotkeyPressed()
		{
			spellToLeftClick = config.spellChoice1().getSpells();
		}

		@Override
		public void hotkeyReleased()
		{
			spellToLeftClick = null;
		}
	};
	private final HotkeyListener spellHotkey2 = new HotkeyListener(() -> config.spellHotkey2())
	{
		@Override
		public void hotkeyPressed()
		{
			spellToLeftClick = config.spellChoice2().getSpells();
		}

		@Override
		public void hotkeyReleased()
		{
			spellToLeftClick = null;
		}
	};



	@Provides
	DagannothHelperConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(DagannothHelperConfig.class);
	}

	@Override
	public void startUp()
	{
		if (client.getGameState() != GameState.LOGGED_IN || !atDks())
		{
			return;
		}

		init();
	}

	private void init()
	{
		atDks = true;

		addOverlays();
		for (final NPC npc : client.getNpcs())
		{
			addNpc(npc);
		}

		initHotkeys();
	}

	@Override
	public void shutDown()
	{
		atDks = false;

		removeOverlays();
		dagannothKings.clear();

		shutDownHotkeys();
		prayersDeactivated = false;
		finishedEquippingItems = false;
		rexDeath = false;
		supremeDeath = false;
		primeDeath = false;
		itemsToEquip = null;
		spellToLeftClick = null;
		lastAttackStyle = -1;
		lastOffensivePrayer = null;
		lastSecondaryOffensivePrayer = null;
		lastProtectionPrayer = null;
	}

	private void addOverlays()
	{
		overlayManager.add(sceneOverlay);
		overlayManager.add(prayerOverlay);
		overlayManager.add(infoboxOverlay);
	}

	private void removeOverlays()
	{
		overlayManager.remove(sceneOverlay);
		overlayManager.remove(prayerOverlay);
		overlayManager.remove(infoboxOverlay);
	}

	private void initHotkeys()
	{
		keyManager.registerKeyListener(meleeGearHotkey);
		keyManager.registerKeyListener(magicGearHotkey);
		keyManager.registerKeyListener(rangedGearHotkey);
		keyManager.registerKeyListener(spellHotkey1);
		keyManager.registerKeyListener(spellHotkey2);
	}

	private void shutDownHotkeys()
	{
		keyManager.unregisterKeyListener(meleeGearHotkey);
		keyManager.unregisterKeyListener(magicGearHotkey);
		keyManager.unregisterKeyListener(rangedGearHotkey);
		keyManager.unregisterKeyListener(spellHotkey1);
		keyManager.unregisterKeyListener(spellHotkey2);
	}


	@Subscribe
	private void onGameStateChanged(final GameStateChanged event)
	{
		final GameState gameState = event.getGameState();

		switch (gameState)
		{
			case LOGGED_IN:
				if (atDks())
				{
					if (!atDks)
					{
						init();
					}
				}
				else
				{
					if (atDks)
					{
						shutDown();
					}
				}
				break;
			case LOGIN_SCREEN:
			case HOPPING:
				shutDown();
				break;
		}
	}

	@Subscribe
	private void onGameTick(final GameTick Event)
	{
		if (!atDks)
		{
			return;
		}
		lastTickTime = System.currentTimeMillis();

		if (dagannothKings.isEmpty())
		{
			deactivatePrayers();
			lastAttackStyle = -1;
			lastOffensivePrayer = null;
			lastSecondaryOffensivePrayer = null;
			lastProtectionPrayer = null;
		}
		else
		{
			for (final DagannothKing dagannothKing : dagannothKings)
			{
				dagannothKing.updateTicksUntilNextAnimation();
			}

			prayProtectionPrayers();
			prayOffensivePrayers();
		}

		prayPreservePrayer();
		handleGearEquips();
	}

	@Subscribe
	private void onClientTick(final ClientTick event)
	{
		if (!atDks || client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer() == null || spellToLeftClick == null)
		{
			return;
		}

		SpellInteractions.createOneClickAttackSpell(spellToLeftClick);
	}

	@Subscribe
	private void onAnimationChanged(final AnimationChanged event)
	{
		if (!atDks)
		{
			return;
		}

		Actor actor = event.getActor();
		int animation = actor.getAnimation();
		// Animation of death is faster than waiting on NpcDespawned event
		if (animation == DAG_KING_DEATH && actor instanceof NPC)
		{
			switch (((NPC) actor).getId())
			{
				case NpcID.DAGANNOTH_REX:
					rexDeath = true;
					break;
				case NpcID.DAGANNOTH_SUPREME:
					supremeDeath = true;
					break;
				case NpcID.DAGANNOTH_PRIME:
					primeDeath = true;
					break;
				default:
					break;
			}
		}
	}

	@Subscribe
	private void onNpcSpawned(final NpcSpawned event)
	{
		if (!atDks)
		{
			return;
		}
		addNpc(event.getNpc());
	}

	@Subscribe
	private void onNpcDespawned(final NpcDespawned event)
	{
		if (!atDks)
		{
			return;
		}

		NPC npc = event.getNpc();

		switch (npc.getId())
		{
			case NpcID.DAGANNOTH_REX:
			case NpcID.DAGANNOTH_SUPREME:
			case NpcID.DAGANNOTH_PRIME:
				dagannothKings.removeIf(dk -> dk.getNpc() == npc);
				break;
			default:
				break;
		}
	}

	private void addNpc(final NPC npc)
	{
		boolean kingSpawned = false;

		switch (npc.getId())
		{
			case NpcID.DAGANNOTH_REX:
				kingSpawned = true;
				rexDeath = false;
				break;
			case NpcID.DAGANNOTH_SUPREME:
				kingSpawned = true;
				supremeDeath = false;
				break;
			case NpcID.DAGANNOTH_PRIME:
				kingSpawned = true;
				primeDeath = false;
				break;
			default:
				break;
		}

		if (kingSpawned)
		{
			dagannothKings.add(new DagannothKing(npc));
			prayersDeactivated = false;
		}
	}

	private boolean atDks()
	{
		return WATERBITH_REGIONS.contains(client.getLocalPlayer().getWorldLocation().getRegionID());
	}

	private void deactivatePrayers()
	{
		if (prayersDeactivated)
		{
			return;
		}

		if (config.autoProtectionPrayers() || config.autoOffensiveMagicPrayer() || config.autoOffensiveMeleePrayer() || config.autoOffensiveRangedPrayer())
		{
			prayersDeactivated = PrayerInteractions.deactivatePrayers(config.autoPreservePrayer());
		}
	}

	private void prayPreservePrayer()
	{
		if (!config.autoPreservePrayer())
		{
			return;
		}

		PrayerInteractions.activatePrayer(Prayer.PRESERVE);
	}

	private void prayProtectionPrayers()
	{
		if (!config.autoProtectionPrayers() || dagannothKings.isEmpty())
		{
			return;
		}

		// If configured, ignore Rex's prayer if it is the only one alive.
		if (config.ignoreRexProtectionPrayer() && dagannothKings.size() == 1)
		{
			if (dagannothKings.stream().anyMatch(dk -> dk.getAttackStyle() == DagannothKing.AttackStyle.MELEE))
			{
				PrayerInteractions.deactivatePrayer(lastProtectionPrayer);
				return;
			}
		}

		Prayer prayerToUse = null;
		int priority = 0;

		Set<DagannothKing.AttackStyle> attacksWithTicksAtZero = new HashSet<>();

		//Get the next attacking DK that is actively targeting you
		for (DagannothKing king : dagannothKings)
		{
			int ticksUntilAttack = king.getTicksUntilNextAnimation();
			DagannothKing.AttackStyle attackStyle = king.getAttackStyle();

			if (ticksUntilAttack == 1 && king.getInteractingActor().equals(client.getLocalPlayer()))
			{
				int attackPriority = attackStyle.getPriority();
				if (attackPriority > priority)
				{
					prayerToUse = attackStyle.getPrayer();
					priority = attackPriority;
				}
			}
			else if (ticksUntilAttack == 0)
			{
				attacksWithTicksAtZero.add(attackStyle);
			}
		}

		//If no DK was about to attack and actively targeting you, then check if there are any spawned DKs (ones with ticks set at 0)
		// and set the highest priority one as the prayer to use
		if (prayerToUse == null && !attacksWithTicksAtZero.isEmpty())
		{
			for (DagannothKing.AttackStyle attack : attacksWithTicksAtZero)
			{
				int attackPriority = attack.getPriority();
				if (attackPriority > priority)
				{
					prayerToUse = attack.getPrayer();
					priority = attackPriority;
				}
			}
		}

		//If still no prayer set, then just don't pray
		if (prayerToUse == null)
		{
			return;
		}

		lastProtectionPrayer = prayerToUse;
		PrayerInteractions.activatePrayer(prayerToUse);
	}

	private void prayOffensivePrayers()
	{
		if (!config.autoOffensiveMeleePrayer() && !config.autoOffensiveRangedPrayer() && !config.autoOffensiveMagicPrayer()
				|| dagannothKings.isEmpty())
		{
			return;
		}

		int currentAttackStyle = VarUtilities.getPlayerAttackStyle();

		// This is to turn on the offensive prayer only once per weapon equip.
		if (lastAttackStyle == currentAttackStyle)
		{
			return;
		}


		lastAttackStyle = currentAttackStyle;

		Prayer prayerToUse = null;
		Prayer secondaryPrayerToUse = null;

		switch (lastAttackStyle)
		{
			case 0:
				if (config.autoOffensiveMeleePrayer())
				{
					prayerToUse = VarUtilities.bestStrengthBoostPrayer();
					if (prayerToUse != Prayer.PIETY && prayerToUse != Prayer.CHIVALRY)
					{
						secondaryPrayerToUse = VarUtilities.bestAttackBoostPrayer();
					}
				}
				break;
			case 1:
				if (config.autoOffensiveRangedPrayer())
				{
					prayerToUse = VarUtilities.bestRangedPrayer();
				}
				break;
			case 2:
				if (config.autoOffensiveMagicPrayer())
				{
					prayerToUse = VarUtilities.bestMagicPrayer();
				}
				break;
		}

		if (prayerToUse != null)
		{
			PrayerInteractions.activatePrayer(prayerToUse);
			lastOffensivePrayer = prayerToUse;

			if (secondaryPrayerToUse != null)
			{
				PrayerInteractions.activatePrayer(secondaryPrayerToUse);
				lastSecondaryOffensivePrayer = secondaryPrayerToUse;
			}
		}
		//This deactivates offensive prayers after weapon switches if a switch is not needed
		else if (lastOffensivePrayer != null)
		{
			PrayerInteractions.deactivatePrayer(lastOffensivePrayer);
			lastOffensivePrayer = null;
			if (lastSecondaryOffensivePrayer != null)
			{
				PrayerInteractions.deactivatePrayer(lastSecondaryOffensivePrayer);
				lastSecondaryOffensivePrayer = null;
			}
		}
	}

	private void handleGearEquips()
	{
		/*
			Check the king death flags and if there isn't an item set to equip already.
			This lets hotkeys override the onDeath gear settings.
			Additionally, if you kill multiple kings at the same thing, it will only choose to equip one gear set, prioritizing supreme -> prime -> rex death.
		 */
		if (supremeDeath && itemsToEquip == null)
		{
			switch (config.onSupremeDeathGear())
			{
				case OFF:
					supremeDeath = false;
					break;
				case MELEE:
					itemsToEquip = InventoryInteractions.parseStringToItemIds(config.meleeGearString());
					break;
				case RANGED:
					itemsToEquip = InventoryInteractions.parseStringToItemIds(config.rangedGearString());
					break;
				case MAGIC:
					itemsToEquip = InventoryInteractions.parseStringToItemIds(config.magicGearString());
					break;
			}
		}
		if (primeDeath && itemsToEquip == null)
		{
			switch (config.onPrimeDeathGear())
			{
				case OFF:
					primeDeath = false;
					break;
				case MELEE:
					itemsToEquip = InventoryInteractions.parseStringToItemIds(config.meleeGearString());
					break;
				case RANGED:
					itemsToEquip = InventoryInteractions.parseStringToItemIds(config.rangedGearString());
					break;
				case MAGIC:
					itemsToEquip = InventoryInteractions.parseStringToItemIds(config.magicGearString());
					break;
			}
		}
		if (rexDeath && itemsToEquip == null)
		{
			switch (config.onRexDeathGear())
			{
				case OFF:
					rexDeath = false;
					return;
				case MELEE:
					itemsToEquip = InventoryInteractions.parseStringToItemIds(config.meleeGearString());
					break;
				case RANGED:
					itemsToEquip = InventoryInteractions.parseStringToItemIds(config.rangedGearString());
					break;
				case MAGIC:
					itemsToEquip = InventoryInteractions.parseStringToItemIds(config.magicGearString());
					break;
			}
		}

		if (itemsToEquip == null)
		{
			return;
		}

		finishedEquippingItems = InventoryInteractions.equipItems(itemsToEquip, 6);

		//Reset all gear equipping variables after a set is finished equipping
		if (finishedEquippingItems)
		{
			finishedEquippingItems = false;
			itemsToEquip = null;
			supremeDeath = false;
			primeDeath = false;
			rexDeath = false;
		}
	}
}
