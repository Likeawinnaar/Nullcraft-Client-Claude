package com.nullcraft.client.gui.hud;

import com.nullcraft.client.core.settings.*;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.ColorUtils;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.UUID;

/**
 * TargetHUD HUD element: Shows target avatar, name, health, distance, animated health bar
 */
public class TargetHud extends HudElement {

    private BooleanSetting showAvatar = new BooleanSetting("Show Avatar", "Display player avatar", true);
    private BooleanSetting showName = new BooleanSetting("Show Name", "Display target name", true);
    private BooleanSetting showHealth = new BooleanSetting("Show Health", "Display health bar and value", true);
    private BooleanSetting showDistance = new BooleanSetting("Show Distance", "Display distance to target", true);
    private BooleanSetting showArmor = new BooleanSetting("Show Armor", "Display armor durability", false);
    private BooleanSetting showPing = new BooleanSetting("Show Ping", "Display target ping (players only)", false);
    private ModeSetting healthStyle = new ModeSetting("Health Style", "Health bar style",
        List.of("Bar", "Heart", "Both", "Numeric"), 0);
    private BooleanSetting animateHealth = new BooleanSetting("Animate Health", "Smooth health bar animation", true);
    private IntegerSetting maxDistance = new IntegerSetting("Max Distance", "Maximum distance to show target", 50, 5, 200);
    private ColorSetting healthColor = new ColorSetting("Health Color", "Color for health bar", DesignTokens.COLOR_ACCENT_GREEN, false);
    private ColorSetting lowHealthColor = new ColorSetting("Low Health Color", "Color for low health (<30%)", DesignTokens.COLOR_ACCENT_RED, false);
    private ColorSetting nameColor = new ColorSetting("Name Color", "Color for target name", DesignTokens.COLOR_PRIMARY_TEXT, false);
    private ColorSetting backgroundColor = new ColorSetting("Background Color", "Background color", DesignTokens.COLOR_PANEL_BACKGROUND, true);

    // Target tracking
    private LivingEntity currentTarget = null;
    private double animatedHealth = 0;
    private double targetHealth = 0;
    private long lastTargetUpdate = 0;
    private UUID targetUuid = null;

    public TargetHud() {
        super("targethud", "TargetHUD", "Shows target avatar, name, health, distance");
        // Default position: center-bottom
        setX(0.5);
        setY(0.7);
        setUseNormalizedCoords(true);
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta,
                       int x, int y, int width, int height) {
        if (!isVisible()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        updateTarget(mc);

        if (currentTarget == null || !currentTarget.isAlive()) {
            // Fade out animation
            if (animatedHealth > 0) {
                animatedHealth = MathHelper.lerp(tickDelta * 0.1f, animatedHealth, 0);
                renderTarget(context, fontRenderer, x, y, true);
            }
            return;
        }

        // Animate health
        float healthPercent = currentTarget.getHealth() / currentTarget.getMaxHealth();
        targetHealth = healthPercent;
        if (animateHealth.getValue()) {
            animatedHealth = MathHelper.lerp(tickDelta * 0.15f, animatedHealth, healthPercent);
        } else {
            animatedHealth = healthPercent;
        }

        renderTarget(context, fontRenderer, x, y, false);
    }

    private void renderTarget(DrawContext context, FontRenderer fontRenderer,
                              int x, int y, boolean fading) {
        FontRenderer boldFont = getBoldFont();

        int avatarSize = 32;
        int padding = 8;
        int nameWidth = showName.getValue() ? boldFont.getStringWidth(currentTarget.getName().getString()) : 0;
        int healthWidth = showHealth.getValue() ? 120 : 0;
        int distanceWidth = showDistance.getValue() ? fontRenderer.getStringWidth("999.9m") : 0;

        int totalWidth = avatarSize + padding + Math.max(nameWidth, healthWidth + distanceWidth + 10);
        int totalHeight = Math.max(avatarSize, boldFont.getFontHeight() + (showHealth.getValue() ? 20 : 0) + 4);

        // Background
        int bgAlpha = fading ? (int) (255 * animatedHealth) : 255;
        int bgColor = (bgAlpha << 24) | (backgroundColor.getValue() & 0x00FFFFFF);
        RoundedRect.render(context, x - 4, y - 4, totalWidth + 8, totalHeight + 8,
            (int) getCornerRadius(), bgColor);

        int currentX = x;
        int currentY = y + (totalHeight - avatarSize) / 2;

        // Avatar (simplified - would render player head)
        if (showAvatar.getValue()) {
            // Draw avatar placeholder
            RoundedRect.render(context, currentX, currentY, avatarSize, avatarSize,
                DesignTokens.RADIUS_SMALL, DesignTokens.COLOR_PRIMARY_PURPLE);
            boldFont.drawCentered(context, currentTarget.getName().getString().substring(0, 1).toUpperCase(),
                currentX + avatarSize / 2, currentY + avatarSize / 2 - boldFont.getFontHeight() / 2, 0xFFFFFFFF);
            currentX += avatarSize + padding;
        }

        // Info area
        int infoY = currentY;

        // Name
        if (showName.getValue()) {
            String name = currentTarget.getName().getString();
            int nameColorVal = fading ? applyAlpha(nameColor.getValue(), (int) (255 * animatedHealth)) : nameColor.getValue();
            boldFont.draw(context, name, currentX, infoY, nameColorVal);
            infoY += boldFont.getFontHeight() + 2;
        }

        // Health
        if (showHealth.getValue()) {
            renderHealthBar(context, fontRenderer, currentX, infoY, healthWidth, fading);
            infoY += 18;
        }

        // Distance
        if (showDistance.getValue() && mc.player != null) {
            double dist = mc.player.distanceTo(currentTarget);
            if (dist <= maxDistance.getValue()) {
                String distStr = String.format("%.1fm", dist);
                int distColor = fading ? applyAlpha(DesignTokens.COLOR_MUTED_TEXT, (int) (255 * animatedHealth)) : DesignTokens.COLOR_MUTED_TEXT;
                fontRenderer.draw(context, distStr, currentX, infoY, distColor);
            }
        }

        // Border
        if (isShowBorder()) {
            RoundedRect.renderOutline(context, x - 4, y - 4, totalWidth + 8, totalHeight + 8,
                (int) getCornerRadius(), (int) getBorderWidth(), getBorderColor());
        }
    }

    private void renderHealthBar(DrawContext context, FontRenderer fontRenderer,
                                 int x, int y, int width, boolean fading) {
        float healthPercent = (float) animatedHealth;
        int barHeight = 6;
        int barWidth = width;

        // Background
        int bgColor = fading ? applyAlpha(0xFF333333, (int) (255 * animatedHealth)) : 0xFF333333;
        RoundedRect.render(context, x, y, barWidth, barHeight, barHeight / 2, bgColor);

        // Health bar
        int fillWidth = (int) (barWidth * healthPercent);
        if (fillWidth > 0) {
            int healthColorVal = healthPercent > 0.3f ? healthColor.getValue() : lowHealthColor.getValue();
            if (fading) {
                healthColorVal = applyAlpha(healthColorVal, (int) (255 * animatedHealth));
            }
            RoundedRect.render(context, x, y, fillWidth, barHeight, barHeight / 2, healthColorVal);
        }

        // Health text
        if (healthStyle.getSelected().equals("Numeric") || healthStyle.getSelected().equals("Both")) {
            String healthText = String.format("%.0f / %.0f",
                currentTarget.getHealth(), currentTarget.getMaxHealth());
            int textColor = fading ? applyAlpha(DesignTokens.COLOR_PRIMARY_TEXT, (int) (255 * animatedHealth)) : DesignTokens.COLOR_PRIMARY_TEXT;
            fontRenderer.draw(context, healthText, x + barWidth + 6, y - 1, textColor);
        }
    }

    private int applyAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 0x00FFFFFF);
    }

    private void updateTarget(MinecraftClient mc) {
        if (mc.player == null) {
            currentTarget = null;
            return;
        }

        // Get targeted entity (crosshair target)
        var hitResult = mc.crosshairTarget;
        if (hitResult != null && hitResult.getType() == net.minecraft.util.hit.HitResult.Type.ENTITY) {
            var entityHit = (net.minecraft.util.hit.EntityHitResult) hitResult;
            if (entityHit.getEntity() instanceof LivingEntity living) {
                if (living != mc.player && living.isAlive()) {
                    double dist = mc.player.distanceTo(living);
                    if (dist <= maxDistance.getValue()) {
                        currentTarget = living;
                        targetUuid = living.getUuid();
                        lastTargetUpdate = System.currentTimeMillis();
                        return;
                    }
                }
            }
        }

        // Clear target if too far or dead
        if (currentTarget != null) {
            double dist = mc.player.distanceTo(currentTarget);
            if (dist > maxDistance.getValue() || !currentTarget.isAlive() ||
                System.currentTimeMillis() - lastTargetUpdate > 2000) {
                currentTarget = null;
            }
        }
    }

    @Override
    public int getPreferredWidth(FontRenderer fontRenderer) {
        FontRenderer boldFont = getBoldFont();
        int avatarSize = 32;
        int padding = 8;
        int nameWidth = boldFont.getStringWidth("PlayerName");
        int healthWidth = 120;
        int distanceWidth = fontRenderer.getStringWidth("999.9m");
        return avatarSize + padding + Math.max(nameWidth, healthWidth + distanceWidth + 10);
    }

    @Override
    public int getPreferredHeight(FontRenderer fontRenderer) {
        FontRenderer boldFont = getBoldFont();
        int avatarSize = 32;
        return Math.max(avatarSize, boldFont.getFontHeight() + 20 + 4);
    }

    @Override
    public List<Setting<?>> createSettings() {
        List<Setting<?>> settings = new ArrayList<>();
        settings.add(showAvatar);
        settings.add(showName);
        settings.add(showHealth);
        settings.add(healthStyle);
        settings.add(animateHealth);
        settings.add(showDistance);
        settings.add(showArmor);
        settings.add(showPing);
        settings.add(maxDistance);
        settings.add(healthColor);
        settings.add(lowHealthColor);
        settings.add(nameColor);
        settings.add(backgroundColor);
        return settings;
    }

    private FontRenderer getBoldFont() {
        return FontRenderer.class.cast(null); // Placeholder
    }
}