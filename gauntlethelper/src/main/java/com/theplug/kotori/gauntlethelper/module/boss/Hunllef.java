package com.theplug.kotori.gauntlethelper.module.boss;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.NPC;
import net.runelite.api.Prayer;

import java.awt.*;

class Hunllef
{
    private static final int ATTACK_TICK_SPEED = 6;
    private static final int ECHO_ATTACK_TICK_SPEED = 5;

    private static final int MAX_ATTACK_COUNT = 4;
    private static final int MAX_PLAYER_ATTACK_COUNT = 6;

    @Getter
    private final NPC npc;

    @Getter
    private AttackPhase attackPhase;

    @Getter
    private int attackCount;
    @Getter
    private int playerAttackCount;
    @Getter
    private int ticksUntilNextAttack;

    @Getter
    private final boolean echo;

    public Hunllef(final NPC npc, boolean echo)
    {
        this.npc = npc;
        this.echo = echo;

        this.attackCount = MAX_ATTACK_COUNT;
        this.playerAttackCount = MAX_PLAYER_ATTACK_COUNT;
        this.ticksUntilNextAttack = 0;

        this.attackPhase = AttackPhase.RANGE;
    }

    public void decrementTicksUntilNextAttack()
    {
        if (ticksUntilNextAttack > 0)
        {
            ticksUntilNextAttack--;
        }
    }

    public void updatePlayerAttackCount()
    {
        if (--playerAttackCount <= 0)
        {
            playerAttackCount = MAX_PLAYER_ATTACK_COUNT;
        }
    }

    public void updateAttackCount()
    {
        if (this.echo)
        {
            ticksUntilNextAttack = ECHO_ATTACK_TICK_SPEED;
        }
        else
        {
            ticksUntilNextAttack = ATTACK_TICK_SPEED;
        }

        if (--attackCount <= 0)
        {
            attackPhase = attackPhase == AttackPhase.RANGE ? AttackPhase.MAGIC : AttackPhase.RANGE;
            attackCount = MAX_ATTACK_COUNT;
        }
    }

    @AllArgsConstructor
    @Getter
    public enum AttackPhase
    {
        MAGIC(Color.CYAN, Prayer.PROTECT_FROM_MAGIC),
        RANGE(Color.GREEN, Prayer.PROTECT_FROM_MISSILES);

        private final Color color;
        private final Prayer prayer;
    }
}
