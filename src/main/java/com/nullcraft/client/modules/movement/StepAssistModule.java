package com.nullcraft.client.modules.movement;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * StepAssist - Automatically steps up blocks.
 */
public class StepAssistModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting onlyWhenSprinting;
    private final BooleanSetting onlyOnGround;
    private final DoubleSetting maxStepHeight;
    private final ModeSetting mode;

    public StepAssistModule() {
        super("step_assist", "StepAssist", "Automatically steps up blocks", ModuleCategory.MOVEMENT, "icon_step_assist");

        this.enabled = new BooleanSetting("enabled", "Enable StepAssist", true);
        this.onlyWhenSprinting = new BooleanSetting("only_when_sprinting", "Only when sprinting", false);
        this.onlyOnGround = new BooleanSetting("only_on_ground", "Only from ground", true);
        this.maxStepHeight = new DoubleSetting("max_step_height", "Max step height (blocks)", 1.0, 0.5, 2.5, 0.1);
        this.mode = new ModeSetting("mode", "Step mode", "vanilla", "vanilla", "instant", "smooth");

        registerSetting(enabled);
        registerSetting(onlyWhenSprinting);
        registerSetting(onlyOnGround);
        registerSetting(maxStepHeight);
        registerSetting(mode);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, mode, maxStepHeight);

        behaviorGroup = createGroup("Behavior", "Step behavior");
        behaviorGroup.addSettings(onlyWhenSprinting, onlyOnGround);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Step assist logic
    }
}