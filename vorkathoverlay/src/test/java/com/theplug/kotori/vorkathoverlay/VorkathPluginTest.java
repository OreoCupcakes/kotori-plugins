package com.theplug.kotori.vorkathoverlay;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class VorkathPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(VorkathPlugin.class);
		RuneLite.main(args);
	}
}