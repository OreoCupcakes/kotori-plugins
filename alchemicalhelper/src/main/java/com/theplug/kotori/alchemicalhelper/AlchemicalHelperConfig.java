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

@ConfigGroup("alchemicalhelper")
public interface AlchemicalHelperConfig extends Config
{
	// Constants
	@Getter
	@AllArgsConstructor
	enum OffensivePrayer
	{
		PIETY(Prayer.PIETY),
		EAGLE_EYE(Prayer.EAGLE_EYE),
		RIGOUR(Prayer.RIGOUR);

		private final Prayer prayer;
	}

	// Sections

	@ConfigSection(
			name = "Overlay Settings",
			description = "Overlay settings to help deal with the boss if you don't want any automation.",
			position = 0
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
		keyName = "hydraImmunityOutline",
		name = "Show Hydra Immunity",
		description = "Overlay the hydra with a colored outline while it has immunity/not weakened.",
		position = 0,
		section = overlaySettings
	)
	default boolean hydraImmunityOutline()
	{
		return false;
	}

	@ConfigItem(
		keyName = "fountainOutline",
		name = "Show Fountain Occupancy",
		description = "Overlay fountains with a colored outline indicating if the hydra is standing on it.",
		position = 1,
		section = overlaySettings
	)
	default boolean fountainOutline()
	{
		return false;
	}

	@ConfigItem(
		keyName = "fountainTicks",
		name = "Show Fountain Ticks",
		description = "Overlay fountains with the ticks until the fountain activates.",
		position = 2,
		section = overlaySettings
	)
	default boolean fountainTicks()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
		name = "Fountain Text Color",
		description = "Adjust fountain ticks font color.",
		position = 3,
		keyName = "fountainTicksFontColor",
		section = overlaySettings
	)
	default Color fountainTicksFontColor()
	{
		return new Color(255, 255, 255, 255);
	}

	@ConfigItem(
		keyName = "showHpUntilPhaseChange",
		name = "Show HP Until Phase Change",
		description = "Overlay hydra with hp remaining until next phase change.",
		position = 4,
		section = overlaySettings
	)
	default boolean showHpUntilPhaseChange()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
		name = "HP Text Color",
		description = "Adjust font color.",
		position = 5,
		keyName = "fontColor",
		section = overlaySettings
	)
	default Color fontColor()
	{
		return new Color(255, 255, 255, 255);
	}

	@ConfigItem(
		keyName = "lightningOutline",
		name = "Highlight Lightning",
		description = "Overlay lightning tiles with a colored outline.",
		position = 6,
		section = overlaySettings
	)
	default boolean lightningOutline()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
		name = "Lightning Color",
		description = "Change the tile fill color of lightning.",
		position = 7,
		keyName = "lightningFillColor",
		section = overlaySettings
	)
	default Color lightningFillColor()
	{
		return new Color(0, 255, 255, 30);
	}

	@ConfigItem(
		keyName = "poisonOutline",
		name = "Highlight Poison Area",
		description = "Overlay poison tiles with a colored outline.",
		position = 8,
		section = overlaySettings
	)
	default boolean poisonOutline()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
		keyName = "poisonFillColor",
		name = "Poison Color",
		description = "Fill color of poison area tiles.",
		position = 9,
		section = overlaySettings
	)
	default Color poisonFillColor()
	{
		return new Color(255, 0, 0, 30);
	}

	@ConfigItem(
			keyName = "renderAttackOverlay",
			name = "Show Upcoming Attack Overlay",
			description = "Display an infobox overlay showing the upcoming attacks.",
			position = 10,
			section = overlaySettings
	)
	default boolean renderAttackOverlay()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
		keyName = "safeColor",
		name = "Safe Color",
		description = "Color indicating there are at least two hydra attacks pending.",
		position = 11,
		section = overlaySettings
	)
	default Color safeColor()
	{
		return new Color(0, 150, 0, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "warningColor",
		name = "Warning Color",
		description = "Color indicating there is one hydra attack pending.",
		position = 12,
		section = overlaySettings
	)
	default Color warningColor()
	{
		return new Color(200, 150, 0, 150);
	}

	@Alpha
	@ConfigItem(
		keyName = "dangerColor",
		name = "Danger Color",
		description = "Color indiciating the hydra will change attacks.",
		position = 13,
		section = overlaySettings
	)
	default Color dangerColor()
	{
		return new Color(150, 0, 0, 150);
	}

	@ConfigItem(
			keyName = "showPrayerOverlay",
			name = "Show Prayer Overlay",
			description = "Renders the prayer overlay over the prayer widgets.",
			position = 14,
			section = overlaySettings
	)
	default boolean showPrayerOverlay()
	{
		return false;
	}

	@ConfigItem(
			keyName = "hidePrayerOnSpecial",
			name = "Hide Prayer Overlay On Spec",
			description = "Hide prayer overlay during special attacks."
					+ "<br>This can help indicate when to save prayer points.",
			position = 15,
			section = overlaySettings
	)
	default boolean hidePrayerOnSpecial()
	{
		return false;
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
			name = "Dodge Poison Special?",
			description = "<html>Enabling this option will cause you to automatically run to the nearest safe tile during the poison special attack.<br>" +
					"The poison special attack occurs during the first and enraged phase.</html>",
			position = 0,
			section = poisonEnragedPhaseHelpers
	)
	default boolean runFromPoisonSpecial() { return false; }

	@ConfigItem(
			keyName = "favorMeleeDistancePoisonSpecial",
			name = "Melee Only: Favor Melee Dist.?",
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
}
