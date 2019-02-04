package dev.aura.powermoney.common.compat.hwyla;

import dev.aura.powermoney.client.gui.GuiPowerReceiver;
import dev.aura.powermoney.client.helper.ReceiverDataClient;
import dev.aura.powermoney.common.handler.PowerMoneyTickHandler;
import dev.aura.powermoney.common.helper.WorldBlockPos;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import dev.aura.powermoney.network.packet.clientbound.PacketSendReceiverData;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PowerReceiverDataProvider implements IWailaDataProvider {
  private static final String NBT_ID = "id";
  private static final String NBT_DISABLED = "disabled";
  private static final String NBT_LOCAL_ENERGY = "localenergy";
  private static final String NBT_TOTAL_ENERGY = "totalenergy";
  private static final String NBT_MONEY = "money";
  private static final String NBT_MONEY_RAW = NBT_MONEY + "_raw";
  private static final String NBT_MONEY_SCALE = NBT_MONEY + "scale";
  private static final String NBT_MONEY_SYMBOL = "moneysymbol";
  private static final String NBT_DEFAULT_DIGITS = "defaultdigits";

  private static int serverDataID = 0;
  private static int clientDataID = -1;

  @Override
  public List<String> getWailaBody(
      ItemStack itemStack,
      List<String> tooltip,
      IWailaDataAccessor accessor,
      IWailaConfigHandler config) {
    final TileEntity tempTileEntity = accessor.getTileEntity();

    if (!(tempTileEntity instanceof TileEntityPowerReceiver)) {
      return tooltip;
    }

    // If the player has a PowerReceiver GUI open (which handles the data syncing)
    if (!(Minecraft.getMinecraft().currentScreen instanceof GuiPowerReceiver)) {
      deserializeNBT(accessor.getNBTData());
    }

    final TileEntityPowerReceiver tileEntity = (TileEntityPowerReceiver) tempTileEntity;
    final UUID uuid = accessor.getPlayer().getUniqueID();
    final ReceiverDataClient receiverData = ReceiverDataClient.getInstance();
    final String[] headings = receiverData.getHeadings();
    final String[] data = new String[4];

    data[0] = receiverData.formatOwnerName(tileEntity, uuid);

    if (receiverData.isWaiting() || !receiverData.isEnabled()) {
      data[1] = data[2] = data[3] = receiverData.getMessage();
    } else {
      data[1] = receiverData.getLocalEnergyFormatted();
      data[2] = receiverData.getTotalEnergyFormatted();
      data[3] = receiverData.getMoneyFormatted();
    }

    for (int i = 0; i < (receiverData.isEnabled() ? headings.length : 1); ++i) {
      tooltip.add(headings[i] + ": " + data[i]);
    }

    if (!receiverData.isEnabled()) {
      tooltip.add(data[1]);
    }

    return tooltip;
  }

  @Override
  public NBTTagCompound getNBTData(
      EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
    if (te instanceof TileEntityPowerReceiver) {
      final TileEntityPowerReceiver tileEntity = (TileEntityPowerReceiver) te;

      serializeNBT(tag, tileEntity.getOwner(), new WorldBlockPos(world, pos));
    }

    return tag;
  }

  private static void serializeNBT(NBTTagCompound tag, UUID blockOwner, WorldBlockPos worldPos) {
    final IMessage packet = PowerMoneyTickHandler.getDataPacket(blockOwner, worldPos);

    tag.setInteger(NBT_ID, ++serverDataID);

    if (packet instanceof PacketSendReceiverData) {
      final PacketSendReceiverData receiverData = (PacketSendReceiverData) packet;

      tag.setLong(NBT_LOCAL_ENERGY, receiverData.getLocalEnergy());
      tag.setLong(NBT_TOTAL_ENERGY, receiverData.getTotalEnergy());
      tag.setByteArray(NBT_MONEY_RAW, receiverData.getMoney().unscaledValue().toByteArray());
      tag.setInteger(NBT_MONEY_SCALE, receiverData.getMoney().scale());
      tag.setString(NBT_MONEY_SYMBOL, receiverData.getMoneySymbol());
      tag.setInteger(NBT_DEFAULT_DIGITS, receiverData.getDefaultDigits());
    } else {
      tag.setBoolean(NBT_DISABLED, true);
    }
  }

  private static void deserializeNBT(NBTTagCompound tag) {
    if (tag.getInteger(NBT_ID) == clientDataID) {
      return;
    }

    clientDataID = tag.getInteger(NBT_ID);

    if (tag.hasKey(NBT_DISABLED) && tag.getBoolean(NBT_DISABLED)) {
      ReceiverDataClient.receiverDisabled();
    } else if (tag.hasKey(NBT_LOCAL_ENERGY)
        && tag.hasKey(NBT_TOTAL_ENERGY)
        && tag.hasKey(NBT_MONEY_RAW)
        && tag.hasKey(NBT_MONEY_SCALE)
        && tag.hasKey(NBT_MONEY_SYMBOL)
        && tag.hasKey(NBT_DEFAULT_DIGITS)) {
      final long localEnergy = tag.getLong(NBT_LOCAL_ENERGY);
      final long totalEnergy = tag.getLong(NBT_TOTAL_ENERGY);
      final BigInteger moneyRaw = new BigInteger(tag.getByteArray(NBT_MONEY_RAW));
      final int moneyScale = tag.getInteger(NBT_MONEY_SCALE);
      final BigDecimal money = new BigDecimal(moneyRaw, moneyScale);
      final String moneySymbol = tag.getString(NBT_MONEY_SYMBOL);
      final int defaultDigits = tag.getInteger(NBT_DEFAULT_DIGITS);

      ReceiverDataClient.setReceiverData(
          localEnergy, totalEnergy, money, moneySymbol, defaultDigits);
    } else {
      ReceiverDataClient.waiting();
    }
  }
}
