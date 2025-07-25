package com.theplug.kotori.nex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.theplug.kotori.kotoriutils.methods.PrayerInteractions;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.gameval.SpriteID;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

@Singleton
public class NexPrayerInfoBox extends Overlay
{
	private static final Color NOT_ACTIVATED_BACKGROUND_COLOR = new Color(150, 0, 0, 150);
	private final Client client;
	private final NexPlugin plugin;
	private final NexConfig config;
	private final SpriteManager spriteManager;
	private final PanelComponent imagePanelComponent = new PanelComponent();

	@Inject
	private NexPrayerInfoBox(final Client client, final NexPlugin plugin, final SpriteManager spriteManager, final NexConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.spriteManager = spriteManager;
		this.config = config;

		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(Overlay.PRIORITY_HIGHEST);
		setPosition(OverlayPosition.BOTTOM_RIGHT);
	}

	public Dimension render(Graphics2D graphics)
	{
		imagePanelComponent.getChildren().clear();

		if (!plugin.isInFight() || plugin.getNex() == null || client.getLocalPlayer() == null)
		{
			return null;
		}

		if (!config.prayerHelper().showInfoBox())
		{
			return null;
		}

		var prayer = NexPhase.phasePrayer(plugin.getCurrentPhase(), client.getLocalPlayer(), plugin.getNex(), plugin.isTrappedInIce());

		if (prayer == null)
		{
			return null;
		}

		BufferedImage prayerImage;
		prayerImage = getPrayerImage(prayer);
		imagePanelComponent.setBackgroundColor(PrayerInteractions.isActive(prayer) ? ComponentConstants.STANDARD_BACKGROUND_COLOR : NOT_ACTIVATED_BACKGROUND_COLOR);

		imagePanelComponent.getChildren().add(new ImageComponent(prayerImage));
		return imagePanelComponent.render(graphics);
	}

	private BufferedImage getPrayerImage(Prayer attack)
	{

		var spriteId = 0;

		switch (attack)
		{
			case PROTECT_FROM_MAGIC:
				spriteId = SpriteID.Prayeron.PROTECT_FROM_MAGIC;
				break;
			case PROTECT_FROM_MISSILES:
				spriteId = SpriteID.Prayeron.PROTECT_FROM_MISSILES;
				break;
			case PROTECT_FROM_MELEE:
				spriteId = SpriteID.Prayeron.PROTECT_FROM_MELEE;
				break;
			default:
				spriteId = SpriteID.GeCancel.BUTTON;
		}

		return spriteManager.getSprite(spriteId, 0);
	}
}
