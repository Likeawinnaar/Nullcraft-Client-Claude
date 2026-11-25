package com.nullcraft.client.modules.player;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * InventoryCleaner - Automatically drops/trashes unwanted items.
 */
public class InventoryCleanerModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup filtersGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting autoDrop;
    private final BooleanSetting trashOnFull;
    private final ModeSetting cleanMode;
    private final IntegerSetting keepAmount;
    private final BooleanSetting protectValuable;
    private final StringSetting blockList;

    public InventoryCleanerModule() {
        super("inventory_cleaner", "InventoryCleaner", "Automatically drops/trashes unwanted items", ModuleCategory.PLAYER, "icon_inventory_cleaner");

        this.enabled = new BooleanSetting("enabled", "Enable InventoryCleaner", false);
        this.autoDrop = new BooleanSetting("auto_drop", "Auto-drop trash items", false);
        this.trashOnFull = new BooleanSetting("trash_on_full", "Clean when inventory full", true);
        this.cleanMode = new ModeSetting("clean_mode", "Clean mode", "whitelist", "whitelist", "blacklist");
        this.keepAmount = new IntegerSetting("keep_amount", "Keep amount per item", 1, 0, 64);
        this.protectValuable = new BooleanSetting("protect_valuable", "Protect valuable items", true);
        this.blockList = new StringSetting("block_list", "Item filter list (comma-separated)", "", 1024, "minecraft:dirt,minecraft:cobblestone", false);

        registerSetting(enabled);
        registerSetting(autoDrop);
        registerSetting(trashOnFull);
        registerSetting(cleanMode);
        registerSetting(keepAmount);
        registerSetting(protectValuable);
        registerSetting(blockList);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, autoDrop, trashOnFull, cleanMode, keepAmount, protectValuable);

        filtersGroup = createGroup("Filters", "Item filters");
        filtersGroup.addSettings(blockList);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Inventory cleaning logic
    }
}