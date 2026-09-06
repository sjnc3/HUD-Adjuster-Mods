package com.example.examplemod.mixin;

import com.example.examplemod.config.ConfigManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.OverlayRegistry;
import net.neoforged.neoforge.client.gui.overlay.OverlayRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OverlayRenderer.class)
public class OverlayRendererMixin {
    @Redirect(
            method = "renderOverlays",
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/event/OverlayRegistry$OverlayEntry;render(Lnet/minecraft/client/gui/GuiGraphics;F)V")
    )
    private static void redirectOverlay(OverlayRegistry.OverlayEntry entry, GuiGraphics graphics, float partialTick) {
        ResourceLocation id = entry.id();
        ConfigManager.Point offset = ConfigManager.get(id.toString());
        if (offset.x() != 0 || offset.y() != 0) {
            graphics.pose().pushPose();
            graphics.pose().translate(offset.x(), offset.y(), 0);
            entry.render(graphics, partialTick);
            graphics.pose().popPose();
        } else {
            entry.render(graphics, partialTick);
        }
    }
}