/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2023, rdutta <https://github.com/rdutta>
 * Copyright (c) 2018, Seth <http://github.com/sethtroll>
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

package com.theplug.kotori.gauntlethelper.module.overlay;

import com.theplug.kotori.gauntlethelper.GauntletHelperConfig;
import com.theplug.kotori.gauntlethelper.GauntletHelperPlugin;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.time.Instant;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ChatMessageType;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

@Singleton
public final class TimerOverlay extends OverlayPanel
{
	private final GauntletHelperConfig config;
	private final ChatMessageManager chatMessageManager;

	private final PanelComponent timerComponent;
	private final LineComponent prepTimeComponent;
	private final LineComponent totalTimeComponent;

	private long timeGauntletStart;
	private long timeHunllefStart;

	private long lastElapsed;

	@Inject
	TimerOverlay(final GauntletHelperPlugin plugin, final GauntletHelperConfig config, final ChatMessageManager chatMessageManager)
	{
		super(plugin);

		this.config = config;
		this.chatMessageManager = chatMessageManager;

		timerComponent = new PanelComponent();
		timerComponent.setBorder(new Rectangle(2, 1, 4, 0));
		timerComponent.setBackgroundColor(null);

		panelComponent.setBorder(new Rectangle(2, 2, 2, 2));
		panelComponent.getChildren().add(TitleComponent.builder().text("Gauntlet Timer").build());
		panelComponent.getChildren().add(timerComponent);

		prepTimeComponent = LineComponent.builder().left("Preparation:").right("").build();
		totalTimeComponent = LineComponent.builder().left("Total:").right("").build();

		timeGauntletStart = -1L;
		timeHunllefStart = -1L;
		lastElapsed = 0L;

		setClearChildren(false);
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "The Gauntlet timer"));
		setPosition(OverlayPosition.DYNAMIC);
		setMovable(true);
		setSnappable(true);
		setPriority(OverlayPriority.HIGH);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(final Graphics2D graphics2D)
	{
		if (!config.timerOverlay() || timeGauntletStart == -1)
		{
			return null;
		}

		graphics2D.setFont(FontManager.getRunescapeSmallFont());

		final LineComponent lineComponent = timeHunllefStart == -1 ? prepTimeComponent : totalTimeComponent;

		final long elapsed = Instant.now().getEpochSecond() - timeGauntletStart;

		if (elapsed != lastElapsed)
		{
			lineComponent.setRight(formatElapsedTime(elapsed));
			lastElapsed = elapsed;
		}

		return super.render(graphics2D);
	}

	public void reset()
	{
		timeGauntletStart = -1L;
		timeHunllefStart = -1L;
		lastElapsed = 0L;
		prepTimeComponent.setRight("");
		totalTimeComponent.setRight("");
		timerComponent.getChildren().clear();
	}

	public void setGauntletStart()
	{
		timeGauntletStart = Instant.now().getEpochSecond();
		prepTimeComponent.setLeftColor(Color.WHITE);
		prepTimeComponent.setRightColor(Color.WHITE);
		timerComponent.getChildren().clear();
		timerComponent.getChildren().add(prepTimeComponent);
	}

	public void setHunllefStart()
	{
		timeHunllefStart = Instant.now().getEpochSecond();
		prepTimeComponent.setLeftColor(Color.LIGHT_GRAY);
		prepTimeComponent.setRightColor(Color.LIGHT_GRAY);
		timerComponent.getChildren().clear();
		timerComponent.getChildren().add(prepTimeComponent);
		timerComponent.getChildren().add(totalTimeComponent);
	}

	public void onPlayerDeath()
	{
		if (!config.timerChatMessage())
		{
			return;
		}

		printTime();
		reset();
	}

	private void printTime()
	{
		if (timeGauntletStart == -1 || timeHunllefStart == -1)
		{
			return;
		}

		final long current = Instant.now().getEpochSecond();

		final String elapsedPrepTime = formatElapsedTime(timeHunllefStart, timeGauntletStart);
		final String elapsedBossTime = formatElapsedTime(current, timeHunllefStart);
		final String elapsedTotalTime = formatElapsedTime(current, timeGauntletStart);

		final ChatMessageBuilder chatMessageBuilder = new ChatMessageBuilder()
			.append(ChatColorType.NORMAL)
			.append("Preparation time: ")
			.append(ChatColorType.HIGHLIGHT)
			.append(elapsedPrepTime)
			.append(ChatColorType.NORMAL)
			.append(". Hunllef time: ")
			.append(ChatColorType.HIGHLIGHT)
			.append(elapsedBossTime)
			.append(ChatColorType.NORMAL)
			.append(". Total time: ")
			.append(ChatColorType.HIGHLIGHT)
			.append(elapsedTotalTime)
			.append(ChatColorType.NORMAL)
			.append(".");

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.CONSOLE)
			.runeLiteFormattedMessage(chatMessageBuilder.build())
			.build());
	}

	private static String formatElapsedTime(final long end, final long start)
	{
		return formatElapsedTime(end - start);
	}

	private static String formatElapsedTime(final long elapsed)
	{
		final long minutes = elapsed % 3600 / 60;
		final long seconds = elapsed % 60;

		return String.format("%01d:%02d", minutes, seconds);
	}
}

