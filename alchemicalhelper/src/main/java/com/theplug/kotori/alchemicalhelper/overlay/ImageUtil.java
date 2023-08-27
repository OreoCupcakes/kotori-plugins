package com.theplug.kotori.alchemicalhelper.overlay;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.SpritePixels;

import java.util.Arrays;

/**
 * Various Image/BufferedImage utilities.
 */
@Slf4j
public class ImageUtil
{

    /**
     * Draw fg centered on top of bg
     */
    public static SpritePixels mergeSprites(final Client client, final SpritePixels bg, final SpritePixels fg)
    {
        assert fg.getHeight() <= bg.getHeight() && fg.getWidth() <= bg.getWidth() : "Background has to be larger than foreground";

        final int[] canvas = Arrays.copyOf(bg.getPixels(), bg.getWidth() * bg.getHeight());
        final SpritePixels result = client.createSpritePixels(canvas, bg.getWidth(), bg.getHeight());

        final int bgWid = bg.getWidth();
        final int fgHgt = fg.getHeight();
        final int fgWid = fg.getWidth();

        final int xOffset = (bgWid - fgWid) / 2;
        final int yOffset = (bg.getHeight() - fgHgt) / 2;

        final int[] fgPixels = fg.getPixels();

        for (int y1 = yOffset, y2 = 0; y2 < fgHgt; y1++, y2++)
        {
            int i1 = y1 * bgWid + xOffset;
            int i2 = y2 * fgWid;

            for (int x = 0; x < fgWid; x++, i1++, i2++)
            {
                if (fgPixels[i2] > 0)
                {
                    canvas[i1] = fgPixels[i2];
                }
            }
        }

        return result;
    }
}