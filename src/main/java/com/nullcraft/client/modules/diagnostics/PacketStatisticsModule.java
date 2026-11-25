package com.nullcraft.client.modules.diagnostics;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * PacketStatistics - Shows packet statistics (bandwidth, packet rates, etc.)
 */
public class PacketStatisticsModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showIncomingRate;
    private final BooleanSetting showOutgoingRate;
    private final BooleanSetting showTotalBandwidth;
    private final BooleanSetting showPacketCounts;
    private final BooleanSetting showOnHUD;
    private final ModeSetting position;
    private final IntegerSetting updateInterval;
    private final ColorSetting incomingColor;
    private final ColorSetting outgoingColor;
    private final ColorSetting backgroundColor;

    public PacketStatisticsModule() {
        super("packet_statistics", "PacketStatistics", "Shows packet statistics (bandwidth, packet rates)", ModuleCategory.DIAGNOSTICS, "icon_packet_statistics");

        this.enabled = new BooleanSetting("enabled", "Enable PacketStatistics", true);
        this.showIncomingRate = new BooleanSetting("show_incoming_rate", "Show incoming packet rate", true);
        this.showOutgoingRate = new BooleanSetting("show_outgoing_rate", "Show outgoing packet rate", true);
        this.showTotalBandwidth = new BooleanSetting("show_total_bandwidth", "Show total bandwidth", true);
        this.showPacketCounts = new BooleanSetting("show_packet_counts", "Show packet counts", true);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", false);
        this.position = new ModeSetting("position", "HUD position", "top_right", "top_left", "top_right", "bottom_left", "bottom_right");
        this.updateInterval = new IntegerSetting("update_interval", "Update interval (ticks)", 20, 1, 100);
        this.incomingColor = new ColorSetting("incoming_color", "Incoming color", 0xFF4CBF80);
        this.outgoingColor = new ColorSetting("outgoing_color", "Outgoing color", 0xFFE65B7A);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);

        registerSetting(enabled);
        registerSetting(showIncomingRate);
        registerSetting(showOutgoingRate);
        registerSetting(showTotalBandwidth);
        registerSetting(showPacketCounts);
        registerSetting(showOnHUD);
        registerSetting(position);
        registerSetting(updateInterval);
        registerSetting(incomingColor);
        registerSetting(outgoingColor);
        registerSetting(backgroundColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showIncomingRate, showOutgoingRate, showTotalBandwidth, showPacketCounts, showOnHUD, position, updateInterval);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(incomingColor, outgoingColor, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Packet statistics collection
    }
}