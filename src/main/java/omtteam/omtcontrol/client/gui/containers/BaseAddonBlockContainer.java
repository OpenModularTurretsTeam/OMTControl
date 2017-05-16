package omtteam.omtcontrol.client.gui.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import omtteam.omtcontrol.handler.NetworkingHandler;
import omtteam.omtcontrol.network.messages.MessageManualTarget;

import javax.annotation.ParametersAreNonnullByDefault;

import static omtteam.omtcontrol.util.WorldUtil.getTurretsFromBase;
import static omtteam.openmodularturrets.util.TurretHeadUtil.getTurretBase;

public class BaseAddonBlockContainer extends Container {
    private final BlockPos pos;

    public BaseAddonBlockContainer(InventoryPlayer inventoryPlayer, BlockPos posIn) {
        pos = posIn;

        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(inventoryPlayer, x, 8 + x * 18, 142));
        }
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(inventoryPlayer, 9 + x + y * 9, 8 + x * 18, 84 + y * 18));
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : this.listeners) {
            if (listener instanceof EntityPlayerMP) {
                World world = ((EntityPlayerMP) listener).getEntityWorld();
                if (getTurretBase(world, pos) == null) {
                    return;
                }
                NetworkingHandler.INSTANCE.sendTo(new MessageManualTarget(this.pos, getTurretsFromBase(getTurretBase(world, pos))), (EntityPlayerMP) listener);
            }
        }
    }
}