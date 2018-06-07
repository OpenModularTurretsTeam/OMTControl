package omtteam.omtcontrol.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import omtteam.omlib.blocks.BlockAbstractTileEntity;
import omtteam.omtcontrol.OMTControl;
import omtteam.omtcontrol.handler.GuiHandler;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.Reference;
import omtteam.omtcontrol.tileentity.TileEntityHackingTerminal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockHackingTerminal extends BlockAbstractTileEntity {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final int GUI_ID = 2;

    public BlockHackingTerminal() {
        super(Material.ROCK);
        this.setCreativeTab(OMTControl.creativeTab);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.STONE);
        this.setUnlocalizedName(OMTControlNames.Blocks.HACKING_TERMINAL);
        this.setRegistryName(Reference.MOD_ID, OMTControlNames.Blocks.HACKING_TERMINAL);

        GuiHandler.getInstance().insertBlock(GUI_ID, TileEntityHackingTerminal.class);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityHackingTerminal();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (hand.equals(EnumHand.OFF_HAND)) return true;
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity == null || player.isSneaking()) return false;

        player.openGui(OMTControl.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityHackingTerminal) {
            TileEntityHackingTerminal tileEntityHackingTerminal = (TileEntityHackingTerminal) te;
            tileEntityHackingTerminal.setOwner(placer.getName());
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    public boolean isOpaqueCube(IBlockState s) {
        return false;
    }

    /*@Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return new AxisAlignedBB(0.2f, 0.0f, 0.2f, 0.8f, 0.65f, 0.82f);
    }*/

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0.2f, 0.0f, 0.2f, 0.8f, 0.65f, 0.82f);
    }
}
