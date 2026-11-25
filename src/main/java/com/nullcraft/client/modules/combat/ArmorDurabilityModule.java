package com.nullcraft.client.modules.combat;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * ArmorDurability - Shows armor durability on HUD.
 */
public class ArmorDurabilityModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showPercent;
    private final BooleanSetting showIcons;
    private final BooleanSetting showBrokenAlert;
    private final ModeSetting position;
    private final IntegerSetting alertThreshold;
    private final ColorSetting highColor;
    private final ColorSetting mediumColor;
    private final ColorSetting lowColor;
    private final ColorSetting brokenColor;

    public ArmorDurabilityModule() {
        super("armor_durability", "ArmorDurability", "Shows armor durability on HUD", ModuleCategory.COMBAT, "icon_armor_durability");

        this.enabled = new BooleanSetting("enabled", "Enable ArmorDurability", true);
        this.showPercent = new BooleanSetting("show_percent", "Show durability percent", true);
        this.showIcons = new BooleanSetting("show_icons", "Show armor icons", true);
        this.showBrokenAlert = new BooleanSetting("show_broken_alert", "Alert when armor breaks", true);
        this.position = new ModeSetting("position", "HUD position", "bottom_right", "top_left", "top_right", "bottom_left", "bottom_right", "center");
        this.alertThreshold = new IntegerSetting("alert_threshold", "Low durability alert (%)", 25, 5, 50);
        this.highColor = new ColorSetting("high_color", "High durability color", 0xFF4CBF80);
        this.mediumColor = new ColorSetting("medium_color", "Medium durability color", 0xFFF3AB4B);
        this.lowColor = new ColorSetting("low_color", "Low durability color", 0xFFE65B7A);
        this.brokenColor = new ColorSetting("broken_color", "Broken color", 0xFF77798C);

        registerSetting(enabled);
        registerSetting(showPercent);
        registerSetting(showIcons);
        registerSetting(showBrokenAlert);
        registerSetting(position);
        registerSetting(alertThreshold);
        registerSetting(highColor);
        registerSetting(mediumColor);
        registerSetting(lowColor);
        registerSetting(brokenColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showPercent, showIcons, showBrokenAlert, position);

        displayGroup = createGroup("Colors", "Durability colors");
        displayGroup.addSettings(alertThreshold, highColor, mediumColor, lowColor, brokenColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Check armor durability for alerts
    }
}