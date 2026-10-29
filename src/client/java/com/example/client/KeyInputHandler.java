package com.uzclient.handler;

import com.uzclient.gui.ClickGuiScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static KeyBinding openGuiKey;

    public static void register() {
        // RIGHT_SHIFT tugmasiga ClickGUI ni bog'lash
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.uzclient.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT, // O'ng Shift
            "category.uzclient.visuals"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ClickGuiScreen());
                }
            }
        });
    }
}
