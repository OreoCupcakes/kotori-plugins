/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2023, rdutta <https://github.com/rdutta>
 * Copyright (c) 2019, kThisIsCvpv <https://github.com/kThisIsCvpv>
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * Copyright (c) 2019, kyle <https://github.com/Kyleeld>
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

package com.theplug.kotori.gauntlethelper;

import com.theplug.kotori.gauntlethelper.module.maze.Resource;

import java.awt.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup("gauntlethelper")
public interface GauntletHelperConfig extends Config
{
	// Sections
	@ConfigSection(
		name = "Resource Tracking",
		description = "Resource tracking section.",
		position = 0,
		closedByDefault = true
	)
	String resourceTrackingSection = "resourceTracking";

	@ConfigSection(
		name = "Resource Overlay",
		description = "Resource overlay section.",
		position = 1,
		closedByDefault = true
	)
	String resourceOverlaySection = "resourceOverlay";

	@ConfigSection(
		name = "Utilities",
		description = "Utilities section.",
		position = 2,
		closedByDefault = true
	)
	String utilitiesSection = "utilities";

	@ConfigSection(
		name = "Npcs",
		description = "Npcs section.",
		position = 3,
		closedByDefault = true
	)
	String npcsSection = "npcs";

	@ConfigSection(
		name = "Hunllef",
		description = "Hunllef section.",
		position = 4,
		closedByDefault = true
	)
	String hunllefSection = "hunllef";

	@ConfigSection(
			name = "Tornadoes",
			description = "Tornadoes section.",
			position = 5,
			closedByDefault = true
	)
	String tornadoesSection = "tornadoes";

	@ConfigSection(
			name = "Player",
			description = "Player section.",
			position = 6,
			closedByDefault = true
	)
	String playerSection = "player";

	@ConfigSection(
		name = "Timer",
		description = "Timer section.",
		position = 7,
		closedByDefault = true
	)
	String timerSection = "timer";

	@ConfigSection(
			name = "Helpers",
			description = "Helper section.",
			position = 8,
			closedByDefault = false
	)
	String helperSection = "helper";


	// Resource Tracking

	@ConfigItem(
		name = "Track Resources",
		description = "Track resources in counter infoboxes.",
		position = 0,
		keyName = "resourceTracker",
		section = resourceTrackingSection
	)
	default boolean resourceTracker()
	{
		return false;
	}

	@ConfigItem(
		name = "Tracking Mode",
		description = "Increment or decrement resource counters." +
			"<br>Disable a counter by setting value to 0.",
		position = 1,
		keyName = "resourceTrackingMode",
		section = resourceTrackingSection
	)
	default TrackingMode resourceTrackingMode()
	{
		return TrackingMode.DECREMENT;
	}

	@ConfigItem(
		name = "Remove Acquired Resources",
		description = "Remove counters when acquired amount reached.",
		position = 2,
		keyName = "resourceRemoveAcquired",
		section = resourceTrackingSection
	)
	default boolean resourceRemoveAcquired()
	{
		return false;
	}

	@ConfigItem(
		name = "Ore",
		description = "The desired number of ores to acquire.",
		position = 3,
		keyName = "resourceOre",
		section = resourceTrackingSection
	)
	@ResourceCount(normal = Resource.CRYSTAL_ORE, corrupted = Resource.CORRUPTED_ORE)
	default int resourceOre()
	{
		return 3;
	}

	@ConfigItem(
		name = "Phren Bark",
		description = "The desired number of phren barks to acquire.",
		position = 4,
		keyName = "resourceBark",
		section = resourceTrackingSection
	)
	@ResourceCount(normal = Resource.PHREN_BARK, corrupted = Resource.CORRUPTED_PHREN_BARK)
	default int resourceBark()
	{
		return 3;
	}

	@ConfigItem(
		name = "Linum Tirinum",
		description = "The desired number of linum tirinums to acquire.",
		position = 5,
		keyName = "resourceTirinum",
		section = resourceTrackingSection
	)
	@ResourceCount(normal = Resource.LINUM_TIRINUM, corrupted = Resource.CORRUPTED_LINUM_TIRINUM)
	default int resourceTirinum()
	{
		return 3;
	}

	@ConfigItem(
		name = "Grym Leaf",
		description = "The desired number of grym leaves to acquire.",
		position = 6,
		keyName = "resourceGrym",
		section = resourceTrackingSection
	)
	@ResourceCount(normal = Resource.GRYM_LEAF, corrupted = Resource.CORRUPTED_GRYM_LEAF)
	default int resourceGrym()
	{
		return 2;
	}

	@ConfigItem(
		name = "Weapon Frames",
		description = "The desired number of weapon frames to acquire.",
		position = 7,
		keyName = "resourceFrame",
		section = resourceTrackingSection
	)
	@ResourceCount(normal = Resource.WEAPON_FRAME, corrupted = Resource.CORRUPTED_WEAPON_FRAME)
	default int resourceFrame()
	{
		return 2;
	}

	@ConfigItem(
		name = "Paddlefish",
		description = "The desired number of paddlefish to acquire.",
		position = 8,
		keyName = "resourcePaddlefish",
		section = resourceTrackingSection
	)
	@ResourceCount(normal = Resource.RAW_PADDLEFISH, corrupted = Resource.RAW_PADDLEFISH)
	default int resourcePaddlefish()
	{
		return 20;
	}

	@ConfigItem(
		name = "Crystal Shards",
		description = "The desired number of crystal shards to acquire.",
		position = 9,
		keyName = "resourceShard",
		section = resourceTrackingSection
	)
	@ResourceCount(normal = Resource.CRYSTAL_SHARDS, corrupted = Resource.CORRUPTED_SHARDS)
	default int resourceShard()
	{
		return 320;
	}

	@ConfigItem(
		name = "Bowstring",
		description = "Whether or not to acquire the crystalline or corrupted bowstring.",
		position = 10,
		keyName = "resourceBowstring",
		section = resourceTrackingSection
	)
	@ResourceCount(normal = Resource.CRYSTALLINE_BOWSTRING, corrupted = Resource.CORRUPTED_BOWSTRING)
	default boolean resourceBowstring()
	{
		return false;
	}

	@ConfigItem(
		name = "Spike",
		description = "Whether or not to acquire the crystal or corrupted spike.",
		position = 11,
		keyName = "resourceSpike",
		section = resourceTrackingSection
	)
	@ResourceCount(normal = Resource.CRYSTAL_SPIKE, corrupted = Resource.CORRUPTED_SPIKE)
	default boolean resourceSpike()
	{
		return false;
	}

	@ConfigItem(
		name = "Orb",
		description = "Whether or not to acquire the crystal or corrupted orb.",
		position = 12,
		keyName = "resourceOrb",
		section = resourceTrackingSection
	)
	@ResourceCount(normal = Resource.CRYSTAL_ORB, corrupted = Resource.CORRUPTED_ORB)
	default boolean resourceOrb()
	{
		return false;
	}

	// Resource Overlay Section

	@ConfigItem(
		name = "Overlay Resources",
		description = "Toggle enabling/disabling resource overlays.",
		position = 0,
		keyName = "overlayResources",
		section = resourceOverlaySection
	)
	default boolean overlayResources()
	{
		return false;
	}

	@ConfigItem(
		name = "Ore Deposit",
		description = "Toggle overlaying ore deposits.",
		position = 1,
		keyName = "overlayOreDeposit",
		section = resourceOverlaySection
	)
	default boolean overlayOreDeposit()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		name = "Ore Outline Color",
		description = "Change the outline color of ore deposits.",
		position = 2,
		keyName = "oreDepositOutlineColor",
		section = resourceOverlaySection
	)
	default Color oreDepositOutlineColor()
	{
		return new Color(255, 0, 0, 255);
	}

	@Alpha
	@ConfigItem(
		name = "Ore Fill Color",
		description = "Change the fill color of ore deposits.",
		position = 3,
		keyName = "oreDepositFillColor",
		section = resourceOverlaySection
	)
	default Color oreDepositFillColor()
	{
		return new Color(255, 0, 0, 50);
	}

	@ConfigItem(
		name = "Phren Roots",
		description = "Toggle overlaying phren roots.",
		position = 4,
		keyName = "overlayPhrenRoots",
		section = resourceOverlaySection
	)
	default boolean overlayPhrenRoots()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		name = "Phren Outline Color",
		description = "Change the outline color of phren roots.",
		position = 5,
		keyName = "phrenRootsOutlineColor",
		section = resourceOverlaySection
	)
	default Color phrenRootsOutlineColor()
	{
		return new Color(0, 255, 0, 255);
	}

	@Alpha
	@ConfigItem(
		name = "Phren Fill Color",
		description = "Change the fill color of phren roots.",
		position = 6,
		keyName = "phrenRootsFillColor",
		section = resourceOverlaySection
	)
	default Color phrenRootsFillColor()
	{
		return new Color(0, 255, 0, 50);
	}

	@ConfigItem(
		name = "Linum Tirinum",
		description = "Toggle overlaying linum tirinum.",
		position = 7,
		keyName = "overlayLinumTirinum",
		section = resourceOverlaySection
	)
	default boolean overlayLinumTirinum()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		name = "Linum Outline Color",
		description = "Change the outline color of linum tirinum.",
		position = 8,
		keyName = "linumTirinumOutlineColor",
		section = resourceOverlaySection
	)
	default Color linumTirinumOutlineColor()
	{
		return new Color(255, 255, 255, 255);
	}

	@Alpha
	@ConfigItem(
		name = "Linum Fill Color",
		description = "Change the fill color of linum tirinum.",
		position = 9,
		keyName = "linumTirinumFillColor",
		section = resourceOverlaySection
	)
	default Color linumTirinumFillColor()
	{
		return new Color(255, 255, 255, 50);
	}

	@ConfigItem(
		name = "Grym Root",
		description = "Toggle overlaying grym roots.",
		position = 10,
		keyName = "overlayGrymRoot",
		section = resourceOverlaySection
	)
	default boolean overlayGrymRoot()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		name = "Grym Outline Color",
		description = "Change the outline color of grym roots.",
		position = 11,
		keyName = "grymRootOutlineColor",
		section = resourceOverlaySection
	)
	default Color grymRootOutlineColor()
	{
		return new Color(255, 255, 0, 255);
	}

	@Alpha
	@ConfigItem(
		name = "Grym Fill Color",
		description = "Change the fill color of grym roots.",
		position = 12,
		keyName = "grymRootFillColor",
		section = resourceOverlaySection
	)
	default Color grymRootFillColor()
	{
		return new Color(255, 255, 0, 50);
	}

	@ConfigItem(
		name = "Fishing Spot",
		description = "Toggle overlaying fishing spots.",
		position = 13,
		keyName = "overlayFishingSpot",
		section = resourceOverlaySection
	)
	default boolean overlayFishingSpot()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
		name = "Fishing Outline Color",
		description = "Change the outline color of fishing spots.",
		position = 14,
		keyName = "fishingSpotOutlineColor",
		section = resourceOverlaySection
	)
	default Color fishingSpotOutlineColor()
	{
		return new Color(0, 255, 255, 255);
	}

	@Alpha
	@ConfigItem(
		name = "Fishing Fill Color",
		description = "Change the fill color of fishing spots.",
		position = 15,
		keyName = "fishingSpotFillColor",
		section = resourceOverlaySection
	)
	default Color fishingSpotFillColor()
	{
		return new Color(0, 255, 255, 50);
	}

	@Range(
		max = 64
	)
	@ConfigItem(
		name = "Icon Size",
		description = "Change the size of the resource icons.<br>0px width = disabled",
		position = 16,
		keyName = "resourceIconSize",
		section = resourceOverlaySection
	)
	@Units(Units.PIXELS)
	default int resourceIconSize()
	{
		return 14;
	}

	@Range(
		max = 2
	)
	@ConfigItem(
		name = "Hull Outline Width",
		description = "Change the width of the resource hull outline.<br>0px width = disabled",
		position = 17,
		keyName = "resourceHullOutlineWidth",
		section = resourceOverlaySection
	)
	@Units(Units.PIXELS)
	default int resourceHullOutlineWidth()
	{
		return 1;
	}

	@Range(
		max = 2
	)
	@ConfigItem(
		name = "Tile Outline Width",
		description = "Change the width of the resource tile outline.<br>0px width = disabled",
		position = 18,
		keyName = "resourceTileOutlineWidth",
		section = resourceOverlaySection
	)
	@Units(Units.PIXELS)
	default int resourceTileOutlineWidth()
	{
		return 1;
	}

	@ConfigItem(
		name = "Minimap Overlays",
		description = "Overlay the minimap with icons for resources.",
		position = 19,
		keyName = "minimapResourceOverlay",
		section = resourceOverlaySection
	)
	default boolean minimapResourceOverlay()
	{
		return false;
	}

	@ConfigItem(
		name = "Dynamically Remove Overlays",
		description = "Remove overlays for acquired tracked resources." +
			"<br>Disabled if incrementally tracking resources.",
		position = 20,
		keyName = "resourceRemoveOutlineOnceAcquired",
		section = resourceOverlaySection
	)
	default boolean resourceRemoveOutlineOnceAcquired()
	{
		return false;
	}

	// Utilities Section

	@ConfigItem(
		name = "Outline Starting Room Utilities",
		description = "Outline various utilities in the starting room.",
		position = 0,
		keyName = "utilitiesOutline",
		section = utilitiesSection
	)
	default boolean utilitiesOutline()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 2
	)
	@ConfigItem(
		name = "Outline Width",
		description = "Change the width of the utilities outline.",
		position = 1,
		keyName = "utilitiesOutlineWidth",
		section = utilitiesSection
	)
	@Units(Units.PIXELS)
	default int utilitiesOutlineWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		name = "Outline Color",
		description = "Change the color of the utilities outline.",
		position = 2,
		keyName = "utilitiesOutlineColor",
		section = utilitiesSection
	)
	default Color utilitiesOutlineColor()
	{
		return Color.MAGENTA;
	}

	@ConfigItem(
		name = "No Entry With Uncooked Fish",
		description = "Removes \"Pass\" and \"Quick-Pass\" while carrying uncooked paddlefish.",
		position = 3,
		keyName = "utilitiesFishCheck",
		section = utilitiesSection
	)
	default boolean utilitiesFishCheck()
	{
		return false;
	}


	// Hunllef Section

	@ConfigItem(
			name = "Display Counter On Hunllef",
			description = "Overlay the Hunllef with an attack and prayer counter.",
			position = 0,
			keyName = "hunllefOverlayAttackCounter",
			section = hunllefSection
	)
	default boolean hunllefOverlayAttackCounter()
	{
		return false;
	}

	@ConfigItem(
			name = "Counter Font Style",
			description = "Change the font style of the attack and prayer counter.",
			position = 1,
			keyName = "hunllefAttackCounterFontStyle",
			section = hunllefSection
	)
	default FontStyle hunllefAttackCounterFontStyle()
	{
		return FontStyle.BOLD;
	}

	@Range(
			min = 12,
			max = 64
	)
	@ConfigItem(
			name = "Counter Font Size",
			description = "Adjust the font size of the attack and prayer counter.",
			position = 2,
			keyName = "hunllefAttackCounterFontSize",
			section = hunllefSection
	)
	@Units(Units.PIXELS)
	default int hunllefAttackCounterFontSize()
	{
		return 22;
	}

	@ConfigItem(
			name = "Outline Hunllef On Wrong Prayer",
			description = "Outline the Hunllef when incorrectly praying against its current attack style.",
			position = 3,
			keyName = "hunllefOverlayWrongPrayerOutline",
			section = hunllefSection
	)
	default boolean hunllefOverlayWrongPrayerOutline()
	{
		return false;
	}

	@Range(
			min = 2,
			max = 12
	)
	@ConfigItem(
			name = "Outline Width",
			description = "Change the width of the wrong prayer outline.",
			position = 4,
			keyName = "hunllefWrongPrayerOutlineWidth",
			section = hunllefSection
	)
	@Units(Units.PIXELS)
	default int hunllefWrongPrayerOutlineWidth()
	{
		return 4;
	}

	@ConfigItem(
		name = "Outline Hunllef Tile",
		description = "Outline the Hunllef's tile.",
		position = 5,
		keyName = "hunllefTileOutline",
		section = hunllefSection
	)
	default boolean hunllefTileOutline()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 2
	)
	@ConfigItem(
		name = "Tile Outline Width",
		description = "Change the width of the Hunllef's tile outline.",
		position = 6,
		keyName = "hunllefTileOutlineWidth",
		section = hunllefSection
	)
	@Units(Units.PIXELS)
	default int hunllefTileOutlineWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		name = "Tile Outline Color",
		description = "Change the outline color of the Hunllef's tile.",
		position = 7,
		keyName = "hunllefOutlineColor",
		section = hunllefSection
	)
	default Color hunllefOutlineColor()
	{
		return Color.WHITE;
	}

	@Alpha
	@ConfigItem(
		name = "Tile Fill Color",
		description = "Change the fill color of the Hunllef's tile.",
		position = 8,
		keyName = "hunllefFillColor",
		section = hunllefSection
	)
	default Color hunllefFillColor()
	{
		return new Color(255, 255, 255, 0);
	}

	@ConfigItem(
			name = "Play Audio On Prayer Attack",
			description = "Play an in-game sound when the Hunllef is about to use its prayer attack.",
			position = 9,
			keyName = "hunllefPrayerAudio",
			section = hunllefSection
	)
	default boolean hunllefPrayerAudio()
	{
		return false;
	}

	//Tornadoes
	@ConfigItem(
			name = "Overlay Tornado Tick Counter",
			description = "Overlay tornadoes with a tick counter.",
			position = 0,
			keyName = "tornadoTickCounter",
			section = tornadoesSection
	)
	default boolean tornadoTickCounter()
	{
		return false;
	}

	@ConfigItem(
			name = "Font Style",
			description = "Bold/Italics/Plain",
			position = 1,
			keyName = "tornadoFontStyle",
			section = tornadoesSection
	)
	default FontStyle tornadoFontStyle()
	{
		return FontStyle.BOLD;
	}

	@ConfigItem(
			name = "Font Shadow",
			description = "Toggle font shadow of the tornado tick counter.",
			position = 2,
			keyName = "tornadoFontShadow",
			section = tornadoesSection
	)
	default boolean tornadoFontShadow()
	{
		return true;
	}

	@Range(
			min = 12,
			max = 64
	)
	@ConfigItem(
			name = "Font Size",
			description = "Adjust the font size of the tornado tick counter.",
			position = 3,
			keyName = "tornadoFontSize",
			section = tornadoesSection
	)
	@Units(Units.PIXELS)
	default int tornadoFontSize()
	{
		return 16;
	}

	@Alpha
	@ConfigItem(
			name = "Font Color",
			description = "Color of the tornado tick counter font.",
			position = 4,
			keyName = "tornadoFontColor",
			section = tornadoesSection
	)
	default Color tornadoFontColor()
	{
		return Color.WHITE;
	}

	@ConfigItem(
		name = "Outline Tornado Tile",
		description = "Outline the tiles of tornadoes.",
		position = 5,
		keyName = "tornadoTileOutline",
		section = tornadoesSection
	)
	default TileOutline tornadoTileOutline()
	{
		return TileOutline.OFF;
	}

	@Range(
		min = 1,
		max = 2
	)
	@ConfigItem(
		name = "Tile Outline Width",
		description = "Change tile outline width of tornadoes.",
		position = 6,
		keyName = "tornadoTileOutlineWidth",
		section = tornadoesSection
	)
	@Units(Units.PIXELS)
	default int tornadoTileOutlineWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		name = "Tile Outline Color",
		description = "Color to outline the tile of a tornado.",
		position = 7,
		keyName = "tornadoOutlineColor",
		section = tornadoesSection
	)
	default Color tornadoOutlineColor()
	{
		return Color.YELLOW;
	}

	@Alpha
	@ConfigItem(
		name = "Tile Fill Color",
		description = "Color to fill the tile of a tornado.",
		position = 8,
		keyName = "tornadoFillColor",
		section = tornadoesSection
	)
	default Color tornadoFillColor()
	{
		return new Color(255, 255, 0, 50);
	}

	// Npcs Section

	@ConfigItem(
		name = "Outline Demi-bosses",
		description = "Overlay demi-bosses with a colored outline.",
		position = 0,
		keyName = "demibossOutline",
		section = npcsSection
	)
	default boolean demibossOutline()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 2
	)
	@ConfigItem(
		name = "Outline Width",
		description = "Change the width of the demi-boss outline.",
		position = 1,
		keyName = "demibossOutlineWidth",
		section = npcsSection
	)
	@Units(Units.PIXELS)
	default int demibossOutlineWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		name = "Dark Beast Color",
		description = "Change the outline color of dark beasts.",
		position = 2,
		keyName = "darkBeastOutlineColor",
		section = npcsSection
	)
	default Color darkBeastOutlineColor()
	{
		return Color.GREEN;
	}

	@Alpha
	@ConfigItem(
		name = "Dragon Color",
		description = "Change the outline color of dragons.",
		position = 3,
		keyName = "dragonOutlineColor",
		section = npcsSection
	)
	default Color dragonOutlineColor()
	{
		return Color.BLUE;
	}

	@Alpha
	@ConfigItem(
		name = "Bear Color",
		description = "Change the outline color of bears.",
		position = 4,
		keyName = "bearOutlineColor",
		section = npcsSection
	)
	default Color bearOutlineColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		name = "Outline Strong Npcs",
		description = "Overlay strong npcs with a colored outline.",
		position = 5,
		keyName = "strongNpcOutline",
		section = npcsSection
	)
	default boolean strongNpcOutline()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 2
	)
	@ConfigItem(
		name = "Outline Width",
		description = "Change the width of the strong npcs outline.",
		position = 6,
		keyName = "strongNpcOutlineWidth",
		section = npcsSection
	)
	@Units(Units.PIXELS)
	default int strongNpcOutlineWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		name = "Outline Color",
		description = "Change the outline color of strong npcs.",
		position = 7,
		keyName = "strongNpcOutlineColor",
		section = npcsSection
	)
	default Color strongNpcOutlineColor()
	{
		return Color.ORANGE;
	}

	@ConfigItem(
		name = "Outline Weak Npcs",
		description = "Overlay weak npcs with a colored outline.",
		position = 8,
		keyName = "weakNpcOutline",
		section = npcsSection
	)
	default boolean weakNpcOutline()
	{
		return false;
	}

	@Range(
		min = 1,
		max = 2
	)
	@ConfigItem(
		name = "Outline Width",
		description = "Change the width of the weak npcs outline.",
		position = 9,
		keyName = "weakNpcOutlineWidth",
		section = npcsSection
	)
	@Units(Units.PIXELS)
	default int weakNpcOutlineWidth()
	{
		return 1;
	}

	@Alpha
	@ConfigItem(
		name = "Outline Color",
		description = "Change the outline color of weak npcs.",
		position = 10,
		keyName = "weakNpcOutlineColor",
		section = npcsSection
	)
	default Color weakNpcOutlineColor()
	{
		return Color.ORANGE;
	}

	@ConfigItem(
		name = "Minimap Overlays",
		description = "Overlay the minimap with icons for demi-bosses.",
		position = 11,
		keyName = "minimapDemibossOverlay",
		section = npcsSection
	)
	default boolean minimapDemibossOverlay()
	{
		return false;
	}

	// Player Section
	@ConfigItem(
			name = "Overlay Prayer",
			description = "Overlay the correct prayer to use against the Hunllef's current attack style.",
			position = 0,
			keyName = "prayerOverlay",
			section = playerSection
	)
	default PrayerHighlightMode prayerOverlay()
	{
		return PrayerHighlightMode.NONE;
	}

	@ConfigItem(
			name = "Flash On Wrong Attack Style",
			description = "Flash the screen if you use the wrong attack style.",
			position = 1,
			keyName = "flashOnWrongAttack",
			section = playerSection
	)
	default boolean flashOnWrongAttack()
	{
		return false;
	}

	@Range(
			min = 10,
			max = 50
	)
	@ConfigItem(
			name = "Flash Duration",
			description = "Change the duration of the flash.",
			position = 2,
			keyName = "flashOnWrongAttackDuration",
			section = playerSection
	)
	default int flashOnWrongAttackDuration()
	{
		return 25;
	}

	@Alpha
	@ConfigItem(
			name = "Flash Color",
			description = "Color of the flash notification.",
			position = 3,
			keyName = "flashOnWrongAttackColor",
			section = playerSection
	)
	default Color flashOnWrongAttackColor()
	{
		return new Color(255, 0, 0, 70);
	}

	@ConfigItem(
			name = "Flash On 5:1 Method",
			description = "Flash the screen to weapon switch when using 5:1 method.",
			position = 4,
			keyName = "flashOn51Method",
			section = playerSection
	)
	default boolean flashOn51Method()
	{
		return false;
	}

	@Range(
			min = 10,
			max = 50
	)
	@ConfigItem(
			name = "Flash Duration",
			description = "Change the duration of the flash.",
			position = 5,
			keyName = "flashOn51MethodDuration",
			section = playerSection
	)
	default int flashOn51MethodDuration()
	{
		return 25;
	}

	@Alpha
	@ConfigItem(
			name = "Flash Color",
			description = "Color of the flash notification.",
			position = 6,
			keyName = "flashOn51MethodColor",
			section = playerSection
	)
	default Color flashOn51MethodColor()
	{
		return new Color(255, 190, 0, 50);
	}


	// Timer Section

	@ConfigItem(
		position = 0,
		keyName = "timerOverlay",
		name = "Overlay Timer",
		description = "Display an overlay that tracks your gauntlet time.",
		section = timerSection
	)
	default boolean timerOverlay()
	{
		return false;
	}

	@ConfigItem(
		position = 1,
		keyName = "timerChatMessage",
		name = "Chat Timer",
		description = "Display a chat message on-death with your gauntlet time.",
		section = timerSection
	)
	default boolean timerChatMessage()
	{
		return false;
	}


	// Helper Section

	@ConfigItem(
			position = 0,
			keyName = "autoProtectionPrayers",
			name = "Auto Protection Prayers",
			description = "The plugin will automatically pray against Hunllef's attacks.",
			section = helperSection
	)
	default boolean autoProtectionPrayers()
	{
		return false;
	}

	@ConfigItem(
			position = 1,
			keyName = "autoOffensivePrayers",
			name = "Auto Offensive Prayers",
			description = "The plugin will determine what prayers you have unlocked. (i.e. Piety, Rigour, etc.)" +
					"<br>It will pray the appropriate offensive prayer based on the weapon you currently have equipped.",
			section = helperSection
	)
	default boolean autoOffensivePrayers()
	{
		return false;
	}

	@ConfigItem(
			position = 2,
			keyName = "autoFlickPrayers",
			name = "1-Tick Flick Prayers",
			description = "Trying to complete the Egniol Diety combat achievements?" +
					"<br>The plugin will one tick flick both protection and offensive prayers for you.",
			section = helperSection
	)
	default boolean autoFlickPrayers()
	{
		return false;
	}

	@ConfigItem(
			position = 3,
			keyName = "autoWeaponSwitch",
			name = "Auto Switch Weapons",
			description = "Need help switching your weapons when Hunllef changes protection prayers?" +
					"<br>How about 5:1 switching? The plugin will determine whether to do 5:1 or normal switching" +
					"<br>based on how many perfected weapons you have when entering Hunllef's lair.",
			section = helperSection
	)
	default boolean autoWeaponSwitch()
	{
		return false;
	}


	// Constants

	@Getter
	@AllArgsConstructor
	enum TileOutline
	{
		OFF("Off"),
		ON("On"),
		TRUE_TILE("True Tile");

		private final String name;

		@Override
		public String toString()
		{
			return name;
		}
	}

	@Getter
	@AllArgsConstructor
	enum TrackingMode
	{
		DECREMENT("Decrement"),
		INCREMENT("Increment");

		private final String name;

		@Override
		public String toString()
		{
			return name;
		}
	}

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


	@AllArgsConstructor
	enum PrayerHighlightMode
	{
		WIDGET("Widget"),
		BOX("Box"),
		BOTH("Both"),
		NONE("None");

		private final String name;

		@Override
		public String toString()
		{
			return name;
		}
	}
}
