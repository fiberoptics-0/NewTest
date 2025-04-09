package dev.fiberoptics.newtest;

import dev.fiberoptics.newtest.block.ModBlocks;
import dev.fiberoptics.newtest.block.entity.ModBlockEntityTypes;
import dev.fiberoptics.newtest.gui.ModMenuTypes;
import dev.fiberoptics.newtest.item.ModItems;
import dev.fiberoptics.newtest.recipe.ModRecipes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(NewTest.MODID)
public class NewTest {

    public static final String MODID = "newtest";

    public NewTest(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntityTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModMenuTypes.register(modEventBus);
    }
}
