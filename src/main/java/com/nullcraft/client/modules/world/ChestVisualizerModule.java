package com.nullcraft.client.modules.world;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * ChestVisualizer - Highlights chests and shows contents.
 */
public class ChestVisualizerModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup visualsGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showChests;
    private final BooleanSetting showTrappedChests;
    private final BooleanSetting showEnderChests;
    private final BooleanSetting showBarrels;
    private final BooleanSetting showShulkerBoxes;
    private final BooleanSetting showContentsOnHover;
    private final BooleanSetting showItemCount;
    private final ModeSetting renderMode;
    private final IntegerSetting maxDistance;
    private final ColorSetting chestColor;
    private final ColorSetting trappedChestColor;
    private final ColorSetting enderChestColor;

    public ChestVisualizerModule() {
        super("chest_visualizer", "ChestVisualizer", "Highlights chests and shows contents", ModuleCategory.WORLD, "icon_chest_visualizer");

        this.enabled = new BooleanSetting("enabled", "Enable ChestVisualizer", true);
        this.showChests = new BooleanSetting("show_chests", "Show chests", true);
        this.showTrappedChests = new BooleanSetting("show_trapped_chests", "Show trapped chests", true);
        this.showEnderChests = new BooleanSetting("show_ender_chests", "Show ender chests", true);
        this.showBarrels = new BooleanSetting("show_barrels", "Show barrels", true);
        this.showShulkerBoxes = new BooleanSetting("show_shulker_boxes", "Show shulker boxes", true);
        this.showContentsOnHover = new BooleanSetting("show_contents_on_hover", "Show contents on hover", true);
        this.showItemCount = new BooleanSetting("show_item_count", "Show item count", true);
        this.renderMode = new ModeSetting("render_mode", "Render mode", "outline", "outline", "box", "glow", "text");
        this.maxDistance = new IntegerSetting("max_distance", "Max render distance", 50, 10, 200);
        this.chestColor = new ColorSetting("chest_color", "Chest color", 0xFF8B5CF6);
        this.trappedChestColor = new ColorSetting("trapped_chest_color", "Trapped chest color", 0xFFE65B7A);
        this.enderChestColor = new ColorSetting("ender_chest_color", "Ender chest color", 0xFF4CBF80);

        registerSetting(enabled);
        registerSetting(showChests);
        registerSetting(showTrappedChests);
        registerSetting(showEnderChests);
        registerSetting(showBarrels);
        registerSetting(showShulkerBoxes);
        registerSetting(showContentsOnHover);
        registerSetting(showItemCount);
        registerSetting(renderMode);
        registerSetting(maxDistance);
        registerSetting(chestColor);
        registerSetting(trappedChestColor);
        registerSetting(enderChestColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showChests, showTrappedChests, showEnderChests, showBarrels, showShulkerBoxes, showContentsOnHover, showItemCount, renderMode, maxDistance);

        visualsGroup = createGroup("Colors", "Container colors");
        visualsGroup.addSettings(chestColor, trappedChestColor, enderChestColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Chest rendering
    }
}