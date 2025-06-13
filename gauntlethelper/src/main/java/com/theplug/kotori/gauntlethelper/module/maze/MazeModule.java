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

import com.theplug.kotori.kotoriutils.methods.MiscUtilities;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.ItemContainer;
import static net.runelite.api.gameval.ItemID.GAUNTLET_RAW_FOOD;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.PostMenuSort;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.gameval.InterfaceID;
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
		NpcID.CRYSTAL_BEAR,
		NpcID.CRYSTAL_DARK_BEAST,
		NpcID.CRYSTAL_DRAGON,
		NpcID.CRYSTAL_BEAR_HM,
		NpcID.CRYSTAL_DARK_BEAST_HM,
		NpcID.CRYSTAL_DRAGON_HM
	);
	private static final List<Integer> GAME_OBJECT_IDS_RESOURCE = List.of(
		ObjectID.GAUNTLET_ROCK,
		ObjectID.GAUNTLET_TREE,
		ObjectID.GAUNTLET_POND,
		ObjectID.GAUNTLET_HERB,
		ObjectID.GAUNTLET_FIBRE,
		ObjectID.GAUNTLET_ROCK_HM,
		ObjectID.GAUNTLET_TREE_HM,
		ObjectID.GAUNTLET_POND_HM,
		ObjectID.GAUNTLET_HERB_HM,
		ObjectID.GAUNTLET_FIBRE_HM
	);
	private static final List<Integer> GAME_OBJECT_IDS_UTILITY = List.of(
		ObjectID.GAUNTLET_SINGING_BOWL_HM,
		ObjectID.GAUNTLET_RANGE_HM,
		ObjectID.GAUNTLET_SINK_HM,
		ObjectID.GAUNTLET_SINGING_BOWL,
		ObjectID.GAUNTLET_RANGE,
		ObjectID.GAUNTLET_SINK
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
					resourceManager.init(MiscUtilities.getPlayerRegionID());
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
		final ItemContainer container = client.getItemContainer(InventoryID.INV);
		if (container == null)
		{
			return;
		}

		final boolean hasRawFish = Arrays.stream(container.getItems()).anyMatch(x -> x.getId() == GAUNTLET_RAW_FOOD);
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
		if (event.getGroupId() == InterfaceID.GAUNTLET_OVERLAY)
		{
			resourceManager.init(MiscUtilities.getPlayerRegionID());
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
			case NpcID.CRYSTAL_BAT:
			case NpcID.CRYSTAL_BAT_HM:
			case NpcID.CRYSTAL_RAT:
			case NpcID.CRYSTAL_RAT_HM:
			case NpcID.CRYSTAL_SPIDER:
			case NpcID.CRYSTAL_SPIDER_HM:
				return HighlightedNpc.builder()
					.npc(npc)
					.outline(true)
					.borderWidth(config.weakNpcOutlineWidth())
					.highlightColor(config.weakNpcOutlineColor())
					.render(n -> config.weakNpcOutline() && !npc.isDead())
					.build();
			case NpcID.CRYSTAL_SCORPION:
			case NpcID.CRYSTAL_SCORPION_HM:
			case NpcID.CRYSTAL_UNICORN:
			case NpcID.CRYSTAL_UNICORN_HM:
			case NpcID.CRYSTAL_WOLF:
			case NpcID.CRYSTAL_WOLF_HM:
				return HighlightedNpc.builder()
					.npc(npc)
					.outline(true)
					.borderWidth(config.strongNpcOutlineWidth())
					.highlightColor(config.strongNpcOutlineColor())
					.render(n -> config.strongNpcOutline() && !npc.isDead())
					.build();
			case NpcID.CRYSTAL_BEAR:
			case NpcID.CRYSTAL_BEAR_HM:
				borderWidth = config.demibossOutlineWidth();
				highlightColor = config.bearOutlineColor();
				break;
			case NpcID.CRYSTAL_DARK_BEAST:
			case NpcID.CRYSTAL_DARK_BEAST_HM:
				borderWidth = config.demibossOutlineWidth();
				highlightColor = config.darkBeastOutlineColor();
				break;
			case NpcID.CRYSTAL_DRAGON:
			case NpcID.CRYSTAL_DRAGON_HM:
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
