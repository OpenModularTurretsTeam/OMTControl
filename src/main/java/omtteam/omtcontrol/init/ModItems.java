package omtteam.omtcontrol.init;


import net.minecraft.item.Item;
import omtteam.omtcontrol.items.ItemLaserPointer;
import omtteam.omtcontrol.items.ItemRemoteTurretConfig;

import static omtteam.omlib.util.InitHelper.registerItem;

public class ModItems {
    public static Item laserPointer;
    public static Item REMOTE_TURRET_CONFIG;

    public static void init() {
        laserPointer = registerItem(new ItemLaserPointer());
        REMOTE_TURRET_CONFIG = registerItem(new ItemRemoteTurretConfig());
    }
}
