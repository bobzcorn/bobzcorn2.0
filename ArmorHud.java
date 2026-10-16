package com.ember.client.module.modules;

import com.ember.client.module.Category;
import com.ember.client.module.Module;
import com.ember.client.setting.BoolSetting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ArmorHud extends Module {
    private final BoolSetting showDurability = add(new BoolSetting("Show durability", true));

    private static final EquipmentSlot[] SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public ArmorHud() {
        super("Armor HUD", "Shows your equipped armor and its durability.", Category.HUD);
    }

    @Override
    public void renderHud(GuiGraphicsExtractor graphics) {
        if (mc.player == null) return;
        int x = mc.getWindow().getGuiScaledWidth() - 22;
        int y = mc.getWindow().getGuiScaledHeight() / 2 - 40;
        for (EquipmentSlot slot : SLOTS) {
            ItemStack stack = mc.player.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                graphics.item(stack, x, y);
                if (showDurability.get() && stack.isDamageableItem()) {
                    int left = stack.getMaxDamage() - stack.getDamageValue();
                    String text = Integer.toString(left);
                    graphics.text(mc.font, text, x - mc.font.width(text) - 3, y + 4, 0xFFFFFFFF, true);
                }
            }
            y += 20;
        }
    }
}
