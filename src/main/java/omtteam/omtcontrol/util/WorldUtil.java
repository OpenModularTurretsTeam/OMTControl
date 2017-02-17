package omtteam.omtcontrol.util;

import net.minecraft.tileentity.TileEntity;
import omtteam.openmodularturrets.tileentity.turrets.TurretHead;
import omtteam.openmodularturrets.util.TurretHeadSettings;
import omtteam.openmodularturrets.util.TurretHeadUtil;

import java.util.HashMap;

import static omtteam.omlib.util.WorldUtil.getTouchingTileEntities;

/**
 * Created by Keridos on 18/02/17.
 * This Class
 */
public class WorldUtil {

    public static HashMap<Integer, TurretHeadSettings> getTurretsFromBase(TileEntity base) {
        HashMap<Integer, TurretHeadSettings> turrets = new HashMap<>();

        for (TileEntity tileEntity : getTouchingTileEntities(base.getWorld(), base.getPos())) {
            if (tileEntity instanceof TurretHead) {
                TurretHead turretHead = (TurretHead) tileEntity;
                turrets.put(TurretHeadUtil.getTurretBaseFacing(base.getWorld(), turretHead.getPos()).getIndex(), new TurretHeadSettings(turretHead));
            }
        }
        return turrets;
    }
}
