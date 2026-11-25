package com.nullcraft.client.modules.misc;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * SessionStats - Tracks session statistics (playtime, blocks mined, etc.)
 */
public class SessionStatsModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showOnHUD;
    private final ModeSetting position;
    private final BooleanSetting showPlayTime;
    private final BooleanSetting showBlocksMined;
    private final BooleanSetting showBlocksPlaced;
    private final BooleanSetting showDistanceWalked;
    private final BooleanSetting showMobsKilled;
    private final BooleanSetting showDeaths;
    private final BooleanSetting resetOnDisconnect;

    public SessionStatsModule() {
        super("session_stats", "SessionStats", "Tracks session statistics (playtime, blocks mined, etc.)", ModuleCategory.MISC, "icon_session_stats");

        this.enabled = new BooleanSetting("enabled", "Enable SessionStats", true);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", false);
        this.position = new ModeSetting("position", "HUD position", "bottom_left", "top_left", "top_right", "bottom_left", "bottom_right", "bottom_center");
        this.showPlayTime = new BooleanSetting("show_play_time", "Show play time", true);
        this.showBlocksMined = new BooleanSetting("show_blocks_mined", "Show blocks mined", true);
        this.showBlocksPlaced = new BooleanSetting("show_blocks_placed", "Show blocks placed", true);
        this.showDistanceWalked = new BooleanSetting("show_distance_walked", "Show distance walked", true);
        this.showMobsKilled = new BooleanSetting("show_mobs_killed", "Show mobs killed", true);
        this.showDeaths = new BooleanSetting("show_deaths", "Show deaths", true);
        this.resetOnDisconnect = new BooleanSetting("reset_on_disconnect", "Reset on disconnect", true);

        registerSetting(enabled);
        registerSetting(showOnHUD);
        registerSetting(position);
        registerSetting(showPlayTime);
        registerSetting(showBlocksMined);
        registerSetting(showBlocksPlaced);
        registerSetting(showDistanceWalked);
        registerSetting(showMobsKilled);
        registerSetting(showDeaths);
        registerSetting(resetOnDisconnect);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showOnHUD, position, resetOnDisconnect);

        displayGroup = createGroup("Statistics", "Statistics to show");
        displayGroup.addSettings(showPlayTime, showBlocksMined, showBlocksPlaced, showDistanceWalked, showMobsKilled, showDeaths);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Session stats tracking
    }
}