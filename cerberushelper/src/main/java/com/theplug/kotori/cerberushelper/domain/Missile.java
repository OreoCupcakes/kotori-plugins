package com.theplug.kotori.cerberushelper.domain;

import lombok.Getter;
import net.runelite.api.Projectile;

public class Missile
{
    @Getter
    private final Projectile projectile;

    public Missile (Projectile projectile) {
        this.projectile = projectile;
    }
}
