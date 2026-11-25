package com.nullcraft.client.gui.hud;

import com.nullcraft.client.core.settings.*;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.ColorUtils;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Keystrokes HUD element: Shows WASD + LMB/RMB + CPS
 */
public class KeystrokesHud extends HudElement {

    private BooleanSetting showWASD = new BooleanSetting("Show WASD", "Display WASD keys", true);
    private BooleanSetting showMouse = new BooleanSetting("Show Mouse", "Display LMB/RMB", true);
    private BooleanSetting showCPS = new BooleanSetting("Show CPS", "Display clicks per second", true);
    private BooleanSetting showSpace = new BooleanSetting("Show Space", "Display space key", true);
    private BooleanSetting showShift = new BooleanSetting("Show Shift", "Display shift key", false);
    private ModeSetting layout = new ModeSetting("Layout", "Key layout style",
        List.of("Horizontal", "Vertical", "Cross", "Compact"), 0);
    private IntegerSetting keySize = new IntegerSetting("Key Size", "Size of key boxes", 40, 20, 80);
    private IntegerSetting keySpacing = new IntegerSetting("Key Spacing", "Spacing between keys", 4, 0, 20);
    private ColorSetting keyColor = new ColorSetting("Key Color", "Color for unpressed keys", DesignTokens.COLOR_PANEL_BACKGROUND, false);
    private ColorSetting pressedKeyColor = new ColorSetting("Pressed Key Color", "Color for pressed keys", DesignTokens.COLOR_PRIMARY_PURPLE, false);
    private ColorSetting textColor = new ColorSetting("Text Color", "Color for key labels", DesignTokens.COLOR_PRIMARY_TEXT, false);
    private ColorSetting cpsColor = new ColorSetting("CPS Color", "Color for CPS counter", DesignTokens.COLOR_ACCENT_GREEN, false);

    // CPS tracking
    private int leftClicks = 0;
    private int rightClicks = 0;
    private int leftCPS = 0;
    private int rightCPS = 0;
    private long lastCPSTick = System.currentTimeMillis();

    public KeystrokesHud() {
        super("keystrokes", "Keystrokes", "Shows WASD + LMB/RMB + CPS");
        // Default position: bottom-center
        setX(0.5);
        setY(0.85);
        setUseNormalizedCoords(true);
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta,
                       int x, int y, int width, int height) {
        if (!isVisible()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        updateCPS();

        List<KeyState> keys = getKeysToDisplay(mc);
        if (keys.isEmpty()) return;

        FontRenderer boldFont = getBoldFont();
        String layoutStr = layout.getSelected();

        int keySizeVal = keySize.getValue();
        int spacing = keySpacing.getValue();

        // Calculate layout dimensions
        int totalWidth = 0, totalHeight = 0;
        switch (layoutStr) {
            case "Horizontal" -> {
                totalWidth = keys.size() * keySizeVal + (keys.size() - 1) * spacing;
                totalHeight = keySizeVal;
            }
            case "Vertical" -> {
                totalWidth = keySizeVal;
                totalHeight = keys.size() * keySizeVal + (keys.size() - 1) * spacing;
            }
            case "Cross" -> {
                // WASD cross layout
                totalWidth = 3 * keySizeVal + 2 * spacing;
                totalHeight = 3 * keySizeVal + 2 * spacing;
            }
            case "Compact" -> {
                totalWidth = 2 * keySizeVal + spacing;
                totalHeight = 2 * keySizeVal + spacing;
            }
        }

        // CPS area
        int cpsWidth = 0, cpsHeight = 0;
        if (showCPS.getValue()) {
            String cpsText = "LMB: " + leftCPS + "  RMB: " + rightCPS;
            cpsWidth = fontRenderer.getStringWidth(cpsText) + 16;
            cpsHeight = fontRenderer.getFontHeight() + 8;
        }

        int bgWidth = Math.max(totalWidth, cpsWidth) + 16;
        int bgHeight = totalHeight + cpsHeight + 16;

        // Background
        if (isShowBackground()) {
            RoundedRect.render(context, x - 8, y - 8, bgWidth + 16, bgHeight + 16,
                (int) getCornerRadius(), getBackgroundColor());
        }

        // Render keys based on layout
        int currentX = x + (bgWidth - totalWidth) / 2;
        int currentY = y + (bgHeight - totalHeight - cpsHeight) / 2;

        renderKeys(context, fontRenderer, boldFont, keys, layoutStr, currentX, currentY,
            keySizeVal, spacing);

        // Render CPS
        if (showCPS.getValue()) {
            String cpsText = "LMB: " + leftCPS + "  RMB: " + rightCPS;
            int cpsX = x + (bgWidth - cpsWidth) / 2;
            int cpsY = currentY + totalHeight + 8;
            RoundedRect.render(context, cpsX - 8, cpsY - 4, cpsWidth, cpsHeight,
                DesignTokens.RADIUS_SMALL, DesignTokens.COLOR_BACKGROUND);
            fontRenderer.draw(context, cpsText, cpsX, cpsY + (cpsHeight - fontRenderer.getFontHeight()) / 2, cpsColor.getValue());
        }

        // Border
        if (isShowBorder()) {
            RoundedRect.renderOutline(context, x - 8, y - 8, bgWidth + 16, bgHeight + 16,
                (int) getCornerRadius(), (int) getBorderWidth(), getBorderColor());
        }
    }

    private List<KeyState> getKeysToDisplay(MinecraftClient mc) {
        List<KeyState> keys = new ArrayList<>();

        if (showWASD.getValue()) {
            keys.add(new KeyState("W", mc.options.forwardKey));
            keys.add(new KeyState("A", mc.options.leftKey));
            keys.add(new KeyState("S", mc.options.backKey));
            keys.add(new KeyState("D", mc.options.rightKey));
        }
        if (showSpace.getValue()) {
            keys.add(new KeyState("SPACE", mc.options.jumpKey));
        }
        if (showShift.getValue()) {
            keys.add(new KeyState("SHIFT", mc.options.sprintKey));
        }
        if (showMouse.getValue()) {
            keys.add(new KeyState("LMB", mc.options.attackKey));
            keys.add(new KeyState("RMB", mc.options.useKey));
        }

        return keys;
    }

    private void renderKeys(DrawContext context, FontRenderer fontRenderer, FontRenderer boldFont,
                            List<KeyState> keys, String layout, int startX, int startY,
                            int keySize, int spacing) {
        int currentX = startX;
        int currentY = startY;

        switch (layout) {
            case "Horizontal" -> {
                for (KeyState key : keys) {
                    renderKey(context, boldFont, key, currentX, currentY, keySize, keySize);
                    currentX += keySize + spacing;
                }
            }
            case "Vertical" -> {
                for (KeyState key : keys) {
                    renderKey(context, boldFont, key, currentX, currentY, keySize, keySize);
                    currentY += keySize + spacing;
                }
            }
            case "Cross" -> {
                // WASD cross: W at top, A left, S center, D right, space below S
                // Indices: 0=W, 1=A, 1=S, 3=D, 4=SPACE
                if (keys.size() >= 4) {
                    renderKey(context, boldFont, keys.get(0), currentX + keySize + spacing, currentY, keySize, keySize); // W
                    renderKey(context, boldFont, keys.get(1), currentX, currentY + keySize + spacing, keySize, keySize); // A
                    renderKey(context, boldFont, keys.get(2), currentX + keySize + spacing, currentY + keySize + spacing, keySize, keySize); // S
                    renderKey(context, boldFont, keys.get(3), currentX + 2 * (keySize + spacing), currentY + keySize + spacing, keySize, keySize); // D
                    if (keys.size() >= 5) {
                        renderKey(context, boldFont, keys.get(4), currentX + keySize + spacing, currentY + 2 * (keySize + spacing), keySize * 2 + spacing, keySize); // SPACE
                    }
                }
            }
            case "Compact" -> {
                // 2x2 grid
                for (int i = 0; i < keys.size(); i++) {
                    int col = i % 2;
                    int row = i / 2;
                    renderKey(context, boldFont, keys.get(i),
                        currentX + col * (keySize + spacing),
                        currentY + row * (keySize + spacing),
                        keySize, keySize);
                }
            }
        }
    }

    private void renderKey(DrawContext context, FontRenderer font, KeyState key,
                           int x, int y, int width, int height) {
        boolean pressed = key.isPressed();
        int bgColor = pressed ? pressedKeyColor.getValue() : keyColor.getValue();
        int txtColor = pressed ? DesignTokens.COLOR_PRIMARY_TEXT : textColor.getValue();

        RoundedRect.render(context, x, y, width, height, DesignTokens.RADIUS_SMALL, bgColor);
        RoundedRect.renderOutline(context, x, y, width, height, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN,
            pressed ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_BORDER);

        font.drawCentered(context, key.label, x + width / 2, y + (height - font.getFontHeight()) / 2, txtColor);
    }

    private void updateCPS() {
        long now = System.currentTimeMillis();
        if (now - lastCPSTick >= 1000) {
            leftCPS = leftClicks;
            rightCPS = rightClicks;
            leftClicks = 0;
            rightClicks = 0;
            lastCPSTick = now;
        }
    }

    public void onLeftClick() {
        leftClicks++;
    }

    public void onRightClick() {
        rightClicks++;
    }

    @Override
    public int getPreferredWidth(FontRenderer fontRenderer) {
        int keySizeVal = keySize.getValue();
        int spacing = keySpacing.getValue();
        int count = 0;
        if (showWASD.getValue()) count += 4;
        if (showSpace.getValue()) count++;
        if (showShift.getValue()) count++;
        if (showMouse.getValue()) count += 2;

        String layoutStr = layout.getSelected();
        return switch (layoutStr) {
            case "Horizontal" -> count * keySizeVal + (count - 1) * spacing;
            case "Vertical" -> keySizeVal;
            case "Cross" -> 3 * keySizeVal + 2 * spacing;
            case "Compact" -> 2 * keySizeVal + spacing;
            default -> keySizeVal * 3;
        };
    }

    @Override
    public int getPreferredHeight(FontRenderer fontRenderer) {
        int keySizeVal = keySize.getValue();
        int spacing = keySpacing.getValue();
        int count = 0;
        if (showWASD.getValue()) count += 4;
        if (showSpace.getValue()) count++;
        if (showShift.getValue()) count++;
        if (showMouse.getValue()) count += 2;

        String layoutStr = layout.getSelected();
        int height = switch (layoutStr) {
            case "Horizontal" -> keySizeVal;
            case "Vertical" -> count * keySizeVal + (count - 1) * spacing;
            case "Cross" -> 3 * keySizeVal + 2 * spacing;
            case "Compact" -> 2 * keySizeVal + spacing;
            default -> keySizeVal * 3;
        };

        if (showCPS.getValue()) {
            height += fontRenderer.getFontHeight() + 16;
        }
        return height;
    }

    @Override
    public List<Setting<?>> createSettings() {
        List<Setting<?>> settings = new ArrayList<>();
        settings.add(showWASD);
        settings.add(showMouse);
        settings.add(showCPS);
        settings.add(showSpace);
        settings.add(showShift);
        settings.add(layout);
        settings.add(keySize);
        settings.add(keySpacing);
        settings.add(keyColor);
        settings.add(pressedKeyColor);
        settings.add(textColor);
        settings.add(cpsColor);
        return settings;
    }

    private static class KeyState {
        final String label;
        final KeyBinding binding;

        KeyState(String label, KeyBinding binding) {
            this.label = label;
            this.binding = binding;
        }

        boolean isPressed() {
            return binding.isPressed();
        }
    }

    private FontRenderer getBoldFont() {
        return FontRenderer.class.cast(null); // Placeholder
    }
}