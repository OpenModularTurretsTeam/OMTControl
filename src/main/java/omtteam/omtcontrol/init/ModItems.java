package omtteam.omtcontrol.init;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import omtteam.omtcontrol.items.ItemLaserPointer;
import omtteam.omtcontrol.items.ItemRemoteTurretConfig;

import java.util.ArrayList;
import java.util.List;

import static omtteam.omlib.util.InitHelper.registerItem;

public class ModItems {
    public static final List<Item> itemBlocks = new ArrayList<>();
    public static Item laserPointer;
    public static Item REMOTE_TURRET_CONFIG;

    public static void init(IForgeRegistry<Item> registry) {
        laserPointer = registerItem(new ItemLaserPointer(), registry);
        REMOTE_TURRET_CONFIG = registerItem(new ItemRemoteTurretConfig(), registry);

        for (Item item : itemBlocks) {
            registry.register(item);
        }
    }
}
