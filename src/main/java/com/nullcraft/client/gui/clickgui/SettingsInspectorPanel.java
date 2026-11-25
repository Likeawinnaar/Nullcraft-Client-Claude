package com.nullcraft.client.gui.clickgui;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.settings.Setting;
import com.nullcraft.client.core.settings.SettingGroup;
import com.nullcraft.client.gui.components.*;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Settings inspector panel: Module header + tabbed settings groups
 */
public class SettingsInspectorPanel {

    private final ClickGuiScreen screen;
    private final int x, y, width, height;

    private Module currentModule = null;
    private SettingGroup selectedGroup = null;
    private int scrollOffset = 0;
    private int maxScrollOffset = 0;

    // Components
    private final List<Component> components = new ArrayList<>();
    private final List<TabButton> groupTabs = new ArrayList<>();

    // Layout
    private static final int HEADER_HEIGHT = 80;
    private static final int TABS_HEIGHT = 36;
    private static final int PADDING = 16;
    private static final int COMPONENT_SPACING = 12;

    public SettingsInspectorPanel(int x, int y, int width, int height, ClickGuiScreen screen) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.screen = screen;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        FontRenderer font = screen.getFontRenderer();
        FontRenderer boldFont = screen.getBoldFontRenderer();

        // Background
        RoundedRect.render(context, x, y, width, height, DesignTokens.RADIUS_LARGE, DesignTokens.COLOR_PANEL_BACKGROUND);
        // Left border
        context.fill(x, y, x + 1, y + height, DesignTokens.COLOR_BORDER);

        if (currentModule == null) {
            // Empty state
            String msg = "Select a module to view settings";
            int msgWidth = font.getStringWidth(msg);
            font.draw(context, msg, x + (width - msgWidth) / 2, y + height / 2, DesignTokens.COLOR_MUTED_TEXT);
            return;
        }

        int currentY = y + PADDING;

        // Module header
        renderModuleHeader(context, boldFont, font, currentY, mouseX, mouseY);
        currentY += HEADER_HEIGHT;

        // Group tabs
        if (!groupTabs.isEmpty()) {
            renderGroupTabs(context, font, currentY, mouseX, mouseY);
            currentY += TABS_HEIGHT;
        }

        // Settings content
        int contentY = currentY - scrollOffset;
        renderSettingsContent(context, font, contentY, mouseX, mouseY);

        // Scrollbar
        if (maxScrollOffset > 0) {
            renderScrollbar(context);
        }
    }

    private void renderModuleHeader(DrawContext context, FontRenderer boldFont, FontRenderer font,
                                    int headerY, int mouseX, int mouseY) {
        boolean isEnabled = currentModule.isEnabled();
        boolean isHovered = mouseX >= x && mouseX < x + width && mouseY >= headerY && mouseY < headerY + HEADER_HEIGHT;

        // Background
        int bgColor = isHovered ? DesignTokens.COLOR_HOVER_BACKGROUND : DesignTokens.COLOR_CARD_BACKGROUND;
        RoundedRect.render(context, x, headerY, width, HEADER_HEIGHT, DesignTokens.RADIUS_MEDIUM, bgColor);
        RoundedRect.renderOutline(context, x, headerY, width, HEADER_HEIGHT, DesignTokens.RADIUS_MEDIUM, DesignTokens.BORDER_THIN, DesignTokens.COLOR_BORDER);

        // Enabled toggle (large)
        int toggleSize = 48;
        int toggleX = x + PADDING;
        int toggleY = headerY + (HEADER_HEIGHT - toggleSize) / 2;
        renderToggle(context, toggleX, toggleY, toggleSize, isEnabled, isHovered);

        // Module info
        int infoX = toggleX + toggleSize + 16;
        int nameY = headerY + 12;
        boldFont.draw(context, currentModule.getDisplayName(), infoX, nameY, DesignTokens.COLOR_PRIMARY_TEXT);

        int descY = nameY + boldFont.getFontHeight() + 4;
        font.draw(context, currentModule.getDescription(), infoX, descY, DesignTokens.COLOR_SECONDARY_TEXT);

        int catY = descY + font.getFontHeight() + 2;
        font.draw(context, currentModule.getCategory().getDisplayName(), infoX, catY, DesignTokens.COLOR_MUTED_TEXT);

        // Keybind button
        int keybindBtnWidth = 100;
        int keybindBtnHeight = 28;
        int keybindBtnX = x + width - PADDING - keybindBtnWidth;
        int keybindBtnY = headerY + (HEADER_HEIGHT - keybindBtnHeight) / 2;
        renderKeybindButton(context, font, keybindBtnX, keybindBtnY, keybindBtnWidth, keybindBtnHeight, mouseX, mouseY);
    }

    private void renderToggle(DrawContext context, int x, int y, int size, boolean enabled, boolean hovered) {
        int trackColor = enabled ? DesignTokens.COLOR_ACCENT_GREEN : (hovered ? DesignTokens.COLOR_HOVER_BACKGROUND : DesignTokens.COLOR_BACKGROUND);
        RoundedRect.render(context, x, y, size, size, size / 2, trackColor);

        int thumbSize = size - 8;
        int thumbX = enabled ? x + size - thumbSize - 4 : x + 4;
        int thumbY = y + 4;
        RoundedRect.render(context, thumbX, thumbY, thumbSize, thumbSize, thumbSize / 2, DesignTokens.COLOR_PRIMARY_TEXT);
    }

    private void renderKeybindButton(DrawContext context, FontRenderer font,
                                     int btnX, int btnY, int btnW, int btnH, int mouseX, int mouseY) {
        boolean hovered = mouseX >= btnX && mouseX < btnX + btnW && mouseY >= btnY && mouseY < btnY + btnH;
        int bgColor = hovered ? DesignTokens.COLOR_HOVER_BACKGROUND : DesignTokens.COLOR_BACKGROUND;

        RoundedRect.render(context, btnX, btnY, btnW, btnH, DesignTokens.RADIUS_SMALL, bgColor);
        RoundedRect.renderOutline(context, btnX, btnY, btnW, btnH, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN,
            hovered ? DesignTokens.COLOR_BORDER_HOVER : DesignTokens.COLOR_BORDER);

        String keyText = currentModule.getKeybind() != null && !currentModule.getKeybind().isNone()
            ? currentModule.getKeybind().toShortString() : "Not bound";
        int textColor = hovered ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_SECONDARY_TEXT;
        font.drawCentered(context, keyText, btnX + btnW / 2, btnY + (btnH - font.getFontHeight()) / 2, textColor);
    }

    private void renderGroupTabs(DrawContext context, FontRenderer font, int tabsY, int mouseX, int mouseY) {
        int tabWidth = (width - PADDING * 2) / groupTabs.size();
        int tabX = x + PADDING;

        for (TabButton tab : groupTabs) {
            boolean isSelected = tab.group == selectedGroup;
            boolean isHovered = mouseX >= tabX && mouseX < tabX + tabWidth && mouseY >= tabsY && mouseY < tabsY + TABS_HEIGHT;

            int bgColor = isSelected ? DesignTokens.COLOR_PRIMARY_PURPLE : (isHovered ? DesignTokens.COLOR_HOVER_BACKGROUND : 0x00000000);
            RoundedRect.render(context, tabX, tabsY, tabWidth, TABS_HEIGHT, DesignTokens.RADIUS_SMALL, bgColor);

            int textColor = isSelected ? DesignTokens.COLOR_PRIMARY_TEXT : (isHovered ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_SECONDARY_TEXT);
            font.drawCentered(context, tab.group.getName(), tabX + tabWidth / 2, tabsY + (TABS_HEIGHT - font.getFontHeight()) / 2, textColor);

            tabX += tabWidth;
        }
    }

    private void renderSettingsContent(DrawContext context, FontRenderer font, int contentY, int mouseX, int mouseY) {
        if (selectedGroup == null) return;

        int currentY = contentY;

        for (Setting<?> setting : selectedGroup.getSettings()) {
            Component component = findComponentForSetting(setting);
            if (component != null) {
                component.setPosition(x + PADDING, currentY);
                component.setWidth(width - PADDING * 2);
                component.render(context, font, 0);
                currentY += component.getHeight() + COMPONENT_SPACING;
            }
        }

        maxScrollOffset = Math.max(0, currentY - contentY - (height - HEADER_HEIGHT - TABS_HEIGHT - PADDING * 2));
    }

    private Component findComponentForSetting(Setting<?> setting) {
        return components.stream()
            .filter(c -> c instanceof SettingComponent)
            .map(c -> (SettingComponent) c)
            .filter(c -> c.getSetting() == setting)
            .findFirst()
            .orElse(null);
    }

    private void renderScrollbar(DrawContext context) {
        int scrollbarWidth = 6;
        int scrollbarX = x + width - PADDING - scrollbarWidth;
        int scrollbarY = y + HEADER_HEIGHT + TABS_HEIGHT + PADDING;
        int scrollbarHeight = height - HEADER_HEIGHT - TABS_HEIGHT - PADDING * 2;

        RoundedRect.render(context, scrollbarX, scrollbarY, scrollbarWidth, scrollbarHeight, DesignTokens.RADIUS_SMALL, DesignTokens.COLOR_BACKGROUND);

        float scrollRatio = (float) scrollOffset / maxScrollOffset;
        float visibleRatio = (float) (height - HEADER_HEIGHT - TABS_HEIGHT - PADDING * 2) /
            (height - HEADER_HEIGHT - TABS_HEIGHT - PADDING * 2 + maxScrollOffset);
        int thumbHeight = Math.max(30, (int) (scrollbarHeight * visibleRatio));
        int thumbY = scrollbarY + (int) ((scrollbarHeight - thumbHeight) * scrollRatio);

        RoundedRect.render(context, scrollbarX, thumbY, scrollbarWidth, thumbHeight, DesignTokens.RADIUS_SMALL, DesignTokens.COLOR_PRIMARY_PURPLE);
    }

    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        if (currentModule == null) return false;

        // Header toggle
        int headerY = y + PADDING;
        int toggleSize = 48;
        int toggleX = x + PADDING;
        int toggleY = headerY + (HEADER_HEIGHT - toggleSize) / 2;
        if (mouseX >= toggleX && mouseX < toggleX + toggleSize && mouseY >= toggleY && mouseY < toggleY + toggleSize && button == 0) {
            currentModule.setEnabled(!currentModule.isEnabled());
            return true;
        }

        // Keybind button
        int keybindBtnWidth = 100;
        int keybindBtnHeight = 28;
        int keybindBtnX = x + width - PADDING - keybindBtnWidth;
        int keybindBtnY = headerY + (HEADER_HEIGHT - keybindBtnHeight) / 2;
        if (mouseX >= keybindBtnX && mouseX < keybindBtnX + keybindBtnWidth &&
            mouseY >= keybindBtnY && mouseY < keybindBtnY + keybindBtnHeight && button == 0) {
            // Start keybind listening
            return true;
        }

        // Group tabs
        int tabsY = headerY + HEADER_HEIGHT;
        if (!groupTabs.isEmpty()) {
            int tabWidth = (width - PADDING * 2) / groupTabs.size();
            int tabX = x + PADDING;
            for (TabButton tab : groupTabs) {
                if (mouseX >= tabX && mouseX < tabX + tabWidth && mouseY >= tabsY && mouseY < tabsY + TABS_HEIGHT && button == 0) {
                    selectedGroup = tab.group;
                    rebuildComponents();
                    return true;
                }
                tabX += tabWidth;
            }
        }

        // Settings components
        int contentY = tabsY + TABS_HEIGHT - scrollOffset;
        for (Component component : components) {
            if (component.onMouseClick(mouseX, mouseY, button)) {
                return true;
            }
        }

        return false;
    }

    public boolean onMouseRelease(int mouseX, int mouseY, int button) {
        for (Component component : components) {
            component.onMouseRelease(mouseX, mouseY, button);
        }
        return false;
    }

    public void onMouseScroll(int mouseX, int mouseY, double verticalAmount) {
        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            scrollOffset = (int) Math.max(0, Math.min(maxScrollOffset, scrollOffset - verticalAmount * 40));
        }
    }

    public void setModule(Module module) {
        this.currentModule = module;
        this.scrollOffset = 0;

        if (module != null) {
            // Build group tabs
            groupTabs.clear();
            List<SettingGroup> groups = module.getSettingGroups();
            for (SettingGroup group : groups) {
                groupTabs.add(new TabButton(group));
            }
            if (!groups.isEmpty()) {
                selectedGroup = groups.get(0);
            }
            rebuildComponents();
        } else {
            groupTabs.clear();
            selectedGroup = null;
            components.clear();
        }
    }

    private void rebuildComponents() {
        components.clear();
        if (selectedGroup == null) return;

        int currentY = 0; // Relative to content area
        for (Setting<?> setting : selectedGroup.getSettings()) {
            Component component = createComponentForSetting(setting);
            if (component != null) {
                component.setPosition(x + PADDING, currentY);
                component.setWidth(width - PADDING * 2);
                components.add(component);
                currentY += component.getHeight() + COMPONENT_SPACING;
            }
        }
    }

    private Component createComponentForSetting(Setting<?> setting) {
        return switch (setting.getType()) {
            case BOOLEAN -> new Toggle(0, 0, width - PADDING * 2, 32,
                setting.getName(), (BooleanSetting) setting);
            case INTEGER -> new Slider.IntegerSlider(0, 0, width - PADDING * 2, 32,
                setting.getName(), (IntegerSetting) setting);
            case DOUBLE -> new Slider.DoubleSlider(0, 0, width - PADDING * 2, 32,
                setting.getName(), (DoubleSetting) setting);
            case RANGE -> new Slider.RangeSlider(0, 0, width - PADDING * 2, 32,
                setting.getName(), (RangeSetting) setting);
            case MODE -> new Dropdown(0, 0, width - PADDING * 2, 32,
                setting.getName(), ((ModeSetting) setting).getOptions(),
                ((ModeSetting) setting).getIndex(), ((ModeSetting) setting)::setIndex);
            case COLOR -> new ColorPicker(0, 0, width - PADDING * 2, 32,
                setting.getName(), ((ColorSetting) setting).getColor(),
                ((ColorSetting) setting).hasAlpha(), ((ColorSetting) setting)::setColor);
            case KEYBIND -> new KeybindButton(0, 0, width - PADDING * 2, 32,
                setting.getName(), ((KeybindSetting) setting).getKeybind(),
                ((KeybindSetting) setting)::setKeybind);
            case STRING -> new TextField(0, 0, width - PADDING * 2, 32,
                setting.getName(), "", 256, false, ((StringSetting) setting)::setValue);
            default -> null;
        };
    }

    private static class TabButton {
        final SettingGroup group;
        TabButton(SettingGroup group) { this.group = group; }
    }

    // Interface for setting components
    private interface SettingComponent {
        Setting<?> getSetting();
    }
}