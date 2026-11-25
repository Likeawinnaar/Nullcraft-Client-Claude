package com.nullcraft.client.modules.movement;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * SafeWalk - Prevents falling off edges.
 */
public class SafeWalkModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting onlyWhenSneaking;
    private final BooleanSetting preventFalling;
    private final BooleanSetting preventVoidFall;
    private final DoubleSetting edgeDistance;
    private final ModeSetting mode;

    public SafeWalkModule() {
        super("safe_walk", "SafeWalk", "Prevents falling off edges", ModuleCategory.MOVEMENT, "icon_safe_walk");

        this.enabled = new BooleanSetting("enabled", "Enable SafeWalk", true);
        this.onlyWhenSneaking = new BooleanSetting("only_when_sneaking", "Only when sneaking", false);
        this.preventFalling = new BooleanSetting("prevent_falling", "Prevent falling off blocks", true);
        this.preventVoidFall = new BooleanSetting("prevent_void_fall", "Prevent falling into void", true);
        this.edgeDistance = new DoubleSetting("edge_distance", "Edge detection distance", 0.3, 0.1, 1.0, 0.05);
        this.mode = new ModeSetting("mode", "Safe walk mode", "strict", "strict", "lenient", "parkour");

        registerSetting(enabled);
        registerSetting(onlyWhenSneaking);
        registerSetting(preventFalling);
        registerSetting(preventVoidFall);
        registerSetting(edgeDistance);
        registerSetting(mode);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, mode);

        behaviorGroup = createGroup("Behavior", "Safety options");
        behaviorGroup.addSettings(onlyWhenSneaking, preventFalling, preventVoidFall, edgeDistance);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Safe walk logic
    }
}