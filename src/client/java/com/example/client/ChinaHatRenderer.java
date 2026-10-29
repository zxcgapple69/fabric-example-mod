package com.uzclient.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

/**
 * 1.21.4 O'yinchi boshi ustida aylanuvchi Xitoy qalpoqchasi effekti
 */
public class ChinaHatRenderer {
    public static boolean enabled = true;
    public static float radius = 0.65f;
    public static float height = 0.3f;

    public static void renderChinaHat(MatrixStack matrices, PlayerEntity player, float tickDelta) {
        if (!enabled) return;

        matrices.push();
        // O'yinchi boshi tepasiga joylashtirish
        matrices.translate(0, player.getHeight() + 0.1, 0);

        // Yumshoq aylanish animatsiyasi
        float angle = (System.currentTimeMillis() % 3600) / 10.0f;
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Qalpoqning uchi (Markaziy cho'qqisi)
        int color = 0xFF8B5CF6; // Main accent color
        buffer.vertex(matrix, 0, height, 0).color(color);

        // Konus asosidagi aylanani chizish
        int segments = 32;
        for (int i = 0; i <= segments; i++) {
            float rad = (float) (i * Math.PI * 2 / segments);
            float x = (float) Math.sin(rad) * radius;
            float z = (float) Math.cos(rad) * radius;
            buffer.vertex(matrix, x, 0, z).color(color & 0x00FFFFFF | 0x80000000);
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.disableBlend();
        matrices.pop();
    }
}
