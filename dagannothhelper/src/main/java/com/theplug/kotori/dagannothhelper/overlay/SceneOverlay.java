/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2020, dutta64 <https://github.com/dutta64>
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
package com.theplug.kotori.dagannothhelper.overlay;

import com.theplug.kotori.dagannothhelper.DagannothHelperConfig;
import com.theplug.kotori.dagannothhelper.DagannothHelperPlugin;
import com.theplug.kotori.dagannothhelper.entity.DagannothKing;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.Set;

@Singleton
public class SceneOverlay extends Overlay
{
	private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

	private final DagannothHelperPlugin plugin;
	private final DagannothHelperConfig config;
	private final Client client;

	private Set<DagannothKing> dagannothKings;

	@Inject
	public SceneOverlay(final DagannothHelperPlugin plugin, final DagannothHelperConfig config, final Client client)
	{
		super(plugin);

		this.plugin = plugin;
		this.config = config;
		this.client = client;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGH);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	@Override
	public Dimension render(final Graphics2D graphics2D)
	{
		dagannothKings = plugin.getDagannothKings();

		if (dagannothKings.isEmpty())
		{
			return null;
		}

		renderNpcTickCount(graphics2D);

		return null;
	}

	private void renderNpcTickCount(final Graphics2D graphics2D)
	{
		final Actor player = client.getLocalPlayer();

		if (!config.showNpcTickCounter() || player == null)
		{
			return;
		}

		for (final DagannothKing dagannothKing : dagannothKings)
		{
			final NPC npc = dagannothKing.getNpc();

			final int ticksUntilNextAnimation = dagannothKing.getTicksUntilNextAnimation();

			if (npc == null || npc.isDead() || ticksUntilNextAnimation <= 0 || (config.ignoringNonAttacking()
				&& dagannothKing.getInteractingActor() != player))
			{
				continue;
			}

			final String text = String.valueOf(ticksUntilNextAnimation);

			final Point npcPoint = npc.getCanvasTextLocation(graphics2D, text, 0);

			if (npcPoint == null)
			{
				return;
			}

			OverlayUtil.renderTextLocation(graphics2D, text, 20, Font.BOLD,
				ticksUntilNextAnimation == 1 ? Color.WHITE : dagannothKing.getColor(), npcPoint, true, 0);
		}
	}
}
