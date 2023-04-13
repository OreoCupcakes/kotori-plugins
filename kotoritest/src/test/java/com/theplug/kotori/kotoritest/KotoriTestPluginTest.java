package com.theplug.kotori.kotoritest;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class KotoriTestPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(KotoriTestPlugin.class);
		RuneLite.main(args);
	}
}