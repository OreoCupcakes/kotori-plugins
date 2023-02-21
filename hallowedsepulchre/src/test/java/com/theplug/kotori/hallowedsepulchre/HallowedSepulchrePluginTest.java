package com.theplug.kotori.hallowedsepulchre;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class HallowedSepulchrePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(HallowedSepulchrePlugin.class);
		RuneLite.main(args);
	}
}