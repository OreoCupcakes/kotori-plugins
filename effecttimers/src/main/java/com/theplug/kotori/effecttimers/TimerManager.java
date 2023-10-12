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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;

@Slf4j
@Singleton
public class TimerManager
{
	@Inject
	private EffectTimersPlugin plugin;
	private HashMap<Actor, HashMap<TimerType, Timer>> timerMap = new HashMap<>();

	private HashMap<TimerType, Timer> getTimersFor(Actor actor)
	{
		if (!timerMap.containsKey(actor) || timerMap.get(actor) == null)
		{
			return null;
		}
		return timerMap.get(actor);
	}

	public boolean isTimerValid(Actor actor, TimerType type)
	{
		Timer timer = getTimerFor(actor, type);
		if (timer == null)
		{
			return false;
		}
		return timer.isValid();
	}

	public boolean hasTimerActive(Actor actor, TimerType type)
	{
		Timer timer = getTimerFor(actor, type);
		if (timer == null)
		{
			return false;
		}
		return timer.getTimerState() != Timer.TimerState.INACTIVE;
	}

	public Timer getTimerFor(Actor actor, TimerType type)
	{
		HashMap<TimerType, Timer> mapping = getTimersFor(actor);
		if (mapping == null)
		{
			return null;
		}
		if (mapping.get(type) == null)
		{
			mapping.put(type, new Timer(plugin, null));
		}
		return mapping.get(type);
	}
	
	public void removeTimerFor(Actor actor, TimerType type)
	{
		HashMap<TimerType, Timer> mapping = getTimersFor(actor);
		if (mapping == null)
		{
			return;
		}
		if (mapping.get(type) == null)
		{
			return;
		}
		mapping.remove(type);
		
		if (mapping.isEmpty())
		{
			timerMap.remove(actor);
		}
	}

	public void setTimerFor(Actor actor, TimerType type, Timer timer)
	{
		timer.setTimerTypeIfNull(type);
		HashMap<TimerType, Timer> mapping = getTimersFor(actor);
		if (mapping == null)
		{
			return;
		}
		mapping.put(type, timer);
	}
	
	public void addTimerFor(Actor actor, TimerType type, Timer timer)
	{
		timer.setTimerTypeIfNull(type);
		if (!timerMap.containsKey(actor) || timerMap.get(actor) == null)
		{
			timerMap.put(actor, new HashMap<>());
		}
		timerMap.get(actor).put(type, timer);
	}

	public void jumpToCooldown(Actor actor, TimerType type)
	{
		Timer timer = getTimerFor(actor, type);
		if (timer == null)
		{
			return;
		}
		timer.setStartMillis(System.currentTimeMillis());
		timer.setTicksStart(plugin.getClient().getTickCount());
		timer.setTicksLength(0);
	}
	
	public void modifyTimerLength(Actor actor, TimerType type, int newLength)
	{
		Timer timer = getTimerFor(actor, type);
		if (timer == null)
		{
			return;
		}
		timer.setTicksLength(newLength);
	}
	
	public boolean timerMapContainsActor(Actor actor)
	{
		return timerMap.containsKey(actor);
	}

	public void clearExpiredTimers()
	{
		if (timerMap.isEmpty())
		{
			return;
		}
		//Store the keys of timerMap in an ArrayList for future removal of entry after iteration of the map.
		ArrayList<Actor> timerMapKeys = new ArrayList<>();
		for (Map.Entry<Actor, HashMap<TimerType, Timer>> actorEntry : timerMap.entrySet())
		{
			Actor keyActor = actorEntry.getKey();
			HashMap<TimerType, Timer> valueTimerMap = actorEntry.getValue();
			//Store the keys of valueTimerMap in an ArrayList for future removal of inactive timers after iteration of the map.
			ArrayList<TimerType> timerHashMapKeys = new ArrayList<>();
			for (Map.Entry<TimerType, Timer> timerEntry : valueTimerMap.entrySet())
			{
				TimerType keyTimerType = timerEntry.getKey();
				Timer valueTimer = timerEntry.getValue();
				//Find inactive timers and mark down their key values.
				if (valueTimer.getTimerState().equals(Timer.TimerState.INACTIVE))
				{
					timerHashMapKeys.add(keyTimerType);
				}
			}
			//Remove the timer entries from valueTimerMap now that we're done searching for inactive timers.
			for (TimerType key : timerHashMapKeys)
			{
				valueTimerMap.remove(key);
			}
			//Now check if the TimerType, Timer hashmap is empty after removal of inactive timers. If empty, then mark down the TimerMap entry for future removal.
			if (valueTimerMap.isEmpty())
			{
				timerMapKeys.add(keyActor);
			}
		}
		//Remove the timerMap entry
		for (Actor key : timerMapKeys)
		{
			timerMap.remove(key);
		}
	}
	
	public void shutDown()
	{
		timerMap.clear();
	}
}
