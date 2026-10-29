package com.soupvisual;

import com.soupvisual.client.feature.AmbientParticleFeature;
import com.soupvisual.client.feature.HudFeature;
import com.soupvisual.client.feature.JumpCircleFeature;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class SoupVisualClient implements ClientModInitializer {

    public static final String MOD_ID = "soupvisual";

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(HudFeature::render);

        WorldRenderEvents.END.register(context -> {
            JumpCircleFeature.render(context);
            AmbientParticleFeature.render(context);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            JumpCircleFeature.tick(client);
            AmbientParticleFeature.tick(client);
        });

        System.out.println("[SoupVisual] Mod loaded! Have fun :D");
    }
}
