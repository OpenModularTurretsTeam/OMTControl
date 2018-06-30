package omtteam.omtcontrol.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.openmodularturrets.tileentity.TurretBase;
import omtteam.openmodularturrets.util.TurretHeadSettings;
import omtteam.openmodularturrets.util.TurretHeadUtil;

import java.util.HashMap;

import static omtteam.omlib.proxy.ClientProxy.getWorld;


public class MessageManualTarget implements IMessage {
    private int x, y, z;
    private HashMap<Integer, TurretHeadSettings> turrets = new HashMap<>();

    public MessageManualTarget() {
    }

    public MessageManualTarget(BlockPos pos, HashMap<Integer, TurretHeadSettings> turrets) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();

        this.turrets = turrets;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();

        int lengthOfTurretList = buf.readInt();
        if (lengthOfTurretList > 0) {
            for (int i = 0; i < lengthOfTurretList; i++) {
                int facing = buf.readInt();
                TurretHeadSettings setting = new TurretHeadSettings().readFromBuf(buf);
                turrets.put(facing, setting);
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);

        buf.writeInt(turrets.size());
        turrets.forEach((k, v) -> {
            buf.writeInt(k);
            v.writeToBuf(buf);
        });
    }

    @Override
    public String toString() {
        return String.format(
                "MessageManualTarget - x:%s, y:%s, z:%s", x, y, z);
    }

    public static class MessageHandlerManualTarget implements IMessageHandler<MessageManualTarget, IMessage> {
        @Override
        @SuppressWarnings("deprecation")
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(MessageManualTarget messageIn, MessageContext ctx) {
            final MessageManualTarget message = messageIn;
            Minecraft.getMinecraft().addScheduledTask(() -> {

                TileEntity tileEntity = TurretHeadUtil.getTurretBase(getWorld(FMLClientHandler.instance().getClient()), new BlockPos(message.x, message.y, message.z));
                if (tileEntity != null) {
                    message.turrets.forEach((k, v) -> {
                        ((TurretBase) tileEntity).setTurretForceFire(EnumFacing.getFront(k), v.forceFire);
                        ((TurretBase) tileEntity).setTurretYawPitch(EnumFacing.getFront(k), v.yaw, v.pitch);
                    });
                }
            });
            return null;
        }

    }
}

