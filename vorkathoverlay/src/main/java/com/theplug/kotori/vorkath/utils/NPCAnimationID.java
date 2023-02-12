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
package com.theplug.kotori.vorkath.utils;

import java.lang.reflect.*;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Actor;

public class NPCAnimationID {

    private Client client;
    private NPC npc;
    private int animationID;

    final private String className = "cb";
    final private String fieldName = "bc";
    final private int obfuscatedGetter = -1519553247;

    public NPCAnimationID(Client client, NPC npc) {
        this.client = client;
        this.npc = npc;
    }

    public int getNPCAnimationID() {
        try {
            Field sequence = client.getClass().getClassLoader().loadClass(className).getDeclaredField(fieldName);
            sequence.setAccessible(true);
            int obfuscatedSequenceValue = sequence.getInt(npc);
            sequence.setAccessible(false);
            animationID = obfuscatedSequenceValue * obfuscatedGetter;
        } catch (Exception e) {
            System.err.println(e);
        }

        return animationID;
    }

}
