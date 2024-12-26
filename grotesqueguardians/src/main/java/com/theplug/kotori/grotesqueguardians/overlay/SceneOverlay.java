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

package com.theplug.kotori.grotesqueguardians.overlay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.util.Set;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import com.theplug.kotori.grotesqueguardians.GrotesqueGuardiansConfig;
import com.theplug.kotori.grotesqueguardians.GrotesqueGuardiansPlugin;
import com.theplug.kotori.grotesqueguardians.entity.Dawn;
import com.theplug.kotori.grotesqueguardians.entity.Dusk;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.game.npcoverlay.NpcOverlayService;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

public class SceneOverlay extends Overlay
{
	private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

	private static final int FLASH_DURATION = 30;

	private final Client client;
	private final GrotesqueGuardiansPlugin plugin;
	private final GrotesqueGuardiansConfig config;
	private final ModelOutlineRenderer modelOutlineRenderer;

	private int flashTimeout;

	@Inject
	private SceneOverlay(final Client client, final GrotesqueGuardiansPlugin plugin, final GrotesqueGuardiansConfig config, final ModelOutlineRenderer modelOutlineRenderer)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		this.modelOutlineRenderer = modelOutlineRenderer;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(Overlay.PRIORITY_HIGHEST);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	@Override
	public Dimension render(final Graphics2D graphics2D)
	{
		final Dusk dusk = plugin.getDusk();

		final boolean gargoyleInvulnOutline = config.invulnerabilityOutline();

		if (dusk != null)
		{
			if (config.duskTickCounter())
			{
				renderDuskTickCounter(graphics2D, dusk);
			}

			if (gargoyleInvulnOutline && dusk.getPhase() == null)
			{
				renderInvulnerabilityOutline(dusk.getNpc());
			}

			if (plugin.isFlashOnExplosion())
			{
				if (dusk.getNpc().getWorldArea().isInMeleeDistance(client.getLocalPlayer().getWorldLocation()))
				{
					if (config.highlightDuskOnExplosion())
					{
						renderExplosionOutline(dusk.getNpc());
					}

					if (config.flashOnExplosion())
					{
						renderFlashOnExplosion(graphics2D);
					}
				}
			}
		}

		final Dawn dawn = plugin.getDawn();

		if (dawn != null)
		{
			if (config.dawnTickCounter())
			{
				renderDawnTickCounter(graphics2D, dawn);
			}

			if (gargoyleInvulnOutline && dawn.getPhase() == null)
			{
				renderInvulnerabilityOutline(dawn.getNpc());
			}
		}

		final Dusk defNotDusk = plugin.getDefNotDusk();

		if (defNotDusk != null)
		{
			if (config.duskTickCounter())
			{
				renderDuskTickCounter(graphics2D, defNotDusk);
			}

			if (gargoyleInvulnOutline && defNotDusk.getPhase() == null)
			{
				renderInvulnerabilityOutline(defNotDusk.getNpc());
			}
		}

		renderStoneOrbOutline(graphics2D);
		renderFallingRocksOutline(graphics2D);
		renderLightningOutline(graphics2D);
		renderEnergySphereOutline(graphics2D);
		renderFlameWallOutline(graphics2D);

		return null;
	}

	private void renderDuskTickCounter(final Graphics2D graphics2D, final Dusk dusk)
	{
		final int ticksUntilNextAttack = dusk.getTicksUntilNextAttack();

		if (ticksUntilNextAttack == 0)
		{
			return;
		}

		final NPC npc = dusk.getNpc();

		if (npc.isDead())
		{
			return;
		}

		final String text = String.valueOf(ticksUntilNextAttack);

		final Point npcPoint = npc.getCanvasTextLocation(graphics2D, text, 0);

		if (npcPoint == null)
		{
			return;
		}

		final Font originalFont = graphics2D.getFont();

		graphics2D.setFont(new Font(Font.SANS_SERIF, config.tickFontStyle().getFont(), config.tickFontSize()));

		OverlayUtil.renderTextLocation(graphics2D, npcPoint, text, ticksUntilNextAttack == 1 ? Color.WHITE : config.tickFontColor());

		graphics2D.setFont(originalFont);
	}

	private void renderDawnTickCounter(final Graphics2D graphics2D, final Dawn dawn)
	{
		final int ticksUntilNextAttack = dawn.getTicksUntilNextAttack();

		if (ticksUntilNextAttack == 0)
		{
			return;
		}

		final NPC npc = dawn.getNpc();

		if (npc.isDead())
		{
			return;
		}

		final String text = String.valueOf(ticksUntilNextAttack);

		final Point npcPoint = npc.getCanvasTextLocation(graphics2D, text, 0);

		if (npcPoint == null)
		{
			return;
		}

		final Font originalFont = graphics2D.getFont();

		graphics2D.setFont(new Font(Font.SANS_SERIF, config.tickFontStyle().getFont(), config.tickFontSize()));

		OverlayUtil.renderTextLocation(graphics2D, npcPoint, text, ticksUntilNextAttack == 1 ? Color.WHITE : config.tickFontColor());

		graphics2D.setFont(originalFont);
	}

	private void renderGraphicObjectSet(Graphics2D graphics2D, Set<GraphicsObject> graphicsObjects, Color color, int borderWidth, int aoeSize)
	{
		if (graphicsObjects.isEmpty())
		{
			return;
		}

		Area aoeArea = new Area();

		for (final GraphicsObject graphic : graphicsObjects)
		{
			if (graphic == null)
			{
				continue;
			}

			final LocalPoint localPoint = graphic.getLocation();

			if (localPoint == null)
			{
				continue;
			}

			final Polygon polygon = Perspective.getCanvasTileAreaPoly(client, localPoint, aoeSize);

			if (polygon == null)
			{
				continue;
			}

			aoeArea.add(new Area(polygon));
		}

		drawOutlineAndFill(graphics2D, new Color(color.getRed(), color.getGreen(), color.getBlue()), color, borderWidth, aoeArea);
	}

	private void renderGameObjectSet(Graphics2D graphics2D, Set<GameObject> gameObjects, Color color, int borderWidth, int aoeSize)
	{
		if (gameObjects.isEmpty())
		{
			return;
		}

		for (final GameObject object : gameObjects)
		{
			if (object == null)
			{
				continue;
			}

			final Polygon polygon = Perspective.getCanvasTileAreaPoly(client, object.getLocalLocation(), aoeSize);

			if (polygon == null)
			{
				continue;
			}

			drawOutlineAndFill(graphics2D, new Color(color.getRed(), color.getGreen(), color.getBlue()), color, borderWidth, polygon);
		}
	}

	private void renderFallingRocksOutline(final Graphics2D graphics2D)
	{
		if (!config.highlightFallingRocks())
		{
			return;
		}

		renderGraphicObjectSet(graphics2D, plugin.getFallingRockObjects(), config.fallingRocksFillColor(), config.fallingRocksWidth(), config.killingEchoVariant() ? 1 : 3);
	}

	private void renderLightningOutline(final Graphics2D graphics2D)
	{
		if (!config.highlightLightning())
		{
			return;
		}

		renderGraphicObjectSet(graphics2D, plugin.getLightningObjects(), config.lightningFillColor(), config.lightningWidth(), 3);
	}

	private void renderFlameWallOutline(final Graphics2D graphics2D)
	{
		if (!config.highlightFlameWall())
		{
			return;
		}

		renderGraphicObjectSet(graphics2D, plugin.getFlameWallObjects(), config.flameWallFillColor(), config.flameWallWidth(), 1);
	}

	private void renderStoneOrbOutline(final Graphics2D graphics2D)
	{
		if (!config.highlightStoneOrb())
		{
			return;
		}

		renderGraphicObjectSet(graphics2D, plugin.getStoneOrbOjects(), config.stoneOrbFillColor(), config.stoneOrbBorderWidth(), config.killingEchoVariant() ? 1 : 3);
	}

	private void renderEnergySphereOutline(final Graphics2D graphics2D)
	{
		if (!config.highlightEnergy())
		{
			return;
		}

		renderGameObjectSet(graphics2D, plugin.getEnergySpheres(), config.energyFillColor(), config.energyWidth(), 1);
	}

	private void renderInvulnerabilityOutline(final NPC npc)
	{
		modelOutlineRenderer.drawOutline(npc, config.invulnerabilityBorderWidth(), config.invulnerabilityFillColor(), 0);
	}

	private void renderExplosionOutline(final NPC npc)
	{
		modelOutlineRenderer.drawOutline(npc, config.explosionBorderWidth(), config.explosionColor(), 0);

		if (++flashTimeout >= FLASH_DURATION)
		{
			flashTimeout = 0;
			plugin.setFlashOnExplosion(false);
		}
	}

	private void renderFlashOnExplosion(final Graphics2D graphics2D)
	{
		final Color originalColor = graphics2D.getColor();

		graphics2D.setColor(config.flashColor());

		graphics2D.fill(client.getCanvas().getBounds());

		graphics2D.setColor(originalColor);

		if (++flashTimeout >= FLASH_DURATION)
		{
			flashTimeout = 0;
			plugin.setFlashOnExplosion(false);
		}
	}

	private static void drawOutlineAndFill(final Graphics2D graphics2D, final Color borderColor, final Color fillColor, final float strokeWidth, final Shape shape)
	{
		final Color originalColor = graphics2D.getColor();
		final Stroke originalStroke = graphics2D.getStroke();

		graphics2D.setStroke(new BasicStroke(strokeWidth));
		graphics2D.setColor(borderColor);
		graphics2D.draw(shape);

		graphics2D.setColor(fillColor);
		graphics2D.fill(shape);

		graphics2D.setColor(originalColor);
		graphics2D.setStroke(originalStroke);
	}
}
