package com.nullcraft.client.modules.movement;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * AutoJump - Automatically jumps when blocked.
 */
public class AutoJumpModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting onlyWhenSprinting;
    private final BooleanSetting onlyOnGround;
    private final BooleanSetting avoidFallDamage;
    private final IntegerSetting maxFallHeight;
    private final ModeSetting jumpMode;

    public AutoJumpModule() {
        super("auto_jump", "AutoJump", "Automatically jumps when blocked", ModuleCategory.MOVEMENT, "icon_auto_jump");

        this.enabled = new BooleanSetting("enabled", "Enable AutoJump", true);
        this.onlyWhenSprinting = new BooleanSetting("only_when_sprinting", "Only jump when sprinting", false);
        this.onlyOnGround = new BooleanSetting("only_on_ground", "Only jump from ground", true);
        this.avoidFallDamage = new BooleanSetting("avoid_fall_damage", "Avoid fall damage", true);
        this.maxFallHeight = new IntegerSetting("max_fall_height", "Max safe fall height (blocks)", 3, 1, 10);
        this.jumpMode = new ModeSetting("jump_mode", "Jump mode", "smart", "always", "smart", "only_obstacles");

        registerSetting(enabled);
        registerSetting(onlyWhenSprinting);
        registerSetting(onlyOnGround);
        registerSetting(avoidFallDamage);
        registerSetting(maxFallHeight);
        registerSetting(jumpMode);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, jumpMode);

        behaviorGroup = createGroup("Behavior", "Jump behavior");
        behaviorGroup.addSettings(onlyWhenSprinting, onlyOnGround, avoidFallDamage, maxFallHeight);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Auto-jump logic
    }
}