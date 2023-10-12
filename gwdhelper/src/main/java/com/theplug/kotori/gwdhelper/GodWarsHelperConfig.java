/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
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

import com.theplug.kotori.kotoriutils.rlapi.WidgetInfoPlus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Prayer;
import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("TickTimers")
public interface GodWarsHelperConfig extends Config
{
	@ConfigSection(
		position = 0,
		name = "Overlay Settings",
		description = "",
		closedByDefault = true
	)
	String overlaySettings = "Overlay Settings";
	
	@ConfigItem(
			position = 0,
			keyName = "showTickTimers",
			name = "Show Tick Timers",
			description = "Show the tick timers of the NPCs' next attack.",
			section = overlaySettings
	)
	default boolean showTickTimers() { return true; }

	@ConfigItem(
		position = 1,
		keyName = "prayerWidgetHelper",
		name = "Prayer Widget Helper",
		description = "Shows you which prayer to click and the time until click.",
		section = overlaySettings
	)
	default boolean showPrayerWidgetHelper()
	{
		return false;
	}

	@ConfigItem(
		position = 2,
		keyName = "showHitSquares",
		name = "Show Hit Squares",
		description = "Shows you where the melee bosses can hit you from.",
		section = overlaySettings
	)
	default boolean showHitSquares()
	{
		return false;
	}

	@ConfigItem(
		position = 3,
		keyName = "changeTickColor",
		name = "Change Tick Color",
		description = "If this is enabled, it will change the tick color to white" +
			"<br> at 1 tick remaining, signaling you to swap.",
		section = overlaySettings
	)
	default boolean changeTickColor()
	{
		return false;
	}

	@ConfigItem(
		position = 4,
		keyName = "ignoreNonAttacking",
		name = "Ignore Non-Attacking",
		description = "Ignore monsters that are not attacking you",
		section = overlaySettings
	)
	default boolean ignoreNonAttacking()
	{
		return false;
	}

	@ConfigItem(
		position = 5,
		keyName = "guitarHeroMode",
		name = "Guitar Hero Mode",
		description = "Render \"Guitar Hero\" style prayer helper",
		section = overlaySettings
	)
	default boolean guitarHeroMode()
	{
		return false;
	}
	
	@ConfigItem(
			position = 6,
			keyName = "overlayLine0",
			name = "-------------------------------------",
			description = "",
			section = overlaySettings
	)
	default Boolean overlayLine0() { return false; }

	@ConfigItem(
		position = 7,
		keyName = "fontStyle",
		name = "Font Style",
		description = "Plain | Bold | Italics",
		section = overlaySettings
	)
	default FontStyle fontStyle()
	{
		return FontStyle.BOLD;
	}

	@Range(
		min = 1,
		max = 40
	)
	@ConfigItem(
		position = 8,
		keyName = "textSize",
		name = "Text Size",
		description = "Text Size for Timers.",
		section = overlaySettings
	)
	default int textSize()
	{
		return 32;
	}

	@ConfigItem(
		position = 9,
		keyName = "shadows",
		name = "Text Shadows",
		description = "Adds Shadows to text.",
		section = overlaySettings
	)
	default boolean shadows()
	{
		return false;
	}
	
	
	//Bandos Section
	@ConfigSection(
			name = "<html>Bandos God Wars Dungeon<br>" +
					"Helper Settings</html>",
			description = "General Graardor's automatic actions configuration.",
			position = 2,
			closedByDefault = true
	)
	String bandosHelper = "Bandos Helper Settings";
	
	@ConfigItem(
			position = 0,
			keyName = "bandosLine0",
			name = "Automate Offensive Prayers For:",
			description = "Assign the type of Offensive Prayer to use for each NPC.<br>" +
					"This feature will switch to the assigned offensive prayer depending on the NPC<br>" +
					"you are currently attacking and the NPCs currently alive in the room.<br>" +
					"<b><u>Example</u></b>: Rigour is assigned to General Graardor and Augury is assigned to<br>" +
					"Sgt. Grimspike. As long as General Graardor is alive in the room, you will be praying<br>" +
					"Rigour even if you misclicked on Sgt. Grimspike.",
			section = bandosHelper
	)
	default Boolean bandosLine0() { return false; }
	
	@ConfigItem(
			position = 1,
			keyName = "generalGraardorOffensivePrayer",
			name = "General Graardor",
			description = "What offensive prayer to use when attacking General Graardor?<br>" +
					"If toggled on, this choice has priority over the ones below.<br>" +
					"As long as General Graardor is alive, then it will continue<br>" +
					"praying your choice, even if you attack another NPC in the room.",
			section = bandosHelper
	)
	default OffensivePrayerChoice generalGraardorOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 2,
			keyName = "sergeantSteelwillOffensivePrayer",
			name = "Sgt. Steelwill",
			description = 	"What offensive prayer to use when attacking Sergeant Steelwill (Magic)?<br>" +
					"This choice will activate when General Graardor dies and you start attacking Sergeant Steelwill.",
			section = bandosHelper
	)
	default OffensivePrayerChoice sergeantSteelwillOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	
	@ConfigItem(
			position = 3,
			keyName = "sergeantGrimspikeOffensivePrayer",
			name = "Sgt. Grimspike",
			description = "What offensive prayer to use when attacking Sergeant Grimspike (Ranged)?<br>" +
					"This choice will activate when General Graardor dies and you start attacking Sergeant Grimspike.",
			section = bandosHelper
	)
	default OffensivePrayerChoice sergeantGrimspikeOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 4,
			keyName = "sergeantStrongstackOffensivePrayer",
			name = "Sgt. Strongstack",
			description = "What offensive prayer to use when attacking Sergeant Strongstack (Melee)?<br>" +
					"This choice will activate when General Graardor dies and you start attacking Sergeant Strongstack.",
			section = bandosHelper
	)
	default OffensivePrayerChoice sergeantStrongstackOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 5,
			keyName = "bandosLine1",
			name = "-------------------------------------",
			description = "",
			section = bandosHelper
	)
	default Boolean bandosLine1() { return false; }
	
	@ConfigItem(
			position = 6,
			keyName = "bandosLine2",
			name = "Automate Protection Prayers?",
			description =
					"<b><u>All Kill Long</u></b>: Automate switching prayers from the start to end of the kill.<br>" +
					"<b><u>Only Minions</u></b>: Automate switching prayers when the boss is dead and only the minions are still alive.<br>" +
					"This option is more human like when doing 6:0 type of kills.<br>" +
					"<b><u>Hotkey</u></b>: Use the assigned hotkey to activate automatic prayer switching when in the boss room.<br>" +
					"<b><u>Off</u></b>: Don't automate defensive prayers.",
			section = bandosHelper
	)
	default Boolean bandosLine2() { return false; }
	
	@ConfigItem(
			position = 7,
			keyName = "bandosPrayerSwitchChoice",
			name = "",
			description =
					"How do you want to automate your protection prayers?<br>" +
					"<b><u>All Kill Long</u></b>: Automate switching prayers from the start to end of the kill.<br>" +
					"<b><u>Only Minions</u></b>: Automate switching prayers when the boss is dead and only the minions are still alive.<br>" +
					"This option is more human like when doing 6:0 type of kills.<br>" +
					"<b><u>Hotkey</u></b>: Use the assigned hotkey to activate automatic prayer switching when in the boss room.<br>" +
					"<b><u>Off</u></b>: Don't automate defensive prayers.",
			section = bandosHelper
	)
	default PrayerSwitchChoice autoBandosDefensePrayers()
	{
		return PrayerSwitchChoice.OFF;
	}
	
	@ConfigItem(
			position = 8,
			keyName = "bandosPrayerHotkey",
			name = "Protection Prayer Hotkey",
			description = "Select the <b>Hotkey</b> option in the <b>Automate Protection Prayers?</b> drop down menu.<br>" +
					"If <b>Hotkey</b> is selected, pressing the assigned hotkey will activate automatic protection<br>" +
					"prayers within the in-game region and <b><u>will deactivate when you leave, hop, or log out.</u></b>",
			section = bandosHelper
	)
	default Keybind bandosAutoPrayerHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 10,
			keyName = "bandosLine3",
			name = "-------------------------------------",
			description = "",
			section = bandosHelper
	)
	default Boolean bandosLine3() { return false; }
	
	@ConfigItem(
			position = 11,
			keyName = "bandosLine4",
			name = "Assign NPC Protection Priority",
			description = "If multiple NPCs attack on the same game cycle, then the script will pray against<br>" +
					"the NPC with the highest assigned priority, as indicated below.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"<b>Highly recommended <u>NOT</u> to assign multiple NPCs to the same priority level.<br>" +
					"If you do not know what you are doing, then do not modify these settings.</b>",
			section = bandosHelper
	)
	default Boolean bandosLine4()
	{
		return false;
	}
	
	@Range(max = 3)
	@ConfigItem(
			position = 12,
			keyName = "generalGraardorPriority",
			name = "General Graardor",
			description = "General Graardor protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"<b>Unless you want to die a lot, keep this priority at its max value.</b>",
			section = bandosHelper
	)
	default int generalGraadorPriority() { return 3; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 13,
			keyName = "sergeantSteelwillPriority",
			name = "Sergeant Steelwill",
			description = "Sergeant Steelwill (Magic) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"Magic priority is roughly better than ranged priority, but can interchange.",
			section = bandosHelper
	)
	default int sergeantSteelwillPriority() { return 2; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 14,
			keyName = "sergeantGrimspikePriority",
			name = "Sergeant Grimspike",
			description = "Sergeant Grimspike (Ranged) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"Ranged priority is roughly worst than magic priority, but can interchange.",
			section = bandosHelper
	)
	default int sergeantGrimspikePriority() { return 1; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 15,
			keyName = "sergeantStrongstackPriority",
			name = "Sergeant Strongstack",
			description = "Sergeant Strongstack (Melee) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"You are an idiot if you set the melee minion priority any higher than 0.",
			section = bandosHelper
	)
	default int sergeantStrongstackPriority() { return 0; }
	
	@ConfigItem(
			position = 16,
			keyName = "bandosLine5",
			name = "-------------------------------------",
			description = "",
			section = bandosHelper
	)
	default Boolean bandosLine5() { return false; }
	
	@ConfigItem(
			position = 17,
			keyName = "bandosLine6",
			name = "Automate Gear Switching On:",
			description = "Underneath are settings to switch gear within Bandos' Stronghold. You can configure to <br>" +
					"automate gear switching at a predetermined event and/or with a hotkey.",
			section = bandosHelper
	)
	default Boolean bandosLine6() { return false; }
	
	@ConfigItem(
			position = 18,
			keyName = "generalGraardorDeathGearBoolean",
			name = "General Graardor's Death?",
			description = "<b><u>Gear IDs Set 1 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the boss.<br>" +
					"This will only trigger once, on General Graardor's death.",
			section = bandosHelper
	)
	default boolean generalGraardorDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 19,
			keyName = "generalGraardorDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 1 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 1.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = bandosHelper
	)
	default Keybind generalGraardorDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 20,
			keyName = "generalGraardorGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 1:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the General Graardor's death event trigger and it's hotkey trigger.<br>",
			section = bandosHelper
	)
	default String generalGraardorGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 21,
			keyName = "sergeantSteelwillDeathGearBoolean",
			name = "Sergeant Steelwill's Death?",
			description = "<b><u>Gear IDs Set 2 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the magic minion.<br>" +
					"This will only trigger once, on Sergeant Steelwill's (Magic) death.",
			section = bandosHelper
	)
	default boolean sergeantSteelwillDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 22,
			keyName = "sergeantSteelwillDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 2 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 2.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = bandosHelper
	)
	default Keybind sergeantSteelwillDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 23,
			keyName = "sergeantSteelwillGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 2:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Sergeant Steelwill's death event trigger and it's hotkey trigger.<br>",
			section = bandosHelper
	)
	default String sergeantSteelwillGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 24,
			keyName = "sergeantGrimspikeDeathGearBoolean",
			name = "Sergeant Grimspike's Death?",
			description = "<b><u>Gear IDs Set 3 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the ranged minion.<br>" +
					"This will only trigger once, on Sergeant Grimspike's (Ranged) death.",
			section = bandosHelper
	)
	default boolean sergeantGrimspikeDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 25,
			keyName = "sergeantGrimspikeDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 3 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 3.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = bandosHelper
	)
	default Keybind sergeantGrimspikeGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 26,
			keyName = "sergeantGrimspikeGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 3:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Sergeant Grimspike's death event trigger and it's hotkey trigger.<br>",
			section = bandosHelper
	)
	default String sergeantGrimspikeGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 27,
			keyName = "sergeantStrongstackDeathGearBoolean",
			name = "Sergeant Strongstack's Death?",
			description = "<b><u>Gear IDs Set 4 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the melee minion.<br>" +
					"This will only trigger once, on Sergeant Strongstack's (Melee) death.",
			section = bandosHelper
	)
	default boolean sergeantStrongstackDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 28,
			keyName = "sergeantStrongstackDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 4 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 4.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = bandosHelper
	)
	default Keybind sergeantStrongstackGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 29,
			keyName = "sergeantStrongstackGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 4:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Sergeant Strongstack's death event trigger and it's hotkey trigger.<br>",
			section = bandosHelper
	)
	default String sergeantStrongstackGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 30,
			keyName = "bandosDeathSpawnGearBoolean",
			name = "All Dead or Graardor's Spawn?",
			description = "<b><u>Gear IDs Set 5 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed all the NPCs or General Graardor spawns.<br>" +
					"This will only trigger once, on boss and minions' deaths or on boss spawn.",
			section = bandosHelper
	)
	default boolean bandosDeathSpawnGearBoolean() { return false; }
	
	@ConfigItem(
			position = 31,
			keyName = "bandosDeathSpawnGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 5 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 5.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = bandosHelper
	)
	default Keybind bandosDeathSpawnGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 32,
			keyName = "bandosDeathSpawnGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 5:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the All Dead or Graardor's Spawn event trigger and it's hotkey trigger.<br>",
			section = bandosHelper
	)
	default String bandosDeathSpawnGearIds() { return "0,0"; }
	
	
	
	
	
	
	
	
	//Zamorak section
	@ConfigSection(
			name = "<html>Zamorak God Wars Dungeon<br>" +
					"Helper Settings</html>",
			description = "Kril Tsutsaroth automatic actions configuration.",
			position = 3,
			closedByDefault = true
	)
	String zamorakHelper = "Zamorak God Wars Dungeon Helper Settings";
	
	@ConfigItem(
			position = 0,
			keyName = "zamorakLine0",
			name = "Automate Offensive Prayers For:",
			description = "Assign the type of Offensive Prayer to use for each NPC.<br>" +
					"This feature will switch to the assigned offensive prayer depending on the NPC<br>" +
					"you are currently attacking and the NPCs currently alive in the room.<br>" +
					"<b><u>Example</u></b>: Rigour is assigned to K'ril Tsutsaroth and Augury is assigned to<br>" +
					"Zakln Gritch. As long as K'ril Tsutsaroth is alive in the room, you will be praying<br>" +
					"Rigour even if you misclicked on Zakln Gritch.",
			section = zamorakHelper
	)
	default Boolean zamorakLine0() { return false; }
	
	@ConfigItem(
			position = 1,
			keyName = "krilTsutsarothOffensivePrayer",
			name = "K'ril Tsutsaroth",
			description = "What offensive prayer to use when attacking K'ril Tsutsaroth?<br>" +
					"If toggled on, this choice has priority over the ones below.<br>" +
					"As long as K'ril Tsutsaroth is alive, then it will continue<br>" +
					"praying your choice, even if you attack another NPC in the room.",
			section = zamorakHelper
	)
	default OffensivePrayerChoice krilTsutsarothOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 2,
			keyName = "balfrugKreeyathOffensivePrayer",
			name = "Balfrug Kreeyath",
			description = 	"What offensive prayer to use when attacking Balfrug Kreeyath (Magic)?<br>" +
					"This choice will activate when K'ril Tustsaroth dies and you start attacking Balfrug Kreeyath.",
			section = zamorakHelper
	)
	default OffensivePrayerChoice balfrugKreeyathOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	
	@ConfigItem(
			position = 3,
			keyName = "zaklnGritchOffensivePrayer",
			name = "Zakl'n Gritch",
			description = "What offensive prayer to use when attacking Zakl'n Gritch (Ranged)?<br>" +
					"This choice will activate when K'ril Tsutsaroth dies and you start attacking Zakl'n Gritch.",
			section = zamorakHelper
	)
	default OffensivePrayerChoice zaklnGritchOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 4,
			keyName = "tstanonKarlakOffensivePrayer",
			name = "Tstanon Karlak",
			description = "What offensive prayer to use when attacking Tstanon Karlak (Melee)?<br>" +
					"This choice will activate when K'ril Tsutsaroth dies and you start attacking Tstanon Karlak.",
			section = zamorakHelper
	)
	default OffensivePrayerChoice tstanonKarlakOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 5,
			keyName = "zamorakLine1",
			name = "-------------------------------------",
			description = "",
			section = zamorakHelper
	)
	default Boolean zamorakLine1() { return false; }
	
	@ConfigItem(
			position = 6,
			keyName = "zamorakLine2",
			name = "K'ril Tsutsaroth Protection Prayer",
			description = "Choose what protection prayer to use against K'ril Tsutsaroth.<br>" +
					"Protect from Melee or Protect from Magic depending on your choice here.",
			section = zamorakHelper
	)
	default Boolean zamorakLine2()
	{
		return false;
	}
	
	@ConfigItem(
			position = 7,
			keyName = "krilProtectionPrayerChoice",
			name = "",
			description = "Choose what protection prayer to use against K'ril Tsutsaroth.<br>" +
					"Protect from Melee or Protect from Magic depending on your choice here.",
			section = zamorakHelper
	)
	default KrilAndZilyanaPrayerChoice krilProtectionPrayerChoice() { return KrilAndZilyanaPrayerChoice.PROTECT_FROM_MELEE; }
	
	@ConfigItem(
			position = 8,
			keyName = "zamorakLine3",
			name = "Automate Protection Prayers?",
			description = "<b><u>All Kill Long</u></b>: Automate switching prayers from the start to end of the kill.<br>" +
					"<b><u>Only Minions</u></b>: Automate switching prayers when the boss is dead and only the minions are still alive.<br>" +
					"This option is more human like when doing 6:0 type of kills.<br>" +
					"<b><u>Hotkey</u></b>: Use the assigned hotkey to activate automatic prayer switching when in the boss room.<br>" +
					"<b><u>Off</u></b>: Don't automate defensive prayers.",
			section = zamorakHelper
	)
	default Boolean zamorakLine3() { return false; }
	
	@ConfigItem(
			position = 9,
			keyName = "zamorakPrayerSwitchChoice",
			name = "",
			description =
					"How do you want to automate your protection prayers?<br>" +
					"<b><u>All Kill Long</u></b>: Automate switching prayers from the start to end of the kill.<br>" +
					"<b><u>Only Minions</u></b>: Automate switching prayers when the boss is dead and only the minions are still alive.<br>" +
					"This option is more human like when doing 6:0 type of kills.<br>" +
					"<b><u>Hotkey</u></b>: Use the assigned hotkey to activate automatic prayer switching when in the boss room.<br>" +
					"<b><u>Off</u></b>: Don't automate defensive prayers.",
			section = zamorakHelper
	)
	default PrayerSwitchChoice autoZamorakDefensePrayers()
	{
		return PrayerSwitchChoice.OFF;
	}
	
	@ConfigItem(
			position = 10,
			keyName = "zamorakPrayerHotkey",
			name = "Protection Prayer Hotkey",
			description = "Select the <b>Hotkey</b> option in the <b>Automate Protection Prayers?</b> drop down menu.<br>" +
					"If <b>Hotkey</b> is selected, pressing the assigned hotkey will activate automatic protection<br>" +
					"prayers within the in-game region and <b><u>will deactivate when you leave, hop, or log out.</u></b>",
			section = zamorakHelper
	)
	default Keybind zamorakAutoPrayerHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 12,
			keyName = "zamorakLine4",
			name = "-------------------------------------",
			description = "",
			section = zamorakHelper
	)
	default Boolean zamorakLine4() { return false; }
	
	@ConfigItem(
			position = 13,
			keyName = "zamorakLine5",
			name = "Assign NPC Protection Priority",
			description = "If multiple NPCs attack on the same game cycle, then the script will pray against<br>" +
					"the NPC with the highest assigned priority, as indicated below.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"<b>Highly recommended <u>NOT</u> to assign multiple NPCs to the same priority level.<br>" +
					"If you do not know what you are doing, then do not modify these settings.</b>",
			section = zamorakHelper
	)
	default Boolean zamorakLine5()
	{
		return false;
	}
	
	@Range(max = 3)
	@ConfigItem(
			position = 14,
			keyName = "krilTsutsarothPriority",
			name = "K'ril Tsutsaroth",
			description = "K'ril Tsutsaroth protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"<b>Unless you want to die a lot, keep this priority at its max value.</b>",
			section = zamorakHelper
	)
	default int krilTsutsarothPriority() { return 3; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 15,
			keyName = "balfrugKreeyathPriority",
			name = "Balfrug Kreeyath",
			description = "Balfrug Kreeyath (Magic) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"Magic priority is roughly better than ranged priority, but can interchange.",
			section = zamorakHelper
	)
	default int balfrugKreeyathPriority() { return 2; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 16,
			keyName = "zaklnGritchPriority",
			name = "Zakl'n Gritch",
			description = "Zakl'n Gritch (Ranged) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"Ranged priority is roughly worst than magic priority, but can interchange.",
			section = zamorakHelper
	)
	default int zaklnGritchPriority() { return 1; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 17,
			keyName = "tstanonKarlakPriority",
			name = "Tstanon Karlak",
			description = "Tstanon Karlak (Melee) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"You are an idiot if you set the melee minion priority any higher than 0.",
			section = zamorakHelper
	)
	default int tstanonKarlakPriority() { return 0; }
	
	@ConfigItem(
			position = 18,
			keyName = "zamorakLine6",
			name = "-------------------------------------",
			description = "",
			section = zamorakHelper
	)
	default Boolean zamorakLine6() { return false; }
	
	@ConfigItem(
			position = 19,
			keyName = "zamorakLine7",
			name = "Automate Gear Switching On:",
			description = "Underneath are settings to switch gear within Zamorak's Fortress. You can configure to <br>" +
					"automate gear switching at a predetermined event and/or with a hotkey.",
			section = zamorakHelper
	)
	default Boolean zamorakLine7() { return false; }
	
	@ConfigItem(
			position = 20,
			keyName = "krilTsutsarothDeathGearBoolean",
			name = "K'ril Tsutsaroth's Death?",
			description = "<b><u>Gear IDs Set 1 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the boss.<br>" +
					"This will only trigger once, on K'ril Tsutsaroth's death.",
			section = zamorakHelper
	)
	default boolean krilTsutsarothDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 21,
			keyName = "krilTsutsarothDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 1 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 1.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = zamorakHelper
	)
	default Keybind krilTsutsarothDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 22,
			keyName = "krilTsutsarothGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 1:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the K'ril Tsutsaroth's death event trigger and it's hotkey trigger.<br>",
			section = zamorakHelper
	)
	default String krilTsutsarothGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 23,
			keyName = "balfrugKreeyathDeathGearBoolean",
			name = "Balfrug Kreeyath's Death?",
			description = "<b><u>Gear IDs Set 2 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the magic minion.<br>" +
					"This will only trigger once, on Balfrug Kreeyath's (Magic) death.",
			section = zamorakHelper
	)
	default boolean balfrugKreeyathDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 24,
			keyName = "balfrugKreeyathDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 2 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 2.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = zamorakHelper
	)
	default Keybind balfrugKreeyathDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 25,
			keyName = "balfrugKreeyathGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 2:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Balfrug Kreeyath's death event trigger and it's hotkey trigger.<br>",
			section = zamorakHelper
	)
	default String balfrugKreeyathGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 26,
			keyName = "zaklnGritchDeathGearBoolean",
			name = "Zakl'n Gritch's Death?",
			description = "<b><u>Gear IDs Set 3 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the ranged minion.<br>" +
					"This will only trigger once, on Zakl'n Gritch's (Ranged) death.",
			section = zamorakHelper
	)
	default boolean zaklnGritchDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 27,
			keyName = "zaklnGritchGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 3 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 3.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = zamorakHelper
	)
	default Keybind zaklnGritchDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 28,
			keyName = "zaklnGritchGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 3:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Zakl'n Gritch's death event trigger and it's hotkey trigger.<br>",
			section = zamorakHelper
	)
	default String zaklnGritchGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 29,
			keyName = "tstanonKarlakDeathGearBoolean",
			name = "Tstanon Karlak's Death?'",
			description = "<b><u>Gear IDs Set 4 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the melee minion.<br>" +
					"This will only trigger once, on Tstanon Karlak's (Melee) death.",
			section = zamorakHelper
	)
	default boolean tstanonKarlakDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 30,
			keyName = "tstanonKarlakDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 4 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 4.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = zamorakHelper
	)
	default Keybind tstanonKarlakDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 31,
			keyName = "tstanonKarlakGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 4:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Tstanon Karlak's death event trigger and it's hotkey trigger.<br>",
			section = zamorakHelper
	)
	default String tstanonKarlakGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 32,
			keyName = "zamorakDeathSpawnGearBoolean",
			name = "All Dead or K'ril's Spawn?",
			description = "<b><u>Gear IDs Set 5 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed all the NPCs or K'ril Tsutsaroth spawns.<br>" +
					"This will only trigger once, on boss and minions' deaths or on boss spawn.",
			section = zamorakHelper
	)
	default boolean zamorakDeathSpawnGearBoolean() { return false; }
	
	@ConfigItem(
			position = 33,
			keyName = "zamorakDeathSpawnGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 5 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 5.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = zamorakHelper
	)
	default Keybind zamorakDeathSpawnGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 34,
			keyName = "zamorakDeathSpawnGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 5:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the All Dead or K'ril's Spawn event trigger and it's hotkey trigger.<br>",
			section = zamorakHelper
	)
	default String zamorakDeathSpawnGearIds() { return "0,0"; }
	
	
	
	
	
	//Saradomin Section
	@ConfigSection(
			name = "<html>Saradomin God Wars Dungeon<br>" +
					"Helper Settings</html>",
			description = "Commander Zilyana's automatic actions configuration.",
			position = 4,
			closedByDefault = true
	)
	String saradominHelper = "Saradomin Helper Settings";
	
	@ConfigItem(
			position = 0,
			keyName = "saradominLine0",
			name = "Automate Offensive Prayers For:",
			description = "Assign the type of Offensive Prayer to use for each NPC.<br>" +
					"This feature will switch to the assigned offensive prayer depending on the NPC<br>" +
					"you are currently attacking and the NPCs currently alive in the room.<br>" +
					"<b><u>Example</u></b>: Rigour is assigned to Commander Zilyana and Augury is assigned to<br>" +
					"Starlight. As long as Commander Zilyana is alive in the room, you will be praying<br>" +
					"Rigour even if you misclicked on Starlight.",
			section = saradominHelper
	)
	default Boolean saradominLine0() { return false; }
	
	@ConfigItem(
			position = 1,
			keyName = "commanderZilyanaOffensivePrayer",
			name = "Cmdr. Zilyana",
			description = "What offensive prayer to use when attacking Commander Zilyana?<br>" +
					"If toggled on, this choice has priority over the ones below.<br>" +
					"As long as Commander Zilyana is alive, then it will continue<br>" +
					"praying your choice, even if you attack another NPC in the room.",
			section = saradominHelper
	)
	default OffensivePrayerChoice commanderZilyanaOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 2,
			keyName = "growlerOffensivePrayer",
			name = "Growler",
			description = 	"What offensive prayer to use when attacking Growler (Magic)?<br>" +
					"This choice will activate when Commander Zilyana dies and you start attacking Growler.",
			section = saradominHelper
	)
	default OffensivePrayerChoice growlerOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	
	@ConfigItem(
			position = 3,
			keyName = "breeOffensivePrayer",
			name = "Bree",
			description = "What offensive prayer to use when attacking Bree (Ranged)?<br>" +
					"This choice will activate when Commander Zilyana dies and you start attacking Bree.",
			section = saradominHelper
	)
	default OffensivePrayerChoice breeOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 4,
			keyName = "starlightOffensivePrayer",
			name = "Starlight",
			description = "What offensive prayer to use when attacking Starlight (Melee)?<br>" +
					"This choice will activate when Commander Zilyana dies and you start attacking Starlight.",
			section = saradominHelper
	)
	default OffensivePrayerChoice starlightOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 5,
			keyName = "saradominLine1",
			name = "-------------------------------------",
			description = "",
			section = saradominHelper
	)
	default Boolean saradominLine1() { return false; }
	
	@ConfigItem(
			position = 6,
			keyName = "saradominLine2",
			name = "Commander Zilyana Protection Prayer",
			description = "Choose what protection prayer to use against Commander Zilyana.<br>" +
					"Protect from Melee or Protect from Magic depending on your choice here.",
			section = saradominHelper
	)
	default Boolean saradominLine2()
	{
		return false;
	}
	
	@ConfigItem(
			position = 7,
			keyName = "zilyanaProtectionPrayerChoice",
			name = "",
			description = "Choose what protection prayer to use against Commander Zilyana.<br>" +
					"Protect from Melee or Protect from Magic depending on your choice here.",
			section = saradominHelper
	)
	default KrilAndZilyanaPrayerChoice zilyanaProtectionPrayerChoice() { return KrilAndZilyanaPrayerChoice.PROTECT_FROM_MAGIC; }
	
	@ConfigItem(
			position = 8,
			keyName = "saradominLine3",
			name = "Automate Protection Prayers?",
			description = "<b><u>All Kill Long</u></b>: Automate switching prayers from the start to end of the kill.<br>" +
					"<b><u>Only Minions</u></b>: Automate switching prayers when the boss is dead and only the minions are still alive.<br>" +
					"This option is more human like when doing 6:0 type of kills.<br>" +
					"<b><u>Hotkey</u></b>: Use the assigned hotkey to activate automatic prayer switching when in the boss room.<br>" +
					"<b><u>Off</u></b>: Don't automate defensive prayers.",
			section = saradominHelper
	)
	default Boolean saradominLine3() { return false; }
	
	@ConfigItem(
			position = 9,
			keyName = "saradominPrayerSwitchChoice",
			name = "",
			description =
					"How do you want to automate your protection prayers?<br>" +
							"<b><u>All Kill Long</u></b>: Automate switching prayers from the start to end of the kill.<br>" +
							"<b><u>Only Minions</u></b>: Automate switching prayers when the boss is dead and only the minions are still alive.<br>" +
							"This option is more human like when doing 6:0 type of kills.<br>" +
							"<b><u>Hotkey</u></b>: Use the assigned hotkey to activate automatic prayer switching when in the boss room.<br>" +
							"<b><u>Off</u></b>: Don't automate defensive prayers.",
			section = saradominHelper
	)
	default PrayerSwitchChoice autoSaradominDefensePrayers()
	{
		return PrayerSwitchChoice.OFF;
	}
	
	@ConfigItem(
			position = 10,
			keyName = "saradominPrayerHotkey",
			name = "Protection Prayer Hotkey",
			description = "Select the <b>Hotkey</b> option in the <b>Automate Protection Prayers?</b> drop down menu.<br>" +
					"If <b>Hotkey</b> is selected, pressing the assigned hotkey will activate automatic protection<br>" +
					"prayers within the in-game region and <b><u>will deactivate when you leave, hop, or log out.</u></b>",
			section = saradominHelper
	)
	default Keybind saradominAutoPrayerHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 11,
			keyName = "saradominLine4",
			name = "-------------------------------------",
			description = "",
			section = saradominHelper
	)
	default Boolean saradominLine4() { return false; }
	
	@ConfigItem(
			position = 12,
			keyName = "saradominLine5",
			name = "Assign NPC Protection Priority",
			description = "If multiple NPCs attack on the same game cycle, then the script will pray against<br>" +
					"the NPC with the highest assigned priority, as indicated below.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"<b>Highly recommended <u>NOT</u> to assign multiple NPCs to the same priority level.<br>" +
					"If you do not know what you are doing, then do not modify these settings.</b>",
			section = saradominHelper
	)
	default Boolean saradominLine5()
	{
		return false;
	}
	
	@Range(max = 3)
	@ConfigItem(
			position = 13,
			keyName = "commanderZilyanaPriority",
			name = "Commander Zilyana",
			description = "Commander Zilyana protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"<b>Unless you want to die a lot, keep this priority at its max value.</b>",
			section = saradominHelper
	)
	default int commanderZilyanaPriority() { return 3; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 14,
			keyName = "growlerPriority",
			name = "Growler",
			description = "Growler (Magic) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"Magic priority is roughly better than ranged priority, but can interchange.",
			section = saradominHelper
	)
	default int growlerPriority() { return 2; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 15,
			keyName = "breePriority",
			name = "Bree",
			description = "Bree (Ranged) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"Ranged priority is roughly worst than magic priority, but can interchange.",
			section = saradominHelper
	)
	default int breePriority() { return 1; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 16,
			keyName = "starlightPriority",
			name = "Starlight",
			description = "Starlight (Melee) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"You are an idiot if you set the melee minion priority any higher than 0.",
			section = saradominHelper
	)
	default int starlightPriority() { return 0; }
	
	@ConfigItem(
			position = 17,
			keyName = "saradominLine6",
			name = "-------------------------------------",
			description = "",
			section = saradominHelper
	)
	default Boolean saradominLine6() { return false; }
	
	@ConfigItem(
			position = 18,
			keyName = "saradomineLine7",
			name = "Automate Gear Switching On:",
			description = "Underneath are settings to switch gear within Saradomin's Encampment. You can<br>" +
					"configure to automate gear switching at a predetermined event and/or with a hotkey.",
			section = saradominHelper
	)
	default Boolean saradominLine7() { return false; }
	
	@ConfigItem(
			position = 19,
			keyName = "commanderZilyanaDeathGearBoolean",
			name = "Commander Zilyana's Death?",
			description = "<b><u>Gear IDs Set 1 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the boss.<br>" +
					"This will only trigger once, on Commander Zilyana's death.",
			section = saradominHelper
	)
	default boolean commanderZilyanaDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 20,
			keyName = "commanderZilyanaDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 1 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 1.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = saradominHelper
	)
	default Keybind commanderZilyanaDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 21,
			keyName = "commanderZilyanaGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 1:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Commander Zilyana's death event trigger and it's hotkey trigger.<br>",
			section = saradominHelper
	)
	default String commanderZilyanaGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 22,
			keyName = "growlerDeathGearBoolean",
			name = "Growler's Death?",
			description = "<b><u>Gear IDs Set 2 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the magic minion.<br>" +
					"This will only trigger once, on Growler's (Magic) death.",
			section = saradominHelper
	)
	default boolean growlerDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 23,
			keyName = "growlerDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 2 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 2.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = saradominHelper
	)
	default Keybind growlerDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 24,
			keyName = "growlerGearIds",
			name = "Growler Gear Ids",
			description = "<b><u>Gear IDs Set 2:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Growler's death event trigger and it's hotkey trigger.<br>",
			section = saradominHelper
	)
	default String growlerGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 25,
			keyName = "breeDeathGearBoolean",
			name = "Bree's Death?",
			description = "<b><u>Gear IDs Set 3 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the ranged minion.<br>" +
					"This will only trigger once, on Bree's (Ranged) death.",
			section = saradominHelper
	)
	default boolean breeGearBoolean() { return false; }
	
	@ConfigItem(
			position = 26,
			keyName = "breeDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 3 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 3.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = saradominHelper
	)
	default Keybind breeDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 27,
			keyName = "breeGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 3:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Bree's death event trigger and it's hotkey trigger.<br>",
			section = saradominHelper
	)
	default String breeGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 28,
			keyName = "starlightDeathGearBoolean",
			name = "Starlight Gear Switch?",
			description = "<b><u>Gear IDs Set 4 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the melee minion.<br>" +
					"This will only trigger once, on Starlight's (Melee) death.",
			section = saradominHelper
	)
	default boolean starlightDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 29,
			keyName = "starlightDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 4 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 4.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = saradominHelper
	)
	default Keybind starlightDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 30,
			keyName = "starlightGearIds",
			name = "Starlight Gear Ids",
			description = "<b><u>Gear IDs Set 4:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Starlight's death event trigger and it's hotkey trigger.<br>",
			section = saradominHelper
	)
	default String starlightGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 31,
			keyName = "saradominDeathSpawnGearBoolean",
			name = "All Dead or Zilyana's Spawn?",
			description = "<b><u>Gear IDs Set 5 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed all the NPCs or Commander Zilyana spawns.<br>" +
					"This will only trigger once, on boss and minions' deaths or on boss spawn.",
			section = saradominHelper
	)
	default boolean saradominDeathSpawnGearBoolean() { return false; }
	
	@ConfigItem(
			position = 32,
			keyName = "saradominDeathSpawnGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 5 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 5.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = saradominHelper
	)
	default Keybind saradominDeathSpawnGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 33,
			keyName = "saradominDeathSpawnGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 5:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the All Dead or Zilyana's Spawn event trigger and it's hotkey trigger.<br>",
			section = saradominHelper
	)
	default String saradominDeathSpawnGearIds() { return "0,0"; }
	
	
	
	
	
	//Armadyl Section
	@ConfigSection(
			name = "<html>Armadyl God Wars Dungeon<br>" +
					"Helper Settings</html>",
			description = "Kree'arra's automatic actions configuration",
			position = 5,
			closedByDefault = true
	)
	String armadylHelper = "Armadyl Helper Settings";
	
	@ConfigItem(
			position = 0,
			keyName = "armadylLine0",
			name = "Automate Offensive Prayers For:",
			description = "Assign the type of Offensive Prayer to use for each NPC.<br>" +
					"This feature will switch to the assigned offensive prayer depending on the NPC<br>" +
					"you are currently attacking and the NPCs currently alive in the room.<br>" +
					"<b><u>Example</u></b>: Rigour is assigned to Kree'arra and Augury is assigned to<br>" +
					"Flight Kilisa. As long as Kree'arra is alive in the room, you will be praying<br>" +
					"Rigour even if you misclicked on Flight Kilisa.",
			section = armadylHelper
	)
	default Boolean armadylLine0() { return false; }
	
	@ConfigItem(
			position = 1,
			keyName = "kreearraOffensivePrayer",
			name = "Kree'arra",
			description = "What offensive prayer to use when attacking Kree'arra?<br>" +
					"If toggled on, this choice has priority over the ones below.<br>" +
					"As long as Kree'arra is alive, then it will continue<br>" +
					"praying your choice, even if you attack another NPC in the room.",
			section = armadylHelper
	)
	default OffensivePrayerChoice kreearraOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 2,
			keyName = "wingmanSkreeOffensivePrayer",
			name = "Wingman Skree",
			description = 	"What offensive prayer to use when attacking Wingman Skree (Magic)?<br>" +
					"This choice will activate when Kree'arra dies and you start attacking Wingman Skree.",
			section = armadylHelper
	)
	default OffensivePrayerChoice wingmanSkreeOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	
	@ConfigItem(
			position = 3,
			keyName = "flockleaderGeerinOffensivePrayer",
			name = "Flockleader Geerin",
			description = "What offensive prayer to use when attacking Flockleader Geerin (Ranged)?<br>" +
					"This choice will activate when Kree'arra dies and you start attacking Flockleader Geerin.",
			section = armadylHelper
	)
	default OffensivePrayerChoice flockleaderGeerinOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 4,
			keyName = "flightKilisaOffensivePrayer",
			name = "Flight Kilisa",
			description = "What offensive prayer to use when attacking Flight Kilisa (Melee)?<br>" +
					"This choice will activate when Kree'arra dies and you start attacking Flight Kilisa.",
			section = armadylHelper
	)
	default OffensivePrayerChoice flightKilisaOffensivePrayer() { return OffensivePrayerChoice.OFF; }
	
	@ConfigItem(
			position = 5,
			keyName = "armadylLine1",
			name = "-------------------------------------",
			description = "",
			section = armadylHelper
	)
	default Boolean armadylLine1() { return false; }
	
	@ConfigItem(
			position = 6,
			keyName = "armadylLine2",
			name = "Automate Protection Prayers?",
			description = "<b><u>All Kill Long</u></b>: Automate switching prayers from the start to end of the kill.<br>" +
					"<b><u>Only Minions</u></b>: Automate switching prayers when the boss is dead and only the minions are still alive.<br>" +
					"This option is more human like when doing 6:0 type of kills.<br>" +
					"<b><u>Hotkey</u></b>: Use the assigned hotkey to activate automatic prayer switching when in the boss room.<br>" +
					"<b><u>Off</u></b>: Don't automate defensive prayers.",
			section = armadylHelper
	)
	default Boolean armadylLine2() { return false; }
	
	@ConfigItem(
			position = 7,
			keyName = "armadylPrayerSwitchChoice",
			name = "",
			description =
					"How do you want to automate your protection prayers?<br>" +
							"<b><u>All Kill Long</u></b>: Automate switching prayers from the start to end of the kill.<br>" +
							"<b><u>Only Minions</u></b>: Automate switching prayers when the boss is dead and only the minions are still alive.<br>" +
							"This option is more human like when doing 6:0 type of kills.<br>" +
							"<b><u>Hotkey</u></b>: Use the assigned hotkey to activate automatic prayer switching when in the boss room.<br>" +
							"<b><u>Off</u></b>: Don't automate defensive prayers.",
			section = armadylHelper
	)
	default PrayerSwitchChoice autoArmadylDefensePrayers()
	{
		return PrayerSwitchChoice.OFF;
	}
	
	@ConfigItem(
			position = 8,
			keyName = "armadylPrayerHotkey",
			name = "Protection Prayer Hotkey",
			description = "Select the <b>Hotkey</b> option in the <b>Automate Protection Prayers?</b> drop down menu.<br>" +
					"If <b>Hotkey</b> is selected, pressing the assigned hotkey will activate automatic protection<br>" +
					"prayers within the in-game region and <b><u>will deactivate when you leave, hop, or log out.</u></b>",
			section = armadylHelper
	)
	default Keybind armadylAutoPrayerHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 9,
			keyName = "armadylLine3",
			name = "-------------------------------------",
			description = "",
			section = armadylHelper
	)
	default Boolean armadylLine3() { return false; }
	
	@ConfigItem(
			position = 10,
			keyName = "armadylLine4",
			name = "Assign NPC Protection Priority",
			description = "If multiple NPCs attack on the same game cycle, then the script will pray against<br>" +
					"the NPC with the highest assigned priority, as indicated below.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"<b>Highly recommended <u>NOT</u> to assign multiple NPCs to the same priority level.<br>" +
					"If you do not know what you are doing, then do not modify these settings.</b>",
			section = armadylHelper
	)
	default Boolean armadylLine4()
	{
		return false;
	}
	
	@Range(max = 3)
	@ConfigItem(
			position = 11,
			keyName = "kreearraPriority",
			name = "Kree'arra",
			description = "Kree'arra protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"<b>Unless you want to die a lot, keep this priority at its max value.</b>",
			section = armadylHelper
	)
	default int kreearraPriority() { return 3; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 12,
			keyName = "wingmanSkreePriority",
			name = "Wingman Skree",
			description = "Wingman Skree (Magic) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"Magic priority is roughly better than ranged priority, but can interchange.",
			section = armadylHelper
	)
	default int wingmanSkreePriority() { return 2; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 13,
			keyName = "flockleaderGeerinPriority",
			name = "Flockleader Geerin",
			description = "Flockleader Geerin (Ranged) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"Ranged priority is roughly worst than magic priority, but can interchange.",
			section = armadylHelper
	)
	default int flockleaderGeerinPriority() { return 1; }
	
	@Range(max = 3)
	@ConfigItem(
			position = 14,
			keyName = "flightKilisaPriority",
			name = "Flight Kilisa",
			description = "Flight Kilisa (Melee) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"You are an idiot if you set the melee minion priority any higher than 0.",
			section = armadylHelper
	)
	default int flightKilisaPriority() { return 0; }
	
	@ConfigItem(
			position = 15,
			keyName = "armadylLine5",
			name = "-------------------------------------",
			description = "",
			section = armadylHelper
	)
	default Boolean armadylLine5() { return false; }
	
	@ConfigItem(
			position = 16,
			keyName = "armadylLine6",
			name = "Automate Gear Switching On:",
			description = "Underneath are settings to switch gear within Armadyl's Eyrie. You can configure to <br>" +
					"automate gear switching at a predetermined event and/or with a hotkey.",
			section = armadylHelper
	)
	default Boolean armadylLine6() { return false; }
	
	@ConfigItem(
			position = 17,
			keyName = "kreearraDeathGearBoolean",
			name = "Kree'arra's Death?",
			description = "<b><u>Gear IDs Set 1 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the boss.<br>" +
					"This will only trigger once, on Kree'arra's death.",
			section = armadylHelper
	)
	default boolean kreearraDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 18,
			keyName = "kreearraDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 1 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 1.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = armadylHelper
	)
	default Keybind kreearraDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 19,
			keyName = "kreearraGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 1:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Kree'arra's death event trigger and it's hotkey trigger.<br>",
			section = armadylHelper
	)
	default String kreearraGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 20,
			keyName = "wingmanSkreeDeathGearBoolean",
			name = "Wingman Skree's Death?",
			description = "<b><u>Gear IDs Set 2 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the magic minion.<br>" +
					"This will only trigger once, on Wingman Skree's (Magic) death.",
			section = armadylHelper
	)
	default boolean wingmanSkreeDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 21,
			keyName = "wingmanSkreeDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 2 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 2.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = armadylHelper
	)
	default Keybind wingmanSkreeDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 22,
			keyName = "wingmanSkreeGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 2:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Wingman Skree's (Magic) death event trigger and it's hotkey trigger.<br>",
			section = armadylHelper
	)
	default String wingmanSkreeGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 23,
			keyName = "flockleaderGeerinDeathGearBoolean",
			name = "Flockleader Geerin's Death?",
			description = "<b><u>Gear IDs Set 3 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the ranged minion.<br>" +
					"This will only trigger once, on Flockleader Geerin's (Ranged) death.",
			section = armadylHelper
	)
	default boolean flockleaderGeerinDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 24,
			keyName = "flockleaderGeerinDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 3 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 3.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = armadylHelper
	)
	default Keybind flockleaderGeerinDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 25,
			keyName = "flockleaderGeerinGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 3:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Flockleader Geerin's (Ranged) death event trigger and it's hotkey trigger.<br>",
			section = armadylHelper
	)
	default String flockleaderGeerinGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 26,
			keyName = "flightKilisaDeathGearBoolean",
			name = "Flight Kilisa's Death?",
			description = "<b><u>Gear IDs Set 4 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed the melee minion.<br>" +
					"This will only trigger once, on Flight Kilisa's (Melee) death.",
			section = armadylHelper
	)
	default boolean flightKilisaDeathGearBoolean() { return false; }
	
	@ConfigItem(
			position = 27,
			keyName = "flockleaderGeerinDeathGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 4 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 4.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = armadylHelper
	)
	default Keybind flightKilisaDeathGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 28,
			keyName = "flightKilisaGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 4:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the Flight Kilisa's (Melee) death event trigger and it's hotkey trigger.<br>",
			section = armadylHelper
	)
	default String flightKilisaGearIds() { return "0,0"; }
	
	@ConfigItem(
			position = 29,
			keyName = "armadylDeathSpawnGearBoolean",
			name = "All Dead or Kree'arra's Spawn?",
			description = "<b><u>Gear IDs Set 5 - Event:</u></b><br>" +
					"If enabled, this will switch to the specified item ids after you killed all the NPCs or Kree'arra spawns.<br>" +
					"This will only trigger once, on boss and minions' deaths or on boss spawn.",
			section = armadylHelper
	)
	default boolean armadylDeathSpawnGearBoolean() { return false; }
	
	@ConfigItem(
			position = 30,
			keyName = "armadylDeathSpawnGearHotkey",
			name = "Hotkey Trigger",
			description = "<b><u>Gear IDs Set 5 - Hotkey:</u></b><br>" +
					"Assign a hotkey to trigger the switching of gear ids found in Gear IDs Set 5.<br>" +
					"Anytime you press the assigned hotkey, it will switch to this set of gear.",
			section = armadylHelper
	)
	default Keybind armadylDeathSpawnGearHotkey() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 31,
			keyName = "armadylDeathSpawnGearIds",
			name = "Enter Gear IDs Here:",
			description = "<b><u>Gear IDs Set 5:</u></b><br>" +
					"Enter in the item ids to switch to with the format \"1234,4321\".<br>" +
					"This set is for the All Dead or Kree'arra's Spawn event trigger and it's hotkey trigger.<br>",
			section = armadylHelper
	)
	default String armadylDeathSpawnGearIds() { return "0,0"; }
	
	
	
	// Universal God Wars Dungeon Helper Settings
	@ConfigSection(
			name = "<html>Universal God Wars Dungeon<br>" +
					"Helper Settings</html>",
			description = "Universal helper settings that work anywhere inside the original God Wars Dungeon.",
			position = 6,
			closedByDefault = true
	)
	String universalHelper = "Universal Helper Settings";
	
	@ConfigItem(
			position = 0,
			keyName = "universalLine0",
			name = "Hotkeys to Left Click Cast Spells",
			description = "Assign hotkeys to left click cast certain spells when inside the boss room.",
			section = universalHelper
	)
	default Boolean universalLine0() { return false; }
	
	@ConfigItem(
			position = 1,
			keyName = "spellChoice1",
			name = "Spell 1",
			description = "Select the spell to assign to Spell Hotkey 1.",
			section = universalHelper
	)
	default SpellChoice spellChoice1() { return SpellChoice.OFF; }
	
	@ConfigItem(
			position = 2,
			keyName = "spellHotkey1",
			name = "Spell Hotkey 1",
			description = "Assign a hotkey to <b>Hold Down</b> and be able to left click cast your selected spell.<br>" +
					"This hotkey will only work within the original God Wars Dungeon and not at Nex.",
			section = universalHelper
	)
	default Keybind spellHotkey1() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 3,
			keyName = "spellChoice2",
			name = "Spell 2",
			description = "Select the spell to assign to Spell Hotkey 2.",
			section = universalHelper
	)
	default SpellChoice spellChoice2() { return SpellChoice.OFF; }
	
	@ConfigItem(
			position = 4,
			keyName = "spellHotkey2",
			name = "Spell Hotkey 2",
			description = "Assign a hotkey to <b>Hold Down</b> and be able to left click cast your selected spell.<br>" +
					"This hotkey will only work within the original God Wars Dungeon and not at Nex.",
			section = universalHelper
	)
	default Keybind spellHotkey2() { return Keybind.NOT_SET; }
	
	@ConfigItem(
			position = 5,
			keyName = "keepPreservePrayerOn",
			name = "Keep Preserve Prayer Turned On?",
			description = "This will automatically turn on the Preserve Prayer upon entering the boss room.<br>" +
					"It will stay turned on and ignore the automatic deactivation of prayers after a kill.",
			section = universalHelper
	)
	default boolean keepPreservePrayerOn() { return false; }
	
	
	
	@Getter(AccessLevel.PACKAGE)
	@AllArgsConstructor
	enum FontStyle
	{
		BOLD("Bold", Font.BOLD),
		ITALIC("Italic", Font.ITALIC),
		PLAIN("Plain", Font.PLAIN);

		private final String name;
		private final int font;

		@Override
		public String toString()
		{
			return getName();
		}
	}
	
	@Getter
	@AllArgsConstructor
	enum PrayerSwitchChoice
	{
		ALL_KILL_LONG(0),
		ONLY_MINIONS(1),
		HOTKEY(2),
		OFF(3);
		
		private final int choice;
	}
	
	@Getter
	@AllArgsConstructor
	enum KrilAndZilyanaPrayerChoice
	{
		PROTECT_FROM_MELEE(Prayer.PROTECT_FROM_MELEE),
		PROTECT_FROM_MAGIC(Prayer.PROTECT_FROM_MAGIC);
		
		private final Prayer prayer;
	}
	
	@Getter
	@AllArgsConstructor
	enum OffensivePrayerChoice
	{
		PIETY(Prayer.PIETY),
		EAGLE_EYE(Prayer.EAGLE_EYE),
		RIGOUR(Prayer.RIGOUR),
		MYSTIC_MIGHT(Prayer.MYSTIC_MIGHT),
		AUGURY(Prayer.AUGURY),
		OFF(null);
		
		private final Prayer prayer;
	}
	
	@Getter
	@AllArgsConstructor
	enum SpellChoice
	{
		BLOOD_BURST("Blood Burst", WidgetInfoPlus.SPELL_BLOOD_BURST),
		ICE_BURST("Ice Burst", WidgetInfoPlus.SPELL_ICE_BURST),
		BLOOD_BARRAGE("Blood Barrage", WidgetInfoPlus.SPELL_BLOOD_BARRAGE),
		ICE_BARRAGE("Ice Barrage", WidgetInfoPlus.SPELL_ICE_BARRAGE),
		SUPERIOR_DEMONBANE("Superior Demonbane", WidgetInfoPlus.SPELL_SUPERIOR_DEMONBANE),
		DARK_DEMONBANE("Dark Demonbane", WidgetInfoPlus.SPELL_DARK_DEMONBANE),
		MARK_OF_DARKNESS("Mark of Darkness", WidgetInfoPlus.SPELL_MARK_OF_DARKNESS),
		OFF("Off", null);
		
		private final String spellString;
		private final WidgetInfoPlus widgetInfo;
	}
}
