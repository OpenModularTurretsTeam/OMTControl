package omtteam.omtcontrol.init;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import omtteam.omtcontrol.blocks.BlockBaseAddonMain;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.Reference;
import omtteam.omtcontrol.tileentity.TileEntityBaseAddonMain;
import omtteam.omtcontrol.tileentity.TileEntityPlayerDefenseModule;

import static omtteam.omlib.util.InitHelper.registerBlock;

public class ModBlocks {
    public static Block baseAddonMain;


    public static void initBlocks(IForgeRegistry<Block> registry) {
        baseAddonMain = registerBlock(new BlockBaseAddonMain(), registry, ModItems.itemBlocks);
    }


    public static void initTileEntities() {
        GameRegistry.registerTileEntity(TileEntityBaseAddonMain.class, toResource(OMTControlNames.Blocks.baseAddonMain));
        GameRegistry.registerTileEntity(TileEntityPlayerDefenseModule.class, toResource(OMTControlNames.Blocks.PLAYER_DEFENSE_MODULE));
    }

    private static ResourceLocation toResource(String name){
        return new ResourceLocation(Reference.MOD_ID, name);
    }
}
