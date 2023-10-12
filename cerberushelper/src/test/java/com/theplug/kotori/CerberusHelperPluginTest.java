package com.theplug.kotori;

import com.theplug.kotori.cerberushelper.CerberusHelperPlugin;
import com.theplug.kotori.kotoriutils.KotoriUtils;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CerberusHelperPluginTest
{

    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(CerberusHelperPlugin.class, KotoriUtils.class);
        RuneLite.main(args);
    }
}
