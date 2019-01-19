package dev.aura.powermoney.common.advancement;

import dev.aura.powermoney.common.advancement.criterion.CriterionEasterEgg;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.UtilityClass;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;

@SuppressFBWarnings(
  value = "MS_CANNOT_BE_FINAL",
  justification = "The criterions need to be initalized late"
)
@UtilityClass
public class CriterionRegistry {
  public static CriterionEasterEgg EASTER_EGG;

  public static void init() {
    EASTER_EGG = register(new CriterionEasterEgg());
  }

  @SuppressWarnings("rawtypes")
  public <T extends ICriterionTrigger> T register(T criterion) {
    return CriteriaTriggers.register(criterion);
  }
}
