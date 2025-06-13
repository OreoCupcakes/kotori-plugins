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

import com.theplug.kotori.kotoriutils.methods.InventoryInteractions;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import net.runelite.api.*;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ItemID;

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
				//27624, 25490, 28262, 28474
				if (InventoryInteractions.yourEquipmentContains(ItemID.ANCIENT_SCEPTRE, ItemID.ANCIENT_SCEPTRE_ICE, ItemID.ANCIENT_SCEPTRE_ICE_TROUVER,
						ItemID.ANCIENT_SCEPTRE_BLOOD, ItemID.ANCIENT_SCEPTRE_BLOOD_TROUVER, ItemID.ANCIENT_SCEPTRE_SMOKE, ItemID.ANCIENT_SCEPTRE_SMOKE_TROUVER,
						ItemID.ANCIENT_SCEPTRE_SHADOW, ItemID.ANCIENT_SCEPTRE_SHADOW_TROUVER))
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
				//12077, 12078, 12079, 12080, 12082, 12063, 12073, 12074, 12075, 12076, 12081
				case NpcID.MUSPAH:
				case NpcID.MUSPAH_MELEE:
				case NpcID.MUSPAH_SOULSPLIT:
				case NpcID.MUSPAH_FINAL:
				case NpcID.MUSPAH_TELEPORT:
				case NpcID.SOTN_MUSPAH_CUTSCENE:
				case NpcID.MUSPAH_QUEST:
				case NpcID.MUSPAH_MELEE_QUEST:
				case NpcID.MUSPAH_SOULSPLIT_QUEST:
				case NpcID.MUSPAH_FINAL_QUEST:
				case NpcID.MUSPAH_TELEPORT_QUEST:
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
						case NpcID.MUSPAH:
						case NpcID.MUSPAH_MELEE:
						case NpcID.MUSPAH_SOULSPLIT:
						case NpcID.MUSPAH_FINAL:
						case NpcID.MUSPAH_TELEPORT:
						case NpcID.SOTN_MUSPAH_CUTSCENE:
						case NpcID.MUSPAH_QUEST:
						case NpcID.MUSPAH_MELEE_QUEST:
						case NpcID.MUSPAH_SOULSPLIT_QUEST:
						case NpcID.MUSPAH_FINAL_QUEST:
						case NpcID.MUSPAH_TELEPORT_QUEST:
						case NpcID.CALLISTO_SINGLES:
						case NpcID.CLANCUP_CALLISTO:
						case NpcID.CALLISTO:
							length = 0;
							break;
					}
				}
				return length;
			case SCORCHING_BOW:
				return 0;
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
