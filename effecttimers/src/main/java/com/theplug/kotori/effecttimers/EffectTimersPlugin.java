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

import com.google.inject.Provides;
import com.theplug.kotori.kotoriutils.methods.MiscUtilities;
import com.theplug.kotori.effecttimers.utils.PvPUtil;
import com.theplug.kotori.effecttimers.utils.WorldTypeExtended;
import com.theplug.kotori.kotoriutils.KotoriUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.kit.KitType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import org.apache.commons.lang3.ArrayUtils;

import javax.inject.Inject;
import java.util.*;

@Slf4j
@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "<html><font color=#6b8af6>[P]</font> Effect Timers</html>",
	description = "Effect timer overlay on players",
	tags = {"freeze", "timers", "barrage", "teleblock", "pklite", "ported", "kotori"}
)
public class EffectTimersPlugin extends Plugin
{
	private static final int VORKATH_REGION = 9023;
	public final Set<Integer> swampBarkArmorIds = Set.of(ItemID.SWAMPBARK_BODY, ItemID.SWAMPBARK_HELM, ItemID.SWAMPBARK_LEGS);

	@Inject
	@Getter
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PrayerTracker prayerTracker;

	@Inject
	private TimerManager timerManager;

	@Inject
	private EffectTimersOverlay overlay;

	@Getter
	@Inject
	private EffectTimersConfig config;

	@Inject
	private KeyManager keyManager;

	private int fakeSpotAnim = -1;
	private boolean initializedPlugin;
	
	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.debugKeybind())
	{
		public void hotkeyPressed()
		{
			fakeSpotAnim = config.debugInteger();
		}
	};

	@Provides
	private EffectTimersConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(EffectTimersConfig.class);
	}

	@Override
	public void startUp()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}
		
		init();
	}

	@Override
	public void shutDown()
	{
		keyManager.unregisterKeyListener(hotkeyListener);
		overlayManager.remove(overlay);
		timerManager.shutDown();
		prayerTracker.shutDown();
		initializedPlugin = false;
	}
	
	private void init()
	{
		overlayManager.add(overlay);
		keyManager.registerKeyListener(hotkeyListener);
		initializedPlugin = true;
	}

	@Subscribe
	private void onGameTick(GameTick gameTick)
	{
		prayerTracker.gameTick();

		if (fakeSpotAnim != -1)
		{
			GraphicChanged event = new GraphicChanged();
			event.setActor(client.getLocalPlayer());
			onGraphicChanged(event);
		}

		EnumSet<WorldType> worldTypes = client.getWorldType();

		for (Actor actor : client.getPlayers())
		{
			if (!timerManager.hasTimerActive(actor, TimerType.TELEBLOCK))
			{
				continue;
			}

			WorldPoint actorLoc = actor.getWorldLocation();

			if (!WorldTypeExtended.isAllPvpWorld(worldTypes) && PvPUtil.getWildernessLevelFrom(actorLoc) <= 0)
			{
				timerManager.removeTimerFor(actor, TimerType.TELEBLOCK);
			}
			else if (WorldType.isPvpWorld(worldTypes) &&
				MapLocations.getPvpSafeZones(actorLoc.getPlane()).contains(actorLoc.getX(), actorLoc.getY()))
			{
				timerManager.removeTimerFor(actor, TimerType.TELEBLOCK);
			}
			else if (WorldTypeExtended.isDeadmanWorld(worldTypes) &&
				MapLocations.getDeadmanSafeZones(actorLoc.getPlane()).contains(actorLoc.getX(), actorLoc.getY()))
			{
				timerManager.removeTimerFor(actor, TimerType.TELEBLOCK);
			}
		}
	}

	@Subscribe
	private void onGraphicChanged(GraphicChanged event)
	{
		//GraphicChanged seems to happen on tick before the game registers your interaction with the actor so getInteracting will always return null.
		Actor actorWithGraphic = event.getActor();
		//	actor.getGraphic() deprecated, actor can have multiple spot anims.
		//	int spotAnim = fakeSpotAnim == -1 ? actor.getGraphic() : fakeSpotAnim;
		IterableHashTable<ActorSpotAnim> actorSpotAnimsTable = actorWithGraphic.getSpotAnims();
		for (ActorSpotAnim actorSpotAnim : actorSpotAnimsTable)
		{
			int spotAnim;
			if (fakeSpotAnim == -1)
			{
				spotAnim = actorSpotAnim.getId();
			}
			else
			{
				spotAnim = fakeSpotAnim;
			}
			fakeSpotAnim = -1;
			
			if (spotAnim == prayerTracker.getSpotanimLastTick(event.getActor()))
			{
				return;
			}
			
			PlayerEffect effect = PlayerEffect.getFromSpotAnim(spotAnim);
			
			if (effect == null)
			{
				return;
			}
			
			if (timerManager.hasTimerActive(actorWithGraphic, effect.getType()))
			{
				return;
			}
			
			timerManager.addTimerFor(actorWithGraphic, effect.getType(), new Timer(this, effect,
					effect.isHalvable() && prayerTracker.getPrayerIconLastTick(actorWithGraphic) == HeadIcons.MAGIC, actorWithGraphic));
		}
	}

	@Subscribe
	private void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.GAMEMESSAGE
			|| !event.getMessage().contains("Your Tele Block has been removed"))
		{
			return;
		}

		timerManager.jumpToCooldown(client.getLocalPlayer(), TimerType.TELEBLOCK);
	}

	private boolean isAtVorkath()
	{
		return ArrayUtils.contains(client.getMapRegions(), VORKATH_REGION);
	}
	
	@Subscribe
	private void onNpcDespawned(NpcDespawned event)
	{
		if (!isAtVorkath())
		{
			return;
		}

		final NPC npc = event.getNpc();

		if (npc.getName() == null)
		{
			return;
		}

		if (npc.getName().contains("Zombified Spawn"))
		{
			// TODO: not sure if we're meant to jump to cooldown here or just remove the timer completely, doesn't mechanically make a difference though
			timerManager.removeTimerFor(client.getLocalPlayer(), TimerType.FREEZE);
		}
	}

	@Subscribe
	private void onActorDeath(ActorDeath event)
	{
		for (TimerType type : TimerType.values())
		{
			timerManager.removeTimerFor(event.getActor(), type);
		}
	}
	
	@Subscribe
	private void onGameStateChanged(GameStateChanged event)
	{
		GameState gameState = event.getGameState();
		
		switch (gameState)
		{
			case LOGIN_SCREEN:
			case HOPPING:
			case CONNECTION_LOST:
				if (initializedPlugin)
				{
					shutDown();
				}
				break;
			case LOGGED_IN:
				if (!initializedPlugin)
				{
					init();
				}
				timerManager.clearExpiredTimers();
				break;
		}
	}
	
	public void debugMsg(String message)
	{
		if (config.debugLogging())
		{
			System.out.println(message);
			log.info(message);
			MiscUtilities.sendGameMessage(message);
		}
	}
}
