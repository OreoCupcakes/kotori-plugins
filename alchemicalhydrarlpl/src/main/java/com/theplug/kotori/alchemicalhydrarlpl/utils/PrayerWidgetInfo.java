package com.theplug.kotori.alchemicalhydrarlpl.utils;

public enum PrayerWidgetInfo {
    PRAYER_THICK_SKIN(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.THICK_SKIN),
    PRAYER_BURST_OF_STRENGTH(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.BURST_OF_STRENGTH),
    PRAYER_CLARITY_OF_THOUGHT(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.CLARITY_OF_THOUGHT),
    PRAYER_SHARP_EYE(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.SHARP_EYE),
    PRAYER_MYSTIC_WILL(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.MYSTIC_WILL),
    PRAYER_ROCK_SKIN(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.ROCK_SKIN),
    PRAYER_SUPERHUMAN_STRENGTH(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.SUPERHUMAN_STRENGTH),
    PRAYER_IMPROVED_REFLEXES(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.IMPROVED_REFLEXES),
    PRAYER_RAPID_RESTORE(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.RAPID_RESTORE),
    PRAYER_RAPID_HEAL(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.RAPID_HEAL),
    PRAYER_PROTECT_ITEM(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.PROTECT_ITEM),
    PRAYER_HAWK_EYE(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.HAWK_EYE),
    PRAYER_MYSTIC_LORE(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.MYSTIC_LORE),
    PRAYER_STEEL_SKIN(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.STEEL_SKIN),
    PRAYER_ULTIMATE_STRENGTH(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.ULTIMATE_STRENGTH),
    PRAYER_INCREDIBLE_REFLEXES(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.INCREDIBLE_REFLEXES),
    PRAYER_PROTECT_FROM_MAGIC(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.PROTECT_FROM_MAGIC),
    PRAYER_PROTECT_FROM_MISSILES(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.PROTECT_FROM_MISSILES),
    PRAYER_PROTECT_FROM_MELEE(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.PROTECT_FROM_MELEE),
    PRAYER_EAGLE_EYE(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.EAGLE_EYE),
    PRAYER_MYSTIC_MIGHT(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.MYSTIC_MIGHT),
    PRAYER_RETRIBUTION(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.RETRIBUTION),
    PRAYER_REDEMPTION(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.REDEMPTION),
    PRAYER_SMITE(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.SMITE),
    PRAYER_PRESERVE(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.PRESERVE),
    PRAYER_CHIVALRY(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.CHIVALRY),
    PRAYER_PIETY(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.PIETY),
    PRAYER_RIGOUR(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.RIGOUR),
    PRAYER_AUGURY(PrayerWidgetID.PRAYER_GROUP_ID, PrayerWidgetID.Prayer.AUGURY),
    ;

    private final int groupId;
    private final int childId;

    PrayerWidgetInfo(int groupId, int childId)
    {
        this.groupId = groupId;
        this.childId = childId;
    }

    /**
     * Gets the ID of the group-child pairing.
     *
     * @return the ID
     */
    public int getId()
    {
        return groupId << 16 | childId;
    }

    /**
     * Gets the group ID of the pair.
     *
     * @return the group ID
     */
    public int getGroupId()
    {
        return groupId;
    }

    /**
     * Gets the ID of the child in the group.
     *
     * @return the child ID
     */
    public int getChildId()
    {
        return childId;
    }

    /**
     * Gets the packed widget ID.
     *
     * @return the packed ID
     */
    public int getPackedId()
    {
        return groupId << 16 | childId;
    }

    /**
     * Utility method that converts an ID returned by {@link #getId()} back
     * to its group ID.
     *
     * @param id passed group-child ID
     * @return the group ID
     */
    public static int TO_GROUP(int id)
    {
        return id >>> 16;
    }

    /**
     * Utility method that converts an ID returned by {@link #getId()} back
     * to its child ID.
     *
     * @param id passed group-child ID
     * @return the child ID
     */
    public static int TO_CHILD(int id)
    {
        return id & 0xFFFF;
    }

    /**
     * Packs the group and child IDs into a single integer.
     *
     * @param groupId the group ID
     * @param childId the child ID
     * @return the packed ID
     */
    public static int PACK(int groupId, int childId)
    {
        return groupId << 16 | childId;
    }
}
