/*
 * Copyright (c) 2018, Jordan Atwood <jordan.atwood423@gmail.com>
 * Copyright (c) 2019, Ganom <https://github.com/Ganom>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.theplug.kotori.fightcaves;

import com.google.inject.Provides;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import com.theplug.kotori.kotoriutils.methods.MiscUtilities;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "<html><font color=#6b8af6>[P]</font> Fight Caves</html>",
	enabledByDefault = false,
	description = "Displays current and upcoming wave monsters in the Fight Caves and what to pray at TzTok-Jad",
	tags = {"bosses", "combat", "minigame", "overlay", "pve", "pvm", "jad", "fire", "cape", "wave", "ported", "kotori"}
)
public class FightCavePlugin extends Plugin
{
	static final int MAX_WAVE = 63;
	@Getter(AccessLevel.PACKAGE)
	static final List<EnumMap<WaveMonster, Integer>> WAVES = new ArrayList<>();
	private static final Pattern WAVE_PATTERN = Pattern.compile(".*Wave: (\\d+).*");
	private static final int FIGHT_CAVE_REGION = 9551;
	private static final int MAX_MONSTERS_OF_TYPE_PER_WAVE = 2;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private WaveOverlay waveOverlay;

	@Inject
	private FightCaveOverlay fightCaveOverlay;

	@Getter(AccessLevel.PACKAGE)
	private Set<FightCaveContainer> fightCaveContainer = new HashSet<>();
	@Getter(AccessLevel.PACKAGE)
	private int currentWave = -1;
	@Getter(AccessLevel.PACKAGE)
	private boolean validRegion;
	@Getter(AccessLevel.PACKAGE)
	private List<Integer> mageTicks = new ArrayList<>();
	@Getter(AccessLevel.PACKAGE)
	private List<Integer> rangedTicks = new ArrayList<>();
	@Getter(AccessLevel.PACKAGE)
	private List<Integer> meleeTicks = new ArrayList<>();

	public static final int TZTOK_JAD_RANGE_ATTACK = 2652;
	public static final int TZTOK_JAD_MELEE_ATTACK = 2655;
	public static final int TZTOK_JAD_MAGIC_ATTACK = 2656;
	public static final int TOK_XIL_RANGE_ATTACK = 2633;
	public static final int TOK_XIL_MELEE_ATTACK = 2628;
	public static final int KET_ZEK_MELEE_ATTACK = 2644;
	public static final int KET_ZEK_MAGE_ATTACK = 2647;
	public static final int MEJ_KOT_MELEE_ATTACK = 2637;
	public static final int MEJ_KOT_HEAL_ATTACK = 2639;
	public static final int IDLE = -1;

	private static int lastJadAnimation = -1;

	static String formatMonsterQuantity(final WaveMonster monster, final int quantity)
	{
		return String.format("%dx %s", quantity, monster);
	}

	@Provides
	FightCaveConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FightCaveConfig.class);
	}

	@Override
	public void startUp()
	{
		if (client.getGameState() != GameState.LOGGED_IN || !regionCheck())
		{
			return;
		}
		
		init();
	}

	@Override
	public void shutDown()
	{
		validRegion = false;
		overlayManager.remove(waveOverlay);
		overlayManager.remove(fightCaveOverlay);
		currentWave = -1;
		lastJadAnimation = -1;
		mageTicks.clear();
		rangedTicks.clear();
		meleeTicks.clear();
		fightCaveContainer.clear();
		WAVES.clear();
	}
	
	private void init()
	{
		validRegion = true;
		buildWaves();
		overlayManager.add(waveOverlay);
		overlayManager.add(fightCaveOverlay);
	}

	@Subscribe
	private void onChatMessage(ChatMessage event)
	{
		if (!validRegion)
		{
			return;
		}

		final Matcher waveMatcher = WAVE_PATTERN.matcher(event.getMessage());

		if (event.getType() != ChatMessageType.GAMEMESSAGE || !waveMatcher.matches())
		{
			return;
		}

		currentWave = Integer.parseInt(waveMatcher.group(1));
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event)
	{
		final GameState gameState = event.getGameState();
		
		if (gameState != GameState.LOGGED_IN)
		{
			return;
		}
		
		if (regionCheck())
		{
			if (!validRegion)
			{
				init();
			}
		}
		else
		{
			if (validRegion)
			{
				shutDown();
			}
		}
		fightCaveContainer.clear();
	}

	@Subscribe
	private void onNpcSpawned(NpcSpawned event)
	{
		if (!validRegion)
		{
			return;
		}

		NPC npc = event.getNpc();

		switch (npc.getId())
		{
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_3A:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_3B:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_4A:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_4B:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_5A:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_5B:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_BOSS:
			case NpcID.CLANCUP_TZHAAR_FIGHTCAVE_SWARM_BOSS:
				fightCaveContainer.add(new FightCaveContainer(npc));
				break;
		}
	}

	@Subscribe
	private void onNpcDespawned(NpcDespawned event)
	{
		if (!validRegion)
		{
			return;
		}

		NPC npc = event.getNpc();

		switch (npc.getId())
		{
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_3A:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_3B:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_4A:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_4B:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_5A:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_5B:
			case NpcID.TZHAAR_FIGHTCAVE_SWARM_BOSS:
			case NpcID.CLANCUP_TZHAAR_FIGHTCAVE_SWARM_BOSS:
				fightCaveContainer.removeIf(c -> c.getNpc() == npc);
				break;
		}
	}

	@Subscribe
	private void onGameTick(GameTick Event)
	{
		if (!validRegion)
		{
			return;
		}

		mageTicks.clear();
		rangedTicks.clear();
		meleeTicks.clear();

		for (FightCaveContainer npc : fightCaveContainer)
		{
			if (npc.getTicksUntilAttack() >= 0)
			{
				npc.setTicksUntilAttack(npc.getTicksUntilAttack() - 1);
			}

			for (int anims : npc.getAnimations())
			{
				if (anims == ReflectionLibrary.getNpcAnimationId(npc.getNpc()))
				{
					if (npc.getNpcName().equals("TzTok-Jad"))
					{
						if (anims == lastJadAnimation)
						{
							break;
						}

						lastJadAnimation = anims;

						if (npc.getTicksUntilAttack() < 1 && anims != -1)
						{
							npc.setTicksUntilAttack(npc.getAttackSpeed());
						}

						switch (anims)
						{
							case TZTOK_JAD_RANGE_ATTACK:
								npc.setAttackStyle(FightCaveContainer.AttackStyle.RANGE);
								break;
							case TZTOK_JAD_MAGIC_ATTACK:
								npc.setAttackStyle(FightCaveContainer.AttackStyle.MAGE);
								break;
							case TZTOK_JAD_MELEE_ATTACK:
								npc.setAttackStyle(FightCaveContainer.AttackStyle.MELEE);
								//Melee attack is instant damage so instead we're counting down until the next attack animation or melee attack.
								npc.setTicksUntilAttack(4);
								break;
						}
					}
					else if (npc.getTicksUntilAttack() < 1)
					{
						npc.setTicksUntilAttack(npc.getAttackSpeed());
					}
				}
			}

			switch (npc.getAttackStyle())
			{
				case RANGE:
					if (npc.getTicksUntilAttack() > 0)
					{
						rangedTicks.add(npc.getTicksUntilAttack());
					}
					break;
				case MELEE:
					if (npc.getTicksUntilAttack() > 0)
					{
						meleeTicks.add(npc.getTicksUntilAttack());
					}
					break;
				case MAGE:
					if (npc.getTicksUntilAttack() > 0)
					{
						mageTicks.add(npc.getTicksUntilAttack());
					}
					break;
			}
		}

		Collections.sort(mageTicks);
		Collections.sort(rangedTicks);
		Collections.sort(meleeTicks);
	}

	static void buildWaves()
	{
		final WaveMonster[] waveMonsters = WaveMonster.values();

		// Add wave 1, future waves are derived from its contents
		final EnumMap<WaveMonster, Integer> waveOne = new EnumMap<>(WaveMonster.class);
		waveOne.put(waveMonsters[0], 1);
		WAVES.add(waveOne);

		for (int wave = 1; wave < MAX_WAVE; wave++)
		{
			final EnumMap<WaveMonster, Integer> prevWave = WAVES.get(wave - 1).clone();
			int maxMonsterOrdinal = -1;

			for (int i = 0; i < waveMonsters.length; i++)
			{
				final int ordinalMonsterQuantity = prevWave.getOrDefault(waveMonsters[i], 0);

				if (ordinalMonsterQuantity == MAX_MONSTERS_OF_TYPE_PER_WAVE)
				{
					maxMonsterOrdinal = i;
					break;
				}
			}

			if (maxMonsterOrdinal >= 0)
			{
				prevWave.remove(waveMonsters[maxMonsterOrdinal]);
			}

			final int addedMonsterOrdinal = maxMonsterOrdinal >= 0 ? maxMonsterOrdinal + 1 : 0;
			final WaveMonster addedMonster = waveMonsters[addedMonsterOrdinal];
			final int addedMonsterQuantity = prevWave.getOrDefault(addedMonster, 0);

			prevWave.put(addedMonster, addedMonsterQuantity + 1);

			WAVES.add(prevWave);
		}
	}

	private boolean regionCheck()
	{
		return FIGHT_CAVE_REGION == MiscUtilities.getPlayerRegionID();
	}
}
