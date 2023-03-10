package com.theplug.kotori.reorderprayers;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ReorderPrayersPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ReorderPrayersPlugin.class);
		RuneLite.main(args);
	}
}