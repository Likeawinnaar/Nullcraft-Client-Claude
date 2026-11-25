package com.nullcraft.client.modules.misc;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * TPSMonitor - Monitors server TPS (ticks per second).
 */
public class TPSMonitorModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showOnHUD;
    private final ModeSetting position;
    private final BooleanSetting showMSPT;
    private final IntegerSetting updateInterval;
    private final ColorSetting goodColor;
    private final ColorSetting warningColor;
    private final ColorSetting badColor;
    private final ColorSetting backgroundColor;

    public TPSMonitorModule() {
        super("tps_monitor", "TPSMonitor", "Monitors server TPS (ticks per second)", ModuleCategory.MISC, "icon_tps_monitor");

        this.enabled = new BooleanSetting("enabled", "Enable TPSMonitor", true);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", true);
        this.position = new ModeSetting("position", "HUD position", "top_right", "top_left", "top_right", "bottom_left", "bottom_right");
        this.showMSPT = new BooleanSetting("show_mspt", "Show MSPT (ms per tick)", true);
        this.updateInterval = new IntegerSetting("update_interval", "Update interval (ticks)", 20, 5, 100);
        this.goodColor = new ColorSetting("good_color", "Good TPS color (>18)", 0xFF4CBF80);
        this.warningColor = new ColorSetting("warning_color", "Warning TPS color (10-18)", 0xFFF3AB4B);
        this.badColor = new ColorSetting("bad_color", "Bad TPS color (<10)", 0xFFE65B7A);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);

        registerSetting(enabled);
        registerSetting(showOnHUD);
        registerSetting(position);
        registerSetting(showMSPT);
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
        generalGroup.addSettings(enabled, showOnHUD, position, showMSPT, updateInterval);

        displayGroup = createGroup("Colors", "TPS quality colors");
        displayGroup.addSettings(goodColor, warningColor, badColor, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // TPS monitoring
    }
}