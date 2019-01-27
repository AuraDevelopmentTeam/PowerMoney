package dev.aura.powermoney.common.advancement.criterion;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import dev.aura.powermoney.PowerMoney;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.server.FMLServerHandler;

public class CriterionEasterEgg implements ICriterionTrigger<CriterionEasterEgg.Instance> {
  @Getter private final ResourceLocation id;
  private final Map<PlayerAdvancements, CriterionEasterEgg.Listeners> listeners = Maps.newHashMap();

  public CriterionEasterEgg() {
    id = new ResourceLocation(PowerMoney.RESOURCE_PACKAGE, "easter_egg");
  }

  @Override
  public void addListener(
      PlayerAdvancements playerAdvancementsIn,
      ICriterionTrigger.Listener<CriterionEasterEgg.Instance> listener) {
    CriterionEasterEgg.Listeners easterEgg$listeners = listeners.get(playerAdvancementsIn);

    if (easterEgg$listeners == null) {
      easterEgg$listeners = new CriterionEasterEgg.Listeners(playerAdvancementsIn);
      listeners.put(playerAdvancementsIn, easterEgg$listeners);
    }

    easterEgg$listeners.add(listener);
  }

  @Override
  public void removeListener(
      PlayerAdvancements playerAdvancementsIn,
      ICriterionTrigger.Listener<CriterionEasterEgg.Instance> listener) {
    CriterionEasterEgg.Listeners easterEgg$listeners = listeners.get(playerAdvancementsIn);

    if (easterEgg$listeners != null) {
      easterEgg$listeners.remove(listener);

      if (easterEgg$listeners.isEmpty()) {
        listeners.remove(playerAdvancementsIn);
      }
    }
  }

  @Override
  public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
    listeners.remove(playerAdvancementsIn);
  }

  @Override
  public CriterionEasterEgg.Instance deserializeInstance(
      JsonObject json, JsonDeserializationContext context) {
    return new CriterionEasterEgg.Instance(getId());
  }

  public void trigger(EntityPlayerMP parPlayer) {
    final MinecraftServer server = FMLServerHandler.instance().getServer();

    if (!server.isCallingFromMinecraftThread()) {
      server.addScheduledTask(() -> trigger(parPlayer));

      return;
    }

    CriterionEasterEgg.Listeners easterEgg$listeners = listeners.get(parPlayer.getAdvancements());

    if (easterEgg$listeners != null) {
      easterEgg$listeners.trigger(parPlayer);
    }
  }

  public static class Instance extends AbstractCriterionInstance {
    public Instance(ResourceLocation parID) {
      super(parID);
    }

    public boolean test() {
      return true;
    }
  }

  @RequiredArgsConstructor
  static class Listeners {
    private final PlayerAdvancements playerAdvancements;
    private final Collection<ICriterionTrigger.Listener<CriterionEasterEgg.Instance>> listeners =
        Sets.newHashSet();

    public boolean isEmpty() {
      return listeners.isEmpty();
    }

    public void add(ICriterionTrigger.Listener<CriterionEasterEgg.Instance> listener) {
      listeners.add(listener);
    }

    public void remove(ICriterionTrigger.Listener<CriterionEasterEgg.Instance> listener) {
      listeners.remove(listener);
    }

    public void trigger(EntityPlayerMP player) {
      List<ICriterionTrigger.Listener<CriterionEasterEgg.Instance>> toGrant = new LinkedList<>();

      for (ICriterionTrigger.Listener<CriterionEasterEgg.Instance> listener : listeners) {
        if (listener.getCriterionInstance().test()) {
          toGrant.add(listener);
        }
      }

      for (ICriterionTrigger.Listener<CriterionEasterEgg.Instance> listener : toGrant) {
        listener.grantCriterion(playerAdvancements);
      }
    }
  }
}
