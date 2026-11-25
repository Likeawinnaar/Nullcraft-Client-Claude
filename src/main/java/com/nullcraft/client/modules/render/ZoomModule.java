package com.nullcraft.client.modules.render;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * Zoom - Zooms in with a keybind.
 */
public class ZoomModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting toggleMode;
    private final DoubleSetting zoomLevel;
    private final DoubleSetting zoomSensitivity;
    private final BooleanSetting smoothZoom;
    private final IntegerSetting transitionTime;
    private final ModeSetting keybindMode;

    public ZoomModule() {
        super("zoom", "Zoom", "Zooms in with a keybind", ModuleCategory.RENDER, "icon_zoom");

        this.enabled = new BooleanSetting("enabled", "Enable Zoom", true);
        this.toggleMode = new BooleanSetting("toggle_mode", "Toggle mode", false);
        this.zoomLevel = new DoubleSetting("zoom_level", "Zoom level (FOV multiplier)", 0.25, 0.05, 0.9, 0.05);
        this.zoomSensitivity = new DoubleSetting("zoom_sensitivity", "Zoom sensitivity multiplier", 0.5, 0.1, 1.0, 0.05);
        this.smoothZoom = new BooleanSetting("smooth_zoom", "Smooth zoom transition", true);
        this.transitionTime = new IntegerSetting("transition_time", "Transition time (ms)", 100, 0, 500);
        this.keybindMode = new ModeSetting("keybind_mode", "Keybind mode", "hold", "hold", "toggle", "double_tap");

        registerSetting(enabled);
        registerSetting(toggleMode);
        registerSetting(zoomLevel);
        registerSetting(zoomSensitivity);
        registerSetting(smoothZoom);
        registerSetting(transitionTime);
        registerSetting(keybindMode);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, toggleMode, keybindMode, zoomLevel, zoomSensitivity);

        behaviorGroup = createGroup("Behavior", "Zoom behavior");
        behaviorGroup.addSettings(smoothZoom, transitionTime);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Zoom logic
    }
}