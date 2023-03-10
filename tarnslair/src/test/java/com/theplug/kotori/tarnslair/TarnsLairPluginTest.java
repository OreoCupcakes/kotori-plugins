package com.theplug.kotori.tarnslair;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TarnsLairPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TarnsLairPlugin.class);
		RuneLite.main(args);
	}
}