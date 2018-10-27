package dev.aura.powermoney.common.block;

import dev.aura.powermoney.PowerMoney;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockPowerReceiver extends Block {
  public BlockPowerReceiver() {
    super(Material.IRON);

    setCreativeTab(PowerMoney.creativeTab);
  }
}
