package com.theplug.kotori.kotoriutils;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

@Slf4j
public class ReflectionLibrary
{
	private final static Client client = RuneLite.getInjector().getInstance(Client.class);
	private final static ClientThread clientThread = RuneLite.getInjector().getInstance(ClientThread.class);
	
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
	private static String actorAnimationObjectClassName;
	@Setter
	private static String actorAnimationObjectFieldName;

	@Setter
	private static String actorAnimationIdClassName;
	@Setter
	private static String actorAnimationIdFieldName;
	@Setter
	private static int actorAnimationIdMultiplier;

	@Setter
	private static String actorPathLengthClassName;
	@Setter
	private static String actorPathLengthFieldName;
	@Setter
	private static int actorPathLengthMultiplier;

	@Setter
	private static String oldNpcOverheadArrayClassName;
	@Setter
	private static String oldNpcOverheadArrayFieldName;

	@Setter
	private static String newNpcOverheadObjectClassName;
	@Setter
	private static String newNpcOverheadObjectFieldName;

	@Setter
	private static String newNpcOverheadArrayClassName;
	@Setter
	private static String newNpcOverheadArrayFieldName;

	@Setter
	private static String npcOverheadMethodClassName;
	@Setter
	private static String npcOverheadMethodName;
	@Setter
	private static int npcOverheadMethodJunkValue;
	
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

	private static Object getFieldObjectValue(Field field, Object objectWithField, String errorMsg)
	{
		if (field == null || objectWithField == null)
		{
			return null;
		}

		try
		{
			field.setAccessible(true);
			Object obj = field.get(objectWithField);
			field.setAccessible(false);
			return obj;
		}
		catch (Exception e)
		{
			log.error(errorMsg, e);
			return null;
		}
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
	
	private static void setFieldIntValue(Field field, Object objectWithField, int valueToSet, String errorMsg)
	{
		if (field == null || objectWithField == null)
		{
			return;
		}
		
		try
		{
			field.setAccessible(true);
			field.setInt(objectWithField, valueToSet);
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
	public static void invokeMenuAction(int param0, int param1, int opcode, int identifier, int itemId, int worldViewId, String option, String target, int x, int y)
	{
		Class<?> clazz = getClass(invokeMenuActionClassName);
		Method method;
		boolean isJunkValueAByte = invokeMenuActionJunkValue < 128 && invokeMenuActionJunkValue >= -128;
		boolean isJunkValueShort = invokeMenuActionJunkValue < 32767 && invokeMenuActionJunkValue >= -32767;
		
		if (clazz == null)
		{
			return;
		}
		
		try
		{
			if (isJunkValueAByte)
			{
				method = clazz.getDeclaredMethod(invokeMenuActionMethodName, int.class, int.class, int.class, int.class, int.class, int.class, String.class, String.class,
						int.class, int.class, byte.class);
			}
			else if (isJunkValueShort)
			{
				method = clazz.getDeclaredMethod(invokeMenuActionMethodName, int.class, int.class, int.class, int.class, int.class, int.class, String.class, String.class,
						int.class, int.class, short.class);
			}
			else
			{
				method = clazz.getDeclaredMethod(invokeMenuActionMethodName, int.class, int.class, int.class, int.class, int.class, int.class, String.class, String.class,
						int.class, int.class, int.class);
			}
		}
		catch (Exception e)
		{
			log.error("Kotori Plugin Utils - Unable to find invokeMenuAction method \"" + invokeMenuActionMethodName +
					"\". Check if obfuscated method name is correct.", e);
			return;
		}

		clientThread.invoke(() -> {
			try
			{
				method.setAccessible(true);
				if (isJunkValueAByte)
				{
					//-1 is the id for the WorldView.
					method.invoke(null, param0, param1, opcode, identifier, itemId, worldViewId, option, target, x, y, (byte) invokeMenuActionJunkValue);
				}
				else if (isJunkValueShort)
				{
					//-1 is the id for the WorldView.
					method.invoke(null, param0, param1, opcode, identifier, itemId, worldViewId, option, target, x, y, (short) invokeMenuActionJunkValue);
				}
				else
				{
					//-1 is the id for the WorldView.
					method.invoke(null, param0, param1, opcode, identifier, itemId, worldViewId, option, target, x, y, invokeMenuActionJunkValue);
				}
				method.setAccessible(false);
			}
			catch (Exception e)
			{
				log.error("Kotori Plugin Utils - Unable to invoke the method invokeMenuAction.", e);
			}
		});
	}
	
	public static void invokeMenuAction(int param0, int param1, int opcode, int identifier, int itemId)
	{
		invokeMenuAction(param0, param1, opcode, identifier, itemId, "", "");
	}

	public static void invokeMenuAction(int param0, int param1, int opcode, int identifier, int itemId, String option, String target)
	{
		invokeMenuAction(param0, param1, opcode, identifier, itemId, -1, option, target, -1, -1);
	}
	
	//Walking Methods
	private static void setXCoordinate(int x)
	{
		Field xField = getField(sceneSelectedXClassName, sceneSelectedXFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set scene selected X coordinate.";
		setFieldIntValue(xField, client.getTopLevelWorldView().getScene(), x, errorMsg);
	}
	
	private static void setYCoordinate(int y)
	{
		Field yField = getField(sceneSelectedYClassName, sceneSelectedYFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set scene selected Y coordinate.";
		setFieldIntValue(yField, client.getTopLevelWorldView().getScene(), y, errorMsg);
	}

	private static void setCheckClick()
	{
		Field checkClick = getField(checkClickClassName, checkClickFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set check click walking boolean.";
		setFieldBooleanValue(checkClick, client.getTopLevelWorldView().getScene(), true, errorMsg);
	}
	
	private static void setViewportWalking()
	{
		Field viewport = getField(viewportWalkingClassName, viewportWalkingFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set scene viewport walking boolean.";
		setFieldBooleanValue(viewport, client.getTopLevelWorldView().getScene(), true, errorMsg);
	}
	
	public static void sceneWalk(WorldPoint worldPoint, boolean convertForInstance)
	{
		if (worldPoint == null)
		{
			return;
		}

		WorldView wv = client.getTopLevelWorldView();

		if (convertForInstance)
		{
			Collection<WorldPoint> localWorldPoints = WorldPoint.toLocalInstance(wv.getScene(), worldPoint);
			if (localWorldPoints.size() != 1)
			{
				return;
			}

			for (WorldPoint localWorld : localWorldPoints)
			{
				sceneWalk(LocalPoint.fromWorld(wv, localWorld));
				return;
			}
		}
		else
		{
			sceneWalk(LocalPoint.fromWorld(wv, worldPoint));
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
	/*
		Spell Widget is just any selected widget's packed id.
	 */
	private static void setSelectedSpellWidget(int widgetPackedId)
	{
		Class<?> clazz = getClass(selectedSpellWidgetClassName);
		Field spellWidget = getField(clazz, selectedSpellWidgetFieldName);
		String errorMsg = "Kotori Plugin Utils - Unable to set selected spell widget.";
		int value = widgetPackedId * selectedSpellWidgetMultiplier;
		setFieldIntValue(spellWidget, clazz, value, errorMsg);
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
		int value = index * selectedSpellChildIndexMultiplier;
		setFieldIntValue(spellChild, client, value, errorMsg);
	}

	/*
		SpellItemId is actually the item ID displayed by a widget. This can be gotten from Widget.getItemId().
		This needs to be set to -1 when you are trying to cast an actual spell because interacting with other widgets could set this value to not -1.
		For example, interacting with an item in the inventory (Use -> Target) would set this value to the item id of the item you interacted with.
	 */
	private static void setSelectedSpellItemId(int itemId)
	{
		Field spellItem = getField(selectedSpellItemIDClassName, selectedSpellItemIDFieldName);
		String errorMsg = "Kotori Plugin Utils - Unable to set selected spell item id.";
		int value = itemId * selectedSpellItemIDMultiplier;
		setFieldIntValue(spellItem, client, value, errorMsg);
	}

	//As explained above, you need to set spellChildIndex and spellItemId to -1 if you want to cast a spell.
	public static void setSelectedSpell(int spellWidgetId)
	{
		setSelectedWidgetHooks(spellWidgetId, -1, -1);
	}

	public static void setSelectedWidgetHooks(int spellWidgetId, int spellChildIndex, int spellItemId)
	{
		clientThread.invoke(() -> {
			setSelectedSpellWidget(spellWidgetId);
			setSelectedSpellChildIndex(spellChildIndex);
			setSelectedSpellItemId(spellItemId);
		});
	}
	
	//Actor Hook Methods
	public static int getNpcAnimationId(Actor npc)
	{
		if (actorAnimationObjectClassName.isBlank())
		{
			Field animation = getField(actorAnimationIdClassName, actorAnimationIdFieldName);
			String errorMsg = "Kotori Plugin Utils - Failed to get NPC animation id.";
			return getFieldIntValue(animation, npc, actorAnimationIdMultiplier, errorMsg);
		}
		else
		{
			Field animationClass = getField(actorAnimationObjectClassName, actorAnimationObjectFieldName);
			String objectErrorMsg = "Kotori Plugin Utils - Failed to get the new Actor animation class object.";
			Object animationObject = getFieldObjectValue(animationClass, npc, objectErrorMsg);
			Field animation = getField(actorAnimationIdClassName, actorAnimationIdFieldName);
			String errorMsg = "Kotori Plugin Utils - Failed to get NPC animation id.";
			return getFieldIntValue(animation, animationObject, actorAnimationIdMultiplier, errorMsg);
		}
	}
	
	public static HeadIcon getNpcOverheadIcon(NPC npc)
	{
		HeadIcon icon = null;

		try
		{
			short[] headIconArray = npc.getOverheadSpriteIds();
			if (headIconArray != null)
			{
				return HeadIcon.values()[headIconArray[0]];
			}
		}
		catch (Exception e)
		{
			log.error("Unable to use RL method npc.getOverheadSpriteIds.", e);
		}

		icon = getOldNpcOverheadArray(npc);
		if (icon != null)
		{
			return icon;
		}
		icon = getNewNpcOverheadArray(npc);
		if (icon != null)
		{
			return icon;
		}
		icon = getNpcOverheadMethod(npc);
		return icon;
	}

	private static HeadIcon getOldNpcOverheadArray(NPC npc)
	{
		if (npc == null)
		{
			return null;
		}

		NPCComposition npcComposition = npc.getComposition();

		try
		{
			Field overheads = getField(oldNpcOverheadArrayClassName, oldNpcOverheadArrayFieldName);
			String objectErrorMsg = "Kotori Plugin Utils - Failed to get the old NPC overheads array object.";
			Object headIconShortArray = getFieldObjectValue(overheads, npcComposition, objectErrorMsg);
			if (headIconShortArray == null)
			{
				return null;
			}
			short overheadIconShortValue = Array.getShort(headIconShortArray, 0);
			return HeadIcon.values()[overheadIconShortValue];
		}
		catch (Exception e)
		{
			log.error("Kotori Plugin Utils - Unable to use old method of getting NPC Composition's overhead icon via field.", e);
			return null;
		}
	}

	private static HeadIcon getNewNpcOverheadArray(NPC npc)
	{
		if (npc == null)
		{
			return null;
		}

		try
		{
			Field overheadClass = getField(newNpcOverheadObjectClassName, newNpcOverheadObjectFieldName);
			String objectErrorMsg = "Kotori Plugin Utils - Failed to get the new NPC overheads class object.";
			Object overheadObject = getFieldObjectValue(overheadClass, npc, objectErrorMsg);
			Field overheadArray = getField(newNpcOverheadArrayClassName, newNpcOverheadArrayFieldName);
			String arrayErrorMsg = "Kotori Plugin Utils - Failed to get NPC overheads array.";
			Object overheadArrayObject = getFieldObjectValue(overheadArray, overheadObject, arrayErrorMsg);
			if (overheadArrayObject == null)
			{
				return null;
			}
			short overheadShortValue = Array.getShort(overheadArrayObject, 0);
			return HeadIcon.values()[overheadShortValue];
		}
		catch (Exception e)
		{
			log.error("Kotori Plugin Utils - Failed to get overhead via NewNpcOverheadArray method.", e);
			return null;
		}
	}

	private static HeadIcon getNpcOverheadMethod(NPC npc)
	{
		if (npc == null)
		{
			return null;
		}

		Class<?> npcClazz = getClass(npcOverheadMethodClassName);
		Method method;

		if (npcClazz == null)
		{
			return null;
		}

		boolean isJunkValueAByte = npcOverheadMethodJunkValue < 128 && npcOverheadMethodJunkValue >= -128;
		boolean isJunkValueShort = npcOverheadMethodJunkValue < 32767 && npcOverheadMethodJunkValue >= -32767;

		try
		{
			if (isJunkValueAByte)
			{
				method = npcClazz.getDeclaredMethod(npcOverheadMethodName, byte.class);
			}
			else if (isJunkValueShort)
			{
				method = npcClazz.getDeclaredMethod(npcOverheadMethodName, short.class);
			}
			else
			{
				method = npcClazz.getDeclaredMethod(npcOverheadMethodName, int.class);
			}
		}
		catch (Exception e)
		{
			log.error("Kotori Plugin Utils - Unable to find the RuneLite getOverhead method in the NPC class.", e);
			return null;
		}

		try
		{
			Object headIconShortArray = null;
			method.setAccessible(true);
			if (isJunkValueAByte)
			{
				headIconShortArray = method.invoke(npc, (byte) npcOverheadMethodJunkValue);
			}
			else if (isJunkValueShort)
			{
				headIconShortArray = method.invoke(npc, (short) npcOverheadMethodJunkValue);
			}
			else
			{
				headIconShortArray = method.invoke(npc, npcOverheadMethodJunkValue);
			}
			method.setAccessible(false);
			if (headIconShortArray == null)
			{
				return null;
			}
			short overheadIconShortValue = Array.getShort(headIconShortArray, 0);
			return HeadIcon.values()[overheadIconShortValue];
		}
		catch (Exception e)
		{
			log.error("Kotori Plugin Utils - Unable to invoke the RuneLite getOverhead method.", e);
		}

		return null;
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
		return getFieldIntValue(optionsCount, client.getMenu(), menuOptionsCountMultiplier, errorMsg);
	}

	public static int getTopMenuEntryIndex()
	{
		return getMenuOptionsCount() - 1;
	}
	
	private static void setMenuIdentifier(int index, int value)
	{
		Field menuIdentifiers = getField(menuIdentifiersClassName, menuIdentifiersFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu identifier \"" + value + "\" in menu index \"" + index + "\".";
		setFieldIntArrayValue(menuIdentifiers, client.getMenu(), index, value, errorMsg);
	}
	
	public static void setMenuItemId(int index, int value)
	{
		Field menuItemIds = getField(menuItemIdsClassName, menuItemIdsFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu item id \"" + value + "\" in menu index \"" + index + "\".";
		setFieldIntArrayValue(menuItemIds, client.getMenu(), index, value, errorMsg);
	}
	
	private static void setMenuOption(int index, String value)
	{
		Field menuOptions = getField(menuOptionsClassName, menuOptionsFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu option \"" + value + "\" in menu index \"" + index + "\".";
		setFieldObjectArrayValue(menuOptions, client.getMenu(), index, value, errorMsg);
	}
	
	private static void setMenuParam0(int index, int value)
	{
		Field menuParam0s = getField(menuParam0ClassName, menuParam0FieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu param0 \"" + value + "\" in menu index \"" + index + "\".";
		setFieldIntArrayValue(menuParam0s, client.getMenu(), index, value, errorMsg);
	}
	
	private static void setMenuParam1(int index, int value)
	{
		Field menuParam1s = getField(menuParam1ClassName, menuParam1FieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu param1 \"" + value + "\" in menu index \"" + index + "\".";
		setFieldIntArrayValue(menuParam1s, client.getMenu(), index, value, errorMsg);
	}
	
	private static void setMenuTarget(int index, String value)
	{
		Field menuTargets = getField(menuTargetsClassName, menuTargetsFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu target \"" + value + "\" in menu index \"" + index + "\".";
		setFieldObjectArrayValue(menuTargets, client.getMenu(), index, value, errorMsg);
	}
	
	private static void setMenuOpcode(int index, int value)
	{
		Field menuOpcodes = getField(menuTypesClassName, menuTypesFieldName);
		String errorMsg = "Kotori Plugin Utils - Failed to set menu option \"" + value + "\" in menu index \"" + index + "\".";
		setFieldIntArrayValue(menuOpcodes, client.getMenu(), index, value, errorMsg);
	}
	
	public static void insertMenuEntry(int index, String option, String target, int opcode, int id, int param0, int param1, int itemId)
	{
		clientThread.invoke(() -> {
			setMenuOption(index, option);
			setMenuTarget(index, target);
			setMenuOpcode(index, opcode);
			setMenuIdentifier(index, id);
			setMenuParam0(index, param0);
			setMenuParam1(index, param1);
			setMenuItemId(index, itemId);
		});
	}
}
