package omtteam.omtcontrol.tileentity;

import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.openmodularturrets.tileentity.TurretBase;
import omtteam.openmodularturrets.util.ITurretBaseAddonTileEntity;
import omtteam.openmodularturrets.util.TurretHeadUtil;

/**
 * Created by Keridos on 17/05/17.
 * This Class
 */
public class TileEntityTurretBaseAddonMain extends TileEntityOwnedBlock implements ITurretBaseAddonTileEntity {
    @Override
    public String getOwnerDelegate() {
        return TurretHeadUtil.getTurretBase(this.getWorld(), this.pos).getOwner();
    }

    public TurretBase getBase() {
        return TurretHeadUtil.getTurretBase(this.getWorld(), this.pos);
    }
}
