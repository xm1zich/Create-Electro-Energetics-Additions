package com.xm1zich.eeadditions;

import com.george_vi.electroenergetics.CEERegistries;
import com.george_vi.electroenergetics.devices.device.SimulatedDeviceType;
import com.xm1zich.eeadditions.content.electric_furnace.ElectricFurnaceDevice;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

// Addon-local electrical devices, registered in Electro Energetics' public device registry.
public class CEEASimulatedDevices {
    private static final DeferredRegister<SimulatedDeviceType<?>> DEVICES =
            DeferredRegister.create(CEERegistries.SIMULATED_DEVICE_TYPE, EEAdditions.ID);

    public static final DeferredHolder<SimulatedDeviceType<?>, SimulatedDeviceType<ElectricFurnaceDevice>> ELECTRIC_FURNACE =
            DEVICES.register("electric_furnace", () -> new SimulatedDeviceType<>(EEAdditions.rl("electric_furnace"),
                    (type, level, pos, savedData) -> new ElectricFurnaceDevice(level, pos, savedData, type)));

    public static void register(IEventBus bus) { DEVICES.register(bus); }
}
