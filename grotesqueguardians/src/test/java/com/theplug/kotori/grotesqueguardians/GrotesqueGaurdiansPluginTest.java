package com.theplug.kotori.grotesqueguardians;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GrotesqueGaurdiansPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GrotesqueGuardiansPlugin.class, KotoriUtils.class);
		RuneLite.main(args);
	}
}