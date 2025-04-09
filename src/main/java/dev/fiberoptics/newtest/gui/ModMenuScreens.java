package dev.fiberoptics.newtest.gui;

import dev.fiberoptics.newtest.NewTest;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = NewTest.MODID, value = Dist.CLIENT)
public class ModMenuScreens {
    @SubscribeEvent
    public static void onClientSetup(final RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.EXAMPLE_MACHINE.get(), ExampleMachineScreen::new);
    }
}
