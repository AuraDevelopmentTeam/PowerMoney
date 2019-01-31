package dev.aura.powermoney.common.payment;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class MoneyCalculatorRoot extends MoneyCalculator {

  public MoneyCalculatorRoot(double calcBase, double baseMultiplier, double shift) {
    super(1, calcBase, baseMultiplier, shift);
  }

  @Override
  public BigDecimal covertEnergyToMoney(long energy) {
    if (energy < 0) throw new IllegalArgumentException("energy must not be negative");
    else if (energy == 0) return BigDecimal.ZERO;

    return roundResult(
        shiftBD.add(
            baseMultiplierBD.multiply(
                root(energy, CalcHelper.doubleValue()), CALCULATION_PRECISION),
            CALCULATION_PRECISION));
  }

  private static BigDecimal root(long val, double base) {
    return BigDecimal.valueOf(Math.pow(val, 1.0 / base));
  }
}
