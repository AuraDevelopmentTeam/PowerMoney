package dev.aura.powermoney.common.compat;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/** @author The_Fireplace */
public interface IModIntegration {
  void preInit(FMLPreInitializationEvent event);

  void init(FMLInitializationEvent event);

  void postInit(FMLPostInitializationEvent event);
}
