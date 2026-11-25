package com.nullcraft.client.gui.font;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import org.lwjgl.stb.STBTruetype;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Glyph cache with texture atlas for efficient font rendering.
 * Manages multiple atlas pages and handles glyph packing.
 */
public class GlyphCache {

    private static final int INITIAL_ATLAS_SIZE = 1024;
    private static final int MAX_ATLAS_SIZE = 4096;
    private static final int GLYPH_PADDING = 2;

    private final FontRenderer fontRenderer;
    private final TextureManager textureManager;
    private final Map<Character, CachedGlyph> glyphCache = new HashMap<>();

    // Atlas management
    private int currentAtlasWidth = INITIAL_ATLAS_SIZE;
    private int currentAtlasHeight = INITIAL_ATLAS_SIZE;
    private int nextX = GLYPH_PADDING;
    private int nextY = GLYPH_PADDING;
    private int rowHeight = 0;
    private Identifier atlasId;
    private int atlasTextureId = -1;

    public GlyphCache(FontRenderer fontRenderer, TextureManager textureManager) {
        this.fontRenderer = fontRenderer;
        this.textureManager = textureManager;
        this.atlasId = Identifier.of("nullcraft", "font_atlas_0");
        createAtlas();
    }

    private void createAtlas() {
        // Create a blank image for the atlas
        NativeImage image = new NativeImage(currentAtlasWidth, currentAtlasHeight, true);
        // Clear to transparent
        for (int i = 0; i < currentAtlasWidth * currentAtlasHeight; i++) {
            image.setColorArgb(i % currentAtlasWidth, i / currentAtlasWidth, 0x00000000);
        }

        // Register texture
        textureManager.registerTexture(atlasId, image);
        atlasTextureId = textureManager.getTextureId(atlasId);
    }

    public CachedGlyph getGlyph(char c) {
        return glyphCache.computeIfAbsent(c, this::loadAndCacheGlyph);
    }

    private CachedGlyph loadAndCacheGlyph(char c) {
        // Get glyph metrics from font renderer
        int glyphIndex = STBTruetype.stbtt_FindGlyphIndex(fontRenderer.getFontInfo(), c);
        if (glyphIndex == 0) {
            return new CachedGlyph(0, 0, 0, 0, 0, 0, 0, 0, 0, fontRenderer.getScale() * 8); // Default advance
        }

        // Get metrics
        int[] advanceWidth = new int[1];
        int[] leftSideBearing = new int[1];
        STBTruetype.stbtt_GetCodepointHMetrics(fontRenderer.getFontInfo(), c, advanceWidth, leftSideBearing);

        int[] x0 = new int[1], y0 = new int[1], x1 = new int[1], y1 = new int[1];
        STBTruetype.stbtt_GetCodepointBitmapBox(fontRenderer.getFontInfo(), c,
            fontRenderer.getScale(), fontRenderer.getScale(), x0, y0, x1, y1);

        int width = x1[0] - x0[0];
        int height = y1[0] - y0[0];

        if (width <= 0 || height <= 0) {
            return new CachedGlyph(0, 0, 0, 0, 0, 0, 0, 0, 0, advanceWidth[0] * fontRenderer.getScale());
        }

        // Check if we need to move to next row
        if (nextX + width + GLYPH_PADDING >= currentAtlasWidth) {
            nextX = GLYPH_PADDING;
            nextY += rowHeight + GLYPH_PADDING;
            rowHeight = 0;
        }

        // Check if we need a new atlas page
        if (nextY + height + GLYPH_PADDING >= currentAtlasHeight) {
            if (currentAtlasWidth < MAX_ATLAS_SIZE) {
                // Resize atlas (would need to recreate)
                resizeAtlas();
            } else {
                // Create new atlas page
                createNewAtlasPage();
            }
        }

        // Render glyph to bitmap
        ByteBuffer bitmap = java.nio.ByteBuffer.allocateDirect(width * height);
        STBTruetype.stbtt_MakeCodepointBitmap(fontRenderer.getFontInfo(), bitmap,
            width, height, width, fontRenderer.getScale(), fontRenderer.getScale(), c);

        // Upload to atlas texture
        uploadToAtlas(nextX, nextY, width, height, bitmap);

        // Calculate UV coordinates
        float u0 = (float) nextX / currentAtlasWidth;
        float v0 = (float) nextY / currentAtlasHeight;
        float u1 = (float) (nextX + width) / currentAtlasWidth;
        float v1 = (float) (nextY + height) / currentAtlasHeight;

        CachedGlyph glyph = new CachedGlyph(
            width, height,
            x0[0], y0[0],
            u0, v0, u1, v1,
            advanceWidth[0] * fontRenderer.getScale(),
            atlasTextureId
        );

        // Advance position
        nextX += width + GLYPH_PADDING;
        rowHeight = Math.max(rowHeight, height);

        return glyph;
    }

    private void uploadToAtlas(int x, int y, int width, int height, ByteBuffer bitmap) {
        // Get the texture and update sub-image
        // In practice, would use glTexSubImage2D
        // For now, placeholder
    }

    private void resizeAtlas() {
        int newSize = Math.min(currentAtlasWidth * 2, MAX_ATLAS_SIZE);
        // Would need to recreate texture and re-upload all glyphs
        // Simplified for now
        currentAtlasWidth = newSize;
        currentAtlasHeight = newSize;
    }

    private void createNewAtlasPage() {
        // Create additional atlas texture
        // For now, reset to beginning (simplified)
        nextX = GLYPH_PADDING;
        nextY = GLYPH_PADDING;
        rowHeight = 0;
    }

    public int getAtlasTextureId() {
        return atlasTextureId;
    }

    public Identifier getAtlasId() {
        return atlasId;
    }

    public void close() {
        glyphCache.clear();
        // Texture cleanup handled by TextureManager
    }

    // ============================================================
    // CACHED GLYPH CLASS
    // ============================================================

    public static class CachedGlyph {
        public final int width, height;
        public final int xOffset, yOffset;
        public final float u0, v0, u1, v1;
        public final float advance;
        public final int textureId;

        public CachedGlyph(int width, int height, int xOffset, int yOffset,
                           float u0, float v0, float u1, float v1,
                           float advance, int textureId) {
            this.width = width;
            this.height = height;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            this.advance = advance;
            this.textureId = textureId;
        }
    }
}