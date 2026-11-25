package com.nullcraft.client.core.event;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central event bus for decoupled communication between modules and systems.
 * Uses reflection to find @Subscribe annotated methods.
 */
public final class EventBus {

    private static final EventBus INSTANCE = new EventBus();

    private final Map<Class<? extends Event>, List<Subscription>> subscriptions = new ConcurrentHashMap<>();

    private EventBus() {}

    public static EventBus getInstance() {
        return INSTANCE;
    }

    // ============================================================
    // SUBSCRIPTION MANAGEMENT
    // ============================================================

    /**
     * Registers an object's @Subscribe methods as event listeners.
     */
    public void subscribe(Object listener) {
        Class<?> clazz = listener.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1 && Event.class.isAssignableFrom(params[0])) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Event> eventType = (Class<? extends Event>) params[0];
                    subscribe(eventType, new Subscription(listener, method));
                }
            }
        }
    }

    /**
     * Unregisters all subscriptions for a listener object.
     */
    public void unsubscribe(Object listener) {
        subscriptions.values().forEach(list -> list.removeIf(sub -> sub.listener == listener));
    }

    private void subscribe(Class<? extends Event> eventType, Subscription subscription) {
        subscriptions.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(subscription);
    }

    // ============================================================
    // EVENT PUBLISHING
    // ============================================================

    /**
     * Publishes an event to all subscribers synchronously.
     */
    public void publish(Event event) {
        List<Subscription> subscribers = subscriptions.get(event.getClass());
        if (subscribers != null) {
            for (Subscription subscription : subscribers) {
                try {
                    subscription.invoke(event);
                } catch (Exception e) {
                    System.err.println("[NullCraft] Error in event handler: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        // Also publish to parent event types (for inheritance)
        Class<?> superClass = event.getClass().getSuperclass();
        while (superClass != null && Event.class.isAssignableFrom(superClass)) {
            @SuppressWarnings("unchecked")
            Class<? extends Event> parentType = (Class<? extends Event>) superClass;
            subscribers = subscriptions.get(parentType);
            if (subscribers != null) {
                for (Subscription subscription : subscribers) {
                    try {
                        subscription.invoke(event);
                    } catch (Exception e) {
                        System.err.println("[NullCraft] Error in event handler: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            superClass = superClass.getSuperclass();
        }
    }

    /**
     * Publishes an event asynchronously on a separate thread.
     */
    public void publishAsync(Event event) {
        new Thread(() -> publish(event), "NullCraft-EventBus-" + event.getClass().getSimpleName()).start();
    }

    // ============================================================
    // INTERNAL SUBSCRIPTION CLASS
    // ============================================================

    private static class Subscription {
        final Object listener;
        final Method method;

        Subscription(Object listener, Method method) {
            this.listener = listener;
            this.method = method;
            this.method.setAccessible(true);
        }

        void invoke(Event event) throws Exception {
            method.invoke(listener, event);
        }
    }

    // ============================================================
    // SUBSCRIBE ANNOTATION
    // ============================================================

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
    public @interface Subscribe {
        // Marker annotation for event handler methods
    }

    // ============================================================
    // BASE EVENT CLASS
    // ============================================================

    public static abstract class Event {
        private final long timestamp = System.currentTimeMillis();
        private boolean cancelled = false;

        public long getTimestamp() {
            return timestamp;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }
    }

    // ============================================================
    // COMMON EVENT CLASSES
    // ============================================================

    public static class TickEvent extends Event {
        public static class Client extends TickEvent {}
        public static class Render extends TickEvent {}
        public static class World extends TickEvent {}
    }

    public static class RenderEvent extends Event {
        public static class World extends RenderEvent {}
        public static class HUD extends RenderEvent {}
        public static class GUI extends RenderEvent {}
    }

    public static class KeyEvent extends Event {
        private final int key;
        private final int action;
        private final int modifiers;

        public KeyEvent(int key, int action, int modifiers) {
            this.key = key;
            this.action = action;
            this.modifiers = modifiers;
        }

        public int getKey() { return key; }
        public int getAction() { return action; }
        public int getModifiers() { return modifiers; }
        public boolean isPressed() { return action == 1; }
        public boolean isReleased() { return action == 0; }
        public boolean isRepeated() { return action == 2; }
    }

    public static class MouseEvent extends Event {
        private final int button;
        private final int action;
        private final double x;
        private final double y;

        public MouseEvent(int button, int action, double x, double y) {
            this.button = button;
            this.action = action;
            this.x = x;
            this.y = y;
        }

        public int getButton() { return button; }
        public int getAction() { return action; }
        public double getX() { return x; }
        public double getY() { return y; }
        public boolean isPressed() { return action == 1; }
        public boolean isReleased() { return action == 0; }
    }

    public static class ChatEvent extends Event {
        private final String message;

        public ChatEvent(String message) {
            this.message = message;
        }

        public String getMessage() { return message; }
    }

    public static class ConfigEvent extends Event {
        public static class Load extends ConfigEvent {}
        public static class Save extends ConfigEvent {}
    }
}