package omtteam.omtcontrol.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import omtteam.omlib.api.gui.IHasTooltips;
import omtteam.omlib.api.permission.EnumAccessLevel;
import omtteam.omlib.api.permission.TrustedPlayer;
import omtteam.omlib.reference.OMLibNames;
import omtteam.omlib.util.player.PlayerUtil;
import omtteam.omtcontrol.client.gui.containers.BaseAddonBlockContainer;
import omtteam.omtcontrol.handler.NetworkingHandler;
import omtteam.omtcontrol.network.messages.MessageSendManualShot;
import omtteam.omtcontrol.network.messages.MessageSetAutoFire;
import omtteam.omtcontrol.network.messages.MessageSetYawPitch;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.openmodularturrets.reference.OMTNames;
import omtteam.openmodularturrets.tileentity.TurretBase;
import omtteam.openmodularturrets.tileentity.turrets.AbstractDirectedTurret;
import omtteam.openmodularturrets.tileentity.turrets.TurretHead;
import omtteam.openmodularturrets.turret.TurretHeadUtil;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static omtteam.omlib.util.GeneralUtil.*;

/**
 * Created by Keridos on 09/02/17.
 * This Class
 */
public class ManualTargetGui extends GuiContainer implements IHasTooltips {
    private final EntityPlayer player;

    private final TurretBase base;
    private GuiTextField yaw, pitch;
    private Map<EnumFacing, TurretHead> turrets = new HashMap<>();
    private EnumFacing selectedTurretFacing;

    private int mouseX;
    private int mouseY;

    public ManualTargetGui(InventoryPlayer inventoryPlayer, BlockPos pos) {
        super(new BaseAddonBlockContainer(inventoryPlayer, pos));
        this.base = TurretHeadUtil.getTurretBase(inventoryPlayer.player.getEntityWorld(), pos);
        player = inventoryPlayer.player;
        turrets = TurretHeadUtil.getBaseTurrets(inventoryPlayer.player.getEntityWorld(), base.getPos());
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        Map<EnumFacing, TurretHead> oldturrets = turrets;
        turrets = TurretHeadUtil.getBaseTurrets(player.getEntityWorld(), base.getPos());
        if (!turrets.equals(oldturrets)) {
            ArrayList<EnumFacing> validFacings = new ArrayList<>();
            turrets.forEach((k, v) -> validFacings.add(k));
            selectedTurretFacing = validFacings.size() > 0 ? validFacings.get(0) : null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        yaw = new GuiTextField(0, fontRenderer, 7, 7, 40, 18);
        pitch = new GuiTextField(1, fontRenderer, 7, 27, 40, 18);
        yaw.setMaxStringLength(5);
        pitch.setMaxStringLength(5);
        TrustedPlayer trustedPlayer = PlayerUtil.getTrustedPlayer(player, base);
        if (PlayerUtil.isPlayerOwner(player, base) || trustedPlayer != null && (trustedPlayer.getAccessLevel() == EnumAccessLevel.CHANGE_SETTINGS || trustedPlayer.getAccessLevel() == EnumAccessLevel.ADMIN)) {
            this.buttonList.add(new GuiButton(1, x + 52, y + 3, 120, 20,
                                              safeLocalize(OMTControlNames.Localizations.GUI.SET_YAW_PITCH)));
            this.buttonList.add(new GuiButton(2, x + 52, y + 23, 120, 20,
                                              safeLocalize(OMTNames.Localizations.GUI.TOGGLE) + " " + safeLocalize(OMTNames.Localizations.GUI.AUTO_FIRE)));
            this.buttonList.add(new GuiButton(3, x + 52, y + 43, 120, 20,
                                              safeLocalize(safeLocalize(OMTControlNames.Localizations.GUI.SHOOT_TURRET))));
            this.buttonList.add(new GuiButton(4, x + 52, y + 63, 120, 20,
                                              safeLocalize(OMTNames.Localizations.GUI.TOGGLE) + " " + safeLocalize(OMTControlNames.Localizations.GUI.SELECTED_TURRET)));
        }
        ArrayList<EnumFacing> validFacings = new ArrayList<>();
        turrets.forEach((k, v) -> validFacings.add(k));
        selectedTurretFacing = validFacings.size() > 0 ? validFacings.get(0) : null;
        getYawPitchFromTurret();
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.mouseX = par1;
        this.mouseY = par2;
        super.drawScreen(par1, par2, par3);
    }

    @SuppressWarnings("EmptyCatchBlock")
    @Override
    public void mouseClicked(int i, int j, int k) {
        try {
            super.mouseClicked(i, j, k);
        } catch (IOException e) {

        }
        yaw.mouseClicked(i - guiLeft, j - guiTop, k);
        pitch.mouseClicked(i - guiLeft, j - guiTop, k);
    }

    @SuppressWarnings("EmptyCatchBlock")
    @Override
    protected void keyTyped(char par1, int par2) {
        if (!yaw.isFocused() && !pitch.isFocused()) {
            try {
                super.keyTyped(par1, par2);
            } catch (IOException e) {

            }
        } else if (yaw.isFocused()) {
            yaw.textboxKeyTyped(par1, par2);
        } else if (pitch.isFocused()) {
            pitch.textboxKeyTyped(par1, par2);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (selectedTurretFacing != null) {
            if (guibutton.id == 1) {
                sendYawPitchToServer();
                return;
            }

            if (guibutton.id == 2) {
                sendForceFireToServer();
                return;
            }

            if (guibutton.id == 3) {
                sendManualFireToServer();
                return;
            }
        }
        if (guibutton.id == 4) {
            if (turrets.size() >= 1) {
                ArrayList<EnumFacing> validFacings = new ArrayList<>();
                turrets.forEach((k, v) -> validFacings.add(k));
                selectedTurretFacing = validFacings.size() > validFacings.indexOf(selectedTurretFacing) + 1
                        && selectedTurretFacing != null ? validFacings.get(validFacings.indexOf(selectedTurretFacing) + 1)
                        : validFacings.get(0);
                getYawPitchFromTurret();
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        ResourceLocation texture = (new ResourceLocation(OMTControlNames.Textures.MANUAL_TARGET_BLOCK));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        ArrayList turretInfo = new ArrayList();
        turretInfo.add("\u00A73" + safeLocalize(OMTNames.Localizations.GUI.BASE) + ":");
        turretInfo.add("\u00A76" + safeLocalize(OMLibNames.Localizations.GUI.OWNER) + ": \u00A7f" + base.getOwner().getName());
        turretInfo.add("\u00A76" + safeLocalize(OMLibNames.Localizations.GUI.ACTIVE) + ": " + (getColoredBooleanLocalizationYesNo(base.isActive())));
        turretInfo.add("\u00A76" + safeLocalize(OMLibNames.Localizations.GUI.ATTACK_MOBS) + ": " + getColoredBooleanLocalizationYesNo(base.isAttacksMobs()));
        turretInfo.add("\u00A76" + safeLocalize(OMLibNames.Localizations.GUI.ATTACK_NEUTRALS) + ": " + getColoredBooleanLocalizationYesNo(base.isAttacksNeutrals()));
        turretInfo.add("\u00A76" + safeLocalize(OMLibNames.Localizations.GUI.ATTACK_PLAYERS) + ": " + getColoredBooleanLocalizationYesNo(base.isAttacksPlayers()));
        turretInfo.add("                               ");
        if (turrets.size() != 0) {
            TurretHead turret = turrets.get(selectedTurretFacing);
            turretInfo.add("\u00A73" + safeLocalize(OMTNames.Localizations.GUI.TURRET) + ":");
            turretInfo.add("\u00A76" + safeLocalize(OMTNames.Localizations.GUI.TYPE) + ": \u00A72" + safeLocalizeBlockName(turret.getBlockType().getRegistryName().toString()).replaceAll(" Turret", ""));
            if (turret instanceof AbstractDirectedTurret) {
                turretInfo.add("\u00A76" + safeLocalize(OMTNames.Localizations.GUI.YAW) + ": \u00A77" + ((AbstractDirectedTurret) turret).getYaw());
                turretInfo.add("\u00A76" + safeLocalize(OMTNames.Localizations.GUI.PITCH) + ": \u00A77" + ((AbstractDirectedTurret) turret).getPitch());
            }
            turretInfo.add("\u00A76" + safeLocalize(OMTNames.Localizations.GUI.AUTO_FIRE) + ": " + getColoredBooleanLocalizationYesNo(turret.getAutoFire()));

            turretInfo.add("\u00A76" + safeLocalize(OMLibNames.Localizations.GUI.FACING) + ": \u00A72" + selectedTurretFacing.toString());
        } else {
            turretInfo.add("\u00A74" + safeLocalize(OMTNames.Localizations.GUI.NO_TURRET));
        }

        this.drawHoveringText(turretInfo, -150, 17, fontRenderer);
        yaw.drawTextBox();
        pitch.drawTextBox();
        drawTooltips();
    }

    @Override
    public void drawTooltips() {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        int tooltipToDraw = buttonList.stream().filter(GuiButton::isMouseOver).mapToInt(s -> s.id).sum();
        if (tooltipToDraw == 0) {
            tooltipToDraw = isMouseOverTextField(yaw, mouseX - this.guiLeft, mouseY - this.guiTop) ? 5 : isMouseOverTextField(pitch, mouseX - this.guiLeft, mouseY - this.guiTop) ? 6 : 0;
        }
        ArrayList<String> tooltip = new ArrayList<>();
        switch (tooltipToDraw) {
            case 1:
                tooltip.add(safeLocalize(OMTControlNames.Localizations.Tooltip.SET_YAW_PITCH));
                break;
            case 2:
                tooltip.add(safeLocalize(OMTControlNames.Localizations.Tooltip.TOGGLE_AUTOFIRE));
                break;
            case 3:
                tooltip.add(safeLocalize(OMTControlNames.Localizations.Tooltip.SHOOT_TURRET));
                break;
            case 4:
                tooltip.add(safeLocalize(OMTControlNames.Localizations.Tooltip.SELECTED_TURRET));
                break;
            case 5:
                tooltip.add(safeLocalize(OMTControlNames.Localizations.Tooltip.YAW));
                break;
            case 6:
                tooltip.add(safeLocalize(OMTControlNames.Localizations.Tooltip.PITCH));
                break;
        }
        if (!tooltip.isEmpty())
            this.drawHoveringText(tooltip, mouseX - k, mouseY - l, Minecraft.getMinecraft().fontRenderer);
    }

    private void sendYawPitchToServer() {
        MessageSetYawPitch message = new MessageSetYawPitch(base.getPos().offset(selectedTurretFacing).getX(), base.getPos().offset(selectedTurretFacing).getY(), base.getPos().offset(selectedTurretFacing).getZ(), getFloatFromString(yaw.getText()), getFloatFromString(pitch.getText()));
        NetworkingHandler.INSTANCE.sendToServer(message);
    }

    private void sendForceFireToServer() {
        MessageSetAutoFire message = new MessageSetAutoFire(base.getPos().offset(selectedTurretFacing).getX(), base.getPos().offset(selectedTurretFacing).getY(), base.getPos().offset(selectedTurretFacing).getZ(), !turrets.get(selectedTurretFacing).getAutoFire());
        NetworkingHandler.INSTANCE.sendToServer(message);
    }

    private void sendManualFireToServer() {
        MessageSendManualShot message = new MessageSendManualShot(base.getPos().offset(selectedTurretFacing).getX(), base.getPos().offset(selectedTurretFacing).getY(), base.getPos().offset(selectedTurretFacing).getZ());
        NetworkingHandler.INSTANCE.sendToServer(message);
    }

    private void getYawPitchFromTurret() {
        if (selectedTurretFacing != null && turrets.get(selectedTurretFacing) instanceof AbstractDirectedTurret) {
            yaw.setText(Float.toString(((AbstractDirectedTurret) turrets.get(selectedTurretFacing)).getYaw()));
            pitch.setText(Float.toString(((AbstractDirectedTurret) turrets.get(selectedTurretFacing)).getPitch()));
        }
    }
}
