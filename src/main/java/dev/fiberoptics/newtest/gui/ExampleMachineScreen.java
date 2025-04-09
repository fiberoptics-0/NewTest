package dev.fiberoptics.newtest.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.fiberoptics.newtest.NewTest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExampleMachineScreen extends AbstractContainerScreen<ExampleMachineMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.
            fromNamespaceAndPath(NewTest.MODID, "textures/gui/example_machine.png");

    public ExampleMachineScreen(ExampleMachineMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        guiGraphics.blit(TEXTURE,0,0,0,0,imageWidth,imageHeight);
        int scaledProgress = (int)(22 * (((float)menu.data.get(0))/((float)menu.data.get(1))));
        guiGraphics.blit(TEXTURE,80,35,176,0,scaledProgress,16);
        int scaledEnergy = (int)(73 * (((float)menu.data.get(2))/((float)menu.data.get(3))));
        guiGraphics.blit(TEXTURE,55,24,176,16,scaledEnergy,3);
        if(!menu.blockEntity.getInputFluid().isSame(Fluids.EMPTY)) {
            int scaledHeight = (int) (41*((float)menu.data.get(4))/((float)menu.data.get(5)));
            int x1 = 46, y1 = 64;
            int x0 = 19, y0 = y1 - scaledHeight + 1;
            renderFluid(guiGraphics,menu.blockEntity.getInputFluid(),x0,y0,x1,y1);
        }
        if(!menu.blockEntity.getOutputFluid().isSame(Fluids.EMPTY)) {
            int scaledHeight = (int) (41*((float)menu.data.get(6))/((float)menu.data.get(7)));
            int x1 = 163, y1 = 64;
            int x0 = 136, y0 = y1 - scaledHeight + 1;
            renderFluid(guiGraphics,menu.blockEntity.getOutputFluid(),x0,y0,x1,y1);
        }
        pose.popPose();
    }

    private void renderFluid(GuiGraphics guiGraphics, Fluid fluid, int x0, int y0, int x1, int y1) {
        IClientFluidTypeExtensions properties = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation fluidTexture = properties.getStillTexture();
        TextureAtlasSprite fluidSprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidTexture);
        Color tintColor = new Color(properties.getTintColor(),true);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.setShaderColor(tintColor.getRed()/255F, tintColor.getGreen()/255F,
                tintColor.getBlue()/255F,tintColor.getAlpha()/255F);
        RenderSystem.enableBlend();
        BufferBuilder vertexBuffer = Tesselator.getInstance()
                .begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        for(int x = x0; x <= x1; x+=16) {
            int width = Math.min(x1-x+1,16);
            for(int y = y0; y <= y1; y+=16) {
                int height = Math.min(y1-y+1,16);
                float uMin = fluidSprite.getU0();
                float vMin = fluidSprite.getV0();
                float uMax = fluidSprite.getU(width/16F);
                float vMax = fluidSprite.getV(height/16F);
                vertexBuffer.addVertex(matrix4f,x,y+height,0).setUv(uMin,vMax);
                vertexBuffer.addVertex(matrix4f,x+width,y+height,0).setUv(uMax,vMax);
                vertexBuffer.addVertex(matrix4f,x+width,y,0).setUv(uMax,vMin);
                vertexBuffer.addVertex(matrix4f,x,y,0).setUv(uMin,vMin);

            }
        }
        try {
            BufferUploader.drawWithShader(vertexBuffer.buildOrThrow());
        } catch (Exception ignored) {}
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        List<Component> tooltips = new ArrayList<>();
        if(mouseX >= x+54 && mouseX <= x+128 && mouseY >= y+23 && mouseY <= y+27) {
            tooltips.add(Component.translatable(
                    "tooltip.machineexample.energy", menu.data.get(2),menu.data.get(3)));
        } else if (mouseX >= x+18 && mouseX <= x+47 && mouseY >= y+23 && mouseY <= y+65) {
            tooltips.add(menu.blockEntity.getInputFluid().getFluidType().getDescription());
            tooltips.add(Component.translatable(
                    "tooltip.machineexample.fluid_amount", menu.data.get(4), menu.data.get(5)));
        } else if (mouseX >= x+135 && mouseX <= x+164 && mouseY >= y+23 && mouseY <= y+65) {
            tooltips.add(menu.blockEntity.getOutputFluid().getFluidType().getDescription());
            tooltips.add(Component.translatable(
                    "tooltip.machineexample.fluid_amount", menu.data.get(6), menu.data.get(7)));
        }
        guiGraphics.renderTooltip(this.font,tooltips, Optional.empty(),mouseX,mouseY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics,mouseX,mouseY,partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
