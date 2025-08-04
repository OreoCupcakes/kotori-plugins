package com.theplug.kotori.kotoriutils;

import com.google.gson.*;
import com.google.inject.Provides;
import com.theplug.kotori.kotoriutils.gson.HookInfo;
import com.theplug.kotori.kotoriutils.gson.Hooks;
import com.theplug.kotori.kotoriutils.methods.MiscUtilities;
import com.theplug.kotori.kotoriutils.methods.PrayerInteractions;
import com.theplug.kotori.kotoriutils.rlapi.PrayerExtended;
import com.theplug.kotori.kotoriutils.rlapi.WidgetInfoPlus;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Slf4j
@Singleton
@PluginDescriptor(
        name = "<html><font color=#6b8af6>Kotori</font> Plugin Utils</html>",
        enabledByDefault = true,
        description = "Utilities needed for certain ported plugins to work.",
        tags = {"ported", "kotori", "utilities", "utils", "plugin"}
)
public class KotoriUtils extends Plugin {

    private final String hooksFileURL = "https://raw.githubusercontent.com/OreoCupcakes/kotori-plugins-releases/master/hooks.json";
    @Inject
    private Client client;
    @Inject
    private KotoriUtilsConfig config;
    @Inject
    private ConfigManager configManager;
    @Inject
    private ClientThread clientThread;

    private Gson gson;
    private Hooks rsHooks;
    private boolean hooksLoaded;

    @Provides
    KotoriUtilsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(KotoriUtilsConfig.class);
    }

    @Override
    protected void startUp()
    {
        gson = new Gson();
        new Thread(() ->
        {
            getHooksJson();
            parseHooksJson();
        }).start();
    }

    @Override
    protected void shutDown()
    {
        gson = null;
        rsHooks = null;
        hooksLoaded = false;
    }
    
    @Subscribe
    private void onConfigChanged(ConfigChanged event)
    {
        if (event.getKey().equals("clickToLoadHooks"))
        {
            new Thread(() ->
            {
                getHooksJson();
                parseHooksJson();
            }).start();
        }
        
        if (event.getKey().equals("walkUtilTest"))
        {
            if (config.walkUtilTest())
            {
                testWalkHooks();
            }
        }
    }
    
    @Subscribe
    private void onGameTick(GameTick event)
    {
        if (config.npcAnimationUtilTest())
        {
            testNpcAnimationHook();
        }
        
        if (config.npcOverheadUtilTest())
        {
            testNpcOverheadIconHook();
        }
        
        if (config.invokeUtilTest())
        {
            clientThread.invokeLater(this::testInvokeHook);
        }
        
        if (config.isMovingUtilTest())
        {
            testIsMovingHook();
        }
    }
    
    @Subscribe
    private void onClientTick(ClientTick event)
    {
        if (config.menuInsertionUtilTest())
        {
            testMenuEntryHooks();
        }
        
        if (config.spellSelectionUtilTest())
        {
            testSpellSelectionHooks();
        }
    }
    
    @Subscribe
    private void onMenuOptionClicked(MenuOptionClicked event)
    {
        if (event.getMenuOption().contains("Kotori Utils Test - Activate "))
        {
            MiscUtilities.sendGameMessage("Menu Insertion Test is a success. Thick Skin prayer activated.");
        }
        if (event.getMenuOption().contains("<col=39ff14>Kotori Utils Test - Cast Fire Bolt</col> -> "))
        {
            MiscUtilities.sendGameMessage("Spell Selection Test is a success. Casted Fire Bolt.");
        }
    }

    private void getHooksJson()
    {
        if (rsHooks != null)
        {
            return;
        }
    
        BufferedReader reader = null;
        for (int i = 1; i <= 15; i++)
        {
            try
            {
                URL url = new URL(hooksFileURL);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                break;
            }
            catch (Exception e1)
            {
                log.error("Attempt #" + i + ". Unable to establish a connection and download the hooks from the URL.", e1);
            }
        }
        
        if (reader == null)
        {
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(client.getCanvas(),
                        "<html>Connection error. Unable to download necessary game hooks information from the Internet." +
                                "<br>Make sure you are connected to the Internet and your proxy or VPN isn't being flagged as suspicious." +
                                "<br>You can re-establish a connection by clicking the \"Click to Load Hooks\" button within Kotori Plugin Utils." +
                                "<br><div style='color:yellow'><b><u>If you are unable load the hooks, the client will crash when using plugins dependent on Kotori Plugin Utils!</u></b></div></html>"
                        , "Kotori Plugin Utils",JOptionPane.WARNING_MESSAGE));
            return;
        }
            
        try
        {
            rsHooks = gson.fromJson(reader, Hooks.class);
            reader.close();
        }
        catch (Exception e)
        {
            log.error("Unable to parse Hooks.json into a Hooks object.", e);
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(client.getCanvas(),
                            "Error in parsing Hooks.json. Yell at Kotori for forgetting a comma.","Kotori Plugin Utils", JOptionPane.WARNING_MESSAGE));
        }
    }

    private void parseHooksJson()
    {
        if (rsHooks == null || hooksLoaded)
        {
            return;
        }
        //Set the game hooks
        for (HookInfo hookInfo : rsHooks.getH())
        {
            String hookName = hookInfo.getN();
            
            switch (hookName)
            {
                case "invokeMenuAction":
                    ReflectionLibrary.setInvokeMenuActionClassName(hookInfo.getC());
                    ReflectionLibrary.setInvokeMenuActionMethodName(hookInfo.getP());
                    ReflectionLibrary.setInvokeMenuActionJunkValue(hookInfo.getM());
                    break;
                case "setViewportWalking":
                    ReflectionLibrary.setViewportWalkingClassName(hookInfo.getC());
                    ReflectionLibrary.setViewportWalkingFieldName(hookInfo.getP());
                    break;
                case "setCheckClick":
                    ReflectionLibrary.setCheckClickClassName(hookInfo.getC());
                    ReflectionLibrary.setCheckClickFieldName(hookInfo.getP());
                    break;
                case "setSelectedSpellWidget":
                    ReflectionLibrary.setSelectedSpellWidgetClassName(hookInfo.getC());
                    ReflectionLibrary.setSelectedSpellWidgetFieldName(hookInfo.getP());
                    ReflectionLibrary.setSelectedSpellWidgetMultiplier(getObfuscatedSetter(hookInfo.getM()));
                    break;
                case "setSelectedSpellChildIndex":
                    ReflectionLibrary.setSelectedSpellChildIndexClassName(hookInfo.getC());
                    ReflectionLibrary.setSelectedSpellChildIndexFieldName(hookInfo.getP());
                    ReflectionLibrary.setSelectedSpellChildIndexMultiplier(getObfuscatedSetter(hookInfo.getM()));
                    break;
                case "setSelectedSpellItemId":
                    ReflectionLibrary.setSelectedSpellItemIDClassName(hookInfo.getC());
                    ReflectionLibrary.setSelectedSpellItemIDFieldName(hookInfo.getP());
                    ReflectionLibrary.setSelectedSpellItemIDMultiplier(getObfuscatedSetter(hookInfo.getM()));
                    break;
                case "getOldNpcOverheadArray":
                    ReflectionLibrary.setOldNpcOverheadArrayClassName(hookInfo.getC());
                    ReflectionLibrary.setOldNpcOverheadArrayFieldName(hookInfo.getP());
                    break;
                case "getNewNpcOverheadObject":
                    ReflectionLibrary.setNewNpcOverheadObjectClassName(hookInfo.getC());
                    ReflectionLibrary.setNewNpcOverheadObjectFieldName(hookInfo.getP());
                    break;
                case "getNewNpcOverheadArray":
                    ReflectionLibrary.setNewNpcOverheadArrayClassName(hookInfo.getC());
                    ReflectionLibrary.setNewNpcOverheadArrayFieldName(hookInfo.getP());
                    break;
                case "getNpcOverheadMethod":
                    ReflectionLibrary.setNpcOverheadMethodClassName(hookInfo.getC());
                    ReflectionLibrary.setNpcOverheadMethodName(hookInfo.getP());
                    ReflectionLibrary.setNpcOverheadMethodJunkValue(hookInfo.getM());
                    break;
                case "menuOptionsCount":
                    ReflectionLibrary.setMenuOptionsCountClassName(hookInfo.getC());
                    ReflectionLibrary.setMenuOptionsCountFieldName(hookInfo.getP());
                    ReflectionLibrary.setMenuOptionsCountMultiplier(hookInfo.getM());
                    break;
                case "menuIdentifiersArray":
                    ReflectionLibrary.setMenuIdentifiersClassName(hookInfo.getC());
                    ReflectionLibrary.setMenuIdentifiersFieldName(hookInfo.getP());
                    break;
                case "menuItemIdsArray":
                    ReflectionLibrary.setMenuItemIdsClassName(hookInfo.getC());
                    ReflectionLibrary.setMenuItemIdsFieldName(hookInfo.getP());
                    break;
                case "menuOptionsArray":
                    ReflectionLibrary.setMenuOptionsClassName(hookInfo.getC());
                    ReflectionLibrary.setMenuOptionsFieldName(hookInfo.getP());
                    break;
                case "menuParam0Array":
                    ReflectionLibrary.setMenuParam0ClassName(hookInfo.getC());
                    ReflectionLibrary.setMenuParam0FieldName(hookInfo.getP());
                    break;
                case "menuParam1Array":
                    ReflectionLibrary.setMenuParam1ClassName(hookInfo.getC());
                    ReflectionLibrary.setMenuParam1FieldName(hookInfo.getP());
                    break;
                case "menuTargetsArray":
                    ReflectionLibrary.setMenuTargetsClassName(hookInfo.getC());
                    ReflectionLibrary.setMenuTargetsFieldName(hookInfo.getP());
                    break;
                case "menuTypesArray":
                    ReflectionLibrary.setMenuTypesClassName(hookInfo.getC());
                    ReflectionLibrary.setMenuTypesFieldName(hookInfo.getP());
                    break;
                case "setSelectedSceneTileX":
                    ReflectionLibrary.setSceneSelectedXClassName(hookInfo.getC());
                    ReflectionLibrary.setSceneSelectedXFieldName(hookInfo.getP());
                    break;
                case "setSelectedSceneTileY":
                    ReflectionLibrary.setSceneSelectedYClassName(hookInfo.getC());
                    ReflectionLibrary.setSceneSelectedYFieldName(hookInfo.getP());
                    break;
                case "isMoving":
                    ReflectionLibrary.setActorPathLengthClassName(hookInfo.getC());
                    ReflectionLibrary.setActorPathLengthFieldName(hookInfo.getP());
                    ReflectionLibrary.setActorPathLengthMultiplier(hookInfo.getM());
                    break;
                case "getActorAnimationId":
                    ReflectionLibrary.setActorAnimationIdClassName(hookInfo.getC());
                    ReflectionLibrary.setActorAnimationIdFieldName(hookInfo.getP());
                    ReflectionLibrary.setActorAnimationIdMultiplier(hookInfo.getM());
                    break;
                case "getActorAnimationObject":
                    ReflectionLibrary.setActorAnimationObjectClassName(hookInfo.getC());
                    ReflectionLibrary.setActorAnimationObjectFieldName(hookInfo.getP());
                    break;
            }
        }

        hooksLoaded = true;
        
        if (!config.disableHooksLoadedPopup())
        {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(client.getCanvas(),
                            "Hooks successfully loaded into the client.", "Kotori Plugin Utils", JOptionPane.INFORMATION_MESSAGE));
        }
    }

    private long[] extendedGCD(long a, long m)
    {
        if (m == 0)
        {
            return new long[]{a, 1, 0};
        }

        long[] result = extendedGCD(m, a % m);

        long gcd = result[0];
        long x1 = result[1];
        long y1 = result[2];

        long x = y1;
        long y = x1 - (a / m) * y1;

        return new long[]{gcd, x, y};
    }

    private long modInverse(long a, long m)
    {
        // Normalize a to be within the range [0, m-1] if it's negative
        a = (a % m + m) % m;

        long[] result = extendedGCD(a, m);
        long gcd = result[0];

        // If gcd(a, m) != 1, there is no inverse
        if (gcd != 1)
        {
            log.error("Unable to find the modular multiplicative inverse of {}.", a);
            return -1;
        }
        else
        {
            // x is the modular multiplicative inverse, ensure it's positive
            return (result[1] % m + m) % m;
        }
    }

    private int getObfuscatedSetter(long a)
    {
        return (int) modInverse(a, 4294967296L);
    }

    private void testWalkHooks()
    {
        WorldPoint currentPlayerLocation = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation());
        WorldPoint walkingPoint = currentPlayerLocation.dx(-1);
        ReflectionLibrary.sceneWalk(walkingPoint);
        MiscUtilities.sendGameMessage("Kotori Utils Test - Current Location: " + currentPlayerLocation);
        MiscUtilities.sendGameMessage("Kotori Utils Test - Walking to: " + walkingPoint);
    }
    
    private void testInvokeHook()
    {
        Prayer prayer = Prayer.THICK_SKIN;
        if (PrayerInteractions.isActive(prayer))
        {
            PrayerInteractions.deactivatePrayer(prayer);
            MiscUtilities.sendGameMessage("Kotori Utils Test - Deactivating Thick Skin prayer");
        }
        else
        {
            PrayerInteractions.activatePrayer(prayer);
            MiscUtilities.sendGameMessage("Kotori Utils Test - Activating Thick Skin prayer");
        }

    //    PrayerInteractions.oneTickFlickPrayers(Prayer.PROTECT_FROM_MAGIC, Prayer.EAGLE_EYE);
    }
    
    private void testNpcAnimationHook()
    {
        Actor actor = client.getLocalPlayer().getInteracting();
        
        if (actor instanceof NPC)
        {
            int animationId = ReflectionLibrary.getNpcAnimationId(actor);
            String npcName = actor.getName();
            if (npcName == null)
            {
                npcName = "null";
            }
            MiscUtilities.sendGameMessage("Kotori Utils Test - NPC Name: " + npcName + ", Animation ID: " + animationId);
        }
    }
    
    private void testNpcOverheadIconHook()
    {
        Actor actor = client.getLocalPlayer().getInteracting();
        
        if (actor instanceof NPC)
        {
            HeadIcon headIcon = ReflectionLibrary.getNpcOverheadIcon((NPC) actor);
            
            String npcName = actor.getName();
            if (npcName == null)
            {
                npcName = "null";
            }

            if (headIcon == null)
            {
                MiscUtilities.sendGameMessage("Kotori Utils Test - NPC Name: " + npcName + " has no overhead icon.");
            }
            else
            {
                MiscUtilities.sendGameMessage("Kotori Utils Test - NPC Name: " + npcName + ", Overhead Icon: " + headIcon.name());
            }
        }
    }
    
    private void testMenuEntryHooks()
    {
        if (client.isMenuOpen())
        {
            return;
        }
        int index = ReflectionLibrary.getMenuOptionsCount();
        if (index == -1)
        {
            return;
        }
        client.getMenu().createMenuEntry(index).setForceLeftClick(true);
        ReflectionLibrary.insertMenuEntry(index, "Kotori Utils Test - Activate ", "Thick Skin Prayer", MenuAction.CC_OP.getId(), 1, -1,
                PrayerExtended.getPrayerWidgetId(Prayer.THICK_SKIN), -1);
    }
    
    private void testSpellSelectionHooks()
    {
        if (client.isMenuOpen())
        {
            return;
        }
        
        MenuEntry[] entries = client.getMenu().getMenuEntries();
        MenuEntry npcEntry = null;
        for (MenuEntry e: entries)
        {
            if (e.getType() == MenuAction.NPC_SECOND_OPTION)
            {
                npcEntry = e;
                break;
            }
        }
        ReflectionLibrary.setSelectedSpell(WidgetInfoPlus.SPELL_WIND_STRIKE.getId());
        String menuOptionText = "<col=39ff14>Kotori Utils Test - Cast Wind Strike</col> -> ";
        MenuEntry hotkeyEntry = client.getMenu().createMenuEntry(-1).setForceLeftClick(true).setParam0(0).setParam1(0).setType(MenuAction.WIDGET_TARGET_ON_NPC)
                .setOption(menuOptionText);
        if (npcEntry == null)
        {
            hotkeyEntry.setTarget(" ").setIdentifier(0);
        }
        else
        {
            hotkeyEntry.setTarget(npcEntry.getTarget()).setIdentifier(npcEntry.getIdentifier());
        }
    }
    
    private void testIsMovingHook()
    {
        Player you = client.getLocalPlayer();
        int pathLength = ReflectionLibrary.getActorPathLength(you);
        int poseAnimation = you.getPoseAnimation();
        int idleAnimation = you.getIdlePoseAnimation();
        MiscUtilities.sendGameMessage("Kotori Utils Test - isMoving? " + ReflectionLibrary.areYouMoving() + ", pathLength: " + pathLength +
                ", poseAnimation: " + poseAnimation + ", idleAnimation: " + idleAnimation);
    }
}
