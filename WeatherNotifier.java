package com.ember.client.module.modules;

import com.ember.client.module.Category;
import com.ember.client.module.Module;
import net.minecraft.network.chat.Component;

/** Prints a chat message to you only (nothing is sent to the server) when the weather changes. */
public class WeatherNotifier extends Module {
    private boolean wasRaining;
    private boolean wasThundering;
    private boolean primed;

    public WeatherNotifier() {
        super("Weather Notifier", "Tells you in chat when rain or thunder starts or stops.", Category.UTILITY);
    }

    @Override
    protected void onEnable() {
        primed = false;
    }

    @Override
    public void onTick() {
        if (mc.level == null) {
            primed = false;
            return;
        }
        boolean raining = mc.level.isRaining();
        boolean thundering = mc.level.isThundering();
        if (primed) {
            if (thundering != wasThundering) {
                notify(thundering ? "Thunderstorm started." : "Thunderstorm ended.");
            } else if (raining != wasRaining) {
                notify(raining ? "It started raining." : "The rain stopped.");
            }
        }
        wasRaining = raining;
        wasThundering = thundering;
        primed = true;
    }

    private void notify(String text) {
        mc.gui.getChat().addMessage(Component.literal("[Ember] " + text));
    }
}
