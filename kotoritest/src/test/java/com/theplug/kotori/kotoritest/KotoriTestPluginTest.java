package com.theplug.kotori.kotoritest;

import com.theplug.kotori.kotoritest.kotoriutils.KotoriUtils;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class KotoriTestPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(KotoriUtils.class, KotoriTestPlugin.class);
		RuneLite.main(args);
	}
}