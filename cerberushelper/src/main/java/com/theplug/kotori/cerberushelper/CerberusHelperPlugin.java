/*
 * Copyright (c) 2023 Kotori <https://github.com/OreoCupcakes/>
 * Copyright (c) 2019 Im2be <https://github.com/Im2be>
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

package com.theplug.kotori.cerberushelper;

import com.theplug.kotori.cerberushelper.domain.*;
import com.theplug.kotori.cerberushelper.overlays.CurrentAttackOverlay;
import com.theplug.kotori.cerberushelper.overlays.PrayerOverlay;
import com.theplug.kotori.cerberushelper.overlays.SceneOverlay;
import com.theplug.kotori.cerberushelper.overlays.UpcomingAttackOverlay;
import com.google.common.collect.ComparisonChain;
import com.google.inject.Provides;

import java.util.*;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import com.theplug.kotori.kotoriutils.methods.*;
import com.theplug.kotori.kotoriutils.rlapi.Spells;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemEquipmentStats;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStats;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@Singleton
@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "<html><font color=#6b8af6>[K]</font> Cerberus Helper</html>",
	enabledByDefault = false,
	description = "A helper plugin for the Cerberus boss. Comes with overlays and auto prayers.",
	tags = {"cerberus", "hellhound", "doggie", "ported","kotori"}
)
public class CerberusHelperPlugin extends Plugin
{
	private static final int ANIMATION_ID_IDLE = -1;
	private static final int ANIMATION_ID_STAND_UP = 4486;
	private static final int ANIMATION_ID_SIT_DOWN = 4487;
	private static final int ANIMATION_ID_FLINCH = 4489;
	private static final int ANIMATION_ID_RANGED = 4490;
	private static final int ANIMATION_ID_MELEE = 4491;
	private static final int ANIMATION_ID_LAVA = 4493;
	private static final int ANIMATION_ID_GHOSTS = 4494;
	private static final int ANIMATION_ID_DEATH = 4495;

	private static final int PROJECTILE_ID_MAGIC = 1242;
	private static final int PROJECTILE_ID_RANGE = 1245;

	private static final int GHOST_PROJECTILE_ID_RANGE = 34;
	private static final int GHOST_PROJECTILE_ID_MAGIC = 100;
	private static final int GHOST_PROJECTILE_ID_MELEE = 1248;

	private static final int PROJECTILE_ID_NO_FUCKING_IDEA = 15;
	private static final int PROJECTILE_ID_LAVA = 1247;

	private static final Set<Integer> REGION_IDS = Set.of(4883, 5140, 5395);

	private static final Set<Integer> CERBERUS_IDS = Set.of(NpcID.CERBERUS_ATTACKING, NpcID.CERBERUS_SITTING, NpcID.CERBERUS_RESETTING);

	private static final int ECHO_PROJECTILE_MAGIC = 3119;
	private static final int ECHO_PROJECTILE_RANGED = 3122;
	private static final int ECHO_PROJECTILE_LAVA = 3124;
	private static final int ECHO_GRAPHIC_LAVA_SPAWNING = 3124;
	private static final int ECHO_GRAPHIC_LAVA_POOL = 3123;

	private static final int ANIMATION_GHOST_RANGE_ATTACK = 8530;
	private static final int ANIMATION_GHOST_MAGIC_ATTACK = 8529;

	@Inject
	private Client client;

	@Inject
	private CerberusHelperConfig config;

	@Inject
	private ItemManager itemManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private SceneOverlay sceneOverlay;

	@Inject
	private PrayerOverlay prayerOverlay;

	@Inject
	private CurrentAttackOverlay currentAttackOverlay;

	@Inject
	private UpcomingAttackOverlay upcomingAttackOverlay;

	@Getter
	private final List<NPC> ghosts = new ArrayList<>();

	@Getter
	private final List<CerberusAttack> upcomingAttacks = new ArrayList<>();

	private final List<Long> tickTimestamps = new ArrayList<>();

	@Getter
	@Nullable
	private Prayer defaultPrayer = Prayer.PROTECT_FROM_MAGIC;

	@Getter
	@Nullable
	private Cerberus cerberus;

	private final Set<Projectile> cerberusProjectiles = new HashSet<>();

	@Getter
	private final Map<LocalPoint, Projectile> lavaProjectiles = new HashMap<>();

	@Getter
	private final Set<GraphicsObject> echoLavaGraphics = new HashSet<>();

	@Getter
	private int gameTick;

	private int tickTimestampIndex;

	@Getter
	private long lastTick;

	private boolean inArena;
	private boolean inAreaPastFlames;
	private boolean allPrayersDeactivated;
	private boolean ranFromLavaOnce;
	private boolean performAttackOnCerb;
	private boolean performAttackAfterPrayer;
	private WorldPoint lavaSafeTile = null;
	private int ticksSinceLavaDodge = 0;
	private int ghostAttacked = 0;

	private boolean sortedGhosts = false;

	@Provides
	CerberusHelperConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(CerberusHelperConfig.class);
	}

	@Override
	protected void startUp()
	{
		if (client.getGameState() != GameState.LOGGED_IN || !inCerberusRegion())
		{
			return;
		}

		init();
	}

	private void init()
	{
		inArena = true;

		if (cerberus == null)
		{
			/*
			This is for Leagues, as when you last recall back into the arena, the NpcSpawned event does not actually trigger causing cerberus to be null.
			 */
			for (NPC npc : NPCInteractions.getNpcs())
			{
				if (CERBERUS_IDS.contains(npc.getId()))
				{
					cerberus = new Cerberus(npc, config.killingEchoCerberus());
					break;
				}
			}
		}

		overlayManager.add(sceneOverlay);
		overlayManager.add(prayerOverlay);
		overlayManager.add(currentAttackOverlay);
		overlayManager.add(upcomingAttackOverlay);
	}

	@Override
	protected void shutDown()
	{
		inArena = false;
		inAreaPastFlames = false;

		overlayManager.remove(sceneOverlay);
		overlayManager.remove(prayerOverlay);
		overlayManager.remove(currentAttackOverlay);
		overlayManager.remove(upcomingAttackOverlay);

		ghosts.clear();
		upcomingAttacks.clear();
		tickTimestamps.clear();
		cerberusProjectiles.clear();
		lavaProjectiles.clear();
		echoLavaGraphics.clear();

		defaultPrayer = Prayer.PROTECT_FROM_MAGIC;

		cerberus = null;

		gameTick = 0;
		tickTimestampIndex = 0;
		lastTick = 0;

		ranFromLavaOnce = false;
		performAttackAfterPrayer = false;
		performAttackOnCerb = false;
		sortedGhosts = false;

		ghostAttacked = 0;

		lavaSafeTile = null;
		ticksSinceLavaDodge = 0;
	}

	@Subscribe
	private void onGameStateChanged(final GameStateChanged event)
	{
		final GameState gameState = event.getGameState();

		switch (gameState)
		{
			case LOGGED_IN:
				if (inCerberusRegion())
				{
					if (!inArena)
					{
						init();
					}
				}
				else
				{
					if (inArena)
					{
						shutDown();
					}
				}
				break;
			case HOPPING:
			case LOGIN_SCREEN:
				if (inArena)
				{
					shutDown();
				}
				break;
			default:
				break;
		}
	}

	@Subscribe
	private void onGameTick(final GameTick event)
	{
		if (!inArena || cerberus == null)
		{
			return;
		}

		inAreaPastFlames();

		if (tickTimestamps.size() <= tickTimestampIndex)
		{
			tickTimestamps.add(System.currentTimeMillis());
		}
		else
		{
			tickTimestamps.set(tickTimestampIndex, System.currentTimeMillis());
		}

		long min = 0;

		for (int i = 0; i < tickTimestamps.size(); ++i)
		{
			if (min == 0)
			{
				min = tickTimestamps.get(i) + 600 * ((tickTimestampIndex - i + 5) % 5);
			}
			else
			{
				min = Math.min(min, tickTimestamps.get(i) + 600 * ((tickTimestampIndex - i + 5) % 5));
			}
		}

		tickTimestampIndex = (tickTimestampIndex + 1) % 5;

		lastTick = min;

		++gameTick;

		if (gameTick % 10 == 3)
		{
			switch (config.overrideAutoAttackCalc())
			{
				case MAGIC:
				case MELEE:
				case MISSILES:
					defaultPrayer = config.overrideAutoAttackCalc().getPrayer();
					break;
				default:
					setAutoAttackPrayer();
					break;
			}
		}

		calculateUpcomingAttacks();

		if (ghosts.size() > 1)
		{
			int startTick = cerberus.isEchoVariant() ? 15 : 13;
			int endTick = cerberus.isEchoVariant() ? 35 : 17;

			if (!sortedGhosts)
			{
				/*
				 * First, sort by the southernmost ghost (e.g with lowest y).
				 * Then, sort by the westernmost ghost (e.g with lowest x).
				 * This will give use the current wave and order of the ghosts based on what ghost will attack first.
				 */
				ghosts.sort((a, b) -> ComparisonChain.start()
						.compare(a.getLocalLocation().getY(), b.getLocalLocation().getY())
						.compare(a.getLocalLocation().getX(), b.getLocalLocation().getX())
						.result());

				if (gameTick - cerberus.getLastGhostYellTick() >= startTick)
				{
					sortedGhosts = true;
				}
			}
			else
			{
				int lastYell = cerberus.getLastGhostYellTick();
				if (lastYell != 0 && gameTick - lastYell >= endTick)
				{
					sortedGhosts = false;
				}
			}
		}
		else
		{
			sortedGhosts = false;
		}

		handlePrayerInteractions();
		handleSpellInteractions();

		if (!config.killingEchoCerberus())
		{
			dodgeLavaSpecial();
		}

		clearProjectileArray();
	}

	private void clearProjectileArray()
	{
		cerberusProjectiles.removeIf(p -> p.getRemainingCycles() <= 0);
		lavaProjectiles.values().removeIf(p -> p.getRemainingCycles() <= 0);
		echoLavaGraphics.removeIf(GraphicsObject::finished);
	}

	@Subscribe(priority = Float.MAX_VALUE)
	private void onProjectileMoved(final ProjectileMoved event)
	{
		if (!inArena || cerberus == null)
		{
			return;
		}

		final Projectile projectile = event.getProjectile();

		final int hp = cerberus.getHp();

		final Phase expectedAttack = cerberus.getNextAttackPhase(1, hp);

		/*
			Store the projectile in the array
		 */
		if (cerberusProjectiles.contains(projectile))
		{
			return;
		}
		else
		{
			cerberusProjectiles.add(projectile);
		}

		switch (projectile.getId())
		{
			case PROJECTILE_ID_MAGIC:
			case ECHO_PROJECTILE_MAGIC:
				log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbProjectile={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "MAGIC");
				if (expectedAttack != Phase.TRIPLE)
				{
					cerberus.nextPhase(Phase.AUTO);
				}
				else
				{
					cerberus.setLastTripleAttack(Cerberus.Attack.MAGIC);
				}

				cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.MAGIC);
				break;
			case PROJECTILE_ID_RANGE:
			case ECHO_PROJECTILE_RANGED:
				log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbProjectile={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "RANGED");
				if (expectedAttack != Phase.TRIPLE)
				{
					cerberus.nextPhase(Phase.AUTO);
				}
				else
				{
					cerberus.setLastTripleAttack(Cerberus.Attack.RANGED);
				}

				cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.RANGED);
				break;
			case GHOST_PROJECTILE_ID_RANGE:
				if (!ghosts.isEmpty())
				{
					log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, ghostProjectile={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "RANGED");
				}
				ghostAttacked++;
				break;
			case GHOST_PROJECTILE_ID_MAGIC:
				if (!ghosts.isEmpty())
				{
					log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, ghostProjectile={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "MAGIC");
				}
				ghostAttacked++;
				break;
			case GHOST_PROJECTILE_ID_MELEE:
				if (!ghosts.isEmpty())
				{
					log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, ghostProjectile={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "MELEE");
				}
				ghostAttacked++;
				break;
			case PROJECTILE_ID_LAVA: //Lava
				lavaProjectiles.put(event.getPosition(), projectile);
				break;
			case PROJECTILE_ID_NO_FUCKING_IDEA:
			default:
				break;
		}
	}

	@Subscribe
	private void onAnimationChanged(final AnimationChanged event)
	{
		if (!inArena || cerberus == null)
		{
			return;
		}

		final Actor actor = event.getActor();

		final NPC npc = cerberus.getNpc();

		if (npc == null || actor != npc)
		{
			return;
		}

		final int animationId = npc.getAnimation();

		final int hp = cerberus.getHp();

		final Phase expectedAttack = cerberus.getNextAttackPhase(1, hp);

		switch (animationId)
		{
			case ANIMATION_ID_MELEE:
				log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbAnimation={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "MELEE");
				cerberus.setLastTripleAttack(null);
				cerberus.nextPhase(expectedAttack);
				cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.MELEE);
				break;
			case ANIMATION_ID_LAVA:
				log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbAnimation={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "LAVA");
				cerberus.nextPhase(Phase.LAVA);
				cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.LAVA);
				break;
			case ANIMATION_ID_GHOSTS:
				/*
					This is for the Echo variant which does the ghost animation 2 or 3 times to summon 9 ghosts. It puts a cooldown on what we would consider a valid ghost animation.
					We want to make sure at least 14 game ticks (how long it takes from start of first animation to first ghost attack) has passed before we mark down another ghost animation.
				 */
				if (gameTick < cerberus.getLastGhostYellTick() + 14)
				{
					break;
				}
				log.debug("gameTick={}, attack={}, cerbHp={}, expectedAttack={}, cerbAnimation={}", gameTick, cerberus.getPhaseCount() + 1, hp, expectedAttack, "GHOSTS");
				cerberus.nextPhase(Phase.GHOSTS);
				cerberus.setLastGhostYellTick(gameTick);
				cerberus.setLastGhostYellTime(System.currentTimeMillis());
				cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.GHOSTS);
				break;
			case ANIMATION_ID_SIT_DOWN:
			case ANIMATION_ID_STAND_UP:
				cerberus = new Cerberus(cerberus.getNpc(), config.killingEchoCerberus());
				gameTick = 0;
				lastTick = System.currentTimeMillis();
				upcomingAttacks.clear();
				tickTimestamps.clear();
				tickTimestampIndex = 0;
				cerberus.doProjectileOrAnimation(gameTick, Cerberus.Attack.SPAWN);
				break;
			case ANIMATION_ID_IDLE:
			case ANIMATION_ID_FLINCH:
			case ANIMATION_ID_RANGED:
				break;
			case ANIMATION_ID_DEATH:
				cerberus = null;
				ghosts.clear();
				cerberusProjectiles.clear();
				lavaProjectiles.clear();
				echoLavaGraphics.clear();
				ranFromLavaOnce = false;
				performAttackAfterPrayer = false;
				performAttackOnCerb = false;
				lavaSafeTile = null;
				ticksSinceLavaDodge = 0;
				ghostAttacked = 0;
				deactivatePrayers();
				break;
			default:
				log.debug("gameTick={}, animationId={} (UNKNOWN)", gameTick, animationId);
				break;
		}
	}

	@Subscribe(priority = Float.MAX_VALUE)
	private void onGraphicsObjectCreated(final GraphicsObjectCreated event)
	{
		if (!inArena)
		{
			return;
		}

		GraphicsObject graphicsObject = event.getGraphicsObject();

		if (graphicsObject.getId() == ECHO_GRAPHIC_LAVA_SPAWNING)
		{
			echoLavaGraphics.add(graphicsObject);
		}
	}

	@Subscribe
	private void onNpcSpawned(final NpcSpawned event)
	{
		if (!inArena)
		{
			return;
		}
		final NPC npc = event.getNpc();

		if (cerberus == null && npc != null && npc.getName() != null && npc.getName().toLowerCase().contains("cerberus"))
		{
			log.debug("onNpcSpawned name={}, id={}", npc.getName(), npc.getId());

			cerberus = new Cerberus(npc, config.killingEchoCerberus());

			gameTick = 0;
			tickTimestampIndex = 0;
			lastTick = System.currentTimeMillis();

			upcomingAttacks.clear();
			tickTimestamps.clear();
			
			allPrayersDeactivated = false;
		}

		if (cerberus == null)
		{
			return;
		}

		final Ghost ghost = Ghost.fromNPC(npc);

		if (ghost != null)
		{
			ghosts.add(npc);
		}
	}

	@Subscribe
	private void onNpcDespawned(final NpcDespawned event)
	{
		if (!inArena)
		{
			return;
		}
		final NPC npc = event.getNpc();

		if (npc != null && npc.getName() != null && npc.getName().toLowerCase().contains("cerberus"))
		{
			cerberus = null;
			ghosts.clear();
			cerberusProjectiles.clear();
			lavaProjectiles.clear();
			echoLavaGraphics.clear();
			ranFromLavaOnce = false;
			performAttackAfterPrayer = false;
			performAttackOnCerb = false;
			sortedGhosts = false;
			lavaSafeTile = null;
			ticksSinceLavaDodge = 0;
			ghostAttacked = 0;
			deactivatePrayers();

			log.debug("onNpcDespawned name={}, id={}", npc.getName(), npc.getId());
		}

		if (cerberus == null && !ghosts.isEmpty())
		{
			ghosts.clear();
			return;
		}

		ghosts.remove(event.getNpc());
	}

	private void calculateUpcomingAttacks()
	{
		upcomingAttacks.clear();

		if (cerberus == null)
		{
			return;
		}

		final Cerberus.Attack lastCerberusAttack = cerberus.getLastAttack();

		if (lastCerberusAttack == null)
		{
			return;
		}

		final int lastCerberusAttackTick = cerberus.getLastAttackTick();

		final int hp = cerberus.getHp();

		final Phase expectedPhase = cerberus.getNextAttackPhase(1, hp);
		final Phase lastPhase = cerberus.getLastAttackPhase();

		int tickDelay = 0;

		if (lastPhase != null)
		{
			tickDelay = lastPhase.getTickDelay();
			if (lastPhase == Phase.GHOSTS && config.killingEchoCerberus())
			{
				tickDelay+= 6;
			}
		}

		for (int tick = gameTick + 1; tick <= gameTick + 10; ++tick)
		{
			if (!config.killingEchoCerberus())
			{
				if (ghosts.size() == 3)
				{
					final Ghost ghost;

					if (cerberus.getLastGhostYellTick() == tick - 13)
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 3));
					}
					else if (cerberus.getLastGhostYellTick() == tick - 15)
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 2));
					}
					else if (cerberus.getLastGhostYellTick() == tick - 17)
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 1));
					}
					else
					{
						ghost = null;
					}

					if (ghost != null)
					{
						switch (ghost.getType())
						{
							case ATTACK:
								upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.GHOST_MELEE));
								break;
							case RANGED:
								upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.GHOST_RANGED));
								break;
							case MAGIC:
								upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.GHOST_MAGIC));
								break;
						}

						continue;
					}
				}
			}
			else
			{
				if (ghosts.size() == 9)
				{
					final Ghost ghost;

					if (cerberus.getLastGhostYellTick() == tick - 15) //good
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 9));
					}
					else if (cerberus.getLastGhostYellTick() == tick - 17)//good
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 8));
					}
					else if (cerberus.getLastGhostYellTick() == tick - 19)//good
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 7));
					}
					else if (cerberus.getLastGhostYellTick() == tick - 23)//2nd set
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 6));
					}
					else if (cerberus.getLastGhostYellTick() == tick - 25)//2nd set
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 5));
					}
					else if (cerberus.getLastGhostYellTick() == tick - 27)//2nd set
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 4));
					}
					else if (cerberus.getLastGhostYellTick() == tick - 31)//3rd set
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 3));
					}
					else if (cerberus.getLastGhostYellTick() == tick - 33)//3rd set
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 2));
					}
					else if (cerberus.getLastGhostYellTick() == tick - 35)//3rd set
					{
						ghost = Ghost.fromNPC(ghosts.get(ghosts.size() - 1));
					}
					else
					{
						ghost = null;
					}

					if (ghost != null)
					{
						switch (ghost.getType())
						{
							case ATTACK:
								upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.GHOST_MELEE));
								break;
							case RANGED:
								upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.GHOST_RANGED));
								break;
							case MAGIC:
								upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.GHOST_MAGIC));
								break;
						}

						continue;
					}
				}
			}

			if (expectedPhase == Phase.TRIPLE)
			{
				if (cerberus.getLastTripleAttack() == Cerberus.Attack.MAGIC)
				{
					if (lastCerberusAttackTick + 4 == tick)
					{
						upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.RANGED));
					}
					else if (lastCerberusAttackTick + 7 == tick)
					{
						upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MELEE));
					}
				}
				else if (cerberus.getLastTripleAttack() == Cerberus.Attack.RANGED)
				{
					if (lastCerberusAttackTick + 4 == tick)
					{
						upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MELEE));
					}
				}
				else if (cerberus.getLastTripleAttack() == null)
				{
					if (lastCerberusAttackTick + tickDelay + 2 == tick)
					{
						upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MAGIC));
					}
					else if (lastCerberusAttackTick + tickDelay + 5 == tick)
					{
						upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.RANGED));
					}
				}
			}
			else if (expectedPhase == Phase.AUTO)
			{
				if (lastCerberusAttackTick + tickDelay + 1 == tick)
				{
					if (!config.killingEchoCerberus())
					{
						if (defaultPrayer == Prayer.PROTECT_FROM_MAGIC)
						{
							upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MAGIC));
						} else if (defaultPrayer == Prayer.PROTECT_FROM_MISSILES)
						{
							upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.RANGED));
						} else if (defaultPrayer == Prayer.PROTECT_FROM_MELEE)
						{
							upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MELEE));
						}
					}
					else
					{
						int attacksInRotation = cerberus.getNonGhostAttacks() % 24;
						if (attacksInRotation < 8)
						{
							upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MAGIC));
						}
						else if (attacksInRotation < 16)
						{
							upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.RANGED));
						}
						else
                        {
                            upcomingAttacks.add(new CerberusAttack(tick, Cerberus.Attack.MELEE));
                        }
					}
				}
			}
		}
	}

	private void setAutoAttackPrayer()
	{
		int defenseStab = 0, defenseMagic = 0, defenseRange = 0;

		final ItemContainer itemContainer = client.getItemContainer(InventoryID.WORN);

		if (itemContainer != null)
		{
			final Item[] items = itemContainer.getItems();

			for (final Item item : items)
			{
				if (item == null)
				{
					continue;
				}

				final ItemStats itemStats = itemManager.getItemStats(item.getId());

				if (itemStats == null)
				{
					continue;
				}

				final ItemEquipmentStats itemStatsEquipment = itemStats.getEquipment();

				if (itemStatsEquipment == null)
				{
					continue;
				}

				defenseStab += itemStatsEquipment.getDstab();
				defenseMagic += itemStatsEquipment.getDmagic();
				defenseRange += itemStatsEquipment.getDrange();
			}
		}

		final int magicLvl = client.getBoostedSkillLevel(Skill.MAGIC);
		final int defenseLvl = client.getBoostedSkillLevel(Skill.DEFENCE);

		final int magicDefenseTotal = (int) (((double) magicLvl) * 0.7 + ((double) defenseLvl) * 0.3) + defenseMagic;
		final int rangeDefenseTotal = defenseLvl + defenseRange;

		int meleeDefenseTotal = defenseLvl + defenseStab;

		final Player player = client.getLocalPlayer();

		if (player != null)
		{
			final WorldPoint worldPointPlayer = client.getLocalPlayer().getWorldLocation();
			if (cerberus == null)
			{
				defaultPrayer = Prayer.PROTECT_FROM_MAGIC;
				return;
			}
			final WorldPoint worldPointCerberus = cerberus.getNpc().getWorldLocation();

			if (worldPointPlayer.getX() < worldPointCerberus.getX() - 1
				|| worldPointPlayer.getX() > worldPointCerberus.getX() + 5
				|| worldPointPlayer.getY() < worldPointCerberus.getY() - 1
				|| worldPointPlayer.getY() > worldPointCerberus.getY() + 5)
			{
				meleeDefenseTotal = Integer.MAX_VALUE;
			}
		}

		if (magicDefenseTotal <= rangeDefenseTotal && magicDefenseTotal <= meleeDefenseTotal)
		{
			defaultPrayer = Prayer.PROTECT_FROM_MAGIC;
		}
		else if (rangeDefenseTotal <= meleeDefenseTotal)
		{
			defaultPrayer = Prayer.PROTECT_FROM_MISSILES;
		}
		else
		{
			defaultPrayer = Prayer.PROTECT_FROM_MELEE;
		}
	}

	private boolean inCerberusRegion()
	{
		return REGION_IDS.contains(MiscUtilities.getPlayerRegionID());
	}

	private void inAreaPastFlames()
	{
		if (!inArena)
		{
			return;
		}

		if (!inAreaPastFlames || cerberus == null)
		{
			inAreaPastFlames = Arena.getArena(WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation())) != null;
		}
	}

	private boolean isCerberusNotAttackingYou()
	{
		if (cerberus == null)
		{
			return true;
		}

		Actor interact = cerberus.getNpc().getInteracting();
		if (interact instanceof Player)
		{
			return !interact.equals(client.getLocalPlayer());
		}
		return true;
	}

	public Prayer getUpcomingAttackPrayer()
	{
		if (cerberus == null || upcomingAttacks.isEmpty())
		{
			return null;
		}

		final CerberusAttack cerberusAttack = upcomingAttacks.get(0);

		if (cerberusAttack.getTick() > getGameTick() + 6)
		{
			return null;
		}

		if (cerberusAttack.getAttack() == Cerberus.Attack.AUTO)
		{
			return getDefaultPrayer();
		}

		return cerberusAttack.getAttack().getPrayer();
	}

	private void dodgeLavaSpecial()
	{
		if (cerberus == null || !config.dodgeLavaSpec())
		{
			return;
		}

		WorldPoint playerLoc = client.getLocalPlayer().getWorldLocation();

		if (lavaProjectiles.isEmpty())
		{
			ranFromLavaOnce = false;
		}
		else
		{
			if (!ranFromLavaOnce)
			{
				//Create dangerousTiles set
				Set<WorldPoint> dangerousTiles = new HashSet<>();
				for (Map.Entry<LocalPoint, Projectile> lava : lavaProjectiles.entrySet())
				{
					WorldPoint lavaLocalWorld = WorldPoint.fromLocal(client, lava.getKey());
					dangerousTiles.add(lavaLocalWorld);
					/*
						https://oldschool.runescape.wiki/w/Update:Summer_Sweep_Up:_Combat
						Summer Sweep Up has nerfed the AoE size from 3 down to 1

					WorldArea lavaSplash = new WorldArea(lavaLocalWorld.getX() - 1, lavaLocalWorld.getY() - 1, 3, 3, lavaLocalWorld.getPlane());
					dangerousTiles.addAll(lavaSplash.toWorldPointList());

					 */
				}
				dangerousTiles.addAll(cerberus.getNpc().getWorldArea().toWorldPointList());

				lavaSafeTile = BreadthFirstSearch.dodgeAoeAttack(client, dangerousTiles, cerberus.getNpc(), VarUtilities.getPlayerAttackStyle() == 0,
						config.preferMeleeDistance(), true);
				ReflectionLibrary.sceneWalk(lavaSafeTile, false);
				dangerousTiles.clear();
				ranFromLavaOnce = true;
				if (config.performAttackAfterLava())
				{
					performAttackOnCerb = true;
					return;
				}
				else
				{
					lavaSafeTile = null;
				}
			}
		}

		if (performAttackOnCerb)
		{
			ticksSinceLavaDodge++;

			if (playerLoc.equals(lavaSafeTile))
			{
				NPCInteractions.attackNpc(cerberus.getNpc());
				performAttackOnCerb = false;
				lavaSafeTile = null;
				ticksSinceLavaDodge = 0;
			}
			else if (ticksSinceLavaDodge > 4)
			{
				lavaSafeTile = null;
				performAttackOnCerb = false;
				ticksSinceLavaDodge = 0;
			}
		}
	}

	private void handlePrayerInteractions()
	{
		if (!inAreaPastFlames || isCerberusNotAttackingYou())
		{
			return;
		}

		prayDefensively();
		prayOffensively();
		prayPreserve();
		handlePrayerPotionDrinking();
	}

	private void prayDefensively()
	{
		if (!config.autoDefensivePrayers() || cerberus == null || upcomingAttacks.isEmpty())
		{
			return;
		}

		final Prayer prayerToInvoke = getUpcomingAttackPrayer();
		if (prayerToInvoke == null)
		{
			return;
		}

		PrayerInteractions.activatePrayer(prayerToInvoke);
	}

	private void prayOffensively()
	{
		if (!config.autoOffensivePrayers() || cerberus == null || upcomingAttacks.isEmpty())
		{
			return;
		}

		final Prayer prayer = config.offensivePrayerChoice().getPrayer();

		if (!config.killingEchoCerberus() && config.conservePrayerGhostSkip() && cerberus.getHp() > 400 && cerberus.getPhaseCount() < 15)
		{
			return;
		}

		PrayerInteractions.activatePrayer(prayer);
	}

	private void prayPreserve()
	{
		if (!config.keepPreservePrayerOn())
		{
			return;
		}

		PrayerInteractions.activatePrayer(Prayer.PRESERVE);
	}

	private void deactivatePrayers()
	{
		if (cerberus != null || allPrayersDeactivated)
		{
			return;
		}

		if (config.autoDefensivePrayers() || config.autoOffensivePrayers())
		{
			allPrayersDeactivated = PrayerInteractions.deactivatePrayers(config.keepPreservePrayerOn());
		}
	}

	private void handlePrayerPotionDrinking()
	{
		//Don't drink prayer doses if ghosts attacks are coming.
		if (!config.drinkPrayerPotions() || cerberus == null || upcomingAttacks.isEmpty())
		{
			return;
		}

		CerberusAttack attack = upcomingAttacks.get(0);
		if (attack != null && attack.getAttack().getPriority() == 2)
		{
			if (ghostAttacked > 0)
			{
				return;
			}
		}
		else
		{
			if (ghostAttacked > 0)
			{
				ghostAttacked = 0;
			}
		}

		final int valueToDrinkAt = config.prayerPointsToDrinkAt();

		if (client.getBoostedSkillLevel(Skill.PRAYER) <= valueToDrinkAt)
		{
			InventoryInteractions.drinkPrayerRestoreDose(true, true, false);
			if (config.performAttackAfterDrinkingPrayer())
			{
				performAttackAfterPrayer = true;
			}
		}
		else if (performAttackAfterPrayer && client.getBoostedSkillLevel(Skill.PRAYER) > valueToDrinkAt)
		{
			NPCInteractions.attackNpc(cerberus.getNpc());
			performAttackAfterPrayer = false;
		}
	}

	private void handleSpellInteractions()
	{
		if (!inAreaPastFlames)
		{
			return;
		}

		autoWardOfArceuus();
		autoDeathCharge();
		autoThralls();
		autoDemonicOffering();
	}
	
	private void autoDeathCharge()
	{
		if (!config.autoCastDeathCharge() || cerberus == null || isCerberusNotAttackingYou())
		{
			return;
		}
		
		if (cerberus.getHpPercentage() <= config.deathChargeHpPercentage())
		{
			SpellInteractions.castSpellDeathCharge();
		}
	}

	private void autoThralls()
	{
		if (!config.autoCastGreaterThrall() || cerberus == null || isCerberusNotAttackingYou())
		{
			return;
		}

		SpellInteractions.castResurrectGreaterThrall(config.thrallType().getSpell());
	}

	private void autoDemonicOffering()
	{
		if (!config.autoCastDemonicOffering() || cerberus != null)
		{
			return;
		}

		SpellInteractions.castDemonicOffering(config.demonicOfferingAmount());
	}

	private void autoWardOfArceuus()
	{
		if (!config.autoCastWardOfArceuus() || cerberus == null || isCerberusNotAttackingYou() || upcomingAttacks.isEmpty())
		{
			return;
		}

		if (upcomingAttacks.get(0).getAttack().name().toLowerCase().contains("ghost"))
		{
			SpellInteractions.castSpellWardOfArceuus();
		}
	}
}
