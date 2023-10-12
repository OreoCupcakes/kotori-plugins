/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2023, rdutta <https://github.com/rdutta>
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

package com.theplug.kotori.gauntlethelper.module.maze;

import java.awt.image.BufferedImage;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectID;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.util.ImageUtil;

class ResourceGameObject
{
	private static final int DEFAULT_ICON_SIZE = 14;

	@Getter(AccessLevel.PACKAGE)
	private final Resource resource;
	@Getter(AccessLevel.PACKAGE)
	private final GameObject gameObject;
	private final BufferedImage originalIcon;
	@Getter(AccessLevel.PACKAGE)
	private final BufferedImage minimapIcon;
	private BufferedImage icon;
	private int iconSize;

	ResourceGameObject(
		@NonNull final GameObject gameObject,
		@NonNull final SkillIconManager skillIconManager,
		final int iconSize)
	{
		this.gameObject = gameObject;
		this.iconSize = iconSize;
		this.resource = getResourceByObjectId(gameObject.getId());

		originalIcon = getOriginalIcon(resource, skillIconManager, false);
		minimapIcon = getOriginalIcon(resource, skillIconManager, true);
	}

	void setIconSize(final int iconSize)
	{
		this.iconSize = iconSize;
		final int size = iconSize <= 0 ? DEFAULT_ICON_SIZE : iconSize;
		icon = ImageUtil.resizeImage(originalIcon, size, size);
	}

	BufferedImage getIcon()
	{
		if (icon == null)
		{
			final int size = iconSize <= 0 ? DEFAULT_ICON_SIZE : iconSize;
			icon = ImageUtil.resizeImage(originalIcon, size, size);
		}

		return icon;
	}

	@Nullable
	Point getMinimapPoint()
	{
		final Point point = gameObject.getMinimapLocation();

		if (point == null)
		{
			return null;
		}

		return new Point(point.getX() - minimapIcon.getHeight() / 2, point.getY() - minimapIcon.getWidth() / 2);
	}

	private static BufferedImage getOriginalIcon(final Resource resource,
												 final SkillIconManager skillIconManager,
												 final boolean small)
	{
		switch (resource)
		{
			case RAW_PADDLEFISH:
				return skillIconManager.getSkillImage(Skill.FISHING, small);
			case CRYSTAL_ORE:
			case CORRUPTED_ORE:
				return skillIconManager.getSkillImage(Skill.MINING, small);
			case PHREN_BARK:
			case CORRUPTED_PHREN_BARK:
				return skillIconManager.getSkillImage(Skill.WOODCUTTING, small);
			case LINUM_TIRINUM:
			case CORRUPTED_LINUM_TIRINUM:
				return skillIconManager.getSkillImage(Skill.FARMING, small);
			case GRYM_LEAF:
			case CORRUPTED_GRYM_LEAF:
				return skillIconManager.getSkillImage(Skill.HERBLORE, small);
			default:
				throw new IllegalArgumentException("Unsupported resource: " + resource);
		}
	}

	private static Resource getResourceByObjectId(final int objectId)
	{
		switch (objectId)
		{
			case ObjectID.CRYSTAL_DEPOSIT:
				return Resource.CRYSTAL_ORE;
			case ObjectID.CORRUPT_DEPOSIT:
				return Resource.CORRUPTED_ORE;
			case ObjectID.PHREN_ROOTS:
				return Resource.PHREN_BARK;
			case ObjectID.CORRUPT_PHREN_ROOTS:
				return Resource.CORRUPTED_PHREN_BARK;
			case ObjectID.LINUM_TIRINUM:
				return Resource.LINUM_TIRINUM;
			case ObjectID.CORRUPT_LINUM_TIRINUM:
				return Resource.CORRUPTED_LINUM_TIRINUM;
			case ObjectID.GRYM_ROOT:
				return Resource.GRYM_LEAF;
			case ObjectID.CORRUPT_GRYM_ROOT:
				return Resource.CORRUPTED_GRYM_LEAF;
			case ObjectID.CORRUPT_FISHING_SPOT:
			case ObjectID.FISHING_SPOT_36068:
				return Resource.RAW_PADDLEFISH;
			default:
				throw new IllegalArgumentException("Unsupported game object id: " + objectId);
		}
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}

		if (!(o instanceof ResourceGameObject))
		{
			return false;
		}

		final ResourceGameObject that = (ResourceGameObject) o;
		return gameObject.equals(that.gameObject);
	}

	@Override
	public int hashCode()
	{
		return gameObject.hashCode();
	}

	@Override
	public String toString()
	{
		return "SkillResource{resource=" + resource + ", gameObject=" + gameObject + '}';
	}
}
