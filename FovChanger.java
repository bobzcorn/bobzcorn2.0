package com.ember.client.module.modules;

import com.ember.client.module.Category;
import com.ember.client.module.Module;
import com.ember.client.setting.NumberSetting;

public class FovChanger extends Module {
    private final NumberSetting fov = add(new NumberSetting("FOV", 90, 30, 110, 1));
    private int previous = 70;

    public FovChanger() {
        super("FOV Changer", "Sets your field of view (within the vanilla range).", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        previous = mc.options.fov().get();
        mc.options.fov().set(fov.intValue());
    }

    @Override
    protected void onDisable() {
        mc.options.fov().set(previous);
    }

    @Override
    public void onTick() {
        int target = fov.intValue();
        if (mc.options.fov().get() != target) {
            mc.options.fov().set(target);
        }
    }
}
