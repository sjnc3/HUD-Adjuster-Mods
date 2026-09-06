package com.example.examplemod.mixin;

import com.example.examplemod.config.ConfigManager;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Gui.class)
public class GuiMixin {
    @ModifyConstant(method = "renderHealth", constant = @Constant(intValue = -91))
    private int modHealthX(int orig) { return orig + ConfigManager.get("minecraft:health").x(); }
    @ModifyConstant(method = "renderHealth", constant = @Constant(intValue = -69))
    private int modHealthY(int orig) { return orig + ConfigManager.get("minecraft:health").y(); }

    @ModifyConstant(method = "renderFood", constant = @Constant(intValue = -91))
    private int modFoodX(int orig) { return orig + ConfigManager.get("minecraft:food").x(); }
    @ModifyConstant(method = "renderFood", constant = @Constant(intValue = -49))
    private int modFoodY(int orig) { return orig + ConfigManager.get("minecraft:food").y(); }

    @ModifyConstant(method = "renderArmor", constant = @Constant(intValue = -91))
    private int modArmorX(int orig) { return orig + ConfigManager.get("minecraft:armor").x(); }
    @ModifyConstant(method = "renderArmor", constant = @Constant(intValue = -89))
    private int modArmorY(int orig) { return orig + ConfigManager.get("minecraft:armor").y(); }

    @ModifyConstant(method = "renderExperience", constant = @Constant(intValue = -91))
    private int modExpX(int orig) { return orig + ConfigManager.get("minecraft:experience").x(); }
    @ModifyConstant(method = "renderExperience", constant = @Constant(intValue = -29))
    private int modExpY(int orig) { return orig + ConfigManager.get("minecraft:experience").y(); }
}