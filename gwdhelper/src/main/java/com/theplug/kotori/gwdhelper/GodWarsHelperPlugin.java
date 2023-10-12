/*
 * Copyright (c) 2019, Ganom <https://github.com/Ganom>
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
package com.theplug.kotori.gwdhelper;

import com.google.inject.Provides;
import com.theplug.kotori.kotoriutils.KotoriUtils;
import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import com.theplug.kotori.kotoriutils.methods.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "<html><font color=#6b8af6>[K]</font> God Wars Helper</html>",
	enabledByDefault = false,
	description = "Overlay and automated actions for the original God Wars Dungeon bosses.",
	tags = {"pvm", "bossing", "kotori", "ported", "gwd", "sara", "zammy", "arma", "bandos"}
)
public class GodWarsHelperPlugin extends Plugin
{
	public static final int GENERAL_REGION = 11347;
	public static final int ARMA_REGION = 11346;
	public static final int SARA_REGION = 11602;
	public static final int ZAMMY_REGION = 11603;
	public static final Set<Integer> GWD_REGION_IDS = Set.of(GENERAL_REGION,ARMA_REGION,SARA_REGION,ZAMMY_REGION);
	public static final int SERGEANT_STRONGSTACK_AUTO = 6154;
	public static final int SERGEANT_STEELWILL_AUTO = 7071;
	public static final int SERGEANT_GRIMSPIKE_AUTO = 7073;
	public static final int GENERAL_AUTO1 = 7018;
	public static final int GENERAL_AUTO2 = 7019;
	public static final int GENERAL_AUTO3 = 7021;
	public static final int ZAMMY_GENERIC_AUTO_1 = 64;
	public static final int ZAMMY_GENERIC_AUTO_2 = 65;
	public static final int KRIL_AUTO = 6947;
	public static final int KRIL_AUTO_2 = 6948;
	public static final int KRIL_SPEC = 6950;
	public static final int ZAKL_AUTO = 7077;
	public static final int BALFRUG_AUTO = 4630;
	public static final int ZILYANA_MELEE_AUTO = 6964;
	public static final int ZILYANA_AUTO = 6967;
	public static final int ZILYANA_AUTO_2 = 6969;
	public static final int ZILYANA_SPEC = 6970;
	public static final int STARLIGHT_AUTO = 6375;
	public static final int STARLIGHT_AUTO_2 = 6376;
	public static final int BREE_AUTO = 7026;
	public static final int GROWLER_AUTO = 7035;
	public static final int GROWLER_AUTO_2 = 7037;
	public static final int KREE_RANGED = 6978;
	public static final int KREE_RANGED_2 = 6980;
	public static final int SKREE_AUTO = 6955;
	public static final int GEERIN_AUTO = 6956;
	public static final int GEERIN_FLINCH = 6958;
	public static final int KILISA_AUTO = 6957;
	public static final int GENERAL_GRAARDOR_DEATH_ID = 7020;
	public static final int BANDOS_BODYGUARDS_DEATH_ID = 6156;
	public static final int KRIL_TSUTSAROTH_DEATH_ID = 6949;
	public static final int TSTANON_KARLAK_DEATH_ID = 68;
	public static final int ZAMORAK_BODYGUARDS_DEATH_ID = 67;
	public static final int COMMANDER_ZILYANA_DEATH_ID = 6968;
	public static final int BREE_DEATH_ID = 7028;
	public static final int GROWLER_DEATH_ID = 7034;
	public static final int STARLIGHT_DEATH_ID = 6377;
	public static final int KREE_ARRA_DEATH_ID = 6979;
	public static final int ARMADYL_BODYGUARDS_DEATH_ID = 6959;
	public static final WorldArea BANDOS_BOSS_ROOM = new WorldArea(2863,5350,15,24,2);
	public static final WorldArea ZAMMY_BOSS_ROOM = new WorldArea(2916,5317,25,16,2);
	public static final WorldArea SARA_BOSS_ROOM = new WorldArea(2884,5257,25,20,0);
	public static final WorldArea ARMA_BOSS_ROOM = new WorldArea(2820,5295,24,15,2);

	@Inject
	private Client client;
	
	@Inject
	private ClientThread clientThread;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TimersOverlay timersOverlay;
	
	@Inject
	private KeyManager keyManager;

	@Inject
	private GodWarsHelperConfig config;

	@Getter(AccessLevel.PACKAGE)
	private Set<NPCContainer> npcContainers = new HashSet<>();
	private boolean validRegion;
	private int currentRegion;
	private int lastRegion;
	private boolean inBossRoom;
	private boolean bossAlive;
	private boolean meleeMinionAlive;
	private boolean magicMinionAlive;
	private boolean rangedMinionAlive;
	private boolean allPrayersDeactivated;
	private boolean set1EquippedOnce;
	private boolean set2EquippedOnce;
	private boolean set3EquippedOnce;
	private boolean set4EquippedOnce;
	private boolean set5EquippedOnce;
	private boolean isBandosPrayerHotkeyOn;
	private boolean isZammyPrayerHotkeyOn;
	private boolean isSaraPrayerHotkeyOn;
	private boolean isArmaPrayerHotkeyOn;
	private boolean isSpellHotkey1Pressed;
	private boolean isSpellHotkey2Pressed;
	private boolean isGraardorGearHotkeyPressed;
	private boolean isSteelwillGearHotkeyPressed;
	private boolean isGrimspikeGearHotkeyPressed;
	private boolean isStrongstackGearHotkeyPressed;
	private boolean isBandosGearHotkeyPressed;
	private boolean isKrilGearHotkeyPressed;
	private boolean isBalfrugGearHotkeyPressed;
	private boolean isZalknGearHotkeyPressed;
	private boolean isTstanonGearHotkeyPressed;
	private boolean isZamorakGearHotkeyPressed;
	private boolean isZilyanaGearHotkeyPressed;
	private boolean isGrowlerGearHotkeyPressed;
	private boolean isBreeGearHotkeyPressed;
	private boolean isStarlightGearHotkeyPressed;
	private boolean isSaradominGearHotkeyPressed;
	private boolean isKreearraGearHotkeyPressed;
	private boolean isWingmanGearHotkeyPressed;
	private boolean isFlockleaderGearHotkeyPressed;
	private boolean isFlightGearHotkeyPressed;
	private boolean isArmadylGearHotkeyPressed;

	@Getter(AccessLevel.PACKAGE)
	private long lastTickTime;

	@Provides
	GodWarsHelperConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GodWarsHelperConfig.class);
	}

	@Override
	public void startUp()
	{
		if (client.getGameState() != GameState.LOGGED_IN || !regionCheck())
		{
			return;
		}
		init();
	}
	
	private void init()
	{
		npcContainers.clear();
		for (NPC npc : client.getNpcs())
		{
			addNpc(npc);
		}
		validRegion = true;
		overlayManager.add(timersOverlay);
		keyManager.registerKeyListener(bandosPrayerHotkey);
		keyManager.registerKeyListener(zamorakPrayerHotkey);
		keyManager.registerKeyListener(saradominPrayerHotkey);
		keyManager.registerKeyListener(armadylPrayerHotkey);
		keyManager.registerKeyListener(spellHotkey1);
		keyManager.registerKeyListener(spellHotkey2);
	}

	@Override
	public void shutDown()
	{
		npcContainers.clear();
		overlayManager.remove(timersOverlay);
		keyManager.unregisterKeyListener(bandosPrayerHotkey);
		keyManager.unregisterKeyListener(zamorakPrayerHotkey);
		keyManager.unregisterKeyListener(saradominPrayerHotkey);
		keyManager.unregisterKeyListener(armadylPrayerHotkey);
		keyManager.unregisterKeyListener(spellHotkey1);
		keyManager.unregisterKeyListener(spellHotkey2);
		deactivateGearHotkeysByRegion();
		resetPrayerHotkeyBooleans();
		validRegion = false;
		inBossRoom = false;
		bossAlive = false;
		meleeMinionAlive = false;
		magicMinionAlive = false;
		rangedMinionAlive = false;
		set1EquippedOnce = false;
		set2EquippedOnce = false;
		set3EquippedOnce = false;
		set4EquippedOnce = false;
		set5EquippedOnce = false;
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event)
	{
		GameState gameState = event.getGameState();

		switch(gameState)
		{
			case LOGGED_IN:
				if (regionCheck())
				{
					if (!validRegion)
					{
						init();
					}
				}
				else
				{
					if (validRegion)
					{
						shutDown();
					}
				}
				break;
			case LOGIN_SCREEN:
			case HOPPING:
				if (validRegion)
				{
					shutDown();
				}
				break;
			default:
				break;
		}
	}

	@Subscribe
	private void onNpcSpawned(NpcSpawned event)
	{
		if (!validRegion)
		{
			return;
		}

		addNpc(event.getNpc());
	}

	@Subscribe
	private void onNpcDespawned(NpcDespawned event)
	{
		if (!validRegion)
		{
			return;
		}

		removeNpc(event.getNpc());
	}
	
	@Subscribe
	private void onAnimationChanged(AnimationChanged event)
	{
		//Used to check for NPC death animations because its faster than waiting for the NPC to despawn.
		if (!validRegion)
		{
			return;
		}
		
		Actor actor = event.getActor();
		
		if (actor == null)
		{
			return;
		}
		
		if (actor instanceof NPC)
		{
			NPC npc = (NPC) actor;
			int npcAnimation = npc.getAnimation();
			
			switch(npc.getId())
			{
				case NpcID.GENERAL_GRAARDOR:
				case NpcID.KRIL_TSUTSAROTH:
				case NpcID.COMMANDER_ZILYANA:
				case NpcID.KREEARRA:
					if (npcAnimation == GENERAL_GRAARDOR_DEATH_ID || npcAnimation == KRIL_TSUTSAROTH_DEATH_ID || npcAnimation == COMMANDER_ZILYANA_DEATH_ID ||
							npcAnimation == KREE_ARRA_DEATH_ID)
					{
						bossAlive = false;
						set5EquippedOnce = false;
					}
					break;
				case NpcID.SERGEANT_STRONGSTACK:
				case NpcID.TSTANON_KARLAK:
				case NpcID.STARLIGHT:
				case NpcID.FLIGHT_KILISA:
					if (npcAnimation == BANDOS_BODYGUARDS_DEATH_ID || npcAnimation == TSTANON_KARLAK_DEATH_ID || npcAnimation == STARLIGHT_DEATH_ID ||
							npcAnimation == ARMADYL_BODYGUARDS_DEATH_ID)
					{
						meleeMinionAlive = false;
					}
					break;
				case NpcID.SERGEANT_STEELWILL:
				case NpcID.BALFRUG_KREEYATH:
				case NpcID.GROWLER:
				case NpcID.WINGMAN_SKREE:
					if (npcAnimation == BANDOS_BODYGUARDS_DEATH_ID || npcAnimation == ZAMORAK_BODYGUARDS_DEATH_ID || npcAnimation == GROWLER_DEATH_ID ||
							npcAnimation == ARMADYL_BODYGUARDS_DEATH_ID)
					{
						magicMinionAlive = false;
					}
					break;
				case NpcID.SERGEANT_GRIMSPIKE:
				case NpcID.ZAKLN_GRITCH:
				case NpcID.BREE:
				case NpcID.FLOCKLEADER_GEERIN:
					if (npcAnimation == BANDOS_BODYGUARDS_DEATH_ID || npcAnimation == ZAMORAK_BODYGUARDS_DEATH_ID || npcAnimation == BREE_DEATH_ID ||
							npcAnimation == ARMADYL_BODYGUARDS_DEATH_ID)
					{
						rangedMinionAlive = false;
					}
					break;
				default:
					break;
			}
		}
	}

	@Subscribe
	public void onGameTick(GameTick Event)
	{
		if (!validRegion)
		{
			return;
		}
		
		lastTickTime = System.currentTimeMillis();
		handleBosses();
		bossRoomCheck();
		autoDefensivePrayers();
		autoOffensivePrayers();
		autoPreservePrayer();
		autoDeactivatePrayers();
		gearSwitch();
	}
	
	@Subscribe
	public void onClientTick(ClientTick event)
	{
		if (!validRegion || client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer() == null)
		{
			return;
		}
		
		if (isSpellHotkey1Pressed)
		{
			createSpellHotkeyMenuEntry(config.spellChoice1());
		}
		
		if (isSpellHotkey2Pressed)
		{
			createSpellHotkeyMenuEntry(config.spellChoice2());
		}
	}
	
	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!validRegion)
		{
			return;
		}
		String spell1Option = "<col=39ff14>Cast " + config.spellChoice1().getSpellString() + "</col> -> ";
		String spell2Option = "<col=39ff14>Cast " + config.spellChoice2().getSpellString() + "</col> -> ";
		String entryOption = event.getMenuOption();
		
		if (entryOption.equals(spell1Option))
		{
			if (!VarUtilities.isSpellInActiveSpellbook(config.spellChoice1().getWidgetInfo()) || event.getMenuTarget().equals(" "))
			{
				event.consume();
			}
		}
		else if (entryOption.equals(spell2Option))
		{
			if (!VarUtilities.isSpellInActiveSpellbook(config.spellChoice2().getWidgetInfo()) || event.getMenuTarget().equals(" "))
			{
				event.consume();
			}
		}
	}

	private void handleBosses()
	{
		for (NPCContainer npc : getNpcContainers())
		{
			npc.setNpcInteracting(npc.getNpc().getInteracting());

			if (npc.getTicksUntilAttack() >= 0)
			{
				npc.setTicksUntilAttack(npc.getTicksUntilAttack() - 1);
			}

			for (int animation : npc.getAnimations())
			{
				if (animation == npc.getNpc().getAnimation() && npc.getTicksUntilAttack() < 1)
				{
					npc.setTicksUntilAttack(npc.getAttackSpeed());
				}
			}
		}
	}

	private boolean regionCheck()
	{
		lastRegion = currentRegion;
		Player you = client.getLocalPlayer();
		if (you == null)
		{
			return false;
		}
		currentRegion = WorldPoint.fromLocalInstance(client, you.getLocalLocation()).getRegionID();
		
		return GWD_REGION_IDS.contains(currentRegion);
	}
	
	private void bossRoomCheck()
	{
		if (!validRegion)
		{
			if (inBossRoom)
			{
				inBossRoom = false;
			}
			return;
		}
		
		if (inBossRoom)
		{
			return;
		}
		
		WorldPoint point = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation());
		currentRegion = point.getRegionID();
		
		switch (currentRegion)
		{
			case GENERAL_REGION:
				if (BANDOS_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
					activateGearHotkeysByRegion();
				}
				break;
			case ZAMMY_REGION:
				if (ZAMMY_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
					activateGearHotkeysByRegion();
				}
				break;
			case SARA_REGION:
				if (SARA_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
					activateGearHotkeysByRegion();
				}
				break;
			case ARMA_REGION:
				if (ARMA_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
					activateGearHotkeysByRegion();
				}
				break;
			default:
				inBossRoom = false;
				break;
		}
	}
	
	private void resetPrayerHotkeyBooleans()
	{
		if (isBandosPrayerHotkeyOn)
		{
			isBandosPrayerHotkeyOn = false;
			MiscUtilities.sendGameMessage("Bandos God Wars Dungeon automatic protection prayers turned off.");
		}
		
		if (isZammyPrayerHotkeyOn)
		{
			isZammyPrayerHotkeyOn = false;
			MiscUtilities.sendGameMessage("Zamorak God Wars Dungeon automatic protection prayers turned off.");
		}
		if (isSaraPrayerHotkeyOn)
		{
			isSaraPrayerHotkeyOn = false;
			MiscUtilities.sendGameMessage("Saradomin God Wars Dungeon automatic protection prayers turned off.");
		}
		if (isArmaPrayerHotkeyOn)
		{
			isArmaPrayerHotkeyOn = false;
			MiscUtilities.sendGameMessage("Armadyl God Wars Dungeon automatic protection prayers turned off.");
		}
	}
	
	private void activateGearHotkeysByRegion()
	{
		switch (currentRegion)
		{
			case GENERAL_REGION:
				keyManager.registerKeyListener(graardorDeathGearHotkey);
				keyManager.registerKeyListener(steelwillDeathGearHotkey);
				keyManager.registerKeyListener(grimspikeDeathGearHotkey);
				keyManager.registerKeyListener(strongstackDeathGearHotkey);
				keyManager.registerKeyListener(bandosDeathSpawnGearHotkey);
				break;
			case ZAMMY_REGION:
				keyManager.registerKeyListener(krilDeathGearHotkey);
				keyManager.registerKeyListener(balfrugDeathGearHotkey);
				keyManager.registerKeyListener(zaklnDeathGearHotkey);
				keyManager.registerKeyListener(tstanonDeathGearHotkey);
				keyManager.registerKeyListener(zamorakDeathSpawnGearHotkey);
				break;
			case SARA_REGION:
				keyManager.registerKeyListener(zilyanaDeathGearHotkey);
				keyManager.registerKeyListener(growlerDeathGearHotkey);
				keyManager.registerKeyListener(breeDeathGearHotkey);
				keyManager.registerKeyListener(starlightDeathGearHotkey);
				keyManager.registerKeyListener(saradominDeathSpawnGearHotkey);
				break;
			case ARMA_REGION:
				keyManager.registerKeyListener(kreearraDeathGearHotkey);
				keyManager.registerKeyListener(wingmanDeathGearHotkey);
				keyManager.registerKeyListener(flockleaderDeathGearHotkey);
				keyManager.registerKeyListener(flightDeathGearHotkey);
				keyManager.registerKeyListener(armadylDeathSpawnGearHotkey);
				break;
			default:
				break;
		}
	}
	
	private void deactivateGearHotkeysByRegion()
	{
		switch (lastRegion)
		{
			case GENERAL_REGION:
				keyManager.unregisterKeyListener(graardorDeathGearHotkey);
				keyManager.unregisterKeyListener(steelwillDeathGearHotkey);
				keyManager.unregisterKeyListener(grimspikeDeathGearHotkey);
				keyManager.unregisterKeyListener(strongstackDeathGearHotkey);
				keyManager.unregisterKeyListener(bandosDeathSpawnGearHotkey);
				break;
			case ZAMMY_REGION:
				keyManager.unregisterKeyListener(krilDeathGearHotkey);
				keyManager.unregisterKeyListener(balfrugDeathGearHotkey);
				keyManager.unregisterKeyListener(zaklnDeathGearHotkey);
				keyManager.unregisterKeyListener(tstanonDeathGearHotkey);
				keyManager.unregisterKeyListener(zamorakDeathSpawnGearHotkey);
				break;
			case SARA_REGION:
				keyManager.unregisterKeyListener(zilyanaDeathGearHotkey);
				keyManager.unregisterKeyListener(growlerDeathGearHotkey);
				keyManager.unregisterKeyListener(breeDeathGearHotkey);
				keyManager.unregisterKeyListener(starlightDeathGearHotkey);
				keyManager.unregisterKeyListener(saradominDeathSpawnGearHotkey);
				break;
			case ARMA_REGION:
				keyManager.unregisterKeyListener(kreearraDeathGearHotkey);
				keyManager.unregisterKeyListener(wingmanDeathGearHotkey);
				keyManager.unregisterKeyListener(flockleaderDeathGearHotkey);
				keyManager.unregisterKeyListener(flightDeathGearHotkey);
				keyManager.unregisterKeyListener(armadylDeathSpawnGearHotkey);
				break;
			default:
				break;
		}
	}

	private void addNpc(NPC npc)
	{
		if (npc == null)
		{
			return;
		}

		switch (npc.getId())
		{
			case NpcID.GENERAL_GRAARDOR:
			case NpcID.KRIL_TSUTSAROTH:
			case NpcID.COMMANDER_ZILYANA:
			case NpcID.KREEARRA:
				bossAlive = true;
				set1EquippedOnce = false;
				set2EquippedOnce = false;
				set3EquippedOnce = false;
				set4EquippedOnce = false;
				set5EquippedOnce = false;
				allPrayersDeactivated = false;
				npcContainers.add(new NPCContainer(npc));
				break;
			case NpcID.SERGEANT_STEELWILL:
			case NpcID.BALFRUG_KREEYATH:
			case NpcID.GROWLER:
			case NpcID.WINGMAN_SKREE:
				magicMinionAlive = true;
				npcContainers.add(new NPCContainer(npc));
				break;
			case NpcID.SERGEANT_STRONGSTACK:
			case NpcID.TSTANON_KARLAK:
			case NpcID.STARLIGHT:
			case NpcID.FLIGHT_KILISA:
				meleeMinionAlive = true;
				npcContainers.add(new NPCContainer(npc));
				break;
			case NpcID.SERGEANT_GRIMSPIKE:
			case NpcID.ZAKLN_GRITCH:
			case NpcID.BREE:
			case NpcID.FLOCKLEADER_GEERIN:
				rangedMinionAlive = true;
				npcContainers.add(new NPCContainer(npc));
				break;
			default:
				break;
		}
	}

	private void removeNpc(NPC npc)
	{
		if (npc == null)
		{
			return;
		}

		switch (npc.getId())
		{
			case NpcID.GENERAL_GRAARDOR:
			case NpcID.KRIL_TSUTSAROTH:
			case NpcID.COMMANDER_ZILYANA:
			case NpcID.KREEARRA:
			case NpcID.SERGEANT_STRONGSTACK:
			case NpcID.SERGEANT_STEELWILL:
			case NpcID.SERGEANT_GRIMSPIKE:
			case NpcID.TSTANON_KARLAK:
			case NpcID.BALFRUG_KREEYATH:
			case NpcID.ZAKLN_GRITCH:
			case NpcID.STARLIGHT:
			case NpcID.BREE:
			case NpcID.GROWLER:
			case NpcID.FLIGHT_KILISA:
			case NpcID.FLOCKLEADER_GEERIN:
			case NpcID.WINGMAN_SKREE:
				npcContainers.removeIf(c -> c.getNpc() == npc);
				break;
			default:
				break;
		}
	}
	
	private void autoDeactivatePrayers()
	{
		if (!bossAlive && !rangedMinionAlive && !magicMinionAlive && !meleeMinionAlive && inBossRoom && !allPrayersDeactivated)
		{
			allPrayersDeactivated = PrayerInteractions.deactivatePrayers(config.keepPreservePrayerOn());
		}
	}
	
	private void autoPreservePrayer()
	{
		if (!inBossRoom)
		{
			return;
		}
		
		if (config.keepPreservePrayerOn())
		{
			PrayerInteractions.activatePrayer(Prayer.PRESERVE);
		}
	}
	
	private void autoOffensivePrayers()
	{
		if (!inBossRoom)
		{
			return;
		}
		
		switch(currentRegion)
		{
			case GENERAL_REGION:
				identifyAndInvokeOffensivePrayer(config.generalGraardorOffensivePrayer().getPrayer(), config.sergeantSteelwillOffensivePrayer().getPrayer(),
						config.sergeantGrimspikeOffensivePrayer().getPrayer(), config.sergeantStrongstackOffensivePrayer().getPrayer());
				break;
			case ZAMMY_REGION:
				identifyAndInvokeOffensivePrayer(config.krilTsutsarothOffensivePrayer().getPrayer(), config.balfrugKreeyathOffensivePrayer().getPrayer(),
						config.zaklnGritchOffensivePrayer().getPrayer(), config.tstanonKarlakOffensivePrayer().getPrayer());
				break;
			case SARA_REGION:
				identifyAndInvokeOffensivePrayer(config.commanderZilyanaOffensivePrayer().getPrayer(), config.growlerOffensivePrayer().getPrayer(),
						config.breeOffensivePrayer().getPrayer(), config.starlightOffensivePrayer().getPrayer());
				break;
			case ARMA_REGION:
				identifyAndInvokeOffensivePrayer(config.kreearraOffensivePrayer().getPrayer(), config.wingmanSkreeOffensivePrayer().getPrayer(),
						config.flockleaderGeerinOffensivePrayer().getPrayer(), config.flightKilisaOffensivePrayer().getPrayer());
			default:
				break;
		}
	}
	
	private void autoDefensivePrayers()
	{
		if (!inBossRoom)
		{
			return;
		}
		
		switch(currentRegion)
		{
			case GENERAL_REGION:
			{
				chooseDefensiveConfigStyle(config.autoBandosDefensePrayers(), config.generalGraadorPriority(), config.sergeantSteelwillPriority(), config.sergeantGrimspikePriority(),
						config.sergeantStrongstackPriority(), isBandosPrayerHotkeyOn);
				break;
			}
			case ZAMMY_REGION:
			{
				chooseDefensiveConfigStyle(config.autoZamorakDefensePrayers(), config.krilTsutsarothPriority(), config.balfrugKreeyathPriority(), config.zaklnGritchPriority(),
						config.tstanonKarlakPriority(), isZammyPrayerHotkeyOn);
				break;
			}
			case SARA_REGION:
			{
				chooseDefensiveConfigStyle(config.autoSaradominDefensePrayers(), config.commanderZilyanaPriority(), config.growlerPriority(), config.breePriority(),
						config.starlightPriority(), isSaraPrayerHotkeyOn);
				break;
			}
			case ARMA_REGION:
			{
				chooseDefensiveConfigStyle(config.autoArmadylDefensePrayers(), config.kreearraPriority(), config.wingmanSkreePriority(), config.flockleaderGeerinPriority(),
						config.flightKilisaPriority(), isArmaPrayerHotkeyOn);
				break;
			}
			default:
				break;
		}
	}
	
	private void chooseDefensiveConfigStyle(GodWarsHelperConfig.PrayerSwitchChoice style, int bossPriority, int magePriority, int rangedPriority,
											int meleePriority, boolean hotkey)
	{
		NPCContainer npcToPrayAgainst = null;
		switch (style)
		{
			case ONLY_MINIONS:
				if (bossAlive)
				{
					return;
				}
				npcToPrayAgainst = identifyNpcToPrayAgainst(bossPriority, magePriority, rangedPriority, meleePriority);
				identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
				break;
			case ALL_KILL_LONG:
				npcToPrayAgainst = identifyNpcToPrayAgainst(bossPriority, magePriority, rangedPriority, meleePriority);
				identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
				break;
			case HOTKEY:
				if (!hotkey)
				{
					return;
				}
				npcToPrayAgainst = identifyNpcToPrayAgainst(bossPriority, magePriority, rangedPriority, meleePriority);
				identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
				break;
			case OFF:
				return;
			default:
				break;
		}
	}
	
	private NPCContainer identifyNpcToPrayAgainst(int bossPriorityConfig, int magicMinionPriorityConfig, int rangedMinionPriorityConfig, int meleeMinionPriorityConfig)
	{
		NPCContainer npcAboutToAttack = null;
		int highestPriorityConfig = -1;
		NPCContainer bossContainer = null;
		
		for (NPCContainer npc : npcContainers)
		{
			switch (npc.getMonsterType())
			{
				case GENERAL_GRAARDOR:
				case KRIL_TSUTSAROTH:
				case COMMANDER_ZILYANA:
				case KREEARRA:
					bossContainer = npc;
					if (npc.getTicksUntilAttack() == 1)
					{
						if (bossPriorityConfig >= highestPriorityConfig)
						{
							highestPriorityConfig = bossPriorityConfig;
							npcAboutToAttack = npc;
						}
					}
					break;
				case SERGEANT_STEELWILL:
				case BALFRUG_KREEYATH:
				case GROWLER:
				case WINGMAN_SKREE:
					if (npc.getTicksUntilAttack() == 1)
					{
						if (magicMinionPriorityConfig > highestPriorityConfig)
						{
							if (npc.getNpcInteracting() == client.getLocalPlayer())
							{
								highestPriorityConfig = magicMinionPriorityConfig;
								npcAboutToAttack = npc;
							}
						}
					}
					break;
				case SERGEANT_GRIMSPIKE:
				case ZAKLN_GRITCH:
				case BREE:
				case FLOCKLEADER_GEERIN:
					if (npc.getTicksUntilAttack() == 1)
					{
						if (rangedMinionPriorityConfig > highestPriorityConfig)
						{
							if (npc.getNpcInteracting() == client.getLocalPlayer())
							{
								highestPriorityConfig = rangedMinionPriorityConfig;
								npcAboutToAttack = npc;
							}
						}
					}
					break;
				case SERGEANT_STRONGSTACK:
				case TSTANON_KARLAK:
				case STARLIGHT:
				case FLIGHT_KILISA:
					if (npc.getTicksUntilAttack() == 1)
					{
						if (meleeMinionPriorityConfig > highestPriorityConfig)
						{
							if (npc.getNpcInteracting() == client.getLocalPlayer())
							{
								highestPriorityConfig = meleeMinionPriorityConfig;
								npcAboutToAttack = npc;
							}
						}
					}
					break;
				default:
					break;
			}
		}
		
		if (npcAboutToAttack == null)
		{
			npcAboutToAttack = bossContainer;
		}
		
		return npcAboutToAttack;
	}
	
	private void identifyAndInvokeOffensivePrayer(Prayer bossConfig, Prayer magicConfig, Prayer rangedConfig, Prayer meleeConfig)
	{
		if (bossAlive)
		{
			if (bossConfig == null)
			{
				return;
			}
			PrayerInteractions.activatePrayer(bossConfig);
		}
		else
		{
			int npcId = -1;
			Actor actor = client.getLocalPlayer().getInteracting();
			if (actor instanceof NPC)
			{
				NPC attackedNpc = (NPC) actor;
				npcId = attackedNpc.getId();
			}
			
			switch (npcId)
			{
				case NpcID.SERGEANT_STEELWILL:
				case NpcID.BALFRUG_KREEYATH:
				case NpcID.GROWLER:
				case NpcID.WINGMAN_SKREE:
					if (magicConfig != null && magicMinionAlive)
					{
						PrayerInteractions.activatePrayer(magicConfig);
					}
					break;
				case NpcID.SERGEANT_GRIMSPIKE:
				case NpcID.ZAKLN_GRITCH:
				case NpcID.BREE:
				case NpcID.FLOCKLEADER_GEERIN:
					if (rangedConfig != null && rangedMinionAlive)
					{
						PrayerInteractions.activatePrayer(rangedConfig);
					}
					break;
				case NpcID.SERGEANT_STRONGSTACK:
				case NpcID.TSTANON_KARLAK:
				case NpcID.STARLIGHT:
				case NpcID.FLIGHT_KILISA:
					if (meleeConfig != null && meleeMinionAlive)
					{
						PrayerInteractions.activatePrayer(meleeConfig);
					}
					break;
				default:
					break;
			}
		}
	}
	
	private void identifyAndInvokeProtectionPrayer(NPCContainer npc)
	{
		if (npc == null)
		{
			return;
		}
		
		Prayer prayerToUse = null;
		
		switch (npc.getMonsterType())
		{
			case GENERAL_GRAARDOR:
				if (npc.getNpcInteracting() != client.getLocalPlayer())
				{
					prayerToUse = Prayer.PROTECT_FROM_MISSILES;
				}
				else
				{
					prayerToUse = npc.getAttackStyle().getPrayer();
				}
				break;
			case KRIL_TSUTSAROTH:
				prayerToUse = config.krilProtectionPrayerChoice().getPrayer();
				break;
			case COMMANDER_ZILYANA:
				prayerToUse = config.zilyanaProtectionPrayerChoice().getPrayer();
				break;
			default:
				prayerToUse = npc.getAttackStyle().getPrayer();
				break;
		}
		
		if (prayerToUse == null)
		{
			return;
		}
		
		PrayerInteractions.activatePrayer(prayerToUse);
	}
	
	private void gearSwitch()
	{
		switch (currentRegion)
		{
			case GENERAL_REGION:
				parseGearConfigsAndEquip(config.generalGraardorDeathGearBoolean(), config.generalGraardorGearIds(), config.sergeantSteelwillDeathGearBoolean(), config.sergeantSteelwillGearIds(),
						config.sergeantGrimspikeDeathGearBoolean(), config.sergeantGrimspikeGearIds(), config.sergeantStrongstackDeathGearBoolean(), config.sergeantStrongstackGearIds(),
						config.bandosDeathSpawnGearBoolean(), config.bandosDeathSpawnGearIds());
				break;
			case ZAMMY_REGION:
				parseGearConfigsAndEquip(config.krilTsutsarothDeathGearBoolean(), config.krilTsutsarothGearIds(), config.balfrugKreeyathDeathGearBoolean(), config.balfrugKreeyathGearIds(),
						config.zaklnGritchDeathGearBoolean(), config.zaklnGritchGearIds(), config.tstanonKarlakDeathGearBoolean(), config.tstanonKarlakGearIds(),
						config.zamorakDeathSpawnGearBoolean(), config.zamorakDeathSpawnGearIds());
				break;
			case SARA_REGION:
				parseGearConfigsAndEquip(config.commanderZilyanaDeathGearBoolean(), config.commanderZilyanaGearIds(), config.growlerDeathGearBoolean(), config.growlerGearIds(),
						config.breeGearBoolean(), config.breeGearIds(), config.starlightDeathGearBoolean(), config.starlightGearIds(),
						config.saradominDeathSpawnGearBoolean(), config.saradominDeathSpawnGearIds());
				break;
			case ARMA_REGION:
				parseGearConfigsAndEquip(config.kreearraDeathGearBoolean(), config.kreearraGearIds(), config.wingmanSkreeDeathGearBoolean(), config.wingmanSkreeGearIds(),
						config.flockleaderGeerinDeathGearBoolean(), config.flockleaderGeerinGearIds(), config.flightKilisaDeathGearBoolean(), config.flightKilisaGearIds(),
						config.armadylDeathSpawnGearBoolean(), config.armadylDeathSpawnGearIds());
				break;
			default:
				break;
		}
	}
	
	private void parseGearConfigsAndEquip(boolean bossBoolean, String bossGear, boolean magicBoolean, String magicGear, boolean rangedBoolean, String rangedGear,
										  boolean meleeBoolean, String meleeGear, boolean allBoolean, String allGear)
	{
		if (!bossAlive)
		{
			if (bossBoolean && !set1EquippedOnce)
			{
				set1EquippedOnce = InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(bossGear));
				return;
			}
			
			if (!magicMinionAlive && magicBoolean && !set2EquippedOnce)
			{
				set2EquippedOnce = InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(magicGear));
				return;
			}
			
			if (!rangedMinionAlive && rangedBoolean && !set3EquippedOnce)
			{
				set3EquippedOnce = InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(rangedGear));
				return;
			}
			
			if (!meleeMinionAlive && meleeBoolean && !set4EquippedOnce)
			{
				set4EquippedOnce = InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(meleeGear));
				return;
			}
			
			if (!magicMinionAlive && !rangedMinionAlive && !meleeMinionAlive && allBoolean && !set5EquippedOnce)
			{
				set5EquippedOnce = InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(allGear));
			}
		}
		else
		{
			if (allBoolean && !set5EquippedOnce)
			{
				set5EquippedOnce = InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(allGear));
			}
		}
	}
	
	private void createSpellHotkeyMenuEntry(GodWarsHelperConfig.SpellChoice spellChoice)
	{
		if (client.isMenuOpen())
		{
			return;
		}
		
		MenuEntry[] entries = client.getMenuEntries();
		MenuEntry npcEntry = null;
		for (MenuEntry e: entries)
		{
			if (e.getType() == MenuAction.NPC_SECOND_OPTION)
			{
				npcEntry = e;
				break;
			}
		}
		ReflectionLibrary.setSelectedSpell(spellChoice.getWidgetInfo().getId());
		String menuOptionText = "<col=39ff14>Cast " + spellChoice.getSpellString() + "</col> -> ";
		MenuEntry hotkeyEntry = client.createMenuEntry(-1).setForceLeftClick(true).setParam0(0).setParam1(0).setType(MenuAction.WIDGET_TARGET_ON_NPC)
				.setOption(menuOptionText);
		if (npcEntry == null)
		{
			hotkeyEntry.setTarget(" ").setIdentifier(0);
		}
		else
		{
			hotkeyEntry.setTarget(npcEntry.getTarget()).setIdentifier(npcEntry.getIdentifier());
		}
	}
	
	
	// Defensive Prayer Hotkeys
	private final HotkeyListener bandosPrayerHotkey = new HotkeyListener(() -> config.bandosAutoPrayerHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isBandosPrayerHotkeyOn)
			{
				isBandosPrayerHotkeyOn = true;
				MiscUtilities.sendGameMessage("Bandos God Wars Dungeon automatic protection prayers turned on.");
			}
			else
			{
				isBandosPrayerHotkeyOn = false;
				MiscUtilities.sendGameMessage("Bandos God Wars Dungeon automatic protection prayers turned off.");
			}
		}
	};
	
	private final HotkeyListener zamorakPrayerHotkey = new HotkeyListener(() -> config.zamorakAutoPrayerHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isZammyPrayerHotkeyOn)
			{
				isZammyPrayerHotkeyOn = true;
				MiscUtilities.sendGameMessage("Zamorak God Wars Dungeon automatic protection prayers turned on.");
			}
			else
			{
				isZammyPrayerHotkeyOn = false;
				MiscUtilities.sendGameMessage("Zamorak God Wars Dungeon automatic protection prayers turned off.");
			}
		}
	};
	
	private final HotkeyListener saradominPrayerHotkey = new HotkeyListener(() -> config.saradominAutoPrayerHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isSaraPrayerHotkeyOn)
			{
				isSaraPrayerHotkeyOn = true;
				MiscUtilities.sendGameMessage("Saradomin God Wars Dungeon automatic protection prayers turned on.");
			}
			else
			{
				isSaraPrayerHotkeyOn = false;
				MiscUtilities.sendGameMessage("Saradomin God Wars Dungeon automatic protection prayers turned off.");
			}
		}
	};
	
	private final HotkeyListener armadylPrayerHotkey = new HotkeyListener(() -> config.armadylAutoPrayerHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isArmaPrayerHotkeyOn)
			{
				isArmaPrayerHotkeyOn = true;
				MiscUtilities.sendGameMessage("Armadyl God Wars Dungeon automatic protection prayers turned on.");
			}
			else
			{
				isArmaPrayerHotkeyOn = false;
				MiscUtilities.sendGameMessage("Armadyl God Wars Dungeon automatic protection prayers turned off.");
			}
		}
	};
	
	
	// Spell Hotkeys
	private final HotkeyListener spellHotkey1 = new HotkeyListener(() -> config.spellHotkey1())
	{
		@Override
		public void hotkeyPressed()
		{
			isSpellHotkey1Pressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isSpellHotkey1Pressed = false;
		}
	};
	
	private final HotkeyListener spellHotkey2 = new HotkeyListener(() -> config.spellHotkey2())
	{
		@Override
		public void hotkeyPressed()
		{
			isSpellHotkey2Pressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isSpellHotkey2Pressed = false;
		}
	};
	
	
	// Gear Switching Hotkeys
	private final HotkeyListener graardorDeathGearHotkey = new HotkeyListener(() -> config.generalGraardorDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isGraardorGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.generalGraardorGearIds()));
			}
			isGraardorGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isGraardorGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener steelwillDeathGearHotkey = new HotkeyListener(() -> config.sergeantSteelwillDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isSteelwillGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.sergeantSteelwillGearIds()));
			}
			isSteelwillGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isSteelwillGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener grimspikeDeathGearHotkey = new HotkeyListener(() -> config.sergeantGrimspikeGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isGrimspikeGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.sergeantGrimspikeGearIds()));
			}
			isGrimspikeGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isSteelwillGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener strongstackDeathGearHotkey = new HotkeyListener(() -> config.sergeantStrongstackGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isStrongstackGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.sergeantStrongstackGearIds()));
			}
			isStrongstackGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isStrongstackGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener bandosDeathSpawnGearHotkey = new HotkeyListener(() -> config.bandosDeathSpawnGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isBandosGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.bandosDeathSpawnGearIds()));
			}
			isBandosGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isBandosGearHotkeyPressed = true;
		}
	};
	
	private final HotkeyListener krilDeathGearHotkey = new HotkeyListener(() -> config.krilTsutsarothDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isKrilGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.krilTsutsarothGearIds()));
			}
			isKrilGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isKrilGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener balfrugDeathGearHotkey = new HotkeyListener(() -> config.balfrugKreeyathDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isBalfrugGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.balfrugKreeyathGearIds()));
			}
			isBalfrugGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isBalfrugGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener zaklnDeathGearHotkey = new HotkeyListener(() -> config.zaklnGritchDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isZalknGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.zaklnGritchGearIds()));
			}
			isZalknGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isZalknGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener tstanonDeathGearHotkey = new HotkeyListener(() -> config.tstanonKarlakDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isTstanonGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.tstanonKarlakGearIds()));
			}
			isTstanonGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isTstanonGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener zamorakDeathSpawnGearHotkey = new HotkeyListener(() -> config.zamorakDeathSpawnGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isZamorakGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.zamorakDeathSpawnGearIds()));
			}
			isZamorakGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isZamorakGearHotkeyPressed = true;
		}
	};
	
	private final HotkeyListener zilyanaDeathGearHotkey = new HotkeyListener(() -> config.commanderZilyanaDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isZilyanaGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.commanderZilyanaGearIds()));
			}
			isZilyanaGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isZilyanaGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener growlerDeathGearHotkey = new HotkeyListener(() -> config.growlerDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isGrowlerGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.growlerGearIds()));
			}
			isGrowlerGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isGrowlerGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener breeDeathGearHotkey = new HotkeyListener(() -> config.breeDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isBreeGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.breeGearIds()));
			}
			isBreeGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isBreeGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener starlightDeathGearHotkey = new HotkeyListener(() -> config.starlightDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isStarlightGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.starlightGearIds()));
			}
			isStarlightGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isStarlightGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener saradominDeathSpawnGearHotkey = new HotkeyListener(() -> config.saradominDeathSpawnGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isSaradominGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.saradominDeathSpawnGearIds()));
			}
			isSaradominGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isSaradominGearHotkeyPressed = true;
		}
	};
	
	private final HotkeyListener kreearraDeathGearHotkey = new HotkeyListener(() -> config.kreearraDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isKreearraGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.kreearraGearIds()));
			}
			isKreearraGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isKreearraGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener wingmanDeathGearHotkey = new HotkeyListener(() -> config.wingmanSkreeDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isWingmanGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.wingmanSkreeGearIds()));
			}
			isWingmanGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isWingmanGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener flockleaderDeathGearHotkey = new HotkeyListener(() -> config.flockleaderGeerinDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isFlockleaderGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.flockleaderGeerinGearIds()));
			}
			isFlockleaderGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isFlockleaderGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener flightDeathGearHotkey = new HotkeyListener(() -> config.flightKilisaDeathGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isFlightGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.flightKilisaGearIds()));
			}
			isFlightGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isFlightGearHotkeyPressed = false;
		}
	};
	
	private final HotkeyListener armadylDeathSpawnGearHotkey = new HotkeyListener(() -> config.armadylDeathSpawnGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!isArmadylGearHotkeyPressed)
			{
				InventoryInteractions.equipItems(InventoryInteractions.parseStringToItemIds(config.armadylDeathSpawnGearIds()));
			}
			isArmadylGearHotkeyPressed = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			isArmadylGearHotkeyPressed = true;
		}
	};
}
