package com.nullcraft.client.gui.themeeditor;

import com.google.gson.JsonObject;
import com.nullcraft.client.core.utils.DesignTokens;

import java.util.HashMap;
import java.util.Map;

/**
 * Theme class for managing design tokens with live preview support.
 */
public class Theme {

    public enum ThemeType {
        DEFAULT("Default", 0xFF1A1A2E, 0xFF6B46C1),
        DARK("Dark", 0xFF0D0D12, 0xFF8B5CF6),
        MIDNIGHT("Midnight", 0xFF0A0A1A, 0xFF3B82F6),
        VIOLET("Violet", 0xFF1E0A2E, 0xFFA855F7),
        EMERALD("Emerald", 0xFF0A1E1A, 0xFF10B981),
        ROSE("Rose", 0xFF2E0A1A, 0xFFF43F5E),
        AMBER("Amber", 0xFF2E1A0A, 0xFFF59E0B),
        CUSTOM("Custom", 0xFF1A1A2E, 0xFF6B46C1);

        private final String name;
        private final int backgroundColor;
        private final int accentColor;

        ThemeType(String name, int backgroundColor, int accentColor) {
            this.name = name;
            this.backgroundColor = backgroundColor;
            this.accentColor = accentColor;
        }

        public String getName() { return name; }
        public int getBackgroundColor() { return backgroundColor; }
        public int getAccentColor() { return accentColor; }
    }

    private final String name;
    private final ThemeType type;
    private final Map<Integer, Integer> colorOverrides = new HashMap<>();
    private final Map<Integer, Float> floatOverrides = new HashMap<>();
    private final Map<Integer, Integer> intOverrides = new HashMap<>();

    public Theme(String name, ThemeType type) {
        this.name = name;
        this.type = type;
        applyThemeDefaults(type);
    }

    private void applyThemeDefaults(ThemeType type) {
        // Base colors from DesignTokens
        setColor(DesignTokens.COLOR_BACKGROUND, type.getBackgroundColor());
        setColor(DesignTokens.COLOR_PANEL_BACKGROUND, darken(type.getBackgroundColor(), 0.1f));
        setColor(DesignTokens.COLOR_SIDEBAR_BACKGROUND, darken(type.getBackgroundColor(), 0.15f));
        setColor(DesignTokens.COLOR_CARD_BACKGROUND, lighten(type.getBackgroundColor(), 0.05f));
        setColor(DesignTokens.COLOR_HOVER_BACKGROUND, lighten(type.getBackgroundColor(), 0.1f));

        setColor(DesignTokens.COLOR_PRIMARY_TEXT, 0xFFFFFFFF);
        setColor(DesignTokens.COLOR_SECONDARY_TEXT, 0xFFB0B0C8);
        setColor(DesignTokens.COLOR_MUTED_TEXT, 0xFF6B6B8A);
        setColor(DesignTokens.COLOR_DISABLED, 0xFF4A4A6A);

        setColor(DesignTokens.COLOR_PRIMARY_PURPLE, type.getAccentColor());
        setColor(DesignTokens.COLOR_ACCENT_GREEN, 0xFF10B981);
        setColor(DesignTokens.COLOR_ACCENT_RED, 0xFFF43F5E);
        setColor(DesignTokens.COLOR_ACCENT_YELLOW, 0xFFF59E0B);
        setColor(DesignTokens.COLOR_ACCENT_CYAN, 0xFF06B6D4);

        setColor(DesignTokens.COLOR_BORDER, 0xFF2A2A4A);
        setColor(DesignTokens.COLOR_BORDER_HOVER, type.getAccentColor());

        // Shadows
        setColor(DesignTokens.SHADOW_GLOW_ALPHA, 0x33000000 | (type.getAccentColor() & 0x00FFFFFF));
        setColor(DesignTokens.SHADOW_DROP_ALPHA, 0x40000000);

        // Radii
        setFloat(DesignTokens.RADIUS_SMALL, 6f);
        setFloat(DesignTokens.RADIUS_MEDIUM, 10f);
        setFloat(DesignTokens.RADIUS_LARGE, 16f);
        setFloat(DesignTokens.RADIUS_XL, 24f);

        // Spacing
        setInt(DesignTokens.SPACING_XS, 4);
        setInt(DesignTokens.SPACING_SM, 8);
        setInt(DesignTokens.SPACING_MD, 16);
        setInt(DesignTokens.SPACING_LG, 24);
        setInt(DesignTokens.SPACING_XL, 32);

        // Shadows
        setFloat(DesignTokens.SHADOW_BLUR, 16f);
        setFloat(DesignTokens.SHADOW_SPREAD, 4f);

        // Animations
        setFloat(DesignTokens.ANIM_FAST, 0.1f);
        setFloat(DesignTokens.ANIM_NORMAL, 0.2f);
        setFloat(DesignTokens.ANIM_SLOW, 0.3f);
        setFloat(DesignTokens.ANIM_HOVER, 0.15f);
        setFloat(DesignTokens.ANIM_PRESS, 0.05f);

        // Typography
        setInt(DesignTokens.TYPOGRAPHY_BASE_SIZE, 14);
        setInt(DesignTokens.TYPOGRAPHY_SMALL_SIZE, 12);
        setInt(DesignTokens.TYPOGRAPHY_LARGE_SIZE, 18);
        setInt(DesignTokens.TYPOGRAPHY_LINE_HEIGHT, 24);

        // Layout
        setInt(DesignTokens.LAYOUT_PANEL_WIDTH, 900);
        setInt(DesignTokens.LAYOUT_PANEL_HEIGHT, 600);
        setInt(DesignTokens.LAYOUT_SIDEBAR_WIDTH, 220);
        setInt(DesignTokens.LAYOUT_HEADER_HEIGHT, 56);
        setInt(DesignTokens.BORDER_THIN, 1);
        setInt(DesignTokens.BORDER_THICK, 2);
    }

    private int darken(int color, float factor) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        r = (int) (r * (1 - factor));
        g = (int) (g * (1 - factor));
        b = (int) (b * (1 - factor));
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }

    private int lighten(int color, float factor) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        r = (int) (r + (255 - r) * factor);
        g = (int) (g + (255 - g) * factor);
        b = (int) (b + (255 - b) * factor);
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public String getName() { return name; }
    public ThemeType getType() { return type; }

    public int getColor(int tokenId) {
        return colorOverrides.getOrDefault(tokenId, getDefaultColor(tokenId));
    }

    public float getFloat(int tokenId) {
        return floatOverrides.getOrDefault(tokenId, getDefaultFloat(tokenId));
    }

    public int getInt(int tokenId) {
        return intOverrides.getOrDefault(tokenId, getDefaultInt(tokenId));
    }

    private int getDefaultColor(int tokenId) {
        // Fallback to DesignTokens
        return switch (tokenId) {
            case 0xFF1A1A2E -> DesignTokens.COLOR_BACKGROUND; // Not a token ID, this is a value
            default -> 0xFF1A1A2E;
        };
    }

    private float getDefaultFloat(int tokenId) {
        return switch (tokenId) {
            case 6 -> DesignTokens.RADIUS_SMALL;
            case 10 -> DesignTokens.RADIUS_MEDIUM;
            case 16 -> DesignTokens.RADIUS_LARGE;
            case 24 -> DesignTokens.RADIUS_XL;
            case 16 -> DesignTokens.SHADOW_BLUR;
            case 4 -> DesignTokens.SHADOW_SPREAD;
            case 0 -> DesignTokens.ANIM_FAST;
            default -> 0f;
        };
    }

    private int getDefaultInt(int tokenId) {
        return switch (tokenId) {
            case 4 -> DesignTokens.SPACING_XS;
            case 8 -> DesignTokens.SPACING_SM;
            case 16 -> DesignTokens.SPACING_MD;
            case 24 -> DesignTokens.SPACING_LG;
            case 32 -> DesignTokens.SPACING_XL;
            case 14 -> DesignTokens.TYPOGRAPHY_BASE_SIZE;
            case 12 -> DesignTokens.TYPOGRAPHY_SMALL_SIZE;
            case 18 -> DesignTokens.TYPOGRAPHY_LARGE_SIZE;
            case 24 -> DesignTokens.TYPOGRAPHY_LINE_HEIGHT;
            case 900 -> DesignTokens.LAYOUT_PANEL_WIDTH;
            case 600 -> DesignTokens.LAYOUT_PANEL_HEIGHT;
            case 220 -> DesignTokens.LAYOUT_SIDEBAR_WIDTH;
            case 56 -> DesignTokens.LAYOUT_HEADER_HEIGHT;
            case 1 -> DesignTokens.BORDER_THIN;
            case 2 -> DesignTokens.BORDER_THICK;
            default -> 0;
        };
    }

    // ============================================================
    // SETTERS
    // ============================================================

    public void setColor(int tokenId, int color) {
        colorOverrides.put(tokenId, color);
    }

    public void setFloat(int tokenId, float value) {
        floatOverrides.put(tokenId, value);
    }

    public void setInt(int tokenId, int value) {
        intOverrides.put(tokenId, value);
    }

    // ============================================================
    // SERIALIZATION
    // ============================================================

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("type", type.name());

        JsonObject colors = new JsonObject();
        for (Map.Entry<Integer, Integer> entry : colorOverrides.entrySet()) {
            colors.addProperty(entry.getKey().toString(), entry.getValue());
        }
        json.add("colors", colors);

        JsonObject floats = new JsonObject();
        for (Map.Entry<Integer, Float> entry : floatOverrides.entrySet()) {
            floats.addProperty(entry.getKey().toString(), entry.getValue());
        }
        json.add("floats", floats);

        JsonObject ints = new JsonObject();
        for (Map.Entry<Integer, Integer> entry : intOverrides.entrySet()) {
            ints.addProperty(entry.getKey().toString(), entry.getValue());
        }
        json.add("ints", ints);

        return json;
    }

    public static Theme fromJson(JsonObject json) {
        String name = json.get("name").getAsString();
        ThemeType type = ThemeType.valueOf(json.get("type").getAsString());
        Theme theme = new Theme(name, type);

        if (json.has("colors")) {
            JsonObject colors = json.getAsJsonObject("colors");
            for (String key : colors.keySet()) {
                theme.setColor(Integer.parseInt(key), colors.get(key).getAsInt());
            }
        }
        if (json.has("floats")) {
            JsonObject floats = json.getAsJsonObject("floats");
            for (String key : floats.keySet()) {
                theme.setFloat(Integer.parseInt(key), floats.get(key).getAsFloat());
            }
        }
        if (json.has("ints")) {
            JsonObject ints = json.getAsJsonObject("ints");
            for (String key : ints.keySet()) {
                theme.setInt(Integer.parseInt(key), ints.get(key).getAsInt());
            }
        }

        return theme;
    }
}