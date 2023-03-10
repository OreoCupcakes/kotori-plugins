package com.theplug.kotori.gwdticktimers;

import com.theplug.kotori.godwarsticktimers.GodWarsTickTimersPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GodWarsTickTimersPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GodWarsTickTimersPlugin.class);
		RuneLite.main(args);
	}
}