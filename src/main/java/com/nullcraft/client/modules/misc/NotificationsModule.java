package com.nullcraft.client.modules.misc;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * Notifications - Custom notification settings.
 */
public class NotificationsModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showModuleToggles;
    private final BooleanSetting showConfigChanges;
    private final BooleanSetting showErrors;
    private final BooleanSetting showWarnings;
    private final IntegerSetting defaultDuration;
    private final ModeSetting position;
    private final IntegerSetting maxNotifications;
    private final BooleanSetting playSounds;

    public NotificationsModule() {
        super("notifications", "Notifications", "Custom notification settings", ModuleCategory.MISC, "icon_notifications");

        this.enabled = new BooleanSetting("enabled", "Enable Notifications", true);
        this.showModuleToggles = new BooleanSetting("show_module_toggles", "Show module toggle notifications", true);
        this.showConfigChanges = new BooleanSetting("show_config_changes", "Show config change notifications", true);
        this.showErrors = new BooleanSetting("show_errors", "Show error notifications", true);
        this.showWarnings = new BooleanSetting("show_warnings", "Show warning notifications", true);
        this.defaultDuration = new IntegerSetting("default_duration", "Default duration (ms)", 3000, 1000, 10000);
        this.position = new ModeSetting("position", "Notification position", "bottom_right", "top_left", "top_right", "bottom_left", "bottom_right", "top_center", "bottom_center");
        this.maxNotifications = new IntegerSetting("max_notifications", "Max simultaneous notifications", 5, 1, 20);
        this.playSounds = new BooleanSetting("play_sounds", "Play notification sounds", true);

        registerSetting(enabled);
        registerSetting(showModuleToggles);
        registerSetting(showConfigChanges);
        registerSetting(showErrors);
        registerSetting(showWarnings);
        registerSetting(defaultDuration);
        registerSetting(position);
        registerSetting(maxNotifications);
        registerSetting(playSounds);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showModuleToggles, showConfigChanges, showErrors, showWarnings, defaultDuration, position, maxNotifications, playSounds);

        displayGroup = createGroup("Display", "Display options");
        displayGroup.addSettings(); // No additional settings
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {}
}