package dev.aura.powermoney.client.gui.config;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.config.PowerMoneyConfigWrapper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class PowerMoneyConfigGUI extends GuiConfig {
  public PowerMoneyConfigGUI(GuiScreen parent) {
    super(parent, getConfigElements(), PowerMoney.ID, false, false, PowerMoney.ID + ".cfg");
  }

  /** Compiles a list of config elements */
  private static List<IConfigElement> getConfigElements() {
    List<IConfigElement> list = new ArrayList<>();

    // Add categories to config GUI
    list.add(
        categoryElement(
            PowerMoneyConfigWrapper.getCalculationCategory(),
            "Calculation",
            "gui.powermoney.config.cat.calculation"));
    list.add(
        categoryElement(
            PowerMoneyConfigWrapper.getPaymentCategory(),
            "Payment",
            "gui.powermoney.config.cat.payment"));

    return list;
  }

  /** Creates a button linking to another screen where all options of the category are available */
  private static IConfigElement categoryElement(
      List<IConfigElement> elements, String name, String tooltip_key) {
    return new DummyConfigElement.DummyCategoryElement(name, tooltip_key, elements);
  }
}
