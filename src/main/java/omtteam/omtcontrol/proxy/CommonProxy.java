package omtteam.omtcontrol.proxy;

import net.minecraftforge.common.MinecraftForge;
import omtteam.omtcontrol.handler.EventsHandler;
import omtteam.omtcontrol.handler.NetworkingHandler;
import omtteam.omtcontrol.handler.recipes.RecipeHandler;
import omtteam.omtcontrol.init.ModBlocks;
import omtteam.omtcontrol.init.ModItems;
import omtteam.omtcontrol.init.ModSounds;

public class CommonProxy {
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(EventsHandler.getInstance());
        ModBlocks.initTileEntities();
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

    public void renderRegistry() {
    }
}