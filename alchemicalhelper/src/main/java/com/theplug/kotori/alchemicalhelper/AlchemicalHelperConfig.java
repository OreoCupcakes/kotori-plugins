/*
 * Copyright (c) 2019, Lucas <https://github.com/lucwousin>
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
package com.theplug.kotori.alchemicalhelper;

import java.awt.Color;
import java.awt.Font;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Prayer;
import net.runelite.client.config.*;

@ConfigGroup("alchemicalhydra")
public interface AlchemicalHelperConfig extends Config
{
	// Sections

	@ConfigSection(
			name = "<html>Alchemical Helper<br>Version 2.0.0</html>",
			description = "",
			position = -1,
			closedByDefault = true
	)
	String versionInfo = "Version";

	@ConfigSection(
			name = "Overlay Settings",
			description = "Overlay settings to help deal with the boss if you don't want any automation.",
			position = 0,
			closedByDefault = true
	)
	String overlaySettings = "Overlay Settings";

	@ConfigSection(
			name = "Prayer Helper",
			description = "Automation of prayers",
			position = 1
	)
	String automatePrayer = "Automate Prayers";

	@ConfigSection(
			name = "Poison/Enraged Phase Helper",
			description = "Automation to help deal with the unique special attacks in each phase.",
			position = 2
	)
	String poisonEnragedPhaseHelpers = "Poison/Enraged Phase Helper";

	@ConfigSection(
			name = "Lightning Phase Helper",
			description = "Automation to help deal with the unique special attacks in each phase.",
			position = 3
	)
	String lightningPhaseHelpers = "Lightning Phase Helper";

	@ConfigSection(
			name = "Flame Phase Helper",
			description = "Automation to help deal with the unique special attacks in each phase.",
			position = 4
	)
	String flamePhaseHelpers = "Flame Phase Helper";


	
	// General
	@ConfigItem(
			keyName = "hydraOverlayLine1",
			name = "-------------------------------------",
			description = "-------------------------------------",
			position = 0,
			section = overlaySettings
	)
	default Boolean hydraOverlayLine1() { return false; }
	
	@ConfigItem(
			keyName = "hydraOverlayGeneralLine",
			name = "General Overlay Settings",
			description = "General overlay settings for Alchemical Hydra.",
			position = 1,
			section = overlaySettings
	)
	default Boolean hydraOverlayGeneralLine() { return false; }
	
	@ConfigItem(
			keyName = "hydraOverlayLine2",
			name = "-------------------------------------",
			description = "-------------------------------------",
			position = 2,
			section = overlaySettings
	)
	default Boolean hydraOverlayLine2() { return false; }
	
	@ConfigItem(
		keyName = "hydraImmunityOutline",
		name = "Hydra immunity outline",
		description = "Overlay the hydra with a colored outline while it has immunity/not weakened.",
		position = 3,
		section = overlaySettings
	)
	default boolean hydraImmunityOutline()
	{
		return false;
	}

	@ConfigItem(
		keyName = "fountainOutline",
		name = "Fountain occupancy outline",
		description = "Overlay fountains with a colored outline indicating if the hydra is standing on it.",
		position = 4,
		section = overlaySettings
	)
	default boolean fountainOutline()
	{
		return false;
	}

	@ConfigItem(
		keyName = "fountainTicks",
		name = "Fountain Ticks",
		description = "Overlay fountains with the ticks until the fountain activates.",
		position = 5,
		section = overlaySettings
	)
	default boolean fountainTicks()
	{
		return false;
	}

	@ConfigItem(
		name = "Font style",
		description = "Fountain ticks Font style can be bold, plain, or italicized.",
		position = 6,
		keyName = "fountainTicksFontStyle",
		section = overlaySettings
	)
	default FontStyle fountainTicksFontStyle()
	{
		return FontStyle.BOLD;
	}

	@ConfigItem(
		name = "Font shadow",
		description = "Toggle fountain ticks font shadow.",
		position = 7,
		keyName = "fountainTicksFontShadow",
		section = overlaySettings
	)
	default boolean fountainTicksFontShadow()
	{
		return true;
	}

	@Range(
		min = 12,
		max = 64
	)
	@ConfigItem(
		name = "Font size",
		description = "Adjust fountain ticks font size.",
		position = 8,
		keyName = "fountainTicksFontSize",
		section = overlaySettings
	)
	@Units(Units.PIXELS)
	default int fountainTicksFontSize()
	{
		return 16;
	}

	@Alpha
	@ConfigItem(
		name = "Font color",
		description = "Adjust fountain ticks font color.",
		position = 9,
		keyName = "fountainTicksFontColor",
		section = overlaySettings
	)
	default Color fountainTicksFontColor()
	{
		return new Color(255, 255, 255, 255);
	}

	@Range(
		min = -100,
		max = 100
	)
	@ConfigItem(
		name = "Font zOffset",
		description = "Adjust the fountain ticks  Z coordinate offset.",
		position = 10,
		keyName = "fountainTicksFontZOffset",
		section = overlaySettings
	)
	@Units(Units.PIXELS)
	default int fountainTicksFontZOffset()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "hidePrayerOnSpecial",
		name = "Hide prayer on special attack",
		description = "Hide prayer overlay during special attacks."
			+ "<br>This can help indicate when to save prayer points.",
		position = 11,
		section = overlaySettings
	)
	default boolean hidePrayerOnSpecial()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showHpUntilPhaseChange",
		name = "Show HP until phase change",
		description = "Overlay hydra with hp remaining until next phase change.",
		position = 12,
		section = overlaySettings
	)
	default boolean showHpUntilPhaseChange()
	{
		return false;
	}

	@ConfigItem(
		name = "Font style",
		description = "Font style can be bold, plain, or italicized.",
		position = 13,
		keyName = "fontStyle",
		section = overlaySettings
	)
	default FontStyle fontStyle()
	{
		return FontStyle.BOLD;
	}

	@ConfigItem(
		name = "Font shadow",
		description = "Toggle font shadow.",
		position = 14,
		keyName = "fontShadow",
		section = overlaySettings
	)
	default boolean fontShadow()
	{
		return true;
	}

	@Range(
		min = 12,
		max = 64
	)
	@ConfigItem(
		name = "Font size",
		description = "Adjust font size.",
		position = 15,
		keyName = "fontSize",
		section = overlaySettings
	)
	@Units(Units.PIXELS)
	default int fontSize()
	{
		return 16;
	}

	@Alpha
	@ConfigItem(
		name = "Font color",
		description = "Adjust font color.",
		position = 16,
		keyName = "fontColor",
		section = overlaySettings
	)
	default Color fontColor()
	{
		return new Color(255, 255, 255, 255);
	}

	@Range(
		min = -100,
		max = 100
	)
	@ConfigItem(
		name = "Font zOffset",
		description = "Adjust the Z coordinate offset.",
		position = 17,
		keyName = "fontZOffset",
		section = overlaySettings
	)
	@Units(Units.PIXELS)
	default int fontZOffset()
	{
		return 0;
	}
	
	
	// Special Attacks
	@ConfigItem(
			name = "-------------------------------------",
			description = "-------------------------------------",
			position = 18,
			keyName = "hydraOverlayLine3",
			section = overlaySettings
	)
	default Boolean hydraOverlayLine3() { return false; }
	
	@ConfigItem(
			name = "Special Attacks Overlay Settings",
			description = "Overlay settings for Alchemical Hydra's special attacks.",
			position = 19,
			keyName = "hydraOverlaySpecialAttackLine",
			section = overlaySettings
	)
	default Boolean hydraOverlaySpecialAttackLine() { return false; }
	
	@ConfigItem(
			name = "-------------------------------------",
			description = "-------------------------------------",
			position = 20,
			keyName = "hydraOverlayLine4",
			section = overlaySettings
	)
	default Boolean hydraOverlayLine4() { return false; }
	
	@ConfigItem(
		keyName = "lightningOutline",
		name = "Lightning outline",
		description = "Overlay lightning tiles with a colored outline.",
		position = 21,
		section = overlaySettings
	)
	default boolean lightningOutline()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 8
	)
	@ConfigItem(
		name = "Outline width",
		description = "Change the stroke width of the lightning tile outline.",
		position = 22,
		keyName = "lightningStroke",
		section = overlaySettings
	)
	@Units(Units.PIXELS)
	default int lightningStroke()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		name = "Outline color",
		description = "Change the tile outline color of lightning.",
		position = 23,
		keyName = "lightningOutlineColor",
		section = overlaySettings
	)
	default Color lightningOutlineColor()
	{
		return Color.CYAN;
	}

	@Alpha
	@ConfigItem(
		name = "Outline fill color",
		description = "Change the tile fill color of lightning.",
		position = 24,
		keyName = "lightningFillColor",
		section = overlaySettings
	)
	default Color lightningFillColor()
	{
		return new Color(0, 255, 255, 30);
	}

	@ConfigItem(
		keyName = "poisonOutline",
		name = "Poison outline",
		description = "Overlay poison tiles with a colored outline.",
		position = 25,
		section = overlaySettings
	)
	default boolean poisonOutline()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 8
	)
	@ConfigItem(
		name = "Outline width",
		description = "Change the stroke width of the poison tile outline.",
		position = 26,
		keyName = "poisonStroke",
		section = overlaySettings
	)
	@Units(Units.PIXELS)
	default int poisonStroke()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		keyName = "poisonOutlineColor",
		name = "Outline color",
		description = "Outline color of poison area tiles.",
		position = 27,
		section = overlaySettings
	)
	default Color poisonOutlineColor()
	{
		return Color.RED;
	}

	@Alpha
	@ConfigItem(
		keyName = "poisonFillColor",
		name = "Outline fill color",
		description = "Fill color of poison area tiles.",
		position = 28,
		section = overlaySettings
	)
	default Color poisonFillColor()
	{
		return new Color(255, 0, 0, 30);
	}
	
	@ConfigItem(
			keyName = "hydraOverlayLine5",
			name = "-------------------------------------",
			description = "-------------------------------------",
			position = 29,
			section = overlaySettings
	)
	default Boolean hydraOverlayLine5() { return false; }
	
	// Misc
	@ConfigItem(
			keyName = "hydraOverlayMiscLine",
			name = "Misc. Overlay Settings",
			description = "Miscellaneous overlay settings for Alchemical Hydra.",
			position = 30,
			section = overlaySettings
	)
	default Boolean hydraOverlayMiscLine() { return false; }
	
	@ConfigItem(
			keyName = "hydraOverlayLine6",
			name = "-------------------------------------",
			description = "-------------------------------------",
			position = 31,
			section = overlaySettings
	)
	default Boolean hydraOverlayLine6() { return false; }
	
	@Alpha
	@ConfigItem(
		keyName = "safeColor",
		name = "Safe color",
		description = "Color indicating there are at least two hydra attacks pending.",
		position = 32,
		section = overlaySettings
	)
	default Color safeColor()
	{
		return new Color(0, 150, 0, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "warningColor",
		name = "Warning color",
		description = "Color indicating there is one hydra attack pending.",
		position = 33,
		section = overlaySettings
	)
	default Color warningColor()
	{
		return new Color(200, 150, 0, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "dangerColor",
		name = "Danger color",
		description = "Color indiciating the hydra will change attacks.",
		position = 34,
		section = overlaySettings
	)
	default Color dangerColor()
	{
		return new Color(150, 0, 0, 150);
	}
	
	
	// Automate Prayers
	
	@ConfigItem(
			keyName = "autoProtectionPrayers",
			name = "Auto Protection Prayers?",
			description = "Automate your protection prayers to pray against the Alchemical Hydra's attack.",
			position = 0,
			section = automatePrayer
	)
	default boolean autoProtectionPrayers() { return false; }
	
	@ConfigItem(
			keyName = "offensivePrayerType",
			name = "Offensive Prayer:",
			description = "What offensive prayer will you use?",
			position = 1,
			section = automatePrayer
	)
	default OffensivePrayer offensivePrayerType() { return OffensivePrayer.PIETY; }
	
	@ConfigItem(
			keyName = "autoOffensivePrayers",
			name = "Auto Offensive Prayers?",
			description = "Automate your offensive prayers to use against the Alchemical Hydra.",
			position = 2,
			section = automatePrayer
	)
	default boolean autoOffensivePrayers() { return false; }
	
	@ConfigItem(
			keyName = "autoPreservePrayer",
			name = "Turn/Keep Preserve Prayer On?",
			description = "Automatically turn on the Preserve prayer upon entering the instance and keep it on between kills.",
			position = 3,
			section = automatePrayer
	)
	default boolean autoPreservePrayer() { return false; }
	
	@ConfigItem(
			keyName = "allowManualPrayers",
			name = "Allow Manual Prayers?",
			description = "<html>If turned off, the script will have full control of prayers and will keep your selected prayers turned on each game tick." +
					"<br>Turning this option on will let you override the script on some game ticks, but won't turn off the script.</html>",
			position = 4,
			section = automatePrayer
	)
	default boolean allowManualPrayers() { return false; }

	@ConfigItem(
			keyName = "drinkPrayerPots",
			name = "Drink Prayer Potions?",
			description = "Automatically drinks prayer potion doses for you. It won't drink if Hydra is dead and you are waiting for it to respawn.",
			position = 5,
			section = automatePrayer
	)
	default boolean drinkPrayerPotions() { return false; }

	@Range(min = 1, max = 99)
	@ConfigItem(
			keyName = "prayerThreshold",
			name = "Drink Prayer Threshold",
			description = "The prayer point threshold you need to be to allow automatic drinking of prayer potions.",
			position = 6,
			section = automatePrayer
	)
	default int prayerThreshold() { return 20; }

	@ConfigItem(
			keyName = "performAttackAfterDrink",
			name = "Attack After Drinking",
			description = "Automatically attack Hydra after drinking a prayer dose.",
			position = 7,
			section = automatePrayer
	)
	default boolean performAttackAfterDrink() { return false; }



	//Poison Phase Helper
	@ConfigItem(
			keyName = "goToSafeTilePoisonSpecial",
			name = "Run To Safety During Special?",
			description = "<html>Enabling this option will cause you to automatically run to the nearest safe tile during the poison special attack.<br>" +
					"The poison special attack occurs during the first and enraged phase.</html>",
			position = 0,
			section = poisonEnragedPhaseHelpers
	)
	default boolean runFromPoisonSpecial() { return false; }

	@ConfigItem(
			keyName = "favorMeleeDistancePoisonSpecial",
			name = "Melee Only: Favor Melee Dist. Tile?",
			description = "<html>Melee only. The plugin will check your combat style. Run To Safety During Special must be on.<br>" +
					"If enabled, the algorithm will find the closest \"safe\" Melee Distance (Melee Dist.) tile from Hydra.<br>" +
					"Depending on the poison layout, the chosen tile might be too far away resulting in you taking damage.</html>",
			position = 1,
			section = poisonEnragedPhaseHelpers
	)
	default boolean favorMeleeDistanceDuringPoison() { return false; }

	@ConfigItem(
			keyName = "performAttackAfterPoison",
			name = "Attack After Poison Spec",
			description = "<html>Run to Safety During Special must be turned on for this to work.<br>" +
					"Automatically attack Hydra after reaching the determined safe tile when running from the poison special.</html>",
			position = 2,
			section = poisonEnragedPhaseHelpers
	)
	default boolean performAttackAfterPoison() { return false; }


	//Lightning Phase Helper
	@ConfigItem(
			keyName = "doLightningSkip",
			name = "Ranged Only: Lightning Skip?",
			description = "<html>If enabled, it will perform the lightning skip if you are standing on the indicated tile.<br>" +
					"Lightning skip is a ranged only mechanic. The plugin will check your combat style.</html>",
			position = 0,
			section = lightningPhaseHelpers
	)
	default boolean doLightningSkip() { return false; }

	@ConfigItem(
			keyName = "performAttackAfterLightning",
			name = "Attack After Lightning Spec",
			description = "<html>Ranged Only: Lightning Skip must be turned on for this to work.<br>" +
					"Automatically attack Hydra after performing the lightning skip mechanic.</html>",
			position = 1,
			section = lightningPhaseHelpers
	)
	default boolean performAttackAfterLightning() { return false; }

	@ConfigItem(
			keyName = "lightningSkipTileBorder",
			name = "Skip Tile Border Color",
			description = "The color of the tile border. The tile being where you need to stand for lightning skip to occur.",
			position = 2,
			section = lightningPhaseHelpers
	)
	default Color lightningSkipTileBorder() { return new Color(153, 255, 255); }

	@Alpha
	@ConfigItem(
			keyName = "lightningSkipTileFillColor",
			name = "Skip Tile Fill Color",
			description = "The color of the tile fill. The tile being where you need to stand for lightning skip to occur.",
			position = 3,
			section = lightningPhaseHelpers
	)
	default Color lightningSkipTileFill() { return new Color(153, 255, 255, 50); }


	//Flame Phase Helper
	@ConfigItem(
			keyName = "doFlameSkip",
			name = "Flame Skip?",
			description = "If enabled, it will perform the flame skip if you are standing on the indicated tile.",
			position = 0,
			section = flamePhaseHelpers
	)
	default boolean doFlameSkip() { return false; }

	@ConfigItem(
			keyName = "performAttackAfterFlame",
			name = "Attack After Flame Spec",
			description = "<html>Flame Skip must be turned on for this to work.<br>" +
					"Automatically attack Hydra after performing the flame skip mechanic.</html>",
			position = 1,
			section = flamePhaseHelpers
	)
	default boolean performAttackAfterFlame() { return false; }

	@ConfigItem(
			keyName = "flameSkipTileBorder",
			name = "Skip Tile Border Color",
			description = "The color of the tile border. The tile being where you need to stand for flame skip to occur.",
			position = 2,
			section = flamePhaseHelpers
	)
	default Color flameSkipTileBorder() { return new Color(255,204,153); }

	@Alpha
	@ConfigItem(
			keyName = "flameSkipTileFill",
			name = "Skip Tile Fill Color",
			description = "The color of the tile fill. The tile being where you need to stand for flame skip to occur.",
			position = 3,
			section = flamePhaseHelpers
	)
	default Color flameSkipTileFill() { return new Color(255, 204, 153, 50); }


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
	
	@Getter
	@AllArgsConstructor
	enum OffensivePrayer
	{
		PIETY(Prayer.PIETY),
		EAGLE_EYE(Prayer.EAGLE_EYE),
		RIGOUR(Prayer.RIGOUR);
		
		private final Prayer prayer;
	}
}
