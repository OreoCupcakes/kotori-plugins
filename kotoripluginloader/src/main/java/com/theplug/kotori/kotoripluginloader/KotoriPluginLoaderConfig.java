package com.theplug.kotori.kotoripluginloader;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import org.checkerframework.checker.units.qual.C;

@ConfigGroup("kotoripluginloader")
public interface KotoriPluginLoaderConfig extends Config
{
    @ConfigSection(
            name = "<html><div style='text-align:center;padding-left:25px;padding-right:25px;'>Kotori Plugin Loader<br>" +
                    "Version 0.0.1</div></html>",
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
            name = "Plugins to Load",
            description = "List of plugins",
            position = 2
    )
    String pluginsToLoad = "Plugins to Load";

    @ConfigItem(
            keyName = "rlplUser",
            name = "Are You Using RLPL?",
            description = "Are you using RLPL? If yes, then I'll" +
                    "<br>load the RLPL versions of some plugins.",
            position = 0,
            section = settings
    )
    default boolean rlplUser()
    {
        return false;
    }

    @ConfigItem(
            keyName = "whenToLoad",
            name = "Load Plugins At",
            description = "<html>Do you want to load your selected plugins on" +
                    "<br><b><u>Client Startup</u></b>, the <b><u>Login Screen</u></b>," +
                    "<br>, the first time you are <b><u>Logged In Game</u></b>, or" +
                    "<br> <b><u>Manually</u></b> by clicking the checkbox below?",
            position = 1,
            section = settings
    )
    default loadChoice whenToLoad() { return loadChoice.CLIENT_STARTUP; }

    @ConfigItem(
            keyName = "manualLoad",
            name = "Click to Load Plugins",
            description = "Check the box to load your selected plugins." +
                    "<br>Only works if <b><u>Manually</b></u> loading is selected.",
            position = 2,
            section = settings
    )
    default boolean manualLoad() { return false; }

    @ConfigItem(
            keyName = "alchemicalHydraChoice",
            name = "Alchemical Hydra",
            description = "Load the Alchemical Hydra plugin.",
            position = 0,
            section = pluginsToLoad
    )
    default boolean alchemicalHydraChoice() { return true; }

    @ConfigItem(
            keyName = "cerberusHelperChoice",
            name = "Cerberus Helper",
            description = "Load the Cerberus Helper plugin.",
            position = 1,
            section = pluginsToLoad
    )
    default boolean cerberusHelperChoice() { return true; }

    @ConfigItem(
            keyName = "dagannothKingsChoice",
            name = "Dagannoth Kings",
            description = "Load the Dagannoth Kings plugin.",
            position = 2,
            section = pluginsToLoad
    )
    default boolean dagannothKingsChoice() { return true; }

    @ConfigItem(
            keyName = "demonicGorillasChoice",
            name = "Demonic Gorillas",
            description = "Load the Demonic Gorillas plugin.",
            position = 3,
            section = pluginsToLoad
    )
    default boolean demonicGorillasChoice() { return true; }

    @ConfigItem(
            keyName = "effectTimersChoice",
            name = "Effect Timers",
            description = "Load the Effect Timers plugin.",
            position = 4,
            section = pluginsToLoad
    )
    default boolean effectTimersChoice() { return true; }

    @ConfigItem(
            keyName = "gauntletExtendedChoice",
            name = "Gauntlet Extended",
            description = "Load the Gauntlet Extended plugin.",
            position = 5,
            section = pluginsToLoad
    )
    default boolean gauntletExtendedChoice() { return true; }

    @ConfigItem(
            keyName = "hallowedHelperChoice",
            name = "Hallowed Sepulchre (Deluxe)",
            description = "Load the Hallowed Sepulchre (Deluxe) plugin.",
            position = 6,
            section = pluginsToLoad
    )
    default boolean hallowedHelperChoice() { return true; }

    @ConfigItem(
            keyName = "hallowedSepulchreChoice",
            name = "Hallowed Sepulchre (Lightweight)",
            description = "Load the Hallowed Sepulchre (Lightweight) plugin.",
            position = 7,
            section = pluginsToLoad
    )
    default boolean hallowedSepulchreChoice() { return true; }

    @ConfigItem(
            keyName = "houseOverlayChoice",
            name = "House Overlay",
            description = "Load the House Overlay plugin.",
            position = 8,
            section = pluginsToLoad
    )
    default boolean houseOverlayChoice() { return true; }

    @ConfigItem(
            keyName = "multiIndicatorsChoice",
            name = "Multi-Lines Indicators",
            description = "Load the Multi-Lines Indicators plugin.",
            position = 9,
            section = pluginsToLoad
    )
    default boolean multiIndicatorsChoice() { return true; }

    @ConfigItem(
            keyName = "vorkathOverlayChoice",
            name = "Vorkath",
            description = "Load the Vorkath plugin.",
            position = 10,
            section = pluginsToLoad
    )
    default boolean vorkathOverlayChoice() { return true; }

    @ConfigItem(
            keyName = "zulrahOverlayChoice",
            name = "Zulrah",
            description = "Load the Zulrah plugin.",
            position = 11,
            section = pluginsToLoad
    )
    default boolean zulrahOverlayChoice() { return true; }

    @Getter
    @AllArgsConstructor
    enum loadChoice
    {
        CLIENT_STARTUP("STARTING"),
        LOGIN_SCREEN("LOGIN_SCREEN"),
        LOGGED_IN_GAME("LOGGED_IN"),
        MANUALLY("MANUALLY");

        private final String loadChoice;
    }
}
