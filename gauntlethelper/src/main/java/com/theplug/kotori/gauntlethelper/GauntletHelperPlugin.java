/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2023, rdutta <https://github.com/rdutta>
 * Copyright (c) 2019, kThisIsCvpv <https://github.com/kThisIsCvpv>
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * Copyright (c) 2019, kyle <https://github.com/Kyleeld>
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

package com.theplug.kotori.gauntlethelper;

import com.theplug.kotori.gauntlethelper.module.boss.BossModule;
import com.theplug.kotori.gauntlethelper.module.maze.MazeModule;
import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
	name = "<html><font color=#6b8af6>[K]</font> Gauntlet Helper</html>",
	description = "All-in-one plugin for The Gauntlet.",
	tags = {"the", "gauntlet"},
	enabledByDefault = false
)
public final class GauntletHelperPlugin extends Plugin
{
	private static final int VARBIT_MAZE = 9178;
	private static final int VARBIT_BOSS = 9177;

	@Inject
	private Client client;
	@Inject
	private ClientThread clientThread;
	@Inject
	private MazeModule mazeModule;
	@Inject
	private BossModule bossModule;

	@Provides
	GauntletHelperConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(GauntletHelperConfig.class);
	}

	@Override
	protected void startUp()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		clientThread.invoke(() -> {
			if (client.getVarbitValue(VARBIT_BOSS) == 1)
			{
				bossModule.start();
			}
			else if (client.getVarbitValue(VARBIT_MAZE) == 1)
			{
				mazeModule.start();
			}
		});
	}

	@Override
	protected void shutDown()
	{
		mazeModule.stop();
		bossModule.stop();
	}

	@Subscribe
	void onVarbitChanged(final VarbitChanged event)
	{
		final int varbit = event.getVarbitId();

		if (varbit == VARBIT_MAZE)
		{
			if (event.getValue() == 1)
			{
				mazeModule.start();
			}
			else
			{
				mazeModule.stop();
			}
		}
		else if (varbit == VARBIT_BOSS)
		{
			if (event.getValue() == 1)
			{
				mazeModule.stop();
				bossModule.start();
			}
			else
			{
				bossModule.stop();
			}
		}
	}
}
