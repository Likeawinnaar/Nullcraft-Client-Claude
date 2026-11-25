package com.nullcraft.client.modules.player;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * AutoEat - Automatically eats food when hungry.
 */
public class AutoEatModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final IntegerSetting hungerThreshold;
    private final IntegerSetting saturationThreshold;
    private final BooleanSetting preferHighSaturation;
    private final BooleanSetting eatWhileWalking;
    private final BooleanSetting eatWhileSprinting;
    private final BooleanSetting stopOnFull;
    private final ModeSetting foodPriority;

    public AutoEatModule() {
        super("auto_eat", "AutoEat", "Automatically eats food when hungry", ModuleCategory.PLAYER, "icon_auto_eat");

        this.enabled = new BooleanSetting("enabled", "Enable AutoEat", true);
        this.hungerThreshold = new IntegerSetting("hunger_threshold", "Hunger threshold (shanks)", 14, 0, 20);
        this.saturationThreshold = new IntegerSetting("saturation_threshold", "Saturation threshold", 5, 0, 20);
        this.preferHighSaturation = new BooleanSetting("prefer_high_saturation", "Prefer high saturation foods", true);
        this.eatWhileWalking = new BooleanSetting("eat_while_walking", "Eat while walking", true);
        this.eatWhileSprinting = new BooleanSetting("eat_while_sprinting", "Eat while sprinting", false);
        this.stopOnFull = new BooleanSetting("stop_on_full", "Stop eating when full", true);
        this.foodPriority = new ModeSetting("food_priority", "Food priority", "saturation", "saturation", "hunger", "stack_size");

        registerSetting(enabled);
        registerSetting(hungerThreshold);
        registerSetting(saturationThreshold);
        registerSetting(preferHighSaturation);
        registerSetting(eatWhileWalking);
        registerSetting(eatWhileSprinting);
        registerSetting(stopOnFull);
        registerSetting(foodPriority);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, hungerThreshold, saturationThreshold, foodPriority);

        behaviorGroup = createGroup("Behavior", "Eating behavior");
        behaviorGroup.addSettings(preferHighSaturation, eatWhileWalking, eatWhileSprinting, stopOnFull);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Auto-eat logic
    }
}