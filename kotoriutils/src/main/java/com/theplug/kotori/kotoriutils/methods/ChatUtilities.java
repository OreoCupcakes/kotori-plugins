package com.theplug.kotori.kotoriutils.methods;

import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.RuneLite;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import java.awt.*;

public class ChatUtilities
{
	static final Client client = RuneLite.getInjector().getInstance(Client.class);
	static final ChatMessageManager chatMessageManager = RuneLite.getInjector().getInstance(ChatMessageManager.class);
	
	public static void sendGameMessage(String chatMessage, Color color)
	{
		final String message = new ChatMessageBuilder()
				.append(color, chatMessage)
				.build();
		
		chatMessageManager.queue(
				QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(message)
						.build());
	}
	
	public static void sendGameMessage(String gameMessage)
	{
		sendGameMessage(gameMessage, Color.RED);
	}
}
