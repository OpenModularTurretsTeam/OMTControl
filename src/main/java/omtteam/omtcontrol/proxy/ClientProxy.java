package omtteam.omtcontrol.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omtcontrol.blocks.BlockBaseAddonMain;
import omtteam.omtcontrol.init.ModBlocks;
import omtteam.omtcontrol.init.ModItems;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.Reference;


@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    private void registerItemModel(final Item item, int meta) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName().toString().toLowerCase()));
    }

    private void registerItemModel(final Item item, int meta, final String variantName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(item.getRegistryName().toString().toLowerCase()), variantName));
    }

    @SuppressWarnings("SameParameterValue")
    private void registerItemModel(final Item item, int meta, final String customName, boolean useCustomName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + customName.toLowerCase()));
    }

    @SuppressWarnings("ConstantConditions")
    private void registerBlockModelAsItem(final Block block, int meta, final String blockName) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + blockName, "inventory"));
    }

    @SuppressWarnings("ConstantConditions")
    private void registerBlockModelAsItem(final Block block, int meta, final String blockName, String variantName) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + blockName, variantName));
    }

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void renderRegistry() {
        for (int i = 0; i < BlockBaseAddonMain.SUBBLOCK_COUNT; i++) {
            registerBlockModelAsItem(ModBlocks.baseAddonMain, i, OMTControlNames.Blocks.baseAddonMain, "facing=north,meta=" + i);
        }
        registerBlockModelAsItem(ModBlocks.HACKING_TERMINAL, 0, OMTControlNames.Blocks.HACKING_TERMINAL);
        registerItemModel(ModItems.laserPointer, 0);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    protected void initTileRenderers() {
        super.initTileRenderers();

    }

    @Override
    protected void initEntityRenderers() {
        super.initEntityRenderers();
    }


    @Override
    public void initHandlers() {
        super.initHandlers();
    }


}