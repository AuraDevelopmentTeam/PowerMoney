package dev.aura.powermoney.common.payment;

import dev.aura.powermoney.helper.AssertHelper;
import java.math.BigDecimal;
import org.junit.Test;

public class MoneyCalculatorRootTest {
  private static final MoneyCalculatorRoot defaultMoneyCalculator =
      new MoneyCalculatorRoot(1, 2, 0);
  private static final MoneyCalculatorRoot[] defaultMoneyCalculators =
      new MoneyCalculatorRoot[] {
        new MoneyCalculatorRoot(0.01, 2, 0),
        new MoneyCalculatorRoot(0.05, 2, 0),
        new MoneyCalculatorRoot(0.1, 2, 0),
        new MoneyCalculatorRoot(0.5, 2, 0),
        new MoneyCalculatorRoot(1, 2, 0),
        new MoneyCalculatorRoot(5, 2, 0),
        new MoneyCalculatorRoot(10, 2, 0),
        new MoneyCalculatorRoot(50, 2, 0),
        new MoneyCalculatorRoot(100, 2, 0)
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
    for (MoneyCalculatorRoot calculator : defaultMoneyCalculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier()),
          calculator.covertEnergyToMoney(1L));
    }
  }

  @Test
  public void fourTest() {
    for (MoneyCalculatorRoot calculator : defaultMoneyCalculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier() * 2.0),
          calculator.covertEnergyToMoney(4L));
    }
  }

  @Test
  public void shiftTest() {
    final double baseMultiplier = 1;
    final double rootBase = 2;

    for (double i = 1E-4; i <= 1E4; i *= 10) {
      assertEquals(
          "Case: " + i,
          BigDecimal.valueOf(i + 1),
          (new MoneyCalculatorRoot(baseMultiplier, rootBase, i)).covertEnergyToMoney(1L));
    }
  }

  @Test
  public void randomValuesTest() {
    // 10 randomly generated pregenerated test cases
    assertEquals(
        new BigDecimal("4745.3431"),
        new MoneyCalculatorRoot(6.098551413317902, 6.003164098841512, 4.6055)
            .covertEnergyToMoney(225352094716593244L));
    assertEquals(
        new BigDecimal("12844.6018"),
        new MoneyCalculatorRoot(5.370389658603699, 5.204105648736891, -31.1245)
            .covertEnergyToMoney(387845730840177832L));
    assertEquals(
        new BigDecimal("770312.1243"),
        new MoneyCalculatorRoot(2.040847212625775, 3.181678983927516, 39.8661)
            .covertEnergyToMoney(554236067197352565L));
    assertEquals(
        new BigDecimal("11689.2527"),
        new MoneyCalculatorRoot(6.92887996519506, 5.44114450735833, 99.3745)
            .covertEnergyToMoney(345996223074805806L));
    assertEquals(
        new BigDecimal("1816985953.2311"),
        new MoneyCalculatorRoot(3.5746568552972957, 2.040929244997453, -14.7184)
            .covertEnergyToMoney(586906925311361380L));
    assertEquals(
        new BigDecimal("924.2941"),
        new MoneyCalculatorRoot(6.474844362789973, 8.229980450187567, -16.9344)
            .covertEnergyToMoney(626686978358831832L));
    assertEquals(
        new BigDecimal("863.9387"),
        new MoneyCalculatorRoot(8.600623418074473, 8.807200665001467, 27.5984)
            .covertEnergyToMoney(321677944775318121L));
    assertEquals(
        new BigDecimal("891.8672"),
        new MoneyCalculatorRoot(3.414168605141459, 7.1806157740392305, -63.3589)
            .covertEnergyToMoney(371264184139064574L));
    assertEquals(
        new BigDecimal("229471144.4837"),
        new MoneyCalculatorRoot(9.257009365461581, 2.413807498485189, 17.5534)
            .covertEnergyToMoney(705202471905101987L));
    assertEquals(
        new BigDecimal("263.4087"),
        new MoneyCalculatorRoot(0.4037369324331397, 6.84643425079581, 95.4256)
            .covertEnergyToMoney(854980083071654808L));
  }
}
