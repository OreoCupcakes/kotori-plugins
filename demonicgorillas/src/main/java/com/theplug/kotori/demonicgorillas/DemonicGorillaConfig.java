package com.theplug.kotori.demonicgorillas;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("demonicgorilla")
public interface DemonicGorillaConfig extends Config
{
    @ConfigSection(
            name = "<html><div style='text-align:center;padding-left:25px;padding-right:25px;'>Demonic Gorillas<br>" +
                    "Version 1.1.4</div></html>",
            description = "",
            position = 0,
            closedByDefault = true
    )
    String versionInfo = "Loader Version";

    @ConfigItem(
            keyName = "version",
            name = "",
            description = "",
            section = versionInfo
    )
    default String versionString() { return "Demonic Gorillas\nVersion 1.1.4"; }
}
