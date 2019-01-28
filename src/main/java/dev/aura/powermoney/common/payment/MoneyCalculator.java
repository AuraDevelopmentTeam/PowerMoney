package dev.aura.powermoney.common.payment;

import com.google.common.annotations.VisibleForTesting;
import java.math.BigDecimal;
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

  private final boolean useLog;
  
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
  private final BigDecimal shiftBD;
  @Getter(AccessLevel.NONE)
  private final BigDecimal CalcHelper;
  
  public MoneyCalculator(double baseMultiplier, double calcBase) { //for compatibility with old versions
    this(true, calcBase, baseMultiplier, 0.0);
  }
  
  public MoneyCalculator(boolean useLog, double calcBase, double baseMultiplier, double shift) {
    this.useLog = useLog;
    shiftBD = BigDecimal.valueOf(shift);
    baseMultiplierBD = BigDecimal.valueOf(baseMultiplier);
    
    if(useLog){
      CalcHelper =
        BigDecimal.ONE.divide(
            BigDecimal.valueOf(Math.log(calcBase) / Math.log(2.0)), CALCULATION_PRECISION);
    }
    else{
      CalcHelper = BigDecimal.valueOf(calcBase);
    }
  }
}
