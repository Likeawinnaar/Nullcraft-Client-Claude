package com.nullcraft.client.modules.player;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * AutoRefill - Automatically refills hotbar from inventory.
 */
public class AutoRefillModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting refillOnBreak;
    private final BooleanSetting refillOnEmpty;
    private final BooleanSetting onlySameItem;
    private final BooleanSetting preferFullStacks;
    private final IntegerSetting refillDelay;

    public AutoRefillModule() {
        super("auto_refill", "AutoRefill", "Automatically refills hotbar from inventory", ModuleCategory.PLAYER, "icon_auto_refill");

        this.enabled = new BooleanSetting("enabled", "Enable AutoRefill", true);
        this.refillOnBreak = new BooleanSetting("refill_on_break", "Refill when tool breaks", true);
        this.refillOnEmpty = new BooleanSetting("refill_on_empty", "Refill when slot empty", true);
        this.onlySameItem = new BooleanSetting("only_same_item", "Only refill with same item", true);
        this.preferFullStacks = new BooleanSetting("prefer_full_stacks", "Prefer full stacks", true);
        this.refillDelay = new IntegerSetting("refill_delay", "Refill delay (ticks)", 2, 0, 20);

        registerSetting(enabled);
        registerSetting(refillOnBreak);
        registerSetting(refillOnEmpty);
        registerSetting(onlySameItem);
        registerSetting(preferFullStacks);
        registerSetting(refillDelay);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, refillOnBreak, refillOnEmpty, refillDelay);

        behaviorGroup = createGroup("Behavior", "Refill behavior");
        behaviorGroup.addSettings(onlySameItem, preferFullStacks);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Auto-refill logic
    }
}