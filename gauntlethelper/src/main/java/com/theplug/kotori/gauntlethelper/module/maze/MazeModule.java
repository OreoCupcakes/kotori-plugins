/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2023, rdutta <https://github.com/rdutta>
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

package com.theplug.kotori.gauntlethelper.module.maze;

import com.theplug.kotori.gauntlethelper.GauntletHelperConfig;
import com.theplug.kotori.gauntlethelper.module.Module;
import com.theplug.kotori.gauntlethelper.module.overlay.TimerOverlay;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import static net.runelite.api.ItemID.RAW_PADDLEFISH;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.ObjectID;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.PostMenuSort;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.game.npcoverlay.NpcOverlayService;
import net.runelite.client.ui.overlay.OverlayManager;

@Singleton
public final class MazeModule implements Module
{
	private static final List<Integer> NPC_IDS_DEMIBOSS = List.of(
		NpcID.CRYSTALLINE_BEAR,
		NpcID.CRYSTALLINE_DARK_BEAST,
		NpcID.CRYSTALLINE_DRAGON,
		NpcID.CORRUPTED_BEAR,
		NpcID.CORRUPTED_DARK_BEAST,
		NpcID.CORRUPTED_DRAGON
	);
	private static final List<Integer> GAME_OBJECT_IDS_RESOURCE = List.of(
		ObjectID.CRYSTAL_DEPOSIT,
		ObjectID.PHREN_ROOTS,
		ObjectID.FISHING_SPOT_36068,
		ObjectID.GRYM_ROOT,
		ObjectID.LINUM_TIRINUM,
		ObjectID.CORRUPT_DEPOSIT,
		ObjectID.CORRUPT_PHREN_ROOTS,
		ObjectID.CORRUPT_FISHING_SPOT,
		ObjectID.CORRUPT_GRYM_ROOT,
		ObjectID.CORRUPT_LINUM_TIRINUM
	);
	private static final List<Integer> GAME_OBJECT_IDS_UTILITY = List.of(
		ObjectID.SINGING_BOWL_35966,
		ObjectID.RANGE_35980,
		ObjectID.WATER_PUMP_35981,
		ObjectID.SINGING_BOWL_36063,
		ObjectID.RANGE_36077,
		ObjectID.WATER_PUMP_36078
	);

	@Getter(AccessLevel.PACKAGE)
	private final Set<ResourceGameObject> resourceGameObjects = new HashSet<>();
	@Getter(AccessLevel.PACKAGE)
	private final Set<Demiboss> demiBosses = new HashSet<>();
	@Getter(AccessLevel.PACKAGE)
	private final Set<GameObject> utilities = new HashSet<>();
	private final Function<NPC, HighlightedNpc> npcHighlighter = this::highlightNpc;

	@Inject
	private EventBus eventBus;
	@Inject
	private Client client;
	@Inject
	private ClientThread clientThread;
	@Inject
	private GauntletHelperConfig config;
	@Inject
	private NpcOverlayService npcOverlayService;
	@Inject
	private ResourceManager resourceManager;
	@Inject
	private SkillIconManager skillIconManager;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private MazeOverlay mazeOverlay;
	@Inject
	private MinimapOverlay minimapOverlay;
	@Inject
	private TimerOverlay timerOverlay;

	@Override
	public void start()
	{
		eventBus.register(this);
		npcOverlayService.registerHighlighter(npcHighlighter);
		overlayManager.add(mazeOverlay);
		overlayManager.add(minimapOverlay);
		overlayManager.add(timerOverlay);
	}

	@Override
	public void stop()
	{
		eventBus.unregister(this);
		npcOverlayService.unregisterHighlighter(npcHighlighter);
		overlayManager.remove(mazeOverlay);
		overlayManager.remove(minimapOverlay);
		overlayManager.remove(timerOverlay);
		resourceManager.reset();
		resourceGameObjects.clear();
		utilities.clear();
		demiBosses.clear();
	}

	@Subscribe
	void onConfigChanged(final ConfigChanged event)
	{
		if (!event.getGroup().equals("gauntlethelper"))
		{
			return;
		}

		clientThread.invoke(() -> {
			switch (event.getKey())
			{
				case "resourceIconSize":
					if (!resourceGameObjects.isEmpty())
					{
						resourceGameObjects.forEach(r -> r.setIconSize(config.resourceIconSize()));
					}
					break;
				case "resourceTracker":
				case "resourceTrackingMode":
				case "resourceRemoveAcquired":
					resourceManager.reset();
					resourceManager.init(client.getMapRegions()[0]);
					break;
				default:
					npcOverlayService.rebuild();
					break;
			}
		});
	}

	@Subscribe
	void onGameStateChanged(final GameStateChanged event)
	{
		switch (event.getGameState())
		{
			case LOADING:
				resourceGameObjects.clear();
				utilities.clear();
				break;
			case LOGIN_SCREEN:
			case HOPPING:
				stop();
				break;
		}
	}

	@Subscribe
	public void onPostMenuSort(final PostMenuSort postMenuSort)
	{
		if ((!config.utilitiesFishCheck()) || client.isMenuOpen())
		{
			return;
		}
		final ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);
		if (container == null)
		{
			return;
		}

		final boolean hasRawFish = Arrays.stream(container.getItems()).anyMatch(x -> x.getId() == RAW_PADDLEFISH);
		if (!hasRawFish)
		{
			return;
		}

		// Remove Quick-pass and Pass
		final MenuEntry[] filteredEntires = Arrays.stream(client.getMenuEntries())
			.filter(x -> !x.getOption().equals("Quick-pass") && !x.getOption().equals("Pass"))
			.toArray(MenuEntry[]::new);

		client.setMenuEntries(filteredEntires);
	}

	@Subscribe
	void onWidgetLoaded(final WidgetLoaded event)
	{
		if (event.getGroupId() == WidgetID.GAUNTLET_TIMER_GROUP_ID)
		{
			resourceManager.init(client.getMapRegions()[0]);
			timerOverlay.setGauntletStart();
		}
	}

	@Subscribe
	void onGameObjectSpawned(final GameObjectSpawned event)
	{
		final GameObject gameObject = event.getGameObject();

		final int id = gameObject.getId();

		if (GAME_OBJECT_IDS_RESOURCE.contains(id))
		{
			resourceGameObjects.add(new ResourceGameObject(gameObject, skillIconManager, config.resourceIconSize()));
		}
		else if (GAME_OBJECT_IDS_UTILITY.contains(id))
		{
			utilities.add(gameObject);
		}
	}

	@Subscribe
	void onGameObjectDespawned(final GameObjectDespawned event)
	{
		final GameObject gameObject = event.getGameObject();

		final int id = gameObject.getId();

		if (GAME_OBJECT_IDS_RESOURCE.contains(id))
		{
			resourceGameObjects.removeIf(o -> o.getGameObject() == gameObject);
		}
		else if (GAME_OBJECT_IDS_UTILITY.contains(id))
		{
			utilities.remove(gameObject);
		}
	}

	@Subscribe
	void onNpcSpawned(final NpcSpawned event)
	{
		final NPC npc = event.getNpc();


		if (NPC_IDS_DEMIBOSS.contains(npc.getId()))
		{
			demiBosses.add(new Demiboss(npc, skillIconManager));
		}
	}

	@Subscribe
	void onNpcDespawned(final NpcDespawned event)
	{
		final NPC npc = event.getNpc();

		if (NPC_IDS_DEMIBOSS.contains(npc.getId()))
		{
			demiBosses.removeIf(d -> d.isNpc(npc));
		}
	}

	@Subscribe
	void onActorDeath(final ActorDeath event)
	{
		if (event.getActor() == client.getLocalPlayer())
		{
			timerOverlay.onPlayerDeath();
		}
	}

	@Subscribe
	void onChatMessage(final ChatMessage event)
	{
		final ChatMessageType type = event.getType();

		if (type == ChatMessageType.SPAM || type == ChatMessageType.GAMEMESSAGE)
		{
			resourceManager.parseChatMessage(event.getMessage());
		}
	}

	private HighlightedNpc highlightNpc(final NPC npc)
	{
		final int id = npc.getId();

		final int borderWidth;
		final Color highlightColor;

		switch (id)
		{
			case NpcID.CRYSTALLINE_BAT:
			case NpcID.CORRUPTED_BAT:
			case NpcID.CRYSTALLINE_RAT:
			case NpcID.CORRUPTED_RAT:
			case NpcID.CRYSTALLINE_SPIDER:
			case NpcID.CORRUPTED_SPIDER:
				return HighlightedNpc.builder()
					.npc(npc)
					.outline(true)
					.borderWidth(config.weakNpcOutlineWidth())
					.highlightColor(config.weakNpcOutlineColor())
					.render(n -> config.weakNpcOutline() && !npc.isDead())
					.build();
			case NpcID.CRYSTALLINE_SCORPION:
			case NpcID.CORRUPTED_SCORPION:
			case NpcID.CRYSTALLINE_UNICORN:
			case NpcID.CORRUPTED_UNICORN:
			case NpcID.CRYSTALLINE_WOLF:
			case NpcID.CORRUPTED_WOLF:
				return HighlightedNpc.builder()
					.npc(npc)
					.outline(true)
					.borderWidth(config.strongNpcOutlineWidth())
					.highlightColor(config.strongNpcOutlineColor())
					.render(n -> config.strongNpcOutline() && !npc.isDead())
					.build();
			case NpcID.CRYSTALLINE_BEAR:
			case NpcID.CORRUPTED_BEAR:
				borderWidth = config.demibossOutlineWidth();
				highlightColor = config.bearOutlineColor();
				break;
			case NpcID.CRYSTALLINE_DARK_BEAST:
			case NpcID.CORRUPTED_DARK_BEAST:
				borderWidth = config.demibossOutlineWidth();
				highlightColor = config.darkBeastOutlineColor();
				break;
			case NpcID.CRYSTALLINE_DRAGON:
			case NpcID.CORRUPTED_DRAGON:
				borderWidth = config.demibossOutlineWidth();
				highlightColor = config.dragonOutlineColor();
				break;
			default:
				return null;
		}

		return HighlightedNpc.builder()
			.npc(npc)
			.outline(true)
			.borderWidth(borderWidth)
			.highlightColor(highlightColor)
			.render(n -> config.demibossOutline() && !npc.isDead())
			.build();
	}
}
