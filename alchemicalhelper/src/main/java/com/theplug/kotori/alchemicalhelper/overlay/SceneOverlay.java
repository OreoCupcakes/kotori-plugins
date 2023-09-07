/*
 * Copyright (c) 2020 dutta64 <https://github.com/dutta64>
 * Copyright (c) 2019, Lucas <https://github.com/lucwousin>
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
package com.theplug.kotori.alchemicalhelper.overlay;

import java.awt.*;
import java.awt.geom.Area;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.theplug.kotori.alchemicalhelper.AlchemicalHelperConfig;
import com.theplug.kotori.alchemicalhelper.AlchemicalHelperPlugin;
import com.theplug.kotori.alchemicalhelper.entity.Hydra;
import com.theplug.kotori.alchemicalhelper.entity.HydraPhase;
import com.theplug.kotori.kotoriutils.methods.VarUtilities;
import net.runelite.api.*;

import static net.runelite.api.Perspective.getCanvasTileAreaPoly;

import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

@Singleton
public class SceneOverlay extends Overlay
{
	private static final int LIGHTNING_ID = 1666;

	private static final Area POISON_AREA = new Area();

	private static final int POISON_AOE_AREA_SIZE = 3;

	private static final int HYDRA_HULL_OUTLINE_STROKE_SIZE = 1;

	private final Client client;
	private final AlchemicalHelperPlugin plugin;
	private final AlchemicalHelperConfig config;
	private final ModelOutlineRenderer modelOutlineRenderer;

	private Hydra hydra;

	@Inject
	public SceneOverlay(final Client client, final AlchemicalHelperPlugin plugin, final AlchemicalHelperConfig config,
						final ModelOutlineRenderer modelOutlineRenderer)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		this.modelOutlineRenderer = modelOutlineRenderer;

		setPriority(OverlayPriority.HIGH);
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	@Override
	public Dimension render(final Graphics2D graphics2D)
	{
		hydra = plugin.getHydra();

		if (hydra == null)
		{
			return null;
		}

		//Lightning/Flame Skip at top because the HpUntilPhaseChange and FountainTicks changes the graphic's font and font size
		renderLightningSkipTile(graphics2D);
		renderFlameSkipTile(graphics2D);
		renderHpUntilPhaseChange(graphics2D);
		renderHydraImmunityOutline();
		renderPoisonProjectileAreaTiles(graphics2D);
		renderLightning(graphics2D);
		renderFountainOutline(graphics2D);
		renderFountainTicks(graphics2D);

		return null;
	}

	private void renderPoisonProjectileAreaTiles(final Graphics2D graphics2D)
	{
		final Map<LocalPoint, Projectile> poisonProjectiles = plugin.getPoisonProjectiles();

		if (!config.poisonOutline() || poisonProjectiles.isEmpty())
		{
			return;
		}

		POISON_AREA.reset();

		for (final Map.Entry<LocalPoint, Projectile> entry : poisonProjectiles.entrySet())
		{
			if (entry.getValue().getEndCycle() < client.getGameCycle())
			{
				continue;
			}

			final LocalPoint localPoint = entry.getKey();

			final Polygon polygon = getCanvasTileAreaPoly(client, localPoint, POISON_AOE_AREA_SIZE);

			if (polygon != null)
			{
				POISON_AREA.add(new Area(polygon));
			}
		}

		Color color = config.poisonFillColor();

		drawOutlineAndFill(graphics2D, new Color(color.getRed(), color.getGreen(), color.getBlue()), color, 2, POISON_AREA);
	}

	private void renderLightning(final Graphics2D graphics2D)
	{
	//	final Deque<GraphicsObject> graphicsObjects = client.getGraphicsObjects();
		Set<GraphicsObject> graphicsObjects = plugin.getLightningObjects();

		if (!config.lightningOutline() || hydra.getPhase() != HydraPhase.LIGHTNING)
		{
			return;
		}

		for (final GraphicsObject graphicsObject : graphicsObjects)
		{
			if (graphicsObject.getId() != LIGHTNING_ID)
			{
				continue;
			}

			final LocalPoint localPoint = graphicsObject.getLocation();

			if (localPoint == null)
			{
				return;
			}

			final Polygon polygon = Perspective.getCanvasTilePoly(client, localPoint);

			if (polygon == null)
			{
				return;
			}

			Color color = config.lightningFillColor();

			drawOutlineAndFill(graphics2D, new Color(color.getRed(), color.getGreen(), color.getBlue()), color,
				1, polygon);
		}
	}

	private void renderHydraImmunityOutline()
	{
		final NPC npc = hydra.getNpc();

		if (!config.hydraImmunityOutline() || !hydra.isImmunity() || npc == null || npc.isDead())
		{
			return;
		}

		final WorldPoint fountainWorldPoint = hydra.getPhase().getFountainWorldPoint();

		if (fountainWorldPoint != null)
		{
			final Collection<WorldPoint> fountainWorldPoints = WorldPoint.toLocalInstance(client, fountainWorldPoint);

			if (fountainWorldPoints.size() == 1)
			{
				WorldPoint worldPoint = null;

				for (final WorldPoint wp : fountainWorldPoints)
				{
					worldPoint = wp;
				}

				final LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);

				if (localPoint != null)
				{
					final Polygon polygon = getCanvasTileAreaPoly(client, localPoint, 3);

					if (polygon != null)
					{
						int stroke = HYDRA_HULL_OUTLINE_STROKE_SIZE;

						if (npc.getWorldArea().intersectsWith(new WorldArea(worldPoint, 1, 1)))
						{
							stroke++;
						}

						modelOutlineRenderer.drawOutline(npc, stroke, hydra.getPhase().getPhaseColor(), 0);
						return;
					}
				}
			}

		}

		modelOutlineRenderer.drawOutline(npc, HYDRA_HULL_OUTLINE_STROKE_SIZE, hydra.getPhase().getPhaseColor(), 0);
	}

	private void renderFountainOutline(final Graphics2D graphics2D)
	{
		final NPC npc = hydra.getNpc();
		final WorldPoint fountainWorldPoint = hydra.getPhase().getFountainWorldPoint();

		if (!config.fountainOutline() || !hydra.isImmunity() || fountainWorldPoint == null || npc == null || npc.isDead())
		{
			return;
		}

		final Collection<WorldPoint> fountainWorldPoints = WorldPoint.toLocalInstance(client, fountainWorldPoint);

		if (fountainWorldPoints.size() != 1)
		{
			return;
		}

		WorldPoint worldPoint = null;

		for (final WorldPoint wp : fountainWorldPoints)
		{
			worldPoint = wp;
		}

		final LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);

		if (localPoint == null)
		{
			return;
		}

		final Polygon polygon = getCanvasTileAreaPoly(client, localPoint, 3);

		if (polygon == null)
		{
			return;
		}

		Color color = hydra.getPhase().getFountainColor();

		if (!npc.getWorldArea().intersectsWith(new WorldArea(worldPoint, 1, 1)))
		{
			color = color.darker();
		}

		drawOutlineAndFill(graphics2D, color, new Color(color.getRed(), color.getGreen(), color.getBlue(), 30), 1, polygon);
	}

	private void renderFountainTicks(final Graphics2D graphics2D)
	{

		if (!config.fountainTicks())
		{
			return;
		}

		final Collection<WorldPoint> fountainWorldPoints = WorldPoint.toLocalInstance(client, HydraPhase.POISON.getFountainWorldPoint());
		fountainWorldPoints.addAll(WorldPoint.toLocalInstance(client, HydraPhase.LIGHTNING.getFountainWorldPoint()));
		fountainWorldPoints.addAll(WorldPoint.toLocalInstance(client, HydraPhase.FLAME.getFountainWorldPoint()));

		if (fountainWorldPoints.isEmpty())
		{
			return;
		}

		WorldPoint worldPoint;

		for (final WorldPoint wp : fountainWorldPoints)
		{
			worldPoint = wp;
			final LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);

			if (localPoint == null)
			{
				return;
			}


			final String text = String.valueOf(plugin.getFountainTicks());
			Point timeLoc = Perspective.getCanvasTextLocation(client, graphics2D, localPoint, text, graphics2D.getFontMetrics().getHeight());


			OverlayUtil.renderTextLocation(
				graphics2D,
				text,
				16,
				Font.BOLD,
				config.fountainTicksFontColor(),
				timeLoc,
				true,
				0
			);
		}


	}

	private void renderHpUntilPhaseChange(final Graphics2D graphics2D)
	{
		final NPC npc = hydra.getNpc();

		if (!config.showHpUntilPhaseChange() || npc == null || npc.isDead())
		{
			return;
		}

		final int hpUntilPhaseChange = hydra.getHpUntilPhaseChange();

		if (hpUntilPhaseChange == 0)
		{
			return;
		}

		final String text = String.valueOf(hpUntilPhaseChange);

		final Point point = npc.getCanvasTextLocation(graphics2D, text, 0);

		if (point == null)
		{
			return;
		}

		OverlayUtil.renderTextLocation(
			graphics2D,
			text,
			16,
			Font.BOLD,
			config.fontColor(),
			point,
			true,
			0
		);
	}

	private void renderLightningSkipTile(final Graphics2D graphics2D)
	{
		if (config.doLightningSkip() && VarUtilities.getPlayerAttackStyle() != 0 && hydra.getPhase() == HydraPhase.LIGHTNING)
		{
			if (!plugin.inLightningSafeSpot)
			{
				Collection<WorldPoint> lightningSkipTile = WorldPoint.toLocalInstance(client, AlchemicalHelperPlugin.LIGHTNING_SAFESPOT_1);
				if (lightningSkipTile.size() != 1)
				{
					return;
				}

				WorldPoint worldPoint = lightningSkipTile.stream().findFirst().orElse(null);
				drawTileOverlayWithText(graphics2D, worldPoint, config.lightningSkipTileBorder(), config.lightningSkipTileFill(), 2,
						"Stand here", config.lightningSkipTileBorder());
			}
			else
			{
				Collection<WorldPoint> lightningSafeTile = WorldPoint.toLocalInstance(client, AlchemicalHelperPlugin.LIGHTNING_SAFESPOT_3);
				if (lightningSafeTile.size() != 1)
				{
					return;
				}

				WorldPoint worldPoint = lightningSafeTile.stream().findFirst().orElse(null);
				drawTileOverlayWithText(graphics2D, worldPoint, config.lightningSkipTileBorder(), config.lightningSkipTileFill(), 2,
						"Do not move until phase change", config.lightningSkipTileBorder());
			}
		}
	}

	private void renderFlameSkipTile(Graphics2D graphics2D)
	{
		if (config.doFlameSkip() && hydra.getPhase() == HydraPhase.FLAME)
		{
			Collection<WorldPoint> flameSkipTile = WorldPoint.toLocalInstance(client, AlchemicalHelperPlugin.FLAME_SAFESPOT_1);
			if (flameSkipTile.size() != 1)
			{
				return;
			}

			WorldPoint worldPoint = flameSkipTile.stream().findFirst().orElse(null);
			drawTileOverlayWithText(graphics2D, worldPoint, config.flameSkipTileBorder(), config.flameSkipTileFill(), 2,
					"Stand here before stun", config.flameSkipTileBorder());
        }
	}

	private static void drawOutlineAndFill(final Graphics2D graphics2D, final Color outlineColor, final Color fillColor, final float strokeWidth, final Shape shape)
	{
		final Color originalColor = graphics2D.getColor();
		final Stroke originalStroke = graphics2D.getStroke();

		graphics2D.setStroke(new BasicStroke(strokeWidth));
		graphics2D.setColor(outlineColor);
		graphics2D.draw(shape);

		graphics2D.setColor(fillColor);
		graphics2D.fill(shape);

		graphics2D.setColor(originalColor);
		graphics2D.setStroke(originalStroke);
	}

	private void drawTileOverlayWithText(Graphics2D graphics2D, WorldPoint worldPoint, Color borderColor, Color fillColor, float strokeWidth, String text, Color textColor)
	{
		LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);
		if (localPoint == null)
		{
			return;
		}

		Polygon polygon = Perspective.getCanvasTilePoly(client, localPoint);
		if (polygon == null)
		{
			return;
		}

		net.runelite.client.ui.overlay.OverlayUtil.renderPolygon(graphics2D, polygon, borderColor, fillColor,
				new BasicStroke(strokeWidth));

		Point canvasTextLocation = Perspective.getCanvasTextLocation(client, graphics2D, localPoint, text, 0);
		if (canvasTextLocation == null)
		{
			return;
		}

		net.runelite.client.ui.overlay.OverlayUtil.renderTextLocation(graphics2D, canvasTextLocation, text, textColor);
	}
}
