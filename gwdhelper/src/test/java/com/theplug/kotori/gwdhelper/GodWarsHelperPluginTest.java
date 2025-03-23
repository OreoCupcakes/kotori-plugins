package com.theplug.kotori.gwdhelper;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GodWarsHelperPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GodWarsHelperPlugin.class, KotoriUtils.class);
		RuneLite.main(args);
	}
}