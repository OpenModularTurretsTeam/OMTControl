package omtteam.omtcontrol.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import omtteam.omlib.tileentity.TileEntityBase;
import omtteam.omlib.util.EnumAccessMode;
import omtteam.omlib.util.PlayerUtil;
import omtteam.omlib.util.TrustedPlayer;
import omtteam.omtcontrol.handler.ConfigHandler;
import omtteam.openmodularturrets.tileentity.TurretBase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dmf444 on 11/13/2017. Code originally written for OMTControl.
 */
public class TileEntityHackingTerminal extends TileEntityBase implements ITickable{

    private int tickCounter, secondCounter;
    private boolean hasQuery = false, startHack = false;
    private List<BlockPos> hackableBlocks;
    private String owner;


    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public void update() {
        if(!hasQuery) {
            queryChunks();
            hasQuery = !hasQuery;
        }

        if(startHack) {
            tickCounter++;

            if (tickCounter % 20 == 0) {
                tickCounter = 0;
                secondCounter++;
            }

            if (!hackableBlocks.isEmpty() && owner != null && secondCounter == 30) {
                for (BlockPos pos : hackableBlocks) {
                    hackAndFriend(pos);
                }
                hackableBlocks.clear();
                secondCounter = 0;
                tickCounter = 0;
                startHack = false;
            } else if (secondCounter == 30) {
                secondCounter = 0;
            }
        }
    }

    public void startHack() {
        this.startHack = true;
    }

    public float getCompletionPercent() {
        return secondCounter / 30;
    }

    private void queryChunks(){
        Chunk chunk = this.getWorld().getChunkFromBlockCoords(this.getPos());
        ChunkPos chunkPosMain = chunk.getPos();
        List<BlockPos> list = new ArrayList<>();

        ChunkPos pos;
        for(int x=0; x < 3; x++){
            for (int z=0; z < 3; z++){
                pos = new ChunkPos(chunkPosMain.x-1+x, chunkPosMain.z-1+z);
                chunk = this.getWorld().getChunkFromChunkCoords(pos.x, pos.z);
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
        trustedPlayer.setUuid(PlayerUtil.getPlayerUUID(owner));
        trustedPlayer.setHacked(true);
        trustedPlayer.setAccessMode(getConfigLevel());
        base.getTrustedPlayers().add(trustedPlayer);
    }

    private EnumAccessMode getConfigLevel(){
        try {
            return EnumAccessMode.valueOf(ConfigHandler.maximumHackingLevel);
        }catch (IllegalArgumentException e) {
            return EnumAccessMode.OPEN_GUI;
        }
    }


}
