package com.theplug.kotori.kotoriutils.reflection;

import com.theplug.kotori.kotoriutils.rlapi.PrayerExtended;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.client.RuneLite;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class InvokesLibrary
{
    private static final Client client = RuneLite.getInjector().getInstance(Client.class);

    @Setter
    private static String invokeMenuActionClassName;
    @Setter
    private static String invokeMenuActionMethodName;
    @Setter
    private static int invokeMenuActionJunkValue;

    public static void invoke(int param0, int param1, int opcode, int identifier, int itemid, String option, String target, int x, int y)
    {
        try
        {
            /*
                Getting Methods with getDeclaredMethod requires you to pass in an Array of its parameter types. So just get all the methods and filter out
                the one you need
             */
            Class<?> classWithMenuAction = client.getClass().getClassLoader().loadClass(invokeMenuActionClassName);
            Method menuAction = Arrays.stream(classWithMenuAction.getDeclaredMethods()).
                    filter(method -> method.getName().equals(invokeMenuActionMethodName)).findAny().orElse(null);

            // When invoking, static methods need null as the first parameter
            assert menuAction != null;
            menuAction.setAccessible(true);
            if (invokeMenuActionJunkValue < 128 && invokeMenuActionJunkValue >= -128)
            {
                menuAction.invoke(null,param0,param1,opcode,identifier,itemid,option,target,x,y,(byte) invokeMenuActionJunkValue);
            }
            else
            {
                menuAction.invoke(null, param0, param1, opcode, identifier, itemid, option, target, x, y, invokeMenuActionJunkValue);
            }
            menuAction.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to invoke menu action.", e);
        }
    }
    
    public static void invoke(int param0, int param1, int opcode, int identifier, int itemid)
    {
        invoke(param0, param1, opcode, identifier, itemid, "", "", 0, 0);
    }
}
