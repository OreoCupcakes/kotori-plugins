package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.kit.KitType;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class InventoryInteractions
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	//143 141 139 2434
	//3030 3028 3026 3024
	//10931 10929 10927 10925
	private static final List<Integer> prayerPotions = List.of(ItemID._1DOSEPRAYERRESTORE, ItemID._2DOSEPRAYERRESTORE, ItemID._3DOSEPRAYERRESTORE, ItemID._4DOSEPRAYERRESTORE);
	private static final List<Integer> superRestores = List.of(ItemID._1DOSE2RESTORE, ItemID._2DOSE2RESTORE, ItemID._3DOSE2RESTORE, ItemID._4DOSE2RESTORE);
	private static final List<Integer> sanfewSerums = List.of(ItemID.SANFEW_SALVE_1_DOSE, ItemID.SANFEW_SALVE_2_DOSE, ItemID.SANFEW_SALVE_3_DOSE, ItemID.SANFEW_SALVE_4_DOSE);
	private static final List<List<Integer>> allPrayerRestoringPotions = List.of(prayerPotions, superRestores, sanfewSerums);

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
		ItemContainer inventory = client.getItemContainer(InventoryID.INV);
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
		Widget inventoryWidget = client.getWidget(InterfaceID.Inventory.ITEMS);
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
			List<String> menuEntries = Arrays.asList(menuActions);
			boolean canWield = menuEntries.contains("Wield");
			boolean canWear = menuEntries.contains("Wear");
			boolean canEquip = menuEntries.contains("Equip");
			//Dynamically gets the index of the Wear or Wield action for the invoke actions.
			//You add 1 to the index of the actions cuz the returned index is always 1 less than the required action.
			int index = 1;

			if (canWield)
			{
				index += menuEntries.lastIndexOf("Wield");
			}
			else if (canWear)
			{
				index += menuEntries.lastIndexOf("Wear");
			}
			else if (canEquip)
			{
				index += menuEntries.lastIndexOf("Equip");
			}
			else
			{
				continue;
			}

			for (int itemId : itemIds)
			{
				if (itemWidget.getItemId() == itemId)
				{
					ReflectionLibrary.invokeMenuAction(slot, InterfaceID.Inventory.ITEMS, MenuAction.CC_OP.getId(), index, itemId);
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

	public static boolean equipItems(int itemId)
	{
		int[] itemToEquip = new int[]{itemId};
		return equipItems(itemToEquip);
	}
	
	public static boolean consumeItem(int itemID)
	{
		int slot = getItemSlotNumber(itemID);
		if (slot == -1)
		{
			return false;
		}
		ReflectionLibrary.invokeMenuAction(slot, InterfaceID.Inventory.ITEMS, MenuAction.CC_OP.getId(), 2, itemID);
		return true;
	}
	
	public static void drinkPrayerRestoreDose(boolean drinkPrayers, boolean drinkSupers, boolean drinkSanfews)
	{
		int listIndex = 0;
		for (List<Integer> setOfPots : allPrayerRestoringPotions)
		{
			for (int itemId : setOfPots)
			{
				if (listIndex == 0 && !drinkPrayers)
				{
					break;
				}
				else if (listIndex == 1 && !drinkSupers)
				{
					break;
				}
				else if (listIndex == 2 && !drinkSanfews)
				{
					break;
				}
				
				boolean drank = consumeItem(itemId);
				if (drank)
				{
					return;
				}
			}
			listIndex++;
		}
	}

	public static boolean inventoryContains(int itemId)
	{
		ItemContainer inventory = client.getItemContainer(InventoryID.INV);
		if (inventory == null)
		{
			return false;
		}

		return inventory.contains(itemId);
	}
	
	// Equipment Methods
	
	public static boolean yourEquipmentContains(int itemId)
	{
		return yourEquipmentContains(itemId, null);
	}
	
	public static boolean yourEquipmentContains(int itemId, EquipmentInventorySlot slot)
	{
		ItemContainer equipmentContainer = client.getItemContainer(InventoryID.WORN);
		if (equipmentContainer == null)
		{
			return false;
		}

		if (slot == null)
		{
			return equipmentContainer.contains(itemId);
		}

		Item[] equipment = equipmentContainer.getItems();
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

	public static int getEquippedItemId(EquipmentInventorySlot slot)
	{
		ItemContainer equipment = client.getItemContainer(InventoryID.WORN);
		if (equipment == null)
		{
			return -1;
		}
		Item weapon = equipment.getItem(slot.getSlotIdx());
		if (weapon == null)
		{
			return -1;
		}
		return weapon.getId();
	}
}
