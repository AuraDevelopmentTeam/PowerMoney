package dev.aura.powermoney.client.compat.hwyla;

import dev.aura.powermoney.client.helper.ReceiverData;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import java.util.List;
import java.util.UUID;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class PowerReceiverDataProvider implements IWailaDataProvider {
  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config) {
    final TileEntity tempTileEntity = accessor.getTileEntity();

    if (tempTileEntity instanceof TileEntityPowerReceiver) {
      final TileEntityPowerReceiver tileEntity = (TileEntityPowerReceiver) tempTileEntity;
      final UUID uuid = accessor.getPlayer().getUniqueID();
      final ReceiverData receiverData = ReceiverData.getInstance();
      final String[] headings = receiverData.getHeadings();
      final String[] data = new String[4];

      data[0] = receiverData.formatOwnerName(tileEntity, uuid);

      if (receiverData.isWaiting() || !receiverData.isEnabled()) {
        data[1] = data[2] = data[3] = receiverData.getMessage();
      } else {
        data[1] = receiverData.getLocalEnergyFormatted();
        data[2] = receiverData.getTotalEnergyFormatted();
        data[3] = receiverData.getMoneyFormatted();
      }

      for (int i = 0; i < headings.length; ++i) {
        tooltip.add(headings[i] + ": " + data[i]);
      }
    }

    return tooltip;
  }
}
