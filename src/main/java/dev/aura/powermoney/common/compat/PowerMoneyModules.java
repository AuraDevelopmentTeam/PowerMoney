package dev.aura.powermoney.common.compat;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.compat.computercraft.ComputerCraftCompat;
import dev.aura.powermoney.common.compat.enderpay.EnderPayCompat;
import dev.aura.powermoney.common.compat.grandeconomy.GrandEconomyCompat;
import dev.aura.powermoney.common.compat.opencomputers.OpenComputersCompat;
import dev.aura.powermoney.common.compat.oxygen.OxygenCompat;
import dev.aura.powermoney.common.compat.sponge.SpongeCompat;
import dev.aura.powermoney.common.compat.theoneprobe.TheOneProbeCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@UtilityClass
public class PowerMoneyModules {
  public static final String SPONGEAPI_MODID = "spongeapi";
  public static final String BUILDCRAFT_MODID = "buildcraftenergy";
  public static final String COMPUTERCRAFT_MODID = "computercraft";
  public static final String ENDERPAY_MODID = "enderpay";
  public static final String GRANDECONOMY_MODID = "grandeconomy";
  public static final String HWYLA_MODID = "waila";
  public static final String IC2_MODID = "ic2";
  public static final String OPENCOMPUTERS_MODID = "opencomputers";
  public static final String OXYGEN_MODID = "oxygen_core";
  public static final String REDSTONEFLUX_MODID = "redstoneflux";
  public static final String TESLA_MODID = "tesla";
  public static final String THEONEPROBE_MODID = "theoneprobe";

  public static final String DEPENDENCIES =
      "after:"
          + SPONGEAPI_MODID
          + ";after:"
          + BUILDCRAFT_MODID
          + ";after:"
          + COMPUTERCRAFT_MODID
          + ";after:"
          + GRANDECONOMY_MODID
          + ";after:"
          + ENDERPAY_MODID
          + ";after:"
          + HWYLA_MODID
          + ";after:"
          + IC2_MODID
          + ";after:"
          + OPENCOMPUTERS_MODID
          + ";after:"
          + OXYGEN_MODID
          + ";after:"
          + REDSTONEFLUX_MODID
          + ";after:"
          + TESLA_MODID
          + ";after:"
          + THEONEPROBE_MODID;

  private static Boolean SPONGEAPI;
  private static Boolean BUILDCRAFT;
  private static Boolean COMPUTERCRAFT;
  private static Boolean ENDERPAY;
  private static Boolean GRANDECONOMY;
  private static Boolean HWYLA;
  private static Boolean IC2;
  private static Boolean OPENCOMPUTERS;
  private static Boolean OXYGEN;
  private static Boolean REDSTONEFLUX;
  private static Boolean TESLA;
  private static Boolean THEONEPROBE;

  @Getter private static final Set<ModuleInformation> allModules = detectAllModules();
  @Getter private static final Set<ModuleInformation> activeModules = new TreeSet<>();

  @Module(
    modid = SPONGEAPI_MODID,
    name = "SpongeAPI",
    message = "Hooking into the currency API of Sponge.",
    integration = SpongeCompat.class
  )
  public static boolean spongeAPI() {
    return testMod(SPONGEAPI_MODID, SPONGEAPI);
  }

  @Module(
    modid = BUILDCRAFT_MODID,
    name = "BuildCraft Energy",
    message = "Enabling support for BuildCraft energy."
  )
  public static boolean buildcraft() {
    return testMod(BUILDCRAFT_MODID, BUILDCRAFT);
  }

  @Module(
    modid = COMPUTERCRAFT_MODID,
    name = "ComputerCraft",
    message = "Adding the PowerReceiver as a CC peripheral.",
    integration = ComputerCraftCompat.class
  )
  public static boolean computerCraft() {
    return testMod(COMPUTERCRAFT_MODID, COMPUTERCRAFT);
  }

  @Module(
    modid = ENDERPAY_MODID,
    name = "EnderPay",
    message = "Adding the EnderPay MoneyInteface.",
    integration = EnderPayCompat.class
  )
  public static boolean enderPay() {
    return testMod(ENDERPAY_MODID, ENDERPAY);
  }

  @Module(
    modid = GRANDECONOMY_MODID,
    name = "Grand Economy",
    message = "Adding the GrandEconomy MoneyInteface.",
    integration = GrandEconomyCompat.class
  )
  public static boolean grandEconomy() {
    return testMod(GRANDECONOMY_MODID, GRANDECONOMY);
  }

  @Module(modid = HWYLA_MODID, name = "HWYLA", message = "Adding PowerReceiver stats to HWYLA.")
  public static boolean hwyla() {
    return testMod(HWYLA_MODID, HWYLA);
  }

  @Module(
    modid = IC2_MODID,
    name = "IndustrialCraft²",
    message = "Enabling support for IC2 energy."
  )
  public static boolean ic2() {
    return testMod(IC2_MODID, IC2);
  }

  @Module(
    modid = OPENCOMPUTERS_MODID,
    name = "OpenComputers",
    message = "Adding the PowerReceiver as a OC component.",
    integration = OpenComputersCompat.class
  )
  public static boolean openComputers() {
    return testMod(OPENCOMPUTERS_MODID, OPENCOMPUTERS);
  }

  @Module(
    modid = OXYGEN_MODID,
    name = "Oxygen",
    message = "Adding the Oxygen MoneyInteface.",
    integration = OxygenCompat.class
  )
  public static boolean oxygen() {
    return testMod(OXYGEN_MODID, OXYGEN);
  }

  @Module(
    modid = REDSTONEFLUX_MODID,
    name = "Redstone Flux",
    message = "Enabling support for RF energy."
  )
  public static boolean redstoneFlux() {
    return testMod(REDSTONEFLUX_MODID, REDSTONEFLUX);
  }

  @Module(modid = TESLA_MODID, name = "TESLA", message = "Enabling support for TESLA energy API.")
  public static boolean tesla() {
    return testMod(TESLA_MODID, TESLA);
  }

  @Module(
    modid = THEONEPROBE_MODID,
    name = "The One Probe",
    message = "Adding PowerReceiver stats to The One Probe.",
    integration = TheOneProbeCompat.class
  )
  public static boolean theOneProbe() {
    return testMod(THEONEPROBE_MODID, THEONEPROBE);
  }

  private static boolean testMod(String modID, Boolean... storage) {
    assert storage.length == 1;

    if (storage[0] == null) {
      storage[0] = Loader.isModLoaded(modID);
    }

    return storage[0];
  }

  private static Set<ModuleInformation> detectAllModules() {
    Set<ModuleInformation> modules = new TreeSet<>();
    Map<String, Field> modidsToFields =
        Arrays.asList(PowerMoneyModules.class.getDeclaredFields())
            .stream()
            .filter(field -> String.class.equals(field.getType()))
            .collect(
                Collectors.toMap(
                    field -> {
                      try {
                        return (String) field.get(null);
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                    },
                    field -> {
                      try {
                        return PowerMoneyModules.class.getDeclaredField(
                            field.getName().replace("_MODID", ""));
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                    }));

    for (Method method : PowerMoneyModules.class.getMethods()) {
      Module module = method.getAnnotation(Module.class);

      if (module != null) {
        try {
          modules.add(
              new ModuleInformation(
                  module.modid(),
                  module.name(),
                  module.message(),
                  modidsToFields.get(module.modid()),
                  method,
                  module.integration()));
        } catch (Exception e) {
          PowerMoney.getLogger().error(e);
        }
      }
    }

    return modules;
  }

  public static void detectActiveModules() {
    PowerMoney.getLogger().info("Checking available modules:");

    for (ModuleInformation module : allModules) {
      try {
        if (module.isActive()) {
          PowerMoney.getLogger()
              .info("\t" + module.getName() + " detected! " + module.getMessage());

          if (module.getIntegrationClass() != IModIntegration.class) {
            module.setIntegration(module.getIntegrationClass().newInstance());
          }

          activeModules.add(module);
        }
      } catch (Exception e) {
        PowerMoney.getLogger().error(e);
      }
    }

    PowerMoney.getLogger().debug("Finished checking available modules!");
  }

  public static void preInit(FMLPreInitializationEvent event) {
    forEachActiveModule(integration -> integration.preInit(event));
  }

  public static void init(FMLInitializationEvent event) {
    forEachActiveModule(integration -> integration.init(event));
  }

  public static void postInit(FMLPostInitializationEvent event) {
    forEachActiveModule(integration -> integration.postInit(event));
  }

  private static void forEachActiveModule(Consumer<? super IModIntegration> consumer) {
    activeModules
        .stream()
        .map(ModuleInformation::getIntegration)
        .filter(Objects::nonNull)
        .forEach(consumer);
  }
}
