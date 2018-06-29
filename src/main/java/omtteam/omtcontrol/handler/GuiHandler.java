package omtteam.omtcontrol.handler;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omtcontrol.client.gui.ManualTargetGui;
import omtteam.omtcontrol.client.gui.containers.BaseAddonBlockContainer;
import omtteam.omtcontrol.tileentity.IGuiable;

import java.util.HashMap;

public class GuiHandler implements IGuiHandler {
    private static GuiHandler instance;

    private GuiHandler() {
    }

    public static GuiHandler getInstance() {
        if (instance == null) {
            instance = new GuiHandler();
        }
        return instance;
    }

    HashMap<Integer, IGuiable> guiBlocks = new HashMap<>();

    public void insertBlock(int guiId, Class<? extends IGuiable> tileEntity) {
        try {
            guiBlocks.put(guiId, tileEntity.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            //TODO: Clean up with a proper log report
            e.printStackTrace();
        }
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (id) {
            case 1:
                return new BaseAddonBlockContainer(player.inventory, new BlockPos(x, y, z));
            default:
                IGuiable te = guiBlocks.get(id);
                if(te != null) {
                    return te.getServerContainer(player.inventory, new BlockPos(x, y, z));
                }
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
                IGuiable te = guiBlocks.get(id);
                if(te != null) {
                    return te.getClientGui(player.inventory, new BlockPos(x, y, z));
                }
                return null;
        }
    }
}