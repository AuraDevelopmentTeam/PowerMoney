package dev.aura.powermoney.common.compat.computercraft.peripheral;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.handler.PowerMoneyTickHandler;
import dev.aura.powermoney.common.payment.SpongeMoneyInterface;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@RequiredArgsConstructor
public class PowerReceiverPeripheral implements IPeripheral {
  public static final String TYPE_ID = PowerMoney.RESOURCE_PREFIX + "power_receiver";

  @NonNull private final World world;
  @NonNull private final BlockPos pos;
  @NonNull private final TileEntityPowerReceiver tileEntity;

  private final String moneySymbol = SpongeMoneyInterface.getMoneySymbol();
  private final int defaultDigits = SpongeMoneyInterface.getDefaultDigits();

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final DecimalFormatSymbols formatSymbols = generateFormatSymbols();

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final DecimalFormat decimalFormat = generateDecimalFormat();

  private static final SortedMap<String, Method> peripheralMethods = findPeripheralMethods();

  private static final String[] methodNames =
      peripheralMethods.keySet().toArray(new String[peripheralMethods.size()]);
  private static final Method[] methods =
      peripheralMethods.values().toArray(new Method[peripheralMethods.size()]);

  /**
   * Should return a string that uniquely identifies this type of peripheral. This can be queried
   * from lua by calling {@code peripheral.getType()}
   *
   * @return A string identifying the type of peripheral.
   */
  @Override
  @Nonnull
  public String getType() {
    return TYPE_ID;
  }

  /**
   * Should return an array of strings that identify the methods that this peripheral exposes to
   * Lua. This will be called once before each attachment, and should not change when called
   * multiple times.
   *
   * @return An array of strings representing method names.
   * @see #callMethod
   */
  @Override
  @Nonnull
  public String[] getMethodNames() {
    return methodNames;
  }

  /**
   * This is called when a lua program on an attached computer calls {@code peripheral.call()} with
   * one of the methods exposed by {@link #getMethodNames()}.
   *
   * <p>Be aware that this will be called from the ComputerCraft Lua thread, and must be thread-safe
   * when interacting with Minecraft objects.
   *
   * @param computer The interface to the computer that is making the call. Remember that multiple
   *     computers can be attached to a peripheral at once.
   * @param context The context of the currently running lua thread. This can be used to wait for
   *     events or otherwise yield.
   * @param method An integer identifying which of the methods from getMethodNames() the
   *     computercraft wishes to call. The integer indicates the index into the getMethodNames()
   *     table that corresponds to the string passed into peripheral.call()
   * @param arguments An array of objects, representing the arguments passed into {@code
   *     peripheral.call()}.<br>
   *     Lua values of type "string" will be represented by Object type String.<br>
   *     Lua values of type "number" will be represented by Object type Double.<br>
   *     Lua values of type "boolean" will be represented by Object type Boolean.<br>
   *     Lua values of type "table" will be represented by Object type Map.<br>
   *     Lua values of any other type will be represented by a null object.<br>
   *     This array will be empty if no arguments are passed.
   * @return An array of objects, representing values you wish to return to the lua program.
   *     Integers, Doubles, Floats, Strings, Booleans, Maps and ILuaObject and null be converted to
   *     their corresponding lua type. All other types will be converted to nil.
   *     <p>You may return null to indicate no values should be returned.
   * @throws LuaException If you throw any exception from this function, a lua error will be raised
   *     with the same message as your exception. Use this to throw appropriate errors if the wrong
   *     arguments are supplied to your method.
   * @throws InterruptedException If the user shuts down or reboots the computer the coroutine is
   *     suspended, InterruptedException will be thrown. This exception must not be caught or
   *     intercepted, or the computer will leak memory and end up in a broken state.
   * @see #getMethodNames
   */
  @Override
  @Nullable
  public Object[] callMethod(
      @Nonnull IComputerAccess computer,
      @Nonnull ILuaContext context,
      int method,
      @Nonnull Object[] arguments)
      throws LuaException, InterruptedException {
    try {
      return (Object[]) methods[method].invoke(this, computer, context, arguments);
    } catch (RuntimeException | IllegalAccessException | InvocationTargetException e) {
      throw new LuaException(
          "An error occured while trying to run a method of the peripheral \""
              + TYPE_ID
              + "\": "
              + e.getMessage());
    }
  }

  /**
   * Determine whether this peripheral is equivalent to another one.
   *
   * <p>The minimal example should at least check whether they are the same object. However, you may
   * wish to check if they point to the same block or tile entity.
   *
   * @param other The peripheral to compare against. This may be {@code null}.
   * @return Whether these peripherals are equivalent.
   */
  @Override
  public boolean equals(@Nullable IPeripheral other) {
    if (!(other instanceof PowerReceiverPeripheral)) return false;

    final PowerReceiverPeripheral otherConverted = (PowerReceiverPeripheral) other;

    return (world == otherConverted.world)
        && pos.equals(otherConverted.pos)
        && (tileEntity == otherConverted.tileEntity);
  }

  @PeripheralMethod
  public Object[] getOwner(
      @Nonnull IComputerAccess computer,
      @Nonnull ILuaContext context,
      @Nonnull Object[] arguments) {
    return new Object[] {tileEntity.getOwnerName()};
  }

  @PeripheralMethod
  public Object[] getOwnerUUID(
      @Nonnull IComputerAccess computer,
      @Nonnull ILuaContext context,
      @Nonnull Object[] arguments) {
    return new Object[] {tileEntity.getOwner().toString()};
  }

  @PeripheralMethod
  public Object[] getEnergyPerSecond(
      @Nonnull IComputerAccess computer,
      @Nonnull ILuaContext context,
      @Nonnull Object[] arguments) {
    return new Object[] {
      PowerMoneyTickHandler.getConsumedEnergy(tileEntity.getOwner()).doubleValue()
    };
  }

  @PeripheralMethod
  public Object[] getEnergyPerSecondString(
      @Nonnull IComputerAccess computer,
      @Nonnull ILuaContext context,
      @Nonnull Object[] arguments) {
    return new Object[] {PowerMoneyTickHandler.getConsumedEnergy(tileEntity.getOwner()).toString()};
  }

  @PeripheralMethod
  public Object[] getMoneyPerSecond(
      @Nonnull IComputerAccess computer,
      @Nonnull ILuaContext context,
      @Nonnull Object[] arguments) {
    return new Object[] {
      PowerMoneyTickHandler.getGeneratedMoney(tileEntity.getOwner()).doubleValue()
    };
  }

  @PeripheralMethod
  public Object[] getMoneyPerSecondString(
      @Nonnull IComputerAccess computer,
      @Nonnull ILuaContext context,
      @Nonnull Object[] arguments) {
    return new Object[] {
      getDecimalFormat().format(PowerMoneyTickHandler.getGeneratedMoney(tileEntity.getOwner()))
    };
  }

  @PeripheralMethod
  public Object[] getMoneySymbol(
      @Nonnull IComputerAccess computer,
      @Nonnull ILuaContext context,
      @Nonnull Object[] arguments) {
    return new Object[] {moneySymbol};
  }

  private DecimalFormatSymbols generateFormatSymbols() {
    final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
    formatSymbols.setDecimalSeparator('.');
    formatSymbols.setGroupingSeparator(',');

    return formatSymbols;
  }

  private DecimalFormat generateDecimalFormat() {
    final DecimalFormat decimalFormat = new DecimalFormat();
    decimalFormat.setMaximumFractionDigits(defaultDigits);
    decimalFormat.setMinimumFractionDigits(defaultDigits);
    decimalFormat.setGroupingUsed(false);
    decimalFormat.setDecimalFormatSymbols(getFormatSymbols());

    return decimalFormat;
  }

  /** This method solely exists so that the class can be initialized earlier. */
  public static void init() {
    // Do nothing
  }

  private static SortedMap<String, Method> findPeripheralMethods() {
    final SortedMap<String, Method> peripheralMethods = new TreeMap<>();

    final Class<?>[] methodParameterSignature =
        new Class<?>[] {IComputerAccess.class, ILuaContext.class, Object[].class};
    final Class<?> methodReturnSignature = Object[].class;

    for (Method method : PowerReceiverPeripheral.class.getMethods()) {
      // Check if the method has the annotation and if the method parameter signature and return
      // signature is the correct one.
      if ((method.getAnnotation(PeripheralMethod.class) != null)
          && Arrays.equals(methodParameterSignature, method.getParameterTypes())
          && methodReturnSignature.equals(method.getReturnType())) {
        try {
          peripheralMethods.put(method.getName(), method);
        } catch (Exception e) {
          PowerMoney.getLogger().error(e);
        }
      }
    }

    return peripheralMethods;
  }
}
