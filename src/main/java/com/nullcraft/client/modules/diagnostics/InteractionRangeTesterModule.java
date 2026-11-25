package com.nullcraft.client.modules.diagnostics;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * InteractionRangeTester - Tests block/entity interaction range.
 */
public class InteractionRangeTesterModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showBlockRange;
    private final BooleanSetting showEntityRange;
    private final BooleanSetting showAttackRange;
    private final BooleanSetting testOnKeybind;
    private final ModeSetting displayMode;
    private final ColorSetting inRangeColor;
    private final ColorSetting outOfRangeColor;

    public InteractionRangeTesterModule() {
        super("interaction_range_tester", "InteractionRangeTester", "Tests block/entity interaction range", ModuleCategory.DIAGNOSTICS, "icon_interaction_range_tester");

        this.enabled = new BooleanSetting("enabled", "Enable InteractionRangeTester", false);
        this.showBlockRange = new BooleanSetting("show_block_range", "Show block interaction range", true);
        this.showEntityRange = new BooleanSetting("show_entity_range", "Show entity interaction range", true);
        this.showAttackRange = new BooleanSetting("show_attack_range", "Show attack range", true);
        this.testOnKeybind = new BooleanSetting("test_on_keybind", "Test on keybind press", true);
        this.displayMode = new ModeSetting("display_mode", "Display mode", "overlay", "overlay", "hud", "both");
        this.inRangeColor = new ColorSetting("in_range_color", "In range color", 0xFF4CBF80);
        this.outOfRangeColor = new ColorSetting("out_of_range_color", "Out of range color", 0xFFE65B7A);

        registerSetting(enabled);
        registerSetting(showBlockRange);
        registerSetting(showEntityRange);
        registerSetting(showAttackRange);
        registerSetting(testOnKeybind);
        registerSetting(displayMode);
        registerSetting(inRangeColor);
        registerSetting(outOfRangeColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showBlockRange, showEntityRange, showAttackRange, testOnKeybind, displayMode);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(inRangeColor, outOfRangeColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Interaction range testing
    }
}