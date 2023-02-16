package com.theplug.kotori.kotoripluginloader;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class KotoriPluginLoaderTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(KotoriPluginLoader.class);
		RuneLite.main(args);
	}
}