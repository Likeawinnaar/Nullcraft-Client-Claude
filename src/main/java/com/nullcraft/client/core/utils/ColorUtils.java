package com.nullcraft.client.core.utils;

/**
 * Color utility functions for HSV/RGB conversion and color manipulation.
 */
public final class ColorUtils {

    private ColorUtils() {}

    // ============================================================
    // RGB <-> HSV CONVERSION
    // ============================================================

    /**
     * Converts RGB to Hue (0-360).
     */
    public static float rgbToHue(int r, int g, int b) {
        float rf = r / 255f;
        float gf = g / 255f;
        float bf = b / 255f;

        float max = Math.max(rf, Math.max(gf, bf));
        float min = Math.min(rf, Math.min(gf, bf));
        float delta = max - min;

        if (delta == 0) return 0f;

        float hue;
        if (max == rf) {
            hue = (gf - bf) / delta % 6;
        } else if (max == gf) {
            hue = (bf - rf) / delta + 2;
        } else {
            hue = (rf - gf) / delta + 4;
        }

        hue *= 60;
        if (hue < 0) hue += 360;
        return hue;
    }

    /**
     * Converts RGB to Saturation (0-1).
     */
    public static float rgbToSaturation(int r, int g, int b) {
        float rf = r / 255f;
        float gf = g / 255f;
        float bf = b / 255f;

        float max = Math.max(rf, Math.max(gf, bf));
        float min = Math.min(rf, Math.min(gf, bf));
        float delta = max - min;

        if (max == 0) return 0f;
        return delta / max;
    }

    /**
     * Converts RGB to Brightness/Value (0-1).
     */
    public static float rgbToBrightness(int r, int g, int b) {
        float rf = r / 255f;
        float gf = g / 255f;
        float bf = b / 255f;
        return Math.max(rf, Math.max(gf, bf));
    }

    /**
     * Converts HSV to RGB (returns 0xRRGGBB).
     */
    public static int hsvToRgb(float hue, float saturation, float brightness) {
        hue = hue % 360f;
        if (hue < 0) hue += 360f;
        saturation = Math.max(0, Math.min(1, saturation));
        brightness = Math.max(0, Math.min(1, brightness));

        float c = brightness * saturation;
        float x = c * (1 - Math.abs((hue / 60f) % 2 - 1));
        float m = brightness - c;

        float r = 0, g = 0, b = 0;

        if (hue < 60) {
            r = c; g = x; b = 0;
        } else if (hue < 120) {
            r = x; g = c; b = 0;
        } else if (hue < 180) {
            r = 0; g = c; b = x;
        } else if (hue < 240) {
            r = 0; g = x; b = c;
        } else if (hue < 300) {
            r = x; g = 0; b = c;
        } else {
            r = c; g = 0; b = x;
        }

        int ir = (int) Math.round((r + m) * 255);
        int ig = (int) Math.round((g + m) * 255);
        int ib = (int) Math.round((b + m) * 255);

        return (ir << 16) | (ig << 8) | ib;
    }

    /**
     * Converts HSVA to ARGB.
     */
    public static int hsvaToArgb(float hue, float saturation, float brightness, float alpha) {
        int rgb = hsvToRgb(hue, saturation, brightness);
        int a = (int) Math.round(Math.max(0, Math.min(1, alpha)) * 255);
        return (a << 24) | rgb;
    }

    // ============================================================
    // COLOR MANIPULATION
    // ============================================================

    /**
     * Linear interpolation between two ARGB colors.
     */
    public static int lerp(int start, int end, float t) {
        t = Math.max(0, Math.min(1, t));
        int sa = (start >> 24) & 0xFF;
        int sr = (start >> 16) & 0xFF;
        int sg = (start >> 8) & 0xFF;
        int sb = start & 0xFF;

        int ea = (end >> 24) & 0xFF;
        int er = (end >> 16) & 0xFF;
        int eg = (end >> 8) & 0xFF;
        int eb = end & 0xFF;

        int a = (int) (sa + (ea - sa) * t);
        int r = (int) (sr + (er - sr) * t);
        int g = (int) (sg + (eg - sg) * t);
        int b = (int) (sb + (eb - sb) * t);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Gets a brighter version of an ARGB color.
     */
    public static int brighter(int color, float factor) {
        int a = (color >> 24) & 0xFF;
        int r = Math.min(255, (int) (((color >> 16) & 0xFF) * factor));
        int g = Math.min(255, (int) (((color >> 8) & 0xFF) * factor));
        int b = Math.min(255, (int) ((color & 0xFF) * factor));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Gets a darker version of an ARGB color.
     */
    public static int darker(int color, float factor) {
        int a = (color >> 24) & 0xFF;
        int r = Math.max(0, (int) (((color >> 16) & 0xFF) * factor));
        int g = Math.max(0, (int) (((color >> 8) & 0xFF) * factor));
        int b = Math.max(0, (int) ((color & 0xFF) * factor));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Applies alpha to an RGB color.
     */
    public static int withAlpha(int rgb, int alpha) {
        return (Math.max(0, Math.min(255, alpha)) << 24) | (rgb & 0x00FFFFFF);
    }

    /**
     * Multiplies the alpha of an ARGB color.
     */
    public static int multiplyAlpha(int argb, float factor) {
        int a = (int) (((argb >> 24) & 0xFF) * factor);
        return withAlpha(argb, a);
    }

    /**
     * Creates a rainbow color based on time.
     */
    public static int rainbow(float offset, float speed, float saturation, float brightness) {
        long time = System.currentTimeMillis();
        float hue = ((time * speed) % 360000) / 1000f + offset;
        return hsvaToArgb(hue, saturation, brightness, 1f);
    }

    /**
     * Creates a pulsing color between two colors.
     */
    public static int pulse(int color1, int color2, float speed) {
        long time = System.currentTimeMillis();
        float t = (float) (Math.sin(time * speed / 1000f) * 0.5 + 0.5);
        return lerp(color1, color2, t);
    }

    /**
     * Gets the contrast color (black or white) for a background color.
     */
    public static int getContrastColor(int background) {
        int r = (background >> 16) & 0xFF;
        int g = (background >> 8) & 0xFF;
        int b = background & 0xFF;

        // Calculate relative luminance
        double luminance = 0.299 * r + 0.587 * g + 0.114 * b;
        return luminance > 128 ? 0xFF000000 : 0xFFFFFFFF;
    }

    /**
     * Blends two colors with alpha (src over dst).
     */
    public static int blend(int src, int dst) {
        int sa = (src >> 24) & 0xFF;
        if (sa == 255) return src;
        if (sa == 0) return dst;

        int sr = (src >> 16) & 0xFF;
        int sg = (src >> 8) & 0xFF;
        int sb = src & 0xFF;

        int da = (dst >> 24) & 0xFF;
        int dr = (dst >> 16) & 0xFF;
        int dg = (dst >> 8) & 0xFF;
        int db = dst & 0xFF;

        float saF = sa / 255f;
        float daF = da / 255f;
        float aOut = saF + daF * (1 - saF);

        if (aOut == 0) return 0;

        int r = (int) ((sr * saF + dr * daF * (1 - saF)) / aOut * 255);
        int g = (int) ((sg * saF + dg * daF * (1 - saF)) / aOut * 255);
        int b = (int) ((sb * saF + db * daF * (1 - saF)) / aOut * 255);
        int a = (int) (aOut * 255);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}