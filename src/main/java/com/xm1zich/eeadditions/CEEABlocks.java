package com.xm1zich.eeadditions;

import com.xm1zich.eeadditions.content.electric_furnace.ElectricFurnaceBlock;
import com.george_vi.electroenergetics.CEETags;
import com.george_vi.electroenergetics.client.ElectricStatsTooltipModifier;
import com.george_vi.electroenergetics.config.CEEConfigs;
import com.george_vi.electroenergetics.content.accumulator.AccumulatorBlock;
import com.george_vi.electroenergetics.content.accumulator.AccumulatorBlockItem;
import com.george_vi.electroenergetics.content.accumulator.AccumulatorStack;
import com.george_vi.electroenergetics.content.bulb.BulbBlock;
import com.george_vi.electroenergetics.content.bundled_wire.BundledWireTerminationBlock;
import com.george_vi.electroenergetics.content.bundled_wire.BundledWireType;
import com.george_vi.electroenergetics.content.buzzer.BuzzerBlock;
import com.george_vi.electroenergetics.content.connector.*;
import com.george_vi.electroenergetics.content.converter.ConverterBlock;
import com.george_vi.electroenergetics.content.creative_battery.CreativeBatteryBlock;
import com.george_vi.electroenergetics.content.cut_off_switch.CutOffSwitchBlock;
import com.george_vi.electroenergetics.content.cut_off_switch.EmergencyStopBlock;
import com.george_vi.electroenergetics.content.cut_off_switch.MomentarySwitchBlock;
import com.george_vi.electroenergetics.content.electric_motor.ElectricMotorBlock;
import com.george_vi.electroenergetics.content.electric_pump.ElectricPumpBlock;
import com.george_vi.electroenergetics.content.electrical_panel.ElectricalPanelBlock;
import com.george_vi.electroenergetics.content.electronic_components.capacitor.CapacitorBlock;
import com.george_vi.electroenergetics.content.electronic_components.diode.DiodeBlock;
import com.george_vi.electroenergetics.content.electronic_components.inductor.InductorBlock;
import com.george_vi.electroenergetics.content.electronic_components.resistor.ResistorBlock;
import com.george_vi.electroenergetics.content.energy_meter.EnergyMeterBlock;
import com.george_vi.electroenergetics.content.energy_meter.EnergyMeterItem;
import com.george_vi.electroenergetics.content.energy_meter.TriPolarEnergyMeterBlock;
import com.george_vi.electroenergetics.content.frequency_meter.FrequencyMeterBlock;
import com.george_vi.electroenergetics.content.fuse.FuseBlock;
import com.george_vi.electroenergetics.content.fuse.FuseBlockItem;
import com.george_vi.electroenergetics.content.fuse.FuseHolderBlock;
import com.george_vi.electroenergetics.content.gauge.ElectricGaugeBlock;
import com.george_vi.electroenergetics.content.gauge.ElectricGaugeMovementBehaviour;
import com.george_vi.electroenergetics.content.ground_rod.GroundRodBlock;
import com.george_vi.electroenergetics.content.indicator_bulb.IndicatorBulbBlock;
import com.george_vi.electroenergetics.content.indicator_bulb.IndicatorBulbBlockItem;
import com.george_vi.electroenergetics.content.pole.ConcretePoleBlock;
import com.george_vi.electroenergetics.content.pole.PoleMountBlock;
import com.george_vi.electroenergetics.content.potentiometer.PotentiometerBlock;
import com.george_vi.electroenergetics.content.potentiometer.RedstonePotentiometerBlock;
import com.george_vi.electroenergetics.content.railway_electrification.catenary.CatenaryHolderBlock;
import com.george_vi.electroenergetics.content.railway_electrification.pantograph.PantographBlock;
import com.george_vi.electroenergetics.content.railway_electrification.pantograph.PantographMovementBehaviour;
import com.george_vi.electroenergetics.content.railway_electrification.third_rail.RailContactShoeBlock;
import com.george_vi.electroenergetics.content.railway_electrification.third_rail.RailContactShoeMovementBehaviour;
import com.george_vi.electroenergetics.content.redstone_relay.RedstoneRelayBlock;
import com.george_vi.electroenergetics.content.relay.RelayBlock;
import com.george_vi.electroenergetics.content.resistive_heater.ResistiveHeaterBlock;
import com.george_vi.electroenergetics.content.resistive_heater.ResistiveHeaterBlockEntity;
import com.george_vi.electroenergetics.content.rotor.AlternatorBrushesBlock;
import com.george_vi.electroenergetics.content.rotor.AlternatorRotorBlock;
import com.george_vi.electroenergetics.content.rotor.StatorBlock;
import com.george_vi.electroenergetics.content.rotor.ThreePhaseAlternatorBrushesBlock;
import com.george_vi.electroenergetics.content.sign.WarningSignBlock;
import com.george_vi.electroenergetics.content.synchroscope.SynchroscopeBlock;
import com.george_vi.electroenergetics.content.transmission_distribution.current_transformer.CurrentTransformerBlock;
import com.george_vi.electroenergetics.content.transmission_distribution.hv_capacitor.HVCapacitorBlock;
import com.george_vi.electroenergetics.content.transmission_distribution.hv_switch.HVSwitchBlock;
import com.george_vi.electroenergetics.content.transmission_distribution.sf6_breaker.SF6BreakerBlock;
import com.george_vi.electroenergetics.content.transmission_distribution.transformer.RadiatorPanelBlock;
import com.george_vi.electroenergetics.content.transmission_distribution.transformer.TransformerBlock;
import com.george_vi.electroenergetics.content.transmission_distribution.transformer.TransformerCoreBlock;
import com.george_vi.electroenergetics.content.transmission_distribution.voltage_regulator.VoltageRegulatorBlock;
import com.george_vi.electroenergetics.content.variac.RedstoneVariacBlock;
import com.george_vi.electroenergetics.content.variac.VariacBlock;
import com.george_vi.electroenergetics.foundation.base.DirectionalRolledDeviceBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.api.boiler.BoilerHeater;
import com.simibubi.create.content.kinetics.gauge.GaugeGenerator;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

import static com.xm1zich.eeadditions.EEAdditions.REGISTRATE;
import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CEEABlocks {
    static {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

    public static final BlockEntry<ElectricFurnaceBlock> ELECTRIC_FURNACE =
            REGISTRATE.block("electric_furnace", ElectricFurnaceBlock::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.mapColor(MapColor.METAL))
                    .transform(pickaxeOnly())
                    .item()
                    .build()
                    .register();

    public static void register() {
        // Static registration is performed by Registrate; kept for addon initialization symmetry.
    }
}
