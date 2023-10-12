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

import com.theplug.kotori.kotoriutils.rlapi.Spells;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.client.config.*;

@ConfigGroup("dagannothhelper")
public interface DagannothHelperConfig extends Config
{
	// Sections

	@ConfigSection(
		position = 0,
		name = "Overlay Settings",
		description = "Various overlay settings."
	)
	String overlaySettings = "Overlay Settings";

	@ConfigSection(
			position = 1,
			name = "Prayer Helper",
			description = "Settings to automatically switch prayers."
	)
	String prayerHelper = "Prayer Helper";

	@ConfigSection(
			position = 2,
			name = "Gear Helper",
			description = "Settings to automatically switch gear."
	)
	String gearHelper = "Gear Helper";

	@ConfigSection(
			position = 3,
			name = "Spell Helper",
			description = "Settings for spell hotkey helpers."
	)
	String spellHelper = "Spell Helper";


	// Settings

	@ConfigItem(
		position = 0,
		keyName = "showPrayerInfoboxOverlay",
		name = "Show Prayer Infobox",
		description = "Infobox overlay with prayer to activate.",
		section = overlaySettings
	)
	default boolean showPrayerInfoboxOverlay()
	{
		return false;
	}

	@ConfigItem(
		position = 1,
		keyName = "showPrayerWidgetOverlay",
		name = "Show Prayer Widget",
		description = "Overlay prayer widget with tick timer.",
		section = overlaySettings
	)
	default boolean showPrayerWidgetOverlay()
	{
		return false;
	}

	@ConfigItem(
		position = 2,
		keyName = "showGuitarHeroOverlay",
		name = "Guitar Hero Mode",
		description = "Render \"Guitar Hero\" style prayer overlay.",
		section = overlaySettings
	)
	default boolean showGuitarHeroOverlay()
	{
		return false;
	}

	@ConfigItem(
		position = 3,
		keyName = "showNpcTickCounter",
		name = "Show Tick Counter",
		description = "Show tick counters on NPCs.",
		section = overlaySettings
	)
	default boolean showNpcTickCounter()
	{
		return false;
	}

	@ConfigItem(
		position = 4,
		keyName = "ignoringNonAttacking",
		name = "Ignore Non-Attacking NPCs",
		description = "Ignore NPCs that are not attacking you.",
		section = overlaySettings
	)
	default boolean ignoringNonAttacking()
	{
		return false;
	}




	@ConfigItem(
			position = 0,
			keyName = "autoProtectionPrayers",
			name = "Auto Protection Prayers",
			description = "Automatically pray against the Dagannoth Kings' attacks." +
					"<br>Priority is Prime, Supreme, Rex. (Magic, Ranged, Melee).",
			section = prayerHelper
	)
	default boolean autoProtectionPrayers()
	{
		return false;
	}

	@ConfigItem(
			position = 1,
			keyName = "ignoreRexProtectionPrayer",
			name = "If Only Rex, No Protection Prayer",
			description = "If Dagannoth Rex is the only king alive, then ignore him." +
					"<br>No protection prayers will be automatically turned on.",
			section = prayerHelper
	)
	default boolean ignoreRexProtectionPrayer()
	{
		return false;
	}

	@ConfigItem(
			position = 2,
			keyName = "autoOffensiveMeleePrayer",
			name = "Melee: Auto Offensive Prayer",
			description = "Automatically turn on your offensive melee prayer when you equip a melee weapon." +
					"<br>The plugin will detect the best prayer you have unlocked." +
					"<br>Piety -> Chivalry -> Ultimate Strength -> etc...",
			section = prayerHelper
	)
	default boolean autoOffensiveMeleePrayer()
	{
		return false;
	}

	@ConfigItem(
			position = 3,
			keyName = "autoOffensiveRangedPrayer",
			name = "Ranged: Auto Offensive Prayer",
			description = "Automatically turn on your offensive ranged prayer when you equip a ranged weapon." +
					"<br>The plugin will detect the best prayer you have unlocked." +
					"<br>Rigour -> Eagle Eye -> etc...",
			section = prayerHelper
	)
	default boolean autoOffensiveRangedPrayer()
	{
		return false;
	}

	@ConfigItem(
			position = 4,
			keyName = "autoOffensiveMagicPrayer",
			name = "Magic: Auto Offensive Prayer",
			description = "Automatically turn on your offensive magic prayer when you equip a magic weapon." +
					"<br>The plugin will detect the best prayer you have unlocked." +
					"<br>Augury -> Mystic Might -> etc...",
			section = prayerHelper
	)
	default boolean autoOffensiveMagicPrayer()
	{
		return false;
	}

	@ConfigItem(
			position = 5,
			keyName = "autoPreservePrayer",
			name = "Turn/Keep Preserve Prayer On",
			description = "Turn and keep the Preserve prayer on as long as you are in the lair.",
			section = prayerHelper
	)
	default boolean autoPreservePrayer()
	{
		return false;
	}





	@ConfigItem(
			position = 0,
			keyName = "meleeGearHotkey",
			name = "Melee Gear Hotkey",
			description = "Assign a hotkey. When pressed, the script will automatically equip all items listed in Melee Gear Ids.",
			section = gearHelper
	)
	default Keybind meleeGearHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			position = 1,
			keyName = "meleeGearString",
			name = "Melee Gear Ids",
			description = "Enter in the item ids to equip for killing Dagannoth Supreme." +
					"<br>The below example will mean you equip an Abyssal whip and Dragon defender." +
					"<br>Example: 4151, 12954",
			section = gearHelper
	)
	default String meleeGearString()
	{
		return "4151, 12954";
	}

	@ConfigItem(
			position = 2,
			keyName = "magicGearHotkey",
			name = "Magic Gear Hotkey",
			description = "Assign a hotkey. When pressed, the script will automatically equip all items listed in Magic Gear Ids.",
			section = gearHelper
	)
	default Keybind magicGearHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			position = 3,
			keyName = "magicGearString",
			name = "Magic Gear Ids",
			description = "Enter in the item ids to equip for killing Dagannoth Rex." +
					"<br>The below example will mean you equip an Iban's staff (u) and Mystic robe top." +
					"<br>Example: 12658, 4091",
			section = gearHelper
	)
	default String magicGearString()
	{
		return "12658, 4091";
	}

	@ConfigItem(
			position = 4,
			keyName = "rangedGearHotkey",
			name = "Ranged Gear Hotkey",
			description = "Assign a hotkey. When pressed, the script will automatically equip all items listed in Ranged Gear Ids.",
			section = gearHelper
	)
	default Keybind rangedGearHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			position = 5,
			keyName = "rangedGearString",
			name = "Ranged Gear Ids",
			description = "Enter in the item ids to equip for killing Dagannoth Prime." +
					"<br>The below example will mean you equip a Magic shortbow (i) and Rune arrows." +
					"<br>Example: 12788, 892",
			section = gearHelper
	)
	default String rangedGearString()
	{
		return "12788, 892";
	}

	@ConfigItem(
			position = 6,
			keyName = "onRexDeathGear",
			name = "On Rex Death",
			description = "On Dagannoth Rex's death, automatically equip this gear set." +
					"<br>Melee: Equip the items listed in Melee Gear Ids." +
					"<br>Magic: Equip the items listed in Magic Gear Ids." +
					"<br>Ranged: Equip the items listed in Ranged Gear Ids." +
					"<br>Off: Don't equip any items for this event.",
			section = gearHelper
	)
	default OnDeathAttackStyle onRexDeathGear()
	{
		return OnDeathAttackStyle.OFF;
	}

	@ConfigItem(
			position = 7,
			keyName = "onPrimeDeathGear",
			name = "On Prime Death",
			description = "On Dagannoth Prime's death, automatically equip this gear set." +
					"<br>Melee: Equip the items listed in Melee Gear Ids." +
					"<br>Magic: Equip the items listed in Magic Gear Ids." +
					"<br>Ranged: Equip the items listed in Ranged Gear Ids." +
					"<br>Off: Don't equip any items for this event.",
			section = gearHelper
	)
	default OnDeathAttackStyle onPrimeDeathGear()
	{
		return OnDeathAttackStyle.OFF;
	}

	@ConfigItem(
			position = 8,
			keyName = "onSupremeDeathGear",
			name = "On Supreme Death",
			description = "On Dagannoth Supreme's death, automatically equip this gear set." +
					"<br>Melee: Equip the items listed in Melee Gear Ids." +
					"<br>Magic: Equip the items listed in Magic Gear Ids." +
					"<br>Ranged: Equip the items listed in Ranged Gear Ids." +
					"<br>Off: Don't equip any items for this event.",
			section = gearHelper
	)
	default OnDeathAttackStyle onSupremeDeathGear()
	{
		return OnDeathAttackStyle.OFF;
	}





	@ConfigItem(
			position = 0,
			keyName = "spellChoice1",
			name = "Spell 1",
			description = "The spell to assign for spell hotkey 1.",
			section = spellHelper
	)
	default SpellChoices spellChoice1()
	{
		return SpellChoices.BLOOD_BARRAGE;
	}

	@ConfigItem(
			position = 1,
			keyName = "spellHotkey1",
			name = "Spell Hotkey 1",
			description = "The hotkey to left click cast spell 1.",
			section = spellHelper
	)
	default Keybind spellHotkey1()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			position = 2,
			keyName = "spellChoice2",
			name = "Spell 2",
			description = "The spell to assign for spell hotkey 2.",
			section = spellHelper
	)
	default SpellChoices spellChoice2()
	{
		return SpellChoices.ICE_BARRAGE;
	}

	@ConfigItem(
			position = 3,
			keyName = "spellHotkey2",
			name = "Spell Hotkey 2",
			description = "The hotkey to left click cast spell 2.",
			section = spellHelper
	)
	default Keybind spellHotkey2()
	{
		return Keybind.NOT_SET;
	}




	@Getter
	@RequiredArgsConstructor
	enum OnDeathAttackStyle
	{
		MELEE("Melee"),
		RANGED("Ranged"),
		MAGIC("Magic"),
		OFF("Off");

		private final String name;
	}

	@Getter
	@RequiredArgsConstructor
	enum SpellChoices
	{
		IBAN_BLAST(Spells.IBAN_BLAST),
		BLOOD_BURST(Spells.BLOOD_BURST),
		BLOOD_BLITZ(Spells.BLOOD_BLITZ),
		BLOOD_BARRAGE(Spells.BLOOD_BARRAGE),
		ICE_BURST(Spells.ICE_BURST),
		ICE_BLITZ(Spells.ICE_BLITZ),
		ICE_BARRAGE(Spells.ICE_BARRAGE);

		private final Spells spells;
	}
}
