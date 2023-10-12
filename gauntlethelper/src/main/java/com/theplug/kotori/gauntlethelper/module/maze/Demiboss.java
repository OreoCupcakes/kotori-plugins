/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2023, rdutta <https://github.com/rdutta>
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
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.client.game.SkillIconManager;

class Demiboss
{
	private final NPC npc;

	@Getter(AccessLevel.PACKAGE)
	private final BufferedImage minimapIcon;

	Demiboss(@NonNull final NPC npc, @NonNull final SkillIconManager skillIconManager)
	{
		this.npc = npc;
		minimapIcon = getIcon(npc, skillIconManager);
	}

	boolean isNpc(final NPC npc)
	{
		return this.npc == npc;
	}

	@Nullable
	Point getMinimapPoint()
	{
		final Point point = npc.getMinimapLocation();

		if (point == null)
		{
			return null;
		}

		return new Point(point.getX() - minimapIcon.getHeight() / 2, point.getY() - minimapIcon.getWidth() / 2);
	}

	private static BufferedImage getIcon(final NPC npc, final SkillIconManager skillIconManager)
	{
		switch (npc.getId())
		{
			case NpcID.CRYSTALLINE_BEAR:
			case NpcID.CORRUPTED_BEAR:
				return skillIconManager.getSkillImage(Skill.ATTACK, true);
			case NpcID.CRYSTALLINE_DARK_BEAST:
			case NpcID.CORRUPTED_DARK_BEAST:
				return skillIconManager.getSkillImage(Skill.RANGED, true);
			case NpcID.CRYSTALLINE_DRAGON:
			case NpcID.CORRUPTED_DRAGON:
				return skillIconManager.getSkillImage(Skill.MAGIC, true);
			default:
				throw new IllegalArgumentException("Unsupported npc id: " + npc.getId());
		}
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}

		if (!(o instanceof Demiboss))
		{
			return false;
		}

		final Demiboss that = (Demiboss) o;
		return npc.equals(that.npc);
	}

	@Override
	public int hashCode()
	{
		return npc.hashCode();
	}

	@Override
	public String toString()
	{
		return "Demiboss{npc=" + npc + '}';
	}
}
