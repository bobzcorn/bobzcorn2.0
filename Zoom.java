package com.ember.client.module.modules;

import com.ember.client.EmberClient;
import com.ember.client.module.Category;
import com.ember.client.module.Module;
import com.ember.client.setting.NumberSetting;

/** Hold the zoom key (default C) to narrow your FOV; release to restore it. */
public class Zoom extends Module {
    private final NumberSetting zoomFov = add(new NumberSetting("Zoom FOV", 20, 5, 60, 1));
    private int saved = -1;

    public Zoom() {
        super("Zoom", "Hold the zoom key (default C) to zoom in.", Category.UTILITY);
    }

    @Override
    public void onFrame() {
        boolean held = isEnabled() && EmberClient.zoomKey != null && EmberClient.zoomKey.isDown();
        if (held) {
            if (saved < 0) saved = mc.options.fov().get();
            mc.options.fov().set(zoomFov.intValue());
        } else if (saved >= 0) {
            mc.options.fov().set(saved);
            saved = -1;
        }
    }

    @Override
    protected void onDisable() {
        if (saved >= 0) {
            mc.options.fov().set(saved);
            saved = -1;
        }
    }
}
