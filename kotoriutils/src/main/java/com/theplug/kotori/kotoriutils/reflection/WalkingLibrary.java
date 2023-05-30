package com.theplug.kotori.kotoriutils.reflection;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.RuneLite;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class WalkingLibrary
{
    private static final Client client = RuneLite.getInjector().getInstance(Client.class);

    /*
    
    @Setter
    private static String xAndYClassName;
    @Setter
    private static String xAndYMethodName;
    
     */
    
    @Setter
    private static String sceneSelectedXClassName;
    @Setter
    private static String sceneSelectedXFieldName;
    
    @Setter
    private static String sceneSelectedYClassName;
    @Setter
    private static String sceneSelectedYFieldName;

    @Setter
    private static String viewportWalkingClassName;
    @Setter
    private static String viewportWalkingFieldName;

    @Setter
    private static String checkClickClassName;
    @Setter
    private static String checkClickFieldName;

    /*
    
    private static void setXandYCoordinates(int x, int y)
    {
        try
        {
            Class<?> classWithSetXandY = client.getClass().getClassLoader().loadClass(xAndYClassName);
            Method setXandY = Arrays.stream(classWithSetXandY.getDeclaredMethods()).
                    filter(method -> method.getName().equals(xAndYMethodName)).findAny().orElse(null);
            assert setXandY != null;
            setXandY.setAccessible(true);
            setXandY.invoke(null,x,y);
            setXandY.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set X and Y coordinates.", e);
        }
    }
    
     */
    
    private static void setXCoordinate(int x)
    {
        try
        {
            Class<?> sceneClass = client.getClass().getClassLoader().loadClass(sceneSelectedXClassName);
            Field xField = sceneClass.getDeclaredField(sceneSelectedXFieldName);
            xField.setAccessible(true);
            xField.setInt(client.getScene(), x);
            xField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set X coordinate.", e);
        }
    }
    
    private static void setYCoordinate(int y)
    {
        try
        {
            Class<?> sceneClass = client.getClass().getClassLoader().loadClass(sceneSelectedYClassName);
            Field yField = sceneClass.getDeclaredField(sceneSelectedYFieldName);
            yField.setAccessible(true);
            yField.setInt(client.getScene(), y);
            yField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set Y coordinate.", e);
        }
    }

    private static void setViewportWalking(boolean bool)
    {
        try
        {
            Class<?> classWithViewportWalkingField = client.getClass().getClassLoader().loadClass(viewportWalkingClassName);
            Field viewportWalkingField = classWithViewportWalkingField.getDeclaredField(viewportWalkingFieldName);
            viewportWalkingField.setAccessible(true);
            viewportWalkingField.setBoolean(classWithViewportWalkingField, bool);
            viewportWalkingField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set viewport walking boolean.", e);
        }
    }

    private static void setCheckClick(boolean bool)
    {
        try
        {
            Class<?> classWithCheckClickField = client.getClass().getClassLoader().loadClass(checkClickClassName);
            Field checkClickField = classWithCheckClickField.getDeclaredField(checkClickFieldName);
            checkClickField.setAccessible(true);
            checkClickField.setBoolean(classWithCheckClickField, bool);
            checkClickField.setAccessible(false);
        }
        catch (Exception e)
        {
            log.error("Failed to set check click field.", e);
        }
    }
    
    public static void sceneWalk(WorldPoint point)
    {
        Collection<WorldPoint> localInstanceWorldPoints = WorldPoint.toLocalInstance(client, point);
        WorldPoint walkingPoint = null;
        for (WorldPoint w : localInstanceWorldPoints)
        {
            walkingPoint = w;
            break;
        }
    
        if (walkingPoint == null || client.getPlane() != walkingPoint.getPlane() || !walkingPoint.isInScene(client))
        {
            return;
        }
    
        int scenePointX = walkingPoint.getX() - client.getBaseX();
        int scenePointY = walkingPoint.getY() - client.getBaseY();
    
        setXCoordinate(scenePointX);
        setYCoordinate(scenePointY);
    //    setXandYCoordinates(scenePointX, scenePointY);
        setCheckClick(false);
        setViewportWalking(true);
    }
    
    public static void sceneWalk(LocalPoint localPoint)
    {
        WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, localPoint);
        sceneWalk(worldPoint);
    }
    
    public static void sceneWalk(int worldPointX, int worldPointY, int plane)
    {
        WorldPoint point = new WorldPoint(worldPointX, worldPointY, plane);
        sceneWalk(point);
    }
}
