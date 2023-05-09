/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
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
package com.theplug.kotori.reorderprayers;

import com.google.common.collect.ImmutableList;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;

import com.theplug.kotori.kotoriutils.KotoriUtils;
import com.theplug.kotori.kotoriutils.rlapi.WidgetIDPlus;
import com.theplug.kotori.kotoriutils.rlapi.WidgetInfoPlus;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import static net.runelite.api.widgets.WidgetConfig.DRAG;
import static net.runelite.api.widgets.WidgetConfig.DRAG_ON;

import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDependency(KotoriUtils.class)
@PluginDescriptor(
	name = "Reorder Prayers",
	enabledByDefault = false,
	description = "Reorder the prayers displayed on the Prayer panel.",
	tags = {"kotori", "ported", "reorder", "prayers"}
)
public class ReorderPrayersPlugin extends Plugin
{

	static final String CONFIG_GROUP_KEY = "reorderprayers";

	static final String CONFIG_UNLOCK_REORDERING_KEY = "unlockPrayerReordering";

	static final String CONFIG_PRAYER_ORDER_KEY = "prayerOrder";

	private static final int PRAYER_WIDTH = 34;

	private static final int PRAYER_HEIGHT = 34;

	private static final int PRAYER_X_OFFSET = 37;

	private static final int PRAYER_Y_OFFSET = 37;

	private static final int QUICK_PRAYER_SPRITE_X_OFFSET = 2;

	private static final int QUICK_PRAYER_SPRITE_Y_OFFSET = 2;

	private static final int PRAYER_COLUMN_COUNT = 5;

	private static final int PRAYER_COUNT = Prayer.values().length;
	
	private static final List<WidgetInfoPlus> PRAYER_WIDGET_INFO_LIST = ImmutableList.of(
			WidgetInfoPlus.PRAYER_THICK_SKIN,
			WidgetInfoPlus.PRAYER_BURST_OF_STRENGTH,
			WidgetInfoPlus.PRAYER_CLARITY_OF_THOUGHT,
			WidgetInfoPlus.PRAYER_SHARP_EYE,
			WidgetInfoPlus.PRAYER_MYSTIC_WILL,
			WidgetInfoPlus.PRAYER_ROCK_SKIN,
			WidgetInfoPlus.PRAYER_SUPERHUMAN_STRENGTH,
			WidgetInfoPlus.PRAYER_IMPROVED_REFLEXES,
			WidgetInfoPlus.PRAYER_RAPID_RESTORE,
			WidgetInfoPlus.PRAYER_RAPID_HEAL,
			WidgetInfoPlus.PRAYER_PROTECT_ITEM,
			WidgetInfoPlus.PRAYER_HAWK_EYE,
			WidgetInfoPlus.PRAYER_MYSTIC_LORE,
			WidgetInfoPlus.PRAYER_STEEL_SKIN,
			WidgetInfoPlus.PRAYER_ULTIMATE_STRENGTH,
			WidgetInfoPlus.PRAYER_INCREDIBLE_REFLEXES,
			WidgetInfoPlus.PRAYER_PROTECT_FROM_MAGIC,
			WidgetInfoPlus.PRAYER_PROTECT_FROM_MISSILES,
			WidgetInfoPlus.PRAYER_PROTECT_FROM_MELEE,
			WidgetInfoPlus.PRAYER_EAGLE_EYE,
			WidgetInfoPlus.PRAYER_MYSTIC_MIGHT,
			WidgetInfoPlus.PRAYER_RETRIBUTION,
			WidgetInfoPlus.PRAYER_REDEMPTION,
			WidgetInfoPlus.PRAYER_SMITE,
			WidgetInfoPlus.PRAYER_PRESERVE,
			WidgetInfoPlus.PRAYER_CHIVALRY,
			WidgetInfoPlus.PRAYER_PIETY,
			WidgetInfoPlus.PRAYER_RIGOUR,
			WidgetInfoPlus.PRAYER_AUGURY
	);
	
	private static final List<Integer> QUICK_PRAYER_CHILD_IDS = ImmutableList.of(
			WidgetIDPlus.QuickPrayer.THICK_SKIN_CHILD_ID,
			WidgetIDPlus.QuickPrayer.BURST_OF_STRENGTH_CHILD_ID,
			WidgetIDPlus.QuickPrayer.CLARITY_OF_THOUGHT_CHILD_ID,
			WidgetIDPlus.QuickPrayer.SHARP_EYE_CHILD_ID,
			WidgetIDPlus.QuickPrayer.MYSTIC_WILL_CHILD_ID,
			WidgetIDPlus.QuickPrayer.ROCK_SKIN_CHILD_ID,
			WidgetIDPlus.QuickPrayer.SUPERHUMAN_STRENGTH_CHILD_ID,
			WidgetIDPlus.QuickPrayer.IMPROVED_REFLEXES_CHILD_ID,
			WidgetIDPlus.QuickPrayer.RAPID_RESTORE_CHILD_ID,
			WidgetIDPlus.QuickPrayer.RAPID_HEAL_CHILD_ID,
			WidgetIDPlus.QuickPrayer.PROTECT_ITEM_CHILD_ID,
			WidgetIDPlus.QuickPrayer.HAWK_EYE_CHILD_ID,
			WidgetIDPlus.QuickPrayer.MYSTIC_LORE_CHILD_ID,
			WidgetIDPlus.QuickPrayer.STEEL_SKIN_CHILD_ID,
			WidgetIDPlus.QuickPrayer.ULTIMATE_STRENGTH_CHILD_ID,
			WidgetIDPlus.QuickPrayer.INCREDIBLE_REFLEXES_CHILD_ID,
			WidgetIDPlus.QuickPrayer.PROTECT_FROM_MAGIC_CHILD_ID,
			WidgetIDPlus.QuickPrayer.PROTECT_FROM_MISSILES_CHILD_ID,
			WidgetIDPlus.QuickPrayer.PROTECT_FROM_MELEE_CHILD_ID,
			WidgetIDPlus.QuickPrayer.EAGLE_EYE_CHILD_ID,
			WidgetIDPlus.QuickPrayer.MYSTIC_MIGHT_CHILD_ID,
			WidgetIDPlus.QuickPrayer.RETRIBUTION_CHILD_ID,
			WidgetIDPlus.QuickPrayer.REDEMPTION_CHILD_ID,
			WidgetIDPlus.QuickPrayer.SMITE_CHILD_ID,
			WidgetIDPlus.QuickPrayer.PRESERVE_CHILD_ID,
			WidgetIDPlus.QuickPrayer.CHIVALRY_CHILD_ID,
			WidgetIDPlus.QuickPrayer.PIETY_CHILD_ID,
			WidgetIDPlus.QuickPrayer.RIGOUR_CHILD_ID,
			WidgetIDPlus.QuickPrayer.AUGURY_CHILD_ID
	);

	private static final String LOCK = "Lock";

	private static final String UNLOCK = "Unlock";

	private static final String MENU_TARGET = "Reordering";

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ReorderPrayersConfig config;

	@Inject
	private MenuManager menuManager;

	private Prayer[] prayerOrder;

	private boolean unlockPrayerReordering;
	private final String defaultPrayerOrderString = prayerOrderToString(Prayer.values());

	static String prayerOrderToString(Prayer[] prayerOrder)
	{
		return Arrays.stream(prayerOrder)
			.map(Prayer::name)
			.collect(Collectors.joining(","));
	}

	private static Prayer[] stringToPrayerOrder(String string)
	{
		return Arrays.stream(string.split(","))
			.map(Prayer::valueOf)
			.toArray(Prayer[]::new);
	}

	private static int getPrayerIndex(Widget widget)
	{
		int x = widget.getOriginalX() / PRAYER_X_OFFSET;
		int y = widget.getOriginalY() / PRAYER_Y_OFFSET;
		return x + y * PRAYER_COLUMN_COUNT;
	}

	private void setWidgetPosition(Widget widget, int x, int y)
	{
		widget.setRelativeX(x);
		widget.setRelativeY(y);
		widget.setOriginalX(x);
		widget.setOriginalY(y);
	}

	@Provides
	ReorderPrayersConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ReorderPrayersConfig.class);
	}

	@Override
	protected void startUp()
	{
		prayerOrder = stringToPrayerOrder(config.prayerOrder());
		reorderPrayers();
	}

	@Override
	protected void shutDown()
	{
		prayerOrder = Prayer.values();
		reorderPrayers(false);
		config.unlockPrayerReordering(false);
		unlockPrayerReordering = false;
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(CONFIG_GROUP_KEY))
		{
			if (event.getKey().equals(CONFIG_PRAYER_ORDER_KEY))
			{
				prayerOrder = stringToPrayerOrder(config.prayerOrder());
			}
			reorderPrayers();
		}
	}

	@Subscribe
	private void onWidgetLoaded(WidgetLoaded event)
	{
		if (event.getGroupId() == WidgetIDPlus.PRAYER_GROUP_ID || event.getGroupId() == WidgetIDPlus.QUICK_PRAYERS_GROUP_ID)
		{
			reorderPrayers();
		}
	}

	@Subscribe
	private void onScriptPostFired(ScriptPostFired event)
	{
		reorderPrayers();
	}

	@Subscribe
	private void onDraggingWidgetChanged(DraggingWidgetChanged event)
	{
		// is dragging widget and mouse button released
		if (event.isDraggingWidget() && client.getMouseCurrentButton() == 0)
		{
			Widget draggedWidget = client.getDraggedWidget();
			Widget draggedOnWidget = client.getDraggedOnWidget();
			if (draggedWidget != null && draggedOnWidget != null)
			{
				int draggedGroupId = WidgetInfo.TO_GROUP(draggedWidget.getId());
				int draggedOnGroupId = WidgetInfo.TO_GROUP(draggedOnWidget.getId());
				if (draggedGroupId != WidgetIDPlus.PRAYER_GROUP_ID || draggedOnGroupId != WidgetIDPlus.PRAYER_GROUP_ID
					|| draggedOnWidget.getWidth() != PRAYER_WIDTH || draggedOnWidget.getHeight() != PRAYER_HEIGHT)
				{
					return;
				}
				// reset dragged on widget to prevent sending a drag widget packet to the server
				client.setDraggedOnWidget(null);

				int fromPrayerIndex = getPrayerIndex(draggedWidget);
				int toPrayerIndex = getPrayerIndex(draggedOnWidget);

				Prayer tmp = prayerOrder[toPrayerIndex];
				prayerOrder[toPrayerIndex] = prayerOrder[fromPrayerIndex];
				prayerOrder[fromPrayerIndex] = tmp;

				save();
			}
		}
	}

	@Subscribe
	private void onMenuOpened(MenuOpened event)
	{
		Widget menuWidget = event.getFirstEntry().getWidget();
		if (menuWidget == null)
		{
			return;
		}

		int menuWidgetId = menuWidget.getId();

		if (menuWidgetId == WidgetInfo.FIXED_VIEWPORT_PRAYER_TAB.getId() ||
				menuWidgetId == WidgetInfo.RESIZABLE_VIEWPORT_PRAYER_TAB.getId() ||
				menuWidgetId == WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_PRAYER_TAB.getId())
		{
			if (unlockPrayerReordering)
			{
				client.createMenuEntry(1).setOption(LOCK).setTarget(MENU_TARGET);
			}
			else
			{
				client.createMenuEntry(1).setOption(UNLOCK).setTarget(MENU_TARGET);
			}
		}
	}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		String menuString = event.getMenuOption() + " " + event.getMenuTarget();

		if (menuString.equals("Unlock Reordering"))
		{
			config.unlockPrayerReordering(true);
			unlockPrayerReordering = true;
		}
		else if (menuString.equals("Lock Reordering"))
		{
			config.unlockPrayerReordering(false);
			unlockPrayerReordering = false;
		}
	}

	private PrayerTabState getPrayerTabState()
	{
		HashTable<WidgetNode> componentTable = client.getComponentTable();
		for (WidgetNode widgetNode : componentTable)
		{
			if (widgetNode.getId() == WidgetIDPlus.PRAYER_GROUP_ID)
			{
				return PrayerTabState.PRAYERS;
			}
			else if (widgetNode.getId() == WidgetIDPlus.QUICK_PRAYERS_GROUP_ID)
			{
				return PrayerTabState.QUICK_PRAYERS;
			}
		}
		return PrayerTabState.NONE;
	}

	private void save()
	{
		config.prayerOrder(prayerOrderToString(prayerOrder));
	}

	private void reorderPrayers()
	{
		reorderPrayers(config.unlockPrayerReordering());
	}

	private void reorderPrayers(boolean unlocked)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		PrayerTabState prayerTabState = getPrayerTabState();

		if (prayerTabState == PrayerTabState.PRAYERS)
		{
			List<Widget> prayerWidgets = PRAYER_WIDGET_INFO_LIST.stream()
				.map(w -> client.getWidget(w.getId()))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

			if (prayerWidgets.size() != PRAYER_WIDGET_INFO_LIST.size())
			{
				return;
			}

			for (int index = 0; index < prayerOrder.length; index++)
			{
				Prayer prayer = prayerOrder[index];
				Widget prayerWidget = prayerWidgets.get(prayer.ordinal());

				int widgetConfig = prayerWidget.getClickMask();
				if (unlocked)
				{
					// allow dragging of this widget
					widgetConfig |= DRAG;
					// allow this widget to be dragged on
					widgetConfig |= DRAG_ON;
				}
				else
				{
					// remove drag flag
					widgetConfig &= ~DRAG;
					// remove drag on flag
					widgetConfig &= ~DRAG_ON;
				}
				prayerWidget.setClickMask(widgetConfig);

				int x = index % PRAYER_COLUMN_COUNT;
				int y = index / PRAYER_COLUMN_COUNT;
				int widgetX = x * PRAYER_X_OFFSET;
				int widgetY = y * PRAYER_Y_OFFSET;
				setWidgetPosition(prayerWidget, widgetX, widgetY);
			}
		}
		else if (prayerTabState == PrayerTabState.QUICK_PRAYERS)
		{
			Widget prayersContainer = client.getWidget(WidgetInfoPlus.QUICK_PRAYER_PRAYERS.getId());
			if (prayersContainer == null)
			{
				return;
			}
			Widget[] prayerWidgets = prayersContainer.getDynamicChildren();
			if (prayerWidgets == null || prayerWidgets.length != PRAYER_COUNT * 3)
			{
				return;
			}

			for (int index = 0; index < prayerOrder.length; index++)
			{
				Prayer prayer = prayerOrder[index];

				int x = index % PRAYER_COLUMN_COUNT;
				int y = index / PRAYER_COLUMN_COUNT;

				Widget prayerWidget = prayerWidgets[QUICK_PRAYER_CHILD_IDS.get(prayer.ordinal())];
				setWidgetPosition(prayerWidget, x * PRAYER_X_OFFSET, y * PRAYER_Y_OFFSET);

				int childId = PRAYER_COUNT + 2 * prayer.ordinal();

				Widget prayerSpriteWidget = prayerWidgets[childId];
				setWidgetPosition(prayerSpriteWidget,
					QUICK_PRAYER_SPRITE_X_OFFSET + x * PRAYER_X_OFFSET,
					QUICK_PRAYER_SPRITE_Y_OFFSET + y * PRAYER_Y_OFFSET);

				Widget prayerToggleWidget = prayerWidgets[childId + 1];
				setWidgetPosition(prayerToggleWidget, x * PRAYER_X_OFFSET, y * PRAYER_Y_OFFSET);
			}
		}
	}
}
