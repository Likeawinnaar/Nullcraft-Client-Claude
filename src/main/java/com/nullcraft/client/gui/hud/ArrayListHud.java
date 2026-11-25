package com.nullcraft.client.gui.hud;

import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleRegistry;
import com.nullcraft.client.core.settings.*;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import com.nullcraft.client.core.utils.ColorUtils;
import com.nullcraft.client.core.utils.DesignTokens;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ArrayList HUD element: Shows enabled modules with keybinds, smooth animations
 */
public class ArrayListHud extends HudElement {

    private BooleanSetting showKeybinds = new BooleanSetting("Show Keybinds", "Display module keybinds", true);
    private BooleanSetting showOnlyEnabled = new BooleanSetting("Show Only Enabled", "Hide disabled modules", true);
    private ModeSetting sortMode = new ModeSetting("Sort Mode", "How to sort modules",
        List.of("Name (A-Z)", "Name (Z-A)", "Category", "Keybind"), 0);
    private IntegerSetting maxModules = new IntegerSetting("Max Modules", "Maximum modules to display", 20, 1, 50);
    private IntegerSetting spacing = new IntegerSetting("Spacing", "Vertical spacing between entries", 2, 0, 10);
    private BooleanSetting animateEntries = new BooleanSetting("Animate Entries", "Smooth entry animations", true);
    private ColorSetting enabledColor = new ColorSetting("Enabled Color", "Color for enabled modules", DesignTokens.COLOR_PRIMARY_TEXT, false);
    private ColorSetting keybindColor = new ColorSetting("Keybind Color", "Color for keybinds", DesignTokens.COLOR_MUTED_TEXT, false);
    private ColorSetting accentColor = new ColorSetting("Accent Color", "Color for accent bar", DesignTokens.COLOR_PRIMARY_PURPLE, false);

    public ArrayListHud() {
        super("arraylist", "ArrayList", "Shows enabled modules with keybinds");
        // Default position: top-right
        setX(0.98);
        setY(0.02);
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta,
                       int x, int y, int width, int height) {
        if (!isVisible()) return;

        List<Module> modules = getModulesToDisplay();
        if (modules.isEmpty()) return;

        FontRenderer boldFont = getBoldFont();
        int currentY = y;
        int maxWidth = 0;

        // Calculate max width
        for (Module module : modules) {
            String name = module.getDisplayName();
            String keybind = "";
            if (showKeybinds.getValue() && module.getKeybind() != null && !module.getKeybind().isNone()) {
                keybind = " [" + module.getKeybind().toShortString() + "]";
            }
            int entryWidth = boldFont.getStringWidth(name + keybind);
            maxWidth = Math.max(maxWidth, entryWidth);
        }

        maxWidth += 16; // Padding + accent bar

        // Background
        int bgHeight = modules.size() * (boldFont.getFontHeight() + spacing.getValue()) + 8;
        if (isShowBackground()) {
            RoundedRect.render(context, x - 4, y - 4, maxWidth + 8, bgHeight + 8,
                (int) getCornerRadius(), getBackgroundColor());
        }

        // Render entries
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            String name = module.getDisplayName();
            String keybind = "";
            if (showKeybinds.getValue() && module.getKeybind() != null && !module.getKeybind().isNone()) {
                keybind = " [" + module.getKeybind().toShortString() + "]";
            }

            int entryY = currentY + i * (boldFont.getFontHeight() + spacing.getValue());

            // Accent bar (animated)
            float animProgress = animateEntries.getValue() ? module.getAnimationProgress() : 1f;
            int barWidth = (int) (4 * animProgress);
            if (barWidth > 0) {
                context.fill(x - 4, entryY, x - 4 + barWidth, entryY + boldFont.getFontHeight(), accentColor.getValue());
            }

            // Module name
            int nameX = x + 4;
            boldFont.draw(context, name, nameX, entryY, enabledColor.getValue());

            // Keybind
            if (!keybind.isEmpty()) {
                int keybindX = x + maxWidth - 8 - boldFont.getStringWidth(keybind);
                fontRenderer.draw(context, keybind, keybindX, entryY, keybindColor.getValue());
            }
        }

        // Border
        if (isShowBorder()) {
            RoundedRect.renderOutline(context, x - 4, y - 4, maxWidth + 8, bgHeight + 8,
                (int) getCornerRadius(), (int) getBorderWidth(), getBorderColor());
        }

        // Update animation targets
        for (Module module : modules) {
            module.setTargetAnimationProgress(1.0);
        }
    }

    private List<Module> getModulesToDisplay() {
        List<Module> modules = ModuleRegistry.getInstance().getAllModules();

        if (showOnlyEnabled.getValue()) {
            modules = modules.stream().filter(Module::isEnabled).collect(Collectors.toList());
        }

        // Sort
        switch (sortMode.getSelected()) {
            case "Name (A-Z)" -> modules.sort(Comparator.comparing(Module::getDisplayName));
            case "Name (Z-A)" -> modules.sort(Comparator.comparing(Module::getDisplayName).reversed());
            case "Category" -> modules.sort(Comparator.comparing(m -> m.getCategory().getDisplayName())
                .thenComparing(Module::getDisplayName));
            case "Keybind" -> modules.sort(Comparator.comparing(m ->
                m.getKeybind() != null ? m.getKeybind().toString() : "~"));
        }

        return modules.stream().limit(maxModules.getValue()).collect(Collectors.toList());
    }

    @Override
    public int getPreferredWidth(FontRenderer fontRenderer) {
        FontRenderer boldFont = getBoldFont();
        int maxWidth = 0;
        for (Module module : getModulesToDisplay()) {
            String name = module.getDisplayName();
            String keybind = "";
            if (showKeybinds.getValue() && module.getKeybind() != null && !module.getKeybind().isNone()) {
                keybind = " [" + module.getKeybind().toShortString() + "]";
            }
            maxWidth = Math.max(maxWidth, boldFont.getStringWidth(name + keybind));
        }
        return maxWidth + 16;
    }

    @Override
    public int getPreferredHeight(FontRenderer fontRenderer) {
        FontRenderer boldFont = getBoldFont();
        int count = Math.min(getModulesToDisplay().size(), maxModules.getValue());
        return count * (boldFont.getFontHeight() + spacing.getValue()) + 8;
    }

    @Override
    public List<Setting<?>> createSettings() {
        List<Setting<?>> settings = new ArrayList<>();
        settings.add(showKeybinds);
        settings.add(showOnlyEnabled);
        settings.add(sortMode);
        settings.add(maxModules);
        settings.add(spacing);
        settings.add(animateEntries);
        settings.add(enabledColor);
        settings.add(keybindColor);
        settings.add(accentColor);
        return settings;
    }

    private FontRenderer getBoldFont() {
        return FontRenderer.class.cast(null); // Placeholder
    }
}