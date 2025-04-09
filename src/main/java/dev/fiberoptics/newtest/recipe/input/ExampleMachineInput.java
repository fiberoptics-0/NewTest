package dev.fiberoptics.newtest.recipe.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public record ExampleMachineInput(ItemStack itemStack, FluidStack fluidStack, int energy) implements RecipeInput {

    @Override
    public ItemStack getItem(int i) {
        if(i == 0) return itemStack;
        return ItemStack.EMPTY;
    }

    @Override
    public int size() {
        return 1;
    }
}
