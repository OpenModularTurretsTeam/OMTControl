package omtteam.omtcontrol.client.gui;

import com.enderio.core.common.util.DyeColor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import omtteam.omtcontrol.OMTControl;
import omtteam.omtcontrol.client.gui.containers.TurretHackingContainer;
import omtteam.omtcontrol.client.gui.widget.TurretHackingButtons;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.Reference;
import omtteam.omtcontrol.tileentity.TileEntityHackingTerminal;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;

public class TurretHackingGui extends GuiContainer{

    private TileEntity te;
    private ArrayList<GuiButton> list = new ArrayList<>();

    public TurretHackingGui(InventoryPlayer inventoryPlayer, BlockPos pos) {
        super(new TurretHackingContainer(inventoryPlayer, pos));
        te = inventoryPlayer.player.getEntityWorld().getTileEntity(pos);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        GuiButton search = new TurretHackingButtons(0, this.guiLeft + 43, this.guiTop + 35, "> Search for Turrets");
        GuiButton hack = new TurretHackingButtons(1, this.guiLeft + 43, this.guiTop + 47,"> Start Hacking");

        list.add(search);
        list.add(hack);

        buttonList.addAll(list);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        ResourceLocation texture = (OMTControlNames.Textures.TURRET_HACKER);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        TileEntityHackingTerminal hackingTerminal = (TileEntityHackingTerminal) te;
        this.fontRenderer.setUnicodeFlag(true);
        this.fontRenderer.drawString("OMTC 2018 Hacking, Inc.", x + 43, y + 10, DyeColor.LIME.getColor());


        String formatted = String.format("Hacking...%.2f%%", hackingTerminal.getTotalCompletionPercentage() * 100);
        OMTControl.getLogger().error(formatted);
        if(!hackingTerminal.isHacking() && !hackingTerminal.isQuerying()) {
            this.fontRenderer.drawString("BIOS Ver: " + Reference.VERSION, x + 43, y + 17, DyeColor.LIME.getColor());
            this.fontRenderer.drawString("CPU: OMTC IoI @ 40 MHz", x + 43, y + 25, DyeColor.LIME.getColor());
            if(buttonList.isEmpty()) {
                buttonList.addAll(list);
            }
        } else if(hackingTerminal.isQuerying()) {

        } else {
            //We are hacking here!

            //String formatted = String.format("Hacking...%.2f%%", hackingTerminal.getTotalCompletionPercentage() * 100);
            this.fontRenderer.drawString(formatted, x + 43, y + 17, DyeColor.LIME.getColor());
        }

        this.fontRenderer.setUnicodeFlag(false);
    }

    protected void actionPerformed(GuiButton button)
    {
        TileEntityHackingTerminal hackingTerminal = (TileEntityHackingTerminal) te;
        switch (button.id){
            case 0:
                // Tell Turret to search for clients.
                hackingTerminal.reQueryChuncks();
                break;
            case 1:
                //Tell turret to hack.
                hackingTerminal.startHack();
                buttonList.remove(0);
                buttonList.remove(0);
                break;
        }
    }
}
