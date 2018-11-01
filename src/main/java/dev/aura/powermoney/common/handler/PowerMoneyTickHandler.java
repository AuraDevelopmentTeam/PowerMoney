package dev.aura.powermoney.common.handler;

import com.google.common.collect.ImmutableMap;
import dev.aura.powermoney.common.capability.EnergyConsumer;
import dev.aura.powermoney.common.config.PowerMoneyConfigWrapper;
import dev.aura.powermoney.common.payment.SpongeMoneyInterface;
import dev.aura.powermoney.network.PacketDispatcher;
import dev.aura.powermoney.network.packet.clientbound.PacketSendReceiverData;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import lombok.NoArgsConstructor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@NoArgsConstructor(staticName = "registrar")
public class PowerMoneyTickHandler {
  private static final long TICKS_PER_SECOND = 20L;

  private static final Map<UUID, UUID> dataReceivers = new HashMap<>();

  private static ImmutableMap<UUID, BigInteger> consumedEnergy;
  private static ImmutableMap<UUID, BigDecimal> generatedMoney;

  private final Map<UUID, BigDecimal> payout = new HashMap<>();

  public static void addDataReceiver(UUID receiver, UUID blockOwner) {
    dataReceivers.put(receiver, blockOwner);
  }

  public static void removeDataReceiver(UUID receiver) {
    dataReceivers.remove(receiver);
  }

  public static PacketSendReceiverData getDataPacket(UUID blockOwner) {
    return new PacketSendReceiverData(
        consumedEnergy.get(blockOwner), generatedMoney.get(blockOwner));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onServerTick(WorldTickEvent event) {
    // Make sure we're
    // - At the end of a tick
    // - Running a server
    // - Only running every 20 ticks
    // - Able to accept money or simulating
    if ((event.phase != Phase.END)
        || (event.side != Side.SERVER)
        || ((event.world.getTotalWorldTime() % TICKS_PER_SECOND) != 0L)
        || !(PowerMoneyConfigWrapper.getSimulate() || SpongeMoneyInterface.canAcceptMoney()))
      return;

    final ImmutableMap<UUID, BigInteger> tempConsumedEnergy =
        EnergyConsumer.getAndResetConsumedEnergy();
    final ImmutableMap.Builder<UUID, BigDecimal> generatedMoneyBuilder = ImmutableMap.builder();

    for (Entry<UUID, BigInteger> entry : tempConsumedEnergy.entrySet()) {
      final UUID player = entry.getKey();
      final BigDecimal earnedMoney =
          PowerMoneyConfigWrapper.getMoneyCalculator().covertEnergyToMoney(entry.getValue());
      BigDecimal playerPayout;

      if (payout.containsKey(player)) {
        playerPayout = payout.get(player).add(earnedMoney);
      } else {
        playerPayout = earnedMoney;
      }

      generatedMoneyBuilder.put(player, earnedMoney);
      payout.put(player, playerPayout);
    }

    consumedEnergy = tempConsumedEnergy;
    generatedMoney = generatedMoneyBuilder.build();

    // Send update packets
    for (Map.Entry<UUID, UUID> entry : dataReceivers.entrySet()) {
      PacketDispatcher.sendTo(
          getDataPacket(entry.getValue()),
          (EntityPlayerMP) event.world.getPlayerEntityByUUID(entry.getKey()));
    }

    // Check if we can payout
    if ((event.world.getTotalWorldTime()
            % (TICKS_PER_SECOND * PowerMoneyConfigWrapper.getPayoutInterval()))
        != 0L) return;

    // Only actually pay when we can
    if (SpongeMoneyInterface.canAcceptMoney()) {
      for (Entry<UUID, BigDecimal> entry : payout.entrySet()) {
        SpongeMoneyInterface.addMoneyToPlayer(entry.getKey(), entry.getValue());
      }
    }

    // Always clear the payouts
    payout.clear();
  }
}
