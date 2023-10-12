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

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "<html><font color=#6b8af6>[P]</font> Tarn's Lair</html>",
	enabledByDefault = false,
	description = "Mark tiles and clickboxes to help traverse the maze. Overlays the path to the elite clue step.",
	tags = {"agility", "maze", "minigame", "overlay", "ported", "kotori", "clue", "elite clue"}
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

	private final Map<Tile, GameObject> gameObjectQueue = new HashMap<>();
	private final Map<Tile, GroundObject> groundObjectQueue = new HashMap<>();
	public Tile toadBattaTile = null;
	
	// Injects our config
	@Inject
	private TarnsLairConfig config;
	
	// Provides our config
	@Provides
	TarnsLairConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TarnsLairConfig.class);
	}

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
		toadBattaTile = null;
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
	private void onGameTick(GameTick event)
	{
		if (!inLair && !isInTarnsLair())
		{
			return;
		}
		
		//Hotfix to clear old stored objects on loading. GameStateChanged is deprioritized over GameObjectSpawned/GroundObjectSpawned even when it's priority
		//is set higher.
		if (client.getGameState().equals(GameState.LOADING))
		{
			staircases.clear();
			wallTraps.clear();
			floorTraps.clear();
			groundObjectQueue.clear();
			gameObjectQueue.clear();
		}
	}
	
	@Subscribe
	private void onItemSpawned(ItemSpawned event)
	{
		if (!inLair && !isInTarnsLair())
		{
			return;
		}
		
		if (event.getItem().getId() == ItemID.TOAD_BATTA)
		{
		 	toadBattaTile = event.getTile();
		}
	}
	
	@Subscribe
	private void onItemDespawned(ItemDespawned event)
	{
		if (!inLair && !isInTarnsLair())
		{
			return;
		}
		
		if (event.getItem().getId() == ItemID.TOAD_BATTA)
		{
			toadBattaTile = null;
		}
	}

	@Subscribe
	private void onGameObjectSpawned(GameObjectSpawned event)
	{
		if (!inLair && !isInTarnsLair())
		{
			return;
		}
		
		GameObject gameObject = event.getGameObject();
		Tile tile = event.getTile();
		int gameObjectId = gameObject.getId();
		
		if (!Obstacles.STAIRCASE_IDS.contains(gameObjectId) && !Obstacles.WALL_TRAP_IDS.contains(gameObjectId))
		{
			return;
		}

		if (!gameObjectQueue.containsKey(tile))
		{
			onTileObject(tile, null, gameObject);
			gameObjectQueue.put(tile, gameObject);
		}
		else
		{
			if (gameObjectQueue.get(tile).getId() != gameObjectId)
			{
				onTileObject(tile, gameObjectQueue.get(tile), gameObject);
				gameObjectQueue.remove(tile);
				gameObjectQueue.put(tile, gameObject);
			}
		}
	}

	@Subscribe
	private void onGameObjectDespawned(GameObjectDespawned event)
	{
		if (!inLair && !isInTarnsLair())
		{
			return;
		}

		onTileObject(event.getTile(), event.getGameObject(), null);
	}

	@Subscribe
	private void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		if (!inLair && !isInTarnsLair())
		{
			return;
		}
		
		GroundObject groundObject = event.getGroundObject();
		Tile tile = event.getTile();
		int groundObjectId = groundObject.getId();
		
		if (!Obstacles.FLOOR_TRAP_IDS.contains(groundObjectId))
		{
			return;
		}

		if (!groundObjectQueue.containsKey(tile))
		{
			onTileObject(tile, null, groundObject);
			groundObjectQueue.put(tile, groundObject);
		}
		else
		{
			if (groundObjectQueue.get(tile).getId() != groundObjectId)
			{
				onTileObject(tile, groundObjectQueue.get(tile), groundObject);
				groundObjectQueue.remove(tile);
				groundObjectQueue.put(tile, groundObject);
			}
		}
	}

	@Subscribe
	private void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		if (!inLair && !isInTarnsLair())
		{
			return;
		}

		onTileObject(event.getTile(), groundObjectQueue.get(event.getTile()), event.getGroundObject());
	}

	private void onTileObject(Tile tile, TileObject oldObject, TileObject newObject)
	{
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
		for (int regionId : client.getMapRegions())
		{
			if (TARNS_LAIR_REGION_IDS.contains(regionId))
			{
				return true;
			}
		}
		return false;
	}
}