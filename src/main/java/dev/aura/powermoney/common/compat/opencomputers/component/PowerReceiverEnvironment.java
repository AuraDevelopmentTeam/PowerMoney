package dev.aura.powermoney.common.compat.opencomputers.component;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.config.PowerMoneyConfigWrapper;
import dev.aura.powermoney.common.handler.PowerMoneyTickHandler;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@SuppressFBWarnings(
    value = {"JLM_JSR166_UTILCONCURRENT_MONITORENTER", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"},
    justification = "Code is generated by lombok which means I don't have any influence on it.")
@RequiredArgsConstructor
public class PowerReceiverEnvironment extends AbstractManagedEnvironment implements NamedBlock {
  public static final String TYPE_ID = "power_receiver";

  private static final Pattern DECIMAL_REMOVER = Pattern.compile("\\.\\d*");

  @NonNull private final TileEntityPowerReceiver tileEntity;

  private final String moneySymbol =
      PowerMoney.getInstance().getActiveMoneyInterface().getCurrencySymbol();
  private final int defaultDigits =
      PowerMoney.getInstance().getActiveMoneyInterface().getDefaultDigits();

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final DecimalFormatSymbols formatSymbols = generateFormatSymbols();

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final DecimalFormat decimalFormat = generateDecimalFormat();

  @Override
  public String preferredName() {
    return TYPE_ID;
  }

  @Override
  public int priority() {
    return 10;
  }

  @Callback(direct = true, doc = "Returns the name of the Owner of the block")
  public Object[] getOwner(Context context, Arguments args) {
    return new Object[] {tileEntity.getOwnerName()};
  }

  @Callback(direct = true, doc = "Returns the UUID (with dashes) of the Owner of the block")
  public Object[] getOwnerUUID(Context context, Arguments args) {
    return new Object[] {tileEntity.getOwner().toString()};
  }

  @Callback(direct = true, doc = "Returns the energy per seconds this block consumes as a number")
  public Object[] getEnergyPerSecond(Context context, Arguments args) {
    return new Object[] {
      PowerMoneyTickHandler.getLocalConsumedEnergy(tileEntity.getEnergyConsumer().getWorldPos())
    };
  }

  @Callback(
      direct = true,
      doc =
          "Returns the energy per seconds all blocks of the owner of this block consume as a number")
  public Object[] getTotalEnergyPerSecond(Context context, Arguments args) {
    return new Object[] {PowerMoneyTickHandler.getConsumedEnergy(tileEntity.getOwner())};
  }

  @Callback(
      direct = true,
      doc =
          "Returns the money generate per sencond by all blocks of the owner of this block as a number")
  public Object[] getMoneyPerSecond(Context context, Arguments args) {
    return new Object[] {
      PowerMoneyTickHandler.getGeneratedMoney(tileEntity.getOwner()).doubleValue()
    };
  }

  @Callback(
      direct = true,
      doc =
          "Returns the money generate per sencond by all blocks of the owner of this block as a string")
  public Object[] getMoneyPerSecondFormatted(Context context, Arguments args) {
    return new Object[] {
      getDecimalFormat().format(PowerMoneyTickHandler.getGeneratedMoney(tileEntity.getOwner()))
    };
  }

  @Callback(
      direct = true,
      doc = "Returns the server wide money symbol that energy gets turned into")
  public Object[] getMoneySymbol(Context context, Arguments args) {
    return new Object[] {moneySymbol};
  }

  @Callback(
      direct = true,
      doc =
          "Returns the money per seconf that would be earned with a energy input per second of the first parameter as a number")
  public Object[] calculateEarnings(Context context, Arguments args) {
    return new Object[] {calculateEarnings(args).doubleValue()};
  }

  @Callback(
      direct = true,
      doc =
          "Returns the money per second that would be earned with a energy input per second of the first parameter as a string")
  public Object[] calculateEarningsString(Context context, Arguments args) {
    return new Object[] {calculateEarnings(args).toString()};
  }

  private BigDecimal calculateEarnings(Arguments args) {
    try {
      final int index = 0;
      long energy;

      if (args.isDouble(index)) {
        final double energyDouble = args.checkDouble(index);

        if (energyDouble > Long.MAX_VALUE)
          throw new IllegalArgumentException(
              "First argument needs to be less than " + Long.MAX_VALUE);

        energy = (long) energyDouble;
      } else if (args.isString(index)) {
        final String energyStr = DECIMAL_REMOVER.matcher(args.checkString(index)).replaceFirst("");
        energy = Long.parseLong(energyStr);
      } else {
        throw new IllegalArgumentException("First argument needs to be a string or an integer");
      }

      return PowerMoneyConfigWrapper.getMoneyCalculator().covertEnergyToMoney(energy);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("First argument needs to be a string or an integer", e);
    }
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
}
