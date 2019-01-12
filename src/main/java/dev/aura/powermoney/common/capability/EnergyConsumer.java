package dev.aura.powermoney.common.capability;

import com.google.common.collect.ImmutableMap;
import dev.aura.powermoney.common.compat.PowerMoneyModules;
import dev.aura.powermoney.common.compat.tesla.TeslaCompat;
import dev.aura.powermoney.common.config.PowerMoneyConfigWrapper;
import dev.aura.powermoney.common.payment.SpongeMoneyInterface;
import dev.aura.powermoney.common.util.WorldBlockPos;
import java.math.BigInteger;
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
  private static final Map<WorldBlockPos, BigInteger> consumedLocalEnergy = new HashMap<>();
  private static final Map<UUID, BigInteger> consumedTotalEnergy = new HashMap<>();

  private final UUID owner;
  private final WorldBlockPos worldPos;

  public static ImmutableMap<WorldBlockPos, BigInteger> getAndResetConsumedLocalEnergy() {
    ImmutableMap<WorldBlockPos, BigInteger> output = ImmutableMap.copyOf(consumedLocalEnergy);

    consumedLocalEnergy.clear();

    return output;
  }

  public static ImmutableMap<UUID, BigInteger> getAndResetConsumedTotalEnergy() {
    ImmutableMap<UUID, BigInteger> output = ImmutableMap.copyOf(consumedTotalEnergy);

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
    final BigInteger input = BigInteger.valueOf(energy);

    final BigInteger currentLocalValue = consumedLocalEnergy.get(worldPos);
    BigInteger localResult;

    if (currentLocalValue == null) localResult = input;
    else localResult = currentLocalValue.add(input);

    consumedLocalEnergy.put(worldPos, localResult);

    final BigInteger currentTotalValue = consumedTotalEnergy.get(owner);
    BigInteger totalResult;

    if (currentTotalValue == null) totalResult = input;
    else totalResult = currentTotalValue.add(input);

    consumedTotalEnergy.put(owner, totalResult);
  }
}
