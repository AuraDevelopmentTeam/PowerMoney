package dev.aura.powermoney.network.helper;

import dev.aura.powermoney.helper.AssertHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Random;
import org.junit.Test;

public class SerializationHelperTest {
  @Test
  public void bigIntegerTest() {
    final Random rand = new Random(0);
    BigInteger value = BigInteger.ONE;

    for (int i = 0; i < 10000; ++i) {
      ByteBuf buf = Unpooled.buffer();
      SerializationHelper.writeBigInteger(buf, value);

      AssertHelper.assertEquals("Case: " + value, value, SerializationHelper.readBigInteger(buf));

      value =
          value
              .multiply(BigInteger.valueOf(rand.nextInt(8) + 2))
              .add(BigInteger.valueOf(rand.nextInt(10)));
    }
  }

  @Test
  public void bigDecimalTest() {
    final Random rand = new Random(0);
    BigDecimal value = BigDecimal.ONE;

    for (int i = 0; i < 10000; ++i) {
      ByteBuf buf = Unpooled.buffer();
      SerializationHelper.writeBigDecimal(buf, value);

      AssertHelper.assertEquals("Case: " + value, value, SerializationHelper.readBigDecimal(buf));

      value =
          value
              .multiply(BigDecimal.valueOf(rand.nextDouble() * 9 + 1), MathContext.DECIMAL128)
              .add(BigDecimal.valueOf(rand.nextDouble() * 10), MathContext.DECIMAL128);
    }
  }
}
