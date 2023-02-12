package com.theplug.kotori.gauntletextended;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GauntletExtendedPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GauntletExtendedPlugin.class);
		RuneLite.main(args);
	}
}