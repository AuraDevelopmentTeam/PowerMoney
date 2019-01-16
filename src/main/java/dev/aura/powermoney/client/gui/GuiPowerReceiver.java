package dev.aura.powermoney.client.gui;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.client.gui.helper.ReceiverData;
import dev.aura.powermoney.common.container.ContainerGeneric;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import dev.aura.powermoney.network.PacketDispatcher;
import dev.aura.powermoney.network.packet.serverbound.PacketChangeRequiresReceiverData;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.math.BigDecimal;
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
  private static final int INVERSE_TEXT_COLOR = TEXT_COLOR ^ 0xFFFFFF;
  private static final int COLUMN_HEIGHT = 34;

  private static ReceiverData receiverData;

  private final UUID player;
  private final TileEntityPowerReceiver tileEntity;

  public static void receiverDisabled() {
    receiverData = ReceiverData.receiverDisabled();
  }

  public static void setReceiverData(
      long localEnergy, long totalEnergy, BigDecimal money, String moneySymbol, int defaultDigits) {
    receiverData =
        ReceiverData.setReceiverData(localEnergy, totalEnergy, money, moneySymbol, defaultDigits);
  }

  public GuiPowerReceiver(EntityPlayer player, TileEntityPowerReceiver tileEntity) {
    super(new ContainerGeneric());

    this.player = player.getUniqueID();
    this.tileEntity = tileEntity;

    xSize = 176;
    ySize = 158;
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
        PacketChangeRequiresReceiverData.startData(tileEntity.getOwner(), tileEntity.getPos()));
  }

  @Override
  public void onGuiClosed() {
    super.onGuiClosed();

    PacketDispatcher.sendToServer(PacketChangeRequiresReceiverData.stopData());
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    final ReceiverData receiverDataLocal = receiverData;

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
    final ReceiverData receiverDataLocal = receiverData;

    // Headings
    final String tileName = tileEntity.getDisplayName().getUnformattedText();
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
  }
}
