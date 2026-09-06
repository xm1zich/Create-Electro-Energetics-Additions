package com.xm1zich.eeadditions;

import com.george_vi.electroenergetics.CEETags;
import com.george_vi.electroenergetics.CEEWireTypes;
import com.george_vi.electroenergetics.client.ElectricStatsTooltipModifier;
import com.george_vi.electroenergetics.config.CEEConfigs;
import com.george_vi.electroenergetics.content.bundled_wire.BundledWireItem;
import com.george_vi.electroenergetics.content.bundled_wire.BundledWireType;
import com.george_vi.electroenergetics.content.clamp_meter.ClampMeterItem;
import com.george_vi.electroenergetics.content.electrical_panel.attachments.MCBItem;
import com.george_vi.electroenergetics.content.linemans_stick.LinemansStickItem;
import com.george_vi.electroenergetics.content.wire_spool.EmptySpoolItem;
import com.george_vi.electroenergetics.content.wire_spool.HangingWireSpoolItem;
import com.george_vi.electroenergetics.content.wire_spool.WireSpoolItem;
import com.george_vi.electroenergetics.simulation.WireType;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleSupplier;

import static com.george_vi.electroenergetics.CreateElectroEnergetics.REGISTRATE;

public class CEEAItems {

    static {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

//    public static final ItemEntry<WireSpoolItem> WIRE_SPOOL = insulatedWireSpoolItem("wire_spool", CEEWireTypes.STANDARD, () -> CEEConfigs.server().voltageValues.wireMaxVoltage.get());

    public static void register() {

    }
}
