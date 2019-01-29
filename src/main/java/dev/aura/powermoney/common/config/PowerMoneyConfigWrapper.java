package dev.aura.powermoney.common.config;

import dev.aura.powermoney.common.payment.MoneyCalculator;
import dev.aura.powermoney.common.payment.MoneyCalculatorLog;
import dev.aura.powermoney.common.payment.MoneyCalculatorRoot;
import java.util.List;
import lombok.Getter;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PowerMoneyConfigWrapper {
  public static final String CAT_CALCULATION = "calculation";
  public static final String CAT_MISC = "misc";
  public static final String CAT_PAYMENT = "payment";

  private static Configuration configStorage;

  @Getter private static double logBase;
  @Getter private static double logBaseMultiplier;
  @Getter private static double logShift;
  
  @Getter private static double rootBase;
  @Getter private static double rootBaseMultiplier;
  @Getter private static double rootShift;
  
  @Getter private static int calcType;
  @Getter private static MoneyCalculator moneyCalculator;

  @Getter private static String currency;
  @Getter private static int payoutInterval;

  @Getter private static boolean simulate;

  public static void loadConfig() {
    loadCalculationSettings();
    loadPaymentSettings();
    loadMiscSettings();

    saveIfChanged();
  }

  public static void loadConfig(Configuration config) {
    configStorage = config;
    configStorage.load();

    loadConfig();
  }

  private static void loadCalculationSettings() {
    calcType =
        getInt(
            CAT_CALCULATION,
            "CalcType",
            0,
			0,
			1,
            "Choose the type of calculation."
                + "0 - logarithm [Shift + BaseMultiplier * (log_CalcBase(EnergyPerSecond) + 1)]"
                + "1 - root [Shift + BaseMultiplier * root_CalcBase(EnergyPerSecond)");

    logBase =
        getDouble(
            CAT_CALCULATION,
            "LogBase",
            2,
            Math.nextUp(1.0),
            1E6,
			"LOGARITHMIC CALCULATION\n"
				+ "#######################\n"
				+ "The logarithmic base in the calculation.\n"
                + "The higher the value the less money the players get.");
    logBaseMultiplier =
        getDouble(
            CAT_CALCULATION,
            "LogBaseMultiplier",
            0.10,
            1E-6,
            1E6,
            "The base multiplier in the log calculation.\n"
                + "Essentially how much 1 unit of energy per second is worth.");
    logShift =
        getDouble(
            CAT_CALCULATION,
            "LogShift",
            0,
            -1E10,
            1E10,
            "The value that will be added each time to the final log calculation result."
				+ "Helps to adjust the energy price.");

    rootBase =
        getDouble(
            CAT_CALCULATION,
            "RootBase",
            2,
            Math.nextUp(1.0),
            1E6,
			"ROOT CALCULATION\n"
				+ "################\n"
				+ "The root base in the calculation.\n"
                + "The higher the value the less money the players get.");
    rootBaseMultiplier =
        getDouble(
            CAT_CALCULATION,
            "RootBaseMultiplier",
            0.10,
            1E-6,
            1E6,
            "The base multiplier in the root calculation.\n"
                + "Essentially how much 1 unit of energy per second is worth.");
    rootShift =
        getDouble(
            CAT_CALCULATION,
            "RootShift",
            0,
            -1E10,
            1E10,
            "The value that will be added each time to the final root calculation result."
				+ "Helps to adjust the energy price.");

	switch (calcType) {
           case  (0):
               moneyCalculator = new MoneyCalculatorLog(logBase, logBaseMultiplier, logShift);
               break;
           case (1):
               moneyCalculator = new MoneyCalculatorRoot(rootBase, rootBaseMultiplier, rootShift);
               break;
           default:
               throw new IllegalArgumentException("Unknown calculation type.");;
               break;
    }

    addCustomCategoryComment(
        CAT_CALCULATION,
        "Here you can tweak the calculations that converts energy into money.\n"
            + "\n"
            + "You can choose one of two formulas for calculating the energy price.\n"
            + "\n"
            + "Logarithmic formula:\n"
            + "    MoneyPerSecond = LogShift + LogBaseMultiplier * (log_LogBase(EnergyPerSecond) + 1)\n"
            + "\n"
            + "Root formula:\n"
            + "    MoneyPerSecond = RootShift + RootBaseMultiplier * root_RootBase(EnergyPerSecond)");
  }

  private static void loadPaymentSettings() {
    currency =
        getString(
            CAT_PAYMENT,
            "Currency",
            "",
            "The currency to make the payments.\n"
                + "If the currency specified here doesn't exist or is empty, then the system will fallback to the\n"
                + "default currency.\n"
                + "Only really needs to be set if there's more than one currency.");
    payoutInterval =
        getInt(
            CAT_PAYMENT,
            "PayoutInterval",
            15,
            1,
            1000,
            "The interval in seconds between payouts.\n"
                + "The value 1 means instant payouts (the money the player gets is calculated on a per second base).");

    addCustomCategoryComment(CAT_PAYMENT, "Settings regarding the payment to the players.");
  }

  private static void loadMiscSettings() {
    simulate =
        getBoolean(
            CAT_MISC,
            "Simulate",
            false,
            "If Sponge or an Economy plugin is missing, the blocks will not consume energy. Enabling this will\n"
                + "make them consume energy. But it will not produce any money. This is useful for testing and\n"
                + "shouldn't be enabled on a production server.\n"
                + "If Sponge and an Economy plugin is installed, this setting has no effect!");

    addCustomCategoryComment(CAT_MISC, "Settings that don't belong anywhere else.");
  }

  private static String getDefaultLangKey(String category, String name) {
    return "gui.powermoney.config.cat." + category.toLowerCase() + '.' + name.toLowerCase();
  }

  /**
   * Creates a boolean property.
   *
   * @param category Category of the property.
   * @param name Name of the property.
   * @param defaultValue Default value of the property.
   * @param comment A brief description what the property does.
   * @return The value of the new boolean property.
   */
  private static boolean getBoolean(
      String category, String name, boolean defaultValue, String comment) {
    Property prop = configStorage.get(category, name, defaultValue);
    prop.setLanguageKey(getDefaultLangKey(category, name));
    prop.setComment(comment + "\n[default: " + defaultValue + "]");

    return prop.getBoolean(defaultValue);
  }

  /**
   * Creates an integer property.
   *
   * @param category Category of the property.
   * @param name Name of the property.
   * @param defaultValue Default value of the property.
   * @param minValue Minimum value of the property.
   * @param maxValue Maximum value of the property.
   * @param comment A brief description what the property does.
   * @return The value of the new integer property.
   */
  private static int getInt(
      String category, String name, int defaultValue, int minValue, int maxValue, String comment) {
    Property prop = configStorage.get(category, name, defaultValue);
    prop.setLanguageKey(getDefaultLangKey(category, name));
    prop.setComment(
        comment + "\n[range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]");
    prop.setMinValue(minValue);
    prop.setMaxValue(maxValue);

    int readValue = prop.getInt(defaultValue);
    int cappedValue = Math.max(Math.min(readValue, maxValue), minValue);

    if (readValue != cappedValue) {
      prop.set(cappedValue);
    }

    return cappedValue;
  }

  /**
   * Creates a double property.
   *
   * @param category Category of the property.
   * @param name Name of the property.
   * @param defaultValue Default value of the property.
   * @param minValue Minimum value of the property.
   * @param maxValue Maximum value of the property.
   * @param comment A brief description what the property does.
   * @return The value of the new double property.
   */
  private static double getDouble(
      String category,
      String name,
      double defaultValue,
      double minValue,
      double maxValue,
      String comment) {
    Property prop = configStorage.get(category, name, defaultValue);
    prop.setLanguageKey(getDefaultLangKey(category, name));
    prop.setComment(
        comment + "\n[range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]");
    prop.setMinValue(minValue);
    prop.setMaxValue(maxValue);

    double readValue = prop.getDouble(defaultValue);
    double cappedValue = Math.max(Math.min(readValue, maxValue), minValue);

    if (readValue != cappedValue) {
      prop.set(cappedValue);
    }

    return cappedValue;
  }

  /**
   * Creates a String property.
   *
   * @param category Category of the property.
   * @param name Name of the property.
   * @param defaultValue Default value of the property.
   * @param comment A brief description what the property does.
   * @return The value of the new String property.
   */
  private static String getString(
      String category, String name, String defaultValue, String comment) {
    Property prop = configStorage.get(category, name, defaultValue);
    prop.setLanguageKey(getDefaultLangKey(category, name));
    prop.setComment(comment + "\n[default: " + defaultValue + "]");
    prop.setDefaultValue(defaultValue);

    return prop.getString();
  }

  /**
   * Adds a comment to the specified ConfigCategory object
   *
   * @param category the config category
   * @param comment a String comment
   */
  private static void addCustomCategoryComment(String category, String comment) {
    configStorage.setCategoryComment(category, comment);
  }

  private static void saveIfChanged() {
    if (configStorage.hasChanged()) {
      configStorage.save();
    }
  }

  @SideOnly(Side.CLIENT)
  public static List<IConfigElement> getCalculationCategory() {
    return new ConfigElement(configStorage.getCategory(CAT_CALCULATION)).getChildElements();
  }

  @SideOnly(Side.CLIENT)
  public static List<IConfigElement> getPaymentCategory() {
    return new ConfigElement(configStorage.getCategory(CAT_PAYMENT)).getChildElements();
  }

  @SideOnly(Side.CLIENT)
  public static List<IConfigElement> getMiscCategory() {
    return new ConfigElement(configStorage.getCategory(CAT_MISC)).getChildElements();
  }
}
