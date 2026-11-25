package com.nullcraft.client.core.notification;

import com.nullcraft.client.core.utils.DesignTokens;

/**
 * Types of notifications with associated styling.
 */
public enum NotificationType {
    SUCCESS("Success", DesignTokens.COLOR_SUCCESS, "icon_success"),
    ERROR("Error", DesignTokens.COLOR_ERROR, "icon_error"),
    WARNING("Warning", DesignTokens.COLOR_WARNING, "icon_warning"),
    INFO("Information", DesignTokens.COLOR_SECONDARY_PURPLE, "icon_info"),
    MODULE_ENABLED("Module Enabled", DesignTokens.COLOR_PRIMARY_PURPLE, "icon_module_on"),
    MODULE_DISABLED("Module Disabled", DesignTokens.COLOR_MUTED_TEXT, "icon_module_off"),
    CONFIG_SAVED("Config Saved", DesignTokens.COLOR_SUCCESS, "icon_config"),
    CONFIG_LOADED("Config Loaded", DesignTokens.COLOR_PRIMARY_PURPLE, "icon_config");

    private final String defaultTitle;
    private final int accentColor;
    private final String iconId;

    NotificationType(String defaultTitle, int accentColor, String iconId) {
        this.defaultTitle = defaultTitle;
        this.accentColor = accentColor;
        this.iconId = iconId;
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public String getIconId() {
        return iconId;
    }
}