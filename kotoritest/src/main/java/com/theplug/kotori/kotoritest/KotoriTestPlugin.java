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
		valid = false;
		keyManager.unregisterKeyListener(hotkey);
	}
	
	public void init()
	{
		valid = true;
		keyManager.registerKeyListener(hotkey);
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
		if (hotkeyOn)
		{
			if (activationNum > 1)
			{
				return;
			}
			fakeClick = true;
			kotoriUtils.getMenusLibrary().insertMenuEntry(client.getMenuEntries().length-1, "Activate", "Thick Skin", MenuAction.CC_OP.getId(), 1, -1, WidgetInfoPlus.PRAYER_THICK_SKIN.getId(), -1);
			MouseEvent eventPressed = new MouseEvent(client.getCanvas(), MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 1, false,1);
			MouseEvent eventReleased = new MouseEvent(client.getCanvas(), MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, 0, 0, 1, false,1);
			MouseEvent eventClicked = new MouseEvent(client.getCanvas(), MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false,1);
			client.getCanvas().dispatchEvent(eventPressed);
			client.getCanvas().dispatchEvent(eventReleased);
			client.getCanvas().dispatchEvent(eventClicked);
			activationNum++;
		}
	}
	
	@Subscribe
	private void onMenuEntryAdded(MenuEntryAdded event)
	{
	
	}
	
	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!fakeClick)
		{
			return;
		}
		
		int index = kotoriUtils.getMenusLibrary().getMenuEntryIndex(event.getMenuEntry());
		kotoriUtils.getMenusLibrary().insertMenuEntry(index, "Activate", "Thick Skin", MenuAction.CC_OP.getId(), 1, -1, WidgetInfoPlus.PRAYER_THICK_SKIN.getId(), -1);
	//	event.consume();
		fakeClick = false;
	}
	
	private final HotkeyListener hotkey = new HotkeyListener(() -> config.keybind())
	{
		@Override
		public void hotkeyPressed()
		{
			hotkeyOn = true;
		}
		
		@Override
		public void hotkeyReleased()
		{
			hotkeyOn = false;
			activationNum = 0;
		}
	};
	
	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
	
	}
}
