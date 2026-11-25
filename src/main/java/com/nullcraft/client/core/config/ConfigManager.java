package com.nullcraft.client.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleRegistry;
import com.nullcraft.client.core.keybind.KeybindManager;
import com.nullcraft.client.core.notification.NotificationManager;
import com.nullcraft.client.core.notification.NotificationType;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Manages configuration persistence for NullCraft.
 * Handles module states, settings, keybinds, HUD layouts, themes, and profiles.
 */
public final class ConfigManager {

    private static final ConfigManager INSTANCE = new ConfigManager();

    private final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create();

    private final Path configDir;
    private final Path configsDir;
    private final Path backupsDir;

    private final HudConfig hudConfig;
    private final ThemeConfig themeConfig;

    private String activeConfig = "default";
    private boolean autoSave = true;
    private long lastAutoSave = 0;
    private static final long AUTO_SAVE_INTERVAL = 30_000; // 30 seconds

    private ConfigManager() {
        // Determine config directory
        String mcDir = System.getProperty("fabric.mc.dir", System.getProperty("user.dir"));
        this.configDir = Paths.get(mcDir, "config", "nullcraft");
        this.configsDir = configDir.resolve("configs");
        this.backupsDir = configDir.resolve("backups");

        this.hudConfig = new HudConfig(this);
        this.themeConfig = new ThemeConfig(this);

        createDirectories();
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public HudConfig getHudConfig() {
        return hudConfig;
    }

    public ThemeConfig getThemeConfig() {
        return themeConfig;
    }

    // ============================================================
    // INITIALIZATION
    // ============================================================

    private void createDirectories() {
        try {
            Files.createDirectories(configDir);
            Files.createDirectories(configsDir);
            Files.createDirectories(backupsDir);
        } catch (IOException e) {
            System.err.println("[NullCraft] Failed to create config directories: " + e.getMessage());
        }
    }

    // ============================================================
    // CONFIG MANAGEMENT
    // ============================================================

    /**
     * Gets the list of available config names.
     */
    public List<String> getConfigs() {
        List<String> configs = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(configsDir, "*.json")) {
            for (Path path : stream) {
                String name = path.getFileName().toString();
                configs.add(name.substring(0, name.length() - 5)); // Remove .json
            }
        } catch (IOException e) {
            System.err.println("[NullCraft] Failed to list configs: " + e.getMessage());
        }
        configs.sort(String::compareToIgnoreCase);
        return configs;
    }

    /**
     * Checks if a config exists.
     */
    public boolean hasConfig(String name) {
        return Files.exists(configsDir.resolve(name + ".json"));
    }

    /**
     * Gets the active config name.
     */
    public String getActiveConfig() {
        return activeConfig;
    }

    /**
     * Sets the active config (does not load it).
     */
    public void setActiveConfig(String name) {
        this.activeConfig = name;
    }

    // ============================================================
    // SAVE / LOAD
    // ============================================================

    /**
     * Saves the current state to the active config.
     */
    public CompletableFuture<Boolean> save() {
        return save(activeConfig);
    }

    /**
     * Saves the current state to a named config.
     */
    public CompletableFuture<Boolean> save(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                JsonObject root = new JsonObject();
                root.addProperty("name", name);
                root.addProperty("version", "1.0.0");
                root.addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

                // Modules
                JsonObject modulesJson = new JsonObject();
                ModuleRegistry registry = ModuleRegistry.getInstance();
                for (Module module : registry.getAll()) {
                    modulesJson.add(module.getId(), module.toJson());
                }
                root.add("modules", modulesJson);

                // Keybinds
                root.add("keybinds", KeybindManager.getInstance().toJson());

                // HUD Layouts (placeholder for now)
                root.add("hudLayouts", new JsonObject());

                // Theme (placeholder)
                root.add("theme", new JsonObject());

                // Write atomically
                Path target = configsDir.resolve(name + ".json");
                Path temp = configsDir.resolve(name + ".json.tmp");

                try (Writer writer = Files.newBufferedWriter(temp)) {
                    gson.toJson(root, writer);
                }

                Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);

                NotificationManager.getInstance().success(
                    "Config Saved",
                    "Configuration '" + name + "' saved successfully",
                    2000
                );

                return true;
            } catch (Exception e) {
                NotificationManager.getInstance().error(
                    "Config Save Failed",
                    "Failed to save '" + name + "': " + e.getMessage(),
                    5000
                );
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Loads a config by name and applies it.
     */
    public CompletableFuture<Boolean> load(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Path source = configsDir.resolve(name + ".json");
                if (!Files.exists(source)) {
                    NotificationManager.getInstance().error(
                        "Config Not Found",
                        "Configuration '" + name + "' does not exist",
                        3000
                    );
                    return false;
                }

                String json = Files.readString(source);
                JsonObject root = gson.fromJson(json, JsonObject.class);

                // Load modules
                if (root.has("modules")) {
                    JsonObject modulesJson = root.getAsJsonObject("modules");
                    ModuleRegistry registry = ModuleRegistry.getInstance();
                    for (Module module : registry.getAll()) {
                        if (modulesJson.has(module.getId())) {
                            module.fromJson(modulesJson.getAsJsonObject(module.getId()));
                        }
                    }
                }

                // Load keybinds
                if (root.has("keybinds")) {
                    KeybindManager.getInstance().fromJson(root.getAsJsonObject("keybinds"));
                }

                // TODO: Load HUD layouts, theme

                activeConfig = name;

                NotificationManager.getInstance().success(
                    "Config Loaded",
                    "Configuration '" + name + "' loaded successfully",
                    2000
                );

                return true;
            } catch (Exception e) {
                NotificationManager.getInstance().error(
                    "Config Load Failed",
                    "Failed to load '" + name + "': " + e.getMessage(),
                    5000
                );
                e.printStackTrace();
                return false;
            }
        });
    }

    /**
     * Creates a new empty config.
     */
    public boolean createConfig(String name) {
        if (hasConfig(name)) {
            NotificationManager.getInstance().warning(
                "Config Exists",
                "Configuration '" + name + "' already exists",
                3000
            );
            return false;
        }

        try {
            JsonObject root = new JsonObject();
            root.addProperty("name", name);
            root.addProperty("version", "1.0.0");
            root.addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            root.add("modules", new JsonObject());
            root.add("keybinds", new JsonObject());
            root.add("hudLayouts", new JsonObject());
            root.add("theme", new JsonObject());

            Path target = configsDir.resolve(name + ".json");
            try (Writer writer = Files.newBufferedWriter(target)) {
                gson.toJson(root, writer);
            }

            NotificationManager.getInstance().success(
                "Config Created",
                "Configuration '" + name + "' created",
                2000
            );
            return true;
        } catch (Exception e) {
            NotificationManager.getInstance().error(
                "Config Create Failed",
                "Failed to create '" + name + "': " + e.getMessage(),
                5000
            );
            return false;
        }
    }

    /**
     * Deletes a config.
     */
    public boolean deleteConfig(String name) {
        if (name.equals("default")) {
            NotificationManager.getInstance().error(
                "Cannot Delete",
                "The default configuration cannot be deleted",
                3000
            );
            return false;
        }

        try {
            Path target = configsDir.resolve(name + ".json");
            if (Files.deleteIfExists(target)) {
                if (activeConfig.equals(name)) {
                    activeConfig = "default";
                }
                NotificationManager.getInstance().success(
                    "Config Deleted",
                    "Configuration '" + name + "' deleted",
                    2000
                );
                return true;
            }
            return false;
        } catch (Exception e) {
            NotificationManager.getInstance().error(
                "Config Delete Failed",
                "Failed to delete '" + name + "': " + e.getMessage(),
                5000
            );
            return false;
        }
    }

    /**
     * Duplicates a config.
     */
    public boolean duplicateConfig(String sourceName, String targetName) {
        if (hasConfig(targetName)) {
            NotificationManager.getInstance().error(
                "Config Exists",
                "Configuration '" + targetName + "' already exists",
                3000
            );
            return false;
        }

        try {
            Path source = configsDir.resolve(sourceName + ".json");
            Path target = configsDir.resolve(targetName + ".json");
            Files.copy(source, target);
            NotificationManager.getInstance().success(
                "Config Duplicated",
                "'" + sourceName + "' duplicated to '" + targetName + "'",
                2000
            );
            return true;
        } catch (Exception e) {
            NotificationManager.getInstance().error(
                "Config Duplicate Failed",
                "Failed to duplicate: " + e.getMessage(),
                5000
            );
            return false;
        }
    }

    /**
     * Exports a config to a file.
     */
    public boolean exportConfig(String name, Path destination) {
        try {
            Path source = configsDir.resolve(name + ".json");
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            NotificationManager.getInstance().success(
                "Config Exported",
                "Configuration exported to " + destination.getFileName(),
                2000
            );
            return true;
        } catch (Exception e) {
            NotificationManager.getInstance().error(
                "Export Failed",
                "Failed to export: " + e.getMessage(),
                5000
            );
            return false;
        }
    }

    /**
     * Imports a config from a file.
     */
    public boolean importConfig(Path source, String name) {
        try {
            if (hasConfig(name)) {
                NotificationManager.getInstance().error(
                    "Config Exists",
                    "Configuration '" + name + "' already exists",
                    3000
                );
                return false;
            }

            Path target = configsDir.resolve(name + ".json");
            Files.copy(source, target);
            NotificationManager.getInstance().success(
                "Config Imported",
                "Configuration imported as '" + name + "'",
                2000
            );
            return true;
        } catch (Exception e) {
            NotificationManager.getInstance().error(
                "Import Failed",
                "Failed to import: " + e.getMessage(),
                5000
            );
            return false;
        }
    }

    /**
     * Renames a config.
     */
    public boolean renameConfig(String oldName, String newName) {
        if (oldName.equals("default")) {
            NotificationManager.getInstance().error(
                "Cannot Rename",
                "The default configuration cannot be renamed",
                3000
            );
            return false;
        }

        if (hasConfig(newName)) {
            NotificationManager.getInstance().error(
                "Config Exists",
                "Configuration '" + newName + "' already exists",
                3000
            );
            return false;
        }

        try {
            Path oldPath = configsDir.resolve(oldName + ".json");
            Path newPath = configsDir.resolve(newName + ".json");
            Files.move(oldPath, newPath);

            if (activeConfig.equals(oldName)) {
                activeConfig = newName;
            }

            NotificationManager.getInstance().success(
                "Config Renamed",
                "'" + oldName + "' renamed to '" + newName + "'",
                2000
            );
            return true;
        } catch (Exception e) {
            NotificationManager.getInstance().error(
                "Rename Failed",
                "Failed to rename: " + e.getMessage(),
                5000
            );
            return false;
        }
    }

    // ============================================================
    // BACKUPS
    // ============================================================

    /**
     * Creates a backup of the active config.
     */
    public void backup() {
        backup(activeConfig);
    }

    /**
     * Creates a backup of a specific config.
     */
    public void backup(String name) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path source = configsDir.resolve(name + ".json");
            Path target = backupsDir.resolve(name + "_" + timestamp + ".json");
            Files.copy(source, target);

            // Clean old backups (keep last 10)
            cleanOldBackups(name);
        } catch (Exception e) {
            System.err.println("[NullCraft] Backup failed: " + e.getMessage());
        }
    }

    private void cleanOldBackups(String name) {
        try {
            List<Path> backups = Files.list(backupsDir)
                .filter(p -> p.getFileName().toString().startsWith(name + "_"))
                .sorted()
                .toList();

            while (backups.size() > 10) {
                Files.deleteIfExists(backups.get(0));
                backups = backups.subList(1, backups.size());
            }
        } catch (Exception e) {
            System.err.println("[NullCraft] Backup cleanup failed: " + e.getMessage());
        }
    }

    // ============================================================
    // AUTO SAVE
    // ============================================================

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    /**
     * Called periodically - saves if auto-save is enabled and interval has passed.
     */
    public void tick() {
        if (autoSave && System.currentTimeMillis() - lastAutoSave > AUTO_SAVE_INTERVAL) {
            save().thenAccept(success -> {
                if (success) {
                    lastAutoSave = System.currentTimeMillis();
                }
            });
        }
    }

    // ============================================================
    // SHUTDOWN
    // ============================================================

    /**
     * Saves current config on shutdown.
     */
    public void shutdown() {
        save().join(); // Wait for save to complete
    }
}