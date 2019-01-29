package dev.aura.powermoney.client.gui;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.PowerMoneySounds;
import dev.aura.powermoney.client.helper.ReceiverData;
import dev.aura.powermoney.common.container.ContainerGeneric;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import dev.aura.powermoney.network.PacketDispatcher;
import dev.aura.powermoney.network.packet.serverbound.PacketEasterEgg;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class GuiPowerReceiver extends GuiContainer {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(PowerMoney.RESOURCE_PACKAGE, "textures/gui/power_receiver.png");
  private static final int TEXT_COLOR = 0x404040;
  private static final int INVERSE_TEXT_COLOR = TEXT_COLOR ^ 0xFFFFFF;
  private static final int COLUMN_HEIGHT = 34;

  private final UUID player;
  private final TileEntityPowerReceiver tileEntity;

  // Easter Egg variables
  private static final ResourceLocation NYAN_TEXTURE =
      new ResourceLocation(
          PowerMoney.RESOURCE_PACKAGE, "textures/gui/power_receiver_easter_egg.png");
  private static final int catScale = 6;
  private static final int starEachPixels = 1000 * (catScale * catScale);
  private static final long millisPerCatFrame = 100L;
  protected final SoundHandler soundHandler;
  private final char[] lastChars;
  private SoundLoop soundLoop;
  private boolean nyanCat;
  private boolean renderNyanCat;
  private long lastAnimationProgress;
  private int numberStars;
  private StarAnimation[] stars;
  private final Random nyanrand = new Random();

  public GuiPowerReceiver(EntityPlayer player, TileEntityPowerReceiver tileEntity) {
    super(new ContainerGeneric());

    this.player = player.getUniqueID();
    this.tileEntity = tileEntity;

    xSize = 176;
    ySize = 158;

    soundHandler = Minecraft.getMinecraft().getSoundHandler();
    lastChars = new char[4];
    soundLoop = null;
    nyanCat = false;
  }

  @SuppressFBWarnings(
    value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
    justification = "Only one object exists at any one time."
  )
  @Override
  public void initGui() {
    super.initGui();

    ReceiverData.waiting(tileEntity);
  }

  @Override
  public void onGuiClosed() {
    ReceiverData.stopSending();

    super.onGuiClosed();
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    final ReceiverData receiverDataLocal = ReceiverData.getInstance();

    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    mc.getTextureManager().bindTexture(TEXTURE);

    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

    if (receiverDataLocal.isWaiting() || !receiverDataLocal.isEnabled()) {
      for (int i = 0; i < 4; ++i) {
        drawTexturedModalRect(guiLeft + 7, guiTop + 30 + (i * COLUMN_HEIGHT), 0, ySize, 162, 19);
      }
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    final ReceiverData receiverDataLocal = ReceiverData.getInstance();

    // Headings
    final String tileName = tileEntity.getDisplayName().getFormattedText();
    final String headings[] =
        new String[] {
          I18n.format("gui.powermoney.owner"),
          I18n.format("gui.powermoney.localenergy"),
          I18n.format("gui.powermoney.totalenergy"),
          I18n.format("gui.powermoney.totalearning")
        };

    // Data
    final String data[] = new String[4];

    String ownerName = tileEntity.getOwnerName();

    if (!player.equals(tileEntity.getOwner())) {
      ownerName += " (" + I18n.format("gui.powermoney.owner.notyou") + ')';
    }

    data[0] = ownerName;

    if (receiverDataLocal.isWaiting() || !receiverDataLocal.isEnabled()) {
      final String message =
          receiverDataLocal.isWaiting()
              ? I18n.format("gui.powermoney.waiting")
              : I18n.format("gui.powermoney.disabled");

      data[1] = data[2] = data[3] = message;
    } else {
      data[1] = receiverDataLocal.getLocalEnergyFormatted();
      data[2] = receiverDataLocal.getTotalEnergyFormatted();
      data[3] = receiverDataLocal.getMoneyFormatted();
    }

    fontRenderer.drawString(
        tileName, (xSize - fontRenderer.getStringWidth(tileName)) / 2, 7, TEXT_COLOR);

    for (int i = 0; i < 4; ++i) {
      int j = (i * COLUMN_HEIGHT);

      fontRenderer.drawString(headings[i] + ':', 7, 20 + j, TEXT_COLOR);
      fontRenderer.drawString(data[i], 11, 36 + j, INVERSE_TEXT_COLOR);
    }

    if (renderNyanCat) {
      drawNyanCat();
    }
  }

  @Override
  protected void keyTyped(char letter, int key) throws IOException {
    if (nyanCat) {
      stopEasterEgg();

      // Just stop the easter egg. Do nothing else.
      return;
    }

    // Closes the GUI when ESC or Inventory key
    super.keyTyped(letter, key);

    // Shift array by one
    System.arraycopy(lastChars, 0, lastChars, 1, lastChars.length - 1);
    lastChars[0] = letter;

    if (((lastChars[2] == 'l') && (lastChars[1] == 'o') && (lastChars[0] == 'l'))
        || ((lastChars[3] == 'a')
            && (lastChars[2] == 's')
            && (lastChars[1] == 'd')
            && (lastChars[0] == 'f'))) {
      startEasterEgg();
    }
  }

  private void startEasterEgg() {
    if (!nyanCat) {
      soundHandler.pauseSounds();

      nyanCat = true;

      soundLoop = new SoundLoop();
      soundLoop.start();

      numberStars = (width * height) / starEachPixels;
      renderNyanCat = false;
    }
  }

  private void stopEasterEgg() {
    if (nyanCat) {
      nyanCat = false;

      soundLoop.stopSound();
      soundLoop = null;

      stars = null;
      renderNyanCat = false;

      (new Thread() {
            @Override
            public void run() {
              try {
                sleep(20);
              } catch (final InterruptedException e) {
                PowerMoney.getLogger().warn(e);
              }

              soundHandler.resumeSounds();
            }
          })
          .start();

      PacketDispatcher.sendToServer(new PacketEasterEgg());
    }
  }

  // TODO: rewrite this so that new stars get added randomly not when an old
  // one disappears
  private void drawNyanCat() {
    // Transforming the rendering space
    zLevel = 150F;
    mc.getTextureManager().bindTexture(NYAN_TEXTURE);

    GlStateManager.pushMatrix();
    GlStateManager.translate(xSize / 2.0, ySize / 2.0, 0.0);
    GlStateManager.scale(catScale, catScale, catScale);

    // Checking for screen size change
    if (numberStars != ((width * height) / starEachPixels)) {
      numberStars = (width * height) / starEachPixels;
      stars = null;
    }

    // Refilling the stars array
    if (stars == null) {
      StarAnimation star;

      stars = new StarAnimation[numberStars];

      for (int i = 0; i < numberStars; i++) {
        star = new StarAnimation();
        star.ranomizeX();

        stars[i] = star;
      }
    }

    // Render the rainbow
    drawTexturedModalRect(-23, -9, 41, ((int) ((lastAnimationProgress / 2) % 2L)) * 19, 16, 18);
    drawTexturedModalRect(-23, -9, 41, ((int) ((lastAnimationProgress / 2) % 2L)) * 19, 14, 19);

    for (int i = 1; i < (((width / (catScale * 2)) / 16) + 1); i++) {
      drawTexturedModalRect(
          (i * -16) - 23, -9, 41, ((int) ((lastAnimationProgress / 2) % 2L)) * 19, 16, 19);
    }

    // Render the cat
    drawTexturedModalRect(-17, -10, 0, ((int) (lastAnimationProgress % 12L)) * 21, 34, 21);

    for (final StarAnimation star : stars) {
      star.render();
    }

    // Progress the animation
    if ((System.currentTimeMillis() / millisPerCatFrame) > lastAnimationProgress) {
      lastAnimationProgress = System.currentTimeMillis() / millisPerCatFrame;

      for (int i = 0; i < stars.length; i++) {
        if (!stars[i].shift()) {
          stars[i] = new StarAnimation();
        }
      }
    }

    // Reset the render space
    GlStateManager.popMatrix();
    zLevel = 0F;
  }

  private ISound playSoundAtClient(SoundEvent event) {
    return playSoundAtClient(event, 1.0f, 1.0f);
  }

  private ISound playSoundAtClient(SoundEvent event, float volume, float pitch) {
    return playSoundAtClient(event, SoundCategory.MASTER, volume, pitch);
  }

  private ISound playSoundAtClient(
      SoundEvent event, SoundCategory category, float volume, float pitch) {
    BlockPos pos = mc.player.getPosition();

    PositionedSoundRecord sound =
        new PositionedSoundRecord(
            event, category, volume, pitch, pos.getX(), pos.getY(), pos.getZ());
    soundHandler.playSound(sound);

    return sound;
  }

  private ISound stopSoundAtClient(ISound sound) {
    soundHandler.stopSound(sound);

    return sound;
  }

  private class SoundLoop extends Thread {
    private boolean run;
    private ISound currentSound;

    public SoundLoop() {
      run = true;

      setPriority(MAX_PRIORITY);
    }

    @Override
    public void run() {
      try {
        currentSound = playSoundAtClient(PowerMoneySounds.nyan_intro);
        sleep(4037);

        if (run) {
          renderNyanCat = true;
          lastAnimationProgress = System.currentTimeMillis() / millisPerCatFrame;
        }

        while (run) {
          currentSound = playSoundAtClient(PowerMoneySounds.nyan_loop);
          sleep(27066);
        }
      } catch (final InterruptedException e) {
        PowerMoney.getLogger().warn(e);
      }
    }

    public void stopSound() {
      run = false;

      if (currentSound != null) {
        stopSoundAtClient(currentSound);
      }
    }
  }

  private class StarAnimation {
    private int x;
    private final int y;
    private int frame;

    public StarAnimation() {
      x = width / (catScale * 2);
      y = nyanrand.nextInt(height / catScale) - (height / (catScale * 2));

      frame = nyanrand.nextInt(6);
    }

    public void ranomizeX() {
      x = nyanrand.nextInt(width / catScale) - (width / (catScale * 2));
    }

    public void render() {
      drawTexturedModalRect(x - 3, y - 3, 34, frame * 7, 7, 7);
    }

    public boolean shift() {
      frame = (frame + 1) % 6;
      x -= 10;

      return x > (width / (catScale * -2));
    }
  }
}
