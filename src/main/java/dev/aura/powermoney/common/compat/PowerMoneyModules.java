package dev.aura.powermoney.common.compat;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.compat.computercraft.ComputerCraftCompat;
import dev.aura.powermoney.common.compat.opencomputers.OpenComputersCompat;
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
  public static final String COMPUTERCRAFT_MODID = "computercraft";
  public static final String OPENCOMPUTERS_MODID = "opencomputers";
  public static final String TESLA_MODID = "tesla";

  public static final String DEPENDENCIES =
      "after:"
          + SPONGEAPI_MODID
          + ";after:"
          + COMPUTERCRAFT_MODID
          + ";after:"
          + OPENCOMPUTERS_MODID
          + ";after:"
          + TESLA_MODID;

  private static Boolean SPONGEAPI;
  private static Boolean COMPUTERCRAFT;
  private static Boolean OPENCOMPUTERS;
  private static Boolean TESLA;

  @Getter private static final Set<ModuleInformation> allModules = detectAllModules();
  @Getter private static final Set<ModuleInformation> activeModules = new TreeSet<>();

  @Module(
    modid = SPONGEAPI_MODID,
    name = "SpongeAPI",
    message = "Hooking into the currency API of Sponge."
  )
  public static boolean spongeAPI() {
    return testMod(SPONGEAPI_MODID, SPONGEAPI);
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
    modid = OPENCOMPUTERS_MODID,
    name = "OpenComputers",
    message = "Adding the PowerReceiver as a OC component.",
    integration = OpenComputersCompat.class
  )
  public static boolean openComputers() {
    return testMod(OPENCOMPUTERS_MODID, OPENCOMPUTERS);
  }

  @Module(modid = TESLA_MODID, name = "TESLA", message = "Enabling support for TESLA energy API.")
  public static boolean tesla() {
    return testMod(TESLA_MODID, TESLA);
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
