package com.nullcraft.client.modules.player;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * EquipmentHUD - Shows equipped items on HUD.
 */
public class EquipmentHUDModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showArmor;
    private final BooleanSetting showOffhand;
    private final BooleanSetting showMainHand;
    private final BooleanSetting showDurability;
    private final ModeSetting position;
    private final DoubleSetting scale;
    private final ColorSetting backgroundColor;
    private final ColorSetting borderColor;

    public EquipmentHUDModule() {
        super("equipment_hud", "EquipmentHUD", "Shows equipped items on HUD", ModuleCategory.PLAYER, "icon_equipment_hud");

        this.enabled = new BooleanSetting("enabled", "Enable EquipmentHUD", true);
        this.showArmor = new BooleanSetting("show_armor", "Show armor slots", true);
        this.showOffhand = new BooleanSetting("show_offhand", "Show offhand", true);
        this.showMainHand = new BooleanSetting("show_main_hand", "Show main hand", true);
        this.showDurability = new BooleanSetting("show_durability", "Show durability", true);
        this.position = new ModeSetting("position", "HUD position", "bottom_center", "top_left", "top_right", "bottom_left", "bottom_right", "bottom_center", "center");
        this.scale = new DoubleSetting("scale", "HUD scale", 1.0, 0.5, 2.0, 0.1);
        this.backgroundColor = new ColorSetting("background_color", "Background color", 0xCC1C1E29, true);
        this.borderColor = new ColorSetting("border_color", "Border color", 0xFF343644);

        registerSetting(enabled);
        registerSetting(showArmor);
        registerSetting(showOffhand);
        registerSetting(showMainHand);
        registerSetting(showDurability);
        registerSetting(position);
        registerSetting(scale);
        registerSetting(backgroundColor);
        registerSetting(borderColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showArmor, showOffhand, showMainHand, showDurability, position, scale);

        displayGroup = createGroup("Colors", "Display colors");
        displayGroup.addSettings(backgroundColor, borderColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Rendering handled by HudManager
    }
}