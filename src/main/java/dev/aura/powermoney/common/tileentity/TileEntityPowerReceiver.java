package dev.aura.powermoney.common.tileentity;

import dev.aura.powermoney.common.capability.EnergyConsumer;
import java.util.UUID;
import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityPowerReceiver extends TileEntity {
  @Getter private String customName;

  @Getter private UUID owner = null;

  @Getter private EnergyConsumer energyConsumer = new EnergyConsumer();

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) return true;
    else return false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) return (T) energyConsumer;

    return super.getCapability(capability, facing);
  }

  public void setCustomName(String newCustomName) {
    customName = newCustomName;

    markDirty();
  }

  public boolean hasCustomName() {
    return customName != null && !customName.isEmpty();
  }

  @Override
  public ITextComponent getDisplayName() {
    return this.hasCustomName()
        ? new TextComponentString(customName)
        : new TextComponentTranslation("container.power_receiver");
  }

  public void setOwner(UUID newOwner) {
    owner = newOwner;
    energyConsumer = new EnergyConsumer(owner);

    markDirty();
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);

    setOwner(compound.getUniqueId("Owner"));
    setCustomName(compound.getString("CustomName"));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);

    compound.setUniqueId("Owner", getOwner());

    if (hasCustomName()) compound.setString("CustomName", getCustomName());
    else compound.removeTag("CustomName");

    return compound;
  }
}
