/*
 * Copyright (c) 2020 dutta64 <https://github.com/dutta64>
 * Copyright (c) 2019 Im2be <https://github.com/Im2be>
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

package com.theplug.kotori.cerberushelper.util;

import java.awt.image.BufferedImage;

import com.theplug.kotori.cerberushelper.domain.Phase;
import com.theplug.kotori.cerberushelper.CerberusHelperPlugin;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.runelite.api.Prayer;
import net.runelite.client.util.ImageUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageManager
{
	private static double RESIZE_FACTOR = 1.25D;

	private static final BufferedImage[][] images = new BufferedImage[3][6];

//	public static BufferedImage getCerberusBufferedImage(final Phase phase, final Prayer prayer, final CerberusConfig.InfoBoxComponentSize size)
	public static BufferedImage getCerberusBufferedImage(final Phase phase, final Prayer prayer)
	{
		if (phase == Phase.AUTO)
		{
		//	return getCerberusPrayerBufferedImage(prayer, size);
			return getCerberusPrayerBufferedImage(prayer);
		}

	//	return getCerberusPhaseBufferedImage(phase, size);
		return getCerberusPhaseBufferedImage(phase);
	}

//	private static BufferedImage getCerberusPrayerBufferedImage(final Prayer prayer, final CerberusConfig.InfoBoxComponentSize size)
	private static BufferedImage getCerberusPrayerBufferedImage(final Prayer prayer)
	{
		final String path;
		final int imgIdx;

		switch (prayer)
		{
			default:
			case PROTECT_FROM_MAGIC:
				path = "cerberus_magic.png";
				imgIdx = 0;
				break;
			case PROTECT_FROM_MISSILES:
				path = "cerberus_range.png";
				imgIdx = 1;
				break;
			case PROTECT_FROM_MELEE:
				path = "cerberus_melee.png";
				imgIdx = 2;
				break;
		}

	//	return getBufferedImage(path, imgIdx, size);
		return getBufferedImage(path, imgIdx);
	}

//	private static BufferedImage getCerberusPhaseBufferedImage(final Phase phase, final CerberusConfig.InfoBoxComponentSize size)
	private static BufferedImage getCerberusPhaseBufferedImage(final Phase phase)
	{
		final String path;
		final int imgIdx;

		switch (phase)
		{
			default:
			case TRIPLE:
				path = "cerberus_triple.png";
				imgIdx = 3;
				break;
			case GHOSTS:
				path = "cerberus_ghosts.png";
				imgIdx = 4;
				break;
			case LAVA:
				path = "cerberus_lava.png";
				imgIdx = 5;
				break;
		}

	//	return getBufferedImage(path, imgIdx, size);
		return getBufferedImage(path, imgIdx);
	}

//	private static BufferedImage getBufferedImage(final String path, final int imgIdx, final CerberusConfig.InfoBoxComponentSize size)
	private static BufferedImage getBufferedImage(final String path, final int imgIdx)
	{
		final BufferedImage img = ImageUtil.loadImageResource(CerberusHelperPlugin.class, path);

	//	Removed the info box size config because medium and large are just ridiculously oversized anyways
	//	final int resize = (int) (size.getSize() / RESIZE_FACTOR);
		final int resize = (int) (40 / RESIZE_FACTOR);

		if (images[0][imgIdx] == null)
		{
			images[0][imgIdx] = ImageUtil.resizeImage(img, resize, resize);
		}
		return images[0][imgIdx];
		/*


		Removed the info box size config because medium and large are just ridiculously oversized anyways
		switch (size)
		{
			default:
			case SMALL:
				if (images[0][imgIdx] == null)
				{
					images[0][imgIdx] = ImageUtil.resizeImage(img, resize, resize);
				}
				return images[0][imgIdx];
			case MEDIUM:
				if (images[1][imgIdx] == null)
				{
					images[1][imgIdx] = ImageUtil.resizeImage(img, resize, resize);
				}
				return images[1][imgIdx];
			case LARGE:
				if (images[2][imgIdx] == null)
				{
					images[2][imgIdx] = ImageUtil.resizeImage(img, resize, resize);
				}
				return images[2][imgIdx];
		}
		*/
	}
}
