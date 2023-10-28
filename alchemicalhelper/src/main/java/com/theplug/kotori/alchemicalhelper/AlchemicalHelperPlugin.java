/*
 * Copyright (c) 2019, Lucas <https://github.com/lucwousin>
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
package com.theplug.kotori.alchemicalhelper;

import com.google.inject.Provides;

import java.util.*;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.theplug.kotori.alchemicalhelper.entity.Hydra;
import com.theplug.kotori.alchemicalhelper.entity.HydraPhase;
import com.theplug.kotori.alchemicalhelper.overlay.AttackOverlay;
import com.theplug.kotori.alchemicalhelper.overlay.SceneOverlay;
import com.theplug.kotori.kotoriutils.KotoriUtils;
import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import com.theplug.kotori.kotoriutils.methods.*;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import com.theplug.kotori.alchemicalhelper.overlay.PrayerOverlay;
import net.runelite.client.ui.overlay.OverlayManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "<html><font color=#6b8af6>[K]</font> Alchemical Helper</html>",
	enabledByDefault = false,
	description = "A plugin for the Alchemical Hydra boss. Overlays and automation for prayer and dodging special attacks.",
	tags = {"alchemical", "hydra", "ported","kotori", "helper", "automation"}
)
public class AlchemicalHelperPlugin extends Plugin
{
	private static final String MESSAGE_NEUTRALIZE = "The chemicals neutralise the Alchemical Hydra's defences!";
	private static final String MESSAGE_STUN = "The Alchemical Hydra temporarily stuns you.";

	private static final int[] HYDRA_REGIONS = {5279, 5280, 5535, 5536};
	private static final WorldArea HYDRA_LAIR = new WorldArea(1354, 10254, 28, 28, 0);

    public static final WorldPoint LIGHTNING_SAFESPOT_1 = new WorldPoint(1358, 10278, 0);
	private static final WorldPoint LIGHTNING_SAFESPOT_2 = new WorldPoint(1359, 10277, 0);
	public static final WorldPoint LIGHTNING_SAFESPOT_3 = new WorldPoint(1359, 10278, 0);

	private static final Set<WorldPoint> LIGHTNING_DANGER_TILES = Set.of(
			new WorldPoint(1360, 10278, 0), new WorldPoint(1360, 10277, 0), new WorldPoint(1360, 10276, 0),
			new WorldPoint(1359, 10276, 0), new WorldPoint(1358, 10276, 0));

	public static final WorldPoint FLAME_SAFESPOT_1 = new WorldPoint(1364, 10271, 0);
	private static final WorldPoint FLAME_SAFESPOT_2 = new WorldPoint(1363, 10270, 0);

	public static final int BIG_ASS_GUTHIX_SPELL = 1774;
	public static final int BIG_ASS_GREY_ENTANGLE = 1788;
	public static final int BIG_ASS_WHITE_ENTANGLE = 1789;
	public static final int BIG_SUPERHEAT = 1800;
	public static final int BIG_SPEC_TRANSFER = 1959;

	@Inject
	private Client client;
	
	@Getter
	@Inject
	private ClientThread clientThread;
	
	@Inject
	private AlchemicalHelperConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AttackOverlay attackOverlay;

	@Inject
	private SceneOverlay sceneOverlay;

	@Inject
	private PrayerOverlay prayerOverlay;

	private boolean atHydra;
	private boolean inLair;

	@Getter
	private Hydra hydra;

	public static final int HYDRA_1_1 = 8237;
	public static final int HYDRA_1_2 = 8238;
	public static final int HYDRA_LIGHTNING = 8241;
	public static final int HYDRA_2_1 = 8244;
	public static final int HYDRA_2_2 = 8245;
	public static final int HYDRA_FIRE = 8248;
	public static final int HYDRA_3_1 = 8251;
	public static final int HYDRA_3_2 = 8252;
	public static final int HYDRA_4_1 = 8257;
	public static final int HYDRA_4_2 = 8258;

	@Getter
	int fountainTicks = -1;
	int lastFountainAnim = -1;

	int currentAnimation;
	int lastUniqueAnimation;

	@Getter
	private final Map<LocalPoint, Projectile> poisonProjectiles = new HashMap<>();

	@Getter
	private final Set<WorldPoint> dangerousTiles = new HashSet<>();

	@Getter
	private final Set<GraphicsObject> poisonObjects = new HashSet<>();

	@Getter
	private final Set<GraphicsObject> lightningObjects = new HashSet<>();

	@Getter
	private final Set<GraphicsObject> flameObjects = new HashSet<>();

	private int lastAttackTick = -1;

	@Getter
	private final Set<GameObject> vents = new HashSet<>();
	
	private boolean offensivePrayerActivated;
	private boolean preservePrayerActivated;
	private boolean allPrayersDeactived;
	private WorldPoint poisonSafeTile = null;
	private boolean ranFromPoisonOnce;
	private int timeSincePoisonDodge = 0;
	private boolean performAttackOnHydra;
	private boolean performAttackAfterDrink;
	private int lightningSkipState = 0;
	public boolean inLightningSafeSpot;
	private int flameSkipState = 0;
	private int flameSpecialAnimations = 0;
	private Prayer lastActivatedProtectionPrayer = null;

	@Provides
	AlchemicalHelperConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(AlchemicalHelperConfig.class);
	}

	@Override
	protected void startUp()
	{
		if (client.getGameState() == GameState.LOGGED_IN && isInHydraRegion())
		{
			init();
		}
	}

	private void init()
	{
		atHydra = true;

		addOverlays();

		for (final NPC npc : client.getNpcs())
		{
			onNpcSpawned(new NpcSpawned(npc));
		}
	}

	@Override
	protected void shutDown()
	{
		atHydra = false;
		inLair = false;

		removeOverlays();

		hydra = null;
		poisonProjectiles.clear();
		dangerousTiles.clear();
		poisonObjects.clear();
		lightningObjects.clear();
		flameObjects.clear();
		lastAttackTick = -1;
		fountainTicks = -1;
		vents.clear();
		lastFountainAnim = -1;
		currentAnimation = -1;
		lastUniqueAnimation = -1;

		offensivePrayerActivated = false;
		preservePrayerActivated = false;
		allPrayersDeactived = false;
		ranFromPoisonOnce = false;
		performAttackOnHydra = false;
		performAttackAfterDrink = false;
		inLightningSafeSpot = false;
		lightningSkipState = 0;
		flameSkipState = 0;
		flameSpecialAnimations = 0;
		timeSincePoisonDodge = 0;
		lastActivatedProtectionPrayer = null;
		poisonSafeTile = null;
	}

	@Subscribe
	private void onGameStateChanged(final GameStateChanged event)
	{
		final GameState gameState = event.getGameState();

		switch (gameState)
		{
			case LOGGED_IN:
				if (isInHydraRegion())
				{
					if (!atHydra)
					{
						init();
					}
				}
				else
				{
					if (atHydra)
					{
						shutDown();
					}
				}
				break;
			case HOPPING:
			case LOGIN_SCREEN:
				if (atHydra)
				{
					shutDown();
				}
			default:
				break;
		}
	}

	@Subscribe
	private void onGameObjectSpawned(GameObjectSpawned event)
	{
		if (!atHydra)
		{
			return;
		}
		GameObject gameobject = event.getGameObject();
		int id = gameobject.getId();
		if (id == ObjectID.CHEMICAL_VENT_RED || id == ObjectID.CHEMICAL_VENT_GREEN || id == ObjectID.CHEMICAL_VENT_BLUE)
		{
			vents.add(gameobject);
		}
	}

	@Subscribe
	private void onGameObjectDespawned(GameObjectDespawned event)
	{
		if (!atHydra)
		{
			return;
		}
		GameObject gameobject = event.getGameObject();
		vents.remove(gameobject);
	}


	@Subscribe
	private void onGameTick(final GameTick event)
	{
		if (!atHydra)
		{
			return;
		}
		
		if (!inLair)
		{
			inLair = isInHydraLair();
		}
		
		attackOverlay.decrementStunTicks();
		updateVentTicks();
		if (!poisonObjects.isEmpty())
		{
			poisonObjects.removeIf(GraphicsObject::finished);
		}
		if (!flameObjects.isEmpty())
		{
			flameObjects.removeIf(GraphicsObject::finished);
		}
		if (!lightningObjects.isEmpty())
		{
			lightningObjects.removeIf(GraphicsObject::finished);
		}

		handlePhaseSpecials();
		handlePrayerInteractions();
	}

	@Subscribe
	private void onClientTick(final ClientTick event)
	{
		if (!atHydra)
		{
			return;
		}

		//Get the current animation that hydra is playing.
		//onClientTick is checks faster than onGameTick
		onAnimationChanged();
	}

	private void onAnimationChanged()
	{
		if (hydra == null)
		{
			return;
		}

		currentAnimation = ReflectionLibrary.getNpcAnimationId(hydra.getNpc());

		//Emulating onAnimationChanged because of RL mixin's blocking Hydra animations.
		if (lastUniqueAnimation == currentAnimation)
		{
			return;
		}

		lastUniqueAnimation = currentAnimation;

		final HydraPhase phase = hydra.getPhase();

		//Handle Phase Changes
		if ((lastUniqueAnimation == phase.getDeathAnimation2() && phase != HydraPhase.FLAME)
				|| (lastUniqueAnimation == phase.getDeathAnimation1() && phase == HydraPhase.FLAME))
		{
			switch (phase)
			{
				case POISON:
					hydra.changePhase(HydraPhase.LIGHTNING);
					poisonSafeTile = null;
					break;
				case LIGHTNING:
					hydra.changePhase(HydraPhase.FLAME);
					lightningSkipState = 0;
					break;
				case FLAME:
					hydra.changePhase(HydraPhase.ENRAGED);
					flameSkipState = 0;
					break;
				case ENRAGED:
					// NpcDespawned event does not fire for Hydra in between kills; must use death animation.
					hydra = null;

					poisonProjectiles.clear();
					flameObjects.clear();
					poisonObjects.clear();
					lightningObjects.clear();
					dangerousTiles.clear();
					currentAnimation = -1;
					lastUniqueAnimation = -1;
					lightningSkipState = 0;
					flameSkipState = 0;
					flameSpecialAnimations = 0;
					timeSincePoisonDodge = 0;
					performAttackOnHydra = false;
					performAttackAfterDrink = false;
					ranFromPoisonOnce = false;
					inLightningSafeSpot = false;
					lastActivatedProtectionPrayer = null;
					offensivePrayerActivated = false;
					poisonSafeTile = null;
					break;
			}

			return;
		}
		//Handle Special Attack Animations
		else if (lastUniqueAnimation == phase.getSpecialAnimationId() && phase.getSpecialAnimationId() != 0)
		{
			switch (phase)
			{
				case FLAME:
					//Keep track of flame special animations, 3 animations means its casting the chasing flame
					//Set the next special attack after the whole flame special is done animating
					//Reset it back to 0, in case flame skip got overridden, the next time it sees the first of three flame animations from the special
					if (flameSpecialAnimations == 3)
					{
						hydra.setNextSpecial();
						flameSpecialAnimations = 0;
					}
					flameSpecialAnimations++;
					break;
				case LIGHTNING:
					//Reset lightning skip variables when it casts the animation
					lightningSkipState = 0;
					inLightningSafeSpot = false;
					break;
				default:
					hydra.setNextSpecial();
					break;
			}
		}

		if (!poisonProjectiles.isEmpty())
		{
			poisonProjectiles.values().removeIf(p -> p.getEndCycle() < client.getGameCycle());
		}
	}

	public boolean isSpecialAttack()
	{
		final HydraPhase phase = hydra.getPhase();

		switch (phase)
		{
			case FLAME:
				final NPC npc = hydra.getNpc();
				return hydra.getNextSpecialRelative() == 0 || (npc != null && npc.getInteracting() == null);
			case POISON:
			case LIGHTNING:
			case ENRAGED:
				return hydra.getNextSpecialRelative() == 0;
		}

		return false;
	}

	private void updateVentTicks()
	{
		if (fountainTicks > 0)
		{
			fountainTicks--;
			if (fountainTicks == 0)
			{
				fountainTicks = 8;
			}
		}

		if (!vents.isEmpty())
		{
			for (final GameObject vent : vents)
			{
				int animation = getAnimation(vent);
				if (animation == 8279 && lastFountainAnim == 8280)
				{
					fountainTicks = 2;
				}
				lastFountainAnim = animation;
				break; // all vents trigger at same time so dont bother going through them all
			}
		}
	}

	int getAnimation(GameObject gameObject)
	{
		final DynamicObject dynamicObject = (DynamicObject) gameObject.getRenderable();
		return (int) (dynamicObject.getAnimation() == null ? -1 : dynamicObject.getAnimation().getId());
	}

	@Subscribe
	private void onNpcSpawned(final NpcSpawned event)
	{
		if (!atHydra)
		{
			return;
		}
		final NPC npc = event.getNpc();

		if (npc.getId() == NpcID.ALCHEMICAL_HYDRA)
		{
			hydra = new Hydra(npc);
			if (client.isInInstancedRegion() && fountainTicks == -1) //handles the initial hydra spawn when your in the lobby but havent gone through the main doors
			{
				fountainTicks = 11;
			}
			allPrayersDeactived = false;
		}
	}
	
	@Subscribe
	private void onProjectileMoved(final ProjectileMoved event)
	{
		if (!atHydra)
		{
			return;
		}
		final Projectile projectile = event.getProjectile();

		if (hydra == null || client.getGameCycle() >= projectile.getStartCycle())
		{
			return;
		}

		final int projectileId = projectile.getId();

		if (hydra.getPhase().getSpecialProjectileId() == projectileId)
		{
			if (hydra.getAttackCount() >= hydra.getNextSpecial())
			{
				hydra.setNextSpecial();
			}

			poisonProjectiles.put(event.getPosition(), projectile);
		}
		else if (client.getTickCount() != lastAttackTick
			&& (projectileId == Hydra.AttackStyle.MAGIC.getProjectileID() || projectileId == Hydra.AttackStyle.RANGED.getProjectileID()))
		{
			hydra.handleProjectile(projectileId);

			lastAttackTick = client.getTickCount();
		}
	}

	@Subscribe
	private void onGraphicsObjectCreated(final GraphicsObjectCreated event)
	{
		if (!atHydra)
		{
			return;
		}

		GraphicsObject graphicsObject = event.getGraphicsObject();

		switch(graphicsObject.getId())
		{
			case 1654:
			case 1655:
			case 1656:
			case 1657:
			case 1658:
			case 1659:
				poisonObjects.add(graphicsObject);
				break;
			case 1666:
				lightningObjects.add(graphicsObject);
				break;
			case 1668:
				flameObjects.add(graphicsObject);
				break;
		}
	}

	@Subscribe
	private void onChatMessage(final ChatMessage event)
	{
		if (!atHydra)
		{
			return;
		}
		final ChatMessageType chatMessageType = event.getType();

		if (chatMessageType != ChatMessageType.SPAM && chatMessageType != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

		final String message = event.getMessage();

		if (message.equals(MESSAGE_NEUTRALIZE))
		{
			clientThread.invokeLater(() ->
			{
				hydra.setImmunity(false);
			});
		}
		else if (message.equals(MESSAGE_STUN))
		{
			attackOverlay.setStunTicks();
		}
	}

	private void handlePhaseSpecials()
	{
		if (!atHydra)
		{
			return;
		}

		if (hydra == null)
		{
			return;
		}

		HydraPhase phase = hydra.getPhase();
		//WorldLocation returns the "true" tile from the server. LocalLocation is the client tile which is delayed.
		WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();

		switch (phase)
		{
			case POISON:
			case ENRAGED:
				if (config.runFromPoisonSpecial())
				{
					//If poisonProjectiles are not present then it's not doing the special attack
					if (poisonProjectiles.isEmpty())
					{
						ranFromPoisonOnce = false;
					}
					else
					{
						if (!ranFromPoisonOnce)
						{
							//Build the set of dangerousTiles consisting of flame, poison splash, and hydra tiles
							for (GraphicsObject flame : flameObjects)
							{
								dangerousTiles.add(WorldPoint.fromLocal(client, flame.getLocation()));
							}
							for (Map.Entry<LocalPoint, Projectile> poison : poisonProjectiles.entrySet())
							{
								WorldPoint poisonLocalWorld = WorldPoint.fromLocal(client, poison.getKey());
								WorldArea splashArea = new WorldArea(poisonLocalWorld.getX() - 1, poisonLocalWorld.getY() - 1, 3, 3, poisonLocalWorld.getPlane());
								dangerousTiles.addAll(splashArea.toWorldPointList());
							}
                            assert hydra.getNpc() != null;
                            dangerousTiles.add(hydra.getNpc().getWorldArea().toWorldPoint());

							poisonSafeTile = BreadthFirstSearch.dodgeAoeAttack(client, dangerousTiles, hydra.getNpc(),
									VarUtilities.getPlayerAttackStyle() == 0, config.favorMeleeDistanceDuringPoison(), flameObjects.isEmpty());
							ReflectionLibrary.sceneWalk(poisonSafeTile, false);
							dangerousTiles.clear();
							ranFromPoisonOnce = true;
							if (config.performAttackAfterPoison())
							{
								performAttackOnHydra = true;
							}
							else
							{
								poisonSafeTile = null;
							}
						}
					}

					//Attack hydra after running from the poison and reaching the safe tile.
					if (performAttackOnHydra)
					{
						timeSincePoisonDodge++;
						/*
							Don't attack if there are still flame walls present in enrage phase and the player is using melee.
							This covers the edge case of there being no safe melee distance tiles due to the flame walls and poison splash
						 */
						if (playerLocation.equals(poisonSafeTile))
						{
							if (VarUtilities.getPlayerAttackStyle() != 0 || flameObjects.isEmpty())
							{
								SpellInteractions.attackNpc(hydra.getNpc());
								performAttackOnHydra = false;
								poisonSafeTile = null;
								timeSincePoisonDodge = 0;
							}
						}
						else if (timeSincePoisonDodge > 4)
						{
							poisonSafeTile = null;
							performAttackOnHydra = false;
							timeSincePoisonDodge = 0;
						}
					}
				}
				break;
			case LIGHTNING:
				if (config.doLightningSkip())
				{
					if (VarUtilities.getPlayerAttackStyle() != 1)
					{
						return;
					}

					Collection<WorldPoint> lightningSafeSpot1LocalWorlds = WorldPoint.toLocalInstance(client, LIGHTNING_SAFESPOT_1);
					Collection<WorldPoint> lightningSafeSpot2LocalWorlds = WorldPoint.toLocalInstance(client, LIGHTNING_SAFESPOT_2);
					Collection<WorldPoint> lightningSafeSpot3LocalWorlds = WorldPoint.toLocalInstance(client, LIGHTNING_SAFESPOT_3);

					switch (lightningSkipState)
					{
						case 0:
							if (lightningSafeSpot1LocalWorlds.contains(playerLocation))
							{
								for (GraphicsObject lightning : lightningObjects)
								{
									WorldPoint lightningLoc = WorldPoint.fromLocalInstance(client, lightning.getLocation());
									if (LIGHTNING_DANGER_TILES.contains(lightningLoc))
									{
										ReflectionLibrary.sceneWalk(LIGHTNING_SAFESPOT_2, true);
										lightningSkipState = 1;
										return;
									}
								}
							}
							break;
						case 1:
							if (lightningSafeSpot2LocalWorlds.contains(playerLocation))
							{
								ReflectionLibrary.sceneWalk(LIGHTNING_SAFESPOT_3, true);
								lightningSkipState = 2;
								if (config.performAttackAfterLightning())
								{
									performAttackOnHydra = true;
								}
							}
							break;
						case 2:
							if (lightningSafeSpot3LocalWorlds.contains(playerLocation))
							{
								if (performAttackOnHydra)
								{
									SpellInteractions.attackNpc(hydra.getNpc());
									performAttackOnHydra = false;
								}
								inLightningSafeSpot = true;
								lightningSkipState = 3;
							}
							break;
						case 3:
							if (!lightningSafeSpot3LocalWorlds.contains(playerLocation))
							{
								inLightningSafeSpot = false;
								lightningSkipState = 0;
							}
							break;
					}
				}
				break;
			case FLAME:
				if (config.doFlameSkip())
				{
					if (flameSpecialAnimations != 3)
					{
						return;
					}

					Collection<WorldPoint> flameSafeSpot1LocalWorlds = WorldPoint.toLocalInstance(client, FLAME_SAFESPOT_1);
					Collection<WorldPoint> flameSafeSpot2LocalWorlds = WorldPoint.toLocalInstance(client, FLAME_SAFESPOT_2);

					switch (flameSkipState)
					{
						case 0:
							if (flameSafeSpot1LocalWorlds.contains(playerLocation))
							{
								ReflectionLibrary.sceneWalk(FLAME_SAFESPOT_2, true);
								flameSkipState = 1;
								if (config.performAttackAfterFlame())
								{
									performAttackOnHydra = true;
								}
							}
							break;
						case 1:
							if (flameSafeSpot2LocalWorlds.contains(playerLocation))
							{
								if (performAttackOnHydra)
								{
									SpellInteractions.attackNpc(hydra.getNpc());
									performAttackOnHydra = false;
								}
								flameSkipState = 2;
							}
							break;
						case 2:
							flameSpecialAnimations = 0;
							flameSkipState = 0;
							break;
					}
				}
				break;
		}
	}
	
	private void handlePrayerInteractions()
	{
		if (!atHydra || !inLair)
		{
			return;
		}
		
		if (hydra == null)
		{
			if ((config.autoProtectionPrayers() || config.autoOffensivePrayers()) && !allPrayersDeactived)
			{
				allPrayersDeactived = PrayerInteractions.deactivatePrayers(config.autoPreservePrayer());
			}
		}
		else
		{
			prayDefensivelyAgainstHydra();
			prayOffensivelyAgainstHydra();
			turnOnPreservePrayer();
			drinkPrayerPotion();
		}
	}
	
	private void prayDefensivelyAgainstHydra()
	{
		if (!config.autoProtectionPrayers() || hydra == null)
		{
			return;
		}
		
		Prayer prayerToUse = hydra.getNextAttack().getPrayer();
		
		if (config.allowManualPrayers())
		{
			if (lastActivatedProtectionPrayer != null && lastActivatedProtectionPrayer.equals(prayerToUse))
			{
				return;
			}
		}
		
		PrayerInteractions.activatePrayer(prayerToUse);
		lastActivatedProtectionPrayer = prayerToUse;
	}
	
	private void prayOffensivelyAgainstHydra()
	{
		if (!config.autoOffensivePrayers() || hydra == null)
		{
			return;
		}
		
		if (config.allowManualPrayers())
		{
			if (offensivePrayerActivated)
			{
				return;
			}
		}
		
		offensivePrayerActivated = PrayerInteractions.activatePrayer(config.offensivePrayerType().getPrayer());
	}
	
	private void turnOnPreservePrayer()
	{
		if (!config.autoPreservePrayer())
		{
			return;
		}
		
		if (config.allowManualPrayers())
		{
			if (preservePrayerActivated)
			{
				return;
			}
		}
		
		preservePrayerActivated = PrayerInteractions.activatePrayer(Prayer.PRESERVE);
	}

	private void drinkPrayerPotion()
	{
		//Check if you're not doing any of the phase handlers before drinking
		if (!config.drinkPrayerPotions() || poisonSafeTile != null || flameSkipState != 0 || lightningSkipState > 0 && lightningSkipState < 3)
		{
			return;
		}

		if (client.getBoostedSkillLevel(Skill.PRAYER) <= config.prayerThreshold())
		{
			InventoryInteractions.drinkPrayerRestoreDose(true, true, false);
			//Same as enraged poisoned, don't attack if there are flame walls present
			if (config.performAttackAfterDrink() && flameObjects.isEmpty())
			{
				performAttackAfterDrink = true;
			}
			return;
		}

		if (performAttackAfterDrink && !ReflectionLibrary.areYouMoving())
		{
			SpellInteractions.attackNpc(hydra.getNpc());
			performAttackAfterDrink = false;
		}
	}

	private void addOverlays()
	{
		overlayManager.add(sceneOverlay);
		overlayManager.add(attackOverlay);
		overlayManager.add(prayerOverlay);
	}

	private void removeOverlays()
	{
		overlayManager.remove(sceneOverlay);
		overlayManager.remove(attackOverlay);
		overlayManager.remove(prayerOverlay);
	}

	private boolean isInHydraRegion()
	{
		return client.isInInstancedRegion() && Arrays.equals(client.getMapRegions(), HYDRA_REGIONS);
	}
	
	private boolean isInHydraLair()
	{
		if (!atHydra)
		{
			return false;
		}
		
		return HYDRA_LAIR.contains(WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation()));
	}
}
