package com.nullcraft.client.modules.misc;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * ScreenshotManager - Enhanced screenshot functionality.
 */
public class ScreenshotManagerModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup behaviorGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting customScreenshotKey;
    private final BooleanSetting hideHUD;
    private final BooleanSetting hideChat;
    private final BooleanSetting addWatermark;
    private final BooleanSetting autoSave;
    private final ModeSetting format;
    private final IntegerSetting quality;
    private final StringSetting customPath;

    public ScreenshotManagerModule() {
        super("screenshot_manager", "ScreenshotManager", "Enhanced screenshot functionality", ModuleCategory.MISC, "icon_screenshot_manager");

        this.enabled = new BooleanSetting("enabled", "Enable ScreenshotManager", true);
        this.customScreenshotKey = new BooleanSetting("custom_screenshot_key", "Use custom screenshot key", false);
        this.hideHUD = new BooleanSetting("hide_hud", "Hide HUD in screenshots", true);
        this.hideChat = new BooleanSetting("hide_chat", "Hide chat in screenshots", true);
        this.addWatermark = new BooleanSetting("add_watermark", "Add NullCraft watermark", true);
        this.autoSave = new BooleanSetting("auto_save", "Auto-save to custom folder", false);
        this.format = new ModeSetting("format", "Image format", "png", "png", "jpg");
        this.quality = new IntegerSetting("quality", "JPEG quality", 95, 50, 100);
        this.customPath = new StringSetting("custom_path", "Custom save path", "", 512, "", false);

        registerSetting(enabled);
        registerSetting(customScreenshotKey);
        registerSetting(hideHUD);
        registerSetting(hideChat);
        registerSetting(addWatermark);
        registerSetting(autoSave);
        registerSetting(format);
        registerSetting(quality);
        registerSetting(customPath);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, customScreenshotKey, hideHUD, hideChat, addWatermark, autoSave);

        behaviorGroup = createGroup("Output", "Output settings");
        behaviorGroup.addSettings(format, quality, customPath);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Screenshot logic
    }
}