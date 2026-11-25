package com.nullcraft.client.gui.hud;

import com.nullcraft.client.core.settings.*;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.ColorUtils;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Watermark HUD element: Shows logo + NULLCRAFT + version
 */
public class WatermarkHud extends HudElement {

    private BooleanSetting showLogo = new BooleanSetting("Show Logo", "Display the NullCraft logo", true);
    private BooleanSetting showVersion = new BooleanSetting("Show Version", "Display client version", true);
    private ModeSetting logoStyle = new ModeSetting("Logo Style", "Logo display style",
        List.of("Minimal", "Full", "Text Only"), 0);
    private ColorSetting accentColor = new ColorSetting("Accent Color", "Color for accent elements",
        DesignTokens.COLOR_PRIMARY_PURPLE, true);
    private IntegerSetting fontSize = new IntegerSetting("Font Size", "Watermark font size", 16, 8, 32);

    public WatermarkHud() {
        super("watermark", "Watermark", "Shows the NullCraft logo and version");
        // Default position: top-left
        setX(0.02);
        setY(0.02);
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta,
                       int x, int y, int width, int height) {
        if (!isVisible()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        FontRenderer boldFont = getBoldFont();

        int currentX = x;
        int currentY = y;
        int accent = accentColor.getValue();

        // Background
        if (isShowBackground()) {
            RoundedRect.render(context, x - 4, y - 2, width + 8, height + 4,
                (int) getCornerRadius(), getBackgroundColor());
        }

        // Logo
        if (showLogo.getValue()) {
            String logoStyleStr = logoStyle.getSelected();
            if ("Minimal".equals(logoStyleStr) || "Full".equals(logoStyleStr)) {
                // Draw "N" logo
                int logoSize = fontSize.getValue() + 4;
                int logoX = currentX;
                int logoY = currentY + (height - logoSize) / 2;
                context.fill(logoX, logoY, logoX + logoSize, logoY + logoSize, accent);
                boldFont.drawCentered(context, "N", logoX + logoSize / 2,
                    logoY + (logoSize - boldFont.getFontHeight()) / 2, 0xFFFFFFFF);
                currentX += logoSize + 6;
            }
        }

        // Text
        if ("Full".equals(logoStyle.getSelected()) || "Text Only".equals(logoStyle.getSelected())) {
            boldFont.draw(context, "NULLCRAFT", currentX, currentY + (height - boldFont.getFontHeight()) / 2, DesignTokens.COLOR_PRIMARY_TEXT);
            currentX += boldFont.getStringWidth("NULLCRAFT") + 4;

            if (showVersion.getValue()) {
                fontRenderer.draw(context, "v1.0.0", currentX, currentY + (height - fontRenderer.getFontHeight()) / 2, DesignTokens.COLOR_MUTED_TEXT);
            }
        }

        // Border
        if (isShowBorder()) {
            RoundedRect.renderOutline(context, x - 4, y - 2, width + 8, height + 4,
                (int) getCornerRadius(), (int) getBorderWidth(), getBorderColor());
        }
    }

    @Override
    public int getPreferredWidth(FontRenderer fontRenderer) {
        int width = 0;
        if (showLogo.getValue() && !"Text Only".equals(logoStyle.getSelected())) {
            width += fontSize.getValue() + 4 + 6;
        }
        if ("Full".equals(logoStyle.getSelected()) || "Text Only".equals(logoStyle.getSelected())) {
            FontRenderer boldFont = getBoldFont();
            width += boldFont.getStringWidth("NULLCRAFT");
            if (showVersion.getValue()) {
                width += 4 + fontRenderer.getStringWidth("v1.0.0");
            }
        }
        return width + 8; // Padding
    }

    @Override
    public int getPreferredHeight(FontRenderer fontRenderer) {
        return Math.max(fontSize.getValue() + 4, fontRenderer.getFontHeight()) + 4;
    }

    @Override
    public List<Setting<?>> createSettings() {
        List<Setting<?>> settings = new ArrayList<>();
        settings.add(showLogo);
        settings.add(showVersion);
        settings.add(logoStyle);
        settings.add(accentColor);
        settings.add(fontSize);
        return settings;
    }

    private FontRenderer getBoldFont() {
        // Would use FontManager.getInstance().getBold()
        return FontRenderer.class.cast(null); // Placeholder
    }
}