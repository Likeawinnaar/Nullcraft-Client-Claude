package com.nullcraft.client.modules.combat;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * AttackCooldownHUD - Shows attack cooldown indicator.
 */
public class AttackCooldownHUDModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showProgressBar;
    private final BooleanSetting showPercentage;
    private final BooleanSetting onlyWhenAttacking;
    private final ModeSetting style;
    private final ModeSetting position;
    private final ColorSetting readyColor;
    private final ColorSetting chargingColor;
    private final ColorSetting backgroundColor;

    public AttackCooldownHUDModule() {
        super("attack_cooldown_hud", "AttackCooldownHUD", "Shows attack cooldown indicator", ModuleCategory.COMBAT, "icon_attack_cooldown");

        this.enabled = new BooleanSetting("enabled", "Enable AttackCooldownHUD", true);
        this.showProgressBar = new BooleanSetting("show_progress_bar", "Show progress bar", true);
        this.showPercentage = new BooleanSetting("show_percentage", "Show percentage", false);
        this.onlyWhenAttacking = new BooleanSetting("only_when_attacking", "Only show when attacking", true);
        this.style = new ModeSetting("style", "Display style", "bar", "bar", "circle", "pie");
        this.position = new ModeSetting("position", "HUD position", "bottom_center", "top_left", "top_right", "bottom_left", "bottom_right", "bottom_center", "crosshair");
        this.readyColor = new ColorSetting("ready_color", "Ready color", 0xFF4CBF80);
        this.chargingColor = new ColorSetting("charging_color", "Charging color", 0xFFF3AB4B);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);

        registerSetting(enabled);
        registerSetting(showProgressBar);
        registerSetting(showPercentage);
        registerSetting(onlyWhenAttacking);
        registerSetting(style);
        registerSetting(position);
        registerSetting(readyColor);
        registerSetting(chargingColor);
        registerSetting(backgroundColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showProgressBar, showPercentage, onlyWhenAttacking, style, position);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(readyColor, chargingColor, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {}
}