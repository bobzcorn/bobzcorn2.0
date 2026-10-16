package com.ember.client.gui;

import com.ember.client.Config;
import com.ember.client.module.Category;
import com.ember.client.module.Module;
import com.ember.client.module.ModuleManager;
import com.ember.client.setting.BoolSetting;
import com.ember.client.setting.NumberSetting;
import com.ember.client.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Dark red category-column menu.
 * Left click a module to toggle it. Right click a module to expand its settings.
 */
public class ClickGuiScreen extends Screen {
    // Palette
    private static final int OVERLAY = 0xB0050102;
    private static final int PANEL = 0xF0140508;
    private static final int HEADER = 0xFF2A0A10;
    private static final int ACCENT = 0xFFE02B3D;
    private static final int ACCENT_DIM = 0xFF7A1824;
    private static final int ROW_HOVER = 0x40E02B3D;
    private static final int TEXT = 0xFFF2E6E8;
    private static final int TEXT_DIM = 0xFF9A7F83;
    private static final int GREEN = 0xFF3DDC84;

    private static final int COL_W = 132;
    private static final int GAP = 8;
    private static final int HEADER_H = 22;
    private static final int ROW_H = 16;
    private static final int SETTING_H = 16;
    private static final int FOOTER_H = 34;

    private final ModuleManager manager;
    private final Set<Module> expanded = new HashSet<>();

    private record Hit(int x, int y, int w, int h, Module module, Setting<?> setting) {
        boolean contains(double mx, double my) {
            return mx >= x && mx < x + w && my >= y && my < y + h;
        }
    }

    private final List<Hit> hits = new ArrayList<>();
    private NumberSetting dragging;
    private Hit draggingHit;

    public ClickGuiScreen(ModuleManager manager) {
        super(Component.literal("Ember Client"));
        this.manager = manager;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, width, height, OVERLAY);
        hits.clear();

        Category[] cats = Category.values();
        int totalW = cats.length * COL_W + (cats.length - 1) * GAP;
        int startX = (width - totalW) / 2;
        int top = 28;

        // Title
        g.text(font, "EMBER CLIENT", startX, 10, ACCENT, false);
        g.text(font, "v1.0  |  Right Click = settings", startX + 80, 10, TEXT_DIM, false);

        Module hovered = null;
        for (int i = 0; i < cats.length; i++) {
            int x = startX + i * (COL_W + GAP);
            Module h = drawColumn(g, cats[i], x, top, mouseX, mouseY);
            if (h != null) hovered = h;
        }

        drawFooter(g, hovered, startX, totalW);
    }

    private Module drawColumn(GuiGraphicsExtractor g, Category cat, int x, int top, int mx, int my) {
        List<Module> mods = manager.in(cat);

        int bodyH = 6;
        for (Module m : mods) {
            bodyH += ROW_H;
            if (expanded.contains(m)) bodyH += m.settings().size() * SETTING_H;
        }
        int maxBottom = height - FOOTER_H - 14;
        int colH = Math.min(HEADER_H + bodyH, maxBottom - top);

        // Panel
        g.fill(x - 1, top - 1, x + COL_W + 1, top + colH + 1, ACCENT_DIM);
        g.fill(x, top, x + COL_W, top + colH, PANEL);
        g.fill(x, top, x + COL_W, top + HEADER_H, HEADER);
        g.fill(x, top + HEADER_H - 1, x + COL_W, top + HEADER_H, ACCENT);
        g.text(font, cat.label, x + 8, top + 7, ACCENT, false);

        Module hovered = null;
        int y = top + HEADER_H + 3;
        for (Module m : mods) {
            if (y + ROW_H > top + colH) break;
            boolean over = mx >= x && mx < x + COL_W && my >= y && my < y + ROW_H;
            if (over) {
                g.fill(x + 1, y, x + COL_W - 1, y + ROW_H, ROW_HOVER);
                hovered = m;
            }
            if (m.isEnabled()) {
                g.fill(x + 1, y, x + 3, y + ROW_H, ACCENT);
            }
            g.text(font, m.name, x + 9, y + 4, m.isEnabled() ? TEXT : TEXT_DIM, false);
            if (!m.settings().isEmpty()) {
                g.text(font, expanded.contains(m) ? "-" : "+", x + COL_W - 12, y + 4, TEXT_DIM, false);
            }
            hits.add(new Hit(x, y, COL_W, ROW_H, m, null));
            y += ROW_H;

            if (expanded.contains(m)) {
                for (Setting<?> s : m.settings()) {
                    if (y + SETTING_H > top + colH) break;
                    drawSetting(g, s, x, y);
                    hits.add(new Hit(x + 6, y, COL_W - 12, SETTING_H, m, s));
                    y += SETTING_H;
                }
            }
        }
        return hovered;
    }

    private void drawSetting(GuiGraphicsExtractor g, Setting<?> s, int x, int y) {
        g.fill(x + 4, y, x + COL_W - 4, y + SETTING_H, 0x30000000);
        if (s instanceof BoolSetting b) {
            g.text(font, s.name, x + 10, y + 4, TEXT_DIM, false);
            int px = x + COL_W - 30;
            g.fill(px, y + 4, px + 18, y + 12, b.get() ? 0xFF1E5E3A : 0xFF3A1519);
            int knob = b.get() ? px + 10 : px;
            g.fill(knob, y + 3, knob + 8, y + 13, b.get() ? GREEN : ACCENT_DIM);
        } else if (s instanceof NumberSetting n) {
            int barX = x + 10;
            int barW = COL_W - 20;
            g.fill(barX, y + 1, barX + (int) (barW * n.fraction()), y + SETTING_H - 1, 0x60E02B3D);
            g.text(font, s.name + ": " + n.intValue(), barX + 2, y + 4, TEXT, false);
        }
    }

    private void drawFooter(GuiGraphicsExtractor g, Module hovered, int x, int totalW) {
        int y = height - FOOTER_H - 6;
        g.fill(x - 1, y - 1, x + totalW + 1, y + FOOTER_H + 1, ACCENT_DIM);
        g.fill(x, y, x + totalW, y + FOOTER_H, PANEL);
        g.fill(x, y, x + 3, y + FOOTER_H, ACCENT);
        if (hovered != null) {
            g.text(font, hovered.name.toUpperCase(), x + 10, y + 7, ACCENT, false);
            g.text(font, hovered.description, x + 10, y + 20, TEXT, false);
        } else {
            g.text(font, "Hover a module to see what it does.", x + 10, y + 13, TEXT_DIM, false);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mx = event.x();
        double my = event.y();
        int button = event.button();

        for (Hit hit : hits) {
            if (!hit.contains(mx, my)) continue;

            if (hit.setting == null) {
                if (button == 0) {
                    hit.module.toggle();
                } else if (button == 1 && !hit.module.settings().isEmpty()) {
                    if (!expanded.remove(hit.module)) expanded.add(hit.module);
                }
                return true;
            }

            if (hit.setting instanceof BoolSetting b && button == 0) {
                b.toggle();
                return true;
            }
            if (hit.setting instanceof NumberSetting n && button == 0) {
                dragging = n;
                draggingHit = hit;
                applyDrag(mx);
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (dragging != null) {
            applyDrag(event.x());
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        dragging = null;
        draggingHit = null;
        return super.mouseReleased(event);
    }

    private void applyDrag(double mx) {
        if (dragging == null || draggingHit == null) return;
        double frac = (mx - draggingHit.x) / (double) draggingHit.w;
        frac = Math.max(0, Math.min(1, frac));
        dragging.set(dragging.min + frac * (dragging.max - dragging.min));
    }

    @Override
    public void onClose() {
        Config.save(manager);
        super.onClose();
    }
}
