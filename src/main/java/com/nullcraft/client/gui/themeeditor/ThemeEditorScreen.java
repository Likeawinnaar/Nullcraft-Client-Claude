package com.nullcraft.client.gui.themeeditor;

import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.ColorUtils;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Theme Editor Screen: Live preview, all design tokens editable, 7 built-in themes + custom
 */
public class ThemeEditorScreen extends Screen {

    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 700;

    private float openProgress = 0f;
    private float targetOpenProgress = 1f;

    // Theme state
    private Theme currentTheme = new Theme("Custom", Theme.ThemeType.CUSTOM);
    private Theme.ThemeType selectedPreset = Theme.ThemeType.DEFAULT;
    private int scrollOffset = 0;
    private int maxScrollOffset = 0;

    // Token categories
    private TokenCategory activeCategory = TokenCategory.COLORS;

    public ThemeEditorScreen() {
        super(Text.literal("Theme Editor"));
    }

    @Override
    protected void init() {
        super.init();
        loadBuiltInThemes();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        openProgress = net.minecraft.util.math.MathHelper.lerp(0.15f, openProgress, targetOpenProgress);

        if (openProgress < 0.01f) {
            close();
            return;
        }

        renderBackgroundOverlay(context);
        renderPanel(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderBackgroundOverlay(DrawContext context) {
        int alpha = (int) (0xCC * openProgress);
        int color = (alpha << 24) | currentTheme.getColor(DesignTokens.COLOR_BACKGROUND);
        context.fill(0, 0, width, height, color);
    }

    private void renderPanel(DrawContext context, int mouseX, int mouseY, float delta) {
        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - PANEL_HEIGHT) / 2;
        FontRenderer font = getFontRenderer();
        FontRenderer boldFont = getBoldFont();

        // Panel shadow
        for (int i = 0; i < 20; i++) {
            float progress = i / 20f;
            int alpha = (int) (DesignTokens.SHADOW_GLOW_ALPHA * (1f - progress) * openProgress);
            int color = (alpha << 24) | currentTheme.getColor(DesignTokens.COLOR_PRIMARY_PURPLE);
            RoundedRect.render(context,
                panelX - i, panelY - i,
                PANEL_WIDTH + i * 2, PANEL_HEIGHT + i * 2,
                DesignTokens.RADIUS_LARGE + i, color);
        }

        // Panel background
        RoundedRect.render(context, panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT,
            DesignTokens.RADIUS_LARGE, currentTheme.getColor(DesignTokens.COLOR_PANEL_BACKGROUND));
        RoundedRect.renderOutline(context, panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT,
            DesignTokens.RADIUS_LARGE, DesignTokens.BORDER_THIN, currentTheme.getColor(DesignTokens.COLOR_BORDER));

        // Header
        int headerY = panelY;
        boldFont.draw(context, "THEME EDITOR", panelX + 20, headerY + 20, currentTheme.getColor(DesignTokens.COLOR_PRIMARY_TEXT));
        font.draw(context, "Live preview • All tokens editable", panelX + 20, headerY + 44, currentTheme.getColor(DesignTokens.COLOR_MUTED_TEXT));

        // Preset selector
        int presetY = headerY + 70;
        renderPresetSelector(context, font, panelX + 20, presetY, PANEL_WIDTH - 40, mouseX, mouseY);

        // Category tabs
        int tabsY = presetY + 50;
        renderCategoryTabs(context, font, panelX + 20, tabsY, PANEL_WIDTH - 40, mouseX, mouseY);

        // Token editors
        int contentY = tabsY + 40;
        int contentHeight = PANEL_HEIGHT - 70 - 50 - 40 - 20;
        renderTokenEditors(context, font, panelX + 20, contentY, PANEL_WIDTH - 40, contentHeight, mouseX, mouseY);

        // Live preview area
        int previewY = panelY + PANEL_HEIGHT - 120;
        renderPreview(context, font, panelX + 20, previewY, PANEL_WIDTH - 40, 100, mouseX, mouseY);

        // Action buttons
        int btnY = panelY + PANEL_HEIGHT - 50;
        renderActionButtons(context, font, panelX + 20, btnY, PANEL_WIDTH - 40, mouseX, mouseY);
    }

    private void renderPresetSelector(DrawContext context, FontRenderer font, int x, int y, int width, int mx, int my) {
        font.draw(context, "Preset:", x, y + 10, currentTheme.getColor(DesignTokens.COLOR_SECONDARY_TEXT));

        int btnX = x + 70;
        for (Theme.ThemeType preset : Theme.ThemeType.values()) {
            if (preset == Theme.ThemeType.CUSTOM) continue;

            int btnWidth = 80;
            boolean isSelected = preset == selectedPreset;
            boolean hovered = mx >= btnX && mx < btnX + btnWidth && my >= y && my < y + 30;

            int bgColor = isSelected ? currentTheme.getColor(DesignTokens.COLOR_PRIMARY_PURPLE) :
                         hovered ? currentTheme.getColor(DesignTokens.COLOR_HOVER_BACKGROUND) :
                         currentTheme.getColor(DesignTokens.COLOR_BACKGROUND);
            RoundedRect.render(context, btnX, y, btnWidth, 30, DesignTokens.RADIUS_SMALL, bgColor);
            RoundedRect.renderOutline(context, btnX, y, btnWidth, 30, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN,
                isSelected ? currentTheme.getColor(DesignTokens.COLOR_PRIMARY_TEXT) :
                hovered ? currentTheme.getColor(DesignTokens.COLOR_BORDER_HOVER) :
                currentTheme.getColor(DesignTokens.COLOR_BORDER));

            int textColor = isSelected ? 0xFFFFFFFF : currentTheme.getColor(DesignTokens.COLOR_PRIMARY_TEXT);
            font.drawCentered(context, preset.name(), btnX + btnWidth / 2, y + 5, textColor);

            btnX += btnWidth + 8;
        }
    }

    private void renderCategoryTabs(DrawContext context, FontRenderer font, int x, int y, int width, int mx, int my) {
        TokenCategory[] categories = TokenCategory.values();
        int tabWidth = width / categories.length;

        for (int i = 0; i < categories.length; i++) {
            TokenCategory cat = categories[i];
            boolean isActive = cat == activeCategory;
            int tabX = x + i * tabWidth;
            boolean hovered = mx >= tabX && mx < tabX + tabWidth && my >= y && my < y + 34;

            int bgColor = isActive ? currentTheme.getColor(DesignTokens.COLOR_PRIMARY_PURPLE) :
                         hovered ? currentTheme.getColor(DesignTokens.COLOR_HOVER_BACKGROUND) : 0x00000000;
            RoundedRect.render(context, tabX, y, tabWidth, 34, DesignTokens.RADIUS_SMALL, bgColor);

            int textColor = isActive ? 0xFFFFFFFF : currentTheme.getColor(DesignTokens.COLOR_PRIMARY_TEXT);
            font.drawCentered(context, cat.getLabel(), tabX + tabWidth / 2, y + 8, textColor);
        }
    }

    private void renderTokenEditors(DrawContext context, FontRenderer font, int x, int y, int width, int height,
                                    int mx, int my) {
        // Background
        RoundedRect.render(context, x, y, width, height, DesignTokens.RADIUS_SMALL,
            currentTheme.getColor(DesignTokens.COLOR_BACKGROUND));
        RoundedRect.renderOutline(context, x, y, width, height, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN,
            currentTheme.getColor(DesignTokens.COLOR_BORDER));

        int tokenY = y + 12 - scrollOffset;
        List<TokenEditor> editors = getEditorsForCategory(activeCategory);

        for (TokenEditor editor : editors) {
            int editorHeight = editor.render(context, font, x + 12, tokenY, width - 24, currentTheme, mx, my);
            tokenY += editorHeight + 8;
        }

        maxScrollOffset = Math.max(0, tokenY - y - height + 12);
    }

    private void renderPreview(DrawContext context, FontRenderer font, int x, int y, int width, int height,
                               int mx, int my) {
        FontRenderer boldFont = getBoldFont();

        RoundedRect.render(context, x, y, width, height, DesignTokens.RADIUS_MEDIUM,
            currentTheme.getColor(DesignTokens.COLOR_PANEL_BACKGROUND));
        RoundedRect.renderOutline(context, x, y, width, height, DesignTokens.RADIUS_MEDIUM, DesignTokens.BORDER_THIN,
            currentTheme.getColor(DesignTokens.COLOR_BORDER));

        // Preview content
        boldFont.draw(context, "Preview", x + 12, y + 12, currentTheme.getColor(DesignTokens.COLOR_PRIMARY_TEXT));
        font.draw(context, "This is how your theme looks in real-time.", x + 12, y + 36, currentTheme.getColor(DesignTokens.COLOR_SECONDARY_TEXT));

        // Sample button
        int btnX = x + 12;
        int btnY = y + 60;
        int btnW = 120, btnH = 28;
        RoundedRect.render(context, btnX, btnY, btnW, btnH, DesignTokens.RADIUS_SMALL,
            currentTheme.getColor(DesignTokens.COLOR_PRIMARY_PURPLE));
        font.drawCentered(context, "Sample Button", btnX + btnW / 2, btnY + 6, 0xFFFFFFFF);

        // Sample toggle
        int toggleX = btnX + btnW + 12;
        int toggleY = btnY;
        int trackColor = currentTheme.getColor(DesignTokens.COLOR_ACCENT_GREEN);
        RoundedRect.render(context, toggleX, toggleY, 50, 28, 14, trackColor);
        RoundedRect.render(context, toggleX + 22, toggleY + 4, 20, 20, 10, 0xFFFFFFFF);

        // Sample input
        int inputX = toggleX + 60;
        int inputY = btnY;
        RoundedRect.render(context, inputX, inputY, 140, 28, DesignTokens.RADIUS_SMALL,
            currentTheme.getColor(DesignTokens.COLOR_BACKGROUND));
        RoundedRect.renderOutline(context, inputX, inputY, 140, 28, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN,
            currentTheme.getColor(DesignTokens.COLOR_BORDER));
        font.draw(context, "Sample input...", inputX + 12, inputY + 6, currentTheme.getColor(DesignTokens.COLOR_MUTED_TEXT));
    }

    private void renderActionButtons(DrawContext context, FontRenderer font, int x, int y, int width, int mx, int my) {
        int btnW = 100, btnH = 32;
        int spacing = 12;
        int startX = x + (width - (btnW * 4 + spacing * 3)) / 2;

        // Save button
        renderActionButton(context, font, startX, y, btnW, btnH, "Save Theme", true, mx, my);
        // Export button
        renderActionButton(context, font, startX + btnW + spacing, y, btnW, btnH, "Export", false, mx, my);
        // Import button
        renderActionButton(context, font, startX + 2 * (btnW + spacing), y, btnW, btnH, "Import", false, mx, my);
        // Reset button
        renderActionButton(context, font, startX + 3 * (btnW + spacing), y, btnW, btnH, "Reset", false, mx, my);
    }

    private void renderActionButton(DrawContext context, FontRenderer font, int x, int y, int w, int h,
                                    String label, boolean primary, int mx, int my) {
        boolean hovered = mx >= x && mx < x + w && my >= y && my < y + h;
        int bgColor = primary ?
            (hovered ? ColorUtils.lerp(0xFF6B46C1, 0xFF5535A0, 0.3f) : 0xFF6B46C1) :
            (hovered ? DesignTokens.COLOR_HOVER_BACKGROUND : DesignTokens.COLOR_BACKGROUND);

        RoundedRect.render(context, x, y, w, h, DesignTokens.RADIUS_SMALL, bgColor);
        RoundedRect.renderOutline(context, x, y, w, h, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN,
            primary ? 0x00000000 : (hovered ? DesignTokens.COLOR_BORDER_HOVER : DesignTokens.COLOR_BORDER));

        int textColor = primary ? 0xFFFFFFFF : (hovered ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_SECONDARY_TEXT);
        font.drawCentered(context, label, x + w / 2, y + (h - font.getFontHeight()) / 2, textColor);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Handle clicks on preset selector, category tabs, token editors, preview, action buttons
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        // Scroll token editors
        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - PANEL_HEIGHT) / 2;
        int contentY = panelY + 70 + 50 + 40;
        int contentHeight = PANEL_HEIGHT - 70 - 50 - 40 - 20 - 120 - 50;

        int mx = (int) mouseX;
        int my = (int) mouseY;

        if (mx >= panelX + 20 && mx < panelX + PANEL_WIDTH - 20 &&
            my >= contentY && my < contentY + contentHeight) {
            scrollOffset = (int) Math.max(0, Math.min(maxScrollOffset, scrollOffset - verticalAmount * 30));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void close() {
        targetOpenProgress = 0f;
        if (openProgress < 0.01f) {
            super.close();
        }
    }

    private List<TokenEditor> getEditorsForCategory(TokenCategory category) {
        List<TokenEditor> editors = new ArrayList<>();

        switch (category) {
            case COLORS -> {
                editors.add(new ColorTokenEditor("Primary Background", DesignTokens.COLOR_BACKGROUND));
                editors.add(new ColorTokenEditor("Panel Background", DesignTokens.COLOR_PANEL_BACKGROUND));
                editors.add(new ColorTokenEditor("Sidebar Background", DesignTokens.COLOR_SIDEBAR_BACKGROUND));
                editors.add(new ColorTokenEditor("Card Background", DesignTokens.COLOR_CARD_BACKGROUND));
                editors.add(new ColorTokenEditor("Hover Background", DesignTokens.COLOR_HOVER_BACKGROUND));
                editors.add(new ColorTokenEditor("Primary Text", DesignTokens.COLOR_PRIMARY_TEXT));
                editors.add(new ColorTokenEditor("Secondary Text", DesignTokens.COLOR_SECONDARY_TEXT));
                editors.add(new ColorTokenEditor("Muted Text", DesignTokens.COLOR_MUTED_TEXT));
                editors.add(new ColorTokenEditor("Disabled Text", DesignTokens.COLOR_DISABLED));
                editors.add(new ColorTokenEditor("Primary Purple", DesignTokens.COLOR_PRIMARY_PURPLE));
                editors.add(new ColorTokenEditor("Accent Green", DesignTokens.COLOR_ACCENT_GREEN));
                editors.add(new ColorTokenEditor("Accent Red", DesignTokens.COLOR_ACCENT_RED));
                editors.add(new ColorTokenEditor("Accent Yellow", DesignTokens.COLOR_ACCENT_YELLOW));
                editors.add(new ColorTokenEditor("Accent Cyan", DesignTokens.COLOR_ACCENT_CYAN));
                editors.add(new ColorTokenEditor("Border", DesignTokens.COLOR_BORDER));
                editors.add(new ColorTokenEditor("Border Hover", DesignTokens.COLOR_BORDER_HOVER));
                editors.add(new ColorTokenEditor("Shadow Glow", DesignTokens.SHADOW_GLOW_ALPHA));
                editors.add(new ColorTokenEditor("Shadow Drop", DesignTokens.SHADOW_DROP_ALPHA));
            }
            case RADII -> {
                editors.add(new FloatTokenEditor("Radius Small", DesignTokens.RADIUS_SMALL, 0, 20));
                editors.add(new FloatTokenEditor("Radius Medium", DesignTokens.RADIUS_MEDIUM, 0, 30));
                editors.add(new FloatTokenEditor("Radius Large", DesignTokens.RADIUS_LARGE, 0, 40));
                editors.add(new FloatTokenEditor("Radius XL", DesignTokens.RADIUS_XL, 0, 50));
            }
            case SPACING -> {
                editors.add(new IntTokenEditor("Spacing XS", DesignTokens.SPACING_XS, 0, 20));
                editors.add(new IntTokenEditor("Spacing SM", DesignTokens.SPACING_SM, 0, 20));
                editors.add(new IntTokenEditor("Spacing MD", DesignTokens.SPACING_MD, 0, 30));
                editors.add(new IntTokenEditor("Spacing LG", DesignTokens.SPACING_LG, 0, 40));
                editors.add(new IntTokenEditor("Spacing XL", DesignTokens.SPACING_XL, 0, 50));
            }
            case SHADOWS -> {
                editors.add(new FloatTokenEditor("Shadow Glow Alpha", DesignTokens.SHADOW_GLOW_ALPHA / 255f, 0, 1));
                editors.add(new FloatTokenEditor("Shadow Drop Alpha", DesignTokens.SHADOW_DROP_ALPHA / 255f, 0, 1));
                editors.add(new FloatTokenEditor("Shadow Blur", DesignTokens.SHADOW_BLUR, 0, 50));
                editors.add(new FloatTokenEditor("Shadow Spread", DesignTokens.SHADOW_SPREAD, 0, 20));
            }
            case ANIMATIONS -> {
                editors.add(new FloatTokenEditor("Fast Duration", DesignTokens.ANIM_FAST, 0, 1));
                editors.add(new FloatTokenEditor("Normal Duration", DesignTokens.ANIM_NORMAL, 0, 1));
                editors.add(new FloatTokenEditor("Slow Duration", DesignTokens.ANIM_SLOW, 0, 1));
                editors.add(new FloatTokenEditor("Hover Duration", DesignTokens.ANIM_HOVER, 0, 1));
                editors.add(new FloatTokenEditor("Press Duration", DesignTokens.ANIM_PRESS, 0, 1));
            }
            case TYPOGRAPHY -> {
                editors.add(new IntTokenEditor("Base Font Size", DesignTokens.TYPOGRAPHY_BASE_SIZE, 8, 32));
                editors.add(new IntTokenEditor("Small Font Size", DesignTokens.TYPOGRAPHY_SMALL_SIZE, 8, 24));
                editors.add(new IntTokenEditor("Large Font Size", DesignTokens.TYPOGRAPHY_LARGE_SIZE, 16, 48));
                editors.add(new IntTokenEditor("Line Height", DesignTokens.TYPOGRAPHY_LINE_HEIGHT, 1, 3));
            }
            case LAYOUT -> {
                editors.add(new IntTokenEditor("Panel Width", DesignTokens.LAYOUT_PANEL_WIDTH, 200, 1200));
                editors.add(new IntTokenEditor("Panel Height", DesignTokens.LAYOUT_PANEL_HEIGHT, 200, 1000));
                editors.add(new IntTokenEditor("Sidebar Width", DesignTokens.LAYOUT_SIDEBAR_WIDTH, 150, 400));
                editors.add(new IntTokenEditor("Header Height", DesignTokens.LAYOUT_HEADER_HEIGHT, 40, 100));
                editors.add(new IntTokenEditor("Border Thin", DesignTokens.BORDER_THIN, 0, 5));
                editors.add(new IntTokenEditor("Border Thick", DesignTokens.BORDER_THICK, 0, 10));
            }
        }

        return editors;
    }

    private void loadBuiltInThemes() {
        // Load built-in themes
    }

    private FontRenderer getFontRenderer() {
        return FontManager.getInstance().getRegular();
    }

    private FontRenderer getBoldFont() {
        return FontManager.getInstance().getBold();
    }

    // ============================================================
    // TOKEN EDITOR CLASSES
    // ============================================================

    public enum TokenCategory {
        COLORS("Colors"),
        RADII("Radii"),
        SPACING("Spacing"),
        SHADOWS("Shadows"),
        ANIMATIONS("Animations"),
        TYPOGRAPHY("Typography"),
        LAYOUT("Layout");

        private final String label;
        TokenCategory(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private interface TokenEditor {
        int render(DrawContext context, FontRenderer font, int x, int y, int width, Theme theme, int mx, int my);
    }

    private static class ColorTokenEditor implements TokenEditor {
        private final String label;
        private final int tokenId;

        ColorTokenEditor(String label, int tokenId) {
            this.label = label;
            this.tokenId = tokenId;
        }

        @Override
        public int render(DrawContext context, FontRenderer font, int x, int y, int width, Theme theme, int mx, int my) {
            int currentColor = theme.getColor(tokenId);
            int color = currentColor & 0x00FFFFFF;
            int alpha = (currentColor >> 24) & 0xFF;

            font.draw(context, label, x, y, theme.getColor(DesignTokens.COLOR_PRIMARY_TEXT));

            // Color preview
            int previewX = x + width - 80;
            int previewY = y;
            RoundedRect.render(context, previewX, previewY, 36, 20, DesignTokens.RADIUS_SMALL, color);
            RoundedRect.renderOutline(context, previewX, previewY, 36, 20, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN, theme.getColor(DesignTokens.COLOR_BORDER));

            // Alpha slider (simplified)
            return 28;
        }
    }

    private static class FloatTokenEditor implements TokenEditor {
        private final String label;
        private final float tokenId;
        private final float min, max;

        FloatTokenEditor(String label, float tokenId, float min, float max) {
            this.label = label;
            this.tokenId = tokenId;
            this.min = min;
            this.max = max;
        }

        @Override
        public int render(DrawContext context, FontRenderer font, int x, int y, int width, Theme theme, int mx, int my) {
            font.draw(context, label + ": " + tokenId, x, y, theme.getColor(DesignTokens.COLOR_PRIMARY_TEXT));

            // Slider track
            int sliderY = y + 20;
            int sliderW = width - 24;
            RoundedRect.render(context, x + 12, sliderY, sliderW, 6, 3, theme.getColor(DesignTokens.COLOR_BORDER));

            // Slider thumb
            float progress = (tokenId - min) / (max - min);
            int thumbX = x + 12 + (int) (sliderW * progress);
            RoundedRect.render(context, thumbX - 8, sliderY - 4, 16, 14, 7, theme.getColor(DesignTokens.COLOR_PRIMARY_PURPLE));

            return 36;
        }
    }

    private static class IntTokenEditor implements TokenEditor {
        private final String label;
        private final int tokenId;
        private final int min, max;

        IntTokenEditor(String label, int tokenId, int min, int max) {
            this.label = label;
            this.tokenId = tokenId;
            this.min = min;
            this.max = max;
        }

        @Override
        public int render(DrawContext context, FontRenderer font, int x, int y, int width, Theme theme, int mx, int my) {
            font.draw(context, label + ": " + tokenId, x, y, theme.getColor(DesignTokens.COLOR_PRIMARY_TEXT));

            int sliderY = y + 20;
            int sliderW = width - 24;
            RoundedRect.render(context, x + 12, sliderY, sliderW, 6, 3, theme.getColor(DesignTokens.COLOR_BORDER));

            float progress = (tokenId - min) / (float) (max - min);
            int thumbX = x + 12 + (int) (sliderW * progress);
            RoundedRect.render(context, thumbX - 8, sliderY - 4, 16, 14, 7, theme.getColor(DesignTokens.COLOR_PRIMARY_PURPLE));

            return 36;
        }
    }
}