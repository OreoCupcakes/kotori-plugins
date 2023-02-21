package com.theplug.kotori.kotoriutils.libs;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import javax.inject.Inject;

@Slf4j
public class MenusLibrary
{
    @Inject
    private Client client;

    @Setter
    private String menuEntryIndexClassName;
    @Setter
    private String menuEntryIndexFieldName;

    @Setter
    private String menuEntryIdentifiersArrayClassName;
    @Setter
    private String menuEntryIdentifiersArrayFieldName;

    @Setter
    private String menuEntryItemIdsArrayClassName;
    @Setter
    private String menuEntryItemIdsArrayFieldName;

    @Setter
    private String menuEntryOptionsArrayClassName;
    @Setter
    private String menuEntryOptionsArrayFieldName;

    @Setter
    private String menuEntryParam0ArrayClassName;
    @Setter
    private String menuEntryParam0ArrayFieldName;

    @Setter
    private String menuEntryParam1ArrayClassName;
    @Setter
    private String menuEntryParam1ArrayFieldName;

    @Setter
    private String menuEntryTargetsArrayClassName;
    @Setter
    private String menuEntryTargetsArrayFieldName;

    @Setter
    private String menuEntryTypesArrayClassName;
    @Setter
    private String menuEntryTypesArrayFieldName;

    public MenusLibrary(Client client) {
        this.client = client;
    }
}
