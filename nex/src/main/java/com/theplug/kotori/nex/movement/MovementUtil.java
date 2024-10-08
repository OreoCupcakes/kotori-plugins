package com.theplug.kotori.nex.movement;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.runelite.api.Client;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

public class MovementUtil
{
	public static boolean isWalkable(Client client, LocalPoint tile)
	{
		WorldView wv = client.getTopLevelWorldView();
		if (wv == null)
		{
			return false;
		}

		if (wv.getCollisionMaps() != null)
		{
			int[][] flags = wv.getCollisionMaps()[wv.getPlane()].getFlags();
			int data = flags[tile.getSceneX()][tile.getSceneY()];

			Set<MovementFlag> movementFlags = MovementFlag.getSetFlags(data);

			return Collections.disjoint(movementFlags, MovementFlag.getNotWalkable());
		}

		return true;
	}

	public static Set<WorldPoint> getRadiusTiles(WorldPoint center, int radius)
	{
		// formula for circumference in tiles is 4(2r - 1) + 4
		//										 8r
		Set<WorldPoint> tiles = new HashSet<>(radius * 8);

		for (int x = -radius; x <= radius; x++)
		{
			tiles.add(center.dx(x).dy(radius));
			tiles.add(center.dx(x).dy(-radius));
			tiles.add(center.dx(radius).dy(x));
			tiles.add(center.dx(-radius).dy(x));
		}

		return tiles;
	}

	public static List<LocalPoint> getWalkableLocalTiles(Client client, WorldPoint center, int radius)
	{
		WorldView wv = client.getTopLevelWorldView();
		if (wv == null)
		{
			return Collections.singletonList(client.getLocalPlayer().getLocalLocation());
		}
		return MovementUtil.getRadiusTiles(center, radius)
			.stream()
			.map(tile -> LocalPoint.fromWorld(wv, tile))
			.filter(tile -> MovementUtil.isWalkable(client, tile))
			.collect(Collectors.toList());
	}

	public static List<WorldPoint> getWalkableWorldTiles(Client client, WorldPoint center, int radius)
	{
		WorldView wv = client.getTopLevelWorldView();
		if (wv == null)
		{
			return Collections.singletonList(client.getLocalPlayer().getWorldLocation());
		}
		return MovementUtil.getRadiusTiles(center, radius)
			.stream()
			.filter(tile -> MovementUtil.isWalkable(client, LocalPoint.fromWorld(wv, tile)))
			.collect(Collectors.toList());
	}
}
