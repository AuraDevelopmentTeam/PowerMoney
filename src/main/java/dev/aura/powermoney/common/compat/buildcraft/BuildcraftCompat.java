package dev.aura.powermoney.common.compat.buildcraft;

import buildcraft.api.mj.MjAPI;
import lombok.experimental.UtilityClass;
import net.minecraftforge.common.capabilities.Capability;

@UtilityClass
public class BuildcraftCompat {
  public static final long CONVERSION_FACTOR = MjAPI.ONE_MINECRAFT_JOULE / 15L;

  public static boolean isBuildcraftEnergyReceiverCapability(Capability<?> capability) {
    return isBuildcraftConectorCapability(capability) || isBuildcraftRecevierCapability(capability);
  }

  public static boolean isBuildcraftConectorCapability(Capability<?> capability) {
    return capability == MjAPI.CAP_CONNECTOR;
  }

  public static boolean isBuildcraftRecevierCapability(Capability<?> capability) {
    return capability == MjAPI.CAP_RECEIVER;
  }
}
