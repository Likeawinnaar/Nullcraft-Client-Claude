package com.nullcraft.client.gui.hud;

import com.nullcraft.client.core.config.ConfigManager;
import com.nullcraft.client.core.event.EventBus;
import com.nullcraft.client.core.event.RenderEvent;
import com.nullcraft.client.gui.font.FontManager;
import com.nullcraft.client.gui.font.FontRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * HUD Manager: Manages all HUD elements, handles rendering, persistence, and layout.
 */
public class HudManager {

    private static HudManager instance;
    private final Map<String, HudElement> elements = new HashMap<>();
    private final List<HudElement> renderOrder = new ArrayList<>();
    private final ConfigManager configManager;

    private HudElement draggedElement = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private boolean editorMode = false;
    private double gridSize = 10.0;

    private HudManager(ConfigManager configManager) {
        this.configManager = configManager;
        registerDefaultElements();
        loadLayouts();
    }

    public static HudManager getInstance() {
        return instance;
    }

    public static void initialize(ConfigManager configManager) {
        if (instance == null) {
            instance = new HudManager(configManager);
        }
    }

    private void registerDefaultElements() {
        // Register built-in HUD elements
        registerElement(new WatermarkHud());
        registerElement(new ArrayListHud());
        registerElement(new InfoHud());
        registerElement(new KeystrokesHud());
        registerElement(new TargetHud());
    }

    public void registerElement(HudElement element) {
        elements.put(element.getId(), element);
        if (!renderOrder.contains(element)) {
            renderOrder.add(element);
        }
    }

    public void unregisterElement(String id) {
        HudElement element = elements.remove(id);
        if (element != null) {
            renderOrder.remove(element);
        }
    }

    public HudElement getElement(String id) {
        return elements.get(id);
    }

    public List<HudElement> getAllElements() {
        return new ArrayList<>(renderOrder);
    }

    public List<HudElement> getVisibleElements() {
        return renderOrder.stream().filter(HudElement::isVisible).toList();
    }

    // ============================================================
    // RENDERING
    // ============================================================

    public void onRender(float tickDelta) {
        if (!net.minecraft.client.MinecraftClient.getInstance().world.isClient()) return;

        FontRenderer fontRenderer = FontManager.getInstance().getRegular();
        net.minecraft.client.gui.DrawContext context = net.minecraft.client.MinecraftClient.getInstance()
            .inGameHud.getDrawContext();

        int screenWidth = net.minecraft.client.MinecraftClient.getInstance().getWindow().getScaledWidth();
        int screenHeight = net.minecraft.client.MinecraftClient.getInstance().getWindow().getScaledHeight();

        for (HudElement element : renderOrder) {
            if (!element.isVisible()) continue;

            int x = element.getScreenX(screenWidth);
            int y = element.getScreenY(screenHeight);
            int width = element.getPreferredWidth(fontRenderer);
            int height = element.getPreferredHeight(fontRenderer);

            // Apply scale
            int scaledWidth = (int) (width * element.getScale());
            int scaledHeight = (int) (height * element.getScale());

            // Apply opacity via color modification
            element.updateAnimation(tickDelta);
            element.render(context, fontRenderer, tickDelta, x, y, scaledWidth, scaledHeight);
        }

        // Render drag preview in editor mode
        if (editorMode && draggedElement != null) {
            renderDragPreview(context, fontRenderer, screenWidth, screenHeight);
        }
    }

    private void renderDragPreview(net.minecraft.client.gui.DrawContext context,
                                   FontRenderer fontRenderer, int screenWidth, int screenHeight) {
        // Render ghost preview at mouse position
    }

    // ============================================================
    // EDITOR MODE
    // ============================================================

    public void setEditorMode(boolean enabled) {
        this.editorMode = enabled;
        if (!enabled) {
            draggedElement = null;
        }
    }

    public boolean isEditorMode() {
        return editorMode;
    }

    public void startDrag(HudElement element, int mouseX, int mouseY, int screenWidth, int screenHeight) {
        if (element.isLocked()) return;
        this.draggedElement = element;
        this.dragOffsetX = mouseX - element.getScreenX(screenWidth);
        this.dragOffsetY = mouseY - element.getScreenY(screenHeight);
    }

    public void updateDrag(int mouseX, int mouseY, int screenWidth, int screenHeight) {
        if (draggedElement == null) return;

        int newX = mouseX - dragOffsetX;
        int newY = mouseY - dragOffsetY;

        // Snap to grid
        if (gridSize > 0) {
            newX = (int) Math.round(newX / gridSize) * (int) gridSize;
            newY = (int) Math.round(newY / gridSize) * (int) gridSize;
        }

        // Clamp to screen
        newX = Math.max(0, Math.min(screenWidth - 10, newX));
        newY = Math.max(0, Math.min(screenHeight - 10, newY));

        draggedElement.setScreenPosition(newX, newY, screenWidth, screenHeight);
    }

    public void endDrag() {
        if (draggedElement != null) {
            saveLayouts();
        }
        draggedElement = null;
    }

    public HudElement getElementAt(int mouseX, int mouseY, int screenWidth, int screenHeight) {
        FontRenderer fontRenderer = FontManager.getInstance().getRegular();

        for (HudElement element : renderOrder) {
            if (!element.isVisible() || element.isLocked()) continue;

            int x = element.getScreenX(screenWidth);
            int y = element.getScreenY(screenHeight);
            int width = element.getPreferredWidth(fontRenderer);
            int height = element.getPreferredHeight(fontRenderer);

            if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
                return element;
            }
        }
        return null;
    }

    public void setGridSize(double size) {
        this.gridSize = Math.max(0, size);
    }

    public double getGridSize() {
        return gridSize;
    }

    // ============================================================
    // LAYOUT PERSISTENCE
    // ============================================================

    public void saveLayouts() {
        com.google.gson.JsonObject layoutJson = new com.google.gson.JsonObject();
        com.google.gson.JsonArray elementsArray = new com.google.gson.JsonArray();

        for (HudElement element : renderOrder) {
            elementsArray.add(element.toJson());
        }

        layoutJson.add("elements", elementsArray);
        layoutJson.addProperty("version", 1);

        configManager.getHudConfig().saveLayout("default", layoutJson);
    }

    public void loadLayouts() {
        var layoutJson = configManager.getHudConfig().loadLayout("default");
        if (layoutJson == null) return;

        if (layoutJson.has("elements")) {
            com.google.gson.JsonArray elementsArray = layoutJson.getAsJsonArray("elements");
            for (var elemJson : elementsArray) {
                com.google.gson.JsonObject obj = elemJson.getAsJsonObject();
                String id = obj.get("id").getAsString();
                HudElement element = elements.get(id);
                if (element != null) {
                    element.fromJson(obj);
                }
            }
        }
    }

    public void duplicateLayout(String sourceName, String newName) {
        var sourceJson = configManager.getHudConfig().loadLayout(sourceName);
        if (sourceJson != null) {
            configManager.getHudConfig().saveLayout(newName, sourceJson);
        }
    }

    public void deleteLayout(String name) {
        configManager.getHudConfig().deleteLayout(name);
    }

    public List<String> getLayoutNames() {
        return configManager.getHudConfig().getLayoutNames();
    }

    // ============================================================
    // PRESETS
    // ============================================================

    public void applyPreset(String presetName) {
        switch (presetName.toLowerCase()) {
            case "minimal" -> applyMinimalPreset();
            case "full" -> applyFullPreset();
            case "pvp" -> applyPvPPreset();
            case "builder" -> applyBuilderPreset();
        }
        saveLayouts();
    }

    private void applyMinimalPreset() {
        getElement("watermark").setVisible(true);
        getElement("arraylist").setVisible(false);
        getElement("infohud").setVisible(true);
        getElement("keystrokes").setVisible(false);
        getElement("targethud").setVisible(false);
    }

    private void applyFullPreset() {
        getElement("watermark").setVisible(true);
        getElement("arraylist").setVisible(true);
        getElement("infohud").setVisible(true);
        getElement("keystrokes").setVisible(true);
        getElement("targethud").setVisible(true);
    }

    private void applyPvPPreset() {
        getElement("watermark").setVisible(true);
        getElement("arraylist").setVisible(true);
        getElement("infohud").setVisible(true);
        getElement("keystrokes").setVisible(true);
        getElement("targethud").setVisible(true);

        // Position for PvP
        getElement("watermark").setScreenPosition(10, 10, 1920, 1080);
        getElement("arraylist").setScreenPosition(1800, 50, 1920, 1080);
        getElement("infohud").setScreenPosition(10, 50, 1920, 1080);
        getElement("keystrokes").setScreenPosition(960, 900, 1920, 1080);
        getElement("targethud").setScreenPosition(960, 700, 1920, 1080);
    }

    private void applyBuilderPreset() {
        getElement("watermark").setVisible(true);
        getElement("arraylist").setVisible(false);
        getElement("infohud").setVisible(true);
        getElement("keystrokes").setVisible(false);
        getElement("targethud").setVisible(false);

        // Position for building
        getElement("infohud").setScreenPosition(10, 10, 1920, 1080);
    }

    // ============================================================
    // IMPORT/EXPORT
    // ============================================================

    public String exportLayout(String layoutName) {
        var json = configManager.getHudConfig().loadLayout(layoutName);
        return json != null ? json.toString() : null;
    }

    public boolean importLayout(String layoutName, String jsonString) {
        try {
            com.google.gson.JsonObject json = com.google.gson.JsonParser.parseString(jsonString).getAsJsonObject();
            configManager.getHudConfig().saveLayout(layoutName, json);
            loadLayouts();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}