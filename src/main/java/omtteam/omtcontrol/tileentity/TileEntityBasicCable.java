package omtteam.omtcontrol.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import omtteam.omlib.tileentity.TileEntityBase;
import omtteam.omlib.api.network.INetworkCable;
import omtteam.omlib.api.network.INetworkTile;
import omtteam.omlib.api.network.OMLibNetwork;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityBasicCable extends TileEntityBase implements INetworkCable {

    private OMLibNetwork network;

    private List<INetworkCable> getConnectedCables() {
        ArrayList<INetworkCable> networkTiles = new ArrayList<>();
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos pos = this.getPos().offset(facing);
            TileEntity tile = this.getWorld().getTileEntity(pos);
            if (tile instanceof INetworkCable) {
                networkTiles.add((INetworkCable) networkTiles);
            }
        }
        return networkTiles;
    }

    public void createNetwork() {
        List<INetworkCable> devices = this.getConnectedCables();
        if (!devices.isEmpty()) {
            OMLibNetwork mergeNet = devices.remove(0).getConnectedNetwork();
            for (INetworkCable cable : devices) {

            }
        } else {
            if (!this.getWorld().isRemote) {
                this.network = new OMLibNetwork(this.getWorld());
            }
        }
    }

    @Override
    public boolean shouldConnect(EnumFacing side) {
        TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(side));
        return tile instanceof INetworkTile || tile instanceof INetworkCable;
    }

    @Override
    public OMLibNetwork getConnectedNetwork() {
        return network;
    }

    @Nullable
    @Override
    public OMLibNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(@Nullable OMLibNetwork network) {
        this.network = network;
    }

    @Nonnull
    @Override
    public String getDeviceName() {
        return "cable";
    }

    @Nonnull
    @Override
    public BlockPos getPosition() {
        return this.getPos();
    }
}
