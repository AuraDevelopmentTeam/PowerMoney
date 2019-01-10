package dev.aura.powermoney.common.compat;

import lombok.experimental.UtilityClass;
import net.minecraftforge.fml.common.Loader;

@UtilityClass
public class PowerMoneyCompats {
  public static final String SPONGEAPI_MODID = "spongeapi";
  public static final String TESLA_MODID = "tesla";

  public static final String DEPENDENCIES = "after:" + SPONGEAPI_MODID + ";after:" + TESLA_MODID;

  private static Boolean SPONGEAPI;
  private static Boolean TESLA;

  public static boolean spongeAPI() {
    return testMod(SPONGEAPI_MODID, SPONGEAPI);
  }

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
}
