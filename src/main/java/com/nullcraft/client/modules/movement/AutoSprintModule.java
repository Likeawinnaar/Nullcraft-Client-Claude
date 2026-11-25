package com.nullcraft.client.modules.movement;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * AutoSprint - Automatically sprints when moving forward.
 */
public class AutoSprintModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting onlyOnGround;
    private final BooleanSetting stopOnAttack;
    private final BooleanSetting stopOnSneak;
    private final BooleanSetting stopOnEat;
    private final BooleanSetting stopOnLowHunger;
    private final IntegerSetting minHunger;

    public AutoSprintModule() {
        super("auto_sprint", "AutoSprint", "Automatically sprints when moving forward", ModuleCategory.MOVEMENT, "icon_auto_sprint");

        this.enabled = new BooleanSetting("enabled", "Enable AutoSprint", true);
        this.onlyOnGround = new BooleanSetting("only_on_ground", "Only sprint on ground", false);
        this.stopOnAttack = new BooleanSetting("stop_on_attack", "Stop sprinting when attacking", true);
        this.stopOnSneak = new BooleanSetting("stop_on_sneak", "Stop sprinting when sneaking", true);
        this.stopOnEat = new BooleanSetting("stop_on_eat", "Stop sprinting when eating", true);
        this.stopOnLowHunger = new BooleanSetting("stop_on_low_hunger", "Stop sprinting when hunger low", true);
        this.minHunger = new IntegerSetting("min_hunger", "Minimum hunger to sprint", 6, 0, 20);

        registerSetting(enabled);
        registerSetting(onlyOnGround);
        registerSetting(stopOnAttack);
        registerSetting(stopOnSneak);
        registerSetting(stopOnEat);
        registerSetting(stopOnLowHunger);
        registerSetting(minHunger);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled);

        behaviorGroup = createGroup("Behavior", "Sprint behavior");
        behaviorGroup.addSettings(onlyOnGround, stopOnAttack, stopOnSneak, stopOnEat, stopOnLowHunger, minHunger);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Auto-sprint logic would go here
    }
}