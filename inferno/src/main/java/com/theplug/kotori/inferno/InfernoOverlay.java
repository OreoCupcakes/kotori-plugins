package com.theplug.kotori.inferno;

import com.google.common.base.Strings;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

import com.theplug.kotori.kotoriutils.methods.PrayerInteractions;
import com.theplug.kotori.kotoriutils.rlapi.PrayerExtended;
import com.theplug.kotori.kotoriutils.rlapi.WidgetInfoPlus;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import com.theplug.kotori.inferno.displaymodes.InfernoPrayerDisplayMode;
import com.theplug.kotori.inferno.displaymodes.InfernoSafespotDisplayMode;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class InfernoOverlay extends Overlay
{
	private static final int TICK_PIXEL_SIZE = 60;
	private static final int BOX_WIDTH = 10;
	private static final int BOX_HEIGHT = 5;

	private final InfernoPlugin plugin;
	private final InfernoConfig config;
	private final Client client;

	@Inject
	private InfernoOverlay(final Client client, final InfernoPlugin plugin, final InfernoConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(Overlay.PRIORITY_HIGHEST);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		final Widget meleePrayerWidget = client.getWidget(WidgetInfoPlus.PRAYER_PROTECT_FROM_MELEE.getId());
		final Widget rangePrayerWidget = client.getWidget(WidgetInfoPlus.PRAYER_PROTECT_FROM_MISSILES.getId());
		final Widget magicPrayerWidget = client.getWidget(WidgetInfoPlus.PRAYER_PROTECT_FROM_MAGIC.getId());

		if (config.indicateObstacles())
		{
			renderObstacles(graphics);
		}

		if (config.safespotDisplayMode() == InfernoSafespotDisplayMode.AREA)
		{
			renderAreaSafepots(graphics);
		}
		else if (config.safespotDisplayMode() == InfernoSafespotDisplayMode.INDIVIDUAL_TILES)
		{
			renderIndividualTilesSafespots(graphics);
		}

		if (config.indicateBlobDeathLocation())
		{
			renderBlobDeathPoly(graphics);
		}

		for (InfernoNPC infernoNPC : plugin.getInfernoNpcs())
		{
			if (infernoNPC.getNpc().getConvexHull() != null)
			{
				if (config.indicateNonSafespotted() && plugin.isNormalSafespots(infernoNPC)
					&& infernoNPC.canAttack(client, client.getLocalPlayer().getWorldLocation()))
				{
					OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.RED);
				}
				if (config.indicateTemporarySafespotted() && plugin.isNormalSafespots(infernoNPC)
					&& infernoNPC.canMoveToAttack(client, client.getLocalPlayer().getWorldLocation(), plugin.getObstacles()))
				{
					OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.YELLOW);
				}
				if (config.indicateSafespotted() && plugin.isNormalSafespots(infernoNPC))
				{
					OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.GREEN);
				}
				if (config.indicateNibblers() && infernoNPC.getType() == InfernoNPC.Type.NIBBLER
					&& (!config.indicateCentralNibbler() || plugin.getCentralNibbler() != infernoNPC))
				{
					OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.CYAN);
				}
				if (config.indicateCentralNibbler() && infernoNPC.getType() == InfernoNPC.Type.NIBBLER
					&& plugin.getCentralNibbler() == infernoNPC)
				{
					OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.BLUE);
				}
				if (config.indicateActiveHealerJad() && infernoNPC.getType() == InfernoNPC.Type.HEALER_JAD
					&& infernoNPC.getNpc().getInteracting() != client.getLocalPlayer())
				{
					OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.CYAN);
				}
				if (config.indicateActiveHealerZuk() && infernoNPC.getType() == InfernoNPC.Type.HEALER_ZUK
					&& infernoNPC.getNpc().getInteracting() != client.getLocalPlayer())
				{
					OverlayUtil.renderPolygon(graphics, infernoNPC.getNpc().getConvexHull(), Color.CYAN);
				}
			}

			if (plugin.isIndicateNpcPosition(infernoNPC))
			{
				renderNpcLocation(graphics, infernoNPC);
			}

			if (plugin.isTicksOnNpc(infernoNPC) && infernoNPC.getTicksTillNextAttack() > 0)
			{
				renderTicksOnNpc(graphics, infernoNPC, infernoNPC.getNpc());
			}

			if (config.ticksOnNpcZukShield() && infernoNPC.getType() == InfernoNPC.Type.ZUK && plugin.getZukShield() != null && infernoNPC.getTicksTillNextAttack() > 0)
			{
				renderTicksOnNpc(graphics, infernoNPC, plugin.getZukShield());
			}

			if (config.ticksOnNpcMeleerDig()
				&& infernoNPC.getType() == InfernoNPC.Type.MELEE
				&& infernoNPC.getIdleTicks() >= config.digTimerThreshold()
				&& infernoNPC.getTicksTillNextAttack() == 0) // don't clobber the attack timer
			{
				renderDigTimer(graphics, infernoNPC);
			}
		}

		var prayerWidgetHidden =
			meleePrayerWidget == null
				|| rangePrayerWidget == null
				|| magicPrayerWidget == null
				|| meleePrayerWidget.isHidden()
				|| rangePrayerWidget.isHidden()
				|| magicPrayerWidget.isHidden();

		if ((config.prayerDisplayMode() == InfernoPrayerDisplayMode.PRAYER_TAB
			|| config.prayerDisplayMode() == InfernoPrayerDisplayMode.BOTH)
			&& (!prayerWidgetHidden || config.alwaysShowPrayerHelper()))
		{
			renderPrayerIconOverlay(graphics);

			if (config.descendingBoxes())
			{
				renderDescendingBoxes(graphics);
			}
		}

		return null;
	}

	private void renderObstacles(Graphics2D graphics)
	{
		for (WorldPoint worldPoint : plugin.getObstacles())
		{
			final LocalPoint localPoint = LocalPoint.fromWorld(client.getTopLevelWorldView(), worldPoint);

			if (localPoint == null)
			{
				continue;
			}

			final Polygon tilePoly = Perspective.getCanvasTilePoly(client, localPoint);

			if (tilePoly == null)
			{
				continue;
			}

			OverlayUtil.renderPolygon(graphics, tilePoly, Color.BLUE);
		}
	}

	private void renderAreaSafepots(Graphics2D graphics)
	{
		for (int safeSpotId : plugin.getSafeSpotAreas().keySet())
		{
			if (safeSpotId > 6)
			{
				continue;
			}

			Color colorEdge1;
			Color colorEdge2 = null;
			Color colorFill;

			switch (safeSpotId)
			{
				case 0:
					colorEdge1 = Color.WHITE;
					colorFill = Color.WHITE;
					break;
				case 1:
					colorEdge1 = Color.RED;
					colorFill = Color.RED;
					break;
				case 2:
					colorEdge1 = Color.GREEN;
					colorFill = Color.GREEN;
					break;
				case 3:
					colorEdge1 = Color.BLUE;
					colorFill = Color.BLUE;
					break;
				case 4:
					colorEdge1 = Color.RED;
					colorEdge2 = Color.GREEN;
					colorFill = Color.YELLOW;
					break;
				case 5:
					colorEdge1 = Color.RED;
					colorEdge2 = Color.BLUE;
					colorFill = new Color(255, 0, 255);
					break;
				case 6:
					colorEdge1 = Color.GREEN;
					colorEdge2 = Color.BLUE;
					colorFill = new Color(0, 255, 255);
					break;
				default:
					continue;
			}

			//Add all edges, calculate average edgeSize and indicate tiles
			final List<int[][]> allEdges = new ArrayList<>();
			int edgeSizeSquared = 0;

			for (WorldPoint worldPoint : plugin.getSafeSpotAreas().get(safeSpotId))
			{
				final LocalPoint localPoint = LocalPoint.fromWorld(client.getTopLevelWorldView(), worldPoint);

				if (localPoint == null)
				{
					continue;
				}

				final Polygon tilePoly = Perspective.getCanvasTilePoly(client, localPoint);

				if (tilePoly == null)
				{
					continue;
				}

                assert colorFill != null;
                renderAreaTilePolygon(graphics, tilePoly, colorFill);

				final int[][] edge1 = new int[][]{{tilePoly.xpoints[0], tilePoly.ypoints[0]}, {tilePoly.xpoints[1], tilePoly.ypoints[1]}};
				edgeSizeSquared += (int) (Math.pow(tilePoly.xpoints[0] - tilePoly.xpoints[1], 2) + Math.pow(tilePoly.ypoints[0] - tilePoly.ypoints[1], 2));
				allEdges.add(edge1);
				final int[][] edge2 = new int[][]{{tilePoly.xpoints[1], tilePoly.ypoints[1]}, {tilePoly.xpoints[2], tilePoly.ypoints[2]}};
				edgeSizeSquared += (int) (Math.pow(tilePoly.xpoints[1] - tilePoly.xpoints[2], 2) + Math.pow(tilePoly.ypoints[1] - tilePoly.ypoints[2], 2));
				allEdges.add(edge2);
				final int[][] edge3 = new int[][]{{tilePoly.xpoints[2], tilePoly.ypoints[2]}, {tilePoly.xpoints[3], tilePoly.ypoints[3]}};
				edgeSizeSquared += (int) (Math.pow(tilePoly.xpoints[2] - tilePoly.xpoints[3], 2) + Math.pow(tilePoly.ypoints[2] - tilePoly.ypoints[3], 2));
				allEdges.add(edge3);
				final int[][] edge4 = new int[][]{{tilePoly.xpoints[3], tilePoly.ypoints[3]}, {tilePoly.xpoints[0], tilePoly.ypoints[0]}};
				edgeSizeSquared += (int) (Math.pow(tilePoly.xpoints[3] - tilePoly.xpoints[0], 2) + Math.pow(tilePoly.ypoints[3] - tilePoly.ypoints[0], 2));
				allEdges.add(edge4);
			}

			if (allEdges.size() <= 0)
			{
				continue;
			}

			edgeSizeSquared /= allEdges.size();

			//Find and indicate unique edges
			final int toleranceSquared = (int) (double) (edgeSizeSquared / 6);

			for (int i = 0; i < allEdges.size(); i++)
			{
				int[][] baseEdge = allEdges.get(i);

				boolean duplicate = false;

				for (int j = 0; j < allEdges.size(); j++)
				{
					if (i == j)
					{
						continue;
					}

					int[][] checkEdge = allEdges.get(j);

					if (edgeEqualsEdge(baseEdge, checkEdge, toleranceSquared))
					{
						duplicate = true;
						break;
					}
				}

				if (!duplicate)
				{
					renderFullLine(graphics, baseEdge, colorEdge1);

					if (colorEdge2 != null)
					{
						renderDashedLine(graphics, baseEdge, colorEdge2);
					}
				}
			}

		}
	}

	private void renderDigTimer(Graphics2D g, InfernoNPC npc)
	{
		String tickString = Integer.toString(npc.getIdleTicks());
		g.setFont(new Font("Arial", plugin.getFontStyle().getFont(), config.getMeleeDigFontSize()));
		Point canvasLocation = npc.getNpc().getCanvasTextLocation(g, tickString, 0);

		if (canvasLocation == null)
		{
			return;
		}

		// NEEDS TO BE WORKED ON WITH SOME STATS
		// MELEE DIG IS UNKNOWN AT THIS TIME
		// COLLECTING DATA
		Color digColor;
		if (npc.getIdleTicks() < config.digTimerDangerThreshold())
		{
			digColor = config.getMeleeDigSafeColor();
		}
		else
		{
			digColor = config.getMeleeDigDangerColor();
		}

		renderTextLocation(g, tickString, config.getMeleeDigFontSize(), plugin.getFontStyle().getFont(), digColor, canvasLocation, false, 0);
	}


	private void renderBlobDeathPoly(Graphics2D graphics)
	{
		graphics.setColor(config.getBlobDeathLocationColor());

		plugin.getBlobDeathSpots().forEach(blobDeathSpot -> {
			Polygon area = Perspective.getCanvasTileAreaPoly(client, blobDeathSpot.getLocation(), 3);


			Color color = config.getBlobDeathLocationColor();
			if (config.blobDeathLocationFade())
			{
				color = new Color(color.getRed(), color.getGreen(), color.getBlue(), blobDeathSpot.fillAlpha());
			}

			renderOutlinePolygon(graphics, area, color);

			graphics.setFont(new Font("Arial", Font.BOLD, plugin.getTextSize()));
			String ticks = String.valueOf(blobDeathSpot.getTicksUntilDone());

			renderTextLocation(graphics,
				ticks,
				plugin.getTextSize(),
				plugin.getFontStyle().getFont(),
				config.getBlobDeathLocationColor(),
				Perspective.getCanvasTextLocation(client, graphics, blobDeathSpot.getLocation(), ticks, 0),
				false,
				0);
		});
	}

	private void renderIndividualTilesSafespots(Graphics2D graphics)
	{
		for (WorldPoint worldPoint : plugin.getSafeSpotMap().keySet())
		{
			final int safeSpotId = plugin.getSafeSpotMap().get(worldPoint);

			if (safeSpotId > 6)
			{
				continue;
			}

			final LocalPoint localPoint = LocalPoint.fromWorld(client.getTopLevelWorldView(), worldPoint);

			if (localPoint == null)
			{
				continue;
			}

			final Polygon tilePoly = Perspective.getCanvasTilePoly(client, localPoint);

			if (tilePoly == null)
			{
				continue;
			}

			Color color;
			switch (safeSpotId)
			{
				case 0:
					color = Color.WHITE;
					break;
				case 1:
					color = Color.RED;
					break;
				case 2:
					color = Color.GREEN;
					break;
				case 3:
					color = Color.BLUE;
					break;
				case 4:
					color = new Color(255, 255, 0);
					break;
				case 5:
					color = new Color(255, 0, 255);
					break;
				case 6:
					color = new Color(0, 255, 255);
					break;
				default:
					continue;
			}

			OverlayUtil.renderPolygon(graphics, tilePoly, color);
		}
	}

	private void renderTicksOnNpc(Graphics2D graphics, InfernoNPC infernoNPC, NPC renderOnNPC)
	{
		final Color color = (infernoNPC.getTicksTillNextAttack() == 1
			|| (infernoNPC.getType() == InfernoNPC.Type.BLOB && infernoNPC.getTicksTillNextAttack() == 4))
			? infernoNPC.getNextAttack().getCriticalColor() : infernoNPC.getNextAttack().getNormalColor();

		graphics.setFont(new Font("Arial", plugin.getFontStyle().getFont(), plugin.getTextSize()));

		final Point canvasPoint = renderOnNPC.getCanvasTextLocation(
			graphics, String.valueOf(infernoNPC.getTicksTillNextAttack()), 0);
		renderTextLocation(graphics, String.valueOf(infernoNPC.getTicksTillNextAttack()),
			plugin.getTextSize(), plugin.getFontStyle().getFont(), color, canvasPoint, false, 0);
	}

	private void renderNpcLocation(Graphics2D graphics, InfernoNPC infernoNPC)
	{
		final LocalPoint localPoint = LocalPoint.fromWorld(client.getTopLevelWorldView(), infernoNPC.getNpc().getWorldLocation());

		if (localPoint != null)
		{
			final Polygon tilePolygon = Perspective.getCanvasTilePoly(client, localPoint);

			if (tilePolygon != null)
			{
				OverlayUtil.renderPolygon(graphics, tilePolygon, Color.BLUE);
			}
		}
	}

	private void renderDescendingBoxes(Graphics2D graphics)
	{
		for (Integer ticksUntilAttack : plugin.getUpcomingAttacks().keySet())
		{
			final Map<InfernoNPC.Attack, Integer> attackPriority = plugin.getUpcomingAttacks().get(ticksUntilAttack);
			final InfernoNPC.Attack priorityAttack = attackPriority.entrySet().stream()
					.min(Map.Entry.comparingByValue())
					.map(Map.Entry::getKey)
					.orElse(null);

			for (InfernoNPC.Attack currentAttack : attackPriority.keySet())
			{
				renderDescendingBox(graphics, currentAttack, priorityAttack, ticksUntilAttack);
			}
		}
	}

	private void renderDescendingBox(Graphics2D graphics, InfernoNPC.Attack currentAttack, InfernoNPC.Attack priorityAttack, int ticksUntilAttack)
	{
		final Color color = getCurrentAttackDescendingBarColor(currentAttack, priorityAttack, ticksUntilAttack);
		final Widget prayerWidget = client.getWidget(PrayerExtended.getPrayerWidgetId(currentAttack.getPrayer()));

		if (prayerWidget == null) return;

		int baseX = (int) prayerWidget.getBounds().getX();
		baseX += (int) (prayerWidget.getBounds().getWidth() / 2);
		baseX -= BOX_WIDTH / 2;

		int baseY = (int) prayerWidget.getBounds().getY() - ticksUntilAttack * TICK_PIXEL_SIZE - BOX_HEIGHT;
		baseY += (int) (TICK_PIXEL_SIZE - ((plugin.getLastTick() + 600 - System.currentTimeMillis()) / 600.0 * TICK_PIXEL_SIZE));

		final Rectangle boxRectangle = new Rectangle(BOX_WIDTH, BOX_HEIGHT);
		boxRectangle.translate(baseX, baseY);

		if (currentAttack == priorityAttack)
		{
			renderFilledPolygon(graphics, boxRectangle, color);
		}
		else if (config.indicateNonPriorityDescendingBoxes())
		{
			renderOutlinePolygon(graphics, boxRectangle, color);
		}
	}

	private Color getCurrentAttackDescendingBarColor(InfernoNPC.Attack currentAttack, InfernoNPC.Attack priorityAttack, int ticksUntilAttack) {
		if(currentAttack != priorityAttack)
		{
			return config.nonPriorityPrayerColor();
		}

		if(PrayerInteractions.isActive(priorityAttack.getPrayer()))
		{
			return config.correctPrayerColor();
		}

		if(ticksUntilAttack == 1)
		{
			return config.mustPrayNextTickColor();
		}

		return config.upcomingPrayerNotPrayedColor();
	}

	private void renderPrayerIconOverlay(Graphics2D graphics)
	{
		if (plugin.getClosestAttack() != null)
		{
			// Prayer indicator in prayer tab
			InfernoNPC.Attack prayerForAttack = null;
			if (PrayerInteractions.isActive(Prayer.PROTECT_FROM_MAGIC))
			{
				prayerForAttack = InfernoNPC.Attack.MAGIC;
			}
			else if (PrayerInteractions.isActive(Prayer.PROTECT_FROM_MISSILES))
			{
				prayerForAttack = InfernoNPC.Attack.RANGED;
			}
			else if (PrayerInteractions.isActive(Prayer.PROTECT_FROM_MELEE))
			{
				prayerForAttack = InfernoNPC.Attack.MELEE;
			}

			if (plugin.getClosestAttack() != prayerForAttack || config.indicateWhenPrayingCorrectly())
			{
				final Widget prayerWidget = client.getWidget(PrayerExtended.getPrayerWidgetId(plugin.getClosestAttack().getPrayer()));
				if (prayerWidget == null)
				{
					return;
				}
				final Rectangle prayerRectangle = new Rectangle((int) prayerWidget.getBounds().getWidth(),
					(int) prayerWidget.getBounds().getHeight());
				prayerRectangle.translate((int) prayerWidget.getBounds().getX(), (int) prayerWidget.getBounds().getY());

				Color prayerColor;
				if (plugin.getClosestAttack() == prayerForAttack)
				{
					prayerColor = config.correctPrayerColor();
				}
				else
				{
					prayerColor = config.mustPrayNextTickColor();
				}

				renderOutlinePolygon(graphics, prayerRectangle, prayerColor);
			}
		}
	}

	private boolean edgeEqualsEdge(int[][] edge1, int[][] edge2, int toleranceSquared)
	{
		return (pointEqualsPoint(edge1[0], edge2[0], toleranceSquared) && pointEqualsPoint(edge1[1], edge2[1], toleranceSquared))
			|| (pointEqualsPoint(edge1[0], edge2[1], toleranceSquared) && pointEqualsPoint(edge1[1], edge2[0], toleranceSquared));
	}

	private boolean pointEqualsPoint(int[] point1, int[] point2, int toleranceSquared)
	{
		double distanceSquared = Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2);

		return distanceSquared <= toleranceSquared;
	}

	public static void renderAreaTilePolygon(Graphics2D graphics, Shape poly, Color color)
	{
		graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 10));
		graphics.fill(poly);
	}

	public static void renderFullLine(Graphics2D graphics, int[][] line, Color color)
	{
		graphics.setColor(color);
		final Stroke originalStroke = graphics.getStroke();
		graphics.setStroke(new BasicStroke(2));
		graphics.drawLine(line[0][0], line[0][1], line[1][0], line[1][1]);
		graphics.setStroke(originalStroke);
	}

	public static void renderDashedLine(Graphics2D graphics, int[][] line, Color color)
	{
		graphics.setColor(color);
		final Stroke originalStroke = graphics.getStroke();
		graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
		graphics.drawLine(line[0][0], line[0][1], line[1][0], line[1][1]);
		graphics.setStroke(originalStroke);
	}

	public static void renderOutlinePolygon(Graphics2D graphics, Shape poly, Color color)
	{
		graphics.setColor(color);
		final Stroke originalStroke = graphics.getStroke();
		graphics.setStroke(new BasicStroke(2));
		graphics.draw(poly);
		graphics.setStroke(originalStroke);
	}

	public static void renderFilledPolygon(Graphics2D graphics, Shape poly, Color color)
	{
		graphics.setColor(color);
		final Stroke originalStroke = graphics.getStroke();
		graphics.setStroke(new BasicStroke(2));
		graphics.draw(poly);
		graphics.fill(poly);
		graphics.setStroke(originalStroke);
	}

	public static void renderTextLocation(Graphics2D graphics, Point txtLoc, String text, Color color)
	{
		if (Strings.isNullOrEmpty(text))
		{
			return;
		}

		int x = txtLoc.getX();
		int y = txtLoc.getY();

		graphics.setColor(Color.BLACK);
		graphics.drawString(text, x + 1, y + 1);

		graphics.setColor(color);
		graphics.drawString(text, x, y);
	}

	public static void renderTextLocation(Graphics2D graphics, String txtString, int fontSize, int fontStyle, Color fontColor, Point canvasPoint, boolean shadows, int yOffset)
	{
		graphics.setFont(new Font("Arial", fontStyle, fontSize));
		if (canvasPoint != null)
		{
			final Point canvasCenterPoint = new Point(
				canvasPoint.getX(),
				canvasPoint.getY() + yOffset);
			final Point canvasCenterPoint_shadow = new Point(
				canvasPoint.getX() + 1,
				canvasPoint.getY() + 1 + yOffset);
			if (shadows)
			{
				renderTextLocation(graphics, canvasCenterPoint_shadow, txtString, Color.BLACK);
			}
			renderTextLocation(graphics, canvasCenterPoint, txtString, fontColor);
		}
	}
}
