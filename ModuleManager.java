package com.ember.client.module;

import com.ember.client.module.modules.*;
import com.ember.client.util.HudText;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        // Render
        modules.add(new Fullbright());
        modules.add(new FovChanger());
        // HUD
        modules.add(new FpsCounter());
        modules.add(new Coordinates());
        modules.add(new PingDisplay());
        modules.add(new Clock());
        modules.add(new ArmorHud());
        // Movement
        modules.add(new ToggleSprint());
        modules.add(new ToggleSneak());
        modules.add(new AutoWalk());
        // Utility
        modules.add(new Zoom());
        modules.add(new WeatherNotifier());
        modules.add(new ChatMacro());
    }

    public List<Module> all() {
        return modules;
    }

    public List<Module> in(Category category) {
        List<Module> result = new ArrayList<>();
        for (Module m : modules) {
            if (m.category == category) result.add(m);
        }
        return result;
    }

    public void tick() {
        for (Module m : modules) {
            if (m.isEnabled()) m.onTick();
        }
    }

    public void renderHud(GuiGraphicsExtractor graphics) {
        HudText.begin();
        for (Module m : modules) {
            m.onFrame();
            if (m.isEnabled()) m.renderHud(graphics);
        }
    }

    public void applyLoadedState() {
        for (Module m : modules) m.applyLoadedState();
    }
}
