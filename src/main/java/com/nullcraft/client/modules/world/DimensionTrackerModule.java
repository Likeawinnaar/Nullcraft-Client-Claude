package com.nullcraft.client.modules.world;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * DimensionTracker - Tracks dimension changes and shows current dimension info.
 */
public class DimensionTrackerModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showDimensionName;
    private final BooleanSetting showDimensionId;
    private final BooleanSetting showPortalCoords;
    private final BooleanSetting logDimensionChanges;
    private final ModeSetting position;
    private final ColorSetting overworldColor;
    private final ColorSetting netherColor;
    private final ColorSetting endColor;
    private final ColorSetting backgroundColor;

    public DimensionTrackerModule() {
        super("dimension_tracker", "DimensionTracker", "Tracks dimension changes and shows current dimension info", ModuleCategory.WORLD, "icon_dimension_tracker");

        this.enabled = new BooleanSetting("enabled", "Enable DimensionTracker", true);
        this.showDimensionName = new BooleanSetting("show_dimension_name", "Show dimension name", true);
        this.showDimensionId = new BooleanSetting("show_dimension_id", "Show dimension ID", false);
        this.showPortalCoords = new BooleanSetting("show_portal_coords", "Show portal coordinates", false);
        this.logDimensionChanges = new BooleanSetting("log_dimension_changes", "Log dimension changes", true);
        this.position = new ModeSetting("position", "HUD position", "top_right", "top_left", "top_right", "bottom_left", "bottom_right");
        this.overworldColor = new ColorSetting("overworld_color", "Overworld color", 0xFF4CBF80);
        this.netherColor = new ColorSetting("nether_color", "Nether color", 0xFFE65B7A);
        this.endColor = new ColorSetting("end_color", "End color", 0xFFA78BFA);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);

        registerSetting(enabled);
        registerSetting(showDimensionName);
        registerSetting(showDimensionId);
        registerSetting(showPortalCoords);
        registerSetting(logDimensionChanges);
        registerSetting(position);
        registerSetting(overworldColor);
        registerSetting(netherColor);
        registerSetting(endColor);
        registerSetting(backgroundColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showDimensionName, showDimensionId, showPortalCoords, logDimensionChanges, position);

        displayGroup = createGroup("Colors", "Dimension colors");
        displayGroup.addSettings(overworldColor, netherColor, endColor, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Dimension tracking logic
    }
}