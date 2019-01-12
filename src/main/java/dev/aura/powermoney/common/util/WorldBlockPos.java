package dev.aura.powermoney.common.util;

import lombok.Value;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Value
public class WorldBlockPos {
  private final World world;
  private final BlockPos pos;
}
