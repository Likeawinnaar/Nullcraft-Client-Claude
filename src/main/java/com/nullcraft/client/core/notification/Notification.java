package com.nullcraft.client.core.notification;

import com.nullcraft.client.core.utils.DesignTokens;

/**
 * A single notification toast.
 */
public final class Notification {

    private final NotificationType type;
    private final String title;
    private final String description;
    private final int duration;
    private final long timestamp;
    private final UUID id;

    private float progress = 0f; // 0 to 1 for animation
    private State state = State.ENTERING;

    public Notification(NotificationType type, String title, String description, int duration) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.timestamp = System.currentTimeMillis();
        this.id = UUID.randomUUID();
    }

    public Notification(NotificationType type, String title, String description) {
        this(type, title, description, getDefaultDuration(type));
    }

    private static int getDefaultDuration(NotificationType type) {
        return switch (type) {
            case ERROR -> 6000;
            case WARNING -> 5000;
            default -> 3000;
        };
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UUID getId() {
        return id;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = Math.max(0, Math.min(1, progress));
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getAccentColor() {
        return type.getAccentColor();
    }

    public String getIconId() {
        return type.getIconId();
    }

    public boolean isExpired() {
        return state == State.EXITED;
    }

    // ============================================================
    // ANIMATION STATES
    // ============================================================

    public enum State {
        ENTERING,   // Sliding in, fading in
        VISIBLE,    // Fully visible, waiting
        EXITING,    // Sliding out, fading out
        EXITED      // Fully hidden, ready for removal
    }

    @Override
    public String toString() {
        return "Notification{" + type + ": " + title + "}";
    }
}