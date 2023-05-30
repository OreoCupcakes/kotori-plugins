package com.theplug.kotori.kotoriutils.reflection;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.RuneLite;

import javax.inject.Inject;
import java.lang.reflect.Field;

@Slf4j
public class SpellsLibrary
{
    private static final Client client = RuneLite.getInjector().getInstance(Client.class);

    @Setter
    private static String selectedSpellWidgetClassName;
    @Setter
    private static String selectedSpellWidgetFieldName;
    @Setter
    private static int selectedSpellWidgetMultiplier;

    @Setter
    private static String selectedSpellChildIndexClassName;
    @Setter
    private static String selectedSpellChildIndexFieldName;
    @Setter
    private static int selectedSpellChildIndexMultiplier;

    @Setter
    private static String selectedSpellItemIDClassName;
    @Setter
    private static String selectedSpellItemIDFieldName;
    @Setter
    private static int selectedSpellItemIDMultiplier;

    public static void setSelectedSpellWidget(int widgetPackedId)
    {
        try
        {
            Class<?> selectedSpellWidgetClass = client.getClass().getClassLoader().loadClass(selectedSpellWidgetClassName);
            Field selectedSpellWidgetField = selectedSpellWidgetClass.getDeclaredField(selectedSpellWidgetFieldName);
            selectedSpellWidgetField.setAccessible(true);
            selectedSpellWidgetField.setInt(selectedSpellWidgetClass,widgetPackedId * selectedSpellWidgetMultiplier);
            selectedSpellWidgetField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set the selected Spell Widget.", e);
        }
    }

    // Used for spells with multiple options, i.e. Teleport to House
    public static void setSelectedSpellChildIndex(int index)
    {
        try
        {
            Class<?> selectedSpellChildIndexClass = client.getClass().getClassLoader().loadClass(selectedSpellChildIndexClassName);
            Field selectedSpellChildIndexField = selectedSpellChildIndexClass.getDeclaredField(selectedSpellChildIndexFieldName);
            selectedSpellChildIndexField.setAccessible(true);
            selectedSpellChildIndexField.setInt(selectedSpellChildIndexClass, index * selectedSpellChildIndexMultiplier);
            selectedSpellChildIndexField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set the selected Spell Child Index.", e);
        }
    }

    // Used for spells that interact with items?, i.e. Plank make?
    public static void setSelectedSpellItemId(int itemId)
    {
        try
        {
            Class<?> selectedSpellItemIdClass = client.getClass().getClassLoader().loadClass(selectedSpellItemIDClassName);
            Field selectedSpellItemIdField = selectedSpellItemIdClass.getDeclaredField(selectedSpellItemIDFieldName);
            selectedSpellItemIdField.setAccessible(true);
            selectedSpellItemIdField.setInt(selectedSpellItemIdClass, itemId * selectedSpellItemIDMultiplier);
            selectedSpellItemIdField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set the selected Spell Item Id.", e);
        }
    }
}
