package dev.aura.powermoney.client.gui;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.client.gui.helper.ReceiverData;
import dev.aura.powermoney.common.container.ContainerGeneric;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import dev.aura.powermoney.network.PacketDispatcher;
import dev.aura.powermoney.network.packet.serverbound.PacketChangeRequiresReceiverData;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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

  private static ReceiverData receiverData;

  private final UUID player;
  private final TileEntityPowerReceiver tileEntity;

  public static void receiverDisabled() {
    receiverData = ReceiverData.receiverDisabled();
  }

  public static void setReceiverData(
      BigInteger energy, BigDecimal money, String moneySymbol, int defaultDigits) {
    receiverData = ReceiverData.setReceiverData(energy, money, moneySymbol, defaultDigits);
  }

  public GuiPowerReceiver(EntityPlayer player, TileEntityPowerReceiver tileEntity) {
    super(new ContainerGeneric());

    this.player = player.getUniqueID();
    this.tileEntity = tileEntity;

    xSize = 123;
    ySize = 123;
  }

  @SuppressFBWarnings(
    value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
    justification = "Only one object exists at any one time."
  )
  @Override
  public void initGui() {
    super.initGui();

    receiverData = ReceiverData.waiting();

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
    final ReceiverData receiverDataLocal = receiverData;

    String tileName = tileEntity.getDisplayName().getUnformattedText();
    String ownerName = I18n.format("gui.powermoney.owner") + ": " + tileEntity.getOwnerName();

    if (!player.equals(tileEntity.getOwner())) {
      ownerName += " (" + I18n.format("gui.powermoney.owner.notyou") + ')';
    }

    fontRenderer.drawString(
        tileName, (xSize - fontRenderer.getStringWidth(tileName)) / 2, 8, TEXT_COLOR);
    fontRenderer.drawString(
        ownerName, (xSize - fontRenderer.getStringWidth(ownerName)) / 2, 16, TEXT_COLOR);

    if (receiverDataLocal.isWaiting()) {
      fontRenderer.drawString("Waiting...", 4, 40, TEXT_COLOR);
    } else if (!receiverDataLocal.isEnabled()) {
      fontRenderer.drawString("Receiving is disabled server wide.", 4, 40, TEXT_COLOR);
    } else {
      final String totalEnergy = I18n.format("gui.powermoney.totalenergy") + ':';
      final String totalEarning = I18n.format("gui.powermoney.totalearning") + ':';

      fontRenderer.drawString(
          totalEnergy, 50 - fontRenderer.getStringWidth(totalEnergy), 32, TEXT_COLOR);
      fontRenderer.drawString(receiverDataLocal.getEnergyFormatted(), 55, 32, TEXT_COLOR);
      fontRenderer.drawString(
          totalEarning, 50 - fontRenderer.getStringWidth(totalEarning), 40, TEXT_COLOR);
      fontRenderer.drawString(receiverDataLocal.getMoneyFormatted(), 55, 40, TEXT_COLOR);
    }
  }
}
