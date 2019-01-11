package dev.aura.powermoney.common.compat.computercraft;

import dan200.computercraft.api.ComputerCraftAPI;
import dev.aura.powermoney.common.compat.IModIntegration;
import dev.aura.powermoney.common.compat.computercraft.peripheral.PowerReceiverPeripheral;
import dev.aura.powermoney.common.compat.computercraft.peripheral.PowerReceiverPeripheralProvider;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ComputerCraftCompat implements IModIntegration {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    // Do nothing
  }

  @Override
  public void init(FMLInitializationEvent event) {
    // Do nothing
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    ComputerCraftAPI.registerPeripheralProvider(new PowerReceiverPeripheralProvider());

    PowerReceiverPeripheral.init();
  }
}
