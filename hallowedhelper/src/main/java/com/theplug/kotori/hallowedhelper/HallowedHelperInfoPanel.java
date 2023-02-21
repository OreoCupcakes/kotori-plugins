package com.theplug.kotori.hallowedhelper;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GroundObject;
import net.runelite.api.Skill;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import java.awt.*;

@Slf4j
public class HallowedHelperInfoPanel extends OverlayPanel
{
        private final Client client;
        private final HallowedHelperConfig config;
        private final HallowedHelperPlugin plugin;

        @Inject
        HallowedHelperInfoPanel(Client client, final HallowedHelperConfig config, HallowedHelperPlugin plugin)
        {
            this.client = client;
            this.plugin = plugin;
            this.config = config;
            setPosition(OverlayPosition.TOP_LEFT);
        }

        private int maxfloor = 1;

        @Override
        public Dimension render(Graphics2D graphics)
        {
            /*
            Player local = client.getLocalPlayer();
            WorldPoint WorldPoint = local.getWorldLocation();
            LocalPoint LocalLocation = local.getLocalLocation();
            maxfloor = getMaxFloor();

            panelComponent.getChildren().add(LineComponent.builder()
                .left("Hallowed-Info")
                .right("")
                .build());

            if(config.ShowValues()) {
                panelComponent.getChildren().add(LineComponent.builder()
                    .left("FLOOR4_ROTATION")
                    .right("" + plugin.floor4_fire_rotation)
                    .build());

                panelComponent.getChildren().add(LineComponent.builder()
                    .left("TICKS_SINCE:")
                    .right("" + plugin.floor_4_ticks_since_statue)
                    .build());

                panelComponent.getChildren().add(LineComponent.builder()
                    .left("FLOOR5_ROTATION")
                    .right("" + plugin.floor5_fire_rotation)
                    .build());

                panelComponent.getChildren().add(LineComponent.builder()
                    .left("TICKS5_SINCE:")
                    .right("" + plugin.floor_5_ticks_since_statue)
                    .build());

                panelComponent.getChildren().add(LineComponent.builder()
                    .left("FLOOR5[2]_ROTATION")
                    .right("" + plugin.floor5_2A_fire_rotation)
                    .build());

                panelComponent.getChildren().add(LineComponent.builder()
                    .left("TICKS5[2]_SINCE:")
                    .right("" + plugin.floor5_2A_ticks_since_statue)
                    .build());

            }
                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("Location: ")
                        .right("" + getLocation())
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("Region ID: ")
                        .right("" + client.getLocalPlayer())
                        .build());

//its shit /\
                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("TicksLeft: ")
                        .right("" + (plugin.isDoorOpen()))
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("DoorOpen: ")
                        .right("" + (plugin.isDoorOpen()))
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("Level: ")
                        .right("" + client.getRealSkillLevel(Skill.AGILITY))
                        .build());

            if(config.ShowValues()) {
                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("Plane: ")
                        .right("" + client.getPlane())
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("WorldX: ")
                        .right("" + WorldPoint.getX())
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("WorldX ")
                        .right("")
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("WorldY ")
                        .right("")
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("WorldY: ")
                        .right("" + WorldPoint.getX())
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("LocalX: ")
                        .right("" + LocalLocation.getX())
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("LocalY: ")
                        .right("" + LocalLocation.getY())
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("SceneX: ")
                        .right("" + LocalLocation.getSceneX())
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("SceneY: ")
                        .right("" + LocalLocation.getSceneY())
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("LH: ")
                        .right("" + client.getLocalPlayer())
                        .build());

                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("Region")
                        .right("" + client.getMapRegions())
                        .build());

                Integer swordcount = plugin.getSwords().size();
                Integer arrowcount = plugin.getArrows().size();
                Integer chestcount = plugin.getChests().size();
                panelComponent.getChildren().add(LineComponent.builder()
                    .left("Swords")
                    .right("" + swordcount.toString())
                    .build());

                panelComponent.getChildren().add(LineComponent.builder()
                    .left("Swordsman")
                    .right("" + plugin.getSwordStatues())
                    .build());

                panelComponent.getChildren().add(LineComponent.builder()
                    .left("Arrows")
                    .right("" + arrowcount.toString())
                    .build());

                panelComponent.getChildren().add(LineComponent.builder()
                    .left("Floor-Stairs")
                    .right("" + plugin.getFloor_gates())
                    .build());

                panelComponent.getChildren().add(LineComponent.builder()
                    .left("Stairs")
                    .right("" + plugin.getStairs())
                    .build());

                panelComponent.getChildren().add(LineComponent.builder()
                    .left("Chests")
                    .right("" + chestcount)
                    .build());

                panelComponent.getChildren().add(LineComponent.builder()
                    .left("Lightningbolts: ")
                    .right(""  + plugin.getLightningboltlocations())
                    .build());

            }

            */


            //client.getVarbitValue(24717); Hallowed Floor? -42 : 10 = FLOOR?
            //Varbit 10392 is de Afteller tijd

            /*
            for(GameObject chest : plugin.getChests())
            {
                ObjectComposition definition = client.getObjectComposition(chest.getId());
                if (definition != null) {
                    if (definition.getImpostorIds() != null) {
                        definition = definition.getImpostor();
                    }
                    int varbit = definition.getId();
                    panelComponent.getChildren().add(LineComponent.builder()
                        .left("Chest ")
                        .right("" + chest.getId())
                        .build());

                }
            }*/


            return super.render(graphics);
        }

        public String getLocation()
        {
            switch(plugin.getCurrentfloor())
            {
                case -1:
                    return "Lobby";
                case 1:
                    return "Floor " + ColorUtil.prependColorTag("1" + getSubFloor(), Color.GREEN) + ColorUtil.prependColorTag("/" + maxfloor, Color.WHITE);
                case 2:
                    return "Floor " + ColorUtil.prependColorTag("2", Color.GREEN) + ColorUtil.prependColorTag("/" + maxfloor, Color.WHITE);
                case 3:
                    return "Floor\r\n" + ColorUtil.prependColorTag("3", Color.GREEN) + ColorUtil.prependColorTag("/" + maxfloor, Color.WHITE) + "\r\n" + ThirdrdFloorLocation();
                case 4:
                    return "Floor\r\n" + ColorUtil.prependColorTag("4", Color.GREEN) + ColorUtil.prependColorTag("/" + maxfloor, Color.WHITE) + "\r\n" + FourthFloorLocation();
                case 5:
                    return "Floor\r\n" + ColorUtil.prependColorTag("5", Color.GREEN) + ColorUtil.prependColorTag("/" + maxfloor, Color.WHITE);
                default:
                    return "?";
            }
        }

    private String ThirdrdFloorLocation()
    {
        if(plugin.getSubfloor() == 0)
        {
            return "Left";
        }
        else if(plugin.getSubfloor() == 1)
        {
            return "Mid";
        }
        else
        {
            return "Right";
        }
    }

    private String FourthFloorLocation()
    {
        if(plugin.getSubfloor() == 0)
        {
            return "Up";
        }
        else if(plugin.getSubfloor() == 1)
        {
            return "Mid";
        }
        else
        {
            return "Down";
        }
    }

    public String getSubFloor()
    {
        if(plugin.getBridges().size() > 0)
        {
            GroundObject bridge = plugin.getBridges().iterator().next();
            int xdistance = (bridge.getX() - client.getLocalPlayer().getLocalLocation().getX());
            if(xdistance > -1408) {
                return "C";
            }
        }
        switch(client.getMapRegions()[0])
        {
            case 8796:
                return "A";
            case 8797:
                return "B";
            case 9052:
                return "C";
        }
        return "";
    }

        public int getMaxFloor()
        {
            int lv = client.getRealSkillLevel(Skill.AGILITY);
            if(lv > 91)
            {
                return 5;
            }
            else if(lv > 81)
            {
                return 4;
            }
            else if(lv > 71)
            {
                return 3;
            }
            else if(lv > 61)
            {
                return 2;
            }
            else if(lv > 51)
            {
                return 1;
            }
            return 0;
        }
    }
