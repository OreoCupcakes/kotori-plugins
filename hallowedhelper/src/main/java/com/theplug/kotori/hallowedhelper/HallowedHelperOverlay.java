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
package com.theplug.kotori.hallowedhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Point;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.TextComponent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@Slf4j
@Singleton
class HallowedHelperOverlay extends Overlay
{
    @Inject
    private ItemManager itemManager;


    private static final int MAX_DISTANCE = 2350;
    private static final Color SHORTCUT_HIGH_LEVEL_COLOR = Color.ORANGE;

    private final Client client;
    private final HallowedHelperConfig config;
    private final HallowedHelperPlugin plugin;
    private final TextComponent textComponent = new TextComponent();


    private Player player;

    @Inject
    private HallowedHelperOverlay(final Client client, final HallowedHelperConfig config, final HallowedHelperPlugin plugin)
    {
        super(plugin);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.config = config;
        this.plugin = plugin;
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
    public Dimension render(Graphics2D graphics)
    {
        if (!plugin.isPlayerInSepulchre())
        {
            return null;
        }

        player = client.getLocalPlayer();

        if (player == null)
        {
            return null;
        }

        render_floor1cSafeSpot(graphics);
        render_arrows(graphics);
        render_swords(graphics);
        render_bridges(graphics);
        render_crossbow(graphics);
        render_servertile(graphics);
        render_sword_statues(graphics);
        render_lightning(graphics);
        render_teleport_portals(graphics, plugin.getBluePortals(), Color.CYAN);
        render_teleport_portals(graphics, plugin.getYellowPortals(), Color.YELLOW);
        if(config.ShowStairs()) {
            render_stairs(graphics);
        }
        render_statues(graphics);
        if(plugin.currentfloor == 4)
        {
            if (plugin.isFloor4ComplicatedWizardStatues)
            {
                render_floor4_statues(graphics);
            }
            else
            {
                if (plugin.gameticksOnFloor4Plane2SouthSide <= 25)
                {
                    render_floor4_southA_statues(graphics);
                    render_floor4_southB_statues(graphics);
                }
            }
        }
        else if(plugin.currentfloor == 5)
        {
            if(client.getPlane() == 2) {
                render_floor5_statues(graphics);
            }
            else if(client.getPlane() == 1) {
                render_floor5_2A_statues(graphics);
            }
            else if(client.getPlane() == 0) {
                render_floor5_4_statues(graphics);
            }
        }

        if(plugin.isDoorOpen()) {
            if(config.ShowChests()) {
                render_chest(graphics);
            }
            if(config.ShowPortals()) {
                render_portal(graphics);
            }
            if(config.ShowFloorGate())
            {
                render_floor_gates(graphics);
            }
        }

        return null;
    }

    private int currentpoint = 1;
    WorldPoint w;
    public void render_floor4_statues(Graphics2D g)
    {
        currentpoint = 1;
        boolean unreachable = client.getLocalPlayer().getLocalLocation().getX() > 7359 && plugin.floor4_fire_rotation > 2;
        if(plugin.Floor4WizardBottom1 != null) {
            w = plugin.Floor4WizardBottom1.getWorldLocation();
            w = new WorldPoint(w.getX() + 1, w.getY() + 1, w.getPlane());
            ArrayList<Color> rotation = Rotation.floor4_rotations_north.get(plugin.floor4_fire_rotation - 1);
            if(rotation != null)
            {
                if(unreachable)
                {
                    OverlayText(g, plugin.Floor4WizardBottom1.getLocalLocation(), "Wait for next Rotation", Color.CYAN, 0, 0);
                }
                for(Color current : rotation)
                {
                    if(unreachable)
                    {
                        if (plugin.floor4_fire_rotation < 5)
                        {
                            current = config.UnsafeTileColor();
                        }
                    }
                    decrementCurrentpoint();
                    render_server_line_offsetx(g, current, w, currentpoint);
                    String ticksText = "" + (7 - plugin.floor_4_ticks_since_statue);
                    render_statue_ticks_floor_4_5_line(g, current, w, currentpoint, ticksText);
                }
            }
        }
    }
    
    public void render_floor4_southA_statues(Graphics2D g)
    {
        currentpoint = 1;
        if(plugin.floor4_SouthA_BottomLeft != null) {
            w = plugin.floor4_SouthA_BottomLeft.getWorldLocation();
            w = new WorldPoint(w.getX() + 1, w.getY() + 1, w.getPlane());
            ArrayList<Color> rotation = Rotation.floor4_rotations_southA.get(plugin.floor4_southA_fire_rotation - 1);
            if(rotation != null)
            {
                for(Color current : rotation)
                {
                    decrementCurrentpoint();
                    if(current == Rotation.blank)
                    {
                        continue;
                    }
                    if(!plugin.floor4_SouthA_first_fire_detected)
                    {
                        render_server_line_offsetx(g, Color.YELLOW, w, currentpoint);
                    }
                    else {
                        render_server_line_offsetx(g, current, w, currentpoint);
                    }
                }
            }
        }
    }
    
    public void render_floor4_southB_statues(Graphics2D g)
    {
        currentpoint = 1;
        if(plugin.floor4_SouthB_BottomLeft != null) {
            w = plugin.floor4_SouthB_BottomLeft.getWorldLocation();
            w = new WorldPoint(w.getX() + 1, w.getY() - 2, w.getPlane());
            ArrayList<Color> rotation = Rotation.floor4_rotations_southB.get(plugin.floor4_southB_fire_rotation - 1);
            if(rotation != null)
            {
                for(Color current : rotation)
                {
                    incrementCurrentpoint();
                    if(current == Rotation.blank)
                    {
                        continue;
                    }
                    if(!plugin.floor4_SouthB_first_fire_detected)
                    {
                        render_server_line_offsety(g, Color.YELLOW, w, currentpoint);
                    }
                    else {
                        render_server_line_offsety(g, current, w, currentpoint);
                    }
                }
            }
        }
    }
    
    public void render_floor5_statues(Graphics2D g)
    {
        currentpoint = 1;
        if(plugin.floor5BottomLeft != null) {
            w = plugin.floor5BottomLeft.getWorldLocation();
            w = new WorldPoint(w.getX() + 1, w.getY() + 1, w.getPlane());
            ArrayList<Color> rotation = Rotation.floor5_rotations.get(plugin.floor5_fire_rotation - 1);
            if(rotation != null)
            {
                for(Color current : rotation)
                {
                    decrementCurrentpoint();
                    if(!plugin.floor_5_first_fire_detected) {
                        render_server_line_offsetx(g, Color.YELLOW, w, currentpoint);
                    }
                    else
                    {
                        render_server_line_offsetx(g, current, w, currentpoint);
                    }
                }
            }
        }
    }
    public void render_floor5_2A_statues(Graphics2D g)
    {
        currentpoint = 1;
        if(plugin.floor5_2ATopLeft != null) {
            w = plugin.floor5_2ATopLeft.getWorldLocation();
            //Offset since were at the top.
            w = new WorldPoint(w.getX() + 12 + 1, w.getY() + 1, w.getPlane());
            ArrayList<Color> rotation = Rotation.floor5_2A_rotations.get(plugin.floor5_2A_fire_rotation - 1);
            ArrayList<Color> rotation2 = Rotation.floor5_3A_rotations.get(plugin.floor5_2A_fire_rotation - 1);
            if(rotation != null)
            {
                for(Color current : rotation)
                {
                    decrementCurrentpoint();
                    if(!plugin.floor5_2A_first_fire_detected)
                    {
                        render_server_line_offsetx(g, Color.YELLOW, w, currentpoint);
                    }
                    else {
                        render_server_line_offsetx(g, current, w, currentpoint);
                    }
                }
            }

            currentpoint = 1;
            int offset = 25;//Prolly needs bigger/smaller
            w = new WorldPoint(w.getX() + offset, w.getY(), w.getPlane());
            if(rotation2 != null)
            {
                for(Color current : rotation2)
                {
                    decrementCurrentpoint();
                    if(!plugin.floor5_2A_first_fire_detected)
                    {
                        render_server_line_offsetx(g, Color.YELLOW, w, currentpoint);
                    }
                    else {
                        render_server_line_offsetx(g, current, w, currentpoint);
                    }
                }
            }
        }
    }

    public void render_floor5_4_statues(Graphics2D g)
    {
        currentpoint = 1;
        if(plugin.floor5_4BottomLeft != null) {
            w = plugin.floor5_4BottomLeft.getWorldLocation();
            w = new WorldPoint(w.getX() + 1, w.getY() + 1, w.getPlane());
            ArrayList<Color> rotation = Rotation.floor5_4_rotations.get(plugin.floor5_4_fire_rotation - 1);
            if(rotation != null)
            {
                for(Color current : rotation)
                {
                    decrementCurrentpoint();
                    if(current == Rotation.blank)
                    {
                        continue;
                    }
                    if(!plugin.floor_5_4_first_fire_detected)
                    {
                        render_server_line_offsetx(g, Color.YELLOW, w, currentpoint);
                    }
                    else {
                        render_server_line_offsetx(g, current, w, currentpoint);
                    }
                }
            }
        }
    }

    public void green(Graphics2D g)
    {
        decrementCurrentpoint();
        render_server_line_offsetx(g, Color.GREEN, w, currentpoint);
    }

    public void red(Graphics2D g)
    {
        decrementCurrentpoint();
        render_server_line_offsetx(g, Color.RED, w, currentpoint);
    }
    public void farsafe(Graphics2D g)
    {
        decrementCurrentpoint();
        render_server_line_offsetx(g, Color.BLUE, w, currentpoint);
    }
    public void nextsafe(Graphics2D g)
    {
        decrementCurrentpoint();
        render_server_line_offsetx(g, Color.ORANGE, w, currentpoint);
    }

    public void decrementCurrentpoint()
    {
        currentpoint--;
    }
    
    public void incrementCurrentpoint()
    {
        currentpoint++;
    }

    public void render_server_line_offsetx(Graphics2D g, Color c, WorldPoint w, int offsetx)
    {
        render_object_server_tile(g, w, c, offsetx, 1);
        render_object_server_tile(g, w, c, offsetx, 2);
        render_object_server_tile(g, w, c, offsetx, 3);
    }
    
    public void render_server_line_offsety(Graphics2D g, Color c, WorldPoint w, int offsety)
    {
        render_object_server_tile(g, w, c, 1, offsety);
        render_object_server_tile(g, w, c, 2, offsety);
        render_object_server_tile(g, w, c, 3, offsety);
    }
    
    public void render_statue_ticks_floor_4_5_line(Graphics2D g, Color c, WorldPoint w, int offsetx, String text)
    {
        render_statue_ticks_floor_4_5_tile(g, w, c, text, offsetx, 2);
    }
    
    public void render_statue_ticks_floor_4_5_tile(Graphics2D graphics, WorldPoint worldPoint, Color color, String text, int offsetx, int offsety)
    {
        WorldPoint w = new WorldPoint(worldPoint.getX() + offsetx, worldPoint.getY() + offsety, worldPoint.getPlane());
        final LocalPoint localPoint = LocalPoint.fromWorld(client, w);
    
        if (localPoint == null)
        {
            return;
        }
    
        OverlayTextManual(graphics, localPoint, text, color, 0, 0);
    }

    public BufferedImage getRedCog()
    {
        return itemManager.getImage(ItemID.RED_COG);
    }

    public final int bolt1 = 9671;
    public final int bolt2 = 9672;
    public final int bolt3 = 9673;

    public void render_floor1cSafeSpot(Graphics2D graphics)
    {
        if(plugin.currentfloor == 1)
        {
            if(plugin.subfloor == 3)
            {
                if(client.getPlane() == 2)
                {
                    LocalPoint lp = new LocalPoint(4159, 7103);
                    if (lp == null)
                    {
                        return;
                    }

                    if(WorldPoint.fromLocal(client, lp).getPlane() != 2)
                    {
                        return;
                    }

                    if(isOutsideRenderDistance(lp))
                    {
                        return;
                    }

                    Polygon poly = Perspective.getCanvasTilePoly(client, lp);
                    if (poly == null)
                    {
                        return;
                    }
                    OverlayUtil.renderPolygon(graphics, poly, Color.CYAN);
                    OverlayText(graphics, lp, "SAFESPOT", Color.GREEN, 0, 0);
                }
            }
        }
    }

    public void render_arrow_point(NPC arrow, Graphics2D graphics)
    {
        int arrow_orientation = arrow.getOrientation();
        int orientation = (arrow_orientation / 512);
        switch(orientation)
        {
            case 0:
                render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 0 , -1);
                render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 0 , -2);
                break;
            case 1:
                render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, -1 , 0);
                render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, -2 , 0);
                break;
            case 2:
                render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 0 , 1);
                render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 0 , 2);
                break;
            case 3:
                render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 1 , 0);
                render_object_server_tile(graphics, arrow.getWorldLocation(), Color.YELLOW, 2 , 0);
                break;
        }
    }

    public void render_arrows(Graphics2D graphics)
    {
        for(NPC arrow : plugin.getArrows())
        {
            if (arrow.getWorldLocation().getPlane() != client.getPlane())
            {
                continue;
            }
            if(isOutsideRenderDistance(arrow.getLocalLocation()))
            {
                continue;
            }
            LocalPoint l = arrow.getLocalLocation();
            //OverlayUtil.renderActorOverlay(graphics, arrow, "Bolt1 (" + or + ")", Color.RED);
            render_object_server_tile(graphics, arrow.getWorldLocation(), Color.RED, 0 , 0);
            render_arrow_point(arrow, graphics);
            if(config.ShowValues()) {
                int or = (arrow.getOrientation() / 512);
                switch (arrow.getId()) {
                    case bolt1:
                        OverlayUtil.renderActorOverlay(graphics, arrow, "Bolt1 (" + or + ")", Color.RED);
                        break;
                    case bolt2:
                        OverlayUtil.renderActorOverlay(graphics, arrow, "Bolt2 (" + or + ")", Color.RED);
                        break;
                    case bolt3:
                        OverlayUtil.renderActorOverlay(graphics, arrow, "Bolt3 (" + or + ")", Color.RED);
                        break;
                }
            }
        }
    }

    public void render_swords(Graphics2D graphics)
    {
        for(HallowedSepulchreSword SWORDCLASS : plugin.getSwords())
        {
            NPC sword = SWORDCLASS.getNpc();
            if (sword.getWorldLocation().getPlane() != client.getPlane())
            {
                continue;
            }
            if(isOutsideRenderDistance(sword.getLocalLocation()))
            {
                continue;
            }
            int or = (sword.getOrientation() / 512);
            LocalPoint l = sword.getLocalLocation();
            WorldPoint w = WorldPoint.fromLocalInstance(client, l);
            GameObject thrower = SWORDCLASS.getThrower();
            if(thrower == null)
            {
                OverlayUtil.renderActorOverlay(graphics, sword, "Sword (" + or + ") - FAILED", Color.RED);
                return;
            }

            int offset = SWORDCLASS.getOffset();
            int distance = SWORDCLASS.getDistance();
            int offseted_distance = distance + offset;
            String distance_text = "" + distance;
            String offset_text = "";
            if(offset != 0)
            {
                offset_text = "" + offset;
            }
            boolean forward = SWORDCLASS.isForward();
            Color c = Color.BLACK;

            int identifier = plugin.identifygameojectbylocalocation(thrower);
            int maxdistance = 16;
            int mindistance = 4;
            int ticksuntilsafe = 0;

            boolean floor4sword1 = false;
            switch(identifier) {
                case 13056://Floor 1c
                    maxdistance = 18;
                    break;
                case 15104://Floor 2 vgm
                case 16640:
                    mindistance = 2;
                    break;
                //case 13569:
                /*case 21191://Floor 1c
                    maxdistance = 12;
                    break;
                case 13569:
                    mindistance = 2;
                    break;
                case 13959://floor1csoortvan
                    //mindistance = 4;
                    break;*/
            }

            if(plugin.getCurrentfloor() == 4)
            {
                if(sword.getId() == 9671)//rune sword
                {
                    mindistance = 3;
                    maxdistance = 4;
                    floor4sword1 = true;
                }
            }

            if(forward)
            {
                if(offseted_distance > maxdistance)
                {
                    c = Color.RED;
                }
                else
                {
                    c = Color.GREEN;
                    ticksuntilsafe = maxdistance - distance;
                }
            }
            else
            {
                if(offseted_distance == 0)
                {
                    c = Color.GREEN;
                    //Hotfix for the offset when the distance reaches 0 instead of 1.
                }
                else {
                    if (offseted_distance == (mindistance + 1) || offseted_distance == (mindistance + 2)) {
                        c = Color.ORANGE;
                    } else {
                        if (offseted_distance <= mindistance) {
                            c = Color.GREEN;
                        } else {
                            c = Color.RED;
                        }
                    }
                }
            }



            int movementpertick = 2;

            if(sword.getId() == 9670)//gold sword
            {
                movementpertick = 3;
                if(offset > 0) {
                    //The two swords on floor 3 and 4 that have a 4 tick cycle
                    if (w.equals(new WorldPoint(2504,5886,2)) || w.equals(new WorldPoint(2391, 5837, 2)))
                    {
                        offset_text += "/4";
                    }
                    //The remaining floor 4 swords (2 of them) with this ID have a 8 tick cycle
                    else if (plugin.getCurrentfloor() == 4)
                    {
                        offset_text += "/8";
                    }
                    //Every other sword with this ID has a 5 tick cycle (the rest of floor 3 swords)
                    else
                    {
                        offset_text += "/5";
                    }
                }
            }
            else if(sword.getId() == 9671)//rune sword
            {
                if(offset_text != "")
                {
                    if(offset > 0) {
                        //Floor 5 Plane 2, 1st sword
                        if (plugin.getCurrentfloor() == 5 && w.getPlane() == 2)
                        {
                            offset_text += "/5";
                        }
                        //Floor 5 Plane 1, 2nd sword
                        else if (plugin.getCurrentfloor() == 5 && w.getPlane() == 1)
                        {
                            offset_text += "/4";
                        }
                        //Floor 4 Plane 1, East sword
                        else
                        {
                            offset_text += "/10";
                        }
                    }
                }
                movementpertick = 4;
            }
            else
            {
                //Floors 1 and 2 swords
                if(offset > 0) {
                    //Floor 2 Plane 2 northwest sword, the only 4 tick sword from this ID
                    if (w.equals(new WorldPoint(2511,5999,2)))
                    {
                        offset_text += "/4";
                    }
                    else
                    {
                        offset_text += "/3";
                    }
                }
            }
            ticksuntilsafe = (int)Math.ceil(Math.abs(distance - mindistance) / movementpertick);
            //ALS DIE GOUD IS MOET DIE /3

            if (c == Color.RED) {
                if(floor4sword1) {
                    if (distance > maxdistance) {
                        c = Color.ORANGE;
                        OverlayText(graphics, sword.getLocalLocation(), "RISK (" + ticksuntilsafe + ")", c, 0, 0);
                    }
                }
                else {
                    OverlayText(graphics, sword.getLocalLocation(), "DANGER (" + ticksuntilsafe + ")", c, 0, 0);
                    OverlayText(graphics, thrower.getLocalLocation(), "DANGER (" + ticksuntilsafe + ")", c, 0, 0);
                }
            } else if (c == Color.GREEN) {
                ticksuntilsafe = (int) Math.ceil(Math.abs(maxdistance - distance) / 2) + 1;
                if (forward) {
                    OverlayText(graphics, sword.getLocalLocation(), "RUN (" + ticksuntilsafe + ")", c, 0, 0);
                    OverlayText(graphics, thrower.getLocalLocation(), "RUN (" + ticksuntilsafe + ")", c, 0, 0);
                } else {
                    OverlayText(graphics, sword.getLocalLocation(), "RUN", c, 0, 0);
                    OverlayText(graphics, thrower.getLocalLocation(), "RUN", c, 0, 0);
                }
            } else if (c == Color.ORANGE) {
                OverlayText(graphics, sword.getLocalLocation(), "GET READY (" + ticksuntilsafe + ")", c, 0, 0);
            }

            if(!offset_text.isEmpty())
            {
                OverlayTextManual(graphics, sword.getLocalLocation(), offset_text, c, 0, 20);
            }
            //OverlayUtil.renderActorOverlay(graphics, sword, "xSword (" + or + ")", c);


            //render_object_server_tile(graphics, sword.getWorldLocation(), Color.CYAN, 0, 0);
            //thrower.getHash() HASH IS FUCKING USELESS.

            if(config.ShowValues()) {
                OverlayUtil.renderTileOverlay(graphics, thrower, "TYPE(" + thrower.getId() + ") - IDENTIFY_ID(" + plugin.identifygameojectbylocalocation(thrower) + ") " + "P:" + thrower.getWorldLocation().getPlane() + ". Dist: " + distance + ", Forw: " + SWORDCLASS.forward, Color.CYAN);
            }
            render_object_server_tile_group(graphics, sword.getWorldLocation(), c, 1, 1);
            /*if(or == 1 || or == 3)
            {
                render_object_server_tile_group(graphics, sword.getWorldLocation(), c, 1, 1);
                //render_object_server_tile(graphics, sword.getWorldLocation(), c, 0, 1);
                //render_object_server_tile(graphics, sword.getWorldLocation(), c, 0, 2);
                //OverlayUtil.renderTileOverlay(client, graphics, new LocalPoint(l.getX(), l.getY() + 150), getRedCog(), c);
                //OverlayUtil.renderTileOverlay(client, graphics, new LocalPoint(l.getX(), l.getY() - 150), getRedCog(), c);
            }
            else {
                render_object_server_tile_group(graphics, sword.getWorldLocation(), c, 1, 1);
                //0 and 2
                //render_object_server_tile(graphics, sword.getWorldLocation(), c, 1, 0);
                //render_object_server_tile(graphics, sword.getWorldLocation(), c, 2, 0);
                //OverlayUtil.renderTileOverlay(client, graphics, new LocalPoint(l.getX() + 150, l.getY()), getRedCog(), c);
                //OverlayUtil.renderTileOverlay(client, graphics, new LocalPoint(l.getX() - 150, l.getY()), getRedCog(), c);
            }*/
        }
    }

    public void render_object_server_tile(Graphics2D graphics, WorldPoint worldlocation, Color color, int offsetx, int offsety)
    {
        WorldPoint w = new WorldPoint(worldlocation.getX() + offsetx, worldlocation.getY() + offsety, worldlocation.getPlane());
        final LocalPoint localPoint = LocalPoint.fromWorld(client, w);

        if (localPoint == null)
        {
            return;
        }

        Polygon polygon = Perspective.getCanvasTilePoly(client, localPoint);

        if (polygon == null)
        {
            return;
        }
        drawStrokeAndFill(graphics, color, config.ServerTileFill(),
                1.0f, polygon);
    }

    public void render_object_server_tile_group(Graphics2D graphics, WorldPoint worldlocation, Color color, int offsetx, int offsety)
    {
        WorldPoint w = new WorldPoint(worldlocation.getX() + offsetx, worldlocation.getY() + offsety, worldlocation.getPlane());
        final LocalPoint localPoint = LocalPoint.fromWorld(client, w);

        if (localPoint == null)
        {
            return;
        }

        Polygon polygon = Perspective.getCanvasTileAreaPoly(client, localPoint,3);

        if (polygon == null)
        {
            return;
        }
        drawStrokeAndFill(graphics, color, config.ServerTileFill(),
                1.0f, polygon);
    }

    private int bridge_build = 38808;
    public void render_bridges(Graphics2D graphics)
    {

        //HIER FIXEN DAT ALLEEN DE EERSTE GESHOWED WORDT
        for(GroundObject bridge : plugin.getBridges())
        {
            if (bridge.getPlane() != client.getPlane())
            {
                continue;
            }
            if(isOutsideRenderDistance(bridge.getLocalLocation()))
            {
                continue;
            }
            ObjectComposition definition = client.getObjectDefinition(bridge.getId());
            if (definition != null) {
                if (definition.getImpostorIds() != null) {
                    definition = definition.getImpostor();
                }
                Shape clickbox = bridge.getClickbox();
                if(clickbox == null)
                {
                    continue;
                }
                int varbit = definition.getId();
                if (varbit == bridge_build) {
                    renderClickBox(graphics, mouse(), clickbox, Color.GREEN);
                    //modelOutlineRenderer.drawOutline(bridge, 2, Color.GREEN);
                }
                else
                {
                    renderClickBox(graphics, mouse(), clickbox, Color.YELLOW);
                    //modelOutlineRenderer.drawOutline(bridge, 2, Color.YELLOW);
                }
            }
        }
    }

    public Point mouse()
    {
        return client.getMouseCanvasPosition();
    }

    private int portal_build = 38829;
    public void render_portal(Graphics2D graphics)
    {
        for(GameObject portal : plugin.getPortals())
        {
            if (portal.getPlane() != client.getPlane())
            {
                continue;
            }
            if(isOutsideRenderDistance(portal.getLocalLocation()))
            {
                continue;
            }
            ObjectComposition definition = client.getObjectDefinition(portal.getId());
            if (definition != null) {
                if (definition.getImpostorIds() != null) {
                    definition = definition.getImpostor();
                }

                Shape clickbox = portal.getClickbox();
                if(clickbox == null)
                {
                    continue;
                }
                int varBit = definition.getId();
                if (varBit == portal_build) {
                    //modelOutlineRenderer.drawOutline(portal, 2, config.portalOpenColor());
                    renderClickBox(graphics, mouse(), clickbox, config.portalOpenColor());
                }
                else
                {
                    //modelOutlineRenderer.drawOutline(portal, 2, config.portalColor());
                    renderClickBox(graphics, mouse(), clickbox, config.portalColor());
                }
            }
        }
    }


    private static final Set<Integer> CHEST_ANIMATIONS = Set.of(
            3692,
            4344,
            8691
    );

    private static final Set<Integer> CHEST_CLOSED_IDS = Set.of(
            38830,
            38832,
            38834,
            38836,
            38838
    );
    public void render_chest(Graphics2D graphics)
    {
        for(GameObject chest : plugin.getChests())
        {
            if (chest.getPlane() != client.getPlane())
            {
                continue;
            }
            if(isOutsideRenderDistance(chest.getLocalLocation()))
            {
                continue;
            }
            Shape chest_clickbox = chest.getClickbox();
            if(chest_clickbox == null)
            {
                continue;
            }
            ObjectComposition definition = client.getObjectDefinition(chest.getId());
            if (definition != null) {
                if (definition.getImpostorIds() != null) {
                    definition = definition.getImpostor();
                }
                int varBit = definition.getId();
                if (CHEST_CLOSED_IDS.contains(varBit)) {
                    //OverlayUtil.renderTileOverlay(graphics, chest, "CHEST[1]", Color.GREEN);
                    if(plugin.getGraphic() == 267)
                    {
                        renderClickBox(graphics, mouse(), chest_clickbox, config.chestOpeningFail());
                        continue;
                    }
                    if(CHEST_ANIMATIONS.contains(plugin.getAnimation()))
                    {
                        switch(plugin.getAnimation()) {
                            case 3692:
                                renderClickBox(graphics, mouse(), chest_clickbox, config.chestOpeningColor());
                                break;
                            case 4344:
                                renderClickBox(graphics, mouse(), chest_clickbox, config.chestOpeningColor2());
                                break;
                            case 8691:
                                renderClickBox(graphics, mouse(), chest_clickbox, config.chestOpeningColor3());
                                break;
                        }
                    }
                    else
                    {
                        renderClickBox(graphics, mouse(), chest_clickbox, config.chestColor());
                    }
                }
            }
        }
    }

    public void render_floor_2(Graphics2D graphics)
    {
        if(plugin.getFloor_gates().size() == 0)
        {
            return;
        }
        GameObject closest = plugin.getFloor_gates().iterator().next();
        for(GameObject gate : plugin.getFloor_gates()) {
            if(gate.getLocalLocation().distanceTo(player.getLocalLocation()) < closest.getLocalLocation().distanceTo(player.getLocalLocation()))
            {
                closest = gate;
            }
        }
        //modelOutlineRenderer.drawOutline(closest, 2, config.floorgateColor());
        Shape clickbox = closest.getClickbox();
        if(clickbox == null)
        {
            return;
        }
        renderClickBox(graphics, mouse(), clickbox, config.floorgateColor());
    }

    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ) );
    }

    public void render_floor_gates(Graphics2D graphics)
    {
        if(plugin.getCurrentfloor() == -1)
        {
            return;
        }
        if(plugin.getCurrentfloor() == 2)
        {
            render_floor_2(graphics);
            return;
        }

        Player local = client.getLocalPlayer();

        int clientplane = client.getPlane();
        for(GameObject stairs : plugin.getFloor_gates())
        {
            if (stairs.getPlane() != clientplane)
            {
                continue;
            }
            if(isOutsideRenderDistance(stairs.getLocalLocation()))
            {
                continue;
            }

            //OverlayUtil.renderTileOverlay(graphics, stairs, "Stairs (" + stairs.getId() + ") Or:[" + stairs.getRsOrientation() + "] - Plank: " + stairs.getPlane() +" - Hash: " + stairs.getHash() +"-X:" + stairs.getX()+"-Y:" + stairs.getY(), Color.RED);
            //modelOutlineRenderer.drawOutline(stairs, 2, Color.CYAN);
            switch (plugin.getCurrentfloor()) {
                case 1:
                    switch (plugin.getSubfloor()) {
                        case 1:
                            if(client.getLocalPlayer().getLocalLocation().getSceneX() > 60)
                            {
                                return;
                            }
                            if ((stairs.getHash() != 5193373618L))
                            {
                                continue;
                            }
                            break;
                        case 2:
                            if ((stairs.getHash() != 5193506773L)) {
                                continue;
                            }
                            break;
                        case 3:
                            if ((stairs.getX() != 7936) && (stairs.getX() != 11008)) {
                                continue;
                            }
                            break;
                    }
                    break;
                case 2:
                    int posY = client.getLocalPlayer().getWorldLocation().getY();
                    if(posY > 12019) {
                        if ((stairs.getY() != 6528)) {
                            continue;
                        }
                    }
                    else {//Miss andere posY check hier
                        if ((stairs.getHash() != 5193504690L)) {
                            continue;
                        }
                    }
                    break;
                case 3:
                    if ((stairs.getHash() != 5193636143L)) {
                        continue;
                    }
                    break;
            }
            //modelOutlineRenderer.drawOutline(stairs, 2, config.floorgateColor());
            Shape clickbox = stairs.getClickbox();
            if(clickbox == null)
            {
                continue;
            }
            renderClickBox(graphics, mouse(), clickbox, config.floorgateColor());
            //return;
        }
    }

    public void render_stairs(Graphics2D graphics)
    {
        for(GameObject stairs : plugin.getStairs())
        {

            if (stairs.getPlane() != client.getPlane())
            {
                continue;
            }
            LocalPoint l = stairs.getLocalLocation();
            if(l == null)
            {
                continue;
            }
            if(isOutsideRenderDistance(l))
            {
                continue;
            }

            Shape Clickbox = stairs.getClickbox();
            if(Clickbox == null)
            {
                continue;
            }
            renderClickBox(graphics, mouse(), Clickbox, config.stairsColor());
            //modelOutlineRenderer.drawOutline(stairs, 1, config.stairsColor());
        }
    }

    private void render_servertile(final Graphics2D graphics2D)
    {
        if (!config.ShowServerTile())
        {
            return;
        }

        final WorldPoint worldPoint = player.getWorldLocation();

        if (worldPoint == null)
        {
            return;
        }

        final LocalPoint localPoint = LocalPoint.fromWorld(client, worldPoint);

        if (localPoint == null)
        {
            return;
        }

        final Polygon polygon = Perspective.getCanvasTilePoly(client, localPoint);

        if (polygon == null)
        {
            return;
        }

        drawStrokeAndFill(graphics2D, config.ServerTileOutline(), config.ServerTileFill(),
                1.0f, polygon);
    }

    public Polygon TileOffsetter(LocalPoint point, int offsetx, int offsety)
    {
        return Perspective.getCanvasTilePoly(client, new LocalPoint(point.getX() + (offsetx * Perspective.LOCAL_TILE_SIZE), point.getY() + (offsety * Perspective.LOCAL_TILE_SIZE)));
    }

    public void OverlayText(Graphics2D graphics, LocalPoint lp, String text, Color color, int offsetx, int offsety)
    {
        final Point textPoint = Perspective.getCanvasTextLocation(client,
                graphics,
                new LocalPoint(lp.getX() + (offsetx * Perspective.LOCAL_TILE_SIZE), lp.getY() + (offsety * Perspective.LOCAL_TILE_SIZE)),
                text,
                0);

        if(textPoint == null)//sometimes fails?
        {
            return;
        }

        textComponent.setText(text);
        textComponent.setColor(color);
        textComponent.setPosition(new java.awt.Point(textPoint.getX(), textPoint.getY()));
        textComponent.render(graphics);
    }

    public void OverlayTextManual(Graphics2D graphics, LocalPoint lp, String text, Color color, int offsetx, int offsety)
    {
        final Point textPoint = Perspective.getCanvasTextLocation(client,
                graphics,
                lp,
                text,
                0);

        if(textPoint == null)//sometimes fails?
        {
            return;
        }

        textComponent.setText(text);
        textComponent.setColor(color);
        textComponent.setPosition(new java.awt.Point(textPoint.getX() + offsetx, textPoint.getY() + offsety));
        textComponent.render(graphics);
    }

    public LocalPoint localpointoffset(LocalPoint lp, int offsetx, int offsety)
    {
        return new LocalPoint(lp.getX() + (offsetx * Perspective.LOCAL_TILE_SIZE), lp.getY() + (offsety * Perspective.LOCAL_TILE_SIZE));
    }

    private static final int CROSSBOW_STATUE_ANIM_DEFAULT = 8681;
    private static final int CROSSBOW_STATUE_ANIM_FINAL = 8685;
    private void render_crossbow(final Graphics2D graphics2D)
    {
        if (plugin.getCrossbowStatues().isEmpty())
        {
            return;
        }

        for (final GameObject gameObject : plugin.getCrossbowStatues())
        {
            if (!gameObject.getWorldLocation().isInScene(client) || isOutsideRenderDistance(gameObject.getLocalLocation()))
            {
                continue;
            }

            final DynamicObject dynamicObject = (DynamicObject) gameObject.getRenderable();

            if (((dynamicObject.getAnimation() == null ? -1 : dynamicObject.getAnimation().getId()) == CROSSBOW_STATUE_ANIM_DEFAULT) ||
                    ((dynamicObject.getAnimation() == null ? -1 : dynamicObject.getAnimation().getId()) == CROSSBOW_STATUE_ANIM_FINAL))
            {
                continue;
            }

            final Shape shape = gameObject.getConvexHull();

            if (shape != null)
            {
                drawStrokeAndFill(graphics2D, Color.RED, new Color(255, 0, 0, 20),
                        1.0f, shape);
            }
        }
    }

    private static final int SWORD_THROW_START = 8667;
    private void render_sword_statues(final Graphics2D graphics2D)
    {
        if (plugin.getSwordStatues().isEmpty())
        {
            return;
        }

        for (final GameObject gameObject : plugin.getSwordStatues())
        {
            if (!gameObject.getWorldLocation().isInScene(client) || isOutsideRenderDistance(gameObject.getLocalLocation()))
            {
                continue;
            }

            final DynamicObject dynamicObject = (DynamicObject) gameObject.getRenderable();

            if ((dynamicObject.getAnimation() == null ? -1 : dynamicObject.getAnimation().getId()) == SWORD_THROW_START)
            {
                final Shape shape = gameObject.getConvexHull();

                if (shape != null)
                {
                    drawStrokeAndFill(graphics2D, Color.RED, new Color(255, 0, 0, 20),
                            1.0f, shape);
                }
            }
        }
    }

    private static void drawStrokeAndFill(final Graphics2D graphics2D, final Color outlineColor, final Color fillColor,
                                          final float strokeWidth, final Shape shape)
    {
        graphics2D.setColor(outlineColor);
        final Stroke originalStroke = graphics2D.getStroke();
        graphics2D.setStroke(new BasicStroke(strokeWidth));
        graphics2D.draw(shape);
        graphics2D.setColor(fillColor);
        graphics2D.fill(shape);
        graphics2D.setStroke(originalStroke);
    }

    public void render_statues(Graphics2D graphics)
    {
        //YES IM SHIT AT CODE SORRYYYYYY
        for(HallowedSepulchreWizardStatue statue : plugin.getWizardStatues()) {
            GameObject gameObject = statue.getGameObject();
            if(gameObject == null)
            {
                if(config.ShowDebugInfo())
                    log.info("Wtf GameOBJECT is NULL");
                return;
            }
            if (!gameObject.getWorldLocation().isInScene(client) || isOutsideRenderDistance(gameObject.getLocalLocation()))
            {
                continue;
            }

            int ticks = statue.getTicksUntilNextAnimation();
            int maxTick = statue.maxTickperfloor(plugin.currentfloor, plugin.subfloor);
            if(maxTick == -1)
            {
                continue;
            }
            if(ticks > -20) {//7?
                //OverlayUtil.renderTileOverlay(graphics, statue.getGameObject(), "S: " + statue.getAnimationTickSpeed() + " T: " + statue.getTicksUntilNextAnimation() + " D:" + gameObject.getRsOrientation() + " Anim:" + statue.getAnimation() + " Or: " + or, Color.YELLOW);
                Color color = Color.ORANGE;
                if(ticks > 1)
                {
                    if(!config.ShowUnsafeTiles())
                    {
                        continue;
                    }
                    color = config.UnsafeTileColor();
                }
                else {
                    if (ticks == 1 || ticks == 0) {
                        if(!config.ShowSafeTiles())
                        {
                            continue;
                        }
                        color = config.SafeTileColor();
                    } else if (ticks == -11) {
                        if(!config.ShowUnsafeTiles())
                        {
                            continue;
                        }
                        color = config.UnsafeTileColor();
                    }


                    if (maxTick != -1) {
                        int diffrence = Math.abs(maxTick - ticks);
                        if (diffrence < 1) {
                            if(!config.ShowUnsafeTiles())
                            {
                                continue;
                            }
                            color = config.UnsafeTileColor();
                        }
                        else if (diffrence == 1) {
                            if(!config.ShowRiskyTiles())
                            {
                                continue;
                            }
                            color = config.RiskyTileColor();
                        }
                        else if (diffrence == 2) {
                            if(!config.ShowRiskyTiles())
                            {
                                continue;
                            }
                            color = config.RiskyTileColor();
                        }
                        else if (diffrence > 2) {
                            if(!config.ShowSafeTiles())
                            {
                                continue;
                            }
                            color = config.SafeTileColor();
                        }
                    }
                }

                String overlaytext = "";
                int diffrenceinticks = Math.abs(ticks - maxTick);
                int ticksuntillunsafe = -1;
                if(config.ShowValues()) {
                    OverlayUtil.renderTileOverlay(graphics, statue.getGameObject(), "" + statue.getTicksUntilNextAnimation() + "/" + maxTick, color);
                }
                else
                {
                    if(ticks > 0) {
                        if(config.ShowFireTickCounter()) {
                            overlaytext = "" + ticks;
                        }
                    }
                    else
                    {
                        ticksuntillunsafe = Math.abs(ticks - maxTick);
                        if(config.ShowReversedFireTickCounter()) {
                            overlaytext = "(" + ticksuntillunsafe + ")";
                        }
                    }
                }
                renderLine(gameObject, graphics, color, diffrenceinticks, ticksuntillunsafe, overlaytext);
            }
        }
    }

    public boolean checkarrow_in_line(WorldPoint point, boolean x)
    {
        for(NPC arrow : plugin.getArrows()) {
            if (arrow.getWorldLocation().getPlane() != client.getPlane()) {
                continue;
            }
            if (isOutsideRenderDistance(arrow.getLocalLocation())) {
                continue;
            }
            if(x)
            {
                if(arrow.getWorldLocation().getX() == point.getX())
                {
                    if(Math.abs(arrow.getWorldLocation().getY() - point.getY()) < 5) {
                        return true;
                    }
                }
            }
            else
            {
                if(arrow.getWorldLocation().getY() == point.getY())
                {
                    if(Math.abs(arrow.getWorldLocation().getX() - point.getX()) < 5) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void render_lightning(Graphics2D graphics)
    {
        for(LocalPoint l : plugin.getLightningboltlocations())
        {
            if(isOutsideRenderDistance(l))
            {
                continue;
            }

            WorldPoint w = WorldPoint.fromLocal(client, l);
            if(w.getPlane() != client.getPlane())
            {
                continue;
            }
            //OverlayUtil.renderTileOverlay(client, graphics, l, Lightning, Color.YELLOW);
            Color c = Color.GREEN;
            int countdown = Math.abs(plugin.getTickssincelightning() - 5);
            if(countdown == 2)
            {
                c = Color.ORANGE;
            }
            else if(countdown == 1)
            {
                c = Color.RED;
            }
            else if(countdown == 0)
            {
                c = Color.GREEN;
            }

            if(config.ShowLightningTiles()) {
                render_object_server_tile(graphics, WorldPoint.fromLocal(client, l), c, 0, 0);
            }
            if(config.ShowLightningCountdown())
            {
                OverlayText(graphics, l, "" + countdown, c, 0, 0);
            }
            //OverlayUtil.renderTileOverlay(graphics);
        }
    }
    
    public void render_teleport_portals(Graphics2D graphics, Map<LocalPoint, HallowedSepulchreTeleportPortal> portalMap, Color color)
    {
        for (Map.Entry<LocalPoint, HallowedSepulchreTeleportPortal> portalEntry : portalMap.entrySet())
        {
            LocalPoint l = portalEntry.getKey();
            if (isOutsideRenderDistance(l))
            {
                continue;
            }
            
            WorldPoint w = WorldPoint.fromLocal(client, l);
            if(w.getPlane() != client.getPlane())
            {
                continue;
            }
            int countdown = portalEntry.getValue().getTicksUntilDespawn() + 1;
            
            if (config.showTeleporterTiles())
            {
                render_object_server_tile(graphics, w, color, 0, 0);
            }
            if (config.showTeleporterTimer())
            {
                OverlayText(graphics, l, "" + countdown, color, 0, 0);
            }
        }
    }

    public void renderLine(GameObject gameObject, Graphics2D graphics, Color color, int diffrenceinticks, int ticksuntillunsafe, String overlaytext)
    {
        int or = (gameObject.getOrientation() / 512);
        WorldPoint point = gameObject.getWorldLocation();
        LocalPoint lp = LocalPoint.fromWorld(client, point);


        if(or == 1 || or == 3)
        {


            int base = 1;
            int offset = 0;
            int offset2 = -1;
            if(or == 1) {
                base = -1;
                offset = -1;
                offset2 = 0;
            }

            offset++;
            offset2++;

            if(!overlaytext.equals("")) {
                if (plugin.currentfloor != 4 || !plugin.isFloor4ComplicatedWizardStatues)
                {
                    OverlayText(graphics, lp, overlaytext, color, offset, offset2);
                }
            }



            //int distance = gameObject.getWorldLocation().distanceTo2D(client.getLocalPlayer().getWorldLocation());
            LocalPoint pointoffset1 = localpointoffset(lp, (base * 1) + offset, offset2);
            LocalPoint pointoffset2 = localpointoffset(lp, (base * 2) + offset, offset2);
            LocalPoint pointoffset3 = localpointoffset(lp, (base * 3) + offset, offset2);
            Color color1 = color;
            Color color2 = color;
            Color color3 = color;
            if(config.ShowArrowDanger()) {
                if (plugin.getCurrentfloor() == 3) {
                    if (color != config.UnsafeTileColor()) {
                        if (checkarrow_in_line(WorldPoint.fromLocal(client, pointoffset1), true)) {
                            color1 = config.ShowArrowDangerColor();
                        }
                        if (checkarrow_in_line(WorldPoint.fromLocal(client, pointoffset2), true)) {
                            color2 = config.ShowArrowDangerColor();
                        }
                        if (checkarrow_in_line(WorldPoint.fromLocal(client, pointoffset3), true)) {
                            color3 = config.ShowArrowDangerColor();
                        }
                    }
                }
            }

            if(config.ShowFireUnreachable()) {
                ticksuntillunsafe = ticksuntillunsafe + 2;
                if (ticksuntillunsafe != -1) {
                    if (color1 == config.SafeTileColor() || color1 == config.RiskyTileColor()) {
                        int distance = WorldPoint.fromLocal(client, pointoffset1).distanceTo2D(client.getLocalPlayer().getWorldLocation());
                        if (distance > ticksuntillunsafe) {
                            color1 = Color.GRAY;
                        }
                        int distance2 = WorldPoint.fromLocal(client, pointoffset2).distanceTo2D(client.getLocalPlayer().getWorldLocation());
                        if (distance2 > ticksuntillunsafe) {
                            color2 = Color.GRAY;
                        }

                        int distance3 = WorldPoint.fromLocal(client, pointoffset3).distanceTo2D(client.getLocalPlayer().getWorldLocation());
                        if (distance3 > ticksuntillunsafe) {
                            color3 = Color.GRAY;
                        }
                    }
                }
            }

            if(config.ShowValues()) {
                int distance = WorldPoint.fromLocal(client, pointoffset1).distanceTo2D(client.getLocalPlayer().getWorldLocation());
                OverlayText(graphics, lp, "" + distance + " (" + (Math.abs(distance - diffrenceinticks)) + ")", Color.ORANGE, (base * 1) + offset, offset2);
                int distance2 = WorldPoint.fromLocal(client, pointoffset2).distanceTo2D(client.getLocalPlayer().getWorldLocation());
                OverlayText(graphics, lp, "" + distance2 + " (" + (Math.abs(distance - diffrenceinticks)) + ")", Color.ORANGE, (base * 2) + offset, offset2);
                int distance3 = WorldPoint.fromLocal(client, pointoffset3).distanceTo2D(client.getLocalPlayer().getWorldLocation());
                OverlayText(graphics, lp, "" + distance3 + " (" + (Math.abs(distance - diffrenceinticks)) + ")", Color.ORANGE, (base * 3) + offset, offset2);
            }
            if ((plugin.currentfloor == 4 && plugin.gameticksOnFloor4Plane2SouthSide < 20) || plugin.currentfloor == 5)
            {
                return;
            }
            Polygon pl  = TileOffsetter(lp, (base * 1) + offset, offset2);
            if(pl == null)
            {
                return;
            }
            renderFireTile(graphics, pl, color1);
            pl  = TileOffsetter(lp, (base * 2) + offset, offset2);
            if(pl == null)
            {
                return;
            }
            renderFireTile(graphics, pl, color2);
            pl  = TileOffsetter(lp, (base * 3) + offset, offset2);
            if(pl == null)
            {
                return;
            }
            renderFireTile(graphics, pl, color3);
        }
        else//0&2
        {
            int base = 1;
            int offset = 0;
            int offset2 = 0;
            if(or == 0) {
                base = -1;
                offset = -1;
                offset2 = -1;
            }

            offset++;
            offset2++;

            if(!overlaytext.equals("")) {
                if (plugin.currentfloor != 4 || !plugin.isFloor4ComplicatedWizardStatues)
                {
                    OverlayText(graphics, lp, overlaytext, color, offset, offset2);
                }
            }

            LocalPoint pointoffset1 = localpointoffset(lp, offset2, (base * 1) + offset);
            LocalPoint pointoffset2 = localpointoffset(lp, offset2, (base * 2) + offset);
            LocalPoint pointoffset3 = localpointoffset(lp, offset2, (base * 3) + offset2);
            Color color1 = color;
            Color color2 = color;
            Color color3 = color;
            if(config.ShowArrowDanger()) {
                if (plugin.getCurrentfloor() > 2) {
                    if (color != config.UnsafeTileColor()) {
                        if (checkarrow_in_line(WorldPoint.fromLocal(client, pointoffset1), false)) {
                            color1 = config.ShowArrowDangerColor();
                        }
                        if (checkarrow_in_line(WorldPoint.fromLocal(client, pointoffset2), false)) {
                            color2 = config.ShowArrowDangerColor();
                        }
                        if (checkarrow_in_line(WorldPoint.fromLocal(client, pointoffset3), false)) {
                            color3 = config.ShowArrowDangerColor();
                        }
                    }
                }
            }

            if(config.ShowValues()) {
                int distance = WorldPoint.fromLocal(client, pointoffset1).distanceTo2D(client.getLocalPlayer().getWorldLocation());
                OverlayText(graphics, lp, "" + distance + " (" + (Math.abs(distance - diffrenceinticks)) + ")", Color.ORANGE, offset2, (base * 1) + offset);
                int distance2 = WorldPoint.fromLocal(client, pointoffset2).distanceTo2D(client.getLocalPlayer().getWorldLocation());
                OverlayText(graphics, lp, "" + distance2 + " (" + (Math.abs(distance - diffrenceinticks)) + ")", Color.ORANGE, offset2, (base * 2) + offset);
                int distance3 = WorldPoint.fromLocal(client, pointoffset3).distanceTo2D(client.getLocalPlayer().getWorldLocation());
                OverlayText(graphics, lp, "" + distance3 + " (" + (Math.abs(distance - diffrenceinticks)) + ")", Color.ORANGE, offset2, (base * 3) + offset);
            }
            if ((plugin.currentfloor == 4 && plugin.gameticksOnFloor4Plane2SouthSide < 20) || plugin.currentfloor == 5)
            {
                return;
            }
            Polygon pl  = TileOffsetter(lp, offset2, (base * 1) + offset);
            if(pl == null)
            {
                return;
            }
            renderFireTile(graphics, pl, color1);
            pl  = TileOffsetter(lp, offset2, (base * 2) + offset);
            if(pl == null)
            {
                return;
            }
            renderFireTile(graphics, pl, color2);
            pl  = TileOffsetter(lp, offset2, (base * 3) + offset);
            if(pl == null)
            {
                return;
            }
            renderFireTile(graphics, pl, color3);
        }
    }

    public void renderFireTile(Graphics2D graphics, Polygon poly, Color color)
    {
        if(config.FillTileOverlay())
        {
            OverlayUtil.renderPolygon(graphics, poly, color);
        }
        else
        {
            OverlayUtil.renderPolygon(graphics, poly, color);
        }
    }

    private boolean isOutsideRenderDistance(final LocalPoint localPoint)
    {
        final int maxDistance = config.renderDistance().getDistance();

        if (maxDistance == 0)
        {
            return false;
        }

        return localPoint.distanceTo(player.getLocalLocation()) >= maxDistance;
    }

    private boolean isOutsideRenderDistance(final int x, final int y)
    {
        final int maxDistance = config.renderDistance().getDistance();

        if (maxDistance == 0)
        {
            return false;
        }

        LocalPoint playerpoint = player.getLocalLocation();
        return (int)Math.hypot((double)(x - playerpoint.getX()), (double)(y - playerpoint.getY())) >= maxDistance;
    }
}