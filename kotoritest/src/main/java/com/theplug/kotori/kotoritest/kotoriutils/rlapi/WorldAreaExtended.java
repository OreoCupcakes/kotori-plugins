package com.theplug.kotori.kotoritest.kotoriutils.rlapi;

import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

import java.util.function.Predicate;

public class WorldAreaExtended
{
	public static Point getComparisonPoint(WorldArea area1, WorldArea area2)
	{
		int x, y;
		if (area2.getX() <= area1.getX())
		{
			x = area1.getX();
		}
		else if (area2.getX() >= area1.getX() + area1.getWidth() - 1)
		{
			x = area1.getX() + area1.getWidth() - 1;
		}
		else
		{
			x = area2.getX();
		}
		if (area2.getY() <= area1.getY())
		{
			y = area1.getY();
		}
		else if (area2.getY() >= area1.getY() + area1.getHeight() - 1)
		{
			y = area1.getY() + area1.getHeight() - 1;
		}
		else
		{
			y = area2.getY();
		}
		return new Point(x, y);
	}
	
	public static Point getAxisDistances(WorldArea area1, WorldArea area2)
	{
		Point p1 = getComparisonPoint(area1, area2);
		Point p2 = getComparisonPoint(area2, area1);
		return new Point(Math.abs(p1.getX() - p2.getX()), Math.abs(p1.getY() - p2.getY()));
	}
	
	public static WorldArea calculateNextTravellingPoint(Client client, WorldArea original, WorldArea target, boolean stopAtMeleeDistance,
												   Predicate<? super WorldPoint> extraCondition)
	{
		if (original.getPlane() != target.getPlane())
		{
			return null;
		}
		
		if (original.intersectsWith(target))
		{
			if (stopAtMeleeDistance)
			{
				// Movement is unpredictable when the NPC and actor stand on top of each other
				return null;
			}
			else
			{
				return original;
			}
		}
		
		int dx = target.getX() - original.getX();
		int dy = target.getY() - original.getY();
		Point axisDistances = getAxisDistances(original, target);
		if (stopAtMeleeDistance && axisDistances.getX() + axisDistances.getY() == 1)
		{
			// NPC is in melee distance of target, so no movement is done
			return original;
		}
		
		LocalPoint lp = LocalPoint.fromWorld(client, original.getX(), original.getY());
		if (lp == null || lp.getSceneX() + dx < 0 || lp.getSceneX() + dy >= Constants.SCENE_SIZE
				|| lp.getSceneY() + dx < 0 || lp.getSceneY() + dy >= Constants.SCENE_SIZE)
		{
			// NPC is travelling out of the scene, so collision data isn't available
			return null;
		}
		
		int dxSig = Integer.signum(dx);
		int dySig = Integer.signum(dy);
		if (stopAtMeleeDistance && axisDistances.getX() == 1 && axisDistances.getY() == 1)
		{
			// When it needs to stop at melee distance, it will only attempt
			// to travel along the x axis when it is standing diagonally
			// from the target
			if (original.canTravelInDirection(client, dxSig, 0, extraCondition))
			{
				return new WorldArea(original.getX() + dxSig, original.getY(), original.getWidth(), original.getHeight(), original.getPlane());
			}
		}
		else
		{
			if (original.canTravelInDirection(client, dxSig, dySig, extraCondition))
			{
				return new WorldArea(original.getX() + dxSig, original.getY() + dySig, original.getWidth(), original.getHeight(), original.getPlane());
			}
			else if (dx != 0 && original.canTravelInDirection(client, dxSig, 0, extraCondition))
			{
				return new WorldArea(original.getX() + dxSig, original.getY(), original.getWidth(), original.getHeight(), original.getPlane());
			}
			else if (dy != 0 && Math.max(Math.abs(dx), Math.abs(dy)) > 1 && original.canTravelInDirection(client, 0, dy, extraCondition))
			{
				// Note that NPCs don't attempts to travel along the y-axis
				// if the target is <= 1 tile distance away
				return new WorldArea(original.getX(), original.getY() + dySig, original.getWidth(), original.getHeight(), original.getPlane());
			}
		}
		
		// The NPC is stuck
		return original;
	}
	
	public static WorldArea calculateNextTravellingPoint(Client client, WorldArea original, WorldArea target, boolean stopAtMeleeDistance)
	{
		return calculateNextTravellingPoint(client, original, target, stopAtMeleeDistance, x -> true);
	}
}
