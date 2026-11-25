package com.nullcraft.client.modules;

import com.nullcraft.client.core.module.*;
import com.nullcraft.client.core.event.EventBus;
import com.nullcraft.client.core.event.ModuleToggleEvent;
import org.junit.jupiter.api.*;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Module lifecycle events.
 */
class ModuleLifecycleTest {

    private ModuleRegistry registry;
    private EventBus eventBus;
    private AtomicInteger enableEventCount;
    private AtomicInteger disableEventCount;

    @BeforeEach
    void setUp() {
        registry = ModuleRegistry.getInstance();
        registry.clear();

        eventBus = EventBus.getInstance();
        enableEventCount = new AtomicInteger(0);
        disableEventCount = new AtomicInteger(0);

        // Subscribe to module toggle events
        eventBus.subscribe(ModuleToggleEvent.class, event -> {
            if (event.isEnabled()) {
                enableEventCount.incrementAndGet();
            } else {
                disableEventCount.incrementAndGet();
            }
        });
    }

    @AfterEach
    void tearDown() {
        registry.clear();
    }

    @Test
    void testEnableDisableLifecycle() {
        LifecycleTestModule module = new LifecycleTestModule("lifecycle_test", "Lifecycle Test", "Test lifecycle", ModuleCategory.COMBAT, "test");
        registry.register(module);

        assertFalse(module.isEnabled());
        assertEquals(0, module.enableCount);
        assertEquals(0, module.disableCount);
        assertEquals(0, enableEventCount.get());
        assertEquals(0, disableEventCount.get());

        // Enable
        module.setEnabled(true);
        assertTrue(module.isEnabled());
        assertEquals(1, module.enableCount);
        assertEquals(0, module.disableCount);
        assertEquals(1, enableEventCount.get());
        assertEquals(0, disableEventCount.get());

        // Disable
        module.setEnabled(false);
        assertFalse(module.isEnabled());
        assertEquals(1, module.enableCount);
        assertEquals(1, module.disableCount);
        assertEquals(1, enableEventCount.get());
        assertEquals(1, disableEventCount.get());

        // Toggle
        module.toggle();
        assertTrue(module.isEnabled());
        assertEquals(2, module.enableCount);
        assertEquals(1, module.disableCount);
        assertEquals(2, enableEventCount.get());
        assertEquals(1, disableEventCount.get());

        module.toggle();
        assertFalse(module.isEnabled());
        assertEquals(2, module.enableCount);
        assertEquals(2, module.disableCount);
        assertEquals(2, enableEventCount.get());
        assertEquals(2, disableEventCount.get());
    }

    @Test
    void testEnableDisableIdempotent() {
        LifecycleTestModule module = new LifecycleTestModule("idempotent_test", "Idempotent Test", "Test idempotent", ModuleCategory.COMBAT, "test");
        registry.register(module);

        module.setEnabled(true);
        assertEquals(1, module.enableCount);

        // Enabling again should not trigger onEnable
        module.setEnabled(true);
        assertEquals(1, module.enableCount);

        module.setEnabled(false);
        assertEquals(1, module.disableCount);

        // Disabling again should not trigger onDisable
        module.setEnabled(false);
        assertEquals(1, module.disableCount);
    }

    @Test
    void testModuleSettingsPersistThroughToggle() {
        LifecycleTestModule module = new LifecycleTestModule("settings_test", "Settings Test", "Test settings persist", ModuleCategory.COMBAT, "test");
        registry.register(module);

        com.nullcraft.client.core.settings.BooleanSetting setting =
            new com.nullcraft.client.core.settings.BooleanSetting("test_setting", "Test Setting", true);
        module.registerSetting(setting);

        setting.setValue(false);
        assertFalse(setting.getValue());

        module.setEnabled(true);
        module.setEnabled(false);

        // Setting value should persist
        assertFalse(setting.getValue());
    }

    @Test
    void testMultipleModulesIndependent() {
        LifecycleTestModule module1 = new LifecycleTestModule("mod1", "Module 1", "Test 1", ModuleCategory.COMBAT, "test");
        LifecycleTestModule module2 = new LifecycleTestModule("mod2", "Module 2", "Test 2", ModuleCategory.COMBAT, "test");
        registry.register(module1);
        registry.register(module2);

        module1.setEnabled(true);
        assertTrue(module1.isEnabled());
        assertFalse(module2.isEnabled());

        module2.setEnabled(true);
        assertTrue(module1.isEnabled());
        assertTrue(module2.isEnabled());

        module1.setEnabled(false);
        assertFalse(module1.isEnabled());
        assertTrue(module2.isEnabled());

        assertEquals(1, module1.enableCount);
        assertEquals(1, module1.disableCount);
        assertEquals(1, module2.enableCount);
        assertEquals(0, module2.disableCount);
    }

    @Test
    void testModuleKeybindPersists() {
        LifecycleTestModule module = new LifecycleTestModule("keybind_test", "Keybind Test", "Test keybind", ModuleCategory.COMBAT, "test");
        registry.register(module);

        com.nullcraft.client.core.keybind.Keybind keybind = new com.nullcraft.client.core.keybind.Keybind(257, 0, 0);
        module.setKeybind(keybind);

        module.setEnabled(true);
        module.setEnabled(false);

        assertEquals(keybind, module.getKeybind());
    }

    @Test
    void testModuleFavoritePersists() {
        LifecycleTestModule module = new LifecycleTestModule("favorite_test", "Favorite Test", "Test favorite", ModuleCategory.COMBAT, "test");
        registry.register(module);

        module.setFavorite(true);
        assertTrue(module.isFavorite());

        module.setEnabled(true);
        module.setEnabled(false);

        assertTrue(module.isFavorite());
    }

    // ============================================================
    // TEST MODULE
    // ============================================================

    private static class LifecycleTestModule extends Module {
        int enableCount = 0;
        int disableCount = 0;

        LifecycleTestModule(String id, String name, String description, ModuleCategory category, String iconId) {
            super(id, name, description, category, iconId);
        }

        @Override
        protected void registerSettings() {
            // No settings needed
        }

        @Override
        protected void createSettingGroups() {
            // No groups needed
        }

        @Override
        public void onEnable() {
            enableCount++;
        }

        @Override
        public void onDisable() {
            disableCount++;
        }
    }
}