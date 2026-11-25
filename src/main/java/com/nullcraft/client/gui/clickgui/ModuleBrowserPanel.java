package com.nullcraft.client.gui.clickgui;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.module.ModuleRegistry;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Module browser panel: 2-column grid of ModuleCards with dynamic loading
 */
public class ModuleBrowserPanel {

    private final ClickGuiScreen screen;
    private final int x, y, width, height;

    private ModuleCategory currentCategory = ModuleCategory.COMBAT;
    private String searchQuery = "";
    private boolean showFavoritesOnly = false;

    private List<Module> filteredModules = new ArrayList<>();
    private int scrollOffset = 0;
    private int maxScrollOffset = 0;

    // Layout
    private static final int CARD_WIDTH = 170;
    private static final int CARD_HEIGHT = 64;
    private static final int CARD_SPACING = 12;
    private static final int PADDING = 16;
    private static final int COLUMNS = 2;

    public ModuleBrowserPanel(int x, int y, int width, int height, ClickGuiScreen screen) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.screen = screen;
        updateFilteredModules();
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        FontRenderer font = screen.getFontRenderer();
        FontRenderer boldFont = screen.getBoldFontRenderer();

        // Background
        RoundedRect.render(context, x, y, width, height, DesignTokens.RADIUS_LARGE, DesignTokens.COLOR_PANEL_BACKGROUND);

        // Clip to panel bounds for scrolling
        // Note: In practice, would use scissor test
        int contentX = x + PADDING;
        int contentY = y + PADDING - scrollOffset;
        int contentWidth = width - PADDING * 2;
        int contentHeight = height - PADDING * 2;

        // Category header
        boldFont.draw(context, currentCategory.getDisplayName(), contentX, y + PADDING - 20, DesignTokens.COLOR_PRIMARY_TEXT);

        // Render module cards
        int cardIndex = 0;
        for (Module module : filteredModules) {
            int col = cardIndex % COLUMNS;
            int row = cardIndex / COLUMNS;

            int cardX = contentX + col * (CARD_WIDTH + CARD_SPACING);
            int cardY = contentY + row * (CARD_HEIGHT + CARD_SPACING);

            // Only render if visible
            if (cardY + CARD_HEIGHT >= y + PADDING && cardY <= y + height - PADDING) {
                renderModuleCard(context, font, module, cardX, cardY, mouseX, mouseY);
            }
            cardIndex++;
        }

        // Scroll indicator
        if (maxScrollOffset > 0) {
            renderScrollbar(context);
        }

        // Empty state
        if (filteredModules.isEmpty()) {
            String msg = searchQuery.isEmpty() ? "No modules in this category" : "No matches for \"" + searchQuery + "\"";
            int msgWidth = font.getStringWidth(msg);
            font.draw(context, msg, x + (width - msgWidth) / 2, y + height / 2, DesignTokens.COLOR_MUTED_TEXT);
        }
    }

    private void renderModuleCard(DrawContext context, FontRenderer font, Module module,
                                  int cardX, int cardY, int mouseX, int mouseY) {
        boolean isSelected = screen.getSelectedModule() == module;
        boolean isHovered = mouseX >= cardX && mouseX < cardX + CARD_WIDTH &&
                           mouseY >= cardY && mouseY < cardY + CARD_HEIGHT;
        boolean isEnabled = module.isEnabled();
        boolean isFavorite = module.isFavorite();

        // Card background
        int bgColor;
        if (isSelected) {
            bgColor = DesignTokens.COLOR_PRIMARY_PURPLE;
        } else if (isHovered) {
            bgColor = DesignTokens.COLOR_HOVER_BACKGROUND;
        } else {
            bgColor = DesignTokens.COLOR_CARD_BACKGROUND;
        }
        RoundedRect.render(context, cardX, cardY, CARD_WIDTH, CARD_HEIGHT, DesignTokens.RADIUS_MEDIUM, bgColor);

        // Border
        int borderColor = isSelected ? DesignTokens.COLOR_PRIMARY_TEXT :
                         isHovered ? DesignTokens.COLOR_BORDER_HOVER : DesignTokens.COLOR_BORDER;
        RoundedRect.renderOutline(context, cardX, cardY, CARD_WIDTH, CARD_HEIGHT, DesignTokens.RADIUS_MEDIUM, DesignTokens.BORDER_THIN, borderColor);

        // Enabled indicator (left accent bar)
        if (isEnabled) {
            context.fill(cardX, cardY, cardX + 3, cardY + CARD_HEIGHT, DesignTokens.COLOR_ACCENT_GREEN);
        }

        // Favorite star
        if (isFavorite) {
            font.draw(context, "★", cardX + CARD_WIDTH - 20, cardY + 6, DesignTokens.COLOR_WARNING);
        }

        // Module icon (placeholder)
        int iconX = cardX + 12;
        int iconY = cardY + (CARD_HEIGHT - 24) / 2;
        font.draw(context, "◇", iconX, iconY, isSelected ? 0xFFFFFFFF : DesignTokens.COLOR_SECONDARY_TEXT);

        // Module name
        int nameX = iconX + 28;
        int nameY = cardY + 10;
        int nameColor = isSelected ? 0xFFFFFFFF : (isEnabled ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_MUTED_TEXT);
        String name = module.getDisplayName();
        // Truncate if too long
        int maxNameWidth = CARD_WIDTH - 50;
        while (font.getStringWidth(name) > maxNameWidth && name.length() > 3) {
            name = name.substring(0, name.length() - 1);
        }
        if (name.length() < module.getDisplayName().length()) name += "…";
        font.draw(context, name, nameX, nameY, nameColor);

        // Description
        int descY = nameY + font.getFontHeight() + 2;
        String desc = module.getDescription();
        if (desc.length() > 35) desc = desc.substring(0, 32) + "…";
        font.draw(context, desc, nameX, descY, isSelected ? 0xCCFFFFFF : DesignTokens.COLOR_MUTED_TEXT);

        // Keybind hint
        if (module.getKeybind() != null && !module.getKeybind().isNone()) {
            String keybindStr = module.getKeybind().toShortString();
            int keybindX = cardX + CARD_WIDTH - 10 - font.getStringWidth(keybindStr);
            int keybindY = cardY + CARD_HEIGHT - font.getFontHeight() - 6;
            font.draw(context, keybindStr, keybindX, keybindY, isSelected ? 0x88FFFFFF : DesignTokens.COLOR_MUTED_TEXT);
        }
    }

    private void renderScrollbar(DrawContext context) {
        int scrollbarWidth = 6;
        int scrollbarX = x + width - PADDING - scrollbarWidth;
        int scrollbarY = y + PADDING;
        int scrollbarHeight = height - PADDING * 2;

        // Track
        RoundedRect.render(context, scrollbarX, scrollbarY, scrollbarWidth, scrollbarHeight, DesignTokens.RADIUS_SMALL, DesignTokens.COLOR_BACKGROUND);

        // Thumb
        float scrollRatio = (float) scrollOffset / maxScrollOffset;
        float visibleRatio = (float) contentHeight / (filteredModules.size() / COLUMNS * (CARD_HEIGHT + CARD_SPACING));
        int thumbHeight = Math.max(30, (int) (scrollbarHeight * visibleRatio));
        int thumbY = scrollbarY + (int) ((scrollbarHeight - thumbHeight) * scrollRatio);

        RoundedRect.render(context, scrollbarX, thumbY, scrollbarWidth, thumbHeight, DesignTokens.RADIUS_SMALL, DesignTokens.COLOR_PRIMARY_PURPLE);
    }

    private int contentHeight;

    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        int contentX = x + PADDING;
        int contentY = y + PADDING - scrollOffset;

        int cardIndex = 0;
        for (Module module : filteredModules) {
            int col = cardIndex % COLUMNS;
            int row = cardIndex / COLUMNS;

            int cardX = contentX + col * (CARD_WIDTH + CARD_SPACING);
            int cardY = contentY + row * (CARD_HEIGHT + CARD_SPACING);

            if (mouseX >= cardX && mouseX < cardX + CARD_WIDTH &&
                mouseY >= cardY && mouseY < cardY + CARD_HEIGHT) {
                if (button == 0) { // Left click - select
                    screen.setSelectedModule(module);
                } else if (button == 1) { // Right click - toggle
                    module.setEnabled(!module.isEnabled());
                } else if (button == 2) { // Middle click - favorite
                    module.setFavorite(!module.isFavorite());
                }
                return true;
            }
            cardIndex++;
        }

        return false;
    }

    public boolean onMouseRelease(int mouseX, int mouseY, int button) {
        return false;
    }

    public void onMouseScroll(int mouseX, int mouseY, double verticalAmount) {
        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            scrollOffset = (int) Math.max(0, Math.min(maxScrollOffset, scrollOffset - verticalAmount * 40));
        }
    }

    public void setCategory(ModuleCategory category) {
        this.currentCategory = category;
        this.scrollOffset = 0;
        updateFilteredModules();
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query.toLowerCase();
        this.scrollOffset = 0;
        updateFilteredModules();
    }

    public void setShowFavoritesOnly(boolean show) {
        this.showFavoritesOnly = show;
        this.scrollOffset = 0;
        updateFilteredModules();
    }

    private void updateFilteredModules() {
        List<Module> modules = ModuleRegistry.getInstance().getModulesByCategory(currentCategory);

        filteredModules = modules.stream()
            .filter(m -> searchQuery.isEmpty() ||
                        m.getName().toLowerCase().contains(searchQuery) ||
                        m.getDisplayName().toLowerCase().contains(searchQuery) ||
                        m.getDescription().toLowerCase().contains(searchQuery))
            .filter(m -> !showFavoritesOnly || m.isFavorite())
            .sorted(Comparator.comparing((Module m) -> !m.isEnabled()) // Enabled first
                .thenComparing(Module::isFavorite).reversed() // Favorites first
                .thenComparing(Module::getDisplayName))
            .collect(Collectors.toList());

        // Calculate max scroll
        int rows = (int) Math.ceil((double) filteredModules.size() / COLUMNS);
        contentHeight = rows * (CARD_HEIGHT + CARD_SPACING);
        int visibleHeight = height - PADDING * 2;
        maxScrollOffset = Math.max(0, contentHeight - visibleHeight);
    }
}