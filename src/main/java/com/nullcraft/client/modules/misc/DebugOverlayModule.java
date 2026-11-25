package com.nullcraft.client.modules.misc;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * DebugOverlay - Shows debug information overlay.
 */
public class DebugOverlayModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showFPS;
    private final BooleanSetting showPing;
    private final BooleanSetting showTPS;
    private final BooleanSetting showMemory;
    private final BooleanSetting showCoordinates;
    private final BooleanSetting showBiome;
    private final BooleanSetting showDirection;
    private final BooleanSetting showLightLevel;
    private final BooleanSetting showEntityCount;
    private final ModeSetting position;
    private final ColorSetting textColor;
    private final ColorSetting backgroundColor;

    public DebugOverlayModule() {
        super("debug_overlay", "DebugOverlay", "Shows debug information overlay", ModuleCategory.MISC, "icon_debug_overlay");

        this.enabled = new BooleanSetting("enabled", "Enable DebugOverlay", false);
        this.showFPS = new BooleanSetting("show_fps", "Show FPS", true);
        this.showPing = new BooleanSetting("show_ping", "Show ping", true);
        this.showTPS = new BooleanSetting("show_tps", "Show TPS", true);
        this.showMemory = new BooleanSetting("show_memory", "Show memory", true);
        this.showCoordinates = new BooleanSetting("show_coordinates", "Show coordinates", true);
        this.showBiome = new BooleanSetting("show_biome", "Show biome", true);
        this.showDirection = new BooleanSetting("show_direction", "Show direction", true);
        this.showLightLevel = new BooleanSetting("show_light_level", "Show light level", false);
        this.showEntityCount = new BooleanSetting("show_entity_count", "Show entity count", false);
        this.position = new ModeSetting("position", "HUD position", "top_left", "top_left", "top_right", "bottom_left", "bottom_right");
        this.textColor = new ColorSetting("text_color", "Text color", 0xFFA1A1B5);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);

        registerSetting(enabled);
        registerSetting(showFPS);
        registerSetting(showPing);
        registerSetting(showTPS);
        registerSetting(showMemory);
        registerSetting(showCoordinates);
        registerSetting(showBiome);
        registerSetting(showDirection);
        registerSetting(showLightLevel);
        registerSetting(showEntityCount);
        registerSetting(position);
        registerSetting(textColor);
        registerSetting(backgroundColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, position);

        displayGroup = createGroup("Display", "Information to show");
        displayGroup.addSettings(showFPS, showPing, showTPS, showMemory, showCoordinates, showBiome, showDirection, showLightLevel, showEntityCount);
        displayGroup.addSettings(textColor, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Debug overlay rendering
    }
}