package com.nullcraft.client.core.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Integer setting with slider UI.
 */
public class IntegerSetting extends Setting<Integer> {

    private final int min;
    private final int max;

    public IntegerSetting(String name, String description, int defaultValue, int min, int max) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    protected Integer parseJson(JsonElement element) {
        return element.getAsInt();
    }

    @Override
    protected JsonElement toJsonElement() {
        return new JsonPrimitive(value);
    }

    @Override
    protected boolean validate(Integer value) {
        return value != null && value >= min && value <= max;
    }

    @Override
    protected Integer copyValue(Integer value) {
        return value;
    }

    @Override
    protected String getTypeName() {
        return "integer";
    }

    public double getProgress() {
        return (double) (value - min) / (max - min);
    }

    public void setProgress(double progress) {
        progress = Math.max(0, Math.min(1, progress));
        set(min + (int) Math.round(progress * (max - min)));
    }
}