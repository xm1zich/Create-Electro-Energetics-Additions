package com.xm1zich.eeadditions.content.electric_furnace;

import com.george_vi.electroenergetics.devices.device.DevicesSavedData;
import com.george_vi.electroenergetics.devices.device.SimulatedDeviceType;
import com.george_vi.electroenergetics.foundation.device.SimpleElectricalDevice;
import com.george_vi.electroenergetics.simulation.BridgeCollector;
import com.george_vi.electroenergetics.simulation.SimulationResults;
import com.xm1zich.eeadditions.config.CEEAConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ElectricFurnaceDevice extends SimpleElectricalDevice {
    private ElectricFurnaceBlockEntity blockEntity;
    public static double resistance = CEEAConfigs.server().resistanceValues.electricFurnaceResistance.get();

    public ElectricFurnaceDevice(Level level, BlockPos pos, DevicesSavedData savedData, SimulatedDeviceType<?> type) {
        super(level, pos, savedData, type);
    }

    @Override
    public void preTick(BridgeCollector bridges) {
        bridges.builder(pos).resistor(0, 1, resistance);
    }

    @Override
    public void postTick(SimulationResults results) {
        double voltage = Math.abs(results.getVoltageAt(pos, 0, 1));
        if (blockEntity == null && level.isLoaded(pos)
                && level.getBlockEntity(pos) instanceof ElectricFurnaceBlockEntity furnace)
            blockEntity = furnace;
        if (blockEntity != null) {
            if (blockEntity.isRemoved()) blockEntity = null;
            else blockEntity.setVoltage((float) voltage);
        }
    }
}
