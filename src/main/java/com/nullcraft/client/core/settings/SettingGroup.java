package com.nullcraft.client.core.settings;

import java.util.ArrayList;
import java.util.List;

/**
 * A group of settings for UI organization in the Settings Inspector.
 * SettingGroups are used to create tabs/sections in the module settings panel.
 */
public final class SettingGroup {

    private final String name;
    private final String description;
    private final List<Setting<?>> settings = new ArrayList<>();
    private final String iconId;

    public SettingGroup(String name) {
        this(name, "", "");
    }

    public SettingGroup(String name, String description) {
        this(name, description, "");
    }

    public SettingGroup(String name, String description, String iconId) {
        this.name = name;
        this.description = description;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconId() {
        return iconId;
    }

    public void addSetting(Setting<?> setting) {
        settings.add(setting);
    }

    public void addSettings(Setting<?>... settings) {
        for (Setting<?> setting : settings) {
            this.settings.add(setting);
        }
    }

    public List<Setting<?>> getSettings() {
        return List.copyOf(settings);
    }

    public int getSettingCount() {
        return settings.size();
    }

    @Override
    public String toString() {
        return "SettingGroup{" + name + " (" + settings.size() + " settings)}";
    }
}