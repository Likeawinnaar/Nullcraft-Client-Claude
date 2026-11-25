package com.nullcraft.client.gui.hud;

import com.nullcraft.client.core.settings.*;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Base class for HUD elements.
 * Supports drag-drop positioning, scaling, opacity, and customization.
 */
public abstract class HudElement {

    private final String id;
    private final String name;
    private final String description;

    // Position (0-1 normalized, or absolute pixels)
    private double x = 0.5; // Normalized 0-1
    private double y = 0.5;
    private boolean useNormalizedCoords = true;

    // Appearance
    private double scale = 1.0;
    private double opacity = 1.0;
    private boolean visible = true;
    private boolean locked = false;

    // Background styling
    private boolean showBackground = true;
    private int backgroundColor = DesignTokens.COLOR_PANEL_BACKGROUND;
    private boolean showBorder = true;
    private int borderColor = DesignTokens.COLOR_BORDER;
    private double borderWidth = DesignTokens.BORDER_THIN;
    private double cornerRadius = DesignTokens.RADIUS_SMALL;

    // Shadow/glow
    private boolean showShadow = true;
    private int shadowColor = 0x80000000;
    private double shadowOffset = 4;
    private double shadowBlur = 8;

    // Animation
    private double animationProgress = 0;
    private double targetAnimationProgress = 0;

    // Runtime
    private final UUID instanceId = UUID.randomUUID();

    protected HudElement(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // ============================================================
    // ABSTRACT METHODS
    // ============================================================

    /**
     * Render the HUD element.
     * @param context Draw context
     * @param fontRenderer Font renderer
     * @param tickDelta Partial tick for animation
     * @param x Screen X position (pixels)
     * @param y Screen Y position (pixels)
     * @param width Element width (pixels)
     * @param height Element height (pixels)
     */
    public abstract void render(DrawContext context, FontRenderer fontRenderer, float tickDelta,
                                int x, int y, int width, int height);

    /**
     * Get the preferred width of this element.
     */
    public abstract int getPreferredWidth(FontRenderer fontRenderer);

    /**
     * Get the preferred height of this element.
     */
    public abstract int getPreferredHeight(FontRenderer fontRenderer);

    /**
     * Create settings for this HUD element.
     */
    public abstract List<Setting<?>> createSettings();

    // ============================================================
    // IDENTITY
    // ============================================================

    public final String getId() { return id; }
    public final String getName() { return name; }
    public final String getDescription() { return description; }
    public final UUID getInstanceId() { return instanceId; }

    // ============================================================
    // POSITION
    // ============================================================

    public double getX() { return x; }
    public void setX(double x) { this.x = Math.max(0, Math.min(1, x)); }

    public double getY() { return y; }
    public void setY(double y) { this.y = Math.max(0, Math.min(1, y)); }

    public boolean isUseNormalizedCoords() { return useNormalizedCoords; }
    public void setUseNormalizedCoords(boolean normalized) { this.useNormalizedCoords = normalized; }

    public int getScreenX(int screenWidth) {
        return useNormalizedCoords ? (int) (x * screenWidth) : (int) x;
    }

    public int getScreenY(int screenHeight) {
        return useNormalizedCoords ? (int) (y * screenHeight) : (int) y;
    }

    public void setScreenPosition(int screenX, int screenY, int screenWidth, int screenHeight) {
        if (useNormalizedCoords) {
            this.x = Math.max(0, Math.min(1, (double) screenX / screenWidth));
            this.y = Math.max(0, Math.min(1, (double) screenY / screenHeight));
        } else {
            this.x = screenX;
            this.y = screenY;
        }
    }

    // ============================================================
    // APPEARANCE
    // ============================================================

    public double getScale() { return scale; }
    public void setScale(double scale) { this.scale = Math.max(0.1, Math.min(5.0, scale)); }

    public double getOpacity() { return opacity; }
    public void setOpacity(double opacity) { this.opacity = Math.max(0, Math.min(1, opacity)); }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }

    public boolean isShowBackground() { return showBackground; }
    public void setShowBackground(boolean show) { this.showBackground = show; }

    public int getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(int color) { this.backgroundColor = color; }

    public boolean isShowBorder() { return showBorder; }
    public void setShowBorder(boolean show) { this.showBorder = show; }

    public int getBorderColor() { return borderColor; }
    public void setBorderColor(int color) { this.borderColor = color; }

    public double getBorderWidth() { return borderWidth; }
    public void setBorderWidth(double width) { this.borderWidth = Math.max(0, Math.min(10, width)); }

    public double getCornerRadius() { return cornerRadius; }
    public void setCornerRadius(double radius) { this.cornerRadius = Math.max(0, Math.min(50, radius)); }

    public boolean isShowShadow() { return showShadow; }
    public void setShowShadow(boolean show) { this.showShadow = show; }

    public int getShadowColor() { return shadowColor; }
    public void setShadowColor(int color) { this.shadowColor = color; }

    public double getShadowOffset() { return shadowOffset; }
    public void setShadowOffset(double offset) { this.shadowOffset = Math.max(0, Math.min(50, offset)); }

    public double getShadowBlur() { return shadowBlur; }
    public void setShadowBlur(double blur) { this.shadowBlur = Math.max(0, Math.min(50, blur)); }

    // ============================================================
    // ANIMATION
    // ============================================================

    public double getAnimationProgress() { return animationProgress; }
    public void setAnimationProgress(double progress) { this.animationProgress = progress; }
    public double getTargetAnimationProgress() { return targetAnimationProgress; }
    public void setTargetAnimationProgress(double progress) { this.targetAnimationProgress = progress; }

    public void updateAnimation(float delta) {
        animationProgress += (targetAnimationProgress - animationProgress) * delta * 10;
    }

    // ============================================================
    // CONFIGURATION
    // ============================================================

    public com.google.gson.JsonObject toJson() {
        com.google.gson.JsonObject json = new com.google.gson.JsonObject();
        json.addProperty("id", id);
        json.addProperty("x", x);
        json.addProperty("y", y);
        json.addProperty("normalized", useNormalizedCoords);
        json.addProperty("scale", scale);
        json.addProperty("opacity", opacity);
        json.addProperty("visible", visible);
        json.addProperty("locked", locked);
        json.addProperty("showBackground", showBackground);
        json.addProperty("backgroundColor", backgroundColor);
        json.addProperty("showBorder", showBorder);
        json.addProperty("borderColor", borderColor);
        json.addProperty("borderWidth", borderWidth);
        json.addProperty("cornerRadius", cornerRadius);
        json.addProperty("showShadow", showShadow);
        json.addProperty("shadowColor", shadowColor);
        json.addProperty("shadowOffset", shadowOffset);
        json.addProperty("shadowBlur", shadowBlur);
        return json;
    }

    public void fromJson(com.google.gson.JsonObject json) {
        if (json.has("x")) x = json.get("x").getAsDouble();
        if (json.has("y")) y = json.get("y").getAsDouble();
        if (json.has("normalized")) useNormalizedCoords = json.get("normalized").getAsBoolean();
        if (json.has("scale")) scale = json.get("scale").getAsDouble();
        if (json.has("opacity")) opacity = json.get("opacity").getAsDouble();
        if (json.has("visible")) visible = json.get("visible").getAsBoolean();
        if (json.has("locked")) locked = json.get("locked").getAsBoolean();
        if (json.has("showBackground")) showBackground = json.get("showBackground").getAsBoolean();
        if (json.has("backgroundColor")) backgroundColor = json.get("backgroundColor").getAsInt();
        if (json.has("showBorder")) showBorder = json.get("showBorder").getAsBoolean();
        if (json.has("borderColor")) borderColor = json.get("borderColor").getAsInt();
        if (json.has("borderWidth")) borderWidth = json.get("borderWidth").getAsDouble();
        if (json.has("cornerRadius")) cornerRadius = json.get("cornerRadius").getAsDouble();
        if (json.has("showShadow")) showShadow = json.get("showShadow").getAsBoolean();
        if (json.has("shadowColor")) shadowColor = json.get("shadowColor").getAsInt();
        if (json.has("shadowOffset")) shadowOffset = json.get("shadowOffset").getAsDouble();
        if (json.has("shadowBlur")) shadowBlur = json.get("shadowBlur").getAsDouble();
    }
}