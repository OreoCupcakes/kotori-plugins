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
package com.theplug.kotori.tarnslair;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.*;

@ConfigGroup("tarnslair")

public interface TarnsLairConfig extends Config
{
	@ConfigItem(
			keyName = "line",
			name = "Paths to the Toad Batta Clue Step",
			description = "There is three known paths to the toad batta clue step.<br>" +
					"   - The most fastest path from the Haunted Mine entrance.<br>" +
					"   - The fast and no trap damage path from the Haunted Mine entrance.<br>" +
					"   - The slow path from the Slayer ring teleport.",
			position = 0
	)
	default Boolean statementLine() { return false; }
	
	@ConfigItem(
			keyName = "fastestPath",
			name = "Show the Fastest Path?",
			description = "This is the fastest path to the clue step. It starts at the Haunted Mine entrance and requires taking damage from a wall trap.",
			position = 1
	)
	default boolean fastestPath()
	{
		return true;
	}
	
	@ConfigItem(
			keyName = "fastestPathColor",
			name = "Fastest Path Color",
			description = "Select the color to display the fastest path as.",
			position = 2
	)
	default Color fastestPathColor() { return Color.MAGENTA; }
	
	@ConfigItem(
			keyName = "slayerRingPath",
			name = "Show the Slayer Ring Path?",
			description = "This is the slowest path to the clue step. It starts at the Tarn's Lair teleport spot via the Slayer ring.<br>" +
					"This path requires you to take damage from multiple traps.",
			position = 3
	)
	default boolean slayerRingPath() { return true; }
	
	@ConfigItem(
			keyName = "slayerRingPathColor",
			name = "Slayer Ring Path Color",
			description = "Select the color to display the slow Slayer ring path as.",
			position = 4
	)
	default Color slayerRingPathColor() { return Color.RED; }
	
	@ConfigItem(
			keyName = "avoidableDamagePath",
			name = "Show the Avoidable Damage Path?",
			description = "This is a ok speed path to the clue step. It starts at the Haunted Mine entrance.<br>" +
					"The main benefit to this path is there isn't any forced trap damage.",
			position = 5
	)
	default boolean avoidablePath() { return false; }
	
	@ConfigItem(
			keyName = "avoidableDamagePathColor",
			name = "Avoidable Damage Path Color",
			description = "Select the color to display the avoidable damage path as.",
			position = 6
	)
	default Color avoidablePathColor() { return Color.YELLOW; }
}