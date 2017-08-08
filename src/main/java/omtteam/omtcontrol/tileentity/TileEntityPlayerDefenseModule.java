package omtteam.omtcontrol.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import omtteam.omlib.tileentity.EnumMachineMode;
import omtteam.omlib.util.PlayerUtil;
import omtteam.openmodularturrets.tileentity.TurretBase;

import java.util.List;

/**
 * Created by dmf444 on 8/7/2017. Code originally written for OMTControl.
 */
public class TileEntityPlayerDefenseModule extends TileEntityBaseAddonMain implements  ITickable {

    private TurretBase base = null;
    private int ticks;
    private boolean isBaseActive = true;
    private EnumMachineMode originalMode = null;

    @Override
    public void update() {
        // Get base on first update. Probably reset by world reload.
        if(base == null){
            base = getBase();
            isBaseActive = base.isActive();
            originalMode = base.getMode();
        }

        if(!isBaseActive) {
            ticks++;

            if (ticks % 20 == 0) {
                if(getWorld().isAnyPlayerWithinRangeAt(base.getPos().getX(), base.getPos().getY(), base.getPos().getZ(), base.getCurrentMaxRange())) {
                    //System.out.println("INRANGE");
                    if (shouldProtectPlayer()) {
                        base.setMode(EnumMachineMode.ALWAYS_ON);
                    }
                } else {
                    base.setMode(originalMode);
                }
            }
        }
    }

    private boolean shouldProtectPlayer(){
        AxisAlignedBB aabb = new AxisAlignedBB(getPos()).expand(base.getCurrentMaxRange(), base.getCurrentMaxRange(), base.getCurrentMaxRange());
        List players = getWorld().getEntitiesWithinAABB(EntityPlayer.class, aabb);
        while(players.iterator().hasNext()){
            EntityPlayer player = (EntityPlayer) players.iterator().next();
            if(PlayerUtil.isPlayerOwner(player, getBase()) || PlayerUtil.getTrustedPlayer(player, getBase()) != null){
                return true;
            }
        }
        return false;
    }
}
