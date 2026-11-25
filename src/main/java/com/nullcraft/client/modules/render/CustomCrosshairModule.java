package com.nullcraft.client.modules.render;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * CustomCrosshair - Customizable crosshair overlay.
 */
public class CustomCrosshairModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup styleGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showDefault;
    private final ModeSetting style;
    private final IntegerSetting size;
    private final IntegerSetting gap;
    private final IntegerSetting thickness;
    private final IntegerSetting dotSize;
    private final ColorSetting color;
    private final ColorSetting outlineColor;
    private final BooleanSetting dynamicGap;
    private final DoubleSetting opacity;

    public CustomCrosshairModule() {
        super("custom_crosshair", "CustomCrosshair", "Customizable crosshair overlay", ModuleCategory.RENDER, "icon_custom_crosshair");

        this.enabled = new BooleanSetting("enabled", "Enable CustomCrosshair", true);
        this.showDefault = new BooleanSetting("show_default", "Show default crosshair", false);
        this.style = new ModeSetting("style", "Crosshair style", "dot", "default", "dot", "cross", "circle", "t_square", "dynamic");
        this.size = new IntegerSetting("size", "Crosshair size", 8, 1, 50);
        this.gap = new IntegerSetting("gap", "Gap size", 4, 0, 20);
        this.thickness = new IntegerSetting("thickness", "Line thickness", 1, 1, 10);
        this.dotSize = new IntegerSetting("dot_size", "Center dot size", 2, 1, 10);
        this.color = new ColorSetting("color", "Crosshair color", 0xFFFFFFFF);
        this.outlineColor = new ColorSetting("outline_color", "Outline color", 0xFF000000, true);
        this.dynamicGap = new BooleanSetting("dynamic_gap", "Dynamic gap (spread)", false);
        this.opacity = new DoubleSetting("opacity", "Opacity", 1.0, 0.1, 1.0, 0.1);

        registerSetting(enabled);
        registerSetting(showDefault);
        registerSetting(style);
        registerSetting(size);
        registerSetting(gap);
        registerSetting(thickness);
        registerSetting(dotSize);
        registerSetting(color);
        registerSetting(outlineColor);
        registerSetting(dynamicGap);
        registerSetting(opacity);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showDefault, style, opacity);

        styleGroup = createGroup("Style", "Crosshair style");
        styleGroup.addSettings(size, gap, thickness, dotSize, color, outlineColor, dynamicGap);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Crosshair rendering
    }
}