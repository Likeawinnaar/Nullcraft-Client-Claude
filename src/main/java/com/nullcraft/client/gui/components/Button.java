package com.nullcraft.client.gui.components;

import com.nullcraft.client.core.utils.DesignTokens;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import net.minecraft.client.gui.DrawContext;

/**
 * Button component with click handling and animations.
 */
public class Button extends Component {

    private final String text;
    private final String iconId;
    private int textColor = DesignTokens.COLOR_PRIMARY_TEXT;

    public Button(int x, int y, int width, int height, String text) {
        this(x, y, width, height, text, null);
    }

    public Button(int x, int y, int width, int height, String text, String iconId) {
        super(x, y, width, height);
        this.text = text;
        this.iconId = iconId;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta) {
        if (!visible) return;

        updateAnimations();

        // Background
        int bgColor = getBackgroundColor();
        if (pressed) {
            bgColor = DesignTokens.darker(bgColor, 0.8f);
        }
        RoundedRect.render(context, x, y, width, height, DesignTokens.RADIUS_SMALL, bgColor);

        // Border
        int borderColor = getBorderColor();
        RoundedRect.renderOutline(context, x, y, width, height, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN, borderColor);

        // Glow
        renderGlow(context);

        // Text
        int tx = x + width / 2;
        int ty = y + height / 2 - fontRenderer.getFontHeight() / 2;

        if (iconId != null) {
            // Render icon + text
            // For now just render text
            fontRenderer.drawCentered(context, text, tx, ty, textColor);
        } else {
            fontRenderer.drawCentered(context, text, tx, ty, textColor);
        }
    }
}