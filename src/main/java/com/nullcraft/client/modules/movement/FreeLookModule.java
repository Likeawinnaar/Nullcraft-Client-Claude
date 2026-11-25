package com.nullcraft.client.modules.movement;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * FreeLook - Allows looking around while moving.
 */
public class FreeLookModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting toggleMode;
    private final BooleanSetting lockYaw;
    private final DoubleSetting maxYawOffset;
    private final DoubleSetting maxPitchOffset;
    private final IntegerSetting smoothing;

    public FreeLookModule() {
        super("free_look", "FreeLook", "Allows looking around while moving", ModuleCategory.MOVEMENT, "icon_free_look");

        this.enabled = new BooleanSetting("enabled", "Enable FreeLook", false);
        this.toggleMode = new BooleanSetting("toggle_mode", "Toggle with keybind", true);
        this.lockYaw = new BooleanSetting("lock_yaw", "Lock yaw when disabled", true);
        this.maxYawOffset = new DoubleSetting("max_yaw_offset", "Max yaw offset (degrees)", 90.0, 10.0, 180.0, 5.0);
        this.maxPitchOffset = new DoubleSetting("max_pitch_offset", "Max pitch offset (degrees)", 90.0, 10.0, 90.0, 5.0);
        this.smoothing = new IntegerSetting("smoothing", "Camera smoothing", 5, 0, 20);

        registerSetting(enabled);
        registerSetting(toggleMode);
        registerSetting(lockYaw);
        registerSetting(maxYawOffset);
        registerSetting(maxPitchOffset);
        registerSetting(smoothing);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, toggleMode, lockYaw, smoothing);

        behaviorGroup = createGroup("Limits", "Look limits");
        behaviorGroup.addSettings(maxYawOffset, maxPitchOffset);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Free look logic
    }
}