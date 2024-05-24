package com.theplug.kotori.aoewarnings;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Projectile;
import net.runelite.api.coords.LocalPoint;

@Getter(AccessLevel.PACKAGE)
class ProjectileContainer
{
	private final Projectile projectile;
	private final Instant startTime;
	private final AoeProjectileInfo aoeProjectileInfo;
	private final int lifetime;
	private final int finalTick;
	@Setter(AccessLevel.PACKAGE)
	private LocalPoint targetPoint;

	ProjectileContainer(Projectile projectile, Instant startTime, int lifetime, int finalTick)
	{
		this.projectile = projectile;
		this.startTime = startTime;
		this.targetPoint = null;
		this.aoeProjectileInfo = AoeProjectileInfo.getById(projectile.getId());
		this.lifetime = lifetime;
		this.finalTick = finalTick;
	}
}
