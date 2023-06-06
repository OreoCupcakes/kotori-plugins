package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import com.theplug.kotori.kotoriutils.rlapi.WidgetInfoPlus;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.RuneLite;

public class SpellInteractions
{
	static final ReflectionLibrary reflectionLibrary = new ReflectionLibrary();
	private static final Client client = RuneLite.getInjector().getInstance(Client.class);
	
	public static void castSpellDeathCharge()
	{
		if (VarUtilities.onArceuusSpellbook() && VarUtilities.isSpellDeathChargeOffCooldown())
		{
			reflectionLibrary.invokeMenuAction(-1, WidgetInfoPlus.SPELL_DEATH_CHARGE.getId(), MenuAction.CC_OP.getId(), 1, -1);
		}
	}
}
