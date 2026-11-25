package com.nullcraft.client.gui;

import com.nullcraft.client.core.utils.ColorUtils;
import com.nullcraft.client.core.utils.DesignTokens;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for font metrics and ColorUtils.
 */
class FontTest {

    @Test
    void testDesignTokensTypography() {
        assertEquals(14, DesignTokens.TYPOGRAPHY_BASE_SIZE);
        assertEquals(12, DesignTokens.TYPOGRAPHY_SMALL_SIZE);
        assertEquals(18, DesignTokens.TYPOGRAPHY_LARGE_SIZE);
        assertEquals(24, DesignTokens.TYPOGRAPHY_LINE_HEIGHT);
    }

    @Test
    void testColorUtilsHSVConversion() {
        // Red
        float hue = ColorUtils.rgbToHue(255, 0, 0);
        float saturation = ColorUtils.rgbToSaturation(255, 0, 0);
        float brightness = ColorUtils.rgbToBrightness(255, 0, 0);

        assertEquals(0f, hue, 1f);
        assertEquals(1f, saturation, 0.01f);
        assertEquals(1f, brightness, 0.01f);

        // Green
        hue = ColorUtils.rgbToHue(0, 255, 0);
        assertEquals(120f, hue, 1f);

        // Blue
        hue = ColorUtils.rgbToHue(0, 0, 255);
        assertEquals(240f, hue, 1f);

        // White
        hue = ColorUtils.rgbToHue(255, 255, 255);
        saturation = ColorUtils.rgbToSaturation(255, 255, 255);
        brightness = ColorUtils.rgbToBrightness(255, 255, 255);
        assertEquals(0f, saturation, 0.01f);
        assertEquals(1f, brightness, 0.01f);

        // Black
        brightness = ColorUtils.rgbToBrightness(0, 0, 0);
        assertEquals(0f, brightness, 0.01f);

        // Gray
        saturation = ColorUtils.rgbToSaturation(128, 128, 128);
        assertEquals(0f, saturation, 0.01f);
    }

    @Test
    void testColorUtilsHSVToRGB() {
        // Red
        int rgb = ColorUtils.hsvToRgb(0f, 1f, 1f);
        assertEquals(0xFF0000, rgb);

        // Green
        rgb = ColorUtils.hsvToRgb(120f, 1f, 1f);
        assertEquals(0x00FF00, rgb);

        // Blue
        rgb = ColorUtils.hsvToRgb(240f, 1f, 1f);
        assertEquals(0x0000FF, rgb);

        // White
        rgb = ColorUtils.hsvToRgb(0f, 0f, 1f);
        assertEquals(0xFFFFFF, rgb);

        // Black
        rgb = ColorUtils.hsvToRgb(0f, 0f, 0f);
        assertEquals(0x000000, rgb);

        // Desaturated red
        rgb = ColorUtils.hsvToRgb(0f, 0.5f, 1f);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        assertEquals(255, r);
        assertTrue(g > 0 && g < 255);
        assertTrue(b > 0 && b < 255);
    }

    @Test
    void testColorUtilsLerp() {
        int color1 = 0xFF0000; // Red
        int color2 = 0x00FF00; // Green

        int mid = ColorUtils.lerp(color1, color2, 0.5f);
        int r = (mid >> 16) & 0xFF;
        int g = (mid >> 8) & 0xFF;
        int b = mid & 0xFF;

        // Mid should be yellow-ish
        assertTrue(r > 100 && r < 150);
        assertTrue(g > 100 && g < 150);
        assertEquals(0, b);

        // At t=0 should be color1
        int atZero = ColorUtils.lerp(color1, color2, 0f);
        assertEquals(color1, atZero);

        // At t=1 should be color2
        int atOne = ColorUtils.lerp(color1, color2, 1f);
        assertEquals(color2, atOne);
    }

    @Test
    void testColorUtilsBlend() {
        int color1 = 0xFF0000FF; // Opaque blue
        int color2 = 0x80FF0000; // Semi-transparent red

        int blended = ColorUtils.blend(color1, color2);
        // Result should be a blend
        int r = (blended >> 16) & 0xFF;
        int g = (blended >> 8) & 0xFF;
        int b = blended & 0xFF;

        assertTrue(r > 0); // Some red from color2
        assertTrue(b > 0); // Some blue from color1
    }

    @Test
    void testColorUtilsWithAlpha() {
        int rgb = 0xFF0000; // Red
        int withAlpha = ColorUtils.withAlpha(rgb, 128);

        assertEquals(0x80FF0000, withAlpha);
        assertEquals(128, (withAlpha >> 24) & 0xFF);
        assertEquals(0xFF0000, withAlpha & 0x00FFFFFF);
    }

    @Test
    void testColorUtilsRainbow() {
        int color1 = ColorUtils.rainbow(0);
        int color2 = ColorUtils.rainbow(360);
        // Should be same (360 degrees = full cycle)
        assertEquals(color1, color2);

        // Different offsets should give different colors
        int color3 = ColorUtils.rainbow(120);
        assertNotEquals(color1, color3);

        // Saturation and brightness parameters
        int pastel = ColorUtils.rainbow(0, 0.5f, 0.8f);
        int vivid = ColorUtils.rainbow(0, 1f, 1f);
        assertNotEquals(pastel, vivid);
    }

    @Test
    void testColorUtilsPulse() {
        int baseColor = 0xFF6B46C1;
        int pulsed1 = ColorUtils.pulse(baseColor, 0f);
        int pulsed2 = ColorUtils.pulse(baseColor, 1f);
        int pulsed3 = ColorUtils.pulse(baseColor, 0.5f);

        // Should return different brightness levels
        assertNotEquals(pulsed1, pulsed2);
    }

    @Test
    void testColorUtilsContrast() {
        int white = 0xFFFFFFFF;
        int black = 0xFF000000;
        int red = 0xFFFF0000;
        int green = 0xFF00FF00;
        int blue = 0xFF0000FF;

        // White on black - high contrast
        double contrast = ColorUtils.getContrastRatio(white, black);
        assertTrue(contrast > 10); // Should be ~21

        // Red on black
        contrast = ColorUtils.getContrastRatio(red, black);
        assertTrue(contrast > 4); // Should be ~5.25

        // Blue on black - low contrast
        contrast = ColorUtils.getContrastRatio(blue, black);
        assertTrue(contrast < 4); // Should be ~2.44

        // White on white - no contrast
        contrast = ColorUtils.getContrastRatio(white, white);
        assertEquals(1.0, contrast, 0.01);
    }

    @Test
    void testColorUtilsReadableTextColor() {
        int darkBg = 0xFF1A1A2E;
        int lightBg = 0xFFFFFFFF;

        int textOnDark = ColorUtils.getReadableTextColor(darkBg);
        int textOnLight = ColorUtils.getReadableTextColor(lightBg);

        // On dark background, text should be light
        int textBrightness = ColorUtils.rgbToBrightness(
            (textOnDark >> 16) & 0xFF,
            (textOnDark >> 8) & 0xFF,
            textOnDark & 0xFF
        );
        assertTrue(textBrightness > 0.5f);

        // On light background, text should be dark
        textBrightness = ColorUtils.rgbToBrightness(
            (textOnLight >> 16) & 0xFF,
            (textOnLight >> 8) & 0xFF,
            textOnLight & 0xFF
        );
        assertTrue(textBrightness < 0.5f);
    }
}