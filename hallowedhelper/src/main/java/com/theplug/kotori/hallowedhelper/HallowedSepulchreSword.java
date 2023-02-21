package com.theplug.kotori.hallowedhelper;

import lombok.*;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class HallowedSepulchreSword
{
    @NonNull
    @EqualsAndHashCode.Include
    private final NPC npc;

    private int ticksUntilNextAnimation = -20;

    private int maxTick = -1;

    public boolean forward = true;

    public GameObject thrower;

    public int distance = -1;

    private int offset = 0;

    public void updateState()
    {
        if(thrower != null)
        {
            int newdistance = thrower.getWorldLocation().distanceTo2D(npc.getWorldLocation());
            if(distance != -1)
            {
                if(newdistance < distance) {
                    forward = false;//alleen false want hij gaat weg lol
                    offset = 0;
                }
            }
            if(distance != newdistance) {
                distance = newdistance;
            }
            else
            {
                if(forward) {
                    offset++;
                }
                else
                {
                    offset--;
                }
            }
        }
    }

    void setThrower(GameObject setthrower)
    {
        thrower = setthrower;
    }

    void setForward(boolean state)
    {
        forward = state;
    }
}