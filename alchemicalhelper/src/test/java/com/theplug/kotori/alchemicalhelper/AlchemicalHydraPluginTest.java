package com.theplug.kotori.alchemicalhelper;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class AlchemicalHydraPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(AlchemicalHelperPlugin.class, KotoriUtils.class);
		RuneLite.main(args);
	}
}