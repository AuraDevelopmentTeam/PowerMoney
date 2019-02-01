package dev.aura.powermoney.common.payment;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public abstract class MoneyCalculator {
  protected static final MathContext CALCULATION_PRECISION = MathContext.DECIMAL128;
  protected static final int RESULT_DIGITS = 4;
  protected static final RoundingMode RESULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

  public abstract BigDecimal covertEnergyToMoney(long energy);

  protected static BigDecimal roundResult(BigDecimal val) {
    return val.setScale(RESULT_DIGITS, RESULT_ROUNDING_MODE);
  }
}
