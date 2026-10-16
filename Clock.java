package com.ember.client.module.modules;

import com.ember.client.module.Category;
import com.ember.client.module.Module;
import com.ember.client.setting.BoolSetting;
import com.ember.client.util.HudText;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Clock extends Module {
    private final BoolSetting twentyFourHour = add(new BoolSetting("24 hour", false));

    private static final DateTimeFormatter H24 = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter H12 = DateTimeFormatter.ofPattern("h:mm a");

    public Clock() {
        super("Clock", "Shows your real-world local time.", Category.HUD);
    }

    @Override
    public void renderHud(GuiGraphicsExtractor graphics) {
        HudText.line(graphics, LocalTime.now().format(twentyFourHour.get() ? H24 : H12));
    }
}
