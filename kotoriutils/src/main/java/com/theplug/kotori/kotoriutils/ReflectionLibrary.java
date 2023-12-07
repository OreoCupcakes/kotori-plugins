package com.theplug.kotori.kotoriutils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.RuneLite;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class ReflectionLibrary
{
	private final static Client client = RuneLite.getInjector().getInstance(Client.class);
	
	// Invoking Hooks
	@Setter
	private static String invokeMenuActionClassName;
	@Setter
	private static String invokeMenuActionMethodName;
	@Setter
	private static int invokeMenuActionJunkValue;
	
	// Walking Hooks
	@Setter
	private static String sceneSelectedXClassName;
	@Setter
	private static String sceneSelectedXFieldName;
	
	@Setter
	private static String sceneSelectedYClassName;
	@Setter
	private static String sceneSelectedYFieldName;

	@Setter
	private static String checkClickClassName;
	@Setter
	private static String checkClickFieldName;
	
	@Setter
	private static String viewportWalkingClassName;
	@Setter
	private static String viewportWalkingFieldName;
	
	//Spell Hooks
	@Setter
	private static String selectedSpellWidgetClassName;
	@Setter
	private static String selectedSpellWidgetFieldName;
	@Setter
	private static int selectedSpellWidgetMultiplier;
	
	@Setter
	private static String selectedSpellChildIndexClassName;
	@Setter
	private static String selectedSpellChildIndexFieldName;
	@Setter
	private static int selectedSpellChildIndexMultiplier;
	
	@Setter
	private static String selectedSpellItemIDClassName;
	@Setter
	private static String selectedSpellItemIDFieldName;
	@Setter
	private static int selectedSpellItemIDMultiplier;
	
	//Actor Hooks
	@Setter
	private static String actorAnimationIdClassName;
	@Setter
	private static String actorAnimationIdFieldName;
	@Setter
	private static int actorAnimationIdMultiplier;
	
	@Setter
	private static String npcOverheadIconClassName;
	@Setter
	private static String npcOverheadIconFieldName;
	
	@Setter
	private static String actorPathLengthClassName;
	@Setter
	private static String actorPathLengthFieldName;
	@Setter
	private static int actorPathLengthMultiplier;
	
	//Menu Entry Hooks
	@Setter
	private static String menuOptionsCountClassName;
	@Setter
	private static String menuOptionsCountFieldName;
	@Setter
	private static int menuOptionsCountMultiplier;
	
	@Setter
	private static String menuIdentifiersClassName;
	@Setter
	private static String menuIdentifiersFieldName;
	
	@Setter
	private static String menuItemIdsClassName;
	@Setter
	private static String menuItemIdsFieldName;
	
	@Setter
	private static String menuOptionsClassName;
	@Setter
	private static String menuOptionsFieldName;
	
	@Setter
	private static String menuParam0ClassName;
	@Setter
	private static String menuParam0FieldName;
	
	@Setter
	private static String menuParam1ClassName;
	@Setter
	private static String menuParam1FieldName;
	
	@Setter
	private static String menuTargetsClassName;
	@Setter
	private static String menuTargetsFieldName;
	
	@Setter
	private static String menuTypesClassName;
	@Setter
	private static String menuTypesFieldName;
	
	//Reflection methods
	private static Class<?> getClass(String className)
	{
		Class<?> clazz;
		
		try
		{
			clazz = client.getClass().getClassLoader().loadClass(className);
		}
		catch (Exception e)
		{
			log.error("Kotori Plugin Utils - Unable to load class \"" + className + "\". Check if obfuscated class name is correct.", e);
			return null;
		}
		
		return clazz;
	}
	
	private static Field getField(Class<?> clazz, String fieldName)
	{
		Field field;
		
		if (clazz == null)
		{
			return null;
		}
		
		try
		{
			field = clazz.getDeclaredField(fieldName);
		}
		catch (Exception e)
		{
			log.error("Kotori Plugin Utils - Unable to get declared field \"" + fieldName + "\". Check if obfuscated field name is correct.", e);
			return null;
		}
		
		return field;
	}
	
	private static Field getField(String className, String fieldName)
	{
		Class<?> clazz = getClass(className);
		return getField(clazz, fieldName);
	}
	
	private static int getFieldIntValue(Field field, Object objectWithField, int multiplier, String errorMsg)
	{
		if (field == null || objectWithField == null)
		{
			return -1;
		}
		
		try
		{
			field.setAccessible(true);
			int value = field.getInt(objectWithField) * multiplier;
			field.setAccessible(false);
			return value;
		}
		catch (Exception e)
		{
			log.error(errorMsg, e);
			return -1;
		}
	}
	
	private static void setFieldIntValue(Field field, Object objectWithField, int valueToSet, int multiplier, String errorMsg)
	{
		if (field == null || objectWithField == null)
		{
			return;
		}
		
		try
		{
			field.setAccessible(true);
			field.setInt(objectWithField, valueToSet * multiplier);
			field.setAccessible(false);
		}
		catch (Exception e)
		{
			log.error(errorMsg, e);
		}
	}
	
	private static void setFieldBooleanValue(Field field, Object objectWithField, boolean valueToSet, String errorMsg)
	{
		if (field == null)
		{
			return;
		}
		
		try
		{
			field.setAccessible(true);
			field.setBoolean(objectWithField, valueToSet);
			field.setAccessible(false);
		}
		catch (Exception e)
		{
			log.error(errorMsg, e);
		}
	}
	
	private static void setFieldIntArrayValue(Field field, Object objectWithField, int index, int valueToSet, String errorMsg)
	{
		if (field == null)
		{
			return;
		}
		
		try
		{
			field.setAccessible(true);
			Object fieldArray = field.get(objectWithField);
			Array.setInt(fieldArray, index, valueToSet);
			field.set(objectWithField, fieldArray);
			field.setAccessible(false);
		}
		catch (Exception e)
		{
			log.error(errorMsg, e);
		}
	}
	
	private static void setFieldObjectArrayValue(Field field, Object objectWithField, int index, Object valueToSet, String errorMsg)
	{
		if (field == null)
		{
			return;
		}
		
		try
		{
			field.setAccessible(true);
			Object optionsArray = field.get(objectWithField);
			Array.set(optionsArray, index, valueToSet);
			field.set(objectWithField, optionsArray);
			field.setAccessible(false);
		}
		catch (Exception e)
		{
			log.error(errorMsg, e);
		}
	}
	
	//Invoke Menu Action Method
	public static void invokeMenuAction(int param0, int param1, int opcode, int identifier, int itemId, String option, String target, int x, int y)
	{
		Class<?> clazz = getClass(invokeMenuActionClassName);
		Method method;
		boolean isJunkValueAByte = invokeMenuActionJunkValue < 128 && invokeMenuActionJunkValue >= -128;
		
		if (clazz == null)
		{
			return;
		}
		
		try
		{
			if (isJunkValueAByte)
			{
				method = clazz.getDeclaredMethod(invokeMenuActionMethodName, int.class, int.class, int.class, int.class, int.class, String.class, String.class,
						int.class, int.class, byte.class);
			}
			else
			{
				method = clazz.getDeclaredMethod(invokeMenuActionMethodName, int.class, int.class, int.class, int.class, int.class, String.class, String.class,
						int.class, int.class, int.class);
			}
		}
		catch (Exception e)
		{
			log.error("Kotori Plugin Utils - Unable to find invokeMenuAction method \"" + invokeMenuActionMethodName +
					"\". Check if obfuscated method name is correct.", e);
			return;
		}
		
		try
		{
			method.setAccessible(true);
			if (isJunkValueAByte)
			{
				method.invoke(null, param0, param1, opcode, identifier, itemId, option, target, x, y, (byte) invokeMenuActionJunkValue);
			}
			else
			{
				method.invoke(null, param0, param1, opcode, identifier, itemId, option, target, x, y, invokeMenuActionJunkValue);
			}
			method.setAccessible(false);
		}
		catch (Exception e)
		{
			log.error("Kotori Plugin Utils - Unable to invoke the method invokeMenuAction.", e);
		}
	}
	
	public static void invokeMenuAction(int param0, int param1, int opcode, int identifier, int itemId)
	{
		invokeMenuAction(param0, param1, opcode, identifier, itemId, "", "", 0, 0);
	}
	
	//Walking Methods
	private static void setXCoordinate(int x)
	{
		Field xField = getField(sceneSelectedXClassName, sceneSelectedXFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set scene selected X coordinate.";
		setFieldIntValue(xField, client.getScene(), x, 1, errorMsg);
	}
	
	private static void setYCoordinate(int y)
	{
		Field yField = getField(sceneSelectedYClassName, sceneSelectedYFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set scene selected Y coordinate.";
		setFieldIntValue(yField, client.getScene(), y, 1, errorMsg);
	}

	private static void setCheckClick()
	{
		Field checkClick = getField(checkClickClassName, checkClickFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set check click walking boolean.";
		setFieldBooleanValue(checkClick, client.getScene(), false, errorMsg);
	}
	
	private static void setViewportWalking()
	{
		Field viewport = getField(viewportWalkingClassName, viewportWalkingFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set scene viewport walking boolean.";
		setFieldBooleanValue(viewport, client.getScene(), true, errorMsg);
	}
	
	public static void sceneWalk(WorldPoint worldPoint, boolean convertForInstance)
	{
		if (worldPoint == null)
		{
			return;
		}

		if (convertForInstance)
		{
			Collection<WorldPoint> localWorldPoints = WorldPoint.toLocalInstance(client, worldPoint);
			if (localWorldPoints.size() != 1)
			{
				return;
			}

			for (WorldPoint localWorld : localWorldPoints)
			{
				sceneWalk(LocalPoint.fromWorld(client, localWorld));
				return;
			}
		}
		else
		{
			sceneWalk(LocalPoint.fromWorld(client, worldPoint));
		}
	}

	public static void sceneWalk(WorldPoint worldPoint)
	{
		sceneWalk(worldPoint, false);
	}

	public static void sceneWalk(int worldPointX, int worldPointY, int plane)
	{
		WorldPoint point = new WorldPoint(worldPointX, worldPointY, plane);
		sceneWalk(point);
	}

	public static void sceneWalk(LocalPoint localPoint)
	{
		if (localPoint == null || !localPoint.isInScene())
		{
			return;
		}

		sceneWalk(localPoint.getSceneX(), localPoint.getSceneY());
	}

	public static void sceneWalk(int sceneX, int sceneY)
	{
		setXCoordinate(sceneX);
		setYCoordinate(sceneY);
		setCheckClick();
		setViewportWalking();
	}
	
	//Spell Insertion Methods
	private static void setSelectedSpellWidget(int widgetPackedId)
	{
		Class<?> clazz = getClass(selectedSpellWidgetClassName);
		Field spellWidget = getField(clazz, selectedSpellWidgetFieldName);
		String errorMsg = "Kotori Plugin Utils - Unable to set selected spell widget.";
		setFieldIntValue(spellWidget, clazz, widgetPackedId, selectedSpellWidgetMultiplier, errorMsg);
	}

	/*
		SpellChildIndex is actually the Widget index in its parent's children array. This can be gotten from Widget.getIndex().
		This needs to be set to -1 when you are trying to cast an actual spell because interacting with other widgets could set this value to not -1.
		For example, interacting with an item in the inventory would set this value to the index in the inventory array.
	 */
	private static void setSelectedSpellChildIndex(int index)
	{
		Field spellChild = getField(selectedSpellChildIndexClassName, selectedSpellChildIndexFieldName);
		String errorMsg = "Kotori Plugin Utils - Unable to set selected spell child index.";
		setFieldIntValue(spellChild, client, index, selectedSpellChildIndexMultiplier, errorMsg);
	}

	/*
		SpellItemId is actually the item ID displayed by a widget. This can be gotten from Widget.getItemId().
		This needs to be set to -1 when you are trying to cast an actual spell because interacting with other widgets could set this value to not -1.
		For example, interacting with an item in the inventory would set this value to the item id of the item you interacted with.
	 */
	private static void setSelectedSpellItemId(int itemId)
	{
		Field spellItem = getField(selectedSpellItemIDClassName, selectedSpellItemIDFieldName);
		String errorMsg = "Kotori Plugin Utils - Unable to set selected spell item id.";
		setFieldIntValue(spellItem, client, itemId, selectedSpellItemIDMultiplier, errorMsg);
	}

	public static void setSelectedSpell(int spellWidgetId)
	{
		setSelectedSpell(spellWidgetId, -1, -1);
	}

	//As explained above, you need to set spellChildIndex and spellItemId to -1 if you want to cast a spell.
	public static void setSelectedSpell(int spellWidgetId, int spellChildIndex, int spellItemId)
	{
		setSelectedSpellWidget(spellWidgetId);
		setSelectedSpellChildIndex(spellChildIndex);
		setSelectedSpellItemId(spellItemId);
	}
	
	//Actor Hook Methods
	public static int getNpcAnimationId(Actor npc)
	{
		Field animation = getField(actorAnimationIdClassName, actorAnimationIdFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to get NPC animation id.";
		return getFieldIntValue(animation, npc, actorAnimationIdMultiplier, errorMsg);
	}
	
	public static HeadIcon getNpcOverheadIcon(NPC npc)
	{
		if (npc == null)
		{
			return null;
		}
		
		NPCComposition npcComposition = npc.getComposition();
		if (npcComposition == null)
		{
			return null;
		}

		/*
		Method getHeadIconArrayMethod = null;
		try
		{
			for (Method declaredMethod : npcComposition.getClass().getDeclaredMethods())
			{
				if (declaredMethod.getReturnType() == short[].class && declaredMethod.getParameterTypes().length == 0)
				{
					getHeadIconArrayMethod = declaredMethod;
					getHeadIconArrayMethod.setAccessible(true);
					short[] headIconArray = (short[]) getHeadIconArrayMethod.invoke(npcComposition);
					getHeadIconArrayMethod.setAccessible(false);
					if (headIconArray == null || headIconArray.length == 0)
					{
						continue;
					}
					return HeadIcon.values()[headIconArray[0]];
				}
			}
		}
		catch (Exception e)
		{
			log.error("Kotori Plugin Utils - Unable to get NPC Composition's overhead icon.", e);
			return null;
		}

		return null;

		 */


		Field overheads = getField(npcOverheadIconClassName, npcOverheadIconFieldName);
		if (overheads == null)
		{
			return null;
		}
		
		try
		{
			overheads.setAccessible(true);
			Object headIconShortArray = overheads.get(npcComposition);
			if (headIconShortArray == null)
			{
				return null;
			}
			short overheadIconShortValue = Array.getShort(headIconShortArray, 0);
			overheads.setAccessible(false);
			return HeadIcon.values()[overheadIconShortValue];
		}
		catch (Exception e)
		{
			log.error("Kotori Plugin Utils - Unable to get NPC Composition's overhead icon.", e);
			return null;
		}
	}
	
	public static int getActorPathLength(Actor actor)
	{
		Field pathLength = getField(actorPathLengthClassName, actorPathLengthFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to get Actor \"" + (actor.getName() != null ? actor.getName() : "null") + "\" path length.";
		return getFieldIntValue(pathLength, actor, actorPathLengthMultiplier, errorMsg);
	}
	
	public static boolean areYouMoving()
	{
		Player you = client.getLocalPlayer();
		return getActorPathLength(you) != 0;
	}

	public static boolean isActorMoving(Actor actor)
	{
		return getActorPathLength(actor) != 0;
	}
	
	//Menus Hook Methods
	public static int getMenuOptionsCount()
	{
		Field optionsCount = getField(menuOptionsCountClassName, menuOptionsCountFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to get menu options count.";
		return getFieldIntValue(optionsCount, client, menuOptionsCountMultiplier, errorMsg);
	}

	public static int getTopMenuEntryIndex()
	{
		return getMenuOptionsCount() - 1;
	}
	
	private static void setMenuIdentifier(int index, int value)
	{
		Field menuIdentifiers = getField(menuIdentifiersClassName, menuIdentifiersFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu identifier \"" + value + "\" in menu index \"" + index + "\".";
		setFieldIntArrayValue(menuIdentifiers, client, index, value, errorMsg);
	}
	
	public static void setMenuItemId(int index, int value)
	{
		Field menuItemIds = getField(menuItemIdsClassName, menuItemIdsFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu item id \"" + value + "\" in menu index \"" + index + "\".";
		setFieldIntArrayValue(menuItemIds, client, index, value, errorMsg);
	}
	
	private static void setMenuOption(int index, String value)
	{
		Field menuOptions = getField(menuOptionsClassName, menuOptionsFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu option \"" + value + "\" in menu index \"" + index + "\".";
		setFieldObjectArrayValue(menuOptions, client, index, value, errorMsg);
	}
	
	private static void setMenuParam0(int index, int value)
	{
		Field menuParam0s = getField(menuParam0ClassName, menuParam0FieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu param0 \"" + value + "\" in menu index \"" + index + "\".";
		setFieldIntArrayValue(menuParam0s, client, index, value, errorMsg);
	}
	
	private static void setMenuParam1(int index, int value)
	{
		Field menuParam1s = getField(menuParam1ClassName, menuParam1FieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu param1 \"" + value + "\" in menu index \"" + index + "\".";
		setFieldIntArrayValue(menuParam1s, client, index, value, errorMsg);
	}
	
	private static void setMenuTarget(int index, String value)
	{
		Field menuTargets = getField(menuTargetsClassName, menuTargetsFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu target \"" + value + "\" in menu index \"" + index + "\".";
		setFieldObjectArrayValue(menuTargets, client, index, value, errorMsg);
	}
	
	private static void setMenuOpcode(int index, int value)
	{
		Field menuOpcodes = getField(menuTypesClassName, menuTypesFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu option \"" + value + "\" in menu index \"" + index + "\".";
		setFieldIntArrayValue(menuOpcodes, client, index, value, errorMsg);
	}
	
	public static void insertMenuEntry(int index, String option, String target, int opcode, int id, int param0, int param1, int itemId)
	{
		setMenuOption(index, option);
		setMenuTarget(index, target);
		setMenuOpcode(index, opcode);
		setMenuIdentifier(index, id);
		setMenuParam0(index, param0);
		setMenuParam1(index, param1);
		setMenuItemId(index, itemId);
	}
}
