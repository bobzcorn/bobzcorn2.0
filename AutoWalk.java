package com.ember.client.module.modules;

import com.ember.client.module.Category;
import com.ember.client.module.Module;

/** Holds the forward key for you, handy for long walks. Turn the module off to stop. */
public class AutoWalk extends Module {
    public AutoWalk() {
        super("Auto Walk", "Keeps the forward key held so you walk on your own.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (mc.player != null && mc.screen == null) {
            mc.options.keyUp.setDown(true);
        }
    }

    @Override
    protected void onDisable() {
        mc.options.keyUp.setDown(false);
    }
}
