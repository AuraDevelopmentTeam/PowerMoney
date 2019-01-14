package dev.aura.powermoney.common.handler;

import com.google.common.collect.ImmutableMap;
import dev.aura.powermoney.common.capability.EnergyConsumer;
import dev.aura.powermoney.common.config.PowerMoneyConfigWrapper;
import dev.aura.powermoney.common.payment.SpongeMoneyInterface;
import dev.aura.powermoney.common.util.WorldBlockPos;
import dev.aura.powermoney.network.PacketDispatcher;
import dev.aura.powermoney.network.packet.clientbound.PacketReceiverDisabled;
import dev.aura.powermoney.network.packet.clientbound.PacketSendReceiverData;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.Value;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

@NoArgsConstructor(staticName = "registrar")
public class PowerMoneyTickHandler {
  private static final long TICKS_PER_SECOND = 20L;

  private static final Map<UUID, ReceiverPostion> dataReceivers = new HashMap<>();

  private static ImmutableMap<WorldBlockPos, BigInteger> consumedLocalEnergy;
  private static ImmutableMap<UUID, BigInteger> consumedTotalEnergy;
  private static ImmutableMap<UUID, BigDecimal> generatedMoney;

  private final Map<UUID, BigDecimal> payout = new HashMap<>();

  public static void addDataReceiver(UUID receiver, UUID blockOwner, WorldBlockPos worldPos) {
    if (canReceiveEnergy()) {
      dataReceivers.put(receiver, new ReceiverPostion(blockOwner, worldPos));
    }
  }

  public static void removeDataReceiver(UUID receiver) {
    dataReceivers.remove(receiver);
  }

  public static IMessage getDataPacket(UUID blockOwner, WorldBlockPos worldPos) {
    if (canReceiveEnergy()) {
      return new PacketSendReceiverData(
          getLocalConsumedEnergy(worldPos),
          getConsumedEnergy(blockOwner),
          getGeneratedMoney(blockOwner));
    } else {
      return new PacketReceiverDisabled();
    }
  }

  public static BigInteger getLocalConsumedEnergy(WorldBlockPos worldPos) {
    return Optional.ofNullable(consumedLocalEnergy.get(worldPos)).orElse(BigInteger.ZERO);
  }

  public static BigInteger getConsumedEnergy(UUID blockOwner) {
    return Optional.ofNullable(consumedTotalEnergy.get(blockOwner)).orElse(BigInteger.ZERO);
  }

  public static BigDecimal getGeneratedMoney(UUID blockOwner) {
    return Optional.ofNullable(generatedMoney.get(blockOwner)).orElse(BigDecimal.ZERO);
  }

  private static boolean canReceiveEnergy() {
    return PowerMoneyConfigWrapper.getSimulate() || SpongeMoneyInterface.canAcceptMoney();
  }

  @SuppressFBWarnings(
    value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
    justification = "Only one instance of this class exists."
  )
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onServerTick(WorldTickEvent event) {
    // Make sure we're
    // - At the end of a tick
    // - Running a server
    // - Only running every 20 ticks
    // - Able to accept money or simulating
    if ((event.phase != Phase.END)
        || (event.side != Side.SERVER)
        || (event.world.provider.getDimensionType() != DimensionType.OVERWORLD)
        || ((event.world.getTotalWorldTime() % TICKS_PER_SECOND) != 0L)
        || !canReceiveEnergy()) return;

    final ImmutableMap<WorldBlockPos, BigInteger> tempConsumedLocalEnergy =
        EnergyConsumer.getAndResetConsumedLocalEnergy();
    final ImmutableMap<UUID, BigInteger> tempConsumedTotalEnergy =
        EnergyConsumer.getAndResetConsumedTotalEnergy();
    final ImmutableMap.Builder<UUID, BigDecimal> generatedMoneyBuilder = ImmutableMap.builder();

    for (Entry<UUID, BigInteger> entry : tempConsumedTotalEnergy.entrySet()) {
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

    consumedLocalEnergy = tempConsumedLocalEnergy;
    consumedTotalEnergy = tempConsumedTotalEnergy;
    generatedMoney = generatedMoneyBuilder.build();

    // Send update packets
    for (Map.Entry<UUID, ReceiverPostion> entry : dataReceivers.entrySet()) {
      PacketDispatcher.sendTo(
          getDataPacket(entry.getValue().getUuid(), entry.getValue().getWorldPos()),
          event.world.getMinecraftServer().getPlayerList().getPlayerByUUID(entry.getKey()));
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

  @Value
  private static class ReceiverPostion {
    private final UUID uuid;
    private final WorldBlockPos worldPos;
  }
}
