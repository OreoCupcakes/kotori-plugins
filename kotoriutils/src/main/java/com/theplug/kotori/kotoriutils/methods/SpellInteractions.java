package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import com.theplug.kotori.kotoriutils.rlapi.WidgetInfoPlus;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.RuneLite;

public class SpellInteractions
{
	private static final Client client = RuneLite.getInjector().getInstance(Client.class);
	
	public static void castSpellDeathCharge()
	{
		if (VarUtilities.onArceuusSpellbook() && VarUtilities.isSpellDeathChargeOffCooldown())
		{
			ReflectionLibrary.invokeMenuAction(-1, WidgetInfoPlus.SPELL_DEATH_CHARGE.getId(), MenuAction.CC_OP.getId(), 1, -1);
		}
	}
}
