package dev.aura.powermoney.common.handler;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.capability.EnergyConsumer;
import java.math.BigInteger;
import java.util.Map.Entry;
import java.util.UUID;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@NoArgsConstructor(staticName = "registrar")
public class PowerMoneyTickHandler {
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onServerTick(WorldTickEvent event) {
    // Make sure we're
    // - At the end of a tick
    // - Running a server
    // - Only running every 20 ticks
    if ((event.phase != Phase.END)
        || (event.side != Side.SERVER)
        || ((event.world.getTotalWorldTime() % 20L) != 0L)) return;

    for (Entry<UUID, BigInteger> entry : EnergyConsumer.getAndResetConsumedEnergy().entrySet()) {
      PowerMoney.getLogger().info("Player {} collected {} enery", entry.getKey(), entry.getValue());
    }
  }
}
