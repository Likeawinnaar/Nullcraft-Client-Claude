package com.nullcraft.client.core.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Double/decimal setting with slider UI.
 */
public class DoubleSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double step;

    public DoubleSetting(String name, String description, double defaultValue, double min, double max, double step) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    @Override
    protected Double parseJson(JsonElement element) {
        return element.getAsDouble();
    }

    @Override
    protected JsonElement toJsonElement() {
        return new JsonPrimitive(value);
    }

    @Override
    protected boolean validate(Double value) {
        return value != null && value >= min && value <= max;
    }

    @Override
    protected Double copyValue(Double value) {
        return value;
    }

    @Override
    protected String getTypeName() {
        return "double";
    }

    public double getProgress() {
        return (value - min) / (max - min);
    }

    public void setProgress(double progress) {
        progress = Math.max(0, Math.min(1, progress));
        double raw = min + progress * (max - min);
        // Snap to step
        double stepped = Math.round(raw / step) * step;
        set(Math.max(min, Math.min(max, stepped)));
    }

    public String getFormattedValue() {
        if (step >= 1) {
            return String.format("%.0f", value);
        } else if (step >= 0.1) {
            return String.format("%.1f", value);
        } else if (step >= 0.01) {
            return String.format("%.2f", value);
        } else {
            return String.format("%.3f", value);
        }
    }
}