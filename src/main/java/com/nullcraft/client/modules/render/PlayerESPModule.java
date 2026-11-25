package com.nullcraft.client.modules.render;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * PlayerESP - Highlights players through walls.
 */
public class PlayerESPModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup visualsGroup;
    private final SettingGroup filtersGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting boxESP;
    private final BooleanSetting skeletonESP;
    private final BooleanSetting tracerESP;
    private final BooleanSetting nameTags;
    private final BooleanSetting healthBar;
    private final BooleanSetting armorIndicators;
    private final BooleanSetting distance;
    private final ModeSetting boxMode;
    private final ColorSetting friendColor;
    private final ColorSetting enemyColor;
    private final ColorSetting neutralColor;
    private final IntegerSetting maxDistance;
    private final BooleanSetting ignoreInvisible;
    private final BooleanSetting ignoreTeammates;

    public PlayerESPModule() {
        super("player_esp", "PlayerESP", "Highlights players through walls", ModuleCategory.RENDER, "icon_player_esp");

        this.enabled = new BooleanSetting("enabled", "Enable PlayerESP", true);
        this.boxESP = new BooleanSetting("box_esp", "Show bounding box", true);
        this.skeletonESP = new BooleanSetting("skeleton_esp", "Show skeleton", false);
        this.tracerESP = new BooleanSetting("tracer_esp", "Show tracers", false);
        this.nameTags = new BooleanSetting("name_tags", "Show name tags", true);
        this.healthBar = new BooleanSetting("health_bar", "Show health bar", true);
        this.armorIndicators = new BooleanSetting("armor_indicators", "Show armor", false);
        this.distance = new BooleanSetting("distance", "Show distance", true);
        this.boxMode = new ModeSetting("box_mode", "Box mode", "corners", "full", "corners", "2d");
        this.friendColor = new ColorSetting("friend_color", "Friend color", 0xFF4CBF80);
        this.enemyColor = new ColorSetting("enemy_color", "Enemy color", 0xFFE65B7A);
        this.neutralColor = new ColorSetting("neutral_color", "Neutral color", 0xFFA78BFA);
        this.maxDistance = new IntegerSetting("max_distance", "Max render distance", 100, 10, 500);
        this.ignoreInvisible = new BooleanSetting("ignore_invisible", "Ignore invisible players", false);
        this.ignoreTeammates = new BooleanSetting("ignore_teammates", "Ignore teammates", true);

        registerSetting(enabled);
        registerSetting(boxESP);
        registerSetting(skeletonESP);
        registerSetting(tracerESP);
        registerSetting(nameTags);
        registerSetting(healthBar);
        registerSetting(armorIndicators);
        registerSetting(distance);
        registerSetting(boxMode);
        registerSetting(friendColor);
        registerSetting(enemyColor);
        registerSetting(neutralColor);
        registerSetting(maxDistance);
        registerSetting(ignoreInvisible);
        registerSetting(ignoreTeammates);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, maxDistance, ignoreInvisible, ignoreTeammates);

        visualsGroup = createGroup("Visuals", "ESP visuals");
        visualsGroup.addSettings(boxESP, skeletonESP, tracerESP, nameTags, healthBar, armorIndicators, distance, boxMode);

        filtersGroup = createGroup("Colors", "Team colors");
        filtersGroup.addSettings(friendColor, enemyColor, neutralColor);
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