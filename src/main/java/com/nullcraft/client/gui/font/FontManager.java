package com.nullcraft.client.gui.font;

import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Font manager for loading and managing multiple font weights.
 * Loads Inter font family (Regular, Medium, SemiBold, Bold) at build time.
 */
public class FontManager {

    private static FontManager instance;
    private final Map<FontWeight, FontRenderer> fonts = new HashMap<>();
    private final Map<FontWeight, Float> fontSizes = new HashMap<>();
    private float baseFontSize = DesignTokens.TYPOGRAPHY_BASE_SIZE;
    private float scale = 1.0f;

    public enum FontWeight {
        REGULAR(400, "Inter-Regular.ttf"),
        MEDIUM(500, "Inter-Medium.ttf"),
        SEMI_BOLD(600, "Inter-SemiBold.ttf"),
        BOLD(700, "Inter-Bold.ttf");

        final int weight;
        final String fileName;

        FontWeight(int weight, String fileName) {
            this.weight = weight;
            this.fileName = fileName;
        }

        public int getWeight() { return weight; }
        public String getFileName() { return fileName; }
    }

    private FontManager() {}

    public static FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }

    public void initialize(ResourceManager resourceManager) {
        for (FontWeight weight : FontWeight.values()) {
            try {
                loadFont(resourceManager, weight);
            } catch (IOException e) {
                System.err.println("Failed to load font: " + weight.getFileName() + " - " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void loadFont(ResourceManager resourceManager, FontWeight weight) throws IOException {
        Identifier fontId = Identifier.of("nullcraft", "fonts/" + weight.getFileName());
        try (InputStream stream = resourceManager.getResource(fontId).orElseThrow(
                () -> new IOException("Font not found: " + fontId)).getInputStream()) {
            FontRenderer renderer = new FontRenderer(stream, baseFontSize * scale);
            fonts.put(weight, renderer);
            fontSizes.put(weight, baseFontSize * scale);
        }
    }

    public FontRenderer getFont(FontWeight weight) {
        return fonts.getOrDefault(weight, fonts.get(FontWeight.REGULAR));
    }

    public FontRenderer getRegular() { return getFont(FontWeight.REGULAR); }
    public FontRenderer getMedium() { return getFont(FontWeight.MEDIUM); }
    public FontRenderer getSemiBold() { return getFont(FontWeight.SEMI_BOLD); }
    public FontRenderer getBold() { return getFont(FontWeight.BOLD); }

    public void setBaseFontSize(float size) {
        this.baseFontSize = size;
        reloadFonts();
    }

    public void setScale(float scale) {
        this.scale = scale;
        reloadFonts();
    }

    public float getBaseFontSize() { return baseFontSize; }
    public float getScale() { return scale; }

    private void reloadFonts() {
        close();
        // Would reload from resource manager
    }

    public void close() {
        for (FontRenderer renderer : fonts.values()) {
            renderer.close();
        }
        fonts.clear();
    }

    // ============================================================
    // CONVENIENCE METHODS
    // ============================================================

    public int getStringWidth(String text) {
        return getRegular().getStringWidth(text);
    }

    public int getStringWidth(String text, FontWeight weight) {
        return getFont(weight).getStringWidth(text);
    }

    public int getFontHeight() {
        return getRegular().getFontHeight();
    }

    public int getFontHeight(FontWeight weight) {
        return getFont(weight).getFontHeight();
    }

    public void draw(DrawContext context, String text, int x, int y, int color) {
        getRegular().draw(context, text, x, y, color);
    }

    public void draw(DrawContext context, String text, int x, int y, int color, FontWeight weight) {
        getFont(weight).draw(context, text, x, y, color);
    }

    public void drawCentered(DrawContext context, String text, int x, int y, int color) {
        getRegular().drawCentered(context, text, x, y, color);
    }

    public void drawWithShadow(DrawContext context, String text, int x, int y, int color) {
        getRegular().drawWithShadow(context, text, x, y, color);
    }

    // Interface for DrawContext to avoid circular dependency
    public interface DrawContext {
        void drawTextWithShadow(net.minecraft.client.font.TextRenderer renderer, String text, int x, int y, int color);
        net.minecraft.client.gui.DrawContext getMatrices();
    }
}