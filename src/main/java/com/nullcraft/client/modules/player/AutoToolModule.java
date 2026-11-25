package com.nullcraft.client.modules.player;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * AutoTool - Automatically switches to the best tool for the block.
 */
public class AutoToolModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting includeSword;
    private final BooleanSetting onlyWhenMining;
    private final BooleanSetting preferSpeed;
    private final BooleanSetting preferDurability;
    private final BooleanSetting switchBack;
    private final IntegerSetting switchDelay;

    public AutoToolModule() {
        super("auto_tool", "AutoTool", "Automatically switches to the best tool for the block", ModuleCategory.PLAYER, "icon_auto_tool");

        this.enabled = new BooleanSetting("enabled", "Enable AutoTool", true);
        this.includeSword = new BooleanSetting("include_sword", "Include swords for combat", false);
        this.onlyWhenMining = new BooleanSetting("only_when_mining", "Only switch when mining", true);
        this.preferSpeed = new BooleanSetting("prefer_speed", "Prefer mining speed", true);
        this.preferDurability = new BooleanSetting("prefer_durability", "Prefer durability", false);
        this.switchBack = new BooleanSetting("switch_back", "Switch back after mining", true);
        this.switchDelay = new IntegerSetting("switch_delay", "Switch delay (ticks)", 0, 0, 10);

        registerSetting(enabled);
        registerSetting(includeSword);
        registerSetting(onlyWhenMining);
        registerSetting(preferSpeed);
        registerSetting(preferDurability);
        registerSetting(switchBack);
        registerSetting(switchDelay);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, includeSword, onlyWhenMining, switchBack, switchDelay);

        behaviorGroup = createGroup("Preferences", "Tool preferences");
        behaviorGroup.addSettings(preferSpeed, preferDurability);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Auto-tool logic
    }
}