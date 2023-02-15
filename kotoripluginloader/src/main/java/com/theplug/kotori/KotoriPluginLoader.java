package com.theplug.kotori;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.api.*;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

@PluginDescriptor(
        name = "Java example",
        description = "Java example"
)
@Slf4j
public class KotoriPluginLoader extends Plugin
{
    final private String pluginsJson = "https://github.com/OreoCupcakes/kotori-ported-plugins-hosting/blob/master/plugins.json?raw=true";
    final private String revisionFile = "https://github.com/OreoCupcakes/kotori-ported-plugins-hosting/blob/master/hooks.txt?raw=true";

    @Inject
    private Client client;

    private int gameRevisionFromHooks;
    private boolean gameRevisionCheck;
    private HashSet<URL> jarURLs = new HashSet<>();


    @Override
    protected void startUp()
    {
        // runs on plugin startup
        log.info("Plugin started");
    }

    @Override
    protected void shutDown()
    {
        // runs on plugin shutdown
        log.info("Plugin stopped");
    }

    private boolean checkGameRevision()
    {
        try
        {
            URL hooksURL = new URL(revisionFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(hooksURL.openStream()));
            String lineToParse;
            while ((lineToParse = bufferedReader.readLine()) != null)
            {
                String[] splitLineArray = lineToParse.replaceAll("[^[a-zA-Z0-9|\\-|\\:|\\.]]","").split(":|\\.");

                if (splitLineArray[0].contains("rsversion"))
                {
                    gameRevisionFromHooks = Integer.parseInt(splitLineArray[1]);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            log.error("Failed at getting RS revision from hooks file.", e);
        }

        if (client.getRevision() == gameRevisionFromHooks) {
            return true;
        }

        return false;
    }

    private boolean getJarURLSet()
    {
        try
        {
            URL pluginsURLs = new URL(pluginsJson);

        }
        catch (Exception e)
        {

        }

        return false;
    }
}