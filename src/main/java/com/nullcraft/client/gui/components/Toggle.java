package com.nullcraft.client.gui.components;

import com.nullcraft.client.core.utils.DesignTokens;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import net.minecraft.client.gui.DrawContext;

/**
 * Toggle switch component for boolean settings.
 */
public class Toggle extends Component {

    private final String label;
    private boolean value;
    private final Runnable onChange;

    private float thumbProgress = 0f;

    public Toggle(int x, int y, int width, int height, String label, boolean initialValue, Runnable onChange) {
        super(x, y, width, height);
        this.label = label;
        this.value = initialValue;
        this.onChange = onChange;
        this.thumbProgress = initialValue ? 1f : 0f;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public void updateAnimations() {
        super.updateAnimations();

        float targetThumb = value ? 1f : 0f;
        thumbProgress = net.minecraft.util.math.MathHelper.lerp(0.2f, thumbProgress, targetThumb);
    }

    @Override
    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        if (!enabled || !visible) return false;
        if (contains(mouseX, mouseY) && button == 0) {
            value = !value;
            thumbProgress = value ? 1f : 0f;
            if (onChange != null) onChange.run();
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta) {
        if (!visible) return;

        updateAnimations();

        int trackHeight = 20;
        int trackWidth = 40;
        int trackX = x + width - trackWidth - 8;
        int trackY = y + (height - trackHeight) / 2;
        int thumbSize = trackHeight - 4;
        int thumbX = trackX + 2 + (int) (thumbProgress * (trackWidth - thumbSize - 4));
        int thumbY = trackY + 2;

        // Label
        int labelColor = enabled ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_DISABLED;
        fontRenderer.draw(context, label, x, y + (height - fontRenderer.getFontHeight()) / 2, labelColor);

        // Track
        int trackBg = value ? DesignTokens.COLOR_PRIMARY_PURPLE : DesignTokens.COLOR_DISABLED;
        int trackColor = DesignTokens.lerpColor(DesignTokens.COLOR_DISABLED, trackBg, thumbProgress);
        RoundedRect.render(context, trackX, trackY, trackWidth, trackHeight, trackHeight / 2f, trackColor);

        // Thumb
        int thumbColor = DesignTokens.COLOR_PRIMARY_TEXT;
        RoundedRect.render(context, thumbX, thumbY, thumbSize, thumbSize, thumbSize / 2f, thumbColor);

        // Thumb indicator
        if (value) {
            // Checkmark or dot
            context.fill(thumbX + thumbSize / 2 - 1, thumbY + thumbSize / 2 - 1, thumbX + thumbSize / 2 + 1, thumbY + thumbSize / 2 + 1, DesignTokens.COLOR_PRIMARY_PURPLE);
        }
    }
}