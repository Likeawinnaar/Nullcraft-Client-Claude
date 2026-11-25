package com.nullcraft.client.core.settings;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Group setting - a container for nested settings that can be collapsed/expanded.
 */
public class GroupSetting extends Setting<JsonObject> {

    private final List<Setting<?>> children = new ArrayList<>();
    private boolean expanded = true;

    public GroupSetting(String name, String description) {
        super(name, description, new JsonObject());
    }

    public <T extends Setting<?>> T addSetting(T setting) {
        children.add(setting);
        return setting;
    }

    public List<Setting<?>> getChildren() {
        return List.copyOf(children);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void toggleExpanded() {
        expanded = !expanded;
    }

    @Override
    protected JsonObject parseJson(JsonElement element) {
        JsonObject obj = element.getAsJsonObject();
        for (Setting<?> child : children) {
            if (obj.has(child.getName())) {
                child.fromJson(obj.get(child.getName()));
            }
        }
        return obj;
    }

    @Override
    protected JsonElement toJsonElement() {
        JsonObject obj = new JsonObject();
        for (Setting<?> child : children) {
            obj.add(child.getName(), child.toJson());
        }
        return obj;
    }

    @Override
    protected boolean validate(JsonObject value) {
        return value != null;
    }

    @Override
    protected JsonObject copyValue(JsonObject value) {
        return value.deepCopy();
    }

    @Override
    protected String getTypeName() {
        return "group";
    }

    @Override
    public String toString() {
        return "GroupSetting{" + getName() + " [" + children.size() + " children]}";
    }
}