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
import net.runelite.api.gameval.ItemID;

public enum Resource
{
	//23904 23858
	TELEPORT_CRYSTAL("Teleport crystal", ItemID.GAUNTLET_TELEPORT_CRYSTAL, false),
	CORRUPTED_TELEPORT_CRYSTAL("Corrupted teleport crystal", ItemID.GAUNTLET_TELEPORT_CRYSTAL_HM, true),

	//23871 23834
	WEAPON_FRAME("Weapon frame", ItemID.GAUNTLET_GENERIC_COMPONENT, false),
	CORRUPTED_WEAPON_FRAME("Weapon frame", ItemID.GAUNTLET_GENERIC_COMPONENT_HM, true),

	//23869 23832
	CRYSTALLINE_BOWSTRING("Crystalline bowstring", ItemID.GAUNTLET_RANGED_COMPONENT, false),
	CORRUPTED_BOWSTRING("Corrupted bowstring", ItemID.GAUNTLET_RANGED_COMPONENT_HM, true),

	//23868 23831
	CRYSTAL_SPIKE("Crystal spike", ItemID.GAUNTLET_MELEE_COMPONENT, false),
	CORRUPTED_SPIKE("Corrupted spike", ItemID.GAUNTLET_MELEE_COMPONENT_HM, true),

	//23870 23833
	CRYSTAL_ORB("Crystal orb", ItemID.GAUNTLET_MAGIC_COMPONENT, false),
	CORRUPTED_ORB("Corrupted orb", ItemID.GAUNTLET_MAGIC_COMPONENT_HM, true),

	//23872
	RAW_PADDLEFISH("Raw paddlefish", ItemID.GAUNTLET_RAW_FOOD, "You manage to catch a fish\\.", false),

	//23866 23824
	CRYSTAL_SHARDS("Crystal shards", ItemID.GAUNTLET_CRYSTAL_SHARD, "You find (\\d+) crystal shards\\.", false),
	CORRUPTED_SHARDS("Corrupted shards", ItemID.GAUNTLET_CRYSTAL_SHARD_HM, "You find (\\d+) corrupted shards\\.", true),

	//23877 23837
	CRYSTAL_ORE("Crystal ore", ItemID.GAUNTLET_ORE, "You manage to mine some ore\\.", false),
	CORRUPTED_ORE("Corrupted ore", ItemID.GAUNTLET_ORE_HM, "You manage to mine some ore\\.", true),

	//23878 23838
	PHREN_BARK("Phren bark", ItemID.GAUNTLET_BARK, "You get some bark\\.", false),
	CORRUPTED_PHREN_BARK("Phren bark", ItemID.GAUNTLET_BARK_HM, "You get some bark\\.", true),

	//23876 23836
	LINUM_TIRINUM("Linum tirinum", ItemID.GAUNTLET_FIBRE, "You pick some fibre from the plant\\.", false),
	CORRUPTED_LINUM_TIRINUM("Linum tirinum", ItemID.GAUNTLET_FIBRE_HM, "You pick some fibre from the plant\\.", true),

	//23875 23835
	GRYM_LEAF("Grym leaf", ItemID.GAUNTLET_HERB, "You pick a herb from the roots\\.", false),
	CORRUPTED_GRYM_LEAF("Grym leaf", ItemID.GAUNTLET_HERB_HM, "You pick a herb from the roots\\.", true);

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
