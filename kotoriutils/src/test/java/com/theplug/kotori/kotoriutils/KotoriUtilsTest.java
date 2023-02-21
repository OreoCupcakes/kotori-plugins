package com.theplug.kotori.kotoriutils;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class KotoriUtilsTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(KotoriUtils.class);
		RuneLite.main(args);
	}
}