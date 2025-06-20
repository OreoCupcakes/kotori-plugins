/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 *
 * Modified by farhan1666
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
package com.theplug.kotori.aoewarnings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import com.theplug.kotori.kotoriutils.rlapi.GraphicIDPlus;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.coords.WorldPoint;
import static com.theplug.kotori.aoewarnings.ColorUtil.setAlphaComponent;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

@Singleton
public class AoeWarningOverlay extends Overlay
{
	private static final int FILL_START_ALPHA = 25;
	private static final int OUTLINE_START_ALPHA = 255;

	private final Client client;
	private final AoeWarningPlugin plugin;
	private final AoeWarningConfig config;

	@Inject
	public AoeWarningOverlay(final Client client, final AoeWarningPlugin plugin, final AoeWarningConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.UNDER_WIDGETS);
		setPriority(Overlay.PRIORITY_HIGHEST);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		WorldPoint lp = client.getLocalPlayer().getWorldLocation();

		plugin.getLightningTrail().forEach(o ->
			OverlayUtil.drawTiles(graphics, client, o, lp, new Color(0, 150, 200), 2, 150, 50));

		plugin.getAcidTrail().forEach(o ->
			OverlayUtil.drawTiles(graphics, client, o.getWorldLocation(), lp, new Color(69, 241, 44), 2, 150, 50));

		plugin.getCrystalSpike().forEach(o ->
			OverlayUtil.drawTiles(graphics, client, o.getWorldLocation(), lp, new Color(255, 0, 84), 2, 150, 50));

		plugin.getWintertodtSnowFall().forEach(o ->
			OverlayUtil.drawTiles(graphics, client, o.getWorldLocation(), lp, new Color(255, 0, 84), 2, 150, 50));

		Instant now = Instant.now();
		Set<ProjectileContainer> projectiles = plugin.getProjectiles();
		projectiles.forEach(proj ->
		{
			if (proj.getTargetPoint() == null)
			{
				return;
			}

			Color color;

			if (now.isAfter(proj.getStartTime().plus(Duration.ofMillis(proj.getLifetime()))))
			{
				return;
			}

			if (proj.getProjectile().getId() == GraphicIDPlus.ICE_DEMON_ICE_BARRAGE_AOE || proj.getProjectile().getId() == GraphicIDPlus.TEKTON_METEOR_AOE)
			{
				if (client.getVarbitValue(VarbitID.RAIDS_CLIENT_INDUNGEON) == 0)
				{
					return;
				}
			}

			final Polygon tilePoly = Perspective.getCanvasTileAreaPoly(client, proj.getTargetPoint(), proj.getAoeProjectileInfo().getAoeSize());

			if (tilePoly == null)
			{
				return;
			}

			final double progress = (System.currentTimeMillis() - proj.getStartTime().toEpochMilli()) / (double) proj.getLifetime();

			final int tickProgress = proj.getFinalTick() - client.getTickCount();

			int fillAlpha, outlineAlpha;
			if (config.isFadeEnabled())
			{
				fillAlpha = (int) ((1 - progress) * FILL_START_ALPHA);
				outlineAlpha = (int) ((1 - progress) * OUTLINE_START_ALPHA);
			}
			else
			{
				fillAlpha = FILL_START_ALPHA;
				outlineAlpha = OUTLINE_START_ALPHA;
			}
			if (tickProgress == 0)
			{
				color = Color.RED;
			}
			else
			{
				color = Color.WHITE;
			}

			if (fillAlpha < 0)
			{
				fillAlpha = 0;
			}
			if (outlineAlpha < 0)
			{
				outlineAlpha = 0;
			}

			if (fillAlpha > 255)
			{
				fillAlpha = 255;
			}
			if (outlineAlpha > 255)
			{
				outlineAlpha = 255;
			}

			if (config.isOutlineEnabled())
			{
				graphics.setColor(new Color(setAlphaComponent(config.overlayColor().getRGB(), outlineAlpha), true));
				graphics.drawPolygon(tilePoly);
			}
			if (config.tickTimers() && tickProgress >= 0)
			{
				OverlayUtil.renderTextLocation(graphics, Integer.toString(tickProgress), config.textSize(),
					config.fontStyle().getFont(), color, centerPoint(tilePoly.getBounds()), config.shadows(), 0);
			}

			graphics.setColor(new Color(setAlphaComponent(config.overlayColor().getRGB(), fillAlpha), true));
			graphics.fillPolygon(tilePoly);
		});
		projectiles.removeIf(proj -> now.isAfter(proj.getStartTime().plus(Duration.ofMillis(proj.getLifetime()))));
		return null;
	}

	private Point centerPoint(Rectangle rect)
	{
		int x = (int) (rect.getX() + rect.getWidth() / 2);
		int y = (int) (rect.getY() + rect.getHeight() / 2);
		return new Point(x, y);
	}
}