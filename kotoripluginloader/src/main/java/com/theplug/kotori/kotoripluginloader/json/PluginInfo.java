package com.theplug.kotori.kotoripluginloader.json;

import lombok.Getter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PluginInfo
{
    @Getter
    private URL projectUrl;
    @Getter
    private String provider;
    @Getter
    private String name;
    @Getter
    private String packageId;
    @Getter
    private String description;
    @Getter
    private String mainClassName;
    @Getter
    private String id;
    @Getter
    private List<Releases> releases = new ArrayList<Releases>();
}
