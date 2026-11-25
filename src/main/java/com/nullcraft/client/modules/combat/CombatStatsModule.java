package com.nullcraft.client.modules.combat;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * CombatStats - Tracks combat statistics for the session.
 */
public class CombatStatsModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting trackKills;
    private final BooleanSetting trackDeaths;
    private final BooleanSetting trackDamageDealt;
    private final BooleanSetting trackDamageTaken;
    private final BooleanSetting showOnHUD;
    private final ModeSetting hudPosition;
    private final BooleanSetting resetOnWorldChange;

    public CombatStatsModule() {
        super("combat_stats", "CombatStats", "Tracks combat statistics for the session", ModuleCategory.COMBAT, "icon_combat_stats");

        this.enabled = new BooleanSetting("enabled", "Enable CombatStats", true);
        this.trackKills = new BooleanSetting("track_kills", "Track kills", true);
        this.trackDeaths = new BooleanSetting("track_deaths", "Track deaths", true);
        this.trackDamageDealt = new BooleanSetting("track_damage_dealt", "Track damage dealt", true);
        this.trackDamageTaken = new BooleanSetting("track_damage_taken", "Track damage taken", true);
        this.showOnHUD = new BooleanSetting("show_on_hud", "Show on HUD", false);
        this.hudPosition = new ModeSetting("hud_position", "HUD position", "top_left", "top_left", "top_right", "bottom_left", "bottom_right");
        this.resetOnWorldChange = new BooleanSetting("reset_on_world_change", "Reset on world change", true);

        registerSetting(enabled);
        registerSetting(trackKills);
        registerSetting(trackDeaths);
        registerSetting(trackDamageDealt);
        registerSetting(trackDamageTaken);
        registerSetting(showOnHUD);
        registerSetting(hudPosition);
        registerSetting(resetOnWorldChange);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, trackKills, trackDeaths, trackDamageDealt, trackDamageTaken, resetOnWorldChange);

        displayGroup = createGroup("HUD", "HUD display options");
        displayGroup.addSettings(showOnHUD, hudPosition);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Update statistics
    }
}