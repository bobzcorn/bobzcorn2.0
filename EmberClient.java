package com.ember.client;

import com.ember.client.gui.ClickGuiScreen;
import com.ember.client.module.ModuleManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmberClient implements ClientModInitializer {
    public static final String MOD_ID = "ember_client";
    public static final Logger LOGGER = LoggerFactory.getLogger("Ember Client");

    public static ModuleManager modules;
    public static KeyMapping guiKey;
    public static KeyMapping zoomKey;
    public static KeyMapping macroKey;

    @Override
    public void onInitializeClient() {
        modules = new ModuleManager();
        Config.load(modules);

        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(MOD_ID, "main"));

        guiKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.ember_client.open_gui", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, category));
        zoomKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.ember_client.zoom", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, category));
        macroKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.ember_client.chat_macro", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (guiKey.consumeClick()) {
                client.setScreen(new ClickGuiScreen(modules));
            }
            modules.tick();
        });

        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(MOD_ID, "hud"),
                (graphics, tickCounter) -> {
                    if (!Minecraft.getInstance().options.hideGui) {
                        modules.renderHud(graphics);
                    }
                });

        // Modules that were saved as enabled re-apply their effect once the game is up.
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> modules.applyLoadedState());
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> Config.save(modules));

        LOGGER.info("Ember Client loaded. Press Right Shift to open the menu.");
    }
}
