package com.nullcraft.client.modules.misc;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * ChatFilter - Filters chat messages based on rules.
 */
public class ChatFilterModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup filtersGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting filterProfanity;
    private final BooleanSetting filterSpam;
    private final BooleanSetting filterServerMessages;
    private final BooleanSetting filterCommands;
    private final BooleanSetting filterDeathMessages;
    private final StringSetting customFilters;
    private final ModeSetting filterAction;

    public ChatFilterModule() {
        super("chat_filter", "ChatFilter", "Filters chat messages based on rules", ModuleCategory.MISC, "icon_chat_filter");

        this.enabled = new BooleanSetting("enabled", "Enable ChatFilter", true);
        this.filterProfanity = new BooleanSetting("filter_profanity", "Filter profanity", false);
        this.filterSpam = new BooleanSetting("filter_spam", "Filter spam", true);
        this.filterServerMessages = new BooleanSetting("filter_server_messages", "Filter server messages", false);
        this.filterCommands = new BooleanSetting("filter_commands", "Filter command output", false);
        this.filterDeathMessages = new BooleanSetting("filter_death_messages", "Filter death messages", false);
        this.customFilters = new StringSetting("custom_filters", "Custom filters (regex, comma-separated)", "", 2048, "", false);
        this.filterAction = new ModeSetting("filter_action", "Filter action", "hide", "hide", "replace", "notify");

        registerSetting(enabled);
        registerSetting(filterProfanity);
        registerSetting(filterSpam);
        registerSetting(filterServerMessages);
        registerSetting(filterCommands);
        registerSetting(filterDeathMessages);
        registerSetting(customFilters);
        registerSetting(filterAction);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, filterAction);

        filtersGroup = createGroup("Filters", "Chat filters");
        filtersGroup.addSettings(filterProfanity, filterSpam, filterServerMessages, filterCommands, filterDeathMessages, customFilters);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Chat filtering logic
    }
}