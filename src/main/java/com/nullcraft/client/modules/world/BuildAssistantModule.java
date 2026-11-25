package com.nullcraft.client.modules.world;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * BuildAssistant - Helps with building (block outlines, ghost blocks, etc.)
 */
public class BuildAssistantModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup visualsGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showGhostBlocks;
    private final BooleanSetting showBlockOutlines;
    private final BooleanSetting showGrid;
    private final BooleanSetting showMeasurements;
    private final ModeSetting gridMode;
    private final IntegerSetting gridSpacing;
    private final ColorSetting ghostBlockColor;
    private final ColorSetting outlineColor;
    private final ColorSetting gridColor;

    public BuildAssistantModule() {
        super("build_assistant", "BuildAssistant", "Helps with building (block outlines, ghost blocks, measurements)", ModuleCategory.WORLD, "icon_build_assistant");

        this.enabled = new BooleanSetting("enabled", "Enable BuildAssistant", true);
        this.showGhostBlocks = new BooleanSetting("show_ghost_blocks", "Show ghost blocks", true);
        this.showBlockOutlines = new BooleanSetting("show_block_outlines", "Show block outlines", true);
        this.showGrid = new BooleanSetting("show_grid", "Show building grid", false);
        this.showMeasurements = new BooleanSetting("show_measurements", "Show measurements", true);
        this.gridMode = new ModeSetting("grid_mode", "Grid mode", "chunk", "chunk", "block", "region");
        this.gridSpacing = new IntegerSetting("grid_spacing", "Grid spacing (blocks)", 16, 1, 64);
        this.ghostBlockColor = new ColorSetting("ghost_block_color", "Ghost block color", 0x808B5CF6);
        this.outlineColor = new ColorSetting("outline_color", "Outline color", 0xFF8B5CF6);
        this.gridColor = new ColorSetting("grid_color", "Grid color", 0x408B5CF6);

        registerSetting(enabled);
        registerSetting(showGhostBlocks);
        registerSetting(showBlockOutlines);
        registerSetting(showGrid);
        registerSetting(showMeasurements);
        registerSetting(gridMode);
        registerSetting(gridSpacing);
        registerSetting(ghostBlockColor);
        registerSetting(outlineColor);
        registerSetting(gridColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showGhostBlocks, showBlockOutlines, showGrid, showMeasurements, gridMode, gridSpacing);

        visualsGroup = createGroup("Colors", "Visual colors");
        visualsGroup.addSettings(ghostBlockColor, outlineColor, gridColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Build assistant rendering
    }
}