package dev.fiberoptics.newtest.recipe;

import dev.fiberoptics.newtest.NewTest;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES;
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;

    public static final DeferredHolder<RecipeSerializer<?>,RecipeSerializer<ExampleMachineRecipe>> EXAMPLE_MACHINE_SERIALIZER;
    public static final DeferredHolder<RecipeType<?>,RecipeType<ExampleMachineRecipe>> EXAMPLE_MACHINE_TYPE;

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }

    static {
        RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, NewTest.MODID);
        RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, NewTest.MODID);

        EXAMPLE_MACHINE_SERIALIZER = RECIPE_SERIALIZERS.register(
                "example_machine",
                ExampleMachineRecipe.Serializer::new
        );
        EXAMPLE_MACHINE_TYPE = RECIPE_TYPES.register(
                "example_machine",
                () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(NewTest.MODID, "example_machine"))
        );
    }
}
