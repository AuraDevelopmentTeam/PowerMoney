package dev.aura.powermoney.common.compat.theoneprobe;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.compat.IModIntegration;
import dev.aura.powermoney.common.compat.PowerMoneyModules;
import java.util.function.Function;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TheOneProbeCompat implements IModIntegration {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    // Do nothing
  }

  @Override
  public void init(FMLInitializationEvent event) {
    FMLInterModComms.sendFunctionMessage(
        PowerMoneyModules.THEONEPROBE_MODID, "getTheOneProbe", GetTheOneProbe.class.getName());
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    // Do nothing
  }

  public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {
    @Override
    public Void apply(ITheOneProbe theOneProbe) {
      final PowerReceiverDataProvider dataProvider = new PowerReceiverDataProvider();

      theOneProbe.registerProvider(dataProvider);
      theOneProbe.registerProbeConfigProvider(dataProvider);

      PowerMoney.getLogger().info("Added PowerReceiver stats to The One Probe");

      return null;
    }
  }
}
