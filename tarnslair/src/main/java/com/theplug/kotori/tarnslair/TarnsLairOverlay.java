/*
 * Copyright (c) 2018, Shaun Dreclin <https://github.com/ShaunDreclin>
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
package com.theplug.kotori.tarnslair;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
@Slf4j
public class TarnsLairOverlay extends Overlay
{
	private static final int MAX_DISTANCE = 2350;
	
	private static final WorldPoint staircase0 = new WorldPoint(3178,4561,0);
	private static final WorldPoint staircase1 = new WorldPoint(3171,4569,1);
	private static final WorldPoint staircase2 = new WorldPoint(3163,4564,0);
	private static final WorldPoint staircase3 = new WorldPoint(3160,4559,0);
	private static final WorldPoint toadBattaWorldPoint = new WorldPoint(3139,4554,0);
	private static final Set<WorldPoint> worldpoints = Set.of(staircase0, staircase1, staircase2, staircase3);
	private static final List<WorldPoint> path1 = Arrays.asList(
			new WorldPoint(3166, 4547, 0),
			new WorldPoint(3166, 4550, 0),
			new WorldPoint(3184, 4550, 0),
			new WorldPoint(3184, 4547, 0),
			new WorldPoint(3190, 4547, 0),
			new WorldPoint(3190, 4561, 0),
			new WorldPoint(3178, 4561, 0));
	
	private static final List<WorldPoint> path2 = Arrays.asList(
			new WorldPoint(3174, 4561, 1),
			new WorldPoint(3174, 4569, 1),
			new WorldPoint(3171, 4569, 1));
	
	private static final List<WorldPoint> path3 = Arrays.asList(
			new WorldPoint(3167, 4569, 0),
			new WorldPoint(3163, 4569, 0),
			new WorldPoint(3163, 4564, 0));
	
	private static final List<WorldPoint> path4 = Arrays.asList(
			new WorldPoint(3163, 4560, 0),
			new WorldPoint(3163, 4559, 0),
			new WorldPoint(3160, 4559, 0));
	
	private static final List<WorldPoint> path5 = Arrays.asList(
			new WorldPoint(3155, 4559, 0),
			new WorldPoint(3151, 4559, 0),
			new WorldPoint(3151, 4556, 0),
			new WorldPoint(3144, 4556, 0),
			new WorldPoint(3144, 4554, 0),
			new WorldPoint(3139, 4554, 0));
	

	private final Client client;
	private final TarnsLairPlugin plugin;
	@Inject
	private TarnsLairConfig config;

	@Inject
	public TarnsLairOverlay(final Client client, final TarnsLairPlugin plugin)
	{
		this.client = client;
		this.plugin = plugin;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!plugin.isInTarnsLair())
		{
			return null;
		}

		LocalPoint playerLocation = client.getLocalPlayer().getLocalLocation();

		plugin.getStaircases().forEach((obstacle, tile) ->
		{
			if (tile.getPlane() == client.getPlane() && obstacle.getLocalLocation().distanceTo(playerLocation) < MAX_DISTANCE)
			{
				Shape p = tile.getGameObjects()[0].getConvexHull();
				if (p != null)
				{
					if (worldpoints.contains(tile.getWorldLocation()))
					{
						if (config.toadBattaFastestPath())
						{
							graphics.setColor(Color.MAGENTA);
							graphics.draw(p);
							Point staircasePoint = Perspective.getCanvasTextLocation(client, graphics, tile.getLocalLocation(), "Enter here", 0);
							OverlayUtil.renderTextLocation(graphics, staircasePoint, "Enter here", Color.MAGENTA);
						}
						else
						{
							graphics.setColor(Color.GREEN);
							graphics.draw(p);
						}
					}
					else
					{
						graphics.setColor(Color.GREEN);
						graphics.draw(p);
					}
				}
			}
		});

		plugin.getWallTraps().forEach((obstacle, tile) ->
		{
			if (tile.getPlane() == client.getPlane() && obstacle.getLocalLocation().distanceTo(playerLocation) < MAX_DISTANCE)
			{
				Shape p = tile.getGameObjects()[0].getConvexHull();
				if (p != null)
				{
					graphics.setColor(Color.CYAN);
					graphics.draw(p);
				}
			}
		});

		plugin.getFloorTraps().forEach((obstacle, tile) ->
		{
			if (tile.getPlane() == client.getPlane() && obstacle.getLocalLocation().distanceTo(playerLocation) < MAX_DISTANCE)
			{
				Polygon p = obstacle.getCanvasTilePoly();
				if (p != null)
				{
					graphics.setColor(Color.CYAN);
					graphics.drawPolygon(p);
				}
			}
		});
		
		if (config.toadBattaFastestPath())
		{
			if (plugin.toadBattaTile != null)
			{
				OverlayUtil.renderTileOverlay(graphics, plugin.toadBattaTile.getGroundObject(), "Toad Batta", Color.MAGENTA);
			}
			
			WorldLines.drawLinesOnWorld(graphics, client, path1, Color.MAGENTA);
			WorldLines.drawLinesOnWorld(graphics, client, path2, Color.MAGENTA);
			WorldLines.drawLinesOnWorld(graphics, client, path3, Color.MAGENTA);
			WorldLines.drawLinesOnWorld(graphics, client, path4, Color.MAGENTA);
			WorldLines.drawLinesOnWorld(graphics, client, path5, Color.MAGENTA);
		}

		return null;
	}
}
