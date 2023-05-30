package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.reflection.InvokesLibrary;
import com.theplug.kotori.kotoriutils.rlapi.PrayerExtended;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.client.RuneLite;

import java.util.Objects;

public class PrayerInteractions
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	
	public static void activatePrayer(Prayer prayer)
	{
		PrayerExtended prayerExtended = PrayerExtended.valueOf(prayer.name());
		//do nothing if prayer is already active or prayer points is at 0
		if (client.isPrayerActive(prayer) || client.getBoostedSkillLevel(Skill.PRAYER) <= 0 || client.getRealSkillLevel(Skill.PRAYER) < prayerExtended.getLevel())
		{
			return;
		}
		
		switch (prayerExtended)
		{
			case CHIVALRY:
			case PIETY:
				if (!VarUtilities.isPietyUnlocked())
				{
					return;
				}
				break;
			case PRESERVE:
				if (!VarUtilities.isPreserveUnlocked())
				{
					return;
				}
				break;
			case RIGOUR:
				if (!VarUtilities.isRigourUnlocked())
				{
					return;
				}
				break;
			case AUGURY:
				if (!VarUtilities.isAuguryUnlocked())
				{
					return;
				}
				break;
		}
		
		int param1 = prayerExtended.getWidgetInfoPlus().getId();
		
		InvokesLibrary.invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1, "", "", 0, 0);
	}
	
	public static void deactivatePrayer(Prayer prayer)
	{
		PrayerExtended prayerExtended = PrayerExtended.valueOf(prayer.name());
		//do nothing if prayer is not active or prayer points is at 0
		if (!client.isPrayerActive(prayer) || client.getBoostedSkillLevel(Skill.PRAYER) <= 0 || client.getRealSkillLevel(Skill.PRAYER) < prayerExtended.getLevel())
		{
			return;
		}
		
		switch (prayerExtended)
		{
			case CHIVALRY:
			case PIETY:
				if (!VarUtilities.isPietyUnlocked())
				{
					return;
				}
				break;
			case PRESERVE:
				if (!VarUtilities.isPreserveUnlocked())
				{
					return;
				}
				break;
			case RIGOUR:
				if (!VarUtilities.isRigourUnlocked())
				{
					return;
				}
				break;
			case AUGURY:
				if (!VarUtilities.isAuguryUnlocked())
				{
					return;
				}
				break;
		}
		
		int param1 = prayerExtended.getWidgetInfoPlus().getId();
		
		InvokesLibrary.invoke(-1, param1, MenuAction.CC_OP.getId(), 1, -1);
	}
	
	public static boolean deactivatePrayers(boolean keepPreserveOn)
	{
		int actionsTaken = 0;
		for (Prayer prayer : Prayer.values())
		{
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
			deactivatePrayer(prayer);
			actionsTaken++;
		}
		return true;
	}
}
