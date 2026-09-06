package com.example.examplemod.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.neoforged.fml.loading.FMLPaths;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ConfigManager {
    private static final Gson GSON = new Gson();
    private static final Path PATH = FMLPaths.CONFIGDIR.get().resolve("hudadjuster.json");
    private static final Map<String, Point> OFFSETS = new HashMap<>();

    public static void load() {
        if (Files.exists(PATH)) {
            try (Reader reader = Files.newBufferedReader(PATH)) {
                Map<String, List<Integer>> map = GSON.fromJson(reader, new TypeToken<Map<String, List<Integer>>>(){}.getType());
                map.forEach((k, v) -> OFFSETS.put(k, new Point(v.get(0), v.get(1))));
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(PATH)) {
            Map<String, List<Integer>> map = new HashMap<>();
            OFFSETS.forEach((k, v) -> map.put(k, List.of(v.x, v.y)));
            GSON.toJson(map, writer);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static Point get(String id) {
        return OFFSETS.computeIfAbsent(id, k -> new Point(0, 0));
    }

    public static void set(String id, int x, int y) {
        OFFSETS.put(id, new Point(x, y));
        save();
    }

    public record Point(int x, int y) {}
}