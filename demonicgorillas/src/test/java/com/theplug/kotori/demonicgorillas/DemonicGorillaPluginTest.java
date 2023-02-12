package com.theplug.kotori.demonicgorillas;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DemonicGorillaPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DemonicGorillaPlugin.class);
		RuneLite.main(args);
	}
}