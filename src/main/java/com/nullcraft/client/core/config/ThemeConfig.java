package com.nullcraft.client.core.config;

import com.google.gson.JsonObject;
import com.nullcraft.client.gui.themeeditor.Theme;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Theme configuration manager for theme persistence.
 */
public class ThemeConfig {

    private final ConfigManager configManager;
    private final File themeDir;
    private final Map<String, Theme> themes = new HashMap<>();

    public ThemeConfig(ConfigManager configManager) {
        this.configManager = configManager;
        this.themeDir = new File(configManager.getConfigDir(), "themes");
        if (!themeDir.exists()) {
            themeDir.mkdirs();
        }
        loadAllThemes();
        ensureBuiltInThemes();
    }

    private void loadAllThemes() {
        File[] files = themeDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                String name = file.getName().replace(".json", "");
                JsonObject json = configManager.loadJson(file);
                if (json != null) {
                    Theme theme = Theme.fromJson(json);
                    themes.put(name, theme);
                }
            }
        }
    }

    private void ensureBuiltInThemes() {
        for (Theme.ThemeType type : Theme.ThemeType.values()) {
            if (type == Theme.ThemeType.CUSTOM) continue;
            String name = type.name().toLowerCase();
            if (!themes.containsKey(name)) {
                Theme theme = new Theme(name, type);
                themes.put(name, theme);
                saveTheme(name, theme);
            }
        }
    }

    public void saveTheme(String name, Theme theme) {
        themes.put(name, theme);
        File file = new File(themeDir, name + ".json");
        configManager.saveJson(file, theme.toJson());
    }

    public Theme loadTheme(String name) {
        return themes.get(name);
    }

    public void deleteTheme(String name) {
        if (Theme.ThemeType.valueOf(name.toUpperCase()) != Theme.ThemeType.CUSTOM) {
            // Don't delete built-in themes
            return;
        }
        themes.remove(name);
        File file = new File(themeDir, name + ".json");
        if (file.exists()) {
            file.delete();
        }
    }

    public List<String> getThemeNames() {
        return List.copyOf(themes.keySet());
    }

    public boolean hasTheme(String name) {
        return themes.containsKey(name);
    }

    public Theme getActiveTheme() {
        return themes.getOrDefault("custom", themes.get("default"));
    }

    public void setActiveTheme(String name) {
        // Would save active theme preference
    }
}