package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.rlapi.WidgetIDPlus;
import com.theplug.kotori.kotoriutils.rlapi.WidgetInfoPlus;
import net.runelite.api.*;
import net.runelite.client.RuneLite;

import java.util.HashSet;
import java.util.Set;

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
			Refer to https://github.com/runelite/runelite/blob/f5c04bf327ea7c72ac155d2e3342884670e23f69/runelite-client/src/main/java/net/runelite/client/plugins/attackstyles/WeaponType.java
			for the list of weapon types and their respective attack styles
		 */
		final int weaponType = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);
		final int attackStyle = client.getVarpValue(VarPlayer.ATTACK_STYLE);
		final int castingMode = client.getVarbitValue(Varbits.DEFENSIVE_CASTING_MODE);
		
		/*
			CurrentStyle Legend
			Melee = 0
			Ranged = 1
			Magic = 2
		 */
		int currentStyle = -1;

		/*
			Jagex "removed" type 22. They incremented everything up by 1, so old 22 became 23 and so on.
		 */
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
			case 18://Regular staves
			case 21://SOTD
				if (castingMode == 1)
				{
					currentStyle = 2;
				}
				else if (attackStyle == 4)
				{
					currentStyle = 2;
				}
				else
				{
					currentStyle = 0;
				}
				break;
			case 24://Powered staves (Tridents)
			case 29://Shadow
				currentStyle = 2;
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
			case 23://Godswords
			case 25:
			case 26:
			case 27:
			case 28:
			case 30://Keris Partisan
				currentStyle = 0;
				break;
		}
		
		return currentStyle;
	}

	// This is the new one used in RL now that weapon style is a global enum
	public static int getPlayerWeaponType()
	{
		final int equippedWeaponType = client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);
		final int attackStyle = client.getVarpValue(VarPlayer.ATTACK_STYLE);
		final int castingMode = client.getVarbitValue(Varbits.DEFENSIVE_CASTING_MODE);

		int weaponStyleEnum = client.getEnum(EnumID.WEAPON_STYLES).getIntValue(equippedWeaponType);
		//	This is to capture if the equipped weapon type isn't in the weapon styles enum.
		if (weaponStyleEnum == -1)
		{
			return -1;
		}
		int[] weaponStyleStructs = client.getEnum(weaponStyleEnum).getIntVals();

		StructComposition attackStyleStruct = client.getStructComposition(weaponStyleStructs[attackStyle]);
		String attackStyleName = attackStyleStruct.getStringValue(ParamID.ATTACK_STYLE_NAME).toLowerCase();

		switch (attackStyleName)
		{
			case "accurate":
			case "aggressive":
			case "controlled":
				return 0;
			case "ranging":
			case "longrange":
				return 1;
			case "casting":
				return 2;
			case "defensive":
				if (attackStyle == 4 && castingMode == 1)
				{
					return 2;
				}
				else
				{
					return 0;
				}
			case "other":
			default:
				return -1;
		}
	}

	public static boolean boostedSkillEqualGreater(Skill skill, int level)
	{
		return client.getBoostedSkillLevel(skill) >= level;
	}

	public static boolean skillEqualGreater(Skill skill, int level)
	{
		return client.getRealSkillLevel(skill) >= level;
	}

	public static Prayer bestStrengthBoostPrayer()
	{
		if (isPietyUnlocked() && skillEqualGreater(Skill.PRAYER, 70) && skillEqualGreater(Skill.DEFENCE, 70))
		{
			return Prayer.PIETY;
		}
		else if (isPietyUnlocked() && skillEqualGreater(Skill.PRAYER, 60) && skillEqualGreater(Skill.DEFENCE, 65))
		{
			return Prayer.CHIVALRY;
		}
		else if (skillEqualGreater(Skill.PRAYER, 31))
		{
			return Prayer.ULTIMATE_STRENGTH;
		}
		else if (skillEqualGreater(Skill.PRAYER, 13))
		{
			return Prayer.SUPERHUMAN_STRENGTH;
		}
		else if (skillEqualGreater(Skill.PRAYER, 4))
		{
			return Prayer.BURST_OF_STRENGTH;
		}
		else
		{
			return null;
		}
	}

	public static Prayer bestAttackBoostPrayer()
	{
		if (skillEqualGreater(Skill.PRAYER, 34))
		{
			return Prayer.INCREDIBLE_REFLEXES;
		}
		else if (skillEqualGreater(Skill.PRAYER, 16))
		{
			return Prayer.IMPROVED_REFLEXES;
		}
		else if (skillEqualGreater(Skill.PRAYER, 7))
		{
			return Prayer.CLARITY_OF_THOUGHT;
		}
		else
		{
			return null;
		}
	}

	public static Prayer bestDefenseBoostPrayer()
	{
		if (skillEqualGreater(Skill.PRAYER, 28))
		{
			return Prayer.STEEL_SKIN;
		}
		else if (skillEqualGreater(Skill.PRAYER, 10))
		{
			return Prayer.ROCK_SKIN;
		}
		else if (skillEqualGreater(Skill.PRAYER, 1))
		{
			return Prayer.THICK_SKIN;
		}
		else
		{
			return null;
		}
	}

	public static Prayer bestRangedPrayer()
	{
		if (isRigourUnlocked() && skillEqualGreater(Skill.PRAYER, 74) && skillEqualGreater(Skill.DEFENCE, 70))
		{
			return Prayer.RIGOUR;
		}
		else if (skillEqualGreater(Skill.PRAYER, 44))
		{
			return Prayer.EAGLE_EYE;
		}
		else if (skillEqualGreater(Skill.PRAYER, 26))
		{
			return Prayer.HAWK_EYE;
		}
		else if (skillEqualGreater(Skill.PRAYER, 8))
		{
			return Prayer.SHARP_EYE;
		}
		else
		{
			return null;
		}
	}

	public static Prayer bestMagicPrayer()
	{
		if (isAuguryUnlocked() && skillEqualGreater(Skill.PRAYER, 77) && skillEqualGreater(Skill.DEFENCE, 70))
		{
			return Prayer.AUGURY;
		}
		else if (skillEqualGreater(Skill.PRAYER, 45))
		{
			return Prayer.MYSTIC_MIGHT;
		}
		else if (skillEqualGreater(Skill.PRAYER, 27))
		{
			return Prayer.MYSTIC_LORE;
		}
		else if (skillEqualGreater(Skill.PRAYER, 9))
		{
			return Prayer.MYSTIC_WILL;
		}
		else
		{
			return null;
		}
	}

	public static Prayer bestOffensivePrayer()
	{
		switch (getPlayerAttackStyle())
		{
			case 0:
				return bestStrengthBoostPrayer();
			case 1:
				return bestRangedPrayer();
			case 2:
				return bestMagicPrayer();
			default:
				return null;
		}
	}

	public static Set<Prayer> bestOffensivePrayers(boolean useAttackPrayer, boolean useDefensePrayer)
	{
		int attackStyle = getPlayerAttackStyle();
		Set<Prayer> prayers = new HashSet<>();
		Prayer mainPrayer = null;

		switch (attackStyle)
		{
			case 0:
				mainPrayer = bestStrengthBoostPrayer();
				break;
			case 1:
				mainPrayer = bestRangedPrayer();
				break;
			case 2:
				mainPrayer = bestMagicPrayer();
				break;
		}

		if (mainPrayer == null)
		{
			return prayers;
		}

		prayers.add(mainPrayer);

		switch (mainPrayer)
		{
			case EAGLE_EYE:
			case HAWK_EYE:
			case SHARP_EYE:
			case MYSTIC_MIGHT:
			case MYSTIC_LORE:
			case MYSTIC_WILL:
				if (useDefensePrayer)
				{
					prayers.add(bestDefenseBoostPrayer());
				}
				break;
			case ULTIMATE_STRENGTH:
			case SUPERHUMAN_STRENGTH:
			case BURST_OF_STRENGTH:
				if (useDefensePrayer)
				{
					prayers.add(bestDefenseBoostPrayer());
				}
				if (useAttackPrayer)
				{
					prayers.add(bestAttackBoostPrayer());
				}
				break;
		}

		return prayers;
	}
}