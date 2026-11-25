package com.nullcraft.client.modules.diagnostics;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * ChunkLoadMonitor - Monitors chunk loading/unloading.
 */
public class ChunkLoadMonitorModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showLoadedChunks;
    private final BooleanSetting showChunkQueue;
    private final BooleanSetting showLoadTimes;
    private final BooleanSetting showOnHUD;
    private final ModeSetting position;
    private final IntegerSetting updateInterval;

    public ChunkLoadMonitorModule() {
        super("chunk_load_monitor", "ChunkLoadMonitor", "Monitors chunk loading/unloading", ModuleCategory.DIAGNOSTICS, "icon_chunk_load_monitor");

        this.enabled = new BooleanSetting("enabled", "Enable ChunkLoadMonitor", false);
        this.showLoadedChunks = new BooleanSetting("show_loaded_chunks", "Show loaded chunks count", true);
        this.showChunkQueue = new BooleanSetting("show_chunk_queue", "Show chunk queue size", true);
        this.showLoadTimes = new BooleanSetting("show_load_times", "Show chunk load times", false);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", false);
        this.position = new ModeSetting("position", "HUD position", "bottom_right", "top_left", "top_right", "bottom_left", "bottom_right");
        this.updateInterval = new IntegerSetting("update_interval", "Update interval (ticks)", 20, 1, 100);

        registerSetting(enabled);
        registerSetting(showLoadedChunks);
        registerSetting(showChunkQueue);
        registerSetting(showLoadTimes);
        registerSetting(showOnHUD);
        registerSetting(position);
        registerSetting(updateInterval);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showLoadedChunks, showChunkQueue, showLoadTimes, showOnHUD, position, updateInterval);

        displayGroup = createGroup("Display", "Display options");
        displayGroup.addSettings(); // No additional settings
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Chunk monitoring logic
    }
}