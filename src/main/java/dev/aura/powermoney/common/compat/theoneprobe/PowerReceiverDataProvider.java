package dev.aura.powermoney.common.compat.theoneprobe;

import dev.aura.powermoney.PowerMoneyBlocks;
import dev.aura.powermoney.common.handler.PowerMoneyTickHandler;
import dev.aura.powermoney.common.helper.ReceiverData;
import dev.aura.powermoney.common.helper.WorldBlockPos;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import mcjty.theoneprobe.api.ILayoutStyle;
import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeConfigProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.IProgressStyle;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class PowerReceiverDataProvider implements IProbeConfigProvider, IProbeInfoProvider {
  private static final String YELLOW = TextFormatting.YELLOW.toString();
  private static final String RED = TextFormatting.RED.toString();

  private final Block powerReceiver = PowerMoneyBlocks.powerReceiver();

  @Override
  public String getID() {
    return powerReceiver.getRegistryName().toString();
  }

  @Override
  public void addProbeInfo(
      ProbeMode mode,
      IProbeInfo probeInfo,
      EntityPlayer player,
      World world,
      IBlockState blockState,
      IProbeHitData hitData) {
    final BlockPos blockPos = hitData.getPos();
    final TileEntity tempTileEntity = world.getTileEntity(blockPos);

    if (!isPowerReceiver(blockState) || !(tempTileEntity instanceof TileEntityPowerReceiver)) {
      return;
    }

    final TileEntityPowerReceiver tileEntity = (TileEntityPowerReceiver) tempTileEntity;
    final WorldBlockPos worldPos = new WorldBlockPos(world, blockPos);
    final ReceiverData receiverData =
        PowerMoneyTickHandler.getReceiverData(tileEntity.getOwner(), worldPos);
    final IProbeInfo renderProbeInfo;

    if (mode == ProbeMode.DEBUG) {
      renderProbeInfo = probeInfo.vertical(getDebugVerticalLayoutStlye(probeInfo));
    } else {
      renderProbeInfo = probeInfo;
    }

    if ((!receiverData.isWaiting() && receiverData.isEnabled())
        && ((mode == ProbeMode.EXTENDED) || (mode == ProbeMode.DEBUG))) {
      renderProbeInfo.progress(
          receiverData.getLocalEnergyPerSecond(),
          receiverData.getTotalEnergyPerSecond(),
          getEnergyProgressStyle(probeInfo));
    }

    final String[] headings = receiverData.getHeadings();
    final String[] data = new String[4];

    data[0] = receiverData.formatOwnerName(tileEntity, player);

    if (receiverData.isWaiting() || !receiverData.isEnabled()) {
      data[1] = data[2] = data[3] = receiverData.getMessage();
    } else {
      data[1] = receiverData.getLocalEnergyFormatted();
      data[2] = receiverData.getTotalEnergyFormatted();
      data[3] = receiverData.getMoneyFormatted();
    }

    final ILayoutStyle verticalLayoutStlye = getVerticalLayoutStlye(probeInfo);
    final IProbeInfo probeInfoHorizontal = renderProbeInfo.horizontal();
    IProbeInfo probeInfoVertical1 = probeInfoHorizontal.vertical(verticalLayoutStlye);
    IProbeInfo probeInfoVertical2 = probeInfoHorizontal.vertical(verticalLayoutStlye);

    for (int i = 0; i < (receiverData.isEnabled() ? headings.length : 1); ++i) {
      probeInfoVertical1.text(YELLOW + headings[i] + ": ");
      probeInfoVertical2.text(data[i]);
    }

    if (!receiverData.isEnabled()) {
      renderProbeInfo.text(RED + data[1]);
    }
  }

  @Override
  public void getProbeConfig(
      IProbeConfig config,
      EntityPlayer player,
      World world,
      Entity entity,
      IProbeHitEntityData data) {
    // Do nothing
  }

  @Override
  public void getProbeConfig(
      IProbeConfig config,
      EntityPlayer player,
      World world,
      IBlockState blockState,
      IProbeHitData data) {
    if ((config == null) || !isPowerReceiver(blockState)) {
      return;
    }

    config.setRFMode(0);
  }

  private boolean isPowerReceiver(IBlockState blockState) {
    return (blockState != null) && isPowerReceiver(blockState.getBlock());
  }

  private boolean isPowerReceiver(Block block) {
    return (block != null) && (block == powerReceiver);
  }

  private IProgressStyle getEnergyProgressStyle(IProbeInfo probeInfo) {
    return probeInfo
        .defaultProgressStyle()
        .suffix(
            ReceiverData.Translator.translateKeyForTOP("gui.powermoney.energyunit")
                + ReceiverData.Translator.translateKeyForTOP("gui.powermoney.persecond"))
        .suffix("FE/s") // Until the above works
        .filledColor(Config.rfbarFilledColor)
        .alternateFilledColor(Config.rfbarAlternateFilledColor)
        .borderColor(Config.rfbarBorderColor)
        .numberFormat(Config.rfFormat);
  }

  private ILayoutStyle getDebugVerticalLayoutStlye(IProbeInfo probeInfo) {
    return probeInfo.defaultLayoutStyle().borderColor(0xffff4444).spacing(2);
  }

  private ILayoutStyle getVerticalLayoutStlye(IProbeInfo probeInfo) {
    return probeInfo.defaultLayoutStyle().spacing(-1);
  }
}
