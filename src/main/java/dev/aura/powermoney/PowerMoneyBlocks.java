package dev.aura.powermoney;

import dev.aura.powermoney.common.block.BlockPowerReceiver;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.NoArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@NoArgsConstructor(staticName = "registrar")
public class PowerMoneyBlocks {
  private static final Map<String, Block> blocks = new LinkedHashMap<>();

  protected static void generateBlocks() {
    PowerMoney.getLogger().debug("Registering blocks");

    addBlock("power_receiver", new BlockPowerReceiver());
  }

  public static void addBlock(String name, Block block) {
    if (blocks.containsKey(name)) return;

    PowerMoney.getLogger()
        .trace("Registering block {} of type {}", name, block.getClass().getName());

    if (block.getRegistryName() == null) {
      block.setTranslationKey(name).setRegistryName(PowerMoney.ID, name);
    }

    blocks.put(name, block);
    ItemBlock item = new ItemBlock(block);

    PowerMoneyItems.addItem(name, item);
  }

  /** @return the instance of PowerReceiver */
  public static final Block brainStone() {
    return blocks.get("power_receiver");
  }

  @SubscribeEvent
  public void registerBlocks(RegistryEvent.Register<Block> event) {
    blocks
        .values()
        .stream()
        .forEach(
            block -> {
              event.getRegistry().register(block);
            });
  }
}
