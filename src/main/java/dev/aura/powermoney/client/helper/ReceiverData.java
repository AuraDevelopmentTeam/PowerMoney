package dev.aura.powermoney.client.helper;

import com.google.common.annotations.VisibleForTesting;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import dev.aura.powermoney.network.PacketDispatcher;
import dev.aura.powermoney.network.packet.serverbound.PacketChangeRequiresReceiverData;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

@SuppressFBWarnings(
  value = {"JLM_JSR166_UTILCONCURRENT_MONITORENTER", "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"},
  justification = "Code is generated by lombok which means I don't have any influence on it."
)
@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReceiverData {
  @Getter private static ReceiverData instance;

  static {
    // Start off disabled
    receiverDisabled();
  }

  @EqualsAndHashCode.Include private final boolean waiting;
  @EqualsAndHashCode.Include private final boolean enabled;
  @EqualsAndHashCode.Include private final long localEnergyPerSecond;
  @EqualsAndHashCode.Include private final long totalEnergyPerSecond;
  @EqualsAndHashCode.Include private final BigDecimal moneyPerSecond;
  @EqualsAndHashCode.Include private final String moneySymbol;
  @EqualsAndHashCode.Include private final int defaultDigits;

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final DecimalFormatSymbols formatSymbols = generateFormatSymbols();

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final DecimalFormat intFormat = generateIntFormat();

  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final DecimalFormat decimalFormat = generateDecimalFormat();

  @Getter(lazy = true)
  private final String localEnergyFormatted = generateEnergyFormatted(localEnergyPerSecond);

  @Getter(lazy = true)
  private final String totalEnergyFormatted = generateEnergyFormatted(totalEnergyPerSecond);

  @Getter(lazy = true)
  private final String moneyFormatted = generateMoneyFormatted();

  @Getter(lazy = true)
  private final String[] headings = generateHeadings();

  @Getter(lazy = true)
  private final String message = generateMessage();

  @Getter(lazy = true)
  private final String notYouMessage = generateNotYouMessage();

  @VisibleForTesting
  static ReceiverData createWaiting() {
    return new ReceiverData(true);
  }

  public static void waiting() {
    instance = createWaiting();
  }

  public static void waiting(TileEntityPowerReceiver tileEntity) {
    waiting();

    if (tileEntity != null)
      PacketDispatcher.sendToServer(
          PacketChangeRequiresReceiverData.startData(tileEntity.getOwner(), tileEntity.getPos()));
  }

  @VisibleForTesting
  static ReceiverData createReceiverDisabled() {
    return new ReceiverData(false);
  }

  public static void receiverDisabled() {
    instance = createReceiverDisabled();
  }

  @VisibleForTesting
  public static ReceiverData createReceiverData(
      long localEnergy, long totalEnergy, BigDecimal money, String moneySymbol, int defaultDigits) {
    return new ReceiverData(localEnergy, totalEnergy, money, moneySymbol, defaultDigits);
  }

  public static void setReceiverData(
      long localEnergy, long totalEnergy, BigDecimal money, String moneySymbol, int defaultDigits) {
    instance = createReceiverData(localEnergy, totalEnergy, money, moneySymbol, defaultDigits);
  }

  public static void stopSending() {
    PacketDispatcher.sendToServer(PacketChangeRequiresReceiverData.stopData());
  }

  public String formatOwnerName(TileEntityPowerReceiver tileEntity, EntityPlayer player) {
    return formatOwnerName(tileEntity, player.getUniqueID());
  }

  public String formatOwnerName(TileEntityPowerReceiver tileEntity, UUID player) {
    final String ownerName = tileEntity.getOwnerName();

    if (player.equals(tileEntity.getOwner())) {
      return ownerName;
    } else {
      return ownerName + getNotYouMessage();
    }
  }

  private ReceiverData(boolean waiting) {
    this.waiting = waiting;
    enabled = false;
    localEnergyPerSecond = 0L;
    totalEnergyPerSecond = 0L;
    moneyPerSecond = null;
    moneySymbol = null;
    defaultDigits = 0;
  }

  private ReceiverData(
      long localEnergy, long totalEnergy, BigDecimal money, String moneySymbol, int defaultDigits) {
    waiting = false;
    enabled = true;
    localEnergyPerSecond = localEnergy;
    totalEnergyPerSecond = totalEnergy;
    moneyPerSecond = money;
    this.moneySymbol = moneySymbol;
    this.defaultDigits = defaultDigits;
  }

  private DecimalFormatSymbols generateFormatSymbols() {
    final DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
    formatSymbols.setDecimalSeparator(I18n.format("gui.powermoney.decimalseparator").charAt(0));
    formatSymbols.setGroupingSeparator(I18n.format("gui.powermoney.groupingseparator").charAt(0));

    return formatSymbols;
  }

  private DecimalFormat generateIntFormat() {
    final DecimalFormat intFormat = new DecimalFormat();
    intFormat.setMaximumFractionDigits(0);
    intFormat.setMinimumFractionDigits(0);
    intFormat.setGroupingUsed(true);
    intFormat.setDecimalFormatSymbols(getFormatSymbols());

    return intFormat;
  }

  private DecimalFormat generateDecimalFormat() {
    final DecimalFormat decimalFormat = new DecimalFormat();
    decimalFormat.setMaximumFractionDigits(defaultDigits);
    decimalFormat.setMinimumFractionDigits(defaultDigits);
    decimalFormat.setGroupingUsed(true);
    decimalFormat.setDecimalFormatSymbols(getFormatSymbols());

    return decimalFormat;
  }

  private String generateEnergyFormatted(long energy) {
    return getIntFormat().format(energy)
        + ' '
        + I18n.format("gui.powermoney.energyunit")
        + I18n.format("gui.powermoney.persecond");
  }

  private String generateMoneyFormatted() {
    return getDecimalFormat().format(moneyPerSecond)
        + ' '
        + moneySymbol
        + I18n.format("gui.powermoney.persecond");
  }

  private String[] generateHeadings() {
    return new String[] {
      I18n.format("gui.powermoney.owner"),
      I18n.format("gui.powermoney.localenergy"),
      I18n.format("gui.powermoney.totalenergy"),
      I18n.format("gui.powermoney.totalearning")
    };
  }

  private String generateMessage() {
    return waiting ? I18n.format("gui.powermoney.waiting") : I18n.format("gui.powermoney.disabled");
  }

  private String generateNotYouMessage() {
    return " (" + I18n.format("gui.powermoney.owner.notyou") + ')';
  }
}
