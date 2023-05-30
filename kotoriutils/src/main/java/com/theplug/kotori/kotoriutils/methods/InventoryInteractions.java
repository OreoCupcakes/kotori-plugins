package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.reflection.InvokesLibrary;
import com.theplug.kotori.kotoriutils.rlapi.PrayerExtended;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;

import java.util.Set;

@Slf4j
public class InventoryInteractions
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	private static final Set<Integer> prayerPotions = Set.of(ItemID.PRAYER_POTION1, ItemID.PRAYER_POTION2, ItemID.PRAYER_POTION3, ItemID.PRAYER_POTION4);
	private static final Set<Integer> superRestores = Set.of(ItemID.SUPER_RESTORE1, ItemID.SUPER_RESTORE2, ItemID.SUPER_RESTORE3, ItemID.SUPER_RESTORE4);
	private static final Set<Integer> sanfewSerums = Set.of(ItemID.SANFEW_SERUM1, ItemID.SANFEW_SERUM2, ItemID.SANFEW_SERUM3, ItemID.SANFEW_SERUM4);
	private static final Set<Set<Integer>> allPrayerRestoringPotions = Set.of(prayerPotions, superRestores, sanfewSerums);
	
	
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
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
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
	
	public static boolean equipItems(int[] itemIds, int numEquips)
	{
		if (itemIds == null)
		{
			return false;
		}
		
		int numItemEquippedAtOnce = 0;
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
		if (inventory == null)
		{
			return false;
		}
		Item[] items = inventory.getItems();
		for (int slot = 0; slot < items.length; slot++)
		{
			for (int itemId : itemIds)
			{
				if (items[slot].getId() == itemId)
				{
					InvokesLibrary.invoke(slot, WidgetInfo.INVENTORY.getId(), MenuAction.CC_OP.getId(), 3, itemId);
					numItemEquippedAtOnce++;
					if (numItemEquippedAtOnce >= numEquips)
					{
						return false;
					}
				}
			}
		}
		//Return true because it went through the entire inventory once
		return true;
	}
	
	public static boolean equipItems(int[] itemIds)
	{
		return equipItems(itemIds, 3);
	}
	
	public static boolean consumeItem(int itemID)
	{
		int slot = getItemSlotNumber(itemID);
		if (slot == -1)
		{
			return false;
		}
		InvokesLibrary.invoke(slot, WidgetInfo.INVENTORY.getId(), MenuAction.CC_OP.getId(), 2, itemID);
		return true;
	}
	
	public static void drinkPrayerRestoreDose(boolean drinkPrayers, boolean drinkSupers, boolean drinkSanfews)
	{
		int setIndex = 0;
		for (Set<Integer> setOfPots : allPrayerRestoringPotions)
		{
			for (int itemId : setOfPots)
			{
				if (setIndex == 0 && !drinkPrayers)
				{
					break;
				}
				else if (setIndex == 1 && !drinkSupers)
				{
					break;
				}
				else if (setIndex == 2 && !drinkSanfews)
				{
					break;
				}
				
				boolean drank = consumeItem(itemId);
				if (drank)
				{
					return;
				}
			}
			setIndex++;
		}
		
		/*
		if (drinkPrayers)
		{
			for (int prayerPotion : prayerPotions)
			{
				boolean drank = consumeItem(prayerPotion);
				if (drank)
				{
					return;
				}
			}
		}
		
		if (drinkSupers)
		{
			for (int superRestore : superRestores)
			{
				boolean drank = consumeItem(superRestore);
				if (drank)
				{
					return;
				}
			}
		}
		
		if (drinkSanfews)
		{
			for (int sanfewSerum : sanfewSerums)
			{
				boolean drank = consumeItem(sanfewSerum);
				if (drank)
				{
					return;
				}
			}
		}
		*/
	}
}
