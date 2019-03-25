package dev.aura.powermoney.common.compat.enderpay;

import com.kamildanak.minecraft.enderpay.EnderPay;
import com.kamildanak.minecraft.enderpay.api.EnderPayApi;
import com.kamildanak.minecraft.enderpay.api.NoSuchAccountException;
import dev.aura.powermoney.api.MoneyInterface;
import dev.aura.powermoney.api.PowerMoneyApi;
import java.math.BigDecimal;
import java.util.UUID;

public class EnderPayMoneyInterface implements MoneyInterface {
  @Override
  public String getName() {
    return PowerMoneyApi.RESOURCE_PREFIX + EnderPay.modID;
  }

  @Override
  public boolean canAcceptMoney() {
    return true;
  }

  @Override
  public void addMoneyToPlayer(UUID player, BigDecimal money) {
    try {
      EnderPayApi.addToBalance(player, money.longValue());
    } catch (NoSuchAccountException e) {
      PowerMoneyApi.getLogger()
          .error("EnderPay does not have an accout for a player witht the UUID " + player, e);
    }
  }

  @Override
  public String getCurrencySymbol() {
    return EnderPayApi.getCurrencyName(0);
  }

  @Override
  public int getDefaultDigits() {
    return 0;
  }
}
