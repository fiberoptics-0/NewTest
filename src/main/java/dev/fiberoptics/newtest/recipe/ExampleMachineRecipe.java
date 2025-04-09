package dev.fiberoptics.newtest.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.fiberoptics.newtest.recipe.input.ExampleMachineInput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class ExampleMachineRecipe implements Recipe<ExampleMachineInput> {

    private final Ingredient inputItem;
    private final ItemStack outputItem;
    private final FluidStack inputFluid;
    private final FluidStack outputFluid;
    private final int inputEnergy;

    ExampleMachineRecipe(Ingredient inputItem, ItemStack outputItem,
                         FluidStack inputFluid, FluidStack outputFluid, int inputEnergy) {
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
        this.inputEnergy = inputEnergy;
    }

    public Ingredient getInputItem() {
        return inputItem;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(1, Ingredient.EMPTY);
        ingredients.set(0, inputItem);
        return ingredients;
    }

    @Override
    public boolean matches(ExampleMachineInput input, Level level) {
        boolean itemCondition = inputItem.test(input.itemStack());
        boolean fluidCondition = input.fluidStack().is(inputFluid.getFluid()) &&
                input.fluidStack().getAmount() >= inputFluid.getAmount();
        boolean energyCondition = input.energy() >= inputEnergy;
        return itemCondition && fluidCondition && energyCondition;
    }

    @Override
    public ItemStack assemble(ExampleMachineInput exampleMachineInput, HolderLookup.Provider provider) {
        return outputItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return outputItem;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.EXAMPLE_MACHINE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.EXAMPLE_MACHINE_TYPE.get();
    }

    public FluidStack getInputFluid() {
        return inputFluid;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    public int getInputEnergy() {
        return inputEnergy;
    }

    public static class Serializer implements RecipeSerializer<ExampleMachineRecipe> {

        private static final MapCodec<ExampleMachineRecipe> CODEC;

        private static final StreamCodec<RegistryFriendlyByteBuf,ExampleMachineRecipe> STREAM_CODEC;

        @Override
        public MapCodec<ExampleMachineRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ExampleMachineRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        static {
            CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Ingredient.CODEC.fieldOf("inputItem").forGetter(ExampleMachineRecipe::getInputItem),
                    ItemStack.CODEC.fieldOf("outputItem").forGetter(ExampleMachineRecipe::getOutputItem),
                    FluidStack.CODEC.fieldOf("inputFluid").forGetter(ExampleMachineRecipe::getInputFluid),
                    FluidStack.CODEC.fieldOf("outputFluid").forGetter(ExampleMachineRecipe::getOutputFluid),
                    Codec.INT.fieldOf("inputEnergy").forGetter(ExampleMachineRecipe::getInputEnergy)
            ).apply(inst, ExampleMachineRecipe::new));

            STREAM_CODEC = StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,ExampleMachineRecipe::getInputItem,
                    ItemStack.STREAM_CODEC, ExampleMachineRecipe::getOutputItem,
                    FluidStack.STREAM_CODEC, ExampleMachineRecipe::getInputFluid,
                    FluidStack.STREAM_CODEC, ExampleMachineRecipe::getOutputFluid,
                    ByteBufCodecs.INT, ExampleMachineRecipe::getInputEnergy,
                    ExampleMachineRecipe::new
            );
        }
    }
}
