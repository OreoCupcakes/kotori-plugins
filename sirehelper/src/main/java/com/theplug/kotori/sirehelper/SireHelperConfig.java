/*
 * Copyright (c) 2018, Andrew EP | ElPinche256 <https://github.com/ElPinche256>
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
package com.theplug.kotori.sirehelper;

import com.theplug.kotori.kotoriutils.rlapi.Spells;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup(SireHelperConfig.GROUP)
public interface SireHelperConfig extends Config
{
	String GROUP = "sirehelperconfig";

	@Getter
	@AllArgsConstructor
	enum ShadowSpells
	{
		RUSH(Spells.SHADOW_RUSH),
		BURST(Spells.SHADOW_BURST),
		BLITZ(Spells.SHADOW_BLITZ),
		BARRAGE(Spells.SHADOW_BARRAGE);

		private final Spells spell;
	}

	@Getter
	@AllArgsConstructor
	enum BloodSpells
	{
		RUSH(Spells.BLOOD_RUSH),
		BURST(Spells.BLOOD_BURST),
		BLITZ(Spells.BLOOD_BLITZ),
		BARRAGE(Spells.BLOOD_BARRAGE);

		private final Spells spell;
	}





	@ConfigSection(
			name = "Helper Features",
			description = "Settings for the plugin's automation features.",
			position = 0
	)
	String helperSection = "Helper Section";

	@ConfigItem(
			name = "Auto Dodge Miasma Pools",
			keyName = "autoDodgeMiasmaPools",
			description = "Automatically dodge the Abyssal Sire's miasma pools." +
					"<br>The direction depends on where you stand in the room.",
			position = 0,
			section = helperSection
	)
	default boolean autoDodgeMiasmaPools()
	{
		return false;
	}

	@ConfigItem(
			name = "Auto Dodge Explosion",
			keyName = "autoDodgeExplosion",
			description = "Automatically dodge the Abyssal Sire's phase 4 explosion attack and the immediate miasma pool." +
					"<br>This is separate from Auto Dodge Miasma Pools because you might want to tank the explosion for" +
					"<br>the Demonic Rebound and They Grow Up Too Fast combat achievements.",
			position = 1,
			section = helperSection
	)
	default boolean autoDodgeExplosion()
	{
		return false;
	}

	@ConfigItem(
			name = "Auto Attack After Dodging",
			keyName = "autoAttackAfterDodging",
			description = "Automatically attack the Abyssal Sire after dodging one of its special attacks.",
			position = 2,
			section = helperSection
	)
	default boolean autoAttackAfterDodging()
	{
		return false;
	}

	@ConfigItem(
			name = "Prioritize Spawn Attack Option",
			keyName = "swapSpawnMenuEntry",
			description = "Swap the Spawn's Attack menu entry to be on top." +
					"<br>This is only active during phase 2 and 3 of Sire." +
					"<br>Helpful for the They Grow Up Too Fast combat achievement.",
			position = 3,
			section = helperSection
	)
	default boolean swapSpawnMenuEntry()
	{
		return false;
	}

	@ConfigItem(
			name = "Left Click Cast Spells",
			keyName = "leftClickSpells",
			description = "Allows you to cast a shadow spell on the Abyssal Sire, when holding down the hotkey, without switching to the spell book menu.",
			position = 4,
			section = helperSection
	)
	default boolean leftClickSpells()
	{
		return false;
	}

	@ConfigItem(
			name = "Shadow Spell",
			keyName = "shadowSpellType",
			description = "The shadow spell you want to left click cast when holding down the hotkey.",
			position = 5,
			section = helperSection
	)
	default ShadowSpells shadowSpellType()
	{
		return ShadowSpells.BARRAGE;
	}

	@ConfigItem(
			name = "Shadow Spell Hotkey",
			keyName = "shadowSpellHotkey",
			description = "The hotkey to hold down, enabling the ability to cast a shadow spell without switching to the spell book menu.",
			position = 6,
			section = helperSection
	)
	default Keybind shadowSpellHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			name = "Blood Spell",
			keyName = "bloodSpellType",
			description = "The blood spell you want to left click cast when holding down the hotkey.",
			position = 7,
			section = helperSection
	)
	default BloodSpells bloodSpellType()
	{
		return BloodSpells.BARRAGE;
	}

	@ConfigItem(
			name = "Blood Spell Hotkey",
			keyName = "bloodSpellHotkey",
			description = "The hotkey to hold down, enabling the ability to cast a blood spell without switching to the spell book menu.",
			position = 8,
			section = helperSection
	)
	default Keybind bloodSpellHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			name = "Auto Gear Swap On Phase Change",
			keyName = "autoGearSwap",
			description = "Automatically swap your gear without the need to press the hotkeys." +
					"<br>Swaps to 'Phase 1 Gear Ids' when you the Abyssal Sire dies." +
					"<br>Swaps to 'Phase 2+ Gear Ids' when phase 1 ends.",
			position = 9,
			section = helperSection
	)
	default boolean autoGearSwap()
	{
		return false;
	}

	@Range(min = 1, max = 8)
	@ConfigItem(
			name = "Equips Per Tick",
			keyName = "equipsPerTick",
			description = "The number of items you want to equip per game tick when using the gear swap helper.",
			position = 10,
			section = helperSection
	)
	default int equipsPerTick()
	{
		return 3;
	}

	@ConfigItem(
			name = "Phase 1 Gear Ids",
			keyName = "phaseOneGearIds",
			description = "The item ids to equip for phase 1 of the Abyssal Sire fight." +
					"<br>This is generally your ranged or magic gear as recommended by the wiki." +
					"<br>Enter the ids in a '###,###,###' format.",
			position = 11,
			section = helperSection
	)
	default String phaseOneGearIds()
	{
		return "0,0";
	}

	@ConfigItem(
			name = "Phase 1 Gear Hotkey",
			keyName = "phaseOneGearHotkey",
			description = "The hotkey to press which will auto equip all item ids within 'Phase 1 Gear Ids'.",
			position = 12,
			section = helperSection
	)
	default Keybind phaseOneGearHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			name = "Phase 2+ Gear Ids",
			keyName = "phaseTwoPlusGearIds",
			description = "The item ids to equip for phase 2+ of the Abyssal Sire fight." +
					"<br>This is generally your melee gear as recommended by the wiki." +
					"<br>Enter the ids in a '###,###,###' format.",
			position = 13,
			section = helperSection
	)
	default String phaseTwoPlusGearIds()
	{
		return "0,0";
	}

	@ConfigItem(
			name = "Phase 2+ Gear Hotkey",
			keyName = "phaseTwoPlusHotkey",
			description = "The hotkey to press which will auto equip all items ids within 'Phase 2+ Gear Ids'.",
			position = 14,
			section = helperSection
	)
	default Keybind phaseTwoPlusHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			name = "Auto Prayers",
			keyName = "autoPrayers",
			description = "Automate protection and offensive prayers against the Abyssal Sire.",
			position = 15,
			section = helperSection
	)
	default boolean autoPrayers()
	{
		return false;
	}

	@ConfigItem(
			name = "Use Prayer On Phase 1",
			keyName = "usePrayerOnPhaseOne",
			description = "Automate offensive prayers usage for phase 1 of the Abyssal Sire." +
					"<br>Phase 1 is the stunning of the tentacles and killing the respiratory systems.",
			position = 16,
			section = helperSection
	)
	default boolean usePrayerOnPhaseOne()
	{
		return false;
	}

	@ConfigItem(
			name = "Keep Preserve Prayer On",
			keyName = "keepPreservePrayerOn",
			description = "Prayers will automatically turn off after the Abyssal Sire dies." +
					"<br>Turning this on keeps the Preserve prayer on.",
			position = 17,
			section = helperSection
	)
	default boolean keepPreservePrayerOn()
	{
		return false;
	}




	@ConfigSection(
			name = "Abyssal Sire",
			description = "Settings for the Abyssal Sire.",
			position = 1
	)
	String abyssalSireSection = "Abyssal Sire";

	@ConfigItem(
			name = "Show HP Until Phase Change",
			keyName = "showHpUntilPhaseChange",
			description = "Show the HP needed to stun or phase the Abyssal Sire." +
					"<br>This will overlay the HP on top of the Abyssal Sire's model.",
			position = 0,
			section = abyssalSireSection
	)
	default boolean showHpUntilPhaseChange()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			name = "Text Color",
			keyName = "phaseChangeTextColor",
			description = "The text color for the HP Until Phase Change overlay.",
			position = 1,
			section = abyssalSireSection
	)
	default Color phaseChangeTextColor()
	{
		return Color.WHITE;
	}

	@Range(min = 16, max = 32)
	@ConfigItem(
			name = "Text Size",
			keyName = "phaseChangeTextSize",
			description = "The text size for the HP Until Phase Change overlay.",
			position = 2,
			section = abyssalSireSection
	)
	default int phaseChangeTextSize()
	{
		return 24;
	}

	@ConfigItem(
			name = "Highlight Abyssal Sire Tile",
			keyName = "highlightAbyssalSire",
			description = "Highlight the true tile of the Abyssal Sire.",
			position = 3,
			section = abyssalSireSection
	)
	default boolean highlightAbyssalSire()
	{
		return true;
	}

	@ConfigItem(
			name = "Also Show South-West Tile",
			keyName = "showSouthWestTrueTile",
			description = "Also show the Abyssal Sire's south west true tile.",
			position = 4,
			section = abyssalSireSection
	)
	default boolean showSouthWestTrueTile()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
			name = "Border Color",
			keyName = "abyssalSireBorderColor",
			description = "The border color of the Abyssal Sire's true tile.",
			position = 5,
			section = abyssalSireSection
	)
	default Color abyssalSireBorderColor()
	{
		return Color.RED;
	}

	@Alpha
	@ConfigItem(
			name = "Fill Color",
			keyName = "abyssalSireFillColor",
			description = "The fill color of the Abyssal Sire's true tile.",
			position = 6,
			section = abyssalSireSection
	)
	default Color abyssalSireFillColor()
	{
		return new Color(255, 0, 0, 25);
	}

	@ConfigItem(
			name = "Show 'Where To Stand' Tiles",
			keyName = "showWhereToStandTiles",
			description = "Highlight the tiles of where you should stand during the fight." +
					"<br>These are the recommended tiles to stand on to dodge specials without running into the tentacles.",
			position = 7,
			section = abyssalSireSection
	)
	default boolean showWhereToStandTiles()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			name = "Border Color",
			keyName = "whereToStandBorderColor",
			description = "The border color for the 'Where to Stand' tiles.",
			position = 8,
			section = abyssalSireSection
	)
	default Color whereToStandBorderColor()
	{
		return Color.YELLOW;
	}

	@Alpha
	@ConfigItem(
			name = "Fill Color",
			keyName = "whereToStandFillColor",
			description = "The fill color for the 'Where to Stand' tiles.",
			position = 9,
			section = abyssalSireSection
	)
	default Color whereToStandFillColor()
	{
		return new Color(255, 255, 0, 25);
	}





	@ConfigSection(
			name = "Miasma Pools",
			description = "Settings for the Abyssal Sire's miasma pool attack.",
			position = 2
	)
	String miasmaPoolsSection = "Miasma Pools";

	@ConfigItem(
			name = "Highlight Miasma Pools",
			keyName = "highlightMiasmaPools",
			description = "Highlight the tile the miasma pools spawn on.",
			position = 0,
			section = miasmaPoolsSection
	)
	default boolean highlightMiasmaPools()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			name = "Border Color",
			keyName = "miasmaBorderColor",
			description = "The color of the border for highlighting miasma tiles.",
			position = 1,
			section = miasmaPoolsSection
	)
	default Color miasmaBorderColor()
	{
		return Color.GREEN;
	}

	@Alpha
	@ConfigItem(
			name = "Fill Color",
			keyName = "miasmaFillColor",
			description = "The color used to fill in the highlighted miasma tiles.",
			position = 2,
			section = miasmaPoolsSection
	)
	default Color miasmaFillColor()
	{
		return new Color(0, 255, 0, 50);
	}

	@ConfigItem(
			name = "Show Miasma Timer",
			keyName = "showMiasmaTimer",
			description = "Show the time remaining until the miasma pool despawns.",
			position = 3,
			section = miasmaPoolsSection
	)
	default boolean showMiasmaTimer()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			name = "Timer Color",
			keyName = "miasmaTimerColor",
			description = "The color used for the miasma timer text.",
			position = 4,
			section = miasmaPoolsSection
	)
	default Color miasmaTimerColor()
	{
		return new Color(255, 219, 88);
	}

	@Range(min = 16, max = 32)
	@ConfigItem(
			name = "Timer Size",
			keyName = "miasmaTimerSize",
			description = "The size of the miasma timer text.",
			position = 5,
			section = miasmaPoolsSection
	)
	default int miasmaTimerSize()
	{
		return 24;
	}





	@ConfigSection(
		name = "Spawns and Scions",
		description = "Settings for the spawns and scions of the Abyssal Sire.",
		position = 3
	)
	String spawnsScionSection = "Spawns and Scion";

	@ConfigItem(
			name = "Highlight Spawns",
			keyName = "highlightSpawns",
			description = "Highlight the Spawn that spawns.",
			position = 0,
			section = spawnsScionSection
	)
	default boolean highlightSpawns()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			name = "Border Color",
			keyName = "spawnBorderColor",
			description = "The border color for the Spawn tiles.",
			position = 1,
			section = spawnsScionSection
	)
	default Color spawnBorderColor()
	{
		return new Color(245, 155, 66);
	}

	@Alpha
	@ConfigItem(
			name = "Fill Color",
			keyName = "spawnFillColor",
			description = "The fill color for the Spawn tiles.",
			position = 2,
			section = spawnsScionSection
	)
	default Color spawnFillColor()
	{
		return new Color(245, 155, 66, 25);
	}

	@ConfigItem(
			name = "Highlight Scions",
			keyName = "highlightScions",
			description = "Highlight the Scions that evolve from Spawns.",
			position = 3,
			section = spawnsScionSection
	)
	default boolean highlightScions()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
			name = "Border Color",
			keyName = "scionBorderColor",
			description = "The border color for the Scion tiles.",
			position = 4,
			section = spawnsScionSection
	)
	default Color scionBorderColor()
	{
		return new Color(245, 155, 66);
	}

	@Alpha
	@ConfigItem(
			name = "Fill Color",
			keyName = "scionFillColor",
			description = "The fill color for the Scion tiles.",
			position = 5,
			section = spawnsScionSection
	)
	default Color scionFillColor()
	{
		return new Color(245, 155, 66, 25);
	}




	@ConfigSection(
			name = "Tentacles and Respirators",
			description = "Settings for the Abyssal Sire's tentacles and respiratory systems.",
			position = 4
	)
	String tentaclesRespiratorsSection = "Tentacles and Respirators";

	@ConfigItem(
			name = "Show Tentacle Attack Range",
			keyName = "highlightTentacles",
			description = "Highlight the tentacles true tile which is their attack range.",
			position = 0,
			section = tentaclesRespiratorsSection
	)
	default boolean highlightTentacles()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			name = "Border Color",
			keyName = "tentacleBorderColor",
			description = "The color for the border of the tentacle tiles.",
			position = 1,
			section = tentaclesRespiratorsSection
	)
	default Color tentacleBorderColor()
	{
		return Color.RED;
	}

	@Alpha
	@ConfigItem(
			name = "Fill Color",
			keyName = "tentacleFillColor",
			description = "The color for the fill of the tentacle tiles.",
			position = 2,
			section = tentaclesRespiratorsSection
	)
	default Color tentacleFillColor()
	{
		return new Color(255, 0, 0, 25);
	}

	@ConfigItem(
			name = "Show Stun Timer On Respirators",
			keyName = "showStunTimerOnRespirators",
			description = "Show how long the Abyssal Sire will still be stunned for on top of the respiratory systems." +
					"<br>If both stun timer and damage dealt is turned on, then the stun timer will be the number on the left side of the text." +
					"<br>For example: '8 : 47', 8 is the stun timer denoted in game ticks and 47 is the amount of damage you did to the vent.",
			position = 3,
			section = tentaclesRespiratorsSection
	)
	default boolean showStunTimerOnRespirators()
	{
		return true;
	}

	@ConfigItem(
			name = "Show Total Damage Dealt",
			keyName = "showTotalDamageDealtRespirators",
			description = "Show how much total damage you have currently done to the respiratory systems." +
					"<br>If both stun timer and damage dealt is turned on, then damage dealt will be the number on the right side of the text." +
					"<br>For example: '8 : 47', 8 is the stun timer denoted in game ticks and 47 is the amount of damage you did to the vent.",
			position = 4,
			section = tentaclesRespiratorsSection
	)
	default boolean showTotalDamageDealtToRespirators()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			name = "Text Color",
			keyName = "respiratorTextColor",
			description = "The color used for the respiratory stun timer and damage dealt overlay text.",
			position = 5,
			section = tentaclesRespiratorsSection
	)
	default Color respiratorTextColor()
	{
		return Color.WHITE;
	}

	@Range(min = 16, max = 32)
	@ConfigItem(
			name = "Text Size",
			keyName = "respiratorTextSize",
			description = "The font size used for the respiratory stun timer and damage dealt overlay text.",
			position = 6,
			section = tentaclesRespiratorsSection
	)
	default int respiratorTextSize()
	{
		return 24;
	}

	@ConfigItem(
			name = "Show Respiratory Safe Spots",
			keyName = "showRespiratorySafeSpots",
			description = "Show the safe spot tiles to attack the respirators for the Respiratory Runner combat achievement.",
			position = 7,
			section = tentaclesRespiratorsSection
	)
	default boolean showRespiratorySafeSpots()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			name = "Border Color",
			keyName = "safeSpotBorderColor",
			description = "The color for the border of the respiratory safe spots.",
			position = 8,
			section = tentaclesRespiratorsSection
	)
	default Color safeSpotBorderColor()
	{
		return Color.GRAY;
	}

	@Alpha
	@ConfigItem(
			name = "Fill Color",
			keyName = "safeSpotFillColor",
			description = "The color for the fill of the respiratory safe spots.",
			position = 9,
			section = tentaclesRespiratorsSection
	)
	default Color safeSpotFillColor()
	{
		return new Color(128, 128, 128, 25);
	}
}