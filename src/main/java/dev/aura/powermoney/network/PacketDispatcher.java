package dev.aura.powermoney.network;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.network.packet.clientbound.PacketSendReceiverData;
import dev.aura.powermoney.network.packet.serverbound.PacketChangeRequiresReceiverData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author coolAlias
 * @author The_Fireplace
 */
public class PacketDispatcher {
  private static byte packetId = 0;
  private static final SimpleNetworkWrapper dispatcher =
      NetworkRegistry.INSTANCE.newSimpleChannel(PowerMoney.ID);

  public static final void registerPackets() {
    registerMessage(
        PacketChangeRequiresReceiverData.Handler.class,
        PacketChangeRequiresReceiverData.class,
        Side.SERVER);
    registerMessage(
        PacketSendReceiverData.Handler.class, PacketSendReceiverData.class, Side.CLIENT);
  }

  /**
   * Registers a packet
   *
   * @param handlerClass The packet recieved handler
   * @param messageClass The packet class
   * @param reciver The side the packet will be sent to
   */
  private static final <P extends IMessage, H extends IMessageHandler<P, IMessage>>
      void registerMessage(Class<H> handlerClass, Class<P> messageClass, Side reciver) {
    dispatcher.registerMessage(handlerClass, messageClass, packetId++, reciver);
  }

  // Wrapper methods
  public static final void sendTo(IMessage message, EntityPlayerMP player) {
    dispatcher.sendTo(message, player);
  }

  public static final void sendToAll(IMessage message) {
    dispatcher.sendToAll(message);
  }

  public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
    dispatcher.sendToAllAround(message, point);
  }

  public static final void sendToAllAround(
      IMessage message, int dimension, double x, double y, double z, double range) {
    sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
  }

  public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
    sendToAllAround(
        message,
        new NetworkRegistry.TargetPoint(
            player.world.provider.getDimension(), player.posX, player.posY, player.posZ, range));
  }

  public static final void sendToDimension(IMessage message, int dimensionId) {
    dispatcher.sendToDimension(message, dimensionId);
  }

  public static final void sendToServer(IMessage message) {
    dispatcher.sendToServer(message);
  }
}
