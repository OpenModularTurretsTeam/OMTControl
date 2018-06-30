package omtteam.omtcontrol.compatability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import omtteam.omtcontrol.OMTControl;
import omtteam.omtcontrol.reference.Reference;

/**
 * Created by Keridos on 23/01/2015. This Class
 */
public class ModCompatibility {
    public static boolean IGWModLoaded = false;
    public static boolean ThermalExpansionLoaded = false;
    public static boolean EnderIOLoaded = false;
    public static boolean MekanismLoaded = false;
    public static boolean ThaumcraftLoaded = false;
    public static boolean OpenComputersLoaded = false;
    public static boolean ComputerCraftLoaded = false;
    public static boolean ValkyrienWarfareLoaded = false;

    public static void checkForMods() {
        ThermalExpansionLoaded = Loader.isModLoaded("ThermalExpansion");
        if (ThermalExpansionLoaded) {
            OMTControl.getLogger().info("Hi there, dV=V0B(t1-t0)! (Found ThermalExpansion)");
        }

        EnderIOLoaded = Loader.isModLoaded("EnderIO");
        if (EnderIOLoaded) {
            OMTControl.getLogger().info("Not sure if iron ingot, or electrical steel ingot... (Found EnderIO)");
        }

        MekanismLoaded = Loader.isModLoaded("Mekanism");
        if (MekanismLoaded) {
            OMTControl.getLogger().info("Mur omsimu, plz. (Found Mekanism)");
        }

        OpenComputersLoaded = Loader.isModLoaded("OpenComputers");
        ComputerCraftLoaded = Loader.isModLoaded("ComputerCraft");
        if (OpenComputersLoaded || ComputerCraftLoaded) {
            OMTControl.getLogger().info("Enabling LUA integration. (Found OpenComputers/ComputerCraft)");
        }

        IGWModLoaded = Loader.isModLoaded("IGWMod");
    }

    private static void addVersionCheckerInfo() {
        NBTTagCompound versionchecker = new NBTTagCompound();
        versionchecker.setString("curseProjectName", "omtcontrol");
        versionchecker.setString("curseFilenameParser", "OMTControl-1.10.2-[].jar");
        versionchecker.setString("modDisplayName", "OMTControl");
        versionchecker.setString("oldVersion", Reference.VERSION);
        FMLInterModComms.sendRuntimeMessage("omtteam/omtcontrol", "VersionChecker", "addCurseCheck", versionchecker);
    }

    public static void performModCompat() {
        FMLInterModComms.sendMessage("Waila", "register",
                                     "omtteam.omtcontrol.compatability.WailaTurretBaseHandler.callbackRegister");
        addVersionCheckerInfo();
        if (ComputerCraftLoaded) {
            registerCCCompat();
        }
    }

    @SuppressWarnings("EmptyMethod")
    @Optional.Method(modid = "ComputerCraft")
    private static void registerCCCompat() {
        //ComputerCraftAPI.registerPeripheralProvider(CCPeripheralProvider.getInstance());
    }
}
