package com.theplug.kotori.houseoverlay;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HouseOverlayPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HouseOverlayPlugin.class);
		RuneLite.main(args);
	}
}