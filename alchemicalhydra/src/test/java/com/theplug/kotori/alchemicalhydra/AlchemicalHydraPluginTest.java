package com.theplug.kotori.alchemicalhydra;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class AlchemicalHydraPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(AlchemicalHydraPlugin.class);
		RuneLite.main(args);
	}
}