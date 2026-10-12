package io.github.brooswitminecraft.magicavoxelloader.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.brooswitminecraft.magicavoxelloader.MagicaVoxelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

/** Renders each loaded voxel as a scaled native Minecraft block model in every item context. */
public final class VoxelItemRenderer extends BlockEntityWithoutLevelRenderer {
    public VoxelItemRenderer() { super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()); }
    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poses, MultiBufferSource buffers, int light, int overlay) {
        var model = MagicaVoxelLoader.sampleVoxelModel();
        float cell = 1.0F / Math.max(model.width(), Math.max(model.height(), model.depth()));
        poses.pushPose();
        poses.translate(-0.5D, -0.5D, -0.5D);
        for (var voxel : model.voxels()) {
            poses.pushPose();
            poses.translate(voxel.x() * cell, voxel.y() * cell, voxel.z() * cell);
            poses.scale(cell, cell, cell);
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.EMERALD_BLOCK.defaultBlockState(), poses, buffers, light, overlay);
            poses.popPose();
        }
        poses.popPose();
    }
}
