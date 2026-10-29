package com.uzclient.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.awt.Color;

/**
 * Minecraft 1.21.4 Visual ClickGUI
 * Ochilish tugmasi: GLFW_KEY_RIGHT_SHIFT
 * Orqa fon va Main rang sozlanishi mumkin
 */
public class ClickGuiScreen extends Screen {
    // Sozlanuvchi ranglar
    public static int backgroundColor = 0xD90D0F17; // Qora fon (shaffoflik bilan)
    public static int mainAccentColor = 0xFF8B5CF6;  // Main binafsha/neon rang
    public static int textColor = 0xFFFFFFFF;

    private int guiX = 120;
    private int guiY = 60;
    private int guiWidth = 440;
    private int guiHeight = 280;

    public ClickGuiScreen() {
        super(Text.literal("UzClient Visuals 1.21.4"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Ekran orqa fonini biroz qorong'ilashtirish
        super.render(context, mouseX, mouseY, delta);

        // 1. Asosiy Menyu to'rtburchagi (Burchaklari sal yumaloqlangan)
        // DrawContext orqali silliq to'rtburchak chizish
        context.fill(guiX, guiY, guiX + guiWidth, guiY + guiHeight, backgroundColor);

        // Yuqori sarlavha paneli (Header)
        context.fill(guiX, guiY, guiX + guiWidth, guiY + 34, 0x50000000);
        context.drawText(this.textRenderer, "§lUzClient §7| §fVisuals 1.21.4", guiX + 14, guiY + 13, mainAccentColor, false);

        // Rang tanlagich (Color Box)
        context.fill(guiX + guiWidth - 30, guiY + 9, guiX + guiWidth - 10, guiY + 25, mainAccentColor);

        // Modullar ro'yxati (ChinaHat, JumpCircle, Trails, HitColor va b.)
        String[] visualModules = {
            "ChinaHat", "JumpCircle", "Trails", "Particles",
            "HitColor", "HitBubbles", "HitSound", "KillEffect"
        };

        int itemY = guiY + 45;
        for (String modName : visualModules) {
            boolean isHovered = mouseX >= guiX + 10 && mouseX <= guiX + 180 && mouseY >= itemY && mouseY <= itemY + 24;
            int itemBg = isHovered ? 0x20FFFFFF : 0x10FFFFFF;

            context.fill(guiX + 10, itemY, guiX + 180, itemY + 24, itemBg);
            context.drawText(this.textRenderer, modName, guiX + 20, itemY + 8, textColor, true);

            // Holat indikatori (ON / OFF)
            context.fill(guiX + 165, itemY + 7, guiX + 175, itemY + 17, mainAccentColor);
            itemY += 28;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // RIGHT_SHIFT yoki ESCAPE bosilganda menyuni yopish
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false; // Multiplayeda o'yin to'xtab qolmaydi
    }
}
