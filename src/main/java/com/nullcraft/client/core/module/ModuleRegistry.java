package com.nullcraft.client.core.module;

import com.nullcraft.client.core.event.EventBus;
import com.nullcraft.client.core.notification.NotificationManager;
import com.nullcraft.client.core.notification.NotificationType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Central registry for all NullCraft modules.
 * Handles registration, lookup, category filtering, and lifecycle management.
 */
public final class ModuleRegistry {

    private static final ModuleRegistry INSTANCE = new ModuleRegistry();

    private final Map<String, Module> modules = new ConcurrentHashMap<>();
    private final Map<ModuleCategory, List<Module>> modulesByCategory = new EnumMap<>(ModuleCategory.class);

    private ModuleRegistry() {
        // Initialize category lists
        for (ModuleCategory category : ModuleCategory.values()) {
            modulesByCategory.put(category, new ArrayList<>());
        }
    }

    public static ModuleRegistry getInstance() {
        return INSTANCE;
    }

    // ============================================================
    // REGISTRATION
    // ============================================================

    /**
     * Registers a module instance.
     * Should be called during mod initialization.
     */
    public void register(Module module) {
        if (modules.containsKey(module.getId())) {
            NotificationManager.getInstance().warning(
                "Module Conflict",
                "Module with ID '" + module.getId() + "' already registered. Overwriting.",
                3000
            );
        }

        modules.put(module.getId(), module);
        modulesByCategory.get(module.getCategory()).add(module);

        NotificationManager.getInstance().info(
            "Module Registered",
            module.getName() + " (" + module.getCategory().getDisplayName() + ")",
            1500
        );
    }

    /**
     * Registers multiple modules at once.
     */
    public void registerAll(Module... modules) {
        for (Module module : modules) {
            register(module);
        }
    }

    /**
     * Unregisters a module by ID.
     */
    public void unregister(String moduleId) {
        Module module = modules.remove(moduleId);
        if (module != null) {
            modulesByCategory.get(module.getCategory()).remove(module);
            if (module.isEnabled()) {
                module.setEnabled(false);
            }
        }
    }

    // ============================================================
    // LOOKUP
    // ============================================================

    public Module get(String id) {
        return modules.get(id);
    }

    public <T extends Module> T get(String id, Class<T> type) {
        Module module = modules.get(id);
        if (type.isInstance(module)) {
            return type.cast(module);
        }
        return null;
    }

    public boolean hasModule(String id) {
        return modules.containsKey(id);
    }

    public Collection<Module> getAll() {
        return Collections.unmodifiableCollection(modules.values());
    }

    public List<Module> getByCategory(ModuleCategory category) {
        return Collections.unmodifiableList(modulesByCategory.get(category));
    }

    public List<Module> getEnabled() {
        return modules.values().stream()
            .filter(Module::isEnabled)
            .collect(Collectors.toList());
    }

    public List<Module> getEnabledByCategory(ModuleCategory category) {
        return modulesByCategory.get(category).stream()
            .filter(Module::isEnabled)
            .collect(Collectors.toList());
    }

    public List<Module> getFavorites() {
        return modules.values().stream()
            .filter(Module::isFavorite)
            .collect(Collectors.toList());
    }

    public List<Module> search(String query) {
        String lower = query.toLowerCase();
        return modules.values().stream()
            .filter(m -> m.getName().toLowerCase().contains(lower)
                      || m.getDescription().toLowerCase().contains(lower)
                      || m.getId().toLowerCase().contains(lower))
            .collect(Collectors.toList());
    }

    // ============================================================
    // LIFECYCLE MANAGEMENT
    // ============================================================

    /**
     * Enables a module by ID.
     */
    public boolean enable(String id) {
        Module module = modules.get(id);
        if (module != null && !module.isEnabled()) {
            module.setEnabled(true);
            return true;
        }
        return false;
    }

    /**
     * Disables a module by ID.
     */
    public boolean disable(String id) {
        Module module = modules.get(id);
        if (module != null && module.isEnabled()) {
            module.setEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * Toggles a module by ID.
     */
    public boolean toggle(String id) {
        Module module = modules.get(id);
        if (module != null) {
            module.toggle();
            return true;
        }
        return false;
    }

    /**
     * Disables all modules.
     */
    public void disableAll() {
        for (Module module : modules.values()) {
            if (module.isEnabled()) {
                module.setEnabled(false);
            }
        }
    }

    /**
     * Called every client tick - delegates to enabled modules.
     */
    public void onTick() {
        for (Module module : modules.values()) {
            if (module.isEnabled()) {
                try {
                    module.onTick();
                } catch (Exception e) {
                    handleModuleError(module, "onTick", e);
                }
            }
        }
    }

    /**
     * Called every render tick - delegates to enabled modules.
     */
    public void onRender(float tickDelta) {
        for (Module module : modules.values()) {
            if (module.isEnabled()) {
                try {
                    module.onRender(tickDelta);
                } catch (Exception e) {
                    handleModuleError(module, "onRender", e);
                }
            }
        }
    }

    /**
     * Called on client disconnect - cleanup all modules.
     */
    public void onDisconnect() {
        for (Module module : modules.values()) {
            if (module.isEnabled()) {
                try {
                    module.onDisable();
                } catch (Exception e) {
                    handleModuleError(module, "onDisconnect", e);
                }
            }
        }
    }

    private void handleModuleError(Module module, String method, Exception e) {
        NotificationManager.getInstance().error(
            "Module Error",
            module.getName() + " failed in " + method + ": " + e.getMessage(),
            5000
        );
        // Optionally disable the module to prevent further errors
        // module.setEnabled(false);
        e.printStackTrace();
    }

    // ============================================================
    // STATISTICS
    // ============================================================

    public int getTotalCount() {
        return modules.size();
    }

    public int getEnabledCount() {
        return (int) modules.values().stream().filter(Module::isEnabled).count();
    }

    public int getFavoriteCount() {
        return (int) modules.values().stream().filter(Module::isFavorite).count();
    }

    public Map<ModuleCategory, Integer> getCountByCategory() {
        Map<ModuleCategory, Integer> counts = new EnumMap<>(ModuleCategory.class);
        for (ModuleCategory category : ModuleCategory.values()) {
            counts.put(category, modulesByCategory.get(category).size());
        }
        return counts;
    }
}