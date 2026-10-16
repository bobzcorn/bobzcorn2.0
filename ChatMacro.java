package com.ember.client.module.modules;

import com.ember.client.EmberClient;
import com.ember.client.module.Category;
import com.ember.client.module.Module;
import com.ember.client.setting.NumberSetting;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Press the macro key (default V) to send one saved chat line. One key press sends exactly one message,
 * and presses are rate limited. Edit the lines in config/ember_macros.txt (one message per line;
 * a line starting with / is sent as a command).
 */
public class ChatMacro extends Module {
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("ember_macros.txt");
    private static final long COOLDOWN_MS = 1500;

    private final NumberSetting slot = add(new NumberSetting("Macro slot", 1, 1, 9, 1));
    private long lastSent;

    public ChatMacro() {
        super("Chat Macro", "Press the macro key (default V) to send a saved chat line.", Category.UTILITY);
    }

    @Override
    protected void onEnable() {
        if (!Files.exists(FILE)) {
            try {
                Files.writeString(FILE, "gg\nbrb\n/home\n");
            } catch (IOException e) {
                EmberClient.LOGGER.warn("Could not create macros file", e);
            }
        }
    }

    @Override
    public void onFrame() {
        if (EmberClient.macroKey == null) return;
        while (EmberClient.macroKey.consumeClick()) {
            if (isEnabled()) send();
        }
    }

    private void send() {
        if (mc.player == null || mc.getConnection() == null || mc.screen != null) return;
        long now = System.currentTimeMillis();
        if (now - lastSent < COOLDOWN_MS) return;

        List<String> lines;
        try {
            lines = Files.readAllLines(FILE);
        } catch (IOException e) {
            mc.gui.getChat().addMessage(Component.literal("[Ember] Could not read config/ember_macros.txt"));
            return;
        }
        int index = slot.intValue() - 1;
        if (index < 0 || index >= lines.size() || lines.get(index).isBlank()) {
            mc.gui.getChat().addMessage(Component.literal("[Ember] Macro slot " + slot.intValue() + " is empty."));
            return;
        }
        String text = lines.get(index).strip();
        lastSent = now;
        if (text.startsWith("/")) {
            mc.getConnection().sendCommand(text.substring(1));
        } else {
            mc.getConnection().sendChat(text);
        }
    }
}
