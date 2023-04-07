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
import com.theplug.kotori.gwdhelper.kotoriutils.KotoriUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Keybind;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "God Wars Helper",
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
	public static final int MINION_AUTO1 = 6154;
	public static final int MINION_AUTO2 = 6156;
	public static final int MINION_AUTO3 = 7071;
	public static final int MINION_AUTO4 = 7073;
	public static final int GENERAL_AUTO1 = 7018;
	public static final int GENERAL_AUTO2 = 7020;
	public static final int GENERAL_AUTO3 = 7021;
	public static final int ZAMMY_GENERIC_AUTO = 64;
	public static final int KRIL_AUTO = 6948;
	public static final int KRIL_SPEC = 6950;
	public static final int ZAKL_AUTO = 7077;
	public static final int BALFRUG_AUTO = 4630;
	public static final int ZILYANA_MELEE_AUTO = 6964;
	public static final int ZILYANA_AUTO = 6967;
	public static final int ZILYANA_SPEC = 6970;
	public static final int STARLIGHT_AUTO = 6376;
	public static final int BREE_AUTO = 7026;
	public static final int GROWLER_AUTO = 7037;
	public static final int KREE_RANGED = 6978;
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
	public static final WorldArea ARMADYL_BOSS_ROOM = new WorldArea(2820,5295,24,15,2);
	
	
	//general graardor - attack 7018,7019 - death 7020
	//Sergeant steelwill - attack 7071, death 6156
	//Sergeant strongstack - attack 6154, death - 6156
	//Sergeant grimspike - attack 7073, death? - 6156
	//Kril - attack 6947,6948 - spec 6950? - death 6949
	//Tstanon - attack 64/65 - death 68
	//Zakl - attack 7077 - death 67
	//Balfrug - attack 4630  - death 67
	//commander zilyana - attack 6969, 6967, 6970 - death 6968
	//bree - attack 7026 - death 7028
	//growler - attack 7037,7035 - death 7034
	//starlight - attack 6376,6375 death 6377
	//kree'arra - attack 6980,6978 - death 6979
	//flockleader geerin - attack 6956 - death 6959
	//flight kilisa - attack 6957 - death 6959
	//wingman skree - attack 6955 - death 6959

	@Inject
	private Client client;
	
	@Inject
	private ClientThread clientThread;
	
	@Inject
	private KotoriUtils kotoriUtils;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TimersOverlay timersOverlay;
	
	@Inject
	private KeyManager keyManager;
	
	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private GodWarsHelperConfig config;

	@Getter(AccessLevel.PACKAGE)
	private Set<NPCContainer> npcContainers = new HashSet<>();
	private boolean validRegion;
	private int currentRegion;
	private boolean inBossRoom;
	private boolean bossAlive;
	private boolean meleeMinionAlive;
	private boolean magicMinionAlive;
	private boolean rangedMinionAlive;
	private boolean isBandosPrayerHotkeyOn;
	private boolean isZammyPrayerHotkeyOn;
	private boolean isSaraPrayerHotkeyOn;
	private boolean isArmaPrayerHotkeyOn;

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
		validRegion = false;
		inBossRoom = false;
		bossAlive = false;
		meleeMinionAlive = false;
		magicMinionAlive = false;
		rangedMinionAlive = false;
		isBandosPrayerHotkeyOn = false;
		isZammyPrayerHotkeyOn = false;
		isSaraPrayerHotkeyOn = false;
		isArmaPrayerHotkeyOn = false;
		sendChatMessage("All God Wars Dungeon automatic protection prayers turned off.");
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
		
		Actor npc = event.getActor();
		
		if (npc == null)
		{
			return;
		}
		
		String npcName = npc.getName();
		
		if (npcName == null)
		{
			return;
		}
		
		int npcAnimation = npc.getAnimation();
		
		switch(npcName)
		{
			case "General Graardor":
				if (npcAnimation == GENERAL_GRAARDOR_DEATH_ID)
				{
					bossAlive = false;
				}
				break;
			case "Sergeant Strongstack":
				if (npcAnimation == BANDOS_BODYGUARDS_DEATH_ID)
				{
					meleeMinionAlive = false;
				}
				break;
			case "Sergeant Steelwill":
				if (npcAnimation == BANDOS_BODYGUARDS_DEATH_ID)
				{
					magicMinionAlive = false;
				}
				break;
			case "Sergeant Grimspike":
				if (npcAnimation == BANDOS_BODYGUARDS_DEATH_ID)
				{
					rangedMinionAlive = false;
				}
				break;
			case "K'ril Tsutsaroth":
				if (npcAnimation == KRIL_TSUTSAROTH_DEATH_ID)
				{
					bossAlive = false;
				}
				break;
			case "Balfrug Kreeyath":
				if (npcAnimation == ZAMORAK_BODYGUARDS_DEATH_ID)
				{
					magicMinionAlive = false;
				}
				break;
			case "Tstanon Karlak":
				if (npcAnimation == TSTANON_KARLAK_DEATH_ID)
				{
					meleeMinionAlive = false;
				}
				break;
			case "Zakl'n Gritch":
				if (npcAnimation == ZAMORAK_BODYGUARDS_DEATH_ID)
				{
					rangedMinionAlive = false;
				}
				break;
			case "Commander Zilyana":
				if (npcAnimation == COMMANDER_ZILYANA_DEATH_ID)
				{
					bossAlive = false;
				}
				break;
			case "Starlight":
				if (npcAnimation == STARLIGHT_DEATH_ID)
				{
					meleeMinionAlive = false;
				}
				break;
			case "Growler":
				if (npcAnimation == GROWLER_DEATH_ID)
				{
					magicMinionAlive = false;
				}
				break;
			case "Bree":
				if (npcAnimation == BREE_DEATH_ID)
				{
					rangedMinionAlive = false;
				}
				break;
			case "Kree'arra":
				if (npcAnimation == KREE_ARRA_DEATH_ID)
				{
					bossAlive = false;
				}
				break;
			case "Wingman Skree":
				if (npcAnimation == ARMADYL_BODYGUARDS_DEATH_ID)
				{
					magicMinionAlive = false;
				}
				break;
			case "Flockleader Geerin":
				if (npcAnimation == ARMADYL_BODYGUARDS_DEATH_ID)
				{
					rangedMinionAlive = false;
				}
				break;
			case "Flight Kilisa":
				if (npcAnimation == ARMADYL_BODYGUARDS_DEATH_ID)
				{
					meleeMinionAlive = false;
				}
				break;
			default:
				break;
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
		autoDeactivatePrayers();
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
		currentRegion = client.getLocalPlayer().getWorldLocation().getRegionID();
		
		if (GWD_REGION_IDS.contains(currentRegion))
		{
			return true;
		}
		return false;
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
		
		WorldPoint point = client.getLocalPlayer().getWorldLocation();
		switch (currentRegion)
		{
			case GENERAL_REGION:
				if (BANDOS_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
				}
				break;
			case ZAMMY_REGION:
				if (ZAMMY_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
				}
				break;
			case SARA_REGION:
				if (SARA_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
				}
				break;
			case ARMA_REGION:
				if (ARMADYL_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
				}
				break;
			default:
				inBossRoom = false;
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
		if (!bossAlive && !rangedMinionAlive && !magicMinionAlive && !meleeMinionAlive && inBossRoom)
		{
			Prayer[] prayers = Prayer.values();
			int actionsTaken = 0;
			for (Prayer p : prayers)
			{
				if (actionsTaken > 3)
				{
					return;
				}
				if (client.isPrayerActive(p))
				{
					kotoriUtils.getInvokesLibrary().deactivatePrayer(p);
					actionsTaken++;
				}
			}
		}
	}
	
	private void autoOffensivePrayers()
	{
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
		NPCContainer npcToPrayAgainst = null;
		switch(currentRegion)
		{
			case GENERAL_REGION:
			{
				switch (config.autoBandosDefensePrayers())
				{
					case ONLY_MINIONS:
						if (bossAlive)
						{
							return;
						}
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.generalGraadorPriority(), config.sergeantSteelwillPriority(),
								config.sergeantGrimspikePriority(), config.sergeantStrongstackPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case ALL_KILL_LONG:
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.generalGraadorPriority(), config.sergeantSteelwillPriority(),
								config.sergeantGrimspikePriority(), config.sergeantStrongstackPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case HOTKEY:
						if (!isBandosPrayerHotkeyOn)
						{
							return;
						}
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.generalGraadorPriority(), config.sergeantSteelwillPriority(),
								config.sergeantGrimspikePriority(), config.sergeantStrongstackPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case OFF:
						return;
					default:
						break;
				}
				break;
			}
			case ZAMMY_REGION:
			{
				switch (config.autoZamorakDefensePrayers())
				{
					case ONLY_MINIONS:
						if (bossAlive)
						{
							return;
						}
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.krilTsutsarothPriority(), config.balfrugKreeyathPriority(),
								config.zaklnGritchPriority(), config.tstanonKarlakPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case ALL_KILL_LONG:
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.krilTsutsarothPriority(), config.balfrugKreeyathPriority(),
								config.zaklnGritchPriority(), config.tstanonKarlakPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case HOTKEY:
						if (!isZammyPrayerHotkeyOn)
						{
							return;
						}
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.krilTsutsarothPriority(), config.balfrugKreeyathPriority(),
								config.zaklnGritchPriority(), config.tstanonKarlakPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case OFF:
						return;
					default:
						break;
				}
				break;
			}
			case SARA_REGION:
			{
				switch (config.autoSaradominDefensePrayers())
				{
					case ONLY_MINIONS:
						if (bossAlive)
						{
							return;
						}
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.commanderZilyanaPriority(), config.growlerPriority(),
								config.breePriority(), config.starlightPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case ALL_KILL_LONG:
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.commanderZilyanaPriority(), config.growlerPriority(),
								config.breePriority(), config.starlightPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case HOTKEY:
						if (!isSaraPrayerHotkeyOn)
						{
							return;
						}
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.commanderZilyanaPriority(), config.growlerPriority(),
								config.breePriority(), config.starlightPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case OFF:
						return;
					default:
						break;
				}
				break;
			}
			case ARMA_REGION:
			{
				switch (config.autoArmadylDefensePrayers())
				{
					case ONLY_MINIONS:
						if (bossAlive)
						{
							return;
						}
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.kreearraPriority(), config.wingmanSkreePriority(),
								config.flockleaderGeerinPriority(), config.flightKilisaPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case ALL_KILL_LONG:
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.kreearraPriority(), config.wingmanSkreePriority(),
								config.flockleaderGeerinPriority(), config.flightKilisaPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case HOTKEY:
						if (!isArmaPrayerHotkeyOn)
						{
							return;
						}
						npcToPrayAgainst = identifyNpcToPrayAgainst(config.kreearraPriority(), config.wingmanSkreePriority(),
								config.flockleaderGeerinPriority(), config.flightKilisaPriority());
						identifyAndInvokeProtectionPrayer(npcToPrayAgainst);
						break;
					case OFF:
						return;
					default:
						break;
				}
				break;
			}
			default:
				break;
		}
	}
	
	private NPCContainer identifyNpcToPrayAgainst(int bossPriorityConfig, int magicMinionPriorityConfig, int rangedMinionPriorityConfig, int meleeMinionPriorityConfig)
	{
		NPCContainer npcAboutToAttack = null;
		int highestPriorityConfig = -1;
		
		for (NPCContainer npc : npcContainers)
		{
			if (npc.getTicksUntilAttack() == 1)
			{
				switch (npc.getMonsterType())
				{
					case GENERAL_GRAARDOR:
					case KRIL_TSUTSAROTH:
					case COMMANDER_ZILYANA:
					case KREEARRA:
						if (bossPriorityConfig > highestPriorityConfig)
						{
							highestPriorityConfig = bossPriorityConfig;
							npcAboutToAttack = npc;
						}
						break;
					case SERGEANT_STEELWILL:
					case BALFRUG_KREEYATH:
					case GROWLER:
					case WINGMAN_SKREE:
						if (magicMinionPriorityConfig > highestPriorityConfig)
						{
							if (npc.getNpcInteracting() == client.getLocalPlayer())
							{
								highestPriorityConfig = magicMinionPriorityConfig;
								npcAboutToAttack = npc;
							}
						}
						break;
					case SERGEANT_GRIMSPIKE:
					case ZAKLN_GRITCH:
					case BREE:
					case FLOCKLEADER_GEERIN:
						if (rangedMinionPriorityConfig > highestPriorityConfig)
						{
							if (npc.getNpcInteracting() == client.getLocalPlayer())
							{
								highestPriorityConfig = rangedMinionPriorityConfig;
								npcAboutToAttack = npc;
							}
						}
						break;
					case SERGEANT_STRONGSTACK:
					case TSTANON_KARLAK:
					case STARLIGHT:
					case FLIGHT_KILISA:
						if (meleeMinionPriorityConfig > highestPriorityConfig)
						{
							if (npc.getNpcInteracting() == client.getLocalPlayer())
							{
								highestPriorityConfig = meleeMinionPriorityConfig;
								npcAboutToAttack = npc;
							}
						}
						break;
					default:
						break;
				}
			}
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
			kotoriUtils.getInvokesLibrary().invokePrayer(bossConfig);
		}
		else
		{
			int npcId = -1;
			Actor actor = client.getLocalPlayer().getInteracting();
			if (actor instanceof NPC)
			{
				NPC attackedNpc = (NPC) actor;
				npcId = attackedNpc.getComposition().getId();
			}
			
			switch (npcId)
			{
				case NpcID.SERGEANT_STEELWILL:
				case NpcID.BALFRUG_KREEYATH:
				case NpcID.GROWLER:
				case NpcID.WINGMAN_SKREE:
					if (magicConfig != null && magicMinionAlive)
					{
						kotoriUtils.getInvokesLibrary().invokePrayer(magicConfig);
					}
					break;
				case NpcID.SERGEANT_GRIMSPIKE:
				case NpcID.ZAKLN_GRITCH:
				case NpcID.BREE:
				case NpcID.FLOCKLEADER_GEERIN:
					if (rangedConfig != null && rangedMinionAlive)
					{
						kotoriUtils.getInvokesLibrary().invokePrayer(rangedConfig);
					}
					break;
				case NpcID.SERGEANT_STRONGSTACK:
				case NpcID.TSTANON_KARLAK:
				case NpcID.STARLIGHT:
				case NpcID.FLIGHT_KILISA:
					if (meleeConfig != null && meleeMinionAlive)
					{
						kotoriUtils.getInvokesLibrary().invokePrayer(meleeConfig);
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
				
				break;
			default:
				prayerToUse = npc.getAttackStyle().getPrayer();
				break;
		}
		
		if (prayerToUse == null)
		{
			return;
		}
		
		kotoriUtils.getInvokesLibrary().invokePrayer(prayerToUse);
	}
	
	private final HotkeyListener bandosPrayerHotkey = new HotkeyListener(() -> config.bandosAutoPrayerHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!validRegion)
			{
				return;
			}
			if (!isBandosPrayerHotkeyOn)
			{
				isBandosPrayerHotkeyOn = true;
				sendChatMessage("Bandos God Wars Dungeon automatic protection prayers turned on.");
			}
			else
			{
				isBandosPrayerHotkeyOn = false;
				sendChatMessage("Bandos God Wars Dungeon automatic protection prayers turned off.");
			}
		}
	};
	
	private final HotkeyListener zamorakPrayerHotkey = new HotkeyListener(() -> config.zamorakAutoPrayerHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!validRegion)
			{
				return;
			}
			if (!isZammyPrayerHotkeyOn)
			{
				isZammyPrayerHotkeyOn = true;
				sendChatMessage("Zamorak God Wars Dungeon automatic protection prayers turned on.");
			}
			else
			{
				isZammyPrayerHotkeyOn = false;
				sendChatMessage("Zamorak God Wars Dungeon automatic protection prayers turned off.");
			}
		}
	};
	
	private final HotkeyListener saradominPrayerHotkey = new HotkeyListener(() -> config.saradominAutoPrayerHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!validRegion)
			{
				return;
			}
			if (!isSaraPrayerHotkeyOn)
			{
				isSaraPrayerHotkeyOn = true;
				sendChatMessage("Saradomin God Wars Dungeon automatic protection prayers turned on.");
			}
			else
			{
				isSaraPrayerHotkeyOn = false;
				sendChatMessage("Saradomin God Wars Dungeon automatic protection prayers turned off.");
			}
		}
	};
	
	private final HotkeyListener armadylPrayerHotkey = new HotkeyListener(() -> config.armadylAutoPrayerHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			if (!validRegion)
			{
				return;
			}
			if (!isArmaPrayerHotkeyOn)
			{
				isArmaPrayerHotkeyOn = true;
				sendChatMessage("Armadyl God Wars Dungeon automatic protection prayers turned on.");
			}
			else
			{
				isArmaPrayerHotkeyOn = false;
				sendChatMessage("Armadyl God Wars Dungeon automatic protection prayers turned off.");
			}
		}
	};
	
	private void sendChatMessage(String chatMessage)
	{
		final String message = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage)
				.build();
		
		chatMessageManager.queue(
				QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(message)
						.build());
	}
}
