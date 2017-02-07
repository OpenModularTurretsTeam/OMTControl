package omtteam.omtcontrol.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import omtteam.omlib.tileentity.TileEntityMachine;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by Keridos on 06/02/17.
 * This Class
 */
public class TileEntityBaseController extends TileEntityMachine {
    public TileEntityBaseController() {
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[0];
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return false;
    }
}
