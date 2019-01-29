package dev.aura.powermoney.common.payment;

import dev.aura.powermoney.helper.AssertHelper;
import java.math.BigDecimal;
import org.junit.Test;

public class MoneyCalculatorTest {
  private static final MoneyCalculatorLog defaultMoneyCalculatorLog = new MoneyCalculatorLog(2, 1, 0);
  private static final MoneyCalculatorLog[] defaultMoneyCalculatorsLog =
      new MoneyCalculatorLog[] {
        new MoneyCalculatorLog(2, 0.01, 0),
        new MoneyCalculatorLog(2, 0.05, 0),
        new MoneyCalculatorLog(2, 0.1, 0),
        new MoneyCalculatorLog(2, 0.5, 0),
        new MoneyCalculatorLog(2, 1, 0),
        new MoneyCalculatorLog(2, 5, 0),
        new MoneyCalculatorLog(2, 10, 0),
        new MoneyCalculatorLog(2, 50, 0),
        new MoneyCalculatorLog(2, 100, 0)
      };

  private static void assertEquals(BigDecimal expected, BigDecimal actual) {
    assertEquals(null, expected, actual);
  }

  private static void assertEquals(String comment, BigDecimal expected, BigDecimal actual) {
    AssertHelper.assertEquals(comment, expected, actual, MoneyCalculator::roundResult);
  }

  @Test(expected = IllegalArgumentException.class)
  public void negativeTest() {
    defaultMoneyCalculatorLog.covertEnergyToMoney(-1L);
  }

  @Test
  public void zeroTest() {
    assertEquals(BigDecimal.ZERO, defaultMoneyCalculatorLog.covertEnergyToMoney(0L));
  }

  @Test
  public void oneTest() {
    for (MoneyCalculatorLog calculator : defaultMoneyCalculatorsLog) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier()),
          calculator.covertEnergyToMoney(1L));
    }
  }

  @Test
  public void twoTest() {
    for (MoneyCalculatorLog calculator : defaultMoneyCalculatorsLog) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier() * 2.0),
          calculator.covertEnergyToMoney(2L));
    }
  }

  @Test
  public void tenTest() {
    MoneyCalculatorLog[] calculatorsLog =
        new MoneyCalculatorLog[] {
          new MoneyCalculatorLog(10, 0.01, 0),
          new MoneyCalculatorLog(10, 0.05, 0),
          new MoneyCalculatorLog(10, 0.1, 0),
          new MoneyCalculatorLog(10, 0.5, 0),
          new MoneyCalculatorLog(10, 1, 0),
          new MoneyCalculatorLog(10, 5, 0),
          new MoneyCalculatorLog(10, 10, 0),
          new MoneyCalculatorLog(10, 50, 0),
          new MoneyCalculatorLog(10, 100, 0)
        };

    for (MoneyCalculatorLog calculator : calculatorsLog) {
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
          "Case: " + value, new BigDecimal(i), defaultMoneyCalculatorLog.covertEnergyToMoney(value));

      value *= 2L;
    }
  }

  @Test
  public void tenScalingTest() {
    final MoneyCalculatorLog calculator = new MoneyCalculatorLog(10, 1, 0);
    long value = 1;

    for (int i = 1; i <= 18; ++i) {
      assertEquals("Case: " + value, new BigDecimal(i), calculator.covertEnergyToMoney(value));

      value *= 10L;
    }
  }

  @Test
  public void randomValuesTest() {
    // 10 randomly generated pregenerated test cases
    assertEquals(
        new BigDecimal("142.0569"),
        new MoneyCalculatorLog(6.003164098841512, 6.098551413317902, 0)
            .covertEnergyToMoney(225352094716593244L));
    assertEquals(
        new BigDecimal("137.2312"),
        new MoneyCalculatorLog(5.204105648736891, 5.370389658603699, 0)
            .covertEnergyToMoney(387845730840177832L));
    assertEquals(
        new BigDecimal("74.0825"),
        new MoneyCalculatorLog(3.181678983927516, 2.040847212625775, 0)
            .covertEnergyToMoney(554236067197352565L));
    assertEquals(
        new BigDecimal("172.1154"),
        new MoneyCalculatorLog(5.44114450735833, 6.92887996519506, 0)
            .covertEnergyToMoney(345996023074805805L));
    assertEquals(
        new BigDecimal("208.5805"),
        new MoneyCalculatorLog(2.040929244997452, 3.5746568552972957, 0)
            .covertEnergyToMoney(586906925311361380L));
    assertEquals(
        new BigDecimal("132.3578"),
        new MoneyCalculatorLog(8.229980450187567, 6.474844362789973, 0)
            .covertEnergyToMoney(626686978358831832L));
    assertEquals(
        new BigDecimal("167.9663"),
        new MoneyCalculatorLog(8.807200665001467, 8.600623418074473, 0)
            .covertEnergyToMoney(321677944775318121L));
    assertEquals(
        new BigDecimal("73.4779"),
        new MoneyCalculatorLog(7.1806157740392305, 3.414168605141459, 0)
            .covertEnergyToMoney(371264184139064574L));
    assertEquals(
        new BigDecimal("440.9813"),
        new MoneyCalculatorLog(2.413807498485189, 9.257009365461581, 0)
            .covertEnergyToMoney(705202471905101987L));
    assertEquals(
        new BigDecimal("9.0693"),
        new MoneyCalculatorLog(6.84643425079581, 0.4037369324331397, 0)
            .covertEnergyToMoney(854980083071654808L));
  }
}
