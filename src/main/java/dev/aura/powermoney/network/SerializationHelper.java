package dev.aura.powermoney.network;

import io.netty.buffer.ByteBuf;
import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SerializationHelper {
  public static void writeBigInteger(ByteBuf buf, BigInteger val) {
    byte[] bytes = val.toByteArray();

    buf.writeInt(bytes.length);
    buf.writeBytes(bytes);
  }

  public static void writeBigDecimal(ByteBuf buf, BigDecimal val) {
    writeBigInteger(buf, val.unscaledValue());
    buf.writeInt(val.scale());
  }

  public static BigInteger readBigInteger(ByteBuf buf) {
    int size = buf.readInt();
    byte[] bytes = new byte[size];
    buf.readBytes(bytes);

    return new BigInteger(bytes);
  }

  public static BigDecimal readBigDecimal(ByteBuf buf) {
    BigInteger unscaledValue = readBigInteger(buf);
    int scale = buf.readInt();

    return new BigDecimal(unscaledValue, scale);
  }
}
