package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.rlapi.WidgetIDPlus;
import com.theplug.kotori.kotoriutils.rlapi.WidgetInfoPlus;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.client.RuneLite;

public class VarUtilities
{
	private static final Client client = RuneLite.getInjector().getInstance(Client.class);
	
	public static final int VARBIT_ACTIVE_SPELLBOOK = 4070;
	
	public static int getActiveSpellbook()
	{
		/*
		 * 0 = Normal Spellbook
		 * 1 = Ancient Spellbook
		 * 2 = Lunar Spellbook
		 * 3 = Arceuus Spellbook
		 */
		return client.getVarbitValue(VARBIT_ACTIVE_SPELLBOOK);
	}
	public static boolean onStandardSpellbook()
	{
		return getActiveSpellbook() == 0;
	}
	public static boolean onAncientSpellbook()
	{
		return getActiveSpellbook() == 1;
	}
	public static boolean onLunarSpellbook()
	{
		return getActiveSpellbook() == 2;
	}
	public static boolean onArceuusSpellbook()
	{
		return getActiveSpellbook() == 3;
	}
	public static boolean isSpellDeathChargeOffCooldown()
	{
		return client.getVarbitValue(Varbits.DEATH_CHARGE_COOLDOWN) == 0;
	}
	public static boolean isSpellDeathChargeActive()
	{
		return client.getVarbitValue(Varbits.DEATH_CHARGE) == 1;
	}
	public static boolean isSpellResurrectThrallOffCooldown()
	{
		return client.getVarbitValue(Varbits.RESURRECT_THRALL_COOLDOWN) == 0;
	}
	public static boolean isSpellResurrectThrallActive()
	{
		return client.getVarbitValue(Varbits.RESURRECT_THRALL) == 1;
	}
	public static boolean isSpellInActiveSpellbook(WidgetInfoPlus spell)
	{
		if (spell.getGroupId() != WidgetIDPlus.SPELLBOOK_GROUP_ID)
		{
			return false;
		}
		
		int activeSpellbook = getActiveSpellbook();
		int spellChildId = spell.getChildId();
		int spellbook = -1;
		if (spellChildId < 76 && spellChildId > 5)
		{
			spellbook = 0;
		}
		else if (spellChildId < 101)
		{
			spellbook = 1;
		}
		else if (spellChildId < 145)
		{
			spellbook = 2;
		}
		else if (spellChildId < 190)
		{
			spellbook = 3;
		}
		return activeSpellbook == spellbook;
	}
	public static boolean isPietyUnlocked()
	{
		return client.getVarbitValue(3909) == 8;
	}
	public static boolean isRigourUnlocked()
	{
		return client.getVarbitValue(5451) == 1;
	}
	public static boolean isAuguryUnlocked()
	{
		return client.getVarbitValue(5452) == 1;
	}
	public static boolean isPreserveUnlocked()
	{
		return client.getVarbitValue(5453) == 1;
	}
}