package com.example.examplemod;

import com.example.examplemod.config.ConfigManager;
import net.neoforged.fml.common.Mod;

@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "hudadjuster";

    public ExampleMod() {
        ConfigManager.load();
    }
}