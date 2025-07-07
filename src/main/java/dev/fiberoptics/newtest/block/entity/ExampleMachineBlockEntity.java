package dev.fiberoptics.newtest.block.entity;

import dev.fiberoptics.newtest.capability.ModEnergyStorage;
import dev.fiberoptics.newtest.capability.ModFluidHandler;
import dev.fiberoptics.newtest.capability.ModItemHandler;
import dev.fiberoptics.newtest.gui.ExampleMachineMenu;
import dev.fiberoptics.newtest.recipe.ExampleMachineRecipe;
import dev.fiberoptics.newtest.recipe.ModRecipes;
import dev.fiberoptics.newtest.recipe.input.ExampleMachineInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ExampleMachineBlockEntity extends BlockEntity implements MenuProvider {

    private final ModItemHandler inventory = new ModItemHandler();
    private final ModFluidHandler fluidInventory = new ModFluidHandler();
    private final ModEnergyStorage energyStorage = new ModEnergyStorage();
    private final ContainerData data;
    private final int MAX_PROGRESS = 60;

    private int progress = 0;

    public ExampleMachineBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.EXAMPLE_MACHINE_BE.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch(index) {
                    case 0 -> progress;
                    case 1 -> MAX_PROGRESS;
                    case 2 -> energyStorage.getEnergyStored();
                    case 3 -> energyStorage.getMaxEnergyStored();
                    case 4 -> BuiltInRegistries.FLUID.getId(fluidInventory.getFluidInTank(0).getFluid());
                    case 5 -> fluidInventory.getFluidInTank(0).getAmount();
                    case 6 -> fluidInventory.getTankCapacity(0);
                    case 7 -> BuiltInRegistries.FLUID.getId(fluidInventory.getFluidInTank(1).getFluid());
                    case 8 -> fluidInventory.getFluidInTank(1).getAmount();
                    case 9 -> fluidInventory.getTankCapacity(1);
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int getCount() {
                return 10;
            }
        };
    }

    public IItemHandler getInvCap(Direction side) {
        return inventory;
    }

    public IFluidHandler getFluidInvCap(Direction side) {
        return fluidInventory;
    }

    public IEnergyStorage getEnergyInvCap(Direction side) {
        return energyStorage;
    }

    protected void tick() {
        Optional<RecipeHolder<ExampleMachineRecipe>> recipe = this.getRecipe();
        if (recipe.isPresent()) {
            if(progress >= MAX_PROGRESS) {
                ItemStack resultItem = recipe.get().value().getOutputItem().copy();
                ItemStack stack = inventory.getStackInSlot(1);
                boolean itemCondition = stack.isEmpty() ||
                        (stack.is(resultItem.getItem()) && stack.getCount()+resultItem.getCount() <= stack.getMaxStackSize());
                FluidStack resultFluid = recipe.get().value().getOutputFluid();
                FluidStack fluidStack = fluidInventory.getFluidInTank(1);
                boolean fluidCondition = fluidStack.isEmpty() || (fluidStack.is(resultFluid.getFluid()) &&
                        fluidStack.getAmount()+resultFluid.getAmount() <= fluidInventory.getTankCapacity(1));
                if(itemCondition && fluidCondition) {
                    inventory.getStackInSlot(0).shrink(1);
                    inventory.setStackInSlot(1,
                            new ItemStack(resultItem.getItem(), stack.getCount()+resultItem.getCount()));
                    fluidInventory.getFluidInTank(0).shrink(recipe.get().value().getInputFluid().getAmount());
                    fluidInventory.setFluidInTank(1,new FluidStack(resultFluid.getFluid(),
                            fluidStack.getAmount()+resultFluid.getAmount()));
                    energyStorage.setEnergyStored(energyStorage.getEnergyStored()-recipe.get().value().getInputEnergy());
                    progress = 0;
                }
            } else progress++;
        } else progress = 0;
    }

    protected Optional<RecipeHolder<ExampleMachineRecipe>> getRecipe() {
        ExampleMachineInput input = new ExampleMachineInput(inventory.getStackInSlot(0),
                fluidInventory.getFluidInTank(0),energyStorage.getEnergyStored());
        return level.getRecipeManager().getRecipeFor(ModRecipes.EXAMPLE_MACHINE_TYPE.get(), input, level);
    }

    public static void ticker(Level level, BlockPos pos, BlockState state, ExampleMachineBlockEntity blockEntity) {
        if(level.isClientSide()) return;
        blockEntity.tick();
    }

    public void drop() {
        if(this.level == null || this.level.isClientSide()) return;
        SimpleContainer container = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            container.setItem(i, inventory.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, container);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Progress", progress);
        tag.put("Inventory",inventory.serializeNBT(registries));
        tag.put("FluidInventory",fluidInventory.serializeNBT(registries));
        tag.put("EnergyStorage",energyStorage.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if(tag.contains("Progress")) progress = tag.getInt("Progress");
        if(tag.contains("Inventory")) inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        if(tag.contains("FluidInventory")) fluidInventory.deserializeNBT(registries, tag.getCompound("FluidInventory"));
        if(tag.contains("EnergyStorage")) energyStorage.deserializeNBT(registries, tag.getCompound("EnergyStorage"));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Example Machine");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ExampleMachineMenu(containerId,inventory,this,this.data);
    }

}
