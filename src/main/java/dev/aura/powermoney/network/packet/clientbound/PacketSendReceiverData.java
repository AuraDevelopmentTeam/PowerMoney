package dev.aura.powermoney.network.packet.clientbound;

import dev.aura.powermoney.network.helper.SerializationHelper;
import io.netty.buffer.ByteBuf;
import java.math.BigDecimal;
import java.math.BigInteger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendReceiverData implements IMessage {
  private BigInteger energy;
  private BigDecimal money;

  public PacketSendReceiverData() {}

  public PacketSendReceiverData(BigInteger energy, BigDecimal money) {
    this.energy = (energy == null) ? BigInteger.ZERO : energy;
    this.money = (money == null) ? BigDecimal.ZERO : money;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    energy = SerializationHelper.readBigInteger(buf);
    money = SerializationHelper.readBigDecimal(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    SerializationHelper.writeBigInteger(buf, energy);
    SerializationHelper.writeBigDecimal(buf, money);
  }

  public static class Handler extends AbstractClientMessageHandler<PacketSendReceiverData> {
    @Override
    public IMessage handleClientMessage(
        EntityPlayer player, PacketSendReceiverData message, MessageContext ctx) {
      // TODO
      return null;
    }
  }
}
