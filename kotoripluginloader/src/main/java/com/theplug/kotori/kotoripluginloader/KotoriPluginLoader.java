package com.theplug.kotori.kotoripluginloader;

import com.google.gson.*;

import javax.inject.Inject;
import javax.swing.*;

import com.google.inject.Provides;
import com.theplug.kotori.kotoripluginloader.json.Info;
import com.theplug.kotori.kotoripluginloader.json.PluginInfo;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.*;
import net.runelite.client.plugins.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@PluginDescriptor(
        name = "Kotori Plugin Loader",
        enabledByDefault = true,
        description = "Plugin loader for Kotori's ported plugins.",
        tags = {"kotori","ported","loader"}
)
@Slf4j
public class KotoriPluginLoader extends net.runelite.client.plugins.Plugin
{
    final private String pluginsJsonURL = "https://github.com/OreoCupcakes/kotori-plugins-releases/blob/master/plugins.json?raw=true";
    final private String infoJsonURL = "https://github.com/OreoCupcakes/kotori-plugins-releases/blob/master/info.json?raw=true";
    final private String currentLoaderVersion = "1.2.0";

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
    private PluginInfo[] pluginsJsonObjects;
    private ArrayList<String> pluginsJsonList = new ArrayList<>();
    private ArrayList<URL> pluginUrlLoadList = new ArrayList<>();
    private ArrayList<String> pluginClassLoadList = new ArrayList<>();
    private ArrayList<String> loadedPlugins = new ArrayList<>();

    private String rlplChoiceAtLoad;

    @Provides
    KotoriPluginLoaderConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(KotoriPluginLoaderConfig.class);
    }

    @Override
    protected void startUp()
    {
        if (config.whenToLoad().getLoadChoice().equals("GAME_STARTUP"))
        {
            new Thread(() ->
            {
                parseInfoJsonFile();
                parsePluginsJsonFile();
                parsePluginsInfo();
                loadPluginsSequence();
                tutorialMessagePopUp();
            }).start();
        }
        else
        {
            parseInfoJsonFile();
            parsePluginsJsonFile();
            parsePluginsInfo();
            if (config.whenToLoad().getLoadChoice().equals("CLIENT_STARTUP"))
            {
                loadPluginsSequence();
            }
            tutorialMessagePopUp();
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

    private boolean outdatedLoaderVersion()
    {
        if (infoJsonObject == null || pluginsJsonList.isEmpty())
        {
            return false;
        }
        String[] pluginVersionSplit = currentLoaderVersion.split("\\.");
        String[] infoJsonVersionSplit = infoJsonObject.getKotoriLoaderVersion().split("\\.");
        String[] githubVersionSplit = pluginsJsonList.get(pluginsJsonList.indexOf("Kotori Plugin Loader")+3).split("\\.");

        //Check local major version number, logic is if local major is greater, then it's not an older version.
        if ((Integer.parseInt(pluginVersionSplit[0]) > Integer.parseInt(infoJsonVersionSplit[0]))
            || (Integer.parseInt(pluginVersionSplit[0]) > Integer.parseInt(githubVersionSplit[0])))
        {
            return false;
        }
        //Check local minor version number, logic is if local minor is greater, then it's not an older version.
        else if ((Integer.parseInt(pluginVersionSplit[1]) > Integer.parseInt(infoJsonVersionSplit[1]))
                    || (Integer.parseInt(pluginVersionSplit[1]) > Integer.parseInt(githubVersionSplit[1])))
        {
            return false;
        }
        //Check local patch version number, logic is if local patch is less, then it is an older version. If equal or greater, then not older version.
        else return (Integer.parseInt(pluginVersionSplit[2]) < Integer.parseInt(infoJsonVersionSplit[2]))
                    && (Integer.parseInt(pluginVersionSplit[2]) < Integer.parseInt(githubVersionSplit[2]));
    }

    private void loaderOutdatedPopUp()
    {
        String loaderOutdatedMsg = "<html>Kotori Plugin Loader is outdated. You are using version " + currentLoaderVersion + "."
                + "<br>Please download version " + pluginsJsonList.get(pluginsJsonList.indexOf("Kotori Plugin Loader")+3)
                + " from https://discord.gg/cuell or https://discord.gg/shismo</html>";

        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(client.getCanvas(),loaderOutdatedMsg,"Kotori Plugin Loader", JOptionPane.WARNING_MESSAGE));
    }

    private void revisionOutdatedPopUp()
    {
        String revisionOutdatedMsg = "<html>Oldschool Runescape has updated its game files." +
                "<br>The detected game revision is: " + client.getRevision() + "." +
                "<br>Some plugins were built for game revision: " + infoJsonObject.getGameRevision() + "." +
                "<br><b><u><div style='color:yellow'>AS SUCH THOSE PLUGINS WILL NOT LOAD UNTIL THEY GET UPDATED!</div></b></u>" + "</html>";

        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(client.getCanvas(),revisionOutdatedMsg,"Kotori Plugin Loader",JOptionPane.WARNING_MESSAGE));
    }

    private void tutorialMessagePopUp()
    {
        if (!config.disableTutorialMsg())
        {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(client.getCanvas()
                            ,"<html>You can choose which plugins to load and when to load them." +
                                    "<br>Please go to Kotori Plugin Loader plugin configuration menu to do so." +
                                    "<br>Disable this message from showing on each startup by clicking \"Disable Tutorial Message\" in the settings.</html>"
                            ,"Kotori Plugin Loader",
                            JOptionPane.INFORMATION_MESSAGE));
        }
    }

    private void pluginsLoadedPopUp()
    {
        if (!config.disablePluginsLoadMsg())
        {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(client.getCanvas(), "Your selected plugins have loaded.",
                            "Kotori Plugin Loader", JOptionPane.INFORMATION_MESSAGE));
        }
    }

    private void loadPluginsSequence()
    {
        buildPluginsLoadList();

        if (outdatedLoaderVersion())
        {
            loaderOutdatedPopUp();
        }
        else
        {
            loadPlugins(pluginUrlLoadList, pluginClassLoadList);
            storeRLPLChoice();
        }
    }

    private void storeRLPLChoice()
    {
        if (rlplChoiceAtLoad == null)
        {
            rlplChoiceAtLoad = "" + config.rlplUser();
        }
    }

    private void parseInfoJsonFile()
    {
        if (infoJsonObject != null)
        {
            return;
        }

        for (int i = 1; i < 16; i++)
        {
            try
            {
                URL infoJsonFile = new URL(infoJsonURL);
                BufferedReader infoReader = new BufferedReader(new InputStreamReader(infoJsonFile.openStream()));
                infoJsonObject = gson.fromJson(infoReader, Info.class);
                break;
            }
            catch (Exception e)
            {
                log.error("Attempt #" + i + ". Unable to get Info.json from URL and parse it. Retrying...", e);

                if (i == 15)
                {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(client.getCanvas(),
                                    "<html>Connection error. Unable to download necessary loader information from the Internet." +
                                            "<br>Make sure you are connected to the Internet. Go to Kotori Plugin Loader's configuration and retry" +
                                            "<br>establishing a connection by clicking the \"Click to Load Plugins\" button until plugins loads.</html>"
                                    , "Kotori Plugin Loader",JOptionPane.WARNING_MESSAGE));
                    return;
                }
            }
        }
    }

    private void parsePluginsJsonFile()
    {
        if (pluginsJsonObjects != null)
        {
            return;
        }

        for (int i = 1; i < 16; i++)
        {
            try
            {
                URL pluginsJsonFile = new URL(pluginsJsonURL);
                BufferedReader pluginsReader = new BufferedReader(new InputStreamReader(pluginsJsonFile.openStream()));
                pluginsJsonObjects = gson.fromJson(pluginsReader, PluginInfo[].class);
                break;
            }
            catch (Exception e)
            {
                log.error("Attempt #" + i + ". Unable to get Plugins.json from URL and parse it. Retrying...", e);

                if (i == 15)
                {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(client.getCanvas(),
                                    "<html>Connection error. Unable to download necessary plugin information from the Internet." +
                                            "<br>Make sure you are connected to the Internet. Go to Kotori Plugin Loader's configuration and retry" +
                                            "<br>establishing a connection by clicking the \"Click to Load Plugins\" button until plugins loads.</html>"
                                    , "Kotori Plugin Loader",JOptionPane.WARNING_MESSAGE));
                    return;
                }
            }
        }
    }

    private void parsePluginsInfo()
    {
        if (pluginsJsonList.isEmpty())
        {
            if (pluginsJsonObjects != null)
            {
                for (PluginInfo plugin : pluginsJsonObjects)
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
        if (infoJsonObject.isMasterPreventLoad())
        {
            return;
        }
        //Plugin Choices That Require KotoriUtils and will break on a game revision update.
        if (checkGameRevision())
        {
            if (!infoJsonObject.isPreventKotoriUtils())
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
                    if (!config.rlplUser())
                    {
                        if (!infoJsonObject.isPreventAlchemicalHydra())
                        {
                            addPluginToLoadLists("Alchemical Hydra");
                        }
                    }
                }
    
                //Load Cerberus
                if (config.cerberusHelperChoice())
                {
                    if (!infoJsonObject.isPreventCerberusHelper())
                    {
                        addPluginToLoadLists("Cerberus Helper");
                    }
                }
    
                //Load God Wars Helper
                if (config.godWarsHelperChoice())
                {
                    if (!infoJsonObject.isPreventGwdHelper())
                    {
                        addPluginToLoadLists("God Wars Helper");
                    }
                }
    
                //Load Demonics
                if (config.demonicGorillasChoice())
                {
                    if (!infoJsonObject.isPreventDemonicGorillas())
                    {
                        addPluginToLoadLists("Demonic Gorillas");
                    }
                }
    
                //Load Gauntlet
                if (config.gauntletExtendedChoice())
                {
                    if (!infoJsonObject.isPreventGauntletExtended())
                    {
                        addPluginToLoadLists("Gauntlet Extended");
                    }
                }
    
                //Load Vorkath depending on client choice by user
                if (config.vorkathOverlayChoice())
                {
                    if (!config.rlplUser())
                    {
                        if (!infoJsonObject.isPreventVorkath())
                        {
                            addPluginToLoadLists("Vorkath");
                        }
                    }
                }
            }
        }
        else
        {
            revisionOutdatedPopUp();
        }
        
        if (config.rlplUser())
        {
            if (config.alchemicalHydraChoice())
            {
                if (!infoJsonObject.isPreventAlchemicalHydraRLPL())
                {
                    addPluginToLoadLists("Alchemical Hydra (RLPL)");
                }
            }
            
            if (config.vorkathOverlayChoice())
            {
                if (!infoJsonObject.isPreventVorkathRLPL())
                {
                    addPluginToLoadLists("Vorkath (RLPL)");
                }
            }
        }

        if (config.dagannothKingsChoice())
        {
            if (!infoJsonObject.isPreventDagannothKings())
            {
                addPluginToLoadLists("Dagannoth Kings");
            }
        }

        if (config.hallowedHelperChoice())
        {
            if (!infoJsonObject.isPreventHallowedHelper())
            {
                addPluginToLoadLists("Hallowed Sepulchre (Deluxe)");
            }
        }

        if (config.hallowedSepulchreChoice())
        {
            if (!infoJsonObject.isPreventHallowedSepulchre())
            {
                addPluginToLoadLists("Hallowed Sepulchre (Lightweight)");
            }
        }

        if (config.houseOverlayChoice())
        {
            if (!infoJsonObject.isPreventHouseOverlay())
            {
                addPluginToLoadLists("House Overlay");
            }
        }

        if (config.multiIndicatorsChoice())
        {
            if (!infoJsonObject.isPreventMultiIndicators())
            {
                addPluginToLoadLists("Multi-Lines Indicators");
            }
        }

        if (config.effectTimersChoice())
        {
            if (!config.multiIndicatorsChoice())
            {
                if (!infoJsonObject.isPreventMultiIndicators())
                {
                    addPluginToLoadLists("Multi-Lines Indicators");
                }
            }
    
            if (!infoJsonObject.isPreventEffectTimers())
            {
                addPluginToLoadLists("Effect Timers");
            }
        }

        if (config.zulrahOverlayChoice())
        {
            if (!infoJsonObject.isPreventZulrah())
            {
                addPluginToLoadLists("Zulrah");
            }
        }

        if (config.grotesqueGuardiansChoice())
        {
            if (!infoJsonObject.isPreventGrotesqueGuardians())
            {
                addPluginToLoadLists("Grotesque Guardians");
            }
        }

        if (config.nexExtendedChoice())
        {
            if (!infoJsonObject.isPreventNex())
            {
                addPluginToLoadLists("Nex Extended");
            }
        }

        if (config.specBarChoice())
        {
            if (!infoJsonObject.isPreventSpecbar())
            {
                addPluginToLoadLists("Spec Bar");
            }
        }

        if (config.templeTrekkingChoice())
        {
            if (!infoJsonObject.isPreventTempleTrekking())
            {
                addPluginToLoadLists("Temple Trekking");
            }
        }

        if (config.tarnsLairChoice())
        {
            if (!infoJsonObject.isPreventTarnsLair())
            {
                addPluginToLoadLists("Tarn's Lair");
            }
        }

        if (config.reorderPrayersChoice())
        {
            if (!infoJsonObject.isPreventReorderPrayers())
            {
                addPluginToLoadLists("Reorder Prayers");
            }
        }

        if (config.nightmareChoice())
        {
            if (!infoJsonObject.isPreventNightmare())
            {
                addPluginToLoadLists("Nightmare of Ashihama");
            }
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

    private void clearBuiltPluginLoadLists()
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
            URLClassLoader kotoriClassLoader = new URLClassLoader(pluginUrls.toArray(URL[]::new),client.getClass().getClassLoader());
            ArrayList<Class<?>> loadedClasses = new ArrayList<>();
            for (String classPath : pluginClassPaths)
            {
                loadedClasses.add(kotoriClassLoader.loadClass(classPath));
            }

            List<Plugin> scannedPlugins = manager.loadPlugins(loadedClasses,null);

            SwingUtilities.invokeLater(() ->
            {
                for (Plugin p : scannedPlugins)
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

            pluginsLoadedPopUp();
        }
        catch (Exception e)
        {
            log.error("Unable to load the plugins.", e);
        }
    }

    private void refreshConfigGui()
    {
        try
        {
            new Thread(() -> eventBus.post(new ProfileChanged())).start();
        }
        catch (Exception e)
        {
            log.error("Failed to refresh plugin configuration GUI via eventBus.", e);
        }
    }

    private void setConfigItem(String key, String value)
    {
        configManager.setConfiguration("kotoripluginloader", key, value);
        refreshConfigGui();
    }

    private void setConfigItems(ArrayList<String> keys, String value)
    {
        for (String key : keys)
        {
            configManager.setConfiguration("kotoripluginloader", key, value);
        }
        refreshConfigGui();
    }

    private void setConfigItems(ArrayList<String> keys, ArrayList<String> values)
    {
        if (keys.size() != values.size())
        {
            return;
        }

        for (String key: keys)
        {
            for (String value : values)
            {
                configManager.setConfiguration("kotoripluginloader", key, value);
            }
        }
        refreshConfigGui();
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event)
    {
        if (!loadedPlugins.isEmpty())
        {
            return;
        }

        if (config.whenToLoad().getLoadChoice().equals("LOGGED_IN"))
        {
            //GameState LOGGED_IN
            if (event.getGameState().getState() == 30)
            {
                loadPluginsSequence();
            }
        }
    }



    @Subscribe
    private void onConfigChanged(ConfigChanged event)
    {
        //Check if independents have loaded
        boolean kotoriUtilsLoaded = loadedPlugins.contains("Kotori Plugin Utils");
        boolean multiIndicatorsLoaded = loadedPlugins.contains("Multi-Lines Indicators");

        //Force turn on plugin dependencies for Effect Timers, else keep it turned off
        if (event.getKey().equals("effectTimersChoice"))
        {
            if (!multiIndicatorsLoaded)
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

        //If selecting Hydra and not RLPL, turn on Kotori Utils if not already on, else if Kotori Utils is already loaded then keep it turned off
        if (event.getKey().equals("alchemicalHydraChoice"))
        {
            if (!config.rlplUser())
            {
                if (!kotoriUtilsLoaded)
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

        //If selecting Cerb, turn on Kotori Utils if not already on, else if Kotori Utils is already loaded then keep it turned off
        if (event.getKey().equals("cerberusHelperChoice"))
        {
            if (!kotoriUtilsLoaded)
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

        //If selecting Demonics, turn on Kotori Utils if not already on, else if Kotori Utils is already loaded then keep it turned off
        if (event.getKey().equals("demonicGorillasChoice"))
        {
            if (!kotoriUtilsLoaded)
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

        //If selecting Gauntlet, turn on Kotori Utils if not already on, else if Kotori Utils is already loaded then keep it turned off
        if (event.getKey().equals("gauntletExtendedChoice"))
        {
            if (!kotoriUtilsLoaded)
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
        
        //If selecting GWD Helper, turn on Kotori Utils if not already on, else if Kotori Utils is already loaded then keep it turned off
        if (event.getKey().equals("godWarsHelperChoice"))
        {
            if (!kotoriUtilsLoaded)
            {
                if (config.godWarsHelperChoice())
                {
                    if (!config.kotoriUtilsChoice())
                    {
                        setConfigItem("kotoriUtilsChoice","true");
                    }
                }
            }
            else
            {
                setConfigItem(event.getKey(), "false");
            }
        }

        //If selecting Vorkath, turn on Kotori Utils if not already on, else if Kotori Utils is already loaded then keep it turned off
        if (event.getKey().equals("vorkathOverlayChoice"))
        {
            if (!config.rlplUser())
            {
                if (!kotoriUtilsLoaded)
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

        //Keep Kotori Utils on until its dependents get turned off
        if (event.getKey().equals("kotoriUtilsChoice"))
        {
            if ((config.alchemicalHydraChoice() && !config.rlplUser()) || config.demonicGorillasChoice()
                    || config.gauntletExtendedChoice() || config.cerberusHelperChoice()
                    || (config.vorkathOverlayChoice() && !config.rlplUser()))
            {
                setConfigItem(event.getKey(),"true");
            }
        }

        //Keep the Multi-Lines Indicators button turned on as long as Effect Timers is also on
        if (event.getKey().equals("multiIndicatorsChoice"))
        {
            if (config.effectTimersChoice())
            {
                setConfigItem(event.getKey(),"true");
            }
        }

        //Keep the RLPL User button "frozen" and prevent it from changing after the first load of plugins
        if (event.getKey().equals("rlplUser"))
        {
            if (!loadedPlugins.isEmpty())
            {
                setConfigItem(event.getKey(),""+rlplChoiceAtLoad);
            }
        }

        //Select All Plugins button
        if (event.getKey().equals("selectAllPluginsChoice"))
        {
            if (config.selectAllPluginsChoice())
            {
                //Check all independent plugins
                ArrayList<String> keys = new ArrayList<>();
                keys.add("dagannothKingsChoice");
                keys.add("hallowedHelperChoice");
                keys.add("hallowedSepulchreChoice");
                keys.add("houseOverlayChoice");
                keys.add("multiIndicatorsChoice");
                keys.add("zulrahOverlayChoice");
                keys.add("grotesqueGuardiansChoice");
                keys.add("nexExtendedChoice");
                keys.add("specBarChoice");
                keys.add("templeTrekkingChoice");
                keys.add("tarnsLairChoice");
                keys.add("reorderPrayersChoice");
                keys.add("nightmareChoice");

                if (config.rlplUser())
                {
                    keys.add("alchemicalHydraChoice");
                    keys.add("vorkathOverlayChoice");
                }

                //Check KotoriUtils and its dependents if it's not loaded already
                if (!kotoriUtilsLoaded)
                {
                    keys.add("kotoriUtilsChoice");
                    keys.add("demonicGorillasChoice");
                    keys.add("gauntletExtendedChoice");
                    keys.add("cerberusHelperChoice");
                    keys.add("godWarsHelperChoice");
                    if (!config.rlplUser())
                    {
                        keys.add("alchemicalHydraChoice");
                        keys.add("vorkathOverlayChoice");
                    }
                }

                //Check Effect Timers if its not loaded already
                if (!multiIndicatorsLoaded)
                {
                    keys.add("effectTimersChoice");
                }

                setConfigItems(keys,"true");
            }
            else
            {
                //Unselect all plugins
                ArrayList<String> keys = new ArrayList<>();
                keys.add("effectTimersChoice");
                keys.add("alchemicalHydraChoice");
                keys.add("vorkathOverlayChoice");
                keys.add("demonicGorillasChoice");
                keys.add("gauntletExtendedChoice");
                keys.add("cerberusHelperChoice");
                keys.add("dagannothKingsChoice");
                keys.add("hallowedHelperChoice");
                keys.add("hallowedSepulchreChoice");
                keys.add("houseOverlayChoice");
                keys.add("zulrahOverlayChoice");
                keys.add("grotesqueGuardiansChoice");
                keys.add("nexExtendedChoice");
                keys.add("godWarsHelperChoice");
                keys.add("specBarChoice");
                keys.add("templeTrekkingChoice");
                keys.add("tarnsLairChoice");
                keys.add("reorderPrayersChoice");
                keys.add("nightmareChoice");
                keys.add("multiIndicatorsChoice");
                keys.add("kotoriUtilsChoice");

                setConfigItems(keys,"false");
            }
        }

        //Keep at the bottom
        if (event.getKey().equals("manualLoad"))
        {
            if (config.manualLoad())
            {
                clearBuiltPluginLoadLists();
                //Get necessary json files manually if people have connection errors when auto-loading
                parseInfoJsonFile();
                parsePluginsJsonFile();
                parsePluginsInfo();
                //Rebuild Load List
                loadPluginsSequence();
                setConfigItem("manualLoad", "false");
            }
        }
    }
}