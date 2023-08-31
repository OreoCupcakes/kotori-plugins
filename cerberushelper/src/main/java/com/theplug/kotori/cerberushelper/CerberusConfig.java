/*
 * Copyright (c) 2020 dutta64 <https://github.com/dutta64>
 * Copyright (c) 2017, Aria <aria@ar1as.space>
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * Copyright (c) 2017, Devin French <https://github.com/devinfrench>
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

package com.theplug.kotori.cerberushelper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Prayer;
import net.runelite.client.config.*;
import net.runelite.client.ui.overlay.components.ComponentOrientation;

@ConfigGroup("cerberus")
public interface CerberusConfig extends Config
{
	// Sections
	@ConfigSection(
			name = "<html>Cerberus Helper<br>Version 2.0.0</html>",
			description = "",
			position = -1,
			closedByDefault = true
	)
	String versionInfo = "Version";

	@ConfigSection(
		name = "General",
		description = "",
		position = 0
	)
	String generalSection = "Overlay";

	@ConfigSection(
		name = "Current Attack",
		description = "",
		position = 1
	)
	String currentAttackSection = "Current Attack";

	@ConfigSection(
		name = "Upcoming Attacks",
		description = "",
		position = 2
	)
	String upcomingAttacksSection = "Upcoming Attacks";

	@ConfigSection(
		name = "Guitar Hero Mode",
		description = "",
		position = 3
	)
	String guitarHeroSection = "Guitar Hero Mode";

	@ConfigSection(
			name = "Auto",
			description = "",
			position = 4
	)
	String autoSection = "Auto";

	// General Section

	@ConfigItem(
		keyName = "drawGhostTiles",
		name = "Show ghost tiles",
		description = "Overlay ghost tiles with respective colors and attack timers.",
		position = 0,
		section = generalSection
	)
	default boolean drawGhostTiles()
	{
		return false;
	}

	@ConfigItem(
		keyName = "calculateAutoAttackPrayer",
		name = "Calculate auto attack prayer",
		description = "Calculate prayer for auto attacks based on your equipment defensive bonuses."
			+ "<br>Default is Protect from Magic.",
		position = 2,
		section = generalSection
	)
	default boolean calculateAutoAttackPrayer()
	{
		return false;
	}

	// Current Attack Section

	@ConfigItem(
		keyName = "showCurrentAttack",
		name = "Show current attack",
		description = "Overlay the current attack in a separate infobox.",
		position = 0,
		section = currentAttackSection
	)
	default boolean showCurrentAttack()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showCurrentAttackTimer",
		name = "Show current attack timer",
		description = "Display a timer on the current attack infobox.",
		position = 1,
		section = currentAttackSection
	)
	default boolean showCurrentAttackTimer()
	{
		return false;
	}

	// Upcoming Attacks Section

	@ConfigItem(
		keyName = "showUpcomingAttacks",
		name = "Show upcoming attacks",
		description = "Overlay upcoming attacks in stacked info boxes.",
		position = 0,
		section = upcomingAttacksSection
	)
	default boolean showUpcomingAttacks()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 10
	)
	@ConfigItem(
		keyName = "amountOfAttacksShown",
		name = "# of attacks",
		description = "Number of upcoming attacks to render.",
		position = 1,
		section = upcomingAttacksSection
	)
	default int amountOfAttacksShown()
	{
		return 4;
	}

	/*

	@ConfigItem(
		keyName = "reverseUpcomingAttacks",
		name = "Reverse order",
		description = "Reverse the order of the upcoming attacks.",
		position = 2,
		section = upcomingAttacksSection
	)
	default boolean reverseUpcomingAttacks()
	{
		return false;
	}

	 */

	@ConfigItem(
		keyName = "showUpcomingAttackNumber",
		name = "Show attack number",
		description = "Display the attack pattern number on each upcoming attack." +
			"<br>See http://pastebin.com/hWCvantS",
		position = 3,
		section = upcomingAttacksSection
	)
	default boolean showUpcomingAttackNumber()
	{
		return false;
	}

	/*

	@ConfigItem(
		keyName = "upcomingAttacksOrientation",
		name = "Upcoming attacks orientation",
		description = "Display upcoming attacks vertically or horizontally.",
		position = 4,
		section = upcomingAttacksSection
	)
	default InfoBoxOrientation upcomingAttacksOrientation()
	{
		return InfoBoxOrientation.VERTICAL;
	}

	@ConfigItem(
		keyName = "infoBoxComponentSize",
		name = "Info box size",
		description = "Size of the upcoming attacks infoboxes.",
		position = 5,
		section = upcomingAttacksSection
	)
	default InfoBoxComponentSize infoBoxComponentSize()
	{
		return InfoBoxComponentSize.SMALL;
	}

	*/

	// Guitar Hero Mode Section

	@ConfigItem(
		keyName = "guitarHeroMode",
		name = "Guitar Hero mode",
		description = "Display descending boxes indicating the correct prayer for the current attack.",
		position = 0,
		section = guitarHeroSection
	)
	default boolean guitarHeroMode()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 10
	)
	@ConfigItem(
		keyName = "guitarHeroTicks",
		name = "# of ticks",
		description = "The number of ticks, before the upcoming current attack, to render.",
		position = 1,
		section = guitarHeroSection
	)
	default int guitarHeroTicks()
	{
		return 4;
	}

	@ConfigItem(
			keyName = "autoDefensivePrayers",
			name = "Auto Defensive Prayers",
			description = "Automatically pray against Cerberus' attacks.",
			position = 0,
			section = autoSection
	)
	default boolean autoDefensivePrayers() { return false; }

	@ConfigItem(
			keyName = "autoOffensivePrayers",
			name = "Auto Offensive Prayers",
			description = "Automatically offensive pray to fight Cerberus.",
			position = 1,
			section = autoSection
	)
	default boolean autoOffensivePrayers() { return false; }

	@ConfigItem(
			keyName = "offensivePrayerChoice",
			name = "Offensive Prayer",
			description = "Which offensive prayer to use?",
			position = 2,
			section = autoSection
	)
	default OffensivePrayers offensivePrayerChoice() { return OffensivePrayers.PIETY; }
	
	@ConfigItem(
			keyName = "keepPreservePrayerOn",
			name = "Keep Preserve Prayer On",
			description = "Keep the Preserve prayer on after a kill?",
			position = 3,
			section = autoSection
	)
	default boolean keepPreservePrayerOn() { return false; }

	@ConfigItem(
			keyName = "conservePrayerGhostSkip",
			name = "Ghost Skip? No Offensive Prayer",
			description = "Are you ghost skipping? Conserve prayer by not using an offensive<br>" +
					"prayer until the 15th attack from Cerberus or when ghosts spawn.",
			position = 4,
			section = autoSection
	)
	default boolean conservePrayerGhostSkip() { return false; }

	@ConfigItem(
			keyName = "drinkPrayerPotions",
			name = "Drink Prayer Potions?",
			description = "Do you want to automatically drink prayer potions" +
					"<br>when below the threshold you set? This will only" +
					"<br>drink prayer potions when the ghosts aren't spawned," +
					"<br>before and after Ghost phase.",
			position = 5,
			section = autoSection
	)
	default boolean drinkPrayerPotions() { return false; }

	@Range(
			min = 1,
			max = 99
	)
	@ConfigItem(
			keyName = "prayerPointsToDrinkAt",
			name = "Drink At",
			description = "Prayer point threshold to drink prayer potions.",
			position = 6,
			section = autoSection
	)
	default int prayerPointsToDrinkAt() { return 60; }
	
	@ConfigItem(
			keyName = "castDeathCharge",
			name = "Autocast Death Charge",
			description = "Autocast the spell Death Charge based on Cerberus' current HP percentage, indicated below, and if its off cooldown.",
			position = 7,
			section = autoSection
	)
	default boolean autocastDeathCharge() { return false; }
	
	@Units(Units.PERCENT)
	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "deathChargeHpPercentage",
			name = "Cast When HP <=",
			description = "The HP percentage threshold, of Cerberus, to autocast the Death Charge spell.<br>" +
					"The default 66% is slightly under 400 HP, the ghost spawn threshold.<br>" +
					"If it's below this threshold, Death Charge will be cast if it's not on cooldown.",
			position = 8,
			section = autoSection
	)
	default int deathChargeHpPercentage() { return 66; }

	// Constants

	/*

	@Getter
	@RequiredArgsConstructor
	enum InfoBoxOrientation
	{
		HORIZONTAL("Horizontal layout", ComponentOrientation.HORIZONTAL),
		VERTICAL("Vertical layout", ComponentOrientation.VERTICAL);

		private final String name;
		private final ComponentOrientation orientation;

		@Override
		public String toString()
		{
			return name;
		}
	}

	@Getter
	@RequiredArgsConstructor
	enum InfoBoxComponentSize
	{
		SMALL("Small boxes", 40), MEDIUM("Medium boxes", 60), LARGE("Large boxes", 80);

		private final String name;
		private final int size;

		@Override
		public String toString()
		{
			return name;
		}
	}

	*/

	@Getter
	@RequiredArgsConstructor
	enum OffensivePrayers
	{
		PIETY("Piety", Prayer.PIETY),
		RIGOUR("Rigour",Prayer.RIGOUR),
		EAGLE_EYE("Eagle Eye",Prayer.EAGLE_EYE);

		private final String prayerName;
		private final Prayer prayer;

		@Override
		public String toString() { return prayerName; }
	}
}
