package com.theplug.kotori.templetrekking;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TempleTrekkingPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TempleTrekkingPlugin.class);
		RuneLite.main(args);
	}
}