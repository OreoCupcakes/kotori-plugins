package com.theplug.kotori.gauntletextended2.kotoriutils.libs;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class WalkingLibrary {

    @Inject
    private Client client;

    @Setter
    private String setXandYClassName;
    @Setter
    private String setXandYMethodName;

    @Setter
    private String setViewportWalkingClassName;
    @Setter
    private String setViewportWalkingFieldName;

    @Setter
    private String checkClickClassName;
    @Setter
    private String checkClickFieldName;

    public void setXandYCoordinates(int x, int y) {
        try {
            Class classWithSetXandY = client.getClass().getClassLoader().loadClass(setXandYClassName);
            Method setXandY = Arrays.stream(classWithSetXandY.getDeclaredMethods()).
                    filter(method -> method.getName().equals(setXandYMethodName)).findAny().orElse(null);
            setXandY.setAccessible(true);
            setXandY.invoke(null,x,y);
            setXandY.setAccessible(false);
        } catch (Exception e) {
            log.error("Failed to set X and Y coordinates.", e);
        }
    }

    public void setViewportWalking(boolean bool) {
        try {
            Class classWithViewportWalkingField = client.getClass().getClassLoader().loadClass(setViewportWalkingClassName);
            Field viewportWalkingField = classWithViewportWalkingField.getDeclaredField(setViewportWalkingFieldName);
            viewportWalkingField.setAccessible(true);
            viewportWalkingField.setBoolean(classWithViewportWalkingField,bool);
            viewportWalkingField.setAccessible(false);
        } catch (Exception e) {
            log.error("Failed to set viewport walking boolean.", e);
        }
    }

    public void setCheckClick(boolean bool) {
        try {
            Class classWithCheckClickField = client.getClass().getClassLoader().loadClass(checkClickClassName);
            Field checkClickField = classWithCheckClickField.getDeclaredField(checkClickFieldName);
            checkClickField.setAccessible(true);
            checkClickField.setBoolean(classWithCheckClickField,bool);
            checkClickField.setAccessible(false);
        } catch (Exception e) {
            log.error("Failed to set check click field.", e);
        }
    }

    public boolean getCheckClick() {
        boolean checkClick = false;

        try {
            Class classWithCheckClickField = client.getClass().getClassLoader().loadClass(checkClickClassName);
            Field checkClickField = classWithCheckClickField.getDeclaredField(checkClickFieldName);
            checkClickField.setAccessible(true);
            checkClick = checkClickField.getBoolean(classWithCheckClickField);
            checkClickField.setAccessible(false);
        } catch (Exception e) {
            log.error("Failed to get check click field.", e);
        }

        return checkClick;
    }
}
