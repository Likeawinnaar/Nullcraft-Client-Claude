package com.nullcraft.client.gui.clickgui;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.module.ModuleRegistry;
import com.nullcraft.client.core.settings.Setting;
import com.nullcraft.client.gui.font.FontManager;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Main ClickGUI screen with 4-panel layout:
 * - Header: Logo, title, search, favorites, pin, settings, minimize, close
 * - Sidebar: Category buttons (7 gameplay + 4 utility)
 * - Module Browser: 2-column grid of ModuleCards
 * - Settings Inspector: Module header + tabbed settings groups
 */
public class ClickGuiScreen extends Screen {

    // Layout constants
    private static final int PANEL_WIDTH = 900;
    private static final int PANEL_HEIGHT = 600;
    private static final int HEADER_HEIGHT = 56;
    private static final int SIDEBAR_WIDTH = 220;
    private static final int BROWSER_WIDTH = 380;
    private static final int INSPECTOR_WIDTH = PANEL_WIDTH - SIDEBAR_WIDTH - BROWSER_WIDTH;

    // Animation
    private float openProgress = 0f;
    private float targetOpenProgress = 1f;

    // Components
    private HeaderPanel headerPanel;
    private SidebarPanel sidebarPanel;
    private ModuleBrowserPanel browserPanel;
    private SettingsInspectorPanel inspectorPanel;

    // State
    private ModuleCategory selectedCategory = ModuleCategory.COMBAT;
    private Module selectedModule = null;
    private String searchQuery = "";
    private boolean showFavoritesOnly = false;
    private boolean pinned = false;

    public ClickGuiScreen() {
        super(Text.literal("NullCraft ClickGUI"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = (width - PANEL_WIDTH) / 2;
        int centerY = (height - PANEL_HEIGHT) / 2;

        // Initialize panels
        headerPanel = new HeaderPanel(centerX, centerY, PANEL_WIDTH, HEADER_HEIGHT, this);
        sidebarPanel = new SidebarPanel(centerX, centerY + HEADER_HEIGHT, SIDEBAR_WIDTH, PANEL_HEIGHT - HEADER_HEIGHT, this);
        browserPanel = new ModuleBrowserPanel(
            centerX + SIDEBAR_WIDTH, centerY + HEADER_HEIGHT,
            BROWSER_WIDTH, PANEL_HEIGHT - HEADER_HEIGHT, this
        );
        inspectorPanel = new SettingsInspectorPanel(
            centerX + SIDEBAR_WIDTH + BROWSER_WIDTH, centerY + HEADER_HEIGHT,
            INSPECTOR_WIDTH, PANEL_HEIGHT - HEADER_HEIGHT, this
        );

        // Set initial category
        sidebarPanel.setSelectedCategory(selectedCategory);
        browserPanel.setCategory(selectedCategory);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Update animations
        openProgress = net.minecraft.util.math.MathHelper.lerp(0.15f, openProgress, targetOpenProgress);

        if (openProgress < 0.01f) {
            close();
            return;
        }

        // Render background overlay
        renderBackgroundOverlay(context);

        // Render main panel with shadow
        renderPanelShadow(context);

        // Render panels
        headerPanel.render(context, mouseX, mouseY, delta);
        sidebarPanel.render(context, mouseX, mouseY, delta);
        browserPanel.render(context, mouseX, mouseY, delta);
        inspectorPanel.render(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderBackgroundOverlay(DrawContext context) {
        int alpha = (int) (0xCC * openProgress); // 80% opacity
        int color = (alpha << 24) | 0x0D0D12; // Dark background
        context.fill(0, 0, width, height, color);
    }

    private void renderPanelShadow(DrawContext context) {
        int centerX = (width - PANEL_WIDTH) / 2;
        int centerY = (height - PANEL_HEIGHT) / 2;

        // Outer glow
        for (int i = 0; i < 20; i++) {
            float progress = i / 20f;
            int alpha = (int) (DesignTokens.SHADOW_GLOW_ALPHA * (1f - progress) * openProgress);
            int color = (alpha << 24) | DesignTokens.COLOR_PRIMARY_PURPLE;
            RoundedRect.render(context,
                centerX - i, centerY - i,
                PANEL_WIDTH + i * 2, PANEL_HEIGHT + i * 2,
                DesignTokens.RADIUS_LARGE + i, color);
        }

        // Drop shadow
        for (int i = 0; i < 10; i++) {
            float progress = i / 10f;
            int alpha = (int) (DesignTokens.SHADOW_DROP_ALPHA * (1f - progress) * openProgress);
            int color = (alpha << 24) | 0x000000;
            RoundedRect.render(context,
                centerX + 4 + i, centerY + 4 + i,
                PANEL_WIDTH - i * 2, PANEL_HEIGHT - i * 2,
                DesignTokens.RADIUS_LARGE + i, color);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        headerPanel.onMouseClick((int) mouseX, (int) mouseY, button);
        sidebarPanel.onMouseClick((int) mouseX, (int) mouseY, button);
        browserPanel.onMouseClick((int) mouseX, (int) mouseY, button);
        inspectorPanel.onMouseClick((int) mouseX, (int) mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        headerPanel.onMouseRelease((int) mouseX, (int) mouseY, button);
        sidebarPanel.onMouseRelease((int) mouseX, (int) mouseY, button);
        browserPanel.onMouseRelease((int) mouseX, (int) mouseY, button);
        inspectorPanel.onMouseRelease((int) mouseX, (int) mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        browserPanel.onMouseScroll((int) mouseX, (int) mouseY, verticalAmount);
        inspectorPanel.onMouseScroll((int) mouseX, (int) mouseY, verticalAmount);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC
            if (headerPanel.isSearchFocused()) {
                headerPanel.clearSearch();
                return true;
            }
            close();
            return true;
        }
        if (keyCode == 340 || keyCode == 344) { // Super/Command key - ignore
            return true;
        }
        // Handle search input
        if (headerPanel.isSearchFocused()) {
            return headerPanel.onKeyPress(keyCode, scanCode, modifiers);
        }
        // Module keybinds
        if (selectedModule != null) {
            // Module-specific key handling
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (headerPanel.isSearchFocused()) {
            return headerPanel.onCharTyped(chr, modifiers);
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public void close() {
        targetOpenProgress = 0f;
        // Actually close when animation completes
        if (openProgress < 0.01f) {
            super.close();
            // Save GUI state (position, pinned, etc.)
        }
    }

    // ============================================================
    // GETTERS / SETTERS
    // ============================================================

    public ModuleCategory getSelectedCategory() { return selectedCategory; }
    public void setSelectedCategory(ModuleCategory category) {
        this.selectedCategory = category;
        sidebarPanel.setSelectedCategory(category);
        browserPanel.setCategory(category);
    }

    public Module getSelectedModule() { return selectedModule; }
    public void setSelectedModule(Module module) {
        this.selectedModule = module;
        inspectorPanel.setModule(module);
    }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String query) {
        this.searchQuery = query;
        browserPanel.setSearchQuery(query);
    }

    public boolean isShowFavoritesOnly() { return showFavoritesOnly; }
    public void setShowFavoritesOnly(boolean show) {
        this.showFavoritesOnly = show;
        browserPanel.setShowFavoritesOnly(show);
    }

    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }

    public FontRenderer getFontRenderer() {
        return FontManager.getInstance().getRegular();
    }

    public FontRenderer getBoldFontRenderer() {
        return FontManager.getInstance().getBold();
    }
}