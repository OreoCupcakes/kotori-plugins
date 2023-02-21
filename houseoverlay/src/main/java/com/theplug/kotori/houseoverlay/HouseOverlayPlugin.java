//Created by PluginCreated by ImNo: https://github.com/ImNoOSRS
//Ported by Kotori
package com.theplug.kotori.houseoverlay;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.kit.KitType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@PluginDescriptor(
        name = "House Overlay",
        description = "Overlays for your house.",
        tags = {"imno","ported","house", "overlay", "poh"}
)
@Slf4j
public class HouseOverlayPlugin extends Plugin {
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
    HouseOverlayConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(HouseOverlayConfig.class);
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) {
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
    protected void startUp() {
        overlayManager.add(overlay);
        init_fairy_rings();
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
        cached_fairy_ring_names.clear();
    }

    public boolean inhouse = false;
    public int currentanimation = 0;
    public boolean fairy_ring_has_staff = false;
    @Subscribe
    private void onAnimationChanged(final AnimationChanged event)
    {
        if(inhouse)
        {
            if(event.getActor() == client.getLocalPlayer()) {
                currentanimation = event.getActor().getAnimation();
            }
        }
    }

    @Subscribe
    private void onBeforeRender(final BeforeRender event) {
        if (this.client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
    }

    @Subscribe
    private void onWidgetLoaded(WidgetLoaded event)
    {

    }

    @Subscribe
    public void onGameTick(GameTick event) {
        inhouse = client.getMapRegions()[0] == 7769 || client.getMapRegions()[0] == 7513 || client.getMapRegions()[0] == 8025;
        int currentweapon = client.getLocalPlayer().getPlayerComposition().getEquipmentIds()[KitType.WEAPON.getIndex()];
        fairy_ring_has_staff = currentweapon == 1284 || currentweapon == 9596;
    }
}