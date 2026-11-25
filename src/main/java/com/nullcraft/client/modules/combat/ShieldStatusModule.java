package com.nullcraft.client.modules.combat;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * ShieldStatus - Shows shield status and durability.
 */
public class ShieldStatusModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showDurability;
    private final BooleanSetting showCooldown;
    private final BooleanSetting showBlockingIndicator;
    private final ModeSetting position;
    private final IntegerSetting lowDurabilityThreshold;
    private final ColorSetting normalColor;
    private final ColorSetting lowColor;
    private final ColorSetting blockingColor;
    private final ColorSetting cooldownColor;

    public ShieldStatusModule() {
        super("shield_status", "ShieldStatus", "Shows shield status and durability", ModuleCategory.COMBAT, "icon_shield_status");

        this.enabled = new BooleanSetting("enabled", "Enable ShieldStatus", true);
        this.showDurability = new BooleanSetting("show_durability", "Show durability", true);
        this.showCooldown = new BooleanSetting("show_cooldown", "Show cooldown", true);
        this.showBlockingIndicator = new BooleanSetting("show_blocking_indicator", "Show blocking indicator", true);
        this.position = new ModeSetting("position", "HUD position", "bottom_left", "top_left", "top_right", "bottom_left", "bottom_right");
        this.lowDurabilityThreshold = new IntegerSetting("low_durability_threshold", "Low durability threshold", 100, 10, 500);
        this.normalColor = new ColorSetting("normal_color", "Normal color", 0xFF4CBF80);
        this.lowColor = new ColorSetting("low_color", "Low durability color", 0xFFE65B7A);
        this.blockingColor = new ColorSetting("blocking_color", "Blocking color", 0xFF8B5CF6);
        this.cooldownColor = new ColorSetting("cooldown_color", "Cooldown color", 0xFFF3AB4B);

        registerSetting(enabled);
        registerSetting(showDurability);
        registerSetting(showCooldown);
        registerSetting(showBlockingIndicator);
        registerSetting(position);
        registerSetting(lowDurabilityThreshold);
        registerSetting(normalColor);
        registerSetting(lowColor);
        registerSetting(blockingColor);
        registerSetting(cooldownColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showDurability, showCooldown, showBlockingIndicator, position);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(lowDurabilityThreshold, normalColor, lowColor, blockingColor, cooldownColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {}
}