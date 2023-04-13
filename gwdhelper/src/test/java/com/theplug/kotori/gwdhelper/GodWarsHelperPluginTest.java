package com.theplug.kotori.gwdhelper;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GodWarsHelperPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GodWarsHelperPlugin.class);
		RuneLite.main(args);
	}
}