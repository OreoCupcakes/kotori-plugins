/*
 * Copyright (c) 2020 ThatGamerBlue
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
package com.theplug.kotori.effecttimers;

import java.awt.Color;

import net.runelite.client.config.*;

@ConfigGroup("effecttimers")
public interface EffectTimersConfig extends Config
{
	@ConfigSection(
			name = "Display Settings",
			description = "Settings that change the visual look of the timer overlays.",
			position = 1
	)
	String displaySettings = "Display Settings";
	
	@ConfigSection(
			name = "Freeze Timer Settings",
			description = "Settings for the Freeze timers.",
			position = 2
	)
	String freezeSettings = "Freeze Timer Settings";
	
	@ConfigSection(
			name = "Other Timer Settings",
			description = "Settings for the other timers.",
			position = 3
	)
	String otherSettings = "Other Timer Settings";

	@ConfigItem(
		name = "Show Timers On NPCs",
		keyName = "showNpcs",
		description = "Should we show the overlay on NPCs?",
		position = 1,
		section = displaySettings
	)
	default boolean showNpcs()
	{
		return false;
	}

	@ConfigItem(
		name = "Show Timers On Players",
		keyName = "showPlayers",
		description = "Should we show the overlay on players?",
		position = 2,
		section = displaySettings
	)
	default boolean showPlayers()
	{
		return true;
	}

	@ConfigItem(
		name = "Display Time As:",
		keyName = "timeMode",
		description = "How should we display the time?",
		position = 3,
		section = displaySettings
	)
	default TimeMode timeMode()
	{
		return TimeMode.TICKS;
	}
	
	@ConfigItem(
			name = "Show Timer Icons",
			keyName = "showIcons",
			description = "Should we render the icons? This will display the effect icon next to the timer, i.e. Vengeance icon." +
					"<br>Note disabling this will override all colors",
			position = 4,
			section = displaySettings
	)
	default boolean showIcons()
	{
		return true;
	}

	@ConfigItem(
		name = "Set Custom Timer Colors",
		keyName = "setColors",
		description = "Should we set our own timer colors?",
		position = 5,
		section = displaySettings
	)
	default boolean setColors()
	{
		return true;
	}

	@ConfigItem(
		name = "Timer Color",
		keyName = "timerColor",
		description = "Color for timers not on cooldown",
		position = 6,
		section = displaySettings
	)
	default Color timerColor()
	{
		return Color.CYAN;
	}

	@ConfigItem(
		name = "Cooldown Color",
		keyName = "cooldownColor",
		description = "Color for timers on cooldown",
		position = 7,
		section = displaySettings
	)
	default Color cooldownColor()
	{
		return Color.ORANGE;
	}
	
	@ConfigItem(
			keyName = "fontStyle",
			name = "Font Style",
			description = "Bold/Italics/Plain",
			position = 8,
			section = displaySettings
	)
	default FontStyle fontStyle()
	{
		return FontStyle.BOLD;
	}
	
	@Range(
			min = 9,
			max = 14
	)
	@ConfigItem(
			keyName = "textSize",
			name = "Text Size",
			description = "Text Size for Timers.",
			position = 9,
			section = displaySettings
	)
	default int textSize()
	{
		return 11;
	}
	
	@ConfigItem(
			name = "X Offset",
			keyName = "xOffset",
			description = "X Offset for overlay rendering",
			position = 10,
			section = displaySettings
	)
	default int xOffset()
	{
		return 20;
	}
	
	@ConfigItem(
		name = "Show Freeze Timers",
		keyName = "freezeTimers",
		description = "Should we render freeze timers? This takes into account your own gear (Ancient Sceptre, Swampbark, etc.)" +
				"<br>and NPC resistance (Phantom Muspah) and will adjust accordingly so you get an accurate timer.",
		position = 1,
		section = freezeSettings
	)
	default boolean freezeTimers()
	{
		return true;
	}
	
	@ConfigItem(
			name = "Adaptive Freeze Timers",
			keyName = "adaptiveFreezeTimers",
			description = "Note: Show Freeze Timers needs to be on for this to work." +
					"<br>With the introduction of gear like Swampbark armor and Ancient Sceptre, as well as NPCs having freeze resistance," +
					"<br>the standard freeze timers are not always accurate. Turning this option on will check your OWN gear when you cast" +
					"<br>spells that increase the freeze timer. This effects all freezes cast, including ones from other players.",
			position = 2,
			section = freezeSettings
	)
	default boolean adaptiveFreezeTimers() { return true; }

	@ConfigItem(
		name = "Show Teleblock Timers",
		keyName = "teleblockTimers",
		description = "Should we render teleblock timers?",
		position = 3,
		section = otherSettings
	)
	default boolean teleblockTimers()
	{
		return true;
	}

	@ConfigItem(
		name = "Show Vengeance Timers",
		keyName = "vengTimers",
		description = "Should we render vengeance timers?",
		position = 4,
		section = otherSettings
	)
	default boolean vengTimers()
	{
		return true;
	}

	@ConfigItem(
		name = "Show Staff of the Dead Timers",
		keyName = "sotdTimers",
		description = "Should we render staff of the dead timers?",
		position = 5,
		section = otherSettings
	)
	default boolean sotdTimers()
	{
		return true;
	}

	@ConfigItem(
		name = "Show Imbued/Sat. Heart Timers",
		keyName = "imbHeartTimers",
		description = "Should we render imbued/saturated heart timers?",
		position = 6,
		section = otherSettings
	)
	default boolean imbHeartTimers()
	{
		return true;
	}

	@ConfigItem(
		name = "Show DFS/DFW Timers",
		keyName = "dfsTimers",
		description = "Should we render the Dragonfire shield and Dragonfire Ward timers?",
		position = 7,
		section = otherSettings
	)
	default boolean dfsTimers()
	{
		return true;
	}

	@ConfigItem(
		name = "Show Wyvern Shield Timers",
		keyName = "ancWyvernTimers",
		description = "Should we render the Ancient Wyvern shield timers?",
		position = 8,
		section = otherSettings
	)
	default boolean ancWyvernTimers()
	{
		return true;
	}

	@ConfigItem(
			name = "Enable Debug Logging",
			keyName = "debugLogging",
			description = "Enable debug messages to show up in the client.log",
			position = 10,
			section = otherSettings,
			hidden = true
	)
	default boolean debugLogging() { return false; }
	
	@ConfigItem(
		name = "Debug Keybind",
		keyName = "debugKeybind",
		description = "Don't press this unless you know what it does :)",
		position = 11,
		section = otherSettings,
		hidden = true
	)
	default Keybind debugKeybind()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		name = "Debug Integer",
		keyName = "debugInteger",
		description = "Related to the keybind in some way :)",
		position = 12,
		section = otherSettings,
		hidden = true
	)
	default int debugInteger()
	{
		return -1;
	}
}