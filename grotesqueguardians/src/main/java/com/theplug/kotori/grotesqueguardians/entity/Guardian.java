package com.theplug.kotori.grotesqueguardians.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Prayer;
import net.runelite.api.Projectile;

import java.awt.*;
import java.util.Set;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Guardian
{
    private static final int DUSK_PHASE_1_ANIMATION_MELEE = 7785;

    private static final int DUSK_PHASE_2_ANIMATION_MELEE_7786 = 7786;
    private static final int DUSK_PHASE_2_ANIMATION_MELEE_7788 = 7788;
    public static final int DUSK_PHASE_2_ECLIPSE_EXPLOSION = 7802;

    private static final int DUSK_PHASE_3_ANIMATION_MELEE_7785 = 7785;
    private static final int DUSK_PHASE_3_ANIMATION_MELEE_7787 = 7787;

    private static final int DUSK_PHASE_4_ANIMATION_MELEE = 7800;
    private static final int DUSK_PHASE_4_ANIMATION_RANGE = 7801;

    public static final int ECHO_DUSK_PHASE_2_TRANSITION = 7799;

    private static final int DUSK_ATTACK_TICK_SPEED = 6;
    private static final int DEFINITELY_NOT_DUSK_ATTACK_TICK_SPEED = 12;

    private static final int DAWN_PROJECTILE_STONE_ORB = 1445;
    private static final int DAWN_PROJECTILE_RANGED_ATTACK = 1444;

    private static final int DAWN_ANIMATION_STONE_ORB = 7771;
    private static final int DAWN_ANIMATION_RANGED_ATTACK = 7770;
    private static final int DAWN_ANIMATION_MELEE_ATTACK = 7769;

    private static final int DAWN_ATTACK_TICK_SPEED = 6;
    private static final int ECHO_DAWN_ATTACK_TICK_SPEED = 12;


    @EqualsAndHashCode.Include
    private final NPC npc;

    @Getter
    private final int npcId;

    @Getter
    private final String npcName;

    @Getter
    private int ticksUntilNextAttack;

    private final Set<Integer> attackAnimations;

    private final Set<Integer> projectileIds;

    private final int attackTickSpeed;

    @Getter
    private AttackStyle attackStyle;

    @Getter
    private Projectile lastAttackProjectile;

    private final boolean echoVariant;

    @Setter
    private boolean echoVariantPhased;

    public Guardian(final NPC npc, boolean echo)
    {
        this.npc = npc;
        this.npcId = npc.getId();
        this.npcName = npc.getName();
        this.echoVariant = echo;
        this.ticksUntilNextAttack = 0;

        final Variant variant = Variant.of(npcId, echoVariant);
        if (variant == null)
        {
            this.attackAnimations = Set.of();
            this.projectileIds = Set.of();
            this.attackTickSpeed = echoVariant ? ECHO_DAWN_ATTACK_TICK_SPEED : DUSK_ATTACK_TICK_SPEED;
            this.attackStyle = AttackStyle.UNKNOWN;
        }
        else
        {
            this.attackAnimations = variant.attackAnimationIdSet;
            this.projectileIds = variant.projectileIdSet;
            this.attackTickSpeed = variant.attackSpeed;
            this.attackStyle = variant.attackStyle;
        }
    }

    public void updateTicksUntilNextAttack()
    {
        if (ticksUntilNextAttack > 0)
        {
            ticksUntilNextAttack--;
        }

        if (ticksUntilNextAttack <= 0)
        {
            final Guardian.Variant phase = getVariant();

            if (phase == null)
            {
                return;
            }

            final int animationId = npc.getAnimation();

            if (!phase.getAttackAnimationIdSet().contains(animationId) && lastAttackProjectile == null)
            {
                return;
            }

            updateLastAttackStyle(animationId);
            ticksUntilNextAttack = echoVariantPhased ? DUSK_ATTACK_TICK_SPEED : phase.getAttackSpeed();
        }
    }

    public void updateLastAttackStyle(final int animationId)
    {
        if (isLastDuskPhase())
        {
            switch (animationId)
            {
                case DUSK_PHASE_4_ANIMATION_MELEE:
                    attackStyle = AttackStyle.MELEE;
                    break;
                case DUSK_PHASE_4_ANIMATION_RANGE:
                    attackStyle = AttackStyle.RANGE;
                    break;
            }
        }
        else
        {
            attackStyle = getVariant().getAttackStyle();
        }
    }

    public void updateLastAttackProjectile(final Projectile projectile)
    {
        final Guardian.Variant phase = getVariant();

        if (phase == null)
        {
            return;
        }

        final int projectileId = projectile.getId();

        Set<Integer> ids = phase.getProjectileIdSet();

        if (ids == null || ids.isEmpty())
        {
            return;
        }

        if (ids.contains(projectileId))
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

    public Variant getVariant()
    {
        return Variant.of(npc.getId(), echoVariant);
    }

    public boolean isLastDuskPhase()
    {
        Variant variant = Variant.of(npc.getId(), echoVariant);
        return variant == Variant.DUSK_PHASE_4 || variant == Variant.ECHO_DUSK;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Variant
    {
        DUSK_PHASE_1(NpcID.DUSK_7851, false, Set.of(DUSK_PHASE_1_ANIMATION_MELEE), Set.of(), DUSK_ATTACK_TICK_SPEED, AttackStyle.MELEE),
        DUSK_PHASE_2(NpcID.DUSK_7882, false, Set.of(DUSK_PHASE_2_ANIMATION_MELEE_7786, DUSK_PHASE_2_ANIMATION_MELEE_7788, DUSK_PHASE_2_ECLIPSE_EXPLOSION), Set.of(), DUSK_ATTACK_TICK_SPEED, AttackStyle.MELEE),
        DUSK_PHASE_3(NpcID.DUSK_7883, false, Set.of(DUSK_PHASE_3_ANIMATION_MELEE_7785, DUSK_PHASE_3_ANIMATION_MELEE_7787), Set.of(), DUSK_ATTACK_TICK_SPEED, AttackStyle.MELEE),
        DUSK_PHASE_4(NpcID.DUSK_7888, false, Set.of(DUSK_PHASE_4_ANIMATION_MELEE, DUSK_PHASE_4_ANIMATION_RANGE), Set.of(DAWN_PROJECTILE_RANGED_ATTACK), DUSK_ATTACK_TICK_SPEED, AttackStyle.RANGE),
        DAWN_PHASE_1(NpcID.DAWN_7852, false, Set.of(DAWN_ANIMATION_MELEE_ATTACK, DAWN_ANIMATION_RANGED_ATTACK, DAWN_ANIMATION_STONE_ORB), Set.of(DAWN_PROJECTILE_RANGED_ATTACK, DAWN_PROJECTILE_STONE_ORB), DAWN_ATTACK_TICK_SPEED, AttackStyle.RANGE),
        DAWN_PHASE_3(NpcID.DAWN_7884, false, Set.of(DAWN_ANIMATION_MELEE_ATTACK, DAWN_ANIMATION_RANGED_ATTACK, DAWN_ANIMATION_STONE_ORB), Set.of(DAWN_PROJECTILE_RANGED_ATTACK, DAWN_PROJECTILE_STONE_ORB), DAWN_ATTACK_TICK_SPEED, AttackStyle.RANGE),
        ECHO_DUSK(NpcID.DUSK_7888, true, Set.of(DUSK_PHASE_4_ANIMATION_RANGE), Set.of(DAWN_PROJECTILE_RANGED_ATTACK), DUSK_ATTACK_TICK_SPEED, AttackStyle.RANGE),
        ECHO_DAWN(NpcID.DAWN_7852, true, Set.of(DAWN_ANIMATION_STONE_ORB), Set.of(DAWN_PROJECTILE_STONE_ORB), ECHO_DAWN_ATTACK_TICK_SPEED, AttackStyle.RANGE),
        DEFINITELY_NOT_DUSK(NpcID.DUSK_7882, true, Set.of(DUSK_PHASE_2_ECLIPSE_EXPLOSION), Set.of(), DEFINITELY_NOT_DUSK_ATTACK_TICK_SPEED, AttackStyle.UNKNOWN);


        private final int npcId;
        private final boolean echo;
        private final Set<Integer> attackAnimationIdSet;
        private final Set<Integer> projectileIdSet;
        private final int attackSpeed;
        private final AttackStyle attackStyle;

        public static Variant of(final int npcId, final boolean echoVariant)
        {
            for (final Variant variant : Variant.values())
            {
                if (variant.npcId == npcId && variant.echo == echoVariant)
                {
                    return variant;
                }
            }

            return null;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum AttackStyle
    {
        MAGE(Prayer.PROTECT_FROM_MAGIC, 3, Color.CYAN),
        RANGE(Prayer.PROTECT_FROM_MISSILES, 2, Color.GREEN),
        MELEE(Prayer.PROTECT_FROM_MELEE, 1, Color.RED),
        UNKNOWN(null, 0, Color.WHITE);

        private final Prayer prayer;
        private final int priority;
        private final Color color;
    }
}
