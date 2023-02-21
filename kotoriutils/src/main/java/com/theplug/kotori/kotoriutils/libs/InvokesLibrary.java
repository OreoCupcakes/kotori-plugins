package com.theplug.kotori.kotoriutils.libs;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class InvokesLibrary {

    @Inject
    private Client client;

    @Setter
    private String worldMapData_0_ClassName;
    @Setter
    private String invokeMenuActionMethodName;
    @Setter
    private int invokeMenuActionGarbageValue;

    public void invoke(int param0, int param1, int opcode, int identifier, int itemid, String option, String target, int x, int y)
    {
        try
        {
            /*
                Getting Methods with getDeclaredMethod requires you to pass in an Array of its parameter types. So just get all the methods and filter out
                the one you need
             */
            Class classWithMenuAction = client.getClass().getClassLoader().loadClass(worldMapData_0_ClassName);
            Method menuAction = Arrays.stream(classWithMenuAction.getDeclaredMethods()).
                    filter(method -> method.getName().equals(invokeMenuActionMethodName)).findAny().orElse(null);

            // When invoking, static methods need null as the first parameter
            menuAction.setAccessible(true);
            if (invokeMenuActionGarbageValue < 128 && invokeMenuActionGarbageValue >= -128)
            {
                menuAction.invoke(null,param0,param1,opcode,identifier,itemid,option,target,x,y,(byte)invokeMenuActionGarbageValue);
            }
            else
            {
                menuAction.invoke(null, param0, param1, opcode, identifier, itemid, option, target, x, y, invokeMenuActionGarbageValue);
            }
            menuAction.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to invoke menu action.", e);
        }
    }

    public void invokePrayer(Prayer prayer) {

        //do nothing if prayer is already active or prayer points is at 0
        if (client.isPrayerActive(prayer))
        {
            return;
        }
        if (client.getBoostedSkillLevel(Skill.PRAYER) <= 0)
        {
            return;
        }

        switch (prayer.name()) {
            case "PROTECT_FROM_MAGIC":
                //Invokes Protect from Magic
                invoke(-1, 35454993, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Protect from Magic</col>", 0, 0);
                break;
            case "PROTECT_FROM_MISSILES":
                //Invokes Protect from Missiles
                invoke(-1, 35454994, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Protect from Missiles</col>", 0, 0);
                break;
            case "PROTECT_FROM_MELEE":
                //Invokes Protect from Melee
                invoke(-1, 35454995, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Protect from Melee</col>", 0, 0);
                break;
            case "PIETY":
                invoke(-1, 35455006, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Piety</col>", 0, 0);
                break;
            case "RIGOUR":
                if (client.getVarbitValue(5451) == 1)
                {
                    invoke(-1, 35455007, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Rigour</col>", 0, 0);
                }
                break;
            case "AUGURY":
                if (client.getVarbitValue(5452) == 1)
                {
                    invoke(-1, 35455008, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Augury</col>", 0, 0);
                }
                break;
            case "EAGLE_EYE":
                invoke(-1, 35455003, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Mystic Might</col>", 0, 0);
                break;
            case "MYSTIC_MIGHT":
                invoke(-1, 35455004, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Eagle Eye</col>", 0, 0);
                break;
            default:
                break;
        }
    }

    public void invokePPotDrinking() {
        // Prayer restoring potion item IDs array, Prayer potions, Super restores, Sanfew Serums
        final int[] prayerRestoringItemIDs = {2434,139,141,143,3024,3026,3028,3030,10925,10927,10929,10931};
        final int prayerDrinkingParam1 = 9764864;
        final String[] prayerRestoringItemTargets =
                {
                        "<col=ff9040>Prayer potion(4)</col>","<col=ff9040>Prayer potion(3)</col>","<col=ff9040>Prayer potion(2)</col>","<col=ff9040>Prayer potion(1)</col>",
                        "<col=ff9040>Super restore(4)</col>","<col=ff9040>Super restore(3)</col>","<col=ff9040>Super restore(2)</col>","<col=ff9040>Super restore(1)</col>",
                        "<col=ff9040>Sanfew serum(4)</col>","<col=ff9040>Sanfew serum(3)</col>","<col=ff9040>Sanfew serum(2)</col>","<col=ff9040>Sanfew serum(1)</col>"
                };

        Item[] inventoryItems = client.getItemContainer(InventoryID.INVENTORY).getItems();

        for (int i = 0; i < inventoryItems.length; i++)
        {
            for (int p = 0; p < prayerRestoringItemIDs.length; p++)
            {
                if (inventoryItems[i].getId() == prayerRestoringItemIDs[p])
                {
                    // i represents param0 which is the inventory slot
                    invoke(i,prayerDrinkingParam1,MenuAction.CC_OP.getId(),2,prayerRestoringItemIDs[p],"Drink",prayerRestoringItemTargets[p],0,0);
                    return;
                }
            }
        }
    }

    public void invokeWieldWeapon(int inventoryIndex, int itemID, String weaponName)
    {
        invoke(inventoryIndex,9764864,MenuAction.CC_OP.getId(),3,itemID,"Wield","<col=ff9040>"+weaponName+"</col>",0,0);
    }
}
