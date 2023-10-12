package com.theplug.kotori.dagannothhelper;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DagannothKingsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DagannothHelperPlugin.class, KotoriUtils.class);
		RuneLite.main(args);
	}
}