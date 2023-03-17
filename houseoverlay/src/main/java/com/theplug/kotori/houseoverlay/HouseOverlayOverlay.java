/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Cas <https://github.com/casvandongen>
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

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.TextComponent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Singleton
class HouseOverlayOverlay extends Overlay {
    private final Client client;
    private final HouseOverlayConfig config;
    private final HouseOverlayPlugin plugin;
    private final TextComponent textComponent = new TextComponent();

    @Inject
    private HouseOverlayOverlay(final Client client, final HouseOverlayConfig config, final HouseOverlayPlugin plugin) {
        super(plugin);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.config = config;
        this.plugin = plugin;
    }


    public Point mouse()
    {
        return client.getMouseCanvasPosition();
    }

    public static void renderClickBox(Graphics2D graphics, Point mousePosition, Shape objectClickbox, Color configColor)
    {
        if (objectClickbox.contains(mousePosition.getX(), mousePosition.getY()))
        {
            graphics.setColor(configColor.darker());
        }
        else
        {
            graphics.setColor(configColor);
        }

        graphics.draw(objectClickbox);
        graphics.setColor(new Color(configColor.getRed(), configColor.getGreen(), configColor.getBlue(), 50));
        graphics.fill(objectClickbox);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if(plugin.inhouse)
        {
            for (final GameObject gameObject : plugin.gameObjectCollection)
            {
                lastaction = "";
                extrainfo = "";
                int id = gameObject.getId();
                 String name = hotfixednames(id);
                if(name.isEmpty()) {
                    name = getname(id);
                    if(name.equals("skip"))
                    {
                        continue;
                    }
                }


                int modelHeight = gameObject.getRenderable().getModelHeight();
                switch(id)
                {
                    case 29241://Rejuvenate Pool
                    case 40848://Frozen reju Pool
                        //Portal Nexus Models
                    case 27097:
                    case 33354:
                    case 33355:
                    case 33356:
                    case 33357:
                    case 33358:
                    case 33359:
                    case 33360:
                    case 33361:
                    case 33362:
                    case 33363:
                    case 33364:
                    case 33365:
                    case 33366:
                    case 33367:
                    case 33368:
                    case 33369:
                    case 33370:
                    case 33371:
                    case 33372:
                    case 33373:
                    case 33374:
                    case 33375:
                    case 33376:
                    case 33377:
                    case 33378:
                    case 33379:
                    case 33380:
                    case 33381:
                    case 33382:
                    case 33383:
                    case 33384:
                    case 33385:
                    case 33386:
                    case 33387:
                    case 33388:
                    case 33389:
                    case 33390:
                    case 33391:
                    case 33392:
                    case 33393:
                    case 33394:
                    case 33395:
                    case 33396:
                    case 33397:
                    case 33398:
                    case 33399:
                    case 33400:
                    case 33401:
                    case 33402:
                    case 33403:
                    case 33404:
                    case 33405:
                    case 33406:
                    case 33407:
                    case 33408:
                    case 33409:
                    case 33410:
                    case 33423:
                    case 33424:
                    case 33425:
                    case 33426:
                    case 33427:
                    case 33428:
                    case 33429:
                    case 33430:
                    case 33431:
                    case 37547:
                    case 37548:
                    case 37549:
                    case 37550:
                    case 37551:
                    case 37552:
                    case 37553:
                    case 37554:
                    case 37555:
                    case 37556:
                    case 37557:
                    case 37559:
                    case 37560:
                    case 37561:
                    case 37562:
                    case 37563:
                    case 37564:
                    case 37565:
                    case 37566:
                    case 37567:
                    case 37568:
                    case 37569:
                    case 37571:
                    case 37572:
                    case 37573:
                    case 37574:
                    case 37575:
                    case 37576:
                    case 37577:
                    case 37578:
                    case 37579:
                    case 37580:
                    case 41413:
                    case 41414:
                    case 41415:
                    case 4525://Exit Portal
                        modelHeight = 65;
                        break;
                }

                ProcessObject(graphics, id, name, gameObject.getClickbox(), modelHeight, gameObject.getCanvasTextLocation(graphics, name, modelHeight), config.HouseObjectsDefaultColor());
            }

            for (final DecorativeObject dob : plugin.decorativeObjectCollection)
            {
                lastaction = "";
                extrainfo = "";
                int id = dob.getId();
                String name = hotfixednames(id);
                if(name.isEmpty()) {
                    name = getname(id);
                    if(name.equals("skip"))
                    {
                        continue;
                    }
                }
                ProcessObject(graphics, id, name, dob.getClickbox(), dob.getRenderable().getModelHeight(), dob.getCanvasTextLocation(graphics, name, dob.getRenderable().getModelHeight()), config.DecorativeColors());
            }
        }
        return null;
    }

    String lastaction = "";
    String extrainfo = "";
    public String getname(int id)
    {
        ObjectComposition def = client.getObjectDefinition(id);
        if (def != null) {
            if (def.getImpostorIds() != null) {
                def = def.getImpostor();
            }

            switch(id)
            {
                case 29156://Jewelery Box
                    lastaction = def.getActions()[2];
                    break;
                    //Mounted Xeric's Talisman Options
                case 33411:
                case 33412:
                case 33413:
                case 33414:
                case 33415:
                case 33419:
                    //Mounted Digsite Pendant Options
                case 33416://DigSite
                case 33417://Fossil Island
                case 33418://Lithkren
                case 33420://Default
                case 13523://Glory
                    //Portal Nexus Options
                case 27097:
                case 33354:
                case 33355:
                case 33356:
                case 33357:
                case 33358:
                case 33359:
                case 33360:
                case 33361:
                case 33362:
                case 33363:
                case 33364:
                case 33365:
                case 33366:
                case 33367:
                case 33368:
                case 33369:
                case 33370:
                case 33371:
                case 33372:
                case 33373:
                case 33374:
                case 33375:
                case 33376:
                case 33377:
                case 33378:
                case 33379:
                case 33380:
                case 33381:
                case 33382:
                case 33383:
                case 33384:
                case 33385:
                case 33386:
                case 33387:
                case 33388:
                case 33389:
                case 33390:
                case 33391:
                case 33392:
                case 33393:
                case 33394:
                case 33395:
                case 33396:
                case 33397:
                case 33398:
                case 33399:
                case 33400:
                case 33401:
                case 33402:
                case 33403:
                case 33404:
                case 33405:
                case 33406:
                case 33407:
                case 33408:
                case 33409:
                case 33410:
                case 33423:
                case 33424:
                case 33425:
                case 33426:
                case 33427:
                case 33428:
                case 33429:
                case 33430:
                case 33431:
                case 37547:
                case 37548:
                case 37549:
                case 37550:
                case 37551:
                case 37552:
                case 37553:
                case 37554:
                case 37555:
                case 37556:
                case 37557:
                case 37559:
                case 37560:
                case 37561:
                case 37562:
                case 37563:
                case 37564:
                case 37565:
                case 37566:
                case 37567:
                case 37568:
                case 37569:
                case 37571:
                case 37572:
                case 37573:
                case 37574:
                case 37575:
                case 37576:
                case 37577:
                case 37578:
                case 37579:
                case 37580:
                case 41413:
                case 41414:
                case 41415:
                    String action = def.getActions()[0];
                    if (action != null)
                    {
                        lastaction = action;
                    }
                    break;
                case 29228://Fairy Ring
                case 29229://Fairy Ring Tree
                case 40779://Frozen Fairy Ring Tree
                    for(String getter : def.getActions())
                    {
                        if(getter.toLowerCase().contains("last"))
                        {
                            lastaction = getter.replace(")", "").replace("(", "!");
                            lastaction = lastaction = "Last: " + plugin.get_fairy_ring_name(lastaction.split("!")[1]);
                            break;
                        }
                    }
                    break;
            }

            String[] actions = def.getActions();
            if(actions == null)
            {
                return "skip";
            }
            if(actions[0] != null && actions[0].equals("Enter")) {
                //WhiteList Portals
            }
            else
            {
                if (actions[1] == null) {
                    return "skip";
                }
                if (actions[1].equals("Jars")) {
                    return "skip";
                }
            }

            return def.getName().replace(" Portal", "");
        }
        return "";
    }

    public void ProcessObject(Graphics2D graphics, int id, String name, Shape clickbox, int ModelHeight, Point getCanvasTextLocation, Color c)
    {
        if(id < 1300)
        {
            //Ignore dumb shit like your player.
            return;
        }
        if(id == 13640)
        {
            //Ignore portal focus.
            return;
        }
        c = getcolor(id, c);
        if(clickbox != null) {
            renderClickBox(graphics, mouse(), clickbox, c);
        }

        if(getCanvasTextLocation == null)
        {
            return;
        }

        OverlayUtil.renderTextLocation(graphics, getCanvasTextLocation, name, config.TelportObjectsTextColor());
        if(!lastaction.isEmpty())
            OverlayUtil.renderTextLocation(graphics, new Point(getCanvasTextLocation.getX(), getCanvasTextLocation.getY() - 15), "(" + lastaction + ")", Color.RED);

        if(!extrainfo.isEmpty())
            OverlayUtil.renderTextLocation(graphics, new Point(getCanvasTextLocation.getX(), getCanvasTextLocation.getY() + 15), extrainfo, Color.MAGENTA);
    }

    public String hotfixednames(int id)
    {
        switch(id)
        {
            case 4525:
                return "Exit";
            case 29241:
            case 40848://Frozen
                return "Rejuvenation Pool";
        }

        return "";
    }

    public Color getcolor(int id, Color defaultcolor)
    {
        if(id == 29241 || id == 40848)//Ornate rej.
        {
            if(plugin.currentanimation == 7305)
            {
                return Color.ORANGE;
            }
            else {
                return Color.MAGENTA;
            }
        }

        if(id == 4525)//Exit
        {
            return Color.RED;
        }

        if(id == 29228)//Fairy Ring
        {
            if(config.fairyStaff())
                if(!plugin.fairy_ring_has_staff)
                {
                    extrainfo = "WIELD STAFF";
                    return Color.PINK;
                }
        }

        return defaultcolor;
    }
}