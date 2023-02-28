package com.theplug.kotori.kotoriutils;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("kotoriutils")
public interface KotoriUtilsConfig extends Config
{
    @ConfigSection(
            name = "<html>Kotori Plugin Utils<br>Version 1.0.1</html>",
            description = "",
            position = -1,
            closedByDefault = true
    )
    String versionInfo = "Version";

    @ConfigItem(
            keyName = "randomTextToShowVersion",
            name = "",
            description = "",
            section = versionInfo
    )
    default String randomTextToShowVersion() { return ""; }
}
