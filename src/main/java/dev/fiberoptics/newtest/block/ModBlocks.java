package dev.fiberoptics.newtest.block;

import dev.fiberoptics.newtest.NewTest;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS;

    public static final DeferredHolder<Block,ExampleMachineBlock> EXAMPLE_MACHINE;

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    static {
        BLOCKS = DeferredRegister.create(Registries.BLOCK, NewTest.MODID);

        EXAMPLE_MACHINE = BLOCKS.register("example_machine", ExampleMachineBlock::new);
    }
}
