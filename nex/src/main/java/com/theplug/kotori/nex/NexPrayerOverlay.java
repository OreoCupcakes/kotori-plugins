package com.theplug.kotori.nex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.theplug.kotori.kotoriutils.methods.PrayerInteractions;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import com.theplug.kotori.kotoriutils.rlapi.PrayerExtended;

@Singleton
@Slf4j
class NexPrayerOverlay extends Overlay
{
	private final Client client;
	private final NexPlugin plugin;
	private final NexConfig config;

	@Inject
	private NexPrayerOverlay(final Client client, final NexPlugin plugin, final NexConfig config)
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
		if (!plugin.isInFight() || plugin.getNex() == null || client.getLocalPlayer() == null)
		{
			return null;
		}

		if (!config.prayerHelper().showWidgetHelper())
		{
			return null;
		}

		var prayer = NexPhase.phasePrayer(plugin.getCurrentPhase(), client.getLocalPlayer(), plugin.getNex(), plugin.isTrappedInIce());

		if (prayer == null)
		{
			return null;
		}

		final Widget meleePrayerWidget = client.getWidget(PrayerExtended.getPrayerWidgetId(Prayer.PROTECT_FROM_MELEE));
		final Widget rangePrayerWidget = client.getWidget(PrayerExtended.getPrayerWidgetId(Prayer.PROTECT_FROM_MISSILES));
		final Widget magicPrayerWidget = client.getWidget(PrayerExtended.getPrayerWidgetId(Prayer.PROTECT_FROM_MAGIC));


		var prayerWidgetHidden = meleePrayerWidget == null
			|| rangePrayerWidget == null
			|| magicPrayerWidget == null
			|| meleePrayerWidget.isHidden()
			|| rangePrayerWidget.isHidden()
			|| magicPrayerWidget.isHidden();


		if (!prayerWidgetHidden || config.alwaysShowPrayerHelper())
		{
			if (PrayerInteractions.isActive(prayer) && !config.indicatePrayerIsCorrect())
			{
				return null;
			}
			Color color = PrayerInteractions.isActive(prayer) ? Color.GREEN : Color.RED;
			renderPrayerOverlay(graphics, client, prayer, color);
		}

		return null;
	}

	private void renderPrayerOverlay(Graphics2D graphics, Client client, Prayer prayer, Color color)
	{
		Widget prayerWidget = client.getWidget(PrayerExtended.getPrayerWidgetId(prayer));

		if (prayerWidget == null)
		{
			return;
		}

		final Rectangle prayerRectangle = new Rectangle((int) prayerWidget.getBounds().getWidth(), (int) prayerWidget.getBounds().getHeight());
		prayerRectangle.translate((int) prayerWidget.getBounds().getX(), (int) prayerWidget.getBounds().getY());

		OverlayUtil.renderPolygon(graphics, prayerRectangle, color);
	}
}