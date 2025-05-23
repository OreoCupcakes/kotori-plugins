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
import java.util.Map;
import javax.inject.Inject;

import com.theplug.kotori.grotesqueguardians.entity.Guardian;
import com.theplug.kotori.kotoriutils.rlapi.PrayerExtended;
import net.runelite.api.*;
import com.theplug.kotori.kotoriutils.rlapi.InterfaceTab;
import net.runelite.api.widgets.Widget;
import com.theplug.kotori.grotesqueguardians.GrotesqueGuardiansConfig;
import com.theplug.kotori.grotesqueguardians.GrotesqueGuardiansPlugin;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import static net.runelite.client.ui.overlay.OverlayUtil.renderPolygon;

public class PrayerOverlay extends Overlay
{
	private static final int TICK_PIXEL_SIZE = 60;
	private static final int BOX_WIDTH = 10;
	private static final int BOX_HEIGHT = 5;

	private final Client client;
	private final GrotesqueGuardiansPlugin plugin;
	private final GrotesqueGuardiansConfig config;

	@Inject
	private PrayerOverlay(final Client client, final GrotesqueGuardiansPlugin plugin, final GrotesqueGuardiansConfig config)
	{

		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(Overlay.PRIORITY_HIGHEST);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(final Graphics2D graphics2D)
	{
		Map<Actor, Guardian> guardianMap = plugin.getGuardians();

		if (guardianMap.isEmpty())
		{
			return null;
		}

		for (Guardian guardian : guardianMap.values())
		{
			renderGuardianAttackTicks(graphics2D, guardian);
		}

		return null;
	}

	private void renderGuardianAttackTicks(final Graphics2D graphics2D, Guardian guardian)
	{
		final int guardianTicksUntilNextAttack = guardian.getTicksUntilNextAttack();

		final NPC npc = guardian.getNpc();

		if (npc == null || npc.isDead() || guardianTicksUntilNextAttack <= 0)
		{
			return;
		}

		Guardian.AttackStyle attackStyle = guardian.getAttackStyle();

		if (attackStyle == Guardian.AttackStyle.UNKNOWN)
		{
			return;
		}

		final Prayer prayer = attackStyle.getPrayer();

		if (config.prayerTickCounter())
		{
			final Color color = getColorFromPrayer(prayer);

			renderPrayerWidget(graphics2D, prayer, color, guardianTicksUntilNextAttack);
		}

		if (config.guitarHeroMode())
		{
			renderDescendingBoxes(graphics2D, prayer, guardianTicksUntilNextAttack);
		}
	}

	private void renderPrayerWidget(final Graphics2D graphics2D, final Prayer prayer, final Color color, final int ticksUntilNextAttack)
	{
		final Rectangle rectangle = renderPrayerOverlay(graphics2D, client, prayer, color);

		if (rectangle == null)
		{
			return;
		}

		final String text = String.valueOf(ticksUntilNextAttack);

		final int fontSize = 16;
		final int fontStyle = Font.BOLD;

		final Color fontColor = ticksUntilNextAttack == 1 ? Color.WHITE : color;

		final int x = (int) (rectangle.getX() + rectangle.getWidth() / 2);
		final int y = (int) (rectangle.getY() + rectangle.getHeight() / 2);

		final Point prayerWidgetPoint = new Point(x, y);

		final Point canvasPoint = new Point(prayerWidgetPoint.getX() - 3, prayerWidgetPoint.getY() + 6);

		renderTextLocation(graphics2D, text, fontSize, fontStyle, fontColor, canvasPoint, true, 0);
	}

	private void renderDescendingBoxes(final Graphics2D graphics2D, final Prayer prayer, final int tick)
	{
		final Color color = tick == 1 ? Color.RED : Color.ORANGE;

		final Widget prayerWidget = client.getWidget(PrayerExtended.getPrayerWidgetId(prayer));

		if (prayerWidget == null || prayerWidget.isHidden())
		{
			return;
		}

		int baseX = (int) prayerWidget.getBounds().getX();
		baseX += (int) (prayerWidget.getBounds().getWidth() / 2);
		baseX -= BOX_WIDTH / 2;

		int baseY = (int) prayerWidget.getBounds().getY() - tick * TICK_PIXEL_SIZE - BOX_HEIGHT;
		baseY += (int) (TICK_PIXEL_SIZE - ((plugin.getLastTickTime() + 600 - System.currentTimeMillis()) / 600.0 * TICK_PIXEL_SIZE));

		final Rectangle boxRectangle = new Rectangle(BOX_WIDTH, BOX_HEIGHT);
		boxRectangle.translate(baseX, baseY);

		renderFilledPolygon(graphics2D, boxRectangle, color);
	}

	private static Color getColorFromPrayer(final Prayer prayer)
	{
		final Color color;

		if (prayer == Prayer.PROTECT_FROM_MELEE)
		{
			color = Color.RED;
		}
		else if (prayer == Prayer.PROTECT_FROM_MISSILES)
		{
			color = Color.GREEN;
		}
		else
		{
			color = Color.WHITE;
		}

		return color;
	}


	public static Rectangle renderPrayerOverlay(Graphics2D graphics, Client client, Prayer prayer, Color color)
	{
		Widget widget = client.getWidget(PrayerExtended.getPrayerWidgetId(prayer));

		if (widget == null || client.getVarcIntValue(VarClientInt.INVENTORY_TAB) != InterfaceTab.PRAYER.getId())
		{
			return null;
		}

		Rectangle bounds = widget.getBounds();
		renderPolygon(graphics, rectangleToPolygon(bounds), color);
		return bounds;
	}

	private static Polygon rectangleToPolygon(Rectangle rect)
	{
		int[] xpoints = {rect.x, rect.x + rect.width, rect.x + rect.width, rect.x};
		int[] ypoints = {rect.y, rect.y, rect.y + rect.height, rect.y + rect.height};

		return new Polygon(xpoints, ypoints, 4);
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
}
