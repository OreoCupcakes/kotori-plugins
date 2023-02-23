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
            keyName = "whenToLoad",
            name = "Load Plugins At",
            description = "<html>Do you want to load your selected plugins on" +
                    "<br><b><u>Client Startup</u></b>, the <b><u>Login Screen</u></b>," +
                    "<br>or the first time you are <b><u>Logged In Game</u></b>?",
            position = 0,
            section = settings
    )
    default loadChoice whenToLoad()
    {
        return loadChoice.CLIENT_STARTUP;
    }

    @ConfigItem(
            keyName = "rlplUser",
            name = "Are You Using RLPL?",
            description = "Are you using RLPL? If yes, then I'll" +
                    "<br>load the RLPL versions of some plugins.",
            position = 1,
            section = settings
    )
    default boolean rlplUser()
    {
        return false;
    }

    @Getter
    @AllArgsConstructor
    enum loadChoice
    {
        CLIENT_STARTUP("STARTING"),
        LOGIN_SCREEN("LOGIN_SCREEN"),
        LOGGED_IN_GAME("LOGGED_IN");

        private final String loadChoice;
    }
}
