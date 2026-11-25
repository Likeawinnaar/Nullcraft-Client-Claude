package com.nullcraft.client.core.utils;

import java.awt.Color;

/**
 * Centralized design tokens for the NullCraft Client GUI.
 * All colors, spacing, radii, shadows, and animation durations are defined here.
 * Themes should override these values through the Theme system.
 */
public final class DesignTokens {

    // Prevent instantiation
    private DesignTokens() {}

    // ============================================================
    // COLOR PALETTE - Obsidian Purple (Default Theme)
    // ============================================================

    // Backgrounds
    public static final int COLOR_MAIN_BACKGROUND = 0xFF11121A;
    public static final int COLOR_SIDEBAR_BACKGROUND = 0xFF15161F;
    public static final int COLOR_PANEL_BACKGROUND = 0xFF1C1E29;
    public static final int COLOR_MODULE_CARD = 0xFF20222E;
    public static final int COLOR_HOVER_BACKGROUND = 0xFF292B39;

    // Accents
    public static final int COLOR_PRIMARY_PURPLE = 0xFF8B5CF6;
    public static final int COLOR_SECONDARY_PURPLE = 0xFFA78BFA;
    public static final int COLOR_BRIGHT_PURPLE = 0xFFB56CFF;

    // Text
    public static final int COLOR_PRIMARY_TEXT = 0xFFF5F5F7;
    public static final int COLOR_SECONDARY_TEXT = 0xFFA1A1B5;
    public static final int COLOR_MUTED_TEXT = 0xFF77798C;

    // Borders & Dividers
    public static final int COLOR_BORDER = 0xFF343644;
    public static final int COLOR_DISABLED = 0xFF454755;

    // Status Colors
    public static final int COLOR_SUCCESS = 0xFF4CBF80;
    public static final int COLOR_WARNING = 0xFFF3AB4B;
    public static final int COLOR_ERROR = 0xFFE65B7A;

    // Gradients (start, end)
    public static final int GRADIENT_PRIMARY_START = COLOR_PRIMARY_PURPLE;
    public static final int GRADIENT_PRIMARY_END = COLOR_BRIGHT_PURPLE;
    public static final int GRADIENT_SIDEBAR_START = COLOR_SIDEBAR_BACKGROUND;
    public static final int GRADIENT_SIDEBAR_END = COLOR_PANEL_BACKGROUND;

    // Glow & Shadow
    public static final int COLOR_GLOW_PURPLE = 0x408B5CF6; // 25% opacity
    public static final int COLOR_SHADOW = 0x80000000; // 50% black

    // ============================================================
    // CORNER RADII
    // ============================================================

    public static final float RADIUS_SMALL = 4f;
    public static final float RADIUS_MEDIUM = 8f;
    public static final float RADIUS_LARGE = 12f;
    public static final float RADIUS_XLARGE = 15f; // Main window
    public static final float RADIUS_FULL = 999f;   // Pills, badges

    // ============================================================
    // SPACING
    // ============================================================

    public static final int SPACING_XS = 4;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 12;
    public static final int SPACING_LG = 16;
    public static final int SPACING_XL = 24;
    public static final int SPACING_XXL = 32;

    // ============================================================
    // BORDERS
    // ============================================================

    public static final float BORDER_THIN = 1f;
    public static final float BORDER_MEDIUM = 2f;

    // ============================================================
    // SHADOWS
    // ============================================================

    public static final int SHADOW_OFFSET_X = 0;
    public static final int SHADOW_OFFSET_Y = 4;
    public static final int SHADOW_BLUR_RADIUS = 12;
    public static final float SHADOW_OPACITY = 0.3f;

    public static final int GLOW_BLUR_RADIUS = 16;
    public static final float GLOW_OPACITY = 0.4f;

    // ============================================================
    // ANIMATIONS
    // ============================================================

    public static final int ANIM_FAST = 120;      // ms
    public static final int ANIM_NORMAL = 200;    // ms
    public static final int ANIM_SLOW = 300;      // ms
    public static final int ANIM_TOAST = 300;     // ms (slide/fade)

    // Easing functions (cubic-bezier approximations)
    public static final float EASE_OUT_QUART = 0.25f;
    public static final float EASE_IN_OUT_QUART = 0.77f;

    // ============================================================
    // TYPOGRAPHY
    // ============================================================

    public static final String FONT_FAMILY = "Inter";
    public static final String FONT_FAMILY_FALLBACK = "Minecraft";

    // Font weights
    public static final int WEIGHT_REGULAR = 400;
    public static final int WEIGHT_MEDIUM = 500;
    public static final int WEIGHT_SEMIBOLD = 600;
    public static final int WEIGHT_BOLD = 700;

    // Font sizes (in pixels, scaled by GUI scale)
    public static final int SIZE_TITLE_MAIN = 21;
    public static final int SIZE_TITLE_CATEGORY = 18;
    public static final int SIZE_TITLE_MODULE = 15;
    public static final int SIZE_LABEL_SIDEBAR = 14;
    public static final int SIZE_DESCRIPTION = 12;
    public static final int SIZE_KEYBIND_BADGE = 12;
    public static final int SIZE_SETTINGS_LABEL = 13;
    public static final int SIZE_HUD = 15;

    // Line height multipliers
    public static final float LINE_HEIGHT_TIGHT = 1.2f;
    public static final float LINE_HEIGHT_NORMAL = 1.5f;
    public static final float LINE_HEIGHT_RELAXED = 1.6f;

    // ============================================================
    // LAYOUT DIMENSIONS (Reference: 1536x864 canvas)
    // ============================================================

    public static final int CLICKGUI_WIDTH = 995;
    public static final int CLICKGUI_HEIGHT = 529;
    public static final int HEADER_HEIGHT = 59;
    public static final int SIDEBAR_WIDTH = 160;
    public static final int MODULE_BROWSER_WIDTH = 490;
    public static final int SETTINGS_INSPECTOR_WIDTH = 330;

    public static final int CARD_WIDTH = 230; // (MODULE_BROWSER_WIDTH - 3*SPACING_MD) / 2
    public static final int CARD_HEIGHT = 90;

    // ============================================================
    // HELPER METHODS
    // ============================================================

    /**
     * Creates a color with alpha applied.
     */
    public static int withAlpha(int color, float alpha) {
        int rgb = color & 0x00FFFFFF;
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        return a | rgb;
    }

    /**
     * Lerps between two colors.
     */
    public static int lerpColor(int start, int end, float t) {
        t = Math.max(0, Math.min(1, t));
        int sr = (start >> 16) & 0xFF;
        int sg = (start >> 8) & 0xFF;
        int sb = start & 0xFF;
        int sa = (start >> 24) & 0xFF;

        int er = (end >> 16) & 0xFF;
        int eg = (end >> 8) & 0xFF;
        int eb = end & 0xFF;
        int ea = (end >> 24) & 0xFF;

        int r = (int) (sr + (er - sr) * t);
        int g = (int) (sg + (eg - sg) * t);
        int b = (int) (sb + (eb - sb) * t);
        int a = (int) (sa + (ea - sa) * t);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Gets a brighter version of a color.
     */
    public static int brighter(int color, float factor) {
        Color c = new Color(color, true);
        return new Color(
            Math.min(255, (int) (c.getRed() * factor)),
            Math.min(255, (int) (c.getGreen() * factor)),
            Math.min(255, (int) (c.getBlue() * factor)),
            c.getAlpha()
        ).getRGB();
    }

    /**
     * Gets a darker version of a color.
     */
    public static int darker(int color, float factor) {
        Color c = new Color(color, true);
        return new Color(
            Math.max(0, (int) (c.getRed() * factor)),
            Math.max(0, (int) (c.getGreen() * factor)),
            Math.max(0, (int) (c.getBlue() * factor)),
            c.getAlpha()
        ).getRGB();
    }
}