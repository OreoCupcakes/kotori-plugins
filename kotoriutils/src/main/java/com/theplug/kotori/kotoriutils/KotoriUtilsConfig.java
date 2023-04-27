package com.theplug.kotori.kotoriutils;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("kotoriutils")
public interface KotoriUtilsConfig extends Config
{
    @ConfigSection(
            name = "<html>Kotori Plugin Utils<br>Version 1.0.6</html>",
            description = "",
            position = -1,
            closedByDefault = true
    )
    String versionInfo = "Version";

    @ConfigItem(
            keyName = "clickToLoadHooks",
            name = "Click to Load Hooks",
            description = "<html>If you failed to download the necessary game hooks on startup," +
                    "<br>click the checkbox to try again.</html>"
    )
    default boolean clickToLoadHooks() { return false; }
}
