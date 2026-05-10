package com.theplug.kotori.inferno;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class InfernoPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(InfernoPlugin.class, KotoriUtils.class);
		RuneLite.main(args);
	}
}