package omtteam.omtcontrol.reference;

public class Reference {
    public static final String MOD_ID = "omtcontrol";
    public static final String NAME = "Open Modular Turrets Control";
    public static final String VERSION = "@VERSION@";
    private static final String OMLIB_MIN_VERSION = "3.0.0-91";
    private static final String OMT_MIN_VERSION = "3.0.0-149";
    public static final String DEPENDENCIES = "after:ThermalFoundation;after:ThermalExpansion;after:OpenComputers;" +
            "after:ComputerCraft;after:Mekanism;after:EnderIO;after:Thaumcraft;" +
            "required-after:omlib@" + OMLIB_MIN_VERSION + ";required-after:openmodularturrets@" + OMT_MIN_VERSION;
}