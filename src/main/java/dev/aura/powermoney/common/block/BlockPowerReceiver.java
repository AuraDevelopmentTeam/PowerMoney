package dev.aura.powermoney.common.block;

import dev.aura.powermoney.PowerMoney;
import dev.aura.powermoney.common.tileentity.TileEntityPowerReceiver;
import dev.aura.powermoney.network.PowerMoneyGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
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
  public static final PropertyBool RECEIVING = PropertyBool.create("receiving");
  public static final int RECEIVING_ON = 0;
  public static final int RECEIVING_OFF = 1;

  public BlockPowerReceiver() {
    super(Material.IRON);

    setCreativeTab(PowerMoney.creativeTab);
    setHardness(2.5f);
    setHarvestLevel("pickaxe", 2);
    setResistance(20.0f);
    setSoundType(SoundType.METAL);
    setDefaultState(blockState.getBaseState().withProperty(RECEIVING, true));
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

  @Override
  public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
    updatePowerBasedBlockState(world, pos, state);
  }

  @Override
  @Deprecated
  public void neighborChanged(
      IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    updatePowerBasedBlockState(worldIn, pos, worldIn.getBlockState(pos));
  }

  // Using that because it's the more generic one
  @Override
  @Deprecated
  public IBlockState getStateForPlacement(
      World world,
      BlockPos pos,
      EnumFacing facing,
      float hitX,
      float hitY,
      float hitZ,
      int meta,
      EntityLivingBase placer) {
    return getPowerBasedBlockState(world, pos, getStateFromMeta(meta));
  }

  private IBlockState getPowerBasedBlockState(World world, BlockPos pos, IBlockState state) {
    final boolean receiving = state.getValue(RECEIVING);
    final boolean blockPowered = world.isBlockPowered(pos);

    if (receiving && blockPowered) {
      return state.withProperty(RECEIVING, false);
    } else if (!receiving && !blockPowered) {
      return state.withProperty(RECEIVING, true);
    }

    return state;
  }

  private void updatePowerBasedBlockState(World world, BlockPos pos, IBlockState state) {
    if (!world.isRemote) {
      final IBlockState newState = getPowerBasedBlockState(world, pos, state);

      if (newState != state) {
        world.setBlockState(pos, newState);
      }
    }
  }

  // TODO: Allowing to connect it to redstone requires active checking if the neighboring blocks are
  // redstone dust to get it working properly.

  //  @Override
  //  public boolean canConnectRedstone(
  //      IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
  //    return true;
  //  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] {RECEIVING});
  }

  @Override
  @Deprecated
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(RECEIVING, meta == RECEIVING_ON);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(RECEIVING) ? RECEIVING_ON : RECEIVING_OFF;
  }

  @Override
  public int damageDropped(IBlockState state) {
    return RECEIVING_ON;
  }
}
