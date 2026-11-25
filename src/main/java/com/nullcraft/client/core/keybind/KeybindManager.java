package com.nullcraft.client.core.keybind;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleRegistry;
import com.nullcraft.client.core.notification.NotificationManager;
import com.nullcraft.client.core.notification.NotificationType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages all keybinds for modules and global actions.
 * Handles conflict detection and persistence.
 */
public final class KeybindManager {

    private static final KeybindManager INSTANCE = new KeybindManager();

    private final Map<String, Keybind> keybinds = new ConcurrentHashMap<>();
    private final Map<Keybind, String> reverseLookup = new ConcurrentHashMap<>();

    // Default keybinds
    private static final Map<String, Keybind> DEFAULTS = Map.of(
        "clickgui", Keybind.of(KeyNames.KEY_RIGHT_SHIFT),
        "hud_editor", Keybind.of(KeyNames.KEY_RIGHT_CONTROL),
        "emergency_disable", Keybind.of(KeyNames.KEY_END)
    );

    private KeybindManager() {
        // Initialize with defaults
        keybinds.putAll(DEFAULTS);
        rebuildReverseLookup();
    }

    public static KeybindManager getInstance() {
        return INSTANCE;
    }

    // ============================================================
    // MODULE KEYBINDS
    // ============================================================

    /**
     * Gets the keybind for a module.
     */
    public Keybind getModuleKeybind(Module module) {
        return keybinds.getOrDefault("module:" + module.getId(), Keybind.NONE);
    }

    /**
     * Sets the keybind for a module.
     */
    public boolean setModuleKeybind(Module module, Keybind keybind) {
        String key = "module:" + module.getId();

        // Check for conflicts
        String conflict = checkConflict(keybind);
        if (conflict != null && !conflict.equals(key)) {
            NotificationManager.getInstance().warning(
                "Keybind Conflict",
                "Keybind conflicts with " + getDisplayName(conflict),
                3000
            );
            return false;
        }

        // Remove old mapping
        Keybind old = keybinds.get(key);
        if (old != null && !old.isNone()) {
            reverseLookup.remove(old);
        }

        // Set new mapping
        if (keybind.isNone()) {
            keybinds.remove(key);
        } else {
            keybinds.put(key, keybind);
            reverseLookup.put(keybind, key);
        }

        module.setKeybind(keybind);
        return true;
    }

    /**
     * Clears the keybind for a module.
     */
    public void clearModuleKeybind(Module module) {
        setModuleKeybind(module, Keybind.NONE);
    }

    // ============================================================
    // GLOBAL KEYBINDS
    // ============================================================

    public Keybind getGlobalKeybind(String action) {
        return keybinds.getOrDefault("global:" + action, Keybind.NONE);
    }

    public boolean setGlobalKeybind(String action, Keybind keybind) {
        String key = "global:" + action;

        String conflict = checkConflict(keybind);
        if (conflict != null && !conflict.equals(key)) {
            NotificationManager.getInstance().warning(
                "Keybind Conflict",
                "Keybind conflicts with " + getDisplayName(conflict),
                3000
            );
            return false;
        }

        Keybind old = keybinds.get(key);
        if (old != null && !old.isNone()) {
            reverseLookup.remove(old);
        }

        if (keybind.isNone()) {
            keybinds.remove(key);
        } else {
            keybinds.put(key, keybind);
            reverseLookup.put(keybind, key);
        }

        return true;
    }

    // ============================================================
    // CONFLICT DETECTION
    // ============================================================

    /**
     * Checks if a keybind conflicts with any existing keybind.
     * @return The conflicting keybind's key, or null if no conflict
     */
    public String checkConflict(Keybind keybind) {
        if (keybind.isNone()) return null;
        return reverseLookup.get(keybind);
    }

    /**
     * Gets all conflicts for a keybind.
     */
    public List<String> getAllConflicts(Keybind keybind) {
        List<String> conflicts = new ArrayList<>();
        for (Map.Entry<Keybind, String> entry : reverseLookup.entrySet()) {
            if (entry.getKey().equals(keybind)) {
                conflicts.add(entry.getValue());
            }
        }
        return conflicts;
    }

    /**
     * Gets a display name for a keybind key.
     */
    private String getDisplayName(String key) {
        if (key.startsWith("module:")) {
            String moduleId = key.substring(7);
            Module module = ModuleRegistry.getInstance().get(moduleId);
            return module != null ? module.getName() : moduleId;
        } else if (key.startsWith("global:")) {
            return key.substring(7).replace('_', ' ');
        }
        return key;
    }

    // ============================================================
    // INPUT HANDLING
    // ============================================================

    /**
     * Processes a key event and triggers matching keybinds.
     * @return true if a keybind was triggered
     */
    public boolean onKeyEvent(Keybind.KeyEvent event) {
        if (event.getAction() != 1) return false; // Only on press

        // Don't trigger keybinds while typing in chat or text fields
        if (isTextInputActive()) return false;

        Keybind pressed = new Keybind(event.getKeyCode(), event.getScanCode(), event.getModifiers());

        // Check global keybinds first
        for (Map.Entry<String, Keybind> entry : keybinds.entrySet()) {
            if (entry.getKey().startsWith("global:") && entry.getValue().matches(pressed)) {
                triggerGlobal(entry.getKey().substring(7));
                return true;
            }
        }

        // Check module keybinds
        for (Map.Entry<String, Keybind> entry : keybinds.entrySet()) {
            if (entry.getKey().startsWith("module:") && entry.getValue().matches(pressed)) {
                String moduleId = entry.getKey().substring(7);
                Module module = ModuleRegistry.getInstance().get(moduleId);
                if (module != null) {
                    module.toggle();
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Triggers a global action by name.
     */
    private void triggerGlobal(String action) {
        switch (action) {
            case "clickgui" -> {
                // Handled by client's key handler
            }
            case "hud_editor" -> {
                // Handled by client's key handler
            }
            case "emergency_disable" -> {
                ModuleRegistry.getInstance().disableAll();
                NotificationManager.getInstance().warning(
                    "Emergency Disable",
                    "All modules disabled",
                    3000
                );
            }
        }
    }

    /**
     * Checks if text input is active (chat, text field, etc.)
     * This should be implemented based on Minecraft's current screen.
     */
    private boolean isTextInputActive() {
        // TODO: Implement check for Minecraft's current screen
        // return MinecraftClient.getInstance().currentScreen instanceof ChatScreen
        //     || MinecraftClient.getInstance().currentScreen instanceof TextFieldScreen;
        return false;
    }

    // ============================================================
    // SERIALIZATION
    // ============================================================

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();

        // Global keybinds
        JsonObject globals = new JsonObject();
        for (Map.Entry<String, Keybind> entry : keybinds.entrySet()) {
            if (entry.getKey().startsWith("global:") && !entry.getValue().isNone()) {
                globals.add(entry.getKey().substring(7), entry.getValue().toJson());
            }
        }
        obj.add("globals", globals);

        // Module keybinds
        JsonObject modules = new JsonObject();
        for (Map.Entry<String, Keybind> entry : keybinds.entrySet()) {
            if (entry.getKey().startsWith("module:") && !entry.getValue().isNone()) {
                modules.add(entry.getKey().substring(7), entry.getValue().toJson());
            }
        }
        obj.add("modules", modules);

        return obj;
    }

    public void fromJson(JsonObject obj) {
        // Clear non-default keybinds
        keybinds.entrySet().removeIf(e ->
            !DEFAULTS.containsKey(e.getKey()) || !DEFAULTS.get(e.getKey()).equals(e.getValue())
        );

        // Load globals
        if (obj.has("globals")) {
            JsonObject globals = obj.getAsJsonObject("globals");
            for (String key : globals.keySet()) {
                Keybind keybind = Keybind.fromJson(globals.getAsJsonObject(key));
                keybinds.put("global:" + key, keybind);
            }
        }

        // Load modules
        if (obj.has("modules")) {
            JsonObject modules = obj.getAsJsonObject("modules");
            for (String key : modules.keySet()) {
                Keybind keybind = Keybind.fromJson(modules.getAsJsonObject(key));
                keybinds.put("module:" + key, keybind);

                // Also update the module
                Module module = ModuleRegistry.getInstance().get(key);
                if (module != null) {
                    module.setKeybind(keybind);
                }
            }
        }

        rebuildReverseLookup();
    }

    private void rebuildReverseLookup() {
        reverseLookup.clear();
        for (Map.Entry<String, Keybind> entry : keybinds.entrySet()) {
            if (!entry.getValue().isNone()) {
                reverseLookup.put(entry.getValue(), entry.getKey());
            }
        }
    }

    // ============================================================
    // UTILITY
    // ============================================================

    public Map<String, Keybind> getAllKeybinds() {
        return Map.copyOf(keybinds);
    }

    public void resetToDefaults() {
        keybinds.clear();
        keybinds.putAll(DEFAULTS);
        rebuildReverseLookup();
    }
}