package dev.aura.powermoney.common.payment;

import dev.aura.powermoney.helper.AssertHelper;
import java.math.BigDecimal;
import org.junit.Test;

public class MoneyCalculatorTest {
  private static final MoneyCalculator defaultMoneyCalculator = new MoneyCalculator(1, 2);
  private static final MoneyCalculator[] defaultMoneyCalculators =
      new MoneyCalculator[] {
        new MoneyCalculator(0.01, 2),
        new MoneyCalculator(0.05, 2),
        new MoneyCalculator(0.1, 2),
        new MoneyCalculator(0.5, 2),
        new MoneyCalculator(1, 2),
        new MoneyCalculator(5, 2),
        new MoneyCalculator(10, 2),
        new MoneyCalculator(50, 2),
        new MoneyCalculator(100, 2)
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
    for (MoneyCalculator calculator : defaultMoneyCalculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier()),
          calculator.covertEnergyToMoney(1L));
    }
  }

  @Test
  public void twoTest() {
    for (MoneyCalculator calculator : defaultMoneyCalculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier() * 2.0),
          calculator.covertEnergyToMoney(2L));
    }
  }

  @Test
  public void tenTest() {
    MoneyCalculator[] calculators =
        new MoneyCalculator[] {
          new MoneyCalculator(0.01, 10),
          new MoneyCalculator(0.05, 10),
          new MoneyCalculator(0.1, 10),
          new MoneyCalculator(0.5, 10),
          new MoneyCalculator(1, 10),
          new MoneyCalculator(5, 10),
          new MoneyCalculator(10, 10),
          new MoneyCalculator(50, 10),
          new MoneyCalculator(100, 10)
        };

    for (MoneyCalculator calculator : calculators) {
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
    final MoneyCalculator calculator = new MoneyCalculator(1, 10);
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
        new BigDecimal("196.9014"),
        new MoneyCalculator(6.098551413317902, 6.003164098841512)
            .covertEnergyToMoney(225352094716593244L));
    assertEquals(
        new BigDecimal("189.7096"),
        new MoneyCalculator(5.370389658603699, 5.204105648736891)
            .covertEnergyToMoney(387845730840177832L));
    assertEquals(
        new BigDecimal("102.5033"),
        new MoneyCalculator(2.040847212625775, 3.181678983927516)
            .covertEnergyToMoney(554236067197352565L));
    assertEquals(
        new BigDecimal("238.0428"),
        new MoneyCalculator(6.92887996519506, 5.44114450735833)
            .covertEnergyToMoney(345996023074805805L));
    assertEquals(
        new BigDecimal("289.3434"),
        new MoneyCalculator(3.5746568552972957, 2.040929244997452)
            .covertEnergyToMoney(586906925311361380L));
    assertEquals(
        new BigDecimal("181.8706"),
        new MoneyCalculator(6.474844362789973, 8.229980450187567)
            .covertEnergyToMoney(626686978358831832L));
    assertEquals(
        new BigDecimal("231.6856"),
        new MoneyCalculator(8.600623418074473, 8.807200665001467)
            .covertEnergyToMoney(321677944775318121L));
    assertEquals(
        new BigDecimal("101.3922"),
        new MoneyCalculator(3.414168605141459, 7.1806157740392305)
            .covertEnergyToMoney(371264184139064574L));
    assertEquals(
        new BigDecimal("610.3009"),
        new MoneyCalculator(9.257009365461581, 2.413807498485189)
            .covertEnergyToMoney(705202471905101987L));
    assertEquals(
        new BigDecimal("12.4521"),
        new MoneyCalculator(0.4037369324331397, 6.84643425079581)
            .covertEnergyToMoney(854980083071654808L));
  }
}
