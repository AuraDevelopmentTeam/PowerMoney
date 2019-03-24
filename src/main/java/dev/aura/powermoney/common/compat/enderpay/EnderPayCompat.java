package dev.aura.powermoney.common.compat.enderpay;

import dev.aura.powermoney.common.compat.IModIntegration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class EnderPayCompat implements IModIntegration {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    // Do nothing
  }

  @Override
  public void init(FMLInitializationEvent event) {
    // TODO: Add EnderPayMoneyInterface
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    // Do nothing
  }
}
