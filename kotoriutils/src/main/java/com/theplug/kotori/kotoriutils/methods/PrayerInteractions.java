package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import com.theplug.kotori.kotoriutils.rlapi.PrayerExtended;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
		if (client.isPrayerActive(prayer) || client.getBoostedSkillLevel(Skill.PRAYER) <= 0 || client.getRealSkillLevel(Skill.PRAYER) < prayerExtended.getLevel())
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
		}

		int param1 = prayerExtended.getWidgetInfoPlus().getId();

		ReflectionLibrary.invokeMenuAction(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "", "", 0, 0);
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
		if (!client.isPrayerActive(prayer) || client.getBoostedSkillLevel(Skill.PRAYER) <= 0 || client.getRealSkillLevel(Skill.PRAYER) < prayerExtended.getLevel())
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
		}
			
		int param1 = prayerExtended.getWidgetInfoPlus().getId();
			
		ReflectionLibrary.invokeMenuAction(-1, param1, MenuAction.CC_OP.getId(), 1, -1);
		return true;
	}
	
	public static boolean deactivatePrayers(boolean keepPreserveOn)
	{
		int actionsTaken = 0;
		for (Prayer prayer : Prayer.values())
		{
			//Skip the Ruinous prayers as I don't support it
			if (prayer.name().contains("RP_"))
			{
				continue;
			}

			if (actionsTaken > 3)
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

	public static void oneTickFlickPrayers(Prayer... prayers)
	{
		int active = 0;
		for (Prayer prayer : Prayer.values())
		{
			//Skip the Ruinous prayers
			if (prayer.name().contains("RP_"))
			{
				continue;
			}

			if (client.isPrayerActive(prayer))
			{
				active++;
			}
		}
		if (active > 0)
		{
			for (Prayer p : prayers)
			{
				deactivatePrayer(p);
			}
		}
		for (Prayer p2 : prayers)
		{
			activatePrayer(p2);
		}
	}
}
