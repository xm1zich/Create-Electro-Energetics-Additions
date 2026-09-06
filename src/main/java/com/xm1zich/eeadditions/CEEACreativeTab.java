package com.xm1zich.eeadditions;

import com.xm1zich.eeadditions.CEEABlocks;
import com.xm1zich.eeadditions.CEEAItems;
import com.george_vi.electroenergetics.CEEBlocks;
import com.george_vi.electroenergetics.CEEItems;
import com.george_vi.electroenergetics.CEEFluids;
import com.george_vi.electroenergetics.CEETags;
import com.george_vi.electroenergetics.CreateElectroEnergetics;
import com.george_vi.electroenergetics.config.CEEConfigs;
import com.george_vi.electroenergetics.content.electric_motor.ElectricMotorBlock;
import com.george_vi.electroenergetics.content.electrical_panel.ElectricalPanelBlock;
import com.simibubi.create.AllCreativeModeTabs;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public class CEEACreativeTab {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EEAdditions.ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = REGISTER.register("base",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.eeadditions"))
                    .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getId())
                    .icon(CEEABlocks.ELECTRIC_FURNACE::asStack)
                    .displayItems(((parameters, output) -> {
                        output.accept(CEEABlocks.ELECTRIC_FURNACE.asStack());
                    }))
                    .build());

//    private static boolean shouldAddElectrum() {
//        for (Holder<Item> ignored : BuiltInRegistries.ITEM.getTagOrEmpty(CEETags.ELECTRUM_NUGGET))
//            return true;
//        return false;
//    }

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }

}
