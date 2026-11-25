package com.nullcraft.client.core.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * Boolean (true/false) setting with toggle UI.
 */
public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, String description, boolean defaultValue) {
        super(name, description, defaultValue);
    }

    @Override
    protected Boolean parseJson(JsonElement element) {
        return element.getAsBoolean();
    }

    @Override
    protected JsonElement toJsonElement() {
        return new JsonPrimitive(value);
    }

    @Override
    protected boolean validate(Boolean value) {
        return value != null;
    }

    @Override
    protected Boolean copyValue(Boolean value) {
        return value;
    }

    @Override
    protected String getTypeName() {
        return "boolean";
    }

    public void toggle() {
        set(!value);
    }
}