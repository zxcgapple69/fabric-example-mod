package com.uzclient.visuals;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.List;

/**
 * Sakraganda yerda kengayuvchi neon to'lqin halqalari (JumpCircle)
 */
public class JumpCircleFeature {
    public static class Circle {
        public Vec3d pos;
        public float radius = 0.1f;
        public float maxRadius = 1.6f;
        public float alpha = 1.0f;

        public Circle(Vec3d pos) {
            this.pos = pos;
        }
    }

    public static final List<Circle> activeCircles = new ArrayList<>();

    public static void onPlayerJump(Vec3d groundPos) {
        activeCircles.add(new Circle(groundPos));
    }

    public static void updateAndRender(MatrixStack matrices) {
        activeCircles.removeIf(c -> c.alpha <= 0.05f);
        for (Circle c : activeCircles) {
            c.radius += 0.04f;
            c.alpha -= 0.02f;
            // 3D Matrix orqali halqani yerga chizish
        }
    }
}
