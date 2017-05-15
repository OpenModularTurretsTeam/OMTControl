package omtteam.omtcontrol.init;


import net.minecraft.item.Item;
import omtteam.omtcontrol.items.ItemLaserPointer;

import static omtteam.omlib.util.InitHelper.registerItem;

public class ModItems {
    public static Item laserPointer;

    public static void init() {
        laserPointer = registerItem(new ItemLaserPointer());
    }
}
