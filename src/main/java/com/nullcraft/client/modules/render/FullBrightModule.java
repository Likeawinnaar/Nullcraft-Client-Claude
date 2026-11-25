package com.nullcraft.client.modules.render;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * FullBright - Increases gamma for full brightness.
 */
public class FullBrightModule extends Module {

    private final SettingGroup generalGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting nightVision;
    private final DoubleSetting gamma;
    private final BooleanSetting restoreOnDisable;

    public FullBrightModule() {
        super("full_bright", "FullBright", "Increases gamma for full brightness", ModuleCategory.RENDER, "icon_full_bright");

        this.enabled = new BooleanSetting("enabled", "Enable FullBright", false);
        this.nightVision = new BooleanSetting("night_vision", "Night vision mode", true);
        this.gamma = new DoubleSetting("gamma", "Gamma multiplier", 10.0, 1.0, 100.0, 0.5);
        this.restoreOnDisable = new BooleanSetting("restore_on_disable", "Restore gamma on disable", true);

        registerSetting(enabled);
        registerSetting(nightVision);
        registerSetting(gamma);
        registerSetting(restoreOnDisable);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, nightVision, gamma, restoreOnDisable);
    }

    @Override
    public void onEnable() {
        // Apply gamma
    }

    @Override
    public void onDisable() {
        // Restore gamma
    }

    @Override
    public void onTick() {
        // Maintain gamma
    }
}