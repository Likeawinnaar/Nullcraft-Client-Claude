package com.nullcraft.client.gui.components;

import com.nullcraft.client.core.utils.DesignTokens;
import com.nullcraft.client.gui.font.FontRenderer;
import com.nullcraft.client.gui.render.RoundedRect;
import net.minecraft.client.gui.DrawContext;

import java.util.List;
import java.util.function.Consumer;

/**
 * Dropdown component for mode settings.
 */
public class Dropdown extends Component {

    private final String label;
    private final List<String> options;
    private int selectedIndex = 0;
    private final Consumer<Integer> onChange;
    private boolean expanded = false;
    private int maxVisibleOptions = 6;

    private float expandProgress = 0f;

    public Dropdown(int x, int y, int width, int height, String label, List<String> options, int initialIndex, Consumer<Integer> onChange) {
        super(x, y, width, height);
        this.label = label;
        this.options = options;
        this.selectedIndex = Math.max(0, Math.min(options.size() - 1, initialIndex));
        this.onChange = onChange;
    }

    public String getSelected() {
        return options.get(selectedIndex);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int index) {
        this.selectedIndex = Math.max(0, Math.min(options.size() - 1, index));
        if (onChange != null) onChange.accept(selectedIndex);
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public void updateAnimations() {
        super.updateAnimations();

        float targetExpand = expanded ? 1f : 0f;
        expandProgress = net.minecraft.util.math.MathHelper.lerp(0.2f, expandProgress, targetExpand);
    }

    @Override
    public boolean onMouseClick(int mouseX, int mouseY, int button) {
        if (!enabled || !visible) return false;

        // Check main dropdown area
        if (contains(mouseX, mouseY) && button == 0) {
            expanded = !expanded;
            return true;
        }

        // Check dropdown options
        if (expanded && button == 0) {
            int optionHeight = height;
            int startY = y + height;
            for (int i = 0; i < options.size() && i < maxVisibleOptions; i++) {
                int optY = startY + i * optionHeight;
                if (mouseX >= x && mouseX < x + width && mouseY >= optY && mouseY < optY + optionHeight) {
                    selectedIndex = i;
                    if (onChange != null) onChange.accept(selectedIndex);
                    expanded = false;
                    return true;
                }
            }
        }

        // Click outside closes dropdown
        if (expanded && button == 0) {
            expanded = false;
        }

        return false;
    }

    @Override
    public void render(DrawContext context, FontRenderer fontRenderer, float tickDelta) {
        if (!visible) return;

        updateAnimations();

        // Label
        int labelColor = enabled ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_DISABLED;
        fontRenderer.draw(context, label, x, y, labelColor);

        // Main dropdown box
        int boxX = x;
        int boxY = y + fontRenderer.getFontHeight() + 4;
        int boxWidth = width;
        int boxHeight = height;

        int bgColor = expanded ? DesignTokens.COLOR_HOVER_BACKGROUND : getBackgroundColor();
        RoundedRect.render(context, boxX, boxY, boxWidth, boxHeight, DesignTokens.RADIUS_SMALL, bgColor);
        RoundedRect.renderOutline(context, boxX, boxY, boxWidth, boxHeight, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN, getBorderColor());

        // Selected value
        String selected = options.get(selectedIndex);
        int textColor = enabled ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_DISABLED;
        fontRenderer.draw(context, selected, boxX + 8, boxY + (boxHeight - fontRenderer.getFontHeight()) / 2, textColor);

        // Dropdown arrow
        int arrowX = boxX + boxWidth - 20;
        int arrowY = boxY + boxHeight / 2;
        int arrowColor = enabled ? DesignTokens.COLOR_SECONDARY_TEXT : DesignTokens.COLOR_DISABLED;
        // Simple triangle
        context.fill(arrowX, arrowY - 3, arrowX + 6, arrowY - 2, arrowColor);
        context.fill(arrowX + 1, arrowY - 2, arrowX + 5, arrowY - 1, arrowColor);
        context.fill(arrowX + 2, arrowY - 1, arrowX + 4, arrowY, arrowColor);

        // Expanded options
        if (expandProgress > 0.01f) {
            int optionHeight = boxHeight;
            int maxOptions = Math.min(options.size(), maxVisibleOptions);
            int listHeight = (int) (maxOptions * optionHeight * expandProgress);

            // Option background
            RoundedRect.render(context, boxX, boxY + boxHeight, boxWidth, listHeight, DesignTokens.RADIUS_SMALL, DesignTokens.COLOR_PANEL_BACKGROUND);
            RoundedRect.renderOutline(context, boxX, boxY + boxHeight, boxWidth, listHeight, DesignTokens.RADIUS_SMALL, DesignTokens.BORDER_THIN, DesignTokens.COLOR_BORDER);

            // Clip to list height for options
            // Render options
            for (int i = 0; i < maxOptions; i++) {
                int optY = boxY + boxHeight + i * optionHeight;
                if (optY + optionHeight > boxY + boxHeight + listHeight) break;

                boolean isSelected = i == selectedIndex;
                boolean isHovered = false; // Would need mouse tracking

                int optBgColor = isSelected ? DesignTokens.COLOR_PRIMARY_PURPLE : (isHovered ? DesignTokens.COLOR_HOVER_BACKGROUND : DesignTokens.COLOR_PANEL_BACKGROUND);
                if (isSelected || isHovered) {
                    RoundedRect.render(context, boxX + 1, optY + 1, boxWidth - 2, optionHeight - 2, DesignTokens.RADIUS_SMALL, optBgColor);
                }

                int optTextColor = isSelected ? DesignTokens.COLOR_PRIMARY_TEXT : (enabled ? DesignTokens.COLOR_PRIMARY_TEXT : DesignTokens.COLOR_DISABLED);
                fontRenderer.draw(context, options.get(i), boxX + 8, optY + (optionHeight - fontRenderer.getFontHeight()) / 2, optTextColor);
            }
        }
    }
}