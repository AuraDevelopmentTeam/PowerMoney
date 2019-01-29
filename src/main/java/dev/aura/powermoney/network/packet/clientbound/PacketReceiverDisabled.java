package dev.aura.powermoney.network.packet.clientbound;

import dev.aura.powermoney.client.helper.ReceiverData;
import io.netty.buffer.ByteBuf;
import lombok.EqualsAndHashCode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@EqualsAndHashCode
public class PacketReceiverDisabled implements IMessage {
  public PacketReceiverDisabled() {}

  @Override
  public void fromBytes(ByteBuf buf) {
    // Nothing
  }

  @Override
  public void toBytes(ByteBuf buf) {
    // Nothing
  }

  public static class Handler extends AbstractClientMessageHandler<PacketReceiverDisabled> {
    @Override
    public IMessage handleClientMessage(
        EntityPlayer player, PacketReceiverDisabled message, MessageContext ctx) {
      ReceiverData.receiverDisabled();

      return null;
    }
  }
}
