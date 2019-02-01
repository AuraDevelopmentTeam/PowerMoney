package dev.aura.powermoney.common.compat.hwyla;

import dev.aura.powermoney.common.block.BlockPowerReceiver;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class HwylaPlugin implements IWailaPlugin {
  @Override
  public void register(IWailaRegistrar registrar) {
    final PowerReceiverDataProvider powerReceiverDataProvider = new PowerReceiverDataProvider();

    registrar.registerNBTProvider(powerReceiverDataProvider, BlockPowerReceiver.class);
    registrar.registerBodyProvider(powerReceiverDataProvider, BlockPowerReceiver.class);
  }
}
