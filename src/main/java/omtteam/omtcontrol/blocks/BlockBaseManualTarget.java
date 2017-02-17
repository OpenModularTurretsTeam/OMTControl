package omtteam.omtcontrol.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import omtteam.omlib.util.PlayerUtil;
import omtteam.omlib.util.TrustedPlayer;
import omtteam.omtcontrol.OMTControl;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.Reference;
import omtteam.openmodularturrets.blocks.BlockTurretBaseAddon;
import omtteam.openmodularturrets.tileentity.TurretBase;

import static omtteam.omlib.util.GeneralUtil.safeLocalize;
import static omtteam.omlib.util.compat.ChatTools.addChatMessage;

/**
 * Created by Keridos on 09/02/17.
 * This Class
 */
public class BlockBaseManualTarget extends BlockTurretBaseAddon {

    public BlockBaseManualTarget() {
        super();
        this.setCreativeTab(OMTControl.creativeTab);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.STONE);
        this.setUnlocalizedName(OMTControlNames.Blocks.manualTarget);
        this.setRegistryName(Reference.MOD_ID, OMTControlNames.Blocks.manualTarget);
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return true;
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
        TrustedPlayer trustedPlayer = PlayerUtil.getTrustedPlayer(playerIn, base);
        if (trustedPlayer != null) {
            if (base.getTrustedPlayer(playerIn.getUniqueID()).canOpenGUI) {
                playerIn.openGui(OMTControl.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }
        if (PlayerUtil.isPlayerOwner(playerIn, base)) {
            if (playerIn.isSneaking() && playerIn.getHeldItemMainhand() == null) {
                worldIn.destroyBlock(pos, true);
            } else {
                playerIn.openGui(OMTControl.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        } else {
            addChatMessage(playerIn, new TextComponentString(safeLocalize("status.ownership")));
        }
        return true;
    }
}
