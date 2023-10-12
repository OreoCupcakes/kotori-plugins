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
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

@Singleton
class MazeOverlay extends Overlay
{
	private final Client client;
	private final GauntletHelperConfig config;
	private final MazeModule mazeModule;
	private final ModelOutlineRenderer modelOutlineRenderer;
	private final ResourceManager resourceManager;

	@Inject
	MazeOverlay(
		final Client client,
		final GauntletHelperPlugin plugin,
		final GauntletHelperConfig config,
		final MazeModule mazeModule,
		final ModelOutlineRenderer modelOutlineRenderer,
		final ResourceManager resourceManager)
	{
		super(plugin);

		this.client = client;
		this.config = config;
		this.mazeModule = mazeModule;
		this.modelOutlineRenderer = modelOutlineRenderer;
		this.resourceManager = resourceManager;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGH);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	@Override
	public Dimension render(final Graphics2D graphics2D)
	{
		renderResources(graphics2D);
		renderUtilities();
		return null;
	}

	private void renderResources(final Graphics2D graphics2D)
	{
		if (!config.overlayResources() || mazeModule.getResourceGameObjects().isEmpty())
		{
			return;
		}

		for (final ResourceGameObject resourceGameObject : mazeModule.getResourceGameObjects())
		{
			if (!isOverlayEnabled(resourceGameObject.getResource()))
			{
				continue;
			}

			if (config.resourceTracker() &&
				config.resourceRemoveOutlineOnceAcquired() &&
				resourceManager.hasAcquired(resourceGameObject.getResource()))
			{
				continue;
			}

			final GameObject gameObject = resourceGameObject.getGameObject();

			final LocalPoint lp = gameObject.getLocalLocation();

			if (config.resourceHullOutlineWidth() > 0)
			{
				modelOutlineRenderer.drawOutline(gameObject, config.resourceHullOutlineWidth(),
					getResourceOutlineColor(resourceGameObject.getResource()), 1);
			}

			if (config.resourceTileOutlineWidth() > 0)
			{
				final Polygon polygon = Perspective.getCanvasTilePoly(client, lp);

				if (polygon != null)
				{
					OverlayUtil.renderPolygon(graphics2D, polygon, getResourceOutlineColor(resourceGameObject.getResource()),
						getResourceFillColor(resourceGameObject.getResource()), new BasicStroke(config.resourceTileOutlineWidth()));
				}
			}

			if (config.resourceIconSize() > 0)
			{
				OverlayUtil.renderImageLocation(client, graphics2D, lp, resourceGameObject.getIcon(), 0);
			}
		}
	}

	private void renderUtilities()
	{
		if (!config.utilitiesOutline() || mazeModule.getUtilities().isEmpty())
		{
			return;
		}

		for (final GameObject gameObject : mazeModule.getUtilities())
		{
			modelOutlineRenderer.drawOutline(gameObject, config.utilitiesOutlineWidth(),
				config.utilitiesOutlineColor(), 1);
		}
	}

	private Color getResourceOutlineColor(final Resource resource)
	{
		switch (resource)
		{
			case RAW_PADDLEFISH:
				return config.fishingSpotOutlineColor();
			case CRYSTAL_ORE:
			case CORRUPTED_ORE:
				return config.oreDepositOutlineColor();
			case PHREN_BARK:
			case CORRUPTED_PHREN_BARK:
				return config.phrenRootsOutlineColor();
			case LINUM_TIRINUM:
			case CORRUPTED_LINUM_TIRINUM:
				return config.linumTirinumOutlineColor();
			case GRYM_LEAF:
			case CORRUPTED_GRYM_LEAF:
				return config.grymRootOutlineColor();
			default:
				throw new IllegalArgumentException("Unsupported resource: " + resource);
		}
	}

	private Color getResourceFillColor(final Resource resource)
	{
		switch (resource)
		{
			case RAW_PADDLEFISH:
				return config.fishingSpotFillColor();
			case CRYSTAL_ORE:
			case CORRUPTED_ORE:
				return config.oreDepositFillColor();
			case PHREN_BARK:
			case CORRUPTED_PHREN_BARK:
				return config.phrenRootsFillColor();
			case LINUM_TIRINUM:
			case CORRUPTED_LINUM_TIRINUM:
				return config.linumTirinumFillColor();
			case GRYM_LEAF:
			case CORRUPTED_GRYM_LEAF:
				return config.grymRootFillColor();
			default:
				throw new IllegalArgumentException("Unsupported resource: " + resource);
		}
	}

	private boolean isOverlayEnabled(final Resource resource)
	{
		switch (resource)
		{
			case RAW_PADDLEFISH:
				return config.overlayFishingSpot();
			case CRYSTAL_ORE:
			case CORRUPTED_ORE:
				return config.overlayOreDeposit();
			case PHREN_BARK:
			case CORRUPTED_PHREN_BARK:
				return config.overlayPhrenRoots();
			case LINUM_TIRINUM:
			case CORRUPTED_LINUM_TIRINUM:
				return config.overlayLinumTirinum();
			case GRYM_LEAF:
			case CORRUPTED_GRYM_LEAF:
				return config.overlayGrymRoot();
			default:
				throw new IllegalArgumentException("Unsupported resource: " + resource);
		}
	}
}
