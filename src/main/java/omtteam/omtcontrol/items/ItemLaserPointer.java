package omtteam.omtcontrol.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
import omtteam.omtcontrol.OMTControl;
import omtteam.omtcontrol.blocks.BlockBaseAddonMain;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.Reference;
import omtteam.omtcontrol.tileentity.TileEntityBaseAddonMain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Created by Rokiyo on 15/05/2017.
 */
public class ItemLaserPointer extends Item {
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
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        NBTTagCompound tagCompound = getTagCompound(stack);

        if (isSelected && entityIn instanceof EntityPlayer && tagCompound.getBoolean("isActive")) {
            aimAllTurrets(worldIn, (EntityPlayer) entityIn, stack);

            if (worldIn.getTotalWorldTime() > tagCompound.getLong("lastActivated") + 5) {
                //stop firing if more than 5 ticks have passed since the player last right-clicked.
                setAllTurretsForceFire(worldIn, stack, false);
            }
        }
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            ItemStack stack = player.getHeldItem(hand);
            clearNBTData(stack);
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (playerIn.isSneaking()) {
            clearNBTData(itemStackIn);
        } else {
            if (hasLinkedBases(itemStackIn)) {
                //TODO: Look into a method that doesn't constantly overwrite the item's NBT data... It makes the item constantly refresh in the player's hand.
                setLastActivation(worldIn, playerIn);
                setAllTurretsForceFire(worldIn, itemStackIn, true);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }

    @Nonnull
    public void aimAllTurrets(World worldIn, EntityPlayer playerIn, ItemStack itemStackIn) {
        if (hasLinkedBases(itemStackIn)) {
            NBTTagList linkedBases = getLinkedBases(itemStackIn);

            for (int i = 0; i < linkedBases.tagCount(); i++) {
                int[] blockPosArray = linkedBases.getCompoundTagAt(i).getIntArray("blockPos");
                BlockPos pos = new BlockPos(blockPosArray[0], blockPosArray[1], blockPosArray[2]);

                TileEntityBaseAddonMain addon = (TileEntityBaseAddonMain) worldIn.getTileEntity(pos);
                if (addon != null) {
                    addon.setAllTurretsYawPitch(playerIn.rotationYawHead + 90, playerIn.rotationPitch + 90);
                }
            }
        }
    }

    @Nonnull
    public void setAllTurretsForceFire(World worldIn, ItemStack itemStackIn, boolean state) {
        if (hasLinkedBases(itemStackIn)) {
            NBTTagList linkedBases = getLinkedBases(itemStackIn);
            NBTTagCompound tagCompound = getTagCompound(itemStackIn);
            tagCompound.setBoolean("isActive", state);

            for (int i = 0; i < linkedBases.tagCount(); i++) {
                int[] blockPosArray = linkedBases.getCompoundTagAt(i).getIntArray("blockPos");
                BlockPos pos = new BlockPos(blockPosArray[0], blockPosArray[1], blockPosArray[2]);

                TileEntityBaseAddonMain addon = (TileEntityBaseAddonMain) worldIn.getTileEntity(pos);
                if (addon != null) {
                    addon.setAllTurretsForceFire(state);

                    //TODO: Remove the following line once auto-firing is working correctly.
                    if (state) {
                        addon.forceShootAllTurrets();
                    }
                }
            }
        }
    }

    @Nonnull
    public NBTTagCompound getTagCompound(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    @Nonnull
    public boolean hasLinkedBases(ItemStack stack) {
        return getTagCompound(stack).hasKey("turretBases");
    }

    @Nonnull
    public NBTTagList getLinkedBases(ItemStack stack) {
        return getTagCompound(stack).getTagList("turretBases", Constants.NBT.TAG_COMPOUND);
    }

    public void clearNBTData(ItemStack stack) {
        Set<String> keySet = getTagCompound(stack).getKeySet();

        if (keySet != null) {
            keySet.clear();
        }
    }

    public void addLinkedBase(EntityPlayer playerIn, BlockPos pos) {
        ItemStack stack = playerIn.getHeldItemMainhand();

        NBTTagList linkedBases = getLinkedBases(stack);
        NBTTagCompound newBase = new NBTTagCompound();

        int[] blockPosArray = {pos.getX(), pos.getY(), pos.getZ()};
        newBase.setIntArray("blockPos", blockPosArray);
        linkedBases.appendTag(newBase);

        getTagCompound(stack).setTag("turretBases", linkedBases);
    }

    public void setLastActivation(World worldIn, EntityPlayer playerIn) {
        ItemStack stack = playerIn.getHeldItemMainhand();
        NBTTagCompound tagCompound = getTagCompound(stack);

        tagCompound.setLong("lastActivated", worldIn.getTotalWorldTime());
        tagCompound.setBoolean("isActive", true);
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        if (hasLinkedBases(stack)) {
            NBTTagCompound tagCompound = getTagCompound(stack);
            NBTTagList linkedBases = getLinkedBases(stack);
            tooltip.add("\u00A76Linked Bases: \u00A7b" + linkedBases.tagCount());
            tooltip.add("\u00A76Active: \u00A7b" + tagCompound.getBoolean("isActive"));
            tooltip.add("\u00A76LastUsed: \u00A7b" + tagCompound.getLong("lastActivated"));
        }
    }
}

