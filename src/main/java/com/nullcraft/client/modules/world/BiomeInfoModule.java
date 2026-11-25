package com.nullcraft.client.modules.world;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * BiomeInfo - Shows current biome information.
 */
public class BiomeInfoModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showBiomeName;
    private final BooleanSetting showBiomeCategory;
    private final BooleanSetting showTemperature;
    private final BooleanSetting showRainfall;
    private final BooleanSetting showHeightVariation;
    private final ModeSetting position;
    private final ColorSetting color;
    private final ColorSetting backgroundColor;

    public BiomeInfoModule() {
        super("biome_info", "BiomeInfo", "Shows current biome information", ModuleCategory.WORLD, "icon_biome_info");

        this.enabled = new BooleanSetting("enabled", "Enable BiomeInfo", true);
        this.showBiomeName = new BooleanSetting("show_biome_name", "Show biome name", true);
        this.showBiomeCategory = new BooleanSetting("show_biome_category", "Show biome category", true);
        this.showTemperature = new BooleanSetting("show_temperature", "Show temperature", false);
        this.showRainfall = new BooleanSetting("show_rainfall", "Show rainfall", false);
        this.showHeightVariation = new BooleanSetting("show_height_variation", "Show height variation", false);
        this.position = new ModeSetting("position", "HUD position", "top_right", "top_left", "top_right", "bottom_left", "bottom_right");
        this.color = new ColorSetting("color", "Text color", 0xFF4CBF80);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);

        registerSetting(enabled);
        registerSetting(showBiomeName);
        registerSetting(showBiomeCategory);
        registerSetting(showTemperature);
        registerSetting(showRainfall);
        registerSetting(showHeightVariation);
        registerSetting(position);
        registerSetting(color);
        registerSetting(backgroundColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showBiomeName, showBiomeCategory, showTemperature, showRainfall, showHeightVariation, position);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(color, backgroundColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Biome info rendering
    }
}