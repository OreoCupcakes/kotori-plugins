/*
 * Copyright (c) 2019, Lucas <https://github.com/Lucwousin>
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
package com.theplug.kotori.specbar;

import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.events.ClientTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetPositionMode;
import net.runelite.api.widgets.WidgetSizeMode;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
	name = "<html><font color=#6b8af6>[P]</font> Spec Bar</html>",
	enabledByDefault = false,
	description = "Adds a spec bar to every weapon.",
	tags = {"spec bar", "special attack", "spec", "bar", "pklite", "kotori", "ported"}
)
public class SpecBarPlugin extends Plugin
{
	@Inject
	private Client client;

	private static final int specBarGroupId = 593;

	private static final int specBarWidth = 146;

	@Subscribe
	private void onClientTick(ClientTick event)
	{
		Widget specBar = client.getWidget(specBarGroupId,35);
		Widget specBarText = client.getWidget(specBarGroupId,40);
		Widget specBarChargeBar1 = client.getWidget(specBarGroupId,38);
		Widget specBarChargeBar2 = client.getWidget(specBarGroupId,39);
		if (specBar != null)
		{
			specBar.setHidden(false);
			if (specBarText != null)
			{
				specBarText.setHidden(false);
				int currentSpecValue = client.getVarpValue(300) / 10;
				specBarText.setText("Special Attack: " + currentSpecValue + "%");
				if (specBarChargeBar1 != null && specBarChargeBar2 != null)
				{
					int currentChargeBarWidth = specBarWidth * currentSpecValue / 100;
					specBarChargeBar1.setSize(currentChargeBarWidth,12);
					specBarChargeBar1.revalidate();
					specBarChargeBar2.revalidate();
				}
			}
		}
	}
}
