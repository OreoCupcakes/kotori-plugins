package com.theplug.kotori.multiindicators;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MultiIndicatorsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(MultiIndicatorsPlugin.class);
		RuneLite.main(args);
	}
}