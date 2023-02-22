package com.theplug.kotori.kotoriutils;

import com.google.gson.*;
import com.theplug.kotori.kotoriutils.gson.Hooks;
import com.theplug.kotori.kotoriutils.libs.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Slf4j
@Singleton
@PluginDescriptor(
        name = "Kotori Plugin Utils",
        enabledByDefault = true,
        description = "Utilities needed for certain ported plugins to work.",
        tags = {"ported", "kotori", "utilities", "utils", "plugin"}
)
public class KotoriUtils extends Plugin {

    @Inject
    private Client client;

    final private String hooksFileURL = "https://github.com/OreoCupcakes/kotori-ported-plugins-hosting/blob/master/hooks.json?raw=true";

    @Inject
    @Getter
    private NPCsLibrary npcsLibrary;
    @Inject
    @Getter
    private InvokesLibrary invokesLibrary;
    @Inject
    @Getter
    private SpellsLibrary spellsLibrary;
    @Inject
    @Getter
    private MenusLibrary menusLibrary;
    @Inject
    @Getter
    private WalkingLibrary walkingLibrary;

    private Hooks rsHooks;

    @Override
    protected void startUp()
    {
        getObfuscatedHooks();
    }

    @Override
    protected void shutDown()
    {

    }

    private void getObfuscatedHooks()
    {
        try
        {
            if (rsHooks == null)
            {
                URL url = new URL(hooksFileURL);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                Gson gson = new Gson();
                rsHooks = gson.fromJson(bufferedReader, Hooks.class);
            }

            //Set the game hooks
            //invokeMenuAction Hooks
            invokesLibrary.setWorldMapData_0_ClassName(getClassName(rsHooks.getInvokeMenuActionHook()));
            invokesLibrary.setInvokeMenuActionMethodName(getFieldOrMethodName(rsHooks.getInvokeMenuActionHook()));
            invokesLibrary.setInvokeMenuActionGarbageValue(rsHooks.getInvokeMenuActionHookJunkValue());
            //NPC Overhead Icons Hook
            npcsLibrary.setNpcCompositionClassName(getClassName(rsHooks.getGetNpcCompositionOverheadIcon()));
            npcsLibrary.setOverheadIconFieldName(getFieldOrMethodName(rsHooks.getGetNpcCompositionOverheadIcon()));
            //NPC Animation IDs Hook
            npcsLibrary.setActorClassName(getClassName(rsHooks.getGetActorAnimationIdFieldHook()));
            npcsLibrary.setSequenceFieldName(getFieldOrMethodName(rsHooks.getGetActorAnimationIdFieldHook()));
            npcsLibrary.setSequenceGetterMultiplier(rsHooks.getGetActorAnimationIdMultiplier());
            //Scene Walking Set X and Y Hook
            walkingLibrary.setSetXandYClassName(getClassName(rsHooks.getSetXandYHook()));
            walkingLibrary.setSetXandYMethodName(getFieldOrMethodName(rsHooks.getSetXandYHook()));
            //Scene Walking Set Viewport Walking Hook
            walkingLibrary.setSetViewportWalkingClassName(getClassName(rsHooks.getSetViewportWalkingFieldHook()));
            walkingLibrary.setSetViewportWalkingFieldName(getFieldOrMethodName(rsHooks.getSetViewportWalkingFieldHook()));
            //Scene Walking Check Click Field Hook
            walkingLibrary.setCheckClickClassName(getClassName(rsHooks.getCheckClickFieldHook()));
            walkingLibrary.setCheckClickFieldName(getFieldOrMethodName(rsHooks.getCheckClickFieldHook()));
            //Selected Spell Widget Hook
            spellsLibrary.setSelectedSpellWidgetClassName(getClassName(rsHooks.getSetSelectedSpellWidgetHook()));
            spellsLibrary.setSelectedSpellWidgetFieldName(getFieldOrMethodName(rsHooks.getSetSelectedSpellWidgetHook()));
            spellsLibrary.setSelectedSpellWidgetMultiplier(rsHooks.getSetSelectedSpellWidgetMultiplier());
            //Selected Spell Child Hook
            spellsLibrary.setSelectedSpellChildIndexClassName(getClassName(rsHooks.getSetSelectedSpellChildIndexHook()));
            spellsLibrary.setSelectedSpellChildIndexFieldName(getFieldOrMethodName(rsHooks.getSetSelectedSpellChildIndexHook()));
            spellsLibrary.setSelectedSpellChildIndexMultiplier(rsHooks.getSetSelectedSpellChildIndexMultiplier());
            //Selected Spell Item Hook
            spellsLibrary.setSelectedSpellItemIDClassName(getClassName(rsHooks.getSetSelectedSpellItemIDHook()));
            spellsLibrary.setSelectedSpellItemIDFieldName(getFieldOrMethodName(rsHooks.getSetSelectedSpellItemIDHook()));
            spellsLibrary.setSelectedSpellItemIDMultiplier(rsHooks.getSetSelectedSpellItemIDMultiplier());
            //MenuEntry Index Hook
            menusLibrary.setMenuEntryIndexClassName(getClassName(rsHooks.getMenuEntryIndexFieldHook()));
            menusLibrary.setMenuEntryIndexFieldName(getFieldOrMethodName(rsHooks.getMenuEntryIndexFieldHook()));
            //MenuEntry Identifiers Hook
            menusLibrary.setMenuEntryIdentifiersArrayClassName(getClassName(rsHooks.getMenuEntryIdentifiersArrayFieldHook()));
            menusLibrary.setMenuEntryIdentifiersArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryIdentifiersArrayFieldHook()));
            //MenuEntry Item Ids Hook
            menusLibrary.setMenuEntryItemIdsArrayClassName(getClassName(rsHooks.getMenuEntryItemIdsArrayFieldHook()));
            menusLibrary.setMenuEntryItemIdsArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryItemIdsArrayFieldHook()));
            //MenuEntry Options Hook
            menusLibrary.setMenuEntryOptionsArrayClassName(getClassName(rsHooks.getMenuEntryOptionsArrayFieldHook()));
            menusLibrary.setMenuEntryOptionsArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryOptionsArrayFieldHook()));
            //MenuEntry Param0 Hook
            menusLibrary.setMenuEntryParam0ArrayClassName(getClassName(rsHooks.getMenuEntryParam0ArrayFieldHook()));
            menusLibrary.setMenuEntryParam0ArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryParam0ArrayFieldHook()));
            //MenuEntry Param1 Hook
            menusLibrary.setMenuEntryParam1ArrayClassName(getClassName(rsHooks.getMenuEntryParam1ArrayFieldHook()));
            menusLibrary.setMenuEntryParam1ArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryParam1ArrayFieldHook()));
            //MenuEntry Targets Hook
            menusLibrary.setMenuEntryTargetsArrayClassName(getClassName(rsHooks.getMenuEntryTargetsArrayFieldHook()));
            menusLibrary.setMenuEntryTargetsArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryTargetsArrayFieldHook()));
            //MenuEntry Types Hook
            menusLibrary.setMenuEntryTypesArrayClassName(getClassName(rsHooks.getMenuEntryTypesArrayFieldHook()));
            menusLibrary.setMenuEntryTypesArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryTypesArrayFieldHook()));
        }
        catch (Exception e)
        {
            log.error("Unable to get necessary game hooks from URL.", e);
        }
    }

    private String getClassName(String jsonString)
    {
        String[] jsonSplit = jsonString.split("\\.");
        return jsonSplit[0];
    }

    private String getFieldOrMethodName(String jsonString)
    {
        String[] jsonSplit = jsonString.split("\\.");
        return jsonSplit[1];
    }

}
