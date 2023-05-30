package com.theplug.kotori.kotoriutils;

import com.google.gson.*;
import com.google.inject.Provides;
import com.theplug.kotori.kotoriutils.gson.Hooks;
import com.theplug.kotori.kotoriutils.reflection.*;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
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

    private final String hooksFileURL = "https://github.com/OreoCupcakes/kotori-plugins-releases/blob/master/hooks.json?raw=true";
    @Inject
    private Client client;
    @Inject
    private KotoriUtilsConfig config;
    @Inject
    private ConfigManager configManager;

    private Gson gson;
    private Hooks rsHooks;
    private boolean hooksLoaded;

    @Provides
    KotoriUtilsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(KotoriUtilsConfig.class);
    }

    @Override
    protected void startUp()
    {
        gson = new Gson();
        new Thread(() ->
        {
            getHooksJson();
            parseHooksJson();
        }).start();
    }

    @Override
    protected void shutDown()
    {
        gson = null;
        rsHooks = null;
        hooksLoaded = false;
    }

    private void getHooksJson()
    {
        if (rsHooks != null)
        {
            return;
        }
    
        BufferedReader reader = null;
        for (int i = 1; i <= 15; i++)
        {
            try
            {
                URL url = new URL(hooksFileURL);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                break;
            }
            catch (Exception e1)
            {
                log.error("Attempt #" + i + ". Unable to establish a connection and download the hooks from the URL.", e1);
            }
        }
        
        if (reader == null)
        {
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(client.getCanvas(),
                        "<html>Connection error. Unable to download necessary game hooks information from the Internet." +
                                "<br>Make sure you are connected to the Internet and your proxy or VPN isn't being flagged as suspicious." +
                                "<br>You can re-establish a connection by clicking the \"Click to Load Hooks\" button within Kotori Plugin Utils." +
                                "<br><div style='color:yellow'><b><u>If you are unable load the hooks, the client will crash when using plugins dependent on Kotori Plugin Utils!</u></b></div></html>"
                        , "Kotori Plugin Utils",JOptionPane.WARNING_MESSAGE));
            return;
        }
            
        try
        {
            rsHooks = gson.fromJson(reader, Hooks.class);
            reader.close();
        }
        catch (Exception e)
        {
            log.error("Unable to parse Hooks.json into a Hooks object.", e);
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(client.getCanvas(),
                            "Error in parsing Hooks.json. Yell at Kotori for forgetting a comma.","Kotori Plugin Utils", JOptionPane.WARNING_MESSAGE));
        }
    }

    private void parseHooksJson()
    {
        if (rsHooks == null || hooksLoaded)
        {
            return;
        }
        //Set the game hooks
        //invokeMenuAction Hooks
        InvokesLibrary.setInvokeMenuActionClassName(getClassName(rsHooks.getInvokeMenuActionHook()));
        InvokesLibrary.setInvokeMenuActionMethodName(getFieldOrMethodName(rsHooks.getInvokeMenuActionHook()));
        InvokesLibrary.setInvokeMenuActionJunkValue(rsHooks.getInvokeMenuActionHookJunkValue());
        //NPC Overhead Icons Hook
        NPCsLibrary.setNpcCompositionClassName(getClassName(rsHooks.getGetNpcCompositionOverheadIcon()));
        NPCsLibrary.setOverheadIconFieldName(getFieldOrMethodName(rsHooks.getGetNpcCompositionOverheadIcon()));
        //NPC Animation IDs Hook
        NPCsLibrary.setActorClassName(getClassName(rsHooks.getActorAnimationIdFieldHook()));
        NPCsLibrary.setActorAnimationIdFieldName(getFieldOrMethodName(rsHooks.getActorAnimationIdFieldHook()));
        NPCsLibrary.setActorAnimationIdMultiplierValue(rsHooks.getActorAnimationIdMultiplier());
        //Scene Walking Set X and Y Hook
    //    WalkingLibrary.setXAndYClassName(getClassName(rsHooks.getSetXandYHook()));
    //    WalkingLibrary.setXAndYMethodName(getFieldOrMethodName(rsHooks.getSetXandYHook()));
        //Scene Walking X Gamepack Hook
        WalkingLibrary.setSceneSelectedXClassName(getClassName(rsHooks.getSceneSelectedXFieldHook()));
        WalkingLibrary.setSceneSelectedXFieldName(getFieldOrMethodName(rsHooks.getSceneSelectedXFieldHook()));
        //Scene Walking Y Gamepack Hook
        WalkingLibrary.setSceneSelectedYClassName(getClassName(rsHooks.getSceneSelectedYFieldHook()));
        WalkingLibrary.setSceneSelectedYFieldName(getFieldOrMethodName(rsHooks.getSceneSelectedYFieldHook()));
        //Scene Walking Set Viewport Walking Hook
        WalkingLibrary.setViewportWalkingClassName(getClassName(rsHooks.getSetViewportWalkingFieldHook()));
        WalkingLibrary.setViewportWalkingFieldName(getFieldOrMethodName(rsHooks.getSetViewportWalkingFieldHook()));
        //Scene Walking Check Click Field Hook
        WalkingLibrary.setCheckClickClassName(getClassName(rsHooks.getCheckClickFieldHook()));
        WalkingLibrary.setCheckClickFieldName(getFieldOrMethodName(rsHooks.getCheckClickFieldHook()));
        //Selected Spell Widget Hook
        SpellsLibrary.setSelectedSpellWidgetClassName(getClassName(rsHooks.getSetSelectedSpellWidgetHook()));
        SpellsLibrary.setSelectedSpellWidgetFieldName(getFieldOrMethodName(rsHooks.getSetSelectedSpellWidgetHook()));
        SpellsLibrary.setSelectedSpellWidgetMultiplier(rsHooks.getSetSelectedSpellWidgetMultiplier());
        //Selected Spell Child Hook
        SpellsLibrary.setSelectedSpellChildIndexClassName(getClassName(rsHooks.getSetSelectedSpellChildIndexHook()));
        SpellsLibrary.setSelectedSpellChildIndexFieldName(getFieldOrMethodName(rsHooks.getSetSelectedSpellChildIndexHook()));
        SpellsLibrary.setSelectedSpellChildIndexMultiplier(rsHooks.getSetSelectedSpellChildIndexMultiplier());
        //Selected Spell Item Hook
        SpellsLibrary.setSelectedSpellItemIDClassName(getClassName(rsHooks.getSetSelectedSpellItemIDHook()));
        SpellsLibrary.setSelectedSpellItemIDFieldName(getFieldOrMethodName(rsHooks.getSetSelectedSpellItemIDHook()));
        SpellsLibrary.setSelectedSpellItemIDMultiplier(rsHooks.getSetSelectedSpellItemIDMultiplier());
        //MenuEntry Index Hook
        MenusLibrary.setMenuEntryIndexClassName(getClassName(rsHooks.getMenuEntryIndexFieldHook()));
        MenusLibrary.setMenuEntryIndexFieldName(getFieldOrMethodName(rsHooks.getMenuEntryIndexFieldHook()));
        //MenuEntry Identifiers Hook
        MenusLibrary.setMenuEntryIdentifiersArrayClassName(getClassName(rsHooks.getMenuEntryIdentifiersArrayFieldHook()));
        MenusLibrary.setMenuEntryIdentifiersArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryIdentifiersArrayFieldHook()));
        //MenuEntry Item Ids Hook
        MenusLibrary.setMenuEntryItemIdsArrayClassName(getClassName(rsHooks.getMenuEntryItemIdsArrayFieldHook()));
        MenusLibrary.setMenuEntryItemIdsArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryItemIdsArrayFieldHook()));
        //MenuEntry Options Hook
        MenusLibrary.setMenuEntryOptionsArrayClassName(getClassName(rsHooks.getMenuEntryOptionsArrayFieldHook()));
        MenusLibrary.setMenuEntryOptionsArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryOptionsArrayFieldHook()));
        //MenuEntry Param0 Hook
        MenusLibrary.setMenuEntryParam0ArrayClassName(getClassName(rsHooks.getMenuEntryParam0ArrayFieldHook()));
        MenusLibrary.setMenuEntryParam0ArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryParam0ArrayFieldHook()));
        //MenuEntry Param1 Hook
        MenusLibrary.setMenuEntryParam1ArrayClassName(getClassName(rsHooks.getMenuEntryParam1ArrayFieldHook()));
        MenusLibrary.setMenuEntryParam1ArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryParam1ArrayFieldHook()));
        //MenuEntry Targets Hook
        MenusLibrary.setMenuEntryTargetsArrayClassName(getClassName(rsHooks.getMenuEntryTargetsArrayFieldHook()));
        MenusLibrary.setMenuEntryTargetsArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryTargetsArrayFieldHook()));
        //MenuEntry Types Hook
        MenusLibrary.setMenuEntryTypesArrayClassName(getClassName(rsHooks.getMenuEntryTypesArrayFieldHook()));
        MenusLibrary.setMenuEntryTypesArrayFieldName(getFieldOrMethodName(rsHooks.getMenuEntryTypesArrayFieldHook()));

        hooksLoaded = true;
        
        if (config.clickToLoadHooks())
        {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(client.getCanvas(),
                            "Hooks successfully loaded into the client.", "Kotori Plugin Utils", JOptionPane.INFORMATION_MESSAGE));
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

    @Subscribe
    private void onConfigChanged(ConfigChanged event)
    {
        if (event.getKey().equals("clickToLoadHooks"))
        {
            getHooksJson();
            parseHooksJson();
        }
    }
}
