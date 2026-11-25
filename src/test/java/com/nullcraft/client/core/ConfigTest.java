package com.nullcraft.client.core;

import com.nullcraft.client.core.config.ConfigManager;
import com.nullcraft.client.core.module.Module;
import com.nullcraft.client.core.module.ModuleCategory;
import com.nullcraft.client.core.module.ModuleRegistry;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ConfigManager.
 */
class ConfigTest {

    private ConfigManager configManager;
    private Path tempConfigDir;

    @BeforeEach
    void setUp() throws IOException {
        // Use a temporary directory for tests
        tempConfigDir = Files.createTempDirectory("nullcraft-test-config");
        System.setProperty("fabric.mc.dir", tempConfigDir.toString());

        configManager = ConfigManager.getInstance();
        // Reset singleton state for testing (not ideal but works for unit tests)
    }

    @AfterEach
    void tearDown() throws IOException {
        configManager.shutdown();
        // Clean up temp directory
        Files.walk(tempConfigDir)
            .sorted((a, b) -> b.compareTo(a))
            .forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    // Ignore
                }
            });
    }

    @Test
    void testConfigDirectoriesCreated() {
        assertTrue(Files.exists(tempConfigDir.resolve("config/nullcraft/configs")));
        assertTrue(Files.exists(tempConfigDir.resolve("config/nullcraft/backups")));
    }

    @Test
    void testCreateConfig() {
        boolean result = configManager.createConfig("test_config");
        assertTrue(result);
        assertTrue(configManager.hasConfig("test_config"));
    }

    @Test
    void testDuplicateConfigFails() {
        configManager.createConfig("test_config");
        boolean result = configManager.createConfig("test_config");
        assertFalse(result);
    }

    @Test
    void testGetConfigs() {
        configManager.createConfig("config_a");
        configManager.createConfig("config_b");
        configManager.createConfig("config_c");

        var configs = configManager.getConfigs();
        assertEquals(4, configs.size()); // default + 3 created
        assertTrue(configs.contains("config_a"));
        assertTrue(configs.contains("config_b"));
        assertTrue(configs.contains("config_c"));
        assertTrue(configs.contains("default"));
    }

    @Test
    void testDeleteConfig() {
        configManager.createConfig("to_delete");
        assertTrue(configManager.hasConfig("to_delete"));

        boolean result = configManager.deleteConfig("to_delete");
        assertTrue(result);
        assertFalse(configManager.hasConfig("to_delete"));
    }

    @Test
    void testCannotDeleteDefault() {
        boolean result = configManager.deleteConfig("default");
        assertFalse(result);
        assertTrue(configManager.hasConfig("default"));
    }

    @Test
    void testRenameConfig() {
        configManager.createConfig("old_name");
        boolean result = configManager.renameConfig("old_name", "new_name");
        assertTrue(result);
        assertFalse(configManager.hasConfig("old_name"));
        assertTrue(configManager.hasConfig("new_name"));
    }

    @Test
    void testCannotRenameDefault() {
        boolean result = configManager.renameConfig("default", "new_default");
        assertFalse(result);
    }

    @Test
    void testDuplicateConfig() {
        configManager.createConfig("source");
        boolean result = configManager.duplicateConfig("source", "target");
        assertTrue(result);
        assertTrue(configManager.hasConfig("source"));
        assertTrue(configManager.hasConfig("target"));
    }

    @Test
    void testExportImportConfig() throws IOException {
        configManager.createConfig("to_export");
        Path exportPath = tempConfigDir.resolve("exported_config.json");

        boolean exportResult = configManager.exportConfig("to_export", exportPath);
        assertTrue(exportResult);
        assertTrue(Files.exists(exportPath));

        boolean importResult = configManager.importConfig(exportPath, "imported");
        assertTrue(importResult);
        assertTrue(configManager.hasConfig("imported"));
    }

    @Test
    void testBackupConfig() {
        configManager.createConfig("backup_test");
        configManager.backup("backup_test");

        var backupsDir = tempConfigDir.resolve("config/nullcraft/backups");
        assertTrue(Files.exists(backupsDir));

        var backupFiles = backupsDir.toFile().listFiles((dir, name) -> name.startsWith("backup_test_"));
        assertNotNull(backupFiles);
        assertEquals(1, backupFiles.length);
    }

    @Test
    void testActiveConfig() {
        assertEquals("default", configManager.getActiveConfig());

        configManager.createConfig("new_active");
        configManager.setActiveConfig("new_active");
        assertEquals("new_active", configManager.getActiveConfig());
    }
}