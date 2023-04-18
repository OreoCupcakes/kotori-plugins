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
		SERGEANT_STRONGSTACK(NpcID.SERGEANT_STRONGSTACK, AttackStyle.MELEE, ImmutableSet.of(SERGEANT_STRONGSTACK_AUTO, BANDOS_BODYGUARDS_DEATH_ID), 5),
		SERGEANT_STEELWILL(NpcID.SERGEANT_STEELWILL, AttackStyle.MAGE, ImmutableSet.of(SERGEANT_STEELWILL_AUTO, BANDOS_BODYGUARDS_DEATH_ID), 5),
		SERGEANT_GRIMSPIKE(NpcID.SERGEANT_GRIMSPIKE, AttackStyle.RANGE, ImmutableSet.of(SERGEANT_GRIMSPIKE_AUTO, BANDOS_BODYGUARDS_DEATH_ID), 5),
		GENERAL_GRAARDOR(NpcID.GENERAL_GRAARDOR, AttackStyle.MELEE, ImmutableSet.of(GENERAL_AUTO1, GENERAL_AUTO2, GENERAL_AUTO3, GENERAL_GRAARDOR_DEATH_ID), 6),

		TSTANON_KARLAK(NpcID.TSTANON_KARLAK, AttackStyle.MELEE, ImmutableSet.of(ZAMMY_GENERIC_AUTO_1, ZAMMY_GENERIC_AUTO_2, TSTANON_KARLAK_DEATH_ID), 5),
		BALFRUG_KREEYATH(NpcID.BALFRUG_KREEYATH, AttackStyle.MAGE, ImmutableSet.of(BALFRUG_AUTO, ZAMORAK_BODYGUARDS_DEATH_ID), 5),
		ZAKLN_GRITCH(NpcID.ZAKLN_GRITCH, AttackStyle.RANGE, ImmutableSet.of(ZAKL_AUTO, ZAMORAK_BODYGUARDS_DEATH_ID), 5),
		KRIL_TSUTSAROTH(NpcID.KRIL_TSUTSAROTH, AttackStyle.UNKNOWN, ImmutableSet.of(KRIL_SPEC, KRIL_AUTO, KRIL_AUTO_2, KRIL_TSUTSAROTH_DEATH_ID), 6),

		STARLIGHT(NpcID.STARLIGHT, AttackStyle.MELEE, ImmutableSet.of(STARLIGHT_AUTO, STARLIGHT_AUTO_2, STARLIGHT_DEATH_ID), 5),
		GROWLER(NpcID.GROWLER, AttackStyle.MAGE, ImmutableSet.of(GROWLER_AUTO, GROWLER_AUTO_2, GROWLER_DEATH_ID), 5),
		BREE(NpcID.BREE, AttackStyle.RANGE, ImmutableSet.of(BREE_AUTO, BREE_DEATH_ID), 5),
		COMMANDER_ZILYANA(NpcID.COMMANDER_ZILYANA, AttackStyle.UNKNOWN, ImmutableSet.of(ZILYANA_AUTO, ZILYANA_AUTO_2, ZILYANA_MELEE_AUTO, ZILYANA_SPEC, COMMANDER_ZILYANA_DEATH_ID), 2),

		FLIGHT_KILISA(NpcID.FLIGHT_KILISA, AttackStyle.MELEE, ImmutableSet.of(KILISA_AUTO, ARMADYL_BODYGUARDS_DEATH_ID), 5),
		FLOCKLEADER_GEERIN(NpcID.FLOCKLEADER_GEERIN, AttackStyle.RANGE, ImmutableSet.of(GEERIN_AUTO, GEERIN_FLINCH, ARMADYL_BODYGUARDS_DEATH_ID), 5),
		WINGMAN_SKREE(NpcID.WINGMAN_SKREE, AttackStyle.MAGE, ImmutableSet.of(SKREE_AUTO, ARMADYL_BODYGUARDS_DEATH_ID), 5),
		KREEARRA(NpcID.KREEARRA, AttackStyle.RANGE, ImmutableSet.of(KREE_RANGED, KREE_RANGED_2, KREE_ARRA_DEATH_ID), 3);

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

		private String name;
		private Color color;
		private Prayer prayer;
	}
}
