package com.theplug.kotori.kotoripluginloader;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("kotoripluginloader")
public interface KotoriPluginLoaderConfig extends Config
{
    //Sections
    @ConfigSection(
            name = "<html>Kotori Plugin Loader<br>Version 0.9.0</html>",
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
            description = "Disable the plugins loaded popup message?",
            position = 1,
            section = settings
    )
    default boolean disablePluginsLoadMsg() { return false; }

    @ConfigItem(
            keyName = "rlplUser",
            name = "Are You Using RLPL?",
            description = "Are you using RLPL? (ThePlug, Illumine, Cuell, or Sandy subscribers)" +
                    "<br>If yes, then I'll load the RLPL versions of some plugins." +
                    "<br><b><u>If you are a Ganom or Shismo user, DO NOT CHECK THIS BOX!</b></u>",
            position = 2,
            section = settings
    )
    default boolean rlplUser()
    {
        return false;
    }

    @ConfigItem(
            keyName = "whenToLoad",
            name = "Auto Load Plugins At",
            description = "<html>Do you want to auto load your selected plugins on" +
                    "<br><b><u>Client Startup</u></b>, <b><u>Game Startup</u></b>," +
                    "<br>when you are <b><u>Logged In</u></b> game, or <b><u>Never</u></b>?",
            position = 3,
            section = settings
    )
    default loadChoice whenToLoad() { return loadChoice.GAME_STARTUP; }

    @ConfigItem(
            keyName = "manualLoadInfoBox",
            name = "Manually Loading Plugins Info",
            description = "",
            position = 4,
            section = settings
    )
    default String pluginInfoStringBox() {
        return  "You can manually hot load certain plugins that haven't already been loaded anytime. " +
                "Not all plugins can hot load in! " +
                "As such the selection menu will prevent you from selecting those plugins.";
    }

    @ConfigItem(
            keyName = "manualLoad",
            name = "Click to Load Plugins",
            description = "Check the box to manually load your selected plugins.",
            position = 5,
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
            keyName = "alchemicalHydraChoice",
            name = "Alchemical Hydra",
            description = "Load the Alchemical Hydra plugin.",
            section = pluginsToLoad
    )
    default boolean alchemicalHydraChoice() { return false; }

    @ConfigItem(
            keyName = "cerberusHelperChoice",
            name = "Cerberus Helper",
            description = "Load the Cerberus Helper plugin.",
            section = pluginsToLoad
    )
    default boolean cerberusHelperChoice() { return false; }

    @ConfigItem(
            keyName = "dagannothKingsChoice",
            name = "Dagannoth Kings",
            description = "Load the Dagannoth Kings plugin.",
            section = pluginsToLoad
    )
    default boolean dagannothKingsChoice() { return false; }

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
            keyName = "gauntletExtendedChoice",
            name = "Gauntlet Extended",
            description = "Load the Gauntlet Extended plugin.",
            section = pluginsToLoad
    )
    default boolean gauntletExtendedChoice() { return false; }

    @ConfigItem(
            keyName = "hallowedHelperChoice",
            name = "Hallowed Sepulchre (Deluxe)",
            description = "Load the Hallowed Sepulchre (Deluxe) plugin.",
            section = pluginsToLoad
    )
    default boolean hallowedHelperChoice() { return false; }

    @ConfigItem(
            keyName = "hallowedSepulchreChoice",
            name = "Hallowed Sepulchre (Lightweight)",
            description = "Load the Hallowed Sepulchre (Lightweight) plugin.",
            section = pluginsToLoad
    )
    default boolean hallowedSepulchreChoice() { return false; }

    @ConfigItem(
            keyName = "houseOverlayChoice",
            name = "House Overlay",
            description = "Load the House Overlay plugin.",
            section = pluginsToLoad
    )
    default boolean houseOverlayChoice() { return false; }

    @ConfigItem(
            keyName = "kotoriUtilsChoice",
            name = "Kotori Plugin Utils",
            description = "Load Kotori Plugin Utils plugin.",
            section = pluginsToLoad
    )
    default boolean kotoriUtilsChoice() { return false; }

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
            name = "Grotesque Guardians",
            description = "Load the Grotesque Guardians plugin.",
            section = pluginsToLoad
    )
    default boolean grotesqueGuardiansChoice() { return false; }

    @Getter
    @AllArgsConstructor
    enum loadChoice
    {
        CLIENT_STARTUP("CLIENT_STARTUP"),
        GAME_STARTUP("GAME_STARTUP"),
        LOGGED_IN("LOGGED_IN"),
        NEVER("NEVER");

        private final String loadChoice;
    }
}
