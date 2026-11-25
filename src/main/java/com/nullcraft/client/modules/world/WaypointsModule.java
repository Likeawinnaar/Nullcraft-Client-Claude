package com.nullcraft.client.modules.world;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * Waypoints - Custom waypoints with teleportation (creative) or navigation.
 */
public class WaypointsModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showOnHUD;
    private final BooleanSetting showInWorld;
    private final BooleanSetting showDistance;
    private final ModeSetting renderStyle;
    private final IntegerSetting maxRenderDistance;
    private final ColorSetting defaultColor;
    private final BooleanSetting enableTeleport;
    private final BooleanSetting requirePermission;

    public WaypointsModule() {
        super("waypoints", "Waypoints", "Custom waypoints with navigation", ModuleCategory.WORLD, "icon_waypoints");

        this.enabled = new BooleanSetting("enabled", "Enable Waypoints", true);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", true);
        this.showInWorld = new BooleanSetting("show_in_world", "Show in world", true);
        this.showDistance = new BooleanSetting("show_distance", "Show distance", true);
        this.renderStyle = new ModeSetting("render_style", "Render style", "beacon", "beacon", "marker", "text", "minimal");
        this.maxRenderDistance = new IntegerSetting("max_render_distance", "Max render distance", 500, 50, 2000);
        this.defaultColor = new ColorSetting("default_color", "Default waypoint color", 0xFF8B5CF6);
        this.enableTeleport = new BooleanSetting("enable_teleport", "Enable teleport (creative/op)", false);
        this.requirePermission = new BooleanSetting("require_permission", "Require permission for teleport", true);

        registerSetting(enabled);
        registerSetting(showOnHUD);
        registerSetting(showInWorld);
        registerSetting(showDistance);
        registerSetting(renderStyle);
        registerSetting(maxRenderDistance);
        registerSetting(defaultColor);
        registerSetting(enableTeleport);
        registerSetting(requirePermission);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showOnHUD, showInWorld, showDistance, renderStyle, maxRenderDistance, enableTeleport, requirePermission);

        displayGroup = createGroup("Appearance", "Default appearance");
        displayGroup.addSettings(defaultColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // Waypoint rendering
    }
}