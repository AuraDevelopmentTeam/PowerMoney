package dev.aura.powermoney.common.payment;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
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
    assertEquals("", expected, actual);
  }

  private static void assertEquals(String comment, BigDecimal expected, BigDecimal actual) {
    if (expected.compareTo(actual) != 0) {
      fail(comment + "expected:<" + expected + "> but was:<" + actual + '>');
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void negativeTest() {
    defaultMoneyCalculator.covertEnergyToMoney(BigInteger.valueOf(-1));
  }

  @Test
  public void zeroTest() {
    assertEquals(BigDecimal.ZERO, defaultMoneyCalculator.covertEnergyToMoney(BigInteger.ZERO));
  }

  @Test
  public void oneTest() {
    for (MoneyCalculator calculator : defaultMoneyCalculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier(), MoneyCalculator.RESULT_PRECISION),
          calculator.covertEnergyToMoney(BigInteger.ONE));
    }
  }

  @Test
  public void twoTest() {
    final BigInteger TWO = BigInteger.valueOf(2);

    for (MoneyCalculator calculator : defaultMoneyCalculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier() * 2.0, MoneyCalculator.RESULT_PRECISION),
          calculator.covertEnergyToMoney(TWO));
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
          new BigDecimal(calculator.getBaseMultiplier() * 2.0, MoneyCalculator.RESULT_PRECISION),
          calculator.covertEnergyToMoney(BigInteger.TEN));
    }
  }

  // TODO: More tests!!!
}
