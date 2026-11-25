package com.nullcraft.client.modules.player;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * DurabilityAlert - Alerts when items are about to break.
 */
public class DurabilityAlertModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup alertsGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showOnHUD;
    private final BooleanSetting playSound;
    private final BooleanSetting sendChatMessage;
    private final IntegerSetting durabilityThreshold;
    private final IntegerSetting criticalThreshold;
    private final ModeSetting alertMode;
    private final ColorSetting warningColor;
    private final ColorSetting criticalColor;

    public DurabilityAlertModule() {
        super("durability_alert", "DurabilityAlert", "Alerts when items are about to break", ModuleCategory.PLAYER, "icon_durability_alert");

        this.enabled = new BooleanSetting("enabled", "Enable DurabilityAlert", true);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", true);
        this.playSound = new BooleanSetting("play_sound", "Play alert sound", true);
        this.sendChatMessage = new BooleanSetting("send_chat_message", "Send chat message", false);
        this.durabilityThreshold = new IntegerSetting("durability_threshold", "Warning threshold (%)", 25, 5, 50);
        this.criticalThreshold = new IntegerSetting("critical_threshold", "Critical threshold (%)", 10, 1, 25);
        this.alertMode = new ModeSetting("alert_mode", "Alert mode", "all", "all", "tools_only", "armor_only", "hotbar_only");
        this.warningColor = new ColorSetting("warning_color", "Warning color", 0xFFF3AB4B);
        this.criticalColor = new ColorSetting("critical_color", "Critical color", 0xFFE65B7A);

        registerSetting(enabled);
        registerSetting(showOnHUD);
        registerSetting(playSound);
        registerSetting(sendChatMessage);
        registerSetting(durabilityThreshold);
        registerSetting(criticalThreshold);
        registerSetting(alertMode);
        registerSetting(warningColor);
        registerSetting(criticalColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showOnHUD, playSound, sendChatMessage, alertMode);

        alertsGroup = createGroup("Thresholds", "Alert thresholds");
        alertsGroup.addSettings(durabilityThreshold, criticalThreshold, warningColor, criticalColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Durability checking logic
    }
}