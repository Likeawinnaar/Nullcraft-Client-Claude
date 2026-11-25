package com.nullcraft.client.modules.misc;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * FPSMonitor - Monitors and displays client FPS.
 */
public class FPSMonitorModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showOnHUD;
    private final ModeSetting position;
    private final BooleanSetting showGraph;
    private final IntegerSetting updateInterval;
    private final ColorSetting goodColor;
    private final ColorSetting warningColor;
    private final ColorSetting badColor;
    private final ColorSetting backgroundColor;

    public FPSMonitorModule() {
        super("fps_monitor", "FPSMonitor", "Monitors and displays client FPS", ModuleCategory.MISC, "icon_fps_monitor");

        this.enabled = new BooleanSetting("enabled", "Enable FPSMonitor", true);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", true);
        this.position = new ModeSetting("position", "HUD position", "top_right", "top_left", "top_right", "bottom_left", "bottom_right");
        this.showGraph = new BooleanSetting("show_graph", "Show FPS graph", false);
        this.updateInterval = new IntegerSetting("update_interval", "Update interval (ticks)", 10, 1, 20);
        this.goodColor = new ColorSetting("good_color", "Good FPS color (>60)", 0xFF4CBF80);
        this.warningColor = new ColorSetting("warning_color", "Warning FPS color (30-60)", 0xFFF3AB4B);
        this.badColor = new ColorSetting("bad_color", "Bad FPS color (<30)", 0xFFE65B7A);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);

        registerSetting(enabled);
        registerSetting(showOnHUD);
        registerSetting(position);
        registerSetting(showGraph);
        registerSetting(updateInterval);
        registerSetting(goodColor);
        registerSetting(warningColor);
        registerSetting(badColor);
        registerSetting(backgroundColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showOnHUD, position, showGraph, updateInterval);

        displayGroup = createGroup("Colors", "FPS quality colors");
        displayGroup.addSettings(goodColor, warningColor, badColor, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // FPS monitoring
    }
}