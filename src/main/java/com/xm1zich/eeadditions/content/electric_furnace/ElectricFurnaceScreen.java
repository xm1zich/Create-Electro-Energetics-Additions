package com.xm1zich.eeadditions.content.electric_furnace;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ElectricFurnaceScreen extends AbstractContainerScreen<ElectricFurnaceMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");

    public ElectricFurnaceScreen(ElectricFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

        // Replace the vanilla fuel slot with a power indicator
        int x = leftPos + 55;
        int y = topPos + 52;
        graphics.fill(x, y, x + 18, y + 18, 0xFF8B8B8B);
        graphics.fill(x + 1, y + 1, x + 17, y + 17, 0xFF373737);
        graphics.fill(x + 4, y + 4, x + 14, y + 14, menu.isPowered() ? 0xFF35B85A : 0xFFB83A3A);
        graphics.fill(x + 6, y + 2, x + 12, y + 5, menu.isPowered() ? 0xFF75E69A : 0xFFE16B6B);

        // Vanilla arrow position but progress is driven by electrical power
        if (menu.getCookProgress() > 0) {
            int total = Math.max(1, menu.getCookTotalTime());
            int width = Math.min(24, menu.getCookProgress() * 24 / total);
            graphics.fill(leftPos + 79, topPos + 36, leftPos + 79 + width, topPos + 39, 0xFF6CCBFF);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        if (mouseX >= leftPos + 55 && mouseX < leftPos + 73 && mouseY >= topPos + 52 && mouseY < topPos + 70) {
            Component tooltip = Component.translatable(menu.isPowered()
                    ? "gui.eeadditions.electric_furnace.powered"
                    : "gui.eeadditions.electric_furnace.unpowered");
            graphics.renderTooltip(font, tooltip, mouseX, mouseY);
        }
        renderTooltip(graphics, mouseX, mouseY);
    }
}
