package omtteam.omtcontrol.client.gui;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import omtteam.omlib.compatibility.minecraft.CompatCreativeTabs;
import omtteam.omtcontrol.reference.Reference;
import omtteam.openmodularturrets.init.ModBlocks;
import omtteam.openmodularturrets.items.blocks.ItemBlockLaserTurret;

@MethodsReturnNonnullByDefault
public class OMTControlTab extends CompatCreativeTabs {
    private static OMTControlTab instance;

    @SuppressWarnings("SameParameterValue")
    private OMTControlTab(String label) {
        super(label);
    }

    public static OMTControlTab getInstance() {
        if (instance == null) {
            instance = new OMTControlTab(Reference.NAME);
        }
        return instance;
    }
    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(ModBlocks.laserTurret);
    }

    @Override
    public Item getItem() {
        return new ItemBlockLaserTurret(ModBlocks.laserTurret);
    }
}
