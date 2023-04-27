package com.theplug.kotori.kotoriutils.rlapi;

import net.runelite.api.Client;

public class VarUtilities
{
	public static final int ACTIVE_SPELLBOOK_VARBIT = 4070;
	public static final int SPELL_DEATH_CHARGE_COOLDOWN_VARBIT = 12138;
	
	public static int getActiveSpellbook(Client client)
	{
		/*
		 * 0 = Normal Spellbook
		 * 1 = Ancient Spellbook
		 * 2 = Lunar Spellbook
		 * 3 = Arceuus Spellbook
		 */
		return client.getVarbitValue(ACTIVE_SPELLBOOK_VARBIT);
	}
	public static boolean onStandardSpellbook(Client client)
	{
		return getActiveSpellbook(client) == 0;
	}
	public static boolean onAncientSpellbook(Client client)
	{
		return getActiveSpellbook(client) == 1;
	}
	public static boolean onLunarSpellbook(Client client)
	{
		return getActiveSpellbook(client) == 2;
	}
	public static boolean onArceuusSpellbook(Client client)
	{
		return getActiveSpellbook(client) == 3;
	}
	public static boolean isSpellDeathChargeOnCooldown(Client client)
	{
		return client.getVarbitValue(SPELL_DEATH_CHARGE_COOLDOWN_VARBIT) == 1;
	}
	public static boolean isSpellInActiveSpellbook(Client client, WidgetInfoPlus spell)
	{
		if (spell.getGroupId() != WidgetIDPlus.SPELLBOOK_GROUP_ID)
		{
			return false;
		}
		
		int activeSpellbook = getActiveSpellbook(client);
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
}