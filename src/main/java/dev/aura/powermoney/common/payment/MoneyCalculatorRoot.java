package dev.aura.powermoney.common.payment;

import com.google.common.annotations.VisibleForTesting;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
public class MoneyCalculatorRoot extends MoneyCalculator {

  public BigDecimal covertEnergyToMoney(long energy) {
    if (energy < 0) throw new IllegalArgumentException("energy must not be negative");
    else if (energy == 0) return BigDecimal.ZERO;

    return roundResult(
      shiftBD.add(
        baseMultiplierBD.multiply(
          root(energy, CalcHelper.doubleValue()),
        CALCULATION_PRECISION),
      CALCULATION_PRECISION));
  }

  private static BigDecimal root(long val, double base) {
    return BigDecimal.valueOf(Math.pow(val, 1.0 / base));
  }

  @VisibleForTesting
  static BigDecimal roundResult(BigDecimal val) {
    return val.setScale(RESULT_DIGITS, RESULT_ROUNDING_MODE);
  }
}
