package com.theplug.kotori.alchemicalhydra.utils;

import java.lang.annotation.*;

/**
 * Used with ConfigItem, defines what units are shown to the side of the box.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Units
{
    String MILLISECONDS = "ms";
    String MINUTES = " mins";
    String PERCENT = "%";
    String PIXELS = "px";
    String POINTS = "pt";
    String SECONDS = "s";
    String TICKS = " ticks";
    String LEVELS = " lvls";
    String FPS = " fps";
    String GP = " GP";

    String value();
}