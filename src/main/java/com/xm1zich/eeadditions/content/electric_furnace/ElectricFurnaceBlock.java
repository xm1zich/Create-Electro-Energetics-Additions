package com.xm1zich.eeadditions.content.electric_furnace;

import com.george_vi.electroenergetics.CEENodeConfigurations;
import com.george_vi.electroenergetics.devices.device.SimulatedDeviceType;
import com.george_vi.electroenergetics.foundation.base.SimpleElectricalDeviceBlock;
import com.xm1zich.eeadditions.CEEABlockEntityTypes;
import com.xm1zich.eeadditions.CEEASimulatedDevices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import com.george_vi.electroenergetics.content.wire_spool.WireSpoolItem;
import com.george_vi.electroenergetics.content.wire_spool.HangingWireSpoolItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

public class ElectricFurnaceBlock extends SimpleElectricalDeviceBlock<ElectricFurnaceDevice> implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public ElectricFurnaceBlock(Properties properties) {
        super(properties.lightLevel(state -> state.getValue(LIT) ? 13 : 0));
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, LIT);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override public SimulatedDeviceType<ElectricFurnaceDevice> getDevice() { return CEEASimulatedDevices.ELECTRIC_FURNACE.get(); }

    @Override
    public Map<Integer, Vec3> getNodePositions(Level level, BlockPos pos, BlockState state) {
        return CEENodeConfigurations.RESISTIVE_HEATER.getNodes(state.getValue(FACING).getOpposite());
    }

    @Override
    public Vec3 getNodePosition(Level level, BlockPos pos, BlockState state, int id) {
        return CEENodeConfigurations.RESISTIVE_HEATER.getNodePos(state.getValue(FACING).getOpposite(), id);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return CEEABlockEntityTypes.ELECTRIC_FURNACE.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide || type != CEEABlockEntityTypes.ELECTRIC_FURNACE.get()) {
            return null;
        }

        return (level1, pos, state1, blockEntity) ->
                ElectricFurnaceBlockEntity.serverTick(
                        (net.minecraft.server.level.ServerLevel) level1,
                        pos,
                        state1,
                        (ElectricFurnaceBlockEntity) blockEntity
                );
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof ElectricFurnaceBlockEntity furnace) {
            net.minecraft.world.Containers.dropContents(level, pos, furnace.getInventory().getItems());
            level.removeBlockEntity(pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        // Do not open the furnace GUI while the player is holding an electrical
        // wire spool. This leaves the interaction to Electro Energetics.
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        if (mainHand.getItem() instanceof WireSpoolItem
                || mainHand.getItem() instanceof HangingWireSpoolItem
                || offHand.getItem() instanceof WireSpoolItem
                || offHand.getItem() instanceof HangingWireSpoolItem) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof ElectricFurnaceBlockEntity furnace
                && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(furnace);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}
