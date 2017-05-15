package omtteam.omtcontrol.items;

import omtteam.omlib.compatability.minecraft.CompatItem;
import omtteam.omtcontrol.OMTControl;
import omtteam.omtcontrol.reference.OMTControlNames;
import omtteam.omtcontrol.reference.Reference;

/**
 * Created by Rokiyo on 15/05/2017.
 */
public class ItemLaserPointer extends CompatItem {
    public ItemLaserPointer() {
        super();

        this.setHasSubtypes(false);
        this.setCreativeTab(OMTControl.creativeTab);
        this.setRegistryName(Reference.MOD_ID, OMTControlNames.Items.laserPointer);
        this.setUnlocalizedName(OMTControlNames.Items.laserPointer);
    }
}
