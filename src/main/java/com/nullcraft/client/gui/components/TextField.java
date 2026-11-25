package com.nullcraft.client.gui.components;

import com.nullcraft.client.core.utils.DesignTokens;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

/**
 * Text field component for string settings.
 */
public class TextField extends Component {

    private final String label;
    private final String placeholder;
    private String text = "";
    private final int maxLength;
    private final boolean multiline;
    private final Consumer<String> onChange;

    private int cursorPosition = 0;
    private int scrollOffset = 0;
    private long cursorBlinkTime = System.currentTimeMillis();
    private boolean cursorVisible = true;

    public TextField(int x, int y, int width, int height, String label, String placeholder, int maxLength, boolean multiline, Consumer<String> onChange) {
        super(x, y, width, height);
        this.label = label;
        this.placeholder = placeholder;
        this.maxLength = maxLength;
        this.multiline = multiline;
        this.onChange = onChange;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text != null ? text : "";
        this.cursorPosition = this.text.length();
        if (onChange != null) onChange.accept(this.text);
    }

    @Override
    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        if (!enabled || !visible) return false;
        if (contains(mouseX, mouseY) && button == 0) {
            focused = true;
            // Set cursor position based on click
            updateCursorFromMouse(mouseX);
            return true;
        }
        if (focused && button == 0) {
            focused = false;
        }
        return false;
    }

    @Override
    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;

        boolean ctrl = (modifiers & 1) != 0; // Simplified

        switch (keyCode) {
            case 259 -> { // Backspace
                if (cursorPosition > 0) {
                    text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
                    cursorPosition--;
                    if (onChange != null) onChange.accept(text);
                }
                return true;
            }
            case 261 -> { // Delete
                if (cursorPosition < text.length()) {
                    text = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
                    if (onChange != null) onChange.accept(text);
                }
                return true;
            }
            case 263 -> { // Left
                if (cursorPosition > 0) cursorPosition--;
                return true;
            }
            case 262 -> { // Right
                if (cursorPosition < text.length()) cursorPosition++;
                return true;
            }
            case 268 -> { // Home
                cursorPosition = 0;
                return true;
            }
            case 269 -> { // End
                cursorPosition = text.length();
                return true;
            }
            case 257 -> { // Enter
                if (multiline) {
                    insertText("\n");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCharTyped(char chr, int modifiers) {
        if (!focused) return false;
        if (text.length() >= maxLength) return false;
        if (chr >= 32 || chr == '\t' || (multiline && chr == '\n')) {
            insertText(String.valueOf(chr));
            return true;
        }
        return false;
    }

    private void insertText(String str) {
        text = text.substring(0, cursorPosition) + str + text.substring(cursorPosition);
        cursorPosition += str.length();
        if (onChange != null) onChange.accept(text);
    }

    private void updateCursorFromMouse(int mouseX) {
        // Simplified - would need font measurement
        int relX = mouseX - x - 8;
        int charWidth = 8; // Approximate
        cursorPosition = Math.max(0, Math.min(text.length(), relX / charWidth));
    }

    @Override
    public void updateAnimations() {
        super.updateAnimations();

        // Cursor blink
        if (System.currentTimeMillis() - cursorBlinkTime > 500) {
            cursorVisible = !cursorVisible;
            cursorBlinkTime = System.currentTimeMillis();
        }
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta) {
        if (!visible) return;

        updateAnimations();

        // Label
        int labelColor = enabled ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_DISABLED;
        fontRenderer.draw(context, label, x, y, labelColor);

        // Text box
        int boxX = x;
        int boxY = y + fontRenderer.getFontHeight() + 4;
        int boxWidth = width;
        int boxHeight = multiline ? height * 3 : height;

        int bgColor = focused ? DesignTokens.COLOR_HOVER_BACKGROUND : getBackgroundColor();
        RoundedRect.render(context, boxX, boxY, boxWidth, boxHeight, DesignTokens.RADIUS_SMALL, bgColor);
        RoundedRect.renderOutline(context, boxX, boxY, boxWidth, boxHeight, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN,
            focused ? DesignTokens.COLOR_PRIMARY_PURPLE : getBorderColor());

        // Text
        int textX = boxX + 8;
        int textY = boxY + (boxHeight - fontRenderer.getFontHeight()) / 2;
        String displayText = text.isEmpty() && !focused ? placeholder : text;
        int textColor = (text.isEmpty() && !focused) ? DesignTokens.COLOR_MUTED_TEXT :
            (enabled ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_DISABLED);

        // Simple clipping for long text
        int maxTextWidth = boxWidth - 16;
        String clippedText = displayText;
        while (fontRenderer.getStringWidth(clippedText) > maxTextWidth && !clippedText.isEmpty()) {
            clippedText = clippedText.substring(1);
        }

        fontRenderer.draw(context, clippedText, textX, textY, textColor);

        // Cursor
        if (focused && cursorVisible) {
            int cursorX = textX + fontRenderer.getStringWidth(clippedText.substring(0, Math.min(cursorPosition, clippedText.length())));
            context.fill(cursorX, textY, cursorX + 1, textY + fontRenderer.getFontHeight(), DesignTokens.COLOR_PRIMARY_PURPLE);
        }
    }
}