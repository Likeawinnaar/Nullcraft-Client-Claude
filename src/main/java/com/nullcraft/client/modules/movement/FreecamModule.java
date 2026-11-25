package com.nullcraft.client.modules.movement;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * Freecam - Allows flying and noclip in spectator-like mode.
 */
public class FreecamModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup controlsGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting toggleMode;
    private final BooleanSetting noclip;
    private final BooleanSetting creativeFlight;
    private final DoubleSetting speed;
    private final DoubleSetting sprintMultiplier;
    private final IntegerSetting keyUp;
    private final IntegerSetting keyDown;

    public FreecamModule() {
        super("freecam", "Freecam", "Allows flying and noclip in spectator-like mode", ModuleCategory.MOVEMENT, "icon_freecam");

        this.enabled = new BooleanSetting("enabled", "Enable Freecam", false);
        this.toggleMode = new BooleanSetting("toggle_mode", "Toggle with keybind", true);
        this.noclip = new BooleanSetting("noclip", "Enable noclip", true);
        this.creativeFlight = new BooleanSetting("creative_flight", "Use creative flight", false);
        this.speed = new DoubleSetting("speed", "Fly speed", 0.5, 0.1, 5.0, 0.1);
        this.sprintMultiplier = new DoubleSetting("sprint_multiplier", "Sprint speed multiplier", 2.0, 1.0, 10.0, 0.1);
        this.keyUp = new IntegerSetting("key_up", "Up key (key code)", 57, 0, 348); // Space
        this.keyDown = new IntegerSetting("key_down", "Down key (key code)", 42, 0, 348); // Shift

        registerSetting(enabled);
        registerSetting(toggleMode);
        registerSetting(noclip);
        registerSetting(creativeFlight);
        registerSetting(speed);
        registerSetting(sprintMultiplier);
        registerSetting(keyUp);
        registerSetting(keyDown);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, toggleMode, noclip, creativeFlight);

        controlsGroup = createGroup("Controls", "Movement controls");
        controlsGroup.addSettings(speed, sprintMultiplier, keyUp, keyDown);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Freecam logic
    }
}