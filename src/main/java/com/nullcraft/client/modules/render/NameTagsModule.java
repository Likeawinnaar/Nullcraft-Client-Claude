package com.nullcraft.client.modules.render;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * NameTags - Custom name tag rendering.
 */
public class NameTagsModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showPlayers;
    private final BooleanSetting showMobs;
    private final BooleanSetting showDistance;
    private final BooleanSetting showHealth;
    private final BooleanSetting showArmor;
    private final ModeSetting style;
    private final IntegerSetting maxDistance;
    private final ColorSetting backgroundColor;
    private final ColorSetting textColor;
    private final DoubleSetting scale;

    public NameTagsModule() {
        super("name_tags", "NameTags", "Custom name tag rendering", ModuleCategory.RENDER, "icon_name_tags");

        this.enabled = new BooleanSetting("enabled", "Enable NameTags", true);
        this.showPlayers = new BooleanSetting("show_players", "Show player names", true);
        this.showMobs = new BooleanSetting("show_mobs", "Show mob names", false);
        this.showDistance = new BooleanSetting("show_distance", "Show distance", true);
        this.showHealth = new BooleanSetting("show_health", "Show health", false);
        this.showArmor = new BooleanSetting("show_armor", "Show armor", false);
        this.style = new ModeSetting("style", "Name tag style", "default", "default", "minimal", "detailed", "compact");
        this.maxDistance = new IntegerSetting("max_distance", "Max render distance", 64, 10, 200);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC000000, true);
        this.textColor = new ColorSetting("text_color", "Text color", 0xFFFFFFFF);
        this.scale = new DoubleSetting("scale", "Name tag scale", 1.0, 0.5, 2.0, 0.1);

        registerSetting(enabled);
        registerSetting(showPlayers);
        registerSetting(showMobs);
        registerSetting(showDistance);
        registerSetting(showHealth);
        registerSetting(showArmor);
        registerSetting(style);
        registerSetting(maxDistance);
        registerSetting(backgroundColor);
        registerSetting(textColor);
        registerSetting(scale);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showPlayers, showMobs, showDistance, showHealth, showArmor, style, maxDistance, scale);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(backgroundColor, textColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Name tag rendering
    }
}