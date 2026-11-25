package com.nullcraft.client.core;

import com.nullcraft.client.core.settings.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Settings system.
 */
class SettingsTest {

    @Test
    void testBooleanSetting() {
        BooleanSetting setting = new BooleanSetting("test_bool", "Test Boolean", false);

        assertEquals("test_bool", setting.getName());
        assertEquals("Test Boolean", setting.getDescription());
        assertEquals(SettingType.BOOLEAN, setting.getType());
        assertFalse(setting.getValue());
        assertFalse(setting.getDefaultValue());

        setting.setValue(true);
        assertTrue(setting.getValue());

        setting.setValue(false);
        assertFalse(setting.getValue());

        // Test JSON serialization
        com.google.gson.JsonObject json = setting.toJson();
        assertEquals("test_bool", json.get("name").getAsString());
        assertEquals("BOOLEAN", json.get("type").getAsString());
        assertEquals(false, json.get("value").getAsBoolean());

        // Test deserialization
        setting.fromJson(json);
        assertFalse(setting.getValue());

        com.google.gson.JsonObject trueJson = new com.google.gson.JsonObject();
        trueJson.addProperty("value", true);
        setting.fromJson(trueJson);
        assertTrue(setting.getValue());
    }

    @Test
    void testIntegerSetting() {
        IntegerSetting setting = new IntegerSetting("test_int", "Test Integer", 5, 0, 10);

        assertEquals(5, setting.getValue());
        assertEquals(5, setting.getDefaultValue());
        assertEquals(0, setting.getMin());
        assertEquals(10, setting.getMax());

        setting.setValue(8);
        assertEquals(8, setting.getValue());

        // Clamping
        setting.setValue(15);
        assertEquals(10, setting.getValue());

        setting.setValue(-5);
        assertEquals(0, setting.getValue());

        // JSON
        com.google.gson.JsonObject json = setting.toJson();
        assertEquals(5, json.get("value").getAsInt());
        assertEquals(0, json.get("min").getAsInt());
        assertEquals(10, json.get("max").getAsInt());
    }

    @Test
    void testDoubleSetting() {
        DoubleSetting setting = new DoubleSetting("test_double", "Test Double", 1.5, 0.0, 5.0);

        assertEquals(1.5, setting.getValue(), 0.001);
        assertEquals(1.5, setting.getDefaultValue(), 0.001);
        assertEquals(0.0, setting.getMin(), 0.001);
        assertEquals(5.0, setting.getMax(), 0.001);

        setting.setValue(3.14);
        assertEquals(3.14, setting.getValue(), 0.001);

        // Clamping
        setting.setValue(10.0);
        assertEquals(5.0, setting.getValue(), 0.001);

        setting.setValue(-1.0);
        assertEquals(0.0, setting.getValue(), 0.001);
    }

    @Test
    void testRangeSetting() {
        RangeSetting setting = new RangeSetting("test_range", "Test Range", 2.0, 5.0, 0.0, 10.0);

        assertEquals(2.0, setting.getMinValue(), 0.001);
        assertEquals(5.0, setting.getMaxValue(), 0.001);

        setting.setMinValue(3.0);
        assertEquals(3.0, setting.getMinValue(), 0.001);
        assertEquals(5.0, setting.getMaxValue(), 0.001);

        // Min cannot exceed max
        setting.setMinValue(8.0);
        assertEquals(5.0, setting.getMinValue(), 0.001); // Clamped to max

        // Max cannot be less than min
        setting.setMaxValue(1.0);
        assertEquals(3.0, setting.getMaxValue(), 0.001); // Clamped to min

        // JSON
        com.google.gson.JsonObject json = setting.toJson();
        assertEquals(3.0, json.get("min").getAsDouble(), 0.001);
        assertEquals(5.0, json.get("max").getAsDouble(), 0.001);
    }

    @Test
    void testModeSetting() {
        ModeSetting setting = new ModeSetting("test_mode", "Test Mode",
            java.util.List.of("Option A", "Option B", "Option C"), 1);

        assertEquals(3, setting.getOptions().size());
        assertEquals(1, setting.getIndex());
        assertEquals("Option B", setting.getSelected());

        setting.setIndex(2);
        assertEquals(2, setting.getIndex());
        assertEquals("Option C", setting.getSelected());

        // Clamping
        setting.setIndex(10);
        assertEquals(2, setting.getIndex());

        setting.setIndex(-1);
        assertEquals(0, setting.getIndex());

        // Set by value
        setting.setValue("Option A");
        assertEquals(0, setting.getIndex());
        assertEquals("Option A", setting.getSelected());

        // JSON
        com.google.gson.JsonObject json = setting.toJson();
        assertEquals(0, json.get("index").getAsInt());
    }

    @Test
    void testMultiSelectSetting() {
        MultiSelectSetting setting = new MultiSelectSetting("test_multi", "Test Multi",
            java.util.List.of("A", "B", "C", "D"), java.util.List.of(0, 2));

        assertEquals(4, setting.getOptions().size());
        assertEquals(2, setting.getSelectedIndices().size());
        assertTrue(setting.getSelectedIndices().contains(0));
        assertTrue(setting.getSelectedIndices().contains(2));

        setting.setSelectedIndices(java.util.List.of(1, 3));
        assertEquals(2, setting.getSelectedIndices().size());
        assertTrue(setting.getSelectedIndices().contains(1));
        assertTrue(setting.getSelectedIndices().contains(3));

        // Clamping - invalid indices removed
        setting.setSelectedIndices(java.util.List.of(0, 5, 2));
        assertEquals(2, setting.getSelectedIndices().size());
        assertTrue(setting.getSelectedIndices().contains(0));
        assertTrue(setting.getSelectedIndices().contains(2));

        // JSON
        com.google.gson.JsonObject json = setting.toJson();
        assertEquals(2, json.get("indices").getAsJsonArray().size());
    }

    @Test
    void testColorSetting() {
        ColorSetting setting = new ColorSetting("test_color", "Test Color", 0xFF6B46C1, true);

        assertEquals(0xFF6B46C1, setting.getColor());
        assertEquals(0x6B46C1, setting.getRGB());
        assertEquals(0xFF, setting.getAlpha());

        setting.setColor(0x80FF0000); // Semi-transparent red
        assertEquals(0x80FF0000, setting.getColor());
        assertEquals(0xFF0000, setting.getRGB());
        assertEquals(0x80, setting.getAlpha());

        // JSON
        com.google.gson.JsonObject json = setting.toJson();
        assertEquals("6B46C1", json.get("rgb").getAsString().toUpperCase());
        assertEquals("FF", json.get("alpha").getAsString().toUpperCase());

        // Without alpha
        ColorSetting noAlpha = new ColorSetting("test_color2", "Test Color 2", 0xFF00FF00, false);
        assertEquals(0xFF00FF00, noAlpha.getColor());
        assertEquals(0x00FF00, noAlpha.getRGB());
        assertEquals(0xFF, noAlpha.getAlpha());

        com.google.gson.JsonObject json2 = noAlpha.toJson();
        assertFalse(json2.has("alpha")); // No alpha in JSON when disabled
    }

    @Test
    void testStringSetting() {
        StringSetting setting = new StringSetting("test_string", "Test String", "default", 50);

        assertEquals("default", setting.getValue());
        assertEquals("default", setting.getDefaultValue());
        assertEquals(50, setting.getMaxLength());

        setting.setValue("hello");
        assertEquals("hello", setting.getValue());

        // Truncation
        String longString = "a".repeat(100);
        setting.setValue(longString);
        assertEquals(50, setting.getValue().length());

        // JSON
        com.google.gson.JsonObject json = setting.toJson();
        assertEquals("default", json.get("value").getAsString());
        assertEquals(50, json.get("maxLength").getAsInt());
    }

    @Test
    void testKeybindSetting() {
        com.nullcraft.client.core.keybind.Keybind keybind = new com.nullcraft.client.core.keybind.Keybind(257, 0, 0); // Enter
        KeybindSetting setting = new KeybindSetting("test_keybind", "Test Keybind", keybind);

        assertEquals(keybind, setting.getKeybind());
        assertEquals(keybind, setting.getDefaultValue());

        com.nullcraft.client.core.keybind.Keybind newKeybind = new com.nullcraft.client.core.keybind.Keybind(258, 0, 1); // Tab + Shift
        setting.setKeybind(newKeybind);
        assertEquals(newKeybind, setting.getKeybind());

        // JSON
        com.google.gson.JsonObject json = setting.toJson();
        assertEquals("257:0:0", json.get("keybind").getAsString()); // Default value
    }

    @Test
    void testSettingGroup() {
        SettingGroup group = new SettingGroup("General", "General settings");

        BooleanSetting boolSetting = new BooleanSetting("bool1", "Bool 1", true);
        IntegerSetting intSetting = new IntegerSetting("int1", "Int 1", 5, 0, 10);

        group.addSetting(boolSetting);
        group.addSetting(intSetting);

        assertEquals(2, group.getSettings().size());
        assertEquals("General", group.getName());
        assertEquals("General settings", group.getDescription());
        assertTrue(group.getSettings().contains(boolSetting));
        assertTrue(group.getSettings().contains(intSetting));
    }

    @Test
    void testSettingValidation() {
        // BooleanSetting - no validation needed
        BooleanSetting boolSetting = new BooleanSetting("bool", "Bool", true);
        assertDoesNotThrow(() -> boolSetting.setValue(false));

        // IntegerSetting validation
        IntegerSetting intSetting = new IntegerSetting("int", "Int", 5, 0, 10);
        assertDoesNotThrow(() -> intSetting.setValue(7));
        assertDoesNotThrow(() -> intSetting.setValue(15)); // Clamped
        assertDoesNotThrow(() -> intSetting.setValue(-5)); // Clamped

        // DoubleSetting validation
        DoubleSetting doubleSetting = new DoubleSetting("double", "Double", 1.0, 0.0, 5.0);
        assertDoesNotThrow(() -> doubleSetting.setValue(3.0));
        assertDoesNotThrow(() -> doubleSetting.setValue(10.0)); // Clamped

        // RangeSetting validation
        RangeSetting rangeSetting = new RangeSetting("range", "Range", 2.0, 5.0, 0.0, 10.0);
        assertDoesNotThrow(() -> rangeSetting.setMinValue(3.0));
        assertDoesNotThrow(() -> rangeSetting.setMaxValue(8.0));
    }
}