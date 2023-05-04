package com.theplug.kotori.kotoritest;

import com.google.inject.Provides;
import com.theplug.kotori.kotoritest.kotoriutils.KotoriUtils;
import com.theplug.kotori.kotoritest.kotoriutils.KotoriUtilsConfig;
import com.theplug.kotori.kotoritest.kotoriutils.rlapi.WidgetInfoPlus;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "Kotori Test",
	description = "Kotori Test plugin for experimental features.",
	tags = {"kotori","test","ported","experimental","features"}
)
@Slf4j
public class KotoriTestPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private KotoriUtils kotoriUtils;
	@Inject
	private KeyManager keyManager;
	@Inject
	private KotoriTestConfig config;
	

	@Provides
	KotoriTestConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(KotoriTestConfig.class);
	}

	@Override
	protected void startUp()
	{
	
	}

	@Override
	protected void shutDown()
	{
	
	}
	
	public void init()
	{
	
	}
	
	@Subscribe
	private void onGameStateChanged(GameStateChanged event)
	{
		final GameState gameState = event.getGameState();
	}

	@Subscribe
	private void onGameTick(GameTick gameTick)
	{
		if (jad != null)
		{
			System.out.println("GameTick - Jad Animation ID: " + jad.getAnimation());
		}
	}
	
	@Subscribe
	private void onClientTick(ClientTick event)
	{
	
	}
	
	@Subscribe
	private void onMenuEntryAdded(MenuEntryAdded event)
	{
	
	}
	
	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event)
	{
	
	}
	
	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
	
	}
	
	@Subscribe
	private void onGraphicChanged(GraphicChanged event)
	{
	
	}
	
	@Subscribe
	private void onNpcSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();
		
		if (jadIds.contains(npc.getId()))
		{
			jad = npc;
		}
	}
	
	private static final Set<Integer> cerberusIds = Set.of(NpcID.CERBERUS, NpcID.CERBERUS_5863, NpcID.CERBERUS_5866);
	
	@Subscribe
	private void onAnimationChanged(AnimationChanged event)
	{
		Actor actor = event.getActor();
		
		if (!(actor instanceof NPC))
		{
			return;
		}
		
		NPC npc = (NPC) actor;
		if (jadIds.contains(npc.getId()))
		{
			System.out.println("AnimationChanged Actor Animation ID: " + actor.getAnimation());
			System.out.println("AnimationChanged NPC Animation ID: " + npc.getAnimation());
		}
	}
	
	private final Set<Integer> jadIds = Set.of(NpcID.JALTOKJAD, NpcID.JALTOKJAD_7704, NpcID.JALTOKJAD_10623, NpcID.TZTOKJAD, NpcID.TZTOKJAD_6506);
	NPC jad;
}
