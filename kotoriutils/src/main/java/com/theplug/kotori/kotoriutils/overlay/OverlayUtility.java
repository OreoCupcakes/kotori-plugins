package com.theplug.kotori.kotoriutils.overlay;

import com.theplug.kotori.kotoriutils.rlapi.InterfaceTab;
import com.theplug.kotori.kotoriutils.rlapi.PrayerExtended;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.OverlayUtil;

import java.awt.*;

public class OverlayUtility
{
	public static Rectangle renderPrayerOverlay(Graphics2D graphics, Client client, Prayer prayer, Color color)
	{
		Widget widget = client.getWidget(PrayerExtended.getPrayerWidgetId(prayer));

		if (widget == null || client.getVarcIntValue(VarClientInt.INVENTORY_TAB) != InterfaceTab.PRAYER.getId())
		{
			return null;
		}

		Rectangle bounds = widget.getBounds();
		OverlayUtil.renderPolygon(graphics, rectangleToPolygon(bounds), color);
		return bounds;
	}

	private static Polygon rectangleToPolygon(Rectangle rect)
	{
		int[] xpoints = {rect.x, rect.x + rect.width, rect.x + rect.width, rect.x};
		int[] ypoints = {rect.y, rect.y, rect.y + rect.height, rect.y + rect.height};

		return new Polygon(xpoints, ypoints, 4);
	}

	//This one is for the guitar hero descending boxes
	public static void renderFilledPolygon(Graphics2D graphics, Shape poly, Color color)
	{
		final Color originalColor = graphics.getColor();
		final Stroke originalStroke = graphics.getStroke();
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(2));
		graphics.draw(poly);
		graphics.fill(poly);
		graphics.setStroke(originalStroke);
		graphics.setColor(originalColor);
	}

	public static void renderTextLocation(Graphics2D graphics, String txtString, int fontSize, int fontStyle, Color fontColor, Point canvasPoint, boolean shadows, int yOffset)
	{
		if (canvasPoint == null)
		{
			return;
		}
		Font originalFont = graphics.getFont();
		graphics.setFont(new Font("Arial", fontStyle, fontSize));
		final Point canvasCenterPoint = new Point(canvasPoint.getX(), canvasPoint.getY() + yOffset);
		final Point canvasCenterPoint_shadow = new Point(canvasPoint.getX() + 1, canvasPoint.getY() + 1 + yOffset);
		if (shadows)
		{
			OverlayUtil.renderTextLocation(graphics, canvasCenterPoint_shadow, txtString, Color.BLACK);
		}
		OverlayUtil.renderTextLocation(graphics, canvasCenterPoint, txtString, fontColor);
		graphics.setFont(originalFont);
	}

	public static void renderTextLocation(Graphics2D graphics2D, Client client, LocalPoint localPoint, String txtString, int fontSize, int fontStyle, Color fontColor, boolean shadows, int yOffset)
	{
		if (localPoint == null)
		{
			return;
		}

		Point canvasPoint = Perspective.getCanvasTextLocation(client, graphics2D, localPoint, txtString, 0);
		if (canvasPoint == null)
		{
			return;
		}

		renderTextLocation(graphics2D, txtString, fontSize, fontStyle, fontColor, canvasPoint, shadows, yOffset);
	}

	public static void renderTextLocation(Graphics2D graphics2D, Client client, LocalPoint localPoint, String text, Color textColor)
	{
		renderTextLocation(graphics2D, client, localPoint, text, textColor, 0, 16);
	}

	public static void renderTextLocation(Graphics2D graphics2D, Client client, LocalPoint localPoint, String text, Color textColor, int zOffset, int fontSize)
	{
		Font originalFont = graphics2D.getFont();
		Point canvasTextLocation = Perspective.getCanvasTextLocation(client, graphics2D, localPoint, text, zOffset);
		if (canvasTextLocation == null)
		{
			return;
		}

		graphics2D.setFont(originalFont.deriveFont((float) fontSize));
		OverlayUtil.renderTextLocation(graphics2D, canvasTextLocation, text, textColor);
		graphics2D.setFont(originalFont);
	}

	public static void renderTextLocation(Graphics2D graphics2D, Client client, WorldPoint worldPoint, String text, Color textColor)
	{
		renderTextLocation(graphics2D, client, worldPoint, text, textColor, 0, 16);
	}

	public static void renderTextLocation(Graphics2D graphics2D, Client client, WorldPoint worldPoint, String text, Color textColor, int zOffset, int fontSize)
	{
		LocalPoint localPoint = LocalPoint.fromWorld(client.getTopLevelWorldView(), worldPoint);
		if (localPoint == null)
		{
			return;
		}

		renderTextLocation(graphics2D, client, localPoint, text, textColor, zOffset, fontSize);
	}

	public static void drawTiles(Graphics2D graphics2D, Client client, WorldPoint point, Color color)
	{
		drawTiles(graphics2D, client, point, color, 2);
	}

	public static void drawTiles(Graphics2D graphics2D, Client client, WorldPoint point, Color color, int strokeWidth)
	{
		drawTiles(graphics2D, client, point, color, new Color(0, 0, 0, 50), strokeWidth);
	}

	public static void drawTiles(Graphics2D graphics, Client client, WorldPoint point, Color color, Color fillColor, int strokeWidth)
	{
		if (point == null)
		{
			return;
		}
		LocalPoint lp = LocalPoint.fromWorld(client.getTopLevelWorldView(), point);
		drawTiles(graphics, client, lp, color, fillColor, strokeWidth);
	}

	public static void drawTiles(Graphics2D graphics2D, Client client, LocalPoint localPoint, Color color)
	{
		drawTiles(graphics2D, client, localPoint, color, 2);
	}

	public static void drawTiles(Graphics2D graphics2D, Client client, LocalPoint localPoint, Color color, int strokeWidth)
	{
		drawTiles(graphics2D, client, localPoint, color, new Color(0, 0, 0, 50), strokeWidth);
	}

	public static void drawTiles(Graphics2D graphics2D, Client client, LocalPoint localPoint, Color color, Color fillColor, int strokeWidth)
	{
		if (localPoint == null)
		{
			return;
		}

		Polygon polygon = Perspective.getCanvasTilePoly(client, localPoint);
		if (polygon == null)
		{
			return;
		}
		OverlayUtil.renderPolygon(graphics2D, polygon, color, fillColor, new BasicStroke(strokeWidth));
	}

	public static void drawOutlineAndFill(final Graphics2D graphics2D, final Color outlineColor, final Color fillColor, final float strokeWidth, final Shape shape)
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

	public static void drawPolygon(Graphics2D graphics2D, Client client, LocalPoint localPoint, int rectangleSize, Color color, Color fillColor, int strokeWidth)
	{
		if (localPoint == null)
		{
			return;
		}

		final Polygon polygon = Perspective.getCanvasTileAreaPoly(client, localPoint, rectangleSize);

		if (polygon == null)
		{
			return;
		}

		OverlayUtil.renderPolygon(graphics2D, polygon, color, fillColor, new BasicStroke(strokeWidth));
	}

	public static void drawPolygon(Graphics2D graphics2D, Client client, WorldPoint worldPoint, int rectangleSize, Color color, Color fillColor, int strokeWidth)
	{
		if (worldPoint == null)
		{
			return;
		}

		LocalPoint localPoint = LocalPoint.fromWorld(client.getTopLevelWorldView(), worldPoint);

		if (localPoint == null)
		{
			return;
		}

		drawPolygon(graphics2D, client, localPoint, rectangleSize, color, fillColor, strokeWidth);
	}
}