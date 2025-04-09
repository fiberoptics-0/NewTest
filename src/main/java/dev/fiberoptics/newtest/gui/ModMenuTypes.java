package dev.fiberoptics.newtest.gui;

import dev.fiberoptics.newtest.NewTest;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES;

    public static final DeferredHolder<MenuType<?>,MenuType<ExampleMachineMenu>> EXAMPLE_MACHINE;

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }

    static {
        MENU_TYPES = DeferredRegister.create(Registries.MENU, NewTest.MODID);

        EXAMPLE_MACHINE = MENU_TYPES.register("example_machine",
                () -> IMenuTypeExtension.create(ExampleMachineMenu::new));
    }
}
