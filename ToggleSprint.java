package com.ember.client.module.modules;

import com.ember.client.module.Category;
import com.ember.client.module.Module;

/** Holds the sprint key for you while you move forward. This is the same as the vanilla "Toggle Sprint" accessibility option. */
public class ToggleSprint extends Module {
    public ToggleSprint() {
        super("Toggle Sprint", "Keeps sprint held while you walk forward.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.options.keyUp.isDown()) {
            mc.options.keySprint.setDown(true);
        }
    }

    @Override
    protected void onDisable() {
        mc.options.keySprint.setDown(false);
    }
}
