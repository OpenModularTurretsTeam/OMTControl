package omtteam.omtcontrol.proxy;

import omtteam.omtcontrol.handler.NetworkingHandler;
import omtteam.omtcontrol.handler.recipes.RecipeHandler;
import omtteam.omtcontrol.init.ModBlocks;
import omtteam.omtcontrol.init.ModItems;
import omtteam.omtcontrol.init.ModSounds;

public class CommonProxy {
    public void preInit() {
        ModItems.init();
        ModBlocks.initBlocks();
        ModBlocks.initTileEntities();
        ModSounds.init();
        initTileRenderers();
        initHandlers();
    }

    protected void initTileRenderers() {

    }

    protected void initEntityRenderers() {

    }

    protected void initHandlers() {
        NetworkingHandler.initNetworking();
    }

    public void init() {
        RecipeHandler.initRecipes();
        initEntityRenderers();
    }
}