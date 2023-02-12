/*
 * Copyright (c) 2022, Kotori <https://github.com/OreoCupcakes/>
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

package com.theplug.kotori.demonicgorillas.utils;

import java.lang.reflect.*;

import net.runelite.api.Client;
import net.runelite.api.HeadIcon;
import net.runelite.api.NPCComposition;

public class NPCCompositionHeadIcon {

    private Client client;
    private NPCComposition npcComposition;
    private HeadIcon headIcon;

    final private String className = "go";
    final private String fieldName = "ah";
    public NPCCompositionHeadIcon(Client client, NPCComposition npcComposition) {
        this.client = client;
        this.npcComposition = npcComposition;
    }

    public HeadIcon getNPCHeadIcon() {
        try {
            Field headIconSpriteIndexes = client.getClass().getClassLoader().loadClass(className).getDeclaredField(fieldName);
            headIconSpriteIndexes.setAccessible(true);
            short[] headIconSpritesValues = short[].class.cast(headIconSpriteIndexes.get(npcComposition));
            headIconSpriteIndexes.setAccessible(false);
            switch (headIconSpritesValues[0]) {
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
        } catch (Exception e) {
            System.err.println(e);
        }

        return headIcon;
    }
}
