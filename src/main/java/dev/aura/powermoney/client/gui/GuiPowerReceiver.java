package dev.aura.powermoney.client.gui;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.container.ContainerGeneric;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import dev.aura.powermoney.network.PacketDispatcher;
import dev.aura.powermoney.network.packet.serverbound.PacketChangeRequiresReceiverData;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiPowerReceiver extends GuiContainer {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(PowerMoney.RESOURCE_PACKAGE, "textures/gui/power_receiver.png");
  private static final int TEXT_COLOR = 0x404040;

  private static boolean waiting;
  private static boolean enabled;
  private static BigInteger energyPerSecond;
  private static BigDecimal moneyPerSecond;

  private final UUID player;
  private final TileEntityPowerReceiver tileEntity;

  public static void receiverDisabled() {
    waiting = false;
    enabled = false;
  }

  public static void setReceiverData(BigInteger energy, BigDecimal money) {
    waiting = false;
    enabled = true;
    energyPerSecond = energy;
    moneyPerSecond = money;
  }

  public GuiPowerReceiver(EntityPlayer player, TileEntityPowerReceiver tileEntity) {
    super(new ContainerGeneric());

    this.player = player.getUniqueID();
    this.tileEntity = tileEntity;

    xSize = 123;
    ySize = 123;
  }

  @Override
  public void initGui() {
    super.initGui();

    waiting = true;

    PacketDispatcher.sendToServer(
        PacketChangeRequiresReceiverData.startData(tileEntity.getOwner()));
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();

    PacketDispatcher.sendToServer(PacketChangeRequiresReceiverData.stopData());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    mc.getTextureManager().bindTexture(TEXTURE);
    drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    String tileName = tileEntity.getDisplayName().getUnformattedText();
    String ownerName = I18n.format("gui.powermoney.owner") + ": " + tileEntity.getOwnerName();

    if (!player.equals(tileEntity.getOwner())) {
      ownerName += " (" + I18n.format("gui.powermoney.owner.notyou") + ')';
    }

    fontRenderer.drawString(
        tileName, (xSize - fontRenderer.getStringWidth(tileName)) / 2, 8, TEXT_COLOR);
    fontRenderer.drawString(
        ownerName, (xSize - fontRenderer.getStringWidth(ownerName)) / 2, 16, TEXT_COLOR);

    if (waiting) {
      fontRenderer.drawString("Waiting...", 4, 40, TEXT_COLOR);
    } else if (!enabled) {
      fontRenderer.drawString("Receiving is disabled server wide.", 4, 40, TEXT_COLOR);
    } else {
      final String totalEnergy = I18n.format("gui.powermoney.totalenergy") + ':';
      final String totalEarning = I18n.format("gui.powermoney.totalearning") + ':';

      final String energy =
          energyPerSecond.toString()
              + ' '
              + I18n.format("gui.powermoney.energyunit")
              + I18n.format("gui.powermoney.persecond");
      final String money =
          moneyPerSecond.toString() + " $" + I18n.format("gui.powermoney.persecond");

      fontRenderer.drawString(
          totalEnergy, 50 - fontRenderer.getStringWidth(totalEnergy), 32, TEXT_COLOR);
      fontRenderer.drawString(energy, 55, 32, TEXT_COLOR);
      fontRenderer.drawString(
          totalEarning, 50 - fontRenderer.getStringWidth(totalEarning), 40, TEXT_COLOR);
      fontRenderer.drawString(money, 55, 40, TEXT_COLOR);
    }
  }
}
