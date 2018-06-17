package omtteam.omtcontrol.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import omtteam.omlib.tileentity.TileEntityBase;
import omtteam.omlib.util.PlayerUtil;
import omtteam.omlib.util.TrustedPlayer;
import omtteam.openmodularturrets.tileentity.TurretBase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dmf444 on 11/13/2017. Code originally written for OMTControl.
 */
public class TileEntityHackingTerminal extends TileEntityBase implements ITickable{

    private int tickCounter;
    private boolean hasQuery = false;
    private List<BlockPos> hackableBlocks;
    private String owner;
    private boolean canChangeTargeting;
    private boolean overwriteAdmin;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public void update() {
        if(!hasQuery) {
            queryChunks();
            hasQuery = !hasQuery;
        }

        if(!hackableBlocks.isEmpty() && owner != null){
            for (BlockPos pos: hackableBlocks) {
                hackAndFriend(pos);
            }
            hackableBlocks.clear();
        }

    }

    private void queryChunks(){
        Chunk chunk = this.getWorld().getChunkFromBlockCoords(this.getPos());
        ChunkPos chunkPosMain = chunk.getChunkCoordIntPair();
        List<BlockPos> list = new ArrayList<>();

        ChunkPos pos;
        for(int x=0; x < 3; x++){
            for (int z=0; z < 3; z++){
                pos = new ChunkPos(chunkPosMain.chunkXPos-1+x, chunkPosMain.chunkZPos-1+z);
                chunk = this.getWorld().getChunkFromChunkCoords(pos.chunkXPos, pos.chunkZPos);
                for (BlockPos blockPos: chunk.getTileEntityMap().keySet()) {
                    TileEntity tile = chunk.getTileEntityMap().get(blockPos);
                    if(tile instanceof TurretBase && ((TurretBase) tile).getTrustedPlayer(owner) == null) {
                        list.add(blockPos);
                    }
                }
            }
        }
        hackableBlocks = list;
    }

    private void hackAndFriend(BlockPos pos) {
        TurretBase base = (TurretBase) this.getWorld().getTileEntity(pos);
        TrustedPlayer trustedPlayer = new TrustedPlayer(owner);
        trustedPlayer.uuid = PlayerUtil.getPlayerUUID(owner);
        trustedPlayer.canOpenGUI = true;
        trustedPlayer.setCanChangeTargeting(canChangeTargeting);
        trustedPlayer.setAdmin(overwriteAdmin);
        base.getTrustedPlayers().add(trustedPlayer);
    }

}
