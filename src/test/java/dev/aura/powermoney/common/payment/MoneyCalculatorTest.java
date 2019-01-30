package dev.aura.powermoney.common.payment;

import dev.aura.powermoney.helper.AssertHelper;
import java.math.BigDecimal;
import org.junit.Test;

public class MoneyCalculatorTest {
  private static final MoneyCalculator logMoneyCalculatorLog = new MoneyCalculatorLog(2, 1, 0);
  private static final MoneyCalculator rootMoneyCalculatorLog = new MoneyCalculatorRoot(1, 1, 0);
  private static final MoneyCalculator[] logMoneyCalculators =
      new MoneyCalculator[] {
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
    logMoneyCalculatorLog.covertEnergyToMoney(-1L);
  }

  @Test
  public void zeroTest() {
    assertEquals(BigDecimal.ZERO, logMoneyCalculatorLog.covertEnergyToMoney(0L));
    assertEquals(BigDecimal.ZERO, rootMoneyCalculatorLog.covertEnergyToMoney(0L));
  }

  @Test
  public void oneTest() {
    for (MoneyCalculator calculator : logMoneyCalculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier()),
          calculator.covertEnergyToMoney(1L));
    }
    assertEquals(
        "Case: " + "Root 1L test",
        new BigDecimal(rootMoneyCalculatorLog.getBaseMultiplier()),
        rootMoneyCalculatorLog.covertEnergyToMoney(1L));
  }

  @Test
  public void twoTest() {
    for (MoneyCalculator calculator : logMoneyCalculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier() * 2.0),
          calculator.covertEnergyToMoney(2L));
    }
    assertEquals(
        "Case: " + "Root 2L test",
        new BigDecimal(rootMoneyCalculatorLog.getBaseMultiplier() * 2.0),
        rootMoneyCalculatorLog.covertEnergyToMoney(2L));
  }

  @Test
  public void tenTest() {
    MoneyCalculator[] calculatorsLog =
        new MoneyCalculator[] {
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

    for (MoneyCalculator calculator : calculatorsLog) {
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
          "Case: " + value, new BigDecimal(i), logMoneyCalculatorLog.covertEnergyToMoney(value));

      value *= 2L;
    }
  }

  @Test
  public void tenScalingTest() {
    final MoneyCalculator calculator = new MoneyCalculatorLog(10, 1, 0);
    long value = 1;

    for (int i = 1; i <= 18; ++i) {
      assertEquals("Case: " + value, new BigDecimal(i), calculator.covertEnergyToMoney(value));

      value *= 10L;
    }
  }

  @Test
  public void randomValuesTest() {
    // 20 randomly generated pregenerated test cases
    // Log
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
    // Root
    assertEquals(
        new BigDecimal("4740.7376"),
        new MoneyCalculatorRoot(6.003164098841512, 6.098551413317902, 0)
            .covertEnergyToMoney(225352094716593244L));
    assertEquals(
        new BigDecimal("12875.7263"),
        new MoneyCalculatorRoot(5.204105648736891, 5.370389658603699, 0)
            .covertEnergyToMoney(387845730840177832L));
    assertEquals(
        new BigDecimal("770272.2582"),
        new MoneyCalculatorRoot(3.181678983927516, 2.040847212625775, 0)
            .covertEnergyToMoney(554236067197352565L));
    assertEquals(
        new BigDecimal("11589.8782"),
        new MoneyCalculatorRoot(5.44114450735833, 6.92887996519506, 0)
            .covertEnergyToMoney(345996223074805806L));
    assertEquals(
        new BigDecimal("1816985967.9495"),
        new MoneyCalculatorRoot(2.040929244997453, 3.5746568552972957, 0)
            .covertEnergyToMoney(586906925311361380L));
    assertEquals(
        new BigDecimal("941.2285"),
        new MoneyCalculatorRoot(8.229980450187567, 6.474844362789973, 0)
            .covertEnergyToMoney(626686978358831832L));
    assertEquals(
        new BigDecimal("836.3403"),
        new MoneyCalculatorRoot(8.807200665001467, 8.600623418074473, 0)
            .covertEnergyToMoney(321677944775318121L));
    assertEquals(
        new BigDecimal("955.2261"),
        new MoneyCalculatorRoot(7.1806157740392305, 3.414168605141459, 0)
            .covertEnergyToMoney(371264184139064574L));
    assertEquals(
        new BigDecimal("229471126.9303"),
        new MoneyCalculatorRoot(2.413807498485189, 9.257009365461581, 0)
            .covertEnergyToMoney(705202471905101987L));
    assertEquals(
        new BigDecimal("167.9831"),
        new MoneyCalculatorRoot(6.84643425079581, 0.4037369324331397, 0)
            .covertEnergyToMoney(854980083071654808L));
  }

  @Test
  public void shiftTest() {
    assertEquals(
        new BigDecimal("441.9813"),
        new MoneyCalculatorLog(2.413807498485189, 9.257009365461581, 1)
            .covertEnergyToMoney(705202471905101987L));
    assertEquals(
        new BigDecimal("439.9813"),
        new MoneyCalculatorLog(2.413807498485189, 9.257009365461581, -1)
            .covertEnergyToMoney(705202471905101987L));
    assertEquals(
        new BigDecimal("4741.7376"),
        new MoneyCalculatorRoot(6.003164098841512, 6.098551413317902, 1)
            .covertEnergyToMoney(225352094716593244L));
    assertEquals(
        new BigDecimal("4739.7376"),
        new MoneyCalculatorRoot(6.003164098841512, 6.098551413317902, -1)
            .covertEnergyToMoney(225352094716593244L));
  }
}
