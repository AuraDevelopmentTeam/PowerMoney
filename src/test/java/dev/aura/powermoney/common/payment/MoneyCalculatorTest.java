package dev.aura.powermoney.common.payment;

import static org.junit.Assert.fail;

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
    assertEquals(null, expected, actual);
  }

  private static void assertEquals(String comment, BigDecimal expected, BigDecimal actual) {
    final BigDecimal expectedRounded = MoneyCalculator.roundResult(expected);
    final BigDecimal actualRounded = MoneyCalculator.roundResult(actual);

    if (expectedRounded.compareTo(actualRounded) != 0) {
      fail(
          (((comment == null) || comment.isEmpty()) ? "" : (comment + ' '))
              + "expected:<"
              + expectedRounded
              + "> but was:<"
              + actualRounded
              + '>');
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
          new BigDecimal(calculator.getBaseMultiplier()),
          calculator.covertEnergyToMoney(BigInteger.ONE));
    }
  }

  @Test
  public void twoTest() {
    final BigInteger TWO = BigInteger.valueOf(2);

    for (MoneyCalculator calculator : defaultMoneyCalculators) {
      assertEquals(
          "Case: " + calculator.toString(),
          new BigDecimal(calculator.getBaseMultiplier() * 2.0),
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
          new BigDecimal(calculator.getBaseMultiplier() * 2.0),
          calculator.covertEnergyToMoney(BigInteger.TEN));
    }
  }

  @Test
  public void twoScalingTest() {
    final BigInteger TWO = BigInteger.valueOf(2);
    BigInteger value = BigInteger.ONE;

    for (int i = 1; i <= 10000; ++i) {
      assertEquals(
          "Case: " + value, new BigDecimal(i), defaultMoneyCalculator.covertEnergyToMoney(value));

      value = value.multiply(TWO);
    }
  }

  @Test
  public void tenScalingTest() {
    final MoneyCalculator calculator = new MoneyCalculator(1, 10);
    BigInteger value = BigInteger.ONE;

    for (int i = 1; i <= 10000; ++i) {
      assertEquals("Case: " + value, new BigDecimal(i), calculator.covertEnergyToMoney(value));

      value = value.multiply(BigInteger.TEN);
    }
  }

  @Test
  public void randomValuesTest() {
    // 10 randomly generated pregenerated test cases
    assertEquals(
        new BigDecimal("196.9014"),
        new MoneyCalculator(6.098551413317902, 6.003164098841512)
            .covertEnergyToMoney(new BigInteger("2253520947165932449892101")));
    assertEquals(
        new BigDecimal("189.7096"),
        new MoneyCalculator(5.370389658603699, 5.204105648736891)
            .covertEnergyToMoney(new BigInteger("3878457308401778324571425")));
    assertEquals(
        new BigDecimal("102.5033"),
        new MoneyCalculator(2.040847212625775, 3.181678983927516)
            .covertEnergyToMoney(new BigInteger("5542360671973525659881335")));
    assertEquals(
        new BigDecimal("238.0428"),
        new MoneyCalculator(6.92887996519506, 5.44114450735833)
            .covertEnergyToMoney(new BigInteger("3459960230748058051286850")));
    assertEquals(
        new BigDecimal("289.3434"),
        new MoneyCalculator(3.5746568552972957, 2.040929244997452)
            .covertEnergyToMoney(new BigInteger("5869069253113613807507050")));
    assertEquals(
        new BigDecimal("181.8706"),
        new MoneyCalculator(6.474844362789973, 8.229980450187567)
            .covertEnergyToMoney(new BigInteger("6266869783588318329367068")));
    assertEquals(
        new BigDecimal("231.6856"),
        new MoneyCalculator(8.600623418074473, 8.807200665001467)
            .covertEnergyToMoney(new BigInteger("3216779447753181210677733")));
    assertEquals(
        new BigDecimal("101.3922"),
        new MoneyCalculator(3.414168605141459, 7.1806157740392305)
            .covertEnergyToMoney(new BigInteger("3712641841390645742879767")));
    assertEquals(
        new BigDecimal("610.3009"),
        new MoneyCalculator(9.257009365461581, 2.413807498485189)
            .covertEnergyToMoney(new BigInteger("7052024719051019877953030")));
    assertEquals(
        new BigDecimal("12.4521"),
        new MoneyCalculator(0.4037369324331397, 6.84643425079581)
            .covertEnergyToMoney(new BigInteger("8549800830716548087142525")));
  }
}
