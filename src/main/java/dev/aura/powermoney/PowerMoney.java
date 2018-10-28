package dev.aura.powermoney;

import dev.aura.powermoney.client.gui.helper.PowerMoneyCreativeTab;
import dev.aura.powermoney.common.CommonProxy;
import dev.aura.powermoney.common.handler.PowerMoneyTickHandler;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import dev.aura.powermoney.network.PowerMoneyGuiHandler;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(
  modid = PowerMoney.ID,
  name = PowerMoney.NAME,
  version = PowerMoney.VERSION,
  dependencies = PowerMoney.DEPENDENCIES,
  certificateFingerprint = PowerMoney.FINGERPRINT
)
public class PowerMoney {
  // Hardcode ID to be able to start mod from Eclipse
  public static final String ID = "powermoney";
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

  public static final PowerMoneyCreativeTab creativeTab = new PowerMoneyCreativeTab();

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    if (logger == null) logger = event.getModLog();

    PowerMoneyBlocks.generateBlocks();
    PowerMoneyItems.generateItems();

    // Event Handlers
    MinecraftForge.EVENT_BUS.register(PowerMoneyBlocks.registrar());
    MinecraftForge.EVENT_BUS.register(PowerMoneyItems.registrar());
    MinecraftForge.EVENT_BUS.register(PowerMoneyTickHandler.registrar());
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new PowerMoneyGuiHandler());

    registerTileEntities(); // TileEntities
  }

  private static final void registerTileEntities() {
    GameRegistry.registerTileEntity(
        TileEntityPowerReceiver.class,
        new ResourceLocation(RESOURCE_PACKAGE, "tileentity_power_receiver"));
  }
}
