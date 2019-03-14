package dev.aura.powermoney.api;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * This interface exists to abstract the implementation of the interface used to payout the money to
 * a player.
 *
 * <p>Implementations are not expected to work if {@link #canAcceptMoney()} returns {@code false}.
 *
 * @since 1.7.0
 * @author BrainStone
 */
public interface MoneyInterface {
  /**
   * This method is supposed to return a unique qualified name of of the interface.
   *
   * <p>The name should look like this: {@code <implementing mod>:<interface>}.<br>
   * For example: {@code powermoney:sponge}
   *
   * <p>This method is expected to always return a valid name, regardless of the return value of
   * {@link #canAcceptMoney()}!
   *
   * @return A unique, qualified name for this interface.
   * @since 1.7.0
   */
  public String getName();

  /**
   * This method is used to determine if this implementation can be used or not.
   *
   * <p><strong>This method is not expected to throw under any circumstance, even if the interface
   * used is not present.</strong><br>
   * If you are expecting this to throw, handle the exception yourself!
   *
   * @return {@code true} if this interface is functional and all requirements are met, {@code
   *     false} otherwise
   * @since 1.7.0
   */
  public boolean canAcceptMoney();

  /**
   * This method is used to handle the payout to players.
   *
   * <p>The {@link UUID} being passed may identify an offline player. As people can still make money
   * while offline.
   *
   * <p>The amount of money may also be negative. So be prepared for that case!<br>
   * It will be rounded to the precision returned by {@link #getDefaultDigits()}.
   *
   * @param player The {@link UUID} of the player that is supposed to receive the money. The
   *     corresponding player may be offline!
   * @param money The amount of money to add to the player's balance. Rounded to the precision
   *     returned by {@link #getDefaultDigits()}.
   * @since 1.7.0
   */
  public void addMoneyToPlayer(UUID player, BigDecimal money);

  /**
   * A simple string that identifies the symbol of the currency being used.
   *
   * <p><strong>This string will be shown client side in the GUI. But this method gets called server
   * side!</strong>
   *
   * <p>For example: {@code $}
   *
   * @return The symbol for the currency in use.
   * @since 1.7.0
   */
  public String getCurrencySymbol();

  /**
   * This is used to in calculations that round the payout amounts.<br>
   * The {@code money} parameter of the {@link #addMoneyToPlayer(UUID, BigDecimal)} method is
   * rounded to this precision.
   *
   * <p>This method is not called often, as it's not expected that this value changes at all.
   *
   * @return How many digits the currency can accept by default
   * @since 1.7.0
   */
  public int getDefaultDigits();
}
