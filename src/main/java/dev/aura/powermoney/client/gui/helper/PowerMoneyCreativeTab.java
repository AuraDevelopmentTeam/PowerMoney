package dev.aura.powermoney.client.gui.helper;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.PowerMoneyBlocks;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PowerMoneyCreativeTab extends CreativeTabs {
  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final ItemStack iconItem = new ItemStack(PowerMoneyBlocks.powerReceiver());

  public PowerMoneyCreativeTab() {
    super(PowerMoney.ID);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public ItemStack createIcon() {
    return getIconItem();
  }
}
