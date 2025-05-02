package com.theplug.kotori.kotoripluginloader;

import com.google.gson.Gson;
import com.google.inject.Provides;
import com.theplug.kotori.kotoripluginloader.json.Info;
import com.theplug.kotori.kotoripluginloader.json.PluginInfo;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.ExternalPluginsChanged;
import net.runelite.client.events.ProfileChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.externalplugins.ExternalPluginManager;

import javax.inject.Inject;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@PluginDescriptor(
		name = "<html><font color=#6b8af6>Kotori</font> Plugin Loader</html>",
		enabledByDefault = true,
		description = "Plugin loader for plugins maintained by Kotori.",
		tags = {"kotori","ported","loader"}
)
@Slf4j
public class KotoriPluginLoader extends Plugin
{
	final private String pluginsJsonURL = "https://raw.githubusercontent.com/OreoCupcakes/kotori-plugins-releases/master/plugins.json";
	final private String infoJsonURL = "https://raw.githubusercontent.com/OreoCupcakes/kotori-plugins-releases/master/info.json";
	final private String currentLoaderVersion = "3.0.1";
	
	@Inject
	private Client client;
	@Inject
	private KotoriPluginLoaderConfig config;
	@Inject
	private PluginManager manager;
	@Inject
	private ExternalPluginManager externalManager;
	@Inject
	private EventBus eventBus;
	@Inject
	private ConfigManager configManager;
	
	private Gson gson;
	private Info infoJsonObject;
	private PluginInfo[] pluginInfoJsonObject;
	private ArrayList<String> pluginInfoList;
	private ArrayList<String> loadedPluginClassPaths;
	private List<Plugin> installedPlugins;
	private URLClassLoader kotoriClassLoader;
	
	//Popup Message Strings
	private final String urlDownloadErrorMessage =
			"<html>Connection error. Unable to download necessary loader information from the Internet." +
			"<br>Make sure you are connected to the Internet. If you are using a proxy or VPN, then you" +
			"<br>are likely getting blocked due to your IP being deemed suspicious. Turn it off or get a" +
			"<br>better proxy or VPN and try again.";
	private final String jsonParseErrorMessage = "JSON parsing error. Please contact Kotori and call him a dumbass for forgetting a comma.";
	private final String pluginsLoadedMessage = "The selected plugins have loaded.";
	private final String tutorialMessage =
			"<html>Select the plugins you wish to load within Kotori Plugin Loader's configuration menu." +
			"<br>Disable this message by selecting \"Disable Tutorial Message\" within the settings.</html>";
	
	@Provides
	KotoriPluginLoaderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(KotoriPluginLoaderConfig.class);
	}
	
	@Override
	protected void startUp()
	{
		gson = new Gson();
		pluginInfoList = new ArrayList<>();
		loadedPluginClassPaths = new ArrayList<>();
		installedPlugins = new ArrayList<>();
		if (config.autoLoadPlugins())
		{
			init();
		}
	}
	
	@Override
	protected void shutDown()
	{
		uninstallPlugins();
		gson = null;
		infoJsonObject = null;
		pluginInfoJsonObject = null;
		pluginInfoList = null;
		loadedPluginClassPaths = null;
		installedPlugins = null;
		kotoriClassLoader = null;
	}
	
	private void init()
	{
		new Thread(() ->
		{
			parseJsonFile(infoJsonURL, infoJsonObject);
			parseJsonFile(pluginsJsonURL, pluginInfoJsonObject);
			parsePluginInfo();
			createKotoriClassLoader();
			if (outdatedLoaderVersion())
			{
				loaderOutdatedPopUp();
			}
			else
			{
				if (!config.disableTutorialMsg())
				{
					createMessagePopUp(tutorialMessage);
				}
				loadPlugins(buildPluginsToLoadList());
			}
		}).start();
	}
	
	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
		if (event.getKey().equals("manualLoad"))
		{
			if (config.manualLoad())
			{
				init();
				setConfigItem("manualLoad", "false");
			}
		}
		
		if (event.getKey().equals("selectAllPluginsChoice"))
		{
			ArrayList<String> keys = new ArrayList<>();
			
			keys.add("alchemicalHelperChoice");
			keys.add("cerberusHelperChoice");
			keys.add("dagannothHelperChoice");
			keys.add("demonicGorillasChoice");
			keys.add("effectTimersChoice");
			keys.add("gauntletHelperChoice");
			keys.add("godWarsHelperChoice");
			keys.add("grotesqueGuardiansChoice");
			keys.add("hallowedHelperChoice");
			keys.add("houseOverlayChoice");
			keys.add("multiIndicatorsChoice");
			keys.add("nexExtendedChoice");
			keys.add("nightmareChoice");
			keys.add("reorderPrayersChoice");
			keys.add("specBarChoice");
			keys.add("tarnsLairChoice");
			keys.add("templeTrekkingChoice");
			keys.add("vorkathOverlayChoice");
			keys.add("zulrahOverlayChoice");
			keys.add("aoeWarningsChoice");
			keys.add("fightCavesChoice");
			keys.add("infernoChoice");
			keys.add("sireHelperChoice");
			
			if (config.selectAllPluginsChoice())
			{
				setConfigItems(keys, "true");
			}
			else
			{
				setConfigItems(keys, "false");
			}
		}
	}
	
	private void parseJsonFile(String url, Object objectToSet)
	{
		if (objectToSet != null)
		{
			return;
		}
		
		BufferedReader reader = null;
		
		for (int i = 1; i < 6; i++)
		{
			try
			{
				URL urlJsonFile = new URL(url);
				reader = new BufferedReader(new InputStreamReader(urlJsonFile.openStream()));
				break;
			}
			catch (Exception e)
			{
				log.error("Attempt #" + i + ". Unable to download JSON file from URL. Retrying...", e);
			}
		}
		
		if (reader == null)
		{
			createMessagePopUp(urlDownloadErrorMessage);
			return;
		}
		
		try
		{
			switch (url)
			{
				case pluginsJsonURL:
					pluginInfoJsonObject = gson.fromJson(reader, PluginInfo[].class);
					break;
				case infoJsonURL:
					infoJsonObject = gson.fromJson(reader, Info.class);
					break;
			}
		}
		catch (Exception e)
		{
			log.error("Unable to convert JSON file into a Java object.", e);
			createMessagePopUp(jsonParseErrorMessage);
		}
		
		try
		{
			reader.close();
		}
		catch (Exception e)
		{
			log.error("Unable to close BufferedReader stream.", e);
		}
	}
	
	private void parsePluginInfo()
	{
		if (pluginInfoJsonObject == null || !pluginInfoList.isEmpty())
		{
			return;
		}
		
		for (PluginInfo plugin : pluginInfoJsonObject)
		{
			//0 = name, 1 = package, 2 = class, 3 = version, 4 = url
			pluginInfoList.add(plugin.getName());
			pluginInfoList.add(plugin.getPackageId());
			pluginInfoList.add(plugin.getMainClassName());
			pluginInfoList.add(plugin.getReleases().get(plugin.getReleases().size() - 1).getVersion());
			pluginInfoList.add(plugin.getReleases().get(plugin.getReleases().size() - 1).getUrl().toString());
		}
	}
	
	private void createKotoriClassLoader()
	{
		if (kotoriClassLoader != null || pluginInfoList == null)
		{
			return;
		}
		
		try
		{
			ArrayList<URL> pluginUrlsList = new ArrayList<>();
			for (int i = 4; i < pluginInfoList.size(); i+=5)
			{
				URL pluginUrl = new URL(pluginInfoList.get(i));
				if (!pluginUrlsList.contains(pluginUrl))
				{
					pluginUrlsList.add(pluginUrl);
				}
			}
			kotoriClassLoader = new URLClassLoader(pluginUrlsList.toArray(URL[]::new), client.getClass().getClassLoader());
		}
		catch (Exception e)
		{
			log.error("Unable to create plugin ClassLoader.", e);
		}
	}
	
	private String getPluginClassPath(String pluginName, int nameVariation)
	{
		String truePluginName = null;
		switch (nameVariation)
		{
			case 1:
				truePluginName = "<html><font color=#6b8af6>[P]</font> " + pluginName + "</html>";
				break;
			case 2:
				truePluginName = "<html><font color=#6b8af6>[K]</font> " + pluginName + "</html>";
				break;
			case 3:
				truePluginName = "<html><font color=#6b8af6>Kotori</font> " + pluginName + "</html>";
				break;
			default:
				truePluginName = pluginName;
		}
		if (truePluginName == null)
		{
			return null;
		}
		int pluginNameIndex = pluginInfoList.indexOf(truePluginName);
		if (pluginNameIndex == -1)
		{
			return null;
		}
		int pluginPackageNameIndex = pluginNameIndex + 1;
		int pluginMainClassNameIndex = pluginNameIndex + 2;
		return "com.theplug.kotori." + pluginInfoList.get(pluginPackageNameIndex) + "." + pluginInfoList.get(pluginMainClassNameIndex);
	}
	
	private void loadPlugins(ArrayList<String> pluginClassPaths)
	{
		if (pluginClassPaths == null || pluginClassPaths.isEmpty())
		{
			return;
		}
		
		try
		{
			ArrayList<Class<?>> classesToLoad = new ArrayList<>();
			for (String classPath : pluginClassPaths)
			{
				if (!loadedPluginClassPaths.contains(classPath))
				{
					classesToLoad.add(kotoriClassLoader.loadClass(classPath));
					loadedPluginClassPaths.add(classPath);
				}
			}
			
			List<Plugin> scannedPlugins = manager.loadPlugins(classesToLoad, null);
			
			//Mark down installed plugins in reverse order (so you can uninstall plugins later on)
			for (int i = scannedPlugins.size() - 1; i >= 0; i--)
			{
				if (!installedPlugins.contains(scannedPlugins.get(i)))
				{
					installedPlugins.add(scannedPlugins.get(i));
				}
			}

            manager.loadDefaultPluginConfiguration(scannedPlugins); 
			
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
						manager.startPlugin(p);
					}
					catch (Exception e)
					{
						log.error("Unable to start plugin: " + p.getName(), e);
					}
				}
			});
			eventBus.post(new ExternalPluginsChanged());
			externalManager.update();
			
			
			if (!config.disablePluginsLoadMsg())
			{
				createMessagePopUp(pluginsLoadedMessage);
			}
		}
		catch (Exception e)
		{
			log.error("Unable to load the plugins.", e);
		}
	}
	
	private void uninstallPlugins()
	{
		if (installedPlugins.isEmpty())
		{
			return;
		}
		
		for (Plugin p : installedPlugins)
		{
			try
			{
				manager.stopPlugin(p);
				manager.remove(p);
			}
			catch (Exception e)
			{
				log.error("Unable to stop and uninstall plugin: " + p.getName());
			}
		}
		eventBus.post(new ExternalPluginsChanged());
	}
	
	//Load Plugins
	private ArrayList<String> buildPluginsToLoadList()
	{
		if (infoJsonObject == null || infoJsonObject.isPreventMasterLoad())
		{
			log.error("Plugins stopped loading due to master stop flag being true.");
			return null;
		}
		
		if (outdatedGameRevision())
		{
			revisionOutdatedPopUp();
			return null;
		}
		
		if (outdatedRuneliteVersion())
		{
			runeliteOutdatedPopup();
		}
		
		ArrayList<String> pluginClassPathsToLoad = new ArrayList<>();
		
		//Load Kotori Plugin Utils always
		addToPluginToLoadList(pluginClassPathsToLoad, true, infoJsonObject.isPreventKotoriUtils(), "Plugin Utils", 3);
		//Load Alch Helper
		addToPluginToLoadList(pluginClassPathsToLoad, config.alchemicalHelperChoice(), infoJsonObject.isPreventAlchemicalHelper(), "Alchemical Helper", 2);
		//Load Cerberus Helper
		addToPluginToLoadList(pluginClassPathsToLoad, config.cerberusHelperChoice(), infoJsonObject.isPreventCerberusHelper(), "Cerberus Helper", 2);
		//Load Dagannoth Kings
		addToPluginToLoadList(pluginClassPathsToLoad, config.dagannothHelperChoice(), infoJsonObject.isPreventDagannothHelper(), "Dagannoth Helper", 2);
		//Load Demonic Gorillas
		addToPluginToLoadList(pluginClassPathsToLoad, config.demonicGorillasChoice(), infoJsonObject.isPreventDemonicGorillas(), "Demonic Gorillas", 1);
		//Load Effect Timers
		addToPluginToLoadList(pluginClassPathsToLoad, config.effectTimersChoice(), infoJsonObject.isPreventEffectTimers(), "Effect Timers", 1);
		//Load Gauntlet Extended
		addToPluginToLoadList(pluginClassPathsToLoad, config.gauntletHelperChoice(), infoJsonObject.isPreventGauntletHelper(), "Gauntlet Helper", 2);
		//Load Grotesque Guardians
		addToPluginToLoadList(pluginClassPathsToLoad, config.grotesqueGuardiansChoice(), infoJsonObject.isPreventGrotesqueGuardians(), "Grotesque Helper", 2);
		//Load God Wars Helper
		addToPluginToLoadList(pluginClassPathsToLoad, config.godWarsHelperChoice(), infoJsonObject.isPreventGwdHelper(), "God Wars Helper", 2);
		//Load Hallowed Helper
		addToPluginToLoadList(pluginClassPathsToLoad, config.hallowedHelperChoice(), infoJsonObject.isPreventHallowedHelper(), "Hallowed Sepulchre", 1);
		//Load House Overlay
		addToPluginToLoadList(pluginClassPathsToLoad, config.houseOverlayChoice(), infoJsonObject.isPreventHouseOverlay(), "House Overlay", 1);
		//Load Multi-Indicators
		addToPluginToLoadList(pluginClassPathsToLoad, config.multiIndicatorsChoice(), infoJsonObject.isPreventMultiIndicators(), "Multi Indicators", 1);
		//Load Nex
		addToPluginToLoadList(pluginClassPathsToLoad, config.nexExtendedChoice(), infoJsonObject.isPreventNex(), "Nex Extended", 1);
		//Load Nightmare
		addToPluginToLoadList(pluginClassPathsToLoad, config.nightmareChoice(), infoJsonObject.isPreventNightmare(), "Nightmare", 1);
		//Load Specbar
		addToPluginToLoadList(pluginClassPathsToLoad, config.specBarChoice(), infoJsonObject.isPreventSpecbar(), "Spec Bar", 1);
		//Load Tarn's Lair
		addToPluginToLoadList(pluginClassPathsToLoad, config.tarnsLairChoice(), infoJsonObject.isPreventTarnsLair(), "Tarn's Lair", 1);
		//Load Temple Trekking
		addToPluginToLoadList(pluginClassPathsToLoad, config.templeTrekkingChoice(), infoJsonObject.isPreventTempleTrekking(), "Temple Trekking", 1);
		//Load Vorkath
		addToPluginToLoadList(pluginClassPathsToLoad, config.vorkathOverlayChoice(), infoJsonObject.isPreventVorkath(), "Vorkath", 1);
		//Load Zulrah
		addToPluginToLoadList(pluginClassPathsToLoad, config.zulrahOverlayChoice(), infoJsonObject.isPreventZulrah(), "Zulrah", 1);
		//Load AoE Warnings
		addToPluginToLoadList(pluginClassPathsToLoad, config.aoeWarningsChoice(), infoJsonObject.isPreventAoeWarnings(), "AoE Warnings", 1);
		//Load Fight Caves
		addToPluginToLoadList(pluginClassPathsToLoad, config.fightCavesChoice(), infoJsonObject.isPreventFightCaves(), "Fight Caves", 1);
		//Load Inferno
		addToPluginToLoadList(pluginClassPathsToLoad, config.infernoChoice(), infoJsonObject.isPreventInferno(), "Inferno", 1);
		//Load Sire Helper
		addToPluginToLoadList(pluginClassPathsToLoad, config.sireHelperChoice(), infoJsonObject.isPreventSireHelper(), "Sire Helper", 2);

		return pluginClassPathsToLoad;
	}
	
	private void addToPluginToLoadList(ArrayList<String> listToAddTo, boolean pluginChoice, boolean preventLoad, String pluginName, int nameVariation)
	{
		if (pluginChoice)
		{
			if (!preventLoad)
			{
				listToAddTo.add(getPluginClassPath(pluginName, nameVariation));
			}
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
	
	
	//Version Check Functions
	private boolean outdatedGameRevision()
	{
		if (infoJsonObject == null)
		{
			return true;
		}
		return client.getRevision() != infoJsonObject.getGameRevision();
	}
	
	private boolean outdatedRuneliteVersion()
	{
		if (infoJsonObject == null)
		{
			return true;
		}
		
		return !RuneLiteProperties.getVersion().equals(infoJsonObject.getRuneliteVersion());
	}
	
	private boolean outdatedLoaderVersion()
	{
		if (infoJsonObject == null)
		{
			return true;
		}
		String[] internalVersionNumber = currentLoaderVersion.split("\\.");
		String[] infoJsonVersionNumber = infoJsonObject.getKotoriLoaderVersion().split("\\.");
		
		//Check local major version number, logic is if local major is less, then it's an older version.
		if (Integer.parseInt(internalVersionNumber[0]) > Integer.parseInt(infoJsonVersionNumber[0]))
		{
			return false;
		}
		else if (Integer.parseInt(internalVersionNumber[0]) < Integer.parseInt(infoJsonVersionNumber[0]))
		{
			return true;
		}
		
		//Check local minor version number, logic is if local minor is less, then it's an older version.
		if (Integer.parseInt(internalVersionNumber[1]) > Integer.parseInt(infoJsonVersionNumber[1]))
		{
			return false;
		}
		else if (Integer.parseInt(internalVersionNumber[1]) < Integer.parseInt(infoJsonVersionNumber[1]))
		{
			return true;
		}
		
		//Check local patch version number, logic is if local patch is less, then it is an older version. If equal or greater, then not older version.
		return Integer.parseInt(internalVersionNumber[2]) < Integer.parseInt(infoJsonVersionNumber[2]);
	}
	
	
	//Pop up Messages
	private void loaderOutdatedPopUp()
	{
		String loaderOutdatedMsg = "<html>Kotori Plugin Loader is outdated. You are using version " + currentLoaderVersion + "."
				+ "<br>Please download version " + infoJsonObject.getKotoriLoaderVersion() + " from Cuell's, Shismo's, or my Discord.</html>";
		createMessagePopUp(loaderOutdatedMsg);
	}
	
	private void revisionOutdatedPopUp()
	{
		String revisionOutdatedMsg = "<html>Oldschool Runescape has updated its game files." +
				"<br>The new game version is: " + client.getRevision() + "." +
				"<br>Plugins were built for game version: " + infoJsonObject.getGameRevision() + "." +
				"<br><b><u><div style='color:yellow'>AS SUCH PLUGINS WILL NOT LOAD NOR WORK UNTIL THEY GET UPDATED!</div></b></u></html>";
		createMessagePopUp(revisionOutdatedMsg);
	}
	
	private void runeliteOutdatedPopup()
	{
		String runeliteOutdatedMsg = "<html>RuneLite has been updated." +
				"<br>The new RuneLite version is: " + RuneLiteProperties.getVersion() + "." +
				"<br>Plugins were built for RuneLite version: " + infoJsonObject.getRuneliteVersion() + "." +
				"<br><b><u>Plugins will still load, but there may be some bugs or crashes!</u></b></html>";
		createMessagePopUp(runeliteOutdatedMsg);
	}
	
	private void createMessagePopUp(String message)
	{
		SwingUtilities.invokeLater(() ->
				JOptionPane.showMessageDialog(client.getCanvas(), message, "Kotori Plugin Loader", JOptionPane.WARNING_MESSAGE));
	}
}
