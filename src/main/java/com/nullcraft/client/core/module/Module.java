package com.nullcraft.client.core.module;

import com.google.gson.JsonObject;
import com.nullcraft.client.core.event.EventBus;
import com.nullcraft.client.core.event.ModuleToggleEvent;
import com.nullcraft.client.core.settings.*;
import com.nullcraft.client.core.keybind.Keybind;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Abstract base class for all NullCraft modules.
 * Provides lifecycle management, settings, keybinds, and event handling.
 */
public abstract class Module {

    private final String id;
    private final String name;
    private final String description;
    private final ModuleCategory category;
    private final String iconId;

    // State
    private boolean enabled = false;
    private boolean favorite = false;
    private Keybind keybind = Keybind.NONE;

    // Settings
    private final List<Setting<?>> settings = new ArrayList<>();
    private final List<SettingGroup> settingGroups = new ArrayList<>();

    // Runtime
    private final UUID instanceId = UUID.randomUUID();

    protected Module(String id, String name, String description, ModuleCategory category, String iconId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.iconId = iconId;

        // Register default settings
        registerSettings();
        createSettingGroups();
    }

    // ============================================================
    // ABSTRACT METHODS - Override in subclasses
    // ============================================================

    /**
     * Register all settings for this module.
     * Called once during construction.
     */
    protected abstract void registerSettings();

    /**
     * Organize settings into groups for the Settings Inspector.
     * Called once during construction after registerSettings().
     */
    protected abstract void createSettingGroups();

    /**
     * Called when the module is enabled.
     * Register event listeners, start tasks, etc.
     */
    public abstract void onEnable();

    /**
     * Called when the module is disabled.
     * Unregister event listeners, cleanup, etc.
     */
    public abstract void onDisable();

    /**
     * Called every client tick while the module is enabled.
     * Override for periodic logic.
     */
    public void onTick() {}

    /**
     * Called every render tick while the module is enabled.
     * Override for HUD/ESP rendering.
     */
    public void onRender(float tickDelta) {}

    // ============================================================
    // IDENTITY
    // ============================================================

    public final String getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    /**
     * Get display name (can be overridden for localized names).
     * Defaults to the module name.
     */
    public String getDisplayName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public final ModuleCategory getCategory() {
        return category;
    }

    public final String getIconId() {
        return iconId;
    }

    public final UUID getInstanceId() {
        return instanceId;
    }

    // ============================================================
    // ENABLED STATE
    // ============================================================

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) {
                onEnable();
                EventBus.getInstance().publish(new ModuleToggleEvent(this, true));
            } else {
                onDisable();
                EventBus.getInstance().publish(new ModuleToggleEvent(this, false));
            }
        }
    }

    public final void toggle() {
        setEnabled(!enabled);
    }

    // ============================================================
    // FAVORITE STATE
    // ============================================================

    public final boolean isFavorite() {
        return favorite;
    }

    public final void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public final void toggleFavorite() {
        setFavorite(!favorite);
    }

    // ============================================================
    // KEYBIND
    // ============================================================

    public final Keybind getKeybind() {
        return keybind;
    }

    public final void setKeybind(Keybind keybind) {
        this.keybind = keybind;
    }

    // ============================================================
    // SETTINGS
    // ============================================================

    protected final <T> Setting<T> registerSetting(Setting<T> setting) {
        settings.add(setting);
        return setting;
    }

    protected final SettingGroup createGroup(String name) {
        SettingGroup group = new SettingGroup(name);
        settingGroups.add(group);
        return group;
    }

    protected final SettingGroup createGroup(String name, String description) {
        SettingGroup group = new SettingGroup(name, description);
        settingGroups.add(group);
        return group;
    }

    public final List<Setting<?>> getSettings() {
        return List.copyOf(settings);
    }

    public final List<SettingGroup> getSettingGroups() {
        return List.copyOf(settingGroups);
    }

    public final <T> Setting<T> getSetting(String name) {
        for (Setting<?> setting : settings) {
            if (setting.getName().equals(name)) {
                @SuppressWarnings("unchecked")
                Setting<T> cast = (Setting<T>) setting;
                return cast;
            }
        }
        return null;
    }

    // ============================================================
    // CONFIGURATION SERIALIZATION
    // ============================================================

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("enabled", enabled);
        json.addProperty("favorite", favorite);
        if (keybind != null && !keybind.isNone()) {
            json.addProperty("keybind", keybind.toString());
        }

        JsonObject settingsJson = new JsonObject();
        for (Setting<?> setting : settings) {
            settingsJson.add(setting.getName(), setting.toJson());
        }
        json.add("settings", settingsJson);

        return json;
    }

    public void fromJson(JsonObject json) {
        if (json.has("enabled")) {
            setEnabled(json.get("enabled").getAsBoolean());
        }
        if (json.has("favorite")) {
            setFavorite(json.get("favorite").getAsBoolean());
        }
        if (json.has("keybind")) {
            setKeybind(Keybind.fromString(json.get("keybind").getAsString()));
        }
        if (json.has("settings")) {
            JsonObject settingsJson = json.getAsJsonObject("settings");
            for (Setting<?> setting : settings) {
                if (settingsJson.has(setting.getName())) {
                    setting.fromJson(settingsJson.get(setting.getName()));
                }
            }
        }
    }

    // ============================================================
    // UTILITY
    // ============================================================

    @Override
    public String toString() {
        return "Module{" + id + " (" + name + ")}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Module module = (Module) obj;
        return id.equals(module.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}