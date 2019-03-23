package dev.aura.powermoney;

import dev.aura.powermoney.api.MoneyInterface;
import dev.aura.powermoney.api.PowerMoneyApi;
import dev.aura.powermoney.client.handler.ConfigChangedHandler;
import dev.aura.powermoney.client.helper.PowerMoneyCreativeTab;
import dev.aura.powermoney.common.CommonProxy;
import dev.aura.powermoney.common.advancement.CriterionRegistry;
import dev.aura.powermoney.common.compat.PowerMoneyModules;
import dev.aura.powermoney.common.config.PowerMoneyConfigWrapper;
import dev.aura.powermoney.common.handler.PowerMoneyTickHandler;
import dev.aura.powermoney.common.payment.SimulateMoneyInterface;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import dev.aura.powermoney.network.PacketDispatcher;
import dev.aura.powermoney.network.PowerMoneyGuiHandler;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod(
  modid = PowerMoneyApi.ID,
  name = PowerMoneyApi.NAME,
  version = PowerMoneyApi.VERSION,
  dependencies = PowerMoneyModules.DEPENDENCIES,
  certificateFingerprint = PowerMoney.FINGERPRINT,
  guiFactory = PowerMoney.GUI_FACTORY
)
public class PowerMoney extends PowerMoneyApi {
  public static final String FINGERPRINT = "2238d4a92d81ab407741a2fdb741cebddfeacba6";
  public static final String GUI_FACTORY =
      "dev.aura.powermoney.client.gui.config.PowerMoneyGuiFactory";

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

  private final Map<String, MoneyInterface> moneyInterfaces = new LinkedHashMap<>();
  @Getter private MoneyInterface activeMoneyInterface;

  public static final PowerMoneyCreativeTab creativeTab = new PowerMoneyCreativeTab();

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    if (logger == null) logger = event.getModLog();

    // Set API instance
    PowerMoneyApi.setInstance(this);

    PacketDispatcher.registerPackets();

    PowerMoneyConfigWrapper.loadConfig(new Configuration(event.getSuggestedConfigurationFile()));

    PowerMoneyModules.detectActiveModules();

    PowerMoneyBlocks.generateBlocks();
    PowerMoneyItems.generateItems();

    PowerMoneyModules.preInit(event);

    // Event Handlers
    MinecraftForge.EVENT_BUS.register(PowerMoneyBlocks.registrar());
    MinecraftForge.EVENT_BUS.register(PowerMoneyItems.registrar());
    MinecraftForge.EVENT_BUS.register(PowerMoneyTickHandler.registrar());
    MinecraftForge.EVENT_BUS.register(PowerMoneySounds.registrar());

    if (event.getSide() == Side.CLIENT) {
      MinecraftForge.EVENT_BUS.register(ConfigChangedHandler.registrar());
    }
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    NetworkRegistry.INSTANCE.registerGuiHandler(this, PowerMoneyGuiHandler.registrar());

    registerTileEntities(); // TileEntities

    // Advancement stuff
    CriterionRegistry.init();

    PowerMoneyModules.init(event);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    PowerMoneyModules.postInit(event);

    selectMoneyInterface(event.getSide());
  }

  private void selectMoneyInterface(Side side) {
    final MoneyInterface simulateInterface = new SimulateMoneyInterface();

    registerMoneyInterface(simulateInterface);

    logger.debug("Available MoneyInterfaces: " + String.join(", ", moneyInterfaces.keySet()));

    final String requestedMoneyInterface = PowerMoneyConfigWrapper.getMoneyInterface();
    activeMoneyInterface = moneyInterfaces.get(requestedMoneyInterface);

    if (activeMoneyInterface == null) {
      if (!"auto".equals(requestedMoneyInterface)) {
        logger.warn("The MoneyInterface \"" + requestedMoneyInterface + "\" is unknown.");
        logger.info("Falling back to automatically selecting a MoneyInterface");
      }
    } else if (!activeMoneyInterface.canAcceptMoney()) {
      logger.warn("The MoneyInterface that has been selected cannot accept money.");
      logger.info("Falling back to automatically selecting a MoneyInterface");
    } else {
      return;
    }

    for (Map.Entry<String, MoneyInterface> entry : moneyInterfaces.entrySet()) {
      final String name = entry.getKey();
      final MoneyInterface moneyInterface = entry.getValue();

      logger.debug("Checking if MoneyInterface \"" + name + "\" can accept money...");

      if (moneyInterface.canAcceptMoney()) {
        logger.debug("    Yes!");
        logger.info("Selecting MoneyInterface \"" + name + "\"!");

        activeMoneyInterface = moneyInterface;

        break;
      } else {
        logger.debug("    No!");
      }
    }

    if (activeMoneyInterface == null) {
      if (side == Side.CLIENT) {
        logger.debug(
            "No working MoneyInterface found. That's ok on the client, as the server may use Sponge.");
      } else if (side == Side.SERVER) {
        logger.warn("No working MoneyInterface found! This is means the mod will NOT work");
      }

      logger.info("Selecting \"powermoney:simulate\" anyways.");

      activeMoneyInterface = simulateInterface;
    }
  }

  private static final void registerTileEntities() {
    GameRegistry.registerTileEntity(
        TileEntityPowerReceiver.class,
        new ResourceLocation(RESOURCE_PACKAGE, "tileentity_power_receiver"));
  }

  @Override
  public void registerMoneyInterface(MoneyInterface moneyInterface) {
    moneyInterfaces.put(moneyInterface.getName(), moneyInterface);
  }

  @Override
  public String getConfiguredCurrencyName() {
    return PowerMoneyConfigWrapper.getCurrency();
  }
}
