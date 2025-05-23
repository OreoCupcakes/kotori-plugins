/*
 * Copyright (c) 2020 ThatGamerBlue
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
 *
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
package com.theplug.kotori.effecttimers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.client.util.ImageUtil;

@Getter
@RequiredArgsConstructor
public enum TimerType
{
	FREEZE(5, loadImage("freeze"), loadImage("freezeimmune"), Color.CYAN), // 3 seconds
	TELEBLOCK(100, loadImage("teleblock"), loadImage("teleblockimmune"), new Color(0x5254ae)), // this is 60 seconds, might be wrong
	VENG(0, loadImage("veng"), null, Color.RED.brighter()),
	SOTD(0, loadImage("sotd"), null, Color.YELLOW),
	IMBUEDHEART(0, loadImage("imbheart"), null, Color.BLACK),
	DFS(0, loadImage("dfs"), null, Color.WHITE),
	ANCWYVERN(0, loadImage("ancwyvern"), null, Color.WHITE),
	POISON(0, loadImage("poison"), null, Color.GREEN),
	VENOM(0, loadImage("venom"), null, Color.GREEN.darker().darker());

	private final int immunityLength;
	private final BufferedImage icon;
	private final BufferedImage cooldownIcon;
	private final Color defaultColor;

	private static BufferedImage loadImage(String name)
	{
		return ImageUtil.loadImageResource(EffectTimersPlugin.class, name + ".png");
	}
}
