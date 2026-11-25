package com.nullcraft.client.gui.components;

import com.nullcraft.client.core.utils.DesignTokens;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import net.minecraft.client.gui.DrawContext;

/**
 * Slider component for numeric settings (integer, double, range).
 */
public class Slider extends Component {

    public enum Type { INTEGER, DOUBLE, RANGE }

    private final Type type;
    private final String label;
    private double min, max, step;
    private double value;
    private double valueMax; // For range sliders
    private final Runnable onChange;

    private boolean dragging = false;
    private boolean draggingMax = false;

    public Slider(int x, int y, int width, int height, String label, Type type,
                  double min, double max, double step, double initialValue, Runnable onChange) {
        this(x, y, width, height, label, type, min, max, step, initialValue, initialValue, onChange);
    }

    public Slider(int x, int y, int width, int height, String label, Type type,
                  double min, double max, double step, double initialValue, double initialMax, Runnable onChange) {
        super(x, y, width, height);
        this.type = type;
        this.label = label;
        this.min = min;
        this.max = max;
        this.step = step;
        this.value = Math.max(min, Math.min(max, initialValue));
        this.valueMax = type == Type.RANGE ? Math.max(min, Math.min(max, initialMax)) : max;
        this.onChange = onChange;
    }

    public double getValue() { return value; }
    public double getValueMax() { return valueMax; }

    public void setValue(double value) {
        this.value = Math.max(min, Math.min(max, snapToStep(value)));
        if (onChange != null) onChange.run();
    }

    public void setValueMax(double valueMax) {
        this.valueMax = Math.max(min, Math.min(max, snapToStep(valueMax)));
        if (onChange != null) onChange.run();
    }

    private double snapToStep(double val) {
        if (step <= 0) return val;
        return Math.round(val / step) * step;
    }

    private double getProgress(double val) {
        return (val - min) / (max - min);
    }

    private double getValueFromProgress(double progress) {
        return min + progress * (max - min);
    }

    @Override
    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        if (!enabled || !visible) return false;
        if (contains(mouseX, mouseY) && button == 0) {
            dragging = true;
            updateValueFromMouse(mouseX);
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseRelease(int mouseX, int mouseY, int button) {
        if (dragging && button == 0) {
            dragging = false;
            draggingMax = false;
            return true;
        }
        dragging = false;
        draggingMax = false;
        return false;
    }

    @Override
    public boolean onMouseMove(int mouseX, int mouseY) {
        boolean hovered = super.onMouseMove(mouseX, mouseY);
        if (dragging) {
            updateValueFromMouse(mouseX);
        }
        return hovered;
    }

    private void updateValueFromMouse(int mouseX) {
        int trackX = x + 8;
        int trackWidth = width - 16;
        double progress = (mouseX - trackX) / (double) trackWidth;
        progress = Math.max(0, Math.min(1, progress));
        double newValue = getValueFromProgress(progress);
        newValue = snapToStep(newValue);

        if (type == Type.RANGE && draggingMax) {
            valueMax = Math.max(value, Math.min(max, newValue));
        } else {
            value = Math.max(min, Math.min(valueMax, newValue));
            if (type == Type.RANGE && value > valueMax) {
                valueMax = value;
            }
        }

        if (onChange != null) onChange.run();
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta) {
        if (!visible) return;

        updateAnimations();

        // Label
        int labelColor = enabled ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_DISABLED;
        fontRenderer.draw(context, label, x, y, labelColor);

        // Track
        int trackHeight = 6;
        int trackY = y + height - trackHeight - 4;
        int trackX = x + 8;
        int trackWidth = width - 16;

        // Background track
        RoundedRect.render(context, trackX, trackY, trackWidth, trackHeight, trackHeight / 2f, DesignTokens.COLOR_DISABLED);

        // Fill track(s)
        double progress = getProgress(value);
        int fillWidth = (int) (trackWidth * progress);
        int fillColor = DesignTokens.COLOR_PRIMARY_PURPLE;
        RoundedRect.render(context, trackX, trackY, fillWidth, trackHeight, trackHeight / 2f, fillColor);

        // Range: second fill
        if (type == Type.RANGE) {
            double progressMax = getProgress(valueMax);
            int fillWidthMax = (int) (trackWidth * progressMax);
            // Render the range between value and valueMax
            int rangeStart = (int) (trackX + trackWidth * progress);
            int rangeWidth = fillWidthMax - (int) (trackWidth * progress);
            if (rangeWidth > 0) {
                RoundedRect.render(context, rangeStart, trackY, rangeWidth, trackHeight, trackHeight / 2f,
                    DesignTokens.withAlpha(DesignTokens.COLOR_PRIMARY_PURPLE, 0.5f));
            }
        }

        // Thumb(s)
        int thumbSize = 14;
        int thumbY = trackY + (trackHeight - thumbSize) / 2;

        // Main thumb
        int thumbX = trackX + (int) (trackWidth * progress) - thumbSize / 2;
        RoundedRect.render(context, thumbX, thumbY, thumbSize, thumbSize, thumbSize / 2f, DesignTokens.COLOR_PRIMARY_TEXT);

        // Max thumb for range
        if (type == Type.RANGE) {
            int thumbXMax = trackX + (int) (trackWidth * getProgress(valueMax)) - thumbSize / 2;
            RoundedRect.render(context, thumbXMax, thumbY, thumbSize, thumbSize, thumbSize / 2f, DesignTokens.COLOR_PRIMARY_TEXT);
        }

        // Value text
        String valueText = type == Type.INTEGER ? String.valueOf((int) value) : String.format("%.2f", value);
        if (type == Type.RANGE) {
            valueText = String.format("%.2f - %.2f", value, valueMax);
        }
        int textColor = enabled ? DesignTokens.COLOR_SECONDARY_TEXT : DesignTokens.COLOR_DISABLED;
        fontRenderer.draw(context, valueText, x + width - fontRenderer.getStringWidth(valueText), y, textColor);
    }
}