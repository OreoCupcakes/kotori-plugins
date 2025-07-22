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
import java.util.Set;

@Slf4j
public class InventoryInteractions
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);

	public static final List<Integer> PRAYER_POTIONS = List.of(ItemID._1DOSEPRAYERRESTORE, ItemID._2DOSEPRAYERRESTORE, ItemID._3DOSEPRAYERRESTORE, ItemID._4DOSEPRAYERRESTORE);
	public static final List<Integer> SUPER_RESTORES = List.of(ItemID._1DOSE2RESTORE, ItemID._2DOSE2RESTORE, ItemID._3DOSE2RESTORE, ItemID._4DOSE2RESTORE);
	public static final List<Integer> SANFEW_SERUMS = List.of(ItemID.SANFEW_SALVE_1_DOSE, ItemID.SANFEW_SALVE_2_DOSE, ItemID.SANFEW_SALVE_3_DOSE, ItemID.SANFEW_SALVE_4_DOSE);
	public static final List<List<Integer>> ALL_PRAYER_RESTORING_POTIONS = List.of(PRAYER_POTIONS, SUPER_RESTORES, SANFEW_SERUMS);

	public static final Set<Integer> DEMONIC_ASHES = Set.of(ItemID.FIENDISH_ASHES, ItemID.VILE_ASHES, ItemID.MALICIOUS_ASHES, ItemID.ABYSSAL_ASHES, ItemID.INFERNAL_ASHES);
	public static final Set<Integer> SINISTER_BONES = Set.of(ItemID.BONES, ItemID.MM_NORMAL_MONKEY_BONES, ItemID.BAT_BONES, ItemID.BIG_BONES, ItemID.TBWT_JOGRE_BONES, ItemID.BABYWYRM_BONES, ItemID.ZOGRE_BONES,
			ItemID.TBWT_BEAST_BONES, ItemID.BABYDRAGON_BONES, ItemID.WYRM_BONES, ItemID.DRAGON_BONES, ItemID.WYVERN_BONES, ItemID.DRAKE_BONES, ItemID.ZOGRE_ANCESTRAL_BONES_FAYG, ItemID.LAVA_DRAGON_BONES,
			ItemID.ZOGRE_ANCESTRAL_BONES_RAURG, ItemID.HYDRA_BONES, ItemID.DAGANNOTH_KING_BONES, ItemID.ZOGRE_ANCESTRAL_BONES_OURG, ItemID.DRAGON_BONES_SUPERIOR);

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
		for (List<Integer> setOfPots : ALL_PRAYER_RESTORING_POTIONS)
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

	public static int inventoryCountOf(int itemId)
	{
		return inventoryCountOf(Set.of(itemId));
	}

	public static int inventoryCountOf(Set<Integer> itemIds)
	{
		ItemContainer inventory = client.getItemContainer(InventoryID.INV);
		if (inventory == null)
		{
			return 0;
		}

		int count = 0;

		for (Item item : inventory.getItems())
		{
			if (itemIds.contains(item.getId()))
			{
				count++;
			}
		}

		return count;
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
