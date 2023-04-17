package com.theplug.kotori.kotoritest.kotoriutils;

import com.google.gson.Gson;
import com.google.inject.Provides;
import com.theplug.kotori.kotoritest.kotoriutils.gson.Hooks;
import com.theplug.kotori.kotoritest.kotoriutils.libs.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;

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

    private final String hooksFileURL = "https://github.com/OreoCupcakes/kotori-ported-plugins-hosting/blob/master/hooks.json?raw=true";
    @Inject
    private Client client;

    @Inject
    private KotoriUtilsConfig config;

    @Inject
    private ConfigManager configManager;

    @Inject
    private PluginManager pluginManager;

    @Inject
    private EventBus eventBus;

    private Gson gson = new Gson();

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

    private boolean hooksLoaded;

    @Provides
    KotoriUtilsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(KotoriUtilsConfig.class);
    }

    @Override
    protected void startUp()
    {
        getHooksJson();
        parseHooksJson();
    }

    @Override
    protected void shutDown()
    {

    }

    private void getHooksJson()
    {
        if (rsHooks != null)
        {
            return;
        }

        for (int i = 0; i < 16; i++)
        {
            try
            {
                URL url = new URL(hooksFileURL);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                rsHooks = gson.fromJson(bufferedReader, Hooks.class);
                break;
            }
            catch (Exception e)
            {
                log.error("Attempt #" + i + ". Unable to get Hooks.json from URL and parse it. Retrying...", e);

                if (i == 15)
                {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(client.getCanvas(),
                                    "<html>Connection error. Unable to download necessary game hooks information from the Internet." +
                                            "<br>Make sure you are connected to the Internet. Go to Kotori Plugin Utils's configuration and retry" +
                                            "<br>establishing a connection by clicking the \"Click to Load Hooks\" button until hooks loads." +
                                            "<br><div style='color:yellow'><b><u>If you do not load the hooks, the client will crash when using plugins dependent on Kotori Plugin Utils!</u></b></div></html>"
                                    , "Kotori Plugin Utils",JOptionPane.WARNING_MESSAGE));
                    return;
                }
            }
        }
    }

    private void parseHooksJson()
    {
        if (rsHooks == null || hooksLoaded == true)
        {
            return;
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
            if (config.clickToLoadHooks())
            {
                getHooksJson();
                parseHooksJson();

                //Reset check box
                configManager.setConfiguration("kotoriutils","clickToLoadHooks","false");
                try
                {
                    new Thread(() -> eventBus.post(new ProfileChanged())).start();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
