package com.example.examplemod.client;

import com.example.examplemod.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.OverlayRegistry;
import org.lwjgl.glfw.GLFW;
import java.util.*;

public class AdjustmentScreen extends Screen {
    private final List<HudElement> elements = new ArrayList<>();
    private HudElement selected = null;
    private ConfigManager.Point dragOffset = null;
    private int scrollOffset = 0;

    public AdjustmentScreen() {
        super(Component.literal("HUD 调节器"));
        elements.add(new HudElement("minecraft:health", "生命值", -91, -69, 81, 9));
        elements.add(new HudElement("minecraft:food", "饥饿值", -91, -49, 81, 9));
        elements.add(new HudElement("minecraft:armor", "护甲值", -91, -89, 161, 9));
        elements.add(new HudElement("minecraft:experience", "经验值", -91, -29, 182, 5));
        for (OverlayRegistry.OverlayEntry entry : OverlayRegistry.getOverlays()) {
            String id = entry.id().toString();
            elements.add(new HudElement(id, id, 0, 0, 100, 20));
        }
        if (!elements.isEmpty()) selected = elements.get(0);
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float delta) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gui != null) mc.gui.render(gui, delta);
        gui.fill(0, 0, this.width, this.height, 0x60000000);
        int leftX = 10, topY = 20;
        gui.drawString(mc.font, "=== HUD 列表 ===", leftX, topY, 0xFFFFFF);
        topY += 15;
        int visible = Math.min(elements.size(), 20);
        for (int i = scrollOffset; i < scrollOffset + visible && i < elements.size(); i++) {
            HudElement e = elements.get(i);
            int color = e.equals(selected) ? 0xFFFF00 : 0xCCCCCC;
            gui.drawString(mc.font, e.name(), leftX, topY, color);
            topY += 12;
        }
        if (selected != null) {
            ConfigManager.Point offset = ConfigManager.get(selected.id());
            int x, y;
            if (selected.id().startsWith("minecraft:")) {
                int sw = mc.getWindow().getGuiScaledWidth();
                int sh = mc.getWindow().getGuiScaledHeight();
                x = sw / 2 + selected.defaultX() + offset.x();
                y = sh + selected.defaultY() + offset.y();
            } else {
                x = offset.x();
                y = offset.y();
            }
            gui.fill(x, y, x + selected.width(), y + selected.height(), 0x80FF00FF);
            gui.renderOutline(x, y, selected.width(), selected.height(), 0xFFFF00FF);
            gui.drawString(mc.font, "拖拽我", x + 5, y + 5, 0xFFFFFF);
            String pos = String.format("X: %d  Y: %d", offset.x(), offset.y());
            gui.drawString(mc.font, pos, 10, this.height - 30, 0xFFFFFF);
        }
        gui.drawString(mc.font, "方向键微调  |  拖拽移动  |  ESC关闭", 10, this.height - 15, 0xAAAAAA);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int btn) {
        int y = 35;
        for (int i = scrollOffset; i < scrollOffset + 20 && i < elements.size(); i++) {
            HudElement e = elements.get(i);
            if (mx >= 10 && mx <= 150 && my >= y && my <= y + 10) {
                selected = e;
                return true;
            }
            y += 12;
        }
        if (selected != null) {
            ConfigManager.Point off = ConfigManager.get(selected.id());
            int x, yPos;
            Minecraft mc = Minecraft.getInstance();
            if (selected.id().startsWith("minecraft:")) {
                int sw = mc.getWindow().getGuiScaledWidth();
                int sh = mc.getWindow().getGuiScaledHeight();
                x = sw / 2 + selected.defaultX() + off.x();
                yPos = sh + selected.defaultY() + off.y();
            } else {
                x = off.x();
                yPos = off.y();
            }
            if (mx >= x && mx <= x + selected.width() && my >= yPos && my <= yPos + selected.height()) {
                dragOffset = new ConfigManager.Point((int)mx - x, (int)my - yPos);
                return true;
            }
        }
        return super.mouseClicked(mx, my, btn);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        if (dragOffset != null && selected != null) {
            int newX, newY;
            Minecraft mc = Minecraft.getInstance();
            if (selected.id().startsWith("minecraft:")) {
                int sw = mc.getWindow().getGuiScaledWidth();
                int sh = mc.getWindow().getGuiScaledHeight();
                newX = (int)mx - sw / 2 - selected.defaultX() - dragOffset.x();
                newY = (int)my - sh - selected.defaultY() - dragOffset.y();
            } else {
                newX = (int)mx - dragOffset.x();
                newY = (int)my - dragOffset.y();
            }
            ConfigManager.set(selected.id(), newX, newY);
            return true;
        }
        return super.mouseDragged(mx, my, btn, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int btn) {
        dragOffset = null;
        return super.mouseReleased(mx, my, btn);
    }

    @Override
    public boolean keyPressed(int key, int scan, int mods) {
        if (selected != null) {
            ConfigManager.Point p = ConfigManager.get(selected.id());
            int dx = 0, dy = 0;
            if (key == GLFW.GLFW_KEY_UP) dy = -1;
            else if (key == GLFW.GLFW_KEY_DOWN) dy = 1;
            else if (key == GLFW.GLFW_KEY_LEFT) dx = -1;
            else if (key == GLFW.GLFW_KEY_RIGHT) dx = 1;
            if (dx != 0 || dy != 0) {
                ConfigManager.set(selected.id(), p.x() + dx, p.y() + dy);
                return true;
            }
        }
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }
        return super.keyPressed(key, scan, mods);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double dx, double dy) {
        scrollOffset = (int) Math.max(0, Math.min(elements.size() - 20, scrollOffset - dy));
        return true;
    }

    @Override
    public boolean isPauseScreen() { return false; }

    private record HudElement(String id, String name, int defaultX, int defaultY, int width, int height) {}
}