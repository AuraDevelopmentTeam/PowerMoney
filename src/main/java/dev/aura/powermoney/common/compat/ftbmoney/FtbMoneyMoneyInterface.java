package dev.aura.powermoney.common.compat.ftbmoney;

import com.feed_the_beast.mods.money.FTBMoney;
import dev.aura.powermoney.api.MoneyInterface;
import dev.aura.powermoney.api.PowerMoneyApi;
import java.math.BigDecimal;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class FtbMoneyMoneyInterface implements MoneyInterface {
  @Override
  public String getName() {
    return PowerMoneyApi.RESOURCE_PREFIX + FTBMoney.MOD_ID;
  }

  @Override
  public boolean canAcceptMoney() {
    return true;
  }

  @Override
  public void addMoneyToPlayer(UUID player, BigDecimal money) {
    final EntityPlayer entityPlayer =
        FMLCommonHandler.instance()
            .getMinecraftServerInstance()
            .getPlayerList()
            .getPlayerByUUID(player);

    FTBMoney.setMoney(entityPlayer, FTBMoney.getMoney(entityPlayer) + money.longValue());
  }

  @Override
  public String getCurrencySymbol() {
    return "\u0398";
  }

  @Override
  public int getDefaultDigits() {
    return 0;
  }
}
