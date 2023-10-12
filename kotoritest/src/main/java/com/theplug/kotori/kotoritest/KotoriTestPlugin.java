package com.theplug.kotori.kotoritest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLiteProperties;
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
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

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

	}

	@Subscribe
	private void onGameTick(GameTick gameTick)
	{

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
	
	}
	
	@Subscribe
	private void onAnimationChanged(AnimationChanged event)
	{
	
	}

	@Subscribe
	private void onItemContainerChanged(ItemContainerChanged event)
	{

	}
}

