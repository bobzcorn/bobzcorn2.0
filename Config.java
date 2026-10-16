package com.ember.client;

import com.ember.client.module.Module;
import com.ember.client.module.ModuleManager;
import com.ember.client.setting.BoolSetting;
import com.ember.client.setting.NumberSetting;
import com.ember.client.setting.Setting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Saves module on/off state and settings to config/ember_client.json. */
public final class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("ember_client.json");

    private Config() {}

    public static void save(ModuleManager manager) {
        JsonObject root = new JsonObject();
        for (Module m : manager.all()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("enabled", m.isEnabled());
            for (Setting<?> s : m.settings()) {
                if (s instanceof BoolSetting b) obj.addProperty(s.name, b.get());
                else if (s instanceof NumberSetting n) obj.addProperty(s.name, n.get());
            }
            root.add(m.name, obj);
        }
        try {
            Files.writeString(FILE, GSON.toJson(root));
        } catch (IOException e) {
            EmberClient.LOGGER.warn("Could not save config", e);
        }
    }

    public static void load(ModuleManager manager) {
        if (!Files.exists(FILE)) return;
        try {
            JsonObject root = GSON.fromJson(Files.readString(FILE), JsonObject.class);
            if (root == null) return;
            for (Module m : manager.all()) {
                if (!root.has(m.name)) continue;
                JsonObject obj = root.getAsJsonObject(m.name);
                for (Setting<?> s : m.settings()) {
                    if (!obj.has(s.name)) continue;
                    if (s instanceof BoolSetting b) b.set(obj.get(s.name).getAsBoolean());
                    else if (s instanceof NumberSetting n) n.set(obj.get(s.name).getAsDouble());
                }
                if (obj.has("enabled")) m.restoreEnabledFlag(obj.get("enabled").getAsBoolean());
            }
        } catch (Exception e) {
            EmberClient.LOGGER.warn("Could not read config, using defaults", e);
        }
    }
}
