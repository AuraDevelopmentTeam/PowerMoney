package dev.aura.powermoney.api;

import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @since 1.7.0
 * @author BrainStone
 */
public abstract class PowerMoneyApi {
  /**
   * The plugin API instance.
   *
   * @param instance the new instance
   * @return the instance of the {@link PowerMoneyApi}
   * @since 1.7.0
   */
  @Getter
  @Setter(AccessLevel.PROTECTED)
  private static PowerMoneyApi instance;

  /**
   * This allows mods to register their custom money interfaces.
   *
   * <p>If the interface is set to auto, PowerMoney will go through all registered interfaces and
   * use the first one that returns {@code true} when calling its {@link
   * MoneyInterface#canAcceptMoney() canAcceptMoney()}-method.<br>
   * <em>Note: The built in interfaces will be registered last.</em>
   *
   * @param moneyInterface the new interface to register
   * @since 1.7.0
   */
  public abstract void registerMoneyInterface(MoneyInterface moneyInterface);

  /**
   * This method allows you to retrieve the currently active {@link MoneyInterface}.
   *
   * @return the currently active {@link MoneyInterface}
   * @since 1.7.0
   */
  public abstract MoneyInterface getActiveMoneyInterface();

  /**
   * Use this method to access the config value that allows a manual override.
   *
   * <p>Only useful if your interface can offer multiple currencies.
   *
   * <p>If the value is empty or doesn't match any currency, you are expected to use the default
   * currency!
   *
   * @return an empty string or a name of a currency
   * @since 1.7.0
   */
  @Nonnull
  public abstract String getConfiguredCurrencyName();
}
