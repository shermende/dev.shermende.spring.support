package dev.shermende.support.spring.jmx.impl;

import dev.shermende.support.spring.jmx.ToggleJmxControl;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.concurrent.atomic.AtomicBoolean;

@ManagedResource
public class ToggleJmxControlImpl implements ToggleJmxControl {
    private final AtomicBoolean enabled;

    public ToggleJmxControlImpl(boolean enabled) {
        this.enabled = new AtomicBoolean(enabled);
    }

    @ManagedOperation
    public boolean isEnabled() {
        return enabled.get();
    }

    @ManagedOperation
    public boolean toggle() {
        enabled.set(!enabled.get());
        return enabled.get();
    }
}
