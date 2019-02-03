package dev.aura.powermoney.common.compat.theoneprobe;

import dev.aura.powermoney.PowerMoneyBlocks;
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
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PowerReceiverDataProvider implements IProbeConfigProvider, IProbeInfoProvider {
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
      IProbeHitData data) {
    if (!isPowerReceiver(blockState)) {
      return;
    }

    if ((mode == ProbeMode.EXTENDED) || (mode == ProbeMode.DEBUG)) {
      probeInfo.progress(10, 20, getEnergyProgressStyle(probeInfo));
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
        .suffix(I18n.format("gui.powermoney.energyunit") + I18n.format("gui.powermoney.persecond"))
        .filledColor(Config.rfbarFilledColor)
        .alternateFilledColor(Config.rfbarAlternateFilledColor)
        .borderColor(Config.rfbarBorderColor)
        .numberFormat(Config.rfFormat);
  }
}
