package omtteam.omtcontrol.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import omtteam.omlib.blocks.BlockAbstractTileEntity;
import omtteam.omtcontrol.tileentity.TileEntityBasicCable;

import javax.annotation.Nonnull;

public class BlockCable extends BlockAbstractTileEntity {

    protected BlockCable() {
        super(Material.GLASS);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityBasicCable();
    }

    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityBasicCable) {
            TileEntityBasicCable cable = (TileEntityBasicCable) tile;
            cable.createNetwork();
        }
    }
}
