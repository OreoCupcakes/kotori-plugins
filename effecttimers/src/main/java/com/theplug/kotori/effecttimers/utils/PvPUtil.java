package com.theplug.kotori.effecttimers.utils;

import java.awt.Polygon;
import java.util.Comparator;
import java.util.Objects;
import java.util.TreeMap;

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.QuantityFormatter;
import org.apache.commons.lang3.ArrayUtils;

public class PvPUtil
{
    private static final Polygon NOT_WILDERNESS_BLACK_KNIGHTS = new Polygon( // this is black knights castle
            new int[]{2994, 2995, 2996, 2996, 2994, 2994, 2997, 2998, 2998, 2999, 3000, 3001, 3002, 3003, 3004, 3005, 3005,
                    3005, 3019, 3020, 3022, 3023, 3024, 3025, 3026, 3026, 3027, 3027, 3028, 3028, 3029, 3029, 3030, 3030, 3031,
                    3031, 3032, 3033, 3034, 3035, 3036, 3037, 3037},
            new int[]{3525, 3526, 3527, 3529, 3529, 3534, 3534, 3535, 3536, 3537, 3538, 3539, 3540, 3541, 3542, 3543, 3544,
                    3545, 3545, 3546, 3546, 3545, 3544, 3543, 3543, 3542, 3541, 3540, 3539, 3537, 3536, 3535, 3534, 3533, 3532,
                    3531, 3530, 3529, 3528, 3527, 3526, 3526, 3525},
            43
    );
    private static final Cuboid MAIN_WILDERNESS_CUBOID = new Cuboid(2944, 3522, 0, 3391, 4351, 3);
    private static final Cuboid GOD_WARS_WILDERNESS_CUBOID = new Cuboid(3008, 10112, 0, 3071, 10175, 3);
    private static final Cuboid VETION_WILDERNESS_CUBOID = new Cuboid(3264,10176,0,3327,10239,3);
    private static final Cuboid VENENATIS_WILDERNESS_CUBOID = new Cuboid(3392,10176,0,3455,10239,3);
    private static final Cuboid CALLISTO_WILDERNESS_CUBOID = new Cuboid(3328,10304,0,3391,10367,3);
    private static final Cuboid ARTIO_WILDERNESS_CUBOID = new Cuboid(1728,11520,0,1791,11583,3);
    private static final Cuboid CALVARION_WILDERNESS_CUBOID = new Cuboid(1856,11520,0,1919,11583,3);
    private static final Cuboid SPINDEL_WILDERNESS_CUBOID = new Cuboid(1600,11520,0,1663,11583,3);
    private static final Cuboid WILDERNESS_ESCAPE_CAVES_CUBOID = new Cuboid(3328,10240,0,3391,10303,3);
    private static final Cuboid WILDERNESS_UNDERGROUND_CUBOID = new Cuboid(2944, 9920, 0, 3455, 10879, 3);

    /**
     * Gets the wilderness level based on a world point
     * Java reimplementation of clientscript 384 [proc,wilderness_level]
     *
     * @param point the point in the world to get the wilderness level for
     * @return the int representing the wilderness level
     */
    public static int getWildernessLevelFrom(WorldPoint point)
    {
        int regionID = point.getRegionID();
        if (regionID == 11931 /* falador party room museum */ ||
                regionID == 12700 /* soul wars underground ferox */ ||
                regionID == 12187 /* falador party room museum */ ||
                regionID == 12955 /*  Bryophyta's lair */ ||
                regionID == 12611 /* Verzik's lair */ ||
                regionID == 12867 /* ToB reward room */ ||
                regionID == 13123 /* Sotetseg's lair */ ||
                regionID == 13122 /* Nylos' lair */ ||
                regionID == 11842 /* some cave */ )
        {
            return 0;
        }

        if (MAIN_WILDERNESS_CUBOID.contains(point))
        {
            if (NOT_WILDERNESS_BLACK_KNIGHTS.contains(point.getX(), point.getY()))
            {
                return 0;
            }

            return ((point.getY() - 3520) / 8) + 1; // calc(((coordz(coord) - (55 * 64)) / 8) + 1)
        }
        else if (GOD_WARS_WILDERNESS_CUBOID.contains(point))
        {
            return ((point.getY() - 9920) / 8) - 1; // calc(((coordz(coord) - (155 * 64)) / 8) - 1)
        }
        else if (VETION_WILDERNESS_CUBOID.contains(point))
        {
            return 35;
        }
        else if (VENENATIS_WILDERNESS_CUBOID.contains(point))
        {
            return 35;
        }
        else if (CALLISTO_WILDERNESS_CUBOID.contains(point))
        {
            return 40;
        }
        else if (ARTIO_WILDERNESS_CUBOID.contains(point))
        {
            return 21;
        }
        else if (CALVARION_WILDERNESS_CUBOID.contains(point))
        {
            return 21;
        }
        else if (SPINDEL_WILDERNESS_CUBOID.contains(point))
        {
            return 29;
        }
        else if (WILDERNESS_ESCAPE_CAVES_CUBOID.contains(point))
        {
            return 33 + ((point.getY() % 64 - 6) * 7 / 50); // calc(33 + scale(coordz(coord) % 64 - 6, 50, 7))
        }
        else if (WILDERNESS_UNDERGROUND_CUBOID.contains(point))
        {
            return ((point.getY() - 9920) / 8) + 1; // calc(((coordz(coord) - (155 * 64)) / 8) + 1)
        }
        return 0;
    }

    /**
     * Determines if another player is attackable based off of wilderness level and combat levels
     *
     * @param client The client of the local player
     * @param player the player to determine attackability
     * @return returns true if the player is attackable, false otherwise
     */
    public static boolean isAttackable(Client client, Player player)
    {
        int wildernessLevel = 0;

        if (WorldTypeExtended.isDeadmanWorld(client.getWorldType()))
        {
            return true;
        }
        if (WorldType.isPvpWorld(client.getWorldType()))
        {
            wildernessLevel += 15;
        }
        if (client.getVarbitValue(Varbits.IN_WILDERNESS) == 1)
        {
            wildernessLevel += getWildernessLevelFrom(client.getLocalPlayer().getWorldLocation());
        }
        return wildernessLevel != 0 && Math.abs(client.getLocalPlayer().getCombatLevel() - player.getCombatLevel()) <= wildernessLevel;
    }

    public static int calculateRisk(Client client, ItemManager itemManager)
    {
        ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
        if (equipment == null)
        {
            return 0;
        }
        if (inventory == null)
        {
            return 0;
        }
        Item[] items = ArrayUtils.addAll(Objects.requireNonNull(client.getItemContainer(InventoryID.EQUIPMENT)).getItems(),
                Objects.requireNonNull(client.getItemContainer(InventoryID.INVENTORY)).getItems());
        TreeMap<Integer, Item> priceMap = new TreeMap<>(Comparator.comparingInt(Integer::intValue));
        int wealth = 0;
        for (Item i : items)
        {
            int value = (itemManager.getItemPrice(i.getId()) * i.getQuantity());

            final ItemComposition itemComposition = itemManager.getItemComposition(i.getId());
            if (!itemComposition.isTradeable() && value == 0)
            {
                value = itemComposition.getPrice() * i.getQuantity();
                priceMap.put(value, i);
            }
            else
            {
                value = itemManager.getItemPrice(i.getId()) * i.getQuantity();
                if (i.getId() > 0 && value > 0)
                {
                    priceMap.put(value, i);
                }
            }
            wealth += value;
        }
        return Integer.parseInt(QuantityFormatter.quantityToRSDecimalStack(priceMap.keySet().stream().mapToInt(Integer::intValue).sum()));

    }
}