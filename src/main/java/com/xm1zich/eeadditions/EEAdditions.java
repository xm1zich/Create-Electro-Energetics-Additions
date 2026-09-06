package com.xm1zich.eeadditions;

import com.george_vi.electroenergetics.*;
import com.george_vi.electroenergetics.client.ElectricStatsTooltipModifier;
import com.george_vi.electroenergetics.compat.computercraft.CCProxy;
import com.george_vi.electroenergetics.content.electrical_panel.attachments.CEEPanelAttachmentTypes;
import com.george_vi.electroenergetics.content.fuse.FuseHoldables;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import com.xm1zich.eeadditions.*;
import com.xm1zich.eeadditions.config.CEEAConfigs;
import com.xm1zich.eeadditions.CEEAMenuTypes;
import com.xm1zich.eeadditions.CEEABlockEntityTypes;

@Mod(EEAdditions.ID)
public class EEAdditions
{
    public static final String ID = "eeadditions";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static CreateRegistrate REGISTRATE;

    public EEAdditions(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE = CreateRegistrate.create(ID)
                .setTooltipModifierFactory(item ->
                        new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                                .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
                                .andThen(TooltipModifier.mapNull(new ElectricStatsTooltipModifier(item)))
                );
        REGISTRATE.registerEventListeners(modEventBus);
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        CEEAItems.register();
        CEEABlocks.register();
        CEEASimulatedDevices.register(modEventBus);
        CEEABlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        CEEAMenuTypes.MENUS.register(modEventBus);
//        CEEFluids.register();
//        CEEPackets.register();
//        CEEMenuTypes.register();
//        CEEEntityTypes.register();
//        CEEPartialModels.register();
//        CEEDisplaySources.register();
//        CEEABlockEntityTypes.register();
//        CEEWireTypes.register(modEventBus);
//        CEEMobEffects.register(modEventBus);
//        CEESoundEvents.register(modEventBus);
        CEEACreativeTab.register(modEventBus);
//        CEEDataComponents.register(modEventBus);
//        CEEPantographTypes.register(modEventBus);
//        CEEWireAttachments.register(modEventBus);
//        CEEASimulatedDevices.register(modEventBus);
//        CEEPanelAttachmentTypes.register(modEventBus);
//        CEEElectricTrainSoundTypes.register(modEventBus);
//        CEEWireInteractionBehaviours.register(modEventBus);
//        CEESimulatedDeviceFeatureTypes.register(modEventBus);
        CEEAConfigs.register(modLoadingContext, modContainer);


        FuseHoldables.register();

        CCProxy.register();
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.tryBuild(ID, path);
    }
}
