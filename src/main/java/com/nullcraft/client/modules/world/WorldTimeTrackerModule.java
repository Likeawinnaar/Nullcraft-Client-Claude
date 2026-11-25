package com.nullcraft.client.modules.world;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * WorldTimeTracker - Tracks and displays world time.
 */
public class WorldTimeTrackerModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showTimeOfDay;
    private final BooleanSetting showDayCount;
    private final BooleanSetting showMoonPhase;
    private final BooleanSetting showTickTime;
    private final ModeSetting timeFormat;
    private final ModeSetting position;
    private final ColorSetting dayColor;
    private final ColorSetting nightColor;
    private final ColorSetting backgroundColor;

    public WorldTimeTrackerModule() {
        super("world_time_tracker", "WorldTimeTracker", "Tracks and displays world time", ModuleCategory.WORLD, "icon_world_time_tracker");

        this.enabled = new BooleanSetting("enabled", "Enable WorldTimeTracker", true);
        this.showTimeOfDay = new BooleanSetting("show_time_of_day", "Show time of day", true);
        this.showDayCount = new BooleanSetting("show_day_count", "Show day count", true);
        this.showMoonPhase = new BooleanSetting("show_moon_phase", "Show moon phase", false);
        this.showTickTime = new BooleanSetting("show_tick_time", "Show tick time", false);
        this.timeFormat = new ModeSetting("time_format", "Time format", "24h", "24h", "12h", "ticks");
        this.position = new ModeSetting("position", "HUD position", "top_center", "top_left", "top_center", "top_right", "bottom_left", "bottom_center", "bottom_right");
        this.dayColor = new ColorSetting("day_color", "Day color", 0xFFF3AB4B);
        this.nightColor = new ColorSetting("night_color", "Night color", 0xFF8B5CF6);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);

        registerSetting(enabled);
        registerSetting(showTimeOfDay);
        registerSetting(showDayCount);
        registerSetting(showMoonPhase);
        registerSetting(showTickTime);
        registerSetting(timeFormat);
        registerSetting(position);
        registerSetting(dayColor);
        registerSetting(nightColor);
        registerSetting(backgroundColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showTimeOfDay, showDayCount, showMoonPhase, showTickTime, timeFormat, position);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(dayColor, nightColor, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Time rendering
    }
}