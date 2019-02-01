package dev.aura.powermoney.common.payment;

import dev.aura.powermoney.helper.AssertHelper;
import java.math.BigDecimal;
import org.junit.Test;

public class MoneyCalculatorLogTest {
  private static final MoneyCalculatorLog defaultMoneyCalculator = new MoneyCalculatorLog(1, 2, 0);
  private static final MoneyCalculatorLog[] defaultMoneyCalculators =
      new MoneyCalculatorLog[] {
        new MoneyCalculatorLog(0.01, 2, 0),
        new MoneyCalculatorLog(0.05, 2, 0),
        new MoneyCalculatorLog(0.1, 2, 0),
        new MoneyCalculatorLog(0.5, 2, 0),
        new MoneyCalculatorLog(1, 2, 0),
        new MoneyCalculatorLog(5, 2, 0),
        new MoneyCalculatorLog(10, 2, 0),
        new MoneyCalculatorLog(50, 2, 0),
        new MoneyCalculatorLog(100, 2, 0)
      };

  private static void assertEquals(BigDecimal expected, BigDecimal actual) {
    assertEquals(null, expected, actual);
  }

  private static void assertEquals(String comment, BigDecimal expected, BigDecimal actual) {
    AssertHelper.assertEquals(comment, expected, actual, MoneyCalculator::roundResult);
  }

  @Test(expected = IllegalArgumentException.class)
  public void negativeTest() {
    defaultMoneyCalculator.covertEnergyToMoney(-1L);
  }

  @Test
  public void zeroTest() {
    assertEquals(BigDecimal.ZERO, defaultMoneyCalculator.covertEnergyToMoney(0L));
  }

  @Test
  public void oneTest() {
    for (MoneyCalculatorLog calculator : defaultMoneyCalculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier()),
          calculator.covertEnergyToMoney(1L));
    }
  }

  @Test
  public void twoTest() {
    for (MoneyCalculatorLog calculator : defaultMoneyCalculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier() * 2.0),
          calculator.covertEnergyToMoney(2L));
    }
  }

  @Test
  public void tenTest() {
    MoneyCalculatorLog[] calculators =
        new MoneyCalculatorLog[] {
          new MoneyCalculatorLog(0.01, 10, 0),
          new MoneyCalculatorLog(0.05, 10, 0),
          new MoneyCalculatorLog(0.1, 10, 0),
          new MoneyCalculatorLog(0.5, 10, 0),
          new MoneyCalculatorLog(1, 10, 0),
          new MoneyCalculatorLog(5, 10, 0),
          new MoneyCalculatorLog(10, 10, 0),
          new MoneyCalculatorLog(50, 10, 0),
          new MoneyCalculatorLog(100, 10, 0)
        };

    for (MoneyCalculatorLog calculator : calculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier() * 2.0),
          calculator.covertEnergyToMoney(10L));
    }
  }

  @Test
  public void twoScalingTest() {
    long value = 1;

    for (int i = 1; i <= 63; ++i) {
      assertEquals(
          "Case: " + value, new BigDecimal(i), defaultMoneyCalculator.covertEnergyToMoney(value));

      value *= 2L;
    }
  }

  @Test
  public void tenScalingTest() {
    final MoneyCalculatorLog calculator = new MoneyCalculatorLog(1, 10, 0);
    long value = 1;

    for (int i = 1; i <= 18; ++i) {
      assertEquals("Case: " + value, new BigDecimal(i), calculator.covertEnergyToMoney(value));

      value *= 10L;
    }
  }

  @Test
  public void shiftTest() {
    final double baseMultiplier = 1;
    final double logBase = 2;

    for (double i = 1E-4; i <= 1E4; i *= 10) {
      assertEquals(
          "Case: " + i,
          BigDecimal.valueOf(i + 1),
          (new MoneyCalculatorLog(baseMultiplier, logBase, i)).covertEnergyToMoney(1L));
    }
  }

  @Test
  public void randomValuesTest() {
    // 10 randomly generated pregenerated test cases
    assertEquals(
        new BigDecimal("146.6624"),
        new MoneyCalculatorLog(6.098551413317902, 6.003164098841512, 4.6055)
            .covertEnergyToMoney(225352094716593244L));
    assertEquals(
        new BigDecimal("106.1067"),
        new MoneyCalculatorLog(5.370389658603699, 5.204105648736891, -31.1245)
            .covertEnergyToMoney(387845730840177832L));
    assertEquals(
        new BigDecimal("113.9486"),
        new MoneyCalculatorLog(2.040847212625775, 3.181678983927516, 39.8661)
            .covertEnergyToMoney(554236067197352565L));
    assertEquals(
        new BigDecimal("271.4899"),
        new MoneyCalculatorLog(6.92887996519506, 5.44114450735833, 99.3745)
            .covertEnergyToMoney(345996023074805805L));
    assertEquals(
        new BigDecimal("193.8621"),
        new MoneyCalculatorLog(3.5746568552972957, 2.040929244997452, -14.7184)
            .covertEnergyToMoney(586906925311361380L));
    assertEquals(
        new BigDecimal("115.4234"),
        new MoneyCalculatorLog(6.474844362789973, 8.229980450187567, -16.9344)
            .covertEnergyToMoney(626686978358831832L));
    assertEquals(
        new BigDecimal("195.5647"),
        new MoneyCalculatorLog(8.600623418074473, 8.807200665001467, 27.5984)
            .covertEnergyToMoney(321677944775318121L));
    assertEquals(
        new BigDecimal("10.119"),
        new MoneyCalculatorLog(3.414168605141459, 7.1806157740392305, -63.3589)
            .covertEnergyToMoney(371264184139064574L));
    assertEquals(
        new BigDecimal("458.5347"),
        new MoneyCalculatorLog(9.257009365461581, 2.413807498485189, 17.5534)
            .covertEnergyToMoney(705202471905101987L));
    assertEquals(
        new BigDecimal("104.4949"),
        new MoneyCalculatorLog(0.4037369324331397, 6.84643425079581, 95.4256)
            .covertEnergyToMoney(854980083071654808L));
  }
}
