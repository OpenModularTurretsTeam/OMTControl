package omtteam.omtcontrol.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omlib.compatability.minecraft.CompatItem;
import omtteam.omtcontrol.OMTControl;
import omtteam.omtcontrol.blocks.BlockBaseAddonMain;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.Reference;
import omtteam.openmodularturrets.tileentity.TurretBase;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static omtteam.omlib.util.GeneralUtil.getColoredBooleanLocalizationYesNo;
import static omtteam.omlib.util.GeneralUtil.safeLocalize;

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
            if (stack.hasTagCompound()) {
                Set<String> keySet = stack.getTagCompound().getKeySet();

                if (keySet != null) {
                    keySet.clear();
                }
            }
        }
        return super.clOnItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    protected ActionResult<ItemStack> clOnItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (playerIn.isSneaking()) {
            if (itemStackIn.hasTagCompound()) {
                Set<String> keySet = itemStackIn.getTagCompound().getKeySet();

                if (keySet != null) {
                    keySet.clear();
                }
            }
        } else {
            if(hasDataStored(itemStackIn)) {
                NBTTagCompound nbtTagCompound = getDataStored(itemStackIn);
                BlockPos pos = new BlockPos(nbtTagCompound.getInteger("x"),nbtTagCompound.getInteger("y"),nbtTagCompound.getInteger("z"));
                TurretBase base = (TurretBase) worldIn.getTileEntity(pos);
                if (base != null) {
                    base.setAllTurretsYawPitch(playerIn.rotationYaw, playerIn.rotationPitch);
                    base.forceShootAllTurrets();
                }
            }

        }
        return super.clOnItemRightClick(worldIn, playerIn, hand);
    }

    public boolean hasDataStored(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("data");
    }

    public NBTTagCompound getDataStored(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getCompoundTag("data") : null;
    }

    @SuppressWarnings("ConstantConditions")
    public void setDataStored(ItemStack stack, NBTTagCompound nbtTagCompound) {
        if (stack.hasTagCompound()) {
            stack.getTagCompound().setTag("data", nbtTagCompound);
        } else {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setTag("data", nbtTagCompound);
            stack.setTagCompound(tagCompound);
        }
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean isAdvanced) {
        if (hasDataStored(stack)) {
            NBTTagCompound nbtTagCompound = getDataStored(stack);
            tooltip.add("\u00A76X: \u00A7b" + nbtTagCompound.getInteger("x"));
            tooltip.add("\u00A76Y: \u00A7b" + nbtTagCompound.getInteger("y"));
            tooltip.add("\u00A76Z: \u00A7b" + nbtTagCompound.getInteger("z"));
        }
    }
}

