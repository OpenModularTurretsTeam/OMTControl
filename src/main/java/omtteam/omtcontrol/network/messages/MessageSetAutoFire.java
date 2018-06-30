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
public class MessageSetAutoFire implements IMessage {
    private int x, y, z;
    private boolean autoFire;

    public MessageSetAutoFire() {
    }

    public MessageSetAutoFire(int x, int y, int z, boolean autoFire) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.autoFire = autoFire;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();

        this.autoFire = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);

        buf.writeBoolean(this.autoFire);
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

    @SuppressWarnings("ConstantConditions")
    public static class MessageHandlerSetAutoFire implements IMessageHandler<MessageSetAutoFire, IMessage> {
        @Override
        public IMessage onMessage(MessageSetAutoFire messageIn, MessageContext ctxIn) {
            final MessageSetAutoFire message = messageIn;
            final MessageContext ctx = ctxIn;
            ((WorldServer) ctx.getServerHandler().player.getEntityWorld()).addScheduledTask(() -> {
                World world = ctx.getServerHandler().player.getEntityWorld();
                TurretHead turret = (TurretHead) world.getTileEntity(new BlockPos(message.getX(), message.getY(), message.getZ()));

                turret.setAutoFire(message.autoFire);
            });
            return null;
        }
    }
}
