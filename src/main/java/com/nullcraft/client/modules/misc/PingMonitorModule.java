package com.nullcraft.client.modules.misc;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * PingMonitor - Monitors and displays server ping.
 */
public class PingMonitorModule extends Module {

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

    public PingMonitorModule() {
        super("ping_monitor", "PingMonitor", "Monitors and displays server ping", ModuleCategory.MISC, "icon_ping_monitor");

        this.enabled = new BooleanSetting("enabled", "Enable PingMonitor", true);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", true);
        this.position = new ModeSetting("position", "HUD position", "top_right", "top_left", "top_right", "bottom_left", "bottom_right");
        this.showGraph = new BooleanSetting("show_graph", "Show ping graph", false);
        this.updateInterval = new IntegerSetting("update_interval", "Update interval (ticks)", 20, 5, 100);
        this.goodColor = new ColorSetting("good_color", "Good ping color (<100ms)", 0xFF4CBF80);
        this.warningColor = new ColorSetting("warning_color", "Warning ping color (100-200ms)", 0xFFF3AB4B);
        this.badColor = new ColorSetting("bad_color", "Bad ping color (>200ms)", 0xFFE65B7A);
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

        displayGroup = createGroup("Colors", "Ping quality colors");
        displayGroup.addSettings(goodColor, warningColor, badColor, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Ping monitoring
    }
}