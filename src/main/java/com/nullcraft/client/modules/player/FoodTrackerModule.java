package com.nullcraft.client.modules.player;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * FoodTracker - Tracks food consumption and saturation.
 */
public class FoodTrackerModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showHunger;
    private final BooleanSetting showSaturation;
    private final BooleanSetting showExhaustion;
    private final BooleanSetting showOnHUD;
    private final ModeSetting position;
    private final ColorSetting hungerColor;
    private final ColorSetting saturationColor;
    private final ColorSetting lowHungerColor;

    public FoodTrackerModule() {
        super("food_tracker", "FoodTracker", "Tracks food consumption and saturation", ModuleCategory.PLAYER, "icon_food_tracker");

        this.enabled = new BooleanSetting("enabled", "Enable FoodTracker", true);
        this.showHunger = new BooleanSetting("show_hunger", "Show hunger level", true);
        this.showSaturation = new BooleanSetting("show_saturation", "Show saturation", true);
        this.showExhaustion = new BooleanSetting("show_exhaustion", "Show exhaustion", false);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", false);
        this.position = new ModeSetting("position", "HUD position", "bottom_left", "top_left", "top_right", "bottom_left", "bottom_right");
        this.hungerColor = new ColorSetting("hunger_color", "Hunger color", 0xFFF3AB4B);
        this.saturationColor = new ColorSetting("saturation_color", "Saturation color", 0xFF4CBF80);
        this.lowHungerColor = new ColorSetting("low_hunger_color", "Low hunger color", 0xFFE65B7A);

        registerSetting(enabled);
        registerSetting(showHunger);
        registerSetting(showSaturation);
        registerSetting(showExhaustion);
        registerSetting(showOnHUD);
        registerSetting(position);
        registerSetting(hungerColor);
        registerSetting(saturationColor);
        registerSetting(lowHungerColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showHunger, showSaturation, showExhaustion, showOnHUD, position);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(hungerColor, saturationColor, lowHungerColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Food tracking logic
    }
}