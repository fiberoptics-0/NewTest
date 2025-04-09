package dev.fiberoptics.newtest.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.energy.EnergyStorage;

public class ModEnergyStorage extends EnergyStorage {

    public ModEnergyStorage() {
        super(100000);
    }

    public void setEnergyStored(int energy) {
        this.energy = energy;
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public Tag serializeNBT(HolderLookup.Provider provider) {
        return IntTag.valueOf(this.getEnergyStored());
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, Tag nbt) {
        super.deserializeNBT(provider, nbt);
    }
}
