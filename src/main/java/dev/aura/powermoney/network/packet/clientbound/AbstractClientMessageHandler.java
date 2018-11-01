package dev.aura.powermoney.network.packet.clientbound;

import dev.aura.powermoney.network.packet.AbstractMessageHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/** @author The_Fireplace */
public abstract class AbstractClientMessageHandler<T extends IMessage>
    extends AbstractMessageHandler<T> {
  @Override
  public final IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx) {
    return null;
  }
}
