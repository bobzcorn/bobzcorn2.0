package com.ember.client.module;

import com.ember.client.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected static final Minecraft mc = Minecraft.getInstance();

    public final String name;
    public final String description;
    public final Category category;
    private boolean enabled;
    private final List<Setting<?>> settings = new ArrayList<>();

    protected Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    protected <S extends Setting<?>> S add(S setting) {
        settings.add(setting);
        return setting;
    }

    public List<Setting<?>> settings() {
        return settings;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean value) {
        if (enabled == value) return;
        enabled = value;
        if (value) onEnable(); else onDisable();
    }

    /** Called once when the config is loaded, so modules that were saved as enabled re-apply their effect. */
    public void applyLoadedState() {
        if (enabled) onEnable();
    }

    protected void onEnable() {}

    protected void onDisable() {}

    /** Called every client tick while enabled. */
    public void onTick() {}

    /** Called every frame while enabled to draw HUD text. Return value is unused. */
    public void renderHud(GuiGraphicsExtractor graphics) {}

    /** Called every frame (enabled or not) for modules that need to react to key state. */
    public void onFrame() {}

    /** Used by the loader to restore the enabled flag without firing onEnable twice. */
    public void restoreEnabledFlag(boolean value) {
        this.enabled = value;
    }
}
