package com.theplug.kotori.kotoripluginloader.json;

import lombok.Getter;

import java.net.URL;

public class Releases
{
    @Getter
    private String date;
    @Getter
    private String sha512sum;
    @Getter
    private String version;
    @Getter
    private URL url;
    @Getter
    private String requires;
}
