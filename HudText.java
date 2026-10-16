package com.ember.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Stacks HUD text lines in the top-left corner so HUD modules never overlap. */
public final class HudText {
    private static int nextY;

    private HudText() {}

    public static void begin() {
        nextY = 4;
    }

    public static void line(GuiGraphicsExtractor graphics, String text) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.font.width(text);
        graphics.fill(2, nextY - 2, 8 + width, nextY + 10, 0x90140508);
        graphics.fill(2, nextY - 2, 4, nextY + 10, 0xFFE02B3D);
        graphics.text(mc.font, text, 7, nextY, 0xFFFFFFFF, false);
        nextY += 14;
    }
}
