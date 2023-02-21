package com.theplug.kotori.vorkathoverlayrlpl.utils;

import lombok.Getter;
import net.runelite.api.Projectile;

public class Missile
{
    @Getter
    private final Projectile projectile;

    public Missile(Projectile projectile) {
        this.projectile = projectile;
    }
}
