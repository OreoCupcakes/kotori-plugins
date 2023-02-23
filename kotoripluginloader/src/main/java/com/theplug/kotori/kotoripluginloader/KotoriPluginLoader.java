package com.theplug.kotori.kotoripluginloader;

import com.google.gson.*;

import javax.inject.Inject;
import javax.swing.*;

import com.google.inject.Provides;
import com.theplug.kotori.kotoripluginloader.json.Plugin;
import com.theplug.kotori.kotoripluginloader.json.Releases;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ExternalPluginsChanged;
import net.runelite.client.plugins.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@PluginDescriptor(
        name = "Kotori Plugin Loader",
        enabledByDefault = false,
        description = "Plugin loader for Kotori's ported plugins.",
        tags = {"kotori","ported","loader"}
)
@Slf4j
public class KotoriPluginLoader extends net.runelite.client.plugins.Plugin
{
    final private String pluginsJson = "https://github.com/OreoCupcakes/kotori-ported-plugins-hosting/blob/master/plugins.json?raw=true";
    final private String infoJson = "https://github.com/OreoCupcakes/kotori-ported-plugins-hosting/blob/master/info.json?raw=true";

    @Inject
    private Client client;

    @Inject
    private KotoriPluginLoaderConfig config;

    @Inject
    private PluginManager manager;

    @Inject
    private EventBus eventBus;

    private int gameRevisionFromHooks;
    private boolean gameRevisionCheck;
    private Plugin[] jsonPlugins;
    private ArrayList<URL> pluginUrlList = new ArrayList<>();
    private ArrayList<String> pluginPackageIdList = new ArrayList<>();
    private ArrayList<String> pluginMainClassList = new ArrayList<>();
    private List<net.runelite.client.plugins.Plugin> scannedPlugins;

    @Provides
    KotoriPluginLoaderConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(KotoriPluginLoaderConfig.class);
    }

    @Override
    protected void startUp()
    {

    }

    @Override
    protected void shutDown()
    {

    }

    private boolean checkGameRevision()
    {
        try
        {
            URL hooksURL = new URL(infoJson);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(hooksURL.openStream()));
            String lineToParse;
            while ((lineToParse = bufferedReader.readLine()) != null)
            {
                String[] splitLineArray = lineToParse.replaceAll("[^[a-zA-Z0-9|\\-|\\:|\\.]]","").split(":|\\.");

                if (splitLineArray[0].contains("rsversion"))
                {
                    gameRevisionFromHooks = Integer.parseInt(splitLineArray[1]);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            log.error("Failed at getting RS revision from hooks file.", e);
            return false;
        }

        if (client.getRevision() == gameRevisionFromHooks)
        {
            return true;
        }

        return false;
    }

    private boolean parsePluginsJson()
    {
        try
        {
            URL pluginsURLs = new URL(pluginsJson);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pluginsURLs.openStream()));
            Gson gson = new Gson();
            jsonPlugins = gson.fromJson(bufferedReader, Plugin[].class);
        }
        catch (Exception e)
        {
            log.error("Unable to parse plugins.json", e);
            return false;
        }

        if (jsonPlugins != null)
        {
            for (Plugin json : jsonPlugins)
            {
                pluginPackageIdList.add(json.getPackageId());
                pluginMainClassList.add(json.getMainClassName());

                List<Releases> releasesList = json.getReleases();
                int latestReleaseIndex = releasesList.size() - 1;
                pluginUrlList.add(releasesList.get(latestReleaseIndex).getUrl());
            }
            return true;
        }

        return false;
    }

    private void loadExternalPlugins()
    {
        try
        {
            URL[] urlArray = pluginUrlList.toArray(URL[]::new);
            URLClassLoader urlClassLoader = new URLClassLoader(urlArray);
            ArrayList<Class<?>> loadedClasses = new ArrayList<>();
            loadedClasses.add(urlClassLoader.loadClass("com.theplug.kotori.gauntletextended.GauntletExtendedPlugin"));
            loadedClasses.add(urlClassLoader.loadClass("com.theplug.kotori.alchemicalhydra.AlchemicalHydraPlugin"));
            loadedClasses.add(urlClassLoader.loadClass("com.theplug.kotori.cerberushelper.CerberusPlugin"));
            loadedClasses.add(urlClassLoader.loadClass("com.theplug.kotori.vorkathoverlay.VorkathPlugin"));
            loadedClasses.add(urlClassLoader.loadClass("com.theplug.kotori.demonicgorillas.DemonicGorillaPlugin"));
            scannedPlugins = manager.loadPlugins(loadedClasses,null);

            SwingUtilities.invokeLater(() -> {
                for (net.runelite.client.plugins.Plugin p : scannedPlugins) {
                    if (p ==null)
                        continue;
                    try {
                        manager.startPlugin(p);
                    } catch (PluginInstantiationException e) {
                        e.printStackTrace();
                    }
                }
            });
            eventBus.post(new ExternalPluginsChanged(new ArrayList<>()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event)
    {

    }
}