package com.theplug.kotori.kotoriutils.overlay;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.IndexDataBase;
import net.runelite.api.SpritePixels;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Various Image/BufferedImage utilities.
 */
@Slf4j
public class ImageUtility
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

    public static SpritePixels getSprite(Client client, int spriteId)
    {
        final IndexDataBase spriteDatabase = client.getIndexSprites();

        if (spriteDatabase == null)
        {
            return null;
        }

        final SpritePixels[] sprites = client.getSprites(spriteDatabase, spriteId, 0);

        if (sprites == null)
        {
            return null;
        }

        return sprites[0];
    }

    public static BufferedImage combineSprites(Client client, int spriteId1, int spriteId2)
    {
        final SpritePixels root = getSprite(client, spriteId1);
        final SpritePixels mark = getSprite(client, spriteId2);

        if (mark == null || root == null)
        {
            return null;
        }

        final SpritePixels sprite = mergeSprites(client, root, mark);

        return sprite.toBufferedImage();
    }
}