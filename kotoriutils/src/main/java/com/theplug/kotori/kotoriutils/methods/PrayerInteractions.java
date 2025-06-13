package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import com.theplug.kotori.kotoriutils.rlapi.PrayerExtended;
import net.runelite.api.*;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.RuneLite;

public class PrayerInteractions
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	
	public static boolean activatePrayer(Prayer prayer)
	{
		if (prayer == null)
		{
			return false;
		}

		PrayerExtended prayerExtended = PrayerExtended.valueOf(prayer.name());
		//do nothing if prayer is already active or prayer points is at 0
		if (isActive(prayer) || client.getBoostedSkillLevel(Skill.PRAYER) <= 0 || client.getRealSkillLevel(Skill.PRAYER) < prayerExtended.getLevel())
		{
			return false;
		}

		switch (prayerExtended)
		{
			case CHIVALRY:
				if (!VarUtilities.isPietyUnlocked() && client.getRealSkillLevel(Skill.DEFENCE) < 65)
				{
					return false;
				}
				break;
			case PIETY:
				if (!VarUtilities.isPietyUnlocked() && client.getRealSkillLevel(Skill.DEFENCE) < 70)
				{
					return false;
				}
				break;
			case PRESERVE:
				if (!VarUtilities.isPreserveUnlocked())
				{
					return false;
				}
				break;
			case RIGOUR:
				if (!VarUtilities.isRigourUnlocked() && client.getRealSkillLevel(Skill.DEFENCE) < 70)
				{
					return false;
				}
				break;
			case AUGURY:
				if (!VarUtilities.isAuguryUnlocked() && client.getRealSkillLevel(Skill.DEFENCE) < 70)
				{
					return false;
				}
				break;
			case MYSTIC_VIGOUR:
				if (!VarUtilities.isMysticVigourUnlocked())
				{
					return false;
				}
				break;
			case DEADEYE:
				if (!VarUtilities.isDeadeyeUnlocked())
				{
					return false;
				}
				break;
		}

		int param1 = prayerExtended.getWidgetInfoPlus().getId();

		ReflectionLibrary.invokeMenuAction(-1, param1, MenuAction.CC_OP.getId(), 1, -1);
		return true;
	}
	
	public static boolean deactivatePrayer(Prayer prayer)
	{
		if (prayer == null)
		{
			return false;
		}

		PrayerExtended prayerExtended = PrayerExtended.valueOf(prayer.name());
		//do nothing if prayer is not active or prayer points is at 0
		if (!isActive(prayer) || client.getBoostedSkillLevel(Skill.PRAYER) <= 0 || client.getRealSkillLevel(Skill.PRAYER) < prayerExtended.getLevel())
		{
			return false;
		}
			
		switch (prayerExtended)
		{
			case CHIVALRY:
			case PIETY:
				if (!VarUtilities.isPietyUnlocked())
				{
					return false;
				}
				break;
			case PRESERVE:
				if (!VarUtilities.isPreserveUnlocked())
				{
					return false;
				}
				break;
			case RIGOUR:
				if (!VarUtilities.isRigourUnlocked())
				{
					return false;
				}
				break;
			case AUGURY:
				if (!VarUtilities.isAuguryUnlocked())
				{
					return false;
				}
				break;
			case DEADEYE:
				if (!VarUtilities.isDeadeyeUnlocked())
				{
					return false;
				}
				break;
			case MYSTIC_VIGOUR:
				if (!VarUtilities.isMysticVigourUnlocked())
				{
					return false;
				}
				break;
		}
			
		int param1 = prayerExtended.getWidgetInfoPlus().getId();
			
		ReflectionLibrary.invokeMenuAction(-1, param1, MenuAction.CC_OP.getId(), 1, -1);
		return true;
	}
	
	public static boolean deactivatePrayers(boolean keepPreserveOn, int actionsToDo)
	{
		int actionsTaken = 0;
		for (Prayer prayer : Prayer.values())
		{
			//Skip the Ruinous prayers as I don't support it
			if (prayer.name().contains("RP_"))
			{
				continue;
			}

			if (actionsTaken > actionsToDo)
			{
				return false;
			}
			if (prayer == Prayer.PRESERVE)
			{
				if (keepPreserveOn)
				{
					continue;
				}
			}
			boolean deactivated = deactivatePrayer(prayer);
			if (deactivated)
			{
				actionsTaken++;
			}
		}
		return true;
	}

	public static boolean deactivatePrayers(boolean keepPreserveOn)
	{
		return deactivatePrayers(keepPreserveOn, 3);
	}

	public static void oneTickFlickPrayers(boolean disableAll, Prayer... prayers)
	{
		//This is to check if there are any active prayers when you first start flicking.
		int active = 0;
		for (Prayer prayer : Prayer.values())
		{
			//Skip the Ruinous prayers
			if (prayer.name().contains("RP_"))
			{
				continue;
			}

			if (isActive(prayer))
			{
				active++;
			}
		}
		//	The way flicking works is you need to send a deactivation then activation within the same game tick.
		if (active > 0)
		{
			if (disableAll)
			{
				deactivatePrayers(false, 4);
			}
			else
			{
				for (Prayer p : prayers)
				{
					deactivatePrayer(p);
				}
			}
		}
		for (Prayer p2 : prayers)
		{
			activatePrayer(p2);
		}
	}

	public static void oneTickFlickPrayers(Prayer... prayers)
	{
		oneTickFlickPrayers(true, prayers);
	}

	public static boolean isActive(Prayer prayer)
	{
		boolean basicPrayerActive = client.isPrayerActive(prayer);
		boolean inLMS = client.getVarbitValue(VarbitID.BR_INGAME) != 0;
		switch (prayer)
		{
			case DEADEYE:
				boolean deadeye = client.getVarbitValue(VarbitID.PRAYER_DEADEYE_UNLOCKED) != 0;
				return basicPrayerActive && (deadeye && !inLMS);
			case MYSTIC_VIGOUR:
				boolean mysticvigour = client.getVarbitValue(VarbitID.PRAYER_MYSTIC_VIGOUR_UNLOCKED) != 0;
				return basicPrayerActive && (mysticvigour && !inLMS);
			default:
				return basicPrayerActive;
		}
	}
}