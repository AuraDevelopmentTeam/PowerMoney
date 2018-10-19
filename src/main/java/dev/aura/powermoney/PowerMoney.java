package dev.aura.powermoney;

import lombok.Getter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;

@Mod(
  modid = PowerMoney.ID,
  name = PowerMoney.NAME,
  version = PowerMoney.VERSION,
  dependencies = PowerMoney.DEPENDENCIES,
  certificateFingerprint = PowerMoney.FINGERPRINT
)
public class PowerMoney {
  public static final String ID = "@id@";
  public static final String NAME = "@name@";
  public static final String VERSION = "@version@";
  public static final String GROUP = "@group@";
  public static final String DESCRIPTION = "@description@";
  public static final String DEPENDENCIES = "after:spongeapi";
  public static final String FINGERPRINT = "2238d4a92d81ab407741a2fdb741cebddfeacba6";

  @Instance(ID)
  @Getter
  private static PowerMoney instance;
}
