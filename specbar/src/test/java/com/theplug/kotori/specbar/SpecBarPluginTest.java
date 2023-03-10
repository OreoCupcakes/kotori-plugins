package com.theplug.kotori.specbar;

import com.theplug.kotori.specbar.SpecBarPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SpecBarPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SpecBarPlugin.class);
		RuneLite.main(args);
	}
}