package com.theplug.kotori.kotoritest;

import com.google.inject.Provides;
import com.theplug.kotori.kotoritest.kotoriutils.KotoriUtils;
import com.theplug.kotori.kotoritest.kotoriutils.KotoriUtilsConfig;
import com.theplug.kotori.kotoritest.kotoriutils.rlapi.WidgetInfoPlus;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
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
	private boolean valid;
	
	private boolean hotkeyOn;
	private boolean fakeClick;
	private int activationNum = 0;
	

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
		GameState gameState = event.getGameState();
		
		switch (gameState)
		{
			case LOGGED_IN:
				if (!valid)
				{
					init();
				}
				break;
			case LOGIN_SCREEN:
			case HOPPING:
			case CONNECTION_LOST:
				if (valid)
				{
					shutDown();
				}
				break;
			default:
				break;
		}
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
}
