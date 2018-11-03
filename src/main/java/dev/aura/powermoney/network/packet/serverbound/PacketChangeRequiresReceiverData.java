package dev.aura.powermoney.network.packet.serverbound;

import dev.aura.powermoney.common.handler.PowerMoneyTickHandler;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@EqualsAndHashCode
public class PacketChangeRequiresReceiverData implements IMessage {
  private boolean requiresData;
  private UUID blockOwner;

  public PacketChangeRequiresReceiverData() {}

  private PacketChangeRequiresReceiverData(boolean requiresData, UUID blockOwner) {
    this.requiresData = requiresData;
    this.blockOwner = blockOwner;
  }

  public static PacketChangeRequiresReceiverData startData(UUID blockOwner) {
    return new PacketChangeRequiresReceiverData(true, blockOwner);
  }

  public static PacketChangeRequiresReceiverData stopData() {
    return new PacketChangeRequiresReceiverData(false, null);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    requiresData = buf.readBoolean();

    if (requiresData) {
      long mostSignificantBits = buf.readLong();
      long leastSignificantBits = buf.readLong();

      blockOwner = new UUID(mostSignificantBits, leastSignificantBits);
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeBoolean(requiresData);

    if (requiresData) {
      buf.writeLong(blockOwner.getMostSignificantBits());
      buf.writeLong(blockOwner.getLeastSignificantBits());
    }
  }

  public static class Handler
      extends AbstractServerMessageHandler<PacketChangeRequiresReceiverData> {
    @Override
    public IMessage handleServerMessage(
        EntityPlayer player, PacketChangeRequiresReceiverData message, MessageContext ctx) {
      if (message.requiresData) {
        PowerMoneyTickHandler.addDataReceiver(player.getUniqueID(), message.blockOwner);

        return PowerMoneyTickHandler.getDataPacket(message.blockOwner);
      } else {
        PowerMoneyTickHandler.removeDataReceiver(player.getUniqueID());

        return null;
      }
    }
  }
}
