package dev.aura.powermoney.common.payment;

import com.google.common.annotations.VisibleForTesting;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
public class MoneyCalculator {
  private static final MathContext CALCULATION_PRECISION = MathContext.DECIMAL128;
  private static final int RESULT_DIGITS = 4;
  private static final RoundingMode RESULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

  private final double baseMultiplier;
  private final double logBase;

  @Getter(AccessLevel.NONE)
  private final BigDecimal baseMultiplierBD;
  /**
   * = 1 / log2(logBase)<br>
   * <br>
   * This is a constant used to calculate the log of the money.<br>
   * Since we only have a log2 for BigInteger, the formula is <code>log_b(x) = log_2(x) / log_2(b)
   * </code>. So we can precalculate <code>log_2(b)</code>. And since multiplication is faster than
   * division we calculate the inverse too, so we just need to multiply it later.
   */
  @Getter(AccessLevel.NONE)
  private final BigDecimal logHelper;

  public MoneyCalculator(double baseMultiplier, double logBase) {
    this.baseMultiplier = baseMultiplier;
    this.logBase = logBase;

    baseMultiplierBD = BigDecimal.valueOf(baseMultiplier);
    logHelper =
        BigDecimal.ONE.divide(
            BigDecimal.valueOf(Math.log(logBase) / Math.log(2.0)), CALCULATION_PRECISION);
  }

  public BigDecimal covertEnergyToMoney(BigInteger money) {
    final int comparison = BigInteger.ZERO.compareTo(money);

    if (comparison > 0) throw new IllegalArgumentException("money must not be negative");
    else if (comparison == 0) return BigDecimal.ZERO;

    // baseMultiplier * ((logHelper * log2(money) + 1))
    // which is also
    // baseMultiplier * (log_logBase(money) + 1)
    return roundResult(
        baseMultiplierBD.multiply(
            logHelper
                .multiply(BigDecimal.valueOf(log2(money)), CALCULATION_PRECISION)
                .add(BigDecimal.ONE, CALCULATION_PRECISION),
            CALCULATION_PRECISION));
  }

  /**
   * Calculates the log to base 2 of any BigInteger.<br>
   * <br>
   * Taken from <a
   * href="https://stackoverflow.com/a/9125512/1996022">https://stackoverflow.com/a/9125512/1996022</a>.
   * Assumed to be correct.
   *
   * @param val The value to calculate the log_2 of.
   * @return The log_2 of the value
   */
  private static double log2(BigInteger val) {
    // Get the minimum number of bits necessary to hold this value.
    int n = val.bitLength();

    // Calculate the double-precision fraction of this number; as if the
    // binary point was left of the most significant '1' bit.
    // (Get the most significant 53 bits and divide by 2^53)
    long mask = 1L << 52; // mantissa is 53 bits (including hidden bit)
    long mantissa = 0;
    int j = 0;

    for (int i = 1; i < 54; i++) {
      j = n - i;
      if (j < 0) break;

      if (val.testBit(j)) mantissa |= mask;
      mask >>>= 1;
    }

    // Round up if next bit is 1.
    if (j > 0 && val.testBit(j - 1)) mantissa++;

    double f = mantissa / (double) (1L << 52);

    // Add the logarithm to the number of bits, and subtract 1 because the
    // number of bits is always higher than necessary for a number
    // (ie. log2(val)<n for every val).
    return (n - 1 + Math.log(f) * 1.44269504088896340735992468100189213742664595415298D);
    // Magic number converts from base e to base 2 before adding. For other
    // bases, correct the result, NOT this number!
  }

  @VisibleForTesting
  static BigDecimal roundResult(BigDecimal val) {
    return val.setScale(RESULT_DIGITS, RESULT_ROUNDING_MODE);
  }
}
