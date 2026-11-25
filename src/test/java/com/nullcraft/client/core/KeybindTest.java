package com.nullcraft.client.core;

import com.nullcraft.client.core.keybind.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Keybind system.
 */
class KeybindTest {

    @Test
    void testKeybindCreation() {
        Keybind keybind = new Keybind(257, 0, 0); // Enter key

        assertEquals(257, keybind.getKeyCode());
        assertEquals(0, keybind.getScanCode());
        assertEquals(0, keybind.getModifiers());
        assertFalse(keybind.isNone());
    }

    @Test
    void testKeybindNone() {
        Keybind none = Keybind.NONE;

        assertEquals(-1, none.getKeyCode());
        assertEquals(-1, none.getScanCode());
        assertEquals(0, none.getModifiers());
        assertTrue(none.isNone());
    }

    @Test
    void testKeybindWithModifiers() {
        // Shift + Ctrl + A
        int modifiers = Keybind.MOD_SHIFT | Keybind.MOD_CTRL;
        Keybind keybind = new Keybind(65, 0, modifiers); // A key

        assertEquals(65, keybind.getKeyCode());
        assertTrue(keybind.hasShift());
        assertTrue(keybind.hasCtrl());
        assertFalse(keybind.hasAlt());
        assertFalse(keybind.hasSuper());
    }

    @Test
    void testKeybindEquality() {
        Keybind kb1 = new Keybind(257, 0, 0);
        Keybind kb2 = new Keybind(257, 0, 0);
        Keybind kb3 = new Keybind(258, 0, 0);

        assertEquals(kb1, kb2);
        assertNotEquals(kb1, kb3);
        assertEquals(kb1.hashCode(), kb2.hashCode());
    }

    @Test
    void testKeybindToString() {
        Keybind keybind = new Keybind(257, 0, Keybind.MOD_SHIFT | Keybind.MOD_CTRL);
        String str = keybind.toString();

        assertTrue(str.contains("257"));
        assertTrue(str.contains("3")); // Shift(1) | Ctrl(2) = 3
    }

    @Test
    void testKeybindFromString() {
        String str = "257:0:3"; // Enter with Shift+Ctrl
        Keybind keybind = Keybind.fromString(str);

        assertEquals(257, keybind.getKeyCode());
        assertEquals(0, keybind.getScanCode());
        assertEquals(3, keybind.getModifiers());
    }

    @Test
    void testKeybindFromStringInvalid() {
        Keybind keybind = Keybind.fromString("invalid");
        assertTrue(keybind.isNone());

        keybind = Keybind.fromString("257:abc:0");
        assertTrue(keybind.isNone());
    }

    @Test
    void testKeybindToShortString() {
        Keybind enter = new Keybind(257, 0, 0);
        assertEquals("Enter", enter.toShortString());

        Keybind shiftA = new Keybind(65, 0, Keybind.MOD_SHIFT);
        assertEquals("Shift+A", shiftA.toShortString());

        Keybind ctrlShiftC = new Keybind(67, 0, Keybind.MOD_CTRL | Keybind.MOD_SHIFT);
        assertEquals("Ctrl+Shift+C", ctrlShiftC.toShortString());

        Keybind none = Keybind.NONE;
        assertEquals("None", none.toShortString());
    }

    @Test
    void testKeyNames() {
        // Test some common keys
        assertEquals("Escape", KeyNames.getKeyName(256));
        assertEquals("Enter", KeyNames.getKeyName(257));
        assertEquals("Tab", KeyNames.getKeyName(258));
        assertEquals("Backspace", KeyNames.getKeyName(259));
        assertEquals("Space", KeyNames.getKeyName(32));
        assertEquals("A", KeyNames.getKeyName(65));
        assertEquals("Z", KeyNames.getKeyName(90));
        assertEquals("F1", KeyNames.getKeyName(290));
        assertEquals("F12", KeyNames.getKeyName(301));
        assertEquals("Left Shift", KeyNames.getKeyName(340));
        assertEquals("Right Shift", KeyNames.getKeyName(344));
        assertEquals("Left Ctrl", KeyNames.getKeyName(341));
        assertEquals("Right Ctrl", KeyNames.getKeyName(345));
        assertEquals("Left Alt", KeyNames.getKeyName(342));
        assertEquals("Right Alt", KeyNames.getKeyName(346));
        assertEquals("Left Super", KeyNames.getKeyName(343));
        assertEquals("Right Super", KeyNames.getKeyName(347));

        // Mouse buttons
        assertEquals("Mouse 1", KeyNames.getKeyName(-100));
        assertEquals("Mouse 2", KeyNames.getKeyName(-101));
        assertEquals("Mouse 3", KeyNames.getKeyName(-102));
    }

    @Test
    void testKeyNamesFromString() {
        assertEquals(257, KeyNames.getKeyCode("Enter"));
        assertEquals(256, KeyNames.getKeyCode("Escape"));
        assertEquals(65, KeyNames.getKeyCode("A"));
        assertEquals(32, KeyNames.getKeyCode("Space"));
        assertEquals(-1, KeyNames.getKeyCode("InvalidKey"));
    }

    @Test
    void testKeybindManager() {
        KeybindManager manager = KeybindManager.getInstance();

        // Test global keybinds
        manager.setGlobalKeybind("test_action", new Keybind(257, 0, 0));
        assertEquals(257, manager.getGlobalKeybind("test_action").getKeyCode());

        // Test module keybinds
        manager.setModuleKeybind("test_module", new Keybind(258, 0, Keybind.MOD_SHIFT));
        assertEquals(258, manager.getModuleKeybind("test_module").getKeyCode());

        // Test conflict detection
        manager.setGlobalKeybind("action1", new Keybind(65, 0, 0)); // A
        manager.setGlobalKeybind("action2", new Keybind(65, 0, 0)); // A again

        var conflicts = manager.getConflicts();
        assertTrue(conflicts.containsKey("A"));
        assertEquals(2, conflicts.get("A").size());

        // Test modifier combinations don't conflict with base
        manager.setGlobalKeybind("action3", new Keybind(65, 0, Keybind.MOD_SHIFT));
        conflicts = manager.getConflicts();
        // Shift+A should not conflict with A (different modifiers)
        assertEquals(2, conflicts.get("A").size()); // Still just the two base A keybinds
    }

    @Test
    void testKeybindManagerSerialization() {
        KeybindManager manager = KeybindManager.getInstance();

        manager.setGlobalKeybind("global1", new Keybind(257, 0, 0));
        manager.setGlobalKeybind("global2", new Keybind(258, 0, Keybind.MOD_CTRL));
        manager.setModuleKeybind("module1", new Keybind(65, 0, Keybind.MOD_SHIFT));

        com.google.gson.JsonObject json = manager.toJson();

        assertTrue(json.has("global"));
        assertTrue(json.has("module"));

        var globalObj = json.getAsJsonObject("global");
        assertTrue(globalObj.has("global1"));
        assertTrue(globalObj.has("global2"));
        assertEquals("257:0:0", globalObj.get("global1").getAsString());
        assertEquals("258:0:2", globalObj.get("global2").getAsString());

        var moduleObj = json.getAsJsonObject("module");
        assertTrue(moduleObj.has("module1"));
        assertEquals("65:0:1", moduleObj.get("module1").getAsString());

        // Test deserialization
        KeybindManager manager2 = new KeybindManager();
        manager2.fromJson(json);

        assertEquals(257, manager2.getGlobalKeybind("global1").getKeyCode());
        assertEquals(2, manager2.getGlobalKeybind("global2").getModifiers());
        assertEquals(65, manager2.getModuleKeybind("module1").getKeyCode());
        assertEquals(1, manager2.getModuleKeybind("module1").getModifiers());
    }
}