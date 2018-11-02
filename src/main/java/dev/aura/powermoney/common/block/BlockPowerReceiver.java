package dev.aura.powermoney.common.block;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import dev.aura.powermoney.network.PowerMoneyGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPowerReceiver extends Block implements ITileEntityProvider {
  public BlockPowerReceiver() {
    super(Material.IRON);

    setCreativeTab(PowerMoney.creativeTab);
    setHardness(2.5f);
    setHarvestLevel("pickaxe", 2);
    setResistance(20.0f);
    setSoundType(SoundType.METAL);
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityPowerReceiver();
  }

  @Override
  public boolean onBlockActivated(
      World worldIn,
      BlockPos pos,
      IBlockState state,
      EntityPlayer playerIn,
      EnumHand hand,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ) {
    if (!worldIn.isRemote) {
      playerIn.openGui(
          PowerMoney.getInstance(),
          PowerMoneyGuiHandler.ID_POWER_RECEIVER,
          worldIn,
          pos.getX(),
          pos.getY(),
          pos.getZ());
    }

    return true;
  }

  @Override
  public void onBlockPlacedBy(
      World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    final TileEntity tempTileEntity = worldIn.getTileEntity(pos);

    if ((tempTileEntity != null) && (tempTileEntity instanceof TileEntityPowerReceiver)) {
      final TileEntityPowerReceiver tileEntity = ((TileEntityPowerReceiver) tempTileEntity);

      tileEntity.setOwner(placer.getUniqueID(), placer.getName());

      if (stack.hasDisplayName()) {
        tileEntity.setCustomName(stack.getDisplayName());
      }
    }
  }
}
