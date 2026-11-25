package com.nullcraft.client.gui.components;

import com.nullcraft.client.core.utils.DesignTokens;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

/**
 * Base class for all GUI components.
 * Provides layout, focus, input handling, and animation support.
 */
public abstract class Component {

    protected int x, y;
    protected int width, height;
    protected boolean visible = true;
    protected boolean enabled = true;
    protected boolean focused = false;
    protected boolean hovered = false;
    protected boolean pressed = false;

    // Animation
    protected float hoverProgress = 0f;
    protected float focusProgress = 0f;
    protected float pressProgress = 0f;
    protected long lastAnimationTime = System.currentTimeMillis();

    // Callbacks
    protected Consumer<Component> onClick;
    protected Consumer<Component> onHover;
    protected Consumer<Component> onFocus;
    protected Consumer<Component> onBlur;

    // Tooltip
    protected String tooltip;

    public Component(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // ============================================================
    // LAYOUT
    // ============================================================

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(int mouseX, int mouseY) {
        return visible && mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    // ============================================================
    // STATE
    // ============================================================

    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public boolean isFocused() { return focused; }
    public void setFocused(boolean focused) {
        if (this.focused != focused) {
            this.focused = focused;
            if (focused && onFocus != null) onFocus.accept(this);
            if (!focused && onBlur != null) onBlur.accept(this);
        }
    }

    public boolean isHovered() { return hovered; }

    public String getTooltip() { return tooltip; }
    public void setTooltip(String tooltip) { this.tooltip = tooltip; }

    // ============================================================
    // CALLBACKS
    // ============================================================

    public void setOnClick(Consumer<Component> onClick) { this.onClick = onClick; }
    public void setOnHover(Consumer<Component> onHover) { this.onHover = onHover; }
    public void setOnFocus(Consumer<Component> onFocus) { this.onFocus = onFocus; }
    public void setOnBlur(Consumer<Component> onBlur) { this.onBlur = onBlur; }

    // ============================================================
    // ANIMATION
    // ============================================================

    protected void updateAnimations() {
        long now = System.currentTimeMillis();
        float delta = (now - lastAnimationTime) / 1000f;
        lastAnimationTime = now;

        float speed = 8f * delta; // 8 units per second

        // Hover animation
        float targetHover = (hovered && enabled) ? 1f : 0f;
        hoverProgress = MathHelper.lerp(speed, hoverProgress, targetHover);

        // Focus animation
        float targetFocus = focused ? 1f : 0f;
        focusProgress = MathHelper.lerp(speed, focusProgress, targetFocus);

        // Press animation
        float targetPress = pressed ? 1f : 0f;
        pressProgress = MathHelper.lerp(speed * 2, pressProgress, targetPress);
    }

    public float getHoverProgress() { return hoverProgress; }
    public float getFocusProgress() { return focusProgress; }
    public float getPressProgress() { return pressProgress; }

    // ============================================================
    // INPUT HANDLING
    // ============================================================

    public boolean onMouseMove(int mouseX, int mouseY) {
        boolean wasHovered = hovered;
        hovered = contains(mouseX, mouseY);
        if (hovered != wasHovered && hovered && onHover != null) {
            onHover.accept(this);
        }
        return hovered;
    }

    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        if (!enabled || !visible) return false;
        if (contains(mouseX, mouseY) && button == 0) {
            pressed = true;
            if (onClick != null) onClick.accept(this);
            return true;
        }
        return false;
    }

    public boolean onMouseRelease(int mouseX, int mouseY, int button) {
        if (pressed && button == 0) {
            pressed = false;
            return true;
        }
        pressed = false;
        return false;
    }

    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean onCharTyped(char chr, int modifiers) {
        return false;
    }

    // ============================================================
    // RENDERING
    // ============================================================

    public abstract void render(DrawContext context, FontRenderer fontRenderer, float tickDelta);

    // ============================================================
    // HELPER METHODS
    // ============================================================

    protected int getBackgroundColor() {
        int base = DesignTokens.COLOR_PANEL_BACKGROUND;
        int hover = DesignTokens.COLOR_HOVER_BACKGROUND;
        return DesignTokens.lerpColor(base, hover, hoverProgress);
    }

    protected int getBorderColor() {
        int base = DesignTokens.COLOR_BORDER;
        int focus = DesignTokens.COLOR_PRIMARY_PURPLE;
        return DesignTokens.lerpColor(base, focus, focusProgress);
    }

    protected float getCornerRadius() {
        return DesignTokens.RADIUS_MEDIUM;
    }

    protected void renderBackground(DrawContext context) {
        RoundedRect.render(context, x, y, width, height, getCornerRadius(), getBackgroundColor());
    }

    protected void renderBorder(DrawContext context) {
        RoundedRect.renderOutline(context, x, y, width, height, getCornerRadius(), DesignTokens.BORDER_THIN, getBorderColor());
    }

    protected void renderGlow(DrawContext context) {
        if (focusProgress > 0.01f) {
            int glowColor = DesignTokens.withAlpha(DesignTokens.COLOR_GLOW_PURPLE, focusProgress * 0.5f);
            RoundedRect.renderGlow(context, x, y, width, height, getCornerRadius() + 4, glowColor, focusProgress);
        }
    }
}