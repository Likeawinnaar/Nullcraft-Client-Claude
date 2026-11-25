package com.nullcraft.client.gui.hudeditor;

import com.nullcraft.client.gui.hud.HudElement;
import com.nullcraft.client.gui.hud.HudManager;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * HUD Editor Screen: Drag-drop, resize, grid snap, alignment guides, lock, visibility, scale, opacity, background/border/glow/shadow customization, font/color settings, presets, import/export
 */
public class HudEditorScreen extends Screen {

    private static final int PANEL_WIDTH = 320;
    private static final int PANEL_HEIGHT = 600;

    private float openProgress = 0f;
    private float targetOpenProgress = 1f;

    // Sidebar
    private HudElement selectedElement = null;
    private int scrollOffset = 0;
    private int maxScrollOffset = 0;

    // Editor state
    private boolean showGrid = true;
    private double gridSize = 10.0;
    private boolean showAlignmentGuides = true;

    public HudEditorScreen() {
        super(Text.literal("HUD Editor"));
    }

    @Override
    protected void init() {
        super.init();
        HudManager.getInstance().setEditorMode(true);
        HudManager.getInstance().setGridSize(gridSize);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render in-game HUD first (behind editor)
        // Note: In practice, would render the actual HUD here

        // Update animations
        openProgress = net.minecraft.util.math.MathHelper.lerp(0.15f, openProgress, targetOpenProgress);

        if (openProgress < 0.01f) {
            close();
            return;
        }

        // Render background overlay
        renderBackgroundOverlay(context);

        // Render main panel
        renderPanel(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderBackgroundOverlay(DrawContext context) {
        int alpha = (int) (0xCC * openProgress);
        int color = (alpha << 24) | 0x0D0D12;
        context.fill(0, 0, width, height, color);
    }

    private void renderPanel(DrawContext context, int mouseX, int mouseY, float delta) {
        int panelX = width - PANEL_WIDTH - 20;
        int panelY = (height - PANEL_HEIGHT) / 2;
        FontRenderer font = getFontRenderer();

        // Panel background with shadow
        for (int i = 0; i < 15; i++) {
            float progress = i / 15f;
            int alpha = (int) (DesignTokens.SHADOW_GLOW_ALPHA * (1f - progress) * openProgress);
            int color = (alpha << 24) | DesignTokens.COLOR_PRIMARY_PURPLE;
            RoundedRect.render(context,
                panelX - i, panelY - i,
                PANEL_WIDTH + i * 2, PANEL_HEIGHT + i * 2,
                DesignTokens.RADIUS_LARGE + i, color);
        }

        RoundedRect.render(context, panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT,
            DesignTokens.RADIUS_LARGE, DesignTokens.COLOR_PANEL_BACKGROUND);
        RoundedRect.renderOutline(context, panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT,
            DesignTokens.RADIUS_LARGE, DesignTokens.BORDER_THIN, DesignTokens.COLOR_BORDER);

        // Header
        int headerY = panelY;
        font.draw(context, "HUD EDITOR", panelX + 16, headerY + 16, DesignTokens.COLOR_PRIMARY_TEXT);
        FontRenderer smallFont = getSmallFont();
        smallFont.draw(context, "Drag elements to reposition", panelX + 16, headerY + 36, DesignTokens.COLOR_MUTED_TEXT);

        // Toolbar
        int toolbarY = panelY + 60;
        renderToolbar(context, font, panelX + 16, toolbarY, PANEL_WIDTH - 32, mouseX, mouseY);

        // Element list
        int listY = toolbarY + 40;
        int listHeight = PANEL_HEIGHT - 60 - 40 - 16;
        renderElementList(context, font, panelX + 16, listY, PANEL_WIDTH - 32, listHeight, mouseX, mouseY);

        // Settings panel (if element selected)
        if (selectedElement != null) {
            renderSettingsPanel(context, font, panelX + 16, listY + listHeight + 16, PANEL_WIDTH - 32, mouseX, mouseY);
        }
    }

    private void renderToolbar(DrawContext context, FontRenderer font, int x, int y, int width, int mouseX, int mouseY) {
        int btnHeight = 28;
        int spacing = 8;

        // Grid toggle
        boolean gridHovered = renderToggleButton(context, font, x, y, 80, btnHeight, "Grid", showGrid, mouseX, mouseY);
        x += 80 + spacing;

        // Alignment guides toggle
        boolean guidesHovered = renderToggleButton(context, font, x, y, 100, btnHeight, "Guides", showAlignmentGuides, mouseX, mouseY);
        x += 100 + spacing;

        // Grid size slider (simplified)
        font.draw(context, "Snap: " + (int) gridSize + "px", x, y + (btnHeight - font.getFontHeight()) / 2, DesignTokens.COLOR_SECONDARY_TEXT);
    }

    private boolean renderToggleButton(DrawContext context, FontRenderer font, int x, int y, int w, int h, String label, boolean state, int mx, int my) {
        boolean hovered = mx >= x && mx < x + w && my >= y && my < y + h;
        int bgColor = hovered ? DesignTokens.COLOR_HOVER_BACKGROUND : (state ? DesignTokens.COLOR_PRIMARY_PURPLE : DesignTokens.COLOR_BACKGROUND);
        RoundedRect.render(context, x, y, w, h, DesignTokens.RADIUS_SMALL, bgColor);
        RoundedRect.renderOutline(context, x, y, w, h, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN,
            hovered ? DesignTokens.COLOR_BORDER_HOVER : DesignTokens.COLOR_BORDER);

        int textColor = state ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_SECONDARY_TEXT;
        font.drawCentered(context, label, x + w / 2, y + (h - font.getFontHeight()) / 2, textColor);
        return hovered;
    }

    private void renderElementList(DrawContext context, FontRenderer font, int x, int y, int width, int height,
                                   int mouseX, int mouseY) {
        List<HudElement> elements = HudManager.getInstance().getAllElements();

        // Background
        RoundedRect.render(context, x, y, width, height, DesignTokens.RADIUS_SMALL, DesignTokens.COLOR_BACKGROUND);
        RoundedRect.renderOutline(context, x, y, width, height, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN, DesignTokens.COLOR_BORDER);

        int itemHeight = 36;
        int visibleItems = height / itemHeight;
        int startY = y + 4 - scrollOffset;

        for (int i = 0; i < elements.size(); i++) {
            HudElement element = elements.get(i);
            int itemY = startY + i * itemHeight;

            // Clip to list bounds
            if (itemY + itemHeight < y + 4 || itemY > y + height - 4) continue;

            boolean isSelected = element == selectedElement;
            boolean isHovered = mouseX >= x && mouseX < x + width && mouseY >= itemY && mouseY < itemY + itemHeight;

            // Item background
            int bgColor = isSelected ? DesignTokens.COLOR_PRIMARY_PURPLE :
                         isHovered ? DesignTokens.COLOR_HOVER_BACKGROUND : 0x00000000;
            RoundedRect.render(context, x + 2, itemY, width - 4, itemHeight - 2, DesignTokens.RADIUS_SMALL, bgColor);

            // Visibility icon
            String visIcon = element.isVisible() ? "󰈈" : "󰈉";
            int iconColor = element.isVisible() ? DesignTokens.COLOR_ACCENT_GREEN : DesignTokens.COLOR_MUTED_TEXT;
            if (isSelected) iconColor = 0xFFFFFFFF;
            font.draw(context, visIcon, x + 8, itemY + (itemHeight - font.getFontHeight()) / 2, iconColor);

            // Lock icon
            String lockIcon = element.isLocked() ? "󰌾" : "󰌿";
            int lockX = x + width - 30;
            font.draw(context, lockIcon, lockX, itemY + (itemHeight - font.getFontHeight()) / 2,
                element.isLocked() ? DesignTokens.COLOR_WARNING : DesignTokens.COLOR_MUTED_TEXT);

            // Name
            int nameColor = isSelected ? 0xFFFFFFFF : (element.isVisible() ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_MUTED_TEXT);
            font.draw(context, element.getName(), x + 32, itemY + (itemHeight - font.getFontHeight()) / 2, nameColor);
        }

        maxScrollOffset = Math.max(0, elements.size() * itemHeight - height + 8);
    }

    private void renderSettingsPanel(DrawContext context, FontRenderer font, int x, int y, int width, int mouseX, int mouseY) {
        // Simplified settings panel for selected element
        int panelHeight = 200;
        RoundedRect.render(context, x, y, width, panelHeight, DesignTokens.RADIUS_SMALL, DesignTokens.COLOR_CARD_BACKGROUND);
        RoundedRect.renderOutline(context, x, y, width, panelHeight, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN, DesignTokens.COLOR_BORDER);

        font.draw(context, selectedElement.getName() + " Settings", x + 12, y + 12, DesignTokens.COLOR_PRIMARY_TEXT);

        // Visibility toggle
        renderSettingsToggle(context, font, x + 12, y + 40, width - 24, 28,
            "Visible", selectedElement.isVisible(), mouseX, mouseY,
            v -> selectedElement.setVisible(v));

        // Lock toggle
        renderSettingsToggle(context, font, x + 12, y + 72, width - 24, 28,
            "Locked", selectedElement.isLocked(), mouseX, mouseY,
            v -> selectedElement.setLocked(v));

        // Scale slider (simplified)
        font.draw(context, "Scale: " + (int)(selectedElement.getScale() * 100) + "%", x + 12, y + 110, DesignTokens.COLOR_SECONDARY_TEXT);

        // Opacity slider
        font.draw(context, "Opacity: " + (int)(selectedElement.getOpacity() * 100) + "%", x + 12, y + 130, DesignTokens.COLOR_SECONDARY_TEXT);

        // Background color
        font.draw(context, "Background", x + 12, y + 150, DesignTokens.COLOR_SECONDARY_TEXT);

        // Border color
        font.draw(context, "Border", x + 12, y + 170, DesignTokens.COLOR_SECONDARY_TEXT);
    }

    private void renderSettingsToggle(DrawContext context, FontRenderer font, int x, int y, int w, int h,
                                      String label, boolean state, int mx, int my, java.util.function.Consumer<Boolean> onChange) {
        boolean hovered = mx >= x && mx < x + w && my >= y && my < y + h;
        int bgColor = hovered ? DesignTokens.COLOR_HOVER_BACKGROUND : (state ? DesignTokens.COLOR_PRIMARY_PURPLE : DesignTokens.COLOR_BACKGROUND);
        RoundedRect.render(context, x, y, w, h, DesignTokens.RADIUS_SMALL, bgColor);
        RoundedRect.renderOutline(context, x, y, w, h, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN,
            hovered ? DesignTokens.COLOR_BORDER_HOVER : DesignTokens.COLOR_BORDER);

        font.draw(context, label, x + 12, y + (h - font.getFontHeight()) / 2, DesignTokens.COLOR_PRIMARY_TEXT);

        // Toggle switch
        int switchX = x + w - 60;
        int switchY = y + (h - 20) / 2;
        int trackColor = state ? DesignTokens.COLOR_ACCENT_GREEN : DesignTokens.COLOR_BORDER;
        RoundedRect.render(context, switchX, switchY, 50, 20, 10, trackColor);
        int thumbX = state ? switchX + 30 : switchX;
        RoundedRect.render(context, thumbX, switchY, 20, 20, 10, DesignTokens.COLOR_PRIMARY_TEXT);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int mx = (int) mouseX;
        int my = (int) mouseY;

        // Check if clicking in panel
        int panelX = width - PANEL_WIDTH - 20;
        int panelY = (height - PANEL_HEIGHT) / 2;

        if (mx >= panelX && mx < panelX + PANEL_WIDTH && my >= panelY && my < panelY + PANEL_HEIGHT) {
            // Click in panel - handle UI
            return handlePanelClick(mx, my, button, panelX, panelY);
        } else if (HudManager.getInstance().isEditorMode()) {
            // Click in game world - try to drag element
            int screenWidth = net.minecraft.client.MinecraftClient.getInstance().getWindow().getScaledWidth();
            int screenHeight = net.minecraft.client.MinecraftClient.getInstance().getWindow().getScaledHeight();
            HudElement element = HudManager.getInstance().getElementAt(mx, my, screenWidth, screenHeight);
            if (element != null && !element.isLocked()) {
                HudManager.getInstance().startDrag(element, mx, my, screenWidth, screenHeight);
                selectedElement = element;
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean handlePanelClick(int mx, int my, int button, int panelX, int panelY) {
        int x = panelX + 16;
        int y = panelY + 60;

        // Grid toggle
        if (my >= y && my < y + 28 && mx >= x && mx < x + 80) {
            showGrid = !showGrid;
            return true;
        }
        x += 88;

        // Guides toggle
        if (my >= y && my < y + 28 && mx >= x && mx < x + 100) {
            showAlignmentGuides = !showAlignmentGuides;
            return true;
        }

        // Element list
        int listY = panelY + 60 + 40;
        int listHeight = PANEL_HEIGHT - 60 - 40 - 16;
        if (mx >= panelX + 16 && mx < panelX + 16 + PANEL_WIDTH - 32 &&
            my >= listY && my < listY + listHeight) {
            int itemHeight = 36;
            int index = (my - listY + scrollOffset) / itemHeight;
            List<HudElement> elements = HudManager.getInstance().getAllElements();
            if (index >= 0 && index < elements.size()) {
                selectedElement = elements.get(index);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        HudManager.getInstance().endDrag();
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int panelX = width - PANEL_WIDTH - 20;
        int panelY = (height - PANEL_HEIGHT) / 2;
        int listY = panelY + 60 + 40;
        int listHeight = PANEL_HEIGHT - 60 - 40 - 16;

        int mx = (int) mouseX;
        int my = (int) mouseY;

        if (mx >= panelX + 16 && mx < panelX + 16 + PANEL_WIDTH - 32 &&
            my >= listY && my < listY + listHeight) {
            scrollOffset = (int) Math.max(0, Math.min(maxScrollOffset, scrollOffset - verticalAmount * 20));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (HudManager.getInstance().isEditorMode()) {
            int screenWidth = net.minecraft.client.MinecraftClient.getInstance().getWindow().getScaledWidth();
            int screenHeight = net.minecraft.client.MinecraftClient.getInstance().getWindow().getScaledHeight();
            HudManager.getInstance().updateDrag((int) mouseX, (int) mouseY, screenWidth, screenHeight);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void close() {
        targetOpenProgress = 0f;
        if (openProgress < 0.01f) {
            HudManager.getInstance().setEditorMode(false);
            HudManager.getInstance().saveLayouts();
            super.close();
        }
    }

    @Override
    public boolean shouldPause() {
        return false; // Don't pause game in editor
    }

    private FontRenderer getFontRenderer() {
        return FontManager.getInstance().getRegular();
    }

    private FontRenderer getSmallFont() {
        return FontManager.getInstance().getRegular(); // Would use smaller size
    }
}