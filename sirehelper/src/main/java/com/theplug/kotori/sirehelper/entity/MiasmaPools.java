package com.theplug.kotori.sirehelper.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import net.runelite.api.GraphicsObject;
import net.runelite.api.coords.LocalPoint;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MiasmaPools
{
    private static final int MIASMA_TICKS = 7;

    @NonNull
    @EqualsAndHashCode.Include
    private final GraphicsObject graphicsObject;

    private final LocalPoint localPoint;

    private int ticksUntilDespawn;

    public MiasmaPools(GraphicsObject graphicsObject)
    {
        this.graphicsObject = graphicsObject;
        this.localPoint = graphicsObject.getLocation();
        this.ticksUntilDespawn = MIASMA_TICKS;
    }

    public void decrementTicksUntilDespawn()
    {
        ticksUntilDespawn--;
    }
}
