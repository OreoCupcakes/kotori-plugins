package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import net.runelite.api.*;
import net.runelite.client.RuneLite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NPCInteractions
{
    private static final Client client = RuneLite.getInjector().getInstance(Client.class);

    public static void attackNpc(NPC npc)
    {
        if (npc != null)
        {
            ReflectionLibrary.invokeMenuAction(0, 0, MenuAction.NPC_SECOND_OPTION.getId(), npc.getIndex(), -1);
        }
    }

    public static int calculateNpcHp(final int ratio, final int health, final int maxHp)
    {
        if (ratio < 0 || health <= 0 || maxHp == -1)
        {
            return -1;
        }

        int exactHealth = 0;

        // This is the reverse of the calculation of healthRatio done by the server
        // which is: healthRatio = 1 + (healthScale - 1) * health / maxHealth (if health > 0, 0 otherwise)
        // It's able to recover the exact health if maxHealth <= healthScale.
        if (ratio > 0)
        {
            int minHealth = 1;
            int maxHealth;
            if (health > 1)
            {
                if (ratio > 1)
                {
                    // This doesn't apply if healthRatio = 1, because of the special case in the server calculation that
                    // health = 0 forces healthRatio = 0 instead of the expected healthRatio = 1
                    minHealth = (maxHp * (ratio - 1) + health - 2) / (health - 1);
                }
                maxHealth = (maxHp * ratio - 1) / (health - 1);
                if (maxHealth > maxHp)
                {
                    maxHealth = maxHp;
                }
            }
            else
            {
                // If healthScale is 1, healthRatio will always be 1 unless health = 0
                // , so we know nothing about the upper limit except that it can't be higher than maxHealth
                maxHealth = maxHp;
            }
            // Take the average of min and max possible healths
            exactHealth = (minHealth + maxHealth + 1) / 2;
        }

        return exactHealth;
    }

    public static List<NPC> getNpcs()
    {
        WorldView wv = client.getTopLevelWorldView();
        return wv == null ? Collections.emptyList() : wv.npcs().stream().collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Player> getPlayers()
    {
        WorldView wv = client.getTopLevelWorldView();
        return wv == null ? Collections.emptyList() : wv.players().stream().collect(Collectors.toCollection(ArrayList::new));
    }

    public static NPC[] getCachedNPCs()
    {
        WorldView wv = client.getTopLevelWorldView();
        return wv == null ? null : wv.npcs().stream().toArray(NPC[]::new);
    }

    public static Player[] getCachedPlayers()
    {
        WorldView wv = client.getTopLevelWorldView();
        return wv == null ? null : wv.players().stream().toArray(Player[]::new);
    }
}
