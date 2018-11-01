package dev.aura.powermoney.client.handler;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.config.PowerMoneyConfigWrapper;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@NoArgsConstructor(staticName = "registrar")
public class ConfigChangedHandler {
  @SubscribeEvent
  public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID() != PowerMoney.ID) return;

    PowerMoneyConfigWrapper.loadConfig();
  }
}
