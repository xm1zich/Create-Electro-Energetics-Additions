package com.xm1zich.eeadditions.content.compact_rotor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.xm1zich.eeadditions.CEEABlockEntityTypes;
import com.xm1zich.eeadditions.config.CEEAConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CompactRotorBlockEntity extends KineticBlockEntity {
    public float voltage;
    public float generatedPower;

    public CompactRotorBlockEntity(BlockPos pos, BlockState state) {
        super(CEEABlockEntityTypes.COMPACT_ROTOR.get(), pos, state);
    }

    public int getMagnets() {
        return 12;
    }

    @Override
    public float calculateStressApplied() {
        float impact = (getMagnets() + 0.125f)
                * CEEAConfigs.server().rotorValues.powerMultiplier.getF()
                * CEEAConfigs.server().rotorValues.stressMultiplier.getF();
        lastStressApplied = impact;
        return impact;
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putFloat("Voltage", voltage);
        tag.putFloat("GeneratedPower", generatedPower);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        voltage = tag.getFloat("Voltage");
        generatedPower = tag.getFloat("GeneratedPower");
    }
}
