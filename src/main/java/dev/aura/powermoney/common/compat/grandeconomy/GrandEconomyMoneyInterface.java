package dev.aura.powermoney.common.compat.grandeconomy;

import dev.aura.powermoney.api.MoneyInterface;
import dev.aura.powermoney.api.PowerMoneyApi;
import java.math.BigDecimal;
import java.util.UUID;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.GrandEconomyApi;

public class GrandEconomyMoneyInterface implements MoneyInterface {
  @Override
  public String getName() {
    return PowerMoneyApi.RESOURCE_PREFIX + GrandEconomy.MODID;
  }

  @Override
  public boolean canAcceptMoney() {
    return true;
  }

  @Override
  public void addMoneyToPlayer(UUID player, BigDecimal money) {
    final boolean result = GrandEconomyApi.addToBalance(player, money.longValue(), false);

    if (!result) {
      PowerMoneyApi.getLogger()
          .error(
              "Tranferring money over GrandEconomy to the player with the UUID "
                  + player
                  + " failed.");
    }
  }

  @Override
  public String getCurrencySymbol() {
    return GrandEconomyApi.getCurrencyName(0);
  }

  @Override
  public int getDefaultDigits() {
    return 0;
  }
}
