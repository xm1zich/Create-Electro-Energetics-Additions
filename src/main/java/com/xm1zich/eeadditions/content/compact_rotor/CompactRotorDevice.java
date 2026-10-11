package com.xm1zich.eeadditions.content.compact_rotor;

import com.xm1zich.eeadditions.config.CEEAConfigs;
import com.george_vi.electroenergetics.devices.device.DevicesSavedData;
import com.george_vi.electroenergetics.devices.device.SimulatedDeviceType;
import com.george_vi.electroenergetics.foundation.base.GeneratingDevice;
import com.george_vi.electroenergetics.simulation.BridgeCollector;
import com.george_vi.electroenergetics.simulation.SimulationResults;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class CompactRotorDevice extends GeneratingDevice {
    private static final int MAGNETS = 12;

    private float voltage;
    private float stress;
    private CompactRotorBlockEntity blockEntity;

    public CompactRotorDevice(Level level, BlockPos pos, DevicesSavedData deviceSD, SimulatedDeviceType<?> type) {
        super(level, pos, deviceSD, type);
    }

    @Override
    public void preTick(BridgeCollector bridges) {
        if (blockEntity == null && level.isLoaded(pos)
                && level.getBlockEntity(pos) instanceof CompactRotorBlockEntity be)
            blockEntity = be;

        if (blockEntity != null) {
            if (blockEntity.isRemoved()) {
                blockEntity = null;
            } else {
                float speed = Math.abs(blockEntity.getSpeed());
                stress = MAGNETS * speed * CEEAConfigs.server().rotorValues.powerMultiplier.getF();
                voltage = stress / CEEAConfigs.server().rotorValues.fullLoadCurrent.getF();
                blockEntity.generatedPower = stress;
            }
        }

        super.preTick(bridges);
    }

    @Override
    public void postTick(SimulationResults results) {
        super.postTick(results);

        if (blockEntity == null && level.isLoaded(pos)
                && level.getBlockEntity(pos) instanceof CompactRotorBlockEntity be)
            blockEntity = be;

        if (blockEntity != null) {
            if (blockEntity.isRemoved()) {
                blockEntity = null;
            } else {
                float actualVoltage = (float) Math.abs(results.getVoltageAt(pos, 0, 1));
                if (Math.abs(blockEntity.voltage - actualVoltage) > 2) {
                    blockEntity.voltage = actualVoltage;
                    blockEntity.sendData();
                }
            }
        }
    }

    @Override
    protected double getVoltage() {
        return voltage;
    }

    @Override
    protected double getPower() {
        return stress;
    }

    @Override
    public void read(net.minecraft.nbt.CompoundTag tag) {
        stress = tag.getFloat("Stress");
        voltage = tag.getFloat("Voltage");
        storedEnergy = tag.getDouble("StoredEnergy");
    }

    @Override
    public void write(net.minecraft.nbt.CompoundTag tag) {
        tag.putFloat("Stress", stress);
        tag.putFloat("Voltage", voltage);
        tag.putDouble("StoredEnergy", storedEnergy);
    }
}
