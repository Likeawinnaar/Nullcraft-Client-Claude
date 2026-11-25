package com.nullcraft.client.modules.render;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * CoordinateOverlay - Shows current coordinates on screen.
 */
public class CoordinateOverlayModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showX;
    private final BooleanSetting showY;
    private final BooleanSetting showZ;
    private final BooleanSetting showBlockCoords;
    private final BooleanSetting showChunkCoords;
    private final BooleanSetting showDimension;
    private final ModeSetting position;
    private final ColorSetting color;
    private final ColorSetting backgroundColor;
    private final DoubleSetting scale;

    public CoordinateOverlayModule() {
        super("coordinate_overlay", "CoordinateOverlay", "Shows current coordinates on screen", ModuleCategory.RENDER, "icon_coordinate_overlay");

        this.enabled = new BooleanSetting("enabled", "Enable CoordinateOverlay", true);
        this.showX = new BooleanSetting("show_x", "Show X coordinate", true);
        this.showY = new BooleanSetting("show_y", "Show Y coordinate", true);
        this.showZ = new BooleanSetting("show_z", "Show Z coordinate", true);
        this.showBlockCoords = new BooleanSetting("show_block_coords", "Show block coordinates", true);
        this.showChunkCoords = new BooleanSetting("show_chunk_coords", "Show chunk coordinates", false);
        this.showDimension = new BooleanSetting("show_dimension", "Show dimension", true);
        this.position = new ModeSetting("position", "HUD position", "top_left", "top_left", "top_right", "bottom_left", "bottom_right", "top_center");
        this.color = new ColorSetting("color", "Text color", 0xFFA78BFA);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);
        this.scale = new DoubleSetting("scale", "Text scale", 1.0, 0.5, 2.0, 0.1);

        registerSetting(enabled);
        registerSetting(showX);
        registerSetting(showY);
        registerSetting(showZ);
        registerSetting(showBlockCoords);
        registerSetting(showChunkCoords);
        registerSetting(showDimension);
        registerSetting(position);
        registerSetting(color);
        registerSetting(backgroundColor);
        registerSetting(scale);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showX, showY, showZ, showBlockCoords, showChunkCoords, showDimension, position, scale);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(color, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Coordinate rendering handled by HudManager
    }
}