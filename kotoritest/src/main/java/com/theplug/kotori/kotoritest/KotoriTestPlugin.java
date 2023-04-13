package com.theplug.kotori.kotoritest;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.awt.*;
import java.awt.event.MouseEvent;

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
	
	private final HotkeyListener hotkey = new HotkeyListener(() -> config.keybind())
	{
		@Override
		public void hotkeyPressed()
		{
			super.hotkeyPressed();
		}
		
		@Override
		public void hotkeyReleased()
		{
			super.hotkeyReleased();
		}
	};
	
	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
	
	}
}
