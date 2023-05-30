package com.theplug.kotori.demonicgorillas;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("demonicgorilla")
public interface DemonicGorillaConfig extends Config
{
    @ConfigSection(
            name = "<html>Demonic Gorillas<br>Version 1.3.0</html>",
            description = "",
            position = 0,
            closedByDefault = true
    )
    String versionInfo = "Version";

    @ConfigItem(
            keyName = "version",
            name = "",
            description = "",
            section = versionInfo
    )
    default Boolean versionString() { return true; }
}
