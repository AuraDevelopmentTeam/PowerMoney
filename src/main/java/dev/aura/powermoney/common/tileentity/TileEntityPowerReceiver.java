package dev.aura.powermoney.common.tileentity;

import java.util.UUID;
import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityPowerReceiver extends TileEntity implements ITickable {
  @Getter private String customName;

  @Getter private UUID owner = null;

  @Getter private IEnergyStorage energyStorage;

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) return true;
    else return false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) return (T) energyStorage;

    return super.getCapability(capability, facing);
  }

  public void setCustomName(String newCustomName) {
    customName = newCustomName;

    markDirty();
  }

  public boolean hasCustomName() {
    return this.customName != null && !this.customName.isEmpty();
  }

  @Override
  public ITextComponent getDisplayName() {
    return this.hasCustomName()
        ? new TextComponentString(this.customName)
        : new TextComponentTranslation("container.power_receiver");
  }

  public void setOwner(UUID newOwner) {
    owner = newOwner;

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

  @Override
  public void update() {
    // TODO Auto-generated method stub
  }
}
