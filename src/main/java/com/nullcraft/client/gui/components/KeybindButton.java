package com.nullcraft.client.gui.components;

import com.nullcraft.client.core.keybind.Keybind;
import com.nullcraft.client.core.keybind.KeyNames;
import com.nullcraft.client.core.utils.DesignTokens;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

/**
 * Keybind button for setting keybinds.
 */
public class KeybindButton extends Component {

    private final String label;
    private Keybind keybind;
    private final Consumer<Keybind> onChange;
    private boolean listening = false;

    public KeybindButton(int x, int y, int width, int height, String label, Keybind initialKeybind, Consumer<Keybind> onChange) {
        super(x, y, width, height);
        this.label = label;
        this.keybind = initialKeybind;
        this.onChange = onChange;
    }

    public Keybind getKeybind() {
        return keybind;
    }

    public void setKeybind(Keybind keybind) {
        this.keybind = keybind;
    }

    public boolean isListening() {
        return listening;
    }

    @Override
    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        if (!enabled || !visible) return false;
        if (contains(mouseX, mouseY) && button == 0) {
            listening = !listening;
            return true;
        }
        // Click outside stops listening
        if (listening && button == 0) {
            listening = false;
        }
        return false;
    }

    @Override
    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        if (listening) {
            if (keyCode == 256) { // ESC clears
                keybind = Keybind.NONE;
            } else {
                keybind = new Keybind(keyCode, scanCode, modifiers);
            }
            listening = false;
            if (onChange != null) onChange.accept(keybind);
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta) {
        if (!visible) return;

        updateAnimations();

        // Label
        int labelColor = enabled ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_DISABLED;
        fontRenderer.draw(context, label, x, y, labelColor);

        // Button
        int boxX = x;
        int boxY = y + fontRenderer.getFontHeight() + 4;
        int boxWidth = width;
        int boxHeight = height;

        int bgColor = listening ? DesignTokens.COLOR_PRIMARY_PURPLE : getBackgroundColor();
        RoundedRect.render(context, boxX, boxY, boxWidth, boxHeight, DesignTokens.RADIUS_SMALL, bgColor);
        RoundedRect.renderOutline(context, boxX, boxY, boxWidth, boxHeight, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN,
            listening ? DesignTokens.COLOR_PRIMARY_TEXT : getBorderColor());

        // Keybind text
        String keyText = listening ? "Press a key..." : (keybind.isNone() ? "None" : keybind.toShortString());
        int textColor = listening ? DesignTokens.COLOR_PRIMARY_TEXT : (enabled ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_DISABLED);
        fontRenderer.drawCentered(context, keyText, boxX + boxWidth / 2, boxY + (boxHeight - fontRenderer.getFontHeight()) / 2, textColor);
    }
}