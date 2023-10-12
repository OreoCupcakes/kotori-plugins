package com.theplug.kotori.aoewarnings;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class AoeWarningsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(AoeWarningPlugin.class, KotoriUtils.class);
		RuneLite.main(args);
	}
}