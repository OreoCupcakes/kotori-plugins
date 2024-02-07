package com.theplug.kotori.sirehelper.entity;

import com.theplug.kotori.kotoriutils.methods.NPCInteractions;
import lombok.Getter;
import lombok.NonNull;
import net.runelite.api.NPC;
import net.runelite.api.coords.LocalPoint;

@Getter
public class RespiratorySystem
{
    private static final int MAX_HP = 50;

    @NonNull
    private final NPC npc;
    private final LocalPoint localPoint;
    private int hp;
    private int damageDealt;

    public RespiratorySystem(@NonNull final NPC npc)
    {
        this.npc = npc;
        this.localPoint = npc.getLocalLocation();
        this.hp = MAX_HP;
        this.damageDealt = 0;
    }

    public void updateHp()
    {
        final int calculatedHp = NPCInteractions.calculateNpcHp(npc.getHealthRatio(), npc.getHealthScale(), MAX_HP);

        if (calculatedHp != -1)
        {
            hp = calculatedHp;
        }
        else if (npc.isDead())
        {
            hp = 0;
        }
    }

    public void addToDamageDealt(int damage)
    {
        damageDealt += damage;
    }
}
