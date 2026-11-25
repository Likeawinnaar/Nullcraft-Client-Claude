package com.nullcraft.client.core.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.nullcraft.client.core.keybind.Keybind;

/**
 * Keybind setting - key selector UI.
 */
public class KeybindSetting extends Setting<Keybind> {

    public KeybindSetting(String name, String description, Keybind defaultValue) {
        super(name, description, defaultValue);
    }

    @Override
    protected Keybind parseJson(JsonElement element) {
        return Keybind.fromString(element.getAsString());
    }

    @Override
    protected JsonElement toJsonElement() {
        return new JsonPrimitive(value.toString());
    }

    @Override
    protected boolean validate(Keybind value) {
        return value != null;
    }

    @Override
    protected Keybind copyValue(Keybind value) {
        return value;
    }

    @Override
    protected String getTypeName() {
        return "keybind";
    }
}