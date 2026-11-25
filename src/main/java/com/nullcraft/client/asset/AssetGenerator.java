package com.nullcraft.client.asset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Procedural asset generator for icons, logo, and textures.
 * Generates all assets at build time using Java 2D.
 */
public class AssetGenerator {

    private static final String ASSETS_DIR = "src/main/resources/assets/nullcraft";
    private static final String ICONS_DIR = ASSETS_DIR + "/textures/icons";
    private static final String GUI_DIR = ASSETS_DIR + "/textures/gui";

    // Category icons (outline style)
    private static final String[] CATEGORY_ICONS = {
        "combat", "movement", "player", "render", "world", "diagnostics", "misc",
        "hud", "config", "keybinds", "about"
    };

    public static void main(String[] args) {
        System.out.println("[NullCraft] Generating procedural assets...");

        try {
            // Create directories
            new File(ICONS_DIR).mkdirs();
            new File(GUI_DIR).mkdirs();

            // Generate logo
            generateLogo();
            System.out.println("[NullCraft] Generated logo");

            // Generate category icons
            for (String icon : CATEGORY_ICONS) {
                generateCategoryIcon(icon);
            }
            System.out.println("[NullCraft] Generated category icons");

            // Generate module icon templates
            generateModuleIconTemplate();
            System.out.println("[NullCraft] Generated module icon template");

            // Generate GUI textures
            generateGuiTextures();
            System.out.println("[NullCraft] Generated GUI textures");

            System.out.println("[NullCraft] Asset generation complete!");
        } catch (Exception e) {
            System.err.println("[NullCraft] Asset generation failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    // ============================================================
    // LOGO GENERATION
    // ============================================================

    private static void generateLogo() throws IOException {
        int size = 256;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        setupGraphics(g2d);

        // Background circle with gradient
        GradientPaint bgGradient = new GradientPaint(
            0, 0, new Color(0xFF1A1A2E),
            size, size, new Color(0xFF0D0D12)
        );
        g2d.setPaint(bgGradient);
        g2d.fill(new Ellipse2D.Float(4, 4, size - 8, size - 8));

        // Outer glow ring
        for (int i = 0; i < 8; i++) {
            float alpha = 0.15f * (1f - i / 8f);
            g2d.setColor(new Color(0x6B46C1, true));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setStroke(new BasicStroke(2 + i));
            g2d.draw(new Ellipse2D.Float(8 + i * 2, 8 + i * 2, size - 16 - i * 4, size - 16 - i * 4));
        }

        // Main "N" letter
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.setFont(new Font("Arial", Font.BOLD, 160));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "N";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        int x = (size - textWidth) / 2;
        int y = (size + textHeight) / 2 - 8;

        // N with gradient fill
        GradientPaint textGradient = new GradientPaint(
            x, y - textHeight, new Color(0xFF8B5CF6),
            x + textWidth, y, new Color(0xFF6B46C1)
        );
        g2d.setPaint(textGradient);
        g2d.drawString(text, x, y);

        // Inner highlight
        g2d.setColor(new Color(0xFFFFFFFF, true));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2d.drawString(text, x - 1, y - 1);

        g2d.dispose();
        ImageIO.write(img, "PNG", new File(ASSETS_DIR + "/logo.png"));
    }

    // ============================================================
    // CATEGORY ICONS
    // ============================================================

    private static void generateCategoryIcon(String name) throws IOException {
        int size = 64;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        setupGraphics(g2d);

        // Background
        g2d.setColor(new Color(0x00000000, true));
        g2d.fillRect(0, 0, size, size);

        // Draw icon based on category
        int center = size / 2;
        int iconSize = 32;
        int stroke = 3;

        g2d.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(0xFF8B5CF6));

        switch (name) {
            case "combat" -> drawSwordIcon(g2d, center, center, iconSize);
            case "movement" -> drawMovementIcon(g2d, center, center, iconSize);
            case "player" -> drawPlayerIcon(g2d, center, center, iconSize);
            case "render" -> drawRenderIcon(g2d, center, center, iconSize);
            case "world" -> drawWorldIcon(g2d, center, center, iconSize);
            case "diagnostics" -> drawDiagnosticsIcon(g2d, center, center, iconSize);
            case "misc" -> drawMiscIcon(g2d, center, center, iconSize);
            case "hud" -> drawHudIcon(g2d, center, center, iconSize);
            case "config" -> drawConfigIcon(g2d, center, center, iconSize);
            case "keybinds" -> drawKeybindsIcon(g2d, center, center, iconSize);
            case "about" -> drawAboutIcon(g2d, center, center, iconSize);
        }

        g2d.dispose();
        ImageIO.write(img, "PNG", new File(ICONS_DIR + "/" + name + ".png"));
    }

    private static void drawSwordIcon(Graphics2D g, int cx, int cy, int size) {
        int half = size / 2;
        // Crossed swords
        g.drawLine(cx - half, cy + half, cx + half, cy - half); // Sword 1
        g.drawLine(cx + half, cy + half, cx - half, cy - half); // Sword 2
        // Hilts
        g.drawLine(cx - half, cy + half, cx - half + 6, cy + half - 6);
        g.drawLine(cx + half, cy + half, cx + half - 6, cy + half - 6);
        g.drawLine(cx - half, cy - half, cx - half + 6, cy - half + 6);
        g.drawLine(cx + half, cy - half, cx + half - 6, cy - half + 6);
    }

    private static void drawMovementIcon(Graphics2D g, int cx, int cy, int size) {
        int half = size / 2;
        // Arrow pointing right/up
        int[] xPoints = {cx - half, cx + half, cx - half, cx - half / 2};
        int[] yPoints = {cy + half, cy, cy - half, cy};
        g.drawPolygon(new Polygon(xPoints, yPoints, 4));
        // Trail lines
        g.drawLine(cx - half + 4, cy + half - 4, cx - half + 12, cy + half - 4);
        g.drawLine(cx - half + 4, cy - half + 4, cx - half + 12, cy - half + 4);
    }

    private static void drawPlayerIcon(Graphics2D g, int cx, int cy, int size) {
        int half = size / 2;
        // Head
        g.drawOval(cx - 6, cy - half, 12, 12);
        // Body
        g.drawLine(cx, cy - half + 12, cx, cy + half - 4);
        // Arms
        g.drawLine(cx, cy - half + 16, cx - 10, cy - half + 4);
        g.drawLine(cx, cy - half + 16, cx + 10, cy - half + 4);
        // Legs
        g.drawLine(cx, cy + half - 4, cx - 8, cy + half + 8);
        g.drawLine(cx, cy + half - 4, cx + 8, cy + half + 8);
    }

    private static void drawRenderIcon(Graphics2D g, int cx, int cy, int size) {
        int half = size / 2;
        // Diamond shape
        int[] xPoints = {cx, cx + half, cx, cx - half};
        int[] yPoints = {cy - half, cy, cy + half, cy};
        g.drawPolygon(new Polygon(xPoints, yPoints, 4));
        // Inner diamond
        int innerHalf = half / 2;
        int[] ixPoints = {cx, cx + innerHalf, cx, cx - innerHalf};
        int[] iyPoints = {cy - innerHalf, cy, cy + innerHalf, cy};
        g.drawPolygon(new Polygon(ixPoints, iyPoints, 4));
        // Cross lines
        g.drawLine(cx, cy - half, cx, cy + half);
        g.drawLine(cx - half, cy, cx + half, cy);
    }

    private static void drawWorldIcon(Graphics2D g, int cx, int cy, int size) {
        int half = size / 2;
        // Globe circle
        g.drawOval(cx - half, cy - half, size, size);
        // Meridian
        g.drawLine(cx, cy - half, cx, cy + half);
        // Equator
        g.drawLine(cx - half, cy, cx + half, cy);
        // Tilted line
        g.drawLine(cx - half, cy - half / 2, cx + half, cy + half / 2);
    }

    private static void drawDiagnosticsIcon(Graphics2D g, int cx, int cy, int size) {
        int half = size / 2;
        // Gear/cog outline
        int teeth = 8;
        for (int i = 0; i < teeth; i++) {
            double angle = Math.toRadians(i * 360.0 / teeth);
            double angle2 = Math.toRadians((i + 0.5) * 360.0 / teeth);
            int x1 = cx + (int) (half * Math.cos(angle));
            int y1 = cy + (int) (half * Math.sin(angle));
            int x2 = cx + (int) ((half - 6) * Math.cos(angle2));
            int y2 = cy + (int) ((half - 6) * Math.sin(angle2));
            g.drawLine(x1, y1, x2, y2);
        }
        // Inner circle
        g.drawOval(cx - half + 8, cy - half + 8, (half - 8) * 2, (half - 8) * 2);
    }

    private static void drawMiscIcon(Graphics2D g, int cx, int cy, int size) {
        int half = size / 2;
        // Three dots
        int dotSize = 6;
        int spacing = 10;
        for (int i = 0; i < 3; i++) {
            int x = cx - spacing + i * spacing;
            g.fillOval(x - dotSize / 2, cy - dotSize / 2, dotSize, dotSize);
        }
    }

    private static void drawHudIcon(Graphics2D g, int cx, int cy, int size) {
        int half = size / 2;
        // Square with corners
        int rectSize = half * 2 - 8;
        g.drawRect(cx - rectSize / 2, cy - rectSize / 2, rectSize, rectSize);
        // Corner accents
        int cornerSize = 8;
        g.drawLine(cx - rectSize / 2, cy - rectSize / 2, cx - rectSize / 2 + cornerSize, cy - rectSize / 2);
        g.drawLine(cx - rectSize / 2, cy - rectSize / 2, cx - rectSize / 2, cy - rectSize / 2 + cornerSize);
        g.drawLine(cx + rectSize / 2, cy - rectSize / 2, cx + rectSize / 2 - cornerSize, cy - rectSize / 2);
        g.drawLine(cx + rectSize / 2, cy - rectSize / 2, cx + rectSize / 2, cy - rectSize / 2 + cornerSize);
        g.drawLine(cx - rectSize / 2, cy + rectSize / 2, cx - rectSize / 2 + cornerSize, cy + rectSize / 2);
        g.drawLine(cx - rectSize / 2, cy + rectSize / 2, cx - rectSize / 2, cy + rectSize / 2 - cornerSize);
        g.drawLine(cx + rectSize / 2, cy + rectSize / 2, cx + rectSize / 2 - cornerSize, cy + rectSize / 2);
        g.drawLine(cx + rectSize / 2, cy + rectSize / 2, cx + rectSize / 2, cy + rectSize / 2 - cornerSize);
    }

    private static void drawConfigIcon(Graphics2D g, int cx, int cy, int size) {
        int half = size / 2;
        // Three horizontal lines (hamburger menu)
        int lineWidth = half * 2 - 8;
        int lineHeight = 3;
        int spacing = 8;
        for (int i = 0; i < 3; i++) {
            int y = cy - spacing + i * spacing;
            g.fillRect(cx - lineWidth / 2, y - lineHeight / 2, lineWidth, lineHeight);
        }
    }

    private static void drawKeybindsIcon(Graphics2D g, int cx, int cy, int size) {
        int half = size / 2;
        // Keyboard outline
        int kbWidth = half * 2 - 4;
        int kbHeight = half * 2 - 12;
        g.drawRoundRect(cx - kbWidth / 2, cy - kbHeight / 2, kbWidth, kbHeight, 4, 4);
        // Keys
        int keySize = 6;
        int keySpacing = 8;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                int x = cx - kbWidth / 2 + 6 + col * keySpacing;
                int y = cy - kbHeight / 2 + 6 + row * keySpacing;
                g.drawRect(x, y, keySize, keySize);
            }
        }
    }

    private static void drawAboutIcon(Graphics2D g, int cx, int cy, int size) {
        int half = size / 2;
        // Circle with "i"
        g.drawOval(cx - half, cy - half, size, size);
        // i
        g.drawLine(cx, cy - half + 6, cx, cy + half - 6);
        g.fillOval(cx - 2, cy + half - 10, 4, 4);
    }

    // ============================================================
    // MODULE ICON TEMPLATE
    // ============================================================

    private static void generateModuleIconTemplate() throws IOException {
        int size = 32;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        setupGraphics(g2d);

        g2d.setColor(new Color(0x00000000, true));
        g2d.fillRect(0, 0, size, size);

        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(0xFF8B5CF6));

        // Generic module icon - cube
        int center = size / 2;
        int cubeSize = 12;
        // Isometric cube
        int[] xPoints = {center, center + cubeSize, center + cubeSize, center, center - cubeSize, center - cubeSize};
        int[] yPoints = {center - cubeSize, center - cubeSize / 2, center + cubeSize / 2, center + cubeSize, center + cubeSize / 2, center - cubeSize / 2};
        g2d.drawPolygon(new Polygon(xPoints, yPoints, 6));

        g2d.dispose();
        ImageIO.write(img, "PNG", new File(ICONS_DIR + "/module_default.png"));
    }

    // ============================================================
    // GUI TEXTURES
    // ============================================================

    private static void generateGuiTextures() throws IOException {
        // Panel background texture
        generatePanelTexture();
        // Slider track texture
        generateSliderTexture();
        // Checkbox texture
        generateCheckboxTexture();
    }

    private static void generatePanelTexture() throws IOException {
        int w = 64, h = 64;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        setupGraphics(g2d);

        // Subtle noise pattern
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                float noise = (float) Math.random() * 0.05f;
                int val = (int) (30 + noise * 255);
                int color = (val << 24) | (val << 16) | (val << 8) | val;
                img.setRGB(x, y, color);
            }
        }

        g2d.dispose();
        ImageIO.write(img, "PNG", new File(GUI_DIR + "/panel_noise.png"));
    }

    private static void generateSliderTexture() throws IOException {
        int w = 32, h = 8;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        setupGraphics(g2d);

        // Track
        g2d.setColor(new Color(0xFF2A2A4A));
        g2d.fillRoundRect(0, 0, w, h, h, h);

        g2d.dispose();
        ImageIO.write(img, "PNG", new File(GUI_DIR + "/slider_track.png"));
    }

    private static void generateCheckboxTexture() throws IOException {
        int size = 24;
        BufferedImage img = new BufferedImage(size * 2, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        setupGraphics(g2d);

        // Unchecked
        g2d.setColor(new Color(0xFF2A2A4A));
        g2d.fillRoundRect(2, 2, size - 4, size - 4, 6, 6);
        g2d.setColor(new Color(0xFF6B46C1));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(2, 2, size - 4, size - 4, 6, 6);

        // Checked
        int cx = size;
        g2d.setColor(new Color(0xFF6B46C1));
        g2d.fillRoundRect(cx + 2, 2, size - 4, size - 4, 6, 6);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(cx + 6, cx + size / 2, cx + 10, cx + size - 6);
        g2d.drawLine(cx + 10, cx + size - 6, cx + size - 4, cx + 4);

        g2d.dispose();
        ImageIO.write(img, "PNG", new File(GUI_DIR + "/checkbox.png"));
    }

    // ============================================================
    // UTILITIES
    // ============================================================

    private static void setupGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }
}