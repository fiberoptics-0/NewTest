package dev.fiberoptics.newtest.item;

import dev.fiberoptics.newtest.NewTest;
import dev.fiberoptics.newtest.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    private static final DeferredRegister<Item> ITEMS;

    public static final DeferredHolder<Item, BlockItem> EXAMPLE_MACHINE;

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    static {
        ITEMS = DeferredRegister.create(Registries.ITEM, NewTest.MODID);

        EXAMPLE_MACHINE = ITEMS.register("example_machine",
                () -> new BlockItem(ModBlocks.EXAMPLE_MACHINE.get(), new Item.Properties()));
    }
}