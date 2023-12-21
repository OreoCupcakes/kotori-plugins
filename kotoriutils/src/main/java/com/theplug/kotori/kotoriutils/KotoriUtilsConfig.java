package com.theplug.kotori.kotoriutils;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("kotoriutils")
public interface KotoriUtilsConfig extends Config
{
    @ConfigSection(
            name = "<html>Kotori Plugin Utils<br>Version 1.3.5</html>",
            description = "",
            position = -1
    )
    String versionInfo = "Version";

    @ConfigItem(
            keyName = "clickToLoadHooks",
            name = "Click to Load Hooks",
            description = "<html>If you failed to download the necessary game hooks on startup," +
                    "<br>click the checkbox to try again.</html>",
            position = 0,
            section = versionInfo
    )
    default boolean clickToLoadHooks() { return false; }
    
    @ConfigItem(
            keyName = "disableHooksLoadedMsg",
            name = "Disable Hooks Loaded Pop Up",
            description = "Disable the \"Hooks successfully loaded into the client.\" pop up message.",
            position = 1,
            section = versionInfo
    )
    default boolean disableHooksLoadedPopup() { return true; }
    
    @ConfigSection(
            name = "Utils Test Functions",
            description = "Test if the plugin hooked into the game properly",
            position = 0
    )
    String testFunctions = "Test Functions";
    
    @ConfigItem(
            keyName = "walkUtilTest",
            name = "Scene Walking Test",
            description = "This test will walk your character one tile to the west once.",
            position = 0,
            section = testFunctions
    )
    default boolean walkUtilTest() { return false; }
    
    @ConfigItem(
            keyName = "invokeUtilTest",
            name = "Invoking Test",
            description = "This test will turn on and off the Thick Skin prayer each game tick.",
            section = testFunctions
    )
    default boolean invokeUtilTest() { return false; }
    
    @ConfigItem(
            keyName = "npcAnimationUtilTest",
            name = "NPC Animation Test",
            description = "This test will output the animation ID of the NPC you are interacting with (attacking).",
            section = testFunctions
    )
    default boolean npcAnimationUtilTest() { return false; }
    
    @ConfigItem(
            keyName = "npcOverheadUtilTest",
            name = "NPC Overhead Icon Test",
            description = "This test will output the overhead prayer the NPC, you are interacting with (attacking), is using.",
            section = testFunctions
    )
    default boolean npcOverheadUtilTest() { return false; }
    
    @ConfigItem(
            keyName = "menuInsertionUtilTest",
            name = "Menu Insertion Test",
            description = "This test will create a \"One Click\" menu entry to activate the Thick Skin prayer.",
            section = testFunctions
    )
    default boolean menuInsertionUtilTest() { return false; }
    
    @ConfigItem(
            keyName = "spellSelectionUtilTest",
            name = "Set Selected Spell Test",
            description = "This test requires you to be on the Normal spellbook." +
                    "<br>It will set Fire Strike as the selected spell and create a one click menu entry to cast it.",
            section = testFunctions
    )
    default boolean spellSelectionUtilTest() { return false; }
    
    @ConfigItem(
            keyName = "isMovingUtilTest",
            name = "Player isMoving Test",
            description = "This test will check if the player is moving by hooking into its path length field." +
                    "<br>It checks if the path length is 0 or if the player's pose animation is not its idle animation.",
            section = testFunctions
    )
    default boolean isMovingUtilTest() { return false; }
}
