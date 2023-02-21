package com.theplug.kotori.hallowedhelper;

import lombok.*;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class HallowedSepulchreWizardStatue
{
    @NonNull
    @EqualsAndHashCode.Include
    private final GameObject gameObject;

    private int ticksUntilNextAnimation = -20;

    private final int animationId;

    private final int animationTickSpeed;

    private int maxTick = -1;


    private static final int WIZARD_STATUE_ANIM_FIRE_DANGER = 8656;
    private boolean danger = false;

    public int maxTickperfloor(int floor, int subfloor)
    {
        if (floor == 3)
        {
            if (maxTick == -1)
            {
                //if(subfloor == 1)
                //{//MID
                    return -12;
                //}
            }
        }
        else if(floor < 3)//2 and 1
        {
            return -4;
        }
        return maxTick;
    }

    void updateTicksUntilNextAnimation()
    {
        if (ticksUntilNextAnimation >= 0)
        {
            /*
            if(ticksUntilNextAnimation == 1)
            {
                danger = false;
            }*/
            ticksUntilNextAnimation--;
        }
        else
        {
            int animation = getAnimation();
            /*
            if(animation == WIZARD_STATUE_ANIM_FIRE_DANGER)
            {
                danger = true;
            }*/
            if (animation == animationId)
            {
                if(ticksUntilNextAnimation != -20)
                {
                    maxTick = ticksUntilNextAnimation;
                }
                ticksUntilNextAnimation = animationTickSpeed;
            }
            else
            {
                if(ticksUntilNextAnimation > -20) {
                    ticksUntilNextAnimation--;
                }
            }
        }
    }

    int getAnimation()
    {
        final DynamicObject dynamicObject = (DynamicObject) gameObject.getRenderable();
        return (int) (dynamicObject.getAnimation() == null ? -1 : dynamicObject.getAnimation().getId());
    }
}