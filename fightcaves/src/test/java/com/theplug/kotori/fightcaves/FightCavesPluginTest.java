package com.theplug.kotori.fightcaves;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class FightCavesPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(FightCavePlugin.class, KotoriUtils.class);
		RuneLite.main(args);
	}
}