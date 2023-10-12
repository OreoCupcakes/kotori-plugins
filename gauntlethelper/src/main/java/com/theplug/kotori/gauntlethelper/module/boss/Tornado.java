package com.theplug.kotori.gauntlethelper.module.boss;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.NPC;

@RequiredArgsConstructor
class Tornado
{
    private static final int TICK_DURATION = 21;

    @Getter
    private int timeLeft = TICK_DURATION;

    @Getter
    private final NPC npc;

    public void updateTimeLeft()
    {
        if (timeLeft >= 0)
        {
            timeLeft--;
        }
    }
}
