package com.theplug.kotori.gauntlethelper.module.boss;

import com.theplug.kotori.gauntlethelper.GauntletHelperConfig;
import com.theplug.kotori.gauntlethelper.GauntletHelperPlugin;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Prayer;
import net.runelite.api.SpriteID;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.InfoBoxComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;

@Singleton
class PrayerInfoBoxOverlay extends Overlay
{
    private static final Color NOT_ACTIVATED_BACKGROUND_COLOR = new Color(150, 0, 0, 150);

    private final Client client;
    private final GauntletHelperConfig config;
    private final BossModule bossModule;
    private final SpriteManager spriteManager;

    private final PanelComponent panelComponent;
    private final InfoBoxComponent infoBoxComponent;

    private BufferedImage spriteProtectFromMagic;
    private BufferedImage spriteProtectFromRange;

    @Inject
    PrayerInfoBoxOverlay(final Client client,
                         final GauntletHelperPlugin plugin,
                         final GauntletHelperConfig config,
                         final BossModule bossModule,
                         final SpriteManager spriteManager)
    {
        super(plugin);

        this.client = client;
        this.config = config;
        this.bossModule = bossModule;
        this.spriteManager = spriteManager;

        this.panelComponent = new PanelComponent();
        this.infoBoxComponent = new InfoBoxComponent();

        infoBoxComponent.setColor(Color.WHITE);
        infoBoxComponent.setPreferredSize(new Dimension(40, 40));

        panelComponent.getChildren().add(infoBoxComponent);
        panelComponent.setPreferredSize(new Dimension(40, 40));
        panelComponent.setBorder(new Rectangle(0, 0, 0, 0));

        setPosition(OverlayPosition.BOTTOM_RIGHT);
        setPriority(OverlayPriority.HIGH);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics2D)
    {
        final GauntletHelperConfig.PrayerHighlightMode prayerHighlightMode = config.prayerOverlay();

        if (prayerHighlightMode == GauntletHelperConfig.PrayerHighlightMode.NONE || prayerHighlightMode == GauntletHelperConfig.PrayerHighlightMode.WIDGET)
        {
            return null;
        }

        final Hunllef hunllef = bossModule.getHunllef();

        if (hunllef == null)
        {
            return null;
        }

        final NPC npc = hunllef.getNpc();

        if (npc == null || npc.isDead())
        {
            return null;
        }

        final Prayer prayer = hunllef.getAttackPhase().getPrayer();

        infoBoxComponent.setImage(getPrayerSprite(prayer));

        infoBoxComponent.setBackgroundColor(client.isPrayerActive(prayer)
                ? ComponentConstants.STANDARD_BACKGROUND_COLOR
                : NOT_ACTIVATED_BACKGROUND_COLOR);

        return panelComponent.render(graphics2D);
    }

    private BufferedImage getPrayerSprite(final Prayer prayer)
    {
        switch (prayer)
        {
            case PROTECT_FROM_MAGIC:
                if (spriteProtectFromMagic == null)
                {
                    spriteProtectFromMagic = scaleSprite(spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MAGIC, 0));
                }

                return spriteProtectFromMagic;
            case PROTECT_FROM_MISSILES:
                if (spriteProtectFromRange == null)
                {
                    spriteProtectFromRange = scaleSprite(spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MISSILES, 0));
                }

                return spriteProtectFromRange;
            default:
                throw new IllegalStateException("Unexpected boss attack phase prayer: " + prayer);
        }
    }

    private static BufferedImage scaleSprite(final BufferedImage bufferedImage)
    {
        if (bufferedImage == null)
        {
            return null;
        }

        final double width = bufferedImage.getWidth(null);
        final double height = bufferedImage.getHeight(null);
        final double size = 36; // Limit size to 2 as that is minimum size not causing breakage
        final double scalex = size / width;
        final double scaley = size / height;
        final double scale = Math.min(scalex, scaley);
        final int newWidth = (int) (width * scale);
        final int newHeight = (int) (height * scale);
        final BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        final Graphics g = scaledImage.createGraphics();
        g.drawImage(bufferedImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return scaledImage;
    }
}
