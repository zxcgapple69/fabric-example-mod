package com.soupvisual.client.feature;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JumpCircleFeature {

    public static int style = 0; // 0 = Soup, 1 = Portal

    private static final List<JumpCircle> circles = new ArrayList<>();
    private static boolean wasOnGround = true;

    public static void tick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        PlayerEntity player = client.player;
        boolean onGround = player.isOnGround();

        // Yerga tushganda circle chiqar
        if (onGround && !wasOnGround) {
            circles.add(new JumpCircle(player.getPos(), style));
        }

        wasOnGround = onGround;

        Iterator<JumpCircle> it = circles.iterator();
        while (it.hasNext()) {
            JumpCircle c = it.next();
            c.tick();
            if (c.isDead()) it.remove();
        }
    }

    public static void render(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        Vec3d cam = context.camera().getPos();
        MatrixStack matrices = context.matrixStack();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);

        Tessellator tessellator = Tessellator.getInstance();
        for (JumpCircle circle : circles) {
            circle.render(matrices, tessellator, cam);
        }

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    static class JumpCircle {
        final Vec3d pos;
        final int style;
        float age = 0;
        static final float MAX_AGE = 30f;

        JumpCircle(Vec3d pos, int style) {
            this.pos = pos.add(0, 0.05, 0);
            this.style = style;
        }

        void tick() { age++; }
        boolean isDead() { return age >= MAX_AGE; }

        void render(MatrixStack matrices, Tessellator tessellator, Vec3d cam) {
            float t = age / MAX_AGE;
            float alpha = 1f - t;
            float radius = 0.3f + t * 1.4f;

            matrices.push();
            matrices.translate(pos.x - cam.x, pos.y - cam.y, pos.z - cam.z);

            BufferBuilder buf = tessellator.begin(
                VertexFormat.DrawMode.DEBUG_LINE_STRIP,
                VertexFormats.POSITION_COLOR
            );
            Matrix4f mat = matrices.peek().getPositionMatrix();

            int segments = 48;
            for (int i = 0; i <= segments; i++) {
                float angle = (float)(i * 2 * Math.PI / segments);if (style == 0) {
                    // Soup — binafsha pulslovchi ring
                    float pulse = MathHelper.sin(angle * 3 + age * 0.2f) * 0.15f;
                    float rad = radius + pulse;
                    float x = MathHelper.cos(angle) * rad;
                    float z = MathHelper.sin(angle) * rad;
                    float r = 0.65f + pulse, g = 0.3f, b = 1.0f;
                    float a = alpha * (0.7f + pulse);
                    buf.vertex(mat, x, 0, z).color(r, g, b, Math.min(a, 1f));
                } else {
                    // Portal — ko'k aylanuvchi ring
                    float x = MathHelper.cos(angle + age * 0.08f) * radius;
                    float z = MathHelper.sin(angle + age * 0.08f) * radius;
                    float wave = (MathHelper.sin(angle * 2 - age * 0.15f) + 1) * 0.5f;
                    float r = 0.2f + wave * 0.3f, g = 0.7f + wave * 0.3f, b = 1.0f;
                    buf.vertex(mat, x, 0, z).color(r, g, b, alpha * 0.85f);
                }
            }

            RenderSystem.lineWidth(2.0f);
            BufferRenderer.drawWithGlobalProgram(buf.end());
            matrices.pop();
        }
    }
}
