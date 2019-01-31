package omtteam.omtcontrol.tileentity;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import omtteam.omlib.tileentity.TileEntityBase;
import omtteam.omlib.util.player.EnumAccessMode;
import omtteam.omlib.util.player.PlayerUtil;
import omtteam.omlib.util.player.TrustedPlayer;
import omtteam.omtcontrol.client.gui.TurretHackingGui;
import omtteam.omtcontrol.client.gui.containers.TurretHackingContainer;
import omtteam.omtcontrol.handler.ConfigHandler;
import omtteam.openmodularturrets.tileentity.TurretBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmf444 on 11/13/2017. Code originally written for OMTControl.
 */
public class TileEntityHackingTerminal extends TileEntityBase implements ITickable, IGuiable {

    private int tickCounter, secondCounter, totalFoundTurrets;
    private boolean hasQuery = false, startHack = false;
    private List<BlockPos> hackableBlocks;
    private String owner;
    private int hackedConsoles = 0;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public void update() {
        if (!hasQuery) {
            queryChunks();
            hasQuery = !hasQuery;
        }

        if (startHack && hackableBlocks.size() > 0) {
            tickCounter++;
            //OMTControl.getLogger().error("HERE!");

            if (tickCounter % 20 == 0) {
                tickCounter = 0;
                secondCounter++;
            }

            if (!hackableBlocks.isEmpty() && owner != null && secondCounter == 30) {
                hackAndFriend(hackableBlocks.remove(0));
                /*for (BlockPos pos : hackableBlocks) {
                    hackAndFriend(pos);
                }
                hackableBlocks.clear();*/
                hackedConsoles++;
                secondCounter = 0;
                tickCounter = 0;
                if (getTotalHacksCompleted() >= 1) {
                    startHack = false;
                }
            } else if (secondCounter == 30) {
                secondCounter = 0;
            }
        }
    }

    public void startHack() {
        this.startHack = true;
    }

    public void reQueryChuncks() {

    }

    public boolean isQuerying() {
        return !hasQuery;
    }

    public boolean isHacking() {
        return startHack;
    }

    public double getSingleCompletionPercent() {
        return (double) secondCounter / 30;
    }

    public double getTotalCompletionPercentage() {
        return ((this.hackedConsoles * 30) + getSingleCompletionPercent()) / (30 * this.totalFoundTurrets);
    }

    public double getTotalHacksCompleted() {
        if (totalFoundTurrets == 0) {
            return 1;
        } else {
            return this.hackedConsoles / this.totalFoundTurrets;
        }
    }

    private void queryChunks() {
        Chunk chunk = this.getWorld().getChunkFromBlockCoords(this.getPos());
        ChunkPos chunkPosMain = chunk.getPos();
        List<BlockPos> list = new ArrayList<>();

        ChunkPos pos;
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                pos = new ChunkPos(chunkPosMain.x - 1 + x, chunkPosMain.z - 1 + z);
                chunk = this.getWorld().getChunkFromChunkCoords(pos.x, pos.z);
                for (BlockPos blockPos : chunk.getTileEntityMap().keySet()) {
                    TileEntity tile = chunk.getTileEntityMap().get(blockPos);
                    if (tile instanceof TurretBase && ((TurretBase) tile).getTrustedPlayer(owner) == null) {
                        list.add(blockPos);
                    }
                }
            }
        }
        hackableBlocks = list;
        totalFoundTurrets = hackableBlocks.size();
    }

    private void hackAndFriend(BlockPos pos) {
        TileEntity base = this.getWorld().getTileEntity(pos);
        if (base != null) {
            TurretBase turretBase = (TurretBase) base;
            TrustedPlayer trustedPlayer = new TrustedPlayer(owner);
            trustedPlayer.setUuid(PlayerUtil.getPlayerUUID(owner));
            trustedPlayer.setHacked(true);
            trustedPlayer.setAccessMode(getConfigLevel());
            turretBase.getTrustedPlayers().add(trustedPlayer);
        }
    }

    private EnumAccessMode getConfigLevel() {
        try {
            return EnumAccessMode.valueOf(ConfigHandler.maximumHackingLevel);
        } catch (IllegalArgumentException e) {
            return EnumAccessMode.OPEN_GUI;
        }
    }

    @Override
    public Container getServerContainer(InventoryPlayer invPlayer, BlockPos blockPos) {
        return new TurretHackingContainer(invPlayer, blockPos);
    }

    @Override
    public Gui getClientGui(InventoryPlayer invPlayer, BlockPos blockPos) {
        return new TurretHackingGui(invPlayer, blockPos);
    }
}
