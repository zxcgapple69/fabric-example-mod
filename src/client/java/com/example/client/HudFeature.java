package com.soupvisual.client.feature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;

public class HudFeature {

    private static int kills = 0;
    private static int deaths = 0;

    public static void addKill()  { kills++; }
    public static void addDeath() { deaths++; }

    public static void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null  client.world == null) return;
        if (client.options.hudHidden) return;

        PlayerEntity player = client.player;
        TextRenderer tr = client.textRenderer;

        int px = 6, py = 6, pw = 130, ph = 110;

        // Panel background
        context.fill(px, py, px + pw, py + ph, 0xBB0A0A12);
        context.drawBorder(px, py, pw, ph, 0xFF2A1A5E);

        // Title
        context.drawText(tr, "§5§lSoup §7Visual", px + 6, py + 6, 0xFFFFFF, false);
        drawLine(context, px + 4, py + 18, pw - 8, 0x662A1A5E);

        // HP
        float hp = player.getHealth();
        float maxHp = player.getMaxHealth();
        int hpPct = (int)(hp / maxHp * 100);
        String hpCol = hpPct > 60 ? "§a" : hpPct > 30 ? "§e" : "§c";
        context.drawText(tr, "§7HP  " + hpCol + (int)hp + " §8/ §7" + (int)maxHp,
                px + 6, py + 23, 0xFFFFFF, false);
        drawBar(context, px + 6, py + 32, 118, 4, hp / maxHp, 0xFF1B5E20, 0xFF4CAF50);

        // Armor
        int armor = player.getArmor();
        context.drawText(tr, "§7Arm §b" + armor + " §8/ §720",
                px + 6, py + 40, 0xFFFFFF, false);
        drawBar(context, px + 6, py + 49, 118, 4, armor / 20f, 0xFF0D47A1, 0xFF2196F3);

        drawLine(context, px + 4, py + 57, pw - 8, 0x662A1A5E);

        // K/D
        float kd = deaths == 0 ? kills : (float) kills / deaths;
        context.drawText(tr, "§5K §f" + kills + "  §cD §f" + deaths,
                px + 6, py + 62, 0xFFFFFF, false);
        context.drawText(tr, "§7K/D §e" + String.format("%.1f", kd),
                px + 66, py + 62, 0xFFFFFF, false);

        // FPS
        int fps = client.getCurrentFps();
        String fpsCol = fps > 100 ? "§a" : fps > 60 ? "§e" : "§c";
        context.drawText(tr, "§7FPS " + fpsCol + fps, px + 6, py + 74, 0xFFFFFF, false);

        // Ping
        int ping = getPing(client);
        String pingCol = ping < 50 ? "§a" : ping < 100 ? "§e" : "§c";
        context.drawText(tr, "§7Ping " + pingCol + ping + "ms",
                px + 60, py + 74, 0xFFFFFF, false);

        drawLine(context, px + 4, py + 85, pw - 8, 0x662A1A5E);

        // Circle style
        String styleName = JumpCircleFeature.style == 0 ? "§5Soup" : "§bPortal";
        context.drawText(tr, "§7Circle " + styleName, px + 6, py + 90, 0xFFFFFF, false);
        context.drawText(tr, "§8[V] toggle", px + 6, py + 101, 0xFFFFFF, false);
    }

    private static void drawBar(DrawContext ctx, int x, int y, int w, int h,
                                 float fill, int bgColor, int fillColor) {
        ctx.fill(x, y, x + w, y + h, bgColor);
        ctx.fill(x, y, x + (int)(w * Math.min(fill, 1f)), y + h, fillColor);
    }

    private static void drawLine(DrawContext ctx, int x, int y, int w, int color) {
        ctx.fill(x, y, x + w, y + 1, color);
    }

    private static int getPing(MinecraftClient client) {
        ClientPlayNetworkHandler handler = client.getNetworkHandler();
        if (handler == null  client.player == null) return 0;
        PlayerListEntry entry = handler.getPlayerListEntry(client.player.getUuid());
        return entry != null ? entry.getLatency() : 0;
    }
                                 }
