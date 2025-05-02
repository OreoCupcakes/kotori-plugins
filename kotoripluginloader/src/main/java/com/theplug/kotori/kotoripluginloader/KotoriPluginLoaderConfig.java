package com.theplug.kotori.kotoripluginloader;


import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("kotoripluginloader")
public interface KotoriPluginLoaderConfig extends Config
{
    //Sections
    @ConfigSection(
            name = "<html>Kotori Plugin Loader<br>Version 3.0.0</html>",
            description = "",
            position = 0,
            closedByDefault = true
    )
    String versionInfo = "Loader Version";

    @ConfigSection(
            name = "Loader Settings",
            description = "Configure how you are using the plugin loader.",
            position = 1
    )
    String settings = "Settings";

    @ConfigSection(
            name = "Plugins To Load",
            description = "List of plugins",
            position = 2
    )
    String pluginsToLoad = "Plugins to Load";


    //Loader Settings
    @ConfigItem(
            keyName = "disableTutorialMsg",
            name = "Disable Tutorial Message",
            description = "Disable the tutorial popup message from showing up on subsequent startups.",
            position = 0,
            section = settings
    )
    default boolean disableTutorialMsg() { return false; }

    @ConfigItem(
            keyName = "disablePluginLoadMsg",
            name = "Disable Plugins Loaded Message",
            description = "Disable the plugins loaded popup message.",
            position = 1,
            section = settings
    )
    default boolean disablePluginsLoadMsg() { return false; }

    @ConfigItem(
            keyName = "autoLoadPlugins",
            name = "Auto Load Plugins?",
            description = "Do you want to auto load your selected plugins on startup?",
            position = 3,
            section = settings
    )
    default boolean autoLoadPlugins() { return false; }

    @ConfigItem(
            keyName = "manualLoad",
            name = "Click to Load Plugins",
            description = "Check the box to manually load your selected plugins.",
            position = 4,
            section = settings
    )
    default boolean manualLoad() { return false; }



    //Plugin Choices

    @ConfigItem(
            keyName = "selectAllPluginsChoice",
            name = "Select/Unselect All Plugins",
            description = "Load all plugins listed below.",
            position = -2,
            section = pluginsToLoad
    )
    default boolean selectAllPluginsChoice() { return false; }
    @ConfigItem(
            keyName = "alchemicalHelperChoice",
            name = "Alchemical Helper",
            description = "Load the Alchemical Helper plugin.",
            section = pluginsToLoad
    )
    default boolean alchemicalHelperChoice() { return false; }

    @ConfigItem(
            keyName = "cerberusHelperChoice",
            name = "Cerberus Helper",
            description = "Load the Cerberus Helper plugin.",
            section = pluginsToLoad
    )
    default boolean cerberusHelperChoice() { return false; }

    @ConfigItem(
            keyName = "dagannothHelperChoice",
            name = "Dagannoth Helper",
            description = "Load the Dagannoth Helper plugin.",
            section = pluginsToLoad
    )
    default boolean dagannothHelperChoice() { return false; }

    @ConfigItem(
            keyName = "demonicGorillasChoice",
            name = "Demonic Gorillas",
            description = "Load the Demonic Gorillas plugin.",
            section = pluginsToLoad
    )
    default boolean demonicGorillasChoice() { return false; }

    @ConfigItem(
            keyName = "effectTimersChoice",
            name = "Effect Timers",
            description = "Load the Effect Timers plugin.",
            section = pluginsToLoad
    )
    default boolean effectTimersChoice() { return false; }

    @ConfigItem(
            keyName = "gauntletHelperChoice",
            name = "Gauntlet Helper",
            description = "Load the Gauntlet Helper plugin.",
            section = pluginsToLoad
    )
    default boolean gauntletHelperChoice() { return false; }

    @ConfigItem(
            keyName = "hallowedHelperChoice",
            name = "Hallowed Sepulchre (Deluxe)",
            description = "Load the Hallowed Sepulchre (Deluxe) plugin.",
            section = pluginsToLoad
    )
    default boolean hallowedHelperChoice() { return false; }

    @ConfigItem(
            keyName = "houseOverlayChoice",
            name = "House Overlay",
            description = "Load the House Overlay plugin.",
            section = pluginsToLoad
    )
    default boolean houseOverlayChoice() { return false; }

    @ConfigItem(
            keyName = "multiIndicatorsChoice",
            name = "Multi-Lines Indicators",
            description = "Load the Multi-Lines Indicators plugin.",
            section = pluginsToLoad
    )
    default boolean multiIndicatorsChoice() { return false; }

    @ConfigItem(
            keyName = "vorkathOverlayChoice",
            name = "Vorkath",
            description = "Load the Vorkath plugin.",
            section = pluginsToLoad
    )
    default boolean vorkathOverlayChoice() { return false; }

    @ConfigItem(
            keyName = "zulrahOverlayChoice",
            name = "Zulrah",
            description = "Load the Zulrah plugin.",
            section = pluginsToLoad
    )
    default boolean zulrahOverlayChoice() { return false; }

    @ConfigItem(
            keyName = "grotesqueGuardiansChoice",
            name = "Grotesque Helper",
            description = "Load the Grotesque Guardians Helper plugin.",
            section = pluginsToLoad
    )
    default boolean grotesqueGuardiansChoice() { return false; }

    @ConfigItem(
            keyName = "nexExtendedChoice",
            name = "Nex Extended",
            description = "Load the Nex Extended plugin.",
            section = pluginsToLoad
    )
    default boolean nexExtendedChoice() { return false; }

    @ConfigItem(
            keyName = "godWarsHelperChoice",
            name = "God Wars Helper",
            description = "Load the God Wars Helper plugin.",
            section = pluginsToLoad
    )
    default boolean godWarsHelperChoice() { return false; }

    @ConfigItem(
            keyName = "specBarChoice",
            name = "Spec Bar",
            description = "Load the Spec Bar plugin.",
            section = pluginsToLoad
    )
    default boolean specBarChoice() { return false; }

    @ConfigItem(
            keyName = "templeTrekkingChoice",
            name = "Temple Trekking",
            description = "Load the Temple Trekking plugin.",
            section = pluginsToLoad
    )
    default boolean templeTrekkingChoice() { return false; }

    @ConfigItem(
            keyName = "tarnsLairChoice",
            name = "Tarn's Lair",
            description = "Load the Tarn's Lair plugin.",
            section = pluginsToLoad
    )
    default boolean tarnsLairChoice() { return false; }

    @ConfigItem(
            keyName = "nightmareChoice",
            name = "Nightmare of Ashihama",
            description = "Load the Nightmare plugin.",
            section = pluginsToLoad
    )
    default boolean nightmareChoice() { return false; }
    
    @ConfigItem(
            keyName = "aoeWarningsChoice",
            name = "AoE Warnings",
            description = "Load the AoE Warnings plugin.",
            section = pluginsToLoad
    )
    default boolean aoeWarningsChoice() { return false; }
    
    @ConfigItem(
            keyName = "fightCavesChoice",
            name = "Fight Cave",
            description = "Load the Fight Caves plugin.",
            section = pluginsToLoad
    )
    default boolean fightCavesChoice() { return false; }
    
    @ConfigItem(
            keyName = "infernoChoice",
            name = "Inferno",
            description = "Load the Inferno plugin.",
            section = pluginsToLoad
    )
    default boolean infernoChoice() { return false; }

    @ConfigItem(
            keyName = "sireHelperChoice",
            name = "Sire Helper",
            description = "load the Sire Helper plugin.",
            section = pluginsToLoad
    )
    default boolean sireHelperChoice()
    {
        return false;
    }
}
