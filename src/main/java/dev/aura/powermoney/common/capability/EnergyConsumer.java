package dev.aura.powermoney.common.capability;

import com.google.common.collect.ImmutableMap;
import dev.aura.powermoney.common.compat.PowerMoneyModules;
import dev.aura.powermoney.common.compat.tesla.TeslaCompat;
import dev.aura.powermoney.common.config.PowerMoneyConfigWrapper;
import dev.aura.powermoney.common.payment.SpongeMoneyInterface;
import dev.aura.powermoney.common.util.WorldBlockPos;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

@Value
@RequiredArgsConstructor
@Optional.Interface(
  iface = "net.darkhax.tesla.api.ITeslaConsumer",
  modid = PowerMoneyModules.TESLA_MODID
)
public class EnergyConsumer implements IEnergyStorage, ITeslaConsumer, ICapabilityProvider {
  private static final Map<WorldBlockPos, Long> consumedLocalEnergy = new HashMap<>();
  private static final Map<UUID, Long> consumedTotalEnergy = new HashMap<>();

  private final UUID owner;
  private final WorldBlockPos worldPos;

  public static ImmutableMap<WorldBlockPos, Long> getAndResetConsumedLocalEnergy() {
    ImmutableMap<WorldBlockPos, Long> output = ImmutableMap.copyOf(consumedLocalEnergy);

    consumedLocalEnergy.clear();

    return output;
  }

  public static ImmutableMap<UUID, Long> getAndResetConsumedTotalEnergy() {
    ImmutableMap<UUID, Long> output = ImmutableMap.copyOf(consumedTotalEnergy);

    consumedTotalEnergy.clear();

    return output;
  }

  public EnergyConsumer() {
    this(null, null);
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return (capability == CapabilityEnergy.ENERGY)
        || (PowerMoneyModules.tesla() && TeslaCompat.isTeslaCapabilityConsumer(capability));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (hasCapability(capability, facing)) return (T) this;

    return null;
  }

  @Override
  public long givePower(long maxReceive, boolean simulate) {
    if (!canReceive()) return 0;

    if (!simulate) {
      addEnergy(maxReceive);
    }

    return maxReceive;
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    return (int) givePower(maxReceive, simulate);
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    return 0;
  }

  @Override
  public int getEnergyStored() {
    return 0;
  }

  @Override
  public int getMaxEnergyStored() {
    return 0;
  }

  @Override
  public boolean canExtract() {
    return false;
  }

  @Override
  public boolean canReceive() {
    return (owner != null)
        && (PowerMoneyConfigWrapper.getSimulate() || SpongeMoneyInterface.canAcceptMoney());
  }

  private void addEnergy(long energy) {
    // TODO: Synchronize and calculate max
    final long localResult = consumedLocalEnergy.computeIfAbsent(worldPos, (world) -> 0L) + energy;
    consumedLocalEnergy.put(worldPos, localResult);

    final long totalResult = consumedTotalEnergy.computeIfAbsent(owner, (world) -> 0L) + energy;
    consumedTotalEnergy.put(owner, totalResult);
  }
}
