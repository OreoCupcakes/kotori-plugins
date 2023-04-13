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
import com.theplug.kotori.gwdhelper.utils.WidgetIDPlus;
import com.theplug.kotori.gwdhelper.utils.WidgetInfoPlus;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
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
	public static final WorldArea ARMA_BOSS_ROOM = new WorldArea(2820,5295,24,15,2);
	
	
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
	private boolean isSpellHotkey1Pressed;
	private boolean isSpellHotkey2Pressed;
	private int spellbookSpriteId;

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
		spellbookSpriteId = determineSpellbookActive();
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
		spellbookSpriteId = -1;
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
		autoDeactivatePrayers();
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
			if (!checkSpellExistInActiveSpellbook(config.spellChoice1().getWidgetInfo()) || event.getMenuTarget().equals(" "))
			{
				event.consume();
			}
		}
		else if (entryOption.equals(spell2Option))
		{
			if (!checkSpellExistInActiveSpellbook(config.spellChoice2().getWidgetInfo()) || event.getMenuTarget().equals(" "))
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
				if (ARMA_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
				}
				break;
			default:
				inBossRoom = false;
				break;
		}
	}
	
	private int determineSpellbookActive()
	{
		int spriteId = -1;
		
		Widget fixedMagicIcon = client.getWidget(WidgetInfo.FIXED_VIEWPORT_MAGIC_ICON);
		Widget resizableMagicIcon = client.getWidget(WidgetInfo.RESIZABLE_VIEWPORT_MAGIC_ICON);
		Widget resizableBottomMagicIcon = client.getWidget(WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_MAGIC_ICON);
		
		if (!fixedMagicIcon.isHidden())
		{
			spriteId = fixedMagicIcon.getSpriteId();
		}
		else if (!resizableMagicIcon.isHidden())
		{
			spriteId = resizableMagicIcon.getSpriteId();
		}
		else if (!resizableBottomMagicIcon.isHidden())
		{
			spriteId = resizableBottomMagicIcon.getSpriteId();
		}
		
		return spriteId;
	}
	
	private boolean checkSpellExistInActiveSpellbook(WidgetInfoPlus widgetInfoPlus)
	{
		boolean correctSpellbook = false;
		int spellSpriteType = 0;
		
		if (widgetInfoPlus.getGroupId() != WidgetIDPlus.SPELLBOOK_GROUP_ID)
		{
			return false;
		}
		
		int spellChildId = widgetInfoPlus.getChildId();
		
		if (spellChildId < 76 && spellChildId > 5)
		{
			spellSpriteType = 780;
		}
		else if (spellChildId < 101)
		{
			spellSpriteType = 1583;
		}
		else if (spellChildId < 145)
		{
			spellSpriteType = 1584;
		}
		else if (spellChildId < 190)
		{
			spellSpriteType = 1711;
		}
		
		if (spellbookSpriteId == spellSpriteType)
		{
			correctSpellbook = true;
		}
		return correctSpellbook;
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
	
	private void chooseDefensiveConfigStyle(GodWarsHelperConfig.PrayerSwitchChoice style, int bossPriority, int magePriority, int rangedPriority, int meleePriority, boolean hotkey)
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
						if (bossPriorityConfig >= highestPriorityConfig)
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
		
		kotoriUtils.getInvokesLibrary().invokePrayer(prayerToUse);
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
		kotoriUtils.getSpellsLibrary().setSelectedSpell(spellChoice.getWidgetInfo().getId(), -1, -1);
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
	
	private final HotkeyListener bandosPrayerHotkey = new HotkeyListener(() -> config.bandosAutoPrayerHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
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
}
