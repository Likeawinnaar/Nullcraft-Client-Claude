package com.nullcraft.client.core.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * String setting - text field UI.
 */
public class StringSetting extends Setting<String> {

    private final int maxLength;
    private final String placeholder;
    private final boolean multiline;

    public StringSetting(String name, String description, String defaultValue) {
        this(name, description, defaultValue, 256, "", false);
    }

    public StringSetting(String name, String description, String defaultValue, int maxLength) {
        this(name, description, defaultValue, maxLength, "", false);
    }

    public StringSetting(String name, String description, String defaultValue, int maxLength, String placeholder, boolean multiline) {
        super(name, description, defaultValue);
        this.maxLength = maxLength;
        this.placeholder = placeholder;
        this.multiline = multiline;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public boolean isMultiline() {
        return multiline;
    }

    @Override
    protected String parseJson(JsonElement element) {
        return element.getAsString();
    }

    @Override
    protected JsonElement toJsonElement() {
        return new JsonPrimitive(value);
    }

    @Override
    protected boolean validate(String value) {
        return value != null && value.length() <= maxLength;
    }

    @Override
    protected String copyValue(String value) {
        return value;
    }

    @Override
    protected String getTypeName() {
        return "string";
    }
}