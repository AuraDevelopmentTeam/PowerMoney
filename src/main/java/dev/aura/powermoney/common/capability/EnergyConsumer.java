package dev.aura.powermoney.common.capability;

import com.google.common.collect.ImmutableMap;
import dev.aura.powermoney.common.config.PowerMoneyConfigWrapper;
import dev.aura.powermoney.common.sponge.SpongeMoneyInterface;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import net.minecraftforge.energy.IEnergyStorage;

@Value
@RequiredArgsConstructor
public class EnergyConsumer implements IEnergyStorage {
  private static final Map<UUID, BigInteger> consumedEnergy = new HashMap<>();

  private final UUID owner;

  public static ImmutableMap<UUID, BigInteger> getAndResetConsumedEnergy() {
    ImmutableMap<UUID, BigInteger> output = ImmutableMap.copyOf(consumedEnergy);

    consumedEnergy.clear();

    return output;
  }

  public EnergyConsumer() {
    this(null);
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    if (!canReceive()) return 0;

    if (!simulate) {
      addEnergy(maxReceive);
    }

    return maxReceive;
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
    final BigInteger currentValue = consumedEnergy.get(owner);
    final BigInteger input = BigInteger.valueOf(energy);
    BigInteger result;

    if (currentValue == null) result = input;
    else result = currentValue.add(input);

    consumedEnergy.put(owner, result);
  }
}
