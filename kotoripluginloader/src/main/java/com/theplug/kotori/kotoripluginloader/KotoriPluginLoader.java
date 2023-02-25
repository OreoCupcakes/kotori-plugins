package com.theplug.kotori.kotoripluginloader;

import com.google.gson.*;

import javax.inject.Inject;
import javax.swing.*;

import com.google.inject.Provides;
import com.theplug.kotori.kotoripluginloader.json.Info;
import com.theplug.kotori.kotoripluginloader.json.Plugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ExternalPluginsChanged;
import net.runelite.client.events.ProfileChanged;
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
    final private String pluginsJsonURL = "https://github.com/OreoCupcakes/kotori-plugins-releases/blob/master/plugins.json?raw=true";
    final private String infoJsonURL = "https://github.com/OreoCupcakes/kotori-plugins-releases/blob/master/info.json?raw=true";
    final private String currentLoaderVersion = "0.5.0";

    @Inject
    private Client client;
    @Inject
    private KotoriPluginLoaderConfig config;
    @Inject
    private PluginManager manager;
    @Inject
    private EventBus eventBus;
    @Inject
    private ConfigManager configManager;

    private Gson gson = new Gson();
    private Info infoJsonObject;
    private Plugin[] pluginsJsonObjects;
    private ArrayList<String> pluginsJsonList = new ArrayList<>();
    private ArrayList<URL> pluginUrlLoadList = new ArrayList<>();
    private ArrayList<String> pluginClassLoadList = new ArrayList<>();
    private ArrayList<String> loadedPlugins = new ArrayList<>();

    @Provides
    KotoriPluginLoaderConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(KotoriPluginLoaderConfig.class);
    }

    @Override
    protected void startUp()
    {
        parseInfoJsonFile();
        parsePluginsJsonFile();
        parsePluginsInfo();
        buildPluginsLoadList();
        if (config.whenToLoad().getLoadChoice().equals("STARTING"))
        {
            if (!checkLoaderVersion())
            {
                loaderOutdatedPopUp();
            }
            else
            {
                loadPlugins(pluginUrlLoadList, pluginClassLoadList);
            }
        }
    }

    @Override
    protected void shutDown()
    {

    }

    private boolean checkGameRevision()
    {
        if (infoJsonObject == null)
        {
            return false;
        }
        if (client.getRevision() == infoJsonObject.getGameRevision())
        {
            return true;
        }
        return false;
    }

    private boolean checkLoaderVersion()
    {
        if (infoJsonObject == null || pluginsJsonList.isEmpty())
        {
            return false;
        }
        String loaderVersionOnGithub = pluginsJsonList.get(pluginsJsonList.indexOf("Kotori Plugin Loader")+3);
        String loaderVersionOnInfoJson = infoJsonObject.getKotoriLoaderVersion();
        if (loaderVersionOnInfoJson.equals(currentLoaderVersion) && loaderVersionOnGithub.equals(currentLoaderVersion))
        {
            return true;
        }
        return false;
    }

    private void loaderOutdatedPopUp()
    {
        String loaderOutdatedMsg = "Kotori Plugin Loader is outdated. Please download the new release, version "
                + pluginsJsonList.get(pluginsJsonList.indexOf("Kotori Plugin Loader")+3) + ", from Discord.";
        String messageTitle = "Kotori Plugin Loader - Outdated Version";
        JOptionPane.showMessageDialog(client.getCanvas(),loaderOutdatedMsg,messageTitle,JOptionPane.WARNING_MESSAGE);
    }

    private void parseInfoJsonFile()
    {
        try
        {
            if (infoJsonObject == null)
            {
                URL infoJsonFile = new URL(infoJsonURL);
                BufferedReader infoReader = new BufferedReader(new InputStreamReader(infoJsonFile.openStream()));
                infoJsonObject = gson.fromJson(infoReader, Info.class);
            }
        }
        catch (Exception e)
        {
            log.error("Unable to get Info.json from URL and parse it.", e);
        }
    }

    private void parsePluginsJsonFile()
    {
        try
        {
            if (pluginsJsonObjects == null)
            {
                URL pluginsJsonFile = new URL(pluginsJsonURL);
                BufferedReader pluginsReader = new BufferedReader(new InputStreamReader(pluginsJsonFile.openStream()));
                pluginsJsonObjects = gson.fromJson(pluginsReader, Plugin[].class);
            }
        }
        catch (Exception e)
        {
            log.error("Unable to get Plugins.json from URL and parse it.", e);
        }
    }

    private void parsePluginsInfo()
    {
        if (pluginsJsonList.isEmpty())
        {
            if (pluginsJsonObjects != null)
            {
                for (Plugin plugin : pluginsJsonObjects)
                {
                    //0 = name, 1 = package, 2 = class, 3 = version, 4 = url
                    pluginsJsonList.add(plugin.getName());
                    pluginsJsonList.add(plugin.getPackageId());
                    pluginsJsonList.add(plugin.getMainClassName());
                    pluginsJsonList.add(plugin.getReleases().get(plugin.getReleases().size() - 1).getVersion());
                    pluginsJsonList.add(plugin.getReleases().get(plugin.getReleases().size() - 1).getUrl().toString());
                }
            }
        }
    }

    private void buildPluginsLoadList()
    {
        //Plugin Choices That Require KotoriUtils and will break on a game revision update.
        if (checkGameRevision())
        {
            //load Plugin Utils if one of the following is selected.
            if (config.kotoriUtilsChoice() || (config.alchemicalHydraChoice() && !config.rlplUser()) ||
                    config.cerberusHelperChoice() || config.demonicGorillasChoice() ||
                    config.gauntletExtendedChoice() || (config.vorkathOverlayChoice() && !config.rlplUser()))
            {
                addPluginToLoadLists("Kotori Plugin Utils");
            }

            //Load Alch Hydra depending on client choice by user
            if (config.alchemicalHydraChoice())
            {
                if (config.rlplUser())
                {
                    addPluginToLoadLists("Alchemical Hydra (RLPL)");
                }
                else
                {
                    addPluginToLoadLists("Alchemical Hydra");
                }
            }

            //Load Cerberus
            if (config.cerberusHelperChoice())
            {
                addPluginToLoadLists("Cerberus Helper");
            }

            //Load Demonics
            if (config.demonicGorillasChoice())
            {
                addPluginToLoadLists("Demonic Gorillas");
            }

            //Load Gauntlet
            if (config.gauntletExtendedChoice())
            {
                addPluginToLoadLists("Gauntlet Extended");
            }

            //Load Vorkath depending on client choice by user
            if (config.vorkathOverlayChoice())
            {
                if (config.rlplUser())
                {
                    addPluginToLoadLists("Vorkath (RLPL)");
                }
                else
                {
                    addPluginToLoadLists("Vorkath");
                }
            }
        }
        else
        {
            String revisionOutdatedMsg = "<html>Oldschool Runescape has updated its game files." +
                    "<br>The detected game revision is: " + client.getRevision() + "." +
                    "<br>Some plugins were built for game revision: " + infoJsonObject.getGameRevision() + "." +
                    "<br><b><u>AS SUCH THOSE PLUGINS WILL NOT LOAD UNTIL THEY GET UPDATED!</b></u>" + "</html>";
            String messageDialogTitle = "Kotori Plugin Loader";
            JOptionPane.showMessageDialog(client.getCanvas(), revisionOutdatedMsg,messageDialogTitle,JOptionPane.WARNING_MESSAGE);
        }

        if (config.dagannothKingsChoice())
        {
            addPluginToLoadLists("Dagannoth Kings");
        }

        if (config.hallowedHelperChoice())
        {
            addPluginToLoadLists("Hallowed Sepulchre (Deluxe)");
        }

        if (config.hallowedSepulchreChoice())
        {
            addPluginToLoadLists("Hallowed Sepulchre (Lightweight)");
        }

        if (config.houseOverlayChoice())
        {
            addPluginToLoadLists("House Overlay");
        }

        if (config.multiIndicatorsChoice())
        {
            addPluginToLoadLists("Multi-Lines Indicators");
        }

        if (config.effectTimersChoice())
        {
            if (config.multiIndicatorsChoice())
            {
                addPluginToLoadLists("Effect Timers");
            }
            else
            {
                addPluginToLoadLists("Multi-Lines Indicators");
                addPluginToLoadLists("Effect Timers");
            }
        }

        if (config.zulrahOverlayChoice())
        {
            addPluginToLoadLists("Zulrah");
        }
    }

    private URL getPluginUrl(String pluginName)
    {
        try
        {
            int pluginNameIndex = pluginsJsonList.indexOf(pluginName);
            int pluginUrlIndex = pluginNameIndex + 4;
            return new URL(pluginsJsonList.get(pluginUrlIndex));
        }
        catch (Exception e)
        {
            log.error("Unable to find plugin URL.", e);
            return null;
        }
    }

    private void addPluginToLoadLists(String pluginName)
    {
        if (loadedPlugins.contains(pluginName))
        {
            return;
        }

        URL pluginUrl = getPluginUrl(pluginName);
        if (!pluginUrlLoadList.contains(pluginUrl))
        {
            pluginUrlLoadList.add(pluginUrl);
        }

        int pluginNameIndex = pluginsJsonList.indexOf(pluginName);
        int pluginArtifactIndex = pluginNameIndex + 1;
        int pluginMainClassIndex = pluginNameIndex + 2;
        String fullMainClassPath = "com.theplug.kotori." + pluginsJsonList.get(pluginArtifactIndex) + "." +
                pluginsJsonList.get(pluginMainClassIndex);

        if (!pluginClassLoadList.contains(fullMainClassPath))
        {
            pluginClassLoadList.add(fullMainClassPath);
        }
    }

    private void clearPluginLoadLists()
    {
        pluginUrlLoadList.clear();
        pluginClassLoadList.clear();
    }

    private void loadPlugins(ArrayList<URL> pluginUrls, ArrayList<String> pluginClassPaths)
    {
        if (pluginUrls.isEmpty() || pluginClassPaths.isEmpty())
        {
            return;
        }

        try
        {
            URLClassLoader urlClassLoader = new URLClassLoader(pluginUrls.toArray(URL[]::new));

            ArrayList<Class<?>> loadedClasses = new ArrayList<>();
            for (String classPath : pluginClassPaths)
            {
                loadedClasses.add(urlClassLoader.loadClass(classPath));
            }

            List<net.runelite.client.plugins.Plugin> scannedPlugins = manager.loadPlugins(loadedClasses,null);

            SwingUtilities.invokeLater(() ->
            {
                for (net.runelite.client.plugins.Plugin p : scannedPlugins)
                {
                    if (p == null)
                    {
                        continue;
                    }
                    try
                    {
                        // Check to see if external plugin is already loaded into client, if not then load it and store its name
                        manager.startPlugin(p);
                        loadedPlugins.add(p.getName());
                    }
                    catch (PluginInstantiationException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            eventBus.post(new ExternalPluginsChanged(new ArrayList<>()));

            JOptionPane.showMessageDialog(client.getCanvas(),"Your selected plugins have loaded.", "Kotori Plugin Loader",JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e)
        {
            log.error("Unable to load the plugins.", e);
        }
    }

    private boolean checkPluginAlreadyLoaded(String pluginName)
    {
        return loadedPlugins.contains(pluginName);
    }

    private void setConfigItem(String key, String value)
    {
        configManager.setConfiguration("kotoripluginloader",key,value);
        eventBus.post(new ProfileChanged());
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event)
    {
        //Force turn on plugin dependencies
        if (event.getKey().equals("effectTimersChoice"))
        {
            if (loadedPlugins.isEmpty())
            {
                if (config.effectTimersChoice())
                {
                    if (!config.multiIndicatorsChoice())
                    {
                        setConfigItem("multiIndicatorsChoice", "true");
                    }
                }
            }
            else
            {
                setConfigItem(event.getKey(),"false");
            }
        }

        if (event.getKey().equals("alchemicalHydraChoice"))
        {
            if (!config.rlplUser())
            {
                if (loadedPlugins.isEmpty())
                {
                    if (config.alchemicalHydraChoice())
                    {
                        if (!config.kotoriUtilsChoice())
                        {
                            setConfigItem("kotoriUtilsChoice", "true");
                        }
                    }
                }
                else
                {
                    setConfigItem(event.getKey(),"false");
                }
            }
        }

        if (event.getKey().equals("cerberusHelperChoice"))
        {
            if (loadedPlugins.isEmpty())
            {
                if (config.cerberusHelperChoice())
                {
                    if (!config.kotoriUtilsChoice())
                    {
                        setConfigItem("kotoriUtilsChoice", "true");
                    }
                }
            }
            else
            {
                setConfigItem(event.getKey(),"false");
            }
        }

        if (event.getKey().equals("demonicGorillasChoice"))
        {
            if (loadedPlugins.isEmpty())
            {
                if (config.demonicGorillasChoice())
                {
                    if (!config.kotoriUtilsChoice())
                    {
                        setConfigItem("kotoriUtilsChoice","true");
                    }
                }
            }
            else
            {
                setConfigItem(event.getKey(),"false");
            }
        }

        if (event.getKey().equals("gauntletExtendedChoice"))
        {
            if (loadedPlugins.isEmpty())
            {
                if (config.gauntletExtendedChoice())
                {
                    if (!config.kotoriUtilsChoice())
                    {
                        setConfigItem("kotoriUtilsChoice","true");
                    }
                }
            }
            else
            {
                setConfigItem(event.getKey(),"false");
            }
        }

        if (event.getKey().equals("vorkathOverlayChoice"))
        {
            if (!config.rlplUser())
            {
                if (loadedPlugins.isEmpty())
                {
                    if (config.vorkathOverlayChoice())
                    {
                        if (!config.kotoriUtilsChoice())
                        {
                            setConfigItem("kotoriUtilsChoice","true");
                        }
                    }
                }
                else
                {
                    setConfigItem(event.getKey(),"false");
                }
            }
        }

        if (event.getKey().equals("kotoriUtilsChoice"))
        {
            if ((config.alchemicalHydraChoice() && !config.rlplUser()) || config.demonicGorillasChoice()
                    || config.gauntletExtendedChoice() || config.cerberusHelperChoice()
                    || (config.vorkathOverlayChoice() && !config.rlplUser()))
            {
                setConfigItem(event.getKey(),"true");
            }
        }

        if (event.getKey().equals("multiIndicatorsChoice"))
        {
            if (config.effectTimersChoice())
            {
                setConfigItem(event.getKey(),"true");
            }
        }

        if (event.getKey().equals("rlplUser"))
        {
            if (!loadedPlugins.isEmpty())
            {
                setConfigItem(event.getKey(),event.getOldValue());
            }
        }

        //Rebuild Load List
        clearPluginLoadLists();
        buildPluginsLoadList();

        //Keep at the bottom
        if (config.whenToLoad().getLoadChoice().equals("MANUALLY"))
        {
            if (config.manualLoad())
            {
                if (!checkLoaderVersion())
                {
                    loaderOutdatedPopUp();
                }
                else
                {
                    loadPlugins(pluginUrlLoadList, pluginClassLoadList);
                }
                setConfigItem("manualLoad", "false");
            }
        }
    }
}