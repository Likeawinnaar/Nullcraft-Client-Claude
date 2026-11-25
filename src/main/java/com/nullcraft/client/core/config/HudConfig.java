package com.nullcraft.client.core.config;

import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HUD configuration manager for layout persistence.
 */
public class HudConfig {

    private final ConfigManager configManager;
    private final File hudDir;
    private final Map<String, JsonObject> layouts = new HashMap<>();

    public HudConfig(ConfigManager configManager) {
        this.configManager = configManager;
        this.hudDir = new File(configManager.getConfigDir(), "hud");
        if (!hudDir.exists()) {
            hudDir.mkdirs();
        }
        loadAllLayouts();
    }

    private void loadAllLayouts() {
        File[] files = hudDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                String name = file.getName().replace(".json", "");
                JsonObject json = configManager.loadJson(file);
                if (json != null) {
                    layouts.put(name, json);
                }
            }
        }
    }

    public void saveLayout(String name, JsonObject layout) {
        layouts.put(name, layout);
        File file = new File(hudDir, name + ".json");
        configManager.saveJson(file, layout);
    }

    public JsonObject loadLayout(String name) {
        return layouts.get(name);
    }

    public void deleteLayout(String name) {
        layouts.remove(name);
        File file = new File(hudDir, name + ".json");
        if (file.exists()) {
            file.delete();
        }
    }

    public List<String> getLayoutNames() {
        return List.copyOf(layouts.keySet());
    }

    public boolean hasLayout(String name) {
        return layouts.containsKey(name);
    }
}