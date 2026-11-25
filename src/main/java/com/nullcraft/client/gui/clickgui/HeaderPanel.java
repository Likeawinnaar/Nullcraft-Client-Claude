package com.nullcraft.client.gui.clickgui;

import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;

import java.util.function.Consumer;

/**
 * Header panel: Logo, title, search, favorites, pin, settings, minimize, close
 */
public class HeaderPanel {

    private final ClickGuiScreen screen;
    private final int x, y, width, height;

    // Search field
    private String searchText = "";
    private boolean searchFocused = false;
    private long cursorBlinkTime = System.currentTimeMillis();
    private boolean cursorVisible = true;

    // Button states
    private boolean favoritesHovered = false;
    private boolean pinHovered = false;
    private boolean settingsHovered = false;
    private boolean minimizeHovered = false;
    private boolean closeHovered = false;

    // Button bounds
    private int searchX, searchY, searchWidth, searchHeight;
    private int favoritesX, favoritesY, btnSize;
    private int pinX, pinY;
    private int settingsX, settingsY;
    private int minimizeX, minimizeY;
    private int closeX, closeY;

    public HeaderPanel(int x, int y, int width, int height, ClickGuiScreen screen) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.screen = screen;
        layoutButtons();
    }

    private void layoutButtons() {
        int padding = 16;
        int btnSize = 28;
        int spacing = 8;

        // Search field (left side, after logo)
        int logoWidth = 120; // Logo + title area
        searchX = x + logoWidth + padding;
        searchY = y + (height - 32) / 2;
        searchWidth = 280;
        searchHeight = 32;

        // Right side buttons
        int rightX = x + width - padding - btnSize;
        closeX = rightX;
        closeY = y + (height - btnSize) / 2;

        minimizeX = closeX - spacing - btnSize;
        minimizeY = closeY;

        settingsX = minimizeX - spacing - btnSize;
        settingsY = closeY;

        pinX = settingsX - spacing - btnSize;
        pinY = closeY;

        favoritesX = pinX - spacing - btnSize;
        favoritesY = closeY;

        this.btnSize = btnSize;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        updateAnimations(mouseX, mouseY);

        FontRenderer font = screen.getFontRenderer();
        FontRenderer boldFont = screen.getBoldFontRenderer();

        // Background
        RoundedRect.render(context, x, y, width, height, DesignTokens.RADIUS_LARGE, DesignTokens.COLOR_PANEL_BACKGROUND);
        // Bottom border only (separates from content)
        context.fill(x, y + height - 1, x + width, y + height, DesignTokens.COLOR_BORDER);

        // Logo area
        renderLogo(context, boldFont);

        // Search field
        renderSearchField(context, font, mouseX, mouseY);

        // Action buttons
        renderIconButton(context, favoritesX, favoritesY, btnSize, btnSize,
            "", favoritesHovered, "Favorites (F)"); // Heart/star icon placeholder
        renderIconButton(context, pinX, pinY, btnSize, btnSize,
            "", pinHovered, screen.isPinned() ? "Unpin" : "Pin (P)"); // Pin icon
        renderIconButton(context, settingsX, settingsY, btnSize, btnSize,
            "", settingsHovered, "Settings (,)"); // Gear icon
        renderIconButton(context, minimizeX, minimizeY, btnSize, btnSize,
            "", minimizeHovered, "Minimize (_)"); // Minimize icon
        renderIconButton(context, closeX, closeY, btnSize, btnSize,
            "", closeHovered, "Close (Esc)"); // X icon
    }

    private void renderLogo(DrawContext context, FontRenderer font) {
        int logoX = x + 16;
        int logoY = y + (height - 28) / 2;

        // Geometric "N" logo (purple gradient square)
        int logoSize = 28;
        // Simplified: purple square with N
        context.fill(logoX, logoY, logoX + logoSize, logoY + logoSize, DesignTokens.COLOR_PRIMARY_PURPLE);
        font.drawCentered(context, "N", logoX + logoSize / 2, logoY + (logoSize - font.getFontHeight()) / 2, 0xFFFFFFFF);

        // Title
        int titleX = logoX + logoSize + 12;
        int titleY = y + (height - font.getFontHeight()) / 2;
        font.draw(context, "NULLCRAFT", titleX, titleY, DesignTokens.COLOR_PRIMARY_TEXT);

        // Version
        String version = "v1.0.0";
        int versionX = titleX + font.getStringWidth("NULLCRAFT") + 8;
        font.draw(context, version, versionX, titleY, DesignTokens.COLOR_MUTED_TEXT);
    }

    private void renderSearchField(DrawContext context, FontRenderer font, int mouseX, int mouseY) {
        boolean hovered = mouseX >= searchX && mouseX < searchX + searchWidth &&
                          mouseY >= searchY && mouseY < searchY + searchHeight;

        int bgColor = searchFocused ? DesignTokens.COLOR_HOVER_BACKGROUND : (hovered ? DesignTokens.COLOR_HOVER_BACKGROUND : DesignTokens.COLOR_BACKGROUND);
        RoundedRect.render(context, searchX, searchY, searchWidth, searchHeight, DesignTokens.RADIUS_SMALL, bgColor);
        RoundedRect.renderOutline(context, searchX, searchY, searchWidth, searchHeight, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN,
            searchFocused ? DesignTokens.COLOR_PRIMARY_PURPLE : (hovered ? DesignTokens.COLOR_BORDER_HOVER : DesignTokens.COLOR_BORDER));

        // Search icon
        int iconX = searchX + 10;
        int iconY = searchY + (searchHeight - font.getFontHeight()) / 2;
        font.draw(context, "", iconX, iconY, DesignTokens.COLOR_MUTED_TEXT); // Magnifying glass

        // Text
        int textX = searchX + 32;
        int textY = searchY + (searchHeight - font.getFontHeight()) / 2;
        String displayText = searchText.isEmpty() && !searchFocused ? "Search modules..." : searchText;
        int textColor = (searchText.isEmpty() && !searchFocused) ? DesignTokens.COLOR_MUTED_TEXT : DesignTokens.COLOR_PRIMARY_TEXT;
        font.draw(context, displayText, textX, textY, textColor);

        // Cursor
        if (searchFocused && cursorVisible) {
            int cursorX = textX + font.getStringWidth(searchText);
            context.fill(cursorX, textY, cursorX + 1, textY + font.getFontHeight(), DesignTokens.COLOR_PRIMARY_PURPLE);
        }
    }

    private void renderIconButton(DrawContext context, int bx, int by, int bw, int bh,
                                  String icon, boolean hovered, String tooltip) {
        int bgColor = hovered ? DesignTokens.COLOR_HOVER_BACKGROUND : 0x00000000;
        RoundedRect.render(context, bx, by, bw, bh, DesignTokens.RADIUS_SMALL, bgColor);

        FontRenderer font = screen.getFontRenderer();
        int iconX = bx + (bw - font.getStringWidth(icon)) / 2;
        int iconY = by + (bh - font.getFontHeight()) / 2;
        int iconColor = hovered ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_SECONDARY_TEXT;
        font.draw(context, icon, iconX, iconY, iconColor);
    }

    private void updateAnimations(int mouseX, int mouseY) {
        favoritesHovered = isInButton(mouseX, mouseY, favoritesX, favoritesY);
        pinHovered = isInButton(mouseX, mouseY, pinX, pinY);
        settingsHovered = isInButton(mouseX, mouseY, settingsX, settingsY);
        minimizeHovered = isInButton(mouseX, mouseY, minimizeX, minimizeY);
        closeHovered = isInButton(mouseX, mouseY, closeX, closeY);

        // Cursor blink
        if (System.currentTimeMillis() - cursorBlinkTime > 500) {
            cursorVisible = !cursorVisible;
            cursorBlinkTime = System.currentTimeMillis();
        }
    }

    private boolean isInButton(int mx, int my, int bx, int by) {
        return mx >= bx && mx < bx + btnSize && my >= by && my < by + btnSize;
    }

    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        if (button != 0) return false;

        // Search field
        if (mouseX >= searchX && mouseX < searchX + searchWidth &&
            mouseY >= searchY && mouseY < searchY + searchHeight) {
            searchFocused = true;
            return true;
        }

        // Buttons
        if (isInButton(mouseX, mouseY, favoritesX, favoritesY)) {
            screen.setShowFavoritesOnly(!screen.isShowFavoritesOnly());
            return true;
        }
        if (isInButton(mouseX, mouseY, pinX, pinY)) {
            screen.setPinned(!screen.isPinned());
            return true;
        }
        if (isInButton(mouseX, mouseY, settingsX, settingsY)) {
            // Open settings screen
            return true;
        }
        if (isInButton(mouseX, mouseY, minimizeX, minimizeY)) {
            // Minimize
            return true;
        }
        if (isInButton(mouseX, mouseY, closeX, closeY)) {
            screen.close();
            return true;
        }

        // Click outside search field
        if (searchFocused) {
            searchFocused = false;
        }
        return false;
    }

    public boolean onMouseRelease(int mouseX, int mouseY, int button) {
        return false;
    }

    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        if (!searchFocused) return false;

        switch (keyCode) {
            case 259 -> { // Backspace
                if (!searchText.isEmpty()) {
                    searchText = searchText.substring(0, searchText.length() - 1);
                    screen.setSearchQuery(searchText);
                }
                return true;
            }
            case 256 -> { // Escape
                searchFocused = false;
                searchText = "";
                screen.setSearchQuery("");
                return true;
            }
        }
        return false;
    }

    public boolean onCharTyped(char chr, int modifiers) {
        if (!searchFocused) return false;
        if (chr >= 32 && searchText.length() < 50) {
            searchText += chr;
            screen.setSearchQuery(searchText);
            return true;
        }
        return false;
    }

    public void clearSearch() {
        searchText = "";
        searchFocused = false;
        screen.setSearchQuery("");
    }

    public boolean isSearchFocused() {
        return searchFocused;
    }
}