package dev.aura.powermoney.network.packet.serverbound;

import dev.aura.powermoney.network.packet.AbstractMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/** @author The_Fireplace */
public abstract class AbstractServerMessageHandler<T extends IMessage>
    extends AbstractMessageHandler<T> {
  @Override
  public final IMessage handleClientMessage(EntityPlayer player, T message, MessageContext ctx) {
    return null;
  }
}
