package com.nullcraft.client.gui;

import com.nullcraft.client.gui.components.*;
import com.nullcraft.client.core.utils.DesignTokens;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GUI layout calculations.
 */
class LayoutTest {

    @Test
    void testComponentBounds() {
        Component component = new Component(10, 20, 100, 50) {};

        assertEquals(10, component.getX());
        assertEquals(20, component.getY());
        assertEquals(100, component.getWidth());
        assertEquals(50, component.getHeight());

        component.setPosition(50, 60);
        component.setSize(200, 80);

        assertEquals(50, component.getX());
        assertEquals(60, component.getY());
        assertEquals(200, component.getWidth());
        assertEquals(80, component.getHeight());
    }

    @Test
    void testComponentContains() {
        Component component = new Component(10, 10, 100, 50) {};

        assertTrue(component.contains(15, 15));
        assertTrue(component.contains(10, 10));
        assertTrue(component.contains(109, 59));
        assertFalse(component.contains(9, 15));
        assertFalse(component.contains(15, 9));
        assertFalse(component.contains(110, 15));
        assertFalse(component.contains(15, 60));
    }

    @Test
    void testComponentVisibility() {
        Component component = new Component(0, 0, 100, 50) {};

        assertTrue(component.isVisible());
        assertTrue(component.isEnabled());

        component.setVisible(false);
        assertFalse(component.isVisible());

        component.setEnabled(false);
        assertFalse(component.isEnabled());
    }

    @Test
    void testButtonLayout() {
        Button button = new Button(10, 10, 80, 30, "Test", () -> {});

        assertEquals("Test", button.getLabel());
        assertEquals(80, button.getWidth());
        assertEquals(30, button.getHeight());

        button.setLabel("New Label");
        assertEquals("New Label", button.getLabel());
    }

    @Test
    void testToggleLayout() {
        com.nullcraft.client.core.settings.BooleanSetting setting =
            new com.nullcraft.client.core.settings.BooleanSetting("test", "Test", false);
        Toggle toggle = new Toggle(10, 10, 50, 24, "Test Toggle", setting);

        assertEquals("Test Toggle", toggle.getLabel());
        assertFalse(toggle.getSetting().getValue());
    }

    @Test
    void testSliderLayout() {
        com.nullcraft.client.core.settings.IntegerSetting intSetting =
            new com.nullcraft.client.core.settings.IntegerSetting("test_int", "Test Int", 5, 0, 10);
        Slider.IntegerSlider intSlider = new Slider.IntegerSlider(10, 10, 200, 24, "Int Slider", intSetting);

        assertEquals("Int Slider", intSlider.getLabel());
        assertEquals(5, intSlider.getSetting().getValue());

        com.nullcraft.client.core.settings.DoubleSetting doubleSetting =
            new com.nullcraft.client.core.settings.DoubleSetting("test_double", "Test Double", 1.5, 0.0, 5.0);
        Slider.DoubleSlider doubleSlider = new Slider.DoubleSlider(10, 40, 200, 24, "Double Slider", doubleSetting);

        assertEquals(1.5, doubleSlider.getSetting().getValue(), 0.001);

        com.nullcraft.client.core.settings.RangeSetting rangeSetting =
            new com.nullcraft.client.core.settings.RangeSetting("test_range", "Test Range", 2.0, 5.0, 0.0, 10.0);
        Slider.RangeSlider rangeSlider = new Slider.RangeSlider(10, 70, 200, 24, "Range Slider", rangeSetting);

        assertEquals(2.0, rangeSlider.getSetting().getMinValue(), 0.001);
        assertEquals(5.0, rangeSlider.getSetting().getMaxValue(), 0.001);
    }

    @Test
    void testDropdownLayout() {
        Dropdown dropdown = new Dropdown(10, 10, 150, 30, "Test Dropdown",
            java.util.List.of("Option A", "Option B", "Option C"), 1, index -> {});

        assertEquals("Option B", dropdown.getSelected());
        assertEquals(1, dropdown.getSelectedIndex());

        dropdown.setSelectedIndex(2);
        assertEquals("Option C", dropdown.getSelected());
        assertEquals(2, dropdown.getSelectedIndex());
    }

    @Test
    void testColorPickerLayout() {
        com.nullcraft.client.core.settings.ColorSetting colorSetting =
            new com.nullcraft.client.core.settings.ColorSetting("test_color", "Test Color", 0xFF6B46C1, true);
        ColorPicker colorPicker = new ColorPicker(10, 10, 200, 30, "Color Picker", colorSetting);

        assertEquals(0xFF6B46C1, colorPicker.getColor());
        assertEquals(0x6B46C1, colorPicker.getRGB());
        assertEquals(0xFF, colorPicker.getAlpha());

        colorPicker.setColor(0x80FF0000);
        assertEquals(0x80FF0000, colorPicker.getColor());
        assertEquals(0xFF0000, colorPicker.getRGB());
        assertEquals(0x80, colorPicker.getAlpha());
    }

    @Test
    void testKeybindButtonLayout() {
        com.nullcraft.client.core.keybind.Keybind keybind = new com.nullcraft.client.core.keybind.Keybind(257, 0, 0);
        KeybindButton keybindButton = new KeybindButton(10, 10, 120, 30, "Keybind", keybind, kb -> {});

        assertEquals("Enter", keybindButton.getKeybind().toShortString());

        keybindButton.setKeybind(new com.nullcraft.client.core.keybind.Keybind(65, 0, com.nullcraft.client.core.keybind.Keybind.MOD_SHIFT));
        assertEquals("Shift+A", keybindButton.getKeybind().toShortString());
    }

    @Test
    void testTextFieldLayout() {
        TextField textField = new TextField(10, 10, 200, 30, "Test Field", "Placeholder", 50, false, text -> {});

        assertEquals("", textField.getText());
        assertEquals("Placeholder", textField.getText()); // Shows placeholder when empty

        textField.setText("Hello World");
        assertEquals("Hello World", textField.getText());

        // Max length
        textField.setText("a".repeat(100));
        assertEquals(50, textField.getText().length());
    }

    @Test
    void testRoundedRectCalculations() {
        // Test corner radius calculations
        float radius = DesignTokens.RADIUS_MEDIUM;
        assertEquals(10, radius);

        // Test spacing tokens
        assertEquals(4, DesignTokens.SPACING_XS);
        assertEquals(8, DesignTokens.SPACING_SM);
        assertEquals(16, DesignTokens.SPACING_MD);
        assertEquals(24, DesignTokens.SPACING_LG);
        assertEquals(32, DesignTokens.SPACING_XL);

        // Test animation durations
        assertEquals(0.1f, DesignTokens.ANIM_FAST, 0.001);
        assertEquals(0.2f, DesignTokens.ANIM_NORMAL, 0.001);
        assertEquals(0.3f, DesignTokens.ANIM_SLOW, 0.001);
    }
}