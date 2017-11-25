package omtteam.omtcontrol.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omlib.api.IHasItemBlock;
import omtteam.omlib.util.PlayerUtil;
import omtteam.omlib.util.TrustedPlayer;
import omtteam.omlib.util.compat.ItemStackTools;
import omtteam.omtcontrol.OMTControl;
import omtteam.omtcontrol.init.ModBlocks;
import omtteam.omtcontrol.items.ItemLaserPointer;
import omtteam.omtcontrol.items.blocks.ItemBlockBaseAddonMain;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.Reference;
import omtteam.omtcontrol.tileentity.TileEntityBaseAddonMain;
import omtteam.omtcontrol.tileentity.TileEntityPlayerDefenseModule;
import omtteam.openmodularturrets.blocks.BlockBaseAddon;
import omtteam.openmodularturrets.blocks.BlockTurretBaseAddon;
import omtteam.openmodularturrets.tileentity.TurretBase;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static omtteam.omlib.util.GeneralUtil.safeLocalize;
import static omtteam.omlib.util.compat.ChatTools.addChatMessage;

/**
 * Created by Keridos on 09/02/17.
 * This Class
 */
public class BlockBaseAddonMain extends BlockTurretBaseAddon implements IHasItemBlock {
    public static final int SUBBLOCK_COUNT = 2;
    private static final PropertyInteger META = PropertyInteger.create("meta", 0, SUBBLOCK_COUNT - 1);
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public  BlockBaseAddonMain() {
        super(Material.ROCK);
        this.setCreativeTab(OMTControl.creativeTab);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.STONE);
        this.setUnlocalizedName(OMTControlNames.Blocks.baseAddonMain);
        this.setRegistryName(Reference.MOD_ID, OMTControlNames.Blocks.baseAddonMain);
    }

    @Override
    public ItemBlock getItemBlock(Block block) {
        return new ItemBlockBaseAddonMain(block);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(META, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(META);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, META, FACING);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntityBaseAddonMain te = ((TileEntityBaseAddonMain) worldIn.getTileEntity(pos));
        if (te != null) {
            return state.withProperty(FACING, te.getOrientation());
        } else return state.withProperty(FACING, EnumFacing.NORTH);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public TileEntity createTileEntity(World world, IBlockState state) {
        switch (state.getValue(META)){
            case 1:
                return new TileEntityPlayerDefenseModule();
            default:
                return new TileEntityBaseAddonMain(state.getValue(META));
        }

    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntityBaseAddonMain te = (TileEntityBaseAddonMain) worldIn.getTileEntity(pos);
        if (te != null) {
            te.setSide();
        }
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        IBlockState blockState = this.getActualState(state, source, pos);
        EnumFacing facing = blockState.getValue(FACING);
        return BlockBaseAddon.getBoundingBoxFromFacing(facing);
    }


    @Override
    public AxisAlignedBB getBoundingBoxFromFacing(EnumFacing facing, World world, BlockPos pos) {
        return BlockBaseAddon.getBoundingBoxFromFacing(facing).offset(pos);
    }


    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    protected boolean clOnBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (hand.equals(EnumHand.OFF_HAND)) return true;
        TurretBase base = getBase(worldIn, pos);
        if (base == null) {
            worldIn.destroyBlock(pos, true);
            return true;
        }
        if(state.getValue(META) == 0) {
            //TODO: Remove code repetition
            TrustedPlayer trustedPlayer = PlayerUtil.getTrustedPlayer(playerIn, base);
            if (trustedPlayer != null) {
                if (base.getTrustedPlayer(playerIn.getUniqueID()).canOpenGUI) {
                    handleMeta0(playerIn, pos, worldIn, false);
                }
            }
            boolean isOwner = PlayerUtil.isPlayerOwner(playerIn, base);
            if (isOwner) {
                handleMeta0(playerIn, pos, worldIn, isOwner);
            } else {
                addChatMessage(playerIn, new TextComponentString(safeLocalize("status.ownership")));
            }
            return true;
        }
        return false;
    }

    private boolean handleMeta0(EntityPlayer playerIn, BlockPos pos, World worldIn, boolean isOwner){
        if (playerIn.getHeldItemMainhand() != ItemStackTools.getEmptyStack() && playerIn.getHeldItemMainhand().getItem() instanceof ItemLaserPointer) {
            if (playerIn.isSneaking()) {
                //TODO: Unlink laser pointer
            } else {
                ((ItemLaserPointer) playerIn.getHeldItemMainhand().getItem()).addLinkedBase(playerIn, pos);
            }
        } else if (isOwner && playerIn.isSneaking() && playerIn.getHeldItemMainhand() == null) {
            worldIn.destroyBlock(pos, true);
        } else {
            playerIn.openGui(OMTControl.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return true;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(META);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    @ParametersAreNonnullByDefault
    public void clGetSubBlocks(Item item, CreativeTabs tab, List subItems) {
        for (int i = 0; i < SUBBLOCK_COUNT; i++) {
            subItems.add(new ItemStack(ModBlocks.baseAddonMain, 1, i));
        }
    }
}
