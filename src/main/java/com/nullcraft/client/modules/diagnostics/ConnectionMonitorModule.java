package com.nullcraft.client.modules.diagnostics;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * ConnectionMonitor - Monitors connection health (ping, packet loss, etc.)
 */
public class ConnectionMonitorModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showPing;
    private final BooleanSetting showPacketLoss;
    private final BooleanSetting showJitter;
    private final BooleanSetting showConnectionState;
    private final BooleanSetting showOnHUD;
    private final ModeSetting position;
    private final IntegerSetting pingTimeout;
    private final ColorSetting goodColor;
    private final ColorSetting warningColor;
    private final ColorSetting badColor;
    private final ColorSetting backgroundColor;

    public ConnectionMonitorModule() {
        super("connection_monitor", "ConnectionMonitor", "Monitors connection health (ping, packet loss, jitter)", ModuleCategory.DIAGNOSTICS, "icon_connection_monitor");

        this.enabled = new BooleanSetting("enabled", "Enable ConnectionMonitor", true);
        this.showPing = new BooleanSetting("show_ping", "Show ping", true);
        this.showPacketLoss = new BooleanSetting("show_packet_loss", "Show packet loss", true);
        this.showJitter = new BooleanSetting("show_jitter", "Show jitter", true);
        this.showConnectionState = new BooleanSetting("show_connection_state", "Show connection state", true);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", false);
        this.position = new ModeSetting("position", "HUD position", "top_right", "top_left", "top_right", "bottom_left", "bottom_right");
        this.pingTimeout = new IntegerSetting("ping_timeout", "Ping timeout (ms)", 5000, 1000, 30000);
        this.goodColor = new ColorSetting("good_color", "Good connection color", 0xFF4CBF80);
        this.warningColor = new ColorSetting("warning_color", "Warning color", 0xFFF3AB4B);
        this.badColor = new ColorSetting("bad_color", "Bad connection color", 0xFFE65B7A);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);

        registerSetting(enabled);
        registerSetting(showPing);
        registerSetting(showPacketLoss);
        registerSetting(showJitter);
        registerSetting(showConnectionState);
        registerSetting(showOnHUD);
        registerSetting(position);
        registerSetting(pingTimeout);
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
        generalGroup.addSettings(enabled, showPing, showPacketLoss, showJitter, showConnectionState, showOnHUD, position, pingTimeout);

        displayGroup = createGroup("Colors", "Connection quality colors");
        displayGroup.addSettings(goodColor, warningColor, badColor, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Connection monitoring
    }
}