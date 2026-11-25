package com.nullcraft.client.gui.hud;

import com.nullcraft.client.core.settings.*;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

/**
 * InfoHUD HUD element: Shows FPS, ping, coordinates, biome, direction
 */
public class InfoHud extends HudElement {

    private BooleanSetting showFPS = new BooleanSetting("Show FPS", "Display frames per second", true);
    private BooleanSetting showPing = new BooleanSetting("Show Ping", "Display server ping", true);
    private BooleanSetting showCoords = new BooleanSetting("Show Coordinates", "Display player coordinates", true);
    private BooleanSetting showBiome = new BooleanSetting("Show Biome", "Display current biome", true);
    private BooleanSetting showDirection = new BooleanSetting("Show Direction", "Display facing direction", true);
    private BooleanSetting showTPS = new BooleanSetting("Show TPS", "Display server TPS", false);
    private ModeSetting coordFormat = new ModeSetting("Coordinate Format", "How to display coordinates",
        List.of("Block", "Chunk", "Both"), 0);
    private BooleanSetting showTime = new BooleanSetting("Show Time", "Display world time", false);
    private IntegerSetting updateInterval = new IntegerSetting("Update Interval", "Update interval in ticks", 20, 1, 100);
    private ColorSetting textColor = new ColorSetting("Text Color", "Color for info text", DesignTokens.COLOR_PRIMARY_TEXT, false);
    private ColorSetting labelColor = new ColorSetting("Label Color", "Color for labels", DesignTokens.COLOR_SECONDARY_TEXT, false);

    // Cached values
    private int cachedFPS = 0;
    private int cachedPing = 0;
    private int cachedTPS = 20;
    private String cachedCoords = "0, 0, 0";
    private String cachedBiome = "Unknown";
    private String cachedDirection = "N";
    private String cachedTime = "0";
    private int updateCounter = 0;

    public InfoHud() {
        super("infohud", "InfoHUD", "Shows FPS, ping, coordinates, biome, direction");
        // Default position: top-left (below watermark)
        setX(0.02);
        setY(0.12);
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta,
                       int x, int y, int width, int height) {
        if (!isVisible()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        updateCache(mc);

        List<String> lines = new ArrayList<>();
        if (showFPS.getValue()) lines.add("FPS: " + cachedFPS);
        if (showPing.getValue()) lines.add("Ping: " + cachedPing + "ms");
        if (showTPS.getValue()) lines.add("TPS: " + cachedTPS);
        if (showCoords.getValue()) lines.add("XYZ: " + cachedCoords);
        if (showBiome.getValue()) lines.add("Biome: " + cachedBiome);
        if (showDirection.getValue()) lines.add("Dir: " + cachedDirection);
        if (showTime.getValue()) lines.add("Time: " + cachedTime);

        if (lines.isEmpty()) return;

        // Calculate dimensions
        int lineHeight = fontRenderer.getFontHeight();
        int maxWidth = 0;
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(line));
        }
        int bgWidth = maxWidth + 16;
        int bgHeight = lines.size() * lineHeight + 8;

        // Background
        if (isShowBackground()) {
            RoundedRect.render(context, x - 4, y - 4, bgWidth + 8, bgHeight + 8,
                (int) getCornerRadius(), getBackgroundColor());
        }

        // Render lines
        int currentY = y;
        for (String line : lines) {
            // Split label and value
            int colonIndex = line.indexOf(": ");
            if (colonIndex > 0) {
                String label = line.substring(0, colonIndex + 1);
                String value = line.substring(colonIndex + 1);
                fontRenderer.draw(context, label, x + 4, currentY, labelColor.getValue());
                fontRenderer.draw(context, value, x + 4 + fontRenderer.getStringWidth(label), currentY, textColor.getValue());
            } else {
                fontRenderer.draw(context, line, x + 4, currentY, textColor.getValue());
            }
            currentY += lineHeight;
        }

        // Border
        if (isShowBorder()) {
            RoundedRect.renderOutline(context, x - 4, y - 4, bgWidth + 8, bgHeight + 8,
                (int) getCornerRadius(), (int) getBorderWidth(), getBorderColor());
        }
    }

    private void updateCache(MinecraftClient mc) {
        updateCounter++;
        if (updateCounter >= updateInterval.getValue()) {
            updateCounter = 0;

            if (showFPS.getValue()) {
                cachedFPS = mc.getFps();
            }
            if (showPing.getValue() && mc.player != null && mc.player.networkHandler != null) {
                cachedPing = mc.player.networkHandler.getLatency();
            }
            if (showTPS.getValue() && mc.player != null && mc.player.getServer() != null) {
                cachedTPS = (int) mc.player.getServer().getAverageTickTime();
            }
            if (showCoords.getValue() && mc.player != null) {
                double px = mc.player.getX();
                double py = mc.player.getY();
                double pz = mc.player.getZ();
                switch (coordFormat.getSelected()) {
                    case "Block" -> cachedCoords = String.format("%d, %d, %d",
                        (int) px, (int) py, (int) pz);
                    case "Chunk" -> cachedCoords = String.format("%d, %d",
                        (int) (px / 16), (int) (pz / 16));
                    case "Both" -> cachedCoords = String.format("%d, %d, %d (%d, %d)",
                        (int) px, (int) py, (int) pz, (int) (px / 16), (int) (pz / 16));
                }
            }
            if (showBiome.getValue() && mc.player != null && mc.world != null) {
                Biome biome = mc.world.getBiome(mc.player.getBlockPos());
                cachedBiome = biome.getName().getString();
            }
            if (showDirection.getValue() && mc.player != null) {
                float yaw = mc.player.getYaw();
                String[] dirs = {"S", "SW", "W", "NW", "N", "NE", "E", "SE", "S"};
                int index = Math.floorMod((int) (yaw / 45 + 4), 8);
                cachedDirection = dirs[index];
            }
            if (showTime.getValue() && mc.world != null) {
                long time = mc.world.getTimeOfDay() % 24000;
                cachedTime = String.format("%02d:%02d", time / 1000, (time % 1000) * 60 / 1000);
            }
        }
    }

    @Override
    public int getPreferredWidth(FontRenderer fontRenderer) {
        int maxWidth = 0;
        if (showFPS.getValue()) maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth("FPS: 999"));
        if (showPing.getValue()) maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth("Ping: 999ms"));
        if (showTPS.getValue()) maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth("TPS: 20.0"));
        if (showCoords.getValue()) maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth("XYZ: -9999, 999, -9999"));
        if (showBiome.getValue()) maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth("Biome: VeryLongBiomeName"));
        if (showDirection.getValue()) maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth("Dir: NW"));
        if (showTime.getValue()) maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth("Time: 23:59"));
        return maxWidth + 16;
    }

    @Override
    public int getPreferredHeight(FontRenderer fontRenderer) {
        int lines = 0;
        if (showFPS.getValue()) lines++;
        if (showPing.getValue()) lines++;
        if (showTPS.getValue()) lines++;
        if (showCoords.getValue()) lines++;
        if (showBiome.getValue()) lines++;
        if (showDirection.getValue()) lines++;
        if (showTime.getValue()) lines++;
        return lines * fontRenderer.getFontHeight() + 8;
    }

    @Override
    public List<Setting<?>> createSettings() {
        List<Setting<?>> settings = new ArrayList<>();
        settings.add(showFPS);
        settings.add(showPing);
        settings.add(showTPS);
        settings.add(showCoords);
        settings.add(coordFormat);
        settings.add(showBiome);
        settings.add(showDirection);
        settings.add(showTime);
        settings.add(updateInterval);
        settings.add(textColor);
        settings.add(labelColor);
        return settings;
    }
}