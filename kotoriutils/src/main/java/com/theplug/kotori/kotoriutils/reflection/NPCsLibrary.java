package com.theplug.kotori.kotoriutils.reflection;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.HeadIcon;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.client.RuneLite;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

@Slf4j
public class NPCsLibrary
{
    private static final Client client = RuneLite.getInjector().getInstance(Client.class);

    @Setter
    private static String actorClassName;
    @Setter
    private static String actorAnimationIdFieldName;
    @Setter
    private static int actorAnimationIdMultiplierValue;
    
    @Setter
    private static String npcCompositionClassName;
    @Setter
    private static String overheadIconFieldName;

    public static int getNPCAnimationID(NPC npc)
    {
        int animationId = -1;
        try
        {
            Field sequence = client.getClass().getClassLoader().loadClass(actorClassName).getDeclaredField(actorAnimationIdFieldName);
            sequence.setAccessible(true);
            int obfuscatedSequenceValue = sequence.getInt(npc);
            sequence.setAccessible(false);
            animationId = obfuscatedSequenceValue * actorAnimationIdMultiplierValue;
        }
        catch (Exception e)
        {
            log.error("Failed to get NPC animation id.", e);
        }
        return animationId;
    }

    public static HeadIcon getNPCHeadIcon(NPCComposition npcComposition)
    {
        HeadIcon headIcon = null;
        try
        {
            Field headIconSpriteIndexes = client.getClass().getClassLoader().loadClass(npcCompositionClassName).getDeclaredField(overheadIconFieldName);
            headIconSpriteIndexes.setAccessible(true);
            Object headIconShortArray = headIconSpriteIndexes.get(npcComposition);
            short headIconShortValue = Array.getShort(headIconShortArray,0);
            headIconSpriteIndexes.setAccessible(false);
            switch (headIconShortValue)
            {
                case 0:
                    headIcon = HeadIcon.MELEE;
                    break;
                case 1:
                    headIcon = HeadIcon.RANGED;
                    break;
                case 2:
                    headIcon = HeadIcon.MAGIC;
                    break;
                case 3:
                    headIcon = HeadIcon.RETRIBUTION;
                    break;
                case 4:
                    headIcon = HeadIcon.SMITE;
                    break;
                case 5:
                    headIcon = HeadIcon.REDEMPTION;
                    break;
                case 6:
                    headIcon = HeadIcon.RANGE_MAGE;
                    break;
                case 7:
                    headIcon = HeadIcon.RANGE_MELEE;
                    break;
                case 8:
                    headIcon = HeadIcon.MAGE_MELEE;
                    break;
                case 9:
                    headIcon = HeadIcon.RANGE_MAGE_MELEE;
                    break;
                case 10:
                    headIcon = HeadIcon.WRATH;
                    break;
                case 11:
                    headIcon = HeadIcon.SOUL_SPLIT;
                    break;
                case 12:
                    headIcon = HeadIcon.DEFLECT_MELEE;
                    break;
                case 13:
                    headIcon = HeadIcon.DEFLECT_RANGE;
                    break;
                case 14:
                    headIcon = HeadIcon.DEFLECT_MAGE;
                    break;
            }
        }
        catch (Exception e)
        {
            log.error("Failed to get NPC overhead icon.", e);
        }
        return headIcon;
    }
}
