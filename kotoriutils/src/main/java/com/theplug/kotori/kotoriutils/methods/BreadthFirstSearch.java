package com.theplug.kotori.kotoriutils.methods;

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;

import java.util.*;

public class BreadthFirstSearch
{
    public static WorldPoint dodgeAoeAttack(Client client, Set<WorldPoint> dangerousTiles, NPC npc, boolean usingMelee, boolean preferMeleeDistance, boolean meleeDistanceCondition)
    {
        //Breadth First Search of the scene's tiles
        //Get the tiles and its collision flags in the current plane
        Tile[][] tiles = client.getScene().getTiles()[client.getPlane()];
        CollisionData[] collisionData = client.getCollisionMaps();
        if (collisionData == null)
        {
            return null;
        }
        int[][] flags = collisionData[client.getPlane()].getFlags();

        //Use the player's current location as the first point of the search
        WorldPoint playerWorldLoc = client.getLocalPlayer().getWorldLocation();
        Tile firstPoint = tiles[playerWorldLoc.getX() - client.getBaseX()][playerWorldLoc.getY() - client.getBaseY()];

        //Initialize the queue and visited array
        HashSet<Tile> visitedTiles = new HashSet<>();
        Queue<Tile> queue = new LinkedList<>();

        //Push the first node into the queue and mark it visited
        queue.add(firstPoint);
        visitedTiles.add(firstPoint);

        while (!queue.isEmpty())
        {
            //Remove the node from front of queue and save it
            Tile tile = queue.poll();
            //Used to traverse the scene's 2d array
            int x = tile.getSceneLocation().getX();
            int y = tile.getSceneLocation().getY();

            /*
                Check for the following information on the polled tile:
                - If tile is at the edge of the scene
                - Get the collision data of the polled tile and see if you can move south/north/west/east from the polled tile
                - Check if the neighbors of the polled tile can actually be moved to, i.e. game object blocking your way
                - Check if the neighbors were already visited
             */
            if (y < 127 && canMoveNorth(flags[x][y]) && canMoveTo(flags[x][y + 1]) && !visitedTiles.contains(tiles[x][y + 1]))
            {
                Tile northNeighbor = tiles[x][y + 1];
                queue.add(northNeighbor);
                visitedTiles.add(northNeighbor);

                //Check if the north neighbor tile is safe from the poison projectiles splash damage and vents
                if (isTileSafe(northNeighbor, dangerousTiles))
                {
                    /*
                        Check if the player is using a melee combat style, prioritize a melee distance tile, and there aren't any existing flame special objects in the scene
                        Reason to check for flame objects is the edge case of transitioning into enrage phase after the flame special creates the flame walls.
                        Flame are walkable and have no collision data. The algorithm would try clicking a melee distance tile on the far side of Hydra and walk into the flame walls.
                     */
                    if (usingMelee && preferMeleeDistance && meleeDistanceCondition)
                    {
                        if (npc.getWorldArea().isInMeleeDistance(northNeighbor.getWorldLocation()))
                        {
                            return northNeighbor.getWorldLocation();
                        }
                    }
                    else
                    {
                        return northNeighbor.getWorldLocation();
                    }
                }
            }

            if (x < 127 && canMoveEast(flags[x][y]) && canMoveTo(flags[x + 1][y]) && !visitedTiles.contains(tiles[x + 1][y]))
            {
                Tile eastNeighbor = tiles[x + 1][y];
                queue.add(eastNeighbor);
                visitedTiles.add(eastNeighbor);

                //Check if the east neighbor tile is safe from the poison projectiles splash damage and vents
                if (isTileSafe(eastNeighbor, dangerousTiles))
                {
                    /*
                        Check if the player is using a melee combat style, prioritize a melee distance tile, and there aren't any existing flame special objects in the scene
                        Reason to check for flame objects is the edge case of transitioning into enrage phase after the flame special creates the flame walls.
                        Flame are walkable and have no collision data. The algorithm would try clicking a melee distance tile on the far side of Hydra and walk into the flame walls.
                     */
                    if (usingMelee && preferMeleeDistance && meleeDistanceCondition)
                    {
                        if (npc.getWorldArea().isInMeleeDistance(eastNeighbor.getWorldLocation()))
                        {
                            return eastNeighbor.getWorldLocation();
                        }
                    }
                    else
                    {
                        return eastNeighbor.getWorldLocation();
                    }
                }
            }

            if (y > 0 && canMoveSouth(flags[x][y]) && canMoveTo(flags[x][y - 1]) && !visitedTiles.contains(tiles[x][y - 1]))
            {
                Tile southNeighbor = tiles[x][y - 1];
                queue.add(southNeighbor);
                visitedTiles.add(southNeighbor);

                //Check if the south neighbor tile is safe from the poison projectiles splash damage and vents
                if (isTileSafe(southNeighbor, dangerousTiles))
                {
                    /*
                        Check if the player is using a melee combat style, prioritize a melee distance tile, and there aren't any existing flame special objects in the scene
                        Reason to check for flame objects is the edge case of transitioning into enrage phase after the flame special creates the flame walls.
                        Flame are walkable and have no collision data. The algorithm would try clicking a melee distance tile on the far side of Hydra and walk into the flame walls.
                     */
                    if (usingMelee && preferMeleeDistance && meleeDistanceCondition)
                    {
                        if (npc.getWorldArea().isInMeleeDistance(southNeighbor.getWorldLocation()))
                        {
                            return southNeighbor.getWorldLocation();
                        }
                    }
                    else
                    {
                        return southNeighbor.getWorldLocation();
                    }
                }
            }

            if (x > 0 && canMoveWest(flags[x][y]) && canMoveTo(flags[x - 1][y]) && !visitedTiles.contains(tiles[x - 1][y]))
            {
                Tile westNeighbor = tiles[x - 1][y];
                queue.add(westNeighbor);
                visitedTiles.add(westNeighbor);

                //Check if the west neighbor tile is safe from the poison projectiles splash damage and vents
                if (isTileSafe(westNeighbor, dangerousTiles))
                {
                    /*
                        Check if the player is using a melee combat style, prioritize a melee distance tile, and there aren't any existing flame special objects in the scene
                        Reason to check for flame objects is the edge case of transitioning into enrage phase after the flame special creates the flame walls.
                        Flame are walkable and have no collision data. The algorithm would try clicking a melee distance tile on the far side of Hydra and walk into the flame walls.
                     */
                    if (usingMelee && preferMeleeDistance && meleeDistanceCondition)
                    {
                        if (npc.getWorldArea().isInMeleeDistance(westNeighbor.getWorldLocation()))
                        {
                            return westNeighbor.getWorldLocation();
                        }
                    }
                    else
                    {
                        return westNeighbor.getWorldLocation();
                    }
                }
            }
        }
        //No safe tiles found so return null
        return null;
    }

    public static WorldPoint dodgeAoeAttack(Client client, Set<WorldPoint> dangerousTiles, NPC npc)
    {
        return dodgeAoeAttack(client, dangerousTiles, npc, false, false, false);
    }

    private static boolean canMoveWest(int flag)
    {
        return (flag & CollisionDataFlag.BLOCK_MOVEMENT_WEST) != CollisionDataFlag.BLOCK_MOVEMENT_WEST;
    }

    private static boolean canMoveEast(int flag)
    {
        return (flag & CollisionDataFlag.BLOCK_MOVEMENT_EAST) != CollisionDataFlag.BLOCK_MOVEMENT_EAST;
    }

    private static boolean canMoveNorth(int flag)
    {
        return (flag & CollisionDataFlag.BLOCK_MOVEMENT_NORTH) != CollisionDataFlag.BLOCK_MOVEMENT_NORTH;
    }

    private static boolean canMoveSouth(int flag)
    {
        return (flag & CollisionDataFlag.BLOCK_MOVEMENT_SOUTH) != CollisionDataFlag.BLOCK_MOVEMENT_SOUTH;
    }

    private static boolean canMoveTo(int flag)
    {
        if ((flag & CollisionDataFlag.BLOCK_MOVEMENT_FULL) == CollisionDataFlag.BLOCK_MOVEMENT_FULL)
        {
            return false;
        }
        if ((flag & CollisionDataFlag.BLOCK_MOVEMENT_FLOOR) == CollisionDataFlag.BLOCK_MOVEMENT_FLOOR)
        {
            return false;
        }
        if ((flag & CollisionDataFlag.BLOCK_MOVEMENT_OBJECT) == CollisionDataFlag.BLOCK_MOVEMENT_OBJECT)
        {
            return false;
        }
        return (flag & CollisionDataFlag.BLOCK_MOVEMENT_FLOOR_DECORATION) != CollisionDataFlag.BLOCK_MOVEMENT_FLOOR_DECORATION;
    }

    private static boolean isTileSafe(Tile tile, Set<WorldPoint> dangerousTiles)
    {
        return !dangerousTiles.contains(tile.getWorldLocation());
    }
}
