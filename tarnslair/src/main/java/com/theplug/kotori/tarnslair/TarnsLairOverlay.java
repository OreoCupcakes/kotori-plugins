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
import java.util.Objects;
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
	
	private static final WorldPoint fastPassageway0 = new WorldPoint(3178,4561,0);
	private static final WorldPoint fastPassageway1 = new WorldPoint(3171,4569,1);
	private static final WorldPoint fastPassageway2 = new WorldPoint(3163,4564,0);
	private static final WorldPoint fastAndEfficientLastPassageway = new WorldPoint(3160,4559,0);
	private static final WorldPoint efficientStaircase1 = new WorldPoint(3158,4553,0);
	private static final WorldPoint slayerRingPassageway1 = new WorldPoint(3190,4598,0);
	private static final WorldPoint slayerRingPassageway2 = new WorldPoint(3178,4598,1);
	private static final WorldPoint slayerRingPassageway3 = new WorldPoint(3168,4596,0);
	private static final WorldPoint slayerRingPassageway4 = new WorldPoint(3168,4585,0);
	private static final WorldPoint slayerRingPassageway5 = new WorldPoint(3165,4577,0);
	private static final WorldPoint slayerRingPassageway6 = new WorldPoint(3150,4583,0);
	private static final WorldPoint slayerRingPassageway7 = new WorldPoint(3144,4581,1);
	private static final WorldPoint slayerRingPassageway8 = new WorldPoint(3141,4564,1);
	private static final WorldPoint efficientStandOnWallTrapTile = new WorldPoint(3165,4559,1);
	private static final WorldPoint slayerRingWallTrapTile1 = new WorldPoint(3173,4600,1);
	private static final WorldPoint slayerRingPillarTile1 = new WorldPoint(3188,4596,1);
	private static final WorldPoint slayerRingPillarTile2 = new WorldPoint(3186,4596,1);
	private static final WorldPoint slayerRingPillarTile3 = new WorldPoint(3186,4598,1);
	private static final WorldPoint slayerRingPillarTile4 = new WorldPoint(3184,4598,1);
	private static final WorldPoint slayerRingPillarTile5 = new WorldPoint(3184,4600,1);
	private static final WorldPoint slayerRingPillarTile6 = new WorldPoint(3182,4600,1);
	private static final WorldPoint slayerRingPillarTile7 = new WorldPoint(3144,4574,2);
	private static final WorldPoint slayerRingFloorTrapTile = new WorldPoint(3141,4558,2);
	private static final Set<WorldPoint> slayerRingPathTileSet = Set.of(slayerRingWallTrapTile1, slayerRingPillarTile1, slayerRingPillarTile2, slayerRingPillarTile3, slayerRingPillarTile4,
			slayerRingPillarTile5, slayerRingPillarTile6, slayerRingPillarTile7, slayerRingFloorTrapTile);
	private static final WorldPoint toadBattaWorldPoint = new WorldPoint(3139,4554,0);
	private static final Set<WorldPoint> fastPathPassageways = Set.of(fastPassageway0, fastPassageway1, fastPassageway2, fastAndEfficientLastPassageway);
	private static final Set<WorldPoint> efficientPathPassageways = Set.of(efficientStaircase1, fastAndEfficientLastPassageway);
	private static final Set<WorldPoint> slayerRingPathPassageways = Set.of(slayerRingPassageway1, slayerRingPassageway2, slayerRingPassageway3, slayerRingPassageway4,
			slayerRingPassageway5, slayerRingPassageway6, slayerRingPassageway7, slayerRingPassageway8);
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
	
	private static final List<WorldPoint> fastAndEfficientLastPath = Arrays.asList(
			new WorldPoint(3155, 4559, 0),
			new WorldPoint(3151, 4559, 0),
			new WorldPoint(3151, 4556, 0),
			new WorldPoint(3144, 4556, 0),
			new WorldPoint(3144, 4554, 0),
			new WorldPoint(3139, 4554, 0));
	
	private static final Set<List<WorldPoint>> fastPathSet = Set.of(path1, path2, path3, path4, fastAndEfficientLastPath);
	
	private static final List<WorldPoint> efficientPath1 = Arrays.asList(
			new WorldPoint(3166, 4547, 0),
			new WorldPoint(3166, 4551, 0),
			new WorldPoint(3158, 4551, 0),
			new WorldPoint(3158, 4553, 0));
	
	private static final List<WorldPoint> efficientPath2 = Arrays.asList(
			new WorldPoint(3158, 4557, 1),
			new WorldPoint(3158, 4561, 1),
			new WorldPoint(3165, 4561, 1),
			new WorldPoint(3165, 4559, 1));
	
	private static final List<WorldPoint> efficientPath3 = Arrays.asList(
			new WorldPoint(3164, 4559, 0),
			new WorldPoint(3160, 4559, 0));
	
	private static final Set<List<WorldPoint>> efficientPathSet = Set.of(efficientPath1, efficientPath2, efficientPath3, fastAndEfficientLastPath);
	
	private static final List<WorldPoint> slayerRingPath1 = Arrays.asList(
			new WorldPoint(3185, 4601, 0),
			new WorldPoint(3185, 4598, 0),
			new WorldPoint(3190, 4598, 0));
	
	private static final List<WorldPoint> slayerRingPath2 = Arrays.asList(
			new WorldPoint(3194, 4598, 1),
			new WorldPoint(3194, 4596, 1),
			new WorldPoint(3186, 4596, 1),
			new WorldPoint(3186, 4598, 1),
			new WorldPoint(3184, 4598, 1),
			new WorldPoint(3184, 4600, 1),
			new WorldPoint(3180, 4600, 1),
			new WorldPoint(3180, 4598, 1),
			new WorldPoint(3178, 4598, 1));
	
	private static final List<WorldPoint> slayerRingPath3 = Arrays.asList(
			new WorldPoint(3175, 4598, 1),
			new WorldPoint(3175, 4600, 1),
			new WorldPoint(3173, 4600, 1));
	
	private static final List<WorldPoint> slayerRingPath4 = Arrays.asList(
			new WorldPoint(3173, 4599, 0),
			new WorldPoint(3170, 4599, 0),
			new WorldPoint(3168,4597,0),
			new WorldPoint(3168, 4596, 0));
	
	private static final List<WorldPoint> slayerRingPath5 = Arrays.asList(
			new WorldPoint(3168, 4592, 0),
			new WorldPoint(3168, 4585, 0));
	
	private static final List<WorldPoint> slayerRingPath6 = Arrays.asList(
			new WorldPoint(3168, 4579, 0),
			new WorldPoint(3168, 4577, 0),
			new WorldPoint(3165, 4577, 0));
	
	private static final List<WorldPoint> slayerRingPath7 = Arrays.asList(
			new WorldPoint(3161, 4577, 0),
			new WorldPoint(3158, 4577, 0),
			new WorldPoint(3158, 4583, 0),
			new WorldPoint(3150, 4583, 0));
	
	private static final List<WorldPoint> slayerRingPath8 = Arrays.asList(
			new WorldPoint(3146, 4583, 1),
			new WorldPoint(3144, 4583, 1),
			new WorldPoint(3144, 4581, 1));
	
	private static final List<WorldPoint> slayerRingPath9 = Arrays.asList(
			new WorldPoint(3144, 4577, 2),
			new WorldPoint(3144, 4574, 2));
	
	private static final List<WorldPoint> slayerRingPath10 = Arrays.asList(
			new WorldPoint(3145, 4574, 1),
			new WorldPoint(3151, 4574, 1),
			new WorldPoint(3151, 4565, 1),
			new WorldPoint(3141, 4565, 1),
			new WorldPoint(3141,4564,1));
	
	private static final List<WorldPoint> slayerRingPath11 = Arrays.asList(
			new WorldPoint(3141, 4560, 2),
			new WorldPoint(3141, 4558, 2));
	
	private static final List<WorldPoint> slayerRingPath12 = Arrays.asList(
			new WorldPoint(3141, 4558, 0),
			new WorldPoint(3141, 4554, 0),
			new WorldPoint(3139, 4554, 0));
	
	private static final Set<List<WorldPoint>> slayerRingPathSet = Set.of(slayerRingPath1, slayerRingPath2, slayerRingPath3, slayerRingPath4, slayerRingPath5,
			slayerRingPath6, slayerRingPath7, slayerRingPath8, slayerRingPath9, slayerRingPath10, slayerRingPath11, slayerRingPath12);

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
				boolean useDefaultGraphic = true;
				Shape p = tile.getGameObjects()[0].getConvexHull();
				if (p != null)
				{
					if (config.fastestPath())
					{
						if (efficientPathPassageways.contains(tile.getWorldLocation()))
						{
							graphics.setColor(config.fastestPathColor());
							graphics.draw(p);
							useDefaultGraphic = false;
						}
					}
					if (config.slayerRingPath())
					{
						if (slayerRingPathPassageways.contains(tile.getWorldLocation()))
						{
							graphics.setColor(config.slayerRingPathColor());
							graphics.draw(p);
							useDefaultGraphic = false;
						}
					}
					if (config.avoidablePath())
					{
						if (fastPathPassageways.contains(tile.getWorldLocation()))
						{
							graphics.setColor(config.avoidablePathColor());
							graphics.draw(p);
							useDefaultGraphic = false;
						}
					}
					if (useDefaultGraphic)
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
		
		if (config.fastestPath())
		{
			renderEfficientPath(graphics);
		}
		if (config.slayerRingPath())
		{
			renderSlayerRingPath(graphics);
		}
		if (config.avoidablePath())
		{
			renderAvoidableDamagePath(graphics);
		}

		return null;
	}
	
	private void renderEfficientPath(Graphics2D graphics)
	{
		for (List<WorldPoint> path : efficientPathSet)
		{
			WorldLines.drawLinesOnWorld(graphics, client, path, config.fastestPathColor());
		}
		LocalPoint lp = LocalPoint.fromWorld(client, efficientStandOnWallTrapTile);
		if (lp != null)
		{
			Polygon polygon = Perspective.getCanvasTilePoly(client, lp);
			if (polygon != null)
			{
				OverlayUtil.renderPolygon(graphics, polygon, config.fastestPathColor());
			}
		}
		if (plugin.toadBattaTile != null)
		{
			OverlayUtil.renderTileOverlay(graphics, plugin.toadBattaTile.getGroundObject(), "Toad Batta", config.fastestPathColor());
		}
	}
	
	private void renderSlayerRingPath(Graphics2D graphics)
	{
		for (List<WorldPoint> path : slayerRingPathSet)
		{
			WorldLines.drawLinesOnWorld(graphics, client, path, config.slayerRingPathColor());
		}
		for (WorldPoint wp : slayerRingPathTileSet)
		{
			LocalPoint lp = LocalPoint.fromWorld(client, wp);
			if (lp != null)
			{
				Polygon polygon = Perspective.getCanvasTilePoly(client, lp);
				if (polygon != null)
				{
					OverlayUtil.renderPolygon(graphics, polygon, config.slayerRingPathColor());
				}
			}
		}
		if (plugin.toadBattaTile != null)
		{
			OverlayUtil.renderTileOverlay(graphics, plugin.toadBattaTile.getGroundObject(), "Toad Batta", config.slayerRingPathColor());
		}
	}
	
	private void renderAvoidableDamagePath(Graphics2D graphics)
	{
		for (List<WorldPoint> path : fastPathSet)
		{
			WorldLines.drawLinesOnWorld(graphics, client, path, config.avoidablePathColor());
		}
		if (plugin.toadBattaTile != null)
		{
			OverlayUtil.renderTileOverlay(graphics, plugin.toadBattaTile.getGroundObject(), "Toad Batta", config.avoidablePathColor());
		}
	}
}
