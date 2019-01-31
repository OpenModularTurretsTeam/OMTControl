package omtteam.omtcontrol.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import omtteam.omlib.api.permission.EnumMachineMode;
import omtteam.omlib.util.player.PlayerUtil;
import omtteam.openmodularturrets.tileentity.TurretBase;

import java.util.Iterator;
import java.util.List;

/**
 * Created by dmf444 on 8/7/2017. Code originally written for OMTControl.
 */
public class TileEntityPlayerDefenseModule extends TileEntityBaseAddonMain implements ITickable {

    private TurretBase base = null;
    private int ticks;
    private boolean isBaseActive = true;
    private EnumMachineMode originalMode = null;

    @Override
    public void update() {
        // Get base on first update. Probably reset by world reload.
        if (base == null) {
            base = getBase();
            if (base != null) {
                isBaseActive = base.isActive();
                originalMode = base.getMode();
            }
        }

        //If the base is already active, no need to update
        if (!isBaseActive) {
            ticks++;

            //Random tick update rate of 20...
            if (ticks % 20 == 0) {
                if (getWorld().isAnyPlayerWithinRangeAt(base.getPos().getX(), base.getPos().getY(), base.getPos().getZ(), base.getCurrentMaxRange())) {
                    if (shouldProtectPlayer()) {
                        base.setMode(EnumMachineMode.ALWAYS_ON);
                    }
                } else {
                    base.setMode(originalMode);
                }
            }
        }
    }

    /**
     * Given the location of this tile, find if there are any active players within it's range, and check if the
     * turret should defend them. If the player is trusted on the turret OR this is the owner, return true. Otherwise
     * return false.
     * Note: this could start lag if enough players are within range.
     *
     * @return bool, if any players in range should be protected
     */
    private boolean shouldProtectPlayer() {
        // Create an AABB that matches the world.isAnyPlayerWithinRangeAt call.
        AxisAlignedBB aabb = new AxisAlignedBB(getPos()).expand(base.getCurrentMaxRange(), base.getCurrentMaxRange(), base.getCurrentMaxRange());
        // Grab all players within that AABB
        List players = getWorld().getEntitiesWithinAABB(EntityPlayer.class, aabb);
        // Find if one of the players should be protected.
        Iterator iterator = players.iterator();
        while (iterator.hasNext()) {
            EntityPlayer player = (EntityPlayer) iterator.next();
            if (PlayerUtil.isPlayerOwner(player, getBase()) || PlayerUtil.getTrustedPlayer(player, getBase()) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        // This is some hack-job code. Basically, on world unload, the block should reset the turret's
        // original Mode. if it doesn't and a player is still in the area, the turret will not shut off.
        if (base != null) {
            base.setMode(originalMode);
        }
        return super.writeToNBT(nbtTagCompound);
    }
}
