package com.theplug.kotori.kotoriutils;

import com.theplug.kotori.kotoriutils.gson.Hooks;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class KotoriUtilsServiceImpl implements KotoriUtilsPluginService
{
    private final KotoriUtils plugin;

    @Inject
    private KotoriUtilsServiceImpl(KotoriUtils plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public Hooks getHooks()
    {
        return plugin.getRsHooks();
    }
}
