package com.nullcraft.client.modules.combat;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * TargetDistance - Shows distance to target.
 */
public class TargetDistanceModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showExact;
    private final BooleanSetting showBlocks;
    private final BooleanSetting showMeters;
    private final ModeSetting unit;
    private final IntegerSetting precision;
    private final ColorSetting color;

    public TargetDistanceModule() {
        super("target_distance", "TargetDistance", "Shows distance to target", ModuleCategory.COMBAT, "icon_target_distance");

        this.enabled = new BooleanSetting("enabled", "Enable TargetDistance", true);
        this.showExact = new BooleanSetting("show_exact", "Show exact distance", true);
        this.showBlocks = new BooleanSetting("show_blocks", "Show in blocks", true);
        this.showMeters = new BooleanSetting("show_meters", "Show in meters", false);
        this.unit = new ModeSetting("unit", "Preferred unit", "blocks", "blocks", "meters", "both");
        this.precision = new IntegerSetting("precision", "Decimal precision", 1, 0, 3);
        this.color = new ColorSetting("color", "Text color", 0xFFA78BFA);

        registerSetting(enabled);
        registerSetting(showExact);
        registerSetting(showBlocks);
        registerSetting(showMeters);
        registerSetting(unit);
        registerSetting(precision);
        registerSetting(color);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, unit, precision);

        displayGroup = createGroup("Display", "Display options");
        displayGroup.addSettings(showExact, showBlocks, showMeters, color);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {}
}