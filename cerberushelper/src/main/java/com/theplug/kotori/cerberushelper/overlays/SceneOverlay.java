/*
 * Copyright (c) 2019 Im2be <https://github.com/Im2be>
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

package com.theplug.kotori.cerberushelper.overlays;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.theplug.kotori.cerberushelper.CerberusHelperConfig;
import com.theplug.kotori.cerberushelper.CerberusHelperPlugin;
import com.theplug.kotori.cerberushelper.domain.Arena;
import com.theplug.kotori.cerberushelper.domain.Cerberus;
import com.theplug.kotori.cerberushelper.domain.Ghost;
import com.theplug.kotori.kotoriutils.overlay.OverlayUtility;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.util.ColorUtil;

@Singleton
public final class SceneOverlay extends Overlay
{
	private static final int MAX_RENDER_DISTANCE = 32;

	private static final int GHOST_TIME_WARNING = 2;
	private static final int GHOST_TIME_FONT_SIZE = 12;

	private static final int GHOST_TILE_OUTLINE_WIDTH = 2;
	private static final int GHOST_TILE_OUTLINE_ALPHA = 255;
	private static final int GHOST_TILE_FILL_ALPHA = 20;

	private static final int GHOST_YELL_TICK_WINDOW = 17;
	private static final int ECHO_GHOST_YELL_TICK_WINDOW = 35;

	private final Client client;
	private final CerberusHelperPlugin plugin;
	private final CerberusHelperConfig config;

	private Cerberus cerberus;

	@Inject
	SceneOverlay(final Client client, final CerberusHelperPlugin plugin, final CerberusHelperConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPriority(Overlay.PRIORITY_HIGHEST);
	}

	@Override
	public Dimension render(final Graphics2D graphics2D)
	{
		cerberus = plugin.getCerberus();

		if (cerberus == null)
		{
			return null;
		}

		renderGhostTiles(graphics2D);

		if (config.killingEchoCerberus())
		{
			renderEchoLavaGraphics(graphics2D);
		}
		else
		{
			renderLavaProjectileAreaTiles(graphics2D);
		}

		return null;
	}

	private void renderGhostTiles(final Graphics2D graphics2D)
	{
		if (!config.drawGhostTiles() || cerberus.getLastGhostYellTick() == 0)
		{
			return;
		}

		if (!config.killingEchoCerberus())
		{
			if ((plugin.getGameTick() - cerberus.getLastGhostYellTick()) >= GHOST_YELL_TICK_WINDOW)
			{
				plugin.getGhosts().clear();
				return;
			}
		}
		else
		{
			if ((plugin.getGameTick() - cerberus.getLastGhostYellTick()) >= ECHO_GHOST_YELL_TICK_WINDOW)
			{
				plugin.getGhosts().clear();
				return;
			}
		}

		final Player player = client.getLocalPlayer();

		if (player == null)
		{
			return;
		}

		final WorldPoint playerTile = player.getWorldLocation();

		final Arena arena = Arena.getArena(WorldPoint.fromLocalInstance(client, player.getLocalLocation()));

		if (arena == null)
		{
			return;
		}

		//Echo variant, then 9 ghosts vs 3 in normal
		final int numberOfTiles = !config.killingEchoCerberus() ? 3 : 9;

		for (int i = 0; i < numberOfTiles; ++i)
		{
			final WorldPoint ghostTileNonInstanced = arena.getGhostTile(i, config.killingEchoCerberus());
			Collection<WorldPoint> possibleGhostTileInstances = WorldPoint.toLocalInstance(client.getTopLevelWorldView(), ghostTileNonInstanced);
			WorldPoint ghostTile = null;
			for (WorldPoint worldPoint : possibleGhostTileInstances)
			{
				ghostTile = worldPoint;
				break;
			}

			if (ghostTile == null || ghostTile.distanceTo(playerTile) >= MAX_RENDER_DISTANCE)
			{
				return;
			}

			renderGhostTileOutline(graphics2D, ghostTile, playerTile, i);

			renderGhostTileAttackTime(graphics2D, ghostTile, i);
		}
	}

	private void renderGhostTileOutline(final Graphics2D graphics2D, final WorldPoint ghostTile, final WorldPoint playerPoint, final int tileIndex)
	{
		Color ghostTileFillColor = Color.WHITE;

		final List<NPC> ghosts = plugin.getGhosts();

		if (tileIndex < ghosts.size())
		{
			final Ghost ghost = Ghost.fromNPC(ghosts.get(tileIndex));

			if (ghost != null)
			{
				ghostTileFillColor = ghost.getColor();
			}
		}

		OverlayUtility.drawTiles(graphics2D, client, ghostTile, ColorUtil.colorWithAlpha(ghostTileFillColor, GHOST_TILE_OUTLINE_ALPHA)
				, ColorUtil.colorWithAlpha(ghostTileFillColor, GHOST_TILE_FILL_ALPHA), GHOST_TILE_OUTLINE_WIDTH);
	}

	private void renderGhostTileAttackTime(final Graphics2D graphics2D, final WorldPoint ghostTile, final int tileIndex)
	{
		final LocalPoint localPoint = LocalPoint.fromWorld(client.getTopLevelWorldView(), ghostTile);

		if (localPoint == null)
		{
			return;
		}

		final Polygon polygon = Perspective.getCanvasTilePoly(client, localPoint);

		if (polygon == null)
		{
			return;
		}

		final long time = System.currentTimeMillis();

		final int tick = plugin.getGameTick();
		final int lastGhostsTick = cerberus.getLastGhostYellTick();

		//Update and get the time when the ghosts were summoned
		final long lastGhostsTime = Math.min(cerberus.getLastGhostYellTime(), time - (600L * (tick - lastGhostsTick)));
		cerberus.setLastGhostYellTime(lastGhostsTime);

		long baseTimeUntilGhosts = 13 + tileIndex * 2L;

		if (config.killingEchoCerberus())
		{
			switch (tileIndex)
			{
				case 1:
				case 2:
				case 3:
					baseTimeUntilGhosts = 13 + tileIndex * 2L;
					break;
				case 4:
				case 5:
				case 6:
					baseTimeUntilGhosts = 17 + tileIndex * 2L;
					break;
				case 7:
				case 8:
				case 9:
					baseTimeUntilGhosts = 19 + tileIndex * 2L;
					break;
			}
		}

		final double timeUntilGhostAttack = Math.max((double) ((lastGhostsTime + 600 * baseTimeUntilGhosts) - System.currentTimeMillis()) / 1000, 0);

		final Color textColor = timeUntilGhostAttack <= GHOST_TIME_WARNING ? Color.RED : Color.WHITE;

		final String timeUntilAttack = String.format("%.1f", timeUntilGhostAttack);

		graphics2D.setFont(new Font("Arial", Font.PLAIN, GHOST_TIME_FONT_SIZE));

		final FontMetrics metrics = graphics2D.getFontMetrics();
		final Point centerPoint = getRectangleCenterPoint(polygon.getBounds());

		final Point newPoint = new Point(centerPoint.getX() - (metrics.stringWidth(timeUntilAttack) / 2), centerPoint.getY() + (metrics.getHeight() / 2));

		OverlayUtility.renderTextLocation(graphics2D, timeUntilAttack, 12,
			Font.PLAIN, textColor, newPoint, true, 0);
	}

	private void renderLavaProjectileAreaTiles(final Graphics2D graphics2D)
	{
		final Map<LocalPoint, Projectile> lavaProjectiles = plugin.getLavaProjectiles();

		if (!config.drawLavaTiles() || lavaProjectiles.isEmpty())
		{
			return;
		}

		for (final Map.Entry<LocalPoint, Projectile> entry : lavaProjectiles.entrySet())
		{
			if (entry.getValue().getRemainingCycles() <= 0)
			{
				continue;
			}

			final LocalPoint localPoint = entry.getKey();

			final Polygon polygon = Perspective.getCanvasTileAreaPoly(client, localPoint, 3);

			if (polygon == null)
			{
				continue;
			}

			Color c = config.lavaFillColor();

			net.runelite.client.ui.overlay.OverlayUtil.renderPolygon(graphics2D, polygon, new Color(c.getRed(), c.getGreen(), c.getBlue()),
					config.lavaFillColor(), new BasicStroke(2));
		}
	}

	private void renderEchoLavaGraphics(final Graphics2D graphics2D)
	{
		final Set<GraphicsObject> lavaSpawns = plugin.getEchoLavaGraphics();

		if (!config.drawLavaTiles() || lavaSpawns.isEmpty())
		{
			return;
		}

		for (GraphicsObject lava : lavaSpawns)
		{
			if (lava.finished())
			{
				continue;
			}

			final LocalPoint localPoint = lava.getLocation();

			final Polygon polygon = Perspective.getCanvasTilePoly(client, localPoint);

			if (polygon == null)
			{
				continue;
			}

			Color c = config.lavaFillColor();

			net.runelite.client.ui.overlay.OverlayUtil.renderPolygon(graphics2D, polygon, new Color(c.getRed(), c.getGreen(), c.getBlue()),
					config.lavaFillColor(), new BasicStroke(2));
		}
	}

	private Point getRectangleCenterPoint(final Rectangle rect)
	{
		final int x = (int) (rect.getX() + rect.getWidth() / 2);
		final int y = (int) (rect.getY() + rect.getHeight() / 2);

		return new Point(x, y);
	}
}
