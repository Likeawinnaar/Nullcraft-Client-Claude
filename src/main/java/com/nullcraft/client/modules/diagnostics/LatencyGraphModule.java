package com.nullcraft.client.modules.diagnostics;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * LatencyGraph - Graphs network latency over time.
 */
public class LatencyGraphModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showOnHUD;
    private final ModeSetting position;
    private final IntegerSetting width;
    private final IntegerSetting height;
    private final IntegerSetting historySize;
    private final IntegerSetting updateInterval;
    private final ColorSetting graphColor;
    private final ColorSetting backgroundColor;
    private final ColorSetting gridColor;

    public LatencyGraphModule() {
        super("latency_graph", "LatencyGraph", "Graphs network latency over time", ModuleCategory.DIAGNOSTICS, "icon_latency_graph");

        this.enabled = new BooleanSetting("enabled", "Enable LatencyGraph", false);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", false);
        this.position = new ModeSetting("position", "HUD position", "bottom_right", "top_left", "top_right", "bottom_left", "bottom_right", "bottom_center");
        this.width = new IntegerSetting("width", "Graph width", 200, 50, 500);
        this.height = new IntegerSetting("height", "Graph height", 80, 30, 200);
        this.historySize = new IntegerSetting("history_size", "History size (samples)", 100, 20, 500);
        this.updateInterval = new IntegerSetting("update_interval", "Update interval (ticks)", 5, 1, 20);
        this.graphColor = new ColorSetting("graph_color", "Graph line color", 0xFF8B5CF6);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);
        this.gridColor = new ColorSetting("grid_color", "Grid color", 0xFF343644);

        registerSetting(enabled);
        registerSetting(showOnHUD);
        registerSetting(position);
        registerSetting(width);
        registerSetting(height);
        registerSetting(historySize);
        registerSetting(updateInterval);
        registerSetting(graphColor);
        registerSetting(backgroundColor);
        registerSetting(gridColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showOnHUD, position, width, height, historySize, updateInterval);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(graphColor, backgroundColor, gridColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Latency sampling
    }
}