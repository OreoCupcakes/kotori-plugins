package com.theplug.kotori.gauntlethelper.module.boss;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;

@Getter
@RequiredArgsConstructor
public enum CrystalWeapons
{
    CRYSTAL_DAGGER_PERFECTED(ItemID.CRYSTAL_DAGGER_PERFECTED, 3, 11, false),

    CORRUPTED_STAFF_PERFECTED(ItemID.CORRUPTED_STAFF_PERFECTED, 2, 10, true),
    CORRUPTED_BOW_PERFECTED(ItemID.CORRUPTED_BOW_PERFECTED, 1, 9, true),
    CORRUPTED_HALBERD_PERFECTED(ItemID.CORRUPTED_HALBERD_PERFECTED, 0, 8, true),

    CORRUPTED_STAFF_ATTUNED(ItemID.CORRUPTED_STAFF_ATTUNED, 2, 7, false),
    CORRUPTED_BOW_ATTUNED(ItemID.CORRUPTED_BOW_ATTUNED, 1, 6, false),
    CORRUPTED_HALBERD_ATTUNED(ItemID.CORRUPTED_HALBERD_ATTUNED, 0, 5, false),

    CORRUPTED_STAFF_BASIC(ItemID.CORRUPTED_STAFF_BASIC, 2, 4, false),
    CORRUPTED_BOW_BASIC(ItemID.CORRUPTED_BOW_BASIC, 1, 3, false),
    CORRUPTED_HALBERD_BASIC(ItemID.CORRUPTED_HALBERD_BASIC, 0, 2, false),

    CORRUPTED_SCEPTRE(ItemID.CORRUPTED_SCEPTRE, 0, 1, false),
    CORRUPTED_AXE(ItemID.CORRUPTED_AXE, 0, 0, false),
    CORRUPTED_PICKAXE(ItemID.CORRUPTED_PICKAXE, 0, 0, false),
    CORRUPTED_HARPOON(ItemID.CORRUPTED_HARPOON, 0, 0, false),

    CRYSTAL_STAFF_PERFECTED(ItemID.CRYSTAL_STAFF_PERFECTED, 2, 10, true),
    CRYSTAL_BOW_PERFECTED(ItemID.CRYSTAL_BOW_PERFECTED, 1, 9, true),
    CRYSTAL_HALBERD_PERFECTED(ItemID.CRYSTAL_HALBERD_PERFECTED, 0, 8, true),

    CRYSTAL_STAFF_ATTUNED(ItemID.CRYSTAL_STAFF_ATTUNED, 2, 7, false),
    CRYSTAL_BOW_ATTUNED(ItemID.CRYSTAL_BOW_ATTUNED, 1, 6, false),
    CRYSTAL_HALBERD_ATTUNED(ItemID.CRYSTAL_HALBERD_ATTUNED, 0, 5, false),

    CRYSTAL_STAFF_BASIC(ItemID.CRYSTAL_STAFF_BASIC, 2, 4, false),
    CRYSTAL_BOW_BASIC(ItemID.CRYSTAL_BOW_BASIC, 1, 3, false),
    CRYSTAL_HALBERD_BASIC(ItemID.CRYSTAL_HALBERD_BASIC, 0, 2, false),

    CRYSTAL_SCEPTRE(ItemID.CRYSTAL_SCEPTRE, 0, 1, false),
    CRYSTAL_AXE(ItemID.CRYSTAL_AXE_23862, 0, 0, false),
    CRYSTAL_PICKAXE(ItemID.CRYSTAL_PICKAXE_23863, 0, 0, false),
    CRYSTAL_HARPOON(ItemID.CRYSTAL_HARPOON_23864, 0, 0, false);


    private final int itemId;
    private final int weaponType;
    private final int priority;
    private final boolean perfected;

    public static CrystalWeapons getWeaponById(int id)
    {
        for (CrystalWeapons weapon : CrystalWeapons.values())
        {
            if (id == weapon.itemId)
            {
                return weapon;
            }
        }

        return null;
    }

    public static int getWeaponTypeById(int id)
    {
        for (CrystalWeapons weapon : CrystalWeapons.values())
        {
            if (id == weapon.itemId)
            {
                return weapon.weaponType;
            }
        }

        return -1;
    }
}
