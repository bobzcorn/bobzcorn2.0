package com.ember.client.module.modules;

import com.ember.client.module.Category;
import com.ember.client.module.Module;

/** Uses the game's own brightness option at its vanilla maximum, and restores your previous value on disable. */
public class Fullbright extends Module {
    private double previous = 0.5;

    public Fullbright() {
        super("Fullbright", "Sets brightness to the vanilla maximum.", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        previous = mc.options.gamma().get();
        mc.options.gamma().set(1.0);
    }

    @Override
    protected void onDisable() {
        mc.options.gamma().set(previous);
    }
}
