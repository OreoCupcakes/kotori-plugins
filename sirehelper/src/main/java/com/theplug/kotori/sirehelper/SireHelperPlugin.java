package com.theplug.kotori.sirehelper;

import com.google.inject.Provides;
import com.theplug.kotori.kotoriutils.KotoriUtils;
import com.theplug.kotori.kotoriutils.ReflectionLibrary;
import com.theplug.kotori.kotoriutils.methods.*;
import com.theplug.kotori.kotoriutils.rlapi.Spells;
import com.theplug.kotori.sirehelper.entity.AbyssalSire;
import com.theplug.kotori.sirehelper.entity.MiasmaPools;
import com.theplug.kotori.sirehelper.entity.RespiratorySystem;
import com.theplug.kotori.sirehelper.entity.Spawn;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.npcoverlay.HighlightedNpc;
import net.runelite.client.game.npcoverlay.NpcOverlayService;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Singleton
@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "<html><font color=#6b8af6>[K]</font> Sire Helper</html>",
	enabledByDefault = false,
	description = "A plugin for the Abyssal Sire boss. Overlays and some automation.",
	tags = {"abyssal","abyss","abby","sire","kotori","helper","ported","boss","pvm","slayer"}
)
public class SireHelperPlugin extends Plugin
{
	private static final String ABYSSAL_SIRE_STUN_MESSAGE = "The Sire has been disorientated temporarily.";
	private static final Set<Integer> SIRE_REGIONS = Set.of(11850, 11851, 12362, 12363);

	//These tiles are the anchor points for the dodge script.
	private static final Set<WorldPoint> ANCHOR_TILES = Set.of(
			new WorldPoint(2670, 4770, 0),
			new WorldPoint(2980, 4834, 0),
			new WorldPoint(3110, 4770, 0),
			new WorldPoint(3105, 4834, 0)
	);

	private static final int MIASMA_ID = 1275;
	private static final int SIRE_GET_OFF_THRONE_ANIMATION_ID = 4532;
	private static final int SIRE_DEATH_ANIMATION_ID = 7100;

	@Getter
	private final Map<LocalPoint, MiasmaPools> miasmaPoolsMap = new HashMap<>();
	@Getter
	private final Map<WorldPoint, RespiratorySystem> respiratorsMap = new HashMap<>();
	@Getter
	private final Set<Spawn> spawnSet = new HashSet<>();

	private final Function<NPC, HighlightedNpc> npcHighlighter = this::highlightNpc;

	@Inject
	private Client client;
	@Inject
	private SireHelperConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private NpcOverlayService npcOverlayService;
	@Inject
	private KeyManager keyManager;
	@Inject
	private SireHelperSceneOverlay sceneOverlay;

	@Getter
	private AbyssalSire abyssalSire;

	@Getter
	private boolean atSire;
	private boolean isYourSireKill;
	private boolean dodgedExplosion;
	private int dodgeExplosionStep;
	private WorldPoint anchorDodgeTile;
	private WorldPoint miasmaDodgeTile;
	private WorldPoint dodgeExplosionTile;
	private Spells spellToUse;
	private int[] itemsToEquip;
	private boolean phaseTwoSwitchDone;
	private boolean finishedEquippingItems;
	private Prayer offensivePrayer;
	private Prayer protectionPrayer;
	private int systemRegenTimer;


	@Provides
	SireHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SireHelperConfig.class);
	}

	@Override
	protected void startUp()
	{
		if (client.getGameState() != GameState.LOGGED_IN || !checkIfAtSire())
		{
			return;
		}

		init();
	}

	@Override
	protected void shutDown()
	{
		atSire = false;

		miasmaPoolsMap.clear();
		respiratorsMap.clear();
		spawnSet.clear();

		abyssalSire = null;
		isYourSireKill = false;
		dodgedExplosion = false;
		phaseTwoSwitchDone = false;
		finishedEquippingItems = false;
		dodgeExplosionStep = 0;
		anchorDodgeTile = null;
		miasmaDodgeTile = null;
		dodgeExplosionTile = null;
		spellToUse = null;
		itemsToEquip = null;
		offensivePrayer = null;
		protectionPrayer = null;
		systemRegenTimer = 0;

		removeServices();
	}
	
	public void init()
	{
		atSire = true;

		isYourSireKill = false;
		dodgedExplosion = false;
		phaseTwoSwitchDone = false;
		finishedEquippingItems = false;
		dodgeExplosionStep = 0;
		anchorDodgeTile = null;
		miasmaDodgeTile = null;
		dodgeExplosionTile = null;
		spellToUse = null;
		itemsToEquip = null;
		offensivePrayer = null;
		protectionPrayer = null;
		systemRegenTimer = 0;

		addServices();

		//This is for Leagues' Last Recall or when you shut down and turn back on the plugin mid-fight
		if (abyssalSire == null)
		{
			for (NPC npc : NPCInteractions.getNpcs())
			{
				switch (npc.getId())
				{
					case NpcID.ABYSSALSIRE_SIRE_STASIS_SLEEPING:
					case NpcID.ABYSSALSIRE_SIRE_STASIS_AWAKE:
					case NpcID.ABYSSALSIRE_SIRE_STASIS_STUNNED:
					case NpcID.ABYSSALSIRE_SIRE_PUPPET:
					case NpcID.ABYSSALSIRE_SIRE_WANDERING:
					case NpcID.ABYSSALSIRE_SIRE_PANICKING:
					case NpcID.ABYSSALSIRE_SIRE_APOCALYPSE:
						abyssalSire = new AbyssalSire(npc);
						break;
				}
			}
		}
	}

	private void addServices()
	{
		overlayManager.add(sceneOverlay);
		npcOverlayService.registerHighlighter(npcHighlighter);
		keyManager.registerKeyListener(shadowSpellHotkey);
		keyManager.registerKeyListener(bloodSpellHotkey);
		keyManager.registerKeyListener(phaseOneGearHotkey);
		keyManager.registerKeyListener(phaseTwoPlusGearHotkey);
	}

	private void removeServices()
	{
		overlayManager.remove(sceneOverlay);
		npcOverlayService.unregisterHighlighter(npcHighlighter);
		keyManager.unregisterKeyListener(shadowSpellHotkey);
		keyManager.unregisterKeyListener(bloodSpellHotkey);
		keyManager.unregisterKeyListener(phaseOneGearHotkey);
		keyManager.unregisterKeyListener(phaseTwoPlusGearHotkey);
	}
	
	@Subscribe
	private void onGameStateChanged(GameStateChanged event)
	{
		switch (event.getGameState())
		{
			case LOGGED_IN:
				if (checkIfAtSire())
				{
					if (!atSire)
					{
						init();
					}
				}
				else
				{
					if (atSire)
					{
						shutDown();

						if (config.autoPrayers())
						{
							PrayerInteractions.deactivatePrayers(config.keepPreservePrayerOn());
						}
					}
				}
				break;
			case LOGIN_SCREEN:
			case HOPPING:
				shutDown();
				break;
		}
	}

	@Subscribe
	private void onGameTick(GameTick gameTick)
	{
		if (!atSire)
		{
			return;
		}

		updateRespiratoryVariables();
		updateSpawnVariables();
		updateMiasmaPools();

		if (abyssalSire == null)
		{
			return;
		}

		updateSireVariables();

		if (isYourSireKill)
		{
			dodgeSireSpecialAttacks();
			handleGearEquips();
			handlePrayers();
		}
	}

	@Subscribe
	private void onClientTick(ClientTick event)
	{
		if (!atSire || client.isMenuOpen())
		{
			return;
		}

		if (config.swapSpawnMenuEntry())
		{
			prioritizeSpawnsOverSire();
		}

		if (config.leftClickSpells() && spellToUse != null)
		{
			SpellInteractions.createOneClickAttackSpell(spellToUse);
		}
	}

	@Subscribe
	private void onGraphicsObjectCreated(GraphicsObjectCreated graphicsObjectCreated)
	{
		if (!atSire)
		{
			return;
		}

		GraphicsObject graphic = graphicsObjectCreated.getGraphicsObject();
		LocalPoint localPoint = graphic.getLocation();

		if (graphic.getId() == MIASMA_ID)
		{
			miasmaPoolsMap.remove(localPoint);
			miasmaPoolsMap.put(localPoint, new MiasmaPools(graphic));
		}
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged configChanged)
	{
		if (!atSire || !configChanged.getGroup().equals(SireHelperConfig.GROUP))
		{
			return;
		}

		npcOverlayService.rebuild();
	}

	@Subscribe
	private void onNpcSpawned(NpcSpawned event)
	{
		if (!atSire)
		{
			return;
		}

		NPC npc = event.getNpc();
		switch (npc.getId())
		{
			case NpcID.ABYSSALSIRE_SPAWN:
			case NpcID.ABYSSALSIRE_SPAWN_DYING:
				spawnSet.add(new Spawn(npc));
				break;
			case NpcID.ABYSSALSIRE_LUNG:
				respiratorsMap.putIfAbsent(npc.getWorldLocation(), new RespiratorySystem(npc));
				break;
			/*
				Sire spawns twice in the fight, once for phase 1 and then for phase 2+.
				If the Sire goes out of scene and comes back into scene, then the sire NPC will be different from the stored sire NPC
			 */
			case NpcID.ABYSSALSIRE_SIRE_STASIS_SLEEPING:
			case NpcID.ABYSSALSIRE_SIRE_STASIS_AWAKE:
			case NpcID.ABYSSALSIRE_SIRE_STASIS_STUNNED:
			case NpcID.ABYSSALSIRE_SIRE_PUPPET:
			case NpcID.ABYSSALSIRE_SIRE_WANDERING:
			case NpcID.ABYSSALSIRE_SIRE_PANICKING:
			case NpcID.ABYSSALSIRE_SIRE_APOCALYPSE:
				if (abyssalSire == null)
				{
					abyssalSire = new AbyssalSire(npc);
				}
				else
				{
					abyssalSire.setNpc(npc);
				}
				break;
		}
	}

	@Subscribe
	private void onNpcDespawned(NpcDespawned event)
	{
		if (!atSire)
		{
			return;
		}

		NPC npc = event.getNpc();
		switch (npc.getId())
		{
			//5916 5917 5914
			case NpcID.ABYSSALSIRE_SPAWN:
			case NpcID.ABYSSALSIRE_SPAWN_DYING:
				spawnSet.removeIf(spawn -> spawn.getNpc().equals(npc));
				break;
			case NpcID.ABYSSALSIRE_LUNG:
				WorldPoint ventPoint = npc.getWorldLocation();
				RespiratorySystem system = respiratorsMap.get(ventPoint);
				if (system != null)
				{
					if (npc.isDead() || npc.getWorldLocation().getRegionID() != client.getLocalPlayer().getWorldLocation().getRegionID())
					{
						system.updateHp();
						respiratorsMap.remove(ventPoint);
					}
				}
				break;
			/*
				It despawns twice in a fight, once when it transitions to phase 2 and then when you kill it.
				Ignore the despawn event for P1 as this event will trigger when the Sire is out of render distance when killing vents
			case NpcID.ABYSSALSIRE_SIRE_STASIS_SLEEPING:
			case NpcID.ABYSSALSIRE_SIRE_STASIS_AWAKE:
			case NpcID.ABYSSALSIRE_SIRE_STASIS_STUNNED:
			 */
			//5889 5890 5891 5908
			case NpcID.ABYSSALSIRE_SIRE_PUPPET:
			case NpcID.ABYSSALSIRE_SIRE_WANDERING:
			case NpcID.ABYSSALSIRE_SIRE_PANICKING:
			case NpcID.ABYSSALSIRE_SIRE_APOCALYPSE:
				abyssalSire = null;
				isYourSireKill = false;
				dodgedExplosion = false;
				phaseTwoSwitchDone = false;
				dodgeExplosionStep = 0;
				anchorDodgeTile = null;
				miasmaDodgeTile = null;
				dodgeExplosionTile = null;
				offensivePrayer = null;
				protectionPrayer = null;
				miasmaPoolsMap.clear();
				respiratorsMap.clear();
				spawnSet.clear();
				break;
		}
	}

	/*
        onInteractingChanged also triggers whenever an Actor loads into the scene.
        However, it returns its target as null until its interaction changes again.

        This event is used to check if the Abyssal Sire is being killed by your account.
        It's for the purpose of determining whether to turn on automated helper functions.
        By default, it'll assume it is not your kill until you interact with the Sire or the Sire interacts with you.
     */
	@Subscribe
	private void onInteractingChanged(InteractingChanged event)
	{
		if (!atSire || isYourSireKill)
		{
			return;
		}

		Actor source = event.getSource();
		Actor target = event.getTarget();

		if (target instanceof NPC)
		{
			//Check if the Sire/vents are being attacked by your account
			NPC npc = (NPC) target;

			switch (npc.getId())
			{
				case NpcID.ABYSSALSIRE_LUNG:
				case NpcID.ABYSSALSIRE_SIRE_STASIS_SLEEPING:
				case NpcID.ABYSSALSIRE_SIRE_STASIS_AWAKE:
				case NpcID.ABYSSALSIRE_SIRE_STASIS_STUNNED:
				case NpcID.ABYSSALSIRE_SIRE_PUPPET:
				case NpcID.ABYSSALSIRE_SIRE_WANDERING:
				case NpcID.ABYSSALSIRE_SIRE_PANICKING:
				case NpcID.ABYSSALSIRE_SIRE_APOCALYPSE:
					isYourSireKill = source != null && source.equals(client.getLocalPlayer());
					break;
			}
		}
		else if (source instanceof NPC)
		{
			//Check if the Sire/tentacles/spawns/scions are attacking your account
			NPC npc = (NPC) source;

			switch (npc.getId())
			{
				case NpcID.ABYSSALSIRE_TENTACLE_SLEEPING_NORTH:
				case NpcID.ABYSSALSIRE_TENTACLE_SLEEPING_SOUTH:
				case NpcID.ABYSSALSIRE_TENTACLE_SLEEPING_UPRIGHT:
				case NpcID.ABYSSALSIRE_TENTACLE_ACTIVE:
				case NpcID.ABYSSALSIRE_TENTACLE_STUNNED:
				case NpcID.ABYSSALSIRE_SPAWN:
				case NpcID.ABYSSALSIRE_SPAWN_DYING:
				case NpcID.ABYSSALSIRE_SCION:
				case NpcID.ABYSSALSIRE_SCION_DYING:
				case NpcID.ABYSSALSIRE_SIRE_STASIS_SLEEPING:
				case NpcID.ABYSSALSIRE_SIRE_STASIS_AWAKE:
				case NpcID.ABYSSALSIRE_SIRE_STASIS_STUNNED:
				case NpcID.ABYSSALSIRE_SIRE_PUPPET:
				case NpcID.ABYSSALSIRE_SIRE_WANDERING:
				case NpcID.ABYSSALSIRE_SIRE_PANICKING:
				case NpcID.ABYSSALSIRE_SIRE_APOCALYPSE:
					isYourSireKill = target != null && target.equals(client.getLocalPlayer());
					break;
			}
		}
	}

	@Subscribe
	private void onHitsplatApplied(HitsplatApplied event)
	{
		if (!atSire && respiratorsMap.isEmpty())
		{
			return;
		}

		RespiratorySystem system = respiratorsMap.get(event.getActor().getWorldLocation());

		if (system != null)
		{
			system.addToDamageDealt(event.getHitsplat().getAmount());
		}
	}

	@Subscribe
	private void onChatMessage(ChatMessage event)
	{
		if (!atSire || abyssalSire == null)
		{
			return;
		}

		String message = event.getMessage();

		if (message.equals(ABYSSAL_SIRE_STUN_MESSAGE))
		{
			abyssalSire.setStunned(true);
			abyssalSire.resetStunTimer();
		}
	}

	private boolean checkIfAtSire()
	{
		return SIRE_REGIONS.contains(MiscUtilities.getPlayerRegionID());
	}

	private void updateSireVariables()
	{
		abyssalSire.updateHp();
		abyssalSire.updatePhase();

		if (abyssalSire.isStunned())
		{
			if (abyssalSire.getStunTimer() <= 1)
			{
				abyssalSire.setStunned(false);
				abyssalSire.resetStunTimer();
			}
			else
			{
				abyssalSire.decrementStunTimer();
			}
		}
	}

	private void updateSpawnVariables()
	{
		if (spawnSet.isEmpty())
		{
			return;
		}

		Set<Spawn> spawnsToRemove = new HashSet<>();

		for (Spawn spawn : spawnSet)
		{
			if (spawn.getEvolutionTimer() <= 1)
			{
				spawnsToRemove.add(spawn);
			}
			else
			{
				spawn.decrementTicksUntilEvolution();
			}
		}

		for (Spawn spawn : spawnsToRemove)
		{
			spawnSet.remove(spawn);
		}
	}

	private void updateRespiratoryVariables()
	{
		systemRegenTimer = systemRegenTimer < 9 ? systemRegenTimer + 1 : 0;

		if (respiratorsMap.isEmpty())
		{
			return;
		}

		for (Map.Entry<WorldPoint, RespiratorySystem> entry : respiratorsMap.entrySet())
		{
			RespiratorySystem system = entry.getValue();
			system.updateHp();

			if (system.getDamageDealt() > 0 && system.getDamageDealt() < 50 && systemRegenTimer == 9)
			{
				system.removeFromDamageDealt();
			}
		}
	}

	private void updateMiasmaPools()
	{
		if (miasmaPoolsMap.isEmpty())
		{
			return;
		}

		Set<LocalPoint> miasmaKeysToRemove = new HashSet<>();

		for (Map.Entry<LocalPoint, MiasmaPools> pool : miasmaPoolsMap.entrySet())
		{
			MiasmaPools poolValue = pool.getValue();
			if (poolValue.getTicksUntilDespawn() <= 1)
			{
				miasmaKeysToRemove.add(pool.getKey());
			}
			else
			{
				poolValue.decrementTicksUntilDespawn();
			}
		}

		for (LocalPoint poolPoint : miasmaKeysToRemove)
		{
			miasmaPoolsMap.remove(poolPoint);
		}
	}

	private void dodgeMiasmaPools(LocalPoint playerLocal, WorldPoint playerWorld)
	{
		if (!config.autoDodgeMiasmaPools() || miasmaPoolsMap.isEmpty())
		{
			return;
		}

		if (!miasmaPoolsMap.containsKey(playerLocal))
		{
			if (miasmaDodgeTile != null)
			{
				if (config.autoAttackAfterDodging())
				{
					NPCInteractions.attackNpc(abyssalSire.getNpc());
				}
				miasmaDodgeTile = null;
			}
			return;
		}

		if (anchorDodgeTile == null)
		{
			for (WorldPoint point : ANCHOR_TILES)
			{
				if (point.getRegionID() == playerWorld.getRegionID())
				{
					anchorDodgeTile = point;
					break;
				}
			}

			if (anchorDodgeTile == null)
			{
				return;
			}
		}

		int xAnchor = anchorDodgeTile.getX();
		int xPlayer = playerWorld.getX();
		//If player is to the left of anchor point, then move right
		if (xPlayer < xAnchor)
		{
			miasmaDodgeTile = new WorldPoint(playerWorld.getX() + 2, playerWorld.getY(), playerWorld.getPlane());
		}
		//If player is on or to right of anchor point, then move left
		else
		{
			miasmaDodgeTile = new WorldPoint(playerWorld.getX() - 2, playerWorld.getY(), playerWorld.getPlane());
		}

		ReflectionLibrary.sceneWalk(miasmaDodgeTile);
	}

	private void dodgeExplosion(LocalPoint playerLocal, WorldPoint playerWorld)
	{
		if (!config.autoDodgeExplosion())
		{
			return;
		}

		switch (dodgeExplosionStep)
		{
			case 0:
				if (abyssalSire.getNpc().getWorldArea().isInMeleeDistance(playerWorld))
				{
					dodgeExplosionTile = new WorldPoint(playerWorld.getX(), playerWorld.getY() - 2, playerWorld.getPlane());
					ReflectionLibrary.sceneWalk(dodgeExplosionTile);
					dodgeExplosionStep = 1;
				}
				break;
			case 1:
				if (!playerWorld.equals(dodgeExplosionTile))
				{
					ReflectionLibrary.sceneWalk(dodgeExplosionTile);
				}
				else
				{
					dodgeExplosionStep = 2;
				}
				break;
			case 2:
				if (miasmaPoolsMap.containsKey(playerLocal))
				{
					WorldPoint phaseThreeTile = new WorldPoint(playerWorld.getX(), playerWorld.getY() + 2, playerWorld.getPlane());
					if (config.autoAttackAfterDodging())
					{
						NPCInteractions.attackNpc(abyssalSire.getNpc());
					}
					else
					{
						ReflectionLibrary.sceneWalk(phaseThreeTile);
					}
					dodgeExplosionStep = 3;
				}
				break;
			case 3:
				if (abyssalSire.getNpc().getWorldArea().isInMeleeDistance(playerWorld))
				{
					dodgedExplosion = true;
					dodgeExplosionStep = 0;
				}
				break;
		}
	}

	private void dodgeSireSpecialAttacks()
	{
		LocalPoint playerLocal = client.getLocalPlayer().getLocalLocation();
		WorldPoint playerWorld = WorldPoint.fromLocal(client, playerLocal);

		switch (abyssalSire.getPhase())
		{
			case 1:
			case 2:
			case 3:
				dodgeMiasmaPools(playerLocal, playerWorld);
				break;
			case 4:
				if (dodgedExplosion || !config.autoDodgeExplosion())
				{
					dodgeMiasmaPools(playerLocal, playerWorld);
				}
				else
				{
					dodgeExplosion(playerLocal, playerWorld);
				}
				break;
		}
	}

	private void prioritizeSpawnsOverSire()
	{
		MenuEntry[] menuEntries = client.getMenu().getMenuEntries();
		Set<Integer> npcIndices = new HashSet<>();
		boolean swapMenuEntries = false;

		for (int i = 0; i < menuEntries.length; i++)
		{
			switch (menuEntries[i].getType())
			{
				case NPC_FIRST_OPTION:
				case NPC_SECOND_OPTION:
				case NPC_THIRD_OPTION:
				case NPC_FOURTH_OPTION:
				case NPC_FIFTH_OPTION:
					NPC npc = menuEntries[i].getNpc();
					if (npc == null)
					{
						continue;
					}
					switch (npc.getId())
					{
						case NpcID.ABYSSALSIRE_SIRE_PUPPET:
						case NpcID.ABYSSALSIRE_SIRE_WANDERING:
						case NpcID.ABYSSALSIRE_SIRE_PANICKING:
						case NpcID.ABYSSALSIRE_SCION:
						case NpcID.ABYSSALSIRE_SCION_DYING:
							npcIndices.add(i);
							break;
						case NpcID.ABYSSALSIRE_SPAWN:
						case NpcID.ABYSSALSIRE_SPAWN_DYING:
							swapMenuEntries = true;
							break;
					}
					break;
			}
		}

		if (swapMenuEntries)
		{
			for (Integer index : npcIndices)
			{
				menuEntries[index].setDeprioritized(true);
			}
			client.getMenu().setMenuEntries(menuEntries);
		}
	}

	private void handleGearEquips()
	{
		if (config.autoGearSwap())
		{
			switch (abyssalSire.getNpc().getAnimation())
			{
				case SIRE_GET_OFF_THRONE_ANIMATION_ID:
					if (!phaseTwoSwitchDone)
					{
						itemsToEquip = InventoryInteractions.parseStringToItemIds(config.phaseTwoPlusGearIds());
						phaseTwoSwitchDone = true;
					}
					break;
				case SIRE_DEATH_ANIMATION_ID:
					itemsToEquip = InventoryInteractions.parseStringToItemIds(config.phaseOneGearIds());
					phaseTwoSwitchDone = false;
					break;
			}

			//This is for if the Sire was off-screen when it transitions to phase 2
			if (abyssalSire.getPhase() == 2 && !phaseTwoSwitchDone)
			{
				itemsToEquip = InventoryInteractions.parseStringToItemIds(config.phaseTwoPlusGearIds());
				phaseTwoSwitchDone = true;
			}
		}

		if (itemsToEquip == null)
		{
			return;
		}

		finishedEquippingItems = InventoryInteractions.equipItems(itemsToEquip, config.equipsPerTick());

		if (finishedEquippingItems)
		{
			finishedEquippingItems = false;
			itemsToEquip = null;
		}
	}

	private void handlePrayers()
	{
		if (!config.autoPrayers())
		{
			return;
		}

		if (abyssalSire.getNpc().getAnimation() == SIRE_DEATH_ANIMATION_ID)
		{
			PrayerInteractions.deactivatePrayers(config.keepPreservePrayerOn());
			return;
		}

		switch(abyssalSire.getPhase())
		{
			case 1:
				if (config.usePrayerOnPhaseOne())
				{
					offensivePrayer = VarUtilities.bestOffensivePrayer();
				}
				break;
			case 2:
				protectionPrayer = Prayer.PROTECT_FROM_MELEE;
				offensivePrayer = VarUtilities.bestOffensivePrayer();
				break;
			case 3:
			case 4:
				protectionPrayer = Prayer.PROTECT_FROM_MISSILES;
				offensivePrayer = VarUtilities.bestOffensivePrayer();
				break;
		}

		PrayerInteractions.activatePrayer(protectionPrayer);
		PrayerInteractions.activatePrayer(offensivePrayer);
	}

	private HighlightedNpc highlightNpc(final NPC npc)
	{
		final int id = npc.getId();

		switch (id)
		{
			case NpcID.ABYSSALSIRE_TENTACLE_SLEEPING_NORTH:
			case NpcID.ABYSSALSIRE_TENTACLE_SLEEPING_SOUTH:
			case NpcID.ABYSSALSIRE_TENTACLE_SLEEPING_UPRIGHT:
			case NpcID.ABYSSALSIRE_TENTACLE_ACTIVE:
			case NpcID.ABYSSALSIRE_TENTACLE_STUNNED:
				return HighlightedNpc.builder()
						.npc(npc)
						.trueTile(true)
						.highlightColor(config.tentacleBorderColor())
						.fillColor(config.tentacleFillColor())
						.render(n -> config.highlightTentacles() && !npc.isDead())
						.build();
			case NpcID.ABYSSALSIRE_SPAWN:
			case NpcID.ABYSSALSIRE_SPAWN_DYING:
				return HighlightedNpc.builder()
						.npc(npc)
						.trueTile(true)
						.outline(true)
						.highlightColor(config.spawnBorderColor())
						.fillColor(config.spawnFillColor())
						.render(n -> config.highlightSpawns() && !npc.isDead())
						.build();
			case NpcID.ABYSSALSIRE_SCION:
			case NpcID.ABYSSALSIRE_SCION_DYING:
				return HighlightedNpc.builder()
						.npc(npc)
						.trueTile(true)
						.outline(true)
						.highlightColor(config.scionBorderColor())
						.fillColor(config.scionFillColor())
						.render(n -> config.highlightScions() && !npc.isDead())
						.build();
			case NpcID.ABYSSALSIRE_SIRE_STASIS_SLEEPING:
			case NpcID.ABYSSALSIRE_SIRE_STASIS_AWAKE:
			case NpcID.ABYSSALSIRE_SIRE_STASIS_STUNNED:
			case NpcID.ABYSSALSIRE_SIRE_PUPPET:
			case NpcID.ABYSSALSIRE_SIRE_WANDERING:
			case NpcID.ABYSSALSIRE_SIRE_PANICKING:
			case NpcID.ABYSSALSIRE_SIRE_APOCALYPSE:
				return HighlightedNpc.builder()
						.npc(npc)
						.trueTile(true)
						.swTrueTile(config.showSouthWestTrueTile())
						.outline(true)
						.highlightColor(config.abyssalSireBorderColor())
						.fillColor(config.abyssalSireFillColor())
						.render(n -> config.highlightAbyssalSire() && !npc.isDead())
						.build();
		}

		return null;
	}

	private final HotkeyListener shadowSpellHotkey = new HotkeyListener(() -> config.shadowSpellHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			spellToUse = config.shadowSpellType().getSpell();
		}

		@Override
		public void hotkeyReleased()
		{
			spellToUse = null;
		}
	};

	private final HotkeyListener bloodSpellHotkey = new HotkeyListener(() -> config.bloodSpellHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			spellToUse = config.bloodSpellType().getSpell();
		}

		@Override
		public void hotkeyReleased()
		{
			spellToUse = null;
		}
	};

	private final HotkeyListener phaseOneGearHotkey = new HotkeyListener(() -> config.phaseOneGearHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			itemsToEquip = InventoryInteractions.parseStringToItemIds(config.phaseOneGearIds());
		}
	};

	private final HotkeyListener phaseTwoPlusGearHotkey = new HotkeyListener(() -> config.phaseTwoPlusHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			itemsToEquip = InventoryInteractions.parseStringToItemIds(config.phaseTwoPlusGearIds());
		}
	};
}

