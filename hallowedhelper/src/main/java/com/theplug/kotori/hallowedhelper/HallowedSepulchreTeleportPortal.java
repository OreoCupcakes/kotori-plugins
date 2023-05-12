package com.theplug.kotori.hallowedhelper;

import lombok.*;
import net.runelite.api.GraphicsObject;

@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HallowedSepulchreTeleportPortal
{
	@NonNull
	@EqualsAndHashCode.Include
	private final GraphicsObject graphicsObject;
	
	private int ticksUntilDespawn = 5;
	public void decrementTicksUntilDespawn()
	{
		this.ticksUntilDespawn--;
	}
}
