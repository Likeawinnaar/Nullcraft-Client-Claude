package com.nullcraft.client.gui;

import com.nullcraft.client.gui.themeeditor.Theme;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Theme system.
 */
class ThemeTest {

    @Test
    void testBuiltInThemes() {
        for (Theme.ThemeType type : Theme.ThemeType.values()) {
            if (type == Theme.ThemeType.CUSTOM) continue;

            Theme theme = new Theme(type.name().toLowerCase(), type);
            assertNotNull(theme);
            assertEquals(type.name().toLowerCase(), theme.getName());
            assertEquals(type, theme.getType());
        }
    }

    @Test
    void testThemeDefaults() {
        Theme theme = new Theme("test", Theme.ThemeType.DEFAULT);

        // Check that all tokens have values
        assertNotEquals(0, theme.getColor(0xFF1A1A2E)); // Background
        assertNotEquals(0, theme.getColor(0xFF6B46C1)); // Primary purple
        assertNotEquals(0, theme.getFloat(10f)); // Radius
        assertNotEquals(0, theme.getInt(16)); // Spacing
    }

    @Test
    void testThemeColorOverrides() {
        Theme theme = new Theme("test", Theme.ThemeType.DEFAULT);

        int originalBg = theme.getColor(0xFF1A1A2E);
        theme.setColor(0xFF1A1A2E, 0xFF000000);
        assertEquals(0xFF000000, theme.getColor(0xFF1A1A2E));
        assertNotEquals(originalBg, theme.getColor(0xFF1A1A2E));
    }

    @Test
    void testThemeFloatOverrides() {
        Theme theme = new Theme("test", Theme.ThemeType.DEFAULT);

        theme.setFloat(10f, 20f);
        assertEquals(20f, theme.getFloat(10f), 0.001);
    }

    @Test
    void testThemeIntOverrides() {
        Theme theme = new Theme("test", Theme.ThemeType.DEFAULT);

        theme.setInt(16, 32);
        assertEquals(32, theme.getInt(16));
    }

    @Test
    void testThemeSerialization() {
        Theme theme = new Theme("test_theme", Theme.ThemeType.VIOLET);
        theme.setColor(0xFF1A1A2E, 0xFF123456);
        theme.setFloat(10f, 15f);
        theme.setInt(16, 24);

        com.google.gson.JsonObject json = theme.toJson();

        assertEquals("test_theme", json.get("name").getAsString());
        assertEquals("VIOLET", json.get("type").getAsString());
        assertTrue(json.has("colors"));
        assertTrue(json.has("floats"));
        assertTrue(json.has("ints"));

        // Deserialize
        Theme deserialized = Theme.fromJson(json);
        assertEquals("test_theme", deserialized.getName());
        assertEquals(Theme.ThemeType.VIOLET, deserialized.getType());
        assertEquals(0xFF123456, deserialized.getColor(0xFF1A1A2E));
        assertEquals(15f, deserialized.getFloat(10f), 0.001);
        assertEquals(24, deserialized.getInt(16));
    }

    @Test
    void testThemeVariants() {
        Theme dark = new Theme("dark", Theme.ThemeType.DARK);
        Theme midnight = new Theme("midnight", Theme.ThemeType.MIDNIGHT);
        Theme violet = new Theme("violet", Theme.ThemeType.VIOLET);
        Theme emerald = new Theme("emerald", Theme.ThemeType.EMERALD);
        Theme rose = new Theme("rose", Theme.ThemeType.ROSE);
        Theme amber = new Theme("amber", Theme.ThemeType.AMBER);

        // Each should have different accent colors
        int darkAccent = dark.getColor(0xFF6B46C1);
        int midnightAccent = midnight.getColor(0xFF6B46C1);
        int violetAccent = violet.getColor(0xFF6B46C1);
        int emeraldAccent = emerald.getColor(0xFF6B46C1);
        int roseAccent = rose.getColor(0xFF6B46C1);
        int amberAccent = amber.getColor(0xFF6B46C1);

        assertNotEquals(darkAccent, midnightAccent);
        assertNotEquals(violetAccent, emeraldAccent);
        assertNotEquals(roseAccent, amberAccent);
    }
}