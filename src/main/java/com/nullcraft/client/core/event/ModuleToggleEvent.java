package com.nullcraft.client.core.event;

import com.nullcraft.client.core.module.Module;

/**
 * Event fired when a module is toggled on/off.
 */
public class ModuleToggleEvent extends Event {

    private final Module module;
    private final boolean enabled;

    public ModuleToggleEvent(Module module, boolean enabled) {
        this.module = module;
        this.enabled = enabled;
    }

    public Module getModule() {
        return module;
    }

    public boolean isEnabled() {
        return enabled;
    }
}