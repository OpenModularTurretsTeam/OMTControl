package omtteam.omtcontrol.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import omtteam.omlib.tileentity.TileEntityBase;
import omtteam.openmodularturrets.api.network.INetworkCable;
import omtteam.openmodularturrets.api.network.INetworkTile;
import omtteam.openmodularturrets.api.network.OMTNetwork;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityBasicCable extends TileEntityBase implements INetworkCable {

    private OMTNetwork network;

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
            OMTNetwork mergeNet = devices.remove(0).getConnectedNetwork();
            for (INetworkCable cable : devices) {

            }
        } else {
            if (!this.getWorld().isRemote) {
                this.network = new OMTNetwork(this.getWorld());
            }
        }
    }

    @Override
    public boolean shouldConnect(EnumFacing side) {
        TileEntity tile = this.getWorld().getTileEntity(this.getPos().offset(side));
        return tile instanceof INetworkTile || tile instanceof INetworkCable;
    }

    @Override
    public OMTNetwork getConnectedNetwork() {
        return network;
    }


    /*@Override
    public List<INetworkTile> getConnectedDevices() {
        List<INetworkTile> devices = new ArrayList<>();
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (this.getWorld().getTileEntity(this.getPos().offset(facing)) instanceof INetworkTile) {
                devices.add((INetworkTile) this.getWorld().getTileEntity(this.getPos().offset(facing)));
            }
        }
        return devices;
    }

    @Override
    public void connectDevice(INetworkTile tile) {
        this.network.addDevice(tile);
    }*/

    @Nullable
    @Override
    public OMTNetwork getNetwork() {
        return this.network;
    }

    @Override
    public void setNetwork(@Nullable OMTNetwork network) {
        this.network = network;
    }

    @Nonnull
    @Override
    public String getDeviceName() {
        return "omtc.cables_basic";
    }

    @Nonnull
    @Override
    public BlockPos getPosition() {
        return this.pos;
    }
}
