package com.nullcraft.client;

import com.nullcraft.client.core.config.ConfigManager;
import com.nullcraft.client.core.event.EventBus;
import com.nullcraft.client.core.keybind.KeybindManager;
import com.nullcraft.client.core.module.ModuleRegistry;
import com.nullcraft.client.core.notification.NotificationManager;
import com.nullcraft.client.gui.clickgui.ClickGuiScreen;
import com.nullcraft.client.gui.hud.HudManager;
import com.nullcraft.client.gui.hudeditor.HudEditorScreen;
import com.nullcraft.client.modules.combat.*;
import com.nullcraft.client.modules.movement.*;
import com.nullcraft.client.modules.player.*;
import com.nullcraft.client.modules.render.*;
import com.nullcraft.client.modules.world.*;
import com.nullcraft.client.modules.diagnostics.*;
import com.nullcraft.client.modules.misc.*;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NullCraft Client - Main entry point for the Fabric client mod.
 */
public final class NullCraftClient implements ClientModInitializer {

    public static final String MOD_ID = "nullcraft";
    public static final String MOD_NAME = "NullCraft Client";
    public static final String VERSION = "1.0.0";

    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Key bindings
    private static KeyBinding clickGuiKey;
    private static KeyBinding hudEditorKey;
    private static KeyBinding emergencyDisableKey;

    // Managers
    private static ModuleRegistry moduleRegistry;
    private static HudManager hudManager;
    private static ConfigManager configManager;
    private static KeybindManager keybindManager;
    private static NotificationManager notificationManager;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing {} v{}", MOD_NAME, VERSION);

        // Initialize managers
        moduleRegistry = ModuleRegistry.getInstance();
        hudManager = HudManager.getInstance();
        configManager = ConfigManager.getInstance();
        keybindManager = KeybindManager.getInstance();
        notificationManager = NotificationManager.getInstance();

        // Register modules
        registerModules();

        // Register key bindings
        registerKeyBindings();

        // Register event listeners
        registerEvents();

        // Load default config
        configManager.load("default").join();

        notificationManager.success(
            "NullCraft Loaded",
            MOD_NAME + " v" + VERSION + " initialized successfully",
            3000
        );

        LOGGER.info("{} v{} initialized successfully", MOD_NAME, VERSION);
    }

    // ============================================================
    // MODULE REGISTRATION
    // ============================================================

    private void registerModules() {
        // Combat modules (8)
        moduleRegistry.registerAll(
            new TargetHUDModule(),
            new DamageIndicatorModule(),
            new TotemCounterModule(),
            new ArmorDurabilityModule(),
            new CombatStatsModule(),
            new TargetDistanceModule(),
            new AttackCooldownHUDModule(),
            new ShieldStatusModule()
        );

        // Movement modules (8)
        moduleRegistry.registerAll(
            new AutoSprintModule(),
            new AutoWalkModule(),
            new AutoJumpModule(),
            new SafeWalkModule(),
            new StepAssistModule(),
            new FreeLookModule(),
            new FreecamModule(),
            new SpeedometerModule()
        );

        // Player modules (8)
        moduleRegistry.registerAll(
            new AutoEatModule(),
            new AutoToolModule(),
            new InventorySorterModule(),
            new InventoryCleanerModule(),
            new AutoRefillModule(),
            new DurabilityAlertModule(),
            new FoodTrackerModule(),
            new EquipmentHUDModule()
        );

        // Render modules (10)
        moduleRegistry.registerAll(
            new PlayerESPModule(),
            new MobESPModule(),
            new ItemESPModule(),
            new FullBrightModule(),
            new CustomCrosshairModule(),
            new NameTagsModule(),
            new ZoomModule(),
            new ChunkBordersModule(),
            new CoordinateOverlayModule(),
            new DirectionCompassModule()
        );

        // World modules (8)
        moduleRegistry.registerAll(
            new WaypointsModule(),
            new MiniMapModule(),
            new BiomeInfoModule(),
            new ChestVisualizerModule(),
            new WorldTimeTrackerModule(),
            new CoordinateOverlayModule(), // Duplicate - will be filtered
            new DimensionTrackerModule(),
            new BuildAssistantModule()
        );

        // Diagnostics modules (8)
        moduleRegistry.registerAll(
            new PacketInspectorModule(),
            new PacketStatisticsModule(),
            new ConnectionMonitorModule(),
            new LatencyGraphModule(),
            new NetworkDiagnosticsModule(),
            new ChunkLoadMonitorModule(),
            new InteractionRangeTesterModule(),
            new MovementValidationVisualizerModule()
        );

        // Misc modules (10)
        moduleRegistry.registerAll(
            new ChatFilterModule(),
            new FriendManagerModule(),
            new NotificationsModule(),
            new ScreenshotManagerModule(),
            new PingMonitorModule(),
            new TPSMonitorModule(),
            new FPSMonitorModule(),
            new MemoryMonitorModule(),
            new SessionStatsModule(),
            new DebugOverlayModule()
        );

        LOGGER.info("Registered {} modules", moduleRegistry.getTotalCount());
    }

    // ============================================================
    // KEY BINDINGS
    // ============================================================

    private void registerKeyBindings() {
        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.nullcraft.clickgui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.nullcraft.general"
        ));

        hudEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.nullcraft.hud_editor",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_CONTROL,
            "category.nullcraft.general"
        ));

        emergencyDisableKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.nullcraft.emergency_disable",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_END,
            "category.nullcraft.general"
        ));
    }

    // ============================================================
    // EVENT LISTENERS
    // ============================================================

    private void registerEvents() {
        // Client tick - module updates, config auto-save
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                moduleRegistry.onTick();
            }
            configManager.tick();
            notificationManager.tick();
        });

        // Key input handling
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            handleKeyInput(client);
        });

        // HUD rendering
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (client != null && client.player != null) {
                moduleRegistry.onRender(tickDelta);
                hudManager.render(drawContext, tickDelta);
            }
        });
    }

    private void handleKeyInput(MinecraftClient client) {
        // ClickGUI toggle
        if (clickGuiKey.wasPressed()) {
            if (client.currentScreen == null) {
                client.setScreen(new ClickGuiScreen());
            } else if (client.currentScreen instanceof ClickGuiScreen) {
                client.setScreen(null);
            }
        }

        // HUD Editor toggle
        if (hudEditorKey.wasPressed()) {
            if (client.currentScreen == null) {
                client.setScreen(new HudEditorScreen());
            } else if (client.currentScreen instanceof HudEditorScreen) {
                client.setScreen(null);
            }
        }

        // Emergency disable
        if (emergencyDisableKey.wasPressed()) {
            moduleRegistry.disableAll();
            notificationManager.warning("Emergency Disable", "All modules disabled", 3000);
        }

        // Let KeybindManager handle module keybinds
        // Note: This is a simplified version - actual implementation would use
        // Fabric's key binding API or a custom key event listener
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public static ModuleRegistry getModuleRegistry() {
        return moduleRegistry;
    }

    public static HudManager getHudManager() {
        return hudManager;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static KeybindManager getKeybindManager() {
        return keybindManager;
    }

    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public static KeyBinding getClickGuiKey() {
        return clickGuiKey;
    }

    public static KeyBinding getHudEditorKey() {
        return hudEditorKey;
    }

    public static KeyBinding getEmergencyDisableKey() {
        return emergencyDisableKey;
    }
}