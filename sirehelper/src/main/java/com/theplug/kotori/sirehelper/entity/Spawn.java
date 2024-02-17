package com.theplug.kotori.sirehelper.entity;

import lombok.Getter;
import lombok.NonNull;
import net.runelite.api.NPC;

@Getter
public class Spawn
{
    private static final int EVOLUTION_TIMER = 20;

    @NonNull
    private final NPC npc;

    private int evolutionTimer;

    public Spawn(@NonNull final NPC npc)
    {
        this.npc = npc;

        this.evolutionTimer = EVOLUTION_TIMER;
    }

    public void decrementTicksUntilEvolution()
    {
        evolutionTimer--;
    }
}
