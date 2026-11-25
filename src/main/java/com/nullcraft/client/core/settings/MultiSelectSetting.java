package com.nullcraft.client.core.settings;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Multi-select setting - multiple selections from a list of options.
 */
public class MultiSelectSetting extends Setting<Set<String>> {

    private final List<String> options;

    public MultiSelectSetting(String name, String description, Set<String> defaultValue, String... options) {
        this(name, description, defaultValue, Arrays.asList(options));
    }

    public MultiSelectSetting(String name, String description, Set<String> defaultValue, List<String> options) {
        super(name, description, defaultValue);
        this.options = List.copyOf(options);
        // Ensure default value only contains valid options
        this.value = this.value.stream().filter(this.options::contains).collect(Collectors.toSet());
        this.defaultValue = this.value;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean isSelected(String option) {
        return value.contains(option);
    }

    public void setSelected(String option, boolean selected) {
        Set<String> newValue = new HashSet<>(value);
        if (selected) {
            newValue.add(option);
        } else {
            newValue.remove(option);
        }
        set(newValue);
    }

    public void toggle(String option) {
        setSelected(option, !isSelected(option));
    }

    public void selectAll() {
        set(new HashSet<>(options));
    }

    public void clear() {
        set(Collections.emptySet());
    }

    public int getSelectedCount() {
        return value.size();
    }

    @Override
    protected Set<String> parseJson(JsonElement element) {
        JsonArray array = element.getAsJsonArray();
        Set<String> result = new HashSet<>();
        for (JsonElement e : array) {
            result.add(e.getAsString());
        }
        return result;
    }

    @Override
    protected JsonElement toJsonElement() {
        JsonArray array = new JsonArray();
        for (String s : value) {
            array.add(new JsonPrimitive(s));
        }
        return array;
    }

    @Override
    protected boolean validate(Set<String> value) {
        return value != null && value.stream().allMatch(options::contains);
    }

    @Override
    protected Set<String> copyValue(Set<String> value) {
        return new HashSet<>(value);
    }

    @Override
    protected String getTypeName() {
        return "multiselect";
    }
}