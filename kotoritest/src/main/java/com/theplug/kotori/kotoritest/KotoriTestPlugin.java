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
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
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
		keyManager.registerKeyListener(hotkey);
	}

	@Override
	protected void shutDown()
	{
		keyManager.unregisterKeyListener(hotkey);
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
		try
		{
			Class<?> clazz = client.getClass().getClassLoader().loadClass("client");
			Field field = clazz.getDeclaredField("ns");
			field.setAccessible(true);
			int option = field.getInt(clazz) * -54831947;
			field.setAccessible(false);
			System.out.println("Menu Options Count: " + option);
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
	
	}
	
	@Subscribe
	private void onAnimationChanged(AnimationChanged event)
	{
	
	}
	
	private final HotkeyListener hotkey = new HotkeyListener(() -> config.keybind())
	{
		@Override
		public void hotkeyPressed()
		{
		
		}
		
		@Override
		public void hotkeyReleased()
		{
		
		}
	};
	
		static  public void gcdExtended(long a, long b)
		{
			long x = 0, y = 1, lastx = 1, lasty = 0, temp;
			while (b != 0)
			{
				long q = a / b;
				long r = a % b;
				
				a = b;
				b = r;
				
				temp = x;
				x = lastx - q * x;
				lastx = temp;
				
				temp = y;
				y = lasty - q * y;
				lasty = temp;
			}
			System.out.println("GCD "+a+" and its Roots  x : "+ (int) lastx +" y :"+ (int )lasty);
		}
	public static long[] egcd(long a, long b) {
		if (b == 0) return new long[] {a, 1, 0};
		else {
			long[] ret = egcd(b, a % b);
			long tmp = ret[1] - ret[2] * (a / b);
			ret[1] = ret[2];
			ret[2] = tmp;
			return ret;
		}
	}
}
