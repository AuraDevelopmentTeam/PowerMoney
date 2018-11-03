package dev.aura.powermoney.network.packet.clientbound;

import dev.aura.powermoney.client.gui.GuiPowerReceiver;
import dev.aura.powermoney.common.payment.SpongeMoneyInterface;
import dev.aura.powermoney.network.helper.SerializationHelper;
import io.netty.buffer.ByteBuf;
import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.EqualsAndHashCode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@EqualsAndHashCode
public class PacketSendReceiverData implements IMessage {
  private BigInteger energy;
  private BigDecimal money;
  private String moneySymbol;
  private int defaultDigits;

  public PacketSendReceiverData() {}

  public PacketSendReceiverData(BigInteger energy, BigDecimal money) {
    this.energy = (energy == null) ? BigInteger.ZERO : energy;
    this.money = (money == null) ? BigDecimal.ZERO : money;

    moneySymbol = SpongeMoneyInterface.getMoneySymbol();
    defaultDigits = SpongeMoneyInterface.getDefaultDigits();
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    energy = SerializationHelper.readBigInteger(buf);
    money = SerializationHelper.readBigDecimal(buf);
    moneySymbol = ByteBufUtils.readUTF8String(buf);
    defaultDigits = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    SerializationHelper.writeBigInteger(buf, energy);
    SerializationHelper.writeBigDecimal(buf, money);
    ByteBufUtils.writeUTF8String(buf, moneySymbol);
    buf.writeInt(defaultDigits);
  }

  public static class Handler extends AbstractClientMessageHandler<PacketSendReceiverData> {
    @Override
    public IMessage handleClientMessage(
        EntityPlayer player, PacketSendReceiverData message, MessageContext ctx) {
      GuiPowerReceiver.setReceiverData(
          message.energy, message.money, message.moneySymbol, message.defaultDigits);

      return null;
    }
  }
}
