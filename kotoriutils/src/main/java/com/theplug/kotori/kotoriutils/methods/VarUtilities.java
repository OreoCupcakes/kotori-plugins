package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.rlapi.WidgetIDPlus;
import com.theplug.kotori.kotoriutils.rlapi.WidgetInfoPlus;
import net.runelite.api.Client;
import net.runelite.api.VarPlayer;
import net.runelite.api.Varbits;
import net.runelite.client.RuneLite;

import java.util.HashMap;
import java.util.Map;

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
	
	/*
		Returns the LocalPlayer's current attack style in the form of an int.
		0 = Melee Attack Style
		1 = Ranged Attack Style
		2 = Magic Attack Style
	 */
	public static int getPlayerAttackStyle()
	{
		/*
			Refer to https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/attackstyles/WeaponType.java
			for the list of weapon types and their respective attack styles
		 */
		int weaponType = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);
		int attackStyle = client.getVarpValue(VarPlayer.ATTACK_STYLE);
		
		/*
			CurrentStyle Legend
			Melee = 0
			Ranged = 1
			Magic = 2
		 */
		int currentStyle = -1;
		
		switch (weaponType)
		{
			//Ranged
			case 3:		//Bows
			case 5:		//Crossbows/Ballista
			case 7:		//Chinchompas
			case 19:	//Darts/Knives/Throwing axes/Blowpipe
				currentStyle = 1;
				break;
			//Magic
			case 23:	//Powered staves (Tridents)
				currentStyle = 2;
				break;
			case 18:	//Normal staves
			case 21:	//Staves of dead?
				if (attackStyle == 4)
				{
					currentStyle = 2;
				}
				else
				{
					currentStyle = 0;
				}
				break;
			case 6:		//Salamanders
				switch (attackStyle)
				{
					case 0:
						currentStyle = 0;
						break;
					case 1:
						currentStyle = 1;
						break;
					case 2:
						currentStyle = 2;
						break;
				}
				break;
			//Melee
			case 0:
			case 1:
			case 2:
			case 4:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 20:
			case 22:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
				currentStyle = 0;
				break;
		}
		
		return currentStyle;
	}
}