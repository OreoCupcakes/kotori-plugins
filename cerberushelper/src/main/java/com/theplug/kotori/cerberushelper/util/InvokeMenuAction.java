/*
 * Copyright (c) 2023, Kotori <https://github.com/OreoCupcakes/>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.theplug.kotori.cerberushelper.util;

import net.runelite.api.*;

import java.lang.reflect.*;
import java.util.Arrays;

public class InvokeMenuAction {

    private Client client;

    private String classNameForInvokeMenuAction = "fw";
    private String methodNameForInvokeMenuAction = "im";
    private int garbageValueForInvokeMenuAction = 1;

    public InvokeMenuAction(Client client) {
        this.client = client;
    }

    public void invoke(int param0, int param1, int opcode, int identifier, int itemid, String option, String target, int x, int y)
    {
        try
        {
            /*
                Getting Methods with getDeclaredMethod requires you to pass in an Array of its parameter types. So just get all the methods and filter out
                the one you need
             */
            Class classWithMenuAction = client.getClass().getClassLoader().loadClass(classNameForInvokeMenuAction);
            Method menuAction = Arrays.stream(classWithMenuAction.getDeclaredMethods()).
                    filter(method -> method.getName().equals(methodNameForInvokeMenuAction)).findAny().orElse(null);

            // When invoking, static methods need null as the first parameter
            menuAction.setAccessible(true);

            if (garbageValueForInvokeMenuAction < 128 && garbageValueForInvokeMenuAction >= -128) {
                menuAction.invoke(null,param0,param1,opcode,identifier,itemid,option,target,x,y,(byte)garbageValueForInvokeMenuAction);
            } else {
                menuAction.invoke(null, param0, param1, opcode, identifier, itemid, option, target, x, y, garbageValueForInvokeMenuAction);
            }

            menuAction.setAccessible(false);
        }
        catch (Exception e)
        {
            System.err.println(e);
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
                invoke(-1, 35454993, 57, 1, -1, "Activate", "<col=ff9040>Protect from Magic</col>", 0, 0);
                break;
            case "PROTECT_FROM_MISSILES":
                //Invokes Protect from Missiles
                invoke(-1, 35454994, 57, 1, -1, "Activate", "<col=ff9040>Protect from Missiles</col>", 0, 0);
                break;
            case "PROTECT_FROM_MELEE":
                //Invokes Protect from Melee
                invoke(-1, 35454995, 57, 1, -1, "Activate", "<col=ff9040>Protect from Melee</col>", 0, 0);
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
}
