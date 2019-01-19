package dev.aura.powermoney.network.packet.serverbound;

import dev.aura.powermoney.common.advancement.CriterionRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEasterEgg implements IMessage {
  @Override
  public void fromBytes(ByteBuf buf) {
    // No data to send. Do nothing
  }

  @Override
  public void toBytes(ByteBuf buf) {
    // No data to send. Do nothing
  }

  public static class Handler extends AbstractServerMessageHandler<PacketEasterEgg> {
    @Override
    public IMessage handleServerMessage(
        EntityPlayer player, PacketEasterEgg message, MessageContext ctx) {
      CriterionRegistry.EASTER_EGG.trigger((EntityPlayerMP) player);

      return null;
    }
  }
}
