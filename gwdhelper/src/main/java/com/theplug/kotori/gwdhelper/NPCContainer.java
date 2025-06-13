/*
 * Copyright (c) 2019, Ganom <https://github.com/Ganom>
 * Copyright (c) 2019, Lucas <https://github.com/lucwousin>
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
package com.theplug.kotori.gwdhelper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.*;
import net.runelite.api.*;
import net.runelite.api.gameval.NpcID;

import java.awt.*;
import java.util.Objects;

import static com.theplug.kotori.gwdhelper.GodWarsHelperPlugin.*;

@Getter(AccessLevel.PACKAGE)
class NPCContainer
{
	@Getter(AccessLevel.PACKAGE)
	private final NPC npc;
	private final int npcIndex;
	private final String npcName;
	private final ImmutableSet<Integer> animations;
	private final int attackSpeed;
	@Getter(AccessLevel.PACKAGE)
	private final BossMonsters monsterType;
	private int npcSize;
	@Setter(AccessLevel.PACKAGE)
	private int ticksUntilAttack;
	@Setter(AccessLevel.PACKAGE)
	private Actor npcInteracting;
	@Setter(AccessLevel.PACKAGE)
	private AttackStyle attackStyle;

	NPCContainer(final NPC npc)
	{
		this.npc = npc;
		this.npcName = npc.getName();
		this.npcIndex = npc.getIndex();
		this.npcInteracting = npc.getInteracting();
		this.attackStyle = AttackStyle.UNKNOWN;
		this.ticksUntilAttack = -1;
		final NPCComposition composition = npc.getTransformedComposition();

		BossMonsters monster = BossMonsters.of(npc.getId());

		if (monster == null)
		{
			throw new IllegalStateException();
		}

		this.monsterType = monster;
		this.animations = monster.animations;
		this.attackStyle = monster.attackStyle;
		this.attackSpeed = monster.attackSpeed;

		if (composition != null)
		{
			this.npcSize = composition.getSize();
		}
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(npc);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		NPCContainer that = (NPCContainer) o;
		return Objects.equals(npc, that.npc);
	}

	@RequiredArgsConstructor
	public enum BossMonsters
	{
		SERGEANT_STRONGSTACK(NpcID.GODWARS_SERGEANT_GOBLIN1, AttackStyle.MELEE, ImmutableSet.of(SERGEANT_STRONGSTACK_AUTO, BANDOS_BODYGUARDS_DEATH_ID), 5),
		SERGEANT_STEELWILL(NpcID.GODWARS_SERGEANT_GOBLIN2, AttackStyle.MAGE, ImmutableSet.of(SERGEANT_STEELWILL_AUTO, BANDOS_BODYGUARDS_DEATH_ID), 5),
		SERGEANT_GRIMSPIKE(NpcID.GODWARS_SERGEANT_GOBLIN3, AttackStyle.RANGE, ImmutableSet.of(SERGEANT_GRIMSPIKE_AUTO, BANDOS_BODYGUARDS_DEATH_ID), 5),
		GENERAL_GRAARDOR(NpcID.GODWARS_BANDOS_AVATAR, AttackStyle.MELEE, ImmutableSet.of(GENERAL_AUTO1, GENERAL_AUTO2, GENERAL_AUTO3, GENERAL_GRAARDOR_DEATH_ID), 6),

		TSTANON_KARLAK(NpcID.GODWARS_ANCIENT_GREATER_DEMON, AttackStyle.MELEE, ImmutableSet.of(ZAMMY_GENERIC_AUTO_1, ZAMMY_GENERIC_AUTO_2, TSTANON_KARLAK_DEATH_ID), 5),
		BALFRUG_KREEYATH(NpcID.GODWARS_ANCIENT_BLACK_DEMON, AttackStyle.MAGE, ImmutableSet.of(BALFRUG_AUTO, ZAMORAK_BODYGUARDS_DEATH_ID), 5),
		ZAKLN_GRITCH(NpcID.GODWARS_ANCIENT_LESSER_DEMON, AttackStyle.RANGE, ImmutableSet.of(ZAKL_AUTO, ZAMORAK_BODYGUARDS_DEATH_ID), 5),
		KRIL_TSUTSAROTH(NpcID.GODWARS_ZAMORAK_AVATAR, AttackStyle.UNKNOWN, ImmutableSet.of(KRIL_SPEC, KRIL_AUTO, KRIL_AUTO_2, KRIL_TSUTSAROTH_DEATH_ID), 6),

		STARLIGHT(NpcID.GODWARS_SARADOMIN_UNICORN, AttackStyle.MELEE, ImmutableSet.of(STARLIGHT_AUTO, STARLIGHT_AUTO_2, STARLIGHT_DEATH_ID), 5),
		GROWLER(NpcID.GODWARS_SARADOMIN_LION, AttackStyle.MAGE, ImmutableSet.of(GROWLER_AUTO, GROWLER_AUTO_2, GROWLER_DEATH_ID), 5),
		BREE(NpcID.GODWARS_SARADOMIN_CENTAUR, AttackStyle.RANGE, ImmutableSet.of(BREE_AUTO, BREE_DEATH_ID), 5),
		COMMANDER_ZILYANA(NpcID.GODWARS_SARADOMIN_AVATAR, AttackStyle.UNKNOWN, ImmutableSet.of(ZILYANA_AUTO, ZILYANA_AUTO_2, ZILYANA_MELEE_AUTO, ZILYANA_SPEC, COMMANDER_ZILYANA_DEATH_ID), 2),

		FLIGHT_KILISA(NpcID.GODWARS_ARMADYL_BODYGUARD_KILISA, AttackStyle.MELEE, ImmutableSet.of(KILISA_AUTO, ARMADYL_BODYGUARDS_DEATH_ID), 5),
		FLOCKLEADER_GEERIN(NpcID.GODWARS_ARMADYL_BODYGUARD_GEERIN, AttackStyle.RANGE, ImmutableSet.of(GEERIN_AUTO, GEERIN_FLINCH, ARMADYL_BODYGUARDS_DEATH_ID), 5),
		WINGMAN_SKREE(NpcID.GODWARS_ARMADYL_BODYGUARD_SKREE, AttackStyle.MAGE, ImmutableSet.of(SKREE_AUTO, ARMADYL_BODYGUARDS_DEATH_ID), 5),
		KREEARRA(NpcID.GODWARS_ARMADYL_AVATAR, AttackStyle.RANGE, ImmutableSet.of(KREE_RANGED, KREE_RANGED_2, KREE_ARRA_DEATH_ID), 3);

		private static final ImmutableMap<Integer, BossMonsters> idMap;

		static
		{
			ImmutableMap.Builder<Integer, BossMonsters> builder = ImmutableMap.builder();

			for (BossMonsters monster : values())
			{
				builder.put(monster.npcID, monster);
			}

			idMap = builder.build();
		}

		private final int npcID;
		private final AttackStyle attackStyle;
		private final ImmutableSet<Integer> animations;
		private final int attackSpeed;

		static BossMonsters of(int npcID)
		{
			return idMap.get(npcID);
		}
	}

	@AllArgsConstructor
	@Getter
	public enum AttackStyle
	{
		MAGE("Mage", Color.CYAN, Prayer.PROTECT_FROM_MAGIC),
		RANGE("Range", Color.GREEN, Prayer.PROTECT_FROM_MISSILES),
		MELEE("Melee", Color.RED, Prayer.PROTECT_FROM_MELEE),
		UNKNOWN("Unknown", Color.WHITE, null);

		private final String name;
		private final Color color;
		private final Prayer prayer;
	}
}
