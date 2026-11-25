package com.nullcraft.client.core.keybind;

/**
 * Mapping between GLFW key codes and human-readable names.
 * Based on GLFW 3.3 key codes.
 */
public final class KeyNames {

    private KeyNames() {}

    // ============================================================
    // KEY CODE CONSTANTS (GLFW)
    // ============================================================

    // Printable keys
    public static final int KEY_SPACE = 32;
    public static final int KEY_APOSTROPHE = 39;
    public static final int KEY_COMMA = 44;
    public static final int KEY_MINUS = 45;
    public static final int KEY_PERIOD = 46;
    public static final int KEY_SLASH = 47;
    public static final int KEY_0 = 48;
    public static final int KEY_1 = 49;
    public static final int KEY_2 = 50;
    public static final int KEY_3 = 51;
    public static final int KEY_4 = 52;
    public static final int KEY_5 = 53;
    public static final int KEY_6 = 54;
    public static final int KEY_7 = 55;
    public static final int KEY_8 = 56;
    public static final int KEY_9 = 57;
    public static final int KEY_SEMICOLON = 59;
    public static final int KEY_EQUAL = 61;
    public static final int KEY_A = 65;
    public static final int KEY_B = 66;
    public static final int KEY_C = 67;
    public static final int KEY_D = 68;
    public static final int KEY_E = 69;
    public static final int KEY_F = 70;
    public static final int KEY_G = 71;
    public static final int KEY_H = 72;
    public static final int KEY_I = 73;
    public static final int KEY_J = 74;
    public static final int KEY_K = 75;
    public static final int KEY_L = 76;
    public static final int KEY_M = 77;
    public static final int KEY_N = 78;
    public static final int KEY_O = 79;
    public static final int KEY_P = 80;
    public static final int KEY_Q = 81;
    public static final int KEY_R = 82;
    public static final int KEY_S = 83;
    public static final int KEY_T = 84;
    public static final int KEY_U = 85;
    public static final int KEY_V = 86;
    public static final int KEY_W = 87;
    public static final int KEY_X = 88;
    public static final int KEY_Y = 89;
    public static final int KEY_Z = 90;
    public static final int KEY_LEFT_BRACKET = 91;
    public static final int KEY_BACKSLASH = 92;
    public static final int KEY_RIGHT_BRACKET = 93;
    public static final int KEY_GRAVE_ACCENT = 96;

    // Function keys
    public static final int KEY_ESCAPE = 256;
    public static final int KEY_ENTER = 257;
    public static final int KEY_TAB = 258;
    public static final int KEY_BACKSPACE = 259;
    public static final int KEY_INSERT = 260;
    public static final int KEY_DELETE = 261;
    public static final int KEY_RIGHT = 262;
    public static final int KEY_LEFT = 263;
    public static final int KEY_DOWN = 264;
    public static final int KEY_UP = 265;
    public static final int KEY_PAGE_UP = 266;
    public static final int KEY_PAGE_DOWN = 267;
    public static final int KEY_HOME = 268;
    public static final int KEY_END = 269;
    public static final int KEY_CAPS_LOCK = 280;
    public static final int KEY_SCROLL_LOCK = 281;
    public static final int KEY_NUM_LOCK = 282;
    public static final int KEY_PRINT_SCREEN = 283;
    public static final int KEY_PAUSE = 284;
    public static final int KEY_F1 = 290;
    public static final int KEY_F2 = 291;
    public static final int KEY_F3 = 292;
    public static final int KEY_F4 = 293;
    public static final int KEY_F5 = 294;
    public static final int KEY_F6 = 295;
    public static final int KEY_F7 = 296;
    public static final int KEY_F8 = 297;
    public static final int KEY_F9 = 298;
    public static final int KEY_F10 = 299;
    public static final int KEY_F11 = 300;
    public static final int KEY_F12 = 301;
    public static final int KEY_F13 = 302;
    public static final int KEY_F14 = 303;
    public static final int KEY_F15 = 304;
    public static final int KEY_F16 = 305;
    public static final int KEY_F17 = 306;
    public static final int KEY_F18 = 307;
    public static final int KEY_F19 = 308;
    public static final int KEY_F20 = 309;
    public static final int KEY_F21 = 310;
    public static final int KEY_F22 = 311;
    public static final int KEY_F23 = 312;
    public static final int KEY_F24 = 313;
    public static final int KEY_F25 = 314;

    // Keypad
    public static final int KEY_KP_0 = 320;
    public static final int KEY_KP_1 = 321;
    public static final int KEY_KP_2 = 322;
    public static final int KEY_KP_3 = 323;
    public static final int KEY_KP_4 = 324;
    public static final int KEY_KP_5 = 325;
    public static final int KEY_KP_6 = 326;
    public static final int KEY_KP_7 = 327;
    public static final int KEY_KP_8 = 328;
    public static final int KEY_KP_9 = 329;
    public static final int KEY_KP_DECIMAL = 330;
    public static final int KEY_KP_DIVIDE = 331;
    public static final int KEY_KP_MULTIPLY = 332;
    public static final int KEY_KP_SUBTRACT = 333;
    public static final int KEY_KP_ADD = 334;
    public static final int KEY_KP_ENTER = 335;
    public static final int KEY_KP_EQUAL = 336;

    // Modifiers
    public static final int KEY_LEFT_SHIFT = 340;
    public static final int KEY_LEFT_CONTROL = 341;
    public static final int KEY_LEFT_ALT = 342;
    public static final int KEY_LEFT_SUPER = 343;
    public static final int KEY_RIGHT_SHIFT = 344;
    public static final int KEY_RIGHT_CONTROL = 345;
    public static final int KEY_RIGHT_ALT = 346;
    public static final int KEY_RIGHT_SUPER = 347;
    public static final int KEY_MENU = 348;

    // ============================================================
    // NAME MAPPINGS
    // ============================================================

    private static final int[] KEY_CODES = {
        KEY_SPACE, KEY_APOSTROPHE, KEY_COMMA, KEY_MINUS, KEY_PERIOD, KEY_SLASH,
        KEY_0, KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9,
        KEY_SEMICOLON, KEY_EQUAL,
        KEY_A, KEY_B, KEY_C, KEY_D, KEY_E, KEY_F, KEY_G, KEY_H, KEY_I, KEY_J,
        KEY_K, KEY_L, KEY_M, KEY_N, KEY_O, KEY_P, KEY_Q, KEY_R, KEY_S, KEY_T,
        KEY_U, KEY_V, KEY_W, KEY_X, KEY_Y, KEY_Z,
        KEY_LEFT_BRACKET, KEY_BACKSLASH, KEY_RIGHT_BRACKET, KEY_GRAVE_ACCENT,
        KEY_ESCAPE, KEY_ENTER, KEY_TAB, KEY_BACKSPACE, KEY_INSERT, KEY_DELETE,
        KEY_RIGHT, KEY_LEFT, KEY_DOWN, KEY_UP, KEY_PAGE_UP, KEY_PAGE_DOWN,
        KEY_HOME, KEY_END, KEY_CAPS_LOCK, KEY_SCROLL_LOCK, KEY_NUM_LOCK,
        KEY_PRINT_SCREEN, KEY_PAUSE,
        KEY_F1, KEY_F2, KEY_F3, KEY_F4, KEY_F5, KEY_F6, KEY_F7, KEY_F8,
        KEY_F9, KEY_F10, KEY_F11, KEY_F12, KEY_F13, KEY_F14, KEY_F15, KEY_F16,
        KEY_F17, KEY_F18, KEY_F19, KEY_F20, KEY_F21, KEY_F22, KEY_F23, KEY_F24,
        KEY_KP_0, KEY_KP_1, KEY_KP_2, KEY_KP_3, KEY_KP_4, KEY_KP_5, KEY_KP_6,
        KEY_KP_7, KEY_KP_8, KEY_KP_9, KEY_KP_DECIMAL, KEY_KP_DIVIDE, KEY_KP_MULTIPLY,
        KEY_KP_SUBTRACT, KEY_KP_ADD, KEY_KP_ENTER, KEY_KP_EQUAL,
        KEY_LEFT_SHIFT, KEY_LEFT_CONTROL, KEY_LEFT_ALT, KEY_LEFT_SUPER,
        KEY_RIGHT_SHIFT, KEY_RIGHT_CONTROL, KEY_RIGHT_ALT, KEY_RIGHT_SUPER, KEY_MENU
    };

    private static final String[] KEY_NAMES = {
        "Space", "Apostrophe", "Comma", "Minus", "Period", "Slash",
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "Semicolon", "Equal",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z",
        "LeftBracket", "Backslash", "RightBracket", "Grave",
        "Escape", "Enter", "Tab", "Backspace", "Insert", "Delete",
        "Right", "Left", "Down", "Up", "PageUp", "PageDown",
        "Home", "End", "CapsLock", "ScrollLock", "NumLock",
        "PrintScreen", "Pause",
        "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8",
        "F9", "F10", "F11", "F12", "F13", "F14", "F15", "F16",
        "F17", "F18", "F19", "F20", "F21", "F22", "F23", "F24",
        "Numpad0", "Numpad1", "Numpad2", "Numpad3", "Numpad4", "Numpad5", "Numpad6",
        "Numpad7", "Numpad8", "Numpad9", "NumpadDecimal", "NumpadDivide", "NumpadMultiply",
        "NumpadSubtract", "NumpadAdd", "NumpadEnter", "NumpadEqual",
        "LeftShift", "LeftCtrl", "LeftAlt", "LeftSuper",
        "RightShift", "RightCtrl", "RightAlt", "RightSuper", "Menu"
    };

    private static final Map<Integer, String> CODE_TO_NAME = new java.util.HashMap<>();
    private static final Map<String, Integer> NAME_TO_CODE = new java.util.HashMap<>();

    static {
        for (int i = 0; i < KEY_CODES.length; i++) {
            CODE_TO_NAME.put(KEY_CODES[i], KEY_NAMES[i]);
            NAME_TO_CODE.put(KEY_NAMES[i].toUpperCase(), KEY_CODES[i]);
            // Also add common aliases
            addAliases(KEY_CODES[i], KEY_NAMES[i]);
        }
    }

    private static void addAliases(int code, String name) {
        // Arrow keys
        if (name.equals("Up")) NAME_TO_CODE.put("UPARROW", code);
        if (name.equals("Down")) NAME_TO_CODE.put("DOWNARROW", code);
        if (name.equals("Left")) NAME_TO_CODE.put("LEFTARROW", code);
        if (name.equals("Right")) NAME_TO_CODE.put("RIGHTARROW", code);

        // Control keys
        if (name.equals("LeftCtrl")) NAME_TO_CODE.put("LCONTROL", code);
        if (name.equals("RightCtrl")) NAME_TO_CODE.put("RCONTROL", code);
        if (name.equals("LeftShift")) NAME_TO_CODE.put("LSHIFT", code);
        if (name.equals("RightShift")) NAME_TO_CODE.put("RSHIFT", code);
        if (name.equals("LeftAlt")) NAME_TO_CODE.put("LALT", code);
        if (name.equals("RightAlt")) NAME_TO_CODE.put("RALT", code);
        if (name.equals("LeftSuper")) NAME_TO_CODE.put("LSUPER", code);
        if (name.equals("RightSuper")) NAME_TO_CODE.put("RSUPER", code);

        // Numpad
        if (name.startsWith("Numpad")) {
            String num = name.substring(6);
            NAME_TO_CODE.put("KP_" + num, code);
            NAME_TO_CODE.put("KP" + num, code);
        }
    }

    // ============================================================
    // PUBLIC API
    // ============================================================

    public static String getKeyName(int keyCode) {
        return CODE_TO_NAME.getOrDefault(keyCode, "Unknown (" + keyCode + ")");
    }

    public static int getKeyCode(String name) {
        return NAME_TO_CODE.getOrDefault(name.toUpperCase(), 0);
    }

    public static boolean isValidKey(int keyCode) {
        return CODE_TO_NAME.containsKey(keyCode);
    }

    public static boolean isModifier(int keyCode) {
        return keyCode >= KEY_LEFT_SHIFT && keyCode <= KEY_MENU;
    }

    public static String getModifierName(int keyCode) {
        return switch (keyCode) {
            case KEY_LEFT_SHIFT, KEY_RIGHT_SHIFT -> "Shift";
            case KEY_LEFT_CONTROL, KEY_RIGHT_CONTROL -> "Ctrl";
            case KEY_LEFT_ALT, KEY_RIGHT_ALT -> "Alt";
            case KEY_LEFT_SUPER, KEY_RIGHT_SUPER -> "Super";
            case KEY_MENU -> "Menu";
            default -> "";
        };
    }
}