package omtteam.omtcontrol.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import omtteam.openmodularturrets.tileentity.turrets.TurretHead;


@SuppressWarnings("unused")
public class MessageSendManualShot implements IMessage {
    private int x, y, z;
    private boolean autoFire;

    public MessageSendManualShot() {
    }

    @SuppressWarnings("ConstantConditions")
    public static class MessageHandlerSendManualShot implements IMessageHandler<MessageSendManualShot, IMessage> {
        @Override
        public IMessage onMessage(MessageSendManualShot messageIn, MessageContext ctxIn) {
            final MessageSendManualShot message = messageIn;
            final MessageContext ctx = ctxIn;
            ((WorldServer) ctx.getServerHandler().playerEntity.getEntityWorld()).addScheduledTask(() -> {
                World world = ctx.getServerHandler().playerEntity.getEntityWorld();
                TurretHead turret = (TurretHead) world.getTileEntity(new BlockPos(message.getX(), message.getY(), message.getZ()));

                turret.forceShot();
            });
            return null;
        }
    }

    public MessageSendManualShot(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    private int getX() {
        return x;
    }

    private int getY() {
        return y;
    }

    private int getZ() {
        return z;
    }
}
