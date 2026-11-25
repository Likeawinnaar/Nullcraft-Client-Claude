package com.nullcraft.client.core.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mode setting - single selection from a list of options (dropdown).
 */
public class ModeSetting extends Setting<String> {

    private final List<String> modes;

    public ModeSetting(String name, String description, String defaultValue, String... modes) {
        this(name, description, defaultValue, Arrays.asList(modes));
    }

    public ModeSetting(String name, String description, String defaultValue, List<String> modes) {
        super(name, description, defaultValue);
        this.modes = List.copyOf(modes);
        if (!this.modes.contains(defaultValue) && !this.modes.isEmpty()) {
            this.value = this.modes.get(0);
            this.defaultValue = this.modes.get(0);
        }
    }

    public List<String> getModes() {
        return modes;
    }

    public int getIndex() {
        return modes.indexOf(value);
    }

    public void setIndex(int index) {
        if (index >= 0 && index < modes.size()) {
            set(modes.get(index));
        }
    }

    public void cycle() {
        int index = getIndex();
        if (index >= 0) {
            setIndex((index + 1) % modes.size());
        }
    }

    public void cycleBack() {
        int index = getIndex();
        if (index >= 0) {
            setIndex((index - 1 + modes.size()) % modes.size());
        }
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
        return value != null && modes.contains(value);
    }

    @Override
    protected String copyValue(String value) {
        return value;
    }

    @Override
    protected String getTypeName() {
        return "mode";
    }

    public JsonElement modesToJson() {
        return com.google.gson.JsonArray.class.cast(
            new com.google.gson.JsonArray().also(arr -> modes.forEach(arr::add))
        );
    }
}