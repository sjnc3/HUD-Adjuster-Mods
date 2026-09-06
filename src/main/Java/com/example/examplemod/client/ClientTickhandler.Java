package com.example.examplemod.client;

import com.example.examplemod.ExampleMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = ExampleMod.MODID, value = Dist.CLIENT)
public class ClientTickHandler {
    public static final KeyMapping OPEN_KEY = new KeyMapping(
            "key.hudadjuster.open",
            GLFW.GLFW_KEY_C,
            "key.categories.hudadjuster"
    );

    @SubscribeEvent
    public static void onTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen == null && OPEN_KEY.isDown() && Screen.hasControlDown() && Screen.hasAltDown()) {
            mc.setScreen(new AdjustmentScreen());
        }
    }
}