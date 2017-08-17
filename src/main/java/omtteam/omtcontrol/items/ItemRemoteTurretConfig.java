package omtteam.omtcontrol.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import omtteam.omlib.compatibility.minecraft.CompatItem;
import omtteam.omlib.util.PlayerUtil;
import omtteam.omtcontrol.OMTControl;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.Reference;
import omtteam.openmodularturrets.tileentity.TurretBase;

/**
 * Created by dmf444 on 8/8/2017. Code originally written for OMTControl.
 */
public class ItemRemoteTurretConfig extends CompatItem{

    public ItemRemoteTurretConfig(){
        this.setHasSubtypes(false);
        this.setCreativeTab(OMTControl.creativeTab);
        this.setRegistryName(Reference.MOD_ID, OMTControlNames.Items.REMOTE_TURRET_CONFIG);
        this.setUnlocalizedName(OMTControlNames.Items.REMOTE_TURRET_CONFIG);
    }

    protected EnumActionResult clOnItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(world.getTileEntity(pos) instanceof TurretBase){
            TurretBase base = (TurretBase) world.getTileEntity(pos);
            if(PlayerUtil.isPlayerOwner(player, base) || PlayerUtil.getTrustedPlayer(player, base) != null){

            }
        }
        System.out.println("This actually works");
        return EnumActionResult.SUCCESS;
    }
}
