package dev.aura.powermoney.client.compat.hwyla;

import dev.aura.powermoney.common.block.BlockPowerReceiver;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class HwylaPlugin implements IWailaPlugin {
  @Override
  public void register(IWailaRegistrar registrar) {
    registrar.registerBodyProvider(new PowerReceiverDataProvider(), BlockPowerReceiver.class);
  }
}
