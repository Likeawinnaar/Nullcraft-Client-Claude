package com.nullcraft.client.modules.combat;

import com.google.gson.JsonObject;
import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * TargetHUD - Displays information about the current combat target.
 */
public class TargetHUDModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showAvatar;
    private final BooleanSetting showHealthBar;
    private final BooleanSetting showDistance;
    private final BooleanSetting showArmor;
    private final BooleanSetting showPotionEffects;
    private final ModeSetting position;
    private final DoubleSetting scale;
    private final ColorSetting healthColor;
    private final ColorSetting backgroundColor;
    private final IntegerSetting maxDistance;

    public TargetHUDModule() {
        super("target_hud", "TargetHUD", "Displays information about your current combat target", ModuleCategory.COMBAT, "icon_target_hud");

        // Settings
        this.enabled = new BooleanSetting("enabled", "Enable TargetHUD", true);
        this.showAvatar = new BooleanSetting("show_avatar", "Show player avatar", true);
        this.showHealthBar = new BooleanSetting("show_health_bar", "Show health bar", true);
        this.showDistance = new BooleanSetting("show_distance", "Show distance", true);
        this.showArmor = new BooleanSetting("show_armor", "Show armor", true);
        this.showPotionEffects = new BooleanSetting("show_potion_effects", "Show potion effects", false);
        this.position = new ModeSetting("position", "HUD position", "top_right", "top_left", "top_right", "bottom_left", "bottom_right", "center");
        this.scale = new DoubleSetting("scale", "HUD scale", 1.0, 0.5, 2.0, 0.1);
        this.healthColor = new ColorSetting("health_color", "Health bar color", 0xFF4CBF80);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);
        this.maxDistance = new IntegerSetting("max_distance", "Max render distance", 100, 10, 500);

        registerSetting(enabled);
        registerSetting(showAvatar);
        registerSetting(showHealthBar);
        registerSetting(showDistance);
        registerSetting(showArmor);
        registerSetting(showPotionEffects);
        registerSetting(position);
        registerSetting(scale);
        registerSetting(healthColor);
        registerSetting(backgroundColor);
        registerSetting(maxDistance);
    }

    @Override
    protected void registerSettings() {
        // Settings registered in constructor
    }

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General TargetHUD settings");
        generalGroup.addSettings(enabled, maxDistance);

        displayGroup = createGroup("Display", "Display options");
        displayGroup.addSettings(showAvatar, showHealthBar, showDistance, showArmor, showPotionEffects);
        displayGroup.addSettings(position, scale, healthColor, backgroundColor);
    }

    @Override
    public void onEnable() {
        // TargetHUD is primarily a render module - rendering happens in onRender
    }

    @Override
    public void onDisable() {
        // Cleanup if needed
    }

    @Override
    public void onRender(float tickDelta) {
        // Rendering is handled by HudManager
        // This module just provides settings
    }
}