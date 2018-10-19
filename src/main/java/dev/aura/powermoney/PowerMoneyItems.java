package dev.aura.powermoney;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.NoArgsConstructor;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@NoArgsConstructor(staticName = "registrar")
public class PowerMoneyItems {
  private static final Map<String, Item> items = new LinkedHashMap<>();

  protected static void generateItems() {
    PowerMoney.getLogger().debug("Registering blocks");

    // TODO
  }

  public static void addItem(String name, Item item) {
    if (items.containsKey(name)) return;

    PowerMoney.getLogger().trace("Registering item {} of type {}", name, item.getClass().getName());

    if (item.getRegistryName() == null) {
      item.setTranslationKey(name).setRegistryName(PowerMoney.ID, name);
    }

    items.put(name, item);
  }

  @SubscribeEvent
  public void registerItems(RegistryEvent.Register<Item> event) {
    items
        .values()
        .stream()
        .forEach(
            item -> {
              event.getRegistry().register(item);
              // TODO
              // BrainStone.proxy.registerModel(item);
            });
  }
}
