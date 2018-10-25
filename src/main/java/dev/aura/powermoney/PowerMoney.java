package dev.aura.powermoney;

import dev.aura.powermoney.common.CommonProxy;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
  modid = PowerMoney.ID,
  name = PowerMoney.NAME,
  version = PowerMoney.VERSION,
  dependencies = PowerMoney.DEPENDENCIES,
  certificateFingerprint = PowerMoney.FINGERPRINT
)
public class PowerMoney {
  public static final String ID = "@id@";
  public static final String RESOURCE_PACKAGE = ID;
  public static final String RESOURCE_PREFIX = RESOURCE_PACKAGE + ":";
  public static final String NAME = "@name@";
  public static final String VERSION = "@version@";
  public static final String GROUP = "@group@";
  public static final String DESCRIPTION = "@description@";
  public static final String DEPENDENCIES = "after:spongeapi";
  public static final String FINGERPRINT = "2238d4a92d81ab407741a2fdb741cebddfeacba6";

  @Instance(ID)
  @Getter
  private static PowerMoney instance;

  @SidedProxy(
    clientSide = "dev.aura.powermoney.client.ClientProxy",
    serverSide = "dev.aura.powermoney.common.CommonProxy"
  )
  @Getter
  private static CommonProxy proxy;

  @Getter private static Logger logger;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    if (logger == null) logger = event.getModLog();

    PowerMoneyBlocks.generateBlocks();
    PowerMoneyItems.generateItems();

    // Event Handlers
    MinecraftForge.EVENT_BUS.register(PowerMoneyBlocks.registrar());
    MinecraftForge.EVENT_BUS.register(PowerMoneyItems.registrar());
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    // Nothing yet
  }
}
