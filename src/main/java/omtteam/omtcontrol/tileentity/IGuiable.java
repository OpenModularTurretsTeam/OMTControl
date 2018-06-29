package omtteam.omtcontrol.tileentity;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;

public interface IGuiable {

    public Container getServerContainer(InventoryPlayer invPlayer, BlockPos blockPos);
    public Gui getClientGui(InventoryPlayer invPlayer, BlockPos blockPos);

}
