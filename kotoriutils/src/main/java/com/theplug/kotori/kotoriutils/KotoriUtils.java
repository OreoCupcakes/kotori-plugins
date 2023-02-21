package com.theplug.kotori.kotoriutils;

import com.theplug.kotori.kotoriutils.libs.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Slf4j
@PluginDescriptor(
        name = "Kotori Plugin Utils",
        enabledByDefault = true,
        description = "Utilities needed for certain ported plugins to work.",
        tags = {"ported", "kotori", "utilities", "utils", "plugin"}
)
public class KotoriUtils extends Plugin {

    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;

    final private String hooksFileURL = "https://pastebin.com/raw/2PBxrrBs";

    @Getter
    private NPCsLibrary npcsLibrary;
    @Getter
    private InvokesLibrary invokesLibrary;
    @Getter
    private SpellsLibrary spellsLibrary;
    @Getter
    private MenusLibrary menusLibrary;
    @Getter
    private WalkingLibrary walkingLibrary;

    @Getter
    private int rsVersion;

    public KotoriUtils() {
        this.invokesLibrary = new InvokesLibrary(this.client);
        this.npcsLibrary = new NPCsLibrary(this.client);
        this.spellsLibrary = new SpellsLibrary(this.client);
        this.menusLibrary = new MenusLibrary(this.client);
        this.walkingLibrary = new WalkingLibrary(this.client);

        getObfuscatedClassHooks();
    }

    public KotoriUtils(Client client) {
        this.invokesLibrary = new InvokesLibrary(client);
        this.npcsLibrary = new NPCsLibrary(client);
        this.spellsLibrary = new SpellsLibrary(client);
        this.menusLibrary = new MenusLibrary(client);
        this.walkingLibrary = new WalkingLibrary(client);

        getObfuscatedClassHooks();
    }

    @Override
    protected void startUp() {

    }

    @Override
    protected void shutDown() {

    }

    private void getObfuscatedClassHooks() {
        try {
            URL url = new URL(hooksFileURL);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String lineToParse;
            while ((lineToParse = bufferedReader.readLine()) != null) {
                String[] hookLinesArray = lineToParse.replaceAll("[^[a-zA-Z0-9|\\-|\\:|\\.]]","").split(":|\\.");
                String caseString = hookLinesArray[0];

                switch (caseString) {
                    case "rsversion":
                        rsVersion = Integer.parseInt(hookLinesArray[1]);
                        break;
                    case "invokeMenuActionHook":
                        invokesLibrary.setWorldMapData_0_ClassName(hookLinesArray[1]);
                        invokesLibrary.setInvokeMenuActionMethodName(hookLinesArray[2]);
                        break;
                    case "invokeMenuActionHookJunkValue":
                        invokesLibrary.setInvokeMenuActionGarbageValue(Integer.parseInt(hookLinesArray[1]));
                        break;
                    case "setXandYHook":
                        walkingLibrary.setSetXandYClassName(hookLinesArray[1]);
                        walkingLibrary.setSetXandYMethodName(hookLinesArray[2]);
                        break;
                    case "setViewportWalkingFieldHook":
                        walkingLibrary.setSetViewportWalkingClassName(hookLinesArray[1]);
                        walkingLibrary.setSetViewportWalkingFieldName(hookLinesArray[2]);
                        break;
                    case "checkClickFieldHook":
                        walkingLibrary.setCheckClickClassName(hookLinesArray[1]);
                        walkingLibrary.setCheckClickFieldName(hookLinesArray[2]);
                        break;
                    case "setSelectedSpellWidgetHook":
                        spellsLibrary.setSelectedSpellWidgetClassName(hookLinesArray[1]);
                        spellsLibrary.setSelectedSpellWidgetFieldName(hookLinesArray[2]);
                        break;
                    case  "setSelectedSpellWidgetMultiplier":
                        spellsLibrary.setSelectedSpellWidgetMultiplier(Integer.parseInt(hookLinesArray[1]));
                        break;
                    case "setSelectedSpellChildIndexHook":
                        spellsLibrary.setSelectedSpellChildIndexClassName(hookLinesArray[1]);
                        spellsLibrary.setSelectedSpellChildIndexFieldName(hookLinesArray[2]);
                        break;
                    case "setSelectedSpellChildIndexMultiplier":
                        spellsLibrary.setSelectedSpellChildIndexMultiplier(Integer.parseInt(hookLinesArray[1]));
                        break;
                    case "setSelectedSpellItemIDHook":
                        spellsLibrary.setSelectedSpellItemIDClassName(hookLinesArray[1]);
                        spellsLibrary.setSelectedSpellItemIDFieldName(hookLinesArray[2]);
                        break;
                    case "setSelectedSpellItemIDMultiplier":
                        spellsLibrary.setSelectedSpellItemIDMultiplier(Integer.parseInt(hookLinesArray[1]));
                        break;
                    case "getNpcCompositionOverheadIcon":
                        npcsLibrary.setNpcCompositionClassName(hookLinesArray[1]);
                        npcsLibrary.setOverheadIconFieldName(hookLinesArray[2]);
                        break;
                    case "menuEntryIndexFieldHook":
                        menusLibrary.setMenuEntryIndexClassName(hookLinesArray[1]);
                        menusLibrary.setMenuEntryIndexFieldName(hookLinesArray[2]);
                        break;
                    case "menuEntryIdentifiersArrayFieldHook":
                        menusLibrary.setMenuEntryIdentifiersArrayClassName(hookLinesArray[1]);
                        menusLibrary.setMenuEntryIdentifiersArrayFieldName(hookLinesArray[2]);
                        break;
                    case "menuEntryItemIdsArrayFieldHook":
                        menusLibrary.setMenuEntryItemIdsArrayClassName(hookLinesArray[1]);
                        menusLibrary.setMenuEntryItemIdsArrayFieldName(hookLinesArray[2]);
                        break;
                    case "menuEntryOptionsArrayFieldHook":
                        menusLibrary.setMenuEntryOptionsArrayClassName(hookLinesArray[1]);
                        menusLibrary.setMenuEntryOptionsArrayFieldName(hookLinesArray[2]);
                        break;
                    case "menuEntryParam0ArrayFieldHook":
                        menusLibrary.setMenuEntryParam0ArrayClassName(hookLinesArray[1]);
                        menusLibrary.setMenuEntryParam0ArrayFieldName(hookLinesArray[2]);
                        break;
                    case "menuEntryParam1ArrayFieldHook":
                        menusLibrary.setMenuEntryParam1ArrayClassName(hookLinesArray[1]);
                        menusLibrary.setMenuEntryParam1ArrayFieldName(hookLinesArray[2]);
                        break;
                    case "menuEntryTargetsArrayFieldHook":
                        menusLibrary.setMenuEntryTargetsArrayClassName(hookLinesArray[1]);
                        menusLibrary.setMenuEntryTargetsArrayFieldName(hookLinesArray[2]);
                        break;
                    case "menuEntryTypesArrayFieldHook":
                        menusLibrary.setMenuEntryTypesArrayClassName(hookLinesArray[1]);
                        menusLibrary.setMenuEntryTypesArrayFieldName(hookLinesArray[2]);
                        break;
                    case "getActorAnimationIdFieldHook":
                        npcsLibrary.setActorClassName(hookLinesArray[1]);
                        npcsLibrary.setSequenceFieldName(hookLinesArray[2]);
                        break;
                    case "getActorAnimationIdMultiplier":
                        npcsLibrary.setSequenceGetterMultiplier(Integer.parseInt(hookLinesArray[1]));
                        break;
                    default:
                        break;
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            log.error("Failed to load the hooks from URL.",e);
        }
    }
}
