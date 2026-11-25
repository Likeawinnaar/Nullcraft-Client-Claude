# NullCraft Client

A professional Minecraft Fabric 1.21.1 client modification with a modern dark-themed GUI, custom font rendering, and 60+ modules across 7 categories.

## Features

### Core Framework
- **Module System**: Extensible module framework with lifecycle management (enable/disable/tick/render)
- **Settings System**: 10 setting types (Boolean, Integer, Double, Range, Mode, MultiSelect, Color, String, Keybind, Group)
- **Event Bus**: Reflection-based `@Subscribe` annotation with event inheritance support
- **Config Manager**: JSON persistence with atomic writes, auto-save, backup system
- **Keybind Manager**: Global/module keybinds with conflict detection and modifiers (Shift/Ctrl/Alt/Super)
- **Notification System**: Toast notifications with enter/visible/exit animations

### GUI System
- **Custom Font Rendering**: Inter font family (Regular, Medium, SemiBold, Bold) via STB TrueType
- **Design Token System**: Consistent theming with colors, radii, spacing, shadows, animations
- **Component Library**: 15+ reusable components (Button, Toggle, Slider, Dropdown, ColorPicker, KeybindButton, TextField, etc.)
- **ClickGUI**: 4-panel application interface (Header, Sidebar, Module Browser, Settings Inspector)

### HUD System
- **Watermark**: Logo + NULLCRAFT + version
- **ArrayList**: Enabled modules with keybinds and smooth animations
- **InfoHUD**: FPS, ping, coordinates, biome, direction, TPS
- **Keystrokes**: WASD + LMB/RMB + CPS counter
- **TargetHUD**: Target avatar, name, health bar, distance, armor
- **HUD Editor**: Drag-drop, grid snap, alignment guides, presets, import/export

### Theme System
- **Theme Editor**: Live preview with 7 built-in themes + custom
- **Design Tokens**: All colors, radii, spacing, shadows, typography, layout dimensions editable
- **Persistence**: Themes saved as JSON with full token support

### Modules (60 Core Modules)

#### Combat (8)
- TargetHUD, DamageIndicator, TotemCounter, ArmorDurability
- CombatStats, TargetDistance, AttackCooldownHUD, ShieldStatus

#### Movement (8)
- AutoSprint, AutoWalk, AutoJump, SafeWalk
- StepAssist, FreeLook, Freecam, Speedometer

#### Player (8)
- AutoEat, AutoTool, InventorySorter, InventoryCleaner
- AutoRefill, DurabilityAlert, FoodTracker, EquipmentHUD

#### Render (10)
- PlayerESP, MobESP, ItemESP, FullBright, CustomCrosshair
- NameTags, Zoom, ChunkBorders, CoordinateOverlay, DirectionCompass

#### World (8)
- Waypoints, MiniMap, BiomeInfo, ChestVisualizer
- WorldTimeTracker, CoordinateOverlay, DimensionTracker, BuildAssistant

#### Diagnostics (8)
- PacketInspector, PacketStatistics, ConnectionMonitor, LatencyGraph
- NetworkDiagnostics, ChunkLoadMonitor, InteractionRangeTester, MovementValidationVisualizer

#### Misc (10)
- ChatFilter, FriendManager, Notifications, ScreenshotManager
- PingMonitor, TPSMonitor, FPSMonitor, MemoryMonitor, SessionStats, DebugOverlay

## Building

### Requirements
- Java 21
- Gradle 8.10+ (wrapper included)

### Build Commands
```bash
# Download Inter fonts and generate assets
./gradlew downloadFonts generateAssets

# Compile
./gradlew compileJava

# Run tests
./gradlew test

# Build JAR
./gradlew build
```

### Output
The built JAR will be in `build/libs/nullcraft-client-1.0.0.jar`

## Installation

1. Install [Fabric Loader 0.16.x](https://fabricmc.net/use/installer/) for Minecraft 1.21.1
2. Install [Fabric API 0.116.x](https://modrinth.com/mod/fabric-api) for 1.21.1
3. Place the built JAR in your `mods` folder
4. Launch Minecraft with the Fabric profile

## Usage

### Keybinds (Default)
| Keybind | Action |
|---------|--------|
| Right Shift | Open ClickGUI |
| Right Ctrl | Open HUD Editor |
| End | Emergency Disable All Modules |

### ClickGUI Navigation
- **Left Click** module: Select module (shows settings in inspector)
- **Right Click** module: Toggle enabled/disabled
- **Middle Click** module: Toggle favorite
- **Search**: Filter modules by name/description
- **Favorites Button**: Show only favorited modules
- **Pin Button**: Keep GUI open when clicking outside

### HUD Editor
1. Open with Right Ctrl
2. Drag HUD elements to reposition
3. Hold Shift while dragging to disable grid snap
4. Click element to select and show settings
5. Use presets (Minimal, Full, PvP, Builder) for quick layouts
6. Export/import layouts as JSON

### Theme Editor
1. Open from ClickGUI settings
2. Select preset theme or customize tokens
3. Live preview updates in real-time
4. Save as custom theme
5. Export/import theme files

## Configuration

Configurations are stored in:
```
.minecraft/config/nullcraft/
├── configs/          # Module configs (default.json, etc.)
├── hud/              # HUD layouts
├── themes/           # Custom themes
├── backups/          # Automatic backups
└── keybinds.json     # Global keybinds
```

## Development

### Project Structure
```
src/main/java/com/nullcraft/client/
├── NullCraftClient.java          # Entry point
├── core/
│   ├── module/                   # Module framework
│   ├── settings/                 # 10 setting types
│   ├── event/                    # Event bus
│   ├── config/                   # Config, HUD, Theme persistence
│   ├── keybind/                  # Keybind management
│   ├── notification/             # Notification system
│   └── utils/                    # DesignTokens, ColorUtils, MathUtils
├── modules/
│   ├── combat/                   # 8 modules
│   ├── movement/                 # 8 modules
│   ├── player/                   # 8 modules
│   ├── render/                   # 10 modules
│   ├── world/                    # 8 modules
│   ├── diagnostics/              # 8 modules
│   └── misc/                     # 10 modules
├── gui/
│   ├── clickgui/                 # 4-panel ClickGUI
│   ├── components/               # Component library
│   ├── render/                   # RoundedRect, glow, shadow
│   ├── font/                     # FontRenderer, FontManager, GlyphCache
│   ├── hud/                      # HUD elements + editor
│   ├── themeeditor/              # Theme editor
│   └── screens/                  # Config, Keybind, About screens
├── asset/                        # Procedural asset generator
└── managers/                     # Module, HUD, Config managers
```

### Adding a New Module

1. Create a new class in the appropriate category package:
```java
package com.nullcraft.client.modules.combat;

public class MyModule extends Module {
    protected MyModule() {
        super("my_module", "My Module", "Description", ModuleCategory.COMBAT, "icon");
    }

    @Override
    protected void registerSettings() {
        registerSetting(new BooleanSetting("enabled", "Enable", true));
    }

    @Override
    protected void createSettingGroups() {
        createGroup("General").addSetting(getSetting("enabled"));
    }

    @Override
    public void onEnable() { /* ... */ }
    @Override
    public void onDisable() { /* ... */ }
}
```

2. Register in `NullCraftClient.java`:
```java
registry.register(new MyModule());
```

### Adding a New Setting Type

1. Extend `Setting<T>` in `core/settings/`
2. Add `SettingType` enum value
3. Create component in `gui/components/`
4. Handle in `SettingsInspectorPanel`

## Asset Generation

Assets are generated at build time:
- **Fonts**: Downloaded from GitHub (Inter, SIL OFL license)
- **Icons**: Procedurally generated with Java 2D (geometric, consistent style)
- **Logo**: Gradient "N" with glow effects
- **GUI Textures**: Noise patterns, slider tracks, checkboxes

Run `./gradlew generateAssets` to regenerate.

## Testing

Run unit tests:
```bash
./gradlew test
```

Tests cover:
- Module registry (registration, search, lifecycle)
- Settings (all 10 types, validation, serialization)
- Config manager (CRUD, backup, import/export)
- Keybind system (parsing, conflicts, modifiers)
- Layout calculations (bounds, visibility, components)
- Font metrics and ColorUtils (HSV/RGB, lerp, blend, contrast)
- Theme system (built-ins, overrides, serialization)
- Module lifecycle (enable/disable, events, persistence)

## License

MIT License - see LICENSE file for details.

## Credits

- **Font**: [Inter](https://rsms.me/inter/) by Rasmus Andersson (SIL OFL)
- **Fabric**: [FabricMC](https://fabricmc.net/)
- **Mappings**: [Yarn](https://github.com/FabricMC/yarn)
- **STB TrueType**: [nothings/stb](https://github.com/nothings/stb)
- **Gson**: Google JSON library

## Disclaimer

This is a client modification for Minecraft. Use at your own risk. Some servers may prohibit client modifications. The authors are not responsible for any bans or issues arising from use.