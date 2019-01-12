package dev.aura.powermoney.common.compat.computercraft.peripheral;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PowerReceiverPeripheralProvider implements IPeripheralProvider {
  /**
   * Produce an peripheral implementation from a block location.
   *
   * @param world The world the block is in.
   * @param pos The position the block is at.
   * @param side The side to get the peripheral from.
   * @return A peripheral, or {@code null} if there is not a peripheral here you'd like to handle.
   * @see dan200.computercraft.api.ComputerCraftAPI#registerPeripheralProvider(IPeripheralProvider)
   */
  @Override
  @Nullable
  public IPeripheral getPeripheral(World world, BlockPos pos, EnumFacing side) {
    final TileEntityPowerReceiver tileEntity = TileEntityPowerReceiver.getTileEntityAt(world, pos);

    if (tileEntity == null) return null;

    return new PowerReceiverPeripheral(world, pos, tileEntity);
  }
}
