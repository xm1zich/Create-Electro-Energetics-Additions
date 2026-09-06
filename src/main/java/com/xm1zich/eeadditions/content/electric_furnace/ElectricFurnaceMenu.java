package com.xm1zich.eeadditions.content.electric_furnace;

import com.xm1zich.eeadditions.CEEAMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ElectricFurnaceMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    public static final int INTERNAL_COUNT = 2;
    public static final int DATA_COUNT = 3;

    private final Container container;
    private final ContainerData data;
    private final Level level;
    private final ElectricFurnaceBlockEntity furnace;

    /** Client-side constructor. The real container is synchronized after the menu opens. */
    public ElectricFurnaceMenu(int id, Inventory playerInventory, RegistryFriendlyByteBuf ignored) {
        this(id, playerInventory, new net.minecraft.world.SimpleContainer(INTERNAL_COUNT), new SimpleContainerData(DATA_COUNT));
    }

    public ElectricFurnaceMenu(int id, Inventory playerInventory, ElectricFurnaceBlockEntity furnace) {
        this(id, playerInventory, furnace.getInventory(), furnace.getContainerData(), furnace);
    }

    private ElectricFurnaceMenu(int id, Inventory playerInventory, Container container, ContainerData data) {
        this(id, playerInventory, container, data, null);
    }

    private ElectricFurnaceMenu(int id, Inventory playerInventory, Container container, ContainerData data, @org.jetbrains.annotations.Nullable ElectricFurnaceBlockEntity furnace) {
        super(CEEAMenuTypes.ELECTRIC_FURNACE.get(), id);
        checkContainerSize(container, INTERNAL_COUNT);
        checkContainerDataCount(data, DATA_COUNT);
        this.container = container;
        this.data = data;
        this.level = playerInventory.player.level();
        this.furnace = furnace;

        addSlot(new Slot(container, INPUT_SLOT, 56, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING,
                        new SingleRecipeInput(stack), level).isPresent();
            }
        });
        addSlot(new Slot(container, RESULT_SLOT, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) { return false; }
            @Override
            public boolean mayPickup(Player player) { return true; }
        });

        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 9; ++col)
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; ++col)
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));

        addDataSlots(data);
    }

    public ContainerData getData() { return data; }

    public boolean isPowered() { return data.get(0) != 0; }
    public int getCookProgress() { return data.get(1); }
    public int getCookTotalTime() { return Math.max(1, data.get(2)); }

    @Override
    public boolean stillValid(Player player) {
        return furnace != null
                ? furnace.stillValid(player)
                : AbstractContainerMenu.stillValid(net.minecraft.world.inventory.ContainerLevelAccess.NULL, player, Blocks.FURNACE);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            original = stack.copy();
            if (index == RESULT_SLOT) {
                if (!moveItemStackTo(stack, INTERNAL_COUNT, slots.size(), true)) return ItemStack.EMPTY;
                slot.onQuickCraft(stack, original);
            } else if (index == INPUT_SLOT) {
                if (!moveItemStackTo(stack, INTERNAL_COUNT, slots.size(), false)) return ItemStack.EMPTY;
            } else {
                if (isSmeltable(stack)) {
                    if (!moveItemStackTo(stack, INPUT_SLOT, INPUT_SLOT + 1, false)) return ItemStack.EMPTY;
                } else if (index >= INTERNAL_COUNT && index < slots.size() - 9) {
                    if (!moveItemStackTo(stack, slots.size() - 9, slots.size(), false)) return ItemStack.EMPTY;
                } else if (index >= slots.size() - 9 && index < slots.size()) {
                    if (!moveItemStackTo(stack, INTERNAL_COUNT, slots.size() - 9, false)) return ItemStack.EMPTY;
                } else {
                    return ItemStack.EMPTY;
                }
            }
            if (stack.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
            if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
            slot.onTake(player, stack);
        }
        return original;
    }

    private boolean isSmeltable(ItemStack stack) {
        return !stack.isEmpty() && level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level).isPresent();
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (container instanceof net.minecraft.world.Container c) c.stopOpen(player);
    }
}
