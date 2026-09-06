package com.xm1zich.eeadditions.content.electric_furnace;

import com.xm1zich.eeadditions.config.CEEAConfigs;
import com.xm1zich.eeadditions.content.electric_furnace.ElectricFurnaceDevice;
import com.xm1zich.eeadditions.CEEABlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ElectricFurnaceBlockEntity extends BlockEntity implements MenuProvider {
    private final SimpleContainer inventory = new SimpleContainer(2);
    private int cookProgress;
    private int cookTotalTime;
    private float voltage;
    private float electricalPower;

    private final ContainerData containerData = new ContainerData() {
        @Override public int get(int index) {
            return switch (index) {
                case 0 -> hasElectricity() ? 1 : 0;
                case 1 -> cookProgress;
                case 2 -> cookTotalTime;
                default -> 0;
            };
        }
        @Override public void set(int index, int value) {
            if (index == 1) cookProgress = Math.max(0, value);
            else if (index == 2) cookTotalTime = Math.max(1, value);
        }
        @Override public int getCount() { return 3; }
    };

    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(CEEABlockEntityTypes.ELECTRIC_FURNACE.get(), pos, state);
        inventory.addListener(container -> setChanged());
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, ElectricFurnaceBlockEntity furnace) {
        furnace.tickCooking(level);
    }

    private void tickCooking(ServerLevel level) {
        ItemStack input = inventory.getItem(0);
        ItemStack output = inventory.getItem(1);
        var recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(input), level).orElse(null);
        boolean canCook = hasElectricity() && recipe != null && canAcceptResult(recipe);

        if (canCook) {
            cookTotalTime = Math.max(1, recipe.value().getCookingTime()*1000);
            cookProgress = cookProgress + (int) (voltage*1000/CEEAConfigs.server().voltageValues.electricFurnaceVoltage.get());
            if (cookProgress >= cookTotalTime) {
                ItemStack result = recipe.value().assemble(new SingleRecipeInput(input), level.registryAccess());
                if (output.isEmpty()) inventory.setItem(1, result.copy());
                else output.grow(result.getCount());
                input.shrink(1);
                cookProgress = 0;
                setChanged();
            }
        } else {
            cookProgress = 0;
        }

        if (cookProgress > 0 && canCook) {
            BlockState litState = getBlockState().setValue(ElectricFurnaceBlock.LIT, true);
            level.setBlock(getBlockPos(), litState, 3);
            setChanged(level, getBlockPos(), litState);
        } else {
            BlockState state = getBlockState().setValue(ElectricFurnaceBlock.LIT, false);
            level.setBlock(getBlockPos(), state, 3);
            setChanged(level, getBlockPos(), state);
        }

        if (canCook) {
            ElectricFurnaceDevice.resistance = CEEAConfigs.server().resistanceValues.electricFurnaceResistance.get();
        } else {
            ElectricFurnaceDevice.resistance = Double.MAX_VALUE;
        }
    }

    private boolean canAcceptResult(RecipeHolder<? extends AbstractCookingRecipe> recipe) {
        ItemStack result = recipe.value().assemble(new SingleRecipeInput(inventory.getItem(0)), level.registryAccess());
        ItemStack output = inventory.getItem(1);
        if (output.isEmpty()) return true;
        return ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
        electricalPower = voltage * voltage / 45.0f;
        setChanged();
    }

    // True only when the EE network is actually driving enough power through the heater
    public boolean hasElectricity() { return electricalPower >= 16.0f; }
    public float getVoltage() { return voltage; }
    public int getCookProgress() { return cookProgress; }
    public ContainerData getContainerData() { return containerData; }
    public SimpleContainer getInventory() { return inventory; }

    @Override public Component getDisplayName() { return Component.translatable("block.eeadditions.electric_furnace"); }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new ElectricFurnaceMenu(id, playerInventory, this);
    }

    public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, inventory.getItems(), registries);
        tag.putInt("CookProgress", cookProgress);
        tag.putInt("CookTotalTime", cookTotalTime);
        tag.putFloat("Voltage", voltage);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, inventory.getItems(), registries);
        cookProgress = tag.getInt("CookProgress");
        cookTotalTime = Math.max(1, tag.getInt("CookTotalTime"));
        voltage = tag.getFloat("Voltage");
        electricalPower = voltage * voltage / 45.0f;
    }
}
