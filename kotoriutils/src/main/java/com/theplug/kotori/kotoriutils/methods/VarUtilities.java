package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.rlapi.WidgetIDPlus;
import com.theplug.kotori.kotoriutils.rlapi.WidgetInfoPlus;
import net.runelite.api.*;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.gameval.VarPlayerID;
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
		return client.getVarbitValue(VarbitID.ARCEUUS_DEATH_CHARGE_COOLDOWN) == 0;
	}
	public static boolean isSpellDeathChargeActive()
	{
		return client.getVarbitValue(VarbitID.ARCEUUS_DEATH_CHARGE_ACTIVE) == 1;
	}
	public static boolean isSpellResurrectThrallOffCooldown()
	{
		return client.getVarbitValue(VarbitID.ARCEUUS_RESURRECTION_COOLDOWN) == 0;
	}
	public static boolean isSpellResurrectThrallActive()
	{
		return client.getVarbitValue(VarbitID.ARCEUUS_RESURRECTION_ACTIVE) == 1;
	}
	public static boolean isSpellWardOfArceuusOffCooldown()
	{
		return client.getVarbitValue(VarbitID.ARCEUUS_WARD_COOLDOWN) == 0;
	}
	public static boolean isSpellWardOfArceuusDisabled()
	{
		return client.getVarbitValue(VarbitID.BUFF_WARD_OF_ARCEUUS_DISABLED) == 1;
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
	public static boolean inLMS()
	{
		return client.getVarbitValue(5314) != 0;
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
	public static boolean isDeadeyeUnlocked()
	{
		return client.getVarbitValue(16097) == 1 && !inLMS();
	}
	public static boolean isMysticVigourUnlocked()
	{
		return client.getVarbitValue(16098) == 1 && !inLMS();
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
		final int weaponType = client.getVarbitValue(VarbitID.COMBAT_WEAPON_CATEGORY);
		final int attackStyle = client.getVarpValue(VarPlayerID.COM_MODE);
		final int castingMode = client.getVarbitValue(VarbitID.AUTOCAST_DEFMODE);
		
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
			case 3://Bows
			case 5://Crossbows/Ballista
			case 7://Chinchompas
			case 19://Thrown weapons, Darts/Knives/Throwing axes/Blowpipe
				currentStyle = 1;
				break;
			//Magic
			case 18://Regular staves
			case 21://Bladed staves, SOTD
			case 22://Blue moon spear
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
			case 29://Powered wands (Beta shadow)
				currentStyle = 2;
				break;
			case 6://Salamanders
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
			case 0://Fists, no weapon
			case 1://Axe
			case 2://Mauls, warhammers, dual wield hammers
			case 4://Claws
			case 8://Gun (fixed device)
			case 9://Longsword and scimitar
			case 10://2h swords
			case 11://Pickaxes
			case 12://Polearms, Halberds
			case 13://Polestaff
			case 14://Scythe
			case 15://Spear
			case 16://Mace
			case 17://Dagger and normal swords
			case 20://Whip
			case 23://Godswords
			case 25://Banner
			case 26://Some discontinued halberd like weapon?
			case 27://Bludgeon
			case 28://Bulwark
			case 30://Keris Partisan
				currentStyle = 0;
				break;
		}
		
		return currentStyle;
	}

	// This is the new one used in RL now that weapon style is a global enum
	public static int getPlayerWeaponType()
	{
		final int equippedWeaponType = client.getVarbitValue(VarbitID.COMBAT_WEAPON_CATEGORY);
		final int attackStyle = client.getVarpValue(VarPlayerID.COM_MODE);
		final int castingMode = client.getVarbitValue(VarbitID.AUTOCAST_DEFMODE);

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
		else if (isDeadeyeUnlocked() && skillEqualGreater(Skill.PRAYER, 62))
		{
			return Prayer.DEADEYE;
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
		else if (isMysticVigourUnlocked() && skillEqualGreater(Skill.PRAYER, 63))
		{
			return Prayer.MYSTIC_VIGOUR;
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