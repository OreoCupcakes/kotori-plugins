package com.theplug.kotori.kotoriutils.libs;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import javax.inject.Inject;
import java.lang.reflect.Field;

@Slf4j
public class SpellsLibrary {

    @Inject
    private Client client;

    @Setter
    private String selectedSpellWidgetClassName;
    @Setter
    private String selectedSpellWidgetFieldName;
    @Setter
    private int selectedSpellWidgetMultiplier;

    @Setter
    private String selectedSpellChildIndexClassName;
    @Setter
    private String selectedSpellChildIndexFieldName;
    @Setter
    private int selectedSpellChildIndexMultiplier;

    @Setter
    private String selectedSpellItemIDClassName;
    @Setter
    private String selectedSpellItemIDFieldName;
    @Setter
    private int selectedSpellItemIDMultiplier;

    public SpellsLibrary(Client client) {
        this.client = client;
    }

    // Passed as the Widget's grouped ID
    public void setSelectedSpellWidget(int widgetGroupId) {
        try {
            Class selectedSpellWidgetClass = client.getClass().getClassLoader().loadClass(selectedSpellWidgetClassName);
            Field selectedSpellWidgetField = selectedSpellWidgetClass.getDeclaredField(selectedSpellWidgetFieldName);
            selectedSpellWidgetField.setAccessible(true);
            selectedSpellWidgetField.setInt(selectedSpellWidgetClass,widgetGroupId * selectedSpellWidgetMultiplier);
            selectedSpellWidgetField.setAccessible(false);
        } catch (Exception e) {
            log.error("Failed to set the selected Spell Widget.", e);
        }
    }

    // Used for spells with multiple options, i.e. Teleport to House
    public void setSelectedSpellChildIndex(int index) {
        try {
            Class selectedSpellChildIndexClass = client.getClass().getClassLoader().loadClass(selectedSpellChildIndexClassName);
            Field selectedSpellChildIndexField = selectedSpellChildIndexClass.getDeclaredField(selectedSpellChildIndexFieldName);
            selectedSpellChildIndexField.setAccessible(true);
            selectedSpellChildIndexField.setInt(selectedSpellChildIndexClass, index * selectedSpellChildIndexMultiplier);
            selectedSpellChildIndexField.setAccessible(false);
        } catch (Exception e) {
            log.error("Failed to set the selected Spell Child Index.", e);
        }
    }

    // Used for spells that interact with items, i.e. High Alch
    public void setSelectedSpellItemId(int itemid) {
        try {
            Class selectedSpellItemIdClass = client.getClass().getClassLoader().loadClass(selectedSpellItemIDClassName);
            Field selectedSpellItemIdField = selectedSpellItemIdClass.getDeclaredField(selectedSpellItemIDFieldName);
            selectedSpellItemIdField.setAccessible(true);
            selectedSpellItemIdField.setInt(selectedSpellItemIdClass, itemid * selectedSpellItemIDMultiplier);
            selectedSpellItemIdField.setAccessible(false);
        } catch (Exception e) {
            log.error("Failed to set the selected Spell Item Id.", e);
        }
    }
}
