package com.xm1zich.eeadditions.content.compact_rotor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.xm1zich.eeadditions.EEAdditions;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class CompactRotorRenderer extends KineticBlockEntityRenderer<CompactRotorBlockEntity> {

    private static final PartialModel SHAFT = PartialModel.of(EEAdditions.rl("block/compact_rotor_shaft"));

    public CompactRotorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(CompactRotorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        // KineticBlockEntityRenderer#renderSafe returns early whenever Flywheel visualization is
        // active. That is fine for Create's own kinetics, which register a Flywheel visual for every
        // block entity type, but the compact rotor's BE type is registered without one, so nothing
        // would draw the shaft. Render the rotating partial manually here instead of relying on super.
        BlockState state = getRenderedBlockState(be);
        renderRotatingBuffer(be, getRotatedModel(be, state), ms, buffer.getBuffer(getRenderType(be, state)), light);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(CompactRotorBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(SHAFT, state);
    }
}
