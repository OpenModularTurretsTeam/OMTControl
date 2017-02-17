package omtteam.omtcontrol.handler;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omtcontrol.client.gui.ManualTargetGui;
import omtteam.omtcontrol.client.gui.containers.BaseAddonBlockContainer;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case 1:
                return new BaseAddonBlockContainer(player.inventory, new BlockPos(x, y, z));
            default:
                return null;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case 1:
                return new ManualTargetGui(player.inventory, new BlockPos(x, y, z));
            default:
                return null;
        }
    }
}