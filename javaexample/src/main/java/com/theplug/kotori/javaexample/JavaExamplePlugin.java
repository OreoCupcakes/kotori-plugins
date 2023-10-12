package com.theplug.kotori.javaexample;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;


@Slf4j
@Singleton
@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "Java example",
	description = "Java example",
	enabledByDefault = false,
	tags = {""}
)
public class JavaExamplePlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private JavaExampleConfig config;

	@Provides
	JavaExampleConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(JavaExampleConfig.class);
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
	private void onGameStateChanged(final GameStateChanged event)
	{
		GameState gameState = event.getGameState();

		switch (gameState)
		{
			case LOGGED_IN:
				break;
			case LOGIN_SCREEN:
			case HOPPING:
				break;
		}
	}

	@Subscribe
	private void onGameTick(final GameTick event)
	{

	}
}