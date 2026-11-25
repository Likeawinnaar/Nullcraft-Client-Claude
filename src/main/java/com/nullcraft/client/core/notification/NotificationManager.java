package com.nullcraft.client.core.notification;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages notification toasts - display, animation, and lifecycle.
 */
public final class NotificationManager {

    private static final NotificationManager INSTANCE = new NotificationManager();

    private final List<Notification> notifications = new CopyOnWriteArrayList<>();
    private final int maxNotifications = 5;
    private long lastNotificationTime = 0;
    private static final long MIN_NOTIFICATION_INTERVAL = 100; // ms

    private NotificationManager() {}

    public static NotificationManager getInstance() {
        return INSTANCE;
    }

    // ============================================================
    // CONVENIENCE METHODS
    // ============================================================

    public void success(String title, String description, int duration) {
        add(new Notification(NotificationType.SUCCESS, title, description, duration));
    }

    public void success(String title, String description) {
        success(title, description, 3000);
    }

    public void error(String title, String description, int duration) {
        add(new Notification(NotificationType.ERROR, title, description, duration));
    }

    public void error(String title, String description) {
        error(title, description, 5000);
    }

    public void warning(String title, String description, int duration) {
        add(new Notification(NotificationType.WARNING, title, description, duration));
    }

    public void warning(String title, String description) {
        warning(title, description, 4000);
    }

    public void info(String title, String description, int duration) {
        add(new Notification(NotificationType.INFO, title, description, duration));
    }

    public void info(String title, String description) {
        info(title, description, 3000);
    }

    public void moduleEnabled(String moduleName) {
        add(new Notification(NotificationType.MODULE_ENABLED, moduleName, "Module has been enabled"));
    }

    public void moduleDisabled(String moduleName) {
        add(new Notification(NotificationType.MODULE_DISABLED, moduleName, "Module has been disabled"));
    }

    public void configSaved(String configName) {
        add(new Notification(NotificationType.CONFIG_SAVED, "Config Saved", "'" + configName + "' has been saved"));
    }

    public void configLoaded(String configName) {
        add(new Notification(NotificationType.CONFIG_LOADED, "Config Loaded", "'" + configName + "' has been loaded"));
    }

    // ============================================================
    // CORE METHODS
    // ============================================================

    public void add(Notification notification) {
        long now = System.currentTimeMillis();
        if (now - lastNotificationTime < MIN_NOTIFICATION_INTERVAL) {
            try {
                Thread.sleep(MIN_NOTIFICATION_INTERVAL - (now - lastNotificationTime));
            } catch (InterruptedException ignored) {}
        }
        lastNotificationTime = System.currentTimeMillis();

        // Remove oldest if at capacity
        if (notifications.size() >= maxNotifications) {
            notifications.remove(0);
        }

        notifications.add(notification);
    }

    public void remove(Notification notification) {
        notifications.remove(notification);
    }

    public void clear() {
        notifications.clear();
    }

    public List<Notification> getNotifications() {
        return List.copyOf(notifications);
    }

    // ============================================================
    // ANIMATION TICK
    // ============================================================

    /**
     * Updates notification animations. Should be called every render frame.
     */
    public void tick() {
        long now = System.currentTimeMillis();
        for (Notification notification : notifications) {
            updateNotification(notification, now);
        }

        // Remove expired notifications
        notifications.removeIf(Notification::isExpired);
    }

    private void updateNotification(Notification notification, long now) {
        long elapsed = now - notification.getTimestamp();
        Notification.State state = notification.getState();

        switch (state) {
            case ENTERING -> {
                // Slide in over 300ms
                float progress = Math.min(1f, elapsed / 300f);
                notification.setProgress(progress);
                if (progress >= 1f) {
                    notification.setState(Notification.State.VISIBLE);
                    notification.setProgress(1f);
                }
            }
            case VISIBLE -> {
                notification.setProgress(1f);
                if (elapsed >= notification.getDuration()) {
                    notification.setState(Notification.State.EXITING);
                }
            }
            case EXITING -> {
                long exitElapsed = elapsed - notification.getDuration();
                float progress = Math.max(0f, 1f - exitElapsed / 300f);
                notification.setProgress(progress);
                if (progress <= 0f) {
                    notification.setState(Notification.State.EXITED);
                }
            }
            case EXITED -> {
                // Will be removed by tick()
            }
        }
    }

    // ============================================================
    // CONFIGURATION
    // ============================================================

    public int getMaxNotifications() {
        return maxNotifications;
    }
}