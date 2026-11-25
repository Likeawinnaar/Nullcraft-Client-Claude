package com.nullcraft.client.modules.combat;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * DamageIndicator - Shows damage numbers when hitting entities.
 */
public class DamageIndicatorModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showCrit;
    private final BooleanSetting showArmorReduction;
    private final ModeSetting style;
    private final DoubleSetting duration;
    private final DoubleSetting scale;
    private final ColorSetting normalColor;
    private final ColorSetting critColor;

    public DamageIndicatorModule() {
        super("damage_indicator", "DamageIndicator", "Shows damage numbers when hitting entities", ModuleCategory.COMBAT, "icon_damage_indicator");

        this.enabled = new BooleanSetting("enabled", "Enable DamageIndicator", true);
        this.showCrit = new BooleanSetting("show_crit", "Show critical hit indicator", true);
        this.showArmorReduction = new BooleanSetting("show_armor_reduction", "Show armor reduction", false);
        this.style = new ModeSetting("style", "Display style", "numbers", "numbers", "particles", "both");
        this.duration = new DoubleSetting("duration", "Display duration (seconds)", 1.5, 0.5, 5.0, 0.1);
        this.scale = new DoubleSetting("scale", "Text scale", 1.0, 0.5, 2.0, 0.1);
        this.normalColor = new ColorSetting("normal_color", "Normal damage color", 0xFFFFFFFF);
        this.critColor = new ColorSetting("crit_color", "Critical hit color", 0xFFFFD700);

        registerSetting(enabled);
        registerSetting(showCrit);
        registerSetting(showArmorReduction);
        registerSetting(style);
        registerSetting(duration);
        registerSetting(scale);
        registerSetting(normalColor);
        registerSetting(critColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, style, duration, scale);

        displayGroup = createGroup("Display", "Display options");
        displayGroup.addSettings(showCrit, showArmorReduction, normalColor, critColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {}
}