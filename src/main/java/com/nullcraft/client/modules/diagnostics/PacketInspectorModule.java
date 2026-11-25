package com.nullcraft.client.modules.diagnostics;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * PacketInspector - Inspects incoming/outgoing packets.
 */
public class PacketInspectorModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup filtersGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showIncoming;
    private final BooleanSetting showOutgoing;
    private final BooleanSetting showPacketSize;
    private final BooleanSetting logToFile;
    private final MultiSelectSetting packetTypes;
    private final IntegerSetting maxLogEntries;
    private final ModeSetting displayMode;

    public PacketInspectorModule() {
        super("packet_inspector", "PacketInspector", "Inspects incoming/outgoing packets", ModuleCategory.DIAGNOSTICS, "icon_packet_inspector");

        this.enabled = new BooleanSetting("enabled", "Enable PacketInspector", false);
        this.showIncoming = new BooleanSetting("show_incoming", "Show incoming packets", true);
        this.showOutgoing = new BooleanSetting("show_outgoing", "Show outgoing packets", true);
        this.showPacketSize = new BooleanSetting("show_packet_size", "Show packet size", true);
        this.logToFile = new BooleanSetting("log_to_file", "Log to file", false);
        this.packetTypes = new MultiSelectSetting("packet_types", "Packet types to monitor", Set.of("all"), "all", "play", "login", "status", "custom");
        this.maxLogEntries = new IntegerSetting("max_log_entries", "Max log entries", 1000, 100, 10000);
        this.displayMode = new ModeSetting("display_mode", "Display mode", "list", "list", "timeline", "stats");

        registerSetting(enabled);
        registerSetting(showIncoming);
        registerSetting(showOutgoing);
        registerSetting(showPacketSize);
        registerSetting(logToFile);
        registerSetting(packetTypes);
        registerSetting(maxLogEntries);
        registerSetting(displayMode);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showIncoming, showOutgoing, showPacketSize, logToFile, maxLogEntries, displayMode);

        filtersGroup = createGroup("Filters", "Packet filters");
        filtersGroup.addSettings(packetTypes);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Packet inspection logic
    }
}