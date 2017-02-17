package omtteam.omtcontrol.handler;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import omtteam.omtcontrol.network.messages.MessageManualTarget;
import omtteam.omtcontrol.network.messages.MessageSendManualShot;
import omtteam.omtcontrol.network.messages.MessageSetAutoFire;
import omtteam.omtcontrol.network.messages.MessageSetYawPitch;
import omtteam.omtcontrol.reference.Reference;

public class NetworkingHandler {
    public final static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    public static void initNetworking() {
        INSTANCE.registerMessage(MessageManualTarget.MessageHandlerManualTarget.class,
                MessageManualTarget.class, 0, Side.CLIENT);

        INSTANCE.registerMessage(MessageSendManualShot.MessageHandlerSendManualShot.class,
                MessageSendManualShot.class, 1, Side.SERVER);

        INSTANCE.registerMessage(MessageSetAutoFire.MessageHandlerSetAutoFire.class,
                MessageSetAutoFire.class, 2, Side.SERVER);

        INSTANCE.registerMessage(MessageSetYawPitch.MessageHandlerSetYawPitch.class,
                MessageSetYawPitch.class, 3, Side.SERVER);
    }
}
