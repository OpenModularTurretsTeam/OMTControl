package omtteam.omtcontrol.handler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omtcontrol.OMTControl;
import omtteam.omtcontrol.init.ModBlocks;
import omtteam.omtcontrol.init.ModItems;
import omtteam.omtcontrol.init.ModSounds;

public class EventsHandler {

    private static EventsHandler instance;

    private EventsHandler() {
    }

    public static EventsHandler getInstance() {
        if (instance == null) {
            instance = new EventsHandler();
        }
        return instance;
    }

    @SubscribeEvent
    public void blockRegisterEvent(RegistryEvent.Register<Block> event) {
        ModBlocks.initBlocks(event.getRegistry());
    }

    @SubscribeEvent
    public void itemRegisterEvent(RegistryEvent.Register<Item> event) {
        ModItems.init(event.getRegistry());
    }

    @SubscribeEvent
    public void soundRegistryEvent(RegistryEvent.Register<SoundEvent> event) {
        ModSounds.init(event.getRegistry());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void modelRegistryEvent(ModelRegistryEvent e) {
        OMTControl.proxy.renderRegistry();
    }
}
