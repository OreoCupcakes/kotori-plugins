/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2023, rdutta <https://github.com/rdutta>
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

package com.theplug.kotori.gauntlethelper.module.maze;

import com.theplug.kotori.gauntlethelper.GauntletHelperConfig;
import com.theplug.kotori.gauntlethelper.GauntletHelperPlugin;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
class MinimapOverlay extends Overlay
{
	private final GauntletHelperConfig config;
	private final MazeModule mazeModule;
	private final ResourceManager resourceManager;

	@Inject
	MinimapOverlay(
		final GauntletHelperPlugin plugin,
		final GauntletHelperConfig config,
		final MazeModule mazeModule,
		final ResourceManager resourceManager)
	{
		super(plugin);

		this.config = config;
		this.mazeModule = mazeModule;
		this.resourceManager = resourceManager;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGH);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(final Graphics2D graphics2D)
	{
		if (config.minimapResourceOverlay())
		{
			renderMinimapResourceIcons(graphics2D, mazeModule.getResourceGameObjects());
		}

		if (config.minimapDemibossOverlay())
		{
			renderMinimapNPCIcons(graphics2D, mazeModule.getDemiBosses());
		}

		return null;
	}

	private void renderMinimapNPCIcons(final Graphics2D graphics2D, final Collection<Demiboss> demiBosses)
	{
		if (demiBosses.isEmpty())
		{
			return;
		}

		for (final Demiboss demiboss : demiBosses)
		{
			final Point point = demiboss.getMinimapPoint();

			if (point == null)
			{
				continue;
			}

			OverlayUtil.renderImageLocation(graphics2D, point, demiboss.getMinimapIcon());
		}
	}

	private void renderMinimapResourceIcons(final Graphics2D graphics2D, final Collection<ResourceGameObject> resourceGameObjects)
	{
		if (resourceGameObjects.isEmpty())
		{
			return;
		}

		for (final ResourceGameObject resourceGameObject : resourceGameObjects)
		{
			if (config.resourceTracker() &&
				config.resourceRemoveOutlineOnceAcquired() &&
				resourceManager.hasAcquired(resourceGameObject.getResource()))
			{
				continue;
			}

			final Point point = resourceGameObject.getMinimapPoint();

			if (point == null)
			{
				continue;
			}

			OverlayUtil.renderImageLocation(graphics2D, point, resourceGameObject.getMinimapIcon());
		}
	}
}
