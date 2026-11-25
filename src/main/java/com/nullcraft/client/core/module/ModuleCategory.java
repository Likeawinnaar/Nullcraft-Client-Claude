package com.nullcraft.client.core.module;

import com.nullcraft.client.core.utils.DesignTokens;

/**
 * Categories for organizing modules in the ClickGUI sidebar.
 * Each category has an identifier, display name, and outline icon identifier.
 */
public enum ModuleCategory {
    // Gameplay categories
    COMBAT("combat", "Combat", "icon_combat", DesignTokens.COLOR_PRIMARY_PURPLE),
    MOVEMENT("movement", "Movement", "icon_movement", DesignTokens.COLOR_PRIMARY_PURPLE),
    PLAYER("player", "Player", "icon_player", DesignTokens.COLOR_PRIMARY_PURPLE),
    RENDER("render", "Render", "icon_render", DesignTokens.COLOR_PRIMARY_PURPLE),
    WORLD("world", "World", "icon_world", DesignTokens.COLOR_PRIMARY_PURPLE),
    DIAGNOSTICS("diagnostics", "Diagnostics", "icon_diagnostics", DesignTokens.COLOR_PRIMARY_PURPLE),
    MISC("misc", "Misc", "icon_misc", DesignTokens.COLOR_PRIMARY_PURPLE),

    // Utility categories (separator below)
    HUD("hud", "HUD", "icon_hud", DesignTokens.COLOR_SECONDARY_PURPLE),
    CONFIGS("configs", "Configs", "icon_configs", DesignTokens.COLOR_SECONDARY_PURPLE),
    THEMES("themes", "Themes", "icon_themes", DesignTokens.COLOR_SECONDARY_PURPLE),
    SETTINGS("settings", "Settings", "icon_settings", DesignTokens.COLOR_SECONDARY_PURPLE);

    private final String id;
    private final String displayName;
    private final String iconId;
    private final int accentColor;

    ModuleCategory(String id, String displayName, String iconId, int accentColor) {
        this.id = id;
        this.displayName = displayName;
        this.iconId = iconId;
        this.accentColor = accentColor;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIconId() {
        return iconId;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public boolean isGameplayCategory() {
        return this.ordinal() < HUD.ordinal();
    }

    public boolean isUtilityCategory() {
        return !isGameplayCategory();
    }

    public static ModuleCategory fromId(String id) {
        for (ModuleCategory category : values()) {
            if (category.id.equalsIgnoreCase(id)) {
                return category;
            }
        }
        return MISC;
    }
}