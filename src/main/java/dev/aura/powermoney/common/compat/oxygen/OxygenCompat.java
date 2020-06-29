package dev.aura.powermoney.common.compat.oxygen;

import dev.aura.powermoney.api.PowerMoneyApi;
import dev.aura.powermoney.common.compat.IModIntegration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class OxygenCompat implements IModIntegration {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    // Do nothing
  }

  @Override
  public void init(FMLInitializationEvent event) {
    PowerMoneyApi.getInstance().registerMoneyInterface(new OxygenMoneyInterface());
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    // Do nothing
  }
}
