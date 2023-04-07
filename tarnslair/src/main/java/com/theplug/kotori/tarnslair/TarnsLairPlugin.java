/*
 * Copyright (c) 2018-2019, Shaun Dreclin <https://github.com/ShaunDreclin>
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Tarn's Lair",
	enabledByDefault = false,
	description = "Mark tiles and clickboxes to help traverse the maze.",
	tags = {"agility", "maze", "minigame", "overlay", "ported", "kotori"}
)
@Slf4j
public class TarnsLairPlugin extends Plugin
{
	private static final Set<Integer> TARNS_LAIR_REGION_IDS = Set.of(12615, 12616);

	@Getter(AccessLevel.PACKAGE)
	private final Map<TileObject, Tile> staircases = new HashMap<>();

	@Getter(AccessLevel.PACKAGE)
	private final Map<TileObject, Tile> wallTraps = new HashMap<>();

	@Getter(AccessLevel.PACKAGE)
	private final Map<TileObject, Tile> floorTraps = new HashMap<>();

	private boolean inLair;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TarnsLairOverlay overlay;

	private Map<Tile, GameObject> gameObjectQueue = new HashMap<>();
	private Map<Tile, GroundObject> groundObjectQueue = new HashMap<>();

	@Override
	protected void startUp()
	{
		if (client.getGameState() != GameState.LOGGED_IN || !isInTarnsLair())
		{
			return;
		}
		
		init();
	}
	
	private void init()
	{
		inLair = true;
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		inLair = false;
		overlayManager.remove(overlay);
		staircases.clear();
		wallTraps.clear();
		floorTraps.clear();
		groundObjectQueue.clear();
		gameObjectQueue.clear();
	}
	
	@Subscribe
	private void onGameStateChanged(GameStateChanged event)
	{
		final GameState gameState = event.getGameState();
		
		switch(gameState)
		{
			case LOGGED_IN:
				if (isInTarnsLair())
				{
					if (!inLair)
					{
						init();
					}
					else
					{
						staircases.clear();
						wallTraps.clear();
						floorTraps.clear();
						groundObjectQueue.clear();
						gameObjectQueue.clear();
					}
				}
				else
				{
					if (inLair)
					{
						shutDown();
					}
				}
				break;
			case HOPPING:
			case LOGIN_SCREEN:
				if (inLair)
				{
					shutDown();
				}
				break;
			default:
				break;
		}
	}

	@Subscribe
	private void onGameObjectSpawned(GameObjectSpawned event)
	{
		if (!inLair)
		{
			return;
		}

		if (!gameObjectQueue.containsKey(event.getTile()))
		{
			onTileObject(event.getTile(), null, event.getGameObject());
			gameObjectQueue.put(event.getTile(), event.getGameObject());
		}
		else
		{
			if (gameObjectQueue.get(event.getTile()).getId() != event.getGameObject().getId())
			{
				onTileObject(event.getTile(), gameObjectQueue.get(event.getTile()), event.getGameObject());
				gameObjectQueue.remove(event.getTile());
				gameObjectQueue.put(event.getTile(), event.getGameObject());
			}
		}
	}

	@Subscribe
	private void onGameObjectDespawned(GameObjectDespawned event)
	{
		if (!inLair)
		{
			return;
		}

		onTileObject(event.getTile(), event.getGameObject(), null);
	}

	@Subscribe
	private void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		if (!inLair)
		{
			return;
		}

		if (!groundObjectQueue.containsKey(event.getTile()))
		{
			onTileObject(event.getTile(), null, event.getGroundObject());
			groundObjectQueue.put(event.getTile(), event.getGroundObject());
		}
		else
		{
			if (groundObjectQueue.get(event.getTile()).getId() != event.getGroundObject().getId())
			{
				onTileObject(event.getTile(), groundObjectQueue.get(event.getTile()), event.getGroundObject());
				groundObjectQueue.remove(event.getTile());
				groundObjectQueue.put(event.getTile(), event.getGroundObject());
			}
		}
	}

	@Subscribe
	private void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		if (!inLair)
		{
			return;
		}

		onTileObject(event.getTile(), groundObjectQueue.get(event.getTile()), event.getGroundObject());
	}

	private void onTileObject(Tile tile, TileObject oldObject, TileObject newObject)
	{
		if (!inLair)
		{
			return;
		}

		staircases.remove(oldObject);
		if (newObject != null && Obstacles.STAIRCASE_IDS.contains(newObject.getId()))
		{
			staircases.put(newObject, tile);
		}

		wallTraps.remove(oldObject);
		if (newObject != null && Obstacles.WALL_TRAP_IDS.contains(newObject.getId()))
		{
			wallTraps.put(newObject, tile);
		}

		floorTraps.remove(oldObject);
		if (newObject != null && Obstacles.FLOOR_TRAP_IDS.contains(newObject.getId()))
		{
			floorTraps.put(newObject, tile);
		}
	}
	
	public boolean isInTarnsLair()
	{
		for (int regionId : TARNS_LAIR_REGION_IDS)
		{
			if (TARNS_LAIR_REGION_IDS.contains(regionId))
			{
				return true;
			}
		}
		return false;
	}
}