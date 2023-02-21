package com.theplug.kotori.effecttimers;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class EffectTimersPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EffectTimersPlugin.class);
		RuneLite.main(args);
	}
}