package com.ember.client.module.modules;

import com.ember.client.module.Category;
import com.ember.client.module.Module;
import com.ember.client.util.HudText;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;

public class PingDisplay extends Module {
    public PingDisplay() {
        super("Ping Display", "Shows your latency to the current server.", Category.HUD);
    }

    @Override
    public void renderHud(GuiGraphicsExtractor graphics) {
        if (mc.player == null || mc.getConnection() == null) return;
        PlayerInfo info = mc.getConnection().getPlayerInfo(mc.player.getUUID());
        if (info == null) return;
        HudText.line(graphics, "Ping: " + info.getLatency() + " ms");
    }
}
