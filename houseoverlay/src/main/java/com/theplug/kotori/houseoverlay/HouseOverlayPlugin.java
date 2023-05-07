//Created by PluginCreated by ImNo: https://github.com/ImNoOSRS
//Ported by Kotori
package com.theplug.kotori.houseoverlay;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.kit.KitType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.*;

@PluginDescriptor(
        name = "House Overlay",
        description = "Overlays for your house.",
        tags = {"imno","ported","house", "overlay", "poh","kotori"}
)
@Slf4j
public class HouseOverlayPlugin extends Plugin
{
    // Injects our config
    @Inject
    private ConfigManager configManager;
    @Inject
    private HouseOverlayConfig config;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private HouseOverlayOverlay overlay;

    @Provides
    HouseOverlayConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(HouseOverlayConfig.class);
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event)
    {
        if (event.getGroup().equals("HouseOverlay"))
        {
            switch(event.getKey())
            {
                case "example":
                    break;
                default:
                    break;
            }
        }
    }

    private Map<String, String> cached_fairy_ring_names = new HashMap<>();

    public void init_fairy_rings()
    {

        // "A" Combinations
        cached_fairy_ring_names.put("AIQ", "Mudskipper Point");
        cached_fairy_ring_names.put("AIR", "South-east of Ardougne");
        cached_fairy_ring_names.put("AJQ", "Cave south of Dorgesh-Kaan");
        cached_fairy_ring_names.put("AJR", "Slayer cave south-east of Rellekka");
        cached_fairy_ring_names.put("AJS", "Penguins near Miscellania");
        cached_fairy_ring_names.put("AKP", "Necropolis");
        cached_fairy_ring_names.put("AKQ", "Piscatoris Hunter area");
        cached_fairy_ring_names.put("AKS", "Feldip Hunter area");
        cached_fairy_ring_names.put("ALP", "Lighthouse");
        cached_fairy_ring_names.put("ALQ", "Haunted Woods east of Canifis");
        cached_fairy_ring_names.put("ALR", "Abyssal Area");
        cached_fairy_ring_names.put("ALS", "McGrubor's Wood");
        // "B" Combinations
        cached_fairy_ring_names.put("BIP", "South-west of Mort Myre");
        cached_fairy_ring_names.put("BIQ", "near Kalphite Hive");
        cached_fairy_ring_names.put("BIS", "Ardougne Zoo");
        cached_fairy_ring_names.put("BJP", "Isle of Souls");
        cached_fairy_ring_names.put("BJR", "Realm of the Fisher King");
        cached_fairy_ring_names.put("BJS", "Near Zul-Andra");
        cached_fairy_ring_names.put("BKP", "South of Castle Wars");
        cached_fairy_ring_names.put("BKQ", "Enchanted Valley");
        cached_fairy_ring_names.put("BKR", "Mort Myre Swamp, south of Canifis");
        cached_fairy_ring_names.put("BKS", "Zanaris");
        cached_fairy_ring_names.put("BLP", "TzHaar area");
        cached_fairy_ring_names.put("BLR", "Legends' Guild");
        cached_fairy_ring_names.put("BLQ", "Yu'biusk");
        // "C" Combinations
        cached_fairy_ring_names.put("CIP", "Miscellania");
        cached_fairy_ring_names.put("CIQ", "North-west of Yanille");
        cached_fairy_ring_names.put("CIR", "South of Mount Karuulm");
        cached_fairy_ring_names.put("CIS", "Arceuus Library");
        cached_fairy_ring_names.put("CJR", "Sinclair Mansion");
        cached_fairy_ring_names.put("CKP", "Cosmic entity's plane");
        cached_fairy_ring_names.put("CKR", "South of Tai Bwo Wannai Village");
        cached_fairy_ring_names.put("CKS", "Canifis");
        cached_fairy_ring_names.put("CLP", "South of Draynor Village");
        cached_fairy_ring_names.put("CLR", "Ape Atoll");
        cached_fairy_ring_names.put("CLS", "Hazelmere's home");
        // "D" Combinations
        cached_fairy_ring_names.put("DIP", "Abyssal Nexus");
        cached_fairy_ring_names.put("DIQ", "POH");
        cached_fairy_ring_names.put("DIR", "Gorak's Plane");
        cached_fairy_ring_names.put("DIS", "Wizards' Tower");
        cached_fairy_ring_names.put("DJP", "Tower of Life");
        cached_fairy_ring_names.put("DJR", "Chasm of Fire");
        cached_fairy_ring_names.put("DKP", "Karambwan fishing spots");
        cached_fairy_ring_names.put("DKR", "Edgeville");
        cached_fairy_ring_names.put("DKS", "Polar Hunter area");
        cached_fairy_ring_names.put("DLQ", "North of Nardah");
        cached_fairy_ring_names.put("DLR", "Poison Waste south of Isafdar");
        cached_fairy_ring_names.put("DLS", "Myreque hideout under The Hollows");


    }

    public String get_fairy_ring_name(String key)
    {
        if(cached_fairy_ring_names.containsKey(key)) {
            return cached_fairy_ring_names.get(key);
        }
        else
        {
            return key;
        }
    }

    @Override
    protected void startUp()
    {
        if (client.getGameState() != GameState.LOGGED_IN || !isInHouse())
        {
            return;
        }
        
        init();
    }
    
    private void init()
    {
        inHouse = true;
        overlayManager.add(overlay);
        init_fairy_rings();
        checkWeaponSlot();
    }

    @Override
    protected void shutDown()
    {
        inHouse = false;
        overlayManager.remove(overlay);
        cached_fairy_ring_names.clear();
        gameObjectCollection.clear();
        decorativeObjectCollection.clear();
        fairy_ring_has_staff = false;
    }

    private static final Set<Integer> REGION_IDS = Set.of(7257, 7512, 7513, 7514, 7768, 7769, 7770, 8025, 8026);
    public boolean inHouse = false;
    public int currentanimation = 0;
    public boolean fairy_ring_has_staff = false;
    public Collection<GameObject> gameObjectCollection = new ArrayList<>();
    public Collection<DecorativeObject> decorativeObjectCollection = new ArrayList<>();
    
    @Subscribe
    private void onAnimationChanged(final AnimationChanged event)
    {
        if (!inHouse)
        {
            return;
        }
        
        if(event.getActor() == client.getLocalPlayer())
        {
            currentanimation = event.getActor().getAnimation();
        }
    }
    
    @Subscribe
    private void onItemContainerChanged(ItemContainerChanged event)
    {
        if (!inHouse || event.getItemContainer().getId() != InventoryID.EQUIPMENT.getId())
        {
            return;
        }
        
        checkWeaponSlot();
    }

    @Subscribe
    private void onGameObjectSpawned(GameObjectSpawned event)
    {
        if (!inHouse)
        {
            return;
        }
        GameObject spawnedGameObject = event.getGameObject();
        if (spawnedGameObject != null)
        {
            if (!gameObjectCollection.stream().anyMatch(o -> o.getId() == spawnedGameObject.getId()))
            {
                gameObjectCollection.add(spawnedGameObject);
            }
        }
    }

    @Subscribe
    private void onDecorativeObjectSpawned(DecorativeObjectSpawned event)
    {
        if (!inHouse)
        {
            return;
        }
        DecorativeObject spawnedDecorativeObject = event.getDecorativeObject();
        if (spawnedDecorativeObject != null)
        {
            if (!decorativeObjectCollection.stream().anyMatch(o -> o.getId() == spawnedDecorativeObject.getId()))
            {
                decorativeObjectCollection.add(spawnedDecorativeObject);
            }
        }
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event)
    {
        final GameState gameState = event.getGameState();
        
        switch (gameState)
        {
            case LOGGED_IN:
                if (isInHouse())
                {
                    if (!inHouse)
                    {
                        init();
                    }
                }
                else
                {
                    if (inHouse)
                    {
                        shutDown();
                    }
                }
                break;
            case HOPPING:
            case LOGIN_SCREEN:
                if (inHouse)
                {
                    shutDown();
                }
            default:
                break;
        }
    }
    
    private boolean isInHouse()
    {
        for (int regionId : client.getMapRegions())
        {
            if (REGION_IDS.contains(regionId))
            {
                return true;
            }
        }
        return false;
    }
    
    private void checkWeaponSlot()
    {
        ItemContainer itemContainer = client.getItemContainer(InventoryID.EQUIPMENT);
        Item weaponItem = itemContainer == null ? null : itemContainer.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
        int currentWeapon = weaponItem == null ? -1 : weaponItem.getId();
        fairy_ring_has_staff = currentWeapon == 772 || currentWeapon == 9084;
    }
}