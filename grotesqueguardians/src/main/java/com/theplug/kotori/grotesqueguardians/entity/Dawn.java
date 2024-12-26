/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2020, dutta64 <https://github.com/dutta64>
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

package com.theplug.kotori.grotesqueguardians.entity;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Prayer;
import net.runelite.api.Projectile;

public class Dawn extends Gargoyle
{
	private static final int PROJECTILE_STONE_ORB = 1445;
	private static final int PROJECTILE_RANGED_ATTACK = 1444;

	private static final int ANIMATION_STONE_ORB = 7771;
	private static final int ANIMATION_RANGED_ATTACK = 7770;
	private static final int ANIMATION_MELEE_ATTACK = 7769;

	private static final int ATTACK_TICK_SPEED = 6;
	private static final int ECHO_ATTACK_TICK_SPEED = 12;

	private final boolean echoVariant;

	@Getter
	private Projectile lastAttackProjectile;

	public Dawn(@NonNull final NPC npc, boolean echoVariant)
	{
		super(npc);
		this.echoVariant = echoVariant;
	}

	public void setLastAttackProjectile(final Projectile projectile)
	{
		final Dawn.Phase phase = getPhase();

		if (phase == null)
		{
			return;
		}

		final int projectileId = projectile.getId();

		if (phase.getProjectileIdSet().contains(projectileId))
		{
			lastAttackProjectile = projectile;
		}
	}

	public void removeExpiredProjectile()
	{
		if (lastAttackProjectile == null)
		{
			return;
		}

		if (lastAttackProjectile.getRemainingCycles() <= 0)
		{
			lastAttackProjectile = null;
		}
	}

	@Override
	public void updateTicksUntilNextAttack()
	{
		// Dawn npc does not always show animation ID when attacking
		// Currently unused
		if (ticksUntilNextAttack > 0)
		{
			ticksUntilNextAttack--;
		}

		if (ticksUntilNextAttack <= 0)
		{
			final Dawn.Phase phase = getPhase();

			if (phase == null)
			{
				return;
			}

			final int animationId = npc.getAnimation();

			if (!phase.getAttackAnimationIdSet().contains(animationId) && lastAttackProjectile == null)
			{
				return;
			}

			ticksUntilNextAttack = this.echoVariant ? ECHO_ATTACK_TICK_SPEED : ATTACK_TICK_SPEED;
		}
	}

	public Phase getPhase()
	{
		return Phase.of(npc.getId());
	}

	@Getter
	@RequiredArgsConstructor
	public enum Phase
	{
		PHASE_1(NpcID.DAWN_7852, Set.of(ANIMATION_MELEE_ATTACK, ANIMATION_RANGED_ATTACK, ANIMATION_STONE_ORB), Set.of(PROJECTILE_RANGED_ATTACK, PROJECTILE_STONE_ORB)),
		PHASE_3(NpcID.DAWN_7884, Set.of(ANIMATION_MELEE_ATTACK, ANIMATION_RANGED_ATTACK, ANIMATION_STONE_ORB), Set.of(PROJECTILE_RANGED_ATTACK, PROJECTILE_STONE_ORB));

		private static final Map<Integer, Phase> MAP;

		static
		{
			final ImmutableMap.Builder<Integer, Phase> builder = new ImmutableMap.Builder<>();

			for (final Phase phase : Phase.values())
			{
				builder.put(phase.getNpcId(), phase);
			}

			MAP = builder.build();
		}

		private final int npcId;
		private final Set<Integer> attackAnimationIdSet;
		@Getter
		private final Set<Integer> projectileIdSet;

		static Phase of(final int npcId)
		{
			return MAP.get(npcId);
		}
	}
}
