package omtteam.omtcontrol.items.blocks;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import omtteam.omtcontrol.blocks.BlockBaseAddonMain;
import omtteam.omtcontrol.init.ModBlocks;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.OMTControlNames.Blocks;
import omtteam.omtcontrol.reference.Reference;
import omtteam.openmodularturrets.items.blocks.ItemBlockBaseAddon;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static omtteam.omlib.util.GeneralUtil.safeLocalize;


/**
 * Created by Keridos on 17/05/17.
 * This Class
 */
public class ItemBlockBaseAddonMain extends ItemBlockBaseAddon {
    public ItemBlockBaseAddonMain(Block block) {
        super(block);
        setHasSubtypes(true);
        this.setRegistryName(Reference.MOD_ID, OMTControlNames.Blocks.baseAddonMain);
    }

    private final static String[] subNames = {
            OMTControlNames.Blocks.manualTarget,
            Blocks.PLAYER_DEFENSE_MODULE
    };

    @Override
    @ParametersAreNonnullByDefault
    public void clGetSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (int i = 0; i < BlockBaseAddonMain.SUBBLOCK_COUNT; i++) {
            subItems.add(new ItemStack(ModBlocks.baseAddonMain, 1, i));
        }
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack itemStack) {
        return "tile." + subNames[itemStack.getItemDamage()];
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    @SuppressWarnings("unchecked")
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        switch (stack.getMetadata()) {
            case 0:
                tooltip.add("");
                tooltip.add(TextFormatting.GOLD + safeLocalize("tooltip.base_addon_main.manual_target1"));
                tooltip.add("");
                tooltip.add(TextFormatting.WHITE + safeLocalize("tooltip.base_addon_main.manual_target2"));
                tooltip.add(TextFormatting.WHITE + safeLocalize("tooltip.base_addon_main.manual_target3") + " 4.");
                tooltip.add("");
                tooltip.add(TextFormatting.DARK_GRAY + safeLocalize("flavour.base_addon_main.manual_target"));
        }
    }
}