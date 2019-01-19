package dev.aura.powermoney;

import java.util.LinkedList;
import java.util.List;
import lombok.NoArgsConstructor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@NoArgsConstructor(staticName = "registrar")
public class PowerMoneySounds {
  private static final List<SoundEvent> sounds = new LinkedList<>();

  public static final SoundEvent nyan_intro = sound("nyan.intro");
  public static final SoundEvent nyan_loop = sound("nyan.loop");

  private static SoundEvent sound(String name) {
    ResourceLocation location = new ResourceLocation(PowerMoney.RESOURCE_PACKAGE, name);
    SoundEvent event = new SoundEvent(location).setRegistryName(location);

    sounds.add(event);

    return event;
  }

  @SubscribeEvent
  public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
    sounds.stream().forEach(event.getRegistry()::register);
  }

  public static void playSoundForAll(Entity entity, SoundEvent sound) {
    playSoundForAll(entity, sound, 1.0f, 1.0f);
  }

  public static void playSoundForAll(Entity entity, SoundEvent sound, float volume, float pitch) {
    entity.world.playSound(
        null, entity.getPosition(), sound, entity.getSoundCategory(), volume, pitch);
  }

  public static void playSoundForPlayer(Entity entity, SoundEvent sound) {
    playSoundForPlayer(entity, sound, 1.0f, 1.0f);
  }

  public static void playSoundForPlayer(
      Entity player, SoundEvent sound, float volume, float pitch) {
    if ((player instanceof EntityPlayerMP) && (((EntityPlayerMP) player).connection != null)) {
      ((EntityPlayerMP) player)
          .connection.sendPacket(
              new SPacketSoundEffect(
                  sound,
                  player.getSoundCategory(),
                  player.posX,
                  player.posY,
                  player.posZ,
                  volume,
                  pitch));
    }
  }
}
