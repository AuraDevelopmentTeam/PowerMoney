package dev.aura.powermoney.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {
  public void registerModel(Item item) {
    // Do nothing
  }

  public EntityPlayer getPlayerEntity(MessageContext ctx) {
    return ctx.getServerHandler().player;
  }
}
