package com.theplug.kotori.effecttimers.utils;

import net.runelite.api.WorldType;

import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.EnumSet;

/**
 * An enumeration of possible world types.
 */
@AllArgsConstructor
public enum WorldTypeExtended
{
    /**
     * Members world type.
     */
    MEMBERS(1),
    /**
     * Pvp world type.
     */
    PVP(1 << 2),
    /**
     * Bounty world type.
     */
    BOUNTY(1 << 5),
    /**
     * PVP arena world type.
     */
    PVP_ARENA(1 << 6),
    /**
     * Skill total world type.
     */
    SKILL_TOTAL(1 << 7),
    /**
     * Quest speedrunning
     */
    QUEST_SPEEDRUNNING(1 << 8),
    /**
     * High risk world type.
     */
    HIGH_RISK(1 << 10),
    /**
     * Last man standing world type.
     */
    LAST_MAN_STANDING(1 << 14),
    /**
     * Beta worlds without profiles that are saved.
     */
    NOSAVE_MODE(1 << 25),
    /**
     * Tournament world type
     */
    TOURNAMENT_WORLD(1 << 26),
    /**
     * Fresh start world type
     */
    FRESH_START_WORLD(1 << 27),
    /**
     * Deadman world type.
     */
    DEADMAN(1 << 29),
    /**
     * Seasonal world type for leagues and seasonal deadman.
     */
    SEASONAL(1 << 30);
    
    private final int mask;
    
    private static final EnumSet<WorldTypeExtended> PVP_WORLD_TYPES = EnumSet.of(
            DEADMAN, // dmmt worlds are also flaged as DEADMAN
            PVP
    );
    
    private static final EnumSet<WorldTypeExtended> DEADMAN_WORLD_TYPES = EnumSet.of(
            DEADMAN
    );
    
    private static final EnumSet<WorldTypeExtended> HIGHRISK_WORLD_TYPES = EnumSet.of(
            HIGH_RISK
    );
    
    private static final EnumSet<WorldTypeExtended> ALL_HIGHRISK_WORLD_TYPES = EnumSet.of(
            HIGH_RISK,
            DEADMAN
    );
    
    private static final EnumSet<WorldTypeExtended> ALL_PVP_WORLD_TYPES = EnumSet.of(
            HIGH_RISK,
            DEADMAN,
            PVP
    );
    
    private static final EnumSet<WorldTypeExtended> ALL_PK_WORLD_TYPES = EnumSet.of(
            HIGH_RISK,
            DEADMAN,
            PVP,
            BOUNTY
    );
    
    /**
     * Create enum set of world types from mask.
     *
     * @param mask the mask
     * @return the enum set
     */
    public static EnumSet<WorldTypeExtended> fromMask(final int mask)
    {
        final EnumSet<WorldTypeExtended> types = EnumSet.noneOf(WorldTypeExtended.class);
        
        for (WorldTypeExtended type : WorldTypeExtended.values())
        {
            if ((mask & type.mask) != 0)
            {
                types.add(type);
            }
        }
        
        return types;
    }
    
    /**
     * Create mask from enum set of world types.
     *
     * @param types the types
     * @return the int containing all mask
     */
    public static int toMask(final EnumSet<WorldTypeExtended> types)
    {
        int mask = 0;
        
        for (WorldTypeExtended type : types)
        {
            mask |= type.mask;
        }
        
        return mask;
    }
    
    /**
     * Checks whether a world having a {@link Collection} of {@link WorldType}s is a PVP world.
     *
     * @param worldTypes A {@link Collection} of {@link WorldType}s describing the given world.
     * @return           True if the given worldtypes of the world are a PVP world, false otherwise.
     */
    public static boolean isPvpWorld(final Collection<WorldType> worldTypes)
    {
        return worldTypes.stream().anyMatch(PVP_WORLD_TYPES::contains);
    }
    
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