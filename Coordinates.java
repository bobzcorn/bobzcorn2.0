package com.ember.client.module.modules;

import com.ember.client.module.Category;
import com.ember.client.module.Module;
import com.ember.client.setting.BoolSetting;
import com.ember.client.util.HudText;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Coordinates extends Module {
    private final BoolSetting showFacing = add(new BoolSetting("Show facing", true));

    public Coordinates() {
        super("Coordinates", "Shows your block position and facing direction.", Category.HUD);
    }

    @Override
    public void renderHud(GuiGraphicsExtractor graphics) {
        if (mc.player == null) return;
        String text = String.format("XYZ: %d / %d / %d",
                (int) Math.floor(mc.player.getX()),
                (int) Math.floor(mc.player.getY()),
                (int) Math.floor(mc.player.getZ()));
        if (showFacing.get()) {
            text += "  " + mc.player.getDirection().getName();
        }
        HudText.line(graphics, text);
    }
}
