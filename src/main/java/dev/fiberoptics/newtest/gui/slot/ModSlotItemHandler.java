package dev.fiberoptics.newtest.gui.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ModSlotItemHandler extends SlotItemHandler {

    private final int index;

    public ModSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        ItemStack currentStack = this.getItemHandler().getStackInSlot(this.index);
        if(currentStack.isEmpty()) return stack.getMaxStackSize();
        if(!currentStack.is(stack.getItem())) return 0;
        return currentStack.getMaxStackSize() - currentStack.getCount();
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !this.getItemHandler().getStackInSlot(this.index).isEmpty();
    }

    @Override
    public @NotNull ItemStack remove(int amount) {
        ItemStack stack = this.getItemHandler().getStackInSlot(this.index);
        if(stack.isEmpty()) return ItemStack.EMPTY;
        if(stack.getCount() >= amount) {
            ItemStack res = stack.copy();
            res.setCount(amount);
            stack.setCount(stack.getCount() - amount);
            return res;
        }
        ItemStack res = stack.copy();
        stack.setCount(0);
        return res;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if(this.index == 1) return false;
        return super.mayPlace(stack);
    }
}
