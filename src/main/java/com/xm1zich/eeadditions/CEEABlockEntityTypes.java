package com.xm1zich.eeadditions;

import com.xm1zich.eeadditions.content.electric_furnace.ElectricFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;

public final class CEEABlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, EEAdditions.ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricFurnaceBlockEntity>> ELECTRIC_FURNACE =
            BLOCK_ENTITY_TYPES.register("electric_furnace", () ->
                    BlockEntityType.Builder.of(ElectricFurnaceBlockEntity::new, CEEABlocks.ELECTRIC_FURNACE.get()).build(null));

    private CEEABlockEntityTypes() {}
}
