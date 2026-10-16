package com.ember.client.module.modules;

import com.ember.client.module.Category;
import com.ember.client.module.Module;

/** Keeps the sneak key held, equivalent to the vanilla "Toggle Sneak" accessibility option. */
public class ToggleSneak extends Module {
    public ToggleSneak() {
        super("Toggle Sneak", "Keeps sneak held so you don't have to hold the key.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.screen == null) {
            mc.options.keyShift.setDown(true);
        }
    }

    @Override
    protected void onDisable() {
        mc.options.keyShift.setDown(false);
    }
}
