package com.nullcraft.client.modules.misc;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * MemoryMonitor - Monitors JVM memory usage.
 */
public class MemoryMonitorModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showOnHUD;
    private final ModeSetting position;
    private final BooleanSetting showHeapUsed;
    private final BooleanSetting showHeapMax;
    private final BooleanSetting showNonHeap;
    private final BooleanSetting showGCCount;
    private final IntegerSetting updateInterval;
    private final ColorSetting goodColor;
    private final ColorSetting warningColor;
    private final ColorSetting badColor;
    private final ColorSetting backgroundColor;

    public MemoryMonitorModule() {
        super("memory_monitor", "MemoryMonitor", "Monitors JVM memory usage", ModuleCategory.MISC, "icon_memory_monitor");

        this.enabled = new BooleanSetting("enabled", "Enable MemoryMonitor", true);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", false);
        this.position = new ModeSetting("position", "HUD position", "bottom_right", "top_left", "top_right", "bottom_left", "bottom_right");
        this.showHeapUsed = new BooleanSetting("show_heap_used", "Show heap used", true);
        this.showHeapMax = new BooleanSetting("show_heap_max", "Show heap max", true);
        this.showNonHeap = new BooleanSetting("show_non_heap", "Show non-heap", false);
        this.showGCCount = new BooleanSetting("show_gc_count", "Show GC count", true);
        this.updateInterval = new IntegerSetting("update_interval", "Update interval (ticks)", 60, 10, 200);
        this.goodColor = new ColorSetting("good_color", "Good memory color (<70%)", 0xFF4CBF80);
        this.warningColor = new ColorSetting("warning_color", "Warning memory color (70-90%)", 0xFFF3AB4B);
        this.badColor = new ColorSetting("bad_color", "Bad memory color (>90%)", 0xFFE65B7A);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);

        registerSetting(enabled);
        registerSetting(showOnHUD);
        registerSetting(position);
        registerSetting(showHeapUsed);
        registerSetting(showHeapMax);
        registerSetting(showNonHeap);
        registerSetting(showGCCount);
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
        generalGroup.addSettings(enabled, showOnHUD, position, showHeapUsed, showHeapMax, showNonHeap, showGCCount, updateInterval);

        displayGroup = createGroup("Colors", "Memory usage colors");
        displayGroup.addSettings(goodColor, warningColor, badColor, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Memory monitoring
    }
}