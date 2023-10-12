/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2023, rdutta <https://github.com/rdutta>
 * Copyright (c) 2020, Anthony Alves
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

import java.util.AbstractMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.ItemID;

public enum Resource
{
	TELEPORT_CRYSTAL("Teleport crystal", ItemID.TELEPORT_CRYSTAL, false),
	CORRUPTED_TELEPORT_CRYSTAL("Corrupted teleport crystal", ItemID.CORRUPTED_TELEPORT_CRYSTAL, true),

	WEAPON_FRAME("Weapon frame", ItemID.WEAPON_FRAME_23871, false),
	CORRUPTED_WEAPON_FRAME("Weapon frame", ItemID.WEAPON_FRAME, true),

	CRYSTALLINE_BOWSTRING("Crystalline bowstring", ItemID.CRYSTALLINE_BOWSTRING, false),
	CORRUPTED_BOWSTRING("Corrupted bowstring", ItemID.CORRUPTED_BOWSTRING, true),

	CRYSTAL_SPIKE("Crystal spike", ItemID.CRYSTAL_SPIKE, false),
	CORRUPTED_SPIKE("Corrupted spike", ItemID.CORRUPTED_SPIKE, true),

	CRYSTAL_ORB("Crystal orb", ItemID.CRYSTAL_ORB, false),
	CORRUPTED_ORB("Corrupted orb", ItemID.CORRUPTED_ORB, true),

	RAW_PADDLEFISH("Raw paddlefish", ItemID.RAW_PADDLEFISH, "You manage to catch a fish\\.", false),

	CRYSTAL_SHARDS("Crystal shards", ItemID.CRYSTAL_SHARDS, "You find (\\d+) crystal shards\\.", false),
	CORRUPTED_SHARDS("Corrupted shards", ItemID.CORRUPTED_SHARDS, "You find (\\d+) corrupted shards\\.", true),

	CRYSTAL_ORE("Crystal ore", ItemID.CRYSTAL_ORE, "You manage to mine some ore\\.", false),
	CORRUPTED_ORE("Corrupted ore", ItemID.CORRUPTED_ORE, "You manage to mine some ore\\.", true),

	PHREN_BARK("Phren bark", ItemID.PHREN_BARK_23878, "You get some bark\\.", false),
	CORRUPTED_PHREN_BARK("Phren bark", ItemID.PHREN_BARK, "You get some bark\\.", true),

	LINUM_TIRINUM("Linum tirinum", ItemID.LINUM_TIRINUM_23876, "You pick some fibre from the plant\\.", false),
	CORRUPTED_LINUM_TIRINUM("Linum tirinum", ItemID.LINUM_TIRINUM, "You pick some fibre from the plant\\.", true),

	GRYM_LEAF("Grym leaf", ItemID.GRYM_LEAF_23875, "You pick a herb from the roots\\.", false),
	CORRUPTED_GRYM_LEAF("Grym leaf", ItemID.GRYM_LEAF, "You pick a herb from the roots\\.", true);

	private static final Resource[] VALUES = Resource.values();

	private final String name;
	@Getter(AccessLevel.PACKAGE)
	private final int itemId;
	private final Pattern pattern;
	private final boolean corrupted;

	Resource(final String name, final int itemId, final String pattern, final boolean corrupted)
	{
		this.name = name;
		this.itemId = itemId;
		this.corrupted = corrupted;
		this.pattern = pattern != null ? Pattern.compile(pattern) : null;
	}

	Resource(final String name, final int itemId, final boolean corrupted)
	{
		this(name, itemId, null, corrupted);
	}

	static Resource fromName(final String name, final boolean corrupted)
	{
		for (final Resource resource : VALUES)
		{
			if ((resource.corrupted == corrupted || resource == RAW_PADDLEFISH) &&
				resource.name.equals(name))
			{
				return resource;
			}
		}

		return null;
	}

	static AbstractMap.Entry<Resource, Integer> fromPattern(final String pattern, final boolean corrupted)
	{
		for (final Resource resource : VALUES)
		{
			if (resource.pattern == null ||
				(corrupted != resource.corrupted && resource != Resource.RAW_PADDLEFISH))
			{
				continue;
			}

			final Matcher matcher = resource.pattern.matcher(pattern);

			if (!matcher.matches())
			{
				continue;
			}

			final int itemCount = matcher.groupCount() == 1 ? Integer.parseInt(matcher.group(1)) : 1;

			return new AbstractMap.SimpleImmutableEntry<>(resource, itemCount);
		}

		return null;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
