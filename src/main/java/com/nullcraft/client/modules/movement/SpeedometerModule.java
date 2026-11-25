package com.nullcraft.client.modules.movement;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * Speedometer - Shows current movement speed.
 */
public class SpeedometerModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showBlocksPerSec;
    private final BooleanSetting showMetersPerSec;
    private final BooleanSetting showKmPerHour;
    private final BooleanSetting showHorizontalOnly;
    private final ModeSetting position;
    private final ColorSetting color;
    private final ColorSetting backgroundColor;
    private final DoubleSetting updateInterval;

    public SpeedometerModule() {
        super("speedometer", "Speedometer", "Shows current movement speed", ModuleCategory.MOVEMENT, "icon_speedometer");

        this.enabled = new BooleanSetting("enabled", "Enable Speedometer", true);
        this.showBlocksPerSec = new BooleanSetting("show_bps", "Show blocks/second", true);
        this.showMetersPerSec = new BooleanSetting("show_mps", "Show meters/second", false);
        this.showKmPerHour = new BooleanSetting("show_kmh", "Show km/h", false);
        this.showHorizontalOnly = new BooleanSetting("show_horizontal", "Horizontal speed only", false);
        this.position = new ModeSetting("position", "HUD position", "bottom_right", "top_left", "top_right", "bottom_left", "bottom_right", "center");
        this.color = new ColorSetting("color", "Text color", 0xFFA78BFA);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);
        this.updateInterval = new DoubleSetting("update_interval", "Update interval (seconds)", 0.1, 0.05, 1.0, 0.05);

        registerSetting(enabled);
        registerSetting(showBlocksPerSec);
        registerSetting(showMetersPerSec);
        registerSetting(showKmPerHour);
        registerSetting(showHorizontalOnly);
        registerSetting(position);
        registerSetting(color);
        registerSetting(backgroundColor);
        registerSetting(updateInterval);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showBlocksPerSec, showMetersPerSec, showKmPerHour, showHorizontalOnly, position, updateInterval);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(color, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Speed calculation logic
    }

    @Override
    public void onRender(float tickDelta) {
        // Rendering handled by HudManager
    }
}