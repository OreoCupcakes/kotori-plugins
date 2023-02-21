package com.theplug.kotori.effecttimers.utils;

import net.runelite.api.WorldType;

import java.util.Collection;
import java.util.EnumSet;

public class WorldTypeExtended {
    private static final EnumSet<WorldType> DEADMAN_WORLD_TYPES = EnumSet.of(
            WorldType.DEADMAN
    );

    private static final EnumSet<WorldType> HIGHRISK_WORLD_TYPES = EnumSet.of(
            WorldType.HIGH_RISK
    );

    private static final EnumSet<WorldType> ALL_HIGHRISK_WORLD_TYPES = EnumSet.of(
            WorldType.HIGH_RISK,
            WorldType.DEADMAN
    );

    private static final EnumSet<WorldType> ALL_PVP_WORLD_TYPES = EnumSet.of(
            WorldType.HIGH_RISK,
            WorldType.DEADMAN,
            WorldType.PVP
    );

    private static final EnumSet<WorldType> ALL_PK_WORLD_TYPES = EnumSet.of(
            WorldType.HIGH_RISK,
            WorldType.DEADMAN,
            WorldType.PVP,
            WorldType.BOUNTY
    );

    public static boolean isDeadmanWorld(final Collection<WorldType> worldTypes)
    {
        return worldTypes.stream().anyMatch(DEADMAN_WORLD_TYPES::contains);
    }

    public static boolean isHighRiskWorld(final Collection<WorldType> worldTypes)
    {
        return worldTypes.stream().anyMatch(HIGHRISK_WORLD_TYPES::contains);
    }

    public static boolean isAllHighRiskWorld(final Collection<WorldType> worldTypes)
    {
        return worldTypes.stream().anyMatch(ALL_HIGHRISK_WORLD_TYPES::contains);
    }

    public static boolean isAllPvpWorld(final Collection<WorldType> worldTypes)
    {
        return worldTypes.stream().anyMatch(ALL_PVP_WORLD_TYPES::contains);
    }

    public static boolean isAllPKWorld(final Collection<WorldType> worldTypes)
    {
        return worldTypes.stream().anyMatch(ALL_PK_WORLD_TYPES::contains);
    }
}
