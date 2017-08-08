package omtteam.omtcontrol.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import omtteam.omtcontrol.blocks.BlockBaseAddonMain;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.tileentity.TileEntityBaseAddonMain;
import omtteam.omtcontrol.tileentity.TileEntityPlayerDefenseModule;

import static omtteam.omlib.util.InitHelper.registerBlock;

public class ModBlocks {
    public static Block baseAddonMain;


    public static void initBlocks() {
        baseAddonMain = registerBlock(new BlockBaseAddonMain());
    }


    public static void initTileEntities() {
        GameRegistry.registerTileEntity(TileEntityBaseAddonMain.class, OMTControlNames.Blocks.baseAddonMain);
        GameRegistry.registerTileEntity(TileEntityPlayerDefenseModule.class, OMTControlNames.Blocks.PLAYER_DEFENSE_MODULE);
    }
}
