package dev.aura.powermoney.common.payment;

import dev.aura.powermoney.api.MoneyInterface;
import dev.aura.powermoney.api.PowerMoneyApi;
import dev.aura.powermoney.common.config.PowerMoneyConfigWrapper;
import java.math.BigDecimal;
import java.util.UUID;

public class SimulateMoneyInterface implements MoneyInterface {
  @Override
  public String getName() {
    return PowerMoneyApi.RESOURCE_PREFIX + "simulate";
  }

  @Override
  public boolean canAcceptMoney() {
    return PowerMoneyConfigWrapper.getSimulate();
  }

  @Override
  public void addMoneyToPlayer(UUID player, BigDecimal money) {
    // Do nothing
  }

  @Override
  public String getCurrencySymbol() {
    return "$";
  }

  @Override
  public int getDefaultDigits() {
    return 2;
  }
}
