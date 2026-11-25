package com.nullcraft.client.core.keybind;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Objects;

/**
 * Represents a key binding with support for key combinations (modifiers).
 */
public final class Keybind {

    public static final Keybind NONE = new Keybind(0, 0, 0, 0);

    private final int keyCode;
    private final int scanCode;
    private final int modifiers;
    private final int hash;

    public Keybind(int keyCode) {
        this(keyCode, 0, 0, 0);
    }

    public Keybind(int keyCode, int scanCode, int modifiers) {
        this(keyCode, scanCode, modifiers, 0);
    }

    private Keybind(int keyCode, int scanCode, int modifiers, int hash) {
        this.keyCode = keyCode;
        this.scanCode = scanCode;
        this.modifiers = modifiers;
        this.hash = hash != 0 ? hash : computeHash();
    }

    private int computeHash() {
        return Objects.hash(keyCode, scanCode, modifiers);
    }

    // ============================================================
    // FACTORY METHODS
    // ============================================================

    public static Keybind of(int keyCode) {
        return new Keybind(keyCode);
    }

    public static Keybind of(int keyCode, int scanCode, int modifiers) {
        return new Keybind(keyCode, scanCode, modifiers);
    }

    public static Keybind fromString(String string) {
        if (string == null || string.isEmpty() || string.equalsIgnoreCase("NONE")) {
            return NONE;
        }

        String[] parts = string.split("\\+");
        int keyCode = 0;
        int scanCode = 0;
        int modifiers = 0;

        for (String part : parts) {
            part = part.trim().toUpperCase();
            switch (part) {
                case "SHIFT", "LSHIFT", "RSHIFT" -> modifiers |= Modifier.SHIFT;
                case "CTRL", "CONTROL", "LCTRL", "RCTRL" -> modifiers |= Modifier.CTRL;
                case "ALT", "LALT", "RALT" -> modifiers |= Modifier.ALT;
                case "SUPER", "META", "WIN", "CMD" -> modifiers |= Modifier.SUPER;
                default -> {
                    // Try to parse as key code
                    try {
                        keyCode = Integer.parseInt(part);
                    } catch (NumberFormatException e) {
                        // Try to get from GLFW key names
                        keyCode = KeyNames.getKeyCode(part);
                    }
                }
            }
        }

        return new Keybind(keyCode, scanCode, modifiers);
    }

    // ============================================================
    // MODIFIER CONSTANTS
    // ============================================================

    public static class Modifier {
        public static final int NONE = 0;
        public static final int SHIFT = 1 << 0;
        public static final int CTRL = 1 << 1;
        public static final int ALT = 1 << 2;
        public static final int SUPER = 1 << 3;

        public static String toString(int modifiers) {
            if (modifiers == NONE) return "";
            StringBuilder sb = new StringBuilder();
            if ((modifiers & SHIFT) != 0) sb.append("Shift + ");
            if ((modifiers & CTRL) != 0) sb.append("Ctrl + ");
            if ((modifiers & ALT) != 0) sb.append("Alt + ");
            if ((modifiers & SUPER) != 0) sb.append("Super + ");
            if (sb.length() > 0) sb.setLength(sb.length() - 3);
            return sb.toString();
        }
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public int getKeyCode() {
        return keyCode;
    }

    public int getScanCode() {
        return scanCode;
    }

    public int getModifiers() {
        return modifiers;
    }

    public boolean hasShift() {
        return (modifiers & Modifier.SHIFT) != 0;
    }

    public boolean hasCtrl() {
        return (modifiers & Modifier.CTRL) != 0;
    }

    public boolean hasAlt() {
        return (modifiers & Modifier.ALT) != 0;
    }

    public boolean hasSuper() {
        return (modifiers & Modifier.SUPER) != 0;
    }

    public boolean hasModifiers() {
        return modifiers != 0;
    }

    public boolean isNone() {
        return keyCode == 0 && modifiers == 0;
    }

    // ============================================================
    // MATCHING
    // ============================================================

    /**
     * Checks if this keybind matches the given input.
     */
    public boolean matches(int keyCode, int scanCode, int modifiers) {
        if (this.keyCode != keyCode) return false;
        if (this.modifiers != modifiers) return false;
        return true;
    }

    /**
     * Checks if this keybind matches the given key event.
     */
    public boolean matches(KeyEvent event) {
        return matches(event.getKeyCode(), event.getScanCode(), event.getModifiers());
    }

    // ============================================================
    // SERIALIZATION
    // ============================================================

    @Override
    public String toString() {
        if (isNone()) return "NONE";

        StringBuilder sb = new StringBuilder();
        if (hasShift()) sb.append("Shift + ");
        if (hasCtrl()) sb.append("Ctrl + ");
        if (hasAlt()) sb.append("Alt + ");
        if (hasSuper()) sb.append("Super + ");
        sb.append(KeyNames.getKeyName(keyCode));
        return sb.toString();
    }

    public String toShortString() {
        if (isNone()) return "NONE";
        StringBuilder sb = new StringBuilder();
        if (hasShift()) sb.append("S+");
        if (hasCtrl()) sb.append("C+");
        if (hasAlt()) sb.append("A+");
        if (hasSuper()) sb.append("M+");
        sb.append(KeyNames.getKeyName(keyCode));
        return sb.toString();
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("keyCode", keyCode);
        obj.addProperty("scanCode", scanCode);
        obj.addProperty("modifiers", modifiers);
        return obj;
    }

    public static Keybind fromJson(JsonObject obj) {
        int keyCode = obj.get("keyCode").getAsInt();
        int scanCode = obj.get("scanCode").getAsInt();
        int modifiers = obj.get("modifiers").getAsInt();
        return new Keybind(keyCode, scanCode, modifiers);
    }

    // ============================================================
    // EQUALS / HASHCODE
    // ============================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keybind keybind = (Keybind) o;
        return keyCode == keybind.keyCode && modifiers == keybind.modifiers;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    // ============================================================
    // KEY EVENT HELPER
    // ============================================================

    public static class KeyEvent {
        private final int keyCode;
        private final int scanCode;
        private final int modifiers;

        public KeyEvent(int keyCode, int scanCode, int modifiers) {
            this.keyCode = keyCode;
            this.scanCode = scanCode;
            this.modifiers = modifiers;
        }

        public int getKeyCode() { return keyCode; }
        public int getScanCode() { return scanCode; }
        public int getModifiers() { return modifiers; }
    }
}