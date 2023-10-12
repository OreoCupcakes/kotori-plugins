package com.theplug.kotori.javaexample;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class JavaExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(JavaExamplePlugin.class, KotoriUtils.class);
		RuneLite.main(args);
	}
}