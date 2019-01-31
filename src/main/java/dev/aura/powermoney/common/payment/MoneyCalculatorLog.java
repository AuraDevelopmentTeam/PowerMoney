package dev.aura.powermoney.common.payment;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

@Value
@EqualsAndHashCode(
  callSuper = false,
  exclude = {"baseMultiplierBD", "logHelper", "shiftBD"}
)
public class MoneyCalculatorLog extends MoneyCalculator {
  private final double baseMultiplier;
  private final double logBase;
  private final double shift;

  @Getter(AccessLevel.NONE)
  private final BigDecimal baseMultiplierBD;

  /**
   * = 1 / ln(logBase)<br>
   * <br>
   * This is a constant used to calculate the log of the money.<br>
   * Since we only have a log2 for BigInteger, the formula is <code>log_b(x) = ln(x) / ln(b)
   * </code>. So we can precalculate <code>ln(b)</code>. And since multiplication is faster than
   * division we calculate the inverse too, so we just need to multiply it later.
   */
  @Getter(AccessLevel.NONE)
  private final BigDecimal logHelper;

  @Getter(AccessLevel.NONE)
  private final BigDecimal shiftBD;

  public MoneyCalculatorLog(double baseMultiplier, double logBase, double shift) {
    this.baseMultiplier = baseMultiplier;
    this.logBase = logBase;
    this.shift = shift;

    baseMultiplierBD = BigDecimal.valueOf(baseMultiplier);
    logHelper = BigDecimal.ONE.divide(BigDecimal.valueOf(Math.log(logBase)), CALCULATION_PRECISION);
    shiftBD = BigDecimal.valueOf(shift);
  }

  @Override
  public BigDecimal covertEnergyToMoney(long energy) {
    if (energy < 0) throw new IllegalArgumentException("energy must not be negative");

    final BigDecimal tempResult;

    if (energy == 0) {
      tempResult = BigDecimal.ZERO;
    } else {
      // baseMultiplier * ((logHelper * ln(money)) + 1)
      // which is also
      // baseMultiplier * (log_logBase(money) + 1)
      tempResult =
          baseMultiplierBD.multiply(
              logHelper
                  .multiply(BigDecimal.valueOf(Math.log(energy)), CALCULATION_PRECISION)
                  .add(BigDecimal.ONE, CALCULATION_PRECISION),
              CALCULATION_PRECISION);
    }

    return roundResult(shiftBD.add(tempResult, CALCULATION_PRECISION));
  }
}
