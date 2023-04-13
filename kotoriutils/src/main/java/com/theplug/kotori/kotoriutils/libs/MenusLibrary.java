package com.theplug.kotori.kotoriutils.libs;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

@Slf4j
public class MenusLibrary
{
    @Inject
    private Client client;

    @Setter
    private String menuEntryIndexClassName;
    @Setter
    private String menuEntryIndexFieldName;

    @Setter
    private String menuEntryIdentifiersArrayClassName;
    @Setter
    private String menuEntryIdentifiersArrayFieldName;

    @Setter
    private String menuEntryItemIdsArrayClassName;
    @Setter
    private String menuEntryItemIdsArrayFieldName;

    @Setter
    private String menuEntryOptionsArrayClassName;
    @Setter
    private String menuEntryOptionsArrayFieldName;

    @Setter
    private String menuEntryParam0ArrayClassName;
    @Setter
    private String menuEntryParam0ArrayFieldName;

    @Setter
    private String menuEntryParam1ArrayClassName;
    @Setter
    private String menuEntryParam1ArrayFieldName;

    @Setter
    private String menuEntryTargetsArrayClassName;
    @Setter
    private String menuEntryTargetsArrayFieldName;

    @Setter
    private String menuEntryTypesArrayClassName;
    @Setter
    private String menuEntryTypesArrayFieldName;
    
    public int getMenuEntryIndex(MenuEntry entry)
    {
        int menuEntryIndex = -1;
        try
        {
            Field menuEntryIndexField = client.getClass().getClassLoader().loadClass(menuEntryIndexClassName).getDeclaredField(menuEntryIndexFieldName);
            menuEntryIndexField.setAccessible(true);
            menuEntryIndex = menuEntryIndexField.getInt(entry);
            menuEntryIndexField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry's index.", e);
            return menuEntryIndex;
        }
        return menuEntryIndex;
    }
    
    public int[] getMenuIdentifiers()
    {
        int[] menuIdentifiers = null;
        try
        {
            Field menuIdentifiersField = client.getClass().getClassLoader().loadClass(menuEntryIdentifiersArrayClassName).getDeclaredField(menuEntryIdentifiersArrayFieldName);
            menuIdentifiersField.setAccessible(true);
            menuIdentifiers = int[].class.cast(menuIdentifiersField.get(client));
            menuIdentifiersField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Identifiers array.", e);
            return null;
        }
        return menuIdentifiers;
    }
    
    public void setMenuIdentifiers(int[] entries)
    {
        try
        {
            Field menuIdentifiersField = client.getClass().getClassLoader().loadClass(menuEntryIdentifiersArrayClassName).getDeclaredField(menuEntryIdentifiersArrayFieldName);
            menuIdentifiersField.setAccessible(true);
            menuIdentifiersField.set(client, entries);
            menuIdentifiersField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Identifiers array.", e);
        }
    }
    
    public int getMenuIdentifier(int index)
    {
        int menuIdentifier = -1;
        try
        {
            Field menuIdentifiersField = client.getClass().getClassLoader().loadClass(menuEntryIdentifiersArrayClassName).getDeclaredField(menuEntryIdentifiersArrayFieldName);
            menuIdentifiersField.setAccessible(true);
            Object menuIdentifierArray = menuIdentifiersField.get(client);
            menuIdentifier = Array.getInt(menuIdentifierArray, index);
            menuIdentifiersField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Identifier from array.", e);
            return menuIdentifier;
        }
        return menuIdentifier;
    }
    
    public void setMenuIdentifier(int index, int value)
    {
        try
        {
            Field menuIdentifiersField = client.getClass().getClassLoader().loadClass(menuEntryIdentifiersArrayClassName).getDeclaredField(menuEntryIdentifiersArrayFieldName);
            menuIdentifiersField.setAccessible(true);
            Object menuIdentifierArray = menuIdentifiersField.get(client);
            Array.setInt(menuIdentifierArray, index, value);
            menuIdentifiersField.set(client, menuIdentifierArray);
            menuIdentifiersField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Identifier to array.", e);
        }
    }
    
    public int[] getMenuItemIds()
    {
        int[] menuItemIds = null;
        try
        {
            Field menuItemIdsField = client.getClass().getClassLoader().loadClass(menuEntryItemIdsArrayClassName).getDeclaredField(menuEntryItemIdsArrayFieldName);
            menuItemIdsField.setAccessible(true);
            menuItemIds = int[].class.cast(menuItemIdsField.get(client));
            menuItemIdsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Item Ids array.", e);
            return null;
        }
        return menuItemIds;
    }
    
    public void setMenuItemIds(int[] entries)
    {
        try
        {
            Field menuItemIdsField = client.getClass().getClassLoader().loadClass(menuEntryItemIdsArrayClassName).getDeclaredField(menuEntryItemIdsArrayFieldName);
            menuItemIdsField.setAccessible(true);
            menuItemIdsField.set(client, entries);
            menuItemIdsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Item Ids array.", e);
        }
    }
    
    public int getMenuItemId(int index)
    {
        int menuItemId = -1;
        try
        {
            Field menuItemIdsField = client.getClass().getClassLoader().loadClass(menuEntryItemIdsArrayClassName).getDeclaredField(menuEntryItemIdsArrayFieldName);
            menuItemIdsField.setAccessible(true);
            Object menuItemIdsArray = menuItemIdsField.get(client);
            menuItemId = Array.getInt(menuItemIdsArray, index);
            menuItemIdsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Item Id from array.", e);
            return menuItemId;
        }
        return menuItemId;
    }
    
    public void setMenuItemId(int index, int value)
    {
        try
        {
            Field menuItemIdsField = client.getClass().getClassLoader().loadClass(menuEntryItemIdsArrayClassName).getDeclaredField(menuEntryItemIdsArrayFieldName);
            menuItemIdsField.setAccessible(true);
            Object menuItemIdsArray = menuItemIdsField.get(client);
            Array.setInt(menuItemIdsArray, index, value);
            menuItemIdsField.set(client, menuItemIdsArray);
            menuItemIdsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Item Id to array.", e);
        }
    }
    
    public String[] getMenuOptions()
    {
        String[] menuOptions = null;
        try
        {
            Field menuOptionsField = client.getClass().getClassLoader().loadClass(menuEntryOptionsArrayClassName).getDeclaredField(menuEntryOptionsArrayFieldName);
            menuOptionsField.setAccessible(true);
            menuOptions = String[].class.cast(menuOptionsField.get(client));
            menuOptionsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Options array.", e);
            return null;
        }
        return menuOptions;
    }
    
    public void setMenuOptions(String[] entries)
    {
        try
        {
            Field menuOptionsField = client.getClass().getClassLoader().loadClass(menuEntryOptionsArrayClassName).getDeclaredField(menuEntryOptionsArrayFieldName);
            menuOptionsField.setAccessible(true);
            menuOptionsField.set(client, entries);
            menuOptionsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Options array.", e);
        }
    }
    
    public String getMenuOption(int index)
    {
        String menuOption = null;
        try
        {
            Field menuOptionsField = client.getClass().getClassLoader().loadClass(menuEntryOptionsArrayClassName).getDeclaredField(menuEntryOptionsArrayFieldName);
            menuOptionsField.setAccessible(true);
            Object menuOptionsArray = menuOptionsField.get(client);
            menuOption = (String) Array.get(menuOptionsArray, index);
            menuOptionsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Option from array.", e);
            return menuOption;
        }
        return menuOption;
    }
    
    public void setMenuOption(int index, String value)
    {
        try
        {
            Field menuOptionsField = client.getClass().getClassLoader().loadClass(menuEntryOptionsArrayClassName).getDeclaredField(menuEntryOptionsArrayFieldName);
            menuOptionsField.setAccessible(true);
            Object menuOptionsArray = menuOptionsField.get(client);
            Array.set(menuOptionsArray, index, value);
            menuOptionsField.set(client, menuOptionsArray);
            menuOptionsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Option to array.", e);
        }
    }
    
    public int[] getMenuParam0s()
    {
        int[] menuParam0s = null;
        try
        {
            Field menuParam0sField = client.getClass().getClassLoader().loadClass(menuEntryParam0ArrayClassName).getDeclaredField(menuEntryParam0ArrayFieldName);
            menuParam0sField.setAccessible(true);
            menuParam0s = int[].class.cast(menuParam0sField.get(client));
            menuParam0sField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Param0s array.", e);
            return null;
        }
        return menuParam0s;
    }
    
    public void setMenuParam0s(int[] entries)
    {
        try
        {
            Field menuParam0sField = client.getClass().getClassLoader().loadClass(menuEntryParam0ArrayClassName).getDeclaredField(menuEntryParam0ArrayFieldName);
            menuParam0sField.setAccessible(true);
            menuParam0sField.set(client, entries);
            menuParam0sField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Param0s array.", e);
        }
    }
    
    public int getMenuParam0(int index)
    {
        int menuParam0 = -1;
        try
        {
            Field menuParam0Field = client.getClass().getClassLoader().loadClass(menuEntryParam0ArrayClassName).getDeclaredField(menuEntryParam0ArrayFieldName);
            menuParam0Field.setAccessible(true);
            Object menuParam0sArray = menuParam0Field.get(client);
            menuParam0 = Array.getInt(menuParam0sArray, index);
            menuParam0Field.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Param0 from array.", e);
            return menuParam0;
        }
        return menuParam0;
    }
    
    public void setMenuParam0(int index, int value)
    {
        try
        {
            Field menuParam0sField = client.getClass().getClassLoader().loadClass(menuEntryParam0ArrayClassName).getDeclaredField(menuEntryParam0ArrayFieldName);
            menuParam0sField.setAccessible(true);
            Object menuParam0sArray = menuParam0sField.get(client);
            Array.setInt(menuParam0sArray, index, value);
            menuParam0sField.set(client, menuParam0sArray);
            menuParam0sField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Param0 to array.", e);
        }
    }
    
    public int[] getMenuParam1s()
    {
        int[] menuParam1s = null;
        try
        {
            Field menuParam1sField = client.getClass().getClassLoader().loadClass(menuEntryParam1ArrayClassName).getDeclaredField(menuEntryParam1ArrayFieldName);
            menuParam1sField.setAccessible(true);
            menuParam1s = int[].class.cast(menuParam1sField.get(client));
            menuParam1sField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Param1s array.", e);
            return null;
        }
        return menuParam1s;
    }
    
    public void setMenuParam1s(int[] entries)
    {
        try
        {
            Field menuParam1sField = client.getClass().getClassLoader().loadClass(menuEntryParam1ArrayClassName).getDeclaredField(menuEntryParam1ArrayFieldName);
            menuParam1sField.setAccessible(true);
            menuParam1sField.set(client, entries);
            menuParam1sField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Param1s array.", e);
        }
    }
    
    public int getMenuParam1(int index)
    {
        int menuParam1 = -1;
        try
        {
            Field menuParam1sField = client.getClass().getClassLoader().loadClass(menuEntryParam1ArrayClassName).getDeclaredField(menuEntryParam1ArrayFieldName);
            menuParam1sField.setAccessible(true);
            Object menuParam1sArray = menuParam1sField.get(client);
            menuParam1 = Array.getInt(menuParam1sArray, index);
            menuParam1sField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Param1 from array.", e);
            return menuParam1;
        }
        return menuParam1;
    }
    
    public void setMenuParam1(int index, int value)
    {
        try
        {
            Field menuParam1sField = client.getClass().getClassLoader().loadClass(menuEntryParam1ArrayClassName).getDeclaredField(menuEntryParam1ArrayFieldName);
            menuParam1sField.setAccessible(true);
            Object menuParam1sArray = menuParam1sField.get(client);
            Array.setInt(menuParam1sArray, index, value);
            menuParam1sField.set(client, menuParam1sArray);
            menuParam1sField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Param1 to array.", e);
        }
    }
    
    public String[] getMenuTargets()
    {
        String[] menuTargets = null;
        try
        {
            Field menuTargetsField = client.getClass().getClassLoader().loadClass(menuEntryTargetsArrayClassName).getDeclaredField(menuEntryTargetsArrayFieldName);
            menuTargetsField.setAccessible(true);
            menuTargets = String[].class.cast(menuTargetsField.get(client));
            menuTargetsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Targets array.", e);
            return null;
        }
        return menuTargets;
    }
    
    public void setMenuTargets(String[] entries)
    {
        try
        {
            Field menuTargetsField = client.getClass().getClassLoader().loadClass(menuEntryTargetsArrayClassName).getDeclaredField(menuEntryTargetsArrayFieldName);
            menuTargetsField.setAccessible(true);
            menuTargetsField.set(client, entries);
            menuTargetsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Targets array.", e);
        }
    }
    
    public String getMenuTarget(int index)
    {
        String menuTarget = null;
        try
        {
            Field menuTargetsField = client.getClass().getClassLoader().loadClass(menuEntryTargetsArrayClassName).getDeclaredField(menuEntryTargetsArrayFieldName);
            menuTargetsField.setAccessible(true);
            Object menuTargetsArray = menuTargetsField.get(client);
            menuTarget = (String) Array.get(menuTargetsArray, index);
            menuTargetsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Target from array.", e);
            return menuTarget;
        }
        return menuTarget;
    }
    
    public void setMenuTarget(int index, String value)
    {
        try
        {
            Field menuTargetsField = client.getClass().getClassLoader().loadClass(menuEntryTargetsArrayClassName).getDeclaredField(menuEntryTargetsArrayFieldName);
            menuTargetsField.setAccessible(true);
            Object menuTargetsArray = menuTargetsField.get(client);
            Array.set(menuTargetsArray, index, value);
            menuTargetsField.set(client, menuTargetsArray);
            menuTargetsField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Target to array.", e);
        }
    }
    
    public int[] getMenuOpcodes()
    {
        int[] menuOpcodes = null;
        try
        {
            Field menuOpcodesField = client.getClass().getClassLoader().loadClass(menuEntryTypesArrayClassName).getDeclaredField(menuEntryTypesArrayFieldName);
            menuOpcodesField.setAccessible(true);
            menuOpcodes = int[].class.cast(menuOpcodesField.get(client));
            menuOpcodesField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Opcodes array.", e);
            return null;
        }
        return menuOpcodes;
    }
    
    public void setMenuOpcodes(int[] entries)
    {
        try
        {
            Field menuOpcodesField = client.getClass().getClassLoader().loadClass(menuEntryTypesArrayClassName).getDeclaredField(menuEntryTypesArrayFieldName);
            menuOpcodesField.setAccessible(true);
            menuOpcodesField.set(client, entries);
            menuOpcodesField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Opcodes array.", e);
        }
    }
    
    public int getMenuOpcode(int index)
    {
        int menuOpcode = -1;
        try
        {
            Field menuOpcodesField = client.getClass().getClassLoader().loadClass(menuEntryTypesArrayClassName).getDeclaredField(menuEntryTypesArrayFieldName);
            menuOpcodesField.setAccessible(true);
            Object menuOpcodesArray = menuOpcodesField.get(client);
            menuOpcode = Array.getInt(menuOpcodesArray, index);
            menuOpcodesField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to get MenuEntry Opcode from array.", e);
            return menuOpcode;
        }
        return menuOpcode;
    }
    
    public void setMenuOpcode(int index, int value)
    {
        try
        {
            Field menuOpcodesField = client.getClass().getClassLoader().loadClass(menuEntryTypesArrayClassName).getDeclaredField(menuEntryTypesArrayFieldName);
            menuOpcodesField.setAccessible(true);
            Object menuOpcodesArray = menuOpcodesField.get(client);
            Array.setInt(menuOpcodesArray, index, value);
            menuOpcodesField.set(client, menuOpcodesArray);
            menuOpcodesField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set MenuEntry Opcode to array.", e);
        }
    }
    
    public void insertMenuEntry(int index, String option, String target, int opcode, int id, int param0, int param1, int itemid)
    {
        setMenuOption(index, option);
        setMenuTarget(index, target);
        setMenuOpcode(index, opcode);
        setMenuIdentifier(index, id);
        setMenuParam0(index, param0);
        setMenuParam1(index, param1);
        setMenuItemId(index, itemid);
    }
}
