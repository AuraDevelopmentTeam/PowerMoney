package dev.aura.powermoney.common.payment;

import com.google.common.annotations.VisibleForTesting;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
public class MoneyCalculatorLog extends MoneyCalculator {
  
  public MoneyCalculatorLog(double calcBase, double baseMultiplier, double shift) {
    super(0, calcBase, baseMultiplier, shift);
  }
  
  public BigDecimal covertEnergyToMoney(long energy) {
    if (energy < 0) throw new IllegalArgumentException("energy must not be negative");
    else if (energy == 0) return BigDecimal.ZERO;

    // baseMultiplier * ((CalcHelper * log2(money)) + 1)
    // which is also
    // baseMultiplier * (log_logBase(money) + 1)
    return roundResult(
      shiftBD.add(
        baseMultiplierBD.multiply(
            CalcHelper
                .multiply(BigDecimal.valueOf(log2(energy)), CALCULATION_PRECISION)
                .add(BigDecimal.ONE, CALCULATION_PRECISION),
            CALCULATION_PRECISION),
        CALCULATION_PRECISION));
  }
            
  private static double log2(long val) {
    return Math.log(val) / Math.log(2.0);
  }
}
