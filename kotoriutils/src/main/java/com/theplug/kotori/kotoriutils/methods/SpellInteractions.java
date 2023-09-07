package com.theplug.kotori.kotoriutils.methods;

import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import com.theplug.kotori.kotoriutils.rlapi.WidgetInfoPlus;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.Map;
import java.util.Set;

public class SpellInteractions
{
	private static final Client client = RuneLite.getInjector().getInstance(Client.class);

	public static void castSpell(WidgetInfoPlus spellWidget)
	{
		ReflectionLibrary.invokeMenuAction(-1, spellWidget.getId(), MenuAction.CC_OP.getId(), 1, -1);
	}

	public static void castResurrectGreaterThrall(WidgetInfoPlus spellWidget)
	{
		if (VarUtilities.onArceuusSpellbook() && !VarUtilities.isSpellResurrectThrallActive() && VarUtilities.isSpellResurrectThrallOffCooldown()
				&& client.getBoostedSkillLevel(Skill.PRAYER) > 5)
		{
			switch (spellWidget)
			{
				case SPELL_RESURRECT_GREATER_GHOST:
				case SPELL_RESURRECT_GREATER_SKELETON:
				case SPELL_RESURRECT_GREATER_ZOMBIE:
					castSpell(spellWidget);
					break;
			}
		}
	}
	
	public static void castSpellDeathCharge()
	{
		if (VarUtilities.onArceuusSpellbook() && !VarUtilities.isSpellDeathChargeActive() &&
				VarUtilities.isSpellDeathChargeOffCooldown() && client.getBoostedSkillLevel(Skill.PRAYER) >= 6)
		{
			castSpell(WidgetInfoPlus.SPELL_DEATH_CHARGE);
		}
	}

	public static void attackNpc(NPC npc)
	{
		if (npc != null)
		{
			ReflectionLibrary.invokeMenuAction(0, 0, MenuAction.NPC_SECOND_OPTION.getId(), npc.getIndex(), -1);
		}
	}
}
