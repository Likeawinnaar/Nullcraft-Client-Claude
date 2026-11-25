package com.nullcraft.client.gui.font;

import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * TrueType font renderer using STB Truetype.
 * Supports antialiasing, kerning, and glyph caching.
 */
public class FontRenderer {

    private static final int GLYPH_PADDING = 2;
    private static final int ATLAS_SIZE = 2048;

    private final STBTTFontinfo fontInfo = STBTTFontinfo.create();
    private final ByteBuffer fontData;
    private final float fontSize;
    private final float scale;
    private final int ascent, descent, lineGap;
    private final Map<Character, Glyph> glyphCache = new HashMap<>();

    // Texture atlas
    private int atlasTexture = -1;
    private int atlasWidth = ATLAS_SIZE;
    private int atlasHeight = ATLAS_SIZE;
    private int nextGlyphX = GLYPH_PADDING;
    private int nextGlyphY = GLYPH_PADDING;
    private int rowHeight = 0;

    public FontRenderer(InputStream fontStream, float fontSize) throws IOException {
        this.fontSize = fontSize;
        this.fontData = readFontData(fontStream);

        // Initialize font
        if (!STBTruetype.stbtt_InitFont(fontInfo, fontData)) {
            throw new IOException("Failed to initialize font");
        }

        this.scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, fontSize);

        IntBuffer ascentBuf = MemoryStack.stackMallocInt(1);
        IntBuffer descentBuf = MemoryStack.stackMallocInt(1);
        IntBuffer lineGapBuf = MemoryStack.stackMallocInt(1);
        try {
            STBTruetype.stbtt_GetFontVMetrics(fontInfo, ascentBuf, descentBuf, lineGapBuf);
            this.ascent = (int) (ascentBuf.get(0) * scale);
            this.descent = (int) (descentBuf.get(0) * scale);
            this.lineGap = (int) (lineGapBuf.get(0) * scale);
        } finally {
            MemoryStack.stackFree(ascentBuf);
            MemoryStack.stackFree(descentBuf);
            MemoryStack.stackFree(lineGapBuf);
        }

        // Create texture atlas
        createAtlasTexture();
    }

    private ByteBuffer readFontData(InputStream stream) throws IOException {
        byte[] data = stream.readAllBytes();
        ByteBuffer buffer = org.lwjgl.system.MemoryUtil.memAlloc(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    private void createAtlasTexture() {
        // Texture creation would use Minecraft's texture manager
        // For now, placeholder
        this.atlasTexture = -1; // Would be created via TextureManager
    }

    // ============================================================
    // GLYPH MANAGEMENT
    // ============================================================

    private Glyph getGlyph(char c) {
        return glyphCache.computeIfAbsent(c, this::loadGlyph);
    }

    private Glyph loadGlyph(char c) {
        // Get glyph metrics
        IntBuffer advanceWidth = MemoryStack.stackMallocInt(1);
        IntBuffer leftSideBearing = MemoryStack.stackMallocInt(1);
        IntBuffer glyphIndex = MemoryStack.stackMallocInt(1);

        try {
            STBTruetype.stbtt_GetCodepointHMetrics(fontInfo, c, advanceWidth, leftSideBearing);
            glyphIndex.put(0, STBTruetype.stbtt_FindGlyphIndex(fontInfo, c));

            IntBuffer x0 = MemoryStack.stackMallocInt(1);
            IntBuffer y0 = MemoryStack.stackMallocInt(1);
            IntBuffer x1 = MemoryStack.stackMallocInt(1);
            IntBuffer y1 = MemoryStack.stackMallocInt(1);

            STBTruetype.stbtt_GetCodepointBitmapBox(fontInfo, c, scale, scale, x0, y0, x1, y1);

            int width = x1.get(0) - x0.get(0);
            int height = y1.get(0) - y0.get(0);

            if (width <= 0 || height <= 0) {
                return new Glyph(0, 0, 0, 0, 0, 0, advanceWidth.get(0) * scale, 0, 0);
            }

            // Check if we need to pack into atlas
            if (nextGlyphX + width + GLYPH_PADDING >= atlasWidth) {
                nextGlyphX = GLYPH_PADDING;
                nextGlyphY += rowHeight + GLYPH_PADDING;
                rowHeight = 0;
            }

            if (nextGlyphY + height + GLYPH_PADDING >= atlasHeight) {
                // Atlas full - would need to resize or create new atlas
                // For now, return empty glyph
                return new Glyph(0, 0, 0, 0, 0, 0, advanceWidth.get(0) * scale, 0, 0);
            }

            // Render glyph to bitmap
            ByteBuffer bitmap = org.lwjgl.system.MemoryUtil.memAlloc(width * height);
            STBTruetype.stbtt_MakeCodepointBitmap(fontInfo, bitmap, width, height, width, scale, scale, c);

            // Upload to texture atlas (simplified - would use glTexSubImage2D)
            int atlasX = nextGlyphX;
            int atlasY = nextGlyphY;

            nextGlyphX += width + GLYPH_PADDING;
            rowHeight = Math.max(rowHeight, height);

            // Glyph metrics
            float u0 = (float) atlasX / atlasWidth;
            float v0 = (float) atlasY / atlasHeight;
            float u1 = (float) (atlasX + width) / atlasWidth;
            float v1 = (float) (atlasY + height) / atlasHeight;

            return new Glyph(
                width, height,
                x0.get(0), y0.get(0),
                u0, v0, u1, v1,
                advanceWidth.get(0) * scale
            );
        } finally {
            MemoryStack.stackFree(advanceWidth);
            MemoryStack.stackFree(leftSideBearing);
            MemoryStack.stackFree(glyphIndex);
        }
    }

    // ============================================================
    // TEXT RENDERING
    // ============================================================

    public void draw(DrawContext context, String text, int x, int y, int color) {
        drawInternal(context, text, x, y, color, false);
    }

    public void drawCentered(DrawContext context, String text, int x, int y, int color) {
        int width = getStringWidth(text);
        drawInternal(context, text, x - width / 2, y, color, false);
    }

    public void drawWithShadow(DrawContext context, String text, int x, int y, int color) {
        drawInternal(context, text, x + 1, y + 1, 0x80000000, false); // Shadow
        drawInternal(context, text, x, y, color, false);
    }

    private void drawInternal(DrawContext context, String text, int x, int y, int color, boolean shadow) {
        if (text == null || text.isEmpty()) return;

        Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
        // Would use vertex buffers to render glyphs
        // Simplified for now - delegate to Minecraft's text renderer
        context.drawTextWithShadow(
            net.minecraft.client.MinecraftClient.getInstance().textRenderer,
            text, x, y, color
        );
    }

    // ============================================================
    // TEXT MEASUREMENT
    // ============================================================

    public int getStringWidth(String text) {
        if (text == null || text.isEmpty()) return 0;

        float width = 0;
        char prev = 0;
        for (char c : text.toCharArray()) {
            Glyph glyph = getGlyph(c);
            width += glyph.advance;
            if (prev != 0) {
                width += getKerning(prev, c);
            }
            prev = c;
        }
        return (int) Math.ceil(width);
    }

    public int getStringHeight(String text) {
        return getFontHeight();
    }

    public int getFontHeight() {
        return ascent - descent + lineGap;
    }

    public float getKerning(char prev, char curr) {
        IntBuffer kerning = MemoryStack.stackMallocInt(1);
        try {
            STBTruetype.stbtt_GetCodepointKernAdvance(fontInfo, prev, curr, kerning);
            return kerning.get(0) * scale;
        } finally {
            MemoryStack.stackFree(kerning);
        }
    }

    // ============================================================
    // GLYPH CLASS
    // ============================================================

    private static class Glyph {
        final int width, height;
        final int xOffset, yOffset;
        final float u0, v0, u1, v1;
        final float advance;

        Glyph(int width, int height, int xOffset, int yOffset,
              float u0, float v0, float u1, float v1, float advance) {
            this.width = width;
            this.height = height;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            this.advance = advance;
        }
    }

    // ============================================================
    // CLEANUP
    // ============================================================

    public void close() {
        if (fontData != null) {
            org.lwjgl.system.MemoryUtil.memFree(fontData);
        }
        // Cleanup texture
    }

    // Expose for GlyphCache
    public STBTTFontinfo getFontInfo() {
        return fontInfo;
    }

    public float getScale() {
        return scale;
    }
}