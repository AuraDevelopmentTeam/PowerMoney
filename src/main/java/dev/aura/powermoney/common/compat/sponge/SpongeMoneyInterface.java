package dev.aura.powermoney.common.compat.sponge;

import dev.aura.powermoney.api.MoneyInterface;
import dev.aura.powermoney.api.PowerMoneyApi;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

@SuppressFBWarnings(
    value = {"JLM_JSR166_UTILCONCURRENT_MONITORENTER", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"},
    justification = "Code is generated by lombok which means I don't have any influence on it.")
public class SpongeMoneyInterface implements MoneyInterface {
  private final Optional<EconomyService> economyService =
      Sponge.getServiceManager().provide(EconomyService.class);

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final Currency currency = getCurrencyFromConfig();

  @Override
  public String getName() {
    return PowerMoneyApi.RESOURCE_PREFIX + "sponge";
  }

  @Override
  public boolean canAcceptMoney() {
    return hasEconomyService();
  }

  @Override
  public void addMoneyToPlayer(UUID player, BigDecimal money) {
    verifyEconomyService();

    final TransactionResult result =
        economyService
            .get()
            .getOrCreateAccount(player)
            .get()
            .deposit(
                getCurrency(), money, Cause.of(EventContext.empty(), PowerMoneyApi.getInstance()));

    if (result.getResult() != ResultType.SUCCESS) {
      PowerMoneyApi.getLogger()
          .error(
              "Tranferring money over Sponge to the player with the UUID "
                  + player
                  + " failed.\n"
                  + "TransactionResult: "
                  + result);
    }
  }

  @Override
  public String getCurrencySymbol() {
    verifyEconomyService();

    return getCurrency().getSymbol().toPlain();
  }

  @Override
  public int getDefaultDigits() {
    verifyEconomyService();

    return getCurrency().getDefaultFractionDigits();
  }

  private Currency getCurrencyFromConfig() {
    verifyEconomyService();

    final String currencyName = PowerMoneyApi.getInstance().getConfiguredCurrencyName();

    for (Currency currency : economyService.get().getCurrencies()) {
      if (currency.getId().equalsIgnoreCase(currencyName)
          || currency.getName().equalsIgnoreCase(currencyName)) {
        return currency;
      }
    }

    return economyService.get().getDefaultCurrency();
  }

  private boolean hasEconomyService() {
    return economyService.isPresent();
  }

  private void verifyEconomyService() {
    if (!hasEconomyService())
      throw new IllegalStateException("An EconomySerice is missing. Cannot accept money!");
  }
}
