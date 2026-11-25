package com.nullcraft.client.core.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Range setting with two values (min/max) - dual slider UI.
 */
public class RangeSetting extends Setting<RangeSetting.Range> {

    private final double min;
    private final double max;
    private final double step;

    public RangeSetting(String name, String description, double defaultMin, double defaultMax, double min, double max, double step) {
        super(name, description, new Range(defaultMin, defaultMax));
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

    public double getLowerValue() {
        return value.lower;
    }

    public double getUpperValue() {
        return value.upper;
    }

    public void setLowerValue(double lower) {
        lower = Math.max(min, Math.min(upper, lower));
        set(new Range(lower, value.upper));
    }

    public void setUpperValue(double upper) {
        upper = Math.max(lower, Math.min(max, upper));
        set(new Range(value.lower, upper));
    }

    public double getLowerProgress() {
        return (value.lower - min) / (max - min);
    }

    public double getUpperProgress() {
        return (value.upper - min) / (max - min);
    }

    public void setLowerProgress(double progress) {
        progress = Math.max(0, Math.min(1, progress));
        double raw = min + progress * (max - min);
        double stepped = Math.round(raw / step) * step;
        setLowerValue(Math.max(min, Math.min(value.upper, stepped)));
    }

    public void setUpperProgress(double progress) {
        progress = Math.max(0, Math.min(1, progress));
        double raw = min + progress * (max - min);
        double stepped = Math.round(raw / step) * step;
        setUpperValue(Math.max(value.lower, Math.min(max, stepped)));
    }

    @Override
    protected Range parseJson(JsonElement element) {
        JsonObject obj = element.getAsJsonObject();
        double lower = obj.get("min").getAsDouble();
        double upper = obj.get("max").getAsDouble();
        return new Range(lower, upper);
    }

    @Override
    protected JsonElement toJsonElement() {
        JsonObject obj = new JsonObject();
        obj.addProperty("min", value.lower);
        obj.addProperty("max", value.upper);
        return obj;
    }

    @Override
    protected boolean validate(Range value) {
        return value != null && value.lower >= min && value.upper <= max && value.lower <= value.upper;
    }

    @Override
    protected Range copyValue(Range value) {
        return new Range(value.lower, value.upper);
    }

    @Override
    protected String getTypeName() {
        return "range";
    }

    public static final class Range {
        public final double lower;
        public final double upper;

        public Range(double lower, double upper) {
            this.lower = lower;
            this.upper = upper;
        }

        @Override
        public String toString() {
            return "[" + lower + ", " + upper + "]";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Range range = (Range) obj;
            return Double.compare(range.lower, lower) == 0 && Double.compare(range.upper, upper) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(lower) ^ Double.hashCode(upper);
        }
    }
}