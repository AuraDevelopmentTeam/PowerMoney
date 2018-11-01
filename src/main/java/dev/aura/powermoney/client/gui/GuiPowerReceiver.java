package dev.aura.powermoney.client.gui;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.container.ContainerGeneric;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import dev.aura.powermoney.network.PacketDispatcher;
import dev.aura.powermoney.network.packet.serverbound.PacketChangeRequiresReceiverData;
import java.util.UUID;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiPowerReceiver extends GuiContainer {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation(PowerMoney.RESOURCE_PACKAGE, "textures/gui/power_receiver.png");

  private final UUID player;
  private final TileEntityPowerReceiver tileEntity;

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
    String ownerName = tileEntity.getOwnerName();

    if (!player.equals(tileEntity.getOwner())) {
      ownerName += " (" + I18n.format("gui.powermoney.owner.notyou") + ')';
    }

    fontRenderer.drawString(
        tileName, (xSize - fontRenderer.getStringWidth(tileName)) / 2, 8, 0x404040);
    fontRenderer.drawString(
        ownerName, (xSize - fontRenderer.getStringWidth(ownerName)) / 2, 16, 0x404040);
  }
}
