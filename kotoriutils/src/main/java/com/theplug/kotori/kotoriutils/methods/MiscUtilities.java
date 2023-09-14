package com.theplug.kotori.kotoriutils.methods;

import net.runelite.api.*;
import net.runelite.client.RuneLite;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class MiscUtilities
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
	
	public static void copyGearIdsToClipboard()
	{
		StringBuilder gearIds = new StringBuilder();
		
		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipment == null)
		{
			return;
		}
		
		Item[] items = equipment.getItems();
		for (Item item : items)
		{
			int id = item.getId();
			if (id < 0)
			{
				continue;
			}
			gearIds.append(id);
			gearIds.append(",");
		}
		
		if (gearIds.lastIndexOf(",") == gearIds.length() - 1)
		{
			gearIds.deleteCharAt(gearIds.length() - 1);
		}
		
		StringSelection stringSelection = new StringSelection(gearIds.toString());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	public static MenuEntry findAnyMenuEntryWithAction(MenuAction... menuActions)
	{
		MenuEntry[] activeEntries = client.getMenuEntries();

		//Go backwards here because the back of the array is the top menu entry
		for (int i = activeEntries.length - 1; i >= 0; i--)
		{
			for (MenuAction action : menuActions)
			{
				MenuEntry entry = activeEntries[i];
				if (entry.getType() == action)
				{
					return entry;
				}
			}
		}

		return null;
	}
}
