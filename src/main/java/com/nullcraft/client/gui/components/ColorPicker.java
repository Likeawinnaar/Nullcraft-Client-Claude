package com.nullcraft.client.gui.components;

import com.nullcraft.client.core.utils.ColorUtils;
import com.nullcraft.client.core.utils.DesignTokens;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

/**
 * Color picker component with HSV selector and alpha support.
 */
public class ColorPicker extends Component {

    private final String label;
    private int color; // ARGB
    private final boolean hasAlpha;
    private final Consumer<Integer> onChange;

    private boolean expanded = false;
    private float hue = 0f, saturation = 1f, brightness = 1f, alpha = 1f;

    // HSV picker state
    private boolean draggingHue = false;
    private boolean draggingSV = false;
    private boolean draggingAlpha = false;

    public ColorPicker(int x, int y, int width, int height, String label, int initialColor, boolean hasAlpha, Consumer<Integer> onChange) {
        super(x, y, width, height);
        this.label = label;
        this.color = initialColor;
        this.hasAlpha = hasAlpha;
        this.onChange = onChange;
        updateHSV();
    }

    private void updateHSV() {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = (color >> 24) & 0xFF;
        this.hue = ColorUtils.rgbToHue(r, g, b);
        this.saturation = ColorUtils.rgbToSaturation(r, g, b);
        this.brightness = ColorUtils.rgbToBrightness(r, g, b);
        this.alpha = a / 255f;
    }

    private void updateColor() {
        int rgb = ColorUtils.hsvToRgb(hue, saturation, brightness);
        int a = hasAlpha ? (int) (alpha * 255) : 255;
        this.color = (a << 24) | rgb;
        if (onChange != null) onChange.accept(color);
    }

    public int getColor() { return color; }
    public int getRGB() { return color & 0x00FFFFFF; }
    public int getAlpha() { return (color >> 24) & 0xFF; }

    public void setColor(int color) {
        this.color = color;
        updateHSV();
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        if (!enabled || !visible) return false;

        // Color preview box
        int previewSize = height;
        int previewX = x;
        int previewY = y + 20;

        if (mouseX >= previewX && mouseX < previewX + previewSize && mouseY >= previewY && mouseY < previewY + previewSize && button == 0) {
            expanded = !expanded;
            return true;
        }

        if (expanded) {
            // HSV picker area (simplified)
            int pickerX = x;
            int pickerY = previewY + previewSize + 10;
            int pickerWidth = width;
            int pickerHeight = 120;

            // Saturation/Brightness square
            int svSize = 100;
            int svX = pickerX + 10;
            int svY = pickerY + 10;

            if (mouseX >= svX && mouseX < svX + svSize && mouseY >= svY && mouseY < svY + svSize && button == 0) {
                draggingSV = true;
                updateSVFromMouse(mouseX, mouseY, svX, svY, svSize);
                return true;
            }

            // Hue slider
            int hueX = svX + svSize + 20;
            int hueY = svY;
            int hueWidth = 20;
            int hueHeight = svSize;

            if (mouseX >= hueX && mouseX < hueX + hueWidth && mouseY >= hueY && mouseY < hueY + hueHeight && button == 0) {
                draggingHue = true;
                updateHueFromMouse(mouseY, hueY, hueHeight);
                return true;
            }

            // Alpha slider
            if (hasAlpha) {
                int alphaX = hueX + hueWidth + 20;
                int alphaY = svY;
                int alphaWidth = 20;
                int alphaHeight = svSize;

                if (mouseX >= alphaX && mouseX < alphaX + alphaWidth && mouseY >= alphaY && mouseY < alphaY + alphaHeight && button == 0) {
                    draggingAlpha = true;
                    updateAlphaFromMouse(mouseY, alphaY, alphaHeight);
                    return true;
                }
            }
        }

        if (expanded && button == 0) {
            expanded = false;
        }

        return false;
    }

    @Override
    public boolean onMouseRelease(int mouseX, int mouseY, int button) {
        if (button == 0) {
            draggingHue = false;
            draggingSV = false;
            draggingAlpha = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseMove(int mouseX, int mouseY) {
        boolean hovered = super.onMouseMove(mouseX, mouseY);
        if (draggingSV) {
            int previewSize = height;
            int previewX = x;
            int previewY = y + 20;
            int pickerY = previewY + previewSize + 10;
            int svX = x + 10;
            int svY = pickerY + 10;
            int svSize = 100;
            updateSVFromMouse(mouseX, mouseY, svX, svY, svSize);
        } else if (draggingHue) {
            int previewSize = height;
            int previewX = x;
            int previewY = y + 20;
            int pickerY = previewY + previewSize + 10;
            int hueY = pickerY + 10;
            int hueHeight = 100;
            updateHueFromMouse(mouseY, hueY, hueHeight);
        } else if (draggingAlpha) {
            int previewSize = height;
            int previewX = x;
            int previewY = y + 20;
            int pickerY = previewY + previewSize + 10;
            int alphaY = pickerY + 10;
            int alphaHeight = 100;
            updateAlphaFromMouse(mouseY, alphaY, alphaHeight);
        }
        return hovered;
    }

    private void updateSVFromMouse(int mouseX, int mouseY, int svX, int svY, int svSize) {
        double relX = (mouseX - svX) / (double) svSize;
        double relY = (mouseY - svY) / (double) svSize;
        relX = Math.max(0, Math.min(1, relX));
        relY = Math.max(0, Math.min(1, relY));
        this.saturation = (float) relX;
        this.brightness = 1f - (float) relY;
        updateColor();
    }

    private void updateHueFromMouse(int mouseY, int hueY, int hueHeight) {
        double relY = (mouseY - hueY) / (double) hueHeight;
        relY = Math.max(0, Math.min(1, relY));
        this.hue = (float) (relY * 360f);
        updateColor();
    }

    private void updateAlphaFromMouse(int mouseY, int alphaY, int alphaHeight) {
        double relY = (mouseY - alphaY) / (double) alphaHeight;
        relY = Math.max(0, Math.min(1, relY));
        this.alpha = 1f - (float) relY;
        updateColor();
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta) {
        if (!visible) return;

        // Label
        int labelColor = enabled ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_DISABLED;
        fontRenderer.draw(context, label, x, y, labelColor);

        // Preview box
        int previewSize = height - 4;
        int previewX = x;
        int previewY = y + 20;
        RoundedRect.render(context, previewX, previewY, previewSize, previewSize, DesignTokens.RADIUS_SMALL, color);
        RoundedRect.renderOutline(context, previewX, previewY, previewSize, previewSize, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN, DesignTokens.COLOR_BORDER);

        // Hex value
        String hex = String.format("#%06X", getRGB()) + (hasAlpha ? String.format("+%02X", getAlpha()) : "");
        fontRenderer.draw(context, hex, previewX + previewSize + 10, previewY + (previewSize - fontRenderer.getFontHeight()) / 2, DesignTokens.COLOR_SECONDARY_TEXT);

        // Expanded picker
        if (expanded) {
            int pickerX = x;
            int pickerY = previewY + previewSize + 10;
            int pickerWidth = width;
            int pickerHeight = 120;

            // Background
            RoundedRect.render(context, pickerX, pickerY, pickerWidth, pickerHeight, DesignTokens.RADIUS_MEDIUM, DesignTokens.COLOR_PANEL_BACKGROUND);
            RoundedRect.renderOutline(context, pickerX, pickerY, pickerWidth, pickerHeight, DesignTokens.RADIUS_MEDIUM, DesignTokens.BORDER_THIN, DesignTokens.COLOR_BORDER);

            // SV Square
            int svSize = 100;
            int svX = pickerX + 10;
            int svY = pickerY + 10;

            // Render SV square (simplified - would use gradient in practice)
            int baseColor = ColorUtils.hsvToRgb(hue, 1f, 1f);
            for (int i = 0; i < svSize; i++) {
                for (int j = 0; j < svSize; j++) {
                    float s = i / (float) svSize;
                    float v = 1f - j / (float) svSize;
                    int c = ColorUtils.hsvToRgb(hue, s, v);
                    context.fill(svX + i, svY + j, svX + i + 1, svY + j + 1, c);
                }
            }

            // SV cursor
            int cursorX = svX + (int) (saturation * svSize);
            int cursorY = svY + (int) ((1f - brightness) * svSize);
            context.fill(cursorX - 4, cursorY - 4, cursorX + 4, cursorY + 4, 0xFFFFFFFF);
            context.fill(cursorX - 3, cursorY - 3, cursorX + 3, cursorY + 3, 0xFF000000);

            // Hue slider
            int hueX = svX + svSize + 20;
            int hueY = svY;
            int hueWidth = 20;
            int hueHeight = svSize;

            for (int i = 0; i < hueHeight; i++) {
                float h = i / (float) hueHeight * 360f;
                int c = ColorUtils.hsvToRgb(h, 1f, 1f);
                context.fill(hueX, hueY + i, hueX + hueWidth, hueY + i + 1, c);
            }

            int hueCursorY = hueY + (int) (hue / 360f * hueHeight);
            context.fill(hueX - 2, hueCursorY - 4, hueX + hueWidth + 2, hueCursorY + 4, 0xFFFFFFFF);
            context.fill(hueX - 1, hueCursorY - 3, hueX + hueWidth + 1, hueCursorY + 3, 0xFF000000);

            // Alpha slider
            if (hasAlpha) {
                int alphaX = hueX + hueWidth + 20;
                int alphaY = svY;
                int alphaWidth = 20;
                int alphaHeight = svSize;

                int rgb = color & 0x00FFFFFF;
                for (int i = 0; i < alphaHeight; i++) {
                    float a = 1f - i / (float) alphaHeight;
                    int c = ColorUtils.withAlpha(rgb, (int) (a * 255));
                    context.fill(alphaX, alphaY + i, alphaX + alphaWidth, alphaY + i + 1, c);
                }

                int alphaCursorY = alphaY + (int) ((1f - alpha) * alphaHeight);
                context.fill(alphaX - 2, alphaCursorY - 4, alphaX + alphaWidth + 2, alphaCursorY + 4, 0xFFFFFFFF);
                context.fill(alphaX - 1, alphaCursorY - 3, alphaX + alphaWidth + 1, alphaCursorY + 3, 0xFF000000);
            }
        }
    }
}