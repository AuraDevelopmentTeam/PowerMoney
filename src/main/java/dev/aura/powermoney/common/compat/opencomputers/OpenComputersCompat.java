package dev.aura.powermoney.common.compat.opencomputers;

import dev.aura.powermoney.common.compat.IModIntegration;
import dev.aura.powermoney.common.compat.opencomputers.component.PowerReceiverDriver;
import li.cil.oc.api.API;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class OpenComputersCompat implements IModIntegration {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    // Do nothing
  }

  @Override
  public void init(FMLInitializationEvent event) {
    API.driver.add(new PowerReceiverDriver());
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    // Do nothing
  }
}
