package dev.aura.powermoney.common.compat.ftbmoney;

import dev.aura.powermoney.common.compat.IModIntegration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class FtbMoneyCompat implements IModIntegration {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    // Do nothing
  }

  @Override
  public void init(FMLInitializationEvent event) {
    // TODO: Add FtbMoneyMoneyInterface
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    // Do nothing
  }
}
