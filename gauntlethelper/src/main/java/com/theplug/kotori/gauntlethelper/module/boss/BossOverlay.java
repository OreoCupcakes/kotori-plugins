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

package com.theplug.kotori.gauntlethelper.module.boss;

import com.theplug.kotori.gauntlethelper.GauntletHelperConfig;
import com.theplug.kotori.gauntlethelper.GauntletHelperConfig.TileOutline;
import com.theplug.kotori.gauntlethelper.GauntletHelperPlugin;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.theplug.kotori.kotoriutils.overlay.OverlayUtility;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.model.Jarvis;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

@Singleton
class BossOverlay extends Overlay
{
	private final Client client;
	private final GauntletHelperConfig config;
	private final BossModule bossModule;
	private final ModelOutlineRenderer modelOutlineRenderer;

	private Hunllef hunllef;

	private int timeout;

	@Inject
	public BossOverlay(
		final Client client,
		final GauntletHelperPlugin plugin,
		final GauntletHelperConfig config,
		final BossModule bossModule,
		final ModelOutlineRenderer modelOutlineRenderer)
	{
		super(plugin);

		this.client = client;
		this.config = config;
		this.bossModule = bossModule;
		this.modelOutlineRenderer = modelOutlineRenderer;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGH);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	@Override
	public Dimension render(final Graphics2D graphics2D)
	{
		hunllef = bossModule.getHunllef();

		if (hunllef == null)
		{
			return null;
		}

		renderTornadoes(graphics2D);
		renderHunllefWrongPrayerOutline();
		renderHunllefAttackCounter(graphics2D);
		renderFlashOnWrongAttack(graphics2D);
		renderFlashOn51Method(graphics2D);

		return null;
	}

	private void renderTornadoes(final Graphics2D graphics2D)
	{
		if ((config.tornadoTileOutline() == TileOutline.OFF && !config.tornadoTickCounter()) || bossModule.getTornadoes().isEmpty())
		{
			return;
		}

		final boolean trueTile = config.tornadoTileOutline() == TileOutline.TRUE_TILE;

		for (final Tornado tornado : bossModule.getTornadoes())
		{
			final int timeLeft = tornado.getTimeLeft();

			if (timeLeft < 0)
			{
				continue;
			}

			final NPC nado = tornado.getNpc();

			if (config.tornadoTileOutline() != TileOutline.OFF)
			{
				final Polygon polygon;

				if (trueTile)
				{
					final WorldPoint worldPoint = nado.getWorldLocation();

					if (worldPoint == null)
					{
						continue;
					}

					final LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);

					if (localPoint == null)
					{
						continue;
					}

					polygon = Perspective.getCanvasTilePoly(client, localPoint);
				}
				else
				{
					polygon = Perspective.getCanvasTilePoly(client, nado.getLocalLocation());
				}

				if (polygon == null)
				{
					continue;
				}

				OverlayUtil.renderPolygon(graphics2D, polygon, config.tornadoOutlineColor(), config.tornadoFillColor(),
						new BasicStroke(config.tornadoTileOutlineWidth()));
			}

			if (config.tornadoTickCounter())
			{
				final String ticksLeftStr = String.valueOf(timeLeft);
				final Point point = nado.getCanvasTextLocation(graphics2D, ticksLeftStr, 0);

				if (point == null)
				{
					continue;
				}

				OverlayUtility.renderTextLocation(graphics2D, ticksLeftStr, config.tornadoFontSize(),
					config.tornadoFontStyle().getFont(), config.tornadoFontColor(), point,
					config.tornadoFontShadow(), 0);
			}
		}
	}

	private void renderHunllefWrongPrayerOutline()
	{
		if (!config.hunllefOverlayWrongPrayerOutline())
		{
			return;
		}

		final Hunllef.AttackPhase phase = hunllef.getAttackPhase();

		if (client.isPrayerActive(phase.getPrayer()))
		{
			return;
		}

		modelOutlineRenderer.drawOutline(hunllef.getNpc(), config.hunllefWrongPrayerOutlineWidth(), phase.getColor(),
				0);
	}

	private void renderHunllefAttackCounter(final Graphics2D graphics2D)
	{
		if (!config.hunllefOverlayAttackCounter())
		{
			return;
		}

		final NPC npc = hunllef.getNpc();

		final String text = String.format("%d | %d", hunllef.getAttackCount(),
				hunllef.getPlayerAttackCount());

		final Point point = npc.getCanvasTextLocation(graphics2D, text, 0);

		if (point == null)
		{
			return;
		}

		final Font originalFont = graphics2D.getFont();

		graphics2D.setFont(new Font(Font.SANS_SERIF,
				config.hunllefAttackCounterFontStyle().getFont(), config.hunllefAttackCounterFontSize()));

		OverlayUtil.renderTextLocation(graphics2D, point, text, hunllef.getAttackPhase().getColor());

		graphics2D.setFont(originalFont);
	}

	private void renderFlashOnWrongAttack(final Graphics2D graphics2D)
	{
		if (!config.flashOnWrongAttack() || !bossModule.isWrongAttackStyle())
		{
			return;
		}

		final Color originalColor = graphics2D.getColor();

		graphics2D.setColor(config.flashOnWrongAttackColor());

		graphics2D.fill(client.getCanvas().getBounds());

		graphics2D.setColor(originalColor);

		if (++timeout >= config.flashOnWrongAttackDuration())
		{
			timeout = 0;
			bossModule.setWrongAttackStyle(false);
		}
	}

	private void renderFlashOn51Method(final Graphics2D graphics2D)
	{
		if (!config.flashOn51Method() || !bossModule.isSwitchWeapon())
		{
			return;
		}

		final Color originalColor = graphics2D.getColor();

		graphics2D.setColor(config.flashOn51MethodColor());

		graphics2D.fill(client.getCanvas().getBounds());

		graphics2D.setColor(originalColor);

		if (++timeout >= config.flashOn51MethodDuration())
		{
			timeout = 0;
			bossModule.setSwitchWeapon(false);
		}
	}
}
