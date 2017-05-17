package omtteam.omtcontrol.init;

import net.minecraft.block.Block;
import omtteam.omtcontrol.blocks.BlockBaseTurretAddonMain;

import static omtteam.omlib.util.InitHelper.registerBlock;

public class ModBlocks {
    public static Block manualTarget;


    public static void initBlocks() {
        manualTarget = registerBlock(new BlockBaseTurretAddonMain());
    }


    public static void initTileEntities() {

    }
}
