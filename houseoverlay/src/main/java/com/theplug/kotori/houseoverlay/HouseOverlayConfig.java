/*
 * Copyright (c) 2018, Andrew EP | ElPinche256 <https://github.com/ElPinche256>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.theplug.kotori.houseoverlay;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("HouseOverlay")

public interface HouseOverlayConfig extends Config {
    @ConfigItem(
            name = "Need Fairy Ring Staff",
            description = "Turn off if you completed Lumbridge Elite Diary",
            position = 1,
            keyName = "fairyStaff"
    )
    default boolean fairyStaff()
    {
        return true;
    }

    @Alpha
    @ConfigItem(
            name = "Default Color",
            description = "Choose the color of teleport objects",
            position = 2,
            keyName = "HouseObjectsDefaultColor"
    )
    default Color HouseObjectsDefaultColor()
    {
        return Color.GREEN;
    }

    @Alpha
    @ConfigItem(
            name = "Decorative Color",
            description = "Choose the color of decorative objects",
            position = 3,
            keyName = "HouseDecorativeColors"
    )
    default Color DecorativeColors()
    {
        return Color.ORANGE;
    }

    @Alpha
    @ConfigItem(
            name = "Text Color",
            description = "Choose the color of text on objects",
            position = 3,
            keyName = "TelportObjectsTextColor"
    )
    default Color TelportObjectsTextColor()
    {
        return Color.CYAN;
    }

}
