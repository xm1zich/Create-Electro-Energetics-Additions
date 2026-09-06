package com.xm1zich.eeadditions;

import net.createmod.catnip.config.ui.BaseConfigScreen;
import com.xm1zich.eeadditions.content.electric_furnace.ElectricFurnaceScreen;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.function.Supplier;

@Mod(value = EEAdditions.ID, dist = Dist.CLIENT)
public class CEEAClient {
    public CEEAClient(IEventBus modEventBus) {
        modEventBus.addListener(CEEAClient::onLoadComplete);
        modEventBus.addListener((RegisterMenuScreensEvent event) ->
                event.register(CEEAMenuTypes.ELECTRIC_FURNACE.get(), ElectricFurnaceScreen::new));
    }

    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        ModContainer container = ModList.get()
                .getModContainerById(EEAdditions.ID)
                .orElseThrow(() -> new IllegalStateException("Create: Electro Energetics Additions mod container missing on LoadComplete"));
        Supplier<IConfigScreenFactory> configScreen = () -> (mc, previousScreen) -> new BaseConfigScreen(previousScreen, EEAdditions.ID);
        container.registerExtensionPoint(IConfigScreenFactory.class, configScreen);
    }
}
