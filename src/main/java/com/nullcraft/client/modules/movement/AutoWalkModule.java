package com.nullcraft.client.modules.movement;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * AutoWalk - Automatically walks forward.
 */
public class AutoWalkModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting toggleMode;
    private final BooleanSetting stopOnDamage;
    private final BooleanSetting stopOnBlock;
    private final BooleanSetting stopOnWater;
    private final IntegerSetting speed;

    public AutoWalkModule() {
        super("auto_walk", "AutoWalk", "Automatically walks forward", ModuleCategory.MOVEMENT, "icon_auto_walk");

        this.enabled = new BooleanSetting("enabled", "Enable AutoWalk", false);
        this.toggleMode = new BooleanSetting("toggle_mode", "Toggle mode (hold to disable)", true);
        this.stopOnDamage = new BooleanSetting("stop_on_damage", "Stop on damage", true);
        this.stopOnBlock = new BooleanSetting("stop_on_block", "Stop when blocked", true);
        this.stopOnWater = new BooleanSetting("stop_on_water", "Stop in water", false);
        this.speed = new IntegerSetting("speed", "Walk speed multiplier", 100, 50, 200);

        registerSetting(enabled);
        registerSetting(toggleMode);
        registerSetting(stopOnDamage);
        registerSetting(stopOnBlock);
        registerSetting(stopOnWater);
        registerSetting(speed);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, toggleMode, speed);

        behaviorGroup = createGroup("Behavior", "Stop conditions");
        behaviorGroup.addSettings(stopOnDamage, stopOnBlock, stopOnWater);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Auto-walk logic
    }
}