package com.theplug.kotori.gauntlethelper.module.boss;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.gameval.ItemID;

@Getter
@RequiredArgsConstructor
public enum CrystalWeapons
{
    CRYSTAL_DAGGER_PERFECTED(ItemID.ECHO_GAUNTLET_CRYSTAL_DAGGER_T3, 3, 11, false),

    CORRUPTED_STAFF_PERFECTED(ItemID.GAUNTLET_MAGIC_T3_HM, 2, 10, true),
    CORRUPTED_BOW_PERFECTED(ItemID.GAUNTLET_RANGED_T3_HM, 1, 9, true),
    CORRUPTED_HALBERD_PERFECTED(ItemID.GAUNTLET_MELEE_T3_HM, 0, 8, true),

    CORRUPTED_STAFF_ATTUNED(ItemID.GAUNTLET_MAGIC_T2_HM, 2, 7, false),
    CORRUPTED_BOW_ATTUNED(ItemID.GAUNTLET_RANGED_T2_HM, 1, 6, false),
    CORRUPTED_HALBERD_ATTUNED(ItemID.GAUNTLET_MELEE_T2_HM, 0, 5, false),

    CORRUPTED_STAFF_BASIC(ItemID.GAUNTLET_MAGIC_T1_HM, 2, 4, false),
    CORRUPTED_BOW_BASIC(ItemID.GAUNTLET_RANGED_T1_HM, 1, 3, false),
    CORRUPTED_HALBERD_BASIC(ItemID.GAUNTLET_MELEE_T1_HM, 0, 2, false),

    CORRUPTED_SCEPTRE(ItemID.GAUNTLET_SCEPTRE_HM, 0, 1, false),
    CORRUPTED_AXE(ItemID.GAUNTLET_AXE_HM, 0, 0, false),
    CORRUPTED_PICKAXE(ItemID.GAUNTLET_PICKAXE_HM, 0, 0, false),
    CORRUPTED_HARPOON(ItemID.GAUNTLET_HARPOON_HM, 0, 0, false),

    CRYSTAL_STAFF_PERFECTED(ItemID.GAUNTLET_MAGIC_T3, 2, 10, true),
    CRYSTAL_BOW_PERFECTED(ItemID.GAUNTLET_RANGED_T3, 1, 9, true),
    CRYSTAL_HALBERD_PERFECTED(ItemID.GAUNTLET_MELEE_T3, 0, 8, true),

    CRYSTAL_STAFF_ATTUNED(ItemID.GAUNTLET_MAGIC_T2, 2, 7, false),
    CRYSTAL_BOW_ATTUNED(ItemID.GAUNTLET_RANGED_T2, 1, 6, false),
    CRYSTAL_HALBERD_ATTUNED(ItemID.GAUNTLET_MELEE_T2, 0, 5, false),

    CRYSTAL_STAFF_BASIC(ItemID.GAUNTLET_MAGIC_T1, 2, 4, false),
    CRYSTAL_BOW_BASIC(ItemID.GAUNTLET_RANGED_T1, 1, 3, false),
    CRYSTAL_HALBERD_BASIC(ItemID.GAUNTLET_MELEE_T1, 0, 2, false),

    CRYSTAL_SCEPTRE(ItemID.GAUNTLET_SCEPTRE, 0, 1, false),
    CRYSTAL_AXE(ItemID.GAUNTLET_AXE, 0, 0, false),
    CRYSTAL_PICKAXE(ItemID.GAUNTLET_PICKAXE, 0, 0, false),
    CRYSTAL_HARPOON(ItemID.GAUNTLET_HARPOON, 0, 0, false);


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
