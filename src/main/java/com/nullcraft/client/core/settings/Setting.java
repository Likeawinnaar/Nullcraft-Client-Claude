package com.nullcraft.client.core.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Base class for all setting types.
 * Provides common functionality for value storage, validation, and serialization.
 *
 * @param <T> The type of value this setting holds
 */
public abstract class Setting<T> {

    private final String name;
    private final String description;
    private T value;
    private T defaultValue;
    private boolean visible = true;
    private SettingChangeListener<T> changeListener;

    public Setting(String name, String description, T defaultValue) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    // ============================================================
    // ABSTRACT METHODS
    // ============================================================

    /**
     * Parses a JSON element into the setting's value type.
     */
    protected abstract T parseJson(JsonElement element);

    /**
     * Converts the current value to a JSON element.
     */
    protected abstract JsonElement toJsonElement();

    /**
     * Validates a proposed value.
     * @return true if valid, false otherwise
     */
    protected abstract boolean validate(T value);

    /**
     * Creates a copy of the value (for immutable safety).
     */
    protected abstract T copyValue(T value);

    // ============================================================
    // VALUE ACCESS
    // ============================================================

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public final T get() {
        return value;
    }

    public final void set(T newValue) {
        if (validate(newValue)) {
            T oldValue = this.value;
            this.value = copyValue(newValue);
            if (changeListener != null) {
                changeListener.onChanged(this, oldValue, newValue);
            }
        }
    }

    public final T getDefault() {
        return defaultValue;
    }

    public final void resetToDefault() {
        set(copyValue(defaultValue));
    }

    // ============================================================
    // VISIBILITY
    // ============================================================

    public final boolean isVisible() {
        return visible;
    }

    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    // ============================================================
    // CHANGE LISTENER
    // ============================================================

    public final void setChangeListener(SettingChangeListener<T> listener) {
        this.changeListener = listener;
    }

    // ============================================================
    // SERIALIZATION
    // ============================================================

    public final JsonElement toJson() {
        JsonObject obj = new JsonObject();
        obj.add("value", toJsonElement());
        obj.addProperty("type", getTypeName());
        return obj;
    }

    public final void fromJson(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.has("value")) {
                T parsed = parseJson(obj.get("value"));
                if (validate(parsed)) {
                    this.value = parsed;
                }
            }
        } else {
            // Legacy format - direct value
            T parsed = parseJson(element);
            if (validate(parsed)) {
                this.value = parsed;
            }
        }
    }

    protected abstract String getTypeName();

    // ============================================================
    // UTILITY
    // ============================================================

    @Override
    public String toString() {
        return "Setting{" + name + "=" + value + "}";
    }

    // ============================================================
    // CHANGE LISTENER INTERFACE
    // ============================================================

    @FunctionalInterface
    public interface SettingChangeListener<T> {
        void onChanged(Setting<T> setting, T oldValue, T newValue);
    }
}