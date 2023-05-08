package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class InventoryInteraction
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	
	private static ItemContainer inventory;
	private static final ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	
	public static int[] parseStringToItemIds(String listOfItemIds)
	{
		if (listOfItemIds == null)
		{
			return null;
		}
		String replacedString = listOfItemIds.replaceAll("[^0-9,]","");
		if (replacedString.isEmpty())
		{
			return null;
		}
		String[] stringIds = replacedString.split(",");
		int[] itemIds = new int[stringIds.length];
		for (int i = 0; i < stringIds.length; i++)
		{
			if (stringIds[i].isEmpty())
			{
				itemIds[i] = -1;
			}
			itemIds[i] = Integer.parseInt(stringIds[i]);
		}
		return itemIds;
	}
	
	public static int getItemSlotNumber(int itemid)
	{
		inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (inventory == null)
		{
			return -1;
		}
		Item[] items = inventory.getItems();
		
		for (int slot = 0; slot < items.length; slot++)
		{
			if (items[slot].getId() == itemid)
			{
				return slot;
			}
		}
		return -1;
	}
	
	public static void equipItems(KotoriUtils kotoriUtils, int[] itemIds)
	{
		executorService.submit(() ->
		{
			if (itemIds == null)
			{
				return;
			}
			
			int numItemEquippedAtOnce = 0;
			inventory = client.getItemContainer(InventoryID.INVENTORY);
			if (inventory == null)
			{
				return;
			}
			Item[] items = inventory.getItems();
			for (int slot = 0; slot < items.length; slot++)
			{
				for (int itemId : itemIds)
				{
					if (items[slot].getId() == itemId)
					{
						kotoriUtils.getInvokesLibrary().invoke(slot, WidgetInfo.INVENTORY.getId(), MenuAction.CC_OP.getId(), 3, itemId, "","", 0, 0);
						numItemEquippedAtOnce++;
						if (numItemEquippedAtOnce > 2)
						{
							try
							{
								long start = System.currentTimeMillis();
								long timeToSleep = ThreadLocalRandom.current().nextLong(600, 1000);
								Thread.sleep(timeToSleep);
								long now;
								while (start + timeToSleep > (now = System.currentTimeMillis()))
								{
									Thread.sleep(start + timeToSleep - now);
								}
								numItemEquippedAtOnce = 0;
							}
							catch (Exception e)
							{
								log.error("Unable to sleep the anon thread.", e);
							}
						}
					}
				}
			}
		});
	}
}
