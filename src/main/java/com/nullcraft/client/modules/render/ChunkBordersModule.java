package com.nullcraft.client.modules.render;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * ChunkBorders - Shows chunk borders.
 */
public class ChunkBordersModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup styleGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showChunkGrid;
    private final BooleanSetting showChunkCoordinates;
    private final BooleanSetting showSlimeChunks;
    private final BooleanSetting onlyInRenderDistance;
    private final DoubleSetting lineWidth;
    private final ColorSetting gridColor;
    private final ColorSetting slimeChunkColor;
    private final ColorSetting currentChunkColor;

    public ChunkBordersModule() {
        super("chunk_borders", "ChunkBorders", "Shows chunk borders", ModuleCategory.RENDER, "icon_chunk_borders");

        this.enabled = new BooleanSetting("enabled", "Enable ChunkBorders", false);
        this.showChunkGrid = new BooleanSetting("show_chunk_grid", "Show chunk grid", true);
        this.showChunkCoordinates = new BooleanSetting("show_chunk_coords", "Show chunk coordinates", true);
        this.showSlimeChunks = new BooleanSetting("show_slime_chunks", "Show slime chunks", false);
        this.onlyInRenderDistance = new BooleanSetting("only_in_render_distance", "Only show in render distance", true);
        this.lineWidth = new DoubleSetting("line_width", "Line width", 1.5, 0.5, 5.0, 0.5);
        this.gridColor = new ColorSetting("grid_color", "Grid color", 0x808B5CF6);
        this.slimeChunkColor = new ColorSetting("slime_chunk_color", "Slime chunk color", 0x804CBF80);
        this.currentChunkColor = new ColorSetting("current_chunk_color", "Current chunk color", 0xFFF3AB4B);

        registerSetting(enabled);
        registerSetting(showChunkGrid);
        registerSetting(showChunkCoordinates);
        registerSetting(showSlimeChunks);
        registerSetting(onlyInRenderDistance);
        registerSetting(lineWidth);
        registerSetting(gridColor);
        registerSetting(slimeChunkColor);
        registerSetting(currentChunkColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showChunkGrid, showChunkCoordinates, showSlimeChunks, onlyInRenderDistance);

        styleGroup = createGroup("Style", "Visual style");
        styleGroup.addSettings(lineWidth, gridColor, slimeChunkColor, currentChunkColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Chunk border rendering
    }
}