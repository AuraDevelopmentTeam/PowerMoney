package dev.aura.powermoney.common.tileentity;

import cofh.redstoneflux.api.IEnergyReceiver;
import dev.aura.powermoney.PowerMoneyBlocks;
import dev.aura.powermoney.common.capability.EnergyConsumer;
import dev.aura.powermoney.common.compat.PowerMoneyModules;
import dev.aura.powermoney.common.helper.WorldBlockPos;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import java.util.UUID;
import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "net.minecraft.util.ITickable", modid = PowerMoneyModules.IC2_MODID)
@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = PowerMoneyModules.IC2_MODID)
@Optional.Interface(
  iface = "ic2.api.energy.tile.IEnergyAcceptor",
  modid = PowerMoneyModules.IC2_MODID
)
@Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = PowerMoneyModules.IC2_MODID)
@Optional.Interface(
  iface = "cofh.redstoneflux.api.IEnergyReceiver",
  modid = PowerMoneyModules.REDSTONEFLUX_MODID
)
@Optional.Interface(
  iface = "cofh.redstoneflux.api.IEnergyHandler",
  modid = PowerMoneyModules.REDSTONEFLUX_MODID
)
@Optional.Interface(
  iface = "cofh.redstoneflux.api.IEnergyConnection",
  modid = PowerMoneyModules.REDSTONEFLUX_MODID
)
public class TileEntityPowerReceiver extends TileEntity
    implements ITickable, IEnergySink, IEnergyReceiver {
  public static final UUID UUID_NOBODY = new UUID(0, 0);
  public static final String NAME_NOBODY = "<nobody>";

  @Getter private String customName;

  @Getter private UUID owner = UUID_NOBODY;
  @Getter private String ownerName = NAME_NOBODY;

  @Getter private EnergyConsumer energyConsumer = new EnergyConsumer();

  private World createWorld;

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return energyConsumer.hasCapability(capability, facing);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (hasCapability(capability, facing)) return (T) energyConsumer;

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

  public void setOwner(UUID newOwner, String newOwnerName) {
    setOwner(newOwner);

    ownerName = newOwnerName;
  }

  public void setOwner(UUID newOwner) {
    owner = newOwner;
    energyConsumer =
        new EnergyConsumer(owner, new WorldBlockPos((world == null) ? createWorld : world, pos));

    if (owner == null) {
      ownerName = null;
    } else {
      final String nameFromCache = UsernameCache.getLastKnownUsername(owner);
      if (nameFromCache != null) {
        ownerName = nameFromCache;
      }
    }

    markDirty();
  }

  @Override
  protected void setWorldCreate(World worldIn) {
    createWorld = worldIn;
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
  public void handleUpdateTag(NBTTagCompound compound) {
    readFromNBT(compound);

    ownerName = compound.getString("OwnerName");
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    NBTTagCompound compound = new NBTTagCompound();

    compound.setString("OwnerName", ownerName);

    compound = writeToNBT(compound);

    return compound;
  }

  @Override
  public boolean shouldRefresh(
      World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
    // false means to keep the TE
    return newState.getBlock() != PowerMoneyBlocks.powerReceiver();
  }

  public static TileEntityPowerReceiver getTileEntityAt(World world, BlockPos pos) {
    final TileEntity tempTileEntity = world.getTileEntity(pos);

    if ((tempTileEntity == null) || !(tempTileEntity instanceof TileEntityPowerReceiver))
      return null;
    else return (TileEntityPowerReceiver) tempTileEntity;
  }

  // ==================================================================================
  // IC2
  // ==================================================================================

  private boolean addedToNet = !PowerMoneyModules.ic2();

  @Override
  public void update() {
    if (addedToNet || world.isRemote) return;

    onLoaded();
  }

  @Override
  public void invalidate() {
    onUnloaded();

    super.invalidate();
  }

  @Override
  public void onChunkUnload() {
    onUnloaded();

    super.onChunkUnload();
  }

  private void onLoaded() {
    if (addedToNet
        || FMLCommonHandler.instance().getEffectiveSide().isClient()
        || !PowerMoneyModules.ic2()) return;

    MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));

    addedToNet = true;
  }

  private void onUnloaded() {
    if (!addedToNet
        || FMLCommonHandler.instance().getEffectiveSide().isClient()
        || !PowerMoneyModules.ic2()) return;

    MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

    addedToNet = false;
  }

  @Override
  public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
    return true;
  }

  @Override
  public double getDemandedEnergy() {
    return Long.MAX_VALUE / 4.0;
  }

  @Override
  public int getSinkTier() {
    return Integer.MAX_VALUE;
  }

  @Override
  public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
    return amount - energyConsumer.givePower((long) (amount * 4.0), false);
  }

  // ==================================================================================
  // RedstoneFlux
  // ==================================================================================

  @Override
  public int getEnergyStored(EnumFacing from) {
    return energyConsumer.getEnergyStored();
  }

  @Override
  public int getMaxEnergyStored(EnumFacing from) {
    return energyConsumer.getMaxEnergyStored();
  }

  @Override
  public boolean canConnectEnergy(EnumFacing from) {
    return true;
  }

  @Override
  public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
    return energyConsumer.receiveEnergy(maxReceive, simulate);
  }
}
