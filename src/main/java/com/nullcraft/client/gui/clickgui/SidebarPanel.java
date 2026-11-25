package com.nullcraft.client.gui.clickgui;

import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;

import java.util.Arrays;
import java.util.List;

/**
 * Sidebar panel: Category buttons (7 gameplay + 4 utility) with outline icons
 */
public class SidebarPanel {

    private final ClickGuiScreen screen;
    private final int x, y, width, height;

    private ModuleCategory selectedCategory = ModuleCategory.COMBAT;
    private ModuleCategory hoveredCategory = null;

    // Category order: 7 gameplay + 4 utility
    private static final List<ModuleCategory> GAMEPLAY_CATEGORIES = Arrays.asList(
        ModuleCategory.COMBAT,
        ModuleCategory.MOVEMENT,
        ModuleCategory.PLAYER,
        ModuleCategory.RENDER,
        ModuleCategory.WORLD,
        ModuleCategory.DIAGNOSTICS,
        ModuleCategory.MISC
    );

    private static final List<ModuleCategory> UTILITY_CATEGORIES = Arrays.asList(
        ModuleCategory.HUD,
        ModuleCategory.CONFIG,
        ModuleCategory.KEYBINDS,
        ModuleCategory.ABOUT
    );

    private int categoryHeight = 40;
    private int iconSize = 20;

    public SidebarPanel(int x, int y, int width, int height, ClickGuiScreen screen) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.screen = screen;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        FontRenderer font = screen.getFontRenderer();

        // Background
        RoundedRect.render(context, x, y, width, height, DesignTokens.RADIUS_LARGE, DesignTokens.COLOR_SIDEBAR_BACKGROUND);
        // Right border
        context.fill(x + width - 1, y, x + width, y + height, DesignTokens.COLOR_BORDER);

        int currentY = y + 16;
        int padding = 16;

        // Gameplay categories section
        FontRenderer boldFont = screen.getBoldFontRenderer();
        boldFont.draw(context, "GAMEPLAY", x + padding, currentY, DesignTokens.COLOR_MUTED_TEXT);
        currentY += 24;

        for (ModuleCategory category : GAMEPLAY_CATEGORIES) {
            renderCategoryButton(context, font, category, currentY, mouseX, mouseY);
            currentY += categoryHeight + 4;
        }

        currentY += 16;

        // Utility categories section
        boldFont.draw(context, "UTILITY", x + padding, currentY, DesignTokens.COLOR_MUTED_TEXT);
        currentY += 24;

        for (ModuleCategory category : UTILITY_CATEGORIES) {
            renderCategoryButton(context, font, category, currentY, mouseX, mouseY);
            currentY += categoryHeight + 4;
        }
    }

    private void renderCategoryButton(DrawContext context, FontRenderer font,
                                      ModuleCategory category, int btnY, int mouseX, int mouseY) {
        boolean isSelected = category == selectedCategory;
        boolean isHovered = isMouseOver(mouseX, mouseY, x + 8, btnY, width - 16, categoryHeight);

        // Background
        int bgColor;
        if (isSelected) {
            bgColor = DesignTokens.COLOR_PRIMARY_PURPLE;
        } else if (isHovered) {
            bgColor = DesignTokens.COLOR_HOVER_BACKGROUND;
        } else {
            bgColor = 0x00000000; // Transparent
        }
        RoundedRect.render(context, x + 8, btnY, width - 16, categoryHeight, DesignTokens.RADIUS_SMALL, bgColor);

        // Icon
        int iconX = x + 16;
        int iconY = btnY + (categoryHeight - iconSize) / 2;
        renderCategoryIcon(context, category, iconX, iconY, isSelected);

        // Label
        int labelX = iconX + iconSize + 12;
        int labelY = btnY + (categoryHeight - font.getFontHeight()) / 2;
        int labelColor = isSelected ? DesignTokens.COLOR_PRIMARY_TEXT : (isHovered ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_SECONDARY_TEXT);
        font.draw(context, category.getDisplayName(), labelX, labelY, labelColor);

        // Module count badge
        int count = screen.getSelectedCategory() == category ?
            screen.getSelectedCategory().equals(category) ? ModuleRegistry.getInstance().getModulesByCategory(category).size() :
            ModuleRegistry.getInstance().getModulesByCategory(category).size() :
            ModuleRegistry.getInstance().getModulesByCategory(category).size();

        String countStr = String.valueOf(count);
        int countWidth = font.getStringWidth(countStr) + 10;
        int countX = x + width - 16 - countWidth;
        int countY = btnY + (categoryHeight - 18) / 2;
        if (isSelected) {
            RoundedRect.render(context, countX, countY, countWidth, 18, DesignTokens.RADIUS_SMALL, DesignTokens.COLOR_PRIMARY_TEXT);
            font.drawCentered(context, countStr, countX + countWidth / 2, countY + (18 - font.getFontHeight()) / 2, 0xFF1A1A2E);
        } else {
            RoundedRect.render(context, countX, countY, countWidth, 18, DesignTokens.RADIUS_SMALL, DesignTokens.COLOR_BACKGROUND);
            font.drawCentered(context, countStr, countX + countWidth / 2, countY + (18 - font.getFontHeight()) / 2, DesignTokens.COLOR_MUTED_TEXT);
        }
    }

    private void renderCategoryIcon(DrawContext context, ModuleCategory category, int x, int y, boolean selected) {
        // Outline icons for each category (drawn as text placeholders)
        // In production, these would be actual icon textures
        FontRenderer font = screen.getFontRenderer();
        String icon = getCategoryIcon(category);
        int color = selected ? 0xFFFFFFFF : DesignTokens.COLOR_SECONDARY_TEXT;
        font.draw(context, icon, x, y, color);
    }

    private String getCategoryIcon(ModuleCategory category) {
        // Unicode placeholder icons - would be replaced with custom textures
        return switch (category) {
            case COMBAT -> "⚔";      // Crossed swords
            case MOVEMENT -> "➤";    // Arrow right
            case PLAYER -> "⬤";      // Person
            case RENDER -> "◈";      // Diamond
            case WORLD -> "⯁";       // Globe
            case DIAGNOSTICS -> "⚙"; // Gear
            case MISC -> "⋯";        // Ellipsis
            case HUD -> "☐";         // Square
            case CONFIG -> "☰";      // Menu
            case KEYBINDS -> "⌨";    // Keyboard
            case ABOUT -> "ⓘ";       // Info
        };
    }

    private boolean isMouseOver(int mx, int my, int bx, int by, int bw, int bh) {
        return mx >= bx && mx < bx + bw && my >= by && my < by + bh;
    }

    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        if (button != 0) return false;

        int currentY = y + 16 + 24; // After GAMEPLAY label
        int padding = 16;

        for (ModuleCategory category : GAMEPLAY_CATEGORIES) {
            if (isMouseOver(mouseX, mouseY, x + 8, currentY, width - 16, categoryHeight)) {
                screen.setSelectedCategory(category);
                return true;
            }
            currentY += categoryHeight + 4;
        }

        currentY += 16 + 24; // After UTILITY label

        for (ModuleCategory category : UTILITY_CATEGORIES) {
            if (isMouseOver(mouseX, mouseY, x + 8, currentY, width - 16, categoryHeight)) {
                screen.setSelectedCategory(category);
                return true;
            }
            currentY += categoryHeight + 4;
        }

        return false;
    }

    public boolean onMouseRelease(int mouseX, int mouseY, int button) {
        return false;
    }

    public void setSelectedCategory(ModuleCategory category) {
        this.selectedCategory = category;
    }
}