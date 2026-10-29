package com.soupvisual.client.feature;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AmbientParticleFeature {

    private static final List<Particle> particles = new ArrayList<>();
    private static final Random rand = new Random();
    private static int spawnTimer = 0;

    private static final float[][] COLORS = {
        {0.65f, 0.35f, 1.0f},  // binafsha
        {0.35f, 0.85f, 1.0f},  // ko'k
        {0.4f,  0.9f,  0.4f},  // yashil
        {1.0f,  0.85f, 0.3f},  // sariq
        {1.0f,  0.45f, 0.45f}, // qizil
    };

    public static void tick(MinecraftClient client) {
        if (client.player == null) return;
        PlayerEntity player = client.player;

        spawnTimer++;
        if (spawnTimer >= 4) {
            spawnTimer = 0;
            double ox = (rand.nextDouble() - 0.5) * 3.0;
            double oz = (rand.nextDouble() - 0.5) * 3.0;
            double oy = rand.nextDouble() * 0.3;
            Vec3d spawnPos = player.getPos().add(ox, oy, oz);
            float[] col = COLORS[rand.nextInt(COLORS.length)];
            particles.add(new Particle(spawnPos, col));
        }

        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.tick();
            if (p.isDead()) it.remove();
        }
    }

    public static void render(WorldRenderContext context) {
        if (particles.isEmpty()) return;

        Vec3d cam = context.camera().getPos();
        MatrixStack matrices = context.matrixStack();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);

        Tessellator tessellator = Tessellator.getInstance();
        for (Particle p : particles) {
            p.render(matrices, tessellator, cam, context.camera());
        }

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    static class Particle {
        Vec3d pos;
        final float[] color;
        float age = 0;
        static final float MAX_AGE = 25f;
        float velY, velX, velZ, size;

        Particle(Vec3d pos, float[] color) {
            this.pos = pos;
            this.color = color;
            this.velY = 0.02f + (float)(Math.random() * 0.03f);
            this.velX = (float)(Math.random() - 0.5) * 0.01f;
            this.velZ = (float)(Math.random() - 0.5) * 0.01f;
            this.size = 0.04f + (float)(Math.random() * 0.04f);
        }

        void tick() {
            age++;
            pos = pos.add(velX, velY, velZ);
            velY *= 0.98f;
        }

        boolean isDead() { return age >= MAX_AGE; }

        void render(MatrixStack matrices, Tessellator tessellator, Vec3d cam,
                    net.minecraft.client.render.Camera camera) {
            float alpha = (1f - age / MAX_AGE) * 0.85f;
            if (alpha <= 0) return;

            matrices.push();
            matrices.translate(pos.x - cam.x, pos.y - cam.y, pos.z - cam.z);
            matrices.multiply(camera.getRotation());

            Matrix4f mat = matrices.peek().getPositionMatrix();
            BufferBuilder buf = tessellator.begin(
                VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            float r = color[0], g = color[1], b = color[2];
            float s = size;
            buf.vertex(mat, -s, -s, 0).color(r, g, b, alpha);
            buf.vertex(mat,  s, -s, 0).color(r, g, b, alpha);
            buf.vertex(mat,  s,  s, 0).color(r, g, b, alpha);
            buf.vertex(mat, -s,  s, 0).color(r, g, b, alpha);
