package com.nullcraft.client.modules.player;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * InventorySorter - Automatically sorts inventory.
 */
public class InventorySorterModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting sortOnOpen;
    private final BooleanSetting sortOnKeybind;
    private final ModeSetting sortMode;
    private final BooleanSetting groupSimilarItems;
    private final BooleanSetting ignoreHotbar;
    private final IntegerSetting sortDelay;

    public InventorySorterModule() {
        super("inventory_sorter", "InventorySorter", "Automatically sorts inventory", ModuleCategory.PLAYER, "icon_inventory_sorter");

        this.enabled = new BooleanSetting("enabled", "Enable InventorySorter", true);
        this.sortOnOpen = new BooleanSetting("sort_on_open", "Sort when opening inventory", false);
        this.sortOnKeybind = new BooleanSetting("sort_on_keybind", "Sort on keybind", true);
        this.sortMode = new ModeSetting("sort_mode", "Sort mode", "type", "type", "name", "rarity", "creative_tab", "custom");
        this.groupSimilarItems = new BooleanSetting("group_similar", "Group similar items", true);
        this.ignoreHotbar = new BooleanSetting("ignore_hotbar", "Ignore hotbar slots", true);
        this.sortDelay = new IntegerSetting("sort_delay", "Sort delay (ticks)", 2, 0, 20);

        registerSetting(enabled);
        registerSetting(sortOnOpen);
        registerSetting(sortOnKeybind);
        registerSetting(sortMode);
        registerSetting(groupSimilarItems);
        registerSetting(ignoreHotbar);
        registerSetting(sortDelay);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, sortOnOpen, sortOnKeybind, sortDelay);

        behaviorGroup = createGroup("Sorting", "Sort behavior");
        behaviorGroup.addSettings(sortMode, groupSimilarItems, ignoreHotbar);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Inventory sorting logic
    }
}