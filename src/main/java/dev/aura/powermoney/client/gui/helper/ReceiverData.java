package dev.aura.powermoney.client.gui.helper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import net.minecraft.client.resources.I18n;

@Value
@Getter(AccessLevel.NONE)
public class ReceiverData {
  @Getter private final boolean waiting;
  @Getter private final boolean enabled;
  private final BigInteger energyPerSecond;
  private final BigDecimal moneyPerSecond;
  private final String moneySymbol;
  private final int defaultDigits;

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final DecimalFormatSymbols formatSymbols = generateFormatSymbols();

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final DecimalFormat intFormat = generateIntFormat();

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final DecimalFormat decimalFormat = generateDecimalFormat();

  @Getter(lazy = true)
  private final String energyFormatted = generateEnergyFormatted();

  @Getter(lazy = true)
  private final String moneyFormatted = generateMoneyFormatted();

  public static ReceiverData waiting() {
    return new ReceiverData(true);
  }

  public static ReceiverData receiverDisabled() {
    return new ReceiverData(false);
  }

  public static ReceiverData setReceiverData(
      BigInteger energy, BigDecimal money, String moneySymbol, int defaultDigits) {
    return new ReceiverData(energy, money, moneySymbol, defaultDigits);
  }

  private ReceiverData(boolean waiting) {
    this.waiting = waiting;
    enabled = false;
    energyPerSecond = null;
    moneyPerSecond = null;
    moneySymbol = null;
    defaultDigits = 0;
  }

  private ReceiverData(BigInteger energy, BigDecimal money, String moneySymbol, int defaultDigits) {
    waiting = false;
    enabled = true;
    energyPerSecond = energy;
    moneyPerSecond = money;
    this.moneySymbol = moneySymbol;
    this.defaultDigits = defaultDigits;
  }

  private DecimalFormatSymbols generateFormatSymbols() {
    final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
    formatSymbols.setDecimalSeparator(I18n.format("gui.powermoney.decimalseparator").charAt(0));
    formatSymbols.setGroupingSeparator(I18n.format("gui.powermoney.groupingseparator").charAt(0));

    return formatSymbols;
  }

  private DecimalFormat generateIntFormat() {
    DecimalFormat intFormat = new DecimalFormat();
    intFormat.setMaximumFractionDigits(0);
    intFormat.setMinimumFractionDigits(0);
    intFormat.setGroupingUsed(true);
    intFormat.setDecimalFormatSymbols(getFormatSymbols());

    return intFormat;
  }

  private DecimalFormat generateDecimalFormat() {
    DecimalFormat decimalFormat = new DecimalFormat();
    decimalFormat.setMaximumFractionDigits(defaultDigits);
    decimalFormat.setMinimumFractionDigits(defaultDigits);
    decimalFormat.setGroupingUsed(true);
    decimalFormat.setDecimalFormatSymbols(getFormatSymbols());

    return decimalFormat;
  }

  private String generateEnergyFormatted() {
    return getIntFormat().format(energyPerSecond)
        + ' '
        + I18n.format("gui.powermoney.energyunit")
        + I18n.format("gui.powermoney.persecond");
  }

  private String generateMoneyFormatted() {
    return getDecimalFormat().format(moneyPerSecond)
        + ' '
        + moneySymbol
        + I18n.format("gui.powermoney.persecond");
  }
}
