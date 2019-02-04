package dev.aura.powermoney.network.packet.serverbound;

import dev.aura.powermoney.common.handler.PowerMoneyTickHandler;
import dev.aura.powermoney.common.helper.WorldBlockPos;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@EqualsAndHashCode
public class PacketChangeRequiresReceiverData implements IMessage {
  private boolean requiresData;
  private UUID blockOwner;
  private BlockPos pos;

  public PacketChangeRequiresReceiverData() {}

  private PacketChangeRequiresReceiverData(boolean requiresData, UUID blockOwner, BlockPos pos) {
    this.requiresData = requiresData;
    this.blockOwner = blockOwner;
    this.pos = pos;
  }

  public static PacketChangeRequiresReceiverData startData(UUID blockOwner, BlockPos pos) {
    return new PacketChangeRequiresReceiverData(true, blockOwner, pos);
  }

  public static PacketChangeRequiresReceiverData stopData() {
    return new PacketChangeRequiresReceiverData(false, null, null);
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    requiresData = buf.readBoolean();

    if (requiresData) {
      final long mostSignificantBits = buf.readLong();
      final long leastSignificantBits = buf.readLong();

      blockOwner = new UUID(mostSignificantBits, leastSignificantBits);

      pos = BlockPos.fromLong(buf.readLong());
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeBoolean(requiresData);

    if (requiresData) {
      buf.writeLong(blockOwner.getMostSignificantBits());
      buf.writeLong(blockOwner.getLeastSignificantBits());

      buf.writeLong(pos.toLong());
    }
  }

  public static class Handler
      extends AbstractServerMessageHandler<PacketChangeRequiresReceiverData> {
    @Override
    public IMessage handleServerMessage(
        EntityPlayer player, PacketChangeRequiresReceiverData message, MessageContext ctx) {
      if (message.requiresData) {
        final WorldBlockPos worldPos = new WorldBlockPos(player.world, message.pos);

        PowerMoneyTickHandler.addDataReceiver(player.getUniqueID(), message.blockOwner, worldPos);

        return PowerMoneyTickHandler.getDataPacket(message.blockOwner, worldPos);
      } else {
        PowerMoneyTickHandler.removeDataReceiver(player.getUniqueID());

        return null;
      }
    }
  }
}
