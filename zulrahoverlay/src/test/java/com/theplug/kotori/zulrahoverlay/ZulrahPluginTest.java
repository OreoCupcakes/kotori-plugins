package com.theplug.kotori.zulrahoverlay;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ZulrahPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ZulrahPlugin.class);
		RuneLite.main(args);
	}
}