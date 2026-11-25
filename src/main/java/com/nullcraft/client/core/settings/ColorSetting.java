package com.nullcraft.client.core.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Color setting with alpha support - HSV color picker UI.
 */
public class ColorSetting extends Setting<Integer> {

    private final boolean hasAlpha;
    private final boolean showRainbow;

    public ColorSetting(String name, String description, int defaultColor) {
        this(name, description, defaultColor, true, false);
    }

    public ColorSetting(String name, String description, int defaultColor, boolean hasAlpha, boolean showRainbow) {
        super(name, description, defaultColor);
        this.hasAlpha = hasAlpha;
        this.showRainbow = showRainbow;
    }

    public boolean hasAlpha() {
        return hasAlpha;
    }

    public boolean showRainbow() {
        return showRainbow;
    }

    public int getRed() {
        return (value >> 16) & 0xFF;
    }

    public int getGreen() {
        return (value >> 8) & 0xFF;
    }

    public int getBlue() {
        return value & 0xFF;
    }

    public int getAlpha() {
        return (value >> 24) & 0xFF;
    }

    public float getHue() {
        return ColorUtils.rgbToHue(getRed(), getGreen(), getBlue());
    }

    public float getSaturation() {
        return ColorUtils.rgbToSaturation(getRed(), getGreen(), getBlue());
    }

    public float getBrightness() {
        return ColorUtils.rgbToBrightness(getRed(), getGreen(), getBlue());
    }

    public void setHSV(float hue, float saturation, float brightness) {
        int rgb = ColorUtils.hsvToRgb(hue, saturation, brightness);
        int alpha = hasAlpha ? getAlpha() : 255;
        set((alpha << 24) | (rgb & 0x00FFFFFF));
    }

    public void setRGBA(int r, int g, int b, int a) {
        int alpha = hasAlpha ? a : 255;
        set((alpha << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF));
    }

    public int getRGB() {
        return value & 0x00FFFFFF;
    }

    public int getARGB() {
        return value;
    }

    @Override
    protected Integer parseJson(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            int r = obj.get("r").getAsInt();
            int g = obj.get("g").getAsInt();
            int b = obj.get("b").getAsInt();
            int a = hasAlpha ? obj.get("a").getAsInt() : 255;
            return (a << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
        } else if (element.isJsonPrimitive()) {
            // Legacy format - direct int
            return element.getAsInt();
        }
        return defaultValue;
    }

    @Override
    protected JsonElement toJsonElement() {
        JsonObject obj = new JsonObject();
        obj.addProperty("r", getRed());
        obj.addProperty("g", getGreen());
        obj.addProperty("b", getBlue());
        if (hasAlpha) {
            obj.addProperty("a", getAlpha());
        }
        return obj;
    }

    @Override
    protected boolean validate(Integer value) {
        return value != null;
    }

    @Override
    protected Integer copyValue(Integer value) {
        return value;
    }

    @Override
    protected String getTypeName() {
        return "color";
    }

    @Override
    public String toString() {
        return String.format("ColorSetting{%s=#%06X%s}", getName(), getRGB(), hasAlpha ? String.format("+%02X", getAlpha()) : "");
    }
}