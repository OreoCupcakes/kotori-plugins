package com.theplug.kotori.kotoripluginloader.json;

import lombok.Getter;

public class Info
{
    @Getter
    private int gameRevision;
    @Getter
    private String kotoriLoaderVersion;
    @Getter
    private boolean preventLoadOfPlugins;
}
