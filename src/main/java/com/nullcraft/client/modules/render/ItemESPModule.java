package com.nullcraft.client.modules.render;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.settings.*;

/**
 * ItemESP - Highlights dropped items and item frames.
 */
public class ItemESPModule extends Module {

    private final SettingGroup generalGroup;
    private final SettingGroup visualsGroup;
    private final SettingGroup filtersGroup;

    private final BooleanSetting enabled;
    private final BooleanSetting showDroppedItems;
    private final BooleanSetting showItemFrames;
    private final BooleanSetting showChestContents;
    private final BooleanSetting showName;
    private final BooleanSetting showCount;
    private final BooleanSetting showDistance;
    private final ModeSetting renderMode;
    private final IntegerSetting maxDistance;
    private final MultiSelectSetting itemCategories;
    private final ColorSetting defaultColor;
    private final BooleanSetting highlightValuable;

    public ItemESPModule() {
        super("item_esp", "ItemESP", "Highlights dropped items and item frames", ModuleCategory.RENDER, "icon_item_esp");

        this.enabled = new BooleanSetting("enabled", "Enable ItemESP", true);
        this.showDroppedItems = new BooleanSetting("show_dropped_items", "Show dropped items", true);
        this.showItemFrames = new BooleanSetting("show_item_frames", "Show item frames", true);
        this.showChestContents = new BooleanSetting("show_chest_contents", "Show chest contents (when open)", false);
        this.showName = new BooleanSetting("show_name", "Show item name", true);
        this.showCount = new BooleanSetting("show_count", "Show item count", true);
        this.showDistance = new BooleanSetting("show_distance", "Show distance", false);
        this.renderMode = new ModeSetting("render_mode", "Render mode", "outline", "outline", "box", "glow", "text_only");
        this.maxDistance = new IntegerSetting("max_distance", "Max render distance", 50, 10, 200);
        this.itemCategories = new MultiSelectSetting("item_categories", "Item categories", Set.of("all"), "all", "tools", "weapons", "armor", "food", "blocks", "valuable", "other");
        this.defaultColor = new ColorSetting("default_color", "Default color", 0xFFA78BFA);
        this.highlightValuable = new BooleanSetting("highlight_valuable", "Highlight valuable items", true);

        registerSetting(enabled);
        registerSetting(showDroppedItems);
        registerSetting(showItemFrames);
        registerSetting(showChestContents);
        registerSetting(showName);
        registerSetting(showCount);
        registerSetting(showDistance);
        registerSetting(renderMode);
        registerSetting(maxDistance);
        registerSetting(itemCategories);
        registerSetting(defaultColor);
        registerSetting(highlightValuable);
    }

    @Override
    protected void registerSettings() {}

    @Override
    protected void createSettingGroups() {
        generalGroup = createGroup("General", "General settings");
        generalGroup.addSettings(enabled, maxDistance, highlightValuable);

        visualsGroup = createGroup("Visuals", "ESP visuals");
        visualsGroup.addSettings(showDroppedItems, showItemFrames, showChestContents, showName, showCount, showDistance, renderMode);

        filtersGroup = createGroup("Filters", "Item filters and colors");
        filtersGroup.addSettings(itemCategories, defaultColor);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onRender(float tickDelta) {
        // ESP rendering logic
    }
}