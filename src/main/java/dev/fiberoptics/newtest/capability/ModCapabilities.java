package dev.fiberoptics.newtest.capability;

import dev.fiberoptics.newtest.NewTest;
import dev.fiberoptics.newtest.block.entity.ExampleMachineBlockEntity;
import dev.fiberoptics.newtest.block.entity.ModBlockEntityTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = NewTest.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModCapabilities {
    @SubscribeEvent
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntityTypes.EXAMPLE_MACHINE_BE.get(),
                ExampleMachineBlockEntity::getInvCap
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntityTypes.EXAMPLE_MACHINE_BE.get(),
                ExampleMachineBlockEntity::getFluidInvCap
        );
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntityTypes.EXAMPLE_MACHINE_BE.get(),
                ExampleMachineBlockEntity::getEnergyInvCap
        );
    }
}
