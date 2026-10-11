package com.xm1zich.eeadditions.content.compact_rotor;

import com.george_vi.electroenergetics.devices.device.SimulatedDeviceType;
import com.george_vi.electroenergetics.foundation.device.ElectricalDeviceBlock;
import com.george_vi.electroenergetics.foundation.nodes.InWorldNodeConnection;
import com.george_vi.electroenergetics.simulation.infrastructure.InWorldNodeData;
import com.george_vi.electroenergetics.simulation.infrastructure.InfrastructureSavedData;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.xm1zich.eeadditions.CEEABlockEntityTypes;
import com.xm1zich.eeadditions.CEEASimulatedDevices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompactRotorBlock extends DirectionalKineticBlock
        implements ElectricalDeviceBlock<CompactRotorDevice>, IBE<CompactRotorBlockEntity> {

    private static final int MAGNETS = 12;

    public CompactRotorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);
        if ((context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) || preferred == null)
            return super.getStateForPlacement(context);
        return defaultBlockState().setValue(FACING, preferred);
    }

    // IRotate: the rotor has a single kinetic shaft on its front side,
    // exactly like Create's Creative Motor.
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public SimulatedDeviceType<CompactRotorDevice> getDevice() {
        return CEEASimulatedDevices.COMPACT_ROTOR.get();
    }

    @Override
    public Map<Integer, Vec3> getNodePositions(Level level, BlockPos pos, BlockState state) {
        Direction.Axis axis = state.getValue(FACING).getAxis();
        Vec3 node0;
        Vec3 node1;

        switch (axis) {
            case X -> {
                node0 = new Vec3(0.5, 0.5, 2 / 16f);
                node1 = new Vec3(0.5, 0.5, 14 / 16f);
            }
            case Y -> {
                node0 = new Vec3(0.5, 0.5, 2 / 16f);
                node1 = new Vec3(0.5, 0.5, 14 / 16f);
            }
            case Z -> {
                node0 = new Vec3(2 / 16f, 0.5, 0.5);
                node1 = new Vec3(14 / 16f, 0.5, 0.5);
            }
            default -> throw new IllegalStateException("Unexpected axis: " + axis);
        }

        return Map.of(0, node0, 1, node1);
    }

    @Override
    public Vec3 getNodePosition(Level level, BlockPos pos, BlockState state, int id) {
        return getNodePositions(level, pos, state).get(id);
    }

    @Override
    public Class<CompactRotorBlockEntity> getBlockEntityClass() {
        return CompactRotorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CompactRotorBlockEntity> getBlockEntityType() {
        return CEEABlockEntityTypes.COMPACT_ROTOR.get();
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.getBlockTicks().hasScheduledTick(pos, this))
            level.scheduleTick(pos, this, 1);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        List<Integer> nodes = new ArrayList<>(getNodePositions(level, pos, state).keySet());
        InfrastructureSavedData sd = InfrastructureSavedData.load(level);
        sd.registerOrUpdateNodes(pos, nodes);
        super.tick(state, level, pos, random);
    }

    @Override
    public @org.jetbrains.annotations.NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level instanceof ServerLevel sl && player.isCreative()) {
            InfrastructureSavedData sd = InfrastructureSavedData.load(sl);
            for (InWorldNodeData nodeData : sd.getNodesAt(pos))
                for (InWorldNodeConnection connection : sd.getConnections(nodeData))
                    sd.removeConnection(connection);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public net.minecraft.world.InteractionResult onSneakWrenched(BlockState state, net.minecraft.world.item.context.UseOnContext context) {
        removeWiresByPlayer(context.getPlayer(), context.getLevel(), context.getClickedPos());
        return super.onSneakWrenched(state, context);
    }

    public int getMagnets() {
        return MAGNETS;
    }
}
