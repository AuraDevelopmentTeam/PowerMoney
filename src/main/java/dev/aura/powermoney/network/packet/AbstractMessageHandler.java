package dev.aura.powermoney.network.packet;

import dev.aura.powermoney.PowerMoney;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** @author The_Fireplace */
public abstract class AbstractMessageHandler<T extends IMessage>
    implements IMessageHandler<T, IMessage> {
  @SideOnly(Side.CLIENT)
  /**
   * Called when a message is received of the appropriate type on the client. You can optionally
   * return a reply message, or null if no reply is needed.
   *
   * @param player The player of that message
   * @param message The message
   * @param ctx The message's context
   * @return an optional return message
   */
  public abstract IMessage handleClientMessage(EntityPlayer player, T message, MessageContext ctx);

  /**
   * Called when a message is received of the appropriate type on the server. You can optionally
   * return a reply message, or null if no reply is needed.
   *
   * @param player The player of that message
   * @param message The message
   * @param ctx The message's context
   * @return an optional return message
   */
  public abstract IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx);

  @Override
  public IMessage onMessage(T message, MessageContext ctx) {
    if (ctx.side.isClient())
      return handleClientMessage(PowerMoney.getProxy().getPlayerEntity(ctx), message, ctx);
    else return handleServerMessage(PowerMoney.getProxy().getPlayerEntity(ctx), message, ctx);
  }
}
