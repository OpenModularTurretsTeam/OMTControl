package omtteam.omtcontrol.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omlib.compatability.minecraft.CompatItem;
import omtteam.omtcontrol.OMTControl;
import omtteam.omtcontrol.blocks.BlockBaseAddonMain;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.Reference;
import omtteam.omtcontrol.tileentity.TileEntityBaseAddonMain;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * Created by Rokiyo on 15/05/2017.
 */
public class ItemLaserPointer extends CompatItem {
    public ItemLaserPointer() {
        super();

        this.setHasSubtypes(false);
        this.setCreativeTab(OMTControl.creativeTab);
        this.setRegistryName(Reference.MOD_ID, OMTControlNames.Items.laserPointer);
        this.setUnlocalizedName(OMTControlNames.Items.laserPointer);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return world.getBlockState(pos).getBlock() instanceof BlockBaseAddonMain || super.doesSneakBypassUse(stack, world, pos, player);
    }

    @Override
    @Nonnull
    public EnumActionResult clOnItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            ItemStack stack = player.getHeldItem(hand);
            clearLinkedBases(stack);
        }
        return super.clOnItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    protected ActionResult<ItemStack> clOnItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (playerIn.isSneaking()) {
            clearLinkedBases(itemStackIn);
        } else {
            if(hasLinkedBases(itemStackIn)) {
                NBTTagList linkedBases = getLinkedBases(itemStackIn);

                for (int i = 0; i < linkedBases.tagCount(); i++) {
                    int[] blockPosArray = linkedBases.getCompoundTagAt(i).getIntArray("blockPos");
                    BlockPos pos = new BlockPos(blockPosArray[0], blockPosArray[1], blockPosArray[2]);

                    TileEntityBaseAddonMain addon = (TileEntityBaseAddonMain) worldIn.getTileEntity(pos);
                    if (addon != null) {
                        addon.setAllTurretsYawPitch(playerIn.rotationYaw, playerIn.rotationPitch);

                        //TODO: Use the tile entity to force-fire the turrets for a few ticks, so that turret's fire rate is not limited by item's use speed.
                        addon.forceShootAllTurrets();
                    }
                }
            }

        }
        return super.clOnItemRightClick(worldIn, playerIn, hand);
    }

    @Nonnull
    public boolean hasLinkedBases(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("turretBases");
    }

    @Nonnull
    public NBTTagList getLinkedBases(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getTagList("turretBases", Constants.NBT.TAG_COMPOUND) : new NBTTagList();
    }

    public void clearLinkedBases(ItemStack stack) {
        if (stack.hasTagCompound()) {
            Set<String> keySet = stack.getTagCompound().getKeySet();

            if (keySet != null) {
                keySet.clear();
            }
        }
    }

    public void addLinkedBase(EntityPlayer playerIn, BlockPos pos) {
        ItemStack stack = playerIn.getHeldItemMainhand();

        NBTTagList linkedBases = getLinkedBases(stack);
        NBTTagCompound newBase = new NBTTagCompound();

        int[] blockPosArray = { pos.getX(), pos.getY(), pos.getZ() };
        newBase.setIntArray("blockPos", blockPosArray);
        linkedBases.appendTag(newBase);

        if (stack.hasTagCompound()) {
            stack.getTagCompound().setTag("turretBases", linkedBases);
        } else {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setTag("turretBases", linkedBases);
            stack.setTagCompound(tagCompound);
        }
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isAdvanced) {
        if(hasLinkedBases(stack)) {
            NBTTagList linkedBases = getLinkedBases(stack);
            tooltip.add("\u00A76Linked Bases: \u00A7b" + linkedBases.tagCount());
        }
    }
}

