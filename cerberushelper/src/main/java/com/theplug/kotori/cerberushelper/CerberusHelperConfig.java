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

import com.theplug.kotori.kotoriutils.rlapi.Spells;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Prayer;
import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("cerberushelper")
public interface CerberusHelperConfig extends Config
{
	// Constants
	@Getter
	@RequiredArgsConstructor
	enum OffensivePrayers
	{
		PIETY(Prayer.PIETY),
		EAGLE_EYE(Prayer.EAGLE_EYE),
		RIGOUR(Prayer.RIGOUR);

		private final Prayer prayer;
	}

	@Getter
	@RequiredArgsConstructor
	enum ProtectionPrayers
	{
		AUTO(null),
		MAGIC(Prayer.PROTECT_FROM_MAGIC),
		MELEE(Prayer.PROTECT_FROM_MELEE),
		MISSILES(Prayer.PROTECT_FROM_MISSILES);

		private final Prayer prayer;
	}

	@Getter
	@RequiredArgsConstructor
	enum Thrall
	{
		GHOST(Spells.RESURRECT_GREATER_GHOST),
		SKELETON(Spells.RESURRECT_GREATER_SKELETON),
		ZOMBIE(Spells.RESURRECT_GREATER_ZOMBIE);

		private final Spells spell;
	}

	// Sections
	@ConfigSection(
		name = "Overlay Settings",
		description = "Various overlay settings",
		position = 0
	)
	String overlaySettings = "Overlay Settings";

	@ConfigSection(
			name = "Lava Helper",
			description = "Automatic functions for the lava special attack.",
			position = 1
	)
	String lavaHelper = "Lava Helper";

	@ConfigSection(
			name = "Prayer Helper",
			description = "Automatic functions to help with prayer.",
			position = 2
	)
	String prayerHelper = "Prayer Helper";

	@ConfigSection(
			name = "Spell Helper",
			description = "Automatically cast certain spells during the fight.",
			position = 3
	)
	String spellHelper = "Spell Helper";


	// Overlay Settings
	@ConfigItem(
		keyName = "showCurrentAttack",
		name = "Show Current Attack",
		description = "Overlay the current attack in a separate infobox.",
		position = 0,
		section = overlaySettings
	)
	default boolean showCurrentAttack()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showCurrentAttackTimer",
		name = "Current Attack Timer",
		description = "Display a timer on the current attack infobox.",
		position = 1,
		section = overlaySettings
	)
	default boolean showCurrentAttackTimer()
	{
		return false;
	}

	@ConfigItem(
		keyName = "showUpcomingAttacks",
		name = "Show Upcoming Attacks",
		description = "Overlay upcoming attacks in vertically stacked info boxes.",
		position = 2,
		section = overlaySettings
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
		name = "Display Amount",
		description = "Number of upcoming attacks to render.",
		position = 3,
		section = overlaySettings
	)
	default int amountOfAttacksShown()
	{
		return 4;
	}

	@ConfigItem(
			keyName = "drawLavaTiles",
			name = "Show Lava Tiles",
			description = "Overlay the lava pool AoE splash tiles.",
			position = 4,
			section = overlaySettings
	)
	default boolean drawLavaTiles()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
			keyName = "lavaFillColor",
			name = "Lava Tile Color",
			description = "Set the color for the lava pool overlay.",
			position = 5,
			section = overlaySettings
	)
	default Color lavaFillColor()
	{
		return new Color(255,200,0,150);
	}

	@ConfigItem(
			keyName = "drawGhostTiles",
			name = "Show Ghost Tiles",
			description = "Overlay ghost tiles with respective colors and attack timers.",
			position = 6,
			section = overlaySettings
	)
	default boolean drawGhostTiles()
	{
		return false;
	}

	@ConfigItem(
			keyName = "showPrayerOverlay",
			name = "Highlight Prayer Widget",
			description = "Overlay the prayer widget of what to pray against.",
			position = 7,
			section = overlaySettings
	)
	default boolean showPrayerOverlay()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
			keyName = "meleeOverlayColor",
			name = "Melee Color",
			description = "Set the color for the melee ghost tile and prayer overlay.",
			position = 8,
			section = overlaySettings
	)
	default Color meleeOverlayColor()
	{
		return new Color(255,0,0, 150);
	}

	@Alpha
	@ConfigItem(
			keyName = "rangedOverlayColor",
			name = "Ranged Color",
			description = "Set the color for the ranged ghost tile and prayer overlay.",
			position = 9,
			section = overlaySettings
	)
	default Color rangedOverlayColor()
	{
		return new Color(0,255,0, 150);
	}

	@Alpha
	@ConfigItem(
			keyName = "magicOverlayColor",
			name = "Magic Color",
			description = "Set the color for the magic ghost tile and prayer overlay.",
			position = 10,
			section = overlaySettings
	)
	default Color magicOverlayColor()
	{
		return new Color(0,255,255, 150);
	}

	@ConfigItem(
		keyName = "guitarHeroMode",
		name = "Guitar Hero Mode",
		description = "Display descending boxes indicating the correct prayer for the current attack.",
		position = 11,
		section = overlaySettings
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
		name = "Ticks Before Render",
		description = "The number of ticks, before the upcoming current attack, to render for guitar hero mode.",
		position = 12,
		section = overlaySettings
	)
	default int guitarHeroTicks()
	{
		return 4;
	}



	@ConfigItem(
			keyName = "dodgeLavaSpec",
			name = "Dodge Lava Spec",
			description = "Automatically run away from the lava special. Plugin will calculate the closest safe tile to run too." +
					"<br>If you are using melee and are in melee distance when the special goes off, you won't be able to avoid taking some damage." +
					"<br>The closer you are to Cerberus, the quicker the damage, from the lava, gets applied.",
			position = 0,
			section = lavaHelper
	)
	default boolean dodgeLavaSpec() { return false; }

	@ConfigItem(
			keyName = "dodgePreferMelee",
			name = "Melee Only: Prefer Melee Dist.",
			description = "Melee Only: The plugin will check your combat style. Dodge Lava Spec must be turned on." +
					"<br>If enabled, the plugin will prefer running to a safe tile within melee distance (Melee Dist.) of Cerberus.",
			position = 1,
			section = lavaHelper
	)
	default boolean preferMeleeDistance() { return false; }

	@ConfigItem(
			keyName = "performAttackAfterLava",
			name = "Attack After Lava Dodge",
			description = "Dodge Lava Spec must be turned on. Automatically attack Cerberus after dodging the lava special.",
			position = 2,
			section = lavaHelper
	)
	default boolean performAttackAfterLava() { return false; }




	@ConfigItem(
			keyName = "autoProtectionPrayers",
			name = "Auto Protection Prayers",
			description = "Automatically pray against Cerberus' attacks.",
			position = 0,
			section = prayerHelper
	)
	default boolean autoDefensivePrayers() { return false; }

	@ConfigItem(
			keyName = "overrideAutoAttackCalc",
			name = "Protection Prayer",
			description = "The preferred protection prayer to use for Cerberus's auto attacks." +
					"<br>You cannot get full protection from Cerberus's auto attacks. It is random and cannot be predicted." +
					"<br><b>Auto:</b> The plugin calculates the best prayer to use depending on your equipment stats." +
					"<br><b>Magic:</b> Override the default automatic calculation and use Protect from Magic instead." +
					"<br><b>Melee:</b> Override the default automatic calculation and use Protect from Melee instead." +
					"<br><b>Missiles:</b> Override the default automatic calculation and use Protect from Missiles instead.",
			position = 1,
			section = prayerHelper
	)
	default ProtectionPrayers overrideAutoAttackCalc() { return ProtectionPrayers.AUTO; }

	@ConfigItem(
			keyName = "autoOffensivePrayers",
			name = "Auto Offensive Prayers",
			description = "Automatically offensive pray to fight Cerberus.",
			position = 2,
			section = prayerHelper
	)
	default boolean autoOffensivePrayers() { return false; }

	@ConfigItem(
			keyName = "offensivePrayerChoice",
			name = "Offensive Prayer",
			description = "Which offensive prayer to use?",
			position = 3,
			section = prayerHelper
	)
	default OffensivePrayers offensivePrayerChoice() { return OffensivePrayers.PIETY; }

	@ConfigItem(
			keyName = "keepPreservePrayerOn",
			name = "Turn/Keep Preserve Prayer On",
			description = "Turn/keep the Preserve prayer on as long as you are in the arena.",
			position = 4,
			section = prayerHelper
	)
	default boolean keepPreservePrayerOn() { return false; }

	@ConfigItem(
			keyName = "conservePrayerGhostSkip",
			name = "Ghost Skip? No Offensive Prayer",
			description = "Are you ghost skipping? Conserve prayer by not using an offensive<br>" +
					"prayer until the 15th attack from Cerberus or when ghosts spawn.",
			position = 5,
			section = prayerHelper
	)
	default boolean conservePrayerGhostSkip() { return false; }

	@ConfigItem(
			keyName = "drinkPrayerPotions",
			name = "Drink Prayer Potions?",
			description = "Do you want to automatically drink prayer potions" +
					"<br>when below the threshold you set? This will only" +
					"<br>drink prayer potions when the ghosts aren't spawned," +
					"<br>before and after Ghost phase.",
			position = 6,
			section = prayerHelper
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
			position = 7,
			section = prayerHelper
	)
	default int prayerPointsToDrinkAt() { return 60; }

	@ConfigItem(
			keyName = "performAttackAfterDrinkingPrayer",
			name = "Attack After Drinking",
			description = "Automatically attack Cerberus after drinking prayer doses and restoring your prayer above your selected threshold.",
			position = 8,
			section = prayerHelper
	)
	default boolean performAttackAfterDrinkingPrayer() { return false; }


	
	@ConfigItem(
			keyName = "castDeathCharge",
			name = "Autocast Death Charge",
			description = "Autocast the spell Death Charge based on Cerberus' current HP percentage, indicated below, and if its off cooldown.",
			position = 0,
			section = spellHelper
	)
	default boolean autoCastDeathCharge() { return false; }
	
	@Units(Units.PERCENT)
	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "deathChargeHpPercentage",
			name = "Cast When HP <=",
			description = "The HP percentage threshold, of Cerberus, to autocast the Death Charge spell.<br>" +
					"The default 66% is slightly under 400 HP, the ghost spawn threshold.<br>" +
					"If it's below this threshold, Death Charge will be cast if it's not on cooldown.",
			position = 1,
			section = spellHelper
	)
	default int deathChargeHpPercentage() { return 66; }

	@ConfigItem(
			keyName = "summonThrall",
			name = "Autocast Thralls",
			description = "Autocast one of the greater thrall spells.",
			position = 2,
			section = spellHelper
	)
	default boolean autoCastGreaterThrall() { return false; }

	@ConfigItem(
			keyName = "thrallType",
			name = "Thrall Type",
			description = "The thrall to summon.",
			position = 3,
			section = spellHelper
	)
	default Thrall thrallType() { return Thrall.GHOST; }

	@ConfigItem(
			keyName = "castDemonicOffering",
			name = "Autocast Demonic Offering",
			description = "Autocast demonic offering once you have three infernal ashes in your inventory.",
			position = 4,
			section = spellHelper
	)
	default boolean autoCastDemonicOffering() { return false; }

	@Range(min = 1, max = 3)
	@ConfigItem(
			keyName = "demonicOfferingAmount",
			name = "Ashes to Offer",
			description = "How many ashes are needed before casting Demonic Offering?" +
					"<br>The spell takes up to three ashes in exchange for XP and prayer points." +
					"<br>Two prayer points are restored for each infernal ash." +
					"<br>This lets you decide if two prayer points is worth the cost of a soul and wrath rune.",
			position = 5,
			section = spellHelper
	)
	default int demonicOfferingAmount() { return 3; }
}
