package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.kit.KitType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;

import java.util.Arrays;
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
			return true;
		}
		
		int numItemEquippedAtOnce = 0;
		Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY.getId());
		if (inventoryWidget == null)
		{
			return true;
		}
		
		Widget[] itemWidgets = inventoryWidget.getChildren();
		if (itemWidgets == null)
		{
			return true;
		}
		
		for (Widget itemWidget : itemWidgets)
		{
			//Item slot number
			int slot = itemWidget.getIndex();
			//Check if you can wield the item
			String[] menuActions = itemWidget.getActions();
			if (menuActions == null)
			{
				continue;
			}
			boolean canWield = Arrays.asList(menuActions).contains("Wield");
			boolean canWear = Arrays.asList(menuActions).contains("Wear");
			boolean canEquip = Arrays.asList(menuActions).contains("Equip");
			//Dynamically gets the index of the Wear or Wield action for the invoke actions.
			//You add 1 to the index of the actions cuz the returned index is always 1 less than the required action.
			int index = 1;
			
			if (canWield)
			{
				index += Arrays.asList(menuActions).lastIndexOf("Wield");
			}
			else if (canWear)
			{
				index += Arrays.asList(menuActions).lastIndexOf("Wear");
			}
			else if (canEquip)
			{
				index += Arrays.asList(menuActions).lastIndexOf("Equip");
			}
			else
			{
				continue;
			}
			
			for (int itemId : itemIds)
			{
				if (itemWidget.getItemId() == itemId)
				{
					/*
					Manually edge cases
					switch (itemId)
					{
						case 21760: //Kharedst's memoirs
						case 25818: //Book of the dead
							ReflectionLibrary.invokeMenuAction(slot, WidgetInfo.INVENTORY.getId(), MenuAction.CC_OP.getId(), 2, itemId);
							break;
						default:
							ReflectionLibrary.invokeMenuAction(slot, WidgetInfo.INVENTORY.getId(), MenuAction.CC_OP.getId(), 3, itemId);
							break;
					}
					*/
					
					ReflectionLibrary.invokeMenuAction(slot, WidgetInfo.INVENTORY.getId(), MenuAction.CC_OP.getId(), index, itemId);
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
		ReflectionLibrary.invokeMenuAction(slot, WidgetInfo.INVENTORY.getId(), MenuAction.CC_OP.getId(), 2, itemID);
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
	
	// Equipment Methods
	
	public static boolean yourEquipmentContains(int itemId)
	{
		return yourEquipmentContains(itemId, null);
	}
	
	public static boolean yourEquipmentContains(int itemId, EquipmentInventorySlot slot)
	{
		ItemContainer equipmentContainer = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipmentContainer == null)
		{
			return false;
		}
		
		Item[] equipment = equipmentContainer.getItems();
		if (slot == null)
		{
			for (Item item : equipment)
			{
				if (item.getId() == itemId)
				{
					return true;
				}
			}
			return false;
		}
		return equipment[slot.getSlotIdx()].getId() == itemId;
	}
	
	public static boolean yourEquipmentContains(int... itemIds)
	{
		for (int itemId : itemIds)
		{
			if (!yourEquipmentContains(itemId))
			{
				return false;
			}
		}
		return true;
	}
	
	public static int yourEquipmentCount(int... itemIds)
	{
		int numberOfItemsEquipped = 0;
		
		for (int itemId : itemIds)
		{
			if (yourEquipmentContains(itemId))
			{
				numberOfItemsEquipped++;
			}
		}
		return numberOfItemsEquipped;
	}
	
	public static boolean playerEquipmentContains(Player player, int itemId, KitType equipmentSlot)
	{
		if (player == null)
		{
			return false;
		}
		
		PlayerComposition playerComposition = player.getPlayerComposition();
		if (playerComposition == null)
		{
			return false;
		}
		
		int[] equipmentIds = playerComposition.getEquipmentIds();
		if (equipmentSlot == null)
		{
			for (int kitId : equipmentIds)
			{
				if (itemId == kitId - 512)
				{
					return true;
				}
			}
			return false;
		}
		return equipmentIds[equipmentSlot.getIndex()] - 512 == itemId;
	}
	
	public static boolean playerEquipmentContains(Player player, int itemId)
	{
		return playerEquipmentContains(player, itemId, null);
	}
	
	public static boolean playerEquipmentContains(Player player, int... itemIds)
	{
		for (int itemId : itemIds)
		{
			if (!playerEquipmentContains(player, itemId))
			{
				return false;
			}
		}
		return true;
	}
	
	public static int playerEquipmentCount(Player player, int... itemIds)
	{
		int numberOfItemsEquipped = 0;
		
		for (int itemId : itemIds)
		{
			if (playerEquipmentContains(player, itemId))
			{
				numberOfItemsEquipped++;
			}
		}
		
		return numberOfItemsEquipped;
	}
}
