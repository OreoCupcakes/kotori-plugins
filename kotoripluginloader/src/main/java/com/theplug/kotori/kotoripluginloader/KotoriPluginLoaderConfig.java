package com.theplug.kotori.kotoripluginloader;


import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("kotoripluginloader")
public interface KotoriPluginLoaderConfig extends Config
{
    @ConfigSection(
            name = "<html><center>Kotori Plugin Loader<br>" +
                    "Version 0.0.1</center></html>",
            description = "",
            position = 0,
            closedByDefault = true
    )
    String versionInfo = "Loader Version";

    @ConfigSection(
            name = "Settings",
            description = "General settings",
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
            keyName = "autoLoad",
            name = "Auto Load Plugins?",
            description = "Do you want to automatically load your<br>" +
                    "previously selected plugins on startup?",
            position = 0,
            section = settings
    )
    default boolean autoLoad()
    {
        return true;
    }

    @ConfigItem(
            keyName = "rlplUser",
            name = "Are You Using RLPL Right Now?",
            description = "Are you using RLPL? If yes, then I'll" +
                    "<br>load the RLPL versions of some plugins.",
            position = 1,
            section = settings
    )
    default boolean rlplUser()
    {
        return false;
    }
}
