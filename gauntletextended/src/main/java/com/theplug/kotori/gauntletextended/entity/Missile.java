/*
 * Copyright (c) 2022, Kotori <https://github.com/OreoCupcakes/>
 * Copyright (c) 2020, dutta64 <https://github.com/dutta64>
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
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

package com.theplug.kotori.gauntletextended.entity;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.theplug.kotori.gauntletextended.utils.GraphicIDPlus;
import lombok.Getter;
import net.runelite.api.Projectile;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.util.ImageUtil;

public class Missile
{
	private static final int FILL_ALPHA = 100;

	@Getter
	private final Projectile projectile;

	private final BufferedImage originalIcon;

	@Getter
	private final Color outlineColor;
	@Getter
	private final Color fillColor;

	private BufferedImage icon;

	private int iconSize;

	public Missile(final Projectile projectile, final SkillIconManager skillIconManager, final int iconSize)
	{
		this.projectile = projectile;
		this.iconSize = iconSize;

		this.originalIcon = getOriginalIcon(skillIconManager, projectile.getId());

		this.outlineColor = getOutlineColor(projectile.getId());
		this.fillColor = new Color(outlineColor.getRed(), outlineColor.getGreen(), outlineColor.getBlue(), FILL_ALPHA);
	}

	public void setIconSize(final int iconSize)
	{
		this.iconSize = iconSize;
		icon = ImageUtil.resizeImage(originalIcon, iconSize, iconSize);
	}

	public BufferedImage getIcon()
	{
		if (icon == null)
		{
			icon = ImageUtil.resizeImage(originalIcon, iconSize, iconSize);
		}

		return icon;
	}

	private static Color getOutlineColor(final int projectileId)
	{
		switch (projectileId)
		{
			case GraphicIDPlus.HUNLLEF_MAGE_ATTACK:
			case GraphicIDPlus.HUNLLEF_CORRUPTED_MAGE_ATTACK:
				return Color.CYAN;
			case GraphicIDPlus.HUNLLEF_RANGE_ATTACK:
			case GraphicIDPlus.HUNLLEF_CORRUPTED_RANGE_ATTACK:
				return Color.GREEN;
			case GraphicIDPlus.HUNLLEF_PRAYER_ATTACK:
			case GraphicIDPlus.HUNLLEF_CORRUPTED_PRAYER_ATTACK:
				return Color.MAGENTA;
			default:
				throw new IllegalArgumentException("Unsupported gauntlet projectile id: " + projectileId);
		}
	}

	private static BufferedImage getOriginalIcon(final SkillIconManager skillIconManager, final int projectileId)
	{
		switch (projectileId)
		{
			case GraphicIDPlus.HUNLLEF_MAGE_ATTACK:
			case GraphicIDPlus.HUNLLEF_CORRUPTED_MAGE_ATTACK:
				return skillIconManager.getSkillImage(Skill.MAGIC);
			case GraphicIDPlus.HUNLLEF_RANGE_ATTACK:
			case GraphicIDPlus.HUNLLEF_CORRUPTED_RANGE_ATTACK:
				return skillIconManager.getSkillImage(Skill.RANGED);
			case GraphicIDPlus.HUNLLEF_PRAYER_ATTACK:
			case GraphicIDPlus.HUNLLEF_CORRUPTED_PRAYER_ATTACK:
				return skillIconManager.getSkillImage(Skill.PRAYER);
			default:
				throw new IllegalArgumentException("Unsupported gauntlet projectile id: " + projectileId);
		}
	}
}
