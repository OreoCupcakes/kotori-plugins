package com.theplug.kotori.kotoriutils.reflection;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.client.RuneLite;

import javax.inject.Inject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

@Slf4j
public class MenusLibrary
{
    private static final Client client = RuneLite.getInjector().getInstance(Client.class);

    @Setter
    private static String menuEntryIndexClassName;
    @Setter
    private static String menuEntryIndexFieldName;

    @Setter
    private static String menuEntryIdentifiersArrayClassName;
    @Setter
    private static String menuEntryIdentifiersArrayFieldName;

    @Setter
    private static String menuEntryItemIdsArrayClassName;
    @Setter
    private static String menuEntryItemIdsArrayFieldName;

    @Setter
    private static String menuEntryOptionsArrayClassName;
    @Setter
    private static String menuEntryOptionsArrayFieldName;

    @Setter
    private static String menuEntryParam0ArrayClassName;
    @Setter
    private static String menuEntryParam0ArrayFieldName;

    @Setter
    private static String menuEntryParam1ArrayClassName;
    @Setter
    private static String menuEntryParam1ArrayFieldName;

    @Setter
    private static String menuEntryTargetsArrayClassName;
    @Setter
    private static String menuEntryTargetsArrayFieldName;

    @Setter
    private static String menuEntryTypesArrayClassName;
    @Setter
    private static String menuEntryTypesArrayFieldName;
    
    public static int getMenuEntryIndex(MenuEntry entry)
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
    
    private static void setMenuIdentifier(int index, int value)
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
    
    private static void setMenuItemId(int index, int value)
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
    
    private static void setMenuOption(int index, String value)
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
    
    private static void setMenuParam0(int index, int value)
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
    
    private static void setMenuParam1(int index, int value)
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
    
    private static void setMenuTarget(int index, String value)
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
    
    private static void setMenuOpcode(int index, int value)
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
    
    public static void insertMenuEntry(int index, String option, String target, int opcode, int id, int param0, int param1, int itemId)
    {
        setMenuOption(index, option);
        setMenuTarget(index, target);
        setMenuOpcode(index, opcode);
        setMenuIdentifier(index, id);
        setMenuParam0(index, param0);
        setMenuParam1(index, param1);
        setMenuItemId(index, itemId);
    }
}
