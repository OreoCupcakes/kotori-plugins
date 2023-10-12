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
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.theplug.kotori.kotoriutils.methods.InventoryInteractions;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import net.runelite.api.*;
import net.runelite.api.kit.KitType;

@ToString
@EqualsAndHashCode
public class Timer
{
	private EffectTimersPlugin plugin;
	private Client client;
	@Setter
	private int ticksStart;
	@Setter
	private long startMillis;
	@Setter
	private int ticksLength;
	private int cooldownLength;
	private TimerType type;
	private boolean modifiedTimer;
	private boolean shutdown = false;
	
	public Timer(EffectTimersPlugin plugin, PlayerEffect effect)
	{
		this(plugin, effect, false, null);
	}

	public Timer(EffectTimersPlugin plugin, PlayerEffect effect, boolean half, Actor actorWithGraphic)
	{
		this.plugin = plugin;
		this.client = plugin.getClient();
		this.ticksStart = client.getTickCount();
		this.startMillis = System.currentTimeMillis();
		this.type = effect == null ? null : effect.getType();
		this.cooldownLength = determineImmunityLength(effect, actorWithGraphic);
		this.ticksLength = determineEffectLength(effect, actorWithGraphic, half);
		this.modifiedTimer = false;
	}

	public boolean isValid()
	{
		return type != null;
	}

	public int getTicksElapsed()
	{
		return client.getTickCount() - ticksStart;
	}

	public TimerState getTimerState()
	{
		int ticksElapsed = getTicksElapsed();
		if (ticksElapsed > ticksLength + cooldownLength)
		{
			return TimerState.INACTIVE;
		}
		else if (ticksElapsed > ticksLength)
		{
			return TimerState.COOLDOWN;
		}
		return TimerState.ACTIVE;
	}

	public void setTimerTypeIfNull(TimerType set)
	{
		if (type == null)
		{
			type = set;
		}
	}

	public long getMillisForRender()
	{
		long millisElapsed = System.currentTimeMillis() - startMillis;
		long millisRemaining = ((ticksLength * 600) + (cooldownLength * 600)) - millisElapsed;
		switch (getTimerState())
		{
			case ACTIVE:
				return millisRemaining - (cooldownLength * 600);
			case COOLDOWN:
				return millisRemaining;
			default:
				return -1;
		}
	}

	public int getTicksForRender()
	{
		int ticksRemaining = (ticksLength + cooldownLength) - getTicksElapsed();
		ticksRemaining++; // so it renders nicely
		switch (getTimerState())
		{
			case ACTIVE:
				return ticksRemaining - cooldownLength;
			case COOLDOWN:
				return ticksRemaining;
			default:
				return -1;
		}
	}

	public BufferedImage getIcon()
	{
		return getTimerState() == TimerState.COOLDOWN ? type.getCooldownIcon() : type.getIcon();
	}

	public Color determineColor()
	{
		if (plugin.getConfig().showIcons())
		{
			if (plugin.getConfig().setColors())
			{
				return getTimerState() == TimerState.COOLDOWN ? plugin.getConfig().cooldownColor() : plugin.getConfig().timerColor();
			}
			else
			{
				return getTimerState() == TimerState.COOLDOWN ? type.getDefaultColor().darker() : type.getDefaultColor();
			}
		}
		if (!plugin.getConfig().showIcons())
		{
			if (plugin.getConfig().setColors())
			{
				return getTimerState() == TimerState.COOLDOWN ? plugin.getConfig().cooldownColor() : plugin.getConfig().timerColor();
			}
			else
			{
				return getTimerState() == TimerState.COOLDOWN ? type.getDefaultColor().darker() : type.getDefaultColor();
			}
		}
		else
		{
			return getTimerState() == TimerState.COOLDOWN ? type.getDefaultColor().darker() : type.getDefaultColor();
		}
	}

	private int determineEffectLength(PlayerEffect effect, Actor actor, boolean half)
	{
		if (effect == null)
		{
			return 0;
		}

		if (half)
		{
			return effect.getTimerLengthTicks() / 2;
		}

		int length = effect.getTimerLengthTicks();

		if (!plugin.getConfig().adaptiveFreezeTimers())
		{
			return length;
		}

		boolean checkNpc = false;

		switch (effect)
		{
			case RUSH:
			case BURST:
			case BLITZ:
			case BARRAGE:
				if (InventoryInteractions.yourEquipmentContains(ItemID.ANCIENT_SCEPTRE, ItemID.ICE_ANCIENT_SCEPTRE,
						ItemID.ICE_ANCIENT_SCEPTRE_28262, ItemID.ICE_ANCIENT_SCEPTRE_L))
				{
					length += length * 10 / 100;
				}
				checkNpc = true;
				break;
			case BIND:
			case SNARE:
			case ENTANGLE:
				length += InventoryInteractions.yourEquipmentCount(plugin.swampBarkArmorIds.stream().mapToInt(Integer::intValue).toArray()) * 2;
				checkNpc = true;
				break;
		}

		if (checkNpc && actor instanceof NPC)
		{
			NPC npc = (NPC) actor;
			switch (npc.getId())
			{
				case NpcID.PHANTOM_MUSPAH:
				case NpcID.PHANTOM_MUSPAH_12078:
				case NpcID.PHANTOM_MUSPAH_12079:
				case NpcID.PHANTOM_MUSPAH_12080:
				case NpcID.PHANTOM_MUSPAH_12082:
				case NpcID.STRANGE_CREATURE:
				case NpcID.STRANGE_CREATURE_12073:
				case NpcID.STRANGE_CREATURE_12074:
				case NpcID.STRANGE_CREATURE_12075:
				case NpcID.STRANGE_CREATURE_12076:
				case NpcID.STRANGE_CREATURE_12081:
					length = length * 2 / 3;
					break;
			}
		}

		return length;
	}

	private int determineImmunityLength(PlayerEffect effect, Actor actorWithGraphic)
	{
		if (effect == null)
		{
			return 0;
		}

		int length = effect.getType().getImmunityLength();

		switch (effect)
		{
			case BIND:
			case SNARE:
			case ENTANGLE:
			case RUSH:
			case BURST:
			case BLITZ:
			case BARRAGE:
				if (actorWithGraphic instanceof NPC)
				{
					NPC npc = (NPC) actorWithGraphic;
					switch (npc.getId())
					{
						case NpcID.PHANTOM_MUSPAH:
						case NpcID.PHANTOM_MUSPAH_12078:
						case NpcID.PHANTOM_MUSPAH_12079:
						case NpcID.PHANTOM_MUSPAH_12080:
						case NpcID.PHANTOM_MUSPAH_12082:
						case NpcID.STRANGE_CREATURE:
						case NpcID.STRANGE_CREATURE_12073:
						case NpcID.STRANGE_CREATURE_12074:
						case NpcID.STRANGE_CREATURE_12075:
						case NpcID.STRANGE_CREATURE_12076:
						case NpcID.STRANGE_CREATURE_12081:
						case NpcID.ARTIO:
						case NpcID.CALLISTO:
						case NpcID.CALLISTO_6609:
							length = 0;
							break;
					}
				}
				return length;
			default:
				return length;
		}
	}

	public enum TimerState
	{
		ACTIVE,
		COOLDOWN,
		INACTIVE
	}
}
