package dev.aura.powermoney.helper;

import java.util.function.Function;
import lombok.experimental.UtilityClass;
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
}
