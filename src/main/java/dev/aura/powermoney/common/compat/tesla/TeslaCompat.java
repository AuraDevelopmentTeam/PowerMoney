package dev.aura.powermoney.common.compat.tesla;

import lombok.experimental.UtilityClass;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraftforge.common.capabilities.Capability;

@UtilityClass
public class TeslaCompat {
  public static boolean isTeslaCapability(Capability<?> capability) {
    return isTeslaCapabilityConsumer(capability)
        || isTeslaCapabilityProducer(capability)
        || isTeslaCapabilityHolder(capability);
  }

  public static boolean isTeslaCapabilityConsumer(Capability<?> capability) {
    return capability == TeslaCapabilities.CAPABILITY_CONSUMER;
  }

  public static boolean isTeslaCapabilityProducer(Capability<?> capability) {
    return capability == TeslaCapabilities.CAPABILITY_PRODUCER;
  }

  public static boolean isTeslaCapabilityHolder(Capability<?> capability) {
    return capability == TeslaCapabilities.CAPABILITY_HOLDER;
  }
}
