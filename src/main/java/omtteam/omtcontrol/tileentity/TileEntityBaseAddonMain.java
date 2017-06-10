package omtteam.omtcontrol.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.openmodularturrets.api.ITurretBaseAddonTileEntity;
import omtteam.openmodularturrets.tileentity.TurretBase;
import omtteam.openmodularturrets.util.TurretHeadUtil;

import static omtteam.openmodularturrets.util.TurretHeadUtil.getTurretBaseFacing;

/**
 * Created by Keridos on 17/05/17.
 * This Class
 */
public class TileEntityBaseAddonMain extends TileEntityOwnedBlock implements ITurretBaseAddonTileEntity {
    private int meta;
    private EnumFacing orientation;

    public TileEntityBaseAddonMain() {
        this.meta = 0;
        this.orientation = EnumFacing.NORTH;
    }

    public TileEntityBaseAddonMain(int meta) {
        this.meta = meta;
        this.orientation = EnumFacing.NORTH;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setByte("direction", (byte) orientation.ordinal());
        nbtTagCompound.setInteger("meta", meta);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.meta = nbtTagCompound.getInteger("meta");
        if (nbtTagCompound.hasKey("direction")) {
            this.setOrientation(EnumFacing.getFront(nbtTagCompound.getByte("direction")));
        }
    }

    @Override
    public String getOwner() {
        return getBase().getOwner();
    }

    @Override
    public String getOwnerName() {
        return getBase().getOwnerName();
    }

    public void setAllTurretsYawPitch(float yaw, float pitch) { getBase().setAllTurretsYawPitch(yaw, pitch); }

    public int forceShootAllTurrets() { return getBase().forceShootAllTurrets(); }

    public void setAllTurretsForceFire(boolean state) { getBase().setAllTurretsForceFire(state); }

    public TurretBase getBase() {
        return TurretHeadUtil.getTurretBase(this.getWorld(), this.pos);
    }

    public EnumFacing getOrientation() {
        return orientation;
    }

    public void setOrientation(EnumFacing orientation) {
        this.orientation = orientation;
    }

    public void setSide() {
        this.setOrientation(getTurretBaseFacing(this.getWorld(), this.pos));
    }
}
