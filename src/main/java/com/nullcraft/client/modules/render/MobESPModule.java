package com.nullcraft.client.modules.render;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * MobESP - Highlights mobs through walls.
 */
public class MobESPModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup visualsGroup;
    private final SettingGroup filtersGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting boxESP;
    private final BooleanSetting nameTags;
    private final BooleanSetting healthBar;
    private final BooleanSetting distance;
    private final ModeSetting boxMode;
    private final ColorSetting hostileColor;
    private final ColorSetting passiveColor;
    private final ColorSetting neutralColor;
    private final IntegerSetting maxDistance;
    private final BooleanSetting ignoreTamed;
    private final MultiSelectSetting mobTypes;

    public MobESPModule() {
        super("mob_esp", "MobESP", "Highlights mobs through walls", ModuleCategory.RENDER, "icon_mob_esp");

        this.enabled = new BooleanSetting("enabled", "Enable MobESP", true);
        this.boxESP = new BooleanSetting("box_esp", "Show bounding box", true);
        this.nameTags = new BooleanSetting("name_tags", "Show name tags", true);
        this.healthBar = new BooleanSetting("health_bar", "Show health bar", true);
        this.distance = new BooleanSetting("distance", "Show distance", true);
        this.boxMode = new ModeSetting("box_mode", "Box mode", "corners", "full", "corners", "2d");
        this.hostileColor = new ColorSetting("hostile_color", "Hostile mob color", 0xFFE65B7A);
        this.passiveColor = new ColorSetting("passive_color", "Passive mob color", 0xFF4CBF80);
        this.neutralColor = new ColorSetting("neutral_color", "Neutral mob color", 0xFFF3AB4B);
        this.maxDistance = new IntegerSetting("max_distance", "Max render distance", 80, 10, 500);
        this.ignoreTamed = new BooleanSetting("ignore_tamed", "Ignore tamed mobs", true);
        this.mobTypes = new MultiSelectSetting("mob_types", "Mob types to render", Set.of("hostile", "passive", "neutral"), "hostile", "passive", "neutral", "boss", "water", "ambient");

        registerSetting(enabled);
        registerSetting(boxESP);
        registerSetting(nameTags);
        registerSetting(healthBar);
        registerSetting(distance);
        registerSetting(boxMode);
        registerSetting(hostileColor);
        registerSetting(passiveColor);
        registerSetting(neutralColor);
        registerSetting(maxDistance);
        registerSetting(ignoreTamed);
        registerSetting(mobTypes);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, maxDistance, ignoreTamed);

        visualsGroup = createGroup("Visuals", "ESP visuals");
        visualsGroup.addSettings(boxESP, nameTags, healthBar, distance, boxMode);

        filtersGroup = createGroup("Filters", "Mob type filters and colors");
        filtersGroup.addSettings(mobTypes, hostileColor, passiveColor, neutralColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // ESP rendering logic
    }
}