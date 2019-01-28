package dev.aura.powermoney.common.compat.opencomputers.component;

import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import li.cil.oc.api.driver.DriverBlock;
import li.cil.oc.api.network.ManagedEnvironment;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PowerReceiverDriver implements DriverBlock {
  @Override
  public boolean worksWith(World world, BlockPos pos, EnumFacing side) {
    return TileEntityPowerReceiver.getTileEntityAt(world, pos) != null;
  }

  @Override
  public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
    final TileEntityPowerReceiver tileEntity = TileEntityPowerReceiver.getTileEntityAt(world, pos);

    if (tileEntity == null) return null;

    return new PowerReceiverEnvironment(tileEntity);
  }
}
