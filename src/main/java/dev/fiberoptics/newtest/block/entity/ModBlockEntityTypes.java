package dev.fiberoptics.newtest.block.entity;

import dev.fiberoptics.newtest.NewTest;
import dev.fiberoptics.newtest.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> ENTITY_TYPES;

    public static final Supplier<BlockEntityType<ExampleMachineBlockEntity>> EXAMPLE_MACHINE_BE;

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    static {
        ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NewTest.MODID);

        EXAMPLE_MACHINE_BE = ENTITY_TYPES.register("example_machine_be",() -> BlockEntityType.Builder.of(
                ExampleMachineBlockEntity::new, ModBlocks.EXAMPLE_MACHINE.get()).build(null));
    }
}
