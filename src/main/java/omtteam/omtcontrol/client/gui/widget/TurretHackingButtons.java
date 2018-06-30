package omtteam.omtcontrol.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumDyeColor;

public class TurretHackingButtons extends GuiButton {

    public TurretHackingButtons(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, 80, 10, buttonText);
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = EnumDyeColor.LIME.getColorValue();

            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = EnumDyeColor.ORANGE.getColorValue();
            }
            fontrenderer.setUnicodeFlag(true);
            this.drawString(fontrenderer, this.displayString, this.x, this.y + (this.height - 8) / 2, j);
            //this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
            fontrenderer.setUnicodeFlag(false);
        }
    }
}
