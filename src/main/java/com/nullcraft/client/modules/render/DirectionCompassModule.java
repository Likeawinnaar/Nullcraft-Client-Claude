package com.nullcraft.client.modules.render;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * DirectionCompass - Shows a compass with direction.
 */
public class DirectionCompassModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup styleGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showCardinalDirections;
    private final BooleanSetting showDegrees;
    private final BooleanSetting showBiome;
    private final ModeSetting style;
    private final ModeSetting position;
    private final IntegerSetting size;
    private final ColorSetting northColor;
    private final ColorSetting otherColor;
    private final ColorSetting backgroundColor;
    private final ColorSetting needleColor;

    public DirectionCompassModule() {
        super("direction_compass", "DirectionCompass", "Shows a compass with direction", ModuleCategory.RENDER, "icon_direction_compass");

        this.enabled = new BooleanSetting("enabled", "Enable DirectionCompass", true);
        this.showCardinalDirections = new BooleanSetting("show_cardinal", "Show cardinal directions (N/E/S/W)", true);
        this.showDegrees = new BooleanSetting("show_degrees", "Show degrees", false);
        this.showBiome = new BooleanSetting("show_biome", "Show current biome", true);
        this.style = new ModeSetting("style", "Compass style", "horizontal", "horizontal", "circular", "minimal");
        this.position = new ModeSetting("position", "HUD position", "top_center", "top_left", "top_center", "top_right", "bottom_left", "bottom_center", "bottom_right");
        this.size = new IntegerSetting("size", "Compass size", 200, 50, 500);
        this.northColor = new ColorSetting("north_color", "North indicator color", 0xFFE65B7A);
        this.otherColor = new ColorSetting("other_color", "Other directions color", 0xFFA1A1B5);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);
        this.needleColor = new ColorSetting("needle_color", "Needle color", 0xFF8B5CF6);

        registerSetting(enabled);
        registerSetting(showCardinalDirections);
        registerSetting(showDegrees);
        registerSetting(showBiome);
        registerSetting(style);
        registerSetting(position);
        registerSetting(size);
        registerSetting(northColor);
        registerSetting(otherColor);
        registerSetting(backgroundColor);
        registerSetting(needleColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showCardinalDirections, showDegrees, showBiome, style, position, size);

        styleGroup = createGroup("Colors", "Display colors");
        styleGroup.addSettings(northColor, otherColor, backgroundColor, needleColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Compass rendering handled by HudManager
    }
}