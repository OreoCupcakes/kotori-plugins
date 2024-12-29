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

package com.theplug.kotori.grotesqueguardians;

import java.awt.Color;
import java.awt.Font;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.config.*;

@ConfigGroup("grotesqueguardiansconfig")
public interface GrotesqueGuardiansConfig extends Config
{
	String GROUP = "grotesqueguardiansconfig";


	// Sections
	@ConfigSection(
			name = "Leagues",
			description = "Leagues - Raging Echoes Settings",
			position = 0
	)
	String leaguesSection = "Leagues";

	@ConfigSection(
			name = "Helper Settings",
			description = "Automatic helper features.",
			position = 1
	)
	String helperSection = "Helper";

	@ConfigSection(
			name = "Tick counters",
			description = "Tick counter settings.",
			position = 2
	)
	String tickCounterSection = "Tick counters";

	@ConfigSection(
			name = "Highlights",
			description = "Highlight settings.",
			position = 3
	)
	String highlightsSection = "Highlights";




	// Tick counters

	@ConfigItem(
		name = "Prayer Widget Tick Counter",
		description = "Enable prayer widget tick counter overlay.",
		position = 0,
		keyName = "prayerTickCounter",
		section = tickCounterSection
	)
	default boolean prayerTickCounter()
	{
		return true;
	}

	@ConfigItem(
		name = "Prayer Guitar Hero Mode",
		description = "Enable Guitar Hero mode for prayers.",
		position = 1,
		keyName = "guitarHeroMode",
		section = tickCounterSection
	)
	default boolean guitarHeroMode()
	{
		return false;
	}

	@ConfigItem(
			name = "Show NPC Tick Counter",
			description = "Show the tick counter overlay on Dusk and Dawn's model.",
			position = 3,
			keyName = "showNpcTickCounter",
			section = tickCounterSection
	)
	default boolean showNpcTickCounter()
	{
		return true;
	}

	/*
	@ConfigItem(
			name = "Dawn Tick Counter",
			description = "Show tick counter on Dusk.",
			position = 2,
			keyName = "dawnTickCounter",
			section = tickCounterSection
	)
	default boolean dawnTickCounter()
	{
		return true;
	}

	@ConfigItem(
		name = "Dusk Tick Counter",
		description = "Show tick counter on Dusk.",
		position = 3,
		keyName = "duskTickCounter",
		section = tickCounterSection
	)
	default boolean duskTickCounter()
	{
		return true;
	}

	 */

	@Range(
		min = 12,
		max = 64
	)
	@ConfigItem(
		name = "Font Size",
		description = "Adjust the font size of Dawn and Dusk's attack tick counter.",
		position = 4,
		keyName = "duskFontSize",
		section = tickCounterSection
	)
	@Units(Units.PIXELS)
	default int tickFontSize()
	{
		return 22;
	}

	@ConfigItem(
		name = "Font Style",
		description = "Adjust the style, Bold/Italics/Plain, of Dawn and Dusk's attack tick counter.",
		position = 5,
		keyName = "duskFontStyle",
		section = tickCounterSection
	)
	default FontStyle tickFontStyle()
	{
		return FontStyle.BOLD;
	}

	@Alpha
	@ConfigItem(
		name = "Font Color",
		description = "Color of Dawn and Dusk's attack ticker counter font.",
		position = 6,
		keyName = "duskFontColor",
		section = tickCounterSection
	)
	default Color tickFontColor()
	{
		return Color.WHITE;
	}

	
	
	
	
	
	// Outlines

	@ConfigItem(
		name = "Show NPC True Tile",
		description = "Show Dusk and Dawn's true tiles by highlighting them.",
		position = 0,
		keyName = "highlightNpcTrueTile",
		section = highlightsSection
	)
	default boolean highlightNpcTrueTile()
	{
		return true;
	}

	@ConfigItem(
			name = "Show NPC SW True Tile",
			description = "Show Dusk and Dawn's south-west true tiles by highlighting them.",
			position = 1,
			keyName = "highlightSouthWestTrueTile",
			section = highlightsSection
	)
	default boolean highlightNpcSouthWestTrueTile()
	{
		return false;
	}
	
	@Range(
		min = 1,
		max = 4
	)
	@ConfigItem(
		name = "NPC Border Width",
		description = "Change the border width of Dusk and Dawn's tile highlight.",
		position = 2,
		keyName = "npcBorderWidth",
		section = highlightsSection
	)
	@Units(Units.PIXELS)
	default int npcBorderWidth()
	{
		return 2;
	}

	@Alpha
	@ConfigItem(
			name = "NPC Border Color",
			description = "Change the border color of Dusk and Dawn's tile highlight.",
			position = 3,
			keyName = "npcBorderColor",
			section = highlightsSection
	)
	default Color npcBorderColor()
	{
		return Color.WHITE;
	}

	@Alpha
	@ConfigItem(
		name = "NPC Fill Color",
		description = "Change the fill color of Dusk and Dawn's tile highlight.",
		position = 4,
		keyName = "npcFillColor",
		section = highlightsSection
	)
	default Color npcFillColor()
	{
		return new Color(255, 255, 255, 25);
	}


	@ConfigItem(
		name = "Highlight Invulnerability",
		description = "Outline Dusk and Dawn when they are invulnerable to attacks.",
		position = 5,
		keyName = "invulnerabilityOutline",
		section = highlightsSection
	)
	default boolean invulnerabilityOutline()
	{
		return true;
	}

	@Range(
		min = 1,
		max = 10
	)
	@ConfigItem(
		name = "Invul. Border Width",
		description = "Change the border width of the highlight.",
		position = 6,
		keyName = "invulnerabilityBorderWidth",
		section = highlightsSection
	)
	@Units(Units.PIXELS)
	default int invulnerabilityBorderWidth()
	{
		return 5;
	}

	@Alpha
	@ConfigItem(
		name = "Invul. Fill Color",
		description = "Change the fill color of the highlight.",
		position = 7,
		keyName = "invulnerabilityFillColor",
		section = highlightsSection
	)
	default Color invulnerabilityFillColor()
	{
		return new Color(255, 200, 0, 255);
	}


	@ConfigItem(
			name = "Highlight Stone Orb",
			description = "Highlight the stone orb AoE attack.",
			position = 8,
			keyName = "highlightStoneOrb",
			section = highlightsSection
	)
	default boolean highlightStoneOrb()
	{
		return true;
	}

	@Range(
			min = 1,
			max = 4
	)
	@ConfigItem(
			name = "Stone Orb Border Width",
			description = "Change the border width of the stone orb outline.",
			position = 9,
			keyName = "stoneOrbBorderWidth",
			section = highlightsSection
	)
	@Units(Units.PIXELS)
	default int stoneOrbBorderWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
			name = "Stone Orb Fill Color",
			description = "Change the fill color of the stone orb outline.",
			position = 10,
			keyName = "stoneOrbFillColor",
			section = highlightsSection
	)
	default Color stoneOrbFillColor()
	{
		return new Color(211, 211, 211, 25);
	}


	@ConfigItem(
			name = "Highlight Falling Rocks",
			description = "Highlight the falling rocks AoE attack.",
			position = 11,
			keyName = "highlightFallingRocks",
			section = highlightsSection
	)
	default boolean highlightFallingRocks()
	{
		return true;
	}

	@Range(
			min = 1,
			max = 4
	)
	@ConfigItem(
			name = "Rocks Border Width",
			description = "Change the border width of the falling rocks outline.",
			position = 12,
			keyName = "fallingRocksWidth",
			section = highlightsSection
	)
	@Units(Units.PIXELS)
	default int fallingRocksWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
			name = "Rocks Fill Color",
			description = "Change the fill color of the falling rocks outline.",
			position = 13,
			keyName = "fallingRocksFillColor",
			section = highlightsSection
	)
	default Color fallingRocksFillColor()
	{
		return new Color(255, 255, 0, 25);
	}


	@ConfigItem(
			name = "Highlight Lightning",
			description = "Highlight the lightning AoE attack.",
			position = 14,
			keyName = "highlightLightning",
			section = highlightsSection
	)
	default boolean highlightLightning()
	{
		return true;
	}

	@Range(
			min = 1,
			max = 4
	)
	@ConfigItem(
			name = "Lightning Border Width",
			description = "Change the border width of the lightning outline.",
			position = 15,
			keyName = "lightningWidth",
			section = highlightsSection
	)
	@Units(Units.PIXELS)
	default int lightningWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
			name = "Lightning Fill Color",
			description = "Change the fill color of the lightning outline.",
			position = 16,
			keyName = "lightningFillColor",
			section = highlightsSection
	)
	default Color lightningFillColor()
	{
		return new Color(0, 255, 255, 25);
	}


	@ConfigItem(
			name = "Highlight Energy Sphere",
			description = "Highlight the healing energy sphere orbs.",
			position = 17,
			keyName = "highlightEnergy",
			section = highlightsSection
	)
	default boolean highlightEnergy()
	{
		return true;
	}

	@Range(
			min = 1,
			max = 4
	)
	@ConfigItem(
			name = "Energy Border Width",
			description = "Change the border width of the energy sphere outline.",
			position = 18,
			keyName = "energyWidth",
			section = highlightsSection
	)
	@Units(Units.PIXELS)
	default int energyWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
			name = "Energy Fill Color",
			description = "Change the fill color of the energy sphere outline.",
			position = 19,
			keyName = "energyFillColor",
			section = highlightsSection
	)
	default Color energyFillColor()
	{
		return new Color(0, 255, 0, 25);
	}


	@ConfigItem(
			name = "Highlight Flame Wall",
			description = "Highlight Dusk's flame wall special attack.",
			position = 20,
			keyName = "highlightFlameWall",
			section = highlightsSection
	)
	default boolean highlightFlameWall()
	{
		return true;
	}

	@Range(
			min = 1,
			max = 4
	)
	@ConfigItem(
			name = "Flame Border Width",
			description = "Change the border width of the flame wall outline.",
			position = 21,
			keyName = "flameWallBorderWidth",
			section = highlightsSection
	)
	@Units(Units.PIXELS)
	default int flameWallWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
			name = "Flame Fill Color",
			description = "Change the fill color of the flame wall outline.",
			position = 22,
			keyName = "flameWallFillColor",
			section = highlightsSection
	)
	default Color flameWallFillColor()
	{
		return new Color(138, 43, 226, 25);
	}

	@ConfigItem(
			name = "Highlight Dusk On Explosion",
			description = "Change the NPC outline and tile color when Dusk is about to do its explosion attack.",
			position = 23,
			keyName = "highlightNpcOnExplosion",
			section = highlightsSection
	)
	default boolean highlightDuskOnExplosion()
	{
		return true;
	}

	@Range(
			min = 1,
			max = 10
	)
	@ConfigItem(
			name = "Explosion Border Width",
			description = "Change Dusk's border width when highlighting the explosion attack.",
			position = 24,
			keyName = "explosionBorderWidth",
			section = highlightsSection
	)
	@Units(Units.PIXELS)
	default int explosionBorderWidth()
	{
		return 5;
	}

	@Alpha
	@ConfigItem(
			name = "Explosion Color",
			description = "Change the color of the Dusk's highlights when it does the explosion attack.",
			position = 25,
			keyName = "highlightExplosionColor",
			section = highlightsSection
	)
	default Color explosionColor()
	{
		return new Color(255, 0, 0, 255);
	}

	@ConfigItem(
			name = "Flash On Explosion",
			description = "Epilepsy warning! Flash the screen when Dusk is about to perform its eclipse explosion attack.",
			position = 26,
			keyName = "flashOnExplosion",
			section = highlightsSection
	)
	default boolean flashOnExplosion()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
			name = "Flash Color",
			description = "Change the color of the flash on explosion.",
			position = 27,
			keyName = "flashColor",
			section = highlightsSection
	)
	default Color flashColor()
	{
		return new Color(255, 0, 0, 70);
	}


	//Helper Section
	@ConfigItem(
			name = "Auto Protection Prayers",
			description = "Automatically switch protection prayers for Dawn and Dusk's attacks.",
			position = 0,
			keyName = "autoProtectionPrayers",
			section = helperSection
	)
	default boolean autoProtectionPrayers()
	{
		return false;
	}

	@ConfigItem(
			name = "Auto Offensive Prayers",
			description = "Automatically switch offensive prayers depending on your weapon type.",
			position = 1,
			keyName = "autoOffensivePrayers",
			section = helperSection
	)
	default boolean autoOffensivePrayers()
	{
		return false;
	}

	@ConfigItem(
			name = "Turn/Keep Preserve Prayer On",
			description = "Turn/keep the Preserve prayer on as long as you are in the arena.",
			position = 2,
			keyName = "keepPreservePrayerOn",
			section = helperSection
	)
	default boolean keepPreservePrayerOn()
	{
		return false;
	}

	@ConfigItem(
			name = "Dusk Gear Hotkey",
			description = "Assign a hotkey. When pressed, the script will automatically equip all items listed in Dusk Gear Ids.",
			position = 3,
			keyName = "duskGearHotkey",
			section = helperSection
	)
	default Keybind duskGearHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			name = "Dusk Gear Ids",
			description = "Enter in the item ids to equip for killing Dusk." +
					"<br>The below example will mean you equip an Abyssal whip and Dragon defender." +
					"<br>Example: 4151, 12954",
			position = 4,
			keyName = "duskGearString",
			section = helperSection
	)
	default String duskGearString()
	{
		return "4151, 12954";
	}

	@ConfigItem(
			name = "Dawn Gear Hotkey",
			description = "Assign a hotkey. When pressed, the script will automatically equip all items listed in Dawn Gear Ids.",
			position = 5,
			keyName = "dawnGearHotkey",
			section = helperSection
	)
	default Keybind dawnGearHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			name = "Dawn Gear Ids",
			description = "Enter in the item ids to equip for killing Dawn." +
					"<br>The below example will mean you equip a Magic shortbow (i) and Rune arrows." +
					"<br>Example: 12788, 892",
			position = 6,
			keyName = "dawnGearString",
			section = helperSection
	)
	default String dawnGearString()
	{
		return "12788, 892";
	}

	@ConfigItem(
			name = "Auto Equip Gear",
			description = "Automatic equip your gear as you enter new phases of the boss.",
			position = 7,
			keyName = "autoEquipGear",
			section = helperSection
	)
	default boolean autoEquipGear()
	{
		return false;
	}



	//Leagues Section
	@ConfigItem(
			name = "Killing Echo Dusk?",
			description = "Toggle on if you are killing the echo variant found in Leagues 5 - Raging Echoes." +
					"<br>This configures the AoE attack overlay as they are 1 tile in the echoed version.",
			position = 0,
			keyName = "killingEchoVariant",
			section = leaguesSection
	)
	default boolean killingEchoVariant()
	{
		return false;
	}




	// Constants

	@Getter
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
			return name;
		}
	}
}
