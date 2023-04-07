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
			name = "<html>God Wars Helper<br>Version 2.0.0</html>",
			description = "",
			position = -1,
			closedByDefault = true
	)
	String versionInfo = "Version";

	@ConfigSection(
		position = 0,
		name = "Overlay Settings",
		description = "",
		closedByDefault = true
	)
	String overlaySettings = "Overlay Settings";

	@ConfigItem(
		position = 0,
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
		position = 1,
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
		position = 2,
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
		position = 3,
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
		position = 4,
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
		position = 5,
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
		position = 6,
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
		position = 7,
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
			position = 9,
			keyName = "bandosLine3",
			name = "-------------------------------------",
			description = "",
			section = bandosHelper
	)
	default Boolean bandosLine3() { return false; }
	
	@ConfigItem(
			position = 10,
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
			position = 11,
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
			position = 12,
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
			position = 13,
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
			position = 14,
			keyName = "sergeantStrongstackPriority",
			name = "Sergeant Strongstack",
			description = "Sergeant Strongstack (Melee) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"You are an idiot if you set the melee minion priority any higher than 0.",
			section = bandosHelper
	)
	default int sergeantStrongstackPriority() { return 0; }
	
	
	
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
			position = 11,
			keyName = "zamorakLine4",
			name = "-------------------------------------",
			description = "",
			section = zamorakHelper
	)
	default Boolean zamorakLine4() { return false; }
	
	@ConfigItem(
			position = 12,
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
			position = 13,
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
			position = 14,
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
			position = 15,
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
			position = 16,
			keyName = "tstanonKarlakPriority",
			name = "Tstanon Karlak",
			description = "Tstanon Karlak (Melee) protection priority.<br>" +
					"Scale of 0 - 3. Lower is less priority and higher is more priority.<br>" +
					"Highly recommended <u><b>NOT</b></u> to assign multiple NPCs to the same priority level.<br>" +
					"You are an idiot if you set the melee minion priority any higher than 0.",
			section = zamorakHelper
	)
	default int tstanonKarlakPriority() { return 0; }
	
	
	
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
	
	
	
	
	
	@Getter(AccessLevel.PACKAGE)
	@AllArgsConstructor
	enum FontStyle
	{
		BOLD("Bold", Font.BOLD),
		ITALIC("Italic", Font.ITALIC),
		PLAIN("Plain", Font.PLAIN);

		private String name;
		private int font;

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
		
		private int choice;
	}
	
	@Getter
	@AllArgsConstructor
	enum KrilAndZilyanaPrayerChoice
	{
		PROTECT_FROM_MELEE(Prayer.PROTECT_FROM_MELEE),
		PROTECT_FROM_MAGIC(Prayer.PROTECT_FROM_MAGIC);
		
		private Prayer prayer;
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
		
		private Prayer prayer;
	}
}
