package dev.aura.powermoney.helper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.junit.Assert;

@UtilityClass
public class AssertHelper {
  public static <T extends Comparable<? super T>> void assertEquals(T expected, T actual) {
    assertEquals(null, expected, actual);
  }

  public static <T extends Comparable<? super T>> void assertEquals(
      T expected, T actual, Function<T, T> rounder) {
    assertEquals(null, expected, actual, rounder);
  }

  public static <T extends Comparable<? super T>> void assertEquals(
      String comment, T expected, T actual) {
    assertEquals(comment, expected, actual, null);
  }

  public static <T extends Comparable<? super T>> void assertEquals(
      String comment, T expected, T actual, Function<T, T> rounder) {
    if (rounder != null) {
      expected = rounder.apply(expected);
      actual = rounder.apply(actual);
    }

    if (expected.compareTo(actual) != 0) {
      Assert.fail(
          (((comment == null) || comment.isEmpty()) ? "" : (comment + ' '))
              + "expected:<"
              + expected
              + "> but was:<"
              + actual
              + '>');
    }
  }

  public static <T extends IMessage> void testPacket(final T expected) {
    testPacket(null, expected);
  }

  public static <T extends IMessage> void testPacket(String message, final T expected) {
    final T actual;

    try {
      Class<?> packetClass = expected.getClass();
      @SuppressWarnings("unchecked")
      Constructor<T> packetDefaultConstructor = (Constructor<T>) packetClass.getConstructor();

      actual = packetDefaultConstructor.newInstance();
    } catch (NoSuchMethodException
        | SecurityException
        | ClassCastException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException e) {
      throw new AssertionError("Could not call default constructor of packet", e);
    }

    final ByteBuf buf = Unpooled.buffer();

    expected.toBytes(buf);
    actual.fromBytes(buf);

    Assert.assertEquals(message, expected, actual);
  }
}
