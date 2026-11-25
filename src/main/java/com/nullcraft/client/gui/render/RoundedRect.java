package com.nullcraft.client.gui.render;

import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import org.joml.Matrix4f;

/**
 * Utility class for rendering rounded rectangles, glows, shadows, and gradients.
 */
public final class RoundedRect {

    private RoundedRect() {}

    // ============================================================
    // FILLED ROUNDED RECT
    // ============================================================

    /**
     * Renders a filled rounded rectangle.
     */
    public static void render(DrawContext context, int x, int y, int width, int height, float radius, int color) {
        if (width <= 0 || height <= 0) return;

        radius = Math.min(radius, Math.min(width, height) / 2f);
        if (radius <= 0) {
            context.fill(x, y, x + width, y + height, color);
            return;
        }

        // Use DrawContext's built-in rounded rect if available, otherwise manual
        // For now, use a simplified approach with multiple fills
        int r = (int) radius;

        // Center rect
        context.fill(x + r, y, x + width - r, y + height, color);

        // Left and right strips
        context.fill(x, y + r, x + r, y + height - r, color);
        context.fill(x + width - r, y + r, x + width, y + height - r, color);

        // Four corners (approximated with fills)
        for (int i = 0; i < r; i++) {
            int cornerHeight = (int) Math.sqrt(r * r - (r - i) * (r - i));
            // Top-left
            context.fill(x + r - i - 1, y + r - cornerHeight, x + r - i, y + r, color);
            // Top-right
            context.fill(x + width - r + i, y + r - cornerHeight, x + width - r + i + 1, y + r, color);
            // Bottom-left
            context.fill(x + r - i - 1, y + height - r, x + r - i, y + height - r + cornerHeight, color);
            // Bottom-right
            context.fill(x + width - r + i, y + height - r, x + width - r + i + 1, y + height - r + cornerHeight, color);
        }
    }

    /**
     * Renders a rounded rectangle outline.
     */
    public static void renderOutline(DrawContext context, int x, int y, int width, int height, float radius, float thickness, int color) {
        if (width <= 0 || height <= 0) return;

        radius = Math.min(radius, Math.min(width, height) / 2f);
        int r = (int) radius;
        int t = (int) Math.ceil(thickness);

        // Top and bottom
        context.fill(x + r, y, x + width - r, y + t, color);
        context.fill(x + r, y + height - t, x + width - r, y + height, color);

        // Left and right
        context.fill(x, y + r, x + t, y + height - r, color);
        context.fill(x + width - t, y + r, x + width, y + height - r, color);

        // Corners (simplified)
        for (int i = 0; i < r; i++) {
            int cornerHeight = (int) Math.sqrt(r * r - (r - i) * (r - i));
            // Top-left
            context.fill(x + r - i - 1, y + r - cornerHeight, x + r - i, y + r - cornerHeight + t, color);
            // Top-right
            context.fill(x + width - r + i, y + r - cornerHeight, x + width - r + i + 1, y + r - cornerHeight + t, color);
            // Bottom-left
            context.fill(x + r - i - 1, y + height - r + cornerHeight - t, x + r - i, y + height - r + cornerHeight, color);
            // Bottom-right
            context.fill(x + width - r + i, y + height - r + cornerHeight - t, x + width - r + i + 1, y + height - r + cornerHeight, color);
        }
    }

    // ============================================================
    // GLOW EFFECT
    // ============================================================

    /**
     * Renders a glow effect around a rounded rectangle.
     */
    public static void renderGlow(DrawContext context, int x, int y, int width, int height, float radius, int color, float intensity) {
        if (intensity <= 0.01f) return;

        int layers = 4;
        int baseRadius = (int) radius + 2;

        for (int i = 0; i < layers; i++) {
            float layerIntensity = intensity * (1f - (float) i / layers) * 0.3f;
            int layerRadius = baseRadius + i * 2;
            int layerColor = DesignTokens.withAlpha(color, layerIntensity);
            render(context, x - i, y - i, width + i * 2, height + i * 2, layerRadius, layerColor);
        }
    }

    // ============================================================
    // SHADOW EFFECT
    // ============================================================

    /**
     * Renders a drop shadow for a rounded rectangle.
     */
    public static void renderShadow(DrawContext context, int x, int y, int width, int height, float radius, int shadowColor, int offsetX, int offsetY, float intensity) {
        if (intensity <= 0.01f) return;
        // Simplified shadow - render a slightly offset darker rect
        int shadowX = x + offsetX;
        int shadowY = y + offsetY;
        int shadowColorAdjusted = DesignTokens.withAlpha(shadowColor, intensity * 0.5f);
        render(context, shadowX, shadowY, width, height, radius, shadowColorAdjusted);
    }

    // ============================================================
    // GRADIENT
    // ============================================================

    /**
     * Renders a vertical gradient rounded rectangle.
     */
    public static void renderGradient(DrawContext context, int x, int y, int width, int height, float radius, int colorTop, int colorBottom) {
        if (width <= 0 || height <= 0) return;

        radius = Math.min(radius, Math.min(width, height) / 2f);
        int r = (int) radius;

        // Render as horizontal strips with interpolated colors
        for (int i = 0; i < height; i++) {
            float t = (float) i / height;
            int color = DesignTokens.lerpColor(colorTop, colorBottom, t);
            int stripY = y + i;
            int stripHeight = 1;

            // Calculate horizontal bounds for this strip considering rounded corners
            int left = x;
            int right = x + width;

            if (i < r) {
                int cornerWidth = (int) Math.sqrt(r * r - (r - i) * (r - i));
                left = x + r - cornerWidth;
                right = x + width - r + cornerWidth;
            } else if (i >= height - r) {
                int cornerWidth = (int) Math.sqrt(r * r - (r - (height - i)) * (r - (height - i)));
                left = x + r - cornerWidth;
                right = x + width - r + cornerWidth;
            }

            context.fill(left, stripY, right, stripY + 1, color);
        }
    }
}