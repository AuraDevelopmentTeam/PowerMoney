package dev.aura.powermoney.client;

import dev.aura.powermoney.common.CommonProxy;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
  private static final String INVENTORY = "inventory";

  private static void registerModel(Item item, ModelResourceLocation model) {
    registerModel(item, 0, model);
  }

  private static void registerModel(Item item, int meta, ModelResourceLocation model) {
    ModelLoader.setCustomModelResourceLocation(item, meta, model);
  }

  @Override
  public void registerModel(Item item) {
    final String prefix = item.getRegistryName().toString();

    registerModel(item, new ModelResourceLocation(prefix, INVENTORY));
  }
}
