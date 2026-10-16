package com.ember.client.module.modules;

import com.ember.client.module.Category;
import com.ember.client.module.Module;
import com.ember.client.util.HudText;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class FpsCounter extends Module {
    public FpsCounter() {
        super("FPS Counter", "Shows your current frames per second.", Category.HUD);
    }

    @Override
    public void renderHud(GuiGraphicsExtractor graphics) {
        HudText.line(graphics, "FPS: " + mc.getFps());
    }
}
