package com.theplug.kotori.kotoriutils.libs;

import com.theplug.kotori.kotoriutils.enums.PrayerExtended;
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

    public void invokePrayer(Prayer prayer)
    {
        //do nothing if prayer is already active or prayer points is at 0
        if (client.isPrayerActive(prayer) || client.getBoostedSkillLevel(Skill.PRAYER) <= 0)
        {
            return;
        }

        int param1 = PrayerExtended.getPrayerExtended(prayer).getPrayerWidgetInfo().getId();

        switch (prayer.name())
        {
            case "THICK_SKIN":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Thick Skin</col>", 0, 0);
                break;
            case "BURST_OF_STRENGTH":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Burst of Strength</col>", 0, 0);
                break;
            case "CLARITY_OF_THOUGHT":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Clarity of Thought</col>", 0, 0);
                break;
            case "ROCK_SKIN":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Rock Skin</col>", 0, 0);
                break;
            case "SUPERHUMAN_STRENGTH":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Superhuman Strength</col>", 0, 0);
                break;
            case "IMPROVED_REFLEXES":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Improved Reflexes</col>", 0, 0);
                break;
            case "RAPID_RESTORE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Rapid Restore</col>", 0, 0);
                break;
            case "RAPID_HEAL":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Rapid Heal</col>", 0, 0);
                break;
            case "PROTECT_ITEM":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Protect Item</col>", 0, 0);
                break;
            case "STEEL_SKIN":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Steel Skin</col>", 0, 0);
                break;
            case "ULTIMATE_STRENGTH":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Ultimate Strength</col>", 0, 0);
                break;
            case "INCREDIBLE_REFLEXES":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Incredible Reflexes</col>", 0, 0);
                break;
            case "PROTECT_FROM_MAGIC":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Protect from Magic</col>", 0, 0);
                break;
            case "PROTECT_FROM_MISSILES":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Protect from Missiles</col>", 0, 0);
                break;
            case "PROTECT_FROM_MELEE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Protect from Melee</col>", 0, 0);
                break;
            case "RETRIBUTION":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Retribution</col>", 0, 0);
                break;
            case "REDEMPTION":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Redemption</col>", 0, 0);
                break;
            case "SMITE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Smite</col>", 0, 0);
                break;
            case "SHARP_EYE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Sharp Eye</col>", 0, 0);
                break;
            case "MYSTIC_WILL":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Mystic Will</col>", 0, 0);
                break;
            case "HAWK_EYE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Hawk Eye</col>", 0, 0);
                break;
            case "MYSTIC_LORE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Mystic Lore</col>", 0, 0);
                break;
            case "EAGLE_EYE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Mystic Might</col>", 0, 0);
                break;
            case "MYSTIC_MIGHT":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Eagle Eye</col>", 0, 0);
                break;
            case "CHIVALRY":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Chivalry</col>", 0, 0);
                break;
            case "PIETY":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Piety</col>", 0, 0);
                break;
            case "RIGOUR":
                if (client.getVarbitValue(5451) == 1)
                {
                    invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Rigour</col>", 0, 0);
                }
                break;
            case "AUGURY":
                if (client.getVarbitValue(5452) == 1)
                {
                    invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Augury</col>", 0, 0);
                }
                break;
            case "PRESERVE":
                if (client.getVarbitValue(5453) == 1)
                {
                    invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Activate", "<col=ff9040>Preserve</col>", 0, 0);
                }
                break;
            default:
                break;
        }
    }

    public void deactivatePrayer(Prayer prayer)
    {
        //do nothing if prayer is not active or prayer points is at 0
        if (!client.isPrayerActive(prayer) || client.getBoostedSkillLevel(Skill.PRAYER) <= 0)
        {
            return;
        }

        int param1 = PrayerExtended.getPrayerExtended(prayer).getPrayerWidgetInfo().getId();

        switch (prayer.name())
        {
            case "THICK_SKIN":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Thick Skin</col>", 0, 0);
                break;
            case "BURST_OF_STRENGTH":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Burst of Strength</col>", 0, 0);
                break;
            case "CLARITY_OF_THOUGHT":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Clarity of Thought</col>", 0, 0);
                break;
            case "ROCK_SKIN":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Rock Skin</col>", 0, 0);
                break;
            case "SUPERHUMAN_STRENGTH":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Superhuman Strength</col>", 0, 0);
                break;
            case "IMPROVED_REFLEXES":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Improved Reflexes</col>", 0, 0);
                break;
            case "RAPID_RESTORE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Rapid Restore</col>", 0, 0);
                break;
            case "RAPID_HEAL":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Rapid Heal</col>", 0, 0);
                break;
            case "PROTECT_ITEM":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Protect Item</col>", 0, 0);
                break;
            case "STEEL_SKIN":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Steel Skin</col>", 0, 0);
                break;
            case "ULTIMATE_STRENGTH":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Ultimate Strength</col>", 0, 0);
                break;
            case "INCREDIBLE_REFLEXES":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Incredible Reflexes</col>", 0, 0);
                break;
            case "PROTECT_FROM_MAGIC":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Protect from Magic</col>", 0, 0);
                break;
            case "PROTECT_FROM_MISSILES":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Protect from Missiles</col>", 0, 0);
                break;
            case "PROTECT_FROM_MELEE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Protect from Melee</col>", 0, 0);
                break;
            case "RETRIBUTION":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Retribution</col>", 0, 0);
                break;
            case "REDEMPTION":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Redemption</col>", 0, 0);
                break;
            case "SMITE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Smite</col>", 0, 0);
                break;
            case "SHARP_EYE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Sharp Eye</col>", 0, 0);
                break;
            case "MYSTIC_WILL":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Mystic Will</col>", 0, 0);
                break;
            case "HAWK_EYE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Hawk Eye</col>", 0, 0);
                break;
            case "MYSTIC_LORE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Mystic Lore</col>", 0, 0);
                break;
            case "EAGLE_EYE":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Mystic Might</col>", 0, 0);
                break;
            case "MYSTIC_MIGHT":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Eagle Eye</col>", 0, 0);
                break;
            case "CHIVALRY":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Chivalry</col>", 0, 0);
                break;
            case "PIETY":
                invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Piety</col>", 0, 0);
                break;
            case "RIGOUR":
                if (client.getVarbitValue(5451) == 1)
                {
                    invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Rigour</col>", 0, 0);
                }
                break;
            case "AUGURY":
                if (client.getVarbitValue(5452) == 1)
                {
                    invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Augury</col>", 0, 0);
                }
                break;
            case "PRESERVE":
                if (client.getVarbitValue(5453) == 1)
                {
                    invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "Deactivate", "<col=ff9040>Preserve</col>", 0, 0);
                }
                break;
            default:
                break;
        }
    }

    public void invokePPotDrinking()
    {
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
