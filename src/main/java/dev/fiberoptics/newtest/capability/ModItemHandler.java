package dev.fiberoptics.newtest.capability;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ModItemHandler extends ItemStackHandler {

    public ModItemHandler() {
        super(2);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(slot == 0) return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if(slot == 1) return stack;
        return super.insertItem(slot, stack, simulate);
    }
}
