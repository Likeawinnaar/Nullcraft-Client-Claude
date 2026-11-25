package com.nullcraft.client.modules.world;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * MiniMap - Minimap with entity radar and waypoints.
 */
public class MiniMapModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showEntities;
    private final BooleanSetting showPlayers;
    private final BooleanSetting showMobs;
    private final BooleanSetting showItems;
    private final BooleanSetting showWaypoints;
    private final BooleanSetting showChunkGrid;
    private final ModeSetting shape;
    private final ModeSetting position;
    private final IntegerSetting size;
    private final IntegerSetting zoom;
    private final ColorSetting backgroundColor;
    private final ColorSetting borderColor;

    public MiniMapModule() {
        super("mini_map", "MiniMap", "Minimap with entity radar and waypoints", ModuleCategory.WORLD, "icon_mini_map");

        this.enabled = new BooleanSetting("enabled", "Enable MiniMap", true);
        this.showEntities = new BooleanSetting("show_entities", "Show entities", true);
        this.showPlayers = new BooleanSetting("show_players", "Show players", true);
        this.showMobs = new BooleanSetting("show_mobs", "Show mobs", true);
        this.showItems = new BooleanSetting("show_items", "Show items", false);
        this.showWaypoints = new BooleanSetting("show_waypoints", "Show waypoints", true);
        this.showChunkGrid = new BooleanSetting("show_chunk_grid", "Show chunk grid", false);
        this.shape = new ModeSetting("shape", "Map shape", "circular", "circular", "square");
        this.position = new ModeSetting("position", "HUD position", "top_right", "top_left", "top_right", "bottom_left", "bottom_right");
        this.size = new IntegerSetting("size", "Map size", 150, 50, 400);
        this.zoom = new IntegerSetting("zoom", "Zoom level", 1, 1, 10);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);
        this.borderColor = new ColorSetting("border_color", "Border color", 0xFF343644);

        registerSetting(enabled);
        registerSetting(showEntities);
        registerSetting(showPlayers);
        registerSetting(showMobs);
        registerSetting(showItems);
        registerSetting(showWaypoints);
        registerSetting(showChunkGrid);
        registerSetting(shape);
        registerSetting(position);
        registerSetting(size);
        registerSetting(zoom);
        registerSetting(backgroundColor);
        registerSetting(borderColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showEntities, showPlayers, showMobs, showItems, showWaypoints, showChunkGrid, shape, position, size, zoom);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(backgroundColor, borderColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Minimap rendering
    }
}