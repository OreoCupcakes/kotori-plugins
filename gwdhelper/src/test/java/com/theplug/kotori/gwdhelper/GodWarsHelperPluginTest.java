package com.theplug.kotori.gwdhelper;

import com.theplug.kotori.gwdhelper.kotoriutils.KotoriUtils;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GodWarsHelperPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(KotoriUtils.class,GodWarsHelperPlugin.class);
		RuneLite.main(args);
	}
}