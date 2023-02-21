package com.theplug.kotori.dagannothkings;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DagannothKingsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DagannothKingsPlugin.class);
		RuneLite.main(args);
	}
}