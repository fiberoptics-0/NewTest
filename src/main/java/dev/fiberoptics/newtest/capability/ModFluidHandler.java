package dev.fiberoptics.newtest.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.UnknownNullability;

public class ModFluidHandler implements IFluidHandler, INBTSerializable<CompoundTag> {

    public NonNullList<FluidStack> fluids = NonNullList.withSize(2, FluidStack.EMPTY);

    @Override
    public int getTanks() {
        return 2;
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        return fluids.get(i);
    }

    public void setFluidInTank(int i, FluidStack fluidStack) {
        fluids.set(i, fluidStack);
    }

    @Override
    public int getTankCapacity(int i) {
        return 10000;
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return true;
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        FluidStack currentFluid = fluids.get(0);
        if(currentFluid.isEmpty()) {
            int fillAmount = Math.min(fluidStack.getAmount(), this.getTankCapacity(0));
            if(fluidAction.execute()) {
                fluids.set(0,new FluidStack(fluidStack.getFluid(), fillAmount));
            }
            return fillAmount;
        }
        if(!currentFluid.is(fluidStack.getFluid())) return 0;
        int fillAmount = Math.min(fluidStack.getAmount()+currentFluid.getAmount(), this.getTankCapacity(0))
                - currentFluid.getAmount();
        if(fluidAction.execute()) {
            fluids.set(0,new FluidStack(fluidStack.getFluid(), currentFluid.getAmount()+fillAmount));
        }
        return fillAmount;
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        FluidStack currentFluid = fluids.get(1);
        if(currentFluid.isEmpty()) return FluidStack.EMPTY;
        if(!currentFluid.is(fluidStack.getFluid())) return FluidStack.EMPTY;
        int drainAmount = Math.min(fluidStack.getAmount(), currentFluid.getAmount());
        if(fluidAction.execute()) {
            fluids.set(1,new FluidStack(fluidStack.getFluid(), currentFluid.getAmount()-drainAmount));
        }
        return new FluidStack(fluidStack.getFluid(), drainAmount);
    }

    @Override
    public FluidStack drain(int amount, FluidAction fluidAction) {
        return this.drain(new FluidStack(fluids.get(1).getFluid(), amount), fluidAction);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag fluidList = new ListTag();
        for (int i = 0; i < this.getTanks(); i++) {
            if(!fluids.get(i).isEmpty()) {
                CompoundTag fluidTag = new CompoundTag();
                fluidTag.putInt("Slot", i);
                fluidList.add(fluids.get(i).save(provider, fluidTag));
            }
        }
        tag.put("Fluids", fluidList);
        tag.putInt("Size", fluids.size());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        fluids = NonNullList.withSize(tag.getInt("Size"), FluidStack.EMPTY);
        ListTag fluidList = tag.getList("Fluids", 10);
        for (int i = 0; i < this.getTanks(); i++) {
            CompoundTag fluidTag = fluidList.getCompound(i);
            int tank = fluidTag.getInt("Slot");
            if(tank >= 0 && tank < this.getTanks()) {
                FluidStack.parse(provider, fluidTag).ifPresent(fluidStack -> fluids.set(tank, fluidStack));
            }
        }
    }
}
