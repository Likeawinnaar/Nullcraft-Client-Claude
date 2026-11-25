package com.nullcraft.client.modules.misc;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * FriendManager - Manages friend list with highlighting.
 */
public class FriendManagerModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup displayGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showFriendsList;
    private final BooleanSetting highlightFriends;
    private final BooleanSetting showFriendNotifications;
    private final StringSetting friendsList;
    private final ColorSetting friendColor;
    private final ColorSetting friendNameColor;

    public FriendManagerModule() {
        super("friend_manager", "FriendManager", "Manages friend list with highlighting", ModuleCategory.MISC, "icon_friend_manager");

        this.enabled = new BooleanSetting("enabled", "Enable FriendManager", true);
        this.showFriendsList = new BooleanSetting("show_friends_list", "Show friends list on HUD", false);
        this.highlightFriends = new BooleanSetting("highlight_friends", "Highlight friends in world", true);
        this.showFriendNotifications = new BooleanSetting("show_friend_notifications", "Show friend join/leave notifications", true);
        this.friendsList = new StringSetting("friends_list", "Friends list (comma-separated names)", "", 4096, "", false);
        this.friendColor = new ColorSetting("friend_color", "Friend highlight color", 0xFF4CBF80);
        this.friendNameColor = new ColorSetting("friend_name_color", "Friend name color", 0xFF8B5CF6);

        registerSetting(enabled);
        registerSetting(showFriendsList);
        registerSetting(highlightFriends);
        registerSetting(showFriendNotifications);
        registerSetting(friendsList);
        registerSetting(friendColor);
        registerSetting(friendNameColor);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, showFriendsList, highlightFriends, showFriendNotifications, friendsList);

        displayGroup = createGroup("Colors", "Friend colors");
        displayGroup.addSettings(friendColor, friendNameColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        // Friend management logic
    }
}