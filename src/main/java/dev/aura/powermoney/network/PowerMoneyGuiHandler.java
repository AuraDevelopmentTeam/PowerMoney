package dev.aura.powermoney.network;

import dev.aura.powermoney.client.gui.GuiPowerReceiver;
import dev.aura.powermoney.common.container.ContainerGeneric;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class PowerMoneyGuiHandler implements IGuiHandler {
  public static final int ID_POWER_RECEIVER = 0;

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    // Uncomment if needed
    // final BlockPos pos = new BlockPos(x, y, z);
    // final TileEntity entity = world.getTileEntity(pos);

    switch (ID) {
      case ID_POWER_RECEIVER:
        return new ContainerGeneric();
      default:
        return null;
    }
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    final TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));

    switch (ID) {
      case ID_POWER_RECEIVER:
        if (entity instanceof TileEntityPowerReceiver)
          return new GuiPowerReceiver((TileEntityPowerReceiver) entity);
        else return null;
      default:
        return null;
    }
  }
}
